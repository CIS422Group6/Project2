import java.util.ArrayList;
import java.util.List;

public class Quiz {
	private String name;
	private List<Question> questions;
	private List<QuizStat> stats;
	
	//fully explicit constructor
	public Quiz(String name, List<Question> questions){
		setName(name);
		this.questions = new ArrayList<Question>(questions);
		this.stats = new ArrayList<QuizStat>();
	}
	
	//half explicit constructor, only defining name
	public Quiz(String name){
		this(name,new ArrayList<Question>());
	}
	
	//empty constructor
	public Quiz(){
		this("");
	}
	
	//clone function will go here
	
	//setters
	public void setName(String name){
		if(name == null){
			this.name = "";
		}else{
			this.name = name;
		}
	}
	
	public void addQuestion(Question question){
		questions.add(question);
	}
	
	public void addStat(QuizStat stat){
		this.stats.add(stat);
	}
	
	//getters
	public String getName(){
		return name;
	}
	
	public List<Question> getQuestions(){
		return questions;
	}
	
	public List<QuizStat> getStats(){
		return stats;
	}
	
	
	
	
}
