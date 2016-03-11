import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A custom data-type that represents a Deck. Currently, a Deck can contain any number of Cards
 * and has a name along with the last session date.
 * @author jack
 */
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
		return name + "    (Deck, " + cards.size() + " cards)";
	}

	public boolean export(String path){
		if(path == null){
			return false;
		}

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuild;
		try {
			dBuild = dbf.newDocumentBuilder();
			Document dDeck = dBuild.newDocument();
			Element dDeckRoot = dDeck.createElement("Deck");
			dDeck.appendChild(dDeckRoot);

			Element name, card, sessionDate, prompt, answer, reviewTime;

			name = dDeck.createElement("name");
			name.appendChild(dDeck.createTextNode(this.getName()));
			dDeckRoot.appendChild(name);

			sessionDate = dDeck.createElement("sessionDate");
			sessionDate.appendChild(dDeck.createTextNode(this.getDate()));
			dDeckRoot.appendChild(sessionDate);
			//iterate through cards
			for(int j = 0;j<this.getCards().size();j++){
				card = dDeck.createElement("card");
				dDeckRoot.appendChild(card);

				prompt = dDeck.createElement("prompt");
				prompt.appendChild(dDeck.createTextNode(this.getCards().get(j).getQuestion()));
				card.appendChild(prompt);

				answer = dDeck.createElement("answer");
				answer.appendChild(dDeck.createTextNode(this.getCards().get(j).getAnswer()));
				card.appendChild(answer);

				reviewTime = dDeck.createElement("reviewTime");
				reviewTime.appendChild(dDeck.createTextNode(Integer.toString(this.getCards().get(j).getReviewTime())));
				card.appendChild(reviewTime);

			}

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer trFo = tf.newTransformer();
			DOMSource dIn = new DOMSource(dDeck);
			StreamResult dOut = new StreamResult(new File(path));
			trFo.transform(dIn, dOut);


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return true;
	}
	//import is reserved
	public boolean deckImport(String path){
		if(path == null){
			return false;
		}

		File setSource = new File(path);

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuild;
			dBuild = dbf.newDocumentBuilder();
			Document dDeck = dBuild.parse(setSource);
			setName(dDeck.getElementsByTagName("name").item(0).getTextContent());
			setDate(dDeck.getElementsByTagName("sessionDate").item(0).getTextContent());


			NodeList cardList = dDeck.getElementsByTagName("card");
			for(int j = 0; j < cardList.getLength(); j++){
				Node cardNode = cardList.item(j);
				if(cardNode.getNodeType() == Node.ELEMENT_NODE){
					Element cardEle = (Element) cardNode;
					Card card = new Card(cardEle.getElementsByTagName("prompt").item(0).getTextContent(),
							cardEle.getElementsByTagName("answer").item(0).getTextContent());
					card.setReviewTime(Integer.parseInt(cardEle.getElementsByTagName("reviewTime").item(0).getTextContent()));
					addCard(card);
				}		
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
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
