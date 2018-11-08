package org.unibl.etf.salterski_radnik;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import org.unibl.etf.karta.Karta;
import org.unibl.etf.karta.Linija;
import org.unibl.etf.karta.MjesecnaKarta;
import org.unibl.etf.karta.Prevoznik;
import org.unibl.etf.karta.RedVoznje;
import org.unibl.etf.karta.Relacija;
import org.unibl.etf.karta.TipKarte;
import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.util.Util;
import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.mysql.jdbc.exceptions.jdbc4.MySQLDataException;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TableView.ResizeFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;

public class ProdajaKarataController implements Initializable {
	
	public static Set<String> relacijeSet = new HashSet<>();
	public static List<Integer> linijeIDList = new ArrayList<>();
	public static ObservableList<Karta> karteObs = FXCollections.observableArrayList();
	public static String daniUSedmici;
	public static File odabranaSlika = null;
	public static boolean kupovinaMjesecne=false;
	
	@FXML
	private JFXButton odaberiSlikuButton = new JFXButton();
	@FXML
	private Label slikaKorisnikaLabel;
	@FXML
	private JFXTextField imeField = new JFXTextField();
	@FXML
	private JFXTextField prezimeField = new JFXTextField();
	@FXML
	private JFXTextField nazivSlikeTextField;
	
	
	
	@FXML
	private ToggleGroup toggleGroup = new ToggleGroup();
	@FXML
	private JFXRadioButton radioButtonObicna;
	@FXML
	private JFXRadioButton radioButtonMjesecna;
	@FXML
	private JFXComboBox<TipKarte> tipKarteComboBox;
	@FXML
	private JFXTextField brojKarata = new JFXTextField();
	@FXML
	private DatePicker datum =  new DatePicker();
	@FXML
	private JFXButton pretragaButton = new JFXButton();
	@FXML
	private JFXTextField odredisteTextField = new JFXTextField();
	@FXML
	private TableView<Karta> linijeTable = new TableView<>();
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
	private JFXButton kupovinaButton = new JFXButton();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nazivSlikeTextField.setEditable(false);
		kupovinaButton.disableProperty().bind(Bindings.isEmpty(linijeTable.getSelectionModel().getSelectedItems()));
		tipKarteComboBox.setItems(FXCollections.observableArrayList(TipKarte.values()));
		radioButtonObicna.setSelected(true);
		disableMjesecna(true);
		radioButtonMjesecna.setToggleGroup(toggleGroup);
		radioButtonObicna.setToggleGroup(toggleGroup);
		toggleGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
		    if (newValue == null) {
		        oldValue.setSelected(true);
		    }
		    else
		    	if(newValue.equals(radioButtonObicna)) {
		    		kupovinaMjesecne=false;
		    		kupovinaButton.disableProperty().bind(Bindings.isEmpty(linijeTable.getSelectionModel().getSelectedItems()));
		    		karteObs.clear();
		    		disableMjesecna(true);
		    	}
		    	else {
		    		kupovinaMjesecne=true;
		    		disableMjesecna(false);
		    		karteObs.clear();
		    		kupovinaButton.disableProperty().bind(Bindings.isEmpty(linijeTable.getSelectionModel().getSelectedItems()).or(Bindings.createBooleanBinding(
		    			    () -> imeField.getText().isEmpty() || prezimeField.getText().isEmpty() || 
		    			    	  nazivSlikeTextField.getText().isEmpty() || 
		    			    	  tipKarteComboBox.getSelectionModel().getSelectedItem() == null, 
		    			    imeField.textProperty(), prezimeField.textProperty(), nazivSlikeTextField.textProperty(),
		    			    tipKarteComboBox.getSelectionModel().selectedItemProperty()
		    			    )));
		    	}
		});	
		
		linijeTable.setItems(karteObs);
		brojKarata.setText("1");
		ucitajRelacije();
		linijeTable.setPlaceholder(new Label("Odaberite relaciju i datum"));
		datum.setValue(LocalDate.now());		
		nazivLinijeColumn.setCellValueFactory(new PropertyValueFactory<>("nazivLinije"));
		vrijemePolaskaColumn.setCellValueFactory(new PropertyValueFactory<>("vrijemePolaska"));
		vrijemeDolaskaColumn.setCellValueFactory(new PropertyValueFactory<>("vrijemeDolaska"));
		prevoznikColumn.setCellValueFactory(new PropertyValueFactory<>("nazivPrevoznika"));
		cijenaColumn.setCellValueFactory(new PropertyValueFactory<>("cijena"));
		peronColumn.setCellValueFactory(new PropertyValueFactory<>("peron"));
		
		datum.setDayCellFactory(picker -> new DateCell() {
	        public void updateItem(LocalDate date, boolean empty) {
	            super.updateItem(date, empty);
	            LocalDate today = LocalDate.now();
	            setDisable(empty || date.compareTo(today) < 0 );
	        }
	    });
		
		JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
	    autoCompletePopup.getSuggestions().addAll(relacijeSet);
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
			    () -> !relacijeSet.contains(odredisteTextField.getText()),
			    	odredisteTextField.textProperty()
			    ));
	}

	
	public void disableMjesecna(boolean b) {
		// TODO Auto-generated method stub
		imeField.setDisable(b);
		prezimeField.setDisable(b);
		odaberiSlikuButton.setDisable(b);
		nazivSlikeTextField.setDisable(b);
	}


	public static void ucitajRelacije() {
		// TODO Auto-generated method stub
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = c.prepareStatement("select Naziv from mjesto where BrojPoste!=?");
			s.setInt(1, SalterskiRadnikController.brojMjesta);
			r = s.executeQuery();
			while(r.next()) {
				relacijeSet.add(r.getString(1));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Util.close(r, s, c);
	}

	
	@FXML
	public void pretragaRelacija() {
		int brojMjestaOdredista = pronadjiTrazenoOdrediste(odredisteTextField.getText());
		karteObs.clear();
		Connection c = null;
		PreparedStatement s = null;
		String sqlQuery = "select dani_u_sedmici,vrijeme_polaska,NazivPrevoznika,Email,prevoznik.Adresa,WEBAdresa,Telefon,prevoznik.BrojPoste,NazivLinije,Peron,Polaziste,Odrediste,vrijeme_dolaska,cijena,relacija.IdRelacije,relacija.IdLinije from linija join (relacija,prevoznik) "
				+ "on (linija.IdLinije=relacija.IdLinije) and (linija.prevoznik_fk=IdPrevoznika) where (linija.IdLinije=relacija.IdLinije) and (polaziste=? && odrediste=?)";
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = c.prepareStatement(sqlQuery);
			s.setInt(2, brojMjestaOdredista);
			s.setInt(1, SalterskiRadnikController.brojMjesta);
			r = s.executeQuery();
			while(r.next()) {
				daniUSedmici = r.getString(1);
				Time vrijemePolaska = r.getTime(2);
				if(zadovoljavaDatumVrijeme(daniUSedmici,vrijemePolaska)) {
					Prevoznik prevoznik = new Prevoznik(r.getString(3), r.getString(4), r.getString(5), r.getString(6), r.getString(7), "BL");
					Linija linija = new Linija(r.getInt(16),r.getString(9), daniUSedmici,r.getInt(10));
					Relacija relacija = new Relacija(r.getInt(15),r.getInt(16),r.getInt(11), r.getInt(12));
					Karta karta = new Karta(linija, relacija, vrijemePolaska, r.getTime(13), r.getDouble(14), LocalDate.now(), prevoznik, PrijavaController.nalog.getKorisnickoIme());
					karteObs.add(karta);
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


	public boolean zadovoljavaDatumVrijeme(String daniUSedmici,Time vrijemePolaska) {
		LocalTime localTime = LocalTime.now();
		if(datum.getValue().equals(LocalDate.now())) {
			System.out.println("danasnji");
			System.out.println(daniUSedmici);
			System.out.println();
			return (localTime.compareTo(vrijemePolaska.toLocalTime())<0);
			}
		else
			return (daniUSedmici.contains(datum.getValue().getDayOfWeek().toString())) && daniUSedmici.contains(datum.getValue().getDayOfWeek().toString());
	}


	public int pronadjiTrazenoOdrediste(String odrediste) {
		// TODO Auto-generated method stub
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select BrojPoste from mjesto where Naziv=?";
		try {
			c = Util.getConnection();
			s = c.prepareStatement(sql);
			s.setString(1, odrediste);
			r = s.executeQuery();
			while(r.next()) {
				int x = r.getInt(1);
				return x;
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		Util.close(r, s, c);
		return 0;
	}

	@FXML
	public void kupovina() {
		if(50-provjeriBrojKarata()<Integer.parseInt(brojKarata.getText())) {
			showNedovoljnoMjesta();
			return;
		}
		if(showPotvrda()) {
			if(!kupovinaMjesecne) {
				System.out.println("prodaja");
				Connection c = null;
				PreparedStatement s = null;
				ResultSet r = null;
				for(int i=0;i<Integer.parseInt(brojKarata.getText());++i) {
					int brojKarata = provjeriBrojKarata();
					System.out.println("Broj karata: " + brojKarata);
					System.out.println(linijeTable.getSelectionModel().getSelectedItem());
					String sqlQuery = "insert into karta value (DEFAULT,?,?,?,?)";
					try {
						c = Util.getConnection();
						s = c.prepareStatement(sqlQuery);
						s.setInt(1, linijeTable.getSelectionModel().getSelectedItem().getRelacija().getIdRelacije());
						s.setDate(2, Date.valueOf(datum.getValue()));
						s.setInt(3, brojKarata+1);
						s.setInt(4, linijeTable.getSelectionModel().getSelectedItem().getLinija().getIdLinije());
						System.out.println(s.executeUpdate());
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						try(BufferedWriter bw = 
							new BufferedWriter(new FileWriter(new File("karta"+linijeTable.getSelectionModel().getSelectedItem().getIdKarte()))))
						{
							bw.write(linijeTable.getSelectionModel().getSelectedItem().toString());
				
						}	catch (IOException	 e) {
						// TODO: handle exception
							e.printStackTrace();
						}
						Util.close(r, s, c);
				}
			}
			else { // KUPOVINA MJESECNE
				Karta k = linijeTable.getSelectionModel().getSelectedItem();
				System.out.println("Ispis");
				System.out.println(k);
				MjesecnaKarta mk = new MjesecnaKarta(k.getIdKarte(), k.getLinija(), k.getRelacija(), k.getVrijemePolaska(), k.getVrijemeDolaska(), k.getCijena(), LocalDate.now(), k.getPrevoznik(), "vc", imeField.getText(), prezimeField.getText(), tipKarteComboBox.getValue(), odabranaSlika);
				System.out.println("Mjesecna karta:");
				System.out.println(mk);
				Connection c = null;
				PreparedStatement s = null;
				
			}
			
		}			
	}

	public boolean showPotvrda() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Kupovina");
		alert.setHeaderText("Da li ste sigurni?");
		Optional<ButtonType> action = alert.showAndWait();
		return action.get().equals(ButtonType.OK);
	}

	public void showNedovoljnoMjesta() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("GRESKA");
		alert.setHeaderText("Nedovoljno mjesta u autobusu");
		alert.showAndWait();
	}
	
	@FXML
	public void odaberiSliku() {
		System.out.println("fasf");
		FileChooser fc  = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter("PNG Files", "*.png"));
	    odabranaSlika = fc.showOpenDialog(null);
		if(odabranaSlika!=null) {
			try {
				nazivSlikeTextField.setText(odabranaSlika.getName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public int provjeriBrojKarata() {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sqlQuery = "select count(*) from karta where IdRelacije=? and Datum=? and IdLinije=?";
		try {
			c = Util.getConnection();
			s = c.prepareStatement(sqlQuery);
			s.setInt(1, linijeTable.getSelectionModel().getSelectedItem().getRelacija().getIdRelacije());
			s.setDate(2, Date.valueOf(datum.getValue()));
			s.setInt(3, linijeTable.getSelectionModel().getSelectedItem().getLinija().getIdLinije());
			r = s.executeQuery();
			while(r.next())
				return r.getInt(1);
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		}
		Util.close(r, s, c);
		return 0;
	}
}
