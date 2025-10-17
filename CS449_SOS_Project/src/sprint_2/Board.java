package sprint_2;

import java.util.Dictionary;

public class Board {

	private int[][] grid;
	private char turn = 'B';
	private char[][] pieceType; 

	public Board(int size) {
		if(size < 3 || size > 9)
			turn = ' ';
		else {
			grid = new int[size][size];
			pieceType = new char[size][size];
		}
	}

	public int getCell(int size, int row, int column) {
		if (row >= 0 && row < size && column >= 0 && column < size)
			return grid[row][column];
		else
			return -1;
	}

	public char getTurn() {
		return turn;
	}
	
	public char getPieceType(int size, int row, int column) {
		if(row >= 0 && row < size && column >= 0 && column < size) 
			return pieceType[row][column];
		else
			return ' ';
	}
	
	public void makeMove(int size, int row, int column, Dictionary<Character, Character> playerPieces) {
		if (row >= 0 && row < size && column >= 0 && column < size
				&& grid[row][column] == 0) {
			grid[row][column] = (turn == 'B')? 1 : 2; 
			pieceType[row][column] = playerPieces.get(getTurn());
			turn = (turn == 'B')? 'R' : 'B';
		}
	}
}
