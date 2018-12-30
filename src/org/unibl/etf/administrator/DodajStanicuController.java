package org.unibl.etf.administrator;

import java.net.URL;
import java.util.ResourceBundle;

import org.unibl.etf.autobuska_stanica.AutobuskaStanica;
import org.unibl.etf.util.Mjesto;
import org.unibl.etf.util.Util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
		potvrdiButton.setDefaultButton(true);
		
		//Util.setAutocompleteList(postanskiBrojTextField, Mjesto.getPostalCodeList());
		Util.setAutocompleteList(postanskiBrojTextField, Mjesto.getCityPostalCodeList());
		
		jibTextField.getValidators().addAll(Util.requiredFieldValidator(jibTextField), Util.jibValidator(jibTextField), Util.collectionValidator(jibTextField, AutobuskaStanica.getJibList(), false, "Zauzeto"));
		nazivTextField.getValidators().add(Util.requiredFieldValidator(nazivTextField));
		brojPeronaTextField.getValidators().addAll(Util.requiredFieldValidator(brojPeronaTextField), Util.integerValidator(brojPeronaTextField));
		brojTelefonaTextField.getValidators().addAll(Util.requiredFieldValidator(brojTelefonaTextField), Util.phoneValidator(brojTelefonaTextField), Util.lengthValidator(brojTelefonaTextField, 15));
		adresaTextField.getValidators().add(Util.requiredFieldValidator(adresaTextField));
		//postanskiBrojTextField.getValidators().addAll(Util.requiredFieldValidator(postanskiBrojTextField), Util.collectionValidator(postanskiBrojTextField, Mjesto.getPostalCodeList(), true, "Nekorektan unos"));
		postanskiBrojTextField.getValidators().addAll(Util.requiredFieldValidator(postanskiBrojTextField), Util.collectionValidator(postanskiBrojTextField, Mjesto.getCityPostalCodeList(), true, "Nekorektan unos"));
		webStranicaTextField.getValidators().addAll(Util.requiredFieldValidator(webStranicaTextField), Util.webValidator(webStranicaTextField));
		emailTextField.getValidators().addAll(Util.requiredFieldValidator(emailTextField), Util.emailValidator(emailTextField));
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
					/*Integer.parseInt(postanskiBrojTextField.getText().trim()),*/
					Integer.parseInt(postanskiBrojTextField.getText().split("-")[0].trim()),
					brojTelefonaTextField.getText().trim(),
					Integer.parseInt(brojPeronaTextField.getText().trim()),
					webStranicaTextField.getText().trim(),
					emailTextField.getText().trim())) {
				AutobuskaStanica.getJibList().add(jibTextField.getText().trim());
				
				/*
				Alert alert=new Alert(AlertType.INFORMATION);
	    		alert.setTitle("Obavještenje");
	    		alert.setHeaderText(null);
	    		alert.setContentText("Stanica uspješno dodana.");
	    		
	    		alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/unibl/etf/application.css").toExternalForm());
				alert.getDialogPane().getStyleClass().add("alert");
				
	    		alert.showAndWait();
	    		*/
				
				Util.getNotifications("Obavještenje", "Autobuska stanica dodana.", "Information").show();
				
				jibTextField.clear();
				jibTextField.resetValidation();
				nazivTextField.clear();
				nazivTextField.resetValidation();
				brojPeronaTextField.clear();
				brojPeronaTextField.resetValidation();
				brojTelefonaTextField.clear();
				brojTelefonaTextField.resetValidation();
				adresaTextField.clear();
				adresaTextField.resetValidation();
				postanskiBrojTextField.clear();
				postanskiBrojTextField.resetValidation();
				webStranicaTextField.clear();
				webStranicaTextField.resetValidation();
				emailTextField.clear();
				emailTextField.resetValidation();
			} else {
				//NASTALA GRESKA
				Util.showBugAlert();
			}
		}
    }
}
