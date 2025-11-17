package sprint_4.production;

import java.util.Map;
import java.util.Random;

import sprint_4.production.SOSGame.Cell;
import sprint_4.production.SOSGame.GameState;

// Copied from previous SImpleAutoSOSGame class

public class ComputerPlayer extends Player {
	private char autoPlayer;
	private int size = game.getSize();

	// constructor
	public ComputerPlayer(SOSGame game, char player) {
		super(game, player);
		this.autoPlayer = player;
	}
	
	// resets the game
	public void resetGame(Map<Character, Character> playerPieces) {
		if (autoPlayer=='B') {
			makeFirstMove(playerPieces);
		}	
	}

	// makes the first move if the computer player is starting the game
	private void makeFirstMove(Map<Character, Character> playerPieces) {
		Random random = new Random();
		int position = random.nextInt(size * size);
		super.makeMove(position/size, position%size, playerPieces);
	}

	// makes the appropriate move if the player is a human/computer
	@Override
	public void makeMove(int row, int column, Map<Character, Character> playerPieces) {
		if (game.getGameState() != GameState.PLAYING) return;
			
		if (game.getTurn() == autoPlayer) {
			makeAutoMove(playerPieces);
		}
		else
			super.makeMove(row, column, playerPieces);
	}
	
	// makes the random or winning computer move
	public void makeAutoMove(Map<Character, Character> playerPieces) {
		if (makeWinningMove(playerPieces)) return;
	    makeRandomMove(playerPieces);
	}
	
	// determines if an SOS can be created and places the final piece if one can
	private boolean makeWinningMove(Map<Character, Character> playerPieces) {
		char currentTurn = game.getTurn();
		for (int r = 0; r < size; r++) {
			for (int c = 0; c < size; c++) {
				if (game.getCell(r, c) != Cell.EMPTY)
	                continue;
				if (game.checkSOSWithO(r, c, 0, 1) || game.checkSOSWithO(r, c, 1, 0) || game.checkSOSWithO(r, c, 1, 1) || game.checkSOSWithO(r, c, 1, -1)) {
						playerPieces.replace(currentTurn, 'O');
					super.makeMove(r, c, playerPieces);
					return true;
				}
				if (game.checkSOSWithS(r, c, 0, 1, -1) || game.checkSOSWithS(r, c, 0, 1, 1) || game.checkSOSWithS(r, c, 1, 0, -1) || game.checkSOSWithS(r, c, 1, 0, 1) || game.checkSOSWithS(r, c, 1, 1, -1) || game.checkSOSWithS(r, c, 1, 1, 1) || game.checkSOSWithS(r, c, 1, -1, -1) || game.checkSOSWithS(r, c, 1, -1, 1)) {
					//if (playerPieces.get(getTurn()) == 'O')
						playerPieces.replace(currentTurn, 'S');
					super.makeMove(r, c, playerPieces);
					return true;
				}
			}
		}
		return false;
	}

	// puts the computer piece on a random spot on the board
	private void makeRandomMove(Map<Character, Character> playerPieces) {
		int numberOfEmptyCells = game.getNumberOfEmptyCells();
		Random random = new Random();
		int targetMove = random.nextInt(numberOfEmptyCells);
		int index=0;
		for (int row = 0; row < size; ++row) {
			for (int col = 0; col < size; ++col) {
				if (game.getCell(row, col) == Cell.EMPTY) {
					if (targetMove == index) {
						super.makeMove(row, col, playerPieces);
						return;
					} else
						index++;
				}
			}
		}
	}
}