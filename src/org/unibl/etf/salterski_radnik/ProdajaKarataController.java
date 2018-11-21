package org.unibl.etf.salterski_radnik;

import java.io.File;
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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import org.unibl.etf.karta.Karta;
import org.unibl.etf.karta.MjesecnaKarta;
import org.unibl.etf.karta.TipKarte;
import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.base.ValidatorBase;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


public class ProdajaKarataController implements Initializable {
	
	public static Set<String> relacijeSet = new HashSet<>();
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
	public static int idKarte;
	@FXML
	private ImageView slikaImageView = new ImageView();
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
	private JFXTextField imeTextField = new JFXTextField();
	@FXML
	private JFXTextField prezimeTextField = new JFXTextField();
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
		slikaImageView.setVisible(false);
		kupovinaButton.setDisable(true);
		tipKarteSetUp();
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
    	rezervacijaSetUp();
		povratnaKartaSetUp();
		toggleSetUp();
		tipKarteComboBox.setItems(FXCollections.observableArrayList(TipKarte.values()));
		tipKarteComboBox.setValue(TipKarte.OBICNA);
		radioButtonObicna.setSelected(true);
		radioButtonMjesecna.setToggleGroup(toggleGroup);
		radioButtonObicna.setToggleGroup(toggleGroup);
		
		karteTable.setItems(karteObs);		
		ucitajRelacije();
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
	
		Util.setAutocompleteList(odredisteTextField, relacijeSet);
		Util.setAutocompleteList(polazisteTextField, relacijeSet);
		//autoComplete();    
		validationSetUp();
	}
	

	@FXML
	public void pretragaRelacija() {
		if(radioButtonObicna.isSelected()) {
			if(odredisteTextField.validate()) {
				kupovinaButton.setDisable(false);
				karteObs.clear();
				for (Karta karta : Karta.getKarteList(InformacijeController.nazivMjesta, odredisteTextField.getText())) {
					daniUSedmici = karta.getLinija().getDaniUSedmici();
					if(zadovoljavaDatumVrijeme(daniUSedmici, karta.getVrijemePolaska())) {
						if(povratnaKartaCheckBox.isSelected())
							karta.setCijena(2*karta.getCijena()*0.8);
						karteObs.add(karta);
					}
				}
				if(karteObs.isEmpty())
					showPrazanSetAlert();
			}
		}
		else {
			if(odredisteTextField.validate() & polazisteTextField.validate())
			{
					karteObs.clear();
					if(kupovinaMjesecne) {
						for (Karta karta : Karta.getMjesecneKarteList(polazisteTextField.getText(), odredisteTextField.getText())) {
							daniUSedmici = karta.getLinija().getDaniUSedmici();
							if(karta.getCijena()!=0) {
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
						if(karteObs.isEmpty())
							showPrazanSetAlert();
						else
							kupovinaButton.setDisable(false);

					}
					else {
						for (Karta karta : Karta.getKarteList(InformacijeController.nazivMjesta, odredisteTextField.getText())) {
							daniUSedmici = karta.getLinija().getDaniUSedmici();
							if(zadovoljavaDatumVrijeme(daniUSedmici, karta.getVrijemePolaska())) {
								if(povratnaKartaCheckBox.isSelected())
									karta.setCijena(2*karta.getCijena()*0.8);
								karteObs.add(karta);
							}
						}
						if(karteObs.isEmpty())
							showPrazanSetAlert();
					}
			}
		}
	}
	
	@FXML
	public void kupovina() { 
		if(karteTable.getSelectionModel().getSelectedItem()==null) {
			showOdaberiteLinijuAlert();
			return;
		}
		if(kupovinaMjesecne & odabranaSlika==null) {
			showOdaberiteSliku();
			return;
		}
		brojKarataZaKupovinu = brojKarataComboBox.getValue();
		if(50-provjeriBrojKarata()<brojKarataZaKupovinu) {
			showNedovoljnoMjesta();
			return;
		}
		Karta karta = karteTable.getSelectionModel().getSelectedItem();

			if(radioButtonMjesecna.isSelected()) {
				if(polazisteTextField.validate() & odredisteTextField.validate() & 
					imeTextField.validate() & prezimeTextField.validate())
				{
				if(showPotvrda()) {
					int brojKarata = provjeriBrojKarata();
					Karta.kreirajKartu(karta,brojKarata,datum.getValue());
					karta.setIdKarte(idKarte);
					MjesecnaKarta.kreirajKartu(karta, brojKarata+1, datum.getValue(), imeTextField.getText(),prezimeTextField.getText(),tipKarteComboBox.getValue(),odabranaSlika.getPath());	
					showUspjesnaKupovina();	
					imeTextField.resetValidation();
					
					}
				}
			}
			else {
				if(rezervacijaCheckBox.isSelected()) {
					if(imeTextField.validate() & prezimeTextField.validate() & brojTelefonaTextField.validate()) {
						if(showPotvrda()) {
							for(int i=0;i<brojKarataZaKupovinu;++i) {
								int brojKarata = provjeriBrojKarata();
							Karta.kreirajKartu(karta, brojKarata+1, datum.getValue());
							Karta.kreirajRezervaciju(imeTextField.getText(), prezimeTextField.getText(), brojTelefonaTextField.getText(), idKarte);
							}
							showUspjesnaKupovina();	
						}
					}
				} 
				else {
					if(showPotvrda()) {
						for(int i=0;i<brojKarataZaKupovinu;++i) {
							int brojKarata = provjeriBrojKarata();
						Karta.kreirajKartu(karta, brojKarata+1, datum.getValue());
						}
						showUspjesnaKupovina();	
						imeTextField.resetValidation();
						
					}
				}
			} 
	}
	
	
	public void validationSetUp() {
		// TODO Auto-generated method stub

		ValidatorBase odredisteValidator = new ValidatorBase("Nekorektan unos") {
			@Override
			protected void eval() {
				if(!odredisteTextField.getText().isEmpty() && !relacijeSet.contains(odredisteTextField.getText())) {
					hasErrors.set(true);
				} else {
					hasErrors.set(false);
				}
			}
		};
		ValidatorBase polazisteValidator = new ValidatorBase("Nekorektan unos") {
			@Override
			protected void eval() {
				if(polazisteTextField.getText().isEmpty() | (!polazisteTextField.getText().isEmpty() && !relacijeSet.contains(polazisteTextField.getText()))) {
					hasErrors.set(true);
				} else {
					hasErrors.set(false);
				}
			}
		};
		polazisteTextField.getValidators().addAll(Util.requredFieldValidator(polazisteTextField),polazisteValidator);
		odredisteTextField.getValidators().addAll(Util.requredFieldValidator(odredisteTextField),odredisteValidator);
		imeTextField.getValidators().add(Util.requredFieldValidator(imeTextField));
		prezimeTextField.getValidators().add(Util.requredFieldValidator(prezimeTextField));
		brojTelefonaTextField.getValidators().addAll(Util.requredFieldValidator(brojTelefonaTextField),Util.phoneValidator(brojTelefonaTextField));
	}


	public void toggleSetUp() {
		// TODO Auto-generated method stub
		toggleGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
		    if (newValue == null) {
		        oldValue.setSelected(true);
		    }
		    else
		    	if(newValue.equals(radioButtonObicna)) {
		    		resetValidation();
		    		slikaImageView.setVisible(false);
		    		kupovinaButton.setDisable(true);
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
		    		karteObs.clear();
		    	}
		    	else { // KUPOVINA MJESECNE KARTE
		    		resetValidation();
		    		slikaImageView.setVisible(true);
		    		kupovinaButton.setDisable(true);
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
		    		imeTextField.setVisible(true);
		    		prezimeTextField.setVisible(true);
		    		brojKarataZaKupovinu=1;
		    		rezervacijaCheckBox.setDisable(true);
		        	polazisteTextField.setVisible(true);
		    		kupovinaMjesecne=true;
		    		brojKarataComboBox.setVisible(false);
		    		karteObs.clear();
		    	}
		});	
	}


	public void povratnaKartaSetUp() {
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
	}


	public void rezervacijaSetUp() {
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
	}


	public void resetValidation() {
		// TODO Auto-generated method stub
		polazisteTextField.resetValidation();
		odredisteTextField.resetValidation();
		imeTextField.resetValidation();
		prezimeTextField.resetValidation();
		brojTelefonaTextField.resetValidation();
		
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
	


	public void ucitajRelacije() {
		// TODO Auto-generated method stub
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select distinct Naziv from mjesto";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false);
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
			s = Util.prepareStatement(c, sql, false, odrediste);
			r = s.executeQuery();
			if(r.next()) 
				return r.getInt(1);
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
		if(odabranaSlika!=null) {
			try {
				slikaImageView.setImage(new Image(odabranaSlika.toURI().toString()));
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
		String sql = "select count(*) from karta join (relacija,linija) on (karta.IdRelacije=relacija.IdRelacije) and (relacija.IdLinije=linija.IdLinije) "
				+ "where (linija.IdLinije=?) and (karta.Datum=?) and (karta.Stanje='Aktivno')";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, karteTable.getSelectionModel().getSelectedItem().getLinija().getIdLinije(), Date.valueOf(datum.getValue()));
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
