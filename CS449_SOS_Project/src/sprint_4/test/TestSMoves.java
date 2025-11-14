package sprint_4.test;

import static org.junit.Assert.*;
import java.util.Map;import java.util.Hashtable;
import org.junit.Before;
import org.junit.Test;

import sprint_4.production.GeneralSOSGame;
import sprint_4.production.SOSGame;
import sprint_4.production.Player;

public class TestSMoves {

    private SOSGame game;
    private Player red, blue;
    private Map<Character, Character> playerPieces;

    @Before
    public void setUp() {
        game = new GeneralSOSGame(3);
        playerPieces = new Hashtable<>();
        playerPieces.put('B', 'S');
        playerPieces.put('R', 'S');
        blue = new Player(game, 'B');
        red = new Player(game, 'R');
    }

    // AC 4.1 / 6.1 Valid blue move
    @Test
    public void testValidBlueMoveS() {
        blue.makeMove(1, 1, playerPieces);
        assertEquals(SOSGame.Cell.BLUE, game.getCell(1, 1)); // Blue occupies cell
        assertEquals('S', game.getPieceType(1, 1));
        assertEquals('R', game.getTurn()); // Turn switched
    }

    // AC 4.2 / 6.2 Illegal blue move (occupied cell)
    @Test
    public void testIllegalBlueMoveOnOccupiedCell() {
    	// Turn 1: Blue moves to (1,1)
        blue.makeMove(1, 1, playerPieces);
        assertEquals(SOSGame.Cell.BLUE, game.getCell(1, 1));
        assertEquals('S', game.getPieceType(1, 1));

        // Turn 2: Red moves to (0,0)
        red.makeMove(0, 0, playerPieces);
        assertEquals(SOSGame.Cell.RED, game.getCell(0, 0));
        assertEquals('S', game.getPieceType(0, 0));

        // Turn 3: Blue tries to move again to (1,1) — illegal move
        blue.makeMove(0, 0, playerPieces);

        // Assertions: cell unchanged, turn remains Red
        assertEquals(SOSGame.Cell.RED, game.getCell(0, 0));      // still Red
        assertEquals('S', game.getPieceType(0, 0));
        assertEquals('B', game.getTurn());          // turn should not switch
    }

    // AC 4.3 / 6.3 Illegal blue move (outside board)
    @Test
    public void testIllegalBlueMoveOutsideBoard() {
        blue.makeMove(-1, 0, playerPieces);

        assertEquals(null, game.getCell(-1, 0));        // invalid cell
        assertEquals('B', game.getTurn());               // turn stays Blue
    }

    // AC 4.4 / 6.4 Valid red move
    @Test
    public void testValidRedMoveS() {
        // Blue goes first
        blue.makeMove(1, 1, playerPieces);
        // Red now moves
        red.makeMove(0, 0, playerPieces);
        assertEquals(SOSGame.Cell.RED, game.getCell(0, 0));
        assertEquals('S', game.getPieceType(0, 0));
        assertEquals('B', game.getTurn());
    }

    // AC 4.5 / 6.5 Illegal red move (occupied)
    @Test
    public void testIllegalRedMoveOnOccupiedCell() {
    	// Turn 1: Blue moves (valid)
        blue.makeMove(1, 1, playerPieces);

        // Turn 2: Red tries to move on occupied cell (1,1)
        red.makeMove(1, 1, playerPieces);

        assertEquals(SOSGame.Cell.BLUE, game.getCell(1, 1));          // cell unchanged
        assertEquals('S', game.getPieceType(1, 1));   // piece unchanged
        assertEquals('R', game.getTurn());               // turn stays Red
    }

    // AC 4.6 / 6.6 Illegal red move (outside)
    @Test
    public void testIllegalRedMoveOutsideBoard() {
    	// Turn 1: Blue moves (valid)
        blue.makeMove(1, 1, playerPieces);


        // Turn 2: Red attempts move outside board
        red.makeMove(3, 3, playerPieces);

        assertEquals(null, game.getCell(3, 3));         // invalid cell
        assertEquals('R', game.getTurn());               // turn stays Red
    }
}