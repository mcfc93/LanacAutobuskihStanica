package org.unibl.etf.administrator;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.unibl.etf.autobuska_stanica.AutobuskaStanica;
import org.unibl.etf.prijava.Nalog;
import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.util.Util;
import org.unibl.etf.zaposleni.Zaposleni;

import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
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

public class AdministratorController implements Initializable {
	@FXML
	private AnchorPane anchorPane;
	
	@FXML
	private AnchorPane dataAnchorPane;
	
	@FXML
	private Label informacijeLabel;
	
	@FXML
    private ToggleGroup toggleGroup;
	
	@FXML
	private ToggleButton odjavaButton;
	
	@FXML
	private ToggleButton dodavanjeKorisnickogNalogaButton;
	
	@FXML
	private ToggleButton brisanjeKorisnickogNalogaButton;
	
	@FXML
	private ToggleButton listaAutobuskihStanicaButton;
	
	@FXML
	private ToggleButton dodavanjeAutobuskeStaniceButton;
	
	@FXML
	private ToggleButton privremenoBlokiranjeAutobuskeStaniceButton;
	
	@FXML
	private ToggleButton upravljanjeKorisnickimNalogomButton;
	
	@FXML
    private ToggleButton upravljanjePerzistentnimPodacimaButton;
	
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
		
		//toggleGroup
		toggleGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
		    if (newValue == null) {
		        oldValue.setSelected(true);
		    }
		});
		
		//toggleGroup.getSelectedToggle().
		listaAutobuskihStanica(null);
		
		//Ucitavanje podataka iz Baze za Autocomplete
		Task<Void> task = new Task<Void>() {
			@Override
            protected Void call() {
            	System.out.println(Thread.currentThread());
            	AutobuskaStanica.loadJibs();
            	Nalog.loadUsernames();
            	Zaposleni.loadJmbgs();
                return null;
            }
            @Override
            protected void succeeded() {
                super.succeeded();
System.out.println(AutobuskaStanica.getJibList());
System.out.println(Nalog.getUsernameList());
System.out.println(Zaposleni.getJmbgList());
            }
        };
        new Thread(task).start();
	}
	
	@FXML
	public void odjava(ActionEvent event) {
		//resetButtons();
		//setCss((Button)event.getSource());
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
	void doubleClickMaximize(MouseEvent event) {
		//System.out.println("Broj klikova: " + event.getClickCount());
		if(event.getClickCount() > 1) {
			maximize(event);
		}
	}
	/*
	private void resetButtons() {
		//System.out.println("ODJAVA:" + odjavaButton.getStyleClass());
		//System.out.println("DODAJ:" + dodajNalogButton.getStyleClass());
		//System.out.println("LISTA:" + listaNalogaButton.getStyleClass());
		
		odjavaButton.getStyleClass().clear();
		listaStanicaButton.getStyleClass().clear();
		dodajStanicuButton.getStyleClass().clear();
		listaNalogaButton.getStyleClass().clear();
		dodajNalogButton.getStyleClass().clear();

		odjavaButton.getStyleClass().addAll("buttonMenu", "buttonRightBorder");
		listaStanicaButton.getStyleClass().addAll("buttonMenu", "buttonRightBorder");
		dodajStanicuButton.getStyleClass().addAll("buttonMenu", "buttonRightBorder");
		listaNalogaButton.getStyleClass().addAll("buttonMenu", "buttonRightBorder");
		dodajNalogButton.getStyleClass().addAll("buttonMenu", "buttonRightBorder");
	}
	
	private void setCss(Button button) {
		button.getStyleClass().removeAll("buttonMenu");
		button.getStyleClass().add("buttonPressed");
	}
	*/
	@FXML
	void listaAutobuskihStanica(ActionEvent event) {
		//resetButtons();
		//setCss((Button)event.getSource());
		//listaStanicaButton.getStyleClass().removeAll("buttonMenu");
		//listaStanicaButton.getStyleClass().add("buttonPressed");
		
		AutobuskaStanica.listaStanica();
		
		
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/org/unibl/etf/administrator/ListaStanica.fxml"));
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
	void dodavanjeAutobuskeStanice(ActionEvent event) {
		//resetButtons();
		//setCss((Button)event.getSource());
		//dodajStanicuButton.getStyleClass().removeAll("buttonMenu");
		//dodajStanicuButton.getStyleClass().add("buttonPressed");
		
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/org/unibl/etf/administrator/DodajStanicu.fxml"));
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
	void dodavanjeKorisnickogNaloga(ActionEvent event) {
		//resetButtons();
		//setCss((Button)event.getSource());
		//dodajNalogButton.getStyleClass().removeAll("buttonMenu");
		//dodajNalogButton.getStyleClass().add("buttonPressed");
		
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/org/unibl/etf/administrator/DodajNalog.fxml"));
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
	void brisanjeKorisnickogNaloga(ActionEvent event) {
		//resetButtons();
		//setCss((Button)event.getSource());
		//listaNalogaButton.getStyleClass().removeAll("buttonMenu");
		//listaNalogaButton.getStyleClass().add("buttonPressed");
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/org/unibl/etf/administrator/ListaNaloga.fxml"));
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
	void upravljanjeKorisnickimNalogom(ActionEvent event) {
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
    void upravljanjePerzistentnimPodacima(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/org/unibl/etf/administrator/UpravljanjePerzistentnimPodacima.fxml"));
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
