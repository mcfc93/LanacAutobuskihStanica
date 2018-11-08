package org.unibl.etf.salterski_radnik;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.util.Util;
import org.unibl.etf.zaposleni.Zaposleni;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class KorisnickiNalogController implements Initializable {
	
    @FXML
    private JFXPasswordField staraLozinkaTextField;

    @FXML
    private JFXPasswordField novaLoxinka1TextField;

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
	}
	
    @FXML
    void potvrdi(ActionEvent event) {

    }
	
}
