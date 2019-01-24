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
import com.jfoenix.validation.base.ValidatorBase;

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
	
	private ValidatorBase polazisteValidator;
	private ValidatorBase odredisteValidator;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		timePicker.setIs24HourView(true);
		timePicker.converterProperty().set(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
		daljeButton.setDefaultButton(true);
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
		polazisteValidator=Util.collectionValidator(polazisteTextField, ListaLinijaController.stajalistaList.stream().map(Stajaliste::toString).collect(Collectors.toList()), true, "Unesite polazište");
		odredisteValidator=Util.collectionValidator(odredisteTextField, ListaLinijaController.stajalistaList.stream().map(Stajaliste::toString).collect(Collectors.toList()), true, "Unesite odredište");
		
		polazisteTextField.getValidators().addAll(Util.requiredFieldValidator(polazisteTextField), polazisteValidator);
		odredisteTextField.getValidators().addAll(Util.requiredFieldValidator(odredisteTextField), odredisteValidator, poklapanjeStajalistaValidator(odredisteTextField));
		
		gotovoButton.setDisable(true);
		
		
	}
	
	public static ValidatorBase poklapanjeStajalistaValidator(JFXTextField textField) {
		ValidatorBase psValidator = new ValidatorBase("Stajalište već dodano") {
			@Override
			protected void eval() {
				System.out.println("===========");
				relacijeList.forEach(r -> System.out.println(r.getPolaziste().toString() + "->" +r.getOdrediste().toString()));
				if(!relacijeList.isEmpty()
	        			&& relacijeList.stream().anyMatch(r -> r.getPolaziste().toString().equals(textField.getText()) || r.getOdrediste().toString().equals(textField.getText()))) {
	        		hasErrors.set(true);
		        } else {
		        	 hasErrors.set(false);
		        }
			}
		};
		psValidator.setIcon(new ImageView());
		return psValidator;
	}
	
	@FXML
	void close(MouseEvent event) {
		if(event.getButton().equals(MouseButton.PRIMARY)) {
			DodavanjeLinijaController.dodaj=false;
			relacijeList.clear();
            ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        }
	}
	
    @FXML
    public boolean dalje(ActionEvent e) {
    	if(timePicker.validate()
    			& polazisteTextField.validate() 
    				& odredisteTextField.validate()) {
    	
	    	Stajaliste polaziste = ListaLinijaController.stajalistaList.stream().filter(s -> s.toString().equals(polazisteTextField.getText())).findFirst().get();
	    	Stajaliste odrediste = ListaLinijaController.stajalistaList.stream().filter(s -> s.toString().equals(odredisteTextField.getText())).findFirst().get();
	    	if(polaziste.equals(odrediste)) {
	    		Util.getNotifications("Greška", "Stajališta ne mogu biti ista!", "Error").show();
	    		//DodavanjeLinijaController.dodaj=false;
	    		return false;
	    	}
	    	relacijeList.add(new Relacija(polaziste,odrediste,timePicker.getValue()));
	    	polazisteTextField.setText(odrediste.toString());
	    	polazisteTextField.setDisable(true);
	    	Util.setAutocompleteList(polazisteTextField, new ArrayList<>());
	    	odredisteTextField.clear();
	    	polazisteTextField.resetValidation();
	    	odredisteTextField.resetValidation();
	    	Platform.runLater(() -> {
	    		odredisteTextField.requestFocus();
	    	});
	    	DodavanjeLinijaController.dodaj=true;
	    	gotovoButton.setDisable(false);
	    	return true;
    	}
    	//DodavanjeLinijaController.dodaj=false;
    	return false;
    }
    
    @FXML
    public void gotovUnos(ActionEvent event) {
    	dalje(event);
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
            scene.getStylesheets().add(getClass().getResource("/org/unibl/etf/application.css").toExternalForm());
			Stage stage=new Stage();
			stage.setScene(scene);
			stage.setResizable(false);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
			Util.setAutocompleteList(polazisteTextField, ListaLinijaController.stajalistaList.stream().map(Stajaliste::toString).collect(Collectors.toList()));
			Util.setAutocompleteList(odredisteTextField, ListaLinijaController.stajalistaList.stream().map(Stajaliste::toString).collect(Collectors.toList()));
			polazisteTextField.getValidators().remove(polazisteValidator);
			odredisteTextField.getValidators().remove(odredisteValidator);
			polazisteValidator=Util.collectionValidator(polazisteTextField, ListaLinijaController.stajalistaList.stream().map(Stajaliste::toString).collect(Collectors.toList()), true, "Unesite polazište");
			odredisteValidator=Util.collectionValidator(odredisteTextField, ListaLinijaController.stajalistaList.stream().map(Stajaliste::toString).collect(Collectors.toList()), true, "Unesite odredište");
			polazisteTextField.getValidators().add(polazisteValidator);
			odredisteTextField.getValidators().add(odredisteValidator);
			
		} catch (IOException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		
    }
}
