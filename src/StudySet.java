
import java.util.ArrayList;
import java.util.List;

public class StudySet {
	private String name;
	private List<Quiz> quizzes;
	private List<Deck> decks;
	
	public StudySet(String name, List<Quiz> quizzes, List<Deck> decks){
		setName(name);
		this.quizzes = new ArrayList<Quiz>(quizzes);
		this.decks = new ArrayList<Deck>(decks);
	}
	
	public StudySet(String name){
		this(name, new ArrayList<Quiz>(), new ArrayList<Deck>());
	}
	
	public StudySet(){
		this("");
	}
	
	//clone go here
	
	//setters
	public void setName(String name){
		if(name == null){
			this.name = "";
		}else{
			this.name = name;
		}
	}
	
	public void addQuiz(Quiz quiz){
		quizzes.add(quiz);
	}
	
	public void addDeck(Deck deck){
		decks.add(deck);
	}
	
	
	//getters
	public String getName(){
		return name;
	}
	
	public List<Quiz> getQuizzes(){
		return quizzes;
	}
	
	public List<Deck> getDecks(){
		return decks;
	}
}
