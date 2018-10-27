package org.unibl.etf.salterski_radnik;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

import org.unibl.etf.karta.MjesecVazenja;
import org.unibl.etf.karta.TipKarte;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class KreiranjeMjesecneKarteController implements Initializable {
	
	@FXML
	private JFXButton kreirajKartuButton = new JFXButton();
	@FXML
	private Button button = new Button();
	@FXML
	private Label imeLabel;
	@FXML
	private Label prezimeLabel;
	@FXML
	private Label mjesecVazenjaLabel;
	@FXML
	private Label slikaLabel;
	@FXML
	private Label tipKarteLabel;
	@FXML
	private Label slikaKorisnikaLabel;
	
	@FXML
	private JFXTextField imeTextField;
	@FXML
	private JFXTextField prezimeTextField;
	@FXML
	private JFXTextField nazivSlikeTextField;
	
	@FXML
	private JFXComboBox<MjesecVazenja> mjesecVazenjaComboBox;
	@FXML
	private  JFXComboBox<TipKarte> tipKarteComboBox;
	

	@FXML
	private ImageView slikaKorisnika;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			slikaKorisnika.setImage(new Image(new FileInputStream(new File("C:\\Users\\93van\\Desktop\\Fakultet\\stalker.png"))));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mjesecVazenjaComboBox.setPromptText("Odaberite mjesec vazenja");
		tipKarteComboBox.setPromptText("Odaberite tip karte");
		mjesecVazenjaComboBox.setItems(FXCollections.observableArrayList(MjesecVazenja.values()));
		tipKarteComboBox.setItems(FXCollections.observableArrayList(TipKarte.values()));
		imeTextField.setPromptText("Unesite ime");
		prezimeTextField.setPromptText("Unesite prezime");
		nazivSlikeTextField.setPromptText("Naziv slike");
		
	}
	
	@FXML
	public void test() {
		System.out.println("AFsfas");
	}
	@FXML
	public void test2() {
		System.out.println("opa");
	}
	

}
