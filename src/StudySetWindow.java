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

public class StudySetWindow {

	public StudySetWindow(StudySet studySet) {
		// layout manager
		GridPane layout = new GridPane();
		layout.setGridLinesVisible(true);
		layout.setHgap(20);
		layout.setVgap(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		Scene scene = new Scene(layout, 600, 450);

		// StudyMaterials list
		ObservableList<Object> studyMaterialsCells = FXCollections.observableArrayList();
		for (Deck deck : studySet.getDecks()) {
			studyMaterialsCells.add(deck);
		}
		for (Quiz quiz : studySet.getQuizzes()) {
			studyMaterialsCells.add(quiz);
		}

		// GUI components
		Label studySetLabel = new Label(studySet.getName());
		ListView<Object> studyMaterialsList = new ListView<Object>(studyMaterialsCells);
		ObservableList<String> studyMaterialsTypes = FXCollections.observableArrayList("Add Deck", "Add Quiz");
		ComboBox<String> addButton = new ComboBox<String>(studyMaterialsTypes);
		Button closeButton = new Button("Close"),
				statisticsButton = new Button("Statistics"),
				openButton = new Button("Open"),
				editButton = new Button("Edit"),
				deleteButton = new Button("Delete");

		// close button
		closeButton.setPrefWidth(80);
		closeButton.setOnAction(event -> {
			studySet.getDecks().clear();
			studySet.getQuizzes().clear();
			for (Object studyMaterial : studyMaterialsCells) {
				if (studyMaterial.getClass().equals(Deck.class)) {
					studySet.getDecks().add((Deck) studyMaterial);
				} else if (studyMaterial.getClass().equals(Quiz.class)) {
					studySet.getQuizzes().add((Quiz) studyMaterial);
				}
			}
			Main.closeScene();
		});
		GridPane.setConstraints(closeButton, 0, 0);
		
		// StudySet label
		studySetLabel.setPrefHeight(20);
		GridPane.setConstraints(studySetLabel, 1, 0, 2, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
		
		// statistics button
		statisticsButton.setPrefWidth(80);
		statisticsButton.disableProperty().bind(studyMaterialsList.getSelectionModel().selectedItemProperty().isNull());
		statisticsButton.setOnAction(event -> {
			// TODO statistics button
			Quiz selectedQuiz = (Quiz) studyMaterialsList.getSelectionModel().getSelectedItem();
			Alert statisticsWindow = new Alert(AlertType.INFORMATION);
			String stats = "";
			for (QuizStat q : selectedQuiz.getStats()) {
				stats += "score: " + q.getScore() + " | date: " + q.getDate() + "\n";
			}
			statisticsWindow.setContentText(stats);
			statisticsWindow.showAndWait();
		});
		GridPane.setConstraints(statisticsButton, 3, 0);
		
		// StudyMaterials list
		GridPane.setConstraints(studyMaterialsList, 0, 1, 4, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
		
		// add button
		addButton.setPrefWidth(80);
		addButton.setPromptText("Add");
		addButton.valueProperty().addListener((event, oldValue, newValue) -> {
			if (newValue == "Add Deck") {
				Optional<String> deckName = addStudyMaterial("Deck");
				if (deckName.isPresent()) {
					Deck newDeck = new Deck(deckName.get());
					studyMaterialsCells.add(newDeck);
					editStudyMaterial(newDeck);
				}
			} else if (newValue == "Add Quiz") {
				Optional<String> quizName = addStudyMaterial("Quiz");
				if (quizName.isPresent()) {
					Quiz newQuiz = new Quiz(quizName.get());
					studyMaterialsCells.add(newQuiz);
					editStudyMaterial(newQuiz);
				}
			}
		});
		GridPane.setConstraints(addButton, 0, 2);
		
		// open button
		openButton.setPrefWidth(80);
		openButton.disableProperty().bind(studyMaterialsList.getSelectionModel().selectedItemProperty().isNull());
		openButton.setOnAction(event -> {
			Object selectedStudyMaterial = studyMaterialsList.getSelectionModel().getSelectedItem();
			openStudyMaterial(selectedStudyMaterial);
		});
		GridPane.setConstraints(openButton, 1, 2, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
		
		// edit button
		editButton.setPrefWidth(80);
		editButton.disableProperty().bind(studyMaterialsList.getSelectionModel().selectedItemProperty().isNull());
		editButton.setOnAction(event -> {
			Object selectedStudyMaterial = studyMaterialsList.getSelectionModel().getSelectedItem();
			editStudyMaterial(selectedStudyMaterial);
		});
		GridPane.setConstraints(editButton, 2, 2);
		
		// delete button
		deleteButton.setPrefWidth(80);
		deleteButton.disableProperty().bind(studyMaterialsList.getSelectionModel().selectedItemProperty().isNull());
		deleteButton.setOnAction(event -> {
			Object selectedStudyMaterial = studyMaterialsList.getSelectionModel().getSelectedItem();
			Optional<Boolean> confirmDeletion = deleteStudyMaterial(selectedStudyMaterial);
			if (confirmDeletion.isPresent() && confirmDeletion.get() == true) {
				studyMaterialsCells.remove(selectedStudyMaterial);
			}
		});
		GridPane.setConstraints(deleteButton, 3, 2);
		
		// build and display the window
		layout.getChildren().addAll(closeButton, studySetLabel, statisticsButton, studyMaterialsList, addButton, openButton, editButton, deleteButton);
		Main.setScene(scene);
	}
	
	public Optional<String> addStudyMaterial(String studyMaterial) {
		Dialog<String> addDialog = new Dialog<String>();
		addDialog.setTitle("Add a new " + studyMaterial);
		TextField studyMaterialText = new TextField();
		HBox layout = new HBox(studyMaterialText);
		layout.setPadding(new Insets(10, 10, 10, 10));
		HBox.setHgrow(studyMaterialText, Priority.ALWAYS);
		addDialog.getDialogPane().setContent(layout);
		ButtonType addButton = new ButtonType("Add", ButtonData.YES);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		addDialog.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);
		addDialog.setResultConverter(result -> {
			if (result == addButton) {
				return studyMaterialText.getText();
			}
			return null;
		});
		return addDialog.showAndWait();
	}
	
	public void openStudyMaterial(Object studyMaterial) {
		if (studyMaterial.getClass().equals(Deck.class)) {
			DeckWindow deckWindow = new DeckWindow((Deck) studyMaterial);
		} else if (studyMaterial.getClass().equals(Quiz.class)) {
			QuizWindow quizWindow = new QuizWindow((Quiz) studyMaterial);
		}
	}
	
	public void editStudyMaterial(Object studyMaterial) {
		if (studyMaterial.getClass().equals(Deck.class)) {
			//DeckWindow deckWindow = new DeckWindow((Deck) studyMaterial, stage);
			//stage.close();
		} else if (studyMaterial.getClass().equals(Quiz.class)) {
			QuizEditWindow quizEditWindow = new QuizEditWindow((Quiz) studyMaterial);
		}
	}
	
	public Optional<Boolean> deleteStudyMaterial(Object studyMaterial) {
		Dialog<Boolean> deleteDialog = new Dialog<Boolean>();
		Label contentLabel = new Label();
		if (studyMaterial.getClass().equals(Deck.class)) {
			Deck deck = (Deck) studyMaterial;
			deleteDialog.setTitle("Delete a Deck");
			contentLabel.setText("Are you sure you want to delete the deck " + deck.getName() + "?");
		} else if (studyMaterial.getClass().equals(Quiz.class)) {
			Quiz quiz = (Quiz) studyMaterial;
			deleteDialog.setTitle("Delete a Quiz");
			contentLabel.setText("Are you sure you want to delete the quiz " + quiz.getName() + "?");
		}
		HBox layout = new HBox(contentLabel);
		layout.setPadding(new Insets(10, 10, 10, 10));
		HBox.setHgrow(contentLabel, Priority.ALWAYS);
		deleteDialog.getDialogPane().setContent(layout);
		ButtonType deleteButton = new ButtonType("Delete", ButtonData.YES);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		deleteDialog.getDialogPane().getButtonTypes().addAll(deleteButton, cancelButton);
		deleteDialog.setResultConverter(result -> {
			if (result == deleteButton) {
				return true;
			}
			return null;
		});
		return deleteDialog.showAndWait();
	}
}