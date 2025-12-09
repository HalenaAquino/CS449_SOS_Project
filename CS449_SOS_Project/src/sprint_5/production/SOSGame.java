package sprint_5.production;

// parent class, contains all of the general rules between simple and general game
public abstract class SOSGame { 

	// --------------------------------   VARIABLE DECLARATIONS  -------------------------------------
	
	// public (used in GUI)
	public enum Cell { EMPTY, BLUE, RED }
	public enum GameState { PLAYING, DRAW, BLUE_WON, RED_WON }
	
	// private (only used in this class)
	private char turn = 'B';
	private String gameMode = ""; 
	private int size;
	private char[][] pieceType;
	private Cell[][] game;
	
	// protected (used in children classes)
	protected int blueScore = 0;
	protected int redScore = 0;
	protected GameState currentGameState;
	protected boolean recorded = false;
	
	private static final int MIN_SIZE = 3;
	private static final int MAX_SIZE = 9;
	
	// --------------------------------   GETTERS  -------------------------------------
	protected boolean getRecorded() { return recorded; }
	protected int getSize() { return size; }
	public int getBlueScore() { return blueScore; }
	public int getRedScore() { return redScore; }
	public char getTurn() { return turn; }
	public String getGamemode() { return gameMode; }
	public GameState getGameState() { return currentGameState; }
	
	/* returns a copy of the piece array
	 * 
	 * Preconditions: 
	 * 		- piece type array isn't empty
	 * 		- there is an ongoing game
	 * 
	 * Postconditions: 
	 * 		- returns a 1:1 copy of the piece type array
	 */
	public char[][] getPieceTypeArray(){
		char[][] copy = new char[size][size];
	    for (int r = 0; r < size; r++)
	        for (int c = 0; c < size; c++)
	            copy[r][c] = pieceType[r][c];
	    return copy;
	}
		
	/* returns the current piece in the cell (S/O)
	 * 
	 * Preconditions: 
	 * 		- 2 integer parameters are passed
	 * 		- parameters must fall within the size of the board
	 * 
	 * Postconditions: 
	 * 		- the piece on the specified cell will be returned
	 */
	public char getPieceType(int row, int column) {
		if (row >= 0 && row < size && column >= 0 && column < size) return pieceType[row][column];
		else return ' ';
	}

	/* returns the player that's currently occupying a cell (1 for blue, 2 for red, 0 for empty) or returns null if the cell doesn't exist
	 * 
	 * Preconditions: 
	 * 		- 2 integer parameters are passed
	 * 		- parameters must fall within the size of the board
	 * 
	 * Postconditions:
	 * 		- the cell type (blue, red, empty) of the specified cell will be returned
	*/
	public Cell getCell(int row, int column) {
		if (game == null) return null;  // exits the function if there isn't a current game
		if (row >= 0 && row < size && column >= 0 && column < size) return game[row][column];
		else return null;
	}
	
	/* returns the current number of empty cells
	 * 
	 * Precondition: 
	 * 		- an ongoing game
	 * 
	 * Postcondition:
	 * 		- the number of non-occupied cells on the board will be returned
	 */
	public int getNumberOfEmptyCells() {
		int numberOfEmptyCells = 0;
		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++) 
				if (game[row][col] == Cell.EMPTY) numberOfEmptyCells++;
		return numberOfEmptyCells;
	}
	
	// --------------------------------   SETTERS  -------------------------------------
	
	/* sets the value of the specified cell
	 * 
	 * Precondition: 
	 * 		- 2 integer and 1 cell type parameter are passed
	 * 		- the cell exists within the current game
	 * 		- the row and column exist within the current game board
	 * 
	 * Postcondition:
	 * 		- the cell at the specified location is set to the passed cell value
	 */
	public void setCell(int row, int column, Cell value) { game[row][column] = value; }
	
	/* sets whether the current game is recorded
	 * 
	 * Precondition: 
	 * 		- a boolean parameter is passed
	 * 
	 * Postcondition:
	 * 		- the value of recorded is set to the given parameter
	 */
	void setRecorded(boolean r) { recorded = r; }
	
	/* sets the piece at a given place
	 * 
	 * Precondition: 
	 * 		- 2 integer and 1 character parameter are passed
	 * 		- the row and column exist within the current board
	 * 		- the character passed is a valid piece (S or O)
	 * 
	 * Postcondition:
	 * 		- the specific cell's piece type is set to the passed character
	 */
	public void setPieceType(int row, int column, char value) { pieceType[row][column] = value; }
	
	/* changes the turn
	 * 
	 * Precondition: 
	 * 		- 1 character parameter is passed
	 * 		- passed character is a valid player (B or R)
	 * 
	 * Postcondition:
	 * 		- the current turn will be set to the passed character
	 */
	public void setTurn(char t) { turn = t; }
	
	// ------------------------- SOS DETECTION HELPER FUNCTIONS -----------------------------
	/* determines if an O piece at the specified location will complete an SOS
	 * 
	 * Precondition: 
	 * 		- 4 integers are passed as parameters
	 * 		- all parameters are valid rows/columns on the board
	 * 
	 * Postcondition:
	 * 		- will return a boolean on whether the O piece will complete an SOS
	 */
	protected boolean checkSOSWithO(int row, int col, int dRow, int dCol) {
		int prevRow = row - dRow;
		int prevCol = col - dCol;
		int nextRow = row + dRow;
		int nextCol = col + dCol;
		
		// ensures that the cells are within board bounds
		if (prevRow < 0 || prevRow >= size || prevCol < 0 || prevCol >= size)	return false;
		if (nextRow < 0 || nextRow >= size || nextCol < 0 || nextCol >= size)	return false;
		
		return pieceType[prevRow][prevCol] == 'S' && pieceType[nextRow][nextCol] == 'S';
	}
	
	/* determines if an S piece at the specified location will complete an SOS
	 * 
	 * Precondition: 
	 * 		- 5 integers are passed as parameters
	 * 		- row/col parameters are valid rows/columns on the board
	 * 		- distance > 0
	 * 
	 * Postcondition:
	 * 		- will return a boolean on whether the S piece will complete an SOS
	 */
	protected boolean checkSOSWithS (int row, int col, int dRow, int dCol, int distance) {
		int oRow = row + dRow * distance;
		int oCol = col + dCol * distance;
		int sRow = row + dRow * distance * 2;
		int sCol = col + dCol * distance * 2;
		
		// ensures the cell is within bounds
		if (oRow < 0 || oRow >= size || oCol < 0 || oCol >= size) return false;
		if (sRow < 0 || sRow >= size || sCol < 0 || sCol >= size) return false;
		
		return pieceType[oRow][oCol] == 'O' && pieceType[sRow][sCol] == 'S';
	}
	
	// -------------------------------   GAME LOGIC ------------------------------------------	
	
	// abstract classes (defined in subclasses)
	protected abstract boolean isDraw();
	public abstract void updateGameState(char turn, int row, int column);
		
	/* creates the board
	 * 
	 * Precondition: 
	 * 		- an integer is passed
	 * 
	 * Postcondition:
	 * 		- a game will be created if the size is valid, otherwise, sets the turn to empty
	 */
	public SOSGame(int s) {
		if (s < MIN_SIZE || s > MAX_SIZE) turn = ' ';
		else {
			game = new Cell[s][s];
			pieceType = new char[s][s];
			size = s;
			initGame();
		}
	}
	
	/* re-initializes game, will reset a leaderboard done in later sprints
	 * 
	 * Precondition: 
	 * 		- there is an ongoing game
	 * 
	 * Postcondition:
	 * 		- the game will be reset
	 */
	public void resetGame() {
		initGame();
	}

	/* updates the current gamemode
	 * 
	 * Precondition: 
	 * 		- a string parameter is passed
	 * 		- the string is either "Simple" or "General" exactly
	 * 
	 * Postcondition:
	 * 		- the game mode is set to the specified mode
	 */
	public void setGamemode(String mode) {
		gameMode = mode;
	}
	
	/* determines if the board has any empty cells
	 * 
	 * Precondition: 
	 * 		- there is an ongoing game
	 * 
	 * Postcondition:
	 * 		- returns false if any cell is unoccupied, true otherwise
	 */
	protected boolean boardFull() {
		// returns false is there are any unoccupied cells
		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++)
				if (game[row][col] == Cell.EMPTY) return false;
		return true;
	}
	
	/* initializes the game, resets all of the variables and board
	 * 
	 * Precondition: 
	 * 		- none
	 * 
	 * Postcondition:
	 * 		- every object related to the game will be reset to its default state
	 */
	private void initGame() {
		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++) {
				game[row][col] = Cell.EMPTY;
				pieceType[row][col] = ' ';
			}
				
		currentGameState = GameState.PLAYING;
		turn = 'B';
		blueScore = 0;
		redScore = 0;
	}
}