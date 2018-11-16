package org.unibl.etf.administrator;

import java.net.URL;
import java.util.ResourceBundle;

import org.unibl.etf.prijava.Nalog;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class DodajNalogController implements Initializable {
    @FXML
    private GridPane gridPane;

    @FXML
    private JFXTextField korisnickoImeTextField;

    @FXML
    private JFXPasswordField lozinkaTextField;

    @FXML
    private JFXTextField jibStaniceTextField;

    @FXML
    private ToggleGroup tipGroup;

    @FXML
    private JFXRadioButton administrativniRadnikRadioButton;
    
    @FXML
    private JFXRadioButton salterskiRadnikRadioButton;

    @FXML
    private JFXTextField imeTextField;

    @FXML
    private JFXTextField prezimeTextField;

    @FXML
    private JFXTextField jmbgTextField;

    @FXML
    private JFXTextField adresaTextField;

    @FXML
    private JFXTextField postanskiBrojTextField;

    @FXML
    private JFXTextField strucnaSpremaTextField;

    @FXML
    private ToggleGroup polGroup;
    
    @FXML
    private JFXRadioButton muskoRadioButton;

    @FXML
    private JFXRadioButton zenskoRadioButton;

    @FXML
    private JFXTextField brojTelefonaTextField;

    @FXML
    private JFXTextField emailTextField;

    @FXML
    private JFXButton potvrdiButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/*
		potvrdiButton.setDisable(true);
	    
	    ChangeListener<String> changeListener=new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable,
	        						String oldValue, String newValue) {
	        	if((korisnickoImeTextField.getText().trim().length() < 6)
		        	|| (lozinkaTextField.getText().trim().length() < 6)) {
		        	potvrdiButton.setDisable(true);
		        } else {
		        	potvrdiButton.setDisable(false);
		        }
	        }
	    };
	    
	    korisnickoImeTextField.textProperty().addListener(changeListener);
	    lozinkaTextField.textProperty().addListener(changeListener);
	    */
		/*
		potvrdiButton.disableProperty().bind(
	    		korisnickoImeTextField.textProperty().isEmpty()
	    			.or(lozinkaTextField.textProperty().isEmpty())
	    		);
	   	*/
	    
	    
	    /*
	    lozinkaTextField.setTooltip(new Tooltip("Lozinka mora sadržavati najmanje 6 karaktera"));
	    
	    lozinkaTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue)->{
	        if(newValue.length() < 6) {
	    		System.out.println(newValue);
	        	lozinkaTextField.getTooltip().show(lozinkaTextField.getScene().getWindow());
	        } else {
	        	lozinkaTextField.getTooltip().hide();
	        }
	    });
	    */
		
		
		
		
		ValidatorBase jmbgValidator = new ValidatorBase("Nekorektan unos") {
			@Override
			protected void eval() {
				if(!jmbgTextField.getText().isEmpty()
						&& jmbgTextField.getText().length() != 13) {
		        	 hasErrors.set(true);
		        } else {
		        	 hasErrors.set(false);
		        	 /*
		        	 try {
		        		 Long.parseLong(jmbgTextField.getText());
		        	 } catch(NumberFormatException e) {
		        		 hasErrors.set(true);
		        	 }
		        	 */
		        	 
		        	if(!jmbgTextField.getText().matches("^[0-9]+$")) {
		        		hasErrors.set(true);
		        	}
		        	
		        	 /*
		        	if(!jmbgTextField.getText().matches("((+)[0-9]2)?[0-9]{3}(/)[0-9]{3}(-)[0-9]{3}[0-9]*")) {
		        		hasErrors.set(true);
		        	}
		        	*/
		        }
			}
		};
		jmbgValidator.setIcon(new ImageView());

   		korisnickoImeTextField.getValidators().add(requredFieldValidator(korisnickoImeTextField));
   		//lozinkaTextField.getValidators().add(requredFieldValidator(lozinkaTextField));
   		jibStaniceTextField.getValidators().add(requredFieldValidator(jibStaniceTextField));
   		
   		jmbgTextField.getValidators().addAll(requredFieldValidator(jmbgTextField), jmbgValidator);
	}
	
	public ValidatorBase requredFieldValidator(JFXTextField textField) {
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
	
	@FXML
    void potvrdi(ActionEvent event) {
		if(Nalog.dodavanjeNaloga(korisnickoImeTextField.getText(),
								Nalog.hash(lozinkaTextField.getText()),
								jibStaniceTextField.getText(),
								administrativniRadnikRadioButton.isSelected() ? "AdministrativniRadnik" : "SalterRadnik",
								imeTextField.getText(), prezimeTextField.getText(),
								jmbgTextField.getText(),
								muskoRadioButton.isSelected() ? "Muški" : "Ženski",
								adresaTextField.getText(),
								Integer.parseInt(postanskiBrojTextField.getText()),
								strucnaSpremaTextField.getText(),
								brojTelefonaTextField.getText(),
								emailTextField.getText())) {
			lozinkaTextField.clear();
			Alert alert=new Alert(AlertType.INFORMATION);
    		alert.setTitle("Informacija");
    		alert.setHeaderText(null);
    		alert.setContentText("Nalog uspjesno dodan.");
    		alert.showAndWait();
		}
    }
}
