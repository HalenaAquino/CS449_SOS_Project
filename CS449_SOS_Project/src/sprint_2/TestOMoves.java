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
        board.makeMove(3, 1, 1, playerPieces);
        board.makeMove(3, 1, 1, playerPieces);
        assertEquals('R', board.getTurn());
        assertEquals('O', board.getPieceType(3, 1, 1));
    }

    @Test
    public void testInvalidBlueMoveOutsideBoard() {
        board.makeMove(3, -1, 0, playerPieces);
        assertEquals('B', board.getTurn());
        board.makeMove(3, 0, 3, playerPieces);
        assertEquals('B', board.getTurn());
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
        board.makeMove(3, 1, 1, playerPieces);
        board.makeMove(3, 1, 1, playerPieces);
        assertEquals('R', board.getTurn());
    }

    @Test
    public void testInvalidRedMoveOutsideBoard() {
        board.makeMove(3, 1, 1, playerPieces);
        board.makeMove(3, 3, 3, playerPieces);
        assertEquals('R', board.getTurn());
    }
}
