import java.util.Optional;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 * The "home" window of the program. From here, StudySets can be created, opened, edited, or
 * deleted as well as some other operations. Functionality that is dependent upon user-input is
 * handled here while any other processing is handled in other classes.
 * @author richard
 */
public class PrimaryWindow {

	/**
	 * Creates an instance of PrimaryWindow, building all the relevant GUI components and adding
	 * them to a Scene to be displayed on the Stage.
	 */
	public PrimaryWindow() {
		// layout manager
		GridPane layout = new GridPane();
		layout.setHgap(20);
		layout.setVgap(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		Scene scene = new Scene(layout, 600, 450);

		// logo image
		Image logo = new Image(Main.class.getResourceAsStream("logo.png"));

		// GUI components
		ImageView logoImage = new ImageView(logo);
		Pane padding1 = new Pane();
		Label userLabel = new Label("Guest"),
				studySetsLabel = new Label("StudySets");
		ListView<StudySet> studySetsList = new ListView<StudySet>(Main.loadedStudySets);
		Button settingsButton = new Button("Settings"),
				exitButton = new Button("Exit"),
				addButton = new Button("Add"),
				openButton = new Button("Open"),
				editButton = new Button("Edit"),
				deleteButton = new Button("Delete");

		// user image
		GridPane.setConstraints(logoImage, 0, 0, 1, 2, HPos.LEFT, VPos.TOP);

		// blank pane
		padding1.setPrefSize(100, 60);
		GridPane.setConstraints(padding1, 0, 1);

		// user label
		userLabel.setPrefHeight(20);
		GridPane.setConstraints(userLabel, 0, 2, 1, 1, HPos.CENTER, VPos.TOP);

		// settings button
		settingsButton.setPrefWidth(80);
		settingsButton.setOnAction(event -> {
			settings();
		});
		GridPane.setConstraints(settingsButton, 0, 3, 1, 1, HPos.CENTER, VPos.TOP);

		// exit button
		exitButton.setPrefWidth(80);
		exitButton.setOnAction(event -> {
			// exit the program
			Main.closeScene();
		});
		GridPane.setConstraints(exitButton, 0, 4, 1, 1, HPos.CENTER, VPos.TOP, Priority.NEVER, Priority.ALWAYS);

		// StudySets label
		studySetsLabel.setPrefHeight(20);
		GridPane.setConstraints(studySetsLabel, 1, 0, 1, 1, HPos.LEFT, VPos.BOTTOM);

		// StudySets list view
		studySetsList.setPrefSize(450, 300);
		GridPane.setConstraints(studySetsList, 1, 1, 4, 4, HPos.LEFT, VPos.TOP, Priority.ALWAYS, Priority.ALWAYS);

		// add button
		addButton.setPrefWidth(80);
		addButton.setOnAction(event -> {
			addStudySet();
		});
		GridPane.setConstraints(addButton, 1, 5, 1, 1, HPos.LEFT, VPos.TOP);

		// open button
		openButton.setPrefWidth(80);
		openButton.disableProperty().bind(studySetsList.getSelectionModel().selectedItemProperty().isNull());
		openButton.setOnAction(event -> {
			openStudySet(studySetsList.getSelectionModel().getSelectedItem());
		});
		GridPane.setConstraints(openButton, 2, 5, 1, 1, HPos.RIGHT, VPos.TOP, Priority.ALWAYS, Priority.NEVER);

		// edit button
		editButton.setPrefWidth(80);
		editButton.disableProperty().bind(studySetsList.getSelectionModel().selectedItemProperty().isNull());
		editButton.setOnAction(event -> {
			editStudySet(studySetsList.getSelectionModel().getSelectedItem());
			// manually refresh the GUI list
			studySetsList.setItems(Main.loadedStudySets);
		});
		GridPane.setConstraints(editButton, 3, 5);

		// delete button
		deleteButton.setPrefWidth(80);
		deleteButton.disableProperty().bind(studySetsList.getSelectionModel().selectedItemProperty().isNull());
		deleteButton.setOnAction(event -> {
			deleteStudySet(studySetsList.getSelectionModel().getSelectedItem());
		});
		GridPane.setConstraints(deleteButton, 4, 5);

		// build and display the window
		layout.getChildren().addAll(logoImage, padding1, userLabel, settingsButton, exitButton, studySetsLabel, studySetsList, addButton, openButton, editButton, deleteButton);
		Main.setScene(scene);
	}

	/**
	 * Handles the behaviour for the "Add" button. Prompts the user to name the new StudySet and
	 * creates one if indicated.
	 */
	public void addStudySet() {
		// prompt the user for a name
		Dialog<String> dialog = new Dialog<String>();
		dialog.setTitle("Add a StudySet");
		TextField studySetText = new TextField();
		HBox layout = new HBox(studySetText);
		layout.setPadding(new Insets(10, 10, 10, 10));
		HBox.setHgrow(studySetText, Priority.ALWAYS);
		dialog.getDialogPane().setContent(layout);
		ButtonType addButton = new ButtonType("Add", ButtonData.YES);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);
		dialog.setResultConverter(result -> {
			if (result == addButton) {
				return studySetText.getText();
			}
			return null;
		});

		// create a StudySet with the given name
		Optional<String> studySetName = dialog.showAndWait();
		if (studySetName.isPresent()) {
			StudySet newStudySet = new StudySet(studySetName.get());
			Main.loadedStudySets.add(newStudySet);
			openStudySet(newStudySet);
		}
	}

	/**
	 * Handles the behaviour for the "Open" button. Creates a new instance of StudySetwindow with
	 * the selected StudySet loaded.
	 * @param studySet the StudySet to be loaded
	 */
	public void openStudySet(StudySet studySet) {
		StudySetWindow studySetWindow = new StudySetWindow(studySet);
	}

	/**
	 * Handles the behaviour for the "Edit" button. Prompts the user to rename the selected
	 * StudySet and commits the change.
	 * @param studySet the StudySet to be edited
	 */
	public void editStudySet(StudySet studySet) {
		// prompt the user to edit the StudySet
		Dialog<String> dialog = new Dialog<String>();
		dialog.setTitle("Edit a StudySet");
		TextField studySetText = new TextField(studySet.getName());
		HBox layout = new HBox(studySetText);
		layout.setPadding(new Insets(10, 10, 10, 10));
		HBox.setHgrow(studySetText, Priority.ALWAYS);
		dialog.getDialogPane().setContent(layout);
		ButtonType editButton = new ButtonType("Edit", ButtonData.YES);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(editButton, cancelButton);
		dialog.setResultConverter(result -> {
			if (result == editButton) {
				return studySetText.getText();
			}
			return null;
		});

		// rename the StudySet
		Optional<String> studySetName = dialog.showAndWait();
		if (studySetName.isPresent()) {
			studySet.setName(studySetName.get());
		}
	}

	/**
	 * Handles the behaviour for the "Delete" button. Prompts the user if they really mean to
	 * delete the selected StudySet.
	 * @param studySet the StudySet to be deleted
	 */
	public void deleteStudySet(StudySet studySet) {
		// prompt the user to delete the StudySet
		Dialog<Boolean> dialog = new Dialog<Boolean>();
		dialog.setTitle("Delete a StudySet");
		Label contentLabel = new Label("Are you sure you want to delete the StudySet " + studySet.getName() + "?");
		HBox layout = new HBox(contentLabel);
		layout.setPadding(new Insets(10, 10, 10, 10));
		HBox.setHgrow(contentLabel, Priority.ALWAYS);
		dialog.getDialogPane().setContent(layout);
		ButtonType deleteButton = new ButtonType("Delete", ButtonData.YES);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(deleteButton, cancelButton);
		dialog.setResultConverter(result -> {
			if (result == deleteButton) {
				return true;
			}
			return null;
		});

		// act accordingly
		Optional<Boolean> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == true) {
			studySet.delete();
			Main.loadedStudySets.remove(studySet);
		}
	}

	/**
	 * Handles the behaviour for the "Settings" button. A new window is created with the option
	 * to delete all statistics (this included previous review-times and scores) and will
	 * confirm if the user means to delete them.
	 */
	public void settings() {
		// build the settings window (currently with a single button)
		Dialog<Void> dialog = new Dialog<Void>();
		dialog.setTitle("Settings");
		Button clearStatistics = new Button("Clear statistics");
		clearStatistics.setPrefWidth(160);
		clearStatistics.setOnAction(bEvent -> {
			// confirm if the user really wants to delete all statistics
			Alert confirmClear = new Alert(AlertType.CONFIRMATION);
			confirmClear.setTitle("Clear statistics");
			confirmClear.setHeaderText("Are you sure you want to delete all recorded statistics?");
			Optional<ButtonType> result = confirmClear.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				for (StudySet s : Main.loadedStudySets) {
					// iterate through all StudySets and delete the review-times and scores
					for (Deck d : s.getDecks()) { for (Card c : d.getCards()) { c.setReviewTime(0); } }
					for (Quiz q : s.getQuizzes()) { q.getStats().clear(); }
				}
			}
		});
		HBox layout = new HBox(clearStatistics);
		layout.setPadding(new Insets(10, 10, 10, 10));
		dialog.getDialogPane().setContent(layout);
		ButtonType doneButton = new ButtonType("Done", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(doneButton);
		dialog.showAndWait();
	}
}