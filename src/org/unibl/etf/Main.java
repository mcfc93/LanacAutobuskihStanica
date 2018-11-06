package org.unibl.etf;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;

import org.unibl.etf.prijava.Nalog;
import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.util.Util;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		//
		//if("src/org/unibl/etf/config.properties" !exists())
		//otvori formu za config
		
		
		//ucitavanje nalog.ser, ako postoji
		/*
    	File f=new File("garaza.ser");
    	if(f.exists()) {
    		try (ObjectInputStream ois = 
    				new ObjectInputStream(
    						new FileInputStream("garaza.ser")
    				)
    			)
    		{
    			PrijavaController.nalog=(Nalog)ois.readObject();
System.out.println("nalog.ser");
    		} catch(IOException e) {
    			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
    		} catch(ClassNotFoundException e) {
    			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
    		}
    	}
		*/
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
			primaryStage.show();
		} catch(Exception e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
