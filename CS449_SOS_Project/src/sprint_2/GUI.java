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



public class GUI extends Application {
	
	Scanner obj = new Scanner(System.in);

	private Square[][] squares;

	static private Board board;

	@Override
	public void start(Stage primaryStage) {
		if (board == null) {
			board = new Board();
		}
		GridPane centerPane = new GridPane();		// actual board
		GridPane topPane = new GridPane();			// player settings (size, game, etc.)
		GridPane bottomPane = new GridPane();		// record checkbox, player turn
		
		
		GridPane centerLPane = new GridPane();
		GridPane centerCPane = new GridPane();
		//centerCPane.setPrefHeight(500);
		centerCPane.setPrefWidth(500);
		GridPane centerRPane = new GridPane();
		
		
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
		TextField textField = new TextField ();
		HBox hb = new HBox();
		hb.getChildren().addAll(boardSize, textField);
		hb.setSpacing(5);
		hb.setTranslateX(120);
		topPane.add(hb,  3,  5);
		
		// Apply button for the textbox and adds it to pane
		Button button = new Button("Apply");
		
		topPane.add(button, 4, 5);
		button.setTranslateX(120);
		
		// creates the errorMessage and gameStatus labels, adds them to the pane, and positions them correctly
		Label errorMessage = new Label("");
		Label gameStatus = new Label("Blue Players Turn");   // TODO: make dynamic
		
		bottomPane.add(gameStatus, 1, 5);
		bottomPane.add(errorMessage, 2, 5);
		
		gameStatus.setTranslateX(300);
		errorMessage.setTranslateX(100);
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
		
		// moves the blue buttons and labels
		blueSButton.setTranslateX(bluePlayerLabel.getTranslateX() - 40);
		blueSButton.setTranslateY(bluePlayerLabel.getTranslateY() + 30);
		blueOButton.setTranslateX(blueSButton.getTranslateX() - 28);
		blueOButton.setTranslateY(blueSButton.getTranslateY() + 25);
		
		// positions the board
		centerCPane.setTranslateX(bluePlayerLabel.getMaxWidth() - 60);
		//centerCPane.setTranslateY(50000000);
		

		// creates and positions the red label
		Label redPlayerLabel = new Label("Red Player: ");
		redPlayerLabel.setTranslateX(centerCPane.getMaxWidth() - 40);
		redPlayerLabel.setMinWidth(65);
		redPlayerLabel.setTranslateY(200);
		
		// creates the red buttons
		RadioButton redSButton = new RadioButton("S");
		RadioButton redOButton = new RadioButton("O");
		ToggleGroup redPieceGroup = new ToggleGroup();
		redSButton.setToggleGroup(redPieceGroup);
		redOButton.setToggleGroup(redPieceGroup);
		
		// moves the red buttons and labels
		redSButton.setTranslateX(redPlayerLabel.getTranslateX() - 40);
		redSButton.setTranslateY(redPlayerLabel.getTranslateY() + 30);
		redOButton.setTranslateX(redSButton.getTranslateX() - 28);
		redOButton.setTranslateY(redSButton.getTranslateY() + 25);
		
		// adds blue player buttons
		centerLPane.add(bluePlayerLabel, 1, 5);
		centerLPane.add(blueSButton, 2, 5);
		centerLPane.add(blueOButton, 3, 5);
		
		// adds red player buttons
		centerRPane.add(redPlayerLabel, 1, 5);
		centerRPane.add(redSButton, 2, 5);
		centerRPane.add(redOButton, 3, 5);
		
		// combines all of the center panes
		centerPane.add(centerLPane, 1, 5);
		centerPane.add(centerCPane, 2, 5);
		centerPane.add(centerRPane, 3, 5);
		

		
		
		
		
		// Adds each pane to the border pane
		centerCPane.setTranslateY(30);
		centerPane.setTranslateX(50);			// TODO: make board location dynamic (depending on its size)

		
		// Changes the size of the board based on the user entered number
		button.setOnAction(event -> {			// TODO: add conditional to do nothing if the textbox is empty/is given a string
			int size =  Integer.parseInt(textField.getText());

			if (size > 0 && size < 10 && (simpleRButton.isSelected() || generalRButton.isSelected())) {
				centerCPane.getChildren().clear();
				squares = new Square[size][size];
				for (int i = 0; i < size; i++)
					for (int j = 0; j < size; j++)
						centerCPane.add(squares[i][j] = new Square(size), j, i);
				
				errorMessage.setText("");
		    }
			else{
				errorMessage.setText("Please enter a valid board size and select a game mode");
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


	public class Square extends Pane {
		public Square(int size) {
			setStyle("-fx-border-color: black");
			this.setPrefSize(500/size, 500/size);			// the max size of the board pane (500) / the number of squares
			this.setOnMouseClicked(e -> handleMouseClick());
		}

		private void handleMouseClick() {			// TODO: 
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
