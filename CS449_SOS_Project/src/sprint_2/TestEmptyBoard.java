package sprint_2;

import static org.junit.Assert.*;
import java.util.Dictionary;
import java.util.Hashtable;
import org.junit.Before;
import org.junit.Test;

public class TestEmptyBoard {

    private Board board;
    private Dictionary<Character, Character> playerPieces;

    @Before
    public void setUp() {
        playerPieces = new Hashtable<>();
        playerPieces.put('B', 'S');
        playerPieces.put('R', 'O');
    }

    // AC 1.1 Valid board size
    @Test
    public void testValidBoardCreation() {
        board = new Board(5);
        assertNotNull(board);
        assertEquals('B', board.getTurn());
    }

    // AC 1.2 Invalid size: too small
    @Test//(expected = NegativeArraySizeException.class)
    public void testInvalidBoardTooSmall() {
        board = new Board(2); // < 3 should not be allowed in GUI; backend throws if forced
        assertEquals(' ', board.getTurn());
    }

    // AC 1.2 Invalid size: too large
    @Test//(expected = NegativeArraySizeException.class)
    public void testInvalidBoardTooLarge() {
        board = new Board(11); // > 10 should not be allowed in GUI
        assertEquals(' ', board.getTurn());
    }

    // AC 3.1 Start game initializes board
    @Test
    public void testEmptyBoardOnStart() {
        int size = 4;
        board = new Board(size);
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                assertEquals(0, board.getCell(size, r, c));
    }
    
    // AC 3.2: Cannot start game without selecting settings
    @Test
    public void testCannotStartGameWithoutSettings() {
    	board = new Board(0);
        // Game mode is not set
        assertEquals("", board.getGamemode());

        // Attempting a move should not succeed (size is invalid)
        board.makeMove(0, 0, 0, playerPieces); // invalid board size
        assertEquals(-1, board.getCell(0, 0, 0)); // should be invalid
        assertEquals(' ', board.getTurn());      // turn should empty
    }

    // AC 3.3 Game reset: new board clears old
    @Test
    public void testGameReset() {
        board = new Board(3);
        board.makeMove(3, 0, 0, playerPieces);
        assertEquals(1, board.getCell(3, 0, 0));
        board = new Board(3); // new board created
        assertEquals(0, board.getCell(3, 0, 0));
        assertEquals('B', board.getTurn());
    }
}

