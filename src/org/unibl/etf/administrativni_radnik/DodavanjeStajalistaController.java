package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.util.ResourceBundle;

import org.unibl.etf.util.Mjesto;
import org.unibl.etf.util.Stajaliste;
import org.unibl.etf.util.Util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DodavanjeStajalistaController implements Initializable {
	
	@FXML
	private AnchorPane anchorPane;
	
	@FXML
	private AnchorPane menuLine;

    @FXML
    private JFXTextField nazivStajalistaTextField;

    @FXML
    private JFXTextField postanskiBrojTextField;

    @FXML
    private JFXButton potvrdiButton;
    
    private double xOffset=0;
    private double yOffset=0;
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	//DragAndDrop
		menuLine.setOnMousePressed(event -> {
			if(event.getButton().equals(MouseButton.PRIMARY)) {
				Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
				xOffset = stage.getX() - event.getScreenX();
				yOffset = stage.getY() - event.getScreenY();
			}
		});
						
		menuLine.setOnMouseDragged(event -> {
			if(event.getButton().equals(MouseButton.PRIMARY)) {
			   	Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
			    if(!stage.isMaximized()) {
			    	stage.setX(event.getScreenX() + xOffset);
			    	stage.setY(event.getScreenY() + yOffset);
			    	stage.setOpacity(0.8);
			    }
			}
		});
						
		menuLine.setOnMouseReleased(event -> {
			if(event.getButton().equals(MouseButton.PRIMARY)) {
				Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
				stage.setOpacity(1.0);
			}
		});
		
		Util.setAutocompleteList(postanskiBrojTextField, Mjesto.getCityPostalCodeList());	
		
		nazivStajalistaTextField.getValidators().add(Util.requiredFieldValidator(nazivStajalistaTextField));
		postanskiBrojTextField.getValidators().addAll(Util.requiredFieldValidator(postanskiBrojTextField), Util.collectionValidator(postanskiBrojTextField, Mjesto.getCityPostalCodeList(), true, "Nekorektan unos"));
	}

    @FXML
	void close(MouseEvent event) {
		if(event.getButton().equals(MouseButton.PRIMARY)) {
            ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        }
	}

    @FXML
    void potvrdi(ActionEvent event) {
    	if(nazivStajalistaTextField.validate()
    			& postanskiBrojTextField.validate()) {
    		
    		int postanskiBrojInt = Integer.parseInt(postanskiBrojTextField.getText().split("-")[0].trim());
    		
    		Stajaliste novoStajaliste = new Stajaliste(0, postanskiBrojInt, null, nazivStajalistaTextField.getText());
    		novoStajaliste.setIdStajalista(Stajaliste.dodajStajaliste(novoStajaliste));
    		String nazivMjesta = ListaLinijaController.stajalistaList.stream().filter(s -> s.getPostanskiBroj()==novoStajaliste.getPostanskiBroj()).findFirst().get().getNaziv();
    		novoStajaliste.setNaziv(nazivMjesta);
    		ListaLinijaController.stajalistaList.add(novoStajaliste);
    		Platform.runLater(() -> {
    			Util.getNotifications("Obavještenje.", "Stajalište dodano.", "Information").show();
    		});
    		
    		((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    	}
    }

}
