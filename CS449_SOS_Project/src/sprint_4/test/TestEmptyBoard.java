package sprint_4.test;

import static org.junit.Assert.*;
import java.util.Hashtable;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import sprint_3.production.SOSGame;
import sprint_3.production.SimpleSOSGame;

public class TestEmptyBoard {

    private SOSGame game;
    private Map<Character, Character> playerPieces;

    @Before
    public void setUp() {
        playerPieces = new Hashtable<>();
        playerPieces.put('B', 'S');
        playerPieces.put('R', 'O');
    }

    // AC 1.1 Valid board size
    @Test
    public void testValidBoardCreation() {
        game = new SimpleSOSGame(5); 
        assertNotNull(game);
        assertEquals('B', game.getTurn());
    }

    // AC 1.2 Invalid size: too small
    @Test
    public void testInvalidBoardTooSmall() {
        game = new SimpleSOSGame(2); // invalid
        assertEquals(' ', game.getTurn());
    }

    // AC 1.2 Invalid size: too large
    @Test
    public void testInvalidBoardTooLarge() {
        game = new SimpleSOSGame(11); // invalid
        assertEquals(' ', game.getTurn());
    }

    // AC 3.1 Start game initializes board
    @Test
    public void testEmptyBoardOnStart() {
        int size = 4;
        game = new SimpleSOSGame(size);
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                assertEquals(SOSGame.Cell.EMPTY, game.getCell(r, c));
            }
        }
    }
    
    // AC 3.2: Cannot start game without selecting settings
    @Test
    public void testCannotStartGameWithoutSettings() {
        game = new SimpleSOSGame(0); // invalid size
        assertEquals("", game.getGamemode());

        // Attempting a move should not succeed
        game.makeMove(0, 0, playerPieces); // updated method signature
        assertEquals(null, game.getCell(0, 0)); // invalid cell
        assertEquals(' ', game.getTurn());    // turn should be empty
    }

    // AC 3.3 Game reset: new board clears old
    @Test
    public void testGameReset() {
        game = new SimpleSOSGame(3);
        game.makeMove(0, 0, playerPieces);
        assertEquals(SOSGame.Cell.BLUE, game.getCell(0, 0));
        game = new SimpleSOSGame(3); // new board created
        assertEquals(SOSGame.Cell.EMPTY, game.getCell(0, 0));
        assertEquals('B', game.getTurn());
    }
}