package org.unibl.etf.salterski_radnik;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import org.unibl.etf.karta.MjesecVazenja;
import org.unibl.etf.karta.MjesecnaKarta;
import org.unibl.etf.karta.TipKarte;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class KreiranjeMjesecneKarteController implements Initializable {	
	
	@FXML
	private JFXButton odaberiSlikuButton = new JFXButton();
	@FXML
	private Label slikaKorisnikaLabel;

	@FXML
	private JFXTextField imeField = new JFXTextField();
	@FXML
	private JFXTextField prezimeField = new JFXTextField();
	@FXML
	private JFXTextField nazivSlikeTextField;
	@FXML
	private JFXButton kreirajButton = new JFXButton();
	
	
	@FXML
	private JFXComboBox<MjesecVazenja> mjesecVazenjaComboBox;
	@FXML
	private JFXComboBox<TipKarte> tipKarteComboBox;
	
	@FXML
	public ImageView slikaKorisnika;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		kreirajButton.setDisable(true);
		nazivSlikeTextField.setEditable(false);
		mjesecVazenjaComboBox.setItems(FXCollections.observableArrayList(MjesecVazenja.values()));
		tipKarteComboBox.setItems(FXCollections.observableArrayList(TipKarte.values()));
		kreirajButton.disableProperty().bind(Bindings.createBooleanBinding(
			    () -> imeField.getText().isEmpty() || prezimeField.getText().isEmpty() || 
			    	  nazivSlikeTextField.getText().isEmpty() || 
			    	  mjesecVazenjaComboBox.getSelectionModel().getSelectedItem() == null ||
			    	  tipKarteComboBox.getSelectionModel().getSelectedItem() == null, 
			    imeField.textProperty(), prezimeField.textProperty(), nazivSlikeTextField.textProperty(),
			    mjesecVazenjaComboBox.getSelectionModel().selectedItemProperty(),
			    tipKarteComboBox.getSelectionModel().selectedItemProperty()
			    ));
		
		System.out.println(mjesecVazenjaComboBox.getSelectionModel().getSelectedItem());
	}
	
	@FXML
	public void odaberiSliku() {
		System.out.println("fasf");
		FileChooser fc  = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter("PNG Files", "*.png"));
		File odabranaSlika = fc.showOpenDialog(null);
		if(odabranaSlika!=null) {
			try {
				slikaKorisnika.setImage(new Image(new FileInputStream(odabranaSlika)));
				nazivSlikeTextField.setText(odabranaSlika.getName());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	public void kreirajKartu() {
		/*
		DODATI LOGIKU ZA PROVJERU UNOSA
		
		*/
		
	}
	

}
