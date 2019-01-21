package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.util.Util;

import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AdministrativniRadnikController implements Initializable {
	
	@FXML
    private GridPane gridPane;
	
	@FXML
    private AnchorPane menuLine;
	
	@FXML
	private AnchorPane dataAnchorPane;
	
	@FXML
	private Label informacijeLabel;
	
	@FXML
    private ToggleGroup toggleGroup;
	@FXML
	private ToggleButton izmjenaLinijaButton;
	@FXML
	private ToggleButton odjavaButton;
	@FXML
	private ToggleButton izmjenaPrevoznikaButton;	
	@FXML
	private ToggleButton dodavanjeLinijaButton;
	@FXML
	private ToggleButton dodavanjePrevoznikaButton;
	@FXML
	private ToggleButton radSaNalogomButton;
	
	private double xOffset=0;
    private double yOffset=0;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		informacijeLabel.setText(PrijavaController.nalog.getZaposleni().getIme() + " " + PrijavaController.nalog.getZaposleni().getPrezime());

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
		
		//toggleGroup
		toggleGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
		    if (newValue == null) {
		        oldValue.setSelected(true);
		    }
		});
		

		radSaLinijama();
		/*
		//Ucitavanje podataka iz Baze za Autocomplete
		Task<Void> task = new Task<Void>() {
			@Override
            protected Void call() {
            	System.out.println(Thread.currentThread());
            	
                return null;
            }
            @Override
            protected void succeeded() {
                super.succeeded();
            }
        };
        */
	}
	@FXML
	public void radSaPrevoznicima() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/org/unibl/etf/administrativni_radnik/ListaPrevoznika.fxml"));
			AnchorPane.setTopAnchor(root,0.0);
			AnchorPane.setBottomAnchor(root,0.0);
			AnchorPane.setLeftAnchor(root,0.0);
			AnchorPane.setRightAnchor(root,0.0);
			dataAnchorPane.getChildren().removeAll();
			dataAnchorPane.getChildren().setAll(root);
		} catch(Exception e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
	
	@FXML
	public void radSaLinijama() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/org/unibl/etf/administrativni_radnik/ListaLinija.fxml"));
			AnchorPane.setTopAnchor(root,0.0);
			AnchorPane.setBottomAnchor(root,0.0);
			AnchorPane.setLeftAnchor(root,0.0);
			AnchorPane.setRightAnchor(root,0.0);
			dataAnchorPane.getChildren().removeAll();
			dataAnchorPane.getChildren().setAll(root);
		} catch(Exception e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
	
	@FXML
	void korisnickiNalog(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/org/unibl/etf/prijava/UpravljanjeKorisnickimNalogom.fxml"));
			AnchorPane.setTopAnchor(root,0.0);
			AnchorPane.setBottomAnchor(root,0.0);
			AnchorPane.setLeftAnchor(root,0.0);
			AnchorPane.setRightAnchor(root,0.0);
			dataAnchorPane.getChildren().removeAll();
			dataAnchorPane.getChildren().setAll(root);
		} catch(Exception e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
	
	@FXML
	public void dodavanjePrevoznika() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/org/unibl/etf/administrativni_radnik/DodavanjePrevoznika.fxml"));
			AnchorPane.setTopAnchor(root,0.0);
			AnchorPane.setBottomAnchor(root,0.0);
			AnchorPane.setLeftAnchor(root,0.0);
			AnchorPane.setRightAnchor(root,0.0);
			dataAnchorPane.getChildren().removeAll();
			dataAnchorPane.getChildren().setAll(root);
		} catch(Exception e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
	@FXML
	public void dodavanjeLinija() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/org/unibl/etf/administrativni_radnik/DodavanjeLinija.fxml"));
			AnchorPane.setTopAnchor(root,0.0);
			AnchorPane.setBottomAnchor(root,0.0);
			AnchorPane.setLeftAnchor(root,0.0);
			AnchorPane.setRightAnchor(root,0.0);
			dataAnchorPane.getChildren().removeAll();
			dataAnchorPane.getChildren().setAll(root);
		} catch(Exception e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}

	@FXML
	public void izmjenaPrevoznika() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/org/unibl/etf/administrativni_radnik/IzmjenaPrevoznika.fxml"));
			AnchorPane.setTopAnchor(root,0.0);
			AnchorPane.setBottomAnchor(root,0.0);
			AnchorPane.setLeftAnchor(root,0.0);
			AnchorPane.setRightAnchor(root,0.0);
			dataAnchorPane.getChildren().removeAll();
			dataAnchorPane.getChildren().setAll(root);
		} catch(Exception e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}

	
	@FXML
	public void odjava(ActionEvent event) {
		if(PrijavaController.nalog.odjava()) {
			PrijavaController.nalog=null;
			try {
				Parent root = (AnchorPane)FXMLLoader.load(getClass().getResource("/org/unibl/etf/prijava/Prijava.fxml"));
				Scene scene = new Scene(root);
				Stage stage=new Stage();
				stage.setScene(scene);
				//stage.setTitle("Prijava");
				stage.setResizable(false);
				stage.initStyle(StageStyle.UNDECORATED);
				stage.show();
			} catch(Exception e) {
				//e.printStackTrace();
				Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			}
		
			//((Stage)((Node)event.getSource()).getScene().getWindow()).show();
			((Stage)gridPane.getScene().getWindow()).close();
		} else {
			System.out.println("GRESKA! - Odjava nije uspjesnja.");
		}
	}
	
	@FXML
	void close(MouseEvent event) {
		if(event.getButton().equals(MouseButton.PRIMARY)) {
            ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        }
	}
	
	@FXML
	void minimize(MouseEvent event) {
		if(event.getButton().equals(MouseButton.PRIMARY)) {
            ((Stage)((Node)event.getSource()).getScene().getWindow()).setIconified(true);
        }
	}
	
	@FXML
	void maximize(MouseEvent event) {
		if(event.getButton().equals(MouseButton.PRIMARY)) {
            Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
            if(!stage.isMaximized()) {
                stage.setMaximized(true);
            } else {
                stage.setMaximized(false);
            }
        }
	}
	
	@FXML
	void doubleClick(MouseEvent event) {
		if(event.getButton().equals(MouseButton.PRIMARY)) {
            if(event.getClickCount() > 1) {
                maximize(event);
            }
        }
	}
}
