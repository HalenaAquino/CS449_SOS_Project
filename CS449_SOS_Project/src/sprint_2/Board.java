package sprint_2;

import java.util.Dictionary;

public class Board {

	// general variable declarations
	private int[][] grid;
	private char turn = 'B';
	private char[][] pieceType; 
	private String gameMode = "";

	// creates the board
	public Board(int size) {
		if(size < 3 || size > 9)
			turn = ' ';
		else {
			grid = new int[size][size];
			pieceType = new char[size][size];
		}
	}

	// returns the player that's currently occupying a cell (1 for blue, 2 for red, 0 for empty); returns -1 if the cell doesn't exist
	public int getCell(int size, int row, int column) {
		if (row >= 0 && row < size && column >= 0 && column < size)
			return grid[row][column];
		else
			return -1;
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
	public char getPieceType(int size, int row, int column) {
		if(row >= 0 && row < size && column >= 0 && column < size) 
			return pieceType[row][column];
		else
			return ' ';
	}
	
	// places the current player's current piece on the given cell and updates the turn
	public void makeMove(int size, int row, int column, Dictionary<Character, Character> playerPieces) {
		if (row >= 0 && row < size && column >= 0 && column < size
				&& grid[row][column] == 0) {
			grid[row][column] = (turn == 'B')? 1 : 2; 
			pieceType[row][column] = playerPieces.get(getTurn());
			turn = (turn == 'B')? 'R' : 'B';
		}
	}
}