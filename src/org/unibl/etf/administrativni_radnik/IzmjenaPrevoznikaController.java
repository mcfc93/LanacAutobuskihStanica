package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.util.ResourceBundle;
import org.unibl.etf.karta.Prevoznik;
import org.unibl.etf.util.Mjesto;
import org.unibl.etf.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class IzmjenaPrevoznikaController implements Initializable{
	
	@FXML
	private AnchorPane anchorPane = new AnchorPane();
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
	private ImageView exitImageView = new ImageView();
	private double xOffset=0;
    private double yOffset=0;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		anchorPane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
				xOffset = stage.getX() - event.getScreenX();
				yOffset = stage.getY() - event.getScreenY();
			}
		});
		anchorPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
			   	Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
			    if(!stage.isMaximized()) {
			    	stage.setX(event.getScreenX() + xOffset);
			    	stage.setY(event.getScreenY() + yOffset);
			    	stage.setOpacity(0.8);
			    }
			}
		});
		anchorPane.setOnMouseReleased((event) -> {
			Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
			stage.setOpacity(1.0);
		});
		
		jibTextField.setEditable(false);
		jibTextField.setText(ListaPrevoznikaController.odabraniPrevoznik.getJIBPrevoznika());
		nazivTextField.setText(ListaPrevoznikaController.odabraniPrevoznik.getNaziv());
		adresaTextField.setText(ListaPrevoznikaController.odabraniPrevoznik.getAdresa());
		emailTextField.setText(ListaPrevoznikaController.odabraniPrevoznik.getEmail());
		telefonTextField.setText(ListaPrevoznikaController.odabraniPrevoznik.getTelefon());
		webAdresaTextField.setText(ListaPrevoznikaController.odabraniPrevoznik.getWebAdresa());
		tekuciRacunTextField.setText(ListaPrevoznikaController.odabraniPrevoznik.getRacun());
		postanskiBrojTextField.setText(String.valueOf(ListaPrevoznikaController.odabraniPrevoznik.getPostanskiBroj() + " - " + Mjesto.getPlaceList().stream().filter(m -> m.getPostanskiBroj()==ListaPrevoznikaController.odabraniPrevoznik.getPostanskiBroj()).findFirst().get().getNaziv()));
		Util.setAutocompleteList(postanskiBrojTextField, Mjesto.getCityPostalCodeList());	
		adresaTextField.getValidators().add(Util.requredFieldValidator(adresaTextField));
		nazivTextField.getValidators().add(Util.requredFieldValidator(nazivTextField));
		emailTextField.getValidators().addAll(Util.requredFieldValidator(emailTextField),Util.emailValidator(emailTextField));
		telefonTextField.getValidators().addAll(Util.requredFieldValidator(telefonTextField),Util.phoneValidator(telefonTextField));
		webAdresaTextField.getValidators().addAll(Util.requredFieldValidator(webAdresaTextField),Util.webValidator(webAdresaTextField));
		postanskiBrojTextField.getValidators().addAll(Util.requredFieldValidator(postanskiBrojTextField),Util.collectionValidator(postanskiBrojTextField, Mjesto.getCityPostalCodeList(), true, "Nekorektan unos"));
		tekuciRacunTextField.getValidators().addAll(Util.requredFieldValidator(tekuciRacunTextField),Util.integerValidator(tekuciRacunTextField));
		Util.collectionValidator(postanskiBrojTextField, Mjesto.getCityPostalCodeList(), true, "Greska.");
	}

	@FXML
	public void exit() {
		Stage stage = (Stage) exitImageView.getScene().getWindow();
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
									tekuciRacunTextField.getText(), adresaTextField.getText(),postanskiBrojTextField.getText().split("-")[0].trim(), 
									ListaPrevoznikaController.odabraniPrevoznik.getJIBPrevoznika());
		ListaPrevoznikaController.odabraniPrevoznik.setAdresa(adresaTextField.getText());
		ListaPrevoznikaController.odabraniPrevoznik.setNaziv(nazivTextField.getText());
		ListaPrevoznikaController.odabraniPrevoznik.setTelefon(telefonTextField.getText());
		ListaPrevoznikaController.odabraniPrevoznik.setEmail(emailTextField.getText());
		ListaPrevoznikaController.odabraniPrevoznik.setWebAdresa(webAdresaTextField.getText());
		ListaPrevoznikaController.odabraniPrevoznik.setRacun(tekuciRacunTextField.getText());
		ListaPrevoznikaController.odabraniPrevoznik.setPostanskiBroj(Integer.parseInt(postanskiBrojTextField.getText().split("-")[0].trim()));
		Stage stage = (Stage) okButton.getScene().getWindow();
		stage.close();
	}
	}
}
