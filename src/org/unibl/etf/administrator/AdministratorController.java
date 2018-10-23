package org.unibl.etf.administrator;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.util.Util;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AdministratorController implements Initializable {
	@FXML
	private AnchorPane anchorPane;
	
	@FXML
	private AnchorPane dataAnchorPane;
	
	@FXML
	private Label informacijeLabel;
	
	@FXML
	private Button odjavaButton;
	
	@FXML
	private Button listaNalogaButton;
	
	@FXML
	private Button dodajNalogButton;
	
	private double xOffset=0;
    private double yOffset=0;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
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
		//System.out.println("Broj klikova: " + event.getClickCount());
		if(event.getClickCount() > 1) {
			maximize(event);
		}
	}
	
	private void resetButtons() {
		//System.out.println("ODJAVA:" + odjavaButton.getStyleClass());
		//System.out.println("DODAJ:" + dodajNalogButton.getStyleClass());
		//System.out.println("LISTA:" + listaNalogaButton.getStyleClass());
		
		odjavaButton.getStyleClass().clear();
		dodajNalogButton.getStyleClass().clear();
		listaNalogaButton.getStyleClass().clear();

		dodajNalogButton.getStyleClass().addAll("button", "buttonMenu", "buttonRightBorder");
		odjavaButton.getStyleClass().addAll("button", "buttonMenu", "buttonRightBorder");
		listaNalogaButton.getStyleClass().addAll("button", "buttonMenu", "buttonRightBorder");
	}
	
	@FXML
	void dodajNalog(ActionEvent event) {
		resetButtons();
		dodajNalogButton.getStyleClass().removeAll("buttonMenu");
		dodajNalogButton.getStyleClass().add("buttonPressed");
		
		
		try {
			Parent root = (AnchorPane)FXMLLoader.load(getClass().getResource("/org/unibl/etf/administrator/DodajNalog.fxml"));
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
	void listaNaloga(ActionEvent event) {
		resetButtons();
		listaNalogaButton.getStyleClass().removeAll("buttonMenu");
		listaNalogaButton.getStyleClass().add("buttonPressed");
	}
}
