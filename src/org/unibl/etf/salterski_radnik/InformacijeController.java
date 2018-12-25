 package org.unibl.etf.salterski_radnik;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import org.unibl.etf.karta.Karta;
import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class InformacijeController implements Initializable{

	private Set<String> mjestaSet = new HashSet<>();
	private static ObservableList<Karta> karteObs = FXCollections.observableArrayList();
	public static String nazivMjesta;

	@FXML
	private JFXButton pretragaButton = new JFXButton();
	@FXML
	private TableView<Karta> karteTable = new TableView<>();
	@FXML
	private TableColumn<Karta,String> nazivLinijeColumn = new TableColumn<>();
	@FXML
	private TableColumn<Karta,Double> cijenaColumn = new TableColumn<>();
	@FXML
	private TableColumn<Karta,LocalTime> vrijemePolaskaColumn = new TableColumn<>();
	@FXML
	private TableColumn<Karta,LocalTime> vrijemeDolaskaColumn = new TableColumn<>();
	@FXML
	private TableColumn<Karta,String> prevoznikColumn = new TableColumn<>();
	@FXML
	private TableColumn<Karta,Integer> peronColumn = new TableColumn<>();	
	@FXML
	private DatePicker datum = new DatePicker();
	@FXML
	private ToggleGroup toggleGroup = new ToggleGroup();
	@FXML
	private JFXRadioButton polasciRadioButton;
	@FXML
	private JFXRadioButton dolasciRadioButton;
	@FXML
	private JFXTextField mjestoTextField = new JFXTextField();
	private static String daniUSedmici;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		karteTable.setItems(karteObs);
		datum.setValue(LocalDate.now());
		nazivMjesta = getNazivMjesta();
		ucitajMjesta();
		karteTable.setPlaceholder(new Label("Odaberite relaciju i datum"));
		polasciRadioButton.setSelected(true);
		mjestoTextField.setPromptText("Destinacija");
		
		datum.setDayCellFactory(picker -> new DateCell() {
	        public void updateItem(LocalDate date, boolean empty) {
	            super.updateItem(date, empty);
	            LocalDate today = LocalDate.now();
	            setDisable(empty || date.compareTo(today) < 0 );
	        }
	    });
		datum.setEditable(false);
		toggleSetUp();
		polasciRadioButton.setToggleGroup(toggleGroup);
		dolasciRadioButton.setToggleGroup(toggleGroup);
		nazivLinijeColumn.setCellValueFactory(new PropertyValueFactory<>("nazivLinije"));
		vrijemePolaskaColumn.setCellValueFactory(new PropertyValueFactory<>("vrijemePolaska"));
		vrijemeDolaskaColumn.setCellValueFactory(new PropertyValueFactory<>("vrijemeDolaska"));
		prevoznikColumn.setCellValueFactory(new PropertyValueFactory<>("nazivPrevoznika"));
		cijenaColumn.setCellValueFactory(new PropertyValueFactory<>("cijena"));
		peronColumn.setCellValueFactory(new PropertyValueFactory<>("peron"));
		
		Util.setAutocompleteList(mjestoTextField, mjestaSet);
		mjestoTextField.getValidators().addAll(Util.requredFieldValidator(mjestoTextField),Util.collectionValidator(mjestoTextField, mjestaSet, true, "Unesite mjesto"));
		
	}
	
	public void toggleSetUp() {
		toggleGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
		    if (newValue == null) {
		        oldValue.setSelected(true);
		    }
		    else
		    	if(newValue.equals(polasciRadioButton)) {
		    		karteObs.clear();
		    		mjestoTextField.clear();
		    		mjestoTextField.setPromptText("Destinacija");
		    		mjestoTextField.resetValidation();
		    		
		    	}
		    	else {
		    		mjestoTextField.clear();
		    		karteObs.clear();
		    		mjestoTextField.setPromptText("Polaziste");		    		
		    		mjestoTextField.resetValidation();
		    	}
		});	
	}

	/*public boolean zadovoljavaDatumVrijeme(String daniUSedmici,Time vrijemePolaska) {
		LocalTime localTime = LocalTime.now();
		if(datum.getValue().equals(LocalDate.now())) {
			return (localTime.compareTo(vrijemePolaska.toLocalTime())<0);
			}
		else
			return (daniUSedmici.contains(datum.getValue().getDayOfWeek().toString())) && daniUSedmici.contains(datum.getValue().getDayOfWeek().toString());
	}*/
	
	@FXML
	public void getKarte() {
		if(mjestoTextField.validate()){
			karteObs.clear();
			if(polasciRadioButton.isSelected()) {
					for(Karta karta : Karta.getKarteList(nazivMjesta, mjestoTextField.getText())) {
						daniUSedmici = karta.getLinija().getDaniUSedmici();
						if(daniUSedmici.contains(datum.getValue().getDayOfWeek().toString()))
							karteObs.add(karta);
					}	
			}
			else {
				for(Karta karta : Karta.getKarteList(mjestoTextField.getText(),nazivMjesta)) {
					daniUSedmici = karta.getLinija().getDaniUSedmici();
					karteObs.add(karta);
				}
			}
			if(karteObs.isEmpty())
				showPrazanSetAlert();
		} 
		
	}

	public void showPrazanSetAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Obavjestenje");
		alert.setHeaderText("Nema linija");
		alert.setContentText("Za odabranu relaciju i datum nema linija ili su vec otisle.");
		alert.showAndWait();

	}
	public void ucitajMjesta() {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select distinct Naziv from mjesto where Naziv!=?";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, nazivMjesta);
			r = s.executeQuery();
			while(r.next()) {
				mjestaSet.add(r.getString(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(r, s, c);
		}
	}

	

	public String getNazivMjesta() {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select mjesto.Naziv from mjesto join autobuska_stanica\n" + 
				"on (mjesto.PostanskiBroj=autobuska_stanica.PostanskiBroj) \n" + 
				"where (autobuska_stanica.JIBStanice=?)";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, PrijavaController.nalog.getIdStanice());
			r = s.executeQuery();
			if(r.next())
				return r.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(r, s, c);
		}
		return null;
	}
}
