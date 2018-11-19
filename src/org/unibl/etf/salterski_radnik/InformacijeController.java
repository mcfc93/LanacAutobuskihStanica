package org.unibl.etf.salterski_radnik;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import org.unibl.etf.karta.Karta;
import org.unibl.etf.karta.Linija;
import org.unibl.etf.karta.Prevoznik;
import org.unibl.etf.karta.Relacija;
import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.util.Util;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
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

	public static Set<String> odredistaSet = new HashSet<>();
	public static Set<String> polazistaSet = new HashSet<>();
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
	private JFXTextField mjesto = new JFXTextField();
	private boolean polasci = true;
	private static String daniUSedmici;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		karteTable.setItems(karteObs);
		datum.setValue(LocalDate.now());
		nazivMjesta = getNazivMjesta();
		System.out.println("Naziv mjesta: " + nazivMjesta);
		ucitajMjesta();
		karteTable.setPlaceholder(new Label("Odaberite relaciju i datum"));
		autoComplete(mjestaSet);
		polasciRadioButton.setToggleGroup(toggleGroup);
		dolasciRadioButton.setToggleGroup(toggleGroup);
		polasciRadioButton.setSelected(true);
		mjesto.setPromptText("Destinacija");
		datum.setDayCellFactory(picker -> new DateCell() {
	        public void updateItem(LocalDate date, boolean empty) {
	            super.updateItem(date, empty);
	            LocalDate today = LocalDate.now();
	            setDisable(empty || date.compareTo(today) < 0 );
	        }
	    });
		toggleGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
		    if (newValue == null) {
		        oldValue.setSelected(true);
		    }
		    else
		    	if(newValue.equals(polasciRadioButton)) {
		    		karteObs.clear();
		    		mjesto.setText("");
		    		polasci = true;
		    		mjesto.setPromptText("Destinacija");
		    		
		    	}
		    	else {
		    		mjesto.setText("");
		    		karteObs.clear();
		    		polasci  = false;
		    		mjesto.setPromptText("Polaziste");		    		
		    	}
		});	
		
		nazivLinijeColumn.setCellValueFactory(new PropertyValueFactory<>("nazivLinije"));
		vrijemePolaskaColumn.setCellValueFactory(new PropertyValueFactory<>("vrijemePolaska"));
		vrijemeDolaskaColumn.setCellValueFactory(new PropertyValueFactory<>("vrijemeDolaska"));
		prevoznikColumn.setCellValueFactory(new PropertyValueFactory<>("nazivPrevoznika"));
		cijenaColumn.setCellValueFactory(new PropertyValueFactory<>("cijena"));
		peronColumn.setCellValueFactory(new PropertyValueFactory<>("peron"));
		
		pretragaButton.disableProperty().bind(Bindings.createBooleanBinding(
			    () -> !mjestaSet.contains(mjesto.getText()),
			    	mjesto.textProperty()
			    ));
		
	}
	
	public boolean zadovoljavaDatumVrijeme(String daniUSedmici,Time vrijemePolaska) {
		LocalTime localTime = LocalTime.now();
		if(datum.getValue().equals(LocalDate.now())) {
			return (localTime.compareTo(vrijemePolaska.toLocalTime())<0);
			}
		else
			return (daniUSedmici.contains(datum.getValue().getDayOfWeek().toString())) && daniUSedmici.contains(datum.getValue().getDayOfWeek().toString());
	}
	
	@FXML
	public void getKarte() {
		karteObs.clear();
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sqlQueryPolasci = "select DaniUSedmici,VrijemePolaska,NazivPrevoznika,Email,prevoznik.Adresa,WEBAdresa,Telefon,prevoznik.PostanskiBroj,NazivLinije,Peron,Polaziste,Odrediste,VrijemeDolaska,CijenaJednokratna,relacija.IdRelacije,relacija.IdLinije from linija join (relacija,prevoznik) "
				+ "on (linija.IdLinije=relacija.IdLinije) and (linija.JIBPrevoznika=prevoznik.JIBPrevoznika) where (linija.IdLinije=relacija.IdLinije) and (Polaziste=? && Odrediste=?)";
		String sqlQueryDolasci = "select DaniUSedmici,VrijemePolaska,NazivPrevoznika,Email,prevoznik.Adresa,WEBAdresa,Telefon,prevoznik.PostanskiBroj,NazivLinije,Peron,Polaziste,Odrediste,VrijemeDolaska,CijenaJednokratna,relacija.IdRelacije,relacija.IdLinije from linija join (relacija,prevoznik) "
				+ "on (linija.IdLinije=relacija.IdLinije) and (linija.JIBPrevoznika=prevoznik.JIBPrevoznika) where (linija.IdLinije=relacija.IdLinije) and (Odrediste=? && Polaziste=?)";
		
		try {
			c = Util.getConnection();
			s = c.prepareStatement( (polasci)? sqlQueryPolasci: sqlQueryDolasci);
			s.setString(1, getNazivMjesta());
			s.setString(2, mjesto.getText());
			r = s.executeQuery();
			
			if(polasci) {
					s = c.prepareStatement(sqlQueryPolasci);
					s.setString(1, getNazivMjesta());
					s.setString(2, mjesto.getText());
					r = s.executeQuery();
					while(r.next()) {
						daniUSedmici = r.getString(1);
						Time vrijemePolaska = r.getTime(2);
						if(daniUSedmici.contains(datum.getValue().getDayOfWeek().toString())) {
							Prevoznik prevoznik = new Prevoznik(r.getString("NazivPrevoznika"));
							Linija linija = new Linija(r.getInt(16),r.getString(9), daniUSedmici,r.getInt(10),r.getString(3));
							Relacija relacija = new Relacija(r.getInt(15),r.getInt(16),r.getString(11), r.getString(12));
							Karta karta = new Karta(linija, relacija, vrijemePolaska, r.getTime(13), r.getDouble(14), LocalDate.now(), prevoznik, PrijavaController.nalog.getKorisnickoIme(),PrijavaController.nalog.getIdStanice());
							System.out.println("nasao");
							karteObs.add(karta);
						}
				}		
			}
			else {
				while(r.next()) {
					daniUSedmici = r.getString(1);
					Time vrijemePolaska = r.getTime(2);
					if(zadovoljavaDatumVrijeme(daniUSedmici,vrijemePolaska)) {
						Prevoznik prevoznik = new Prevoznik(r.getString("NazivPrevoznika"));
						Linija linija = new Linija(r.getInt(16),r.getString(9), daniUSedmici,r.getInt(10),r.getString(3));
						Relacija relacija = new Relacija(r.getInt(15),r.getInt(16),r.getString(11), r.getString(12));
						Karta karta = new Karta(linija, relacija, vrijemePolaska, r.getTime(13), r.getDouble(14), LocalDate.now(), prevoznik, PrijavaController.nalog.getKorisnickoIme(),PrijavaController.nalog.getIdStanice());
						System.out.println("nasao");
						karteObs.add(karta);
					}
				}
			}
			
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		if(karteObs.isEmpty())
			showPrazanSetAlert();
		Util.close(r, s, c);
	}

	public void showPrazanSetAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Obavjestenje");
		alert.setHeaderText("Nema linija");
		alert.setContentText("Za odabranu relaciju i datum nema linija ili su vec otisle.");
		alert.showAndWait();

	}
	public void ucitajMjesta() {
		// TODO Auto-generated method stub
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = c.prepareStatement("select distinct Naziv from mjesto where Naziv!=?");
			s.setString(1,nazivMjesta);
			r = s.executeQuery();
			while(r.next()) {
				mjestaSet.add(r.getString(1));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Util.close(r, s, c);
	}


	public void autoComplete(Set<String> polazistaSet2) {
		JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
		autoCompletePopup.getSuggestions().clear();
	    autoCompletePopup.getSuggestions().addAll(polazistaSet2);
	    autoCompletePopup.setSelectionHandler(event -> {
	        mjesto.setText(event.getObject());
	    });
	     
	    mjesto.textProperty().addListener(observable -> {
	        autoCompletePopup.filter(string -> string.toLowerCase().contains(mjesto.getText().toLowerCase()));
	        if (autoCompletePopup.getFilteredSuggestions().isEmpty() || mjesto.getText().isEmpty()) {
	            autoCompletePopup.hide();
	        } else {
	            autoCompletePopup.show(mjesto);
	        }
	    });
		
	}


	public String getNazivMjesta() {
		// TODO Auto-generated method stub
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select mjesto.Naziv from mjesto join autobuska_stanica\n" + 
				"on (mjesto.PostanskiBroj=autobuska_stanica.PostanskiBroj) \n" + 
				"where (autobuska_stanica.JIBStanice=?)";
		try {
			c = Util.getConnection();
			s = c.prepareStatement(sql);
			s.setString(1, PrijavaController.nalog.getIdStanice());
			r = s.executeQuery();
			while(r.next()) {
				return r.getString(1);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}




}
