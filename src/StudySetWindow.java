import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class StudySetWindow {

	public StudySetWindow(StudySet studySet) {
		Stage stage = new Stage();
		stage.setTitle("StudyCompanion v0.1");
		stage.setResizable(false);
		
		GridPane layout = new GridPane();
		layout.setHgap(20);
		layout.setVgap(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		
		ObservableList<HBox> studyMaterialsCells = FXCollections.observableArrayList();
		for (Deck d : studySet.getDecks()) {
			HBox cell = new HBox();
			Label name;
			Button take, edit, delete, add;
			name = new Label(d.getName());
			Pane space = new Pane();
			HBox.setHgrow(space, Priority.ALWAYS);
			take = new Button("T");
			take.setOnAction(event -> {
				DeckWindow window = new DeckWindow(d);
				stage.close();
			});
			edit = new Button("E");
			delete = new Button("D");
			cell.getChildren().addAll(name, space, take, edit, delete);
			studyMaterialsCells.add(cell);
		}
		for (Quiz q : studySet.getQuizzes()) {
			HBox cell = new HBox();
			Label name;
			Button take, edit, delete, add;
			name = new Label(q.getName());
			Pane space = new Pane();
			HBox.setHgrow(space, Priority.ALWAYS);
			take = new Button("T");
			take.setOnAction(event -> {
				QuizWindow window = new QuizWindow(q);
				stage.close();
			});
			edit = new Button("E");
			delete = new Button("D");
			cell.getChildren().addAll(name, space, take, edit, delete);
			studyMaterialsCells.add(cell);
		}
		
		ListView<HBox> studyMaterialsList = new ListView<HBox>(studyMaterialsCells);
		studyMaterialsList.setMaxHeight(300);
		GridPane.setVgrow(studyMaterialsList, Priority.ALWAYS);
		layout.add(studyMaterialsList, 0, 0);
		
		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
}