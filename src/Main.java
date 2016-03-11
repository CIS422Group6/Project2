import java.util.Stack;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main class for data and window management of the program. Existing StudySets are loaded into
 * program memory and the home window is created. Most methods are static and can be accessed by
 * the rest of the program to assist in the window management.
 * @author richard
 */
public class Main extends Application {
	// window management, using a single stage and swapping out scenes as needed
	public static Stage stage;
	public static Stack<Scene> scenes;

	// global list of loaded StudySets
	public static ObservableList<StudySet> loadedStudySets;

	/**
	 * Main method of the program.
	 */
	public static void main(String[] args) {
		// load the StudySets from the current directory and build a list
		loadedStudySets = FXCollections.observableArrayList(StudySet.loadAll());
		// start the application
		launch(args);
	}

	/**
	 * Since Main extends Application and is the managing class, we override start() to setup the
	 * Stage and Scenes and begin window creation with PrimaryWindow.
	 */
	@Override
	public void start(Stage stage) {
		// window properties
		Main.stage = stage;
		Main.stage.setTitle("StudyCompanion");
		Main.stage.setMinWidth(600);
		Main.stage.setMinHeight(450);
		Main.stage.show();
		// initialize scene manager
		Main.scenes = new Stack<Scene>();
		PrimaryWindow window = new PrimaryWindow();
	}

	/**
	 * Stores and applies a new Scene onto the Stage.
	 * @param scene new scene to be applied
	 */
	public static void setScene(Scene scene) {
		scenes.add(scene);
		stage.setScene(scene);
	}

	/**
	 * Reverts the previous Scene to the Stage.
	 */
	public static void closeScene() {
		scenes.pop();
		if (scenes.isEmpty()) {
			// if no scenes are left, exit the program and write the changes back
			for (StudySet studySet : loadedStudySets) {
				studySet.save();
			}
			stage.close();
		} else {
			// otherwise 
			stage.setScene(scenes.peek());
		}
	}
}