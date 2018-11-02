package org.unibl.etf.salterski_radnik;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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

public class SalterskiRadnikController implements Initializable {
	
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
	private Button odjavaButton;
	@FXML
	private Button kreirajMjesecnuKartuButton;
	@FXML
	private Button prodajaKarataButton;
	@FXML
	private Button otkazivanjeRezervacijaButton;
	
	
	private double xOffset=0;
    private double yOffset=0;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		informacijeLabel.setText(PrijavaController.nalog.getZaposleni().getIme() + " " + PrijavaController.nalog.getZaposleni().getPrezime());
		
		Connection c = null;
		String query = "SELECT * FROM autobuska_stanica";
		Statement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			System.out.println("Broj stanice: " + PrijavaController.nalog.getIdStanice());
			r = s.executeQuery(query);
			while(r.next()) {
				System.out.println("fas");
				System.out.println(r.toString());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
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
		prodajaKarataButton.getStyleClass().clear();
		odjavaButton.getStyleClass().clear();
		kreirajMjesecnuKartuButton.getStyleClass().clear();
		otkazivanjeRezervacijaButton.getStyleClass().clear();
		odjavaButton.getStyleClass().addAll("button", "buttonMenu", "buttonRightBorder");
		prodajaKarataButton.getStyleClass().addAll("button", "buttonMenu", "buttonRightBorder");
		kreirajMjesecnuKartuButton.getStyleClass().addAll("button", "buttonMenu", "buttonRightBorder");
		otkazivanjeRezervacijaButton.getStyleClass().addAll("button", "buttonMenu", "buttonRightBorder");
	}
	
	@FXML
	void prodajaKarata(ActionEvent event) {
		resetButtons();
		prodajaKarataButton.getStyleClass().removeAll("buttonMenu");
		prodajaKarataButton.getStyleClass().add("buttonPressed");
		try {
			Parent root = (AnchorPane)FXMLLoader.load(getClass().getResource("/org/unibl/etf/salterski_radnik/ProdajaKarataView.fxml"));
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
	public void mjesecnaKarta(ActionEvent event) {
		resetButtons();
		kreirajMjesecnuKartuButton.getStyleClass().removeAll("buttonMenu");
		kreirajMjesecnuKartuButton.getStyleClass().add("buttonPressed");
		try {
			Parent root = (AnchorPane)FXMLLoader.load(getClass().getResource("/org/unibl/etf/salterski_radnik/KreiranjeMjesecneKarte.fxml"));
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
	public void otkazivanjeKarata(ActionEvent event) {
		resetButtons();
		otkazivanjeRezervacijaButton.getStyleClass().removeAll("buttonMenu");
		otkazivanjeRezervacijaButton.getStyleClass().add("buttonPressed");
		try {
			Parent root = (AnchorPane)FXMLLoader.load(getClass().getResource("/org/unibl/etf/salterski_radnik/OtkazivanjeRezervacijaView.fxml"));
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
