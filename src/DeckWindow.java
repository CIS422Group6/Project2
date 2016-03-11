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

/**
 * The window for displaying a Deck. A user can progress through the cards in a Deck and mark
 * whether it was an easy, medium, or difficult question. Depending on the user's feedback,
 * the card will appear less or more frequently.
 * @author jack
 * @author richard
 */
public class DeckWindow {
	// tracks the current question
	int currentQuestion = 0;

	/**
	 * Creates an instance of DeckWindow, building all the relevant GUI components and adding them
	 * to a Scene to be displayed on the Stage.
	 * @param deck the Deck to be loaded
	 */
	public DeckWindow(Deck deck) {
		// layout manager
		GridPane layout = new GridPane();
		layout.setHgap(20);
		layout.setVgap(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		Scene scene = new Scene(layout, 600, 450);

		// log the current date
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Date now = new Date();

		long deckDate = 0;

		try {
			deckDate = df.parse(deck.getDate()).getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// backing list of cards
		LinkedList<Card> cards = new LinkedList<Card>(deck.getCards());

		// check whether any cards are left
		if (cards.peek()==null) {
			Main.setScene(scene);
			finish();
		}

		// GUI components
		Label questionLabel = new Label(cards.peek().getQuestion()),
				answerLabel = new Label(cards.peek().getAnswer());
		Button showButton = new Button("Show answer"),
				easyButton = new Button("Easy"),
				mediumButton = new Button("Medium"),
				hardButton = new Button("Hard");

		// question label
		questionLabel.setPrefHeight(20);
		GridPane.setConstraints(questionLabel, 0, 0, 3, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);

		// answer label
		answerLabel.setPrefHeight(20);
		answerLabel.setVisible(false);
		GridPane.setConstraints(answerLabel, 0, 1, 3, 1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.ALWAYS);

		// show button
		showButton.setPrefWidth(160);
		showButton.setOnAction(event -> {
			showButton.setVisible(false);
			answerLabel.setVisible(true);
		});
		GridPane.setConstraints(showButton, 0, 1, 3, 1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.ALWAYS);

		// easy button
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

		// medium button
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

		// hard button
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

		// build and display the window
		layout.getChildren().addAll(questionLabel, answerLabel, showButton, easyButton, mediumButton, hardButton);
		Main.setScene(scene);
	}

	/**
	 * Terminating function for when the Deck has been completed.
	 */
	public void finish(){
		// alert the user that the Deck has been completed
		Alert finWindow = new Alert(AlertType.INFORMATION);
		finWindow.setTitle("Finish");
		finWindow.setHeaderText("You've completed this deck for now!");
		finWindow.showAndWait();
		Main.closeScene();
	}
}
