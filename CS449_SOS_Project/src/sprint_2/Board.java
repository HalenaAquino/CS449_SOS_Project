package sprint_2;
import java.util.Dictionary;

public class Board {

	private int[][] grid;
	private char turn = 'X';

	public Board(int size) {
		grid = new int[size][size];
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
	
	public void makeMove(int size, int row, int column) {
		if (row >= 0 && row < size && column >= 0 && column < size
				&& grid[row][column] == 0) {
			grid[row][column] = (turn == 'B')? 1 : 2; 
			turn = (turn == 'B')? 'R' : 'B';
		}
	}
}
