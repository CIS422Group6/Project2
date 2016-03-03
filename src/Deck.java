
import java.util.ArrayList;
import java.util.List;

public class Deck {
	private String name, sessionDate;
	private List<Card> cards;
	
	//fully explicit constructor
	public Deck(String name, List<Card> cards){
		setName(name);
		this.cards = new ArrayList<Card>(cards);
		sessionDate = "2016/01/15";
	}
	
	//half explicit constructor, only defines a name
	public Deck(String name){
		this(name, new ArrayList<Card>());
	}
	
	//empty constructor
	public Deck(){
		this("");
	}
	
	//clone will go gere
	
	@Override
	public String toString() {
		return name + "\t(Deck, " + cards.size() + " cards)";
	}
	
	//setters
	public void setName(String name){
		if(name == null){
			this.name = "";
		}else{
			this.name = name;
		}
	}
	
	public void addCard(Card card){
		cards.add(card);
	}
	
	public void setDate(String date){
		if(date == null){
			sessionDate = "";
		}else{
			sessionDate = date;
		}
	}
	
	//getters
	public String getName(){
		return name;
	}
	
	public List<Card> getCards(){
		return cards;
	}
	
	public String getDate(){
		return sessionDate;
	}

}
