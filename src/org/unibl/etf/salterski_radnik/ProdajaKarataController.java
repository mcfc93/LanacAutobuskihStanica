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
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;

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
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
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
	private static final int MAX_BROJ_KARATA=10;
	public static boolean povratna=false;
	private static final double POPUST_PENZIONERSKA = 0.75;
	private static final double POPUST_DJACKA = 0.80;
	private static final double POPUST_POVRATNA = 0.85;
	@FXML
	private JFXCheckBox povratnaKartaCheckBox = new JFXCheckBox();
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
	private Label nazivSlikeLabel;
	@FXML
	private ToggleGroup toggleGroup = new ToggleGroup();
	@FXML
	private JFXRadioButton radioButtonObicna;
	@FXML
	private JFXRadioButton radioButtonMjesecna;
	@FXML
	private JFXComboBox<TipKarte> tipKarteComboBox;
	@FXML
	private JFXComboBox<Integer> brojKarataComboBox;
	@FXML
	private DatePicker datum =  new DatePicker();
	@FXML
	private JFXButton pretragaButton = new JFXButton();
	@FXML
	private JFXTextField odredisteTextField = new JFXTextField();
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
	private JFXButton kupovinaButton = new JFXButton();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tipKarteSetUp();
		brojTelefonaTextField.setTextFormatter(new TextFormatter<>(change -> (change.getControlNewText().matches("([0-9][0-9]*)?")) ? change : null));
		nazivSlikeLabel.setVisible(false);
		odaberiSlikuButton.setVisible(false);
		imeTextField.setVisible(false);
		prezimeTextField.setVisible(false);
		tipKarteComboBox.setVisible(false);
		brojTelefonaTextField.setVisible(false);
		for(int i=1;i<=MAX_BROJ_KARATA;++i)
			brojKarataComboBox.getItems().add(i);
		brojKarataComboBox.getSelectionModel().selectFirst();
		brojKarataComboBox.setVisibleRowCount(3);
		polazisteTextField.setVisible(false);
    	rezervacijaCheckBox.setSelected(false);
    	povratnaKartaCheckBox.setSelected(false);
		rezervacijaCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if(newValue) {
			       imeTextField.setVisible(true);
			       prezimeTextField.setVisible(true);
			       brojTelefonaTextField.setVisible(true);
		        }
		        else {
		        	imeTextField.setVisible(false);
				    prezimeTextField.setVisible(false);
				    brojTelefonaTextField.setVisible(false);  
		        }
		    }
		});	
		
		povratnaKartaCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	if(newValue) {
		    		for (Karta karta : karteObs) {
						karta.setCijena( 2* (karta.getCijena() * POPUST_POVRATNA));
		    		}
		    		karteTable.refresh();
		    	}
		    	else
		    		for (Karta karta : karteObs) {
						karta.setCijena(( karta.getCijena() / POPUST_POVRATNA)/2);
					}
		    	karteTable.refresh();
		    }
		});	

		kupovinaButton.disableProperty().bind(Bindings.isEmpty(karteTable.getSelectionModel().getSelectedItems()));
		tipKarteComboBox.setItems(FXCollections.observableArrayList(TipKarte.values()));
		tipKarteComboBox.setValue(TipKarte.OBICNA);
		radioButtonObicna.setSelected(true);
		radioButtonMjesecna.setToggleGroup(toggleGroup);
		radioButtonObicna.setToggleGroup(toggleGroup);
		toggleGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
		    if (newValue == null) {
		        oldValue.setSelected(true);
		    }
		    else
		    	if(newValue.equals(radioButtonObicna)) {
		    		nazivSlikeLabel.setVisible(false);
		    		odaberiSlikuButton.setVisible(false);
		    		imeTextField.setVisible(false);
		    		prezimeTextField.setVisible(false);
		    		tipKarteComboBox.setVisible(false);
		    		brojTelefonaTextField.setVisible(false);
		    		datum.setDisable(false);
		    		odredisteTextField.clear();
		    		povratnaKartaCheckBox.setDisable(false);
		    		imeTextField.clear();
		    		prezimeTextField.clear();
		    		brojTelefonaTextField.clear();
		    		tipKarteComboBox.setValue(TipKarte.OBICNA);
		    		tipKarteComboBox.setDisable(true);
		    		rezervacijaCheckBox.setDisable(false);
		    		polazisteTextField.setVisible(false);
		        	brojKarataComboBox.setVisible(true);
		    		odabranaSlika = null;
		    		brojKarataComboBox.getSelectionModel().selectFirst();
		    		kupovinaMjesecne=false;
		    		kupovinaButton.disableProperty().bind(Bindings.isEmpty(karteTable.getSelectionModel().getSelectedItems()));
		    		karteObs.clear();
		    	}
		    	else { // KUPOVINA MJESECNE KARTE
		    		nazivSlikeLabel.setVisible(true);
		    		odaberiSlikuButton.setVisible(true);
		    		imeTextField.setVisible(true);
		    		prezimeTextField.setVisible(true);
		    		tipKarteComboBox.setVisible(true);		    		
		    		
		    		
		    		brojKarataZaKupovinu=1;
		    		povratnaKartaCheckBox.setSelected(false);
		    		povratnaKartaCheckBox.setDisable(true);	
		    		brojKarataComboBox.getSelectionModel().selectFirst();
		    		tipKarteComboBox.setDisable(false);
		    		polazisteTextField.clear();
		    		odredisteTextField.clear();
		    		imeTextField.clear();
		    		prezimeTextField.clear();
		    		brojTelefonaTextField.clear();
		    		datum.setValue(LocalDate.now());
		    		datum.setDisable(true);
		    		rezervacijaCheckBox.setSelected(false);
		    		brojKarataZaKupovinu=1;
		    		rezervacijaCheckBox.setDisable(true);
		        	polazisteTextField.setVisible(true);
		    		kupovinaMjesecne=true;
		    		brojKarataComboBox.setVisible(false);
		    		karteObs.clear();
		    		kupovinaButton.disableProperty().bind(Bindings.isEmpty(karteTable.getSelectionModel().getSelectedItems()).or(Bindings.createBooleanBinding(
		    			    () -> imeTextField.getText().isEmpty() || prezimeTextField.getText().isEmpty(),
		    			    imeTextField.textProperty(), prezimeTextField.textProperty(),
		    			    tipKarteComboBox.getSelectionModel().selectedItemProperty()
		    			    )));
		    	}
		});	
		
		karteTable.setItems(karteObs);		
		ucitajRelacije();
		System.out.println("aaa");
		karteTable.setPlaceholder(new Label("Odaberite relaciju i datum"));
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



	
	public void tipKarteSetUp() {
		// TODO Auto-generated method stub
		tipKarteComboBox.valueProperty().addListener(new ChangeListener<TipKarte>() {
			@Override
			public void changed(ObservableValue<? extends TipKarte> observable, TipKarte oldValue, TipKarte newValue) {
				// TODO Auto-generated method stub
				if(oldValue==TipKarte.OBICNA) {
					
					if(newValue==TipKarte.DJACKA) 
						for (Karta karta : karteObs) 
							karta.setCijena( karta.getCijena() * POPUST_DJACKA);
						
					else
						if(newValue==TipKarte.PENZIONERSKA) {
							for (Karta karta : karteObs)
								karta.setCijena( karta.getCijena() * POPUST_PENZIONERSKA);
								
					}
					karteTable.refresh();
				}
				else	
					if(oldValue==TipKarte.DJACKA) {
					
						if(newValue==TipKarte.OBICNA)
							for (Karta karta : karteObs) {
								karta.setCijena( karta.getCijena() / POPUST_DJACKA);
							}
						else
							if(newValue==TipKarte.PENZIONERSKA)
								for (Karta karta : karteObs) {
									karta.setCijena( karta.getCijena() / POPUST_DJACKA * POPUST_PENZIONERSKA);
								}
					}
					else
						if(oldValue==TipKarte.PENZIONERSKA) {
							
							if(newValue==TipKarte.DJACKA)
								for (Karta karta : karteObs) {
									karta.setCijena( karta.getCijena() / POPUST_PENZIONERSKA * POPUST_DJACKA);
								}
							else
								if(newValue==TipKarte.OBICNA)
									for (Karta karta : karteObs) {
										karta.setCijena( karta.getCijena() / POPUST_PENZIONERSKA);
									}
						}
				karteTable.refresh();	
			}
		});
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
				+ "on (linija.IdLinije=relacija.IdLinije) and (linija.JIBPrevoznika=prevoznik.JIBPrevoznika) where (linija.Stanje='Aktivno') and (linija.IdLinije=relacija.IdLinije) and (Polaziste=? && Odrediste=?)";
		String sqlQueryMjesecna = "select DaniUSedmici,VrijemePolaska,NazivPrevoznika,Email,prevoznik.Adresa,WEBAdresa,Telefon,prevoznik.PostanskiBroj,NazivLinije,Peron,Polaziste,Odrediste,VrijemeDolaska,CijenaMjesecna,relacija.IdRelacije,relacija.IdLinije from linija join (relacija,prevoznik) "
				+ "on (linija.IdLinije=relacija.IdLinije) and (linija.JIBPrevoznika=prevoznik.JIBPrevoznika) where (linija.Stanje='Aktivno') and (linija.IdLinije=relacija.IdLinije) and (polaziste=? && odrediste=?)";
		try {
			c = Util.getConnection();
			
			if(kupovinaMjesecne) {
				s = c.prepareStatement(sqlQueryMjesecna);
				s.setString(1, polazisteTextField.getText());
				s.setString(2, odredisteTextField.getText());
				r = s.executeQuery();
				while(r.next()) {
					System.out.println("nadjena");
					Time vrijemePolaska = r.getTime(2);
					daniUSedmici = r.getString(1);
					Prevoznik prevoznik = new Prevoznik(r.getString(3), r.getString(4), r.getString(5), r.getString(6), r.getString(7), "BL");
					Linija linija = new Linija(r.getInt(16),r.getString(9), daniUSedmici,r.getInt(10),r.getString(3));
					Relacija relacija = new Relacija(r.getInt(15),r.getInt(16),r.getString(11), r.getString(12));
					Karta karta = new Karta(linija, relacija, vrijemePolaska, r.getTime(13), r.getDouble(14), LocalDate.now(), prevoznik, PrijavaController.nalog.getKorisnickoIme(),"1111111111");
					if(karta.getCijena()==0)
						showNemaUPonudi();
					else {
						switch(tipKarteComboBox.getValue()) {
						case DJACKA:
							karta.setCijena(POPUST_DJACKA * karta.getCijena());
							break;
						case PENZIONERSKA:
							karta.setCijena(POPUST_PENZIONERSKA * karta.getCijena());
							break;
						case OBICNA:
							break;
						}
				
						karteObs.add(karta);
					}
				}
			}
			else {
				s = c.prepareStatement(sqlQuery);
				s.setString(1, InformacijeController.nazivMjesta);
				s.setString(2, odredisteTextField.getText());
				r = s.executeQuery();
			while(r.next()) {	
				System.out.println("nadjena obicna");
				daniUSedmici = r.getString(1);
				Time vrijemePolaska = r.getTime(2);
				if(zadovoljavaDatumVrijeme(daniUSedmici,vrijemePolaska)) {
					Prevoznik prevoznik = new Prevoznik(r.getString(3), r.getString(4), r.getString(5), r.getString(6), r.getString(7), "BL");
					Linija linija = new Linija(r.getInt(16),r.getString(9), daniUSedmici,r.getInt(10),r.getString(3));
					Relacija relacija = new Relacija(r.getInt(15),r.getInt(16),r.getString(11), r.getString(12));
					Karta karta = new Karta(linija, relacija, vrijemePolaska, r.getTime(13), r.getDouble(14), LocalDate.now(), prevoznik, PrijavaController.nalog.getKorisnickoIme(),"1111111111");
					if(povratnaKartaCheckBox.isSelected())
						karta.setCijena( 2*karta.getCijena()*80/100); // 80% DUPLE CIJENE
					System.out.println(karteObs.add(karta));
				}

			}

			if(karteObs.isEmpty())
				showPrazanSetAlert();
			}

			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		finally {
			Util.close(r, s, c);
		}
		
	}
	
	private void showOdaberiteTipKarte() {
		// TODO Auto-generated method stub
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Obavjestenje");
		alert.setHeaderText("Odaberite tip karte.");
		alert.showAndWait();
	}


	private void showNemaUPonudi() {
		// TODO Auto-generated method stub
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Obavjestenje");
		alert.setHeaderText("Nema linija");
		alert.setContentText("Za odabranu relaciju nema mjesecnih karata.");
		alert.showAndWait();
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
		
		if(karteTable.getSelectionModel().getSelectedItem()==null) {
			showOdaberiteLinijuAlert();
			return;
		}
		if(kupovinaMjesecne && odabranaSlika==null) {
			showOdaberiteSliku();
			return;
		}
		brojKarataZaKupovinu = brojKarataComboBox.getValue();
		if(50-provjeriBrojKarata()<brojKarataZaKupovinu) {
			showNedovoljnoMjesta();
			return;
		}
		if(showPotvrda()) {
				Connection c = null;
				PreparedStatement s = null;
				for(int i=0;i<brojKarataZaKupovinu;++i) {
					int brojKarata = provjeriBrojKarata();
					String sqlQuery = "insert into karta value (DEFAULT,?,?,?,?,?)";
					String sqlMjesecna = "insert into karta value (DEFAULT,?,?,DEFAULT,?,?)";
					try {
						c = Util.getConnection();
						if(kupovinaMjesecne) {
							Karta karta = karteTable.getSelectionModel().getSelectedItem();
							  s = c.prepareStatement(sqlMjesecna,
                                     Statement.RETURN_GENERATED_KEYS);
							s.setInt(1, karta.getRelacija().getIdRelacije());
							s.setDate(2, Date.valueOf(datum.getValue()));
							s.setString(3, karta.getJIBStanice());
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
							Karta k = karteTable.getSelectionModel().getSelectedItem();
							s = c.prepareStatement(sqlQuery,Statement.RETURN_GENERATED_KEYS);
							s.setInt(1, k.getRelacija().getIdRelacije());
							s.setDate(2, Date.valueOf(datum.getValue()));
							s.setInt(3, brojKarata+1);
							s.setString(4, k.getJIBStanice());
							s.setDouble(5, k.getCijena());
							s.executeUpdate();
						
							if(rezervacijaCheckBox.isSelected()) {
								try (ResultSet generatedKeys = s.getGeneratedKeys()) {
						            if (generatedKeys.next()) 
										dodajRezervaciju(generatedKeys.getInt(1));
						            
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
			showUspjesnaKupovina();
			
		}			
	}

	public void showUspjesnaKupovina() {
		// TODO Auto-generated method stub
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("USPJEH");
		alert.setHeaderText("Karte napravljene.");
		alert.showAndWait();
	}


	public void showOdaberiteLinijuAlert() {
		// TODO Auto-generated method stub
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("GRESKA");
		alert.setHeaderText("Odaberite liniju iz tabele");
		alert.showAndWait();
	}


	public void showOdaberiteSliku() {
		// TODO Auto-generated method stub
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("GRESKA");
		alert.setHeaderText("Odaberite sliku");
		alert.showAndWait();
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
			s.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ValidatorBase requredFieldValidator(JFXTextField textField) {
    	ValidatorBase requiredFieldValidator = new RequiredFieldValidator();
	    requiredFieldValidator.setMessage("Obavezan unos");
	    requiredFieldValidator.setIcon(new ImageView());
	    requiredFieldValidator.setVisible(false);
	    
	    textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)->{
	        if(!newValue) {
	        	textField.validate();
	        }
	    });
	    
	    textField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue)->{
	        //if(textField.getText().trim().isEmpty()) {
	        	textField.validate();
	        //} else {
	        //	textField.resetValidation();
	        //}
	        
	        	
	    });
	    
	    
	    return requiredFieldValidator;
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
		finally {
			Util.close(r, s, c);
		}
		return null;
	}
	
	public void dodajMjesecnuKartu(int i) {
		// TODO Auto-generated method stub
		Connection c= null;
		PreparedStatement s = null;
		Karta k = karteTable.getSelectionModel().getSelectedItem();
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
	    nazivSlikeLabel.setText("Naziv slike");
		if(odabranaSlika!=null) {
			try {
				nazivSlikeLabel.setText("Naziv slike: " + odabranaSlika.getName());
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
			s.setInt(1, karteTable.getSelectionModel().getSelectedItem().getLinija().getIdLinije());
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
