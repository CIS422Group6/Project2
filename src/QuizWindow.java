import java.util.Date;
import java.util.Optional;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class QuizWindow {
	Stage stage, parentStage;
	int a;
	
	public QuizWindow(Quiz quiz, Stage parentStage) {
		// window properties
		this.parentStage = parentStage;
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
		
		// User's answers
		String[] userAnswers = new String[quiz.getQuestions().size()];
		
		// GUI components
		Pagination questionsCells = new Pagination(quiz.getQuestions().size());
		Button finishButton = new Button("Finish");
		
		// question pages
		// TODO change implementation
		questionsCells.setPageFactory(new Callback<Integer, Node>() {
			@Override
			public Node call(Integer pageIndex) {
				VBox questionLayout = new VBox();
				questionLayout.setAlignment(Pos.CENTER);
				questionLayout.setSpacing(20);
				questionLayout.setPadding(new Insets(10, 10, 10, 10));
				
				// track the question and given answer
				Question question = quiz.getQuestions().get(pageIndex);
				
				// question label
				Label questionLabel = new Label("Question: " + question.getQuestion());
				questionLayout.getChildren().add(questionLabel);
				
				switch (question.getType()) {
				case "tf" :
					ToggleGroup tfAnswers = new ToggleGroup();
					RadioButton trueButton = new RadioButton("True");
					trueButton.setToggleGroup(tfAnswers);
					trueButton.setOnAction(event -> userAnswers[pageIndex] = "True");
					RadioButton falseButton = new RadioButton("False");
					falseButton.setToggleGroup(tfAnswers);
					falseButton.setOnAction(event -> userAnswers[pageIndex] = "False");
					if (userAnswers[pageIndex] == "True") {
						trueButton.setSelected(true);
					} else if (userAnswers[pageIndex] == "False") {
						falseButton.setSelected(true);
					}
					questionLayout.getChildren().addAll(trueButton, falseButton);
					break;
				case "mc" :
					ToggleGroup mcAnswers = new ToggleGroup();
					int wrongAnswers = question.getWrongAnswers().size();
					RadioButton[] mcButtons = new RadioButton[1 + wrongAnswers];
					
					mcButtons[0] = new RadioButton(question.getRightAnswer());
					mcButtons[0].setToggleGroup(mcAnswers);
					mcButtons[0].setOnAction(event -> userAnswers[pageIndex] = question.getRightAnswer());
					
					a = 1;
					for (String s : question.getWrongAnswers()) {
						mcButtons[a] = new RadioButton(s);
						mcButtons[a].setToggleGroup(mcAnswers);
						mcButtons[a].setOnAction(event -> userAnswers[pageIndex] = s);
						a++;
					}
					
					for (RadioButton b : mcButtons) {
						if (b.getText() == userAnswers[pageIndex]) {
							b.setSelected(true);
						}
						questionLayout.getChildren().add(b);
					}
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
				for (int i = 0; i < quiz.getQuestions().size(); i++) {
					if (userAnswers[i] == quiz.getQuestions().get(i).getRightAnswer()) {
						score++;
					}
				}
				quiz.addStat(new QuizStat(score));
				Alert scoreWindow = new Alert(AlertType.INFORMATION);
				scoreWindow.setTitle("Final score");
				scoreWindow.setContentText("Score: " + score + " out of " + quiz.getQuestions().size());
				scoreWindow.showAndWait();
				stage.close();
				parentStage.show();
			}
		});
		GridPane.setConstraints(finishButton, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		
		// build and display the window
		layout.getChildren().addAll(questionsCells, finishButton);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
	
	public Optional<Boolean> finishQuiz(int unanswered) {
		Dialog<Boolean> finishWindow = new Dialog<Boolean>();
		finishWindow.setTitle("Finish Quiz");
		Label finishLabel = new Label("Are you sure you want to finish the quiz? You have " + unanswered + " questions remaining.");
		HBox layout = new HBox(finishLabel);
		layout.setPadding(new Insets(10, 10, 10, 10));
		HBox.setHgrow(finishLabel, Priority.ALWAYS);
		finishWindow.getDialogPane().setContent(layout);
		
		ButtonType finishButton = new ButtonType("Finish", ButtonData.YES);
		ButtonType closeButton = new ButtonType("Close", ButtonData.CANCEL_CLOSE);
		finishWindow.getDialogPane().getButtonTypes().addAll(finishButton, closeButton);
		finishWindow.setResultConverter(result -> {
			if (result == finishButton) {
				return true;
			}
			return null;
		});
		return finishWindow.showAndWait();
	}
}
