package sprint_4.production;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import sprint_4.production.SOSGame.GameState;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SOSGameGUI extends Application { 
	
	//   --------------------------------------------  VARIABLE DECLARATIONS  ---------------------------------------------------
	
	// general
	private Square[][] squares;
	private TextField boardSizeField;
	private GridPane boardPane;
	private Timeline autoPlayTimeline;
	
	// buttons
	private Button applyButton;
	private Button newGameButton;
	private RadioButton simpleRButton;
	private RadioButton generalRButton;
	private RadioButton blueSButton;
	private RadioButton blueOButton;
	private RadioButton redSButton;
	private RadioButton redOButton;
	private RadioButton blueHumanButton;
	private RadioButton blueComputerButton;
	private RadioButton redHumanButton;
	private RadioButton redComputerButton;
	
	// labels
	private Label gameStatus;
	private Label errorMessage;
	public Label blueScoreLabel;
	public Label redScoreLabel;
	
	// integers
	private int lastBlueScore = 0;
	private int lastRedScore = 0;
	
	// characters
	private char bluePiece = ' ';
	private char redPiece = ' ';
	public char bluePlayerType = ' ';
	public char redPlayerType = ' ';
	
	// data structures
	private List<SOSLine> completedSOS = new ArrayList<>();
	private Set<String> recordedSOS = new HashSet<>();
	private Map<Character, Character> playerSelectedPieces;
	
	// objects
	private SOSGame game;
	private Player bluePlayer;
	private Player redPlayer;
	
	// -----------------------------------------------------  GUI LOGIC  ----------------------------------------------------
	// helper class used to draw the line through SOS's
	private class SOSLine {
		int row, col;
		String direction;
		Color color;
		
		SOSLine(int row, int col, String direction, Color color) {
			this.row = row;
			this.col = col;
			this.direction = direction;
			this.color = color;
			}
		}

	@Override
	public void start(Stage primaryStage) {
		// Pane declaration
		GridPane centerPane = new GridPane();		// actual board and player pieces
		GridPane topPane = new GridPane();			// player settings (size, game, etc.)
		GridPane bottomPane = new GridPane();		// record checkbox, player turn
		
		// Creates the pane's for the stage
		createTopPane(topPane);
		createBottomPane(bottomPane);
		createCenterPane(centerPane);
		
		// Changes the size of the board based on the user entered number
		applyButton.setOnAction(event -> {	
			// throws an exception if the user enters an invalid size/type or doesn't select a gamemode/piece
			try {
				int size =  Integer.parseInt(boardSizeField.getText());
				
				// Sets the gamemode depending on which button was chosen, throws error if none chosen
				if(simpleRButton.isSelected()) {
					game = new SimpleSOSGame(size);
					game.setGamemode("Simple");
					blueScoreLabel.setText("");
					redScoreLabel.setText("");
				}
				else if (generalRButton.isSelected()) {
					game = new GeneralSOSGame(size);
					game.setGamemode("General");
					blueScoreLabel.setText("Blue score: 0");
					redScoreLabel.setText("Red score: 0");
				}
				else
					throw new NumberFormatException(); 
				
				// Sets the player type of both players depending on the buttons chosen; throws exception otherwise
				if(bluePlayerType == 'C' && redPlayerType == 'H') {
					bluePlayer = new ComputerPlayer(game, 'B', playerSelectedPieces);
					redPlayer = new Player(game, 'R', playerSelectedPieces);
				}
				else if (bluePlayerType == 'H' && redPlayerType == 'C') {
					bluePlayer = new Player(game, 'B', playerSelectedPieces);
					redPlayer = new ComputerPlayer(game, 'R', playerSelectedPieces);
				}
				else if (bluePlayerType == 'H' && redPlayerType == 'H') {
					bluePlayer = new Player(game, 'B', playerSelectedPieces);
					redPlayer = new Player(game, 'R', playerSelectedPieces);
				}
				else if (bluePlayerType == 'C' && redPlayerType == 'C') {
					bluePlayer = new ComputerPlayer(game, 'B', playerSelectedPieces);
					redPlayer = new ComputerPlayer(game, 'R', playerSelectedPieces);
				}
				else
					throw new NumberFormatException();
				
				// only creates the board if all settings are chosen
				if (game.getTurn() != ' ' && game.getGamemode() != "" && ((blueHumanButton.isSelected() && bluePiece != ' ') || (redHumanButton.isSelected() && redPiece != ' ') || (blueComputerButton.isSelected() && redComputerButton.isSelected()))) {
					// resets game settings
					game.resetGame();
					gameStatus.setText("Blue Players turn");
					boardPane.getChildren().clear();
					completedSOS.clear();
					recordedSOS.clear();
					lastBlueScore = 0;
					lastRedScore = 0;
					
					// creates the squares in the board
					squares = new Square[size][size];
					for (int i = 0; i < size; i++)
						for (int j = 0; j < size; j++)
							boardPane.add(squares[i][j] = new Square(size, i, j, playerSelectedPieces), j, i);
					
					// calls a function that plays the entire game with computer players
					if(bluePlayerType == 'C' && redPlayerType == 'C')
						doubleComputerPlayers(size);
					
					// Disables the setup during an active game
					simpleRButton.setDisable(true);
					generalRButton.setDisable(true);
					boardSizeField.setDisable(true);
					applyButton.setDisable(true);
					
					if (blueHumanButton.isSelected()) blueComputerButton.setDisable(true);
					if (blueComputerButton.isSelected()) blueHumanButton.setDisable(true);
					if (redHumanButton.isSelected()) redComputerButton.setDisable(true);
					if (redComputerButton.isSelected()) redHumanButton.setDisable(true);
					
					errorMessage.setText("");		// Sets the error message to empty if there's no error
			    }
				else
					throw new NumberFormatException();		// Throws an error if any of the setup condiitons aren't met
			}
			catch (NumberFormatException e){
				errorMessage.setText("Please enter a valid board size, select a game mode, and choose the piece for both players");
			}});
		
		// resets the entire interface when the user starts a new game
		newGameButton.setOnAction(event -> {
			
			stopAutoPlay();
			
			// settings that're exclusive for an active game won't try to reset if there's no ongoing game
			if (game != null) {
		        boardPane.getChildren().clear();
		        game.resetGame();
		        completedSOS.clear();
		        recordedSOS.clear();
		        lastBlueScore = 0;
		        lastRedScore = 0;
		        boardSizeField.setText("");
		        gameStatus.setText("Blue Players turn");
		    }
			
			
			// resets all of the button settings
			List<RadioButton> settingsRadioButtons = List.of(simpleRButton, generalRButton, blueSButton, blueOButton, 
					redSButton, redOButton, blueHumanButton, blueComputerButton, redHumanButton, redComputerButton);
			
			for(int i = 0; i < settingsRadioButtons.size(); i++) {
				settingsRadioButtons.get(i).setDisable(false);
				settingsRadioButtons.get(i).setSelected(false);
			}
			
			boardSizeField.setDisable(false);
			applyButton.setDisable(false);
			
			blueScoreLabel.setText("");
			redScoreLabel.setText("");
			gameStatus.setText("Blue Players Turn");
			
		});	
		
		// adds all of the panes to the border pane
		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(centerPane);
		borderPane.setBottom(bottomPane);
		borderPane.setTop(topPane);
				
		// Creates the scene with the border pane
		Scene scene = new Scene(borderPane, 700, 700);
		primaryStage.setTitle("SOS Game");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// draws the actual board
	public void drawBoard(int size, Map<Character, Character> playerSelectedPieces) {
	    for (int row = 0; row < size; row++) {
	        for (int column = 0; column < size; column++) {
	            squares[row][column].getChildren().clear();		// Clears anything pre-existing in the squares
	            char piece = game.getPieceType(row, column);
	            
	            // Places the piece of the current player
	            if (game.getCell(row, column) == SOSGame.Cell.BLUE)
	            	if(piece == 'S') squares[row][column].drawS(Color.BLUE);
	            	else squares[row][column].drawO(Color.BLUE);
	            	
	            else if (game.getCell(row, column) == SOSGame.Cell.RED)
	            	if(piece == 'S') squares[row][column].drawS(Color.RED);
	            	else squares[row][column].drawO(Color.RED);
	        }
	    }
	}
	
	// Stops the double computer game if it's running
	private void stopAutoPlay() {
		if (autoPlayTimeline != null) {
			autoPlayTimeline.stop();
			autoPlayTimeline = null;
		}
	}
	
	public void doubleComputerPlayers(int size) {
		stopAutoPlay();
		
	    autoPlayTimeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
	        if (game.getGameState() != GameState.PLAYING) 
	            return;
	        
	        char currentPlayer = game.getTurn();

	        if (currentPlayer == 'B') bluePlayer.makeMove(0, 0);
	        else redPlayer.makeMove(0, 0);

	        drawBoard(size, playerSelectedPieces);
	        highlightCompletedSOS(size, currentPlayer);
	        displayGameStatus();
	    }));

	    autoPlayTimeline.setCycleCount(Timeline.INDEFINITE);
	    autoPlayTimeline.play();
	}

			
	// responsible for determining if an SOS was made THIS turn and updating the set and list
	public void highlightCompletedSOS(int s, char player) {
		int currentBlueScore = game.getBlueScore();
		int currentRedScore = game.getRedScore();
			    
		// If a new SOS was just completed, finds and stores it
		if ((game.getGamemode() == "General" && (currentBlueScore > lastBlueScore || currentRedScore > lastRedScore))
				|| (game.getGamemode() == "Simple" && (game.getGameState() == GameState.BLUE_WON || game.getGameState() == GameState.RED_WON))){
			char[][] pieces = game.getPieceTypeArray();
			int size = s;
			        
			// Determine which player just scored
			Color color = (player == 'B') ? Color.BLUE : Color.RED;

			for (int r = 0; r < size; r++) {
				for (int c = 0; c < size; c++) {
			     
					// Horizontal
			        if (c <= size - 3 && pieces[r][c] == 'S' && pieces[r][c+1] == 'O' && pieces[r][c+2] == 'S') {
			        	String key = r + "," + c + ",H";
			        	if (!recordedSOS.contains(key)) {
			        		completedSOS.add(new SOSLine(r, c, "H", color));
			        		recordedSOS.add(key);
			        		}
			        	}
			                
			            // Vertical
			            if (r <= size - 3 && pieces[r][c] == 'S' && pieces[r+1][c] == 'O' && pieces[r+2][c] == 'S') {
			            	String key = r + "," + c + ",V";
			            	if (!recordedSOS.contains(key)) {
			            		completedSOS.add(new SOSLine(r, c, "V", color));
			            		recordedSOS.add(key);
			            		}
			            	}
			                
			            // Left diagonal \
			            if (r <= size - 3 && c <= size - 3 && pieces[r][c] == 'S' && pieces[r+1][c+1] == 'O' && pieces[r+2][c+2] == 'S') {
			            	String key = r + "," + c + ",LD";
			            	if (!recordedSOS.contains(key)) {
			            		completedSOS.add(new SOSLine(r, c, "LD", color));
			            		recordedSOS.add(key);
			            		}
			            	}

			            // Right diagonal /
			            if (r <= size - 3 && c >= 2 && pieces[r][c] == 'S' && pieces[r+1][c-1] == 'O' && pieces[r+2][c-2] == 'S') {
			            	String key = r + "," + c + ",RD";
			            	if (!recordedSOS.contains(key)) {
			            		completedSOS.add(new SOSLine(r, c, "RD", color));
			            		recordedSOS.add(key);
			            		}
			                }
			            }
			        }
			        
			// Update the stored scores
			lastBlueScore = currentBlueScore;
			lastRedScore = currentRedScore;
			}
			   
		// Always redraw ALL completed SOS lines
		for (SOSLine sos : completedSOS) {
			drawSOSLine(sos);
			}
		}
	
	
	// Taken from the TicTacToe example; changes the current turn
	public void displayGameStatus() {
		if (game.getGameState() == GameState.PLAYING) {
			if(game.getGamemode() == "General") {
				blueScoreLabel.setText("Blue score: " + game.getBlueScore());
				redScoreLabel.setText("Red score: " + game.getRedScore());
			}
			if (game.getTurn() == 'B') {
				gameStatus.setText("Blue Players Turn");
			}
			else {
				gameStatus.setText("Red Players Turn");
			}
			
		} else if (game.getGameState() == GameState.DRAW) {
			gameStatus.setText("It's a Draw! Click to play again.");
		} else if (game.getGameState() == GameState.BLUE_WON) {
			gameStatus.setText("Blue Won! Click to play again.");
		} else if (game.getGameState() == GameState.RED_WON) {
			gameStatus.setText("Red Won! Click to play again.");
		}
	}
			
	// draws the full SOS line
	private void drawSOSLine(SOSLine sos) {
		// determines which direction the SOS is (which direction the line needs to be drawn) and calls the function to draw each line
		switch(sos.direction) {
			case "H": // Horizontal
				squares[sos.row][sos.col].drawSlash("H", sos.color);
				squares[sos.row][sos.col+1].drawSlash("H", sos.color);
				squares[sos.row][sos.col+2].drawSlash("H", sos.color);
				break;
			case "V": // Vertical
				squares[sos.row][sos.col].drawSlash("V", sos.color);
				squares[sos.row+1][sos.col].drawSlash("V", sos.color);
				squares[sos.row+2][sos.col].drawSlash("V", sos.color);
				break;
			case "LD": // Left diagonal
				squares[sos.row][sos.col].drawSlash("LD", sos.color);
				squares[sos.row+1][sos.col+1].drawSlash("LD", sos.color);
				squares[sos.row+2][sos.col+2].drawSlash("LD", sos.color);
				break;
			case "RD": // Right diagonal
				squares[sos.row][sos.col].drawSlash("RD", sos.color);
				squares[sos.row+1][sos.col-1].drawSlash("RD", sos.color);
				squares[sos.row+2][sos.col-2].drawSlash("RD", sos.color);
				break;
				}
		}

	// creates all of the objects on the top pane
	private void createTopPane(GridPane topPane) {
		// Creates the game choice radio buttons and adds them to the top plane
		simpleRButton = new RadioButton("Simple Game");
		generalRButton = new RadioButton("General Game");
		ToggleGroup gameButtonGroup = new ToggleGroup();
		simpleRButton.setToggleGroup(gameButtonGroup);
		generalRButton.setToggleGroup(gameButtonGroup);
		generalRButton.setTranslateX(30);
		topPane.add(simpleRButton, 1, 5);
		topPane.add(generalRButton, 2, 5);
				
				
		// Creates a textbox for the board size and adds it to the top plane
		Label boardSize = new Label("Board Size:");
		boardSizeField = new TextField ();
		boardSizeField.setPromptText("2 < size < 10");
		HBox hb = new HBox();
		hb.getChildren().addAll(boardSize, boardSizeField);
		hb.setSpacing(5);
		hb.setTranslateX(120);
		topPane.add(hb,  3,  5);
				
		// Apply button for the textbox and adds it to pane
		applyButton = new Button("Apply");
		topPane.add(applyButton, 4, 5);
		applyButton.setTranslateX(120);
	}
	
	// creates all of the objects on the bottom pane
	private void createBottomPane(GridPane bottomPane) {
		// new game button
		newGameButton = new Button("New Game");
		bottomPane.add(newGameButton,  1,  5);
		newGameButton.setTranslateX(600);
		newGameButton.setTranslateY(-75);
		
		// game status label
		gameStatus= new Label("Blue Players Turn");
		bottomPane.add(gameStatus, 2, 5);
		gameStatus.setTranslateX(210);
		gameStatus.setFont(new Font("Arial", 15));
		
		// error message label
		errorMessage = new Label("");
		bottomPane.add(errorMessage, 3, 5);
		errorMessage.setTranslateX(-130);
		errorMessage.setTranslateY(-30);
		errorMessage.setTextFill(Color.RED);			// makes the errorMessage red and larger
		errorMessage.setFont(Font.font("Arial", FontWeight.BOLD, 14));		// makes game status bold and bigger

		// general pane settings
		bottomPane.setMinWidth(800);
		bottomPane.setMaxHeight(200);
	}
	
	// creates the items needed for each player
	private void createPlayerPane(GridPane playerPane, String playerName, char playerChar, Color playerColor, double offset) {
		playerSelectedPieces = new Hashtable<>();
		
		// creates score label
		Label scoreLabel = new Label("");
		
		// creates and positions the player label
		Label playerLabel = new Label(playerName + " Player: ");
		playerLabel.setTranslateX(offset);
		playerLabel.setMinWidth(65);
		playerLabel.setTranslateY(200);
		
		// creates the player type buttons
		RadioButton humanButton = new RadioButton("Human");
		RadioButton computerButton = new RadioButton("Computer");
		ToggleGroup typeGroup = new ToggleGroup();
		humanButton.setToggleGroup(typeGroup);
		computerButton.setToggleGroup(typeGroup);
		
		// creates the piece buttons
		RadioButton sButton = new RadioButton("S");
		RadioButton oButton = new RadioButton("O");
		ToggleGroup pieceGroup = new ToggleGroup();
		sButton.setToggleGroup(pieceGroup);
		oButton.setToggleGroup(pieceGroup);
		sButton.setDisable(true);
		oButton.setDisable(true);
		
		// sets the player piece to the the shape they chose (S/O)
		sButton.setOnAction(e -> {
			if (playerChar == 'B') bluePiece = 'S';
			else redPiece = 'S';
			playerSelectedPieces.put(playerChar, 'S');
		});
		
		oButton.setOnAction(e -> {
			if (playerChar == 'B') bluePiece = 'O';
			else redPiece = 'O';
			playerSelectedPieces.put(playerChar, 'O');
		});
		
		// sets the blue player type to the type they chose
		humanButton.setOnAction(e -> {
			sButton.setDisable(false);
			oButton.setDisable(false);
			
			if (playerChar == 'B') bluePlayerType = 'H';
			else redPlayerType = 'H';
		});
		
		computerButton.setOnAction(e -> {
			sButton.setDisable(true);
			oButton.setDisable(true);
			
			if (playerChar == 'B') {
				bluePlayerType = 'C';
				bluePiece = 'S';
				playerSelectedPieces.put('B', 'S');
			}
			else {
				redPlayerType = 'C';
				redPiece = 'O';
				playerSelectedPieces.put('R', 'O');
			}
		});
		
		// moves the blue buttons and labels
		humanButton.setTranslateY(playerLabel.getTranslateY() + 30);
		computerButton.setTranslateY(humanButton.getTranslateY() + 80);
		humanButton.setTranslateX(playerLabel.getTranslateX() + 15);
		computerButton.setTranslateX(humanButton.getTranslateX());
		
		sButton.setTranslateX(playerLabel.getTranslateX() - 25);
		sButton.setTranslateY(humanButton.getTranslateY() + 30);
		oButton.setTranslateX(sButton.getTranslateX() - 28);
		oButton.setTranslateY(sButton.getTranslateY() + 25);
		scoreLabel.setTranslateY(computerButton.getTranslateY() + 50);
		scoreLabel.setTranslateX(computerButton.getTranslateX() - 15);
		
		// adds everything to the pane
		playerPane.add(playerLabel, 1, 5);
		playerPane.add(sButton, 2, 5);
		playerPane.add(oButton, 3, 5);
		playerPane.add(humanButton, 1, 5);
		playerPane.add(computerButton, 1, 5);
		playerPane.add(scoreLabel, 1, 5);
		
		// Assign score label to appropriate class variable
		if (playerChar == 'B') {
			blueScoreLabel = scoreLabel;
			blueHumanButton = humanButton;
			blueComputerButton = computerButton;
			blueSButton = sButton;
			blueOButton = oButton;
		} 
		else {
			redScoreLabel = scoreLabel;
			redHumanButton = humanButton;
			redComputerButton = computerButton;
			redSButton = sButton;
			redOButton = oButton;
		}
	}
	
	// creates all of the objects in the center pane
	private void createCenterPane(GridPane centerPane) {
		// Creates panes for the red and blue player buttons
		GridPane blueControlPane = new GridPane();
		boardPane = new GridPane();
		boardPane.setPrefWidth(470);
		GridPane redControlPane = new GridPane();

		createPlayerPane(blueControlPane, "Blue", 'B', Color.BLUE, -35);
		createPlayerPane(redControlPane, "Red", 'R', Color.RED, boardPane.getMaxWidth() - 50);
		boardPane.setTranslateX(blueControlPane.getMaxWidth() - 60);
		
				
		// combines all of the center panes
		centerPane.add(blueControlPane, 1, 5);
		centerPane.add(boardPane, 2, 5);
		centerPane.add(redControlPane, 3, 5);
				
		// Adds each pane to the border pane
		boardPane.setTranslateY(30);
		centerPane.setTranslateX(50);
	}
	
	public class Square extends Pane {
		
		private int row, column;
		
		// Creates each square and handles piece placement (moves being made)
		public Square(int size, int row, int column, Map<Character, Character> playerSelectedPieces) {
			this.row = row;
			this.column = column;
			setStyle("-fx-border-color: black");
			this.setPrefSize(500/size, 500/size);			// the max size of the board pane (500) / the number of squares
			this.setOnMouseClicked(e -> handleMouseClick(size, playerSelectedPieces));
		}

		// Makes the actual move and updates the board
		private void handleMouseClick(int size, Map<Character, Character> playerSelectedPieces) {
			
			if (game.getGameState() != GameState.PLAYING) {
				game.resetGame();
				lastBlueScore = 0;
		        lastRedScore = 0;
				completedSOS.clear();
				recordedSOS.clear();
				
				if(game.getGamemode() == "General") {
					blueScoreLabel.setText("Blue score: 0");
					redScoreLabel.setText("Red score: 0");
				}
				
				drawBoard(size, playerSelectedPieces);
			    displayGameStatus();
				return;
			}

		    char currentPlayer = game.getTurn();
		    
		    // if the current player is a human, makes a move
		    if ((currentPlayer == 'B' && bluePlayerType == 'H') || (currentPlayer == 'R' && redPlayerType == 'H')) {
		        if (currentPlayer == 'B') 
		        	bluePlayer.makeMove(row, column);
		        else 
		        	redPlayer.makeMove(row, column);
		    }

		    drawBoard(size, playerSelectedPieces);
		    highlightCompletedSOS(size, currentPlayer);
		    displayGameStatus();
		    
		    // if the current player is a computer, makes a computer move
		    while (game.getGameState() == GameState.PLAYING && ((game.getTurn() == 'B' && bluePlayerType == 'C') || (game.getTurn() == 'R' && redPlayerType == 'C'))) {
		        char computerPlayer = game.getTurn();
		        
		        // makes the computer move
		        if (computerPlayer == 'B') 
		        	bluePlayer.makeMove(0, 0);
		        else 
		        	redPlayer.makeMove(0, 0);

		        drawBoard(size, playerSelectedPieces);
		        highlightCompletedSOS(size, computerPlayer);
		        displayGameStatus();
		    }
		}
		
		// draws the actual slash that goes through each box the SOS is contained by
		public void drawSlash(String direction, Color color) {
		    Line line = new Line();

		    // draws the same slash for any SOS line going each direction
		    switch(direction) { 
		        case "LD": // left diagonal (\)
		            line.startXProperty().bind(widthProperty().multiply(0.02));
		            line.startYProperty().bind(heightProperty().multiply(0.02));
		            line.endXProperty().bind(widthProperty().multiply(0.98));
		            line.endYProperty().bind(heightProperty().multiply(0.98));
		            break;

		        case "RD": // right diagonal (/)
		            line.startXProperty().bind(widthProperty().multiply(0.02));
		            line.startYProperty().bind(heightProperty().multiply(0.98));
		            line.endXProperty().bind(widthProperty().multiply(0.98));
		            line.endYProperty().bind(heightProperty().multiply(0.02));
		            break;

		        case "H": // Horizontal
		            line.startXProperty().bind(widthProperty().multiply(0.02));
		            line.startYProperty().bind(heightProperty().divide(2));
		            line.endXProperty().bind(widthProperty().multiply(0.98));
		            line.endYProperty().bind(heightProperty().divide(2));
		            break;

		        case "V": // Vertical
		            line.startXProperty().bind(widthProperty().divide(2));
		            line.startYProperty().bind(heightProperty().multiply(0.02)); 
		            line.endXProperty().bind(widthProperty().divide(2));
		            line.endYProperty().bind(heightProperty().multiply(0.98));
		            break;
		    }

		    line.setStroke(color);
		    line.setStrokeWidth(getHeight() / 25);
		    getChildren().add(line);
		}
		
		// Draws the S piece
		public void drawS(Color c) {
			Label label = new Label(String.valueOf('S'));
		    label.setTextFill(c);
		    label.setFont(new Font((this.getHeight()/1.5)));
		    label.setAlignment(Pos.CENTER);
		    label.setPrefSize(this.getWidth(), this.getHeight());
		    this.getChildren().add(label);
		}
		
		// Taken and altered from the drawNaught method in the TicTacToe example; draws the O piece
		public void drawO(Color c) {
			Ellipse ellipse = new Ellipse(this.getWidth() / 1.5, this.getHeight() / 1.5, this.getWidth() / 1.5,
					this.getHeight() / 1.5);
			ellipse.centerXProperty().bind(this.widthProperty().divide(2));
			ellipse.centerYProperty().bind(this.heightProperty().divide(2));
			ellipse.radiusXProperty().bind(this.widthProperty().divide(2.5).subtract(10));
			ellipse.radiusYProperty().bind(this.heightProperty().divide(2.5).subtract(10));
			ellipse.setStroke(c);
			ellipse.setStrokeWidth(this.getHeight()/25);
			ellipse.setFill(Color.TRANSPARENT);
			getChildren().add(ellipse);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}