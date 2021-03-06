/**
 * A custom data-type that represents a Card. Currently, a Card contains a question, answer, and
 * review time that represents when the card should next be reviewed again.
 * @author jack
 */
public class Card {
	private String question, answer;
	int reviewTime;

	//explicit constructor
	public Card(String question, String answer){
		setQuestion(question);
		setAnswer(answer);
		setReviewTime(0);
	}

	//blank constructor
	public Card(){
		this("","");
	}

	//return clone of this card
	public Card clone(){
		return new Card(getQuestion(),getAnswer());
	}
	
	public String toString() {
		return question;
	}
	
	//setters
	public void setQuestion(String question){
		if (question == null) {
			this.question = "";
		} else {
			this.question = question;
		}
	}

	public void setAnswer(String answer){
		if (answer == null) {
			this.answer = "";
		} else {
			this.answer = answer;
		}
	}

	public void setReviewTime(int reviewTime){
		this.reviewTime = reviewTime;
	}

	//getters
	public String getQuestion(){
		return question;
	}

	public String getAnswer(){
		return answer;
	}

	public int getReviewTime(){
		return reviewTime;
	}

}
