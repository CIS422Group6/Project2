import java.util.Optional;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

public class QuizWindow {
	
	public QuizWindow(Quiz quiz) {
		// layout manager
		GridPane layout = new GridPane();
		layout.setGridLinesVisible(true);
		layout.setHgap(20);
		layout.setVgap(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		Scene scene = new Scene(layout, 600, 450);
		
		// User's answers
		String[] userAnswers = new String[quiz.getQuestions().size()];
		
		// GUI components
		Pagination questionsCells = new Pagination(quiz.getQuestions().size());
		Button finishButton = new Button("Finish");
		
		// question pages
		questionsCells.setPageFactory(new Callback<Integer, Node>() {
			@Override
			public Node call(Integer pageIndex) {
				// question layout manager
				GridPane questionLayout = new GridPane();
				questionLayout.setGridLinesVisible(true);
				questionLayout.setHgap(0);
				questionLayout.setVgap(20);
				questionLayout.setPadding(new Insets(10, 10, 10, 10));
				
				ColumnConstraints col1 = new ColumnConstraints(),
						col2 = new ColumnConstraints(),
						col3 = new ColumnConstraints();
				col1.hgrowProperty().set(Priority.ALWAYS);
				col3.hgrowProperty().set(Priority.ALWAYS);
				questionLayout.getColumnConstraints().addAll(col1, col2, col3);
				
				
				// track the question and given answer
				Question question = quiz.getQuestions().get(pageIndex);
				
				// question label
				Label questionLabel = new Label(question.getQuestion());
				GridPane.setConstraints(questionLabel, 0, 0, 3, 1, HPos.CENTER, VPos.BOTTOM);
				questionLayout.getChildren().add(questionLabel);
				
				switch (question.getType()) {
				case "tf" :
					ToggleGroup tfAnswers = new ToggleGroup();
					RadioButton trueButton = new RadioButton("True");
					trueButton.setToggleGroup(tfAnswers);
					trueButton.setOnAction(event -> userAnswers[pageIndex] = trueButton.getText());
					GridPane.setConstraints(trueButton, 1, 1);
					RadioButton falseButton = new RadioButton("False");
					falseButton.setToggleGroup(tfAnswers);
					falseButton.setOnAction(event -> userAnswers[pageIndex] = falseButton.getText());
					GridPane.setConstraints(falseButton, 1, 2);
					if (userAnswers[pageIndex] == trueButton.getText()) {
						trueButton.setSelected(true);
					} else if (userAnswers[pageIndex] == falseButton.getText()) {
						falseButton.setSelected(true);
					}
					questionLayout.getChildren().addAll(trueButton, falseButton);
					break;
				case "mc" :
					ToggleGroup mcAnswers = new ToggleGroup();
					RadioButton[] mcButtons = new RadioButton[1 + question.getWrongAnswers().size()];
					
					mcButtons[0] = new RadioButton(question.getRightAnswer());
					mcButtons[0].setToggleGroup(mcAnswers);
					mcButtons[0].setOnAction(event -> userAnswers[pageIndex] = mcButtons[0].getText());
					GridPane.setConstraints(mcButtons[0], 1, 1);
					questionLayout.getChildren().add(mcButtons[0]);
					
					int i = 1;
					for (String questionText : question.getWrongAnswers()) {
						mcButtons[i] = new RadioButton(questionText);
						mcButtons[i].setToggleGroup(mcAnswers);
						mcButtons[i].setOnAction(event -> userAnswers[pageIndex] = questionText);
						GridPane.setConstraints(mcButtons[i], 1, i+1);
						questionLayout.getChildren().add(mcButtons[i]);
						i++;
					}
					
					for (RadioButton button : mcButtons) {
						if (button.getText() == userAnswers[pageIndex]) {
							button.setSelected(true);
							break;
						}
					}
					break;
				case "txt" :
					// TODO text questions
					break;
				}
				return questionLayout;
			}
		});
		GridPane.setConstraints(questionsCells, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
		
		// finish button
		finishButton.setPrefWidth(80);
		finishButton.setOnAction(event -> {
			// check if all questions are answered
			int unanswered = 0;
			for (String s : userAnswers) {
				if (s == null) {
					unanswered++;
				}
			}
			// prompt the user
			Optional<Boolean> result = finishQuiz(unanswered);
			// calculate and display score
			if (result.isPresent() && result.get() == true) {
				// TODO change implementation
				int score = 0;
				for (int j = 0; j < quiz.getQuestions().size(); j++) {
					if (userAnswers[j] == quiz.getQuestions().get(j).getRightAnswer()) {
						score++;
					}
				}
				quiz.addStat(new QuizStat(score));
				Alert scoreWindow = new Alert(AlertType.INFORMATION);
				scoreWindow.setTitle("Final score");
				scoreWindow.setContentText("Score: " + score + " out of " + quiz.getQuestions().size());
				scoreWindow.showAndWait();
			}
		});
		GridPane.setConstraints(finishButton, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		
		// build and display the window
		layout.getChildren().addAll(questionsCells, finishButton);
		Main.stage.setScene(scene);
	}
	
	public Optional<Boolean> finishQuiz(int unanswered) {
		Dialog<Boolean> finishDialog = new Dialog<Boolean>();
		finishDialog.setTitle("Finish the Quiz");
		String text = "Are you sure you want to finish and grade the quiz?";
		if (unanswered > 0) {
			text += " You have " + unanswered + " questions remaining.";
		}
		Label finishLabel = new Label(text);
		HBox layout = new HBox(finishLabel);
		layout.setPadding(new Insets(10, 10, 10, 10));
		HBox.setHgrow(finishLabel, Priority.ALWAYS);
		finishDialog.getDialogPane().setContent(layout);
		
		ButtonType gradeButton = new ButtonType("Grade", ButtonData.YES);
		ButtonType discardButton = new ButtonType("Discard", ButtonData.NO);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		finishDialog.getDialogPane().getButtonTypes().addAll(gradeButton, discardButton, cancelButton);
		finishDialog.setResultConverter(result -> {
			if (result == gradeButton) {
				return true;
			}
			return null;
		});
		return finishDialog.showAndWait();
	}
}
