package sprint_2;

import static org.junit.Assert.*;
import java.util.Dictionary;
import java.util.Hashtable;
import org.junit.Before;
import org.junit.Test;

public class TestOMoves {

    private Board board;
    private Dictionary<Character, Character> playerPieces;

    @Before
    public void setUp() {
        board = new Board(3);
        playerPieces = new Hashtable<>();
        playerPieces.put('B', 'O');
        playerPieces.put('R', 'O');
    }

    @Test
    public void testValidBlueMoveO() {
        board.makeMove(3, 1, 1, playerPieces);
        assertEquals(1, board.getCell(3, 1, 1));
        assertEquals('O', board.getPieceType(3, 1, 1));
        assertEquals('R', board.getTurn());
    }

    @Test
    public void testInvalidBlueMoveOccupied() {
    	// Turn 1: Blue moves (valid)
        board.makeMove(3, 1, 1, playerPieces);

        // Turn 2: Red moves (valid)
        board.makeMove(3, 0, 0, playerPieces);

        // Turn 3: Blue tries to move on occupied cell (1,1)
        board.makeMove(3, 0, 0, playerPieces);

        assertEquals(2, board.getCell(3, 0, 0));          // cell unchanged
        assertEquals('O', board.getPieceType(3, 1, 1));   // piece unchanged
        assertEquals('B', board.getTurn());               // turn stays Blue
    }

    @Test
    public void testInvalidBlueMoveOutsideBoard() {
        board.makeMove(3, -1, 0, playerPieces);

        assertEquals(-1, board.getCell(3, -1, 0));        // invalid cell
        assertEquals('B', board.getTurn());               // turn stays Blue
    }

    @Test
    public void testValidRedMoveO() {
        board.makeMove(3, 0, 0, playerPieces); // Blue
        board.makeMove(3, 2, 2, playerPieces); // Red
        assertEquals(2, board.getCell(3, 2, 2));
        assertEquals('O', board.getPieceType(3, 2, 2));
        assertEquals('B', board.getTurn());
    }

    @Test
    public void testInvalidRedMoveOccupied() {
    	// Turn 1: Blue moves (valid)
        board.makeMove(3, 1, 1, playerPieces);

        // Turn 2: Red moves (invalid)
        board.makeMove(3, 1, 1, playerPieces);


        assertEquals(1, board.getCell(3, 1, 1));          // cell unchanged
        assertEquals('O', board.getPieceType(3, 1, 1));   // piece unchanged
        assertEquals('R', board.getTurn());               // turn stays Red
    }

    @Test
    public void testInvalidRedMoveOutsideBoard() {
    	// Turn 1: Blue moves (valid)
        board.makeMove(3, 1, 1, playerPieces);


        // Turn 2: Red attempts move outside board
        board.makeMove(3, 3, 3, playerPieces);

        assertEquals(-1, board.getCell(3, 3, 3));         // invalid cell
        assertEquals('R', board.getTurn());               // turn stays Red
    }
}
