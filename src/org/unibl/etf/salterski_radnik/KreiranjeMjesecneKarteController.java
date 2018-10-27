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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class KreiranjeMjesecneKarteController implements Initializable {
	
	@FXML
	public Label imeLabel;
	@FXML
	public Label prezimeLabel;
	@FXML
	public Label mjesecVazenjaLabel;
	@FXML
	public Label slikaLabel;
	@FXML
	public Label tipKarteLabel;
	@FXML
	public Label slikaKorisnikaLabel;
	
	@FXML
	public JFXTextField imeTextField;
	@FXML
	public JFXTextField prezimeTextField;
	@FXML
	public JFXTextField nazivSlikeTextField;
	
	@FXML
	public JFXComboBox<MjesecVazenja> mjesecVazenjaComboBox;
	@FXML
	public JFXComboBox<TipKarte> tipKarteComboBox;
	
	@FXML
	public JFXButton kreirajButton = new JFXButton();
	
	@FXML
	public ImageView slikaKorisnika;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		kreirajButton.setVisible(true);
		/*try {
			slikaKorisnika.setImage(new Image(new FileInputStream(new File("C:\\Users\\93van\\Desktop\\Fakultet\\stalker.png"))));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		mjesecVazenjaComboBox.setPromptText("Odaberite mjesec vazenja");
		tipKarteComboBox.setPromptText("Odaberite tip karte");
		mjesecVazenjaComboBox.setItems(FXCollections.observableArrayList(MjesecVazenja.values()));
		tipKarteComboBox.setItems(FXCollections.observableArrayList(TipKarte.values()));
		imeTextField.setPromptText("Unesite ime");
		prezimeTextField.setPromptText("Unesite prezime");
		nazivSlikeTextField.setPromptText("Naziv slike");
		
	}
	
	

}
