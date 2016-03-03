import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.LinkedList;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class DeckWindow {
	int currentQuestion = 0;
	Stage stage, parentStage;
	
	public DeckWindow(Deck deck, Stage parentStage) {
		this.parentStage = parentStage;
		stage = new Stage();
		stage.setTitle("StudyCompanion v0.1");
		stage.setResizable(true);
		
		GridPane layout = new GridPane();
		layout.setHgap(20);
		layout.setVgap(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		
		SimpleDateFormat df =new SimpleDateFormat("yyyy/MM/dd");
		Date now = new Date();
		
		long deckDate = 0;
		
		try {
			deckDate=df.parse(deck.getDate()).getTime();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		LinkedList<Card> cards = new LinkedList<Card>();
		
	
		
		if(cards.peek()==null){
			finish();
		}
		
		Label question = new Label();
		Label answer = new Label();
		
		question.setText("Question: " + cards.peek().getQuestion());
		layout.add(question, 0, 0);
		answer.setText("");
		layout.add(answer, 0, 1);
		
		Button reveal = new Button("reveal");
		
		
		reveal.setOnAction(event -> {
			answer.setText("Answer: " + cards.peek().getAnswer());
		});
		layout.add(reveal, 0, 2);

		Button easy = new Button("Easy");
		easy.setOnAction(event -> {
			
			cards.poll().setReviewTime(5);

			if(cards.peek() == null){
				deck.setDate(df.format(now));
				finish();
			}else{	
			
			question.setText(cards.peek().getQuestion());
			answer.setText("");
			
			}
		});
		layout.add(easy, 1, 2);
		
		Button good = new Button("Good");
		good.setOnAction(event -> {
			
			cards.poll().setReviewTime(15);
			
			if(cards.peek() == null){
				deck.setDate(df.format(now));
				finish();
			}else{
			question.setText(cards.peek().getQuestion());
			answer.setText("");
			}
		});
		layout.add(good, 2, 2);
	
		Button hard = new Button("Hard");
		hard.setOnAction(event ->{
			
			cards.poll().setReviewTime(10);
			
			if(cards.peek() == null){
				deck.setDate(df.format(now));
				finish();
			}else{	
			question.setText(cards.peek().getQuestion());
			answer.setText("");
			}
		});
		layout.add(hard, 3, 2);
		
		
		
		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
	
	public void finish(){
		Alert finWindow = new Alert(AlertType.INFORMATION);
		finWindow.setTitle("Complete!");
		finWindow.setContentText("You've completed this deck for now!");
		finWindow.showAndWait();
		stage.close();
		parentStage.show();
	}
	
}
