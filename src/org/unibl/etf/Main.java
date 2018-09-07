package org.unibl.etf;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

import java.util.logging.Level;

import org.unibl.etf.util.Util;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/org/unibl/etf/prijava/Prijava.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Prijava");
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			//e.printStackTrace();
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
