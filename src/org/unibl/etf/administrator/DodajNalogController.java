package org.unibl.etf.administrator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.unibl.etf.autobuska_stanica.AutobuskaStanica;
import org.unibl.etf.prijava.Nalog;
import org.unibl.etf.util.Mjesto;
import org.unibl.etf.util.Util;
import org.unibl.etf.zaposleni.Zaposleni;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
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
	    lozinkaTextField.setTooltip(new Tooltip("Lozinka mora sadr�avati najmanje 6 karaktera"));
	    
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
			
			lozinkaTextField.setTooltip(new Tooltip("Lozinka mora sadr�avati najmanje 6 karaktera"));
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
		
		Util.setAutocompleteList(postanskiBrojTextField, Mjesto.getPostalCodeList());
		Util.setAutocompleteList(jibStaniceTextField, AutobuskaStanica.getJibList());
		

   		korisnickoImeTextField.getValidators().addAll(Util.requredFieldValidator(korisnickoImeTextField), Util.collectionValidator(korisnickoImeTextField, Nalog.getUsernameList(), false, "Zauzeto"));
   		lozinkaTextField.getValidators().addAll(Util.requredFieldValidator(lozinkaTextField), Util.passwordValidator(lozinkaTextField));
   		jibStaniceTextField.getValidators().addAll(Util.requredFieldValidator(jibStaniceTextField), Util.jibValidator(jibStaniceTextField), Util.collectionValidator(jibStaniceTextField, AutobuskaStanica.getJibList(), true, "Ne postoji"));
   		imeTextField.getValidators().add(Util.requredFieldValidator(imeTextField));
   		prezimeTextField.getValidators().add(Util.requredFieldValidator(prezimeTextField));
   		jmbgTextField.getValidators().addAll(Util.requredFieldValidator(jmbgTextField), Util.jmbgValidator(jmbgTextField), Util.collectionValidator(jmbgTextField, Zaposleni.getJmbgList(), false, "Vec postoji"));
   		adresaTextField.getValidators().add(Util.requredFieldValidator(adresaTextField));
   		postanskiBrojTextField.getValidators().addAll(Util.requredFieldValidator(postanskiBrojTextField), Util.collectionValidator(postanskiBrojTextField, Mjesto.getPostalCodeList(), true, "Nekorektan unos"));
   		strucnaSpremaTextField.getValidators().add(Util.requredFieldValidator(strucnaSpremaTextField));
   		brojTelefonaTextField.getValidators().addAll(Util.requredFieldValidator(brojTelefonaTextField), Util.phoneValidator(brojTelefonaTextField));
   		emailTextField.getValidators().addAll(Util.requredFieldValidator(emailTextField), Util.emailValidator(emailTextField));
	}
	
	@FXML
    void potvrdi(ActionEvent event) {
		WritableImage image = gridPane.snapshot(new SnapshotParameters(), null);
		File file = new File("C:\\JavaProjects\\GUI\\src\\menu\\images\\screenshoot.png");
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		}catch (IOException e) {
		    System.out.println("EXCEPTION");
		}
		
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
					administrativniRadnikRadioButton.isSelected() ? "Administrativni radnik" : "�alterski radnik",
					imeTextField.getText(),
					prezimeTextField.getText(),
					jmbgTextField.getText(),
					muskiRadioButton.isSelected() ? "Mu�ki" : "�enski",
					adresaTextField.getText(),
					Integer.parseInt(postanskiBrojTextField.getText()),
					strucnaSpremaTextField.getText(),
					brojTelefonaTextField.getText(),
					emailTextField.getText())) {
				lozinkaTextField.clear();
				Zaposleni.getJmbgList().add(jmbgTextField.getText().trim());
				Nalog.getUsernameList().add(korisnickoImeTextField.getText().trim());
				Alert alert=new Alert(AlertType.INFORMATION);
	    		alert.setTitle("Informacija");
	    		alert.setHeaderText(null);
	    		alert.setContentText("Nalog uspjesno dodan.");
	    		alert.showAndWait();
			}
		}
    }
}
