package sprint_4.test;

import static org.junit.Assert.*;

import java.util.Map;import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;

import sprint_3.production.GeneralSOSGame;
import sprint_3.production.SOSGame;
import sprint_3.production.SimpleSOSGame;

public class TestGameOver {
	
	private SOSGame gameS, gameG;
    private Map<Character, Character> playerPieces;
	
	@Before
    public void setUp() {
        gameS = new SimpleSOSGame(3);
        gameG = new GeneralSOSGame(3);
        playerPieces = new Hashtable<>();
        playerPieces.put('B', 'S');
        playerPieces.put('R', 'O');
    }

	// AC 5.1 - a simple game ends with a blue win
	@Test
	public void testSimpleWinbyBlue() {
		gameS.makeMove(0, 0, playerPieces);
		gameS.makeMove(1, 1, playerPieces);
		gameS.makeMove(2, 2, playerPieces);
		assertEquals(SOSGame.GameState.BLUE_WON, gameS.getGameState());
	}
	
	// AC 5.2 - a simple game ends with a red win
	@Test
	public void testSimpleWinbyRed() {
		gameS.makeMove(0, 0, playerPieces);
		gameS.makeMove(0, 1, playerPieces);
		gameS.makeMove(2, 2, playerPieces);
		gameS.makeMove(1, 1, playerPieces);
		assertEquals(SOSGame.GameState.RED_WON, gameS.getGameState());
	}
		
	// AC 5.3 - a simple game ends in a draw
	@Test
	public void testTiedSimpleGame() {
		gameS.makeMove(0, 0, playerPieces);
		gameS.makeMove(0, 2, playerPieces);
		gameS.makeMove(0, 1, playerPieces);
		gameS.makeMove(1, 2, playerPieces);
		gameS.makeMove(1, 1, playerPieces);
		gameS.makeMove(1, 0, playerPieces);
		gameS.makeMove(2, 1, playerPieces);
		gameS.makeMove(2, 0, playerPieces);
		gameS.makeMove(2, 2, playerPieces);
		assertEquals(SOSGame.GameState.DRAW, gameS.getGameState());
	}
	
	// AC 7.1 - a general game ends in a player win
	@Test
	public void testGeneralGameOverPlayerWin() {
		gameG.makeMove(0, 0, playerPieces);
		gameG.makeMove(1, 1, playerPieces);
		gameG.makeMove(2, 2, playerPieces);
		gameG.makeMove(1, 0, playerPieces);
		gameG.makeMove(2, 0, playerPieces);
		gameG.makeMove(0, 1, playerPieces);
		gameG.makeMove(0, 2, playerPieces);
		gameG.makeMove(1, 2, playerPieces);
		gameG.makeMove(2, 1, playerPieces);
		assertEquals(SOSGame.GameState.BLUE_WON, gameG.getGameState());
	}
	
	// AC 7.2 - a general game ends in a draw
	@Test
	public void testGeneralGameOverDraw() {
		gameG.makeMove(0, 0, playerPieces);
		gameG.makeMove(0, 2, playerPieces);
		gameG.makeMove(0, 1, playerPieces);
		gameG.makeMove(1, 0, playerPieces);
		gameG.makeMove(1, 1, playerPieces);
		gameG.makeMove(1, 2, playerPieces);
		gameG.makeMove(2, 1, playerPieces);
		gameG.makeMove(2, 0, playerPieces);
		gameG.makeMove(2, 2, playerPieces);
		assertEquals(SOSGame.GameState.DRAW, gameG.getGameState());
	}
	
	// AC 7.3 - game continues after SOS is made
	@Test
	public void testGeneralContinueAfterSOS() {
		gameG.makeMove(0, 0, playerPieces);
		gameG.makeMove(1, 1, playerPieces);
		gameG.makeMove(2, 2, playerPieces);		// makes SOS
		assertEquals(1, gameG.getBlueScore());
		assertEquals(SOSGame.GameState.PLAYING, gameG.getGameState());		// determines if the game is still active after SOS made
	}
	
	// AC 7.4 - draw detection mid-turn
	@Test
	public void testGeneralDrawDetectionMidTurn() {
		gameG.makeMove(0, 0, playerPieces);
		gameG.makeMove(1, 1, playerPieces);
		gameG.makeMove(2, 2, playerPieces);
		gameG.makeMove(0, 1, playerPieces);
		gameG.makeMove(2, 0, playerPieces);
		gameG.makeMove(1, 0, playerPieces);
		assertEquals(SOSGame.GameState.PLAYING, gameG.getGameState());
		assertEquals(gameG.getBlueScore(), gameG.getRedScore());		// tests if the SOS scores are even mid-game
	}
}