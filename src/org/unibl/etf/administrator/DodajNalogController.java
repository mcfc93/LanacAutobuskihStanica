package org.unibl.etf.administrator;

import java.net.URL;
import java.util.ResourceBundle;

import org.unibl.etf.autobuska_stanica.AutobuskaStanica;
import org.unibl.etf.prijava.Nalog;
import org.unibl.etf.util.Mjesto;
import org.unibl.etf.util.Util;
import org.unibl.etf.zaposleni.Zaposleni;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

public class DodajNalogController implements Initializable {
    @FXML
    private GridPane gridPane;

    @FXML
    private JFXTextField korisnickoImeTextField;

    @FXML
    private JFXPasswordField lozinkaTextField;

    @FXML
    private JFXTextField jibStaniceTextField;

    @FXML
    private ToggleGroup tipGroup;

    @FXML
    private JFXRadioButton administrativniRadnikRadioButton;
    
    @FXML
    private JFXRadioButton salterskiRadnikRadioButton;

    @FXML
    private JFXTextField imeTextField;

    @FXML
    private JFXTextField prezimeTextField;

    @FXML
    private JFXTextField jmbgTextField;

    @FXML
    private JFXTextField adresaTextField;

    @FXML
    private JFXTextField postanskiBrojTextField;

    @FXML
    private JFXTextField strucnaSpremaTextField;

    @FXML
    private ToggleGroup polGroup;
    
    @FXML
    private JFXRadioButton muskiRadioButton;

    @FXML
    private JFXRadioButton zenskiRadioButton;

    @FXML
    private JFXTextField brojTelefonaTextField;

    @FXML
    private JFXTextField emailTextField;

    @FXML
    private JFXButton potvrdiButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/*
		potvrdiButton.setDisable(true);
	    
	    ChangeListener<String> changeListener=new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable,
	        						String oldValue, String newValue) {
	        	if((korisnickoImeTextField.getText().trim().length() < 6)
		        	|| (lozinkaTextField.getText().trim().length() < 6)) {
		        	potvrdiButton.setDisable(true);
		        } else {
		        	potvrdiButton.setDisable(false);
		        }
	        }
	    };
	    
	    korisnickoImeTextField.textProperty().addListener(changeListener);
	    lozinkaTextField.textProperty().addListener(changeListener);
	    */
		/*
		potvrdiButton.disableProperty().bind(
	    		korisnickoImeTextField.textProperty().isEmpty()
	    			.or(lozinkaTextField.textProperty().isEmpty())
	    		);
	   	*/
	    
	    
	    /*
	    lozinkaTextField.setTooltip(new Tooltip("Lozinka mora sadržavati najmanje 6 karaktera"));
	    
	    lozinkaTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue)->{
	        if(newValue.length() < 6) {
	    		System.out.println(newValue);
	        	lozinkaTextField.getTooltip().show(lozinkaTextField.getScene().getWindow());
	        } else {
	        	lozinkaTextField.getTooltip().hide();
	        }
	    });
	    */
		/*
		Platform.runLater(() -> {
			Bounds boundsInScene = lozinkaTextField.localToScene(lozinkaTextField.getBoundsInLocal());
			System.out.println("MinX=" + boundsInScene.getMinX() + ", MinY=" + boundsInScene.getMinY());
			Bounds boundsInScreen = lozinkaTextField.localToScreen(lozinkaTextField.getBoundsInLocal());
			System.out.println("MinX=" + boundsInScreen.getMinX() + ", MinY=" + boundsInScreen.getMinY());
			
			System.out.println(lozinkaTextField.getLayoutX() + " " + lozinkaTextField.getLayoutY());
			
			lozinkaTextField.setTooltip(new Tooltip("Lozinka mora sadržavati najmanje 6 karaktera"));
			lozinkaTextField.getTooltip().setWrapText(true);
			lozinkaTextField.getTooltip().maxWidth(125);
		    
		    lozinkaTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue)->{
		        if(newValue.length() < 6) {
		        	lozinkaTextField.getTooltip().show(lozinkaTextField.getScene().getWindow(), boundsInScreen.getMinX(), boundsInScreen.getMinY());
		        } else {
		        	lozinkaTextField.getTooltip().hide();
		        }
		    });
		});
		*/
		
		potvrdiButton.setDefaultButton(true);
		
		//Util.setAutocompleteList(postanskiBrojTextField, Mjesto.getPostalCodeList());
		Util.setAutocompleteList(postanskiBrojTextField, Mjesto.getCityPostalCodeList());
		//Util.setAutocompleteList(jibStaniceTextField, AutobuskaStanica.getJibList());
		Util.setAutocompleteList(jibStaniceTextField, AutobuskaStanica.getActiveJibList());
		

   		korisnickoImeTextField.getValidators().addAll(Util.requiredFieldValidator(korisnickoImeTextField), Util.collectionValidator(korisnickoImeTextField, Nalog.getUsernameList(), false, "Zauzeto"), Util.lengthValidator(korisnickoImeTextField, 30));
   		lozinkaTextField.getValidators().addAll(Util.requiredFieldValidator(lozinkaTextField), Util.passwordValidator(lozinkaTextField));
   		//jibStaniceTextField.getValidators().addAll(Util.requiredFieldValidator(jibStaniceTextField), Util.jibValidator(jibStaniceTextField), Util.collectionValidator(jibStaniceTextField, AutobuskaStanica.getJibList(), true, "Ne postoji"));
   		jibStaniceTextField.getValidators().addAll(Util.requiredFieldValidator(jibStaniceTextField), Util.jibValidator(jibStaniceTextField), Util.collectionValidator(jibStaniceTextField, AutobuskaStanica.getActiveJibList(), true, "Ne postoji"));
   		imeTextField.getValidators().addAll(Util.requiredFieldValidator(imeTextField), Util.nameValidator(imeTextField), Util.lengthValidator(imeTextField, 35));
   		prezimeTextField.getValidators().addAll(Util.requiredFieldValidator(prezimeTextField), Util.nameValidator(prezimeTextField), Util.lengthValidator(prezimeTextField, 35));
   		jmbgTextField.getValidators().addAll(Util.requiredFieldValidator(jmbgTextField), Util.jmbgValidator(jmbgTextField), Util.collectionValidator(jmbgTextField, Zaposleni.getJmbgList(), false, "Vec postoji"));
   		adresaTextField.getValidators().addAll(Util.requiredFieldValidator(adresaTextField), Util.lengthValidator(adresaTextField, 35));
   		//postanskiBrojTextField.getValidators().addAll(Util.requiredFieldValidator(postanskiBrojTextField), Util.collectionValidator(postanskiBrojTextField, Mjesto.getPostalCodeList(), true, "Nekorektan unos"));
   		postanskiBrojTextField.getValidators().addAll(Util.requiredFieldValidator(postanskiBrojTextField), Util.collectionValidator(postanskiBrojTextField, Mjesto.getCityPostalCodeList(), true, "Nekorektan unos"));
   		strucnaSpremaTextField.getValidators().add(Util.requiredFieldValidator(strucnaSpremaTextField));
   		brojTelefonaTextField.getValidators().addAll(Util.requiredFieldValidator(brojTelefonaTextField), Util.phoneValidator(brojTelefonaTextField), Util.lengthValidator(brojTelefonaTextField, 16));
   		emailTextField.getValidators().addAll(Util.requiredFieldValidator(emailTextField), Util.emailValidator(emailTextField), Util.lengthValidator(emailTextField, 35));
	}
	
	@FXML
    void potvrdi(ActionEvent event) {
		if(korisnickoImeTextField.validate()
				& lozinkaTextField.validate()
					& jibStaniceTextField.validate()
						& imeTextField.validate()
							& prezimeTextField.validate()
								& jmbgTextField.validate()
									& adresaTextField.validate()
										& postanskiBrojTextField.validate()
											& strucnaSpremaTextField.validate()
												& brojTelefonaTextField.validate()
													& emailTextField.validate()) {
			if(Nalog.dodavanjeNaloga(korisnickoImeTextField.getText(),
					Nalog.hash(lozinkaTextField.getText()),
					jibStaniceTextField.getText(),
					administrativniRadnikRadioButton.isSelected() ? "Administrativni radnik" : "Šalterski radnik",
					imeTextField.getText(),
					prezimeTextField.getText(),
					jmbgTextField.getText(),
					muskiRadioButton.isSelected() ? "Muški" : "Ženski",
					adresaTextField.getText(),
					//Integer.parseInt(postanskiBrojTextField.getText()),
					Integer.parseInt(postanskiBrojTextField.getText().split("-")[0].trim()),
					strucnaSpremaTextField.getText(),
					brojTelefonaTextField.getText(),
					emailTextField.getText())) {
				Zaposleni.getJmbgList().add(jmbgTextField.getText().trim());
				Nalog.getUsernameList().add(korisnickoImeTextField.getText().trim());
				
				/*
				Alert alert=new Alert(AlertType.INFORMATION);
	    		alert.setTitle("Obavještenje");
	    		alert.setHeaderText(null);
	    		alert.setContentText("Nalog uspjesno dodan.");
	    		
	    		alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/unibl/etf/application.css").toExternalForm());
				alert.getDialogPane().getStyleClass().add("alert");
				
	    		alert.showAndWait();
	    		*/
				
				Util.getNotifications("Obavještenje", "Korisnički nalog kreiran.", "Information").show();
				
				korisnickoImeTextField.clear();
				korisnickoImeTextField.resetValidation();
				lozinkaTextField.clear();
				lozinkaTextField.resetValidation();
				jibStaniceTextField.clear();
				jibStaniceTextField.resetValidation();
				administrativniRadnikRadioButton.setSelected(true);
				imeTextField.clear();
				imeTextField.resetValidation();
				prezimeTextField.clear();
				prezimeTextField.resetValidation();
				jmbgTextField.clear();
				jmbgTextField.resetValidation();
				adresaTextField.clear();
				adresaTextField.resetValidation();
				postanskiBrojTextField.clear();
				postanskiBrojTextField.resetValidation();
				strucnaSpremaTextField.clear();
				strucnaSpremaTextField.resetValidation();
				muskiRadioButton.setSelected(true);
				brojTelefonaTextField.clear();
				brojTelefonaTextField.resetValidation();
				emailTextField.clear();
				emailTextField.resetValidation();
			} else {
				//NASTALA GRESKA
				Util.showBugAlert();
			}
		}
    }
}
