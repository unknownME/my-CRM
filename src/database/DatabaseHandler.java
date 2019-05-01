package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHandler {

	private static Connection dbConnection;
	private static final String URL = "jdbc:mysql://localhost:3306/leafdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&serverTimezone=UTC";
	private static final String DB_LOGIN = "root";
	private static final String DB_PASSWORD = "root";

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			dbConnection = DriverManager.getConnection(URL, DB_LOGIN, DB_PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection getDbConnection() {
		return dbConnection;
	}

}
