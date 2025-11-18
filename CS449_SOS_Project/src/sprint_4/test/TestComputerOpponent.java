package sprint_4.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

import sprint_4.production.*;
import sprint_4.production.SOSGame.Cell;
import sprint_4.production.SOSGame.GameState;

public class TestComputerOpponent {
    
    private SOSGame simpleGame;
    private SOSGame generalGame;
    private Map<Character, Character> playerPieces;
    
    @Before
    public void setUp() {
        playerPieces = new HashMap<>();
        playerPieces.put('B', 'S');
        playerPieces.put('R', 'O');
    }
    
    // AC 8.1 - 1 computer opponent selected (blue)
    @Test
    public void testOneComputerPlayerBlueSelected() {
        simpleGame = new SimpleSOSGame(3);
        
        Player bluePlayer = new ComputerPlayer(simpleGame, 'B', playerPieces);
        Player redPlayer = new Player(simpleGame, 'R', playerPieces);
        
        assertNotNull(bluePlayer);
        assertTrue(bluePlayer instanceof ComputerPlayer);
        assertFalse(redPlayer instanceof ComputerPlayer);
    }
    
    // AC 8.1 - 1 computer opponent selected (red)
    @Test
    public void testOneComputerPlayerRedSelected() {
        generalGame = new GeneralSOSGame(3);
        
        Player bluePlayer = new Player(generalGame, 'B', playerPieces);
        Player redPlayer = new ComputerPlayer(generalGame, 'R', playerPieces);
        
        assertTrue(redPlayer instanceof ComputerPlayer);
        assertFalse(bluePlayer instanceof ComputerPlayer);
    }
    
    // AC 8.1 - 2 computer opponents selected
    @Test
    public void testTwoComputerPlayersSelected() {
        generalGame = new GeneralSOSGame(4);
        
        Player bluePlayer = new ComputerPlayer(generalGame, 'B', playerPieces);
        Player redPlayer = new ComputerPlayer(generalGame, 'R', playerPieces);
        
        assertNotNull(bluePlayer);
        assertNotNull(redPlayer);
        assertTrue(bluePlayer instanceof ComputerPlayer);
        assertTrue(redPlayer instanceof ComputerPlayer);
    }
    
    
    // AC 8.2 - 2 humans selected
    @Test
    public void testHumanvHumanSelected() {
        simpleGame = new SimpleSOSGame(3);
        
        Player bluePlayer = new Player(simpleGame, 'B', playerPieces);
        Player redPlayer = new Player(simpleGame, 'R', playerPieces);
        
        assertNotNull(bluePlayer);
        assertNotNull(redPlayer);
        assertFalse(bluePlayer instanceof ComputerPlayer);
        assertFalse(redPlayer instanceof ComputerPlayer);
    }
    
    
    // AC 9.1 - computer move is triggered
    @Test
    public void testComputerMoveTriggered() {
        simpleGame = new SimpleSOSGame(3);
        ComputerPlayer computerPlayer = new ComputerPlayer(simpleGame, 'B', playerPieces);
        assertEquals('B', simpleGame.getTurn());
        
        int emptyBefore = simpleGame.getNumberOfEmptyCells();
        computerPlayer.makeMove(0, 0);
        
        int emptyAfter =  simpleGame.getNumberOfEmptyCells();
        assertTrue(emptyAfter < emptyBefore);
    }
    
    // AC 9.1 - computer move triggered after human move
    @Test
    public void testComputerMoveAfterHumanMove() {
        simpleGame = new SimpleSOSGame(3);
        Player humanPlayer = new Player(simpleGame, 'B', playerPieces);
        ComputerPlayer computerPlayer = new ComputerPlayer(simpleGame, 'R', playerPieces);
        
        humanPlayer.makeMove(0, 0);
        int emptyAfterHuman = simpleGame.getNumberOfEmptyCells();
        
        computerPlayer.makeMove(0, 0);
        assertTrue(simpleGame.getNumberOfEmptyCells() < emptyAfterHuman);
    }
    
    
    // AC 9.2 - computer chooses empty cell
    @Test
    public void testComputerChoosesEmptyCell() {
        simpleGame = new SimpleSOSGame(3);
        ComputerPlayer computerPlayer = new ComputerPlayer(simpleGame, 'B', playerPieces);
        
        computerPlayer.makeMove(0, 0);
        
        boolean foundMove = false;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (simpleGame.getCell(r, c) != Cell.EMPTY) {
                    foundMove = true;
                    break;
                }
            }
        }
        assertTrue(foundMove);
    }
    
    // AC 9.2 - computer never chooses occupied cell
    @Test
    public void testComputerNeverChoosesOccupiedCell() {
        generalGame = new GeneralSOSGame(4);
        ComputerPlayer blueComputer = new ComputerPlayer(generalGame, 'B', playerPieces);
        ComputerPlayer redComputer = new ComputerPlayer(generalGame, 'R', playerPieces);
        
        for (int i = 0; i < 10; i++) {
            if (generalGame.getGameState() != GameState.PLAYING) break;
            
            char currentTurn = generalGame.getTurn();
            if (currentTurn == 'B') {
                blueComputer.makeMove(0, 0);
            } else {
                redComputer.makeMove(0, 0);
            }
        }
        
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (generalGame.getCell(r, c) != Cell.EMPTY) {
                    char piece = generalGame.getPieceType(r, c);
                    assertTrue(piece == 'S' || piece == 'O');
                }
            }
        }
    }
    
    
    // AC 9.3 - computer move alternates turn (simple)
    @Test
    public void testTurnChangesAfterComputerMoveSimple() {
        simpleGame = new SimpleSOSGame(3);
        ComputerPlayer computerPlayer = new ComputerPlayer(simpleGame, 'B', playerPieces);
        assertEquals('B', simpleGame.getTurn());
        
        computerPlayer.makeMove(0, 0);
        
        if (simpleGame.getGameState() == GameState.PLAYING)
            assertEquals('R', simpleGame.getTurn());
    }
    
    // AC 9.3 - computer move alternates turn (general)
    @Test
    public void testTurnChangesAfterComputerMoveGeneral() {
        generalGame = new GeneralSOSGame(5);
        ComputerPlayer computerPlayer = new ComputerPlayer(generalGame, 'B', playerPieces);
        
        computerPlayer.makeMove(0, 0);
        int blueScore = generalGame.getBlueScore();
        
        if (blueScore == 0 && generalGame.getGameState() == GameState.PLAYING)
            assertEquals('R', generalGame.getTurn());
    }
    
    
    // AC 10.1 - random move mode
    @Test
    public void testComputerMakesRandomMove() {
        simpleGame = new SimpleSOSGame(3);
        ComputerPlayer computerPlayer = new ComputerPlayer(simpleGame, 'B', playerPieces);
        
        int emptyBefore = simpleGame.getNumberOfEmptyCells();
        computerPlayer.makeAutoMove();
        int emptyAfter = simpleGame.getNumberOfEmptyCells();
        
        assertEquals(emptyBefore - 1, emptyAfter);
    }
    
    // AC 10.1 - random moves vary
    @Test
    public void testRandomMovesDiffer() {
        boolean foundDifferentMoves = false;
        int firstMoveRow = -1;
        int firstMoveCol = -1;
        
        for (int trial = 0; trial < 20; trial++) {
            SOSGame game = new SimpleSOSGame(5);
            ComputerPlayer computer = new ComputerPlayer(game, 'B', playerPieces);
            
            computer.makeMove(0, 0);
            
            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 5; c++) {
                    if (game.getCell(r, c) != Cell.EMPTY) {
                        if (firstMoveRow == -1) {
                            firstMoveRow = r;
                            firstMoveCol = c;
                        } else if (r != firstMoveRow || c != firstMoveCol) {
                            foundDifferentMoves = true;
                            break;
                        }
                    }
                }
                if (foundDifferentMoves) break;
            }
            if (foundDifferentMoves) break;
        }
        
        assertTrue(foundDifferentMoves);
    }
    
    
    // AC 10.2 - strategic move mode (horizontal)
    @Test
    public void testComputerCompletesHorizontalSOS() {
        generalGame = new GeneralSOSGame(3);
        ComputerPlayer computerPlayer = new ComputerPlayer(generalGame, 'B', playerPieces);
        
        generalGame.setCell(0, 0, Cell.RED);
        generalGame.setPieceType(0, 0, 'S');
        generalGame.setCell(0, 2, Cell.RED);
        generalGame.setPieceType(0, 2, 'S');
        generalGame.setTurn('B');
        
        playerPieces.put('B', 'O');
        computerPlayer.makeAutoMove();
        
        assertEquals('O', generalGame.getPieceType(0, 1));
        assertEquals(Cell.BLUE, generalGame.getCell(0, 1));
    }
    
    // AC 10.2 - strategic move mode (vertical)
    @Test
    public void testComputerCompletesVerticalSOS() {
        generalGame = new GeneralSOSGame(3);
        ComputerPlayer computerPlayer = new ComputerPlayer(generalGame, 'B', playerPieces);
        
        generalGame.setCell(0, 1, Cell.RED);
        generalGame.setPieceType(0, 1, 'S');
        generalGame.setCell(2, 1, Cell.RED);
        generalGame.setPieceType(2, 1, 'S');
        generalGame.setTurn('B');
        
        playerPieces.put('B', 'O');
        computerPlayer.makeAutoMove();
        
        assertEquals('O', generalGame.getPieceType(1, 1));
        assertEquals(Cell.BLUE, generalGame.getCell(1, 1));
    }
    
    // AC 10.2 - strategic move mode (diagonal)
    @Test
    public void testComputerCompletesDiagonalSOS() {
        generalGame = new GeneralSOSGame(3);
        ComputerPlayer computerPlayer = new ComputerPlayer(generalGame, 'B', playerPieces);
        
        generalGame.setCell(0, 0, Cell.RED);
        generalGame.setPieceType(0, 0, 'S');
        generalGame.setCell(2, 2, Cell.RED);
        generalGame.setPieceType(2, 2, 'S');
        generalGame.setTurn('B');
        
        playerPieces.put('B', 'O');
        computerPlayer.makeAutoMove();
        
        assertEquals('O', generalGame.getPieceType(1, 1));
    }
    
    // AC 10.2 - strategic move with S piece
    @Test
    public void testComputerCompletesSOSWithS() {
        generalGame = new GeneralSOSGame(3);
        ComputerPlayer computerPlayer = new ComputerPlayer(generalGame, 'B', playerPieces);
        
        generalGame.setCell(0, 1, Cell.RED);
        generalGame.setPieceType(0, 1, 'O');
        generalGame.setCell(0, 2, Cell.RED);
        generalGame.setPieceType(0, 2, 'S');
        generalGame.setTurn('B');
        
        playerPieces.put('B', 'S');
        computerPlayer.makeAutoMove();
        
        assertEquals('S', generalGame.getPieceType(0, 0));
        assertEquals(Cell.BLUE, generalGame.getCell(0, 0));
    }
    
    // AC 10.2 - computer keeps turn after SOS
    @Test
    public void testStillComputerTurnAfterSOS() {
        generalGame = new GeneralSOSGame(3);
        ComputerPlayer computerPlayer = new ComputerPlayer(generalGame, 'B', playerPieces);
        
        generalGame.setCell(0, 0, Cell.RED);
        generalGame.setPieceType(0, 0, 'S');
        generalGame.setCell(0, 2, Cell.RED);
        generalGame.setPieceType(0, 2, 'S');
        generalGame.setTurn('B');
        
        playerPieces.put('B', 'O');
        computerPlayer.makeAutoMove();
        
        if (generalGame.getGameState() == GameState.PLAYING)
            assertEquals('B', generalGame.getTurn());
    }
    
    
    // AC 11.1 - SOS detected and score increased
    @Test
    public void testComputerSOSIncreasesScore() {
        generalGame = new GeneralSOSGame(3);
        Player redPlayer = new Player(generalGame, 'R', playerPieces);
        ComputerPlayer computerPlayer = new ComputerPlayer(generalGame, 'B', playerPieces);
        
        generalGame.setTurn('R');
        playerPieces.put('R', 'S');
        redPlayer.makeMove(0, 0);
        
        generalGame.setTurn('R');
        playerPieces.put('R', 'S');
        redPlayer.makeMove(0, 2);
        
        generalGame.setTurn('B');
        int scoreBefore = generalGame.getBlueScore();
        
        playerPieces.put('B', 'O');
        computerPlayer.makeAutoMove();
        
        assertTrue(generalGame.getBlueScore() > scoreBefore);
    }
    
    // AC 11.1 - computer scores multiple SOS patterns
    @Test
    public void testComputerSOSMultiplePatterns() {
        generalGame = new GeneralSOSGame(3);
        ComputerPlayer computerPlayer = new ComputerPlayer(generalGame, 'B', playerPieces);
        
        generalGame.setCell(1, 0, Cell.RED);
        generalGame.setPieceType(1, 0, 'S');
        generalGame.setCell(1, 2, Cell.RED);
        generalGame.setPieceType(1, 2, 'S');
        generalGame.setCell(0, 1, Cell.RED);
        generalGame.setPieceType(0, 1, 'S');
        generalGame.setCell(2, 1, Cell.RED);
        generalGame.setPieceType(2, 1, 'S');
        generalGame.setTurn('B');
        
        playerPieces.put('B', 'O');
        computerPlayer.makeAutoMove();
        
        assertTrue(generalGame.getBlueScore() >= 2);
    }
    
    
    // AC 11.2 - same turn after SOS in general game
    @Test
    public void testComputerTurnAfterSOS() {
        generalGame = new GeneralSOSGame(4);
        ComputerPlayer computerPlayer = new ComputerPlayer(generalGame, 'B', playerPieces);
        
        generalGame.setCell(1, 0, Cell.RED);
        generalGame.setPieceType(1, 0, 'S');
        generalGame.setCell(1, 2, Cell.RED);
        generalGame.setPieceType(1, 2, 'S');
        generalGame.setTurn('B');
        
        playerPieces.put('B', 'O');
        computerPlayer.makeAutoMove();
        
        if (generalGame.getGameState() == GameState.PLAYING)
            assertEquals('B', generalGame.getTurn());
    }
    
    
    // AC 12.1 - computer win in simple game
    @Test
    public void testComputerWinsSimpleGame() { 
        simpleGame = new SimpleSOSGame(3);
        ComputerPlayer computerPlayer = new ComputerPlayer(simpleGame, 'B', playerPieces);
        
        simpleGame.setCell(0, 0, Cell.RED);
        simpleGame.setPieceType(0, 0, 'S');
        simpleGame.setCell(0, 2, Cell.RED);
        simpleGame.setPieceType(0, 2, 'S');
        simpleGame.setTurn('B');
        
        playerPieces.put('B', 'O');
        computerPlayer.makeAutoMove();
        
        assertEquals(GameState.BLUE_WON, simpleGame.getGameState());
    }
    
    // AC 12.1 - computer recognizes simple win
    @Test
    public void testComputerRecognizesSimpleWin() {
        simpleGame = new SimpleSOSGame(3);
        ComputerPlayer blueComputer = new ComputerPlayer(simpleGame, 'B', playerPieces);
        
        simpleGame.setCell(1, 0, Cell.RED);
        simpleGame.setPieceType(1, 0, 'S');
        simpleGame.setCell(1, 2, Cell.RED);
        simpleGame.setPieceType(1, 2, 'S');
        
        simpleGame.setTurn('B');
        playerPieces.put('B', 'O');
        blueComputer.makeAutoMove();
        
        assertEquals(GameState.BLUE_WON, simpleGame.getGameState());
    }
    
    
    // AC 12.2 - computer win in general game
    @Test
    public void testComputerWinsGeneralGame() {
        generalGame = new GeneralSOSGame(3);
        Player bluePlayer = new Player(generalGame, 'B', playerPieces);
        Player redPlayer = new Player(generalGame, 'R', playerPieces);
        
        generalGame.setTurn('B');
        playerPieces.put('B', 'S');
        bluePlayer.makeMove(0, 0);
        
        generalGame.setTurn('B');
        playerPieces.put('B', 'O');
        bluePlayer.makeMove(0, 1);
        
        generalGame.setTurn('B');
        playerPieces.put('B', 'S');
        bluePlayer.makeMove(0, 2);
        
        generalGame.setTurn('R');
        playerPieces.put('R', 'S');
        redPlayer.makeMove(1, 0);
        
        generalGame.setTurn('R');
        playerPieces.put('R', 'S');
        redPlayer.makeMove(1, 1);
        
        generalGame.setTurn('R');
        playerPieces.put('R', 'O');
        redPlayer.makeMove(1, 2);
        
        generalGame.setTurn('B');
        playerPieces.put('B', 'O');
        bluePlayer.makeMove(2, 0);
        
        generalGame.setTurn('B');
        playerPieces.put('B', 'S');
        bluePlayer.makeMove(2, 1);
        
        generalGame.setTurn('R');
        playerPieces.put('R', 'O');
        redPlayer.makeMove(2, 2);
        
        assertTrue(generalGame.getBlueScore() > generalGame.getRedScore());
        assertEquals(GameState.BLUE_WON, generalGame.getGameState());
    }
    
    // AC 12.3 - draw in simple game
    @Test
    public void testDrawInSimpleGame() {
        simpleGame = new SimpleSOSGame(3);
        ComputerPlayer blueComputer = new ComputerPlayer(simpleGame, 'B', playerPieces);
        
        simpleGame.setCell(0, 0, Cell.BLUE);
        simpleGame.setPieceType(0, 0, 'S');
        simpleGame.setCell(0, 1, Cell.RED);
        simpleGame.setPieceType(0, 1, 'S');
        simpleGame.setCell(0, 2, Cell.BLUE);
        simpleGame.setPieceType(0, 2, 'S');
        simpleGame.setCell(1, 0, Cell.RED);
        simpleGame.setPieceType(1, 0, 'S');
        simpleGame.setCell(1, 1, Cell.BLUE);
        simpleGame.setPieceType(1, 1, 'S');
        simpleGame.setCell(1, 2, Cell.RED);
        simpleGame.setPieceType(1, 2, 'S');
        simpleGame.setCell(2, 0, Cell.BLUE);
        simpleGame.setPieceType(2, 0, 'S');
        simpleGame.setCell(2, 1, Cell.RED);
        simpleGame.setPieceType(2, 1, 'S');
        
        simpleGame.setTurn('B');
        playerPieces.put('B', 'S');
        blueComputer.makeMove(0, 0);
        
        assertEquals(GameState.DRAW, simpleGame.getGameState());
    }
    
    // AC 12.3 - draw in general game
    @Test
    public void testDrawInGeneralGame() {
        generalGame = new GeneralSOSGame(3);
        Player bluePlayer = new Player(generalGame, 'B', playerPieces);
        Player redPlayer = new Player(generalGame, 'R', playerPieces);
        
        generalGame.setTurn('B');
        playerPieces.put('B', 'S');
        bluePlayer.makeMove(0, 0);
        
        generalGame.setTurn('B');
        playerPieces.put('B', 'O');
        bluePlayer.makeMove(0, 1);
        
        generalGame.setTurn('B');
        playerPieces.put('B', 'S');
        bluePlayer.makeMove(0, 2);
        
        generalGame.setTurn('R');
        playerPieces.put('R', 'S');
        redPlayer.makeMove(1, 0);
        
        generalGame.setTurn('R');
        playerPieces.put('R', 'O');
        redPlayer.makeMove(1, 1);
        
        generalGame.setTurn('R');
        playerPieces.put('R', 'S');
        redPlayer.makeMove(1, 2);
        
        generalGame.setTurn('B');
        playerPieces.put('B', 'O');
        bluePlayer.makeMove(2, 0);
        
        generalGame.setTurn('B');
        playerPieces.put('B', 'S');
        bluePlayer.makeMove(2, 1);
        
        generalGame.setTurn('R');
        playerPieces.put('R', 'O');
        redPlayer.makeMove(2, 2);
        
        assertEquals(generalGame.getBlueScore(), generalGame.getRedScore());
        assertEquals(GameState.DRAW, generalGame.getGameState());
    }
    
    // AC 12.3 - two computers can draw
    @Test
    public void testTwoComputersCanDraw() {
        generalGame = new GeneralSOSGame(3);
        
        generalGame.setCell(0, 0, Cell.BLUE);
        generalGame.setPieceType(0, 0, 'S');
        generalGame.updateGameState('B', 0, 0);
        generalGame.setCell(0, 2, Cell.RED);
        generalGame.setPieceType(0, 2, 'S');
        generalGame.updateGameState('R', 0, 2);
        generalGame.setCell(0, 1, Cell.BLUE);
        generalGame.setPieceType(0, 1, 'O');
        generalGame.updateGameState('B', 0, 1);
        
        generalGame.setCell(1, 1, Cell.BLUE);
        generalGame.setPieceType(1, 1, 'O');
        generalGame.updateGameState('B', 1, 1);
        generalGame.setCell(1, 0, Cell.RED);
        generalGame.setPieceType(1, 0, 'O');
        generalGame.updateGameState('R', 1, 0);
        generalGame.setCell(2, 0, Cell.BLUE);
        generalGame.setPieceType(2, 0, 'S');
        generalGame.updateGameState('B', 2, 0);
        
        generalGame.setCell(2, 1, Cell.BLUE);
        generalGame.setPieceType(2, 1, 'O');
        generalGame.updateGameState('B', 2, 1);
        generalGame.setCell(2, 2, Cell.RED);
        generalGame.setPieceType(2, 2, 'S');
        generalGame.updateGameState('R', 2, 2);
        generalGame.setCell(1, 2, Cell.RED);
        generalGame.setPieceType(1, 2, 'O');
        generalGame.updateGameState('R', 1, 2);
        
        assertEquals(generalGame.getBlueScore(), generalGame.getRedScore());
        assertEquals(GameState.DRAW, generalGame.getGameState());
    }
}