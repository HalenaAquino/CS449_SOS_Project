package sprint_4.test;

import static org.junit.Assert.*;
import org.junit.Test;

import sprint_4.production.GeneralSOSGame;
import sprint_4.production.SOSGame;
import sprint_4.production.SimpleSOSGame;

public class TestGamemode {

    private SOSGame game;

    // AC 2.1 General game selected
    @Test
    public void testSetGeneralGameMode() {
    	game = new GeneralSOSGame(5);
        game.setGamemode("General");
        assertEquals("General", game.getGamemode());
    }

    // AC 2.2 Simple game selected
    @Test
    public void testSetSimpleGameMode() {
    	game = new SimpleSOSGame(5);
        game.setGamemode("Simple");
        assertEquals("Simple", game.getGamemode());
    }

    // Optional: ensure default mode is null or empty before selection
    @Test
    public void testDefaultGameMode() {
    	game = new SimpleSOSGame(5);
    	assertEquals("", game.getGamemode());
    }
}