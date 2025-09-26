package sprint_0;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import java.util.Scanner;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

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
		
		
		// Creates the game choice radio buttons and adds them to the top plane
		RadioButton simpleRButton = new RadioButton("Simple Game");
		RadioButton generalRButton = new RadioButton("General Game");
		ToggleGroup group = new ToggleGroup();
		simpleRButton.setToggleGroup(group);
		generalRButton.setToggleGroup(group);
		generalRButton.setTranslateX(30);
		topPane.add(simpleRButton, 1, 5);
		topPane.add(generalRButton, 2, 5);
		
		// Creates a textbox for the board size and adds it to the top plane
		Label label1 = new Label("Board Size:");
		TextField textField = new TextField ();
		HBox hb = new HBox();
		hb.getChildren().addAll(label1, textField);
		hb.setSpacing(5);
		hb.setTranslateX(120);
		topPane.add(hb,  3,  5);
		
		// Apply button for the textbox and adds it to pane
		Button button = new Button("Apply");
		
		topPane.add(button, 4, 5);
		button.setTranslateX(120);
		
		// Changes the size of the board based on the user entered number
		button.setOnAction(event -> {			// TODO: add conditional to do nothing if the textbox is empty
			int size =  Integer.parseInt(textField.getText());

			if (size > 0) {
				squares = new Square[size][size];
				for (int i = 0; i < size; i++)
					for (int j = 0; j < size; j++)
						centerPane.add(squares[i][j] = new Square(), j, i);
		    }});
		
		CheckBox myCheckBox = new CheckBox();
		myCheckBox.setText("Record game");
		Label gameStatus = new Label("Player One's Turn");   // TODO: make dynamic
		bottomPane.add(myCheckBox, 1, 5);
		bottomPane.add(gameStatus, 2, 5);
		gameStatus.setTranslateX(120);
		

		// Adds each pane to the border pane
		centerPane.setTranslateY(30);
		centerPane.setTranslateX(50);			// TODO: make board location dynamic (depending on its size)
		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(centerPane);
		borderPane.setBottom(bottomPane);
		borderPane.setTop(topPane);
				
		// Creates the scene with the border plane
		Scene scene = new Scene(borderPane, 600, 600);
		primaryStage.setTitle("SOS Game");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public class Square extends Pane {
		public Square() {
			setStyle("-fx-border-color: black");
			this.setPrefSize(100, 100);			// TODO: make window size dynamic
			this.setOnMouseClicked(e -> handleMouseClick());
		}

		private void handleMouseClick() {			// TODO: 
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
