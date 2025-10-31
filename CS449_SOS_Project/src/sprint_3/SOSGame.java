package sprint_3;

import java.util.Dictionary;

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
	        // Horizontal
	        if (column > 0 && column < SIZE - 1 &&
	            pieces[row][column - 1] == 'S' &&
	            pieces[row][column + 1] == 'S')
	            return true;

	        // Vertical
	        if (row > 0 && row < SIZE - 1 &&
	            pieces[row - 1][column] == 'S' &&
	            pieces[row + 1][column] == 'S')
	            return true;

	        // Diagonal (\)
	        if (row > 0 && row < SIZE - 1 && column > 0 && column < SIZE - 1 &&
	            pieces[row - 1][column - 1] == 'S' &&
	            pieces[row + 1][column + 1] == 'S')
	            return true;

	        // Other diagonal (/)
	        if (row > 0 && row < SIZE - 1 && column > 0 && column < SIZE - 1 &&
	            pieces[row - 1][column + 1] == 'S' &&
	            pieces[row + 1][column - 1] == 'S')
	            return true;
	    }

	    // Case 2: when S is placed completing SOS on the right (S O S)
	    if (placed == 'S') {
	        // Horizontal: S placed after SO
	        if (column >= 2 &&
	            pieces[row][column - 1] == 'O' &&
	            pieces[row][column - 2] == 'S')
	            return true;

	        // Horizontal: S placed before OS
	        if (column <= SIZE - 3 &&
	            pieces[row][column + 1] == 'O' &&
	            pieces[row][column + 2] == 'S')
	            return true;

	        // Vertical: S placed after SO
	        if (row >= 2 &&
	            pieces[row - 1][column] == 'O' &&
	            pieces[row - 2][column] == 'S')
	            return true;

	        // Vertical: S placed before OS
	        if (row <= SIZE - 3 &&
	            pieces[row + 1][column] == 'O' &&
	            pieces[row + 2][column] == 'S')
	            return true;

	        // Diagonal (\): S placed after SO
	        if (row >= 2 && column >= 2 &&
	            pieces[row - 1][column - 1] == 'O' &&
	            pieces[row - 2][column - 2] == 'S')
	            return true;

	        // Diagonal (\): S placed before OS
	        if (row <= SIZE - 3 && column <= SIZE - 3 &&
	            pieces[row + 1][column + 1] == 'O' &&
	            pieces[row + 2][column + 2] == 'S')
	            return true;

	        // Other diagonal (/): S placed after SO
	        if (row >= 2 && column <= SIZE - 3 &&
	            pieces[row - 1][column + 1] == 'O' &&
	            pieces[row - 2][column + 2] == 'S')
	            return true;

	        // Other diagonal (/): S placed before OS
	        if (row <= SIZE - 3 && column >= 2 &&
	            pieces[row + 1][column - 1] == 'O' &&
	            pieces[row + 2][column - 2] == 'S')
	            return true;
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
	        // Horizontal
	        if (col - 1 >= 0 && col + 1 < SIZE &&
	            pieces[row][col - 1] == 'S' && pieces[row][col + 1] == 'S') points++;

	        // Vertical
	        if (row - 1 >= 0 && row + 1 < SIZE &&
	            pieces[row - 1][col] == 'S' && pieces[row + 1][col] == 'S') points++;

	        // Diagonal \ 
	        if (row - 1 >= 0 && col - 1 >= 0 && row + 1 < SIZE && col + 1 < SIZE &&
	            pieces[row - 1][col - 1] == 'S' && pieces[row + 1][col + 1] == 'S') points++;

	        // Diagonal /
	        if (row - 1 >= 0 && col + 1 < SIZE && row + 1 < SIZE && col - 1 >= 0 &&
	            pieces[row - 1][col + 1] == 'S' && pieces[row + 1][col - 1] == 'S') points++;
	    }

	    // finds and counts SOS's when an O was placed
	    if (placed == 'S') {
	        if (col + 2 < SIZE &&
	            pieces[row][col + 1] == 'O' && pieces[row][col + 2] == 'S') points++;
	        if (col - 2 >= 0 &&
	            pieces[row][col - 1] == 'O' && pieces[row][col - 2] == 'S') points++;

	        if (row + 2 < SIZE &&
	            pieces[row + 1][col] == 'O' && pieces[row + 2][col] == 'S') points++;
	        if (row - 2 >= 0 &&
	            pieces[row - 1][col] == 'O' && pieces[row - 2][col] == 'S') points++;

	        if (row + 2 < SIZE && col + 2 < SIZE &&
	            pieces[row + 1][col + 1] == 'O' && pieces[row + 2][col + 2] == 'S') points++;
	        if (row - 2 >= 0 && col - 2 >= 0 &&
	            pieces[row - 1][col - 1] == 'O' && pieces[row - 2][col - 2] == 'S') points++;

	        if (row - 2 >= 0 && col + 2 < SIZE &&
	            pieces[row - 1][col + 1] == 'O' && pieces[row - 2][col + 2] == 'S') points++;
	        if (row + 2 < SIZE && col - 2 >= 0 &&
	            pieces[row + 1][col - 1] == 'O' && pieces[row + 2][col - 2] == 'S') points++;
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
abstract class SOSGame {

	// --------------------------------   VARIABLE DECLARATIONS  -------------------------------------
	
	// public (used in GUI)
	public enum Cell { EMPTY, BLUE, RED }
	public enum GameState { PLAYING, DRAW, BLUE_WON, RED_WON }
	
	// private (only used in this class)
	private char turn;
	private String gameMode = "";
	
	// protected (used in children classes)
	protected char[][] pieceType; 
	protected static int SIZE;
	protected int blueScore = 0;
	protected int redScore  = 0;
	protected GameState currentGameState;
	protected Cell[][] game;

	
	// --------------------------------   GETTERS  -------------------------------------
	public int getBlueScore() { return blueScore; }
	public int getRedScore() { return redScore; }
	public char getTurn() {return turn; }
	public String getGamemode() { return gameMode; }
	public char[][] getPieceTypeArray(){ return pieceType; }
	public GameState getGameState() { return currentGameState; }
		
	// returns the current piece in the cell (S/O)
	public char getPieceType(int row, int column) {
		if(row >= 0 && row < SIZE && column >= 0 && column < SIZE) 
			return pieceType[row][column];
		else
			return ' ';
	}

	// returns the player that's currently occupying a cell (1 for blue, 2 for red, 0 for empty) or returns -1 if the cell doesn't exist
	public Cell getCell(int row, int column) {
		if (row >= 0 && row < SIZE && column >= 0 && column < SIZE)
			return game[row][column];
		else
			return null;
	}
	
	// -------------------------------   GAME LOGIC ------------------------------------------	
	
	// abstract classes (defined in subclasses)
	protected abstract boolean isDraw();
	protected abstract void updateGameState(char turn, int row, int column);
	
	// creates the board
	public SOSGame(int size) {
		if(size < 3 || size > 9)
			turn = ' ';
		else {
			game = new Cell[size][size];
			pieceType = new char[size][size];
			SIZE = size;
		}
	}
	
	// initializes the game, resets all of the variables and board
	private void initGame() {
		for (int row = 0; row < SIZE; ++row) {
			for (int col = 0; col < SIZE; ++col) {
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
	public void makeMove(int row, int column, Dictionary<Character, Character> playerPieces) {
		if (row >= 0 && row < SIZE && column >= 0 && column < SIZE
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
	    for (int r = 0; r < SIZE; r++)
	        for (int c = 0; c < SIZE; c++)
	            if (game[r][c] == Cell.EMPTY) return false;
	    
	    return true;
	}
}