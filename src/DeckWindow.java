import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.LinkedList;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class DeckWindow {
	int currentQuestion = 0;

	public DeckWindow(Deck deck) {
		GridPane layout = new GridPane();
		layout.setHgap(20);
		layout.setVgap(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		Scene scene = new Scene(layout, 600, 450);

		SimpleDateFormat df =new SimpleDateFormat("yyyy/MM/dd");
		Date now = new Date();

		long deckDate = 0;

		try {
			deckDate = df.parse(deck.getDate()).getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}


		LinkedList<Card> cards = new LinkedList<Card>(deck.getCards());

		if(cards.peek()==null){
			System.out.println("null");
			finish();
		}

		Label questionLabel = new Label(cards.peek().getQuestion()),
				answerLabel = new Label(cards.peek().getAnswer());

		Button showButton = new Button("Show answer"),
				easyButton = new Button("Easy"),
				mediumButton = new Button("Medium"),
				hardButton = new Button("Hard");

		questionLabel.setPrefHeight(20);
		GridPane.setConstraints(questionLabel, 0, 0, 3, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);

		answerLabel.setPrefHeight(20);
		answerLabel.setVisible(false);
		GridPane.setConstraints(answerLabel, 0, 1, 3, 1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.ALWAYS);

		showButton.setPrefWidth(160);
		showButton.setOnAction(event -> {
			showButton.setVisible(false);
			answerLabel.setVisible(true);
		});
		GridPane.setConstraints(showButton, 0, 1, 3, 1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.ALWAYS);

		easyButton.setPrefWidth(80);
		easyButton.disableProperty().bind(answerLabel.visibleProperty().not());
		easyButton.setOnAction(event -> {
			cards.poll().setReviewTime(5);
			if (cards.peek() == null){
				deck.setDate(df.format(now));
				finish();
			} else {
				questionLabel.setText(cards.peek().getQuestion());
				answerLabel.setText(cards.peek().getAnswer());
				answerLabel.setVisible(false);
				showButton.setVisible(true);
			}
		});
		GridPane.setConstraints(easyButton, 0, 2, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);

		mediumButton.setPrefWidth(80);
		mediumButton.disableProperty().bind(answerLabel.visibleProperty().not());
		mediumButton.setOnAction(event -> {
			cards.poll().setReviewTime(15);
			if(cards.peek() == null){
				deck.setDate(df.format(now));
				finish();
			}else{
				questionLabel.setText(cards.peek().getQuestion());
				answerLabel.setText(cards.peek().getAnswer());
				answerLabel.setVisible(false);
				showButton.setVisible(true);
			}
		});
		GridPane.setConstraints(mediumButton, 1, 2, 1, 1, HPos.CENTER, VPos.CENTER);

		hardButton.setPrefWidth(80);
		hardButton.disableProperty().bind(answerLabel.visibleProperty().not());
		hardButton.setOnAction(event ->{
			cards.poll().setReviewTime(10);
			if(cards.peek() == null){
				deck.setDate(df.format(now));
				finish();
			}else{	
				questionLabel.setText(cards.peek().getQuestion());
				answerLabel.setText(cards.peek().getAnswer());
				answerLabel.setVisible(false);
				showButton.setVisible(true);
			}
		});
		GridPane.setConstraints(hardButton, 2, 2, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);

		layout.getChildren().addAll(questionLabel, answerLabel, showButton, easyButton, mediumButton, hardButton);
		Main.setScene(scene);
	}

	public void finish(){
		Alert finWindow = new Alert(AlertType.INFORMATION);
		finWindow.setTitle("Complete!");
		finWindow.setHeaderText("You've completed this deck for now!");
		finWindow.showAndWait();
		Main.closeScene();
	}
}
