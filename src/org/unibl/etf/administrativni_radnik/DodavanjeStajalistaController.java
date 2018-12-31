package org.unibl.etf.administrativni_radnik;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.unibl.etf.util.Mjesto;
import org.unibl.etf.util.Util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DodavanjeStajalistaController implements Initializable {
	
	@FXML
	private AnchorPane anchorPane;

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
		
		Util.setAutocompleteList(postanskiBrojTextField, Mjesto.getCityPostalCodeList());	
		
		nazivStajalistaTextField.getValidators().add(Util.requiredFieldValidator(nazivStajalistaTextField));
		postanskiBrojTextField.getValidators().addAll(Util.requiredFieldValidator(postanskiBrojTextField), Util.collectionValidator(postanskiBrojTextField, Mjesto.getCityPostalCodeList(), true, "Nekorektan unos"));
	}

    @FXML
    void close(MouseEvent event) {
    	((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void potvrdi(ActionEvent event) {
    	if(nazivStajalistaTextField.validate()
    			& postanskiBrojTextField.validate()) {
    		Platform.runLater(() -> {
    			Util.getNotifications("Obavještenje.", "Stajalište dodano.", "Information").show();
    		});
    		
    		((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    	}
    }

}
