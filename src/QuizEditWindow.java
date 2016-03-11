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
 * The window for editing a Quiz. A user can select a question from the list and edit it, changing
 * the type (true/false, multiple choice, or text based), question, and answer(s).
 * @author richard
 */
public class QuizEditWindow {
	// backing data of questions for the quiz list
	ObservableList<Question> questionsCells;

	/**
	 * Creates an instance of QuizEditWindow, building all the relevant GUI components and adding
	 * them to a Scene to be displayed on the Stage.
	 * @param quiz the Quiz to be edited
	 */
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
			// update the data
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

		// build and display the window
		layout.getChildren().addAll(closeButton, quizLabel, questionsList, addButton, editButton, deleteButton);
		Main.setScene(scene);
	}

	/**
	 * Handles the behaviour for the "Add" button. Creates a new question and opens the question
	 * editor.
	 */
	public void addQuestion() {
		Question newQuestion = new Question();
		editQuestion(newQuestion);
	}

	/**
	 * Handles the behaviour for the "Edit" button. Opens the question editor with the passed
	 * question.
	 * @param question the Question to be edited
	 */
	public void editQuestion(Question question) {
		// build the dialog
		Dialog<Question> dialog = new Dialog<Question>();
		dialog.setTitle("Question editor");
		VBox layout = new VBox();
		layout.setSpacing(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		TextField questionText = new TextField(question.getQuestion());
		questionText.setPromptText("Question");
		// GUI components for different question types
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
		// select the type of question
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

		// pre-fill the fields with the passed question
		if (question.getType().equals("tf")) {
			questionTypeCombo.setValue("True/False");
		} else if (question.getType().equals("mc")) {
			questionTypeCombo.setValue("Multiple choice");
		} else if (question.getType().equals("txt")) {
			questionTypeCombo.setValue("Text input");
		} else {
			layout.getChildren().add(questionTypeCombo);
		}
		dialog.getDialogPane().setMinSize(300, 450);
		dialog.getDialogPane().setContent(layout);
		ButtonType saveButton = new ButtonType("Save", ButtonData.YES);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(saveButton, cancelButton);
		dialog.setResultConverter(result -> {
			// return the saved question
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
						if (!mcAnswerText[i].getText().trim().isEmpty()) {
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
		Optional<Question> updatedQuestion = dialog.showAndWait();
		if (updatedQuestion.isPresent()) {
			questionsCells.remove(question);
			questionsCells.add(updatedQuestion.get());
		}
	}

	/**
	 * Handles the behaviour for the "Delete" button. Prompts the user if they want to delete the
	 * selected question
	 * @param question the Question to be deleted
	 */
	public void deleteQuestion(Question question) {
		// prompt the user to delete the StudySet
		Dialog<Boolean> dialog = new Dialog<Boolean>();
		dialog.setTitle("Delete a question");
		Label contentLabel = new Label("Are you sure you want to delete the selected question?");
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
			questionsCells.remove(question);
		}
	}
}