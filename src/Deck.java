
import java.util.ArrayList;
import java.util.List;

public class Deck {
	private String name;
	private List<Card> cards;
	
	//fully explicit constructor
	public Deck(String name, List<Card> cards){
		setName(name);
		this.cards = new ArrayList<Card>(cards);
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
	
	//getters
	public String getName(){
		return name;
	}
	
	public List<Card> getCards(){
		return cards;
	}

}
