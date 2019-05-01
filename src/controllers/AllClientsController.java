package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import database.ClientsDbHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AllClientsController extends ClientsDbHandler {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ListView<String> listView_area;

	@FXML
	private Button edit_client_button;

	@FXML
	private Button delete_client_button;

	@FXML
	private TableView<TempClient> table_view;

	@FXML
	void initialize() {

		ObservableList<TempClient> items = FXCollections.observableArrayList();

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
		
		table_view.getColumns().addAll(nameColumn, lastNameColumn, ageColumn, massageTypeColumn,
				registrationDateColumn);

		ResultSet rs = getAllClients();

		try {
			while (rs.next()) {
				items.add(new TempClient(rs.getString(2), rs.getString(3), rs.getString(5), rs.getString(8),
						rs.getString(11)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		table_view.setItems(items);
		
//		listView_area.setItems(items);

	}
	
	public class TempClient {
		private String name;
		private String lastName;
		private String age;
		private String typeOfMassage;
		private String registrationDate;

		public TempClient(String name, String lastName, String age, String typeOfMassage, String registrationDate) {
			this.name = name;
			this.lastName = lastName;
			this.age = age;
			this.typeOfMassage = typeOfMassage;
			this.registrationDate = registrationDate;
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

	}
}
