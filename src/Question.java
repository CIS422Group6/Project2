import java.util.ArrayList;
import java.util.List;

/**
 * A custom data-type that represents a Question. Currently, a Question contains a question, type
 * (t/f, mc, txt), a single right answer and any number of wrong answer(s).
 * @author jack
 */
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

	public String toString() {
		String text = question;
		if (type.equals("tf")) text += " (T/F)";
		else if (type.equals("mc")) text += " (MC)";
		else if (type.equals("txt")) text += " (TXT)";
		return text;
	}

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
	public void clearWrongAnswers() {
		wrongAnswers.clear();
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
	//checks if a given string (intended to be user input) is the same as correct answer, white space removed
	public boolean compareAnswer(String response){
		if(response.replaceAll("\\s", "").toLowerCase().equals(rightAnswer.replaceAll("\\s", "").toLowerCase())){
			return true;
		}else{
			return false;
		}
	}
}
