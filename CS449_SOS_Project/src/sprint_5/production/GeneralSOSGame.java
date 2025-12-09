package sprint_5.production;

// subclass for general game rules

public class GeneralSOSGame extends SOSGame{
	
	/* constructor
	 * 
	 * Precondition: 
	 * 		- an integer is passed
	 * 
	 * Postcondition:
	 * 		- a general SOS game is created
	 */
	public GeneralSOSGame(int s) {
		super(s);
	}
	
	/* counts all of the SOS's completed in the current turn
	 * 
	 * Precondition: 
	 * 		- a character and 2 integer parameters is passed
	 * 		- the passed character is either B or R
	 * 		- the row and col are < board size
	 * 		- there is an ongoing game
	 * 
	 * Postcondition:
	 * 		- returns the number of completed SOS
	 */
	public void countSOS(char player, int row, int col) {
		// Count SOS patterns that include the last placed piece at (row, col)
	    char[][] pieces = getPieceTypeArray();
	    int points = 0;
	    char placed = pieces[row][col];

	    // finds and counts SOS's when an O was placed
	    if (placed == 'O') {
	    	if (checkSOSWithO(row, col, 0, 1)) points++;   // Horizontal
            if (checkSOSWithO(row, col, 1, 0)) points++;   // Vertical
            if (checkSOSWithO(row, col, 1, 1)) points++;   // Diagonal \
            if (checkSOSWithO(row, col, 1, -1)) points++;  // Other diagonal /
	    }

	    // finds and counts SOS's when an S was placed
	    if (placed == 'S') {
	    	if (checkSOSWithS(row, col, 0, 1, -1)) points++;
            if (checkSOSWithS(row, col, 0, 1, 1)) points++;
            if (checkSOSWithS(row, col, 1, 0, -1)) points++;
            if (checkSOSWithS(row, col, 1, 0, 1)) points++;
            if (checkSOSWithS(row, col, 1, 1, -1)) points++;
            if (checkSOSWithS(row, col, 1, 1, 1)) points++;
            if (checkSOSWithS(row, col, 1, -1, -1)) points++;
            if (checkSOSWithS(row, col, 1, -1, 1)) points++;
	    }

	    // updates the appropriate player score
	    if (points > 0) {
	        if (player == 'B') blueScore += points;
	        else               redScore  += points;
	    }
	}
	
	/* general game needs a specialized update function for determining when to continue and who won
	  
	 * Precondition: 
	 * 		- a character and 2 integer parameters is passed
	 * 		- the passed character is either B or R
	 * 		- the row and col are < board size
	 * 		- there is an ongoing game
	 * 
	 * Postcondition:
	 * 		- updates the game state to win or draw if applicable
	 */
	public void updateGameState(char turn, int row, int col) {
		// stores the old scores
		int oldBlue = blueScore;
	    int oldRed = redScore;
	    
	    // Count points first
	    countSOS(turn, row, col);
	    
	    boolean madeSOS = (blueScore > oldBlue || redScore > oldRed);

	    // If board isn't full, continues playing
	    if (!boardFull()) {
	        currentGameState = GameState.PLAYING;
	        
	        // Only switch turn if no SOS was made
	        if (!madeSOS) {
	        	char newTurn = (turn == 'B') ? 'R' : 'B';
	        	setTurn(newTurn);
	        }
	        return;
	    }

	    // If board is full, determines the winner
	    if (blueScore > redScore) {
	        currentGameState = GameState.BLUE_WON;
	    } else if (redScore > blueScore) {
	        currentGameState = GameState.RED_WON;
	    } else {
	        currentGameState = GameState.DRAW;
	    }
	}
	
	/* a general can only be a draw if the board is full and the player points are equal
	 * 
	 * Precondition: 
	 * 		- there is an ongoing game
	 * 
	 * Postcondition:
	 * 		- returns true if the board is full and the scores are equal
	 */
	public boolean isDraw() {
		return boardFull() && (blueScore == redScore);
	}
}