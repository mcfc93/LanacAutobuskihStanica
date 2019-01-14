package org.unibl.etf.salterski_radnik;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.util.Util;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SalterskiRadnikController implements Initializable {
	
	public static int brojStanice;
	public static int brojMjesta;
	private double xOffset=0;
    private double yOffset=0;
    
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private AnchorPane dataAnchorPane;
	@FXML
	private AnchorPane menuAnchorPane;
	@FXML
	private AnchorPane infoAnchorPane;
	@FXML
	private Label informacijeLabel;
	@FXML
	private ToggleGroup toggleGroup;
	@FXML
	private ToggleButton informacijeButton;
	@FXML
	private ToggleButton otkazivanjeRezervacijeButton;
	@FXML
	private ToggleButton odjavaButton;
	@FXML
	private ToggleButton prodajaKarataButton;
	@FXML
	private ToggleButton korisnickiNalogButton;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		toggleGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
		    if (newValue == null) {
		        oldValue.setSelected(true);
		    }
		});
		
		ObservableList<Toggle> toggleButtons=toggleGroup.getToggles();
		for(Toggle t: toggleButtons) {
			t.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
				if(newValue) {
					((ToggleButton)t).setDisable(true);
				} else {
					((ToggleButton)t).setDisable(false);
				}
			});
		}
		
		
		informacijeLabel.setText(PrijavaController.nalog.getZaposleni().getIme() + " " + PrijavaController.nalog.getZaposleni().getPrezime());
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
		
		informacijeButton.setDisable(true);
		info();
	}
	
	@FXML
	void info( ) {
		//InformacijeController.startTask();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/org/unibl/etf/salterski_radnik/InformacijeView.fxml"));
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
	public void otkazivanjeRezervacije(ActionEvent event) {
		InformacijeController.endTask();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/org/unibl/etf/salterski_radnik/OtkazivanjeRezervacijeView.fxml"));
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
		InformacijeController.endTask();
		if(PrijavaController.nalog.odjava()) {
			PrijavaController.nalog=null;
			try {
				Parent root = (AnchorPane)FXMLLoader.load(getClass().getResource("/org/unibl/etf/prijava/Prijava.fxml"));
				Scene scene = new Scene(root);
				Stage stage=new Stage();
				stage.setScene(scene);
				stage.setResizable(false);
				stage.initStyle(StageStyle.UNDECORATED);
				stage.show();
			} catch(Exception e) {
				//e.printStackTrace();
				Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			}
		
			//((Stage)((Node)event.getSource()).getScene().getWindow()).show();
			((Stage)anchorPane.getScene().getWindow()).close();
		} else {
System.out.println("GRESKA! - Odjava nije uspjesnja.");
		}
	}
	@FXML
	void close(MouseEvent event) {
		//System.exit(0);
		Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
		stage.close();
	}
	
	@FXML
	void minimize(MouseEvent event) {
		Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
		stage.setIconified(true);
	}
	
	@FXML
	void maximize(MouseEvent event) {
		Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
		if(!stage.isMaximized()) {
			stage.setMaximized(true);
		} else {
			stage.setMaximized(false);
		}
	}
	
	@FXML
	void doubleClick(MouseEvent event) {
		if(event.getClickCount() > 1) {
			maximize(event);
		}
	}
	
	@FXML
	void korisnickiNalog(ActionEvent event) {
		InformacijeController.endTask();
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
	void prodajaKarata(ActionEvent event) {
		InformacijeController.endTask();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/org/unibl/etf/salterski_radnik/ProdajaKarataView.fxml"));
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
	
	


}
