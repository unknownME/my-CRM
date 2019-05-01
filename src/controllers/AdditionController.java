
package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import animations.Alerts;
import application.Client;
import database.ClientsDbHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class AdditionController {

	private FileChooser fileChooser;
	private File file;
	private Stage primaryStage;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField additional_name_field;

	@FXML
	private TextField additional_last_name_field;

	@FXML
	private TextField additional_patronymic_field;

	@FXML
	private TextField additional_age_field;

	@FXML
	private TextField additional_activity_field;

	@FXML
	private TextField additional_massageType_field;

	@FXML
	private TextField additional_disease_field;

	@FXML
	private TextArea additional_recomendations_field;

	@FXML
	private DatePicker additional_rgistrationDate_field;

	@FXML
	private RadioButton additional_male_radio;

	@FXML
	private RadioButton additional_female_radio;

	@FXML
	private Button additional_uploadPhoto_button;

	@FXML
	private Button additional_addClient_button;

	@FXML
	void initialize() throws FileNotFoundException {
		addNewClient();
	}

	private void addNewClient() throws FileNotFoundException {
		ClientsDbHandler dbh = new ClientsDbHandler();

		additional_uploadPhoto_button.setOnAction(event -> {
			fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg"));
			file = fileChooser.showOpenDialog(primaryStage);

		});

		additional_addClient_button.setOnAction(event -> {

			String gender;
			if (additional_female_radio.isSelected()) {
				gender = "Female";
			} else {
				gender = "Male";
			}

			String registrationDate;

			if (additional_rgistrationDate_field.getValue() == null) {
				registrationDate = null;
			} else {
				registrationDate = additional_rgistrationDate_field.getValue()
						.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			}

			String firstName = additional_name_field.getText();
			String lastName = additional_last_name_field.getText();
			String patronymic = additional_patronymic_field.getText();
			String age = additional_age_field.getText();
			String activity = additional_activity_field.getText();
			String massagetype = additional_massageType_field.getText();
			String diseases = additional_disease_field.getText();
			String recommendations = additional_recomendations_field.getText();

			if (!isInteger(additional_age_field.getText())) {
				Alerts.infoBox("age should be a number!", "warning");
				return;
			}

			Client client = new Client(firstName, lastName, patronymic, age, activity, massagetype, diseases,
					recommendations);

			try {
				if (file == null) {
					dbh.addNewClient(client, gender, registrationDate, null);
				} else {
					dbh.addNewClient(client, gender, registrationDate, new FileInputStream(file));
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				additional_addClient_button.getScene().getWindow().hide();
				Alerts.infoBox("New client added successfully!", "info");
			}

		});
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}

}
