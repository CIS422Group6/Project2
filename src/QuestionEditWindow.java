import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class QuestionEditWindow {
	Stage stage, parentStage;

	public QuestionEditWindow(Question question, Stage parentStage) {
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
		
		// Question types
		ObservableList<String> questionType = FXCollections.observableArrayList("True/False", "Multiple Choice");
		// Wrong answers
		ObservableList<String> wrongAnswers = FXCollections.observableArrayList(question.getWrongAnswers());
		
		// GUI components
		Label questionLabel = new Label("Question:");
		TextField questionText = new TextField(question.getQuestion());
		Label questionTypeLabel = new Label("Type:");
		ComboBox<String> questionTypeCombo = new ComboBox<String>(questionType);
		Label answerLabel = new Label("Correct answer:");
		TextArea answerText = new TextArea(question.getRightAnswer());
		Label wrongLabel = new Label("Wrong answer(s):");
		ListView<String> wrongList = new ListView<String>(wrongAnswers);
		Button saveButton = new Button("Apply"),
				cancelButton = new Button("Cancel");
		
		// question label
		questionLabel.setPrefHeight(20);
		GridPane.setConstraints(questionLabel, 0, 0, 1, 1, HPos.RIGHT, VPos.CENTER);
		
		// question text
		GridPane.setConstraints(questionText, 1, 0, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
		
		// type label
		questionTypeLabel.setPrefHeight(20);
		GridPane.setConstraints(questionTypeLabel, 0, 1, 1, 1, HPos.RIGHT, VPos.CENTER);
		
		// type combobox
		if (question.getType() == "tf") {
			questionTypeCombo.setValue("True/False");
		} else if (question.getType() == "mc") {
			questionTypeCombo.setValue("Multiple Choice");
		}
		questionTypeCombo.setOnAction(event -> {
			
		});
		GridPane.setConstraints(questionTypeCombo, 1, 1, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
		
		// answer label
		answerLabel.setPrefHeight(20);
		GridPane.setConstraints(answerLabel, 0, 2, 1, 1, HPos.RIGHT, VPos.CENTER);
		
		// answer text
		GridPane.setConstraints(answerText, 1, 2, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
		
		// wrong label
		wrongLabel.setPrefHeight(20);
		GridPane.setConstraints(wrongLabel, 0, 3, 1, 1, HPos.RIGHT, VPos.CENTER);
		
		// wrong list
		wrongList.setEditable(true);
		wrongList.setCellFactory(TextFieldListCell.forListView());
		wrongList.setOnEditCommit(event -> {
			System.out.println(event.getNewValue());
			if (event.getNewValue() == "" || event.getNewValue() == null) {
				wrongAnswers.remove(event.getIndex());
			} else {
				wrongAnswers.set(event.getIndex(), event.getNewValue());
			}
		});
		GridPane.setConstraints(wrongList, 1, 3, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
		
		// save button
		saveButton.setPrefWidth(80);
		saveButton.setOnAction(event -> {
			question.setQuestion(questionText.getText());
			if (questionTypeCombo.getValue() == "True/False") {
				question.setType("tf");
				question.setRightAnswer(answerText.getText());
			} else if (questionTypeCombo.getValue() == "Multiple Choice") {
				question.setType("mc");
				question.setRightAnswer(answerText.getText());
				question.clearWrongAnswers();
				for (String wrongAnswer : wrongAnswers) {
					question.addWrongAnswer(wrongAnswer);
				}
			}
			stage.close();
			parentStage.show();
		});
		GridPane.setConstraints(saveButton, 0, 4);
		
		// cancel button
		cancelButton.setPrefWidth(80);
		GridPane.setConstraints(cancelButton, 1, 4);
		
		// create and display the window
		layout.getChildren().addAll(questionLabel, questionText, questionTypeLabel, questionTypeCombo, answerLabel, answerText, wrongLabel, wrongList, saveButton, cancelButton);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
}
