package org.unibl.etf.administrativni_radnik;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.controlsfx.control.MaskerPane;
import org.unibl.etf.autobuska_stanica.AutobuskaStanica;
import org.unibl.etf.karta.Linija;
import org.unibl.etf.karta.Prevoznik;
import org.unibl.etf.karta.Relacija;
import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.util.Stajaliste;
import org.unibl.etf.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.validation.base.ValidatorBase;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class DodavanjeLinijaController implements Initializable {
	
	public static ObservableList<Prevoznik> prevozniciObs = FXCollections.observableArrayList();
	private ObservableList<Relacija> relacijeObsList = FXCollections.observableArrayList();
	public static Linija linija;
	public static int idLinije;
	
	@FXML
	private AnchorPane anchorPane;
	@FXML
    private GridPane gridPane;
	@FXML
	private TableView<Relacija> relacijeTableView;
	@FXML
	private TableColumn<Relacija, String> polazisteColumn;
	@FXML
	private TableColumn<Relacija, String> odredisteColumn;
	@FXML
	private TableColumn<Relacija, Double> cijenaJednokratnaColumn;
	@FXML
	private TableColumn<Relacija, Double> cijenaMjesecnaColumn;
	@FXML
	private TableColumn<Relacija, Time> vrijemePolaskaColumn;
	@FXML
	private TableColumn<Relacija, Time> vrijemeDolaskaColumn;
	@FXML
	private TableColumn<Relacija, Time> vrijemePolaskaPovratakColumn;
	@FXML
	private TableColumn<Relacija, Time> vrijemeDolaskaPovratakColumn;
	
	
	@FXML
    private JFXTextField nazivLinijeTextField;

    @FXML
    private JFXComboBox<Prevoznik> prevoznikComboBox;

    @FXML
    private JFXComboBox<Integer> peronComboBox;
    
    @FXML
    private JFXComboBox<String> prazniciComboBox;

    @FXML
    private JFXButton dodajLinijuButton;
    
    @FXML
    private JFXButton sljedeciPolazakButton;
    @FXML
    private JFXButton sacuvajButton;
    

    @FXML
    private JFXButton krajUnosaButton;
    
    @FXML
    private JFXCheckBox ponedeljakCheckBox;

    @FXML
    private JFXCheckBox utorakCheckBox;
    
    @FXML
    private JFXCheckBox srijedaCheckBox;

    @FXML
    private JFXCheckBox cetvrtakCheckBox;
    
    @FXML
    private JFXCheckBox petakCheckBox;

    @FXML
    private JFXCheckBox subotaCheckBox;

    @FXML
    private JFXCheckBox nedeljaCheckBox;
    
    @FXML
    private JFXCheckBox odaberiSveCheckBox;
    
    @FXML
    private JFXTimePicker vrijemePolaska1TimePicker;

    @FXML
    private JFXTimePicker vrijemePolaska2TimePicker;

    @FXML
    private JFXComboBox<Integer> zadrzavanjeComboBox;
    
    @FXML
    private JFXTextField cijenaJednokratnaTextField;

    @FXML
    private JFXTextField cijenaMjesecnaTextField;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	UnosRelacijaController.relacijeList.clear();
    	disableRelacija(true);
    	sljedeciPolazakButton.setVisible(false);
    	List<Stajaliste> stajalistaStanicaList = Stajaliste.getStajalistaStanicaList();
        relacijeTableView.setItems(relacijeObsList);
    	zadrzavanjeComboBox.getItems().addAll(0,3,5,10,15,20,25,30);
    	zadrzavanjeComboBox.getSelectionModel().select(2);
		vrijemePolaska1TimePicker.setIs24HourView(true);
		vrijemePolaska1TimePicker.converterProperty().set(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
		vrijemePolaska2TimePicker.setIs24HourView(true);
		vrijemePolaska2TimePicker.converterProperty().set(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
		
		nazivLinijeTextField.getValidators().add(Util.requiredFieldValidator(nazivLinijeTextField));
		prevoznikComboBox.getValidators().add(Util.requiredFieldValidator(prevoznikComboBox));
		peronComboBox.getValidators().add(Util.requiredFieldValidator(peronComboBox));
		prazniciComboBox.getValidators().add(Util.requiredFieldValidator(prazniciComboBox));
		cijenaJednokratnaTextField.getValidators().addAll(Util.requiredFieldValidator(cijenaJednokratnaTextField),Util.doubleValidator(cijenaJednokratnaTextField));
		cijenaMjesecnaTextField.getValidators().add(Util.doubleValidator(cijenaMjesecnaTextField));
		vrijemePolaska1TimePicker.getValidators().add(Util.timeValidator(vrijemePolaska1TimePicker));
		vrijemePolaska2TimePicker.getValidators().add(Util.timeValidator(vrijemePolaska2TimePicker));
		
		//popunjavanje ComboBox
		int brojPerona = AutobuskaStanica.getBrojPeronaStanice();
		for(int i=1; i<=brojPerona; ++i)
			peronComboBox.getItems().add(i);
		prevozniciObs.setAll(Prevoznik.getPrevozniciList());
		prevoznikComboBox.setItems(prevozniciObs);
		prazniciComboBox.getItems().addAll("Ponedjeljak","Utorak","Srijeda","Cetvrtak","Petak","Subota","Nedjelja","Bez izmjene","Ne vozi");
		
		polazisteColumn.setCellValueFactory(new PropertyValueFactory<>("nazivPolazista"));
		odredisteColumn.setCellValueFactory(new PropertyValueFactory<>("nazivOdredista"));
		cijenaJednokratnaColumn.setCellValueFactory(new PropertyValueFactory<>("cijenaJednokratna"));
		cijenaMjesecnaColumn.setCellValueFactory(new PropertyValueFactory<>("cijenaMjesecna"));
		vrijemePolaskaColumn.setCellValueFactory(new PropertyValueFactory<>("vrijemePolaska"));
		vrijemePolaskaPovratakColumn.setCellValueFactory(new PropertyValueFactory<>("vrijemePolaskaPovratna"));
		vrijemeDolaskaColumn.setCellValueFactory(new PropertyValueFactory<>("vrijemeDolaska"));
		vrijemeDolaskaPovratakColumn.setCellValueFactory(new PropertyValueFactory<>("vrijemeDolaskaPovratna"));
		
    	zadrzavanjeComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {

			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				/*
				 * stanicno zadrzavanje u oba smjera na svim relacijama (osim prve) koja za polaziste imaju autobusku stanicu*/

			/*	for(int i=1; i<UnosRelacijaController.brojRelacija; ++i) {
					if(!UnosRelacijaController.relacijeList.get(i).getPolaziste().equals(UnosRelacijaController.relacijeList.get(0).getPolaziste()) // nema zadrzavanja na prvoj stanici
							&& stajalistaStanicaList.contains(UnosRelacijaController.relacijeList.get(i).getPolaziste())) {
						LocalTime vrijemePolaskaPlusPauza = UnosRelacijaController.relacijeList.get(i).getVrijemePolaska().toLocalTime().plusMinutes(zadrzavanjeComboBox.getValue());
						LocalTime vrijemePolaskaPovratnaPlusPauza = UnosRelacijaController.relacijeList.get(i).getVrijemePolaskaPovratna().toLocalTime().plusMinutes(zadrzavanjeComboBox.getValue());
						UnosRelacijaController.relacijeList.get(i).setVrijemePolaska(Time.valueOf(vrijemePolaskaPlusPauza));
						UnosRelacijaController.relacijeList.get(i).setVrijemePolaskaPovratna(Time.valueOf(vrijemePolaskaPovratnaPlusPauza));
						
					}
				}	
				relacijeTableView.refresh();*/
				//UnosRelacijaController.relacijeList.forEach(r -> System.out.println(r.getPolaziste() + " - " + r.getOdrediste() + "=" + r.getDuzinaPuta()));
			}
		});
    	
		relacijeTableView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Relacija odabranaRelacija = UnosRelacijaController.relacijeList.get((int) newValue);
				if(odabranaRelacija.getCijenaJednokratna()==null)
					cijenaJednokratnaTextField.clear();
				else
					cijenaJednokratnaTextField.setText(Double.toString(odabranaRelacija.getCijenaJednokratna()));
				if(odabranaRelacija.getCijenaMjesecna()==null)
					cijenaMjesecnaTextField.clear();
				else
					cijenaMjesecnaTextField.setText(Double.toString(odabranaRelacija.getCijenaMjesecna()));
				
			}
		});
		
		vrijemePolaska1TimePicker.valueProperty().addListener(new ChangeListener<LocalTime>() {

			@Override
			public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue,
					LocalTime newValue) {
				/*
				 * unos vremena polazaka i dolazaka za lancane relacije*/
				
				UnosRelacijaController.relacijeList.get(0).setVrijemePolaska(Time.valueOf(vrijemePolaska1TimePicker.getValue()));
				LocalTime vrijemeDolaskaPlusHours = UnosRelacijaController.relacijeList.get(0).getVrijemePolaska().toLocalTime().plusHours(UnosRelacijaController.relacijeList.get(0).getDuzinaPuta().getHour());
				LocalTime vrijemeDolaskaPlusMinutes = vrijemeDolaskaPlusHours.plusMinutes(UnosRelacijaController.relacijeList.get(0).getDuzinaPuta().getMinute());
				UnosRelacijaController.relacijeList.get(0).setVrijemeDolaska(Time.valueOf(vrijemeDolaskaPlusMinutes));
				for(int i=1; i< UnosRelacijaController.brojRelacija; ++i) {
					final int x = i;
					LocalTime vrijemePolaska = UnosRelacijaController.relacijeList.stream().filter(r -> r.getOdrediste().equals(UnosRelacijaController.relacijeList.get(x).getPolaziste())).findFirst().get().getVrijemeDolaska().toLocalTime();
					UnosRelacijaController.relacijeList.get(x).setVrijemePolaska(Time.valueOf(vrijemePolaska));
					LocalTime vrijemePolaskaPlusHours = vrijemePolaska.plusHours(UnosRelacijaController.relacijeList.get(x).getDuzinaPuta().getHour());
					LocalTime vrijemePolaskaPlusMinutes = vrijemePolaskaPlusHours.plusMinutes(UnosRelacijaController.relacijeList.get(x).getDuzinaPuta().getMinute());
					UnosRelacijaController.relacijeList.get(x).setVrijemeDolaska(Time.valueOf(vrijemePolaskaPlusMinutes));
				}
				/*
				 * unos vremena za medjurelacije*/
				for(int i=UnosRelacijaController.brojRelacija;i<UnosRelacijaController.relacijeList.size();++i) {
					final int x = i;
					LocalTime vrijemePolaska = UnosRelacijaController.relacijeList.stream().filter(r -> r.getPolaziste().equals(UnosRelacijaController.relacijeList.get(x).getPolaziste())).findFirst().get().getVrijemePolaska().toLocalTime();
					UnosRelacijaController.relacijeList.get(x).setVrijemePolaska(Time.valueOf(vrijemePolaska));
					LocalTime vrijemePolaskaPlusHours = vrijemePolaska.plusHours(UnosRelacijaController.relacijeList.get(x).getDuzinaPuta().getHour());
					LocalTime vrijemePolaskaPlusMinutes = vrijemePolaskaPlusHours.plusMinutes(UnosRelacijaController.relacijeList.get(x).getDuzinaPuta().getMinute());
					UnosRelacijaController.relacijeList.get(x).setVrijemeDolaska(Time.valueOf(vrijemePolaskaPlusMinutes));
				}
				relacijeTableView.refresh();					
			}
		});
		
		vrijemePolaska2TimePicker.valueProperty().addListener(new ChangeListener<LocalTime>() {

			@Override
			public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue,
					LocalTime newValue) {
				/* povratni smjer
				 * unos vremena polazaka i dolazaka za lancane relacije*/
				UnosRelacijaController.relacijeList.get(0).setVrijemePolaskaPovratna(Time.valueOf(vrijemePolaska2TimePicker.getValue()));
				LocalTime vrijemeDolaskaPovratnaPlusHours = UnosRelacijaController.relacijeList.get(0).getVrijemePolaskaPovratna().toLocalTime().plusHours(UnosRelacijaController.relacijeList.get(0).getDuzinaPuta().getHour());
				LocalTime vrijemeDolaskaPovratnaPlusMinutes = vrijemeDolaskaPovratnaPlusHours.plusMinutes(UnosRelacijaController.relacijeList.get(0).getDuzinaPuta().getMinute());
				UnosRelacijaController.relacijeList.get(0).setVrijemeDolaskaPovratna(Time.valueOf(vrijemeDolaskaPovratnaPlusMinutes));
				for(int i=1; i< UnosRelacijaController.brojRelacija; ++i) {
					final int x = i;
					LocalTime vrijemePolaskaPovratna = UnosRelacijaController.relacijeList.stream().filter(r -> r.getOdrediste().equals(UnosRelacijaController.relacijeList.get(x).getPolaziste())).findFirst().get().getVrijemeDolaskaPovratna().toLocalTime();
					UnosRelacijaController.relacijeList.get(x).setVrijemePolaskaPovratna(Time.valueOf(vrijemePolaskaPovratna));
					LocalTime vrijemePolaskaPovratnaPlusHours = vrijemePolaskaPovratna.plusHours(UnosRelacijaController.relacijeList.get(x).getDuzinaPuta().getHour());
					LocalTime vrijemePolaskaPovratnaPlusMinutes = vrijemePolaskaPovratnaPlusHours.plusMinutes(UnosRelacijaController.relacijeList.get(x).getDuzinaPuta().getMinute());
					UnosRelacijaController.relacijeList.get(x).setVrijemeDolaskaPovratna(Time.valueOf(vrijemePolaskaPovratnaPlusMinutes));
				}
				/*
				 * unos vremena za medjurelacije*/
				for(int i=UnosRelacijaController.brojRelacija;i<UnosRelacijaController.relacijeList.size();++i) {
					final int x = i;
					LocalTime vrijemePolaskaPovratna = UnosRelacijaController.relacijeList.stream().filter(r -> r.getPolaziste().equals(UnosRelacijaController.relacijeList.get(x).getPolaziste())).findFirst().get().getVrijemePolaskaPovratna().toLocalTime();
					UnosRelacijaController.relacijeList.get(x).setVrijemePolaskaPovratna(Time.valueOf(vrijemePolaskaPovratna));
					LocalTime vrijemePolaskaPovratnaPlusHours = vrijemePolaskaPovratna.plusHours(UnosRelacijaController.relacijeList.get(x).getDuzinaPuta().getHour());
					LocalTime vrijemePolaskaPovratnaPlusMinutes = vrijemePolaskaPovratnaPlusHours.plusMinutes(UnosRelacijaController.relacijeList.get(x).getDuzinaPuta().getMinute());
					UnosRelacijaController.relacijeList.get(x).setVrijemeDolaskaPovratna(Time.valueOf(vrijemePolaskaPovratnaPlusMinutes));
				}
				relacijeTableView.refresh();					
			}
		});
	}
    

	public void disableRelacija(boolean b) {
		vrijemePolaska1TimePicker.setDisable(b);
		vrijemePolaska2TimePicker.setDisable(b);
		sljedeciPolazakButton.setDisable(b);
		krajUnosaButton.setDisable(b);
		sacuvajButton.setDisable(b);
		cijenaJednokratnaTextField.setDisable(b);
		cijenaMjesecnaTextField.setDisable(b);
		zadrzavanjeComboBox.setDisable(b);
		disableCB(b);
	}

	public void disableCB(boolean b) {
		ponedeljakCheckBox.setDisable(b);
		utorakCheckBox.setDisable(b);
		srijedaCheckBox.setDisable(b);
		cetvrtakCheckBox.setDisable(b);
		petakCheckBox.setDisable(b);
		subotaCheckBox.setDisable(b);
		nedeljaCheckBox.setDisable(b);
		odaberiSveCheckBox.setDisable(b);
	}

	@FXML
    void odaberiSveCheckBox(MouseEvent event) {
		if (event.getSource() instanceof JFXCheckBox) {
			if(odaberiSveCheckBox.isSelected()) {
				ponedeljakCheckBox.setSelected(true);
				utorakCheckBox.setSelected(true);
				srijedaCheckBox.setSelected(true);
				cetvrtakCheckBox.setSelected(true);
				petakCheckBox.setSelected(true);
				subotaCheckBox.setSelected(true);
				nedeljaCheckBox.setSelected(true);
			} else {
				ponedeljakCheckBox.setSelected(false);
				utorakCheckBox.setSelected(false);
				srijedaCheckBox.setSelected(false);
				cetvrtakCheckBox.setSelected(false);
				petakCheckBox.setSelected(false);
				subotaCheckBox.setSelected(false);
				nedeljaCheckBox.setSelected(false);
			}
		}
    }
    
    @FXML
    void danCheckBox(MouseEvent event) {
		if (event.getSource() instanceof JFXCheckBox) {
	    	if(allCheckBoxesSelected()) {
            	odaberiSveCheckBox.setSelected(true);
            } else {
            	odaberiSveCheckBox.setSelected(false);
            }
		}
    }
	
	private boolean allCheckBoxesSelected() {
		if(ponedeljakCheckBox.isSelected()
			&& utorakCheckBox.isSelected()
				&& srijedaCheckBox.isSelected()
					&& cetvrtakCheckBox.isSelected()
						&& petakCheckBox.isSelected()
							&& subotaCheckBox.isSelected()
								&& nedeljaCheckBox.isSelected()) {
			return true;
		}
		return false;
	}
	
	public boolean anyCheckBoxSelected() {
		if(ponedeljakCheckBox.isSelected() || utorakCheckBox.isSelected() || srijedaCheckBox.isSelected() 
				|| cetvrtakCheckBox.isSelected() || petakCheckBox.isSelected() || subotaCheckBox.isSelected() || nedeljaCheckBox.isSelected())
			return true;
		else 
			Util.getNotifications("Greška", "Odaberite bar jedan dan.", "Error").show();
		return false;
	}
	@FXML
    void dodajLiniju(ActionEvent event) {
		if(nazivLinijeTextField.validate()
				& prevoznikComboBox.validate()
					& peronComboBox.validate()
						& prazniciComboBox.validate()) {
			linija = new Linija(0, nazivLinijeTextField.getText(), (int)peronComboBox.getValue(), prevoznikComboBox.getValue(), prazniciComboBox.getSelectionModel().getSelectedIndex()+1);
			Util.getNotifications("Obavještenje", "Linija dodana.", "Information").show();
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/unibl/etf/administrativni_radnik/UnosRelacija.fxml"));
	            Parent root1 = (Parent) fxmlLoader.load();
	            Stage stage = new Stage();
	        	stage.setResizable(false);
				stage.initStyle(StageStyle.UNDECORATED);
				stage.initModality(Modality.APPLICATION_MODAL);
	            stage.setTitle(linija.getNazivLinije());
	            stage.setScene(new Scene(root1));  
	            stage.showAndWait();
	            relacijeObsList.setAll(UnosRelacijaController.relacijeList); 
	            relacijeTableView.getSelectionModel().selectFirst();
	            disableRelacija(false);
	            krajUnosaButton.setDisable(true);
				Util.getNotifications("Obavještenje", "Unesite cijene relacija.", "Information").show();
	            relacijeTableView.refresh();
	            vrijemePolaska1TimePicker.setDisable(true);
	            vrijemePolaska2TimePicker.setDisable(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML
    void sledeciPolazak(ActionEvent event) {
		if(anyCheckBoxSelected()
				& vrijemePolaska1TimePicker.validate()
					& vrijemePolaska2TimePicker.validate()) 
		{
			if(showDaLiSteSigurni()) {
				linija.setIdLinije(Linija.dodajLiniju(linija));
				UnosRelacijaController.relacijeList.forEach(r -> r.setLinija(linija));
				String dani = mapiranjeDana();
				String dani2 = dani.substring(0, dani.length()-1);
				UnosRelacijaController.relacijeList.forEach(r -> r.setDani(dani2));
				MaskerPane progressPane = Util.getMaskerPane(anchorPane);
				Task<Void> task = new Task<Void>() {
		            @Override
		            protected Void call() /*throws Exception*/ {
		                progressPane.setVisible(true);
		                for (Relacija r : UnosRelacijaController.relacijeList) {
							System.out.println(Relacija.dodajRelaciju(r));
						}
		                return null;
		            }
		            @Override
		            protected void succeeded(){
		                super.succeeded();
		                progressPane.setVisible(false);
				        Util.getNotifications("Obavještenje", "Relacije dodate.", "Information").show();
		            }
		        };
		        new Thread(task).start();
				krajUnosaButton.setDisable(false);
			}
		}
	}
	
	
	public String mapiranjeDana() {
		String dani = "";
		if(ponedeljakCheckBox.isSelected())
			dani += "1,";
		if(utorakCheckBox.isSelected())
			dani += "2,";
		if(srijedaCheckBox.isSelected())
			dani += "3,";
		if(cetvrtakCheckBox.isSelected())
			dani += "4,";
		if(petakCheckBox.isSelected())
			dani += "5,";
		if(subotaCheckBox.isSelected())
			dani += "6,";
		if(nedeljaCheckBox.isSelected())
			dani += "7,";
		return dani;
	}

	@FXML
	public void sacuvaj(ActionEvent event) {
		if( (cijenaMjesecnaTextField.getText().isEmpty()? true : cijenaMjesecnaTextField.validate())
					& cijenaJednokratnaTextField.validate()) {
			
			UnosRelacijaController.relacijeList.get(relacijeTableView.getSelectionModel().getSelectedIndex()).setCijenaJednokratna(Double.parseDouble(cijenaJednokratnaTextField.getText()));
			if(!cijenaMjesecnaTextField.getText().isEmpty())
				UnosRelacijaController.relacijeList.get(relacijeTableView.getSelectionModel().getSelectedIndex()).setCijenaMjesecna(Double.parseDouble(cijenaMjesecnaTextField.getText()));
			cijenaJednokratnaTextField.resetValidation();
			cijenaMjesecnaTextField.resetValidation();
			relacijeTableView.refresh();
			for(int i=0; i<relacijeTableView.getSelectionModel().getSelectedIndex(); ++i)
				if(UnosRelacijaController.relacijeList.get(i).getCijenaJednokratna()==null) { 
					Util.getNotifications("Obavještenje", "Relacije iznad odabrane nemaju cijene.", "Error").show();
					return;
				}
			
			if(UnosRelacijaController.relacijeList.stream().allMatch(r -> r.getCijenaJednokratna()!=null)) {
				if(showPotvrdaKrajaUnosaCijena()) {
					vrijemePolaska1TimePicker.setDisable(false);
					vrijemePolaska2TimePicker.setDisable(false);
					sljedeciPolazakButton.setVisible(true);
					cijenaJednokratnaTextField.setVisible(false);
					cijenaMjesecnaTextField.setVisible(false);
					sacuvajButton.setVisible(false);
					return;
				}
			}
			
			if(relacijeTableView.getSelectionModel().getSelectedIndex()+1==UnosRelacijaController.relacijeList.size()) {
				if(showPotvrdaKrajaUnosaCijena()) {
					vrijemePolaska1TimePicker.setDisable(false);
					vrijemePolaska2TimePicker.setDisable(false);
					sljedeciPolazakButton.setVisible(true);
					cijenaJednokratnaTextField.setVisible(false);
					cijenaMjesecnaTextField.setVisible(false);
					sacuvajButton.setVisible(false);

				}
			}
			else {
				relacijeTableView.getSelectionModel().selectNext();
			}
	}
	}

	public boolean showPotvrdaKrajaUnosaCijena() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("");
		alert.setHeaderText("Da li ste završili sa unosom cijena?");
		Optional<ButtonType> action = alert.showAndWait();
		return action.get().equals(ButtonType.OK);
	}

	public boolean showDaLiSteSigurni() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("");
		alert.setHeaderText("Da li ste sigurni?");
		Optional<ButtonType> action = alert.showAndWait();
		return action.get().equals(ButtonType.OK);
	}
	@FXML
	void krajUnosa(ActionEvent event) {
		
	}

}
