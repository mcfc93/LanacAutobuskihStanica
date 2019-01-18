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
    private GridPane gridPane;
	
	@FXML
	private TableView<Relacija> relacijeTableView;
	@FXML
	private TableColumn<Relacija, String> polazisteColumn;
	@FXML
	private TableColumn<Relacija,String> odredisteColumn;
	@FXML
	private TableColumn<Relacija,Double> cijenaJednokratnaColumn;
	@FXML
	private TableColumn<Relacija,Double> cijenaMjesecnaColumn;
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
    private JFXButton sledeciPolazakButton;

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
    private JFXTimePicker vrijemeDolaska1TimePicker;
    
    @FXML
    private JFXTimePicker vrijemePolaska2TimePicker;

    @FXML
    private JFXTimePicker vrijemeDolaska2TimePicker;
    
    @FXML
    private JFXComboBox<Integer> zadrzavanjeComboBox;
    
    @FXML
    private JFXTextField cijenaObicneKarteTextField;

    @FXML
    private JFXTextField cijenaMjesecneKarteTextField;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	
    	List<Stajaliste> stajalistaStanicaList = Stajaliste.getStajalistaStanicaList();
    	
    	zadrzavanjeComboBox.getItems().addAll(0,3,5,10,15,20,25,30);
    	zadrzavanjeComboBox.getSelectionModel().select(2);
    	zadrzavanjeComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
    		
			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				// TODO Auto-generated method stub
				System.out.println("old:" + oldValue + ", new: " + newValue);
				//UnosRelacijaController.relacijeList.stream().filter(r -> stajalistaStanicaList.contains(r.getPolaziste())).forEach(r -> r.setDuzinaPuta(LocalTime.of(r.getDuzinaPuta().getHour(), r.getDuzinaPuta().plusMinutes((long)newValue).getMinute())));
				
				relacijeTableView.refresh();
				UnosRelacijaController.relacijeList.forEach(r -> System.out.println(r.getPolaziste() + " - " + r.getOdrediste() + "=" + r.getDuzinaPuta()));
			}
		});
    	
    	krajUnosaButton.setDisable(true);
		vrijemePolaska1TimePicker.setIs24HourView(true);
		vrijemePolaska1TimePicker.converterProperty().set(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
		vrijemeDolaska1TimePicker.setIs24HourView(true);
		vrijemeDolaska1TimePicker.converterProperty().set(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
		vrijemePolaska2TimePicker.setIs24HourView(true);
		vrijemePolaska2TimePicker.converterProperty().set(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
		vrijemeDolaska2TimePicker.setIs24HourView(true);
		vrijemeDolaska2TimePicker.converterProperty().set(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
		
		nazivLinijeTextField.getValidators().add(Util.requiredFieldValidator(nazivLinijeTextField));
		prevoznikComboBox.getValidators().add(Util.requiredFieldValidator(prevoznikComboBox));
		peronComboBox.getValidators().add(Util.requiredFieldValidator(peronComboBox));
		vrijemePolaska1TimePicker.getValidators().add(Util.timeValidator(vrijemePolaska1TimePicker));
		vrijemeDolaska1TimePicker.getValidators().add(Util.timeValidator(vrijemeDolaska1TimePicker));
		vrijemePolaska2TimePicker.getValidators().add(Util.timeValidator(vrijemePolaska2TimePicker));
		vrijemeDolaska2TimePicker.getValidators().add(Util.timeValidator(vrijemeDolaska2TimePicker));
		
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
		
		relacijeTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {
		        System.out.println("nova: " + newSelection);
		        System.out.println("jednokratna: " + newSelection.getCijenaJednokratna());
		        System.out.println("mjesecna:" + newSelection.getCijenaMjesecna());
		    }
		    else
		    	System.out.println("null");
		});
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
	
	@FXML
    void dodajLiniju(ActionEvent event) {
		if(nazivLinijeTextField.validate()
				& prevoznikComboBox.validate()
					& peronComboBox.validate()) {
			linija = new Linija(0, nazivLinijeTextField.getText(), (int)peronComboBox.getValue(), prevoznikComboBox.getValue(), prazniciComboBox.getSelectionModel().getSelectedIndex());
			idLinije = Linija.dodajLiniju(linija);
			linija.setIdLinije(idLinije);
			System.out.println("Id kreirane linije: " +linija.getIdLinije());
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
	            relacijeTableView.setItems(relacijeObsList);
	            relacijeTableView.getItems().get(1).setCijenaJednokratna(25.5);
	            relacijeTableView.getItems().get(1).setCijenaMjesecna(70);
	            relacijeTableView.getSelectionModel().select(1);
	          	           
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML
    void sledeciPolazak(ActionEvent event) {
		if(vrijemePolaska1TimePicker.validate()
				& vrijemeDolaska1TimePicker.validate()
					&vrijemePolaska1TimePicker.validate()
						& vrijemeDolaska1TimePicker.validate()) {
			relacijeTableView.getSelectionModel().select(0);
			relacijeTableView.getSelectionModel().getSelectedItem().setCijenaJednokratna(3.5);
			
			relacijeTableView.refresh();
		}
	}
	
	@FXML
	void krajUnosa(ActionEvent event) {
		
	}

}
