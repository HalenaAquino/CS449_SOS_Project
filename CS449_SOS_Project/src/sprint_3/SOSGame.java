package sprint_3;

import java.util.Dictionary;



//TODO: Turn Board into SOSGame class and have GeneralSOSGame and SimpleSOSGame classes inherit from it
// 				hasWon should be the only unique function in the children classes

// DEPENDENCY INVERSION WILL BE CONSIDERED FOR EXTRA CREDIT






/*// Parent class
abstract class Game {
	protected int size;
    protected Board board;

    public Game(Board board, int size) {
        this.board = board;
        this.size = size;
    }

    public abstract boolean hasWon(char player, int row, int column);

    public abstract boolean isDraw();
}*/

class SimpleSOSGame extends SOSGame{
	//SOSGame game;
	//int size;;
	
	public SimpleSOSGame(int s) {
		super(s);
		//game = g;
		//size = s;
	}	
	
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
		
		
		return false;
		
	}

	public boolean isDraw() {
		return boardFull();
	}
	
	
	/*public boolean isDraw() {
		for (int row = 0; row < size; ++row) {
			for (int col = 0; col < size; ++col) {
				if (board[row][col] == Board.Cell.EMPTY) {
					return false; // an empty cell found, not draw
				}
			}
		}
		return true;
	}*/
	
	
}

class GeneralSOSGame extends SOSGame{
	// TODO
	public GeneralSOSGame(int size) {
		super(size);
	}
	
	
	@Override
	public boolean hasWon(char player, int row, int col) {
	    // Count SOS patterns that include the last placed piece at (row, col)
	    char[][] pieces = getPieceTypeArray();
	    int points = 0;
	    char placed = pieces[row][col];


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

	    if (points > 0) {
	        if (player == 'B') blueScore += points;
	        else                redScore  += points;
	    }

	    // Return false so the game does NOT immediately end in General mode.
	    System.out.print("Blue score: " + blueScore);		// debugging
	    System.out.print("\nRed score: " + redScore + "\n");  // debugging
	    
	    if(player == 'B' && blueScore > redScore) {
	    	currentGameState = GameState.BLUE_WON;
	    	return true;
	    }
	    else if (player == 'R' && blueScore < redScore) {
	    	currentGameState = GameState.RED_WON;
	    	return true;
	    }
	    return false;
	}

	
	
	
	
	public boolean isDraw() {
		return boardFull() && (blueScore == redScore);
	}
}

abstract class SOSGame {

	// general variable declarations
	private char turn;
	protected char[][] pieceType; 
	private String gameMode = "";
	protected static int SIZE;
	protected GameState currentGameState;
	protected Cell[][] game;

	protected int blueScore = 0;
	protected int redScore  = 0;

	public int getBlueScore() { return blueScore; }
	public int getRedScore() { return redScore; }



	public enum Cell {
		EMPTY, BLUE, RED
	}
	
	public enum GameState {
		PLAYING, DRAW, BLUE_WON, RED_WON
	}
		
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
	
	private void initGame() {
		for (int row = 0; row < SIZE; ++row) {
			for (int col = 0; col < SIZE; ++col) {
				game[row][col] = Cell.EMPTY;
				pieceType[row][col] = ' ';
			}
		}
		currentGameState = GameState.PLAYING;
		turn = 'B';
	}
	
	public void resetGame() {
		initGame();
	}
	
	public GameState getGameState() {
		return currentGameState;
	}

	// returns the player that's currently occupying a cell (1 for blue, 2 for red, 0 for empty); returns -1 if the cell doesn't exist
	public Cell getCell(int row, int column) {
		if (row >= 0 && row < SIZE && column >= 0 && column < SIZE)
			return game[row][column];
		else
			return null;
	}

	// returns the current turn
	public char getTurn() {
		return turn;
	}
	
	// updates the current gamemode
	public void setGamemode(String mode) {
		gameMode = mode;
	}
	
	// returns the current gamemode
	public String getGamemode() {
		return gameMode;
	}
	
	// returns the current piece in the cell (S/O)
	public char getPieceType(int row, int column) {
		if(row >= 0 && row < SIZE && column >= 0 && column < SIZE) 
			return pieceType[row][column];
		else
			return ' ';
	}
	
	public char[][] getPieceTypeArray(){
		return pieceType;
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
	
	protected boolean boardFull() {
		// returns false is there are any unoccupied cells
	    for (int r = 0; r < SIZE; r++)
	        for (int c = 0; c < SIZE; c++)
	            if (game[r][c] == Cell.EMPTY) return false;
	    
	    return true;
	}
	
	public abstract boolean hasWon(char player, int row, int column);
	public abstract boolean isDraw();
	
	private void updateGameState(char turn, int row, int column) {
		if (hasWon(turn, row, column)) { // check for win
			currentGameState = (turn == 'B') ? GameState.BLUE_WON : GameState.RED_WON;
		} else if (isDraw()) {
			currentGameState = GameState.DRAW;
		}
	}
	

	/*private boolean hasWon(char turn, int row, int column) {
		Cell token = (turn == 'B') ? Cell.BLUE : Cell.RED;
		return (grid[row][0] == token // 3-in-the-row
				&& grid[row][1] == token && grid[row][2] == token
				|| grid[0][column] == token // 3-in-the-column
						&& grid[1][column] == token && grid[2][column] == token
				|| row == column // 3-in-the-diagonal
						&& grid[0][0] == token && grid[1][1] == token && grid[2][2] == token
				|| row + column == 2 // 3-in-the-opposite-diagonal
						&& grid[0][2] == token && grid[1][1] == token && grid[2][0] == token);
	}*/
}