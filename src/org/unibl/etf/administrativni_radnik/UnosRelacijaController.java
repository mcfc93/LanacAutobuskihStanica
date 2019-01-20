package org.unibl.etf.administrativni_radnik;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.unibl.etf.karta.Relacija;
import org.unibl.etf.util.Stajaliste;
import org.unibl.etf.util.Util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.LocalTimeStringConverter;

public class UnosRelacijaController implements Initializable {
	
	public static List<Relacija> relacijeList = new ArrayList<>();
	
	@FXML
	private AnchorPane menuLine;
	
	@FXML
	private ImageView exitImageView;
	@FXML
	private JFXTextField polazisteTextField;
	@FXML
	private JFXTextField odredisteTextField;
	@FXML
	private JFXTimePicker timePicker;
	@FXML
    private JFXButton novoStajalisteButton;
	@FXML
    private JFXButton gotovoButton;
	@FXML
    private JFXButton daljeButton;
	@FXML
	private AnchorPane anchorPane;

    private double xOffset=0;
    private double yOffset=0;

	public static int brojRelacija;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		timePicker.setIs24HourView(true);
		timePicker.converterProperty().set(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
		
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
		
		timePicker.getValidators().add(Util.timeValidator(timePicker));
		Util.setAutocompleteList(polazisteTextField, ListaLinijaController.stajalistaList.stream().map(Stajaliste::toString).collect(Collectors.toList()));
		Util.setAutocompleteList(odredisteTextField, ListaLinijaController.stajalistaList.stream().map(Stajaliste::toString).collect(Collectors.toList()));
		polazisteTextField.getValidators().addAll(Util.requiredFieldValidator(polazisteTextField),Util.collectionValidator(polazisteTextField, ListaLinijaController.stajalistaList.stream().map(Stajaliste::toString).collect(Collectors.toList()), true, "Unesite polaziste"));
		odredisteTextField.getValidators().addAll(Util.requiredFieldValidator(odredisteTextField),Util.collectionValidator(odredisteTextField, ListaLinijaController.stajalistaList.stream().map(Stajaliste::toString).collect(Collectors.toList()), true, "Unesite odrediste"));
		
	}
	
	@FXML
	void close(MouseEvent event) {
		if(event.getButton().equals(MouseButton.PRIMARY)) {
            ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        }
	}
	
    @FXML
    public void dalje(ActionEvent e) {
    	if(timePicker.validate()
    			& polazisteTextField.validate() 
    				& odredisteTextField.validate()) {
    	
    	Stajaliste polaziste = ListaLinijaController.stajalistaList.stream().filter(s -> s.toString().equals(polazisteTextField.getText())).findFirst().get();
    	Stajaliste odrediste = ListaLinijaController.stajalistaList.stream().filter(s -> s.toString().equals(odredisteTextField.getText())).findFirst().get();
    	if(polaziste.equals(odrediste)) {
    		Util.getNotifications("Greška", "Stajališta ne mogu biti ista!", "Error");
    		return;
    	}
    	relacijeList.add(new Relacija(polaziste,odrediste,timePicker.getValue()));
    	polazisteTextField.setText(odrediste.toString());
    	odredisteTextField.clear();
    	polazisteTextField.resetValidation();
    	odredisteTextField.resetValidation();
    	Platform.runLater(() -> {
    		odredisteTextField.requestFocus();
    	});
    	}
    }
    
    @FXML
    public void gotovUnos(ActionEvent event) {
    	brojRelacija = relacijeList.size();
    	// dvostruka petlja kreiranja medjurelacija
    	for(int i=0;i<brojRelacija-1;++i) {
    		for(int j=i+1;j<brojRelacija;++j) {
    			Relacija novaMedjuRelacija = new Relacija(relacijeList.get(i).getPolaziste(), relacijeList.get(j).getOdrediste(), null);
    			//novaMedjuRelacija.setDuzinaPuta(LocalTime.of(0, 0));
				LocalTime newTime = LocalTime.of(0, 0);
    			// petlja sabiranja vremena
    			for(int k=i;k<=j;++k) {
    				//novaMedjuRelacija.getDuzinaPuta().
    				LocalTime plusHours = newTime.plusHours(relacijeList.get(k).getDuzinaPuta().getHour());
    				LocalTime plusMinutes = plusHours.plusMinutes(relacijeList.get(k).getDuzinaPuta().getMinute());
    				newTime = LocalTime.of(plusMinutes.getHour(), plusMinutes.getMinute());
    			}
    			novaMedjuRelacija.setDuzinaPuta(LocalTime.of(newTime.getHour(), newTime.getMinute()));
    			relacijeList.add(novaMedjuRelacija);
    		}
    	}
    	relacijeList.forEach(r -> System.out.println(r + ", " + r.getDuzinaPuta()));
    	((Stage)((Node)event.getSource()).getScene().getWindow()).close();

    }
    
    @FXML
    void novoStajaliste(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/org/unibl/etf/administrativni_radnik/DodavanjeStajalista.fxml"));
			Scene scene = new Scene(root);
			Stage stage=new Stage();
			stage.setScene(scene);
			stage.setResizable(false);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (IOException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		
    }
}
