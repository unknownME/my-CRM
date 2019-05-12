package application;

import animations.Alerts;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/views/sample.fxml"));
			primaryStage.setScene(new Scene(root));
 			primaryStage.show();
		} catch (Exception e) {
			Alerts.infoBox(e.toString(), "sql error");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
