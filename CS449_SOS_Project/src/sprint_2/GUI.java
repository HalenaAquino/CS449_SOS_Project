package sprint_2;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import java.util.Scanner;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.geometry.Pos;

import java.util.Dictionary;
import java.util.Hashtable;



public class GUI extends Application {
	
	//Scanner obj = new Scanner(System.in);

	private Square[][] squares;

	static private Board board;
	
	private Label gameStatus = new Label("Blue Players Turn");
	
	private char bluePiece = ' ';
	private char redPiece = ' ';

	@Override
	public void start(Stage primaryStage) {
		GridPane centerPane = new GridPane();		// actual board
		GridPane topPane = new GridPane();			// player settings (size, game, etc.)
		GridPane bottomPane = new GridPane();		// record checkbox, player turn
		
		
		GridPane blueControlPane = new GridPane();
		GridPane boardPane = new GridPane();
		boardPane.setPrefWidth(500);
		GridPane redControlPane = new GridPane();
		
		
		// Creates the game choice radio buttons and adds them to the top plane
		RadioButton simpleRButton = new RadioButton("Simple Game");
		RadioButton generalRButton = new RadioButton("General Game");
		ToggleGroup gameButtonGroup = new ToggleGroup();
		simpleRButton.setToggleGroup(gameButtonGroup);
		generalRButton.setToggleGroup(gameButtonGroup);
		generalRButton.setTranslateX(30);
		topPane.add(simpleRButton, 1, 5);
		topPane.add(generalRButton, 2, 5);
		
		
		// Creates a textbox for the board size and adds it to the top plane
		Label boardSize = new Label("Board Size:");
		TextField boardSizeField = new TextField ();
		HBox hb = new HBox();
		hb.getChildren().addAll(boardSize, boardSizeField);
		hb.setSpacing(5);
		hb.setTranslateX(120);
		topPane.add(hb,  3,  5);
		
		// Apply button for the textbox and adds it to pane
		Button applyButton = new Button("Apply");
		
		topPane.add(applyButton, 4, 5);
		applyButton.setTranslateX(120);
		
		// creates the errorMessage and gameStatus labels, adds them to the pane, and positions them correctly
		Label errorMessage = new Label("");
		
		bottomPane.add(gameStatus, 1, 5);
		bottomPane.add(errorMessage, 2, 5);
		
		gameStatus.setTranslateX(300);
		errorMessage.setTranslateX(-50);
		errorMessage.setTranslateY(-25);
		
		errorMessage.setTextFill(Color.RED);			// makes the errorMessage red and larger
		errorMessage.setFont(new Font("Arial", 15));
				
		
		
		// creates and positions the blue label
		Label bluePlayerLabel = new Label("Blue Player: ");
		bluePlayerLabel.setTranslateX(-25);
		bluePlayerLabel.setMinWidth(65);
		bluePlayerLabel.setTranslateY(200);
		
		// creates the blue buttons
		RadioButton blueSButton = new RadioButton("S");
		RadioButton blueOButton = new RadioButton("O");
		ToggleGroup bluePieceGroup = new ToggleGroup();
		blueSButton.setToggleGroup(bluePieceGroup);
		blueOButton.setToggleGroup(bluePieceGroup);

		
		Dictionary<Character, Character> playerSelectedPieces = new Hashtable<>();
		
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
		RadioButton redSButton = new RadioButton("S");
		RadioButton redOButton = new RadioButton("O");
		ToggleGroup redPieceGroup = new ToggleGroup();
		redSButton.setToggleGroup(redPieceGroup);
		redOButton.setToggleGroup(redPieceGroup);
		
		
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
		centerPane.setTranslateX(50);			// TODO: make board location dynamic (depending on its size)

		
		// Changes the size of the board based on the user entered number
		applyButton.setOnAction(event -> {			// TODO: add conditional to do nothing if the textbox is empty/is given a string
			int size =  Integer.parseInt(boardSizeField.getText());
			
			board = new Board(size);
			
			if(simpleRButton.isSelected()) 
				board.setGamemode("Simple");
			else if (generalRButton.isSelected())
				board.setGamemode("General");
			

			if (board.getTurn() != ' ' && board.getGamemode() != "" && bluePiece != ' ' && redPiece != ' ') {
				boardPane.getChildren().clear();
				squares = new Square[size][size];
				for (int i = 0; i < size; i++)
					for (int j = 0; j < size; j++)
						boardPane.add(squares[i][j] = new Square(size, i, j, playerSelectedPieces), j, i);
				
				
				errorMessage.setText("");
		    }
			else{
				errorMessage.setText("Please enter a valid board size, select a game mode, and choose the piece for both players");
			}});
			
		
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
	    for (int row = 0; row < size; row++)
	        for (int column = 0; column < size; column++) {
	            squares[row][column].getChildren().clear();
	            int cellValue = board.getCell(size, row, column);
	            char piece = board.getPieceType(size, row, column);
	            if (cellValue == 1)
	            	if(piece == 'S') 
	            		squares[row][column].drawS(Color.BLUE);
	            	else
	            		squares[row][column].drawO(Color.BLUE);
	            	
	            else if (cellValue == 2)
	            	if(piece == 'S') 
	            		squares[row][column].drawS(Color.RED);
	            	else
	            		squares[row][column].drawO(Color.RED);
	        }
	}
	
	public class Square extends Pane {
		
		private int row, column;
		
		public Square(int size, int row, int column, Dictionary<Character, Character> playerSelectedPieces) {
			this.row = row;
			this.column = column;
			setStyle("-fx-border-color: black");
			this.setPrefSize(500/size, 500/size);			// the max size of the board pane (500) / the number of squares
			this.setOnMouseClicked(e -> handleMouseClick(size, playerSelectedPieces));
		}

		private void handleMouseClick(int size, Dictionary<Character, Character> playerSelectedPieces) {
			board.makeMove(size, row, column, playerSelectedPieces);
			drawBoard(size, playerSelectedPieces);
			displayGameStatus();
		}
		
		public void drawS(Color c) {
			// TODO: make the size of S proportional to the square it's inside of
			Label label = new Label(String.valueOf('S'));
		    label.setTextFill(c);
		    label.setFont(new Font((this.getHeight()/2) + 10));
		    label.setAlignment(Pos.CENTER);
		    label.setPrefSize(this.getWidth(), this.getHeight());

		    this.getChildren().add(label);
		}
		
		public void drawO(Color c) {
			Ellipse ellipse = new Ellipse(this.getWidth() / 2, this.getHeight() / 2, this.getWidth() / 2 - 10,
					this.getHeight() / 2 - 10);
			ellipse.centerXProperty().bind(this.widthProperty().divide(2));
			ellipse.centerYProperty().bind(this.heightProperty().divide(2));
			ellipse.radiusXProperty().bind(this.widthProperty().divide(2).subtract(10));
			ellipse.radiusYProperty().bind(this.heightProperty().divide(2).subtract(10));
			ellipse.setStroke(c);
			ellipse.setStrokeWidth(this.getHeight()/25);
			ellipse.setFill(Color.TRANSPARENT);

			getChildren().add(ellipse);
		}
		
		private void displayGameStatus() {
			if (board.getTurn() == 'B') {
				gameStatus.setText("Blue Players Turn");
			} else {
				gameStatus.setText("Red Players Turn");
			}
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
