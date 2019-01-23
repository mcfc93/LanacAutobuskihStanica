package org.unibl.etf.salterski_radnik;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.MonthDay;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.controlsfx.control.MaskerPane;
import org.unibl.etf.karta.Karta;
import org.unibl.etf.karta.MjesecnaKarta;
import org.unibl.etf.karta.TipKarte;
import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.util.Praznik;
import org.unibl.etf.util.Stajaliste;
import org.unibl.etf.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class ProdajaKarataController implements Initializable {
	
	public static Set<String> relacijeSet = new HashSet<>();
	public static ObservableList<Karta> karteObs = FXCollections.observableArrayList();
	public static String daniUSedmici;
	public static File odabranaSlika = null;
	public static boolean kupovinaMjesecne=false;
	private int brojKarataZaKupovinu;
	private static final int MAX_BROJ_KARATA=10;
	public static boolean povratna=false;
	private static final double POPUST_POVRATNA = 0.85;
	public static final double REZERVACIJA = 2.5;
	public static final double STANICNA_USLUGA = 0.5;
	public static int idKarte;
	public static int idMjesecneKarte;
	public static boolean potvrda;
	public static boolean produzavanjeKarte;
	//public static int x;
	
	@FXML
	private JFXCheckBox studentskaCheckBox;
	@FXML
	private AnchorPane tableAnchorPane;
	@FXML
	private JFXRadioButton produziMjesecnuRadioButton;
	@FXML
	private GridPane gridPane;
	@FXML
	private JFXRadioButton kupiMjesecnuRadioButton;
	@FXML
	private ToggleGroup toggleGroupMjesecna = new ToggleGroup();
	@FXML
	private AnchorPane anchorPane = new AnchorPane();
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
	//@FXML
	//private ToggleGroup toggleGroup = new ToggleGroup();
	@FXML
	private ToggleGroup tipKarteToggleGroup;
	@FXML
	private JFXRadioButton radioButtonObicna;
	@FXML
	private JFXRadioButton radioButtonMjesecna;
	@FXML
	private JFXComboBox<TipKarte> tipKarteComboBox;
	@FXML
	private JFXComboBox<Integer> brojKarataComboBox;
	@FXML
	private JFXDatePicker datum;
	@FXML
	private JFXButton pretragaButton = new JFXButton();
	@FXML
	private JFXTextField odredisteTextField = new JFXTextField();
	@FXML
	private TableView<Karta> karteTable = new TableView<>();
	@FXML
	private TableColumn<Karta,String> nazivRelacijeColumn = new TableColumn<>();
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
	@FXML
	private JFXTextField serijskiBrojTextField;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		karteObs.clear();
		datum.setValue(LocalDate.now());
		slikaImageView.setVisible(false);
		kupovinaButton.setDisable(true);
		tipKarteSetUp();
		odaberiSlikuButton.setVisible(false);
		imeTextField.setVisible(false);
		serijskiBrojTextField.setVisible(false);
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
		tipKarteComboBox.setValue(TipKarte.OBIČNA);
		radioButtonObicna.setSelected(true);
		//radioButtonMjesecna.setToggleGroup(toggleGroup);
		//radioButtonObicna.setToggleGroup(toggleGroup);
		kupiMjesecnuRadioButton.setVisible(false);
		produziMjesecnuRadioButton.setVisible(false);
		
		karteTable.setItems(karteObs);		
		//ucitajRelacije();
		karteTable.setPlaceholder(new Label("Odaberite relaciju i datum"));
		datum.setValue(LocalDate.now());		
		nazivRelacijeColumn.setCellValueFactory(new PropertyValueFactory<>("nazivRelacije"));
		vrijemePolaskaColumn.setCellValueFactory(new PropertyValueFactory<>("vrijemePolaska"));
		vrijemeDolaskaColumn.setCellValueFactory(new PropertyValueFactory<>("vrijemeDolaska"));
		prevoznikColumn.setCellValueFactory(new PropertyValueFactory<>("nazivPrevoznika"));
		cijenaColumn.setCellValueFactory(new PropertyValueFactory<>("cijena"));
		peronColumn.setCellValueFactory(new PropertyValueFactory<>("peron"));
		/*
		datum.setDayCellFactory(picker -> new DateCell() {
	        public void updateItem(LocalDate date, boolean empty) {
	            super.updateItem(date, empty);
	            LocalDate today = LocalDate.now();
	            setDisable(empty || date.compareTo(today) < 0 );
	        }
	    });
		*/
		Util.setAutocompleteList(odredisteTextField, InformacijeController.stajalistaBezStanica.stream().map(Stajaliste::toString).collect(Collectors.toList()));
		Util.setAutocompleteList(polazisteTextField, InformacijeController.stajalistaBezStanica.stream().map(Stajaliste::toString).collect(Collectors.toList()));
		//autoComplete();    
		validationSetUp();
		
		
		datum.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.compareTo(LocalDate.now()) < 0 );
                for(Praznik p: Praznik.getHolidayList()) {
	                if (MonthDay.from(item).equals(MonthDay.of(p.getMjesec(), p.getDan()))) {
	                    setTooltip(new Tooltip(p.getOpis()));
	                    //setStyle("-fx-background-color: #ff4444;");
	                    setTextFill(Color.RED);
	                }
                }
            }
        });
        datum.setEditable(false);
	}
	

	@FXML
	public void pretragaRelacija(Event event) {
		karteObs.clear();
		if((!event.getSource().equals(odredisteTextField)
				|| (event.getSource().equals(odredisteTextField)
						&& ((KeyEvent)event).getCode().equals(KeyCode.ENTER)))
			&& (!event.getSource().equals(polazisteTextField)
					|| (event.getSource().equals(polazisteTextField)
							&& ((KeyEvent)event).getCode().equals(KeyCode.ENTER)))) {
			//Stajaliste polaziste = InformacijeController.stajalistaList.stream().filter(s -> s.getIdStajalista()==PrijavaController.autobuskaStanica.getIdStajalista()).findFirst().get();
			if(radioButtonObicna.isSelected()) {
				if(odredisteTextField.validate()) {
					Stajaliste odrediste = InformacijeController.stajalistaList.stream().filter(s -> s.toString().equals(odredisteTextField.getText())).findFirst().get();
	
					kupovinaButton.setDisable(false);
					karteObs.clear();
					for (Karta karta : Karta.getKarteList(InformacijeController.pocetnoStajaliste, odrediste)) {
						daniUSedmici = karta.getRelacija().getDani();
						if(Praznik.getHolidayList().stream().noneMatch(p -> p.getDan()==datum.getValue().getDayOfMonth() & p.getMjesec()==datum.getValue().getMonthValue())) 
						{	
							if(zadovoljavaDatumVrijeme(daniUSedmici, karta.getRelacija().getVrijemePolaska())) {
								if(povratnaKartaCheckBox.isSelected())
									karta.getRelacija().setCijenaJednokratna(Double.valueOf(String.format("%.2f", 2*karta.getRelacija().getCijenaJednokratna()*0.8)));
								karteObs.add(karta);
							}
						}
					}
					if(karteObs.isEmpty())
				    	Util.getNotifications("Greška", "Nema linija za odabranu relaciju i dan!", "Error").show();
				}
			}
			else {
				if(odredisteTextField.validate() & polazisteTextField.validate())
				{
					Stajaliste odrediste = InformacijeController.stajalistaList.stream().filter(s -> s.toString().equals(odredisteTextField.getText())).findFirst().get();
	
						karteObs.clear();
						if(kupovinaMjesecne) {
							for (Karta karta : MjesecnaKarta.getMjesecneKarteList(InformacijeController.pocetnoStajaliste, odrediste)) {
								//daniUSedmici = karta.getLinija().getDaniUSedmici();
									switch(tipKarteComboBox.getValue()) {
									case ĐAČKA:
										//karta.getRelacija().setCijenaMjesecna(POPUST_DJACKA * karta.getRelacija().getCijenaMjesecna());
										karta.setCijena(Double.valueOf(String.format("%.2f", karta.getRelacija().getCijenaMjesecna() * karta.getRelacija().getLinija().getPrevoznik().getDjackiPopust())));
										break;
									case PENZIONERSKA:
										karta.setCijena( Double.valueOf(String.format("%.2f", karta.getRelacija().getCijenaMjesecna() * karta.getRelacija().getLinija().getPrevoznik().getPenzionerskiPopust())));
										break;
									case OBIČNA:
										break;
									}
									karteObs.add(karta);
							}
							// set vrijeme polaska na null ako je kupovina mjesecne karte
							karteObs.stream().forEach(k -> { k.setVrijemeDolaska(null); k.setVrijemePolaska(null);});
							
							
							if(karteObs.isEmpty())
						    	Util.getNotifications("Greška", "Nema linija za odabranu relaciju!", "Error").show();
							else
								kupovinaButton.setDisable(false);
						}
						else {
							for (Karta karta : Karta.getKarteList(InformacijeController.pocetnoStajaliste, new Stajaliste(odredisteTextField.getText()))) {
								daniUSedmici = karta.getRelacija().getDani();
								if(zadovoljavaDatumVrijeme(daniUSedmici, karta.getRelacija().getVrijemePolaska())) {
									if(povratnaKartaCheckBox.isSelected())
										karta.getRelacija().setCijenaJednokratna(Double.valueOf(String.format("%.2f", 2*karta.getRelacija().getCijenaJednokratna()*0.8)));
									karteObs.add(karta);
								}
							}
							
							if(karteObs.isEmpty())
						    	Util.getNotifications("Greška", "Nema linija za odabranu relaciju i dan!", "Error").show();
						}
				}
			}
		}
		if(event.getSource().equals(odredisteTextField)
				&& ((KeyEvent)event).getCode().equals(KeyCode.ENTER)
					&& polazisteTextField.getText().isEmpty()) {
			polazisteTextField.resetValidation();
		}
		if(event.getSource().equals(polazisteTextField)
				&& ((KeyEvent)event).getCode().equals(KeyCode.ENTER)
					&& odredisteTextField.getText().isEmpty()) {
			odredisteTextField.resetValidation();
		}
	}
	
	
	@FXML
	public void kupovina() {
		
		Karta karta = karteTable.getSelectionModel().getSelectedItem();
		if(radioButtonMjesecna.isSelected()) {
			// PRODUZAVANJE MJESECNE KARTE
			if(produziMjesecnuRadioButton.isSelected()) {
				if(serijskiBrojTextField.validate()) {
					MjesecnaKartaController.karta = MjesecnaKarta.pronadjiKartu(Integer.parseInt(serijskiBrojTextField.getText()));

					if(MjesecnaKartaController.karta==null) {
				    	Util.getNotifications("Greška", "Pogrešan serijski broj!", "Error").show();
						return;
					}
					MjesecnaKartaController.karta.setJIBStanice(PrijavaController.autobuskaStanica.getJib());

					MjesecnaKartaController.datum = LocalDate.now();

					showPotvrda();
				}
				return;
			}
			// KUPOVINA MJESECNE KARTE
			else {
				if(karteTable.getSelectionModel().getSelectedItem()==null) {
			    	Util.getNotifications("Greška", "Odaberite liniju iz tabele!", "Warning").show();
					return;
				}
				if(odabranaSlika==null) {
			    	Util.getNotifications("Greška", "Odaberite sliku!", "Warning").show();
					return;
				}
				brojKarataZaKupovinu = brojKarataComboBox.getValue();
				karta.setJIBStanice(PrijavaController.autobuskaStanica.getJib());
				if(50-Karta.provjeriBrojKarata(karta, Date.valueOf(datum.getValue()))<brojKarataZaKupovinu) {
			    	Util.getNotifications("Greška", "Nedovoljno slobodnih mjesta u autobusu!", "Warning").show();
					return;
				}
				
				if(polazisteTextField.validate() & odredisteTextField.validate() & 
						imeTextField.validate() & prezimeTextField.validate()) { 
					
					MjesecnaKartaController.karta = new MjesecnaKarta(karta.getRelacija(),imeTextField.getText(),prezimeTextField.getText(),odabranaSlika,karta.getRelacija().getLinija().getPrevoznik().getNaziv(),tipKarteComboBox.getValue());
					// izmjena komentar
					//MjesecnaKartaController.datum = datum.getValue();
					MjesecnaKartaController.datum = LocalDate.now();
					if(showPotvrda()) {
						Util.getNotifications("Obavještenje", "Karte napravljene.", "Information").show();
						imeTextField.resetValidation();		
						return;
					}
				 }
				        
						
				} // iznad ovog je } za kraj if
					
			
		}
		// KUPOVINA OBICNIH KARATA
		else {
			if(karta==null) {
		    	Util.getNotifications("Greška", "Odaberite liniju iz tabele!", "Warning").show();
		    	return;
			}
			karta.setDatumPolaska(Date.valueOf(datum.getValue()));
			brojKarataZaKupovinu = brojKarataComboBox.getValue();

			// REZERVACIJA KARATA
			if(rezervacijaCheckBox.isSelected()) {
				if(imeTextField.validate() & prezimeTextField.validate() & brojTelefonaTextField.validate()) {
					if(showPotvrda()) {
						karta.setRezervacija(true);
						MaskerPane progressPane = Util.getMaskerPane(anchorPane);
						Task<Void> task = new Task<Void>() {
				            @Override
				            protected Void call() /*throws Exception*/ {
								karta.setPovratna(povratnaKartaCheckBox.isSelected());
				                progressPane.setVisible(true);
				                for(int i=0;i<brojKarataZaKupovinu;++i) {
									int brojKarata = Karta.provjeriBrojKarata(karta, Date.valueOf(datum.getValue()));
								karta.setBrojSjedista(brojKarata+1);
				            	karta.setDatumPolaska(Date.valueOf(datum.getValue()));
								Karta.kreirajKartu(karta, datum.getValue());
								
								//karta.setIdKarte(idKarte);
								karta.setSerijskiBroj(idKarte);
								karta.setBrojSjedista(brojKarata+1);
								karta.setRezervacija(true);
								karta.stampajKartu();
								Karta.kreirajRezervaciju(imeTextField.getText(), prezimeTextField.getText(), brojTelefonaTextField.getText(), idKarte);
								}
				               return null;
				            }
				            @Override
				            protected void succeeded(){
				                super.succeeded();
				                progressPane.setVisible(false);
						    	Util.getNotifications("Obavještenje", "Karte napravljene.", "Information").show();
				            }
				        };
				        new Thread(task).start();
					}
				}
			}
			
			else {
				// KUPOVINA KARATA
				if(showPotvrda()) {
					MaskerPane progressPane = Util.getMaskerPane(anchorPane);
					Task<Void> task = new Task<Void>() {
			            @Override
			            protected Void call() /*throws Exception*/ {
			                progressPane.setVisible(true);
							for(int i=0;i<brojKarataZaKupovinu;++i) {
								int brojKarata = Karta.provjeriBrojKarata(karta, Date.valueOf(datum.getValue()));
								karta.setDatumPolaska(Date.valueOf(datum.getValue()));
								karta.setBrojSjedista(brojKarata+1);
								karta.setPovratna(povratnaKartaCheckBox.isSelected());
								Karta.kreirajKartu(karta, datum.getValue());
								karta.setSerijskiBroj(idKarte);
								karta.stampajKartu();
							}
			                return null;
			            }
			            @Override
			            protected void succeeded(){
			                super.succeeded();
			                progressPane.setVisible(false);
					    	Util.getNotifications("Obavještenje", "Karte napravljene.", "Information").show();
			            }
			        };
			        new Thread(task).start();
					imeTextField.resetValidation();
					
				}
			}
		}
	}
/*
	public void showPogresanSerijskiBroj() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("GRESKA");
		alert.setHeaderText("Ne postoji karta sa trazenim serijskim brojem!");
		alert.showAndWait();
	}
*/
	public void validationSetUp() {
		polazisteTextField.getValidators().addAll(Util.requiredFieldValidator(polazisteTextField),Util.collectionValidator(polazisteTextField, InformacijeController.stajalistaBezStanica.stream().map(Stajaliste::toString).collect(Collectors.toList()), true, "Unesite polaziste"));
		odredisteTextField.getValidators().addAll(Util.requiredFieldValidator(odredisteTextField),Util.collectionValidator(odredisteTextField, InformacijeController.stajalistaBezStanica.stream().map(Stajaliste::toString).collect(Collectors.toList()), true, "Unesite odrediste"));
		imeTextField.getValidators().addAll(Util.requiredFieldValidator(imeTextField), Util.nameValidator(imeTextField), Util.lengthValidator(imeTextField, 35));
		prezimeTextField.getValidators().addAll(Util.requiredFieldValidator(prezimeTextField), Util.nameValidator(prezimeTextField), Util.lengthValidator(prezimeTextField, 35));
		brojTelefonaTextField.getValidators().addAll(Util.requiredFieldValidator(brojTelefonaTextField),Util.phoneValidator(brojTelefonaTextField));
		serijskiBrojTextField.getValidators().addAll(Util.requiredFieldValidator(serijskiBrojTextField), Util.serialValidator(serijskiBrojTextField));
	}
	


	public void toggleSetUp() {
		toggleGroupMjesecna.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
		    if (newValue == null) {
		        //oldValue.setSelected(true);
		        produziMjesecnuRadioButton.setSelected(true);
		    }
		    else
		    	if(newValue.equals(produziMjesecnuRadioButton)) {
		    		/*
		    		 * novi dio, vjerovatno ne radi*/
		    		cijenaColumn.setCellValueFactory(new PropertyValueFactory<>("cijena"));
		    		karteTable.refresh();
		    		serijskiBrojTextField.clear();
		    		serijskiBrojTextField.resetValidation();
		    		polazisteTextField.clear();
		    		odredisteTextField.clear();
		    		polazisteTextField.resetValidation();
		    		odredisteTextField.resetValidation();
		    		//pretragaButton.setDisable(true);
		    		pretragaButton.setVisible(false);
		    		odaberiSlikuButton.setVisible(false);
		    		slikaImageView.setVisible(false);
		    		//polazisteTextField.setDisable(true);
		    		polazisteTextField.setVisible(false);
		    		//odredisteTextField.setDisable(true);
		    		odredisteTextField.setVisible(false);
		    		produzavanjeKarte = true;
		    		serijskiBrojTextField.setVisible(true);
		    		imeTextField.setVisible(false);
		    		prezimeTextField.setVisible(false);
		    		tipKarteComboBox.setVisible(false);
		    		kupovinaButton.setDisable(false);
		    		karteTable.getItems().clear();
		    		karteTable.setVisible(false);
		    	}
		    	else { // KUPOVINA MJESECNE KARTE
		    		/*
		    		 * novi dio, vjerovatno ne radi*/
		    		cijenaColumn.setCellValueFactory(new PropertyValueFactory<>("cijena"));
		    		karteTable.refresh();
		    		imeTextField.clear();
		    		imeTextField.resetValidation();
		    		prezimeTextField.clear();
		    		prezimeTextField.resetValidation();
		    		tipKarteComboBox.getSelectionModel().selectLast();
		    		//pretragaButton.setDisable(false);
		    		pretragaButton.setVisible(true);
		    		slikaImageView.setVisible(true);
		    		odaberiSlikuButton.setVisible(true);
		    		produzavanjeKarte = false;
		    		//polazisteTextField.setDisable(false);
		    		polazisteTextField.setVisible(true);
		    		//odredisteTextField.setDisable(false);
		    		odredisteTextField.setVisible(true);
		    		serijskiBrojTextField.setVisible(false);
		    		imeTextField.setVisible(true);
		    		prezimeTextField.setVisible(true);
		    		tipKarteComboBox.setVisible(true);
		    		kupovinaButton.setDisable(true);
		    		polazisteTextField.clear();
		    		odredisteTextField.clear();
		    		polazisteTextField.resetValidation();
		    		odredisteTextField.resetValidation();
		    		karteTable.setVisible(true);
		    	}
		});	
		    	
		
		/*toggleGroup*/tipKarteToggleGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
		    if (newValue == null) {
		        oldValue.setSelected(true);
		    }
		    else
		    	if(newValue.equals(radioButtonObicna)) {
		    		studentskaCheckBox.setVisible(true);
		    		studentskaCheckBox.setSelected(false);

		    		
		    		kupiMjesecnuRadioButton.setVisible(false);
		    		produziMjesecnuRadioButton.setVisible(false);
		    		resetValidation();
		    		slikaImageView.setVisible(false);
		    		kupovinaButton.setDisable(true);
		    		odaberiSlikuButton.setVisible(false);
		    		imeTextField.setVisible(false);
		    		prezimeTextField.setVisible(false);
		    		tipKarteComboBox.setVisible(false);
		    		brojTelefonaTextField.setVisible(false);
		    		//datum.setDisable(false);
		    		datum.setVisible(true);
		    		odredisteTextField.clear();
		    		//povratnaKartaCheckBox.setDisable(false);
		    		povratnaKartaCheckBox.setVisible(true);
		    		imeTextField.clear();
		    		prezimeTextField.clear();
		    		brojTelefonaTextField.clear();
		    		tipKarteComboBox.setValue(TipKarte.OBIČNA);
		    		tipKarteComboBox.setDisable(true);
		    		//rezervacijaCheckBox.setDisable(false);
		    		rezervacijaCheckBox.setVisible(true);
		    		polazisteTextField.setVisible(false);
		    		serijskiBrojTextField.setVisible(false);
		    		odredisteTextField.setVisible(true);
		    		pretragaButton.setVisible(true);
		        	brojKarataComboBox.setVisible(true);
		    		odabranaSlika = null;
		    		brojKarataComboBox.getSelectionModel().selectFirst();
		    		kupovinaMjesecne=false;
		    		karteObs.clear();
		    		karteTable.setVisible(true);
		    	}
		    	else { // KUPOVINA MJESECNE KARTE
		    		studentskaCheckBox.setVisible(false);
		    		studentskaCheckBox.setSelected(false);
		    		
		    		kupiMjesecnuRadioButton.setVisible(true);
		    		produziMjesecnuRadioButton.setVisible(true);
		    		resetValidation();
		    		slikaImageView.setVisible(true);
		    		kupovinaButton.setDisable(true);
		    		odaberiSlikuButton.setVisible(true);
		    		imeTextField.setVisible(true);
		    		prezimeTextField.setVisible(true);
		    		tipKarteComboBox.setVisible(true);		    		
		    		brojKarataZaKupovinu=1;
		    		povratnaKartaCheckBox.setSelected(false);
		    		//povratnaKartaCheckBox.setDisable(true);
		    		povratnaKartaCheckBox.setVisible(false);	
		    		brojKarataComboBox.getSelectionModel().selectFirst();
		    		tipKarteComboBox.setDisable(false);
		    		polazisteTextField.clear();
		    		odredisteTextField.clear();
		    		imeTextField.clear();
		    		prezimeTextField.clear();
		    		brojTelefonaTextField.clear();
		    		datum.setValue(LocalDate.now());
		    		//datum.setDisable(true);
		    		datum.setVisible(false);
		    		rezervacijaCheckBox.setSelected(false);
		    		imeTextField.setVisible(true);
		    		prezimeTextField.setVisible(true);
		    		brojKarataZaKupovinu=1;
		    		//rezervacijaCheckBox.setDisable(true);
		    		rezervacijaCheckBox.setVisible(false);
		        	polazisteTextField.setVisible(true);
		        	kupiMjesecnuRadioButton.setSelected(true);
		    		kupovinaMjesecne=true;
		    		brojKarataComboBox.setVisible(false);
		    		karteObs.clear();
		    		karteTable.setVisible(true);
		    	}
		});	
	}


	public void povratnaKartaSetUp() {
		povratnaKartaCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	if(newValue) {
		    		for (Karta karta : karteObs) {
						karta.getRelacija().setCijenaJednokratna( Double.valueOf(String.format("%.2f", 2* (karta.getRelacija().getCijenaJednokratna() * POPUST_POVRATNA))));
		    		}
		    		karteTable.refresh();
		    	}
		    	else
		    		for (Karta karta : karteObs) {
						karta.getRelacija().setCijenaJednokratna(Double.valueOf(String.format("%.2f", ( karta.getRelacija().getCijenaJednokratna() / POPUST_POVRATNA)/2)));
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
		        	imeTextField.clear();
		        	imeTextField.resetValidation();
		        	prezimeTextField.clear();
		        	prezimeTextField.resetValidation();
		        	brojTelefonaTextField.clear();
		        	brojTelefonaTextField.resetValidation();
		        	
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
		polazisteTextField.clear();
		odredisteTextField.clear();
		polazisteTextField.resetValidation();
		odredisteTextField.resetValidation();
		imeTextField.resetValidation();
		prezimeTextField.resetValidation();
		brojTelefonaTextField.resetValidation();
		
	}

	public void tipKarteSetUp() {
		tipKarteComboBox.valueProperty().addListener(new ChangeListener<TipKarte>() {
			@Override
			public void changed(ObservableValue<? extends TipKarte> observable, TipKarte oldValue, TipKarte newValue) {
				if(oldValue==TipKarte.OBIČNA) {
					
					if(newValue==TipKarte.ĐAČKA)  {
						for (Karta karta : karteObs) 
							karta.setCijena(Double.valueOf(String.format("%.2f",  karta.getRelacija().getCijenaMjesecna() * karta.getRelacija().getLinija().getPrevoznik().getDjackiPopust())));

					}
					else
						if(newValue==TipKarte.PENZIONERSKA) {
							for (Karta karta : karteObs)
								karta.setCijena(Double.valueOf(String.format("%.2f",  karta.getRelacija().getCijenaMjesecna() * karta.getRelacija().getLinija().getPrevoznik().getPenzionerskiPopust())));

					}
					karteTable.refresh();
				}
				else	
					if(oldValue==TipKarte.ĐAČKA) {
					
						if(newValue==TipKarte.OBIČNA)
							for (Karta karta : karteObs) {
								karta.getRelacija().setCijenaMjesecna(Double.valueOf(String.format("%.2f",  karta.getCijena() / karta.getRelacija().getLinija().getPrevoznik().getDjackiPopust())));
							}
						else
							if(newValue==TipKarte.PENZIONERSKA)
								for (Karta karta : karteObs) {
									karta.setCijena(Double.valueOf(String.format("%.2f",  karta.getCijena() / karta.getRelacija().getLinija().getPrevoznik().getDjackiPopust() * karta.getRelacija().getLinija().getPrevoznik().getPenzionerskiPopust())));
								}
					}
					else
						if(oldValue==TipKarte.PENZIONERSKA) {
							
							if(newValue==TipKarte.ĐAČKA)
								for (Karta karta : karteObs) {
									karta.setCijena(Double.valueOf(String.format("%.2f",  karta.getCijena() / karta.getRelacija().getLinija().getPrevoznik().getPenzionerskiPopust() * karta.getRelacija().getLinija().getPrevoznik().getDjackiPopust())));
								}
							else
								if(newValue==TipKarte.OBIČNA)
									for (Karta karta : karteObs) {
										karta.setCijena(Double.valueOf(String.format("%.2f",  karta.getCijena() / karta.getRelacija().getLinija().getPrevoznik().getPenzionerskiPopust())));
									}
						}
				karteTable.refresh();	
			}
		});
	}
	


	/*public void ucitajRelacije() {
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
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		finally {
			Util.close(r, s, c);
		}
	}*/

	public boolean zadovoljavaDatumVrijeme(String daniUSedmici,Time vrijemePolaska) {
		LocalTime localTime = LocalTime.now();
		if(datum.getValue().equals(LocalDate.now())) {
			return (localTime.compareTo(vrijemePolaska.toLocalTime())<0);
			}
		else
			return (daniUSedmici.contains(String.valueOf(datum.getValue().getDayOfWeek().getValue())));
	}
	
	public boolean showPotvrda() {
		if(/*toggleGroup*/tipKarteToggleGroup.getSelectedToggle().equals(radioButtonObicna)) {
			Alert alert=new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Prodaja karte");
			alert.setHeaderText(null);
			alert.setContentText("Da li ste sigurni?");
			alert.getButtonTypes().clear();
		    alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
			Button yesButton=(Button)alert.getDialogPane().lookupButton(ButtonType.YES);
			yesButton.setText("Da");
			yesButton.setDefaultButton(false);
			Button noButton=(Button)alert.getDialogPane().lookupButton(ButtonType.NO);
			noButton.setText("Ne");
			noButton.setDefaultButton(true);
			
			alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/unibl/etf/application.css").toExternalForm());
			alert.getDialogPane().getStyleClass().addAll("alert", "alertDelete");
			
			Optional<ButtonType> action = alert.showAndWait();
			return action.get().equals(ButtonType.YES);
		}
		else {
			try {
		        Parent root = FXMLLoader.load(getClass().getResource("/org/unibl/etf/salterski_radnik/MjesecnaKartaView.fxml"));
				Scene scene = new Scene(root);
				Stage stage=new Stage();
				stage.setScene(scene);
				stage.setResizable(false);
				stage.initStyle(StageStyle.UNDECORATED);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
				return potvrda;
				
	        } catch(Exception e) {
	        	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			}
		}
		return false;	
	}

	public void showMjesecnaPreview() {
		try {
	        Parent root = FXMLLoader.load(getClass().getResource("/org/unibl/etf/salterski_radnik/MjesecnaKartaView.fxml"));
			Scene scene = new Scene(root);
			Stage stage=new Stage();
			stage.setScene(scene);
			stage.setResizable(false);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
        } catch(Exception e) {
        	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
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
				Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			}
		}
	}
	
/*	@FXML
	public void produziKartu(ActionEvent e) {


	}*/

}
