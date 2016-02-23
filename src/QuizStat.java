
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class QuizStat {
	private String date;
	private int score;
	
	//fully explicit constructor
	public QuizStat(int score, String date){
		setScore(score);
		setDate(date);
	}
	
	//half explicit constructor, builds with a specific score
	public QuizStat(int score){
		setScore(score);
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Date dateObj = new Date();
		setDate(df.format(dateObj));
		
	}
	
	//blank constructor
	public QuizStat(){
		this(0);
	}
	
	//setters
	public void setDate(String date){
		this.date = date;
	}
	
	public void setScore(int score){
		this.score = score;
	}
	
	//getters
	public String getDate(){
		return this.date;
	}
	
	public int getScore(){
		return this.score;
	}

}
