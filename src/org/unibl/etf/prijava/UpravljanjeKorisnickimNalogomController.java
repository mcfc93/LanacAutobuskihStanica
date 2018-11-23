package org.unibl.etf.prijava;

import java.net.URL;
import java.util.ResourceBundle;

import org.unibl.etf.util.Util;
import org.unibl.etf.zaposleni.Zaposleni;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class UpravljanjeKorisnickimNalogomController implements Initializable {
	
    @FXML
    private GridPane gridPane;
	
    @FXML
    private JFXPasswordField staraLozinkaTextField;

    @FXML
    private JFXPasswordField novaLozinka1TextField;

    @FXML
    private JFXPasswordField novaLozinka2TextField;

    @FXML
    private JFXButton potvrdiButton;

    @FXML
    private TextField imePrezimeTextField;

    @FXML
    private TextField jmbgTextField;

    @FXML
    private TextField polTextField;

    @FXML
    private TextField adresaTextField;

    @FXML
    private TextField brojTelefonaTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField strucnaSpremaTextField;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Zaposleni zaposleni=PrijavaController.nalog.getZaposleni();
		imePrezimeTextField.setText(zaposleni.getIme() + " " + zaposleni.getPrezime());
   		jmbgTextField.setText(zaposleni.getJmbg());
   		polTextField.setText(zaposleni.getPol());
   		adresaTextField.setText(zaposleni.getAdresa());
   		brojTelefonaTextField.setText(zaposleni.getBrojTelefona());
   		emailTextField.setText(zaposleni.getEmail());
   		strucnaSpremaTextField.setText(zaposleni.getStrucnaSprema());
   		zaposleni=null;
   		
   		/*
   		potvrdiButton.disableProperty().bind(Bindings.createBooleanBinding(
	    		() -> staraLozinkaTextField.getText().trim().isEmpty()
	    				|| novaLozinka1TextField.getText().trim().isEmpty()
	    					|| novaLozinka2TextField.getText().trim().isEmpty()
	    						, staraLozinkaTextField.textProperty()
	    							, novaLozinka1TextField.textProperty()
	    								, novaLozinka2TextField.textProperty()
	    									));
   		*/

   		staraLozinkaTextField.getValidators().add(Util.requredFieldValidator(staraLozinkaTextField));
   		novaLozinka1TextField.getValidators().addAll(Util.requredFieldValidator(novaLozinka1TextField), Util.passwordValidator(novaLozinka1TextField));
   		novaLozinka2TextField.getValidators().addAll(Util.requredFieldValidator(novaLozinka2TextField), Util.samePasswordValidator(novaLozinka1TextField, novaLozinka2TextField));
	}
    
    @FXML
    void potvrdi(ActionEvent event) {
    	// & umjesto && da se ne radi short-circuit evaluation
    	if(staraLozinkaTextField.validate() & novaLozinka1TextField.validate() & novaLozinka2TextField.validate()) {
    		System.out.println("Validacija uspjesna.");
    		if(Nalog.provjeraLozinke(Nalog.hash(staraLozinkaTextField.getText()))) {
    			if(Nalog.promjenaLozinke(Nalog.hash(novaLozinka1TextField.getText().trim()))) {
    				System.out.println("Nova lozinka: " + novaLozinka1TextField.getText());
    				System.out.println("Hash: " + Nalog.hash(novaLozinka1TextField.getText().trim()));
    				PrijavaController.nalog.setLozinka(Nalog.hash(novaLozinka1TextField.getText()));
    			} else {
    				//
    				
    			}
    			
    			staraLozinkaTextField.clear();
    			novaLozinka1TextField.clear();
    			novaLozinka2TextField.clear();
    			staraLozinkaTextField.resetValidation();
    			novaLozinka1TextField.resetValidation();
    			novaLozinka2TextField.resetValidation();
    		} else {
    			staraLozinkaTextField.clear();
    	 		Alert alert=new Alert(AlertType.ERROR);
        		alert.setTitle("Greška");
        		alert.setHeaderText(null);
        		alert.setContentText("Lozinka pogrešna.");
        		alert.showAndWait();
        	}
    	}
    }
}