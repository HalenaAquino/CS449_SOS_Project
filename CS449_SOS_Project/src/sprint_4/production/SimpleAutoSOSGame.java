package sprint_4.production;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;



// TODO: reconsider inheritence. May be better to have a general AutoSOSGame that inherits from SOSGame rather than 1 per game mode


public class SimpleAutoSOSGame extends SimpleSOSGame {
	private char autoPlayer;

	public SimpleAutoSOSGame(int s) {
		super(s);
	}
	
	public SimpleAutoSOSGame(int s, char player, Map<Character, Character> playerPieces) {
		super(s);
		this.autoPlayer = player;
		if (autoPlayer=='B') {
			makeFirstMove(playerPieces);
		}
	}

	/*@Override
	public void resetGame(Map<Character, Character> playerPieces) {
		super.resetGame();
		if (autoPlayer=='B') {
			makeFirstMove(playerPieces);
		}	
	}*/

	private void makeFirstMove(Map<Character, Character> playerPieces) {
		Random random = new Random();
		int position = random.nextInt(getSize() * getSize());
		super.makeMove(position/getSize(), position%getSize(), playerPieces);
	}

	@Override
	public void makeMove(int row, int column, Map<Character, Character> playerPieces) {
		
			
			if (getTurn() == autoPlayer && currentGameState == GameState.PLAYING) 
				makeAutoMove(playerPieces);
			else
				super.makeMove(row, column, playerPieces);
			
			
		
	}
	
	
	@Override
	public void makeAutoMove(Map<Character, Character> playerPieces) {
		System.out.println("player pieces: " + playerPieces);
		if (!makeWinningMove(playerPieces)) {
			if (!blockOpponentWinningMove())
				makeRandomMove(playerPieces);
		}

	}

	// need to fix game win logic
	private boolean makeWinningMove(Map<Character, Character> playerPieces) {
		char currentTurn = getTurn();
		for (int r = 0; r < getSize(); r++) {
			for (int c = 0; c < getSize(); c++) {
				if (game[r][c] != Cell.EMPTY)
	                continue;
				if (super.checkSOSWithO(r, c, 0, 1) || super.checkSOSWithO(r, c, 1, 0) || super.checkSOSWithO(r, c, 1, 1) || super.checkSOSWithO(r, c, 1, -1)) {
					//if (playerPieces.get(getTurn()) == 'S')
						playerPieces.replace(currentTurn, 'O');
					super.makeMove(r, c, playerPieces);
					return false;
				}
				if (super.checkSOSWithS(r, c, 0, 1, -1) || super.checkSOSWithS(r, c, 0, 1, 1) || super.checkSOSWithS(r, c, 1, 0, -1) || super.checkSOSWithS(r, c, 1, 0, 1) || super.checkSOSWithS(r, c, 1, 1, -1) || super.checkSOSWithS(r, c, 1, 1, 1) || super.checkSOSWithS(r, c, 1, -1, -1) || super.checkSOSWithS(r, c, 1, -1, 1)) {
					//if (playerPieces.get(getTurn()) == 'O')
						playerPieces.replace(currentTurn, 'S');
					super.makeMove(r, c, playerPieces);
					return false;
				}
			}
		}
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
