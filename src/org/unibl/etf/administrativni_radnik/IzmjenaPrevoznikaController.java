package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.util.ResourceBundle;
import org.unibl.etf.karta.Prevoznik;
import org.unibl.etf.util.Mjesto;
import org.unibl.etf.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.base.ValidatorBase;

import javafx.application.Platform;
import javafx.event.ActionEvent;
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
	private AnchorPane anchorPane;
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
	private JFXTextField djackiPopustTextField = new JFXTextField();
	@FXML
	private JFXTextField penzionerskiPopustTextField = new JFXTextField();
	@FXML
	private JFXTextField radnickiPopustTextField = new JFXTextField();
	
	@FXML
	private JFXButton okButton = new JFXButton();
	
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
		
		
		
		jibTextField.setDisable(true);
		postanskiBrojTextField.setDisable(true);
		okButton.setDefaultButton(true);
		
		jibTextField.setText(ListaPrevoznikaController.odabraniPrevoznik.getJIBPrevoznika());
		nazivTextField.setText(ListaPrevoznikaController.odabraniPrevoznik.getNaziv());
		adresaTextField.setText(ListaPrevoznikaController.odabraniPrevoznik.getAdresa());
		emailTextField.setText(ListaPrevoznikaController.odabraniPrevoznik.getEmail());
		telefonTextField.setText(ListaPrevoznikaController.odabraniPrevoznik.getTelefon());
		webAdresaTextField.setText(ListaPrevoznikaController.odabraniPrevoznik.getWebAdresa());
		tekuciRacunTextField.setText(ListaPrevoznikaController.odabraniPrevoznik.getRacun());
		postanskiBrojTextField.setText(String.valueOf(ListaPrevoznikaController.odabraniPrevoznik.getMjesto().getPostanskiBroj() + " - " + Mjesto.getPlaceList().stream().filter(m -> m.getPostanskiBroj()==ListaPrevoznikaController.odabraniPrevoznik.getMjesto().getPostanskiBroj()).findFirst().get().getNaziv()));
		int djackiPopust = (int) (100 - 100*ListaPrevoznikaController.odabraniPrevoznik.getDjackiPopust());
		int penzionerskiPopust = (int) (100 - 100*ListaPrevoznikaController.odabraniPrevoznik.getPenzionerskiPopust());
		int radnickiPopust = (int) (100 - 100*ListaPrevoznikaController.odabraniPrevoznik.getRadnickiPopust());
		
		djackiPopustTextField.setText(Integer.toString(djackiPopust));
		penzionerskiPopustTextField.setText(Integer.toString(penzionerskiPopust));
		radnickiPopustTextField.setText(Integer.toString(radnickiPopust));
		
		
		
		//Util.setAutocompleteList(postanskiBrojTextField, Mjesto.getCityPostalCodeList());	
		//Util.collectionValidator(postanskiBrojTextField, Mjesto.getCityPostalCodeList(), true, "Greska.");
		
		jibTextField.getValidators().addAll(Util.requiredFieldValidator(jibTextField), Util.jibValidator(jibTextField));
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
    void close(MouseEvent event) {
    	((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }
	
	@FXML
	public void izmjeniPrevoznika(ActionEvent event) {
		int djackiPopust = Integer.parseInt(djackiPopustTextField.getText());
		int penzionerskiPopust = Integer.parseInt(penzionerskiPopustTextField.getText());
		int radnickiPopust = Integer.parseInt(radnickiPopustTextField.getText());
		if(jibTextField.validate()
				& nazivTextField.validate()
					& adresaTextField.validate()
						& emailTextField.validate()
							& telefonTextField.validate()
								& webAdresaTextField.validate()
									& postanskiBrojTextField.validate()
										& tekuciRacunTextField.validate()
											& djackiPopustTextField.validate()
												& radnickiPopustTextField.validate()
													& penzionerskiPopustTextField.validate()
													) {
			if(Prevoznik.izmjeniPrevoznika(nazivTextField.getText(), telefonTextField.getText(), emailTextField.getText(), webAdresaTextField.getText(), 
										tekuciRacunTextField.getText(), adresaTextField.getText(),postanskiBrojTextField.getText().split("-")[0].trim(), 
										ListaPrevoznikaController.odabraniPrevoznik.getJIBPrevoznika(),djackiPopust,penzionerskiPopust,radnickiPopust)) {
				ListaPrevoznikaController.odabraniPrevoznik.setAdresa(adresaTextField.getText());
				ListaPrevoznikaController.odabraniPrevoznik.setNaziv(nazivTextField.getText());
				ListaPrevoznikaController.odabraniPrevoznik.setTelefon(telefonTextField.getText());
				ListaPrevoznikaController.odabraniPrevoznik.setEmail(emailTextField.getText());
				ListaPrevoznikaController.odabraniPrevoznik.setWebAdresa(webAdresaTextField.getText());
				ListaPrevoznikaController.odabraniPrevoznik.setRacun(tekuciRacunTextField.getText());
				ListaPrevoznikaController.odabraniPrevoznik.getMjesto().setPostanskiBroj(Integer.parseInt(postanskiBrojTextField.getText().split("-")[0].trim()));
				ListaPrevoznikaController.odabraniPrevoznik.setDjackiPopust((100-djackiPopust)/100 );
				ListaPrevoznikaController.odabraniPrevoznik.setPenzionerskiPopust( (100-penzionerskiPopust)/100 );
				ListaPrevoznikaController.odabraniPrevoznik.setRadnickiPopust( (100-radnickiPopust)/100 );
				Platform.runLater(() -> {
		    		Util.getNotifications("Obavještenje", "Prevoznik izmjenjen.", "Information").show();
		    	});
			} else {
				//NASTALA GRESKA
				Util.showBugAlert();
			}
			((Stage)((Node)event.getSource()).getScene().getWindow()).close();
		}
	}
}
