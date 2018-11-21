package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.util.ResourceBundle;

import org.unibl.etf.karta.Prevoznik;
import org.unibl.etf.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class DodavanjePrevoznikaController implements Initializable{

	@FXML
	private ImageView successImageView = new ImageView();
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
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		successImageView.setVisible(false);
		validateSetUp();
		
	}
	
	public void validateSetUp() {
		emailTextField.getValidators().addAll(Util.requredFieldValidator(emailTextField),Util.emailValidator(emailTextField));
		jibTextField.getValidators().addAll(Util.requredFieldValidator(jibTextField),Util.jibValidator(jibTextField));
		webAdresaTextField.getValidators().addAll(Util.requredFieldValidator(webAdresaTextField),Util.webValidator(webAdresaTextField));
		telefonTextField.getValidators().addAll(Util.requredFieldValidator(telefonTextField),Util.phoneValidator(telefonTextField));
		postanskiBrojTextField.getValidators().addAll(Util.requredFieldValidator(postanskiBrojTextField),Util.postalCodeValidator(postanskiBrojTextField));
		Util.setAutocompleteList(postanskiBrojTextField, Util.getPostalCodeList());
		tekuciRacunTextField.getValidators().addAll(Util.requredFieldValidator(tekuciRacunTextField),Util.integerValidator(tekuciRacunTextField));
		nazivTextField.getValidators().add(Util.requredFieldValidator(nazivTextField));
		tekuciRacunTextField.getValidators().addAll(Util.requredFieldValidator(tekuciRacunTextField),Util.integerValidator(tekuciRacunTextField));
		adresaTextField.getValidators().add(Util.requredFieldValidator(adresaTextField));
	}


	
	@FXML
	public void dodajPrevoznika() {
		if(jibTextField.validate() &
				nazivTextField.validate() &
					telefonTextField.validate() &
						emailTextField.validate() &
							tekuciRacunTextField.validate() &
								adresaTextField.validate() &
									webAdresaTextField.validate() &
										postanskiBrojTextField.validate()) {
		if(Prevoznik.dodajPrevoznika(jibTextField.getText(),nazivTextField.getText(),telefonTextField.getText(),emailTextField.getText(),
				webAdresaTextField.getText(),tekuciRacunTextField.getText(),adresaTextField.getText(),Integer.parseInt(postanskiBrojTextField.getText()))) {
			showSuccess();
		}
	}

	}

	public void showSuccess() {
		Timeline timeline = new Timeline();
		nazivTextField.clear();
		jibTextField.clear();
		postanskiBrojTextField.clear();
		adresaTextField.clear();
		webAdresaTextField.clear();
		telefonTextField.clear();
		emailTextField.clear();
		tekuciRacunTextField.clear();
		postanskiBrojTextField.clear();

		telefonTextField.resetValidation();
		jibTextField.resetValidation();
		adresaTextField.resetValidation();
		webAdresaTextField.resetValidation();
		emailTextField.resetValidation();
		tekuciRacunTextField.resetValidation();
		postanskiBrojTextField.resetValidation();
		nazivTextField.resetValidation();
		
		//successImageView.setImage(new Image(getClass().getResource("img/checkmark.png").toExternalForm()));
		successImageView.setVisible(true);
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(3),
		    new EventHandler<ActionEvent>() {
		        @Override
		        public void handle(ActionEvent event) {
		        	successImageView.setVisible(false);
		        }
		    }));
		timeline.play();
	}

}
