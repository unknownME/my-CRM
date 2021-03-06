package database;

import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import animations.Alerts;
import application.Client;
import controllers.AllClientsController.TempClient;

public class ClientsDbHandler {

	public void addNewClient(Client client, String gender, String registrationDate, String lastMassageDate,
			FileInputStream photo) throws ClassNotFoundException {

		String insert = "insert into " + Const.CLIENTS_TABLE + "(cstatus," + Const.CLIENT_FIRSTNAME + ","
				+ Const.CLIENT_LASTNAME + "," + Const.CLIENT_PATRONYMIC + "," + Const.CLIENT_AGE + ","
				+ Const.CLIENT_GENDER + "," + Const.CLIENT_ACTIVITY + "," + Const.CLIENT_MASSAGE_TYPE + ","
				+ Const.CLIENT_DISEASES + "," + Const.CLIENT_RECOMMENDATIONS + "," + Const.CLIENT_REGISTRATION_DATE
				+ "," + Const.CLIENT_LASTMASSAGE_DATE + "," + Const.CLIENT_PHOTO + ")"
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement ps = DatabaseHandler.getDbConnection().prepareStatement(insert);
			ps.setString(1, "active");
			ps.setString(2, client.getFirstName());
			ps.setString(3, client.getLastName());
			ps.setString(4, client.getPatronymic());
			ps.setString(5, client.getAge());
			ps.setString(6, gender);
			ps.setString(7, client.getActivity());
			ps.setString(8, client.getMassagetype());
			ps.setString(9, client.getDiseases());
			ps.setString(10, client.getRecommendations());
			ps.setString(11, registrationDate);
			ps.setString(12, lastMassageDate);
			ps.setBlob(13, photo);

			ps.executeUpdate();
		} catch (SQLException e) {
			Alerts.infoBox(e.toString(), "sql error");
			return;
		}
		Alerts.infoBox("New client added successfully!", "info");
	}

	public void updateExistingClient(Client client, String gender, String registrationDate, String lastMassageDate,
			FileInputStream photo, int clientId) {

		String update2 = "update " + Const.CLIENTS_TABLE + " set " + Const.CLIENT_FIRSTNAME + " = ?, "
				+ Const.CLIENT_LASTNAME + " = ?, " + Const.CLIENT_PATRONYMIC + " = ?, " + Const.CLIENT_AGE + " = ?, "
				+ Const.CLIENT_GENDER + " = ?, " + Const.CLIENT_ACTIVITY + " = ?, " + Const.CLIENT_MASSAGE_TYPE
				+ " = ?," + Const.CLIENT_DISEASES + " = ?, " + Const.CLIENT_RECOMMENDATIONS + " = ?, "
				+ Const.CLIENT_REGISTRATION_DATE + " = ?, " + Const.CLIENT_LASTMASSAGE_DATE + " = ?, "
				+ Const.CLIENT_PHOTO + " = ? where " + Const.CLIENT_ID + "= ?";

		try {
			PreparedStatement ps = DatabaseHandler.getDbConnection().prepareStatement(update2);
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
			ps.setString(11, lastMassageDate);
			ps.setBlob(12, photo);
			ps.setInt(13, clientId);
			ps.executeUpdate();
		} catch (SQLException e) {
			Alerts.infoBox(e.toString(), "sql error");
			return;
		}
		Alerts.infoBox("client updated successfully!", "info");
	}
	
	public void updateExistingClientWithoutPhoto(Client client, String gender, String registrationDate, String lastMassageDate, int clientId) {

		String update = "update " + Const.CLIENTS_TABLE + " set " + Const.CLIENT_FIRSTNAME + " = ?, "
				+ Const.CLIENT_LASTNAME + " = ?, " + Const.CLIENT_PATRONYMIC + " = ?, " + Const.CLIENT_AGE + " = ?, "
				+ Const.CLIENT_GENDER + " = ?, " + Const.CLIENT_ACTIVITY + " = ?, " + Const.CLIENT_MASSAGE_TYPE
				+ " = ?, " + Const.CLIENT_DISEASES + " = ?, " + Const.CLIENT_RECOMMENDATIONS + " = ?, "
				+ Const.CLIENT_REGISTRATION_DATE + " = ?, " + Const.CLIENT_LASTMASSAGE_DATE + " = ? where " + Const.CLIENT_ID + "= ?";

		try {
			PreparedStatement ps = DatabaseHandler.getDbConnection().prepareStatement(update);
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
			ps.setString(11, lastMassageDate);
			ps.setInt(12, clientId);
			ps.executeUpdate();
		} catch (SQLException e) {
			Alerts.infoBox(e.toString(), "sql error");
			return;
		}
		Alerts.infoBox("client updated successfully!", "info");
	}

	public ResultSet getAllActiveClients() {
		ResultSet resultSet = null;
		String request = "select * from " + Const.CLIENTS_TABLE + " where cstatus='active'";
		try {
			PreparedStatement ps = DatabaseHandler.getDbConnection().prepareStatement(request);
			resultSet = ps.executeQuery();
		} catch (SQLException e) {
			Alerts.infoBox(e.toString(), "sql error");
		}
		return resultSet;
	}

	public ResultSet getAllRemovedClients() {
		ResultSet resultSet = null;
		String request = "select * from " + Const.CLIENTS_TABLE + " where cstatus='removed'";
		try {
			PreparedStatement ps = DatabaseHandler.getDbConnection().prepareStatement(request);
			resultSet = ps.executeQuery();
		} catch (SQLException e) {
			Alerts.infoBox(e.toString(), "sql error");
		}
		return resultSet;
	}

	public void removeClient(TempClient client) {

		String request = "update " + Const.CLIENTS_TABLE + " set cstatus " + "=" + "'" + "removed" + "'" + " where "
				+ Const.CLIENT_ID + "=" + client.getId() + " and cstatus='active'";

		try {
			PreparedStatement ps = DatabaseHandler.getDbConnection().prepareStatement(request);
			ps.executeUpdate();
		} catch (SQLException e) {
			Alerts.infoBox(e.toString(), "sql error");
		}
	}

	public void reActivateClient(TempClient client) {
		String request = "update " + Const.CLIENTS_TABLE + " set cstatus " + "=" + "'" + "active" + "'" + " where "
				+ Const.CLIENT_ID + "=" + client.getId() + " and cstatus='removed'";

		try {
			PreparedStatement ps = DatabaseHandler.getDbConnection().prepareStatement(request);
			ps.executeUpdate();
		} catch (SQLException e) {
			Alerts.infoBox(e.toString(), "sql error");
		}
	}

	public ResultSet getConcreteClient(TempClient tempClient) {
		ResultSet resultSet = null;
		String request = "select * from " + Const.CLIENTS_TABLE + " where " + Const.CLIENT_ID + "="
				+ tempClient.getId();
		try {
			PreparedStatement ps = DatabaseHandler.getDbConnection().prepareStatement(request);
			resultSet = ps.executeQuery();
		} catch (SQLException e) {
			Alerts.infoBox(e.toString(), "sql error");
		}
		return resultSet;
	}

}
