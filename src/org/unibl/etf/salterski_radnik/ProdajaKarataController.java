package org.unibl.etf.salterski_radnik;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.unibl.etf.karta.Linija;
import org.unibl.etf.util.Util;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class ProdajaKarataController implements Initializable {
	
	public static List<String> relacijeList = new ArrayList<>();
	public static List<Integer> linijeIDList = new ArrayList<>();
	public static ObservableList<Linija> obsLinije = FXCollections.observableArrayList();

	@FXML
	private JFXTextField brojKarata = new JFXTextField();
	@FXML
	private JFXRadioButton djecijaKarta = new JFXRadioButton();
	@FXML
	private DatePicker datum =  new DatePicker();
	@FXML
	private JFXButton pretragaButton = new JFXButton();
	@FXML
	private JFXTextField odredisteTextField = new JFXTextField();
	@FXML
	private TableView<Linija> linijeTable = new TableView<>();
	@FXML
	private TableColumn<Linija,Integer> idLinijeColumn = new TableColumn<>();
	@FXML
	private TableColumn<Linija,Integer> idStaniceColumn = new TableColumn<>();
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
		ucitajRelacije();
		linijeTable.setPlaceholder(new Label("Odaberite relaciju i datum"));
		datum.setValue(LocalDate.now());
		linijeTable.setItems(obsLinije);
		nazivLinijeColumn.setCellValueFactory(new PropertyValueFactory<>("nazivLinije"));
		idLinijeColumn.setCellValueFactory(new PropertyValueFactory<>("idLinije"));
		idStaniceColumn.setCellValueFactory(new PropertyValueFactory<>("idStanice"));
		
		datum.setDayCellFactory(picker -> new DateCell() {
	        public void updateItem(LocalDate date, boolean empty) {
	            super.updateItem(date, empty);
	            LocalDate today = LocalDate.now();

	            setDisable(empty || date.compareTo(today) < 0 );
	        }
	    });
		
		JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
	    autoCompletePopup.getSuggestions().addAll(relacijeList);
	    autoCompletePopup.setSelectionHandler(event -> {
	        odredisteTextField.setText(event.getObject());
	    });
	    odredisteTextField.textProperty().addListener(observable -> {
	        autoCompletePopup.filter(string -> string.toLowerCase().contains(odredisteTextField.getText().toLowerCase()));
	        if (autoCompletePopup.getFilteredSuggestions().isEmpty() || odredisteTextField.getText().isEmpty()) {
	            autoCompletePopup.hide();
	        } else {
	            autoCompletePopup.show(odredisteTextField);
	        }
	    });
	    
	    pretragaButton.disableProperty().bind(Bindings.createBooleanBinding(
			    () -> !relacijeList.contains(odredisteTextField.getText()),
			    	odredisteTextField.textProperty()
			    ));
	}

	
	public void ucitajRelacije() {
		// TODO Auto-generated method stub
		Connection c = null;
		Statement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = c.createStatement();
			r = s.executeQuery("SELECT * FROM relacija");
			while(r.next()) {
				relacijeList.add(r.getString(4));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	@FXML
	public void pretragaRelacija() {
		System.out.println("Datum: " + datum.getValue());
		System.out.println("Odrediste: " + odredisteTextField.getText());
		Connection c = null;
		PreparedStatement s = null;
		String sqlQuery = "SELECT * FROM relacija WHERE Odrediste=?";
		
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = c.prepareStatement(sqlQuery);
			s.setString(1, odredisteTextField.getText());
			r = s.executeQuery();
			System.out.println("Krenuli...");
			while(r.next()) {
				System.out.println("Nova linija: " + r.getString(2));
				
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	public void dodajLinije() {
		
	}
}
