import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * The window for editing a Deck. A user can select a card from the list and edit it, changing
 * the question, and answer.
 * @author richard
 */
public class DeckEditWindow {
	// backing data of cards for the deck list
	ObservableList<Card> cardsCells;

	/**
	 * Creates an instance of DeckEditWindow, building all the relevant GUI components and adding
	 * them to a Scene to be displayed on the Stage.
	 * @param deck the Deck to be edited
	 */
	public DeckEditWindow(Deck deck) {
		// layout manager
		GridPane layout = new GridPane();
		layout.setHgap(20);
		layout.setVgap(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		Scene scene = new Scene(layout, 600, 450);

		// Questions list
		cardsCells = FXCollections.observableArrayList(deck.getCards());

		// GUI components
		ListView<Card> cardsList = new ListView<Card>(cardsCells);
		Label deckLabel = new Label(deck.getName());
		Button closeButton = new Button("Close"),
				addButton = new Button("Add"),
				editButton = new Button("Edit"),
				deleteButton = new Button("Delete");


		// close button
		closeButton.setPrefWidth(80);
		closeButton.setOnAction(event -> {
			// update the data
			deck.getCards().clear();
			deck.getCards().addAll(cardsCells);
			Main.closeScene();
		});
		GridPane.setConstraints(closeButton, 0, 0);

		// Deck label
		deckLabel.setPrefHeight(20);
		GridPane.setConstraints(deckLabel, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);

		// Cards list
		GridPane.setConstraints(cardsList, 0, 1, 3, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);

		// add button
		addButton.setPrefWidth(80);
		addButton.setOnAction(event -> {
			addCard();
		});
		GridPane.setConstraints(addButton, 0, 2);

		// edit button
		editButton.setPrefWidth(80);
		editButton.disableProperty().bind(cardsList.getSelectionModel().selectedItemProperty().isNull());
		editButton.setOnAction(event -> {
			editCard(cardsList.getSelectionModel().getSelectedItem());
		});
		GridPane.setConstraints(editButton, 1, 2, 1, 1, HPos.RIGHT, VPos.TOP, Priority.ALWAYS, Priority.NEVER);

		// delete button
		deleteButton.setPrefWidth(80);
		deleteButton.disableProperty().bind(cardsList.getSelectionModel().selectedItemProperty().isNull());
		deleteButton.setOnAction(event -> {
			deleteCard(cardsList.getSelectionModel().getSelectedItem());
		});
		GridPane.setConstraints(deleteButton, 2, 2);

		// build and display the window
		layout.getChildren().addAll(closeButton, deckLabel, cardsList, addButton, editButton, deleteButton);
		Main.setScene(scene);
	}

	/**
	 * Handles the behaviour for the "Add" button. Creates a new card and opens the card editor.
	 */
	public void addCard() {
		Card newCard = new Card();
		editCard(newCard);
	}

	/**
	 * Handles the behaviour for the "Edit" button. Opens the card editor with the passed card.
	 * @param card the Card to be edited
	 */
	public void editCard(Card card) {
		// build the dialog
		Dialog<Card> dialog = new Dialog<Card>();
		dialog.setTitle("Card editor");
		VBox layout = new VBox();
		layout.setSpacing(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		TextField questionText = new TextField(card.getQuestion());
		questionText.setPromptText("Question");						
		TextField answerText = new TextField(card.getAnswer());
		answerText.setPromptText("Answer");
		layout.getChildren().addAll(questionText, answerText);
		dialog.getDialogPane().setContent(layout);
		ButtonType saveButton = new ButtonType("Save", ButtonData.YES);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(saveButton, cancelButton);
		dialog.setResultConverter(result -> {
			// return the saved question
			if (result == saveButton) {
				return new Card(questionText.getText(), answerText.getText());
			}
			return null;
		});
		Optional<Card> updatedCard = dialog.showAndWait();
		if (updatedCard.isPresent()) {
			cardsCells.remove(card);
			cardsCells.add(updatedCard.get());
		}
	}

	/**
	 * Handles the behaviour for the "Delete" button. Prompts the user if they want to delete the
	 * selected card.
	 * @param card the Card to be deleted
	 */
	public void deleteCard(Card card) {
		// prompt the user to delete the StudySet
		Dialog<Boolean> dialog = new Dialog<Boolean>();
		dialog.setTitle("Delete a card");
		Label contentLabel = new Label("Are you sure you want to delete the selected card?");
		HBox layout = new HBox(contentLabel);
		layout.setPadding(new Insets(10, 10, 10, 10));
		HBox.setHgrow(contentLabel, Priority.ALWAYS);
		dialog.getDialogPane().setContent(layout);
		ButtonType deleteButton = new ButtonType("Delete", ButtonData.YES);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(deleteButton, cancelButton);
		dialog.setResultConverter(result -> {
			if (result == deleteButton) {
				return true;
			}
			return null;
		});

		// act accordingly
		Optional<Boolean> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == true) {
			cardsCells.remove(card);
		}
	}
}