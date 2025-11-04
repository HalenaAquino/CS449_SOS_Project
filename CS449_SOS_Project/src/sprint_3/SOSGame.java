package sprint_3;

import java.util.Map;

// DEPENDENCY INVERSION WILL BE CONSIDERED FOR EXTRA CREDIT

// subclass for simple game rules
class SimpleSOSGame extends SOSGame{
	
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
		if (hasWon(turn, row, column)) // check for win
			currentGameState = (turn == 'B') ? GameState.BLUE_WON : GameState.RED_WON;
		else if (isDraw())
			currentGameState = GameState.DRAW;
	}

	// can only be a draw (in a simple game) if the board is full
	public boolean isDraw() {
		return boardFull();
	}
	
}

// subclass for general game rules
class GeneralSOSGame extends SOSGame{
	// constructor
	public GeneralSOSGame(int s) {
		super(s);
	}
	
	// counts all of the SOS's completed in the current turn
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
	
	// general game needs a specialized update function for determining when to continue and who won
	@Override
	protected void updateGameState(char turn, int row, int column) {
	    // Count points first
	    countSOS(turn, row, column);

	    // If board isn't full, continues playing
	    if (!boardFull()) {
	        currentGameState = GameState.PLAYING;
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
	
	// a general can only be a draw if the board is full and the player points are equal
	public boolean isDraw() {
		return boardFull() && (blueScore == redScore);
	}
}

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
	private Cell[][] game;
	
	// protected (used in children classes)
	protected int blueScore = 0;
	protected int redScore  = 0;
	protected GameState currentGameState;
	protected char[][] pieceType;

	
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
	
	// places the current player's current piece on the given cell and updates the turn
	public void makeMove(int row, int column, Map<Character, Character> playerPieces) {
		if (game == null) return;  // exits the function if there's not an ongoing game
		if (row >= 0 && row < size && column >= 0 && column < size
				&& game[row][column] == Cell.EMPTY) {
			game[row][column] = (turn == 'B')? Cell.BLUE : Cell.RED; 
			pieceType[row][column] = playerPieces.get(getTurn());
			updateGameState(turn, row, column);
			turn = (turn == 'B')? 'R' : 'B';
		}
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