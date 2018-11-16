package org.unibl.etf.administrator;

import java.net.URL;
import java.util.ResourceBundle;

import org.unibl.etf.autobuska_stanica.AutobuskaStanica;

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
		
	}
	
	@FXML
    void potvrdi(ActionEvent event) {
		if(AutobuskaStanica.dodavanjeAutobuskeStanice(jibTextField.getText().trim(), nazivTextField.getText().trim(), adresaTextField.getText().trim(), Integer.parseInt(postanskiBrojTextField.getText().trim()), brojTelefonaTextField.getText().trim(), Integer.parseInt(brojPeronaTextField.getText().trim()), webStranicaTextField.getText().trim())) {
			Alert alert=new Alert(AlertType.INFORMATION);
    		alert.setTitle("Greška");
    		alert.setHeaderText(null);
    		alert.setContentText("Stanica uspjesno dodana.");
    		alert.showAndWait();
		}
    }
}
