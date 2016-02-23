import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class QuizWindow {
	int a;
	public QuizWindow(Quiz quiz) {
		// window properties
		Stage stage = new Stage();
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
		Button closeButton = new Button("Finish");
		
		// question pages
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
					trueButton.setOnAction((event) -> userAnswers[pageIndex] = "t");
					RadioButton falseButton = new RadioButton("False");
					falseButton.setToggleGroup(tfAnswers);
					falseButton.setOnAction((event) -> userAnswers[pageIndex] = "f");
					if (userAnswers[pageIndex] == "t") {
						trueButton.setSelected(true);
					} else if (userAnswers[pageIndex] == "f") {
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
					mcButtons[0].setOnAction((event) -> userAnswers[pageIndex] = question.getRightAnswer());
					
					a = 1;
					for (String s : question.getWrongAnswers()) {
						mcButtons[a] = new RadioButton(s);
						mcButtons[a].setToggleGroup(mcAnswers);
						mcButtons[a].setOnAction((event) -> userAnswers[pageIndex] = s);
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
		
		// close button
		closeButton.setPrefWidth(80);
		GridPane.setConstraints(closeButton, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		
		// build and display window
		layout.getChildren().addAll(questionsCells, closeButton);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
}
