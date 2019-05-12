package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import animations.Alerts;
import database.ClientsDbHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AllClientsController extends ClientsDbHandler {

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	private static final Date localDate = new Date();
	private ResultSet rs;
	private static TempClient client;
	private static TableView<TempClient> tableView;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button view_client_button;

	@FXML
	private Button delete_client_button;

	@FXML
	private TableView<TempClient> table_view;

	@FXML
	private Button show_removed_button;

	@FXML
	private Button re_activate_button;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@FXML
	void initialize() throws ParseException {

		delete_client_button.setOnAction(event -> {
			rs = getAllActiveClients();
			TempClient currentClient = table_view.getSelectionModel().getSelectedItem();
			
			if(currentClient == null) {
				Alerts.infoBox("please choose the client", "warning");
				return;
			}
			
			if(currentClient.getStatus().equals("removed")) {
				Alerts.infoBox("please choose the ACTIVE client", "warning");
				return;
			}
			
			try {
				while (rs.next()) {
					removeClient(currentClient);
				}
			} catch (SQLException e) {
				Alerts.infoBox(e.toString(), "sql error");
			}

			try {
				table_view.setItems(getListItems(getAllActiveClients()));
			} catch (ParseException e) {
				Alerts.infoBox(e.toString(), "parse error");
			}
		});

		show_removed_button.setOnAction(event -> {
			try {
				table_view.setItems(getListItems(getAllRemovedClients()));
			} catch (ParseException e) {
				Alerts.infoBox(e.toString(), "parse error");
			}
		});

		re_activate_button.setOnAction(event -> {
			
			if(client == null) {
				Alerts.infoBox("please choose the REMOVED client", "warning");
				return;
			}	
			
			rs = getAllRemovedClients();
			TempClient currentClient = table_view.getSelectionModel().getSelectedItem();

			try {
				while (rs.next()) {
					reActivateClient(currentClient);
				}
			} catch (SQLException e) {
				Alerts.infoBox(e.toString(), "sql error");
			}

			try {
				table_view.setItems(getListItems(getAllRemovedClients()));
			} catch (ParseException e) {
				Alerts.infoBox(e.toString(), "parse error");
			}

		});
		
		view_client_button.setOnAction(event -> {
			
			client = table_view.getSelectionModel().getSelectedItem();
			
			if(client == null) {
				Alerts.infoBox("please choose the client", "warning");
				return;
			}	
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/views/ViewClient.fxml"));

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

		TableColumn idColumn = new TableColumn("ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<TempClient, String>("id"));

		TableColumn statusColumn = new TableColumn("Status");
		statusColumn.setCellValueFactory(new PropertyValueFactory<TempClient, String>("status"));

		TableColumn nameColumn = new TableColumn("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<TempClient, String>("name"));

		TableColumn lastNameColumn = new TableColumn("Last name");
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<TempClient, String>("lastName"));

		TableColumn ageColumn = new TableColumn("Age");
		ageColumn.setCellValueFactory(new PropertyValueFactory<TempClient, String>("age"));

		TableColumn massageTypeColumn = new TableColumn("Type of massage");
		massageTypeColumn.setCellValueFactory(new PropertyValueFactory<TempClient, String>("typeOfMassage"));

		TableColumn registrationDateColumn = new TableColumn("Registration date");
		registrationDateColumn.setCellValueFactory(new PropertyValueFactory<TempClient, String>("registrationDate"));

		TableColumn discountColumn = new TableColumn("Discount");
		discountColumn.setCellValueFactory(new PropertyValueFactory<String, String>("discount"));

		TableColumn lastMassageColumn = new TableColumn("Last massage from session");
		lastMassageColumn.setCellValueFactory(new PropertyValueFactory<TempClient, String>("lastMassageFromSession"));

		TableColumn notificationColumn = new TableColumn("Three month period");
		notificationColumn.setCellValueFactory(new PropertyValueFactory<String, String>("periodExpiredNotification"));
		notificationColumn.setStyle("-fx-text-fill: red;");

		table_view.getColumns().addAll(idColumn, statusColumn, nameColumn, lastNameColumn, ageColumn, massageTypeColumn,
				registrationDateColumn, discountColumn, lastMassageColumn, notificationColumn);

		table_view.setItems(getListItems(getAllActiveClients()));
		
		tableView = table_view;
			
	}

	public ObservableList<TempClient> getListItems(ResultSet resultSet) throws ParseException {
		ObservableList<TempClient> items = FXCollections.observableArrayList();
		rs = resultSet;

		try {

			while (rs.next()) {
				Date date = rs.getDate(12);

				String tDate = dateFormat.format(date);
				
				String tLastMassageDate = rs.getDate(13) == null ? tLastMassageDate = ""
						: dateFormat.format(rs.getDate(13));

				items.add(new TempClient(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(6), rs.getString(9), tDate, getDiscount(tDate), tLastMassageDate,
						getNotificationAfterLastMassagePeriodExpired(tLastMassageDate)));

			}
		} catch (SQLException e) {
			Alerts.infoBox(e.toString(), "sql error");
		}
		return items;
	}

	private String getDiscount(String dateFromDb) throws ParseException {
		long diffInMillies = Math
				.abs(dateFormat.parse(dateFormat.format(localDate)).getTime() - dateFormat.parse(dateFromDb).getTime());
		long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		if (diff > 365 && diff < 730) {
			return "5%";
		} else if (diff > 730) {
			return "10%";
		} else {
			return "";
		}
	}

	private String getNotificationAfterLastMassagePeriodExpired(String dateFromDb) throws ParseException {
		if (dateFromDb.equals("")) {
			return "";
		}
		long diffInMillies = Math
				.abs(dateFormat.parse(dateFormat.format(localDate)).getTime() - dateFormat.parse(dateFromDb).getTime());
		long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		return diff > 90 ? "Three months have elapsed" : "";
	}
	
	public static TempClient getConcreteClient() {
		return client;
	}
	
	public static TableView<TempClient> getTableView(){
		return tableView;
	}
	
	public class TempClient {
		private int id;
		private String name;
		private String lastName;
		private String age;
		private String typeOfMassage;
		private String registrationDate;
		private String status;
		private String discount;
		private String lastMassageFromSession;
		private String periodExpiredNotification;

		public TempClient(int id, String status, String name, String lastName, String age, String typeOfMassage,
				String registrationDate, String discount, String lastMassageFromSession,
				String periodExpiredNotification) {
			this.id = id;
			this.status = status;
			this.name = name;
			this.lastName = lastName;
			this.age = age;
			this.typeOfMassage = typeOfMassage;
			this.registrationDate = registrationDate;
			this.discount = discount;
			this.lastMassageFromSession = lastMassageFromSession;
			this.periodExpiredNotification = periodExpiredNotification;
		}

		public String getDiscount() {
			return discount;
		}

		public void setDiscount(String discount) {
			this.discount = discount;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getAge() {
			return age;
		}

		public void setAge(String age) {
			this.age = age;
		}

		public String getTypeOfMassage() {
			return typeOfMassage;
		}

		public void setTypeOfMassage(String typeOfMassage) {
			this.typeOfMassage = typeOfMassage;
		}

		public String getRegistrationDate() {
			return registrationDate;
		}

		public void setRegistrationDate(String registrationDate) {
			this.registrationDate = registrationDate;
		}

		public String getLastMassageFromSession() {
			return lastMassageFromSession;
		}

		public void setLastMassageFromSession(String lastMassageFromSession) {
			this.lastMassageFromSession = lastMassageFromSession;
		}

		public String getPeriodExpiredNotification() {
			return periodExpiredNotification;
		}

		public void setPeriodExpiredNotification(String periodExpiredNotification) {
			this.periodExpiredNotification = periodExpiredNotification;
		}
	}
}
