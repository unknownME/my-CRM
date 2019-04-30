package application;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseHandler extends Configs {

	Connection dbConnection;

	public Connection getDbConnection() throws ClassNotFoundException, SQLException {
		String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName
				+ "?useUnicode=true&useJDBCCompliantTimezoneShift=true&serverTimezone=UTC";

		Class.forName("com.mysql.cj.jdbc.Driver");
		dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
		return dbConnection;
	}

	public void addNewClient(Client client, String gender, String registrationDate, FileInputStream photo)
			throws ClassNotFoundException {

		String insert = "insert into " + Const.CLIENTS_TABLE + "(" + Const.CLIENT_FIRSTNAME + ","
				+ Const.CLIENT_LASTNAME + "," + Const.CLIENT_PATRONYMIC + "," + Const.CLIENT_AGE + ","
				+ Const.CLIENT_GENDER + "," + Const.CLIENT_ACTIVITY + "," + Const.CLIENT_MASSAGE_TYPE + ","
				+ Const.CLIENT_DISEASES + "," + Const.CLIENT_RECOMMENDATIONS + "," + Const.CLIENT_REGISTRATION_DATE
				+ "," + Const.CLIENT_PHOTO + ")" + " values(?,?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement ps = getDbConnection().prepareStatement(insert);
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

			e.printStackTrace();
		}

	}

}
