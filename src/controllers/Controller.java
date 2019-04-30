package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import animations.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField login_field;

	@FXML
	private PasswordField password_field;

	@FXML
	private Button authSignInButton;

	@FXML
	void initialize() {
		
		authSignInButton.setOnAction(event -> {
			String login = login_field.getText().trim();
			String password = password_field.getText().trim();
			
			if(!login.equals("") && !password.equals("")) {
				loginUser(login, password);
			} else {
//				Alerts.infoBox("fields can't be empty", "warning");
			}
			
			authSignInButton.getScene().getWindow().hide();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getClassLoader().getResource("\\views\\app.fxml"));

			try {
				loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Parent root = loader.getRoot();
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.showAndWait();
		});
		
	}
	
	private void loginUser(String login, String password) {
		
	}
}
