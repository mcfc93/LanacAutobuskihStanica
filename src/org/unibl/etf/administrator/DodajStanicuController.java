package org.unibl.etf.administrator;

import java.net.URL;
import java.util.ResourceBundle;

import org.unibl.etf.autobuska_stanica.AutobuskaStanica;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class DodajStanicuController implements Initializable {
    @FXML
    private GridPane gridPane;

    @FXML
    private JFXTextField jibTextField;

    @FXML
    private JFXTextField nazivTextField;

    @FXML
    private JFXTextField brojPeronaTextField;

    @FXML
    private JFXTextField brojTelefonaTextField;

    @FXML
    private JFXTextField adresaTextField;

    @FXML
    private JFXTextField postanskiBrojTextField;

    @FXML
    private JFXTextField webStranicaTextField;

    @FXML
    private JFXTextField emailTextField;

    @FXML
    private JFXButton potvrdiButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		jibTextField.getValidators().add(requredFieldValidator(jibTextField));
		nazivTextField.getValidators().add(requredFieldValidator(nazivTextField));
		brojPeronaTextField.getValidators().add(requredFieldValidator(brojPeronaTextField));
		brojTelefonaTextField.getValidators().add(requredFieldValidator(brojTelefonaTextField));
		adresaTextField.getValidators().add(requredFieldValidator(adresaTextField));
		postanskiBrojTextField.getValidators().add(requredFieldValidator(postanskiBrojTextField));
		webStranicaTextField.getValidators().add(requredFieldValidator(webStranicaTextField));
		emailTextField.getValidators().add(requredFieldValidator(emailTextField));
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
	        	textField.validate();
	    });
	    return requiredFieldValidator;
    }
	
	@FXML
    void potvrdi(ActionEvent event) {
		if(jibTextField.validate()
				& nazivTextField.validate()
					& brojPeronaTextField.validate()
						& brojTelefonaTextField.validate()
							& adresaTextField.validate()
								& postanskiBrojTextField.validate()
									& webStranicaTextField.validate()
										& emailTextField.validate()) {
			if(AutobuskaStanica.dodavanjeAutobuskeStanice(jibTextField.getText().trim(),
					nazivTextField.getText().trim(),
					adresaTextField.getText().trim(),
					Integer.parseInt(postanskiBrojTextField.getText().trim()),
					brojTelefonaTextField.getText().trim(),
					Integer.parseInt(brojPeronaTextField.getText().trim()),
					webStranicaTextField.getText().trim(),
					emailTextField.getText().trim())) {
				Alert alert=new Alert(AlertType.INFORMATION);
	    		alert.setTitle("Obavještenje");
	    		alert.setHeaderText(null);
	    		alert.setContentText("Stanica uspješno dodana.");
	    		alert.showAndWait();
			}
		}
    }
}
