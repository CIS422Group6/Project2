
import java.util.ArrayList;
import java.util.List;

public class Question {
	private String type, question, rightAnswer;
	private List<String> wrongAnswers;
	
	//explicit constructor, wrong answers are added separately 
	public Question(String type, String question, String rightAnswer){
		wrongAnswers = new ArrayList<String>();
		setType(type);
		setQuestion(question);
		setRightAnswer(rightAnswer);
		
	}
	
	//empty constructor
	public Question(){
		this("","","");
	}
	
	//clone function will go here soon
	
	//setters
	public void setType(String type){
		if(type == null){
			this.type = "";
		}else{
			this.type = type;
		}
	}
	
	public void setQuestion(String question){
		if(question == null){
			this.question = "";
		}else{
			this.question = question;
		}
	}
	
	public void setRightAnswer(String answer){
		if(answer == null){
			rightAnswer = "";
		}else{
			rightAnswer = answer;
		}
	}
	public void addWrongAnswer(String answer){
		wrongAnswers.add(answer);
	}
	
	//getters
	public String getType(){
		return type;
	}
	
	public String getQuestion(){
		return question;
	}
	
	public String getRightAnswer(){
		return rightAnswer;
	}
	
	public List<String> getWrongAnswers(){
		return new ArrayList<String>(wrongAnswers);
	}
	
}
