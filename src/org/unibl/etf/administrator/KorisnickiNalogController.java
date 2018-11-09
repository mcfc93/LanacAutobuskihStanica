package org.unibl.etf.administrator;

import java.net.URL;
import java.util.ResourceBundle;

import org.unibl.etf.prijava.PrijavaController;
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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class KorisnickiNalogController implements Initializable {
	
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
   		//polTextField.setText(zaposleni.getPol());
   		polTextField.setText("TBD");
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
   		/*
   		staraLozinkaTextField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)->{
	        if(staraLozinkaTextField.getText().equals("student")) {
	        	staraLozinkaTextField.validate();
	        }
	    });
		*/
   		/*
   		novaLozinka2TextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue)->{
   			if(!novaLozinka1TextField.getText().trim().isEmpty()
	        		&& !novaLozinka2TextField.getText().trim().isEmpty()
	        			&& !novaLozinka1TextField.getText().trim().equals(novaLozinka2TextField.getText().trim())) {
	        	System.out.println("UNOS");
	        	novaLozinka2TextField.validate();
   			} else {
   				System.out.println("RESET");
   				novaLozinka2TextField.resetValidation();
   			}
	    });
		*/
   		

   		ValidatorBase samePassword = new ValidatorBase("Nije jednako") {
			@Override
			protected void eval() {
				setIcon(new ImageView());
			    //setMessage("Input Required");
				if(!novaLozinka1TextField.getText().trim().isEmpty()
		        		&& !novaLozinka2TextField.getText().trim().isEmpty()
		        			&& !novaLozinka1TextField.getText().equals(novaLozinka2TextField.getText())) {
		        	 hasErrors.set(true);
		        } else {
		        	 hasErrors.set(false);
		        }
			}
		};
		
//		novaLozinka1TextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue)->{
//	    	/*if(!novaLozinka2TextField.getText().trim().isEmpty()
//	    			&& !novaLozinka1TextField.getText().trim().equals(novaLozinka2TextField.getText().trim())) {
//	    	*/
//				if(!newValue.isEmpty()) {
//	    		novaLozinka2TextField.validate();
//	        }/* else {
//	        	novaLozinka2TextField.resetValidation();
//	        }*/
//	    });
		/*
		novaLozinka2TextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue)->{
	    	if(!novaLozinka1TextField.getText().equals(newValue)) {
	    		novaLozinka2TextField.validate();
	        } else {
	        	novaLozinka2TextField.resetValidation();
	        }
	    });
	    */
	    
   		//staraLozinkaTextField.getValidators().add(requiredFieldValidator);
   		staraLozinkaTextField.getValidators().add(requredFieldValidator(staraLozinkaTextField));
   		novaLozinka1TextField.getValidators().add(requredFieldValidator(novaLozinka1TextField));
   		novaLozinka2TextField.getValidators().addAll(requredFieldValidator(novaLozinka2TextField), samePassword);
	}
	
    @FXML
    void potvrdi(ActionEvent event) {
    	//if(staraLozinkaTextField.getText().equals() && novaLozinka1TextField.getText().equals(novaLozinka2TextField.getText())) {
    		
    	//}
    	
    	if(staraLozinkaTextField.validate() && novaLozinka1TextField.validate() && novaLozinka2TextField.validate()) {
    		System.out.println("DUGME");
    	}
    }
	
    
    public ValidatorBase requredFieldValidator(JFXPasswordField textField) {
    	ValidatorBase requiredFieldValidator = new RequiredFieldValidator();
	    requiredFieldValidator.setMessage("Obavezan unos");
	    requiredFieldValidator.setIcon(new ImageView());
	    
	    
	    textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)->{
	        if(!newValue) {
	        	textField.validate();
	        }
	    });
	    
	    
	    textField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue)->{
	        //if(textField.getText().trim().isEmpty()) {
	        	textField.validate();
	        //} else {
	        //	textField.resetValidation();
	        //}
	    });
	    
	    
	    return requiredFieldValidator;
    }
}