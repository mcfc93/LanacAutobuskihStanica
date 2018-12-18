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
		Util.setAutocompleteList(postanskiBrojTextField, Mjesto.getPostalCodeList());
		
		jibTextField.getValidators().addAll(Util.requredFieldValidator(jibTextField), Util.jibValidator(jibTextField), Util.collectionValidator(jibTextField, AutobuskaStanica.getJibList(), false, "Zauzeto"));
		nazivTextField.getValidators().add(Util.requredFieldValidator(nazivTextField));
		brojPeronaTextField.getValidators().addAll(Util.requredFieldValidator(brojPeronaTextField), Util.integerValidator(brojPeronaTextField));
		brojTelefonaTextField.getValidators().addAll(Util.requredFieldValidator(brojTelefonaTextField), Util.phoneValidator(brojTelefonaTextField));
		adresaTextField.getValidators().add(Util.requredFieldValidator(adresaTextField));
		postanskiBrojTextField.getValidators().addAll(Util.requredFieldValidator(postanskiBrojTextField), Util.collectionValidator(postanskiBrojTextField, Mjesto.getPostalCodeList(), true, "Nekorektan unos"));
		webStranicaTextField.getValidators().addAll(Util.requredFieldValidator(webStranicaTextField), Util.webValidator(webStranicaTextField));
		emailTextField.getValidators().addAll(Util.requredFieldValidator(emailTextField), Util.emailValidator(emailTextField));
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
				AutobuskaStanica.getJibList().add(jibTextField.getText().trim());
				Alert alert=new Alert(AlertType.INFORMATION);
	    		alert.setTitle("Obavje�tenje");
	    		alert.setHeaderText(null);
	    		alert.setContentText("Stanica uspje�no dodana.");
	    		alert.showAndWait();
			}
		}
    }
}
