package org.unibl.etf.administrativni_radnik;

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
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AdministrativniRadnikController implements Initializable {
	@FXML
	private AnchorPane anchorPane;
	
	@FXML
	private AnchorPane dataAnchorPane;
	
	@FXML
	private Label informacijeLabel;
	
	@FXML
	private Button odjavaButton;
	
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
		resetButtons();
		setCss((Button)event.getSource());
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
		odjavaButton.getStyleClass().clear();
		//xButton.getStyleClass().clear();
		//yButton.getStyleClass().clear();
		//z.getStyleClass().clear();

		odjavaButton.getStyleClass().addAll("buttonMenu", "buttonRightBorder");
		//xButton.getStyleClass().addAll("buttonMenu", "buttonRightBorder");
		//yButton.getStyleClass().addAll("buttonMenu", "buttonRightBorder");
		//zButton.getStyleClass().addAll("buttonMenu", "buttonRightBorder");
	}
	
	private void setCss(Button button) {
		button.getStyleClass().removeAll("buttonMenu");
		button.getStyleClass().add("buttonPressed");
	}
}
