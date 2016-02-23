import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class StudySetWindow {
	Stage stage, parentStage;

	public StudySetWindow(StudySet studySet, Stage parentStage) {
		this.parentStage = parentStage;
		// window properties
		stage = new Stage();
		stage.setTitle("StudyCompanion");
		stage.setMinWidth(600);
		stage.setMinHeight(450);

		// layout manager
		GridPane layout = new GridPane();
		//layout.setGridLinesVisible(true);
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
		ObservableList<String> studyMaterialsTypes = FXCollections.observableArrayList("Add Quiz", "Add Deck");
		ComboBox<String> addButton = new ComboBox<String>(studyMaterialsTypes);
		Button closeButton = new Button("Close"),
				statisticsButton = new Button("Statistics"),
				openButton = new Button("Open"),
				editButton = new Button("Edit"),
				deleteButton = new Button("Delete");

		// close button
		closeButton.setPrefWidth(80);
		closeButton.setOnAction(event -> {
			stage.close();
			parentStage.show();
		});
		GridPane.setConstraints(closeButton, 0, 0);
		
		// StudySet label
		studySetLabel.setPrefHeight(20);
		GridPane.setConstraints(studySetLabel, 1, 0, 2, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
		
		// statistics button
		statisticsButton.setPrefWidth(80);
		statisticsButton.disableProperty().bind(studyMaterialsList.getSelectionModel().selectedItemProperty().isNull());
		statisticsButton.setOnAction(event -> {
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
		addButton.setPrefWidth(100);
		addButton.setPromptText("Add...");
		addButton.valueProperty().addListener((event, oldValue, newValue) -> {
			System.out.println(newValue);
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
			// edit
		});
		GridPane.setConstraints(editButton, 2, 2);
		
		// delete button
		deleteButton.setPrefWidth(80);
		deleteButton.disableProperty().bind(studyMaterialsList.getSelectionModel().selectedItemProperty().isNull());
		deleteButton.setOnAction(event -> {
			// delete
		});
		GridPane.setConstraints(deleteButton, 3, 2);
		
		// build and display the window
		layout.getChildren().addAll(closeButton, studySetLabel, statisticsButton, studyMaterialsList, addButton, openButton, editButton, deleteButton);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
	
	public void openStudyMaterial(Object studyMaterial) {
		if (studyMaterial.getClass().equals(Deck.class)) {
			//DeckWindow deckWindow = new DeckWindow((Deck) selectedStudyMaterial);
			//stage.close();
		} else if (studyMaterial.getClass().equals(Quiz.class)) {
			QuizWindow quizWindow = new QuizWindow((Quiz) studyMaterial, stage);
			stage.close();
		} else {
			System.out.println("unidentified object");
		}
	}
}