package org.unibl.etf.administrator;

import java.net.URL;
import java.util.ResourceBundle;

import org.unibl.etf.autobuska_stanica.AutobuskaStanica;
import org.unibl.etf.util.Util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class IzmjeniStanicuController implements Initializable {
	
	@FXML
    private AnchorPane anchorPane;

    @FXML
    private AnchorPane menuLine;

    @FXML
    private Label close;

    @FXML
    private GridPane gridPane;

    @FXML
    private JFXTextField jibTextField;

    @FXML
    private JFXTextField nazivTextField;

    @FXML
    private JFXTextField brojPeronaTextField;

    @FXML
    private JFXTextField brojTelefonaTextField;

    @FXML
    private JFXTextField adresaTextField;

    @FXML
    private JFXTextField postanskiBrojTextField;

    @FXML
    private JFXTextField webStranicaTextField;

    @FXML
    private JFXTextField emailTextField;

    @FXML
    private JFXButton potvrdiButton;
    
    public static AutobuskaStanica autobuskaStanica=null;

	private double xOffset=0;
    private double yOffset=0;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//DragAndDrop
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
		
		
		
		jibTextField.getValidators().addAll(Util.requredFieldValidator(jibTextField));
		nazivTextField.getValidators().add(Util.requredFieldValidator(nazivTextField));
		brojPeronaTextField.getValidators().add(Util.requredFieldValidator(brojPeronaTextField));
		brojTelefonaTextField.getValidators().add(Util.requredFieldValidator(brojTelefonaTextField));
		adresaTextField.getValidators().add(Util.requredFieldValidator(adresaTextField));
		postanskiBrojTextField.getValidators().add(Util.requredFieldValidator(postanskiBrojTextField));
		webStranicaTextField.getValidators().add(Util.requredFieldValidator(webStranicaTextField));
		emailTextField.getValidators().add(Util.requredFieldValidator(emailTextField));
		
		
		jibTextField.setDisable(true);
		postanskiBrojTextField.setDisable(true);
		
		jibTextField.setText(autobuskaStanica.getJib());
		nazivTextField.setText(autobuskaStanica.getNaziv());
		brojPeronaTextField.setText("" + autobuskaStanica.getBrojPerona());
		brojTelefonaTextField.setText(autobuskaStanica.getBrojTelefona());
		adresaTextField.setText(autobuskaStanica.getUlicaIBroj());
		postanskiBrojTextField.setText("" + autobuskaStanica.getPostanskiBroj());
		webStranicaTextField.setText(autobuskaStanica.getWebStranica());
		emailTextField.setText(autobuskaStanica.getEmail());
	}
	
    @FXML
    void close(MouseEvent event) {
    	Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
		stage.close();
    }

    @FXML
    void potvrdi(ActionEvent event) {
    	if(jibTextField.validate()
				& nazivTextField.validate()
					& brojPeronaTextField.validate()
						& brojTelefonaTextField.validate()
							& adresaTextField.validate()
								& postanskiBrojTextField.validate()
									& webStranicaTextField.validate()
										& emailTextField.validate()) {
	    	if(AutobuskaStanica.izmjenaAutobuskeStanice(jibTextField.getText().trim(), nazivTextField.getText().trim(), adresaTextField.getText().trim(), Integer.parseInt(brojPeronaTextField.getText().trim()), brojTelefonaTextField.getText().trim(), webStranicaTextField.getText().trim(), emailTextField.getText().trim())) {
	    		//autobuskaStanica.setJib();
		    	autobuskaStanica.setNaziv(nazivTextField.getText().trim());
		    	autobuskaStanica.setBrojPerona(Integer.parseInt(brojPeronaTextField.getText().trim()));
		    	autobuskaStanica.setBrojTelefona(brojTelefonaTextField.getText().trim());
		    	autobuskaStanica.setUlicaIBroj(adresaTextField.getText().trim());
		    	autobuskaStanica.setPostanskiBroj(Integer.parseInt(postanskiBrojTextField.getText().trim()));
		    	autobuskaStanica.setWebStranica(webStranicaTextField.getText().trim());
		    	autobuskaStanica.setEmail(emailTextField.getText().trim());
	    	}

    	Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
		stage.close();
    	}
    }
}
