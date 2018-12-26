package org.unibl.etf;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
			//primaryStage.setTitle("Prijava");
			primaryStage.setResizable(false);
			//StageStyle.DECORATED, StageStyle.UNDECORATED, StageStyle.UNIFIED, StageStyle.UTILITY
			primaryStage.initStyle(StageStyle.UNDECORATED);    //brisanje _ [] X
			//Modality.NONE, Modality.WINDOW_MODAL, Modality.APPLICATION_MODAL
			//stage.initModality(Modality.APPLICATION_MODAL);
			/*
			primaryStage.setOnCloseRequest(event -> {
				event.consume();
				System.out.println("CLOSE");
				Util.fileHandler.close();
			});
			*/
			primaryStage.show();
		} catch(Exception e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
	
	@Override
	public void stop() {
		System.out.println("CLOSE");
		Util.fileHandler.close();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
