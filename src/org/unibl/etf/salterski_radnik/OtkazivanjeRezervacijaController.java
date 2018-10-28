package org.unibl.etf.salterski_radnik;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class OtkazivanjeRezervacijaController implements Initializable {

	
	@FXML
	private JFXTextField idRezervacijeTextField = new JFXTextField();
	@FXML
	private JFXTextField test2 = new JFXTextField();
	@FXML
	private JFXButton pretragaButton = new JFXButton();
	@FXML
	private JFXButton otkazivanjeButton = new JFXButton();
	@FXML
	private JFXTextArea podaciTextArea = new JFXTextArea();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		pretragaButton.setDisable(true);
		otkazivanjeButton.setDisable(true);
		pretragaButton.disableProperty().bind(Bindings.createBooleanBinding(
			    () -> idRezervacijeTextField.getText().isEmpty() || test2.getText().isEmpty(), 
			    idRezervacijeTextField.textProperty(), test2.textProperty()));
		
	}
	
	@FXML
	public void provjera() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Potvrda korisnika");
		alert.setHeaderText("Potvrda o otkazivanju rezervacije");
		alert.setContentText("Da li ste sigurni?");
		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == ButtonType.OK){
		    // KONEKCIJA SA BAZOM
			System.out.println("Uklonjena rezervacija");
		}
		else
			System.out.println("Korisnik odustao");
	}

	
	
}
