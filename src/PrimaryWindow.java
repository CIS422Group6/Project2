import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
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
import javafx.stage.Stage;

/**
 * Description.
 */

public class PrimaryWindow {
	Stage stage, parentStage;

	/** Description. */
	public PrimaryWindow(String username, Stage parentStage) {
		// window properties
		this.parentStage = parentStage;
		stage = new Stage();
		stage.setTitle("StudyCompanion");
		stage.setMinWidth(600);
		stage.setMinHeight(450);

		// layout manager
		GridPane layout = new GridPane();
		//layout.setGridLinesVisible(true);
		layout.setHgap(20);
		layout.setVgap(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		Scene scene = new Scene(layout, 600, 450);

		// StudySets list
		ObservableList<StudySet> studySetsCells = FXCollections.observableArrayList(Main.sampleStudySets);
		
		// GUI components
		Rectangle userImage = new Rectangle();
		Pane space1 = new Pane();
		Label userLabel = new Label(username),
				studySetsLabel = new Label("StudySets");
		ListView<StudySet> studySetsList = new ListView<StudySet>(studySetsCells);
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
			// TODO settings button
		});
		GridPane.setConstraints(settingsButton, 0, 3, 1, 1, HPos.CENTER, VPos.TOP);

		// exit button
		exitButton.setPrefWidth(80);
		exitButton.setOnAction(event -> {
			// TODO exit/logout button
			stage.close();
			parentStage.show();
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
			StudySet newStudySet = new StudySet();
			Optional<String> studySetName = editStudySet(newStudySet);
			if (studySetName.isPresent()) {
				newStudySet.setName(studySetName.get());
				studySetsCells.add(newStudySet);
				openStudySet(newStudySet);
			}
		});
		GridPane.setConstraints(addButton, 1, 5, 1, 1, HPos.LEFT, VPos.TOP);
		
		// open button
		openButton.setPrefWidth(80);
		openButton.disableProperty().bind(studySetsList.getSelectionModel().selectedItemProperty().isNull());
		openButton.setOnAction(event -> {
			openStudySet(studySetsList.getSelectionModel().getSelectedItem());
			stage.close();
		});
		GridPane.setConstraints(openButton, 2, 5, 1, 1, HPos.RIGHT, VPos.TOP, Priority.ALWAYS, Priority.NEVER);
		
		// edit button
		editButton.setPrefWidth(80);
		editButton.disableProperty().bind(studySetsList.getSelectionModel().selectedItemProperty().isNull());
		editButton.setOnAction(event -> {
			StudySet selectedStudySet = studySetsList.getSelectionModel().getSelectedItem();
			Optional<String> studySetName = editStudySet(selectedStudySet);
			if (studySetName.isPresent()) {
				selectedStudySet.setName(studySetName.get());
				studySetsList.refresh();
			} else {
				System.out.println("cancelled");
			}
		});
		GridPane.setConstraints(editButton, 3, 5, 1, 1, HPos.RIGHT, VPos.TOP);
		
		// delete button
		deleteButton.setPrefWidth(80);
		deleteButton.disableProperty().bind(studySetsList.getSelectionModel().selectedItemProperty().isNull());
		deleteButton.setOnAction(event -> {
			StudySet selectedStudySet = studySetsList.getSelectionModel().getSelectedItem();
			Optional<Boolean> confirmDeletion = deleteStudySet(selectedStudySet);
			if (confirmDeletion.isPresent() && confirmDeletion.get() == true) {
				studySetsCells.remove(selectedStudySet);
			} else {
				System.out.println("cancelled");
			}
		});
		GridPane.setConstraints(deleteButton, 4, 5, 1, 1, HPos.RIGHT, VPos.TOP);
		
		// build and display the window
		layout.getChildren().addAll(userImage, space1, userLabel, settingsButton, exitButton, studySetsLabel, studySetsList, addButton, openButton, editButton, deleteButton);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
	
	public void openStudySet(StudySet studySet) {
		StudySetWindow studySetWindow = new StudySetWindow(studySet, stage);
		stage.hide();
	}
	
	public Optional<String> editStudySet(StudySet studySet) {
		Dialog<String> editWindow = new Dialog<String>();
		if (studySet.getName() == "") {
			editWindow.setTitle("Add a new StudySet");
		} else {
			editWindow.setTitle("Edit a StudySet");
		}
		TextField studySetText = new TextField(studySet.getName());
		HBox layout = new HBox(studySetText);
		layout.setPadding(new Insets(10, 10, 10, 10));
		HBox.setHgrow(studySetText, Priority.ALWAYS);
		editWindow.getDialogPane().setContent(layout);
		ButtonType editButton = new ButtonType("Edit", ButtonData.YES);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		editWindow.getDialogPane().getButtonTypes().addAll(editButton, cancelButton);
		editWindow.setResultConverter(result -> {
			if (result == editButton) {
				return studySetText.getText();
			}
			return null;
		});
		return editWindow.showAndWait();
	}
	
	public Optional<Boolean> deleteStudySet(StudySet studySet) {
		Dialog<Boolean> deleteWindow = new Dialog<Boolean>();
		deleteWindow.setTitle("Delete a StudySet");
		Label contentLabel = new Label("Are you sure you want to delete the StudySet " + studySet.getName() + "?");
		HBox layout = new HBox(contentLabel);
		layout.setPadding(new Insets(10, 10, 10, 10));
		HBox.setHgrow(contentLabel, Priority.ALWAYS);
		deleteWindow.getDialogPane().setContent(layout);
		ButtonType deleteButton = new ButtonType("Delete", ButtonData.YES);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		deleteWindow.getDialogPane().getButtonTypes().addAll(deleteButton, cancelButton);
		deleteWindow.setResultConverter(result -> {
			if (result == deleteButton) {
				return true;
			}
			return null;
		});
		return deleteWindow.showAndWait();
	}
}