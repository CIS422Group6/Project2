import java.util.Optional;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * Description.
 */

public class LoginWindow {
	Stage stage;
	
	/** Description. */
	public LoginWindow() {
		// window properties
		stage = new Stage();
		stage.setTitle("StudyCompanion");
		stage.setResizable(false);
		
		// layout manager
		HBox layout = new HBox();
		layout.setSpacing(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		Scene scene = new Scene(layout);
		
		// Users list
		ObservableList<String> users = FXCollections.observableArrayList(Main.sampleUsers);
		users.add("Create a new user...");
		
		// GUI components
		ComboBox<String> usersCombo = new ComboBox<String>(users);
		Button loginButton = new Button("Login");

		// Users selection ComboBox
		usersCombo.setPrefWidth(180);
		usersCombo.setVisibleRowCount(10);
		usersCombo.setPromptText("Select a user");
		//userSelection.valueProperty().addListener((change, oldV, newV) -> System.out.println(userSelection.getSelectionModel().isEmpty()));

		// login button
		loginButton.setPrefWidth(80);
		loginButton.disableProperty().bind(usersCombo.valueProperty().isNull());
		loginButton.textProperty().bind(Bindings.when(usersCombo.valueProperty().isEqualTo("Create a new user..."))
				.then("Create").otherwise("Login"));
		loginButton.setOnAction(event -> {
			if (loginButton.getText() == "Login") {
				// login as the selected user
				login(usersCombo.getValue());
				usersCombo.getSelectionModel().clearSelection();
			} else {
				// register a new user
				Optional<String> username = registerUser();
				if (username.isPresent()) {
					System.out.println("username: \"" + username.get() + "\"");
					login(username.get());
					usersCombo.getSelectionModel().clearSelection();
				} else {
					System.out.println("cancelled");
				}
			}
		});
		
		// build and display the window
		layout.getChildren().addAll(usersCombo, loginButton);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
	
	public void login(String username) {
		PrimaryWindow primaryWindow = new PrimaryWindow(username, stage);
		stage.hide();
	}
	
	public Optional<String> registerUser() {
		Dialog<String> registerWindow = new Dialog<String>();
		registerWindow.setTitle("Create user");
		HBox layout = new HBox();
		layout.setPadding(new Insets(10, 10, 10, 10));
		TextField userText = new TextField();
		HBox.setHgrow(userText, Priority.ALWAYS);
		layout.getChildren().add(userText);
		registerWindow.getDialogPane().setContent(layout);
		
		ButtonType createButton = new ButtonType("Create", ButtonData.APPLY);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		registerWindow.setResultConverter(result -> {
			if (result == createButton) {
				return userText.getText();
			}
			return null;
		});
		registerWindow.getDialogPane().getButtonTypes().addAll(createButton, cancelButton);
		
		return registerWindow.showAndWait();
	}
}