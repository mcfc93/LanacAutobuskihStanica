package org.unibl.etf.administrator;

import java.net.URL;
import java.util.ResourceBundle;

import org.unibl.etf.util.Mjesto;
import org.unibl.etf.util.Praznik;
import org.unibl.etf.util.Util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.base.ValidatorBase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

public class UpravljanjePerzistentnimPodacimaController implements Initializable {
	
	@FXML
    private GridPane gridPane;

    @FXML
    private ToggleGroup mjestoToggleGroup;
    
    @FXML
    private JFXRadioButton dodajMjestoRadioButton;

    @FXML
    private JFXRadioButton izmjeniMjestoRadioButton;

    @FXML
    private JFXTextField odaberiMjestoTextField;

    @FXML
    private JFXTextField postanskiBrojTextField;

    @FXML
    private JFXTextField nazivMjestaTextField;

    @FXML
    private JFXButton potvrdiMjestoButton;

    @FXML
    private ToggleGroup praznikToggleGroup;
    
    @FXML
    private JFXRadioButton dodajPraznikRadioButton;

    @FXML
    private JFXRadioButton izmjeniPraznikRadioButton;

    @FXML
    private JFXRadioButton obrisiPraznikRadioButton;
    
    @FXML
    private JFXComboBox<Praznik> odaberiPraznikComboBox;

    //@FXML
    //private JFXTextField odaberiPraznikTextField;

    @FXML
    private JFXTextField datumTextField;

    @FXML
    private JFXTextField opisTextField;

    @FXML
    private JFXButton potvrdiPraznikButton;
    
    //private List<String> praznici=new ArrayList<>();
    private ObservableList<Praznik> praznici=FXCollections.observableArrayList();
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	odaberiMjestoTextField.setDisable(true);
    	
    	Util.setAutocompleteList(odaberiMjestoTextField, Mjesto.getCityPostalCodeList());
    	odaberiMjestoTextField.getValidators().addAll(Util.requiredFieldValidator(odaberiMjestoTextField), Util.collectionValidator(odaberiMjestoTextField, Mjesto.getCityPostalCodeList(), true, "Nekorektan unos"));
     	ValidatorBase postalCodeValidator=Util.collectionValidator(postanskiBrojTextField, Mjesto.getPostalCodeList(), false, "Već postoji");
    	//ValidatorBase cityValdator=Util.collectionValidator(nazivMjestaTextField, Mjesto.getCityList(), false, "Već postoji");
    	postanskiBrojTextField.getValidators().addAll(Util.requiredFieldValidator(postanskiBrojTextField), Util.integerValidator(postanskiBrojTextField), postalCodeValidator, Util.lengthValidator(postanskiBrojTextField, 7));
   		nazivMjestaTextField.getValidators().addAll(Util.requiredFieldValidator(nazivMjestaTextField), Util.lengthValidator(nazivMjestaTextField, 35)/*, cityValdator*/);
   		
   		mjestoToggleGroup.selectedToggleProperty().addListener(listener -> {
    		if(dodajMjestoRadioButton.isSelected()) {
    			odaberiMjestoTextField.setDisable(true);
    			odaberiMjestoTextField.clear();
    			postanskiBrojTextField.clear();
    			nazivMjestaTextField.clear();
    			postanskiBrojTextField.getValidators().add(postalCodeValidator);
    	   		//nazivMjestaTextField.getValidators().add(cityValdator);
    	   		odaberiMjestoTextField.resetValidation();
    	   		postanskiBrojTextField.resetValidation();
    	   		nazivMjestaTextField.resetValidation();
    		} else {
    			odaberiMjestoTextField.setDisable(false);
    			odaberiMjestoTextField.clear();
    			postanskiBrojTextField.clear();
    			nazivMjestaTextField.clear();
    			postanskiBrojTextField.getValidators().removeAll(postalCodeValidator);
    			//nazivMjestaTextField.getValidators().removeAll(cityValdator);
    			odaberiMjestoTextField.resetValidation();
    			postanskiBrojTextField.resetValidation();
    	   		nazivMjestaTextField.resetValidation();
    		}
    	});
		
   		//odaberiPraznikTextField.setDisable(true);
   		odaberiPraznikComboBox.setDisable(true);
   		/*
    	for(Praznik p: Praznik.getHolidayList()) {
    		praznici.add(String.format("%02d/%02d - %s", p.getDan(), p.getMjesec(), p.getOpis()));
    	}
    	*/
    	//Util.setAutocompleteList(odaberiPraznikTextField, praznici);
   		praznici.addAll(Praznik.getHolidayList());
    	odaberiPraznikComboBox.setItems(praznici);
    	//odaberiPraznikTextField.getValidators().addAll(Util.requredFieldValidator(odaberiPraznikTextField), Util.collectionValidator(odaberiPraznikTextField, praznici, true, "Nekorektan unos"));
    	odaberiPraznikComboBox.getValidators().add(Util.requiredFieldValidator(odaberiPraznikComboBox));
    	datumTextField.getValidators().addAll(Util.requiredFieldValidator(datumTextField), Util.dateValidator(datumTextField));
    	opisTextField.getValidators().addAll(Util.requiredFieldValidator(opisTextField), Util.lengthValidator(opisTextField, 35));
    	
    	praznikToggleGroup.selectedToggleProperty().addListener(listener -> {
    		if(dodajPraznikRadioButton.isSelected()) {
    			//odaberiPraznikTextField.setDisable(true);
    			//odaberiPraznikTextField.clear();
    			odaberiPraznikComboBox.setDisable(true);
odaberiPraznikComboBox.getSelectionModel().clearSelection();
    			datumTextField.clear();
    			opisTextField.clear();
    			//odaberiPraznikTextField.resetValidation();
    			odaberiPraznikComboBox.resetValidation();
    		} else {
    			//odaberiPraznikTextField.setDisable(false);
    			//odaberiPraznikTextField.clear();
    			odaberiPraznikComboBox.setDisable(false);
odaberiPraznikComboBox.getSelectionModel().clearSelection();
    			datumTextField.clear();
    			opisTextField.clear();
    			//odaberiPraznikTextField.resetValidation();
    			odaberiPraznikComboBox.resetValidation();
    		}
    		if(obrisiPraznikRadioButton.isSelected()) {
    			datumTextField.setDisable(true);
    			opisTextField.setDisable(true);
    			
    			datumTextField.resetValidation();
    			opisTextField.resetValidation();
    		} else {
    			datumTextField.setDisable(false);
    			opisTextField.setDisable(false);
    			
    			datumTextField.resetValidation();
    			opisTextField.resetValidation();
    		}
    	});
    }

    @FXML
    void potvrdiMjesto(ActionEvent event) {
    	if(dodajMjestoRadioButton.isSelected()
    			&& (postanskiBrojTextField.validate()
    					& nazivMjestaTextField.validate())) {
    		if(Mjesto.dodavanjeMjesta(Integer.parseInt(postanskiBrojTextField.getText()), nazivMjestaTextField.getText().trim())) {
				/*
				Mjesto.getPlaceList().add(new Mjesto(Integer.parseInt(postanskiBrojTextField.getText()), nazivMjestaTextField.getText()));
				Mjesto.getCityPostalCodeList().add(Integer.parseInt(postanskiBrojTextField.getText()) + " - " + nazivMjestaTextField.getText());
				Mjesto.getPostalCodeList().add(postanskiBrojTextField.getText());
				Mjesto.getCityList().add(nazivMjestaTextField.getText().trim());
				*/
    			Mjesto.loadPlaces();
System.out.println(Mjesto.getPlaceList());
System.out.println(Mjesto.getCityPostalCodeList());
System.out.println(Mjesto.getPostalCodeList());
System.out.println(Mjesto.getCityList());
				
				odaberiMjestoTextField.clear();
				odaberiMjestoTextField.resetValidation();
				postanskiBrojTextField.clear();
				postanskiBrojTextField.resetValidation();
				nazivMjestaTextField.clear();
				nazivMjestaTextField.resetValidation();

				Util.setAutocompleteList(odaberiMjestoTextField, Mjesto.getCityPostalCodeList());
				
				/*
				Alert alert=new Alert(AlertType.INFORMATION);
	    		alert.setTitle("Obavještenje");
	    		alert.setHeaderText(null);
	    		alert.setContentText("Mjesto uspješno dodano.");
	    		
	    		alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/unibl/etf/application.css").toExternalForm());
				alert.getDialogPane().getStyleClass().add("alert");
	    		
	    		alert.showAndWait();
	    		*/
				
				Util.getNotifications("Obavještenje", "Mjesto dodano.", "Information").show();
			} else {
				//NASTALA GRESKA
				Util.showBugAlert();
			}
    	} else if(izmjeniMjestoRadioButton.isSelected()
							&& (odaberiMjestoTextField.validate()
									& postanskiBrojTextField.validate()
										& nazivMjestaTextField.validate())) {
    		if(Mjesto.izmjenaMjesta(Integer.parseInt(postanskiBrojTextField.getText()), nazivMjestaTextField.getText().trim(), Integer.parseInt(odaberiMjestoTextField.getText().split("-")[0].trim()))) {
    			Mjesto.loadPlaces();
System.out.println(Mjesto.getPlaceList());
System.out.println(Mjesto.getCityPostalCodeList());
System.out.println(Mjesto.getPostalCodeList());
System.out.println(Mjesto.getCityList());

				odaberiMjestoTextField.clear();
				odaberiMjestoTextField.resetValidation();
				postanskiBrojTextField.clear();
				postanskiBrojTextField.resetValidation();
				nazivMjestaTextField.clear();
				nazivMjestaTextField.resetValidation();

				Util.setAutocompleteList(odaberiMjestoTextField, Mjesto.getCityPostalCodeList());
				
				/*
				Alert alert=new Alert(AlertType.INFORMATION);
	    		alert.setTitle("Obavještenje");
	    		alert.setHeaderText(null);
	    		alert.setContentText("Mjesto uspješno izmjenjeno.");
	    		
	    		alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/unibl/etf/application.css").toExternalForm());
				alert.getDialogPane().getStyleClass().add("alert");
				
	    		alert.showAndWait();
	    		*/
				
				Util.getNotifications("Obavještenje", "Mjesto izmjenjeno.", "Information").show();
    		} else {
    			//NASTALA GRESKA
				Util.showBugAlert();
    		}
    	}
    }

    @FXML
    void potvrdiPraznik(ActionEvent event) {
    	if(dodajPraznikRadioButton.isSelected()
    			&& (datumTextField.validate()
    					& opisTextField.validate())) {
    		if(Praznik.dodavanjePraznika(Integer.parseInt(datumTextField.getText().split("/")[0].trim()), Integer.parseInt(datumTextField.getText().split("/")[1].trim()), opisTextField.getText())) {
    			
    			//Praznik.getHolidayList().add(new Praznik(Integer.parseInt(datumTextField.getText().split("/")[0].trim()), Integer.parseInt(datumTextField.getText().split("/")[1].trim()), opisTextField.getText()));
    			Praznik.loadHolidays();
System.out.println(Praznik.getHolidayList());
    			//praznici.add(String.format("%02d/%02d - %s", Integer.parseInt(datumTextField.getText().split("/")[0].trim()), Integer.parseInt(datumTextField.getText().split("/")[1].trim()), opisTextField.getText()));
    			//Util.setAutocompleteList(odaberiPraznikTextField, praznici);
				praznici.clear();
				praznici.addAll(Praznik.getHolidayList());
				odaberiPraznikComboBox.setItems(praznici);
				
				odaberiPraznikComboBox.getSelectionModel().clearSelection();
				odaberiPraznikComboBox.resetValidation();
				datumTextField.clear();
				datumTextField.resetValidation();
				opisTextField.clear();
				opisTextField.resetValidation();

				/*
    			Alert alert=new Alert(AlertType.INFORMATION);
	    		alert.setTitle("Obavještenje");
	    		alert.setHeaderText(null);
	    		alert.setContentText("Praznik uspješno dodan.");
	    		
	    		alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/unibl/etf/application.css").toExternalForm());
				alert.getDialogPane().getStyleClass().add("alert");
				
	    		alert.showAndWait();
	    		*/
				
				Util.getNotifications("Obavještenje", "Praznik dodan.", "Information").show();
    		} else {
    			//NASTALA GRESKA
				Util.showBugAlert();
    		}
    	} else if(izmjeniPraznikRadioButton.isSelected()
    			&& (/*odaberiPraznikTextField.validate()*/
    				odaberiPraznikComboBox.validate()
    					& datumTextField.validate()
    						& opisTextField.validate())) {
    		if(Praznik.izmjenaPraznika(odaberiPraznikComboBox.getSelectionModel().getSelectedItem().getId(), Integer.parseInt(datumTextField.getText().split("/")[0].trim()), Integer.parseInt(datumTextField.getText().split("/")[1].trim()), opisTextField.getText())) {
    			
    			Praznik.loadHolidays();
System.out.println(Praznik.getHolidayList());
    			//praznici.add(String.format("%02d/%02d - %s", Integer.parseInt(datumTextField.getText().split("/")[0].trim()), Integer.parseInt(datumTextField.getText().split("/")[1].trim()), opisTextField.getText()));
    			//Util.setAutocompleteList(odaberiPraznikTextField, praznici);
				praznici.clear();
				praznici.addAll(Praznik.getHolidayList());
				odaberiPraznikComboBox.setItems(praznici);
				
				odaberiPraznikComboBox.getSelectionModel().clearSelection();
				odaberiPraznikComboBox.resetValidation();
				datumTextField.clear();
				datumTextField.resetValidation();
				opisTextField.clear();
				opisTextField.resetValidation();

				/*
    			Alert alert=new Alert(AlertType.INFORMATION);
	    		alert.setTitle("Obavještenje");
	    		alert.setHeaderText(null);
	    		alert.setContentText("Praznik uspjesno izmjenjen.");
	    		
	    		alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/unibl/etf/application.css").toExternalForm());
				alert.getDialogPane().getStyleClass().add("alert");
				
	    		alert.showAndWait();
	    		*/
				
				Util.getNotifications("Obavještenje", "Praznik izmjenjen.", "Information").show();
    		} else {
    			//NASTALA GRESKA
				Util.showBugAlert();
    		}
    	} else if(obrisiPraznikRadioButton.isSelected()
    			&& /*odaberiPraznikTextField.validate()*/
    				odaberiPraznikComboBox.validate()) {
    		if(Praznik.brisanjePraznika(odaberiPraznikComboBox.getSelectionModel().getSelectedItem().getId())) {
    			
    			Praznik.loadHolidays();
System.out.println(Praznik.getHolidayList());
    			//praznici.add(String.format("%02d/%02d - %s", Integer.parseInt(datumTextField.getText().split("/")[0].trim()), Integer.parseInt(datumTextField.getText().split("/")[1].trim()), opisTextField.getText()));
    			//Util.setAutocompleteList(odaberiPraznikTextField, praznici);
				praznici.clear();
				praznici.addAll(Praznik.getHolidayList());
				odaberiPraznikComboBox.setItems(praznici);
				
				odaberiPraznikComboBox.getSelectionModel().clearSelection();
				odaberiPraznikComboBox.resetValidation();
				datumTextField.clear();
				datumTextField.resetValidation();
				opisTextField.clear();
				opisTextField.resetValidation();

				/*
    			Alert alert=new Alert(AlertType.INFORMATION);
	    		alert.setTitle("Obavještenje");
	    		alert.setHeaderText(null);
	    		alert.setContentText("Praznik uspješno obrisan.");
	    		
	    		alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/unibl/etf/application.css").toExternalForm());
				alert.getDialogPane().getStyleClass().add("alert");
	    		
	    		alert.showAndWait();
	    		*/
				
				Util.getNotifications("Obavještenje", "Praznik obrisan.", "Information").show();
    		} else {
    			//NASTALA GRESKA
				Util.showBugAlert();
    		}
    	}
    }
}
