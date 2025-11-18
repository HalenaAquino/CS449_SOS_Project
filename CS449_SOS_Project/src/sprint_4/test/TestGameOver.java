package sprint_4.test;

import static org.junit.Assert.*;

import java.util.Map;import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;

import sprint_4.production.GeneralSOSGame;
import sprint_4.production.Player;
import sprint_4.production.SOSGame;
import sprint_4.production.SimpleSOSGame;

public class TestGameOver { 
	
	private SOSGame gameS, gameG;
	private Player redS, blueS, redG, blueG;
    private Map<Character, Character> playerPieces;
	
	@Before
    public void setUp() {
        gameS = new SimpleSOSGame(3);
        gameG = new GeneralSOSGame(3);
        playerPieces = new Hashtable<>();
        playerPieces.put('B', 'S');
        playerPieces.put('R', 'O');
        blueS = new Player(gameS, 'B', playerPieces);
        blueG = new Player(gameG, 'B', playerPieces);
        redS = new Player(gameS, 'R', playerPieces);
        redG = new Player(gameG, 'R', playerPieces);
    }

	// AC 5.1 - a simple game ends with a blue win
	@Test
	public void testSimpleWinbyBlue() {
		blueS.makeMove(0, 0);
		redS.makeMove(1, 1);
		blueS.makeMove(2, 2);
		assertEquals(SOSGame.GameState.BLUE_WON, gameS.getGameState());
	}
	
	// AC 5.2 - a simple game ends with a red win
	@Test
	public void testSimpleWinbyRed() {
		blueS.makeMove(0, 0);
		redS.makeMove(0, 1);
		blueS.makeMove(2, 2);
		redS.makeMove(1, 1);
		assertEquals(SOSGame.GameState.RED_WON, gameS.getGameState());
	}
		
	// AC 5.3 - a simple game ends in a draw
	@Test
	public void testTiedSimpleGame() {
		blueS.makeMove(0, 0);
		redS.makeMove(0, 2);
		blueS.makeMove(0, 1);
		redS.makeMove(1, 2);
		blueS.makeMove(1, 1);
		redS.makeMove(1, 0);
		blueS.makeMove(2, 1);
		redS.makeMove(2, 0);
		blueS.makeMove(2, 2);
		assertEquals(SOSGame.GameState.DRAW, gameS.getGameState());
	}
	
	// AC 7.1 - a general game ends in a player win
	@Test
	public void testGeneralGameOverPlayerWin() {
		blueG.makeMove(0, 0);
		redG.makeMove(1, 1);
		blueG.makeMove(2, 2);
		redG.makeMove(1, 0);
		blueG.makeMove(2, 0);
		redG.makeMove(0, 1);
		blueG.makeMove(0, 2);
		redG.makeMove(1, 2);
		blueG.makeMove(2, 1);
		assertEquals(SOSGame.GameState.BLUE_WON, gameG.getGameState());
	}
	
	// AC 7.2 - a general game ends in a draw
	@Test
	public void testGeneralGameOverDraw() {
		blueG.makeMove(0, 0);
		redG.makeMove(0, 2);
		blueG.makeMove(0, 1);
		redG.makeMove(1, 0);
		blueG.makeMove(1, 1);
		redG.makeMove(1, 2);
		blueG.makeMove(2, 1);
		redG.makeMove(2, 0);
		blueG.makeMove(2, 2);
		assertEquals(SOSGame.GameState.DRAW, gameG.getGameState());
	}
	
	// AC 7.3 - game continues after SOS is made
	@Test
	public void testGeneralContinueAfterSOS() {
		blueG.makeMove(0, 0);
		redG.makeMove(1, 1);
		blueG.makeMove(2, 2);		// makes SOS
		assertEquals(1, gameG.getBlueScore());
		assertEquals(SOSGame.GameState.PLAYING, gameG.getGameState());		// determines if the game is still active after SOS made
	}
	
	// AC 7.4 - draw detection mid-turn
	@Test
	public void testGeneralDrawDetectionMidTurn() {
		blueG.makeMove(0, 0);
		redG.makeMove(1, 1);
		blueG.makeMove(2, 2);
		gameG.setTurn('R');
		redG.makeMove(0, 1);
		blueG.makeMove(2, 0);
		redG.makeMove(1, 0);
		assertEquals(SOSGame.GameState.PLAYING, gameG.getGameState());
		assertEquals(gameG.getBlueScore(), gameG.getRedScore());		// tests if the SOS scores are even mid-game
	}
}