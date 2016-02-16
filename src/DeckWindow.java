import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class DeckWindow {
	int currentQuestion = 0;
	
	public DeckWindow(Deck deck) {
		Stage stage = new Stage();
		stage.setTitle("StudyCompanion v0.1");
		stage.setResizable(false);
		
		GridPane layout = new GridPane();
		layout.setHgap(20);
		layout.setVgap(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		
		Label question = new Label();
		Label answer = new Label();
		
		question.setText(deck.getCards().get(currentQuestion).getQuestion());
		layout.add(question, 0, 0);
		answer.setText(deck.getCards().get(currentQuestion).getAnswer());
		layout.add(answer, 0, 1);
		
		Button next = new Button("Next");
		next.setOnAction(event -> {
			currentQuestion = currentQuestion + 1;
			question.setText(deck.getCards().get(currentQuestion).getQuestion());
			answer.setText(deck.getCards().get(currentQuestion).getAnswer());
		});
		layout.add(next, 0, 2);
		
		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
}
