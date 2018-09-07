package org.unibl.etf.prijava;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.unibl.etf.util.Util;
import org.unibl.etf.zaposleni.AdministrativniRadnik;
import org.unibl.etf.zaposleni.Administrator;

//import java.util.logging.Level;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PrijavaController implements Initializable {
	
	public static Nalog nalog=null;
	
	@FXML
	private AnchorPane anchorPane;
	
	@FXML
    private TextField korisnickoImeTextField;

    @FXML
    private PasswordField lozinkaTextField;
    
    @FXML
    private Label greskaLabel;

    @FXML
    private Button prijavaButton;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		greskaLabel.setVisible(false);
	}
	
	@FXML
    void prijava(ActionEvent event) {
		
		if ((nalog=Nalog.prijava(korisnickoImeTextField.getText(), lozinkaTextField.getText())) != null) {
			//((Stage)anchorPane.getScene().getWindow()).close();
			//((Stage)anchorPane.getScene().getWindow()).hide();
			//((Node)event.getSource()).getScene().getWindow().hide();
			((Stage)((Node)event.getSource()).getScene().getWindow()).close();
			
			//nalog.setKorisnickoIme(korisnickoImeTextField.getText());
			//nalog.setLozinka(lozinkaTextField.getText());
			
			
			if(nalog.getZaposleni() instanceof Administrator) {
				//administrator
    			try {
    				Parent root = (AnchorPane)FXMLLoader.load(getClass().getResource("/org/unibl/etf/administrator/AdministratorView.fxml"));
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
    				//e.printStackTrace();
    				Util.LOGGER.log(Level.SEVERE, e.toString(), e);
    			}
			} else if(nalog.getZaposleni() instanceof AdministrativniRadnik) {
				//administrativni radnik
    			try {
    				Parent root = (AnchorPane)FXMLLoader.load(getClass().getResource("/org/unibl/etf/administrativni_radnik/AdministrativniRadnikView.fxml"));
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
    				//e.printStackTrace();
    				Util.LOGGER.log(Level.SEVERE, e.toString(), e);
    			}
			} else {
				//if("SalterskiRadnik".equals(r.getString("Tip"))) {
        		//salterski radnik
               	try {
            		Parent root = (AnchorPane)FXMLLoader.load(getClass().getResource("/org/unibl/etf/salterski_radnik/SalterskiRadnikView.fxml"));
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
           			//e.printStackTrace();
           			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
            	}
			}
			
			
			
			
			
			
		} else {
			greskaLabel.setText("Korisničko ime ili lozinka pogrešni!");
			greskaLabel.setVisible(true);
			korisnickoImeTextField.clear();
			lozinkaTextField.clear();
			//korisnickoImeTextField.requestFocus();
		}
    }
	
	void odjava(ActionEvent event) {
		((Stage)anchorPane.getScene().getWindow()).show();
	}
	
	@FXML
	void sakrijLabelu(MouseEvent event) {
		greskaLabel.setVisible(false);
	}
	
	/*
	 		Alert alert=new Alert(AlertType.ERROR);
    		alert.setTitle("Greška");
    		alert.setHeaderText(null);
    		alert.setContentText("Poruka.");
    		alert.showAndWait();
	 */
}