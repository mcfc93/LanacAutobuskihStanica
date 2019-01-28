package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.util.ResourceBundle;
import org.unibl.etf.karta.Prevoznik;
import org.unibl.etf.util.Mjesto;
import org.unibl.etf.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

public class DodavanjePrevoznikaController implements Initializable {
	@FXML
    private GridPane gridPane;
	
	@FXML
	private JFXButton dodajPrevoznikaButton = new JFXButton();
	@FXML
	private JFXTextField jibTextField = new JFXTextField();
	@FXML
	private JFXTextField nazivTextField = new JFXTextField();
	@FXML
	private JFXTextField telefonTextField = new JFXTextField();
	@FXML
	private JFXTextField emailTextField = new JFXTextField();
	@FXML
	private JFXTextField webAdresaTextField = new JFXTextField();
	@FXML
	private JFXTextField tekuciRacunTextField = new JFXTextField();
	@FXML
	private JFXTextField postanskiBrojTextField = new JFXTextField();
	@FXML
	private JFXTextField adresaTextField = new JFXTextField();
	@FXML
	private JFXTextField djackiPopustTextField = new JFXTextField();
	@FXML
	private JFXTextField penzionerskiPopustTextField = new JFXTextField();
	@FXML
	private JFXTextField radnickiPopustTextField = new JFXTextField();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dodajPrevoznikaButton.setDefaultButton(true);
		Util.setAutocompleteList(postanskiBrojTextField, Mjesto.getCityPostalCodeList());

		jibTextField.getValidators().addAll(Util.requiredFieldValidator(jibTextField), Util.jibValidator(jibTextField), Util.jibValidator(jibTextField)/*, Util.collectionValidator(jibTextField, Prevoznik.getJibList(), false, "Zauzeto")*/);
		nazivTextField.getValidators().addAll(Util.requiredFieldValidator(nazivTextField), Util.lengthValidator(nazivTextField, 35));
		tekuciRacunTextField.getValidators().addAll(Util.requiredFieldValidator(tekuciRacunTextField), Util.iinValidator(tekuciRacunTextField));
		telefonTextField.getValidators().addAll(Util.requiredFieldValidator(telefonTextField), Util.phoneValidator(telefonTextField), Util.lengthValidator(telefonTextField, 16));
		adresaTextField.getValidators().addAll(Util.requiredFieldValidator(adresaTextField), Util.lengthValidator(adresaTextField, 35));
		postanskiBrojTextField.getValidators().addAll(Util.requiredFieldValidator(postanskiBrojTextField), Util.collectionValidator(postanskiBrojTextField, Mjesto.getCityPostalCodeList(), true, "Nekorektan unos"));
		webAdresaTextField.getValidators().addAll(Util.requiredFieldValidator(webAdresaTextField), Util.webValidator(webAdresaTextField), Util.lengthValidator(webAdresaTextField, 35));
		emailTextField.getValidators().addAll(Util.requiredFieldValidator(emailTextField), Util.emailValidator(emailTextField), Util.lengthValidator(emailTextField, 35));
		djackiPopustTextField.getValidators().addAll(Util.requiredFieldValidator(djackiPopustTextField), Util.popustValidator(djackiPopustTextField));
		penzionerskiPopustTextField.getValidators().addAll(Util.requiredFieldValidator(penzionerskiPopustTextField), Util.popustValidator(penzionerskiPopustTextField));
		radnickiPopustTextField.getValidators().addAll(Util.requiredFieldValidator(radnickiPopustTextField), Util.popustValidator(radnickiPopustTextField));
		
	}
	
	@FXML
	public void dodajPrevoznika() {
		int djackiPopust = Integer.parseInt(djackiPopustTextField.getText());
		int radnickiPopust = Integer.parseInt(radnickiPopustTextField.getText());
		int penzionerskiPopust = Integer.parseInt(penzionerskiPopustTextField.getText());
		if(jibTextField.validate() &
				nazivTextField.validate() &
					telefonTextField.validate() &
						emailTextField.validate() &
							tekuciRacunTextField.validate() &
								adresaTextField.validate() &
									webAdresaTextField.validate() &
										postanskiBrojTextField.validate() &
											djackiPopustTextField.validate() &
												penzionerskiPopustTextField.validate() &
													radnickiPopustTextField.validate()) {
			if(Prevoznik.dodajPrevoznika(jibTextField.getText(),nazivTextField.getText(),telefonTextField.getText(),emailTextField.getText(),
					webAdresaTextField.getText(),tekuciRacunTextField.getText(),adresaTextField.getText(),Integer.parseInt(postanskiBrojTextField.getText().split("-")[0].trim()),
						djackiPopust, radnickiPopust, penzionerskiPopust)) {
//Prevoznik.getJibList().add(jibTextField.getText().trim());
				Util.getNotifications("Obavještenje", "Prevoznik dodan.", "Information").show();
				
				nazivTextField.clear();
				jibTextField.clear();
				postanskiBrojTextField.clear();
				adresaTextField.clear();
				webAdresaTextField.clear();
				telefonTextField.clear();
				emailTextField.clear();
				tekuciRacunTextField.clear();

				telefonTextField.resetValidation();
				jibTextField.resetValidation();
				adresaTextField.resetValidation();
				webAdresaTextField.resetValidation();
				emailTextField.resetValidation();
				tekuciRacunTextField.resetValidation();
				postanskiBrojTextField.resetValidation();
				nazivTextField.resetValidation();
			} else {
				//NASTALA GRESKA
				Util.showBugAlert();
			}
		}

	}
}
