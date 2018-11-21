package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.util.ResourceBundle;
import org.unibl.etf.karta.Prevoznik;
import org.unibl.etf.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

public class IzmjenaPrevoznikaController implements Initializable{
	
	@FXML
	private JFXTextField nazivTextField = new JFXTextField();
	@FXML
	private JFXTextField adresaTextField = new JFXTextField();
	@FXML
	private JFXTextField emailTextField = new JFXTextField();
	@FXML
	private JFXTextField telefonTextField = new JFXTextField();
	@FXML
	private JFXTextField webAdresaTextField = new JFXTextField();
	@FXML
	private JFXTextField tekuciRacunTextField = new JFXTextField();
	@FXML
	private JFXTextField postanskiBrojTextField = new JFXTextField();
	@FXML
	private JFXTextField jibTextField = new JFXTextField();
	@FXML
	private JFXButton okButton = new JFXButton();
	@FXML
	private JFXButton otkaziButton = new JFXButton();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		jibTextField.setEditable(false);
		jibTextField.setText(ListaPrevoznikaController.odabraniPrevoznik.getJIBPrevoznika());
		nazivTextField.setText(ListaPrevoznikaController.odabraniPrevoznik.getNaziv());
		adresaTextField.setText(ListaPrevoznikaController.odabraniPrevoznik.getAdresa());
		emailTextField.setText(ListaPrevoznikaController.odabraniPrevoznik.getEmail());
		telefonTextField.setText(ListaPrevoznikaController.odabraniPrevoznik.getTelefon());
		webAdresaTextField.setText(ListaPrevoznikaController.odabraniPrevoznik.getWebAdresa());
		tekuciRacunTextField.setText(ListaPrevoznikaController.odabraniPrevoznik.getRacun());
		postanskiBrojTextField.setText(String.valueOf(ListaPrevoznikaController.odabraniPrevoznik.getPostanskiBroj()));
		Util.setAutocompleteList(postanskiBrojTextField, Util.getPostalCodeList());
		
		adresaTextField.getValidators().add(Util.requredFieldValidator(adresaTextField));
		nazivTextField.getValidators().add(Util.requredFieldValidator(nazivTextField));
		emailTextField.getValidators().addAll(Util.requredFieldValidator(emailTextField),Util.emailValidator(emailTextField));
		telefonTextField.getValidators().addAll(Util.requredFieldValidator(telefonTextField),Util.phoneValidator(telefonTextField));
		webAdresaTextField.getValidators().addAll(Util.requredFieldValidator(webAdresaTextField),Util.webValidator(webAdresaTextField));
		postanskiBrojTextField.getValidators().addAll(Util.requredFieldValidator(postanskiBrojTextField),Util.postalCodeValidator(postanskiBrojTextField));
		tekuciRacunTextField.getValidators().addAll(Util.requredFieldValidator(tekuciRacunTextField),Util.integerValidator(tekuciRacunTextField));

	}

	@FXML
	public void otkazi() {
		 Stage stage = (Stage) otkaziButton.getScene().getWindow();
		    stage.close();
	}
	@FXML
	public void izmjeniPrevoznika() {
	if(nazivTextField.validate() &
			adresaTextField.validate() &
				emailTextField.validate() &
					telefonTextField.validate() &
						webAdresaTextField.validate() &
							postanskiBrojTextField.validate() &
								tekuciRacunTextField.validate()) {
		
		Prevoznik.izmjeniPrevoznika(nazivTextField.getText(), telefonTextField.getText(), emailTextField.getText(), webAdresaTextField.getText(), 
									tekuciRacunTextField.getText(), adresaTextField.getText(),postanskiBrojTextField.getText(), 
									ListaPrevoznikaController.odabraniPrevoznik.getJIBPrevoznika());
		ListaPrevoznikaController.odabraniPrevoznik.setAdresa(adresaTextField.getText());
		ListaPrevoznikaController.odabraniPrevoznik.setNaziv(nazivTextField.getText());
		ListaPrevoznikaController.odabraniPrevoznik.setTelefon(telefonTextField.getText());
		ListaPrevoznikaController.odabraniPrevoznik.setEmail(emailTextField.getText());
		ListaPrevoznikaController.odabraniPrevoznik.setWebAdresa(webAdresaTextField.getText());
		ListaPrevoznikaController.odabraniPrevoznik.setRacun(tekuciRacunTextField.getText());
		ListaPrevoznikaController.odabraniPrevoznik.setPostanskiBroj(Integer.parseInt(postanskiBrojTextField.getText()));
		Stage stage = (Stage) okButton.getScene().getWindow();
		stage.close();
	}
	}
}
