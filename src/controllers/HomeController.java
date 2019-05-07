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
import javafx.stage.Stage;

public class HomeController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button all_clients_button;

	@FXML
	private Button add_new_client_button;

	@FXML
	void initialize() {
		add_new_client_button.setOnAction(event -> {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getClassLoader().getResource("\\views\\addNew.fxml"));

			try {
				loader.load();
			} catch (IOException e) {
				Alerts.infoBox(e.toString(), "sql error");
			}

			Parent root = loader.getRoot();
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.showAndWait();
		});
		
		all_clients_button.setOnAction(event -> {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getClassLoader().getResource("\\views\\AllClients.fxml"));

			try {
				loader.load();
			} catch (IOException e) {
				Alerts.infoBox(e.toString(), "error");
			}

			Parent root = loader.getRoot();
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.showAndWait();
		});
	}
}
