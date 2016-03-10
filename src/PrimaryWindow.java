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
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Description.
 */

public class PrimaryWindow {

	/** Description. */
	public PrimaryWindow() {
		// layout manager
		GridPane layout = new GridPane();
		layout.setHgap(20);
		layout.setVgap(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		Scene scene = new Scene(layout, 600, 450);

		// GUI components
		Rectangle userImage = new Rectangle();
		Pane space1 = new Pane();
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
		userImage.setWidth(100);
		userImage.setHeight(100);
		userImage.setFill(Color.DARKGRAY);
		GridPane.setConstraints(userImage, 0, 0, 1, 2, HPos.LEFT, VPos.TOP);
		
		// blank pane
		space1.setPrefSize(100, 60);
		GridPane.setConstraints(space1, 0, 1);

		// user label
		userLabel.setPrefHeight(20);
		GridPane.setConstraints(userLabel, 0, 2, 1, 1, HPos.CENTER, VPos.TOP);
		
		// settings button
		settingsButton.setPrefWidth(80);
		settingsButton.setOnAction(event -> {
			Dialog<Void> settingsDialog = new Dialog<Void>();
			settingsDialog.setTitle("Settings");
			Button clearStatistics = new Button("Clear statistics");
			clearStatistics.setPrefWidth(160);
			clearStatistics.setOnAction(bEvent -> {
				Alert confirmClear = new Alert(AlertType.CONFIRMATION);
				confirmClear.setTitle("Clear settings");
				confirmClear.setHeaderText("Are you sure you want to delete all statistics for all StudySets?");
				Optional<ButtonType> result = confirmClear.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					for (StudySet s : Main.loadedStudySets) {
						for (Deck d : s.getDecks()) {
							for (Card c : d.getCards()) {
								c.setReviewTime(0);
							}
						}
						for (Quiz q : s.getQuizzes()) {
							q.getStats().clear();
						}
					}
				}
			});
			HBox settingsLayout = new HBox(clearStatistics);
			settingsLayout.setPadding(new Insets(10, 10, 10, 10));
			settingsDialog.getDialogPane().setContent(settingsLayout);
			ButtonType doneButton = new ButtonType("Done", ButtonData.OK_DONE);
			settingsDialog.getDialogPane().getButtonTypes().addAll(doneButton);
			settingsDialog.showAndWait();
		});
		GridPane.setConstraints(settingsButton, 0, 3, 1, 1, HPos.CENTER, VPos.TOP);

		// exit button
		exitButton.setPrefWidth(80);
		exitButton.setOnAction(event -> {
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
			studySetsList.refresh();
		});
		GridPane.setConstraints(editButton, 3, 5, 1, 1, HPos.RIGHT, VPos.TOP);
		
		// delete button
		deleteButton.setPrefWidth(80);
		deleteButton.disableProperty().bind(studySetsList.getSelectionModel().selectedItemProperty().isNull());
		deleteButton.setOnAction(event -> {
			deleteStudySet(studySetsList.getSelectionModel().getSelectedItem());
		});
		GridPane.setConstraints(deleteButton, 4, 5, 1, 1, HPos.RIGHT, VPos.TOP);
		
		// build and display the window
		layout.getChildren().addAll(userImage, space1, userLabel, settingsButton, exitButton, studySetsLabel, studySetsList, addButton, openButton, editButton, deleteButton);
		Main.setScene(scene);
	}
	
	public void addStudySet() {
		// prompt the user for a name
		Dialog<String> addDialog = new Dialog<String>();
		addDialog.setTitle("Add a new StudySet");
		TextField studySetText = new TextField();
		HBox layout = new HBox(studySetText);
		layout.setPadding(new Insets(10, 10, 10, 10));
		HBox.setHgrow(studySetText, Priority.ALWAYS);
		addDialog.getDialogPane().setContent(layout);
		ButtonType editButton = new ButtonType("Edit", ButtonData.YES);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		addDialog.getDialogPane().getButtonTypes().addAll(editButton, cancelButton);
		addDialog.setResultConverter(result -> {
			if (result == editButton) {
				return studySetText.getText();
			}
			return null;
		});
		
		// create a StudySet with the given name
		Optional<String> studySetName = addDialog.showAndWait();
		if (studySetName.isPresent()) {
			StudySet newStudySet = new StudySet(studySetName.get());
			Main.loadedStudySets.add(newStudySet);
			openStudySet(newStudySet);
		}
	}
	
	public void openStudySet(StudySet studySet) {
		StudySetWindow studySetWindow = new StudySetWindow(studySet);
	}
	
	public void editStudySet(StudySet studySet) {
		// prompt the user to edit the StudySet
		Dialog<String> editDialog = new Dialog<String>();
		editDialog.setTitle("Edit a StudySet");
		TextField studySetText = new TextField(studySet.getName());
		HBox layout = new HBox(studySetText);
		layout.setPadding(new Insets(10, 10, 10, 10));
		HBox.setHgrow(studySetText, Priority.ALWAYS);
		editDialog.getDialogPane().setContent(layout);
		ButtonType editButton = new ButtonType("Edit", ButtonData.YES);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		editDialog.getDialogPane().getButtonTypes().addAll(editButton, cancelButton);
		editDialog.setResultConverter(result -> {
			if (result == editButton) {
				return studySetText.getText();
			}
			return null;
		});
		
		// rename the StudySet
		Optional<String> studySetName = editDialog.showAndWait();
		if (studySetName.isPresent()) {
			studySet.setName(studySetName.get());
		}
	}
	
	public void deleteStudySet(StudySet studySet) {
		// prompt the user to delete the StudySet
		Dialog<Boolean> deleteDialog = new Dialog<Boolean>();
		deleteDialog.setTitle("Delete a StudySet");
		Label contentLabel = new Label("Are you sure you want to delete the StudySet " + studySet.getName() + "?");
		HBox layout = new HBox(contentLabel);
		layout.setPadding(new Insets(10, 10, 10, 10));
		HBox.setHgrow(contentLabel, Priority.ALWAYS);
		deleteDialog.getDialogPane().setContent(layout);
		ButtonType deleteButton = new ButtonType("Delete", ButtonData.YES);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		deleteDialog.getDialogPane().getButtonTypes().addAll(deleteButton, cancelButton);
		deleteDialog.setResultConverter(result -> {
			if (result == deleteButton) {
				return true;
			}
			return null;
		});
		
		// act accordingly
		Optional<Boolean> result = deleteDialog.showAndWait();
		if (result.isPresent() && result.get() == true) {
			studySet.delete();
			Main.loadedStudySets.remove(studySet);
		}
	}
	
	public void close() {
		
	}
}