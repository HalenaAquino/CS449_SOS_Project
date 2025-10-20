package sprint_2.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import sprint_2.production.Board;

public class TestGamemode {

    private Board board;

    @Before
    public void setUp() {
        board = new Board(5); // size doesn't matter for this test
    }

    // AC 2.1 General game selected
    @Test
    public void testSetGeneralGameMode() {
        board.setGamemode("General");
        assertEquals("General", board.getGamemode());
    }

    // AC 2.2 Simple game selected
    @Test
    public void testSetSimpleGameMode() {
        board.setGamemode("Simple");
        assertEquals("Simple", board.getGamemode());
    }

    // Optional: ensure default mode is null or empty before selection
    @Test
    public void testDefaultGameMode() {
    	assertEquals("", board.getGamemode());
    }
}