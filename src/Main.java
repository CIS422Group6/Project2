import java.util.Stack;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main class for data and window management.
 */

public class Main extends Application {
	// window management
	public static Stage stage;
	public static Stack<Scene> scenes;
	
	// list of StudySets
	public static ObservableList<StudySet> loadedStudySets;
	
	/** Initialize the data (StudySets) and window. */
	public static void main(String[] args) {
		// load the StudySets
		loadedStudySets = FXCollections.observableArrayList(StudySet.loadAll());
		// start the application
		launch(args);
	}
	
	/** Overriding the default start() behavior. */
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
	
	/** Update the scene. */
	public static void setScene(Scene scene) {
		scenes.add(scene);
		stage.setScene(scene);
	}
	
	public static void closeScene() {
		scenes.pop();
		if (scenes.isEmpty()) {
			saveStudySets();
			stage.close();
		} else {
			stage.setScene(scenes.peek());
		}
	}
	
	public static void saveStudySets() {
		for (StudySet studySet : loadedStudySets) {
			studySet.save();
		}
	}
}