package sprint_5.production;

import java.util.Map;
import sprint_5.production.SOSGame.Cell;
import sprint_5.production.SOSGame.GameState;

public class Player {
	protected SOSGame game;
	protected char player;
	protected Map<Character, Character> playerPieces;
	
	/* constructor
	 * 
	 * Precondition: 
	 * 		- a valid game, character, and map are passed as parameters
	 * 		- the character passed is either B or R
	 * 
	 * Postcondition:
	 * 		- the class' game, player, and pieces are set to the passed parameters
	 */
	public Player(SOSGame game, char player, Map<Character, Character> pieces) {
		this.game = game;
		this.player = player;
		this.playerPieces = pieces;
	}
	
	/* places the current player's current piece on the given cell and updates the turn
	 * 
	 * Precondition: 
	 * 		- 2 integer parameters are passed
	 * 		- row and column are < board size
	 * 		- there is an established connection to the database
	 * 
	 * Postcondition:
	 * 		- makes a move on the specified cell if it's unoccupied
	 * 		- returns if there is no ongoing game
	 * 		- writes the move to the database if the game is being recorded
	 */
	public void makeMove(int row, int column) { 
		int size = game.getSize();
		char turn = game.getTurn();
		if (game == null || game.getGameState() != GameState.PLAYING) return;  // exits the function if there's not an ongoing game
		if (row >= 0 && row < size && column >= 0 && column < size && game.getCell(row, column) == Cell.EMPTY) {
			Cell value = (turn == 'B')? Cell.BLUE : Cell.RED; 
			game.setCell(row, column, value);
			game.setPieceType(row, column, playerPieces.get(turn));
			game.updateGameState(turn, row, column);
			
			// Saves the game to the database if the game is being recorded 
			if (game.recorded) InfluxDB.write(String.valueOf(turn), String.valueOf(playerPieces.get(turn)), row, column);
		}
	}
}