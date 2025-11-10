package sprint_4.production;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SimpleAutoSOSGame extends SimpleSOSGame {
	private char autoPlayer;
	private Map<Character, Character> pieces;

	public SimpleAutoSOSGame(int s) {
		super(s);
	}
	
	public SimpleAutoSOSGame(int s, char player) {
		super(s);
		this.autoPlayer = player;
		if (autoPlayer=='B') {
			makeFirstMove();
		}
	}

	@Override
	public void resetGame() {
		super.resetGame();
		if (autoPlayer=='B') {
			makeFirstMove();
		}	
	}

	private void makeFirstMove() {
		Random random = new Random();
		int position = random.nextInt(getSize() * getSize());
		super.makeMove(position/getSize(), position%getSize(), pieces);
	}

	@Override
	public void makeMove(int row, int column, Map<Character, Character> playerPieces) {
		pieces = playerPieces;
		System.out.println("Making an auto move");
		System.out.println("autoPlayer = " + autoPlayer);
		
			
			if (getTurn() == autoPlayer && currentGameState == GameState.PLAYING) 
				makeAutoMove(playerPieces);
			else
				super.makeMove(row, column, playerPieces);
			
		System.out.println("current Turn: " + getTurn());
			
		
	}
	
	
	@Override
	public void makeAutoMove(Map<Character, Character> playerPieces) {
		if (!makeWinningMove()) {
			if (!blockOpponentWinningMove())
				makeRandomMove(playerPieces);
		}

	}

	// need to fix game win logic
	private boolean makeWinningMove() {
		/*for (int r = 0; r < getSize(); r++) {
			for (int c = 0; c < getSize(); c++) {
				if(hasWon(getTurn(), r, c)) {
					makeMove(r, c, pieces);
					return true;
				}
			}
		}*/
		return false;
	}

	private boolean blockOpponentWinningMove() {
		return false;
	}

	private void makeRandomMove(Map<Character, Character> playerPieces) {
		int numberOfEmptyCells = getNumberOfEmptyCells();
		Random random = new Random();
		int targetMove = random.nextInt(numberOfEmptyCells);
		int index=0;
		System.out.println("makeRandomMove ran");
		for (int row = 0; row < getSize(); ++row) {
			for (int col = 0; col < getSize(); ++col) {
				if (game[row][col] == Cell.EMPTY) {
					if (targetMove == index) {
						super.makeMove(row, col, playerPieces);
						return;
					} else
						index++;
				}
			}
		}
	}

	private int getNumberOfEmptyCells() {
		int numberOfEmptyCells = 0;
		for (int row = 0; row < getSize(); ++row) {
			for (int col = 0; col < getSize(); ++col) {
				if (game[row][col] == Cell.EMPTY) {
					numberOfEmptyCells++;
				}
			}
		}
		return numberOfEmptyCells;
	}
	


}
