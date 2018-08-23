package org.unibl.etf.prijava;

import java.net.URL;
import java.util.ResourceBundle;
//import java.util.logging.Level;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PrijavaController implements Initializable {
	
	@FXML
	private AnchorPane anchorPane;
	
	@FXML
    private TextField korisnickoImeTextField;

    @FXML
    private PasswordField lozinkaTextField;

    @FXML
    private Button prijavaButton;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}
	
	@FXML
    void prijava(ActionEvent event) {
		//salterski radnik
		try {
			//																//org.unibl.etf.prijava.
			Parent root = (AnchorPane)FXMLLoader.load(getClass().getResource("SalterskiRadnikView.fxml"));
			Scene scene = new Scene(root);
			Stage stage=new Stage();
			stage.setScene(scene);
			stage.setTitle("Šalterski radnik");
			stage.setResizable(false);
			//primaryStage.initStyle(StageStyle.DECORATED);
			//primaryStage.initStyle(StageStyle.UNDECORATED);    //brisanje _ [] X
			//primaryStage.initStyle(StageStyle.UNIFIED);
			//primaryStage.initStyle(StageStyle.UTILITY);
			
			stage.show();
			
			
			//Modality.NONE, Modality.WINDOW_MODAL, Modality.APPLICATION_MODAL
			//stage.initModality(Modality.APPLICATION_MODAL);
			//stage.showAndWait();
		} catch(Exception e) {
			e.printStackTrace();
//Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		
		
		
		
		//administrativni radnik
		try {
		//																//org.unibl.etf.prijava.
			Parent root = (AnchorPane)FXMLLoader.load(getClass().getResource("SalterskiRadnikView.fxml"));
			Scene scene = new Scene(root);
			Stage stage=new Stage();
			stage.setScene(scene);
			stage.setTitle("Administrativni radnik");
			stage.setResizable(false);
			//primaryStage.initStyle(StageStyle.DECORATED);
			//primaryStage.initStyle(StageStyle.UNDECORATED);    //brisanje _ [] X
			//primaryStage.initStyle(StageStyle.UNIFIED);
			//primaryStage.initStyle(StageStyle.UTILITY);

			stage.show();

			//Modality.NONE, Modality.WINDOW_MODAL, Modality.APPLICATION_MODAL
			//stage.initModality(Modality.APPLICATION_MODAL);
			//stage.showAndWait();
		} catch(Exception e) {
			e.printStackTrace();
//Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}


		
		
		
		
		//administrator
		try {
			//																//org.unibl.etf.prijava.
			Parent root = (AnchorPane)FXMLLoader.load(getClass().getResource("SalterskiRadnikView.fxml"));
			Scene scene = new Scene(root);
			Stage stage=new Stage();
			stage.setScene(scene);
			stage.setTitle("Administrator");
			stage.setResizable(false);
			//primaryStage.initStyle(StageStyle.DECORATED);
			//primaryStage.initStyle(StageStyle.UNDECORATED);    //brisanje _ [] X
			//primaryStage.initStyle(StageStyle.UNIFIED);
			//primaryStage.initStyle(StageStyle.UTILITY);

			stage.show();

			//Modality.NONE, Modality.WINDOW_MODAL, Modality.APPLICATION_MODAL
			//stage.initModality(Modality.APPLICATION_MODAL);
			//stage.showAndWait();
		} catch(Exception e) {
			e.printStackTrace();
//Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		
		
		
		
		
		
		((Stage)anchorPane.getScene().getWindow()).close();
    }
	
	
	
	/*
	 		Alert alert=new Alert(AlertType.ERROR);
    		alert.setTitle("Greška");
    		alert.setHeaderText(null);
    		alert.setContentText("Poruka.");
    		alert.showAndWait();
	 */
}
