package sprint_5.production;


// subclass for simple game rules

public class SimpleSOSGame extends SOSGame{
	
	/* constructor
	 * 
	 * Precondition: 
	 * 		- an integer is passed
	 * 
	 * Postcondition:
	 * 		- a simple SOS game is created
	 */
	public SimpleSOSGame(int s) {
		super(s);
	}	
	
	/* determines if an SOS was made for the given player
	 * 
	 * Precondition: 
	 * 		- a character and 2 integer parameters are passed
	 * 		- player is either B or R
	 * 		- row and column are both < board size
	 * 		- there is an ongoing game
	 * 
	 * Postcondition:
	 * 		- returns true if a the specified piece at the specified location would complete an SOS, false otherwise
	 */
	public boolean hasWon(char player, int row, int column) {
		char[][] pieces = getPieceTypeArray();
	    char placed = pieces[row][column];

	    // Case 1: when O is placed in the center
	    if (placed == 'O') {
	    	if (checkSOSWithO(row, column, 0, 1)) return true;   // Horizontal
            if (checkSOSWithO(row, column, 1, 0)) return true;   // Vertical
            if (checkSOSWithO(row, column, 1, 1)) return true;   // Diagonal \
            if (checkSOSWithO(row, column, 1, -1)) return true;  // Other diagonal /
	    }

	    // Case 2: when S is placed completing SOS on the right (S O S)
	    if (placed == 'S') {
	    	if (checkSOSWithS(row, column, 0, 1, -1)) return true;   // Horizontal to the left
            if (checkSOSWithS(row, column, 0, 1, 1)) return true;    // Horizontal to the right
            if (checkSOSWithS(row, column, 1, 0, -1)) return true;   // Vertical upwards
            if (checkSOSWithS(row, column, 1, 0, 1)) return true;    // Vertical downwards
            if (checkSOSWithS(row, column, 1, 1, -1)) return true;   // Diagonal upwards and left
            if (checkSOSWithS(row, column, 1, 1, 1)) return true;    // Diagonal downwards and right
            if (checkSOSWithS(row, column, 1, -1, -1)) return true;  // Diagonal upwards and right
            if (checkSOSWithS(row, column, 1, -1, 1)) return true;   // Diagonal downwards and left
	    }
		
		return false;		// otherwise false
	}
	
	/* simple game has its own unique update function
	 * 
	 * Precondition: 
	 * 		- a character and 2 integer parameters are passed
	 * 		- player is either B or R
	 * 		- row and column are both < board size
	 * 		- there is an ongoing game
	 * 
	 * Postcondition:
	 * 		- sets the game state to the appropriate WON value if either player has completed an SOS
	 * 		- sets the game state to DRAW if the game is a draw
	 * 		- changes the turn otherwise
	 */
	public void updateGameState(char turn, int row, int column) {
		if (hasWon(turn, row, column)) // check for win{
			currentGameState = (turn == 'B') ? GameState.BLUE_WON : GameState.RED_WON;
		
		else if (isDraw())
			currentGameState = GameState.DRAW;
		
		else {
			char newTurn = (turn == 'B') ? 'R' : 'B';
        	setTurn(newTurn);
		}
	}

	/* can only be a draw (in a simple game) if the board is full
	 * 
	 * Precondition: 
	 * 		- there is an ongoing game
	 * 
	 * Postcondition:
	 * 		- returns true if the board is full, false otherwise
	 */
	public boolean isDraw() {
		return boardFull();
	}
}