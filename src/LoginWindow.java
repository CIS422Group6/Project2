import java.util.Optional;

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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Description.
 */

public class LoginWindow {
	
	/** Description. */
	public LoginWindow() throws Exception {
		Stage stage = new Stage();
		stage.setTitle("StudyCompanion v0.1");
		stage.setResizable(false);
		
		HBox layout = new HBox();
		layout.setSpacing(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		
		// list of user account
		ObservableList<String> users = FXCollections.observableArrayList("Richie", "Jack", "Honglu", "Jack", "Hedong", "Create a new user...");
		// declare GUI components
		ComboBox<String> userSelection = new ComboBox<String>(users);
		Button login = new Button("Login");

		// user selection
		userSelection.setPrefWidth(180);
		userSelection.setVisibleRowCount(10);
		userSelection.valueProperty().addListener((change, oldV, newV) -> {
			login.setDisable(false);
			if (newV == "Create a new user...") {
				login.setText("Create");
			} else {
				login.setText("Login");
			}
		});

		// login button
		login.setPrefWidth(80);
		login.setDisable(true);
		login.setOnAction(event -> {
			if (login.getText() == "Login") {
				// login as user
				login(userSelection.getSelectionModel().getSelectedItem());
				stage.close();
			} else {
				// register a new user
				registerUser();
			}
		});
		
		layout.getChildren().addAll(userSelection, login);

		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
	
	public void login(String username) {
		PrimaryWindow window = new PrimaryWindow(username);
	}
	
	public void registerUser() {
		Dialog<String> registerWindow = new Dialog<String>();
		registerWindow.setTitle("Create user");
		//register.setHeaderText("text");
		//register.setContentText("text");
		VBox layout = new VBox();
		layout.setPadding(new Insets(10, 10, 10, 10));
		TextField username = new TextField();
		layout.getChildren().add(username);
		GridPane.setFillWidth(username, true);
		registerWindow.getDialogPane().setContent(layout);
		ButtonType create = new ButtonType("Create", ButtonData.APPLY);
		ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		registerWindow.setResultConverter(result -> {
			if (result == create) {
				return username.getText();
			}
			return null;
		});
		registerWindow.getDialogPane().getButtonTypes().addAll(create, cancel);
		Optional<String> user = registerWindow.showAndWait();
		if (user.isPresent()) {
			System.out.println("username: \"" + user.get() + "\"");
		} else {
			System.out.println("cancelled");
		}
	}
}