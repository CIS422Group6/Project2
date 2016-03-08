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

public class QuizEditWindow {
	ObservableList<Question> questionsCells;
	
	public QuizEditWindow(Quiz quiz) {
		// layout manager
		GridPane layout = new GridPane();
		layout.setHgap(20);
		layout.setVgap(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		Scene scene = new Scene(layout, 600, 450);
		
		// Questions list
		questionsCells = FXCollections.observableArrayList(quiz.getQuestions());
		
		// GUI components
		ListView<Question> questionsList = new ListView<Question>(questionsCells);
		Label quizLabel = new Label(quiz.getName());
		Button closeButton = new Button("Close"),
				addButton = new Button("Add"),
				editButton = new Button("Edit"),
				deleteButton = new Button("Delete");
				
		
		// close button
		closeButton.setPrefWidth(80);
		closeButton.setOnAction(event -> {
			quiz.getQuestions().clear();
			quiz.getQuestions().addAll(questionsCells);
			Main.closeScene();
		});
		GridPane.setConstraints(closeButton, 0, 0);
		
		// Quiz label
		quizLabel.setPrefHeight(20);
		GridPane.setConstraints(quizLabel, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		
		// Questions list
		GridPane.setConstraints(questionsList, 0, 1, 3, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
		
		// add button
		addButton.setPrefWidth(80);
		addButton.setOnAction(event -> {
			addQuestion();
		});
		GridPane.setConstraints(addButton, 0, 2);
		
		// edit button
		editButton.setPrefWidth(80);
		editButton.disableProperty().bind(questionsList.getSelectionModel().selectedItemProperty().isNull());
		editButton.setOnAction(event -> {
			editQuestion(questionsList.getSelectionModel().getSelectedItem());
		});
		GridPane.setConstraints(editButton, 1, 2, 1, 1, HPos.RIGHT, VPos.TOP, Priority.ALWAYS, Priority.NEVER);
		
		// delete button
		deleteButton.setPrefWidth(80);
		deleteButton.disableProperty().bind(questionsList.getSelectionModel().selectedItemProperty().isNull());
		deleteButton.setOnAction(event -> {
			deleteQuestion(questionsList.getSelectionModel().getSelectedItem());
		});
		GridPane.setConstraints(deleteButton, 2, 2);
		
		layout.getChildren().addAll(closeButton, quizLabel, questionsList, addButton, editButton, deleteButton);
		Main.setScene(scene);
	}
	
	public void addQuestion() {
		Question newQuestion = new Question();
		editQuestion(newQuestion);
	}
	
	public void editQuestion(Question question) {
		Dialog<Question> editDialog = new Dialog<Question>();
		editDialog.setTitle("Question");
		VBox layout = new VBox();
		layout.setSpacing(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		
		TextField questionText = new TextField(question.getQuestion());
		questionText.setPromptText("Question");
		ToggleGroup tfAnswers = new ToggleGroup();
		RadioButton trueButton = new RadioButton("True"),
				falseButton = new RadioButton("False");
		trueButton.setToggleGroup(tfAnswers);
		falseButton.setToggleGroup(tfAnswers);
		TextField[] mcAnswerText = new TextField[5];
		mcAnswerText[0] = new TextField();
		mcAnswerText[0].setPromptText("Answer");
		Label wrongAnswers = new Label("Wrong options");
		mcAnswerText[1] = new TextField();
		mcAnswerText[2] = new TextField();
		mcAnswerText[3] = new TextField();
		mcAnswerText[4] = new TextField();							
		TextField answerText = new TextField();
		answerText.setPromptText("Answer");
		
		ComboBox<String> questionTypeCombo = new ComboBox<String>(FXCollections.observableArrayList("True/False", "Multiple choice", "Text input"));
		questionTypeCombo.valueProperty().addListener((event, oldValue, newValue) -> {
			layout.getChildren().clear();
			layout.getChildren().addAll(questionText, questionTypeCombo);
			if (newValue.equals("True/False")) {
				if (question.getRightAnswer().equals("True")) {
					trueButton.setSelected(true);
				} else if (question.getRightAnswer().equals("False")) {
					falseButton.setSelected(true);
				} else {
					trueButton.setSelected(true);
				}
				layout.getChildren().addAll(trueButton, falseButton);
			} else if (newValue.equals("Multiple choice")) {
				mcAnswerText[0].setText(question.getRightAnswer());
				int i = 1;
				for (String wrong : question.getWrongAnswers()) {
					mcAnswerText[i].setText(wrong);
					i++;
				}
				layout.getChildren().addAll(mcAnswerText[0], wrongAnswers, mcAnswerText[1], mcAnswerText[2], mcAnswerText[3], mcAnswerText[4]);
			} else if (newValue.equals("Text input")) {
				answerText.setText(question.getRightAnswer());
				layout.getChildren().add(answerText);	
			}
		});
		
		if (question.getType().equals("tf")) {
			questionTypeCombo.setValue("True/False");
		} else if (question.getType().equals("mc")) {
			questionTypeCombo.setValue("Multiple choice");
		} else if (question.getType().equals("txt")) {
			questionTypeCombo.setValue("Text input");
		} else {
			layout.getChildren().add(questionTypeCombo);
		}

		editDialog.getDialogPane().setMinSize(300, 450);
		editDialog.getDialogPane().setContent(layout);
		ButtonType saveButton = new ButtonType("Save", ButtonData.YES);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		editDialog.getDialogPane().getButtonTypes().addAll(saveButton, cancelButton);
		editDialog.setResultConverter(result -> {
			if (result == saveButton) {
				Question editedQuestion = new Question();
				editedQuestion.setQuestion(questionText.getText());
				int typeIndex = questionTypeCombo.getSelectionModel().getSelectedIndex();
				if (typeIndex == 0) {
					editedQuestion.setType("tf");
					if (trueButton.isSelected()) {
						editedQuestion.setRightAnswer("True");
						editedQuestion.addWrongAnswer("False");
					} else if (falseButton.isSelected()) {
						editedQuestion.setRightAnswer("False");
						editedQuestion.addWrongAnswer("True");
					}
				} else if (typeIndex == 1) {
					editedQuestion.setType("mc");
					editedQuestion.setRightAnswer(mcAnswerText[0].getText());
					for (int i = 1; i < 5; i++) {
						if (mcAnswerText[i].getText() != null || mcAnswerText[i].getText() != "") {
							editedQuestion.addWrongAnswer(mcAnswerText[i].getText());
						}
					}
				} else if (typeIndex == 2) {
					editedQuestion.setType("txt");
					editedQuestion.setRightAnswer(answerText.getText());
				}
				return editedQuestion;
			}
			return null;
		});

		Optional<Question> updatedQuestion = editDialog.showAndWait();
		if (updatedQuestion.isPresent()) {
			questionsCells.remove(question);
			questionsCells.add(updatedQuestion.get());
			//questionsList.refresh();
		}
	}
	
	public void deleteQuestion(Question question) {
		// prompt the user to delete the StudySet
		Dialog<Boolean> deleteDialog = new Dialog<Boolean>();
		deleteDialog.setTitle("Delete a question");
		Label contentLabel = new Label("Are you sure you want to delete the selected question?");
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
		
		// act accordingly
		Optional<Boolean> result = deleteDialog.showAndWait();
		if (result.isPresent() && result.get() == true) {
			questionsCells.remove(question);
		}
	}
}