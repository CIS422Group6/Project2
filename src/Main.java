import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main class for the application.
 */

public class Main extends Application {
	// sample data
	public static List<String> sampleUsers = new ArrayList<String>();
	public static List<StudySet> sampleStudySets = new ArrayList<StudySet>();
	
	/** Starting point of the application. */
	public static void main(String[] args) {
		// build sample data
		sampleUsers = Arrays.asList("Richie", "Jack", "Honglu", "Jack", "Hedong");
		
		StudySet math111 = new StudySet("MATH 111");
		Quiz finalexam = new Quiz("Final exam");
		Question f1 = new Question("tf", "1 + 1 = 2", "True");
		finalexam.addQuestion(f1);
		Question f2 = new Question("mc", "1 + 3 = ?", "4");
		f2.addWrongAnswer("2");
		f2.addWrongAnswer("5");
		f2.addWrongAnswer("13");
		finalexam.addQuestion(f2);
		Question f3 = new Question("mc", "1 - 1", "0");
		f3.addWrongAnswer("2");
		finalexam.addQuestion(f3);
		math111.addQuiz(finalexam);
		sampleStudySets.add(math111);
		
		StudySet cis422 = new StudySet("CIS 422");
		Deck chap1 = new Deck("Chapter 1");
		Card c1 = new Card("What is the name of this program?", "StudyCompanion");
		chap1.addCard(c1);
		Card c2 = new Card("What is the current year?", "2016");
		chap1.addCard(c2);
		cis422.addDeck(chap1);
		Quiz midterm = new Quiz("Midterm");
		Question q1 = new Question("tf", "Are we working on project 2?", "True");
		midterm.addQuestion(q1);
		Question q2 = new Question("mc", "What did we create for our first project?", "An address book");
		q2.addWrongAnswer("A calculator");
		q2.addWrongAnswer("A scheduler");
		q2.addWrongAnswer("Nothing");
		midterm.addQuestion(q2);
		cis422.addQuiz(midterm);
		sampleStudySets.add(cis422);
		
		StudySet hum300 = new StudySet("HUM 300");
		sampleStudySets.add(hum300);
		
		// start the application
		launch(args);
	}
	
	/** Overriding the default start() behavior. */
	@Override
	public void start(Stage stage) {
		LoginWindow loginWindow = new LoginWindow();
	}
}