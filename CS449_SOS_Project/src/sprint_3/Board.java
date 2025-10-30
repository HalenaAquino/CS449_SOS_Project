package sprint_3;

import java.util.Dictionary;

// Parent class
abstract class Game {
	protected int size;
    protected Board board;

    public Game(Board board, int size) {
        this.board = board;
        this.size = size;
    }

    public abstract boolean hasWon(char player, int row, int column);

    public abstract boolean isDraw();
}

class SimpleGame {
	// TODO
}

class GeneralGame {
	// TODO
}

public class Board {

	// general variable declarations
	private char turn;
	private char[][] pieceType; 
	private String gameMode = "";
	private static int SIZE;

	public enum Cell {
		EMPTY, BLUE, RED
	}
	
	private Cell[][] grid;

	public enum GameState {
		PLAYING, DRAW, BLUE_WON, RED_WON
	}
	
	private GameState currentGameState;
	
	// creates the board
	public Board(int size) {
		if(size < 3 || size > 9)
			turn = ' ';
		else {
			grid = new Cell[size][size];
			pieceType = new char[size][size];
			SIZE = size;
		}
	}
	
	private void initGame() {
		for (int row = 0; row < SIZE; ++row) {
			for (int col = 0; col < SIZE; ++col) {
				grid[row][col] = Cell.EMPTY;
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
			return grid[row][column];
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
	
	// places the current player's current piece on the given cell and updates the turn
	public void makeMove(int row, int column, Dictionary<Character, Character> playerPieces) {
		if (row >= 0 && row < SIZE && column >= 0 && column < SIZE
				&& grid[row][column] == Cell.EMPTY) {
			grid[row][column] = (turn == 'B')? Cell.BLUE : Cell.RED; 
			pieceType[row][column] = playerPieces.get(getTurn());
			updateGameState(turn, row, column);
			turn = (turn == 'B')? 'R' : 'B';
		}
	}
	
	private void updateGameState(char turn, int row, int column) {
		if (hasWon(turn, row, column)) { // check for win
			currentGameState = (turn == 'B') ? GameState.BLUE_WON : GameState.RED_WON;
		} else if (isDraw()) {
			currentGameState = GameState.DRAW;
		}
	}
	
	private boolean isDraw() {
		for (int row = 0; row < SIZE; ++row) {
			for (int col = 0; col < SIZE; ++col) {
				if (grid[row][col] == Cell.EMPTY) {
					return false; // an empty cell found, not draw
				}
			}
		}
		return true;
	}

	private boolean hasWon(char turn, int row, int column) {
		Cell token = (turn == 'B') ? Cell.BLUE : Cell.RED;
		return (grid[row][0] == token // 3-in-the-row
				&& grid[row][1] == token && grid[row][2] == token
				|| grid[0][column] == token // 3-in-the-column
						&& grid[1][column] == token && grid[2][column] == token
				|| row == column // 3-in-the-diagonal
						&& grid[0][0] == token && grid[1][1] == token && grid[2][2] == token
				|| row + column == 2 // 3-in-the-opposite-diagonal
						&& grid[0][2] == token && grid[1][1] == token && grid[2][0] == token);
	}
}