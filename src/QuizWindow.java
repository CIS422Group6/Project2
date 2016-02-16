import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class QuizWindow {
	int currentQuestion = 0;
	
	public QuizWindow(Quiz quiz) {
		Stage stage = new Stage();
		stage.setTitle("StudyCompanion v0.1");
		stage.setResizable(false);
		
		GridPane layout = new GridPane();
		layout.setHgap(20);
		layout.setVgap(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		
		Label question = new Label();
		Label answer = new Label();
		Label others = new Label();
		Label type = new Label();
		
		question.setText(quiz.getQuestions().get(currentQuestion).getQuestion());
		layout.add(question, 0, 0);
		answer.setText(quiz.getQuestions().get(currentQuestion).getRightAnswer());
		layout.add(answer, 0, 1);
		others.setText(quiz.getQuestions().get(currentQuestion).getWrongAnswers().toString());
		layout.add(others, 0, 2);
		type.setText(quiz.getQuestions().get(currentQuestion).getType());
		layout.add(type, 0, 3);
		
		Button next = new Button("Next");
		next.setOnAction(event -> {
			currentQuestion = currentQuestion + 1;
			question.setText(quiz.getQuestions().get(currentQuestion).getQuestion());
			answer.setText(quiz.getQuestions().get(currentQuestion).getRightAnswer());
			others.setText(quiz.getQuestions().get(currentQuestion).getWrongAnswers().toString());
			type.setText(quiz.getQuestions().get(currentQuestion).getType());
		});
		layout.add(next, 0, 4);
		
		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
}
