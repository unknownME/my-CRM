package database;

import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import animations.Alerts;
import application.Client;

public class ClientsDbHandler {

	public void addNewClient(Client client, String gender, String registrationDate, FileInputStream photo)
			throws ClassNotFoundException {

		String insert = "insert into " + Const.CLIENTS_TABLE + "(" + Const.CLIENT_FIRSTNAME + ","
				+ Const.CLIENT_LASTNAME + "," + Const.CLIENT_PATRONYMIC + "," + Const.CLIENT_AGE + ","
				+ Const.CLIENT_GENDER + "," + Const.CLIENT_ACTIVITY + "," + Const.CLIENT_MASSAGE_TYPE + ","
				+ Const.CLIENT_DISEASES + "," + Const.CLIENT_RECOMMENDATIONS + "," + Const.CLIENT_REGISTRATION_DATE
				+ "," + Const.CLIENT_PHOTO + ")" + " values(?,?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement ps = DatabaseHandler.getDbConnection().prepareStatement(insert);
			ps.setString(1, client.getFirstName());
			ps.setString(2, client.getLastName());
			ps.setString(3, client.getPatronymic());
			ps.setString(4, client.getAge());
			ps.setString(5, gender);
			ps.setString(6, client.getActivity());
			ps.setString(7, client.getMassagetype());
			ps.setString(8, client.getDiseases());
			ps.setString(9, client.getRecommendations());
			ps.setString(10, registrationDate);
			ps.setBlob(11, photo);

			ps.executeUpdate();
		} catch (SQLException e) {
			Alerts.infoBox(e.toString(), "sql error");
		}
	}

	public ResultSet getAllClients() {
		ResultSet resultSet = null;
		String request = "select * from " + Const.CLIENTS_TABLE;
		try {
			PreparedStatement ps = DatabaseHandler.getDbConnection().prepareStatement(request);
			resultSet = ps.executeQuery();
		} catch (SQLException e) {
			Alerts.infoBox(e.toString(), "sql error");
		}
		return resultSet;
	}

}
