package sprint_4.test;

import static org.junit.Assert.*;
import java.util.Map;import java.util.Hashtable;
import org.junit.Before;
import org.junit.Test;

import sprint_4.production.GeneralSOSGame;
import sprint_4.production.Player;
import sprint_4.production.SOSGame;

public class TestOMoves {
 
    private SOSGame game;
    private Player red, blue;
    private Map<Character, Character> playerPieces;

    @Before
    public void setUp() {
        game = new GeneralSOSGame(3);
        playerPieces = new Hashtable<>();
        playerPieces.put('B', 'O');
        playerPieces.put('R', 'O');
        blue = new Player(game, 'B', playerPieces);
        red = new Player(game, 'R', playerPieces);
    }

    @Test
    public void testValidBlueMoveO() {
        blue.makeMove(1, 1);
        assertEquals(SOSGame.Cell.BLUE, game.getCell(1, 1));
        assertEquals('O', game.getPieceType(1, 1));
        assertEquals('R', game.getTurn());
    }

    @Test
    public void testInvalidBlueMoveOccupied() {
    	// Turn 1: Blue moves (valid)
        blue.makeMove(1, 1);

        // Turn 2: Red moves (valid)
        red.makeMove(0, 0);

        // Turn 3: Blue tries to move on occupied cell (1,1)
        blue.makeMove(0, 0);

        assertEquals(SOSGame.Cell.RED, game.getCell(0, 0));          // cell unchanged
        assertEquals('O', game.getPieceType(1, 1));   // piece unchanged
        assertEquals('B', game.getTurn());               // turn stays Blue
    }

    @Test
    public void testInvalidBlueMoveOutsideBoard() {
        blue.makeMove(-1, 0);

        assertEquals(null, game.getCell(-1, 0));        // invalid cell
        assertEquals('B', game.getTurn());               // turn stays Blue
    }

    @Test
    public void testValidRedMoveO() {
        blue.makeMove(0, 0); // Blue
        red.makeMove(2, 2); // Red
        assertEquals(SOSGame.Cell.RED, game.getCell(2, 2));
        assertEquals('O', game.getPieceType(2, 2));
        assertEquals('B', game.getTurn());
    }

    @Test
    public void testInvalidRedMoveOccupied() {
    	// Turn 1: Blue moves (valid)
        blue.makeMove(1, 1);

        // Turn 2: Red moves (invalid)
        red.makeMove(1, 1);


        assertEquals(SOSGame.Cell.BLUE, game.getCell(1, 1));          // cell unchanged
        assertEquals('O', game.getPieceType(1, 1));   // piece unchanged
        assertEquals('R', game.getTurn());               // turn stays Red
    }

    @Test
    public void testInvalidRedMoveOutsideBoard() {
    	// Turn 1: Blue moves (valid)
        blue.makeMove(1, 1);


        // Turn 2: Red attempts move outside board
        red.makeMove(3, 3);

        assertEquals(null, game.getCell(3, 3));         // invalid cell
        assertEquals('R', game.getTurn());               // turn stays Red
    }
}