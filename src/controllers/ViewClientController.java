package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import animations.Alerts;
import application.Client;
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
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ViewClientController extends ClientsDbHandler {

	private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private FileChooser fileChooser;
	private File file;
	private Stage primaryStage = new Stage();

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

		uploadPhoto_button.setOnAction(event -> {
			fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg"));
			file = fileChooser.showOpenDialog(primaryStage);
			if (file != null) {
				Image image = new Image(file.toURI().toString());
				photo_image_view.setImage(image);
				photo_image_view.setPreserveRatio(true);
			}
		});

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

				first_name_field.setText(tFirstName);
				last_name_field.setText(tLastName);
				patronymic_field.setText(tPatronymicName);
				age_field.setText(tAge);
				activity_field.setText(tActivity);
				massageType_field.setText(tMassageType);
				disease_field.setText(tDiseases);
				recomendations_field.setText(tRecommendations);
				registrationDate_field.setValue(LocalDate.parse(tRegistrationDate, DTF));
				String tLastMassageDate = rs.getDate(13) == null ? ""
						: AllClientsController.dateFormat.format(rs.getDate(13));

				if (!tLastMassageDate.equals("")) {
					last_massage_date_field.setValue(LocalDate.parse(tLastMassageDate, DTF));
				}
			}

		} catch (SQLException e) {
			Alerts.infoBox(e.toString(), "sql error");
		}

		save_button.setOnAction(event -> {

			if (!female_radio.isSelected() && !male_radio.isSelected()) {
				Alerts.infoBox("please select the gender type", "warning");
				return;
			}

			if (female_radio.isSelected() && male_radio.isSelected()) {
				Alerts.infoBox("gender type should be only one", "warning");
				return;
			}

			if (!AdditionController.isInteger(age_field.getText())) {
				Alerts.infoBox("age should be a number!", "warning");
				return;
			}

			if (registrationDate_field.getValue() == null) {
				Alerts.infoBox("please enter registration date", "warning");
				return;
			}

			if (first_name_field.getText().equals("")) {
				Alerts.infoBox("please enter the name", "warning");
				return;
			}

			String tFirstName = first_name_field.getText();
			String tLastName = last_name_field.getText().equals("") ? null : last_name_field.getText();
			String tPatronymic = patronymic_field.getText().equals("") ? null : patronymic_field.getText();
			String tAge = age_field.getText().equals("") ? null : age_field.getText();
			String tGender = female_radio.isSelected() ? "Female" : "Male";
			String tActivity = activity_field.getText().equals("") ? null : activity_field.getText();
			String tTypeOfMassage = massageType_field.getText().equals("") ? null : massageType_field.getText();
			String tDiseases = disease_field.getText().equals("") ? null : disease_field.getText();
			String tRecomendations = recomendations_field.getText().equals("") ? null : recomendations_field.getText();
			String tRegistrationDate = registrationDate_field.getValue() == null ? null
					: registrationDate_field.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String tLastMassageDate = last_massage_date_field.getValue() == null ? null
					: last_massage_date_field.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			int tClientId = tc.getId();

			try {
				if (AdditionController.isTrueDate(tRegistrationDate)) {
					Alerts.infoBox(
							"please enter the correct date, because date " + tRegistrationDate + " is in the future",
							"warning");
					return;
				}
			} catch (ParseException e1) {
				Alerts.infoBox(e1.toString(), "error");
			}

			try {
				if (tLastMassageDate != null) {
					if (AdditionController.isTrueDate(tLastMassageDate)) {
						Alerts.infoBox(
								"please enter the correct date, because date " + tLastMassageDate + " is in the future",
								"warning");
						return;
					}
				}
			} catch (ParseException e1) {
				Alerts.infoBox(e1.toString(), "error");
			}

			Client tClient = new Client(tFirstName, tLastName, tPatronymic, tAge, tActivity, tTypeOfMassage, tDiseases,
					tRecomendations);

			try {
				if (photo_image_view.getImage() == null && file == null) {
					updateExistingClient(tClient, tGender, tRegistrationDate, tLastMassageDate, null, tClientId);
				} else if (photo_image_view.getImage() != null && file != null) {
					updateExistingClient(tClient, tGender, tRegistrationDate, tLastMassageDate,
							new FileInputStream(file), tClientId);
				} else {
					updateExistingClientWithoutPhoto(tClient, tGender, tRegistrationDate, tLastMassageDate, tClientId);
				}

			} catch (FileNotFoundException e) {
				Alerts.infoBox(e.toString(), "error");
			} finally {
				save_button.getScene().getWindow().hide();
			}

			AllClientsController acc = new AllClientsController();

			try {
				AllClientsController.getTableView().setItems(acc.getListItems(getAllActiveClients()));
			} catch (ParseException e) {
				Alerts.infoBox(e.toString(), "error");
			}

		});

	}

	public Button getSaveButton() {
		return save_button;
	}

}
