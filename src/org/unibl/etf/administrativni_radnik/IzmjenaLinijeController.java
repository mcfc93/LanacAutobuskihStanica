package org.unibl.etf.administrativni_radnik;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;

import java.util.Locale;
import java.util.ResourceBundle;

import org.unibl.etf.autobuska_stanica.AutobuskaStanica;
import org.unibl.etf.karta.Prevoznik;
import org.unibl.etf.karta.Relacija;
import org.unibl.etf.util.Stajaliste;
import org.unibl.etf.util.Util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;

import java.net.URL;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.FormatStyle;

public class IzmjenaLinijeController implements Initializable {
	
	public static ObservableList<Prevoznik> prevozniciObs = FXCollections.observableArrayList();
	private ObservableList<Relacija> relacijeObsList = FXCollections.observableArrayList();
	
	
	
	@FXML
	private AnchorPane anchorPane;
	
	@FXML
	private AnchorPane menuLine;
	
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
	private JFXCheckBox linijaAktivnaCheckBox;
	
	@FXML
    private JFXTextField nazivLinijeTextField;

    @FXML
    private JFXComboBox<Prevoznik> prevoznikComboBox;

    @FXML
    private JFXComboBox<Integer> peronComboBox;
    
    @FXML
    private JFXComboBox<String> prazniciComboBox;
    
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
    
    private double xOffset=0;
    private double yOffset=0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//DragAndDrop
		menuLine.setOnMousePressed(event -> {
			if(event.getButton().equals(MouseButton.PRIMARY)) {
				Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
				xOffset = stage.getX() - event.getScreenX();
				yOffset = stage.getY() - event.getScreenY();
			}
		});
						
		menuLine.setOnMouseDragged(event -> {
			if(event.getButton().equals(MouseButton.PRIMARY)) {
			   	Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
			    if(!stage.isMaximized()) {
			    	stage.setX(event.getScreenX() + xOffset);
			    	stage.setY(event.getScreenY() + yOffset);
			    	stage.setOpacity(0.8);
			    }
			}
		});
						
		menuLine.setOnMouseReleased(event -> {
			if(event.getButton().equals(MouseButton.PRIMARY)) {
				Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
				stage.setOpacity(1.0);
			}
		});

		relacijeTableView.setItems(relacijeObsList);
    	zadrzavanjeComboBox.getItems().addAll(0,3,5,10,15,20,25,30);
    	zadrzavanjeComboBox.getSelectionModel().select(2);
		vrijemePolaska1TimePicker.setIs24HourView(true);
		vrijemePolaska1TimePicker.converterProperty().set(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
		vrijemePolaska2TimePicker.setIs24HourView(true);
		vrijemePolaska2TimePicker.converterProperty().set(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
		
		nazivLinijeTextField.getValidators().addAll(Util.requiredFieldValidator(nazivLinijeTextField),Util.lengthValidator(nazivLinijeTextField, 200));
		prevoznikComboBox.getValidators().add(Util.requiredFieldValidator(prevoznikComboBox));
		peronComboBox.getValidators().add(Util.requiredFieldValidator(peronComboBox));
		prazniciComboBox.getValidators().add(Util.requiredFieldValidator(prazniciComboBox));
		cijenaJednokratnaTextField.getValidators().addAll(Util.requiredFieldValidator(cijenaJednokratnaTextField),Util.naturalDoubleValidator(cijenaJednokratnaTextField),Util.lengthValidator(cijenaJednokratnaTextField, 10));
		cijenaMjesecnaTextField.getValidators().addAll(/*Util.doubleValidator(cijenaMjesecnaTextField)*/Util.notRequiredDoubleValidator(cijenaMjesecnaTextField),Util.lengthValidator(cijenaMjesecnaTextField, 10));
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
		
		relacijeTableView.setPlaceholder(new Label("Nema relacija za odabranu liniju."));
    	for(TableColumn<?,?> column:relacijeTableView.getColumns()) {
        	column.setReorderable(false);
        	column.setSortable(false);
        }
    	
    	relacijeTableView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(!relacijeTableView.getItems().isEmpty()) {
					Relacija odabranaRelacija = UnosRelacijaController.relacijeList.get((int) newValue);
					relacijeTableView.scrollTo((int)newValue);
					if(odabranaRelacija.getCijenaJednokratna()==null)
						cijenaJednokratnaTextField.clear();
					else
						cijenaJednokratnaTextField.setText(Double.toString(odabranaRelacija.getCijenaJednokratna()));
					if(odabranaRelacija.getCijenaMjesecna()==null)
						cijenaMjesecnaTextField.clear();
					else
						cijenaMjesecnaTextField.setText(Double.toString(odabranaRelacija.getCijenaMjesecna()));
					cijenaJednokratnaTextField.resetValidation();
					cijenaMjesecnaTextField.resetValidation();
				}
			}
		});
    	
    	vrijemePolaska1TimePicker.valueProperty().addListener(new ChangeListener<LocalTime>() {

			@Override
			public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue,
					LocalTime newValue) {
				/*
				 * unos vremena polazaka i dolazaka za lancane relacije*/
				if(newValue != null) {
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
			}
		});
		
		vrijemePolaska2TimePicker.valueProperty().addListener(new ChangeListener<LocalTime>() {

			@Override
			public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue,
					LocalTime newValue) {
				/* povratni smjer
				 * unos vremena polazaka i dolazaka za lancane relacije*/
				if(newValue != null) {
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
			}
		});
		
		
Relacija relacija1 = new Relacija(7, ListaLinijaController.odabranaLinija, new Stajaliste(10, "X"), new Stajaliste(11, "Y"), null, null, 7.5, "1,2,3,4,5,6,7");
Relacija relacija2 = new Relacija(10, ListaLinijaController.odabranaLinija, new Stajaliste(10, "Z"), new Stajaliste(11, "W"), null, null, 7.5, "1,3,5,6,7");
UnosRelacijaController.relacijeList.add(relacija1);
UnosRelacijaController.relacijeList.add(relacija2);
relacijeObsList.addAll(relacija1, relacija2);
System.out.println(ListaLinijaController.odabranaLinija.getPrevoznik());
System.out.println(ListaLinijaController.odabranaLinija.getPrevoznik().getJIBPrevoznika());

		linijaAktivnaCheckBox.setSelected(ListaLinijaController.odabranaLinija.getStanje().equals("Blokirano")? false: true);
		nazivLinijeTextField.setText(ListaLinijaController.odabranaLinija.getNazivLinije());
		prevoznikComboBox.getSelectionModel().select(ListaLinijaController.odabranaLinija.getPrevoznik());
		peronComboBox.getSelectionModel().select(ListaLinijaController.odabranaLinija.getPeron() - 1);
		prazniciComboBox.getSelectionModel().select(ListaLinijaController.odabranaLinija.getVoznjaPraznikom() - 1);
		
		relacijeTableView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue!=null) {
				postavljanjeCheckBox(relacijeTableView.getItems().get((int)newValue).getDani());
			}
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
	
	public boolean anyCheckBoxSelected() {
		if(ponedeljakCheckBox.isSelected() || utorakCheckBox.isSelected() || srijedaCheckBox.isSelected() 
				|| cetvrtakCheckBox.isSelected() || petakCheckBox.isSelected() || subotaCheckBox.isSelected() || nedeljaCheckBox.isSelected())
			return true;
		else 
			Util.getNotifications("Greška", "Odaberite bar jedan dan!", "Warning").show();
		return false;
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
	
	private void postavljanjeCheckBox(String dani) {
		if(dani.contains("1")) {
			ponedeljakCheckBox.setSelected(true);
		} else {
			ponedeljakCheckBox.setSelected(false);
		}
		if(dani.contains("2")) {
			utorakCheckBox.setSelected(true);
		} else {
			utorakCheckBox.setSelected(false);
		}
		if(dani.contains("3")) {
			srijedaCheckBox.setSelected(true);
		} else {
			srijedaCheckBox.setSelected(false);
		}
		if(dani.contains("4")) {
			cetvrtakCheckBox.setSelected(true);
		} else {
			cetvrtakCheckBox.setSelected(false);
		}
		if(dani.contains("5")) {
			petakCheckBox.setSelected(true);
		} else {
			petakCheckBox.setSelected(false);
		}
		if(dani.contains("6")) {
			subotaCheckBox.setSelected(true);
		} else {
			subotaCheckBox.setSelected(false);
		}
		if(dani.contains("7")) {
			nedeljaCheckBox.setSelected(true);
		} else {
			nedeljaCheckBox.setSelected(false);
		}
		if(allCheckBoxesSelected()) {
			odaberiSveCheckBox.setSelected(true);
		} else {
			odaberiSveCheckBox.setSelected(false);
		}
	}
	
	@FXML
	public void sacuvaj(ActionEvent event) {
		if(cijenaMjesecnaTextField.validate()
					& cijenaJednokratnaTextField.validate()) {
			
		}
	}
	
	@FXML
    void sledeciPolazak(ActionEvent event) {
		if(anyCheckBoxSelected()
				& vrijemePolaska1TimePicker.validate()
					& vrijemePolaska2TimePicker.validate()) {
			
		}
	}
	
	@FXML
	void krajUnosa(ActionEvent event) {
		
	}
	
	@FXML
	void close(MouseEvent event) {
		if(event.getButton().equals(MouseButton.PRIMARY)) {
            ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        }
	}
	/*
	@FXML
	void minimize(MouseEvent event) {
		if(event.getButton().equals(MouseButton.PRIMARY)) {
            ((Stage)((Node)event.getSource()).getScene().getWindow()).setIconified(true);
        }
	}
	*/
	@FXML
	void maximize(MouseEvent event) {
		if(event.getButton().equals(MouseButton.PRIMARY)) {
            Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
            if(!stage.isMaximized()) {
                stage.setMaximized(true);
            } else {
                stage.setMaximized(false);
            }
        }
	}
	
	@FXML
	void doubleClick(MouseEvent event) {
		if(event.getButton().equals(MouseButton.PRIMARY)) {
            if(event.getClickCount() > 1) {
                maximize(event);
            }
        }
	}
	
	
	/*
	private static ObservableList<Relacija> relacijeObs = FXCollections.observableArrayList();
	private static ObservableList<Integer> peroniObs = FXCollections.observableArrayList();
	private static List<String> daniUSedmiciList = new ArrayList<>();
	private static String daniString="";
	private int peroni;
	
	@FXML
	private AnchorPane anchorPane = new AnchorPane();
	@FXML
	private ImageView checkMark = new ImageView();
	@FXML
	private JFXComboBox<Integer> peronComboBox = new JFXComboBox<>();
	@FXML
	private JFXCheckBox ponedjeljakCB = new JFXCheckBox();
	@FXML
	private JFXCheckBox utorakCB = new JFXCheckBox();
	@FXML
	private JFXCheckBox srijedaCB = new JFXCheckBox();
	@FXML
	private JFXCheckBox cetvrtakCB = new JFXCheckBox();
	@FXML
	private JFXCheckBox petakCB = new JFXCheckBox();
	@FXML
	private JFXCheckBox subotaCB = new JFXCheckBox();
	@FXML
	private JFXCheckBox nedjeljaCB = new JFXCheckBox();
	@FXML
	private JFXButton potvrdiButton = new JFXButton();
	@FXML
	private JFXButton okButton = new JFXButton();
	@FXML
	private JFXButton otkaziButton = new JFXButton();
	@FXML
	private JFXTimePicker vrijemePolaskaTimeChooser = new JFXTimePicker();
	@FXML
	private JFXTimePicker vrijemeDolaskaTimeChooser = new JFXTimePicker();
	@FXML
	private JFXTextField cijenaJednokratnaTextField = new JFXTextField();
	@FXML
	private JFXTextField nazivLinijeTextField = new JFXTextField();
	@FXML
	private JFXComboBox<Relacija> relacijeComboBox = new JFXComboBox<>();
	@FXML
	private JFXCheckBox linijaAktivnaCB = new JFXCheckBox();
	@FXML
	private TextField polazisteTextField = new TextField();
	@FXML
	private TextField odredisteTextField = new TextField();
	@FXML
	private JFXTextField cijenaMjesecnaTextField = new JFXTextField();
	@FXML
	private ImageView questionMarkImageView = new ImageView(); 
	@FXML
	private ImageView exitImageView = new ImageView();
	private double xOffset=0;
    private double yOffset=0;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		anchorPane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
				xOffset = stage.getX() - event.getScreenX();
				yOffset = stage.getY() - event.getScreenY();
			}
		});
		anchorPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
			   	Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
			    if(!stage.isMaximized()) {
			    	stage.setX(event.getScreenX() + xOffset);
			    	stage.setY(event.getScreenY() + yOffset);
			    	stage.setOpacity(0.8);
			    }
			}
		});
						
		anchorPane.setOnMouseReleased((event) -> {
			Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
			stage.setOpacity(1.0);
		});
		
		
		
		daniString="";
		daniUSedmiciList.clear();
		Tooltip.install(questionMarkImageView, new Tooltip("Ostavite prazno ako nema u ponudi."));
		checkMark.setVisible(false);
		nazivLinijeTextField.setText(ListaLinijaController.odabranaLinija.getNazivLinije());
		relacijeObs.addAll(Relacija.getRelacije(ListaLinijaController.odabranaLinija.getIdLinije()));
		if(!relacijeObs.isEmpty()) {
			/*relacijeComboBox.setItems(relacijeObs);
			relacijeComboBox.getSelectionModel().selectFirst();
			polazisteTextField.setText(relacijeComboBox.getValue().getPolaziste().getNaziv());
			odredisteTextField.setText(relacijeComboBox.getValue().getOdrediste().getNaziv());
			vrijemePolaskaTimeChooser.setValue(relacijeComboBox.getValue().getVrijemePolaska().toLocalTime());
			vrijemeDolaskaTimeChooser.setValue(relacijeComboBox.getValue().getVrijemeDolaska().toLocalTime());
			cijenaJednokratnaTextField.setText(Double.toString(relacijeComboBox.getValue().getCijenaJednokratna()));
			if(relacijeComboBox.getValue().getCijenaMjesecna()==0)
				cijenaMjesecnaTextField.clear();
			else
				cijenaMjesecnaTextField.setText(Double.toString(relacijeComboBox.getValue().getCijenaMjesecna()));*/
/*		}
		peroni = AutobuskaStanica.getBrojPeronaStanice();
		for(int i=1;i<=peroni;++i)
			peroniObs.add(i);
		peronComboBox.setVisibleRowCount(3);
		peronComboBox.setItems(peroniObs);
		peronComboBox.setValue(ListaLinijaController.odabranaLinija.getPeron());
		checkBoxInit();
		validateSetup();

	}
	

	public void validateSetup() {
		cijenaJednokratnaTextField.getValidators().addAll(Util.requiredFieldValidator(cijenaJednokratnaTextField),Util.naturalDoubleValidator(cijenaJednokratnaTextField));
		cijenaMjesecnaTextField.getValidators().add(Util.notRequiredDoubleValidator(cijenaMjesecnaTextField));
	}

	@FXML
	private void otkazi() {
		 Stage stage = (Stage) exitImageView.getScene().getWindow();
		    stage.close();
	}
	
	private void checkBoxInit() {
		
	}
	
	/*public void loadCBListeners() {
		ponedjeljakCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if(newValue) 
		        	daniUSedmiciList.add(DayOfWeek.MONDAY.toString());
		        else 
		        	daniUSedmiciList.remove(DayOfWeek.MONDAY.toString());
		    }
		});
		utorakCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if(newValue) 
		        	daniUSedmiciList.add(DayOfWeek.TUESDAY.toString());
		        else 
		        	daniUSedmiciList.remove(DayOfWeek.TUESDAY.toString());
		    }
		});
		srijedaCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if(newValue) 
		        	daniUSedmiciList.add(DayOfWeek.WEDNESDAY.toString());
		        else 
		        	daniUSedmiciList.remove(DayOfWeek.WEDNESDAY.toString());
		    }
		});
		cetvrtakCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if(newValue) 
		        	daniUSedmiciList.add(DayOfWeek.THURSDAY.toString());
		        else 
		        	daniUSedmiciList.remove(DayOfWeek.THURSDAY.toString());
		    }
		});
		petakCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if(newValue) 
		        	daniUSedmiciList.add(DayOfWeek.FRIDAY.toString());
		        else 
		        	daniUSedmiciList.remove(DayOfWeek.FRIDAY.toString());

		    }
		});
		subotaCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if(newValue) 
		        	daniUSedmiciList.add(DayOfWeek.SATURDAY.toString());
		        else 
		        	daniUSedmiciList.remove(DayOfWeek.SATURDAY.toString());

		    }
		});
		nedjeljaCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if(newValue) 
		        	daniUSedmiciList.add(DayOfWeek.SUNDAY.toString());
		        else 
		        	daniUSedmiciList.remove(DayOfWeek.SUNDAY.toString());

		    }
		});
		
	}*/

/*	public boolean showPotvrda() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("");
		alert.setHeaderText("Da li ste sigurni?");
		Optional<ButtonType> action = alert.showAndWait();
		return action.get().equals(ButtonType.OK);
	}
	

	@FXML
	public void izmjenaLinije() {
		//mapiranjeDana();
		if(showPotvrda()) {
			ListaLinijaController.odabranaLinija.setNazivLinije(nazivLinijeTextField.getText());
			ListaLinijaController.odabranaLinija.setPeron(peronComboBox.getValue());
			if(Linija.izmjeniLiniju(ListaLinijaController.odabranaLinija)) {
				Stage stage = (Stage) okButton.getScene().getWindow();
			    stage.close();
			}
		}
	}

	public void mapiranjeDana() {
		daniString="";
		if(daniUSedmiciList.size()==1) {
			daniString += daniUSedmiciList.get(0);
			return;
		}
		else {
		for(int i=0;i<daniUSedmiciList.size()-1;++i)
			daniString += daniUSedmiciList.get(i) + ",";
		daniString += daniUSedmiciList.get(daniUSedmiciList.size()-1);
		}
	}
	
	@FXML
	public void comboBoxChange(ActionEvent event) {
		polazisteTextField.setText(relacijeComboBox.getValue().getPolaziste().getNaziv());
		odredisteTextField.setText(relacijeComboBox.getValue().getOdrediste().getNaziv());
		vrijemePolaskaTimeChooser.setValue(relacijeComboBox.getValue().getVrijemePolaska().toLocalTime());
		vrijemeDolaskaTimeChooser.setValue(relacijeComboBox.getValue().getVrijemeDolaska().toLocalTime());
		cijenaJednokratnaTextField.setText(Double.toString(relacijeComboBox.getValue().getCijenaJednokratna()));
		
		if(relacijeComboBox.getValue().getCijenaMjesecna()==0)
			cijenaMjesecnaTextField.clear();
		else
			cijenaMjesecnaTextField.setText(Double.toString(relacijeComboBox.getValue().getCijenaMjesecna()));
	}
	
	public boolean vrijemeValidno() {
		return vrijemePolaskaTimeChooser.getValue()!=null && vrijemeDolaskaTimeChooser.getValue()!=null && !vrijemeDolaskaTimeChooser.getValue().equals(vrijemePolaskaTimeChooser.getValue());
	}

	public void showVrijemeNijeValidno() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Greska");
		alert.setHeaderText("Unosi o vremenima polaska i dolaska nisu validni.");
		alert.showAndWait();
	}
	@FXML
	public void izmjeniRelaciju() {
		if(!vrijemeValidno()) {
			showVrijemeNijeValidno();
			return;
		}
		if(cijenaJednokratnaTextField.validate()
				& cijenaMjesecnaTextField.validate())
		{
			if(showPotvrda()) {
				relacijeComboBox.getValue().setCijenaJednokratna(Double.parseDouble(cijenaJednokratnaTextField.getText()));
				//relacijeComboBox.getValue().setCijenaMjesecna(Double.parseDouble(cijenaMjesecnaTextField.getText()));
				relacijeComboBox.getValue().setVrijemePolaska(Time.valueOf(vrijemePolaskaTimeChooser.getValue()));
				relacijeComboBox.getValue().setVrijemeDolaska(Time.valueOf(vrijemeDolaskaTimeChooser.getValue()));
				if(Relacija.izmijeniRelaciju(relacijeComboBox.getValue()))
				{
					relacijeComboBox.getValue().setCijenaJednokratna(Double.parseDouble(cijenaJednokratnaTextField.getText()));
					relacijeComboBox.getValue().setVrijemePolaska(Time.valueOf(vrijemePolaskaTimeChooser.getValue()));
					relacijeComboBox.getValue().setVrijemeDolaska(Time.valueOf(vrijemeDolaskaTimeChooser.getValue()));	
					if(cijenaMjesecnaTextField.getText().isEmpty()) {
						cijenaMjesecnaTextField.clear();
						relacijeComboBox.getValue().setCijenaMjesecna(0);
						cijenaMjesecnaTextField.resetValidation();
					}
					else
						relacijeComboBox.getValue().setCijenaMjesecna(Double.parseDouble(cijenaMjesecnaTextField.getText()));	
					showSuccess();
				}	
		} 
	}
	}
	
	public void showSuccess() {
		checkMark.setVisible(true);
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(3),
		    new EventHandler<ActionEvent>() {
		        @Override
		        public void handle(ActionEvent event) {
		            checkMark.setVisible(false);
		        }
		    }));
		timeline.play();
		
	}
	*/
}
