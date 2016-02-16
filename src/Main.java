import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main class for the application.
 */

public class Main extends Application {
	public static List<StudySet> studySets = new ArrayList<StudySet>();
	
	/** Starting point of the application. */
	public static void main(String[] args) {
		// create sample data accessible by all classes
		StudySet cis422 = new StudySet("CIS 422");
		Deck chap1 = new Deck("Chapter 1");
		Card c1 = new Card("Question one", "Answer one");
		Card c2 = new Card("Question two", "Answer two");
		chap1.addCard(c1);
		chap1.addCard(c2);
		cis422.addDeck(chap1);
		Quiz midterm = new Quiz("Midterm");
		Question q1 = new Question("tf", "Right answer is True", "True");
		midterm.addQuestion(q1);
		Question q2 = new Question("mc", "Right answer is C", "C");
		q2.addWrongAnswer("A");
		q2.addWrongAnswer("B");
		q2.addWrongAnswer("D");
		midterm.addQuestion(q2);
		cis422.addQuiz(midterm);
		studySets.add(cis422);
		
		StudySet hum300 = new StudySet("HUM 300");
		studySets.add(hum300);
		StudySet math353 = new StudySet("MATH 353");
		studySets.add(math353);
		
		launch(args);
	}
	
	/** Overriding the default start() behavior. */
	@Override
	public void start(Stage stage) {
		try {
			LoginWindow loginWindow = new LoginWindow();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}