package sprint_3;

import javafx.application.Application;
import javafx.stage.Stage;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.geometry.Pos;
import java.util.Dictionary;
import java.util.Hashtable;
import sprint_3.SOSGame.GameState;



public class GUI extends Application {
	
	// general variable declarations
	private Square[][] squares;
	
	private Label gameStatus = new Label("Blue Players Turn");
	private Button applyButton;
	private Button newGameButton;
	private Label errorMessage;
	private RadioButton simpleRButton;
	private RadioButton generalRButton;
	private TextField boardSizeField;
	private GridPane boardPane;
	private Dictionary<Character, Character> playerSelectedPieces;
	private SOSGame game;
	
	RadioButton blueSButton = new RadioButton("S");
	RadioButton blueOButton = new RadioButton("O");
	RadioButton redSButton = new RadioButton("S");
	RadioButton redOButton = new RadioButton("O");
	
	
	
	
	
	private int lastBlueScore = 0;
	private int lastRedScore = 0;
	private java.util.List<SOSLine> completedSOS = new java.util.ArrayList<>();
	private java.util.Set<String> recordedSOS = new java.util.HashSet<>();

	
	
	
	
	private char bluePiece = ' ';
	private char redPiece = ' ';
	
	
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
		GridPane centerPane = new GridPane();		// actual board
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

				
				// Sets the gamemode depending on which button was chosen
				if(simpleRButton.isSelected()) {
					game = new SimpleSOSGame(size);
					game.setGamemode("Simple");
				}
				else if (generalRButton.isSelected()) {
					game = new GeneralSOSGame(size);
					game.setGamemode("General");
				}
				else
					throw new NumberFormatException();
				
				if (game.getTurn() != ' ' && game.getGamemode() != "" && bluePiece != ' ' && redPiece != ' ') {
					// resets everything
					game.resetGame();
					gameStatus.setText("Blue player's turn");
					boardPane.getChildren().clear();
					completedSOS.clear();
					recordedSOS.clear();
					lastBlueScore = 0;
					lastRedScore = 0;
					
					squares = new Square[size][size];
					for (int i = 0; i < size; i++)
						for (int j = 0; j < size; j++)
							boardPane.add(squares[i][j] = new Square(size, i, j, playerSelectedPieces), j, i);
					
					
					// Disables the setup during an active game
					simpleRButton.setDisable(true);
					generalRButton.setDisable(true);
					boardSizeField.setDisable(true);
					applyButton.setDisable(true);
					
					errorMessage.setText("");		// Sets the error message to empty if there's no error
			    }
				else
					throw new NumberFormatException();		// Throws an error if any of the setup condiitons aren't met
			}
			catch (NumberFormatException e){
				errorMessage.setText("Please enter a valid board size, select a game mode, and choose the piece for both players");
			}});
		
		newGameButton.setOnAction(event -> {
			boardPane.getChildren().clear();
			game.resetGame();
			completedSOS.clear();
			recordedSOS.clear();
			lastBlueScore = 0;
			lastRedScore = 0;
			simpleRButton.setDisable(false);
			generalRButton.setDisable(false);
			boardSizeField.setDisable(false);
			applyButton.setDisable(false);
			
			generalRButton.setSelected(false);
			simpleRButton.setSelected(false);
			boardSizeField.clear();
		    blueSButton.setSelected(false);
		    blueOButton.setSelected(false);
		    redSButton.setSelected(false);
		    redOButton.setSelected(false);
			
		}
			);
			
		
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


	public void drawBoard(int size, Dictionary<Character, Character> playerSelectedPieces) {
	    for (int row = 0; row < size; row++) {
	        for (int column = 0; column < size; column++) {
	            squares[row][column].getChildren().clear();		// Clears anything pre-existing in the squares
	            //Cell cellValue = board.getCell(row, column);
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
	
	private void createBottomPane(GridPane bottomPane) {
		// creates the errorMessage label, adds it to the pane, and positions it correctly
		errorMessage = new Label("");
		
		newGameButton = new Button("New Game");
		bottomPane.add(newGameButton,  1,  5);
		newGameButton.setTranslateX(600);
		newGameButton.setTranslateY(-75);
		
				
		bottomPane.add(gameStatus, 2, 5);
		bottomPane.add(errorMessage, 3, 5);
		
				
		gameStatus.setTranslateX(230);
		errorMessage.setTranslateX(-130);
		errorMessage.setTranslateY(-30);
		bottomPane.setMinWidth(800);
		bottomPane.setMaxHeight(200);
				
		errorMessage.setTextFill(Color.RED);			// makes the errorMessage red and larger
		errorMessage.setFont(new Font("Arial", 15));
	}
	
	private void createCenterPane(GridPane centerPane) {
		// Creates panes for the red and blue player buttons
		GridPane blueControlPane = new GridPane();
		boardPane = new GridPane();
		boardPane.setPrefWidth(470);
		GridPane redControlPane = new GridPane();
		
		// creates and positions the blue label
		Label bluePlayerLabel = new Label("Blue Player: ");
		bluePlayerLabel.setTranslateX(-25);
		bluePlayerLabel.setMinWidth(65);
		bluePlayerLabel.setTranslateY(200);
				
		// creates the blue buttons
		ToggleGroup bluePieceGroup = new ToggleGroup();
		blueSButton.setToggleGroup(bluePieceGroup);
		blueOButton.setToggleGroup(bluePieceGroup);

				
		playerSelectedPieces = new Hashtable<>();
		
		// Sets the blue piece to the the shape they chose (S/O)
		blueSButton.setOnAction(e -> {
			bluePiece = 'S';
			playerSelectedPieces.put('B', bluePiece);
			});
		
		blueOButton.setOnAction(e -> {
			bluePiece = 'O';
			playerSelectedPieces.put('B', bluePiece);
			});
				
		// moves the blue buttons and labels
		blueSButton.setTranslateX(bluePlayerLabel.getTranslateX() - 40);
		blueSButton.setTranslateY(bluePlayerLabel.getTranslateY() + 30);
		blueOButton.setTranslateX(blueSButton.getTranslateX() - 28);
		blueOButton.setTranslateY(blueSButton.getTranslateY() + 25);
				
		// positions the board
		boardPane.setTranslateX(bluePlayerLabel.getMaxWidth() - 60);
				

		// creates and positions the red label
		Label redPlayerLabel = new Label("Red Player: ");
		redPlayerLabel.setTranslateX(boardPane.getMaxWidth() - 40);
		redPlayerLabel.setMinWidth(65);
		redPlayerLabel.setTranslateY(200);
				
		// creates the red buttons
		ToggleGroup redPieceGroup = new ToggleGroup();
		redSButton.setToggleGroup(redPieceGroup);
		redOButton.setToggleGroup(redPieceGroup);
				
		// Sets the red piece to the the shape they chose (S/O)
		redSButton.setOnAction(e -> {
			redPiece = 'S';
			playerSelectedPieces.put('R', redPiece);
			});

		redOButton.setOnAction(e -> {
			redPiece = 'O';
			playerSelectedPieces.put('R', redPiece);
			});
		                
		// moves the red buttons and labels
		redSButton.setTranslateX(redPlayerLabel.getTranslateX() - 40);
		redSButton.setTranslateY(redPlayerLabel.getTranslateY() + 30);
		redOButton.setTranslateX(redSButton.getTranslateX() - 28);
		redOButton.setTranslateY(redSButton.getTranslateY() + 25);
				
		// adds blue player buttons
		blueControlPane.add(bluePlayerLabel, 1, 5);
		blueControlPane.add(blueSButton, 2, 5);
		blueControlPane.add(blueOButton, 3, 5);
				
		// adds red player buttons
		redControlPane.add(redPlayerLabel, 1, 5);
		redControlPane.add(redSButton, 2, 5);
		redControlPane.add(redOButton, 3, 5);
				
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
		public Square(int size, int row, int column, Dictionary<Character, Character> playerSelectedPieces) {
			this.row = row;
			this.column = column;
			setStyle("-fx-border-color: black");
			this.setPrefSize(500/size, 500/size);			// the max size of the board pane (500) / the number of squares
			this.setOnMouseClicked(e -> handleMouseClick(size, playerSelectedPieces));
		}

		// Makes the actual move and updates the board
		private void handleMouseClick(int size, Dictionary<Character, Character> playerSelectedPieces) {
			if (game.getGameState() == GameState.PLAYING)
				game.makeMove(row, column, playerSelectedPieces);
			else
				game.resetGame();
			drawBoard(size, playerSelectedPieces);
			highlightCompletedSOS();
			displayGameStatus();
		}
		
		
		private void highlightCompletedSOS() {
		    int currentBlueScore = game.getBlueScore();
		    int currentRedScore = game.getRedScore();
		    
		    // If a new SOS was just completed, finds and stores it
		    if (currentBlueScore > lastBlueScore || currentRedScore > lastRedScore) {
		        char[][] pieces = game.getPieceTypeArray();
		        int size = SOSGame.SIZE;
		        
		        // Determine which player just scored
		        Color color = (game.getTurn() == 'B') ? Color.RED : Color.BLUE;

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

		private void drawSOSLine(SOSLine sos) {
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
		
		public void drawSlash(String direction, Color color) {
		    Line line = new Line();

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
		



		
		// Taken from the TicTacToe example; changes the current turn
		private void displayGameStatus() {
			if (game.getGameState() == GameState.PLAYING) {
				if (game.getTurn() == 'B')
					gameStatus.setText("Blue Players Turn");
				else
					gameStatus.setText("Red Players Turn");
			} else if (game.getGameState() == GameState.DRAW) {
				gameStatus.setText("It's a Draw! Click to play again.");
			} else if (game.getGameState() == GameState.BLUE_WON) {
				gameStatus.setText("Blue Won! Click to play again.");
			} else if (game.getGameState() == GameState.RED_WON) {
				gameStatus.setText("Red Won! Click to play again.");
			}
			//if (board.getTurn() == 'B') gameStatus.setText("Blue Players Turn");
			//else gameStatus.setText("Red Players Turn");
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}