package sprint_4.production;

import java.util.Map;

import sprint_4.production.SOSGame.Cell;
import sprint_4.production.SOSGame.GameState;

public class Player {
	protected SOSGame game;
	protected char players;
	
	public Player(SOSGame game, char players) {
		this.game = game;
		this.players = players;
	}
	
	// Taken from SOSGame
	// places the current player's current piece on the given cell and updates the turn
	public void makeMove(int row, int column, Map<Character, Character> playerPieces) {
		int size = game.getSize();
		char turn = game.getTurn();
		if (game == null || game.getGameState() != GameState.PLAYING) return;  // exits the function if there's not an ongoing game
		if (row >= 0 && row < size && column >= 0 && column < size
				&& game.getCell(row, column) == Cell.EMPTY) {
			Cell value = (turn == 'B')? Cell.BLUE : Cell.RED; 
			game.setCell(row, column, value);
			game.setPieceType(row, column, playerPieces.get(turn));
			game.updateGameState(turn, row, column);
		}
	}
}