package sprint_2;

import static org.junit.Assert.*;
import java.util.Dictionary;
import java.util.Hashtable;
import org.junit.Before;
import org.junit.Test;

public class TestSMoves {

    private Board board;
    private Dictionary<Character, Character> playerPieces;

    @Before
    public void setUp() {
        board = new Board(3);
        playerPieces = new Hashtable<>();
        playerPieces.put('B', 'S');
        playerPieces.put('R', 'S');
    }

    // AC 4.1 / 6.1 Valid blue move
    @Test
    public void testValidBlueMoveS() {
        board.makeMove(3, 1, 1, playerPieces);
        assertEquals(1, board.getCell(3, 1, 1)); // Blue occupies cell
        assertEquals('S', board.getPieceType(3, 1, 1));
        assertEquals('R', board.getTurn()); // Turn switched
    }

    // AC 4.2 / 6.2 Illegal blue move (occupied cell)
    @Test
    public void testIllegalBlueMoveOnOccupiedCell() {
    	// Turn 1: Blue moves to (1,1)
        board.makeMove(3, 1, 1, playerPieces);
        assertEquals(1, board.getCell(3, 1, 1));
        assertEquals('S', board.getPieceType(3, 1, 1));

        // Turn 2: Red moves to (0,0)
        board.makeMove(3, 0, 0, playerPieces);
        assertEquals(2, board.getCell(3, 0, 0));
        assertEquals('S', board.getPieceType(3, 0, 0));

        // Turn 3: Blue tries to move again to (1,1) — illegal move
        board.makeMove(3, 0, 0, playerPieces);

        // Assertions: cell unchanged, turn remains Red
        assertEquals(2, board.getCell(3, 0, 0));      // still Red
        assertEquals('S', board.getPieceType(3, 0, 0));
        assertEquals('B', board.getTurn());          // turn should not switch
    }

    // AC 4.3 / 6.3 Illegal blue move (outside board)
    @Test
    public void testIllegalBlueMoveOutsideBoard() {
        board.makeMove(3, -1, 0, playerPieces);

        assertEquals(-1, board.getCell(3, -1, 0));        // invalid cell
        assertEquals('B', board.getTurn());               // turn stays Blue
    }

    // AC 4.4 / 6.4 Valid red move
    @Test
    public void testValidRedMoveS() {
        // Blue goes first
        board.makeMove(3, 1, 1, playerPieces);
        // Red now moves
        board.makeMove(3, 0, 0, playerPieces);
        assertEquals(2, board.getCell(3, 0, 0));
        assertEquals('S', board.getPieceType(3, 0, 0));
        assertEquals('B', board.getTurn());
    }

    // AC 4.5 / 6.5 Illegal red move (occupied)
    @Test
    public void testIllegalRedMoveOnOccupiedCell() {
    	// Turn 1: Blue moves (valid)
        board.makeMove(3, 1, 1, playerPieces);

        // Turn 2: Red tries to move on occupied cell (1,1)
        board.makeMove(3, 1, 1, playerPieces);

        assertEquals(1, board.getCell(3, 1, 1));          // cell unchanged
        assertEquals('S', board.getPieceType(3, 1, 1));   // piece unchanged
        assertEquals('R', board.getTurn());               // turn stays Red
    }

    // AC 4.6 / 6.6 Illegal red move (outside)
    @Test
    public void testIllegalRedMoveOutsideBoard() {
    	// Turn 1: Blue moves (valid)
        board.makeMove(3, 1, 1, playerPieces);


        // Turn 2: Red attempts move outside board
        board.makeMove(3, 3, 3, playerPieces);

        assertEquals(-1, board.getCell(3, 3, 3));         // invalid cell
        assertEquals('R', board.getTurn());               // turn stays Red
    }
}