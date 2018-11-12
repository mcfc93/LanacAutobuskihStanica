package org.unibl.etf.salterski_radnik;

import java.io.File;
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
import org.unibl.etf.karta.Prevoznik;
import org.unibl.etf.karta.Relacija;
import org.unibl.etf.karta.TipKarte;
import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.util.Util;
import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


public class ProdajaKarataController implements Initializable {
	
	public static Set<String> relacijeSet = new HashSet<>();
	public static List<Integer> linijeIDList = new ArrayList<>();
	public static ObservableList<Karta> karteObs = FXCollections.observableArrayList();
	public static String daniUSedmici;
	public static File odabranaSlika = null;
	public static boolean kupovinaMjesecne=false;
	private int brojKarataZaKupovinu;
	@FXML
	private JFXTextField brojTelefonaTextField = new JFXTextField();
	@FXML
	private JFXTextField polazisteTextField = new JFXTextField();
	@FXML
	public JFXCheckBox rezervacijaCheckBox = new JFXCheckBox();
	@FXML
	private JFXButton odaberiSlikuButton = new JFXButton();
	@FXML
	private Label slikaKorisnikaLabel;
	@FXML
	private JFXTextField imeTextField = new JFXTextField();
	@FXML
	private JFXTextField prezimeTextField = new JFXTextField();
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
	private JFXTextField brojKarataTextField = new JFXTextField();
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
    	//
		disableMjesecna(true);
		//
		polazisteTextField.setVisible(false);
    	brojTelefonaTextField.setDisable(true);
    	rezervacijaCheckBox.setSelected(false);
		rezervacijaCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if(newValue) {
			       imeTextField.setDisable(false);
			        prezimeTextField.setDisable(false);
			    	brojTelefonaTextField.setDisable(false);

		        }
		        else {
		        	imeTextField.setDisable(true);
		        	prezimeTextField.setDisable(true);
		        	brojTelefonaTextField.setDisable(true);

		        }
		    }
		});

		nazivSlikeTextField.setEditable(false);
		kupovinaButton.disableProperty().bind(Bindings.isEmpty(linijeTable.getSelectionModel().getSelectedItems()));
		tipKarteComboBox.setItems(FXCollections.observableArrayList(TipKarte.values()));
		radioButtonObicna.setSelected(true);
		radioButtonMjesecna.setToggleGroup(toggleGroup);
		radioButtonObicna.setToggleGroup(toggleGroup);
		toggleGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
		    if (newValue == null) {
		        oldValue.setSelected(true);
		    }
		    else
		    	if(newValue.equals(radioButtonObicna)) {
		    		datum.setDisable(false);
		    		odredisteTextField.setText("");
		    		imeTextField.setText("");
		    		prezimeTextField.setText("");
		    		brojTelefonaTextField.setText("");
		    		tipKarteComboBox.setValue(null);
		    		tipKarteComboBox.setDisable(true);
		    		rezervacijaCheckBox.setDisable(false);
		    		polazisteTextField.setVisible(false);
		        	brojKarataTextField.setVisible(true);
		    		imeTextField.setText("");
		    		prezimeTextField.setText("");
		    		nazivSlikeTextField.setText("");
		    		odabranaSlika = null;
		    		brojKarataTextField.setText("1");
		    		kupovinaMjesecne=false;
		    		kupovinaButton.disableProperty().bind(Bindings.isEmpty(linijeTable.getSelectionModel().getSelectedItems()));
		    		karteObs.clear();
		    		disableMjesecna(true);
		    	}
		    	else {
		    		brojKarataZaKupovinu=1;
		    		brojKarataTextField.setText("1");
		    		tipKarteComboBox.setDisable(false);
		    		polazisteTextField.setText("");
		    		odredisteTextField.setText("");
		    		imeTextField.setText("");
		    		prezimeTextField.setText("");
		    		brojTelefonaTextField.setText("");
		    		datum.setValue(LocalDate.now());
		    		datum.setDisable(true);
		    		rezervacijaCheckBox.setSelected(false);
		    		brojKarataZaKupovinu=1;
		    		rezervacijaCheckBox.setDisable(true);
		        	polazisteTextField.setVisible(true);
		    		kupovinaMjesecne=true;
		    		brojKarataTextField.setVisible(false);
		    		System.out.println("Mjesecna");
		    		disableMjesecna(false);
		    		karteObs.clear();
		    		kupovinaButton.disableProperty().bind(Bindings.isEmpty(linijeTable.getSelectionModel().getSelectedItems()).or(Bindings.createBooleanBinding(
		    			    () -> imeTextField.getText().isEmpty() || prezimeTextField.getText().isEmpty() || 
		    			    	  nazivSlikeTextField.getText().isEmpty() || 
		    			    	  tipKarteComboBox.getSelectionModel().getSelectedItem() == null, 
		    			    imeTextField.textProperty(), prezimeTextField.textProperty(), nazivSlikeTextField.textProperty(),
		    			    tipKarteComboBox.getSelectionModel().selectedItemProperty()
		    			    )));
		    	}
		});	
		
		linijeTable.setItems(karteObs);
		brojKarataTextField.setText("1");
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
		
		autoComplete();
	    
	    pretragaButton.disableProperty().bind(Bindings.createBooleanBinding(
			    () -> !relacijeSet.contains(odredisteTextField.getText()),
			    	odredisteTextField.textProperty()
			    ));
	}

	
	private void autoComplete() {
		// TODO Auto-generated method stub
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
	    JFXAutoCompletePopup<String> autoCompletePopup2 = new JFXAutoCompletePopup<>();
	    autoCompletePopup2.getSuggestions().addAll(relacijeSet);
	    autoCompletePopup2.setSelectionHandler(event -> {
	        polazisteTextField.setText(event.getObject());
	    });
	    polazisteTextField.textProperty().addListener(observable -> {
	        autoCompletePopup2.filter(string -> string.toLowerCase().contains(polazisteTextField.getText().toLowerCase()));
	        if (autoCompletePopup2.getFilteredSuggestions().isEmpty() || polazisteTextField.getText().isEmpty()) {
	            autoCompletePopup2.hide();
	        } else {
	            autoCompletePopup2.show(polazisteTextField);
	        }
	    });
	    
	}


	public void disableMjesecna(boolean b) {
		// TODO Auto-generated method stub
		imeTextField.setDisable(b);
		prezimeTextField.setDisable(b);
		odaberiSlikuButton.setDisable(b);
		nazivSlikeTextField.setDisable(b);
	}


	public void ucitajRelacije() {
		// TODO Auto-generated method stub
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql1 = "select distinct Naziv from mjesto where PostanskiBroj!=?";
		String sql2 = "select distinct Naziv from mjesto";
		try {
			c = Util.getConnection();
			s = c.prepareStatement( (rezervacijaCheckBox.isSelected())? sql2:sql1);
			s.setInt(1,SalterskiRadnikController.brojMjesta);
			r = s.executeQuery();
			while(r.next()) {
				relacijeSet.add(r.getString(1));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			Util.close(r, s, c);
		}
	}

	
	@FXML
	public void pretragaRelacija() {
		karteObs.clear();
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sqlQuery = "select DaniUSedmici,VrijemePolaska,NazivPrevoznika,Email,prevoznik.Adresa,WEBAdresa,Telefon,prevoznik.PostanskiBroj,NazivLinije,Peron,Polaziste,Odrediste,VrijemeDolaska,CijenaJednokratna,relacija.IdRelacije,relacija.IdLinije from linija join (relacija,prevoznik) "
				+ "on (linija.IdLinije=relacija.IdLinije) and (linija.IdPrevoznika=prevoznik.IdPrevoznika) where (linija.IdLinije=relacija.IdLinije) and (Polaziste=? && Odrediste=?)";
		String sqlQueryMjesecna = "select DaniUSedmici,VrijemePolaska,NazivPrevoznika,Email,prevoznik.Adresa,WEBAdresa,Telefon,prevoznik.PostanskiBroj,NazivLinije,Peron,Polaziste,Odrediste,VrijemeDolaska,CijenaMjesecna,relacija.IdRelacije,relacija.IdLinije from linija join (relacija,prevoznik) "
				+ "on (linija.IdLinije=relacija.IdLinije) and (linija.IdPrevoznika=prevoznik.IdPrevoznika) where (linija.IdLinije=relacija.IdLinije) and (polaziste=? && odrediste=?)";
		try {
			c = Util.getConnection();
			
			if(kupovinaMjesecne) {
				s = c.prepareStatement(sqlQueryMjesecna);
				s.setString(1, polazisteTextField.getText());
				s.setString(2, odredisteTextField.getText());
				r = s.executeQuery();
				while(r.next()) {
					
					Time vrijemePolaska = r.getTime(2);
					daniUSedmici = r.getString(1);
					Prevoznik prevoznik = new Prevoznik(r.getString(3), r.getString(4), r.getString(5), r.getString(6), r.getString(7), "BL");
					Linija linija = new Linija(r.getInt(16),r.getString(9), daniUSedmici,r.getInt(10));
					Relacija relacija = new Relacija(r.getInt(15),r.getInt(16),r.getString(11), r.getString(12));
					Karta karta = new Karta(linija, relacija, vrijemePolaska, r.getTime(13), r.getDouble(14), LocalDate.now(), prevoznik, PrijavaController.nalog.getKorisnickoIme(),PrijavaController.nalog.getIdStanice());
					karteObs.add(karta);
					System.out.println("JIB stanice: " + karta.getJIBStanice());
				}
			}
			else {
				s = c.prepareStatement(sqlQuery);
				s.setString(1, InformacijeController.nazivMjesta);
				s.setString(2, odredisteTextField.getText());
				r = s.executeQuery();
			while(r.next()) {	
				daniUSedmici = r.getString(1);
				Time vrijemePolaska = r.getTime(2);
				if(zadovoljavaDatumVrijeme(daniUSedmici,vrijemePolaska)) {
					Prevoznik prevoznik = new Prevoznik(r.getString(3), r.getString(4), r.getString(5), r.getString(6), r.getString(7), "BL");
					Linija linija = new Linija(r.getInt(16),r.getString(9), daniUSedmici,r.getInt(10));
					Relacija relacija = new Relacija(r.getInt(15),r.getInt(16),r.getString(11), r.getString(12));
					Karta karta = new Karta(linija, relacija, vrijemePolaska, r.getTime(13), r.getDouble(14), LocalDate.now(), prevoznik, PrijavaController.nalog.getKorisnickoIme(),PrijavaController.nalog.getIdStanice());
					karteObs.add(karta);
					System.out.println(karta);
					System.out.println("JIB stanice: " + karta.getJIBStanice());

				}
			
			}
			}

			if(karteObs.isEmpty())
				showPrazanSetAlert();
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		finally {
			Util.close(r, s, c);
		}
		
	}
	
	public void showPrazanSetAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Obavjestenje");
		alert.setHeaderText("Nema linija");
		alert.setContentText("Za odabrani datum i destinaciju (ili relacije) nema linija.");
		alert.showAndWait();

	}


	public boolean zadovoljavaDatumVrijeme(String daniUSedmici,Time vrijemePolaska) {
		LocalTime localTime = LocalTime.now();
		if(datum.getValue().equals(LocalDate.now())) {
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
		String sql = "select PostanskiBroj from mjesto where Naziv=?";
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
		finally {
			Util.close(r, s, c);
		}
		return 0;
	}

	@FXML
	public void kupovina() {
		brojKarataZaKupovinu = Integer.parseInt(brojKarataTextField.getText());
		if(50-provjeriBrojKarata()<brojKarataZaKupovinu) {
			showNedovoljnoMjesta();
			return;
		}
		if(showPotvrda()) {
				Connection c = null;
				PreparedStatement s = null;
				for(int i=0;i<brojKarataZaKupovinu;++i) {
					int brojKarata = provjeriBrojKarata();
					System.out.println("Broj karata: " + brojKarata);
					String sqlQuery = "insert into karta value (DEFAULT,?,?,?,?,?)";
					String sqlMjesecna = "insert into karta value (DEFAULT,?,?,DEFAULT,?,?)";
					try {
						c = Util.getConnection();
						if(kupovinaMjesecne) {
							Karta karta = linijeTable.getSelectionModel().getSelectedItem();
							  s = c.prepareStatement(sqlMjesecna,
                                     Statement.RETURN_GENERATED_KEYS);
							s.setInt(1, karta.getRelacija().getIdRelacije());
							s.setDate(2, Date.valueOf(datum.getValue()));
							s.setInt(3, karta.getJIBStanice());
							s.setDouble(4, karta.getCijena());
							s.executeUpdate();
							
							 try (ResultSet generatedKeys = s.getGeneratedKeys()) {
						            if (generatedKeys.next()) {
										dodajMjesecnuKartu(generatedKeys.getInt(1));

						            }
						            else {
						                throw new SQLException("Creating user failed, no ID obtained.");
						            }
						        }
						}
						else {
							Karta k = linijeTable.getSelectionModel().getSelectedItem();
							System.out.println(k);
							s = c.prepareStatement(sqlQuery,Statement.RETURN_GENERATED_KEYS);
							s.setInt(1, k.getRelacija().getIdRelacije());
							s.setDate(2, Date.valueOf(datum.getValue()));
							s.setInt(3, brojKarata+1);
							s.setInt(4, k.getJIBStanice());
							s.setDouble(5, k.getCijena());
							s.executeUpdate();
							if(rezervacijaCheckBox.isSelected()) {
								try (ResultSet generatedKeys = s.getGeneratedKeys()) {
						            if (generatedKeys.next()) {
										dodajRezervaciju(generatedKeys.getInt(1));
										System.out.println("SERial zadnje: " + generatedKeys.getInt(1));
						            }
						            else {
						                throw new SQLException("Creating user failed, no ID obtained.");
						            }
						        }
							}
						}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						finally {
							Util.close(s, c);
						}
					// ISPIS KARTE
			
					
					// ispis
						
				
			}
			
			
		}			
	}

	private void dodajRezervaciju(int serijskiBrojKarte) {
		// TODO Auto-generated method stub
		Connection c= null;
		PreparedStatement s = null;
		String sqlQuery = "insert into rezervacija value (default,?,?,?,?)";
		try {
			c = Util.getConnection();
			s = c.prepareStatement(sqlQuery);
			s.setDate(1, Date.valueOf(LocalDate.now()));
			s.setString(2, imeTextField.getText() + " " + prezimeTextField.getText());
			s.setString(3, brojTelefonaTextField.getText());
			s.setInt(4, serijskiBrojKarte);
			System.out.println("Ser. broj: " + serijskiBrojKarte);
			s.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public String getNazivMjesta() {
		// TODO Auto-generated method stub
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select mjesto.Naziv from mjesto join autobuska_stanica\n" + 
				"on (mjesto.PostanskiBroj=autobuska_stanica.PostanskiBroj) \n" + 
				"where (autobuska_stanica.IdStanice=?)";
		try {
			c = Util.getConnection();
			s = c.prepareStatement(sql);
			s.setInt(1, PrijavaController.nalog.getIdStanice());
			r = s.executeQuery();
			while(r.next()) {
				return r.getString(1);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(r, s, c);
		}
		return null;
	}
	
	public void dodajMjesecnuKartu(int i) {
		// TODO Auto-generated method stub
		Connection c= null;
		PreparedStatement s = null;
		Karta k = linijeTable.getSelectionModel().getSelectedItem();
		String sqlQuery = "insert into mjesecna_karta value (default,?,?,?,?,?,?)";
		try {
			c = Util.getConnection();
			s = c.prepareStatement(sqlQuery);
			s.setDouble(1, k.getCijena());
			s.setString(2, imeTextField.getText());
			s.setString(3, prezimeTextField.getText());
			s.setString(4, odabranaSlika.getAbsolutePath());
			s.setString(5, tipKarteComboBox.getValue().toString());
			s.setInt(6, i);
			s.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(s, c);
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
		FileChooser fc  = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter("PNG Files", "*.png"));
	    odabranaSlika = fc.showOpenDialog(null);
	    System.out.println(odabranaSlika.getAbsolutePath());
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
		String sqlQuery = "select count(*) from karta join (relacija,linija) on (karta.IdRelacije=relacija.IdRelacije) and (relacija.IdLinije=linija.IdLinije) "
				+ "where (linija.IdLinije=?) and (karta.Datum=?)";
		try {
			c = Util.getConnection();
			s = c.prepareStatement(sqlQuery);
			s.setInt(1, linijeTable.getSelectionModel().getSelectedItem().getLinija().getIdLinije());
			s.setDate(2, Date.valueOf(datum.getValue()));
			r = s.executeQuery();
			if(r.next())
				return r.getInt(1);
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		}
		finally {
			Util.close(r, s, c);
		}
		return 0;
	}
}
