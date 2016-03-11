import java.io.File;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;

/**
 * The window for displaying StudySets. Currently, a StudySet can contain a Deck or a Quiz, and
 * these can be added or modified here using the buttons. Statistics can also be viewed.
 * @author richard
 */
public class StudySetWindow {
	// backing data for the GUI list
	ObservableList<Object> studyMaterialsCells;

	/**
	 * Creates an instance of StudySetWindow, building all the relevant GUI components and adding
	 * them to a Scene to be displayed on the Stage.
	 * @param studySet the StudySet to be loaded
	 */
	public StudySetWindow(StudySet studySet) {
		// layout manager
		GridPane layout = new GridPane();
		layout.setHgap(20);
		layout.setVgap(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		Scene scene = new Scene(layout, 600, 450);

		// build the StudyMaterials list of both Deck and Quiz
		studyMaterialsCells = FXCollections.observableArrayList();
		for (Deck deck : studySet.getDecks()) { studyMaterialsCells.add(deck); }
		for (Quiz quiz : studySet.getQuizzes()) { studyMaterialsCells.add(quiz); }

		// GUI components
		Label studySetLabel = new Label(studySet.getName());
		ListView<Object> studyMaterialsList = new ListView<Object>(studyMaterialsCells);
		ObservableList<String> studyMaterialsTypes = FXCollections.observableArrayList("Add Deck", "Add Quiz");
		ComboBox<String> addButton = new ComboBox<String>(studyMaterialsTypes);
		Button closeButton = new Button("Close"),
				statisticsButton = new Button("Statistics"),
				openButton = new Button("Open"),
				editButton = new Button("Edit"),
				exportButton = new Button("Export"),
				deleteButton = new Button("Delete");

		// close button
		closeButton.setPrefWidth(80);
		closeButton.setOnAction(event -> {
			// update any changed data
			close(studySet);
			Main.closeScene();
		});
		GridPane.setConstraints(closeButton, 0, 0);

		// StudySet label
		studySetLabel.setPrefHeight(20);
		GridPane.setConstraints(studySetLabel, 1, 0, 3, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);

		// statistics button
		statisticsButton.setPrefWidth(80);
		statisticsButton.disableProperty().bind(studyMaterialsList.getSelectionModel().selectedItemProperty().isNull());
		statisticsButton.setOnAction(event -> {
			showStatistics(studyMaterialsList.getSelectionModel().getSelectedItem());
		});
		GridPane.setConstraints(statisticsButton, 4, 0);

		// StudyMaterials list
		GridPane.setConstraints(studyMaterialsList, 0, 1, 5, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);

		// add button
		addButton.setPrefWidth(80);
		addButton.setPromptText("Add");
		addButton.valueProperty().addListener((event, oldValue, newValue) -> {
			addStudyMaterial(newValue);
		});
		GridPane.setConstraints(addButton, 0, 2);

		// open button
		openButton.setPrefWidth(80);
		openButton.disableProperty().bind(studyMaterialsList.getSelectionModel().selectedItemProperty().isNull());
		openButton.setOnAction(event -> {
			openStudyMaterial(studyMaterialsList.getSelectionModel().getSelectedItem());
		});
		GridPane.setConstraints(openButton, 1, 2, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);

		// edit button
		editButton.setPrefWidth(80);
		editButton.disableProperty().bind(studyMaterialsList.getSelectionModel().selectedItemProperty().isNull());
		editButton.setOnAction(event -> {
			editStudyMaterial(studyMaterialsList.getSelectionModel().getSelectedItem());
		});
		GridPane.setConstraints(editButton, 2, 2);

		// export button
		exportButton.setPrefWidth(80);
		exportButton.disableProperty().bind(studyMaterialsList.getSelectionModel().selectedItemProperty().isNull());
		exportButton.setOnAction(event -> {
			exportStudyMaterial(studyMaterialsList.getSelectionModel().getSelectedItem());
		});
		GridPane.setConstraints(exportButton, 3, 2);

		// delete button
		deleteButton.setPrefWidth(80);
		deleteButton.disableProperty().bind(studyMaterialsList.getSelectionModel().selectedItemProperty().isNull());
		deleteButton.setOnAction(event -> {
			deleteStudyMaterial(studyMaterialsList.getSelectionModel().getSelectedItem());
		});
		GridPane.setConstraints(deleteButton, 4, 2);

		// build and display the window
		layout.getChildren().addAll(closeButton, studySetLabel, statisticsButton, studyMaterialsList, addButton, openButton, editButton, exportButton, deleteButton);
		Main.setScene(scene);
	}

	/**
	 * Handles the behaviour for the "Add" button. Prompts the user to name the new StudyMaterial
	 * and creates one if indicated.
	 * @param type the type of StudyMaterial to create (Deck or Quiz)
	 */
	public void addStudyMaterial(String type) {
		// prompt the user for a name
		Dialog<String> dialog = new Dialog<String>();
		if (type.equals("Add Deck")) {
			dialog.setTitle("Add a Deck");
		} else if (type.equals("Add Quiz")) {
			dialog.setTitle("Add a Quiz");
		}
		TextField studyMaterialText = new TextField();
		HBox layout = new HBox(studyMaterialText);
		layout.setPadding(new Insets(10, 10, 10, 10));
		HBox.setHgrow(studyMaterialText, Priority.ALWAYS);
		dialog.getDialogPane().setContent(layout);
		ButtonType addButton = new ButtonType("Add", ButtonData.YES);
		ButtonType importButton = new ButtonType("Import", ButtonData.LEFT);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(addButton, importButton, cancelButton);
		dialog.setResultConverter(result -> {
			if (result == addButton) {
				// return the new StudyMaterial name
				return studyMaterialText.getText();
			} else if (result == importButton) {
				// prompt the user to select a file to import
				FileChooser importWindow = new FileChooser();
				FileChooser.ExtensionFilter xml = new FileChooser.ExtensionFilter("eXtensibleMarkup Language file", "*.xml");
				File file = null;
				importWindow.getExtensionFilters().add(xml);
				file = importWindow.showOpenDialog(Main.stage);
				if (file != null) {
					// attempt to import the selected file and warn if it fails
					if (type.equals("Add Deck")) {
						Deck newDeck = new Deck();
						if (newDeck.deckImport(file.getPath())) {
							studyMaterialsCells.add(newDeck);
							editStudyMaterial(newDeck);
						} else {
							Alert importFailed = new Alert(AlertType.ERROR);
							importFailed.setTitle("Import failed");
							importFailed.setHeaderText("Failed to import the deck.");
							importFailed.showAndWait();
						}
					} else if (type.equals("Add Quiz")) {
						Quiz newQuiz = new Quiz();
						if (newQuiz.quizImport(file.getPath())) {
							studyMaterialsCells.add(newQuiz);
							editStudyMaterial(newQuiz);
						} else {
							Alert importFailed = new Alert(AlertType.ERROR);
							importFailed.setTitle("Import failed");
							importFailed.setHeaderText("Failed to import the quiz.");
							importFailed.showAndWait();
						}
					}
				}
				return null;
			}
			return null;
		});

		// create the desired study material
		Optional<String> studyMaterialName = dialog.showAndWait();
		if (studyMaterialName.isPresent()) {
			if (type.equals("Add Deck")) {
				Deck newDeck = new Deck(studyMaterialName.get());
				studyMaterialsCells.add(newDeck);
				editStudyMaterial(newDeck);
			} else if (type.equals("Add Quiz")) {
				Quiz newQuiz = new Quiz(studyMaterialName.get());
				studyMaterialsCells.add(newQuiz);
				editStudyMaterial(newQuiz);
			}
		}
	}

	/**
	 * Handles the behaviour for the "Open" button. Determines the type of the selected
	 * StudyMaterial and opens the appropiate window.
	 * @param studyMaterial the StudyMaterial to be opened
	 */
	public void openStudyMaterial(Object studyMaterial) {
		if (studyMaterial.getClass().equals(Deck.class)) {
			DeckWindow deckWindow = new DeckWindow((Deck) studyMaterial);
		} else if (studyMaterial.getClass().equals(Quiz.class)) {
			QuizWindow quizWindow = new QuizWindow((Quiz) studyMaterial);
		}
	}

	/**
	 * Handles the behaviour for the "Edit" button. Determines the type of the selected
	 * StudyMaterial and opens the appropiate window for editing.
	 * @param studyMaterial the StudyMaterial to be edited
	 */
	public void editStudyMaterial(Object studyMaterial) {
		if (studyMaterial.getClass().equals(Deck.class)) {
			DeckEditWindow quizEditWindow = new DeckEditWindow((Deck) studyMaterial);
		} else if (studyMaterial.getClass().equals(Quiz.class)) {
			QuizEditWindow quizEditWindow = new QuizEditWindow((Quiz) studyMaterial);
		}
	}

	/**
	 * Handles the behaviour for the "Export" button. Determines the type of the selected
	 * StudyMaterial and prompts the user for a location to save to.
	 * @param studyMaterial the StudyMaterial to be exported
	 */
	public void exportStudyMaterial(Object studyMaterial) {
		// prompt the user for a location to save to
		FileChooser exportWindow = new FileChooser();
		FileChooser.ExtensionFilter xml = new FileChooser.ExtensionFilter("eXtensibleMarkup Language file", "*.xml");
		File file = null;
		exportWindow.getExtensionFilters().add(xml);
		file = exportWindow.showSaveDialog(Main.stage);
		// if a location is selected, export the StudyMaterial to there
		if (file != null) {
			if (studyMaterial.getClass().equals(Deck.class)) {
				Deck exportDeck = (Deck) studyMaterial;
				exportDeck.export(file.getPath());
			} else if (studyMaterial.getClass().equals(Quiz.class)) {
				Quiz exportQuiz = (Quiz) studyMaterial;
				exportQuiz.export(file.getPath());
			}
		}
	}

	/**
	 * Handles the behaviour for the "Delete" button. Confirms with the user whether they want to
	 * delete the selected StudyMaterial.
	 * @param studyMaterial the StudyMaterial to be deleted
	 */
	public void deleteStudyMaterial(Object studyMaterial) {
		// prompt the user to delete the study material
		Dialog<Boolean> dialog = new Dialog<Boolean>();
		Label contentLabel = new Label();
		if (studyMaterial.getClass().equals(Deck.class)) {
			Deck deck = (Deck) studyMaterial;
			dialog.setTitle("Delete a Deck");
			contentLabel.setText("Are you sure you want to delete the deck " + deck.getName() + "?");
		} else if (studyMaterial.getClass().equals(Quiz.class)) {
			Quiz quiz = (Quiz) studyMaterial;
			dialog.setTitle("Delete a Quiz");
			contentLabel.setText("Are you sure you want to delete the quiz " + quiz.getName() + "?");
		}
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
			studyMaterialsCells.remove(studyMaterial);
		}
	}

	/**
	 * Handles the behaviour for the "Statistics" button. Determines the type of the selected
	 * StudyMaterial and displays the relevant statistics.
	 * @param studyMaterial the StudyMaterial to be opened
	 */
	public void showStatistics(Object studyMaterial) {
		// build the window
		Alert dialog = new Alert(AlertType.INFORMATION);
		String headerText = "";
		if (studyMaterial.getClass().equals(Quiz.class)) {
			Quiz quiz = (Quiz) studyMaterial;
			// display each score
			for (QuizStat q : quiz.getStats()) {
				String score = String.format("%.2f", (double) q.getScore() / quiz.getQuestions().size() * 100);
				headerText += "You scored " + score + "% on " + q.getDate() + "\n";
			}
			if (headerText == "") {
				headerText = "No statistics have been recorded yet.";
			}
		} else if (studyMaterial.getClass().equals(Deck.class)) {
			headerText = "There are no statistics for Decks!";
		}
		dialog.setHeaderText(headerText);
		dialog.showAndWait();
	}

	/**
	 * Handles the behaviour for the "Close" button. Saves any changes and returns to the
	 * PrimaryWindow.
	 * @param studySet the StudySet to be updated
	 */
	public void close(StudySet studySet) {
		// clear the old data
		studySet.getDecks().clear();
		studySet.getQuizzes().clear();
		// iterate through the backing data for the GUI list and update
		for (Object studyMaterial : studyMaterialsCells) {
			if (studyMaterial.getClass().equals(Deck.class)) {
				studySet.getDecks().add((Deck) studyMaterial);
			} else if (studyMaterial.getClass().equals(Quiz.class)) {
				studySet.getQuizzes().add((Quiz) studyMaterial);
			}
		}
	}
}