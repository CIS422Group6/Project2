
import java.util.ArrayList;
import java.util.List;
import java.io.File;
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

public class StudySet {
	private String name, path;
	private List<Quiz> quizzes;
	private List<Deck> decks;

	public StudySet(String name, List<Quiz> quizzes, List<Deck> decks){
		setName(name);
		setPath("");
		this.quizzes = new ArrayList<Quiz>(quizzes);
		this.decks = new ArrayList<Deck>(decks);

	}

	public StudySet(String name){
		this(name, new ArrayList<Quiz>(), new ArrayList<Deck>());
	}

	public StudySet(){
		this("StudySet");
	}

	public StudySet clone() {
		return new StudySet(this.name, this.quizzes, this.decks);
	}

	@Override
	public String toString() {
		return name;
	}

	//setters
	public void setName(String name){
		if(name == null){
			this.name = "StudySet";
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
	public void setPath(String path){
		if(path == null){
			this.path = "";
		}else{
			this.path = path;
		}
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

	public String getPath(){
		return path;
	}

	//save function
	public void save(){
		//if no path is specified as of yet, it will save locally with the name of the file being the name of the study set
		if(path.length() == 0){	
			setPath(name.replaceAll("\\s", "")+".xml");
		}
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuild = dbf.newDocumentBuilder();

			Document dSet = dBuild.newDocument();
			Element dSetRoot = dSet.createElement("studySet");
			dSet.appendChild(dSetRoot);

			Element sSetName = dSet.createElement("setName");
			sSetName.appendChild(dSet.createTextNode(name));
			dSetRoot.appendChild(sSetName);

			Element quiz, deck, name;
			Element question,type, prompt, rightAnswer, wrongAnswer;
			Element stat, date, score;
			Element card, answer, reviewTime;
			Element sessionDate;

			//iterate through quizzes
			for(int i = 0;i<quizzes.size();i++){
				quiz = dSet.createElement("quiz");
				dSetRoot.appendChild(quiz);

				name = dSet.createElement("name");
				name.appendChild(dSet.createTextNode(quizzes.get(i).getName()));
				quiz.appendChild(name);
				//iterate through questions
				for(int j=0;j<quizzes.get(i).getQuestions().size();j++){
					question = dSet.createElement("question");
					quiz.appendChild(question);

					type = dSet.createElement("type");
					type.appendChild(dSet.createTextNode(quizzes.get(i).getQuestions().get(j).getType()));
					question.appendChild(type);

					prompt = dSet.createElement("prompt");
					prompt.appendChild(dSet.createTextNode(quizzes.get(i).getQuestions().get(j).getQuestion()));
					question.appendChild(prompt);

					rightAnswer = dSet.createElement("rightAnswer");
					rightAnswer.appendChild(dSet.createTextNode(quizzes.get(i).getQuestions().get(j).getRightAnswer()));
					question.appendChild(rightAnswer);
					//iterate through wrong answers
					for(int k = 0; k<quizzes.get(i).getQuestions().get(j).getWrongAnswers().size();k++){
						wrongAnswer = dSet.createElement("wrongAnswer");
						wrongAnswer.appendChild(dSet.createTextNode(quizzes.get(i).getQuestions().get(j).getWrongAnswers().get(k)));
						question.appendChild(wrongAnswer);
					}
				}
				//iterate through stats
				for(int j = 0;j<quizzes.get(i).getStats().size();j++){
					stat = dSet.createElement("stat");
					quiz.appendChild(stat);

					date = dSet.createElement("date");
					date.appendChild(dSet.createTextNode(quizzes.get(i).getStats().get(j).getDate()));
					stat.appendChild(date);

					score = dSet.createElement("score");
					score.appendChild(dSet.createTextNode(Integer.toString(quizzes.get(i).getStats().get(j).getScore())));
					stat.appendChild(score);
				}
			}
			//iterate through decks
			for(int i = 0;i<decks.size();i++){
				deck = dSet.createElement("deck");
				dSetRoot.appendChild(deck);

				name = dSet.createElement("name");
				name.appendChild(dSet.createTextNode(decks.get(i).getName()));
				deck.appendChild(name);

				sessionDate = dSet.createElement("sessionDate");
				sessionDate.appendChild(dSet.createTextNode(decks.get(i).getDate()));
				deck.appendChild(sessionDate);
				//iterate through cards
				for(int j = 0;j<decks.get(i).getCards().size();j++){
					card = dSet.createElement("card");
					deck.appendChild(card);

					prompt = dSet.createElement("prompt");
					prompt.appendChild(dSet.createTextNode(decks.get(i).getCards().get(j).getQuestion()));
					card.appendChild(prompt);

					answer = dSet.createElement("answer");
					answer.appendChild(dSet.createTextNode(decks.get(i).getCards().get(j).getAnswer()));
					card.appendChild(answer);

					reviewTime = dSet.createElement("reviewTime");
					reviewTime.appendChild(dSet.createTextNode(Integer.toString(decks.get(i).getCards().get(j).getReviewTime())));
					card.appendChild(reviewTime);
				}
			}
			//write out
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer trFo = tf.newTransformer();
			DOMSource dIn = new DOMSource(dSet);
			StreamResult dOut = new StreamResult(new File(path));
			trFo.transform(dIn, dOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//load function, takes a String variable for a path
	//to use this load function, create a new blank StudySet, then call load(path) to read contents from file into class structures
	public void load(String path){
		// set class path variable to path argument for future save usage
		setPath(path);
		File setSource = new File(path);


		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuild;
			dBuild = dbf.newDocumentBuilder();
			Document dSet = dBuild.parse(setSource);

			setName(dSet.getElementsByTagName("setName").item(0).getTextContent());

			NodeList quizList = dSet.getElementsByTagName("quiz");
			NodeList deckList = dSet.getElementsByTagName("deck");

			for(int i = 0; i< quizList.getLength();i++){
				Node quizNode = quizList.item(i);

				if(quizNode.getNodeType() == Node.ELEMENT_NODE){
					Element quizEle = (Element) quizNode;
					Quiz quiz = new Quiz(quizEle.getElementsByTagName("name").item(0).getTextContent());
					//System.out.println(quizEle.getElementsByTagName("name").item(0).getTextContent());
					NodeList questionList = quizEle.getElementsByTagName("question");

					for(int j = 0; j < questionList.getLength();j++){
						Node questionNode = questionList.item(j);

						if(questionNode.getNodeType() == Node.ELEMENT_NODE){
							Element questionEle = (Element) questionNode;
							Question question = new Question(questionEle.getElementsByTagName("type").item(0).getTextContent(),
									questionEle.getElementsByTagName("prompt").item(0).getTextContent(),
									questionEle.getElementsByTagName("rightAnswer").item(0).getTextContent());
							NodeList wrongAnswers = questionEle.getElementsByTagName("wrongAnswer");
							for(int k = 0; k < wrongAnswers.getLength(); k++){
								Node answerNode = wrongAnswers.item(k);

								if(answerNode.getNodeType() == Node.ELEMENT_NODE){
									Element answerEle = (Element) answerNode;
									question.addWrongAnswer(answerEle.getTextContent());

								}
							}
							quiz.addQuestion(question);
						}
					}
					NodeList statList = quizEle.getElementsByTagName("stat");

					for(int j = 0; j< statList.getLength();j++){
						Node statNode = statList.item(j);

						if(statNode.getNodeType()==Node.ELEMENT_NODE){
							Element statEle = (Element) statNode;
							quiz.addStat(new QuizStat(Integer.parseInt(statEle.getElementsByTagName("score").item(0).getTextContent()),
									statEle.getElementsByTagName("date").item(0).getTextContent()));
						}
					}
					addQuiz(quiz);
				}	
			}
			for(int i = 0;i<deckList.getLength();i++){
				Node deckNode = deckList.item(i);

				if(deckNode.getNodeType() == Node.ELEMENT_NODE){
					Element deckEle = (Element) deckNode;
					Deck deck = new Deck(deckEle.getElementsByTagName("name").item(0).getTextContent());
					deck.setDate(deckEle.getElementsByTagName("sessionDate").item(0).getTextContent());

					NodeList cardList = deckEle.getElementsByTagName("card");
					for(int j = 0; j < cardList.getLength(); j++){
						Node cardNode = cardList.item(j);
						if(cardNode.getNodeType() == Node.ELEMENT_NODE){
							Element cardEle = (Element) cardNode;
							Card card = new Card(cardEle.getElementsByTagName("prompt").item(0).getTextContent(),
									cardEle.getElementsByTagName("answer").item(0).getTextContent());
							card.setReviewTime(Integer.parseInt(cardEle.getElementsByTagName("reviewTime").item(0).getTextContent()));
							deck.addCard(card);
						}		
					}
					addDeck(deck);
				}
			}
		} catch (Exception e) {
			// prevents loading imported/exported xmls
			setName("error");
			e.printStackTrace();
		}
	}
	
	public void delete() {
		File file = new File(path);
		file.delete();
	}

	public static List<StudySet> loadAll() {
		List<StudySet> studySets = new ArrayList<StudySet>();
		File folder = new File(".");
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				String filename = file.getName();
				String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
				if (extension.equals("xml")) {
					System.out.println(filename);
					StudySet studySet = new StudySet();
					studySet.load(file.getPath());
					if (studySet.getName() != "error") studySets.add(studySet);
				}
			}
		}
		return studySets;
	}
}