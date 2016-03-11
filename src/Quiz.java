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
 * A custom data-type that represents a Quiz. Currently, a Quiz can contain any number of questions
 * and has a name.
 * @author jack
 */
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

	@Override
	public String toString() {
		return name + "    (Quiz, " + questions.size() + " questions)";
	}

	public boolean export(String path){
		if(path == null){
			return false;
		}

		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuild = dbf.newDocumentBuilder();

			Document dQuiz = dBuild.newDocument();
			Element dQuizRoot = dQuiz.createElement("quiz");
			dQuiz.appendChild(dQuizRoot);

			Element name = dQuiz.createElement("name");
			name.appendChild(dQuiz.createTextNode(getName()));
			dQuizRoot.appendChild(name);

			Element question, type, prompt, rightAnswer, wrongAnswer;
			Element stat, date, score;

			for(int j=0;j<getQuestions().size();j++){
				question = dQuiz.createElement("question");
				dQuizRoot.appendChild(question);

				type = dQuiz.createElement("type");
				type.appendChild(dQuiz.createTextNode(getQuestions().get(j).getType()));
				question.appendChild(type);

				prompt = dQuiz.createElement("prompt");
				prompt.appendChild(dQuiz.createTextNode(getQuestions().get(j).getQuestion()));
				question.appendChild(prompt);

				rightAnswer = dQuiz.createElement("rightAnswer");
				rightAnswer.appendChild(dQuiz.createTextNode(getQuestions().get(j).getRightAnswer()));
				question.appendChild(rightAnswer);
				//iterate through wrong answers
				for(int k = 0; k<getQuestions().get(j).getWrongAnswers().size();k++){
					wrongAnswer = dQuiz.createElement("wrongAnswer");
					wrongAnswer.appendChild(dQuiz.createTextNode(getQuestions().get(j).getWrongAnswers().get(k)));
					question.appendChild(wrongAnswer);
				}

			}
			//iterate through stats
			for(int j = 0;j<getStats().size();j++){
				stat = dQuiz.createElement("stat");
				dQuizRoot.appendChild(stat);

				date = dQuiz.createElement("date");
				date.appendChild(dQuiz.createTextNode(getStats().get(j).getDate()));
				stat.appendChild(date);

				score = dQuiz.createElement("score");
				score.appendChild(dQuiz.createTextNode(Integer.toString(getStats().get(j).getScore())));
				stat.appendChild(score);

			}

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer trFo = tf.newTransformer();
			DOMSource dIn = new DOMSource(dQuiz);
			StreamResult dOut = new StreamResult(new File(path));
			trFo.transform(dIn, dOut);

		}catch(Exception e){
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean quizImport(String path){
		if(path == null){
			return false;
		}

		File setSource = new File(path);

		try{

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuild;
			dBuild = dbf.newDocumentBuilder();
			Document dQuiz = dBuild.parse(setSource);

			//System.out.println(quizEle.getElementsByTagName("name").item(0).getTextContent());
			setName(dQuiz.getElementsByTagName("name").item(0).getTextContent());
			NodeList questionList = dQuiz.getElementsByTagName("question");

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
					addQuestion(question);
				}
			}

			NodeList statList = dQuiz.getElementsByTagName("stat");

			for(int j = 0; j< statList.getLength();j++){
				Node statNode = statList.item(j);

				if(statNode.getNodeType()==Node.ELEMENT_NODE){
					Element statEle = (Element) statNode;
					addStat(new QuizStat(Integer.parseInt(statEle.getElementsByTagName("score").item(0).getTextContent()),
							statEle.getElementsByTagName("date").item(0).getTextContent()));
				}
			}

		}catch(Exception e){
			e.printStackTrace();
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
