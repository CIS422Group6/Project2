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
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

/**
 * The window for displaying a Quiz. A user can progress through the questions in a Quiz and mark
 * their answers. Once they are finished, they can choose if they want to save their score or not.
 * @author richard
 */
public class QuizWindow {

	/**
	 * Creates an instance of QuizWindow, building all the relevant GUI components and adding them
	 * to a Scene to be displayed on the Stage.
	 * @param quiz the Quiz to be loaded
	 */
	public QuizWindow(Quiz quiz) {
		// layout manager
		GridPane layout = new GridPane();
		layout.setHgap(20);
		layout.setVgap(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		Scene scene = new Scene(layout, 600, 450);

		// user's answers
		String[] userAnswers = new String[quiz.getQuestions().size()];

		// GUI components
		Pagination questionsCells = new Pagination(quiz.getQuestions().size());
		Button finishButton = new Button("Finish");

		// finish button
		finishButton.setPrefWidth(80);
		finishButton.setOnAction(event -> {
			// count unanswered questions
			int unanswered = 0;
			for (String s : userAnswers) {
				if (s == null) { unanswered++; }
			}
			
			// prompt the user for an action
			Optional<Integer> result = finishQuiz(unanswered);
			// act accordingly (1 = grade, 0 = discard, null = resume)
			if (result.isPresent()) {
				if (result.get() == 1) {
					// if graded, calculate the score
					int score = 0, total = quiz.getQuestions().size();
					for (int i = 0; i < total; i++) {
						if (userAnswers[i] != null && quiz.getQuestions().get(i).compareAnswer(userAnswers[i])) {
							score++;
						}
					}
					// display and save the result
					Alert scoreDialog = new Alert(AlertType.INFORMATION);
					scoreDialog.setTitle("Score");
					String percent = String.format("%.2f", (double) score/total*100);
					scoreDialog.setHeaderText("Final score: " + percent + "%");
					scoreDialog.setContentText("You answered " + (total-unanswered) + " questions out of " + total + "."
							+ "\nYour score has been saved.");
					scoreDialog.showAndWait();
					quiz.addStat(new QuizStat(score));
					Main.closeScene();
				}
				if (result.get() == 0) {
					// if discarded, quit
					Main.closeScene();
				}
				// otherwise do nothing and continue with the quiz
			}
		});
		GridPane.setConstraints(finishButton, 0, 0, 1, 1, HPos.LEFT, VPos.CENTER);

		// question pages
		questionsCells.setPageFactory(new Callback<Integer, Node>() {
			@Override
			public Node call(Integer pageIndex) {
				// question layout manager
				GridPane questionLayout = new GridPane();
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
					// True/False
					ToggleGroup tfAnswers = new ToggleGroup();
					RadioButton trueButton = new RadioButton("True");
					trueButton.setToggleGroup(tfAnswers);
					trueButton.setOnAction(event -> userAnswers[pageIndex] = "True");
					GridPane.setConstraints(trueButton, 1, 1);
					RadioButton falseButton = new RadioButton("False");
					falseButton.setToggleGroup(tfAnswers);
					falseButton.setOnAction(event -> userAnswers[pageIndex] = "False");
					GridPane.setConstraints(falseButton, 1, 2);

					if (userAnswers[pageIndex] != null && userAnswers[pageIndex].equals(trueButton.getText())) {
						trueButton.setSelected(true);
					} else if (userAnswers[pageIndex] != null && userAnswers[pageIndex].equals(falseButton.getText())) {
						falseButton.setSelected(true);
					}
					questionLayout.getChildren().addAll(trueButton, falseButton);
					break;
				case "mc" :
					// Multiple choice
					ToggleGroup mcAnswers = new ToggleGroup();
					RadioButton[] mcButtons = new RadioButton[1 + question.getWrongAnswers().size()];

					mcButtons[0] = new RadioButton(question.getRightAnswer());
					mcButtons[0].setToggleGroup(mcAnswers);
					mcButtons[0].setOnAction(event -> userAnswers[pageIndex] = question.getRightAnswer());
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
						if (userAnswers[pageIndex] != null && userAnswers[pageIndex].equals(button.getText())) {
							button.setSelected(true);
							break;
						}
					}
					break;
				case "txt" :
					// Text input
					TextField textField = new TextField();
					textField.textProperty().addListener(event -> userAnswers[pageIndex] = textField.getText());
					GridPane.setConstraints(textField, 1, 1);
					if (userAnswers[pageIndex] != null) {
						textField.setText(userAnswers[pageIndex]);
					}
					questionLayout.getChildren().add(textField);
					break;
				}
				return questionLayout;
			}
		});
		GridPane.setConstraints(questionsCells, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);

		// build and display the window
		layout.getChildren().addAll(finishButton, questionsCells);
		Main.setScene(scene);
		}

		/**
		 * Handles the behaviour for the "Finish" button. Prompts the user whether they would like
		 * to Grade, Discard, or Resume the quiz and returns the choice.
		 * @param unanswered total number of unanswered questions
		 * @return An Optional type containing the user's choice
		 */
		public Optional<Integer> finishQuiz(int unanswered) {
			// prompt the user
			Dialog<Integer> dialog = new Dialog<Integer>();
			dialog.setTitle("Finish the Quiz");
			String prompt = "Are you sure you want to finish and grade the quiz?";
			if (unanswered > 0) { prompt += " You have " + unanswered + " questions remaining."; }
			Label finishLabel = new Label(prompt);
			HBox layout = new HBox(finishLabel);
			layout.setPadding(new Insets(10, 10, 10, 10));
			HBox.setHgrow(finishLabel, Priority.ALWAYS);
			dialog.getDialogPane().setContent(layout);

			ButtonType gradeButton = new ButtonType("Grade", ButtonData.YES);
			ButtonType discardButton = new ButtonType("Discard", ButtonData.OTHER);
			ButtonType resumeButton = new ButtonType("Resume", ButtonData.CANCEL_CLOSE);
			dialog.getDialogPane().getButtonTypes().addAll(gradeButton, discardButton, resumeButton);
			dialog.setResultConverter(result -> {
				if (result == gradeButton) {
					return 1;
				} else if (result == discardButton) {
					return 0;
				} else {
					return null;
				}
			});
			return dialog.showAndWait();
		}
	}