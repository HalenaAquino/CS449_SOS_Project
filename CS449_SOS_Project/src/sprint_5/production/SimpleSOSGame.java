package sprint_5.production;


// subclass for simple game rules

public class SimpleSOSGame extends SOSGame{
	
	// constructor
	public SimpleSOSGame(int s) {
		super(s);
	}	
	
	// determines if an SOS was made for the given player
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
	
	// simple game has its own unique update function
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

	// can only be a draw (in a simple game) if the board is full
	public boolean isDraw() {
		return boardFull();
	}
}