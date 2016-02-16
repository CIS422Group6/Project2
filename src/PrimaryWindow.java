import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Description.
 */

public class PrimaryWindow {
	
	/** Description. */
	public PrimaryWindow(String username) {
		Stage stage = new Stage();
		stage.setTitle("StudyCompanion v0.1");
		stage.setResizable(false);
		
		GridPane layout = new GridPane();
		layout.setHgap(20);
		layout.setVgap(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		
		ObservableList<HBox> studySetsCells = FXCollections.observableArrayList();
		for (StudySet i : Main.studySets) {
			HBox cell = new HBox();
			Label name;
			Button take, edit, delete;
			name = new Label(i.getName());
			Pane space = new Pane();
			HBox.setHgrow(space, Priority.ALWAYS);
			take = new Button("T");
			take.setOnAction(event -> {
				StudySetWindow window = new StudySetWindow(i);
				stage.close();
			});
			edit = new Button("E");
			delete = new Button("D");
			cell.getChildren().addAll(name, space, take, edit, delete);
			studySetsCells.add(cell);
		}
		
		Rectangle r = new Rectangle();
		r.setWidth(100);
		r.setHeight(100);
		r.setFill(Color.DARKGRAY);
		layout.add(r, 0, 0);
		
		Label usernameLabel = new Label(username);
		GridPane.setHalignment(usernameLabel, HPos.CENTER);
		layout.add(usernameLabel, 0, 1);
		
		Button settings = new Button("Settings");
		settings.setPrefWidth(80);
		GridPane.setVgrow(settings, Priority.ALWAYS);
		GridPane.setValignment(settings, VPos.BOTTOM);
		layout.add(settings, 0, 2);
		
		Button exit = new Button("Exit");
		exit.setPrefWidth(80);
		GridPane.setVgrow(exit, Priority.NEVER);
		GridPane.setValignment(exit, VPos.BOTTOM);
		layout.add(exit, 0, 3);
		
		ListView<HBox> studySetsList = new ListView<HBox>(studySetsCells);
		studySetsList.setMaxHeight(300);
		GridPane.setVgrow(studySetsList, Priority.ALWAYS);
		layout.add(studySetsList, 1, 0, 4, 4);
		
		
		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
	
	public void openStudySet(String name) {
		
	}
}
