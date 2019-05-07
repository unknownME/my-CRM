package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import animations.Alerts;
import controllers.AllClientsController.TempClient;
import database.ClientsDbHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ViewClientController extends ClientsDbHandler {

	private FileChooser fileChooser;
	private File file;
	private Stage primaryStage;
	
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField first_name_field;

	@FXML
	private TextField last_name_field;

	@FXML
	private TextField patronymic_field;

	@FXML
	private TextField age_field;

	@FXML
	private TextField activity_field;

	@FXML
	private TextField massageType_field;

	@FXML
	private TextField disease_field;

	@FXML
	private TextArea recomendations_field;

	@FXML
	private DatePicker registrationDate_field;

	@FXML
	private RadioButton male_radio;

	@FXML
	private RadioButton female_radio;

	@FXML
	private Button uploadPhoto_button;

	@FXML
	private Button save_button;

	@FXML
	private ImageView photo_image_view;

	@FXML
	private DatePicker last_massage_date_field;

	@FXML
	void initialize() throws FileNotFoundException {
		TempClient tc = AllClientsController.getConcreteClient();
		ResultSet rs = getConcreteClient(tc);

		try {
			while (rs.next()) {
				InputStream is = rs.getBinaryStream(14);
				if (is != null) {

					OutputStream os = new FileOutputStream(new File("img.jpg"));
					byte[] content = new byte[4096];
					int size = 0;

					try {
						while ((size = is.read(content)) != -1) {
							os.write(content, 0, size);
						}
					} catch (IOException e) {
						Alerts.infoBox(e.toString(), "error");
					}

					try {
						os.close();
						is.close();
					} catch (IOException e) {
						Alerts.infoBox(e.toString(), "error");
					}

					Image imageFromDb = new Image("file:img.jpg", photo_image_view.getFitWidth(),
							photo_image_view.getFitHeight(), true, true);
					photo_image_view.setImage(imageFromDb);
					photo_image_view.setPreserveRatio(true);
				}

				String tFirstName = rs.getString(3);
				String tLastName = rs.getString(4) == null ? "" : rs.getString(4);
				String tPatronymicName = rs.getString(5) == null ? "" : rs.getString(5);
				String tAge = rs.getString(6);
				String tGender = rs.getString(7);
				if (tGender.equals("Male")) {
					male_radio.setSelected(true);
				} else {
					female_radio.setSelected(true);
				}
				String tActivity = rs.getString(8) == null ? "" : rs.getString(8);
				String tMassageType = rs.getString(9) == null ? "" : rs.getString(9);
				String tDiseases = rs.getString(10) == null ? "" : rs.getString(10);
				String tRecommendations = rs.getString(11) == null ? "" : rs.getString(11);
				String tRegistrationDate = rs.getDate(12) == null ? ""
						: AllClientsController.dateFormat.format(rs.getDate(12));
				String tLastMassageDate = rs.getDate(13) == null ? ""
						: AllClientsController.dateFormat.format(rs.getDate(13));
			}

		} catch (SQLException e) {
			Alerts.infoBox(e.toString(), "sql error");
		}

	}
}
