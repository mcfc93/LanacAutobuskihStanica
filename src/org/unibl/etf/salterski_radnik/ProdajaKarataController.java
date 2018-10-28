package org.unibl.etf.salterski_radnik;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.unibl.etf.karta.Linija;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ProdajaKarataController implements Initializable {
	
	public static Set<String> relacijeSet = new HashSet<>();
	public static List<Linija> lines = new ArrayList<>();
	
	@FXML
	private DatePicker ddd = new DatePicker();
	@FXML
	private JFXButton pretragaButton = new JFXButton();
	@FXML
	private JFXTextField odredisteTextField = new JFXTextField();
	@FXML
	private JFXDatePicker datum = new JFXDatePicker();
	@FXML
	private TableView<Linija> linijeTable = new TableView<>();
	@FXML
	private TableColumn<Linija,String> nazivLinijeColumn = new TableColumn<>();
	@FXML
	private TableColumn<Linija,Double> cijenaColumn = new TableColumn<>();
	@FXML
	private TableColumn<Linija,LocalTime> vrijemePolaskaColumn = new TableColumn<>();
	@FXML
	private TableColumn<Linija,Integer> peronColumn = new TableColumn<>();
	@FXML
	private TableColumn<Linija,Integer> brojSjedistaColumn = new TableColumn<>();
	@FXML
	private TableColumn<Linija,String> prevoznikColumn = new TableColumn<>();
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		ddd.setDayCellFactory(picker -> new DateCell() {
	        public void updateItem(LocalDate date, boolean empty) {
	            super.updateItem(date, empty);
	            LocalDate today = LocalDate.now();

	            setDisable(empty || date.compareTo(today) < 0 );
	        }
	    });
		linijeTable.setPlaceholder(new Label("Odaberite relaciju i datum"));
		// TODO Auto-generated method stub
		ucitajRelacije();
		
		
		JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
	    autoCompletePopup.getSuggestions().addAll(relacijeSet);

	    
	    autoCompletePopup.setSelectionHandler(event -> {
	        odredisteTextField.setText(event.getObject());

	        // you can do other actions here when text completed
	    });
	    // filtering options
	    odredisteTextField.textProperty().addListener(observable -> {
	        autoCompletePopup.filter(string -> string.toLowerCase().contains(odredisteTextField.getText().toLowerCase()));
	        if (autoCompletePopup.getFilteredSuggestions().isEmpty() || odredisteTextField.getText().isEmpty()) {
	            autoCompletePopup.hide();
	            // if you remove textField.getText.isEmpty() when text field is empty it suggests all options
	            // so you can choose
	        } else {
	            autoCompletePopup.show(odredisteTextField);
	        }
	    });
	}

	
	public void ucitajRelacije() {
		// TODO Auto-generated method stub
		
	}

	
	@FXML
	public void pretragaRelacija() {
		
	}
	
	public void dodajLinije() {
		
	}
}
