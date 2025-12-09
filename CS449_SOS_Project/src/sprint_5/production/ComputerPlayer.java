package sprint_5.production;

import java.util.Map;
import java.util.Random;
import sprint_5.production.SOSGame.Cell;
import sprint_5.production.SOSGame.GameState;

public class ComputerPlayer extends Player {
	private char autoPlayer;
	private int size = game.getSize();

	/* constructor
	 * 
	 * Precondition: 
	 * 		- a valid game, character, and map are passed as parameters
	 * 		- the character passed is either B or R
	 * 
	 * Postcondition:
	 * 		- a game is created (using the parent class)
	 * 		- the computer player is set to the specified player (R/B)
	 */
	public ComputerPlayer(SOSGame game, char player, Map<Character, Character> pieces) {
		super(game, player, pieces);
		this.autoPlayer = player;
	}
	
	/* resets the game
	 * 
	 * Precondition: 
	 * 		- there is an ongoing game
	 * 
	 * Postcondition:
	 * 		- if the blue player is a computer player, makes the first move in the game
	 */
	public void resetGame() {
		if (autoPlayer=='B') {
			makeFirstMove();
		}	
	}

	/* makes the first move if the computer player is starting the game
	 * 
	 * Precondition: 
	 * 		- there is an ongoing game
	 * 
	 * Postcondition:
	 * 		- a blue piece is placed at a spot
	 */
	private void makeFirstMove() {
		Random random = new Random();
		int position = random.nextInt(size * size);
		super.makeMove(position/size, position%size);
	}

	/* makes the appropriate move if the player is a human/computer
	 * 
	 * Precondition: 
	 * 		- 2 integer parameters are passed
	 * 		- row and column are < board size
	 * 
	 * Postcondition:
	 * 		- a computer or player move is made if there is an ongoing game
	 */
	@Override
	public void makeMove(int row, int column) {
		if (game.getGameState() != GameState.PLAYING) return;
			
		if (game.getTurn() == autoPlayer) {
			makeAutoMove();
		}
		else
			super.makeMove(row, column);
	}
	
	/* makes the random or winning computer move
	 * 
	 * Precondition: 
	 * 		- there is an ongoing game
	 * 
	 * Postcondition:
	 * 		- a computer move is made
	 */
	public void makeAutoMove() {
		if (makeWinningMove()) return;
	    makeRandomMove();
	}
	
	/* determines if an SOS can be created and places the final piece if one can
	 * 
	 * Precondition: 
	 * 		- there is an ongoing game
	 * 
	 * Postcondition:
	 * 		- returns true and makes a move to complete an SOS if possible, returns false otherwise
	 * 		- sets the computer player's current piece to the other piece type if an SOS can be created
	 */
	private boolean makeWinningMove() {
		char currentTurn = game.getTurn();
		for (int r = 0; r < size; r++) {
			for (int c = 0; c < size; c++) {
				if (game.getCell(r, c) != Cell.EMPTY)
	                continue;
				if (game.checkSOSWithO(r, c, 0, 1) || game.checkSOSWithO(r, c, 1, 0) || game.checkSOSWithO(r, c, 1, 1) || game.checkSOSWithO(r, c, 1, -1)) {
						playerPieces.replace(currentTurn, 'O');
					super.makeMove(r, c);
					return true;
				}
				if (game.checkSOSWithS(r, c, 0, 1, -1) || game.checkSOSWithS(r, c, 0, 1, 1) || game.checkSOSWithS(r, c, 1, 0, -1) || game.checkSOSWithS(r, c, 1, 0, 1) || game.checkSOSWithS(r, c, 1, 1, -1) || game.checkSOSWithS(r, c, 1, 1, 1) || game.checkSOSWithS(r, c, 1, -1, -1) || game.checkSOSWithS(r, c, 1, -1, 1)) {
						playerPieces.replace(currentTurn, 'S');
					super.makeMove(r, c);
					return true;
				}
			}
		}
		return false;
	}

	/* puts the computer piece on a random spot on the board
	 * 
	 * Precondition: 
	 * 		- there is an ongoing game
	 * 		- the board is not full
	 * 
	 * Postcondition:
	 * 		- a computer piece will be put in a random unoccupied cell
	 */
	private void makeRandomMove() {
		int numberOfEmptyCells = game.getNumberOfEmptyCells();
		Random random = new Random();
		int targetMove = random.nextInt(numberOfEmptyCells);
		int index=0;
		for (int row = 0; row < size; ++row) {
			for (int col = 0; col < size; ++col) {
				if (game.getCell(row, col) == Cell.EMPTY) {
					if (targetMove == index) {
						super.makeMove(row, col);
						return;
					} else
						index++;
				}
			}
		}
	}
}