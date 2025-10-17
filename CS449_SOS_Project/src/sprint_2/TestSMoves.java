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
        board.makeMove(3, 1, 1, playerPieces);
        assertEquals('R', board.getTurn());
        board.makeMove(3, 1, 1, playerPieces); // Red tries same cell
        assertEquals('S', board.getPieceType(3, 1, 1)); // no overwrite
        assertEquals('R', board.getTurn()); // turn stays same
    }

    // AC 4.3 / 6.3 Illegal blue move (outside board)
    @Test
    public void testIllegalMoveOutsideBoard() {
        board.makeMove(3, -1, 0, playerPieces); // invalid row
        assertEquals('B', board.getTurn());
        board.makeMove(3, 0, 3, playerPieces); // invalid col
        assertEquals('B', board.getTurn());
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
        board.makeMove(3, 1, 1, playerPieces);
        board.makeMove(3, 1, 1, playerPieces); // Red tries same cell
        assertEquals('R', board.getTurn()); // should remain red
    }

    // AC 4.6 / 6.6 Illegal red move (outside)
    @Test
    public void testIllegalRedMoveOutsideBoard() {
        board.makeMove(3, 1, 1, playerPieces); // Blue moves
        board.makeMove(3, 3, 3, playerPieces); // Red invalid
        assertEquals('R', board.getTurn()); // turn not changed
    }
}

