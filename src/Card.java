
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
