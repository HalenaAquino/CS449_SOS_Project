package sprint_4.production;

import java.util.Map;

// DEPENDENCY INVERSION WILL BE CONSIDERED FOR EXTRA CREDIT

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
	protected int redScore  = 0;
	protected GameState currentGameState;
	
	// --------------------------------   GETTERS  -------------------------------------
	public int getBlueScore() { return blueScore; }
	public int getRedScore() { return redScore; }
	public char getTurn() { return turn; }
	public int getSize() { return size; }
	public String getGamemode() { return gameMode; }
	public GameState getGameState() { return currentGameState; }
	
	public char[][] getPieceTypeArray(){
		char[][] copy = new char[size][size];
	    for (int r = 0; r < size; r++)
	        for (int c = 0; c < size; c++)
	            copy[r][c] = pieceType[r][c];
	    return copy;
	}
		
	// returns the current piece in the cell (S/O)
	public char getPieceType(int row, int column) {
		if(row >= 0 && row < size && column >= 0 && column < size) 
			return pieceType[row][column];
		else
			return ' ';
	}

	// returns the player that's currently occupying a cell (1 for blue, 2 for red, 0 for empty) or returns null if the cell doesn't exist
	public Cell getCell(int row, int column) {
		if (game == null) return null;  // exits the function if there isn't a current game
		if (row >= 0 && row < size && column >= 0 && column < size)
			return game[row][column];
		else
			return null;
	}
	
	public void setCell(int row, int column, Cell value) {
		game[row][column] = value;
	}
	
	public void setPieceType(int row, int column, char value) {
		pieceType[row][column] = value;
	}
	
	public void setTurn(char t) {
		turn = t;
	}
	
	// ------------------------- SOS DETECTION HELPER FUNCTIONS -----------------------------
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
	protected abstract void updateGameState(char turn, int row, int column);
	
	//public void makeAutoMove(Map<Character, Character> playerPieces) {}
	
	// creates the board
	public SOSGame(int s) {
		if(s < 3 || s > 9)
			turn = ' ';
		else {
			game = new Cell[s][s];
			pieceType = new char[s][s];
			size = s;
			initGame();
		}
	}
	
	// initializes the game, resets all of the variables and board
	private void initGame() {
		for (int row = 0; row < size; ++row) {
			for (int col = 0; col < size; ++col) {
				game[row][col] = Cell.EMPTY;
				pieceType[row][col] = ' ';
			}
		}
		currentGameState = GameState.PLAYING;
		turn = 'B';
		blueScore = 0;
		redScore = 0;
	}
	
	// re-initializes game, will reset a leaderboard done in later sprints
	public void resetGame() {
		initGame();
	}

	// updates the current gamemode
	public void setGamemode(String mode) {
		gameMode = mode;
	}
	
	// determines if the board has any empty cells
	protected boolean boardFull() {
		// returns false is there are any unoccupied cells
	    for (int r = 0; r < size; r++)
	        for (int c = 0; c < size; c++)
	            if (game[r][c] == Cell.EMPTY) return false;
	    
	    return true;
	}
}