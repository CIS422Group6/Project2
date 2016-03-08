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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class QuizEditWindow {
	
	public QuizEditWindow(Quiz quiz) {
		// layout manager
		GridPane layout = new GridPane();
		//layout.setGridLinesVisible(true);
		layout.setHgap(20);
		layout.setVgap(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		Scene scene = new Scene(layout, 600, 450);
		
		// Questions list
		ObservableList<Question> questionsCells = FXCollections.observableArrayList(quiz.getQuestions());
		
		// GUI components
		ListView<Question> questionsList = new ListView<Question>(questionsCells);
		Label quizLabel = new Label(quiz.getName());
		Button closeButton = new Button("Close"),
				addButton = new Button("Add"),
				editButton = new Button("Edit"),
				deleteButton = new Button("Delete");
				
		
		// Close button
		closeButton.setPrefWidth(80);
		closeButton.setOnAction(event -> {
			Main.closeScene();
		});
		GridPane.setConstraints(closeButton, 0, 0);
		
		// Quiz label
		quizLabel.setPrefHeight(20);
		GridPane.setConstraints(quizLabel, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		
		// Questions list
		GridPane.setConstraints(questionsList, 0, 1, 3, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
		
		// Add button
		addButton.setPrefWidth(80);
		addButton.setOnAction(event -> {
			// TODO add question
			Question newQuestion = new Question();
			quiz.addQuestion(newQuestion);
			questionsCells.add(newQuestion);
			editQuestion(newQuestion);
		});
		GridPane.setConstraints(addButton, 0, 2);
		
		// Edit button
		editButton.setPrefWidth(80);
		editButton.disableProperty().bind(questionsList.getSelectionModel().selectedItemProperty().isNull());
		editButton.setOnAction(event -> {
			// TODO edit question
			Question selectedQuestion = questionsList.getSelectionModel().getSelectedItem();
			editQuestion(selectedQuestion);
		});
		GridPane.setConstraints(editButton, 1, 2, 1, 1, HPos.RIGHT, VPos.TOP, Priority.ALWAYS, Priority.NEVER);
		
		// Delete button
		deleteButton.setPrefWidth(80);
		deleteButton.disableProperty().bind(questionsList.getSelectionModel().selectedItemProperty().isNull());
		deleteButton.setOnAction(event -> {
			// TODO delete question
			Question selectedQuestion = questionsList.getSelectionModel().getSelectedItem();
			Optional<Boolean> confirmDeletion = deleteQuestion();
			if (confirmDeletion.isPresent() && confirmDeletion.get() == true) {
				questionsCells.remove(selectedQuestion);
				quiz.getQuestions().remove(selectedQuestion);
			} else {
				System.out.println("cancelled");
			}
		});
		GridPane.setConstraints(deleteButton, 2, 2);
		
		layout.getChildren().addAll(closeButton, quizLabel, questionsList, addButton, editButton, deleteButton);
		Main.setScene(scene);
	}
	
	public void editQuestion(Question question) {
		QuestionEditWindow questionEditWindow = new QuestionEditWindow(question);
	}
	
	public Optional<Boolean> deleteQuestion() {
		Dialog<Boolean> deleteWindow = new Dialog<Boolean>();
		deleteWindow.setTitle("Delete a Question");
		Label contentLabel = new Label("Are you sure you want to delete the selected question?");
		HBox layout = new HBox(contentLabel);
		layout.setPadding(new Insets(10, 10, 10, 10));
		HBox.setHgrow(contentLabel, Priority.ALWAYS);
		deleteWindow.getDialogPane().setContent(layout);
		
		ButtonType deleteButton = new ButtonType("Delete", ButtonData.YES);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		deleteWindow.getDialogPane().getButtonTypes().addAll(deleteButton, cancelButton);
		deleteWindow.setResultConverter(result -> {
			if (result == deleteButton) {
				return true;
			}
			return null;
		});
		return deleteWindow.showAndWait();
	}
}