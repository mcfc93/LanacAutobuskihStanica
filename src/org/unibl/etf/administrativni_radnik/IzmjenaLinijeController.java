package org.unibl.etf.administrativni_radnik;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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

import org.controlsfx.control.MaskerPane;
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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
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
	private JFXComboBox<String> vremenaPolazaka;
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
		relacijeTableView.setItems(relacijeObsList);

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


		
		vremenaPolazaka.setItems(Relacija.getVremenaPolazaka(ListaLinijaController.odabranaLinija.getpStajaliste(), ListaLinijaController.odabranaLinija.getoStajaliste()));
		
		vremenaPolazaka.valueProperty().addListener((observable,oldValue,newValue) -> {
			relacijeObsList.clear();
			relacijeObsList.setAll(Relacija.getRelacije(ListaLinijaController.odabranaLinija.getIdLinije(), vremenaPolazaka.getSelectionModel().getSelectedIndex()+1));
			relacijeTableView.getSelectionModel().selectFirst();
			//relacijeTableView.refresh();
			vrijemePolaska1TimePicker.setValue(LocalTime.parse(vremenaPolazaka.getValue().split(" - ")[0], DateTimeFormatter.ofPattern("HH:mm")));
			vrijemePolaska2TimePicker.setValue(LocalTime.parse(vremenaPolazaka.getValue().split(" - ")[1], DateTimeFormatter.ofPattern("HH:mm")));
			postavljanjeCheckBox(relacijeTableView.getSelectionModel().getSelectedItem().getDani());
			System.out.println(relacijeTableView.getSelectionModel().getSelectedItem().getDani());

		});
		
		vremenaPolazaka.getSelectionModel().selectFirst();
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
					Relacija odabranaRelacija = relacijeObsList.get((int) newValue);
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
				if(newValue != null) {
					relacijeObsList.get(0).setVrijemePolaska(Time.valueOf(vrijemePolaska1TimePicker.getValue()));
				LocalTime vrijemeDolaskaPlusHours = relacijeObsList.get(0).getVrijemePolaska().toLocalTime().plusHours(relacijeObsList.get(0).getDuzinaPuta().getHour());
				LocalTime vrijemeDolaskaPlusMinutes = vrijemeDolaskaPlusHours.plusMinutes(relacijeObsList.get(0).getDuzinaPuta().getMinute());
				relacijeObsList.get(0).setVrijemeDolaska(Time.valueOf(vrijemeDolaskaPlusMinutes));
				for(int i=1; i< UnosRelacijaController.brojRelacija; ++i) {
					final int x = i;
					LocalTime vrijemePolaska = relacijeObsList.stream().filter(r -> r.getOdrediste().equals(relacijeObsList.get(x).getPolaziste())).findFirst().get().getVrijemeDolaska().toLocalTime();
					relacijeObsList.get(x).setVrijemePolaska(Time.valueOf(vrijemePolaska));
					LocalTime vrijemePolaskaPlusHours = vrijemePolaska.plusHours(relacijeObsList.get(x).getDuzinaPuta().getHour());
					LocalTime vrijemePolaskaPlusMinutes = vrijemePolaskaPlusHours.plusMinutes(relacijeObsList.get(x).getDuzinaPuta().getMinute());
					relacijeObsList.get(x).setVrijemeDolaska(Time.valueOf(vrijemePolaskaPlusMinutes));
				}
				for(int i=UnosRelacijaController.brojRelacija;i<relacijeObsList.size();++i) {
					final int x = i;
					LocalTime vrijemePolaska = relacijeObsList.stream().filter(r -> r.getPolaziste().equals(relacijeObsList.get(x).getPolaziste())).findFirst().get().getVrijemePolaska().toLocalTime();
					relacijeObsList.get(x).setVrijemePolaska(Time.valueOf(vrijemePolaska));
					LocalTime vrijemePolaskaPlusHours = vrijemePolaska.plusHours(relacijeObsList.get(x).getDuzinaPuta().getHour());
					LocalTime vrijemePolaskaPlusMinutes = vrijemePolaskaPlusHours.plusMinutes(relacijeObsList.get(x).getDuzinaPuta().getMinute());
					relacijeObsList.get(x).setVrijemeDolaska(Time.valueOf(vrijemePolaskaPlusMinutes));
				}
				relacijeTableView.refresh();
				}
			}
		});
		
		vrijemePolaska2TimePicker.valueProperty().addListener(new ChangeListener<LocalTime>() {

			@Override
			public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue,
					LocalTime newValue) {
			
				if(newValue != null) {
				relacijeObsList.get(0).setVrijemePolaskaPovratna(Time.valueOf(vrijemePolaska2TimePicker.getValue()));
				LocalTime vrijemeDolaskaPovratnaPlusHours = relacijeObsList.get(0).getVrijemePolaskaPovratna().toLocalTime().plusHours(relacijeObsList.get(0).getDuzinaPuta().getHour());
				LocalTime vrijemeDolaskaPovratnaPlusMinutes = vrijemeDolaskaPovratnaPlusHours.plusMinutes(relacijeObsList.get(0).getDuzinaPuta().getMinute());
				relacijeObsList.get(0).setVrijemeDolaskaPovratna(Time.valueOf(vrijemeDolaskaPovratnaPlusMinutes));
				for(int i=1; i< UnosRelacijaController.brojRelacija; ++i) {
					final int x = i;
					LocalTime vrijemePolaskaPovratna = relacijeObsList.stream().filter(r -> r.getOdrediste().equals(relacijeObsList.get(x).getPolaziste())).findFirst().get().getVrijemeDolaskaPovratna().toLocalTime();
					relacijeObsList.get(x).setVrijemePolaskaPovratna(Time.valueOf(vrijemePolaskaPovratna));
					LocalTime vrijemePolaskaPovratnaPlusHours = vrijemePolaskaPovratna.plusHours(relacijeObsList.get(x).getDuzinaPuta().getHour());
					LocalTime vrijemePolaskaPovratnaPlusMinutes = vrijemePolaskaPovratnaPlusHours.plusMinutes(relacijeObsList.get(x).getDuzinaPuta().getMinute());
					relacijeObsList.get(x).setVrijemeDolaskaPovratna(Time.valueOf(vrijemePolaskaPovratnaPlusMinutes));
				}
				for(int i=UnosRelacijaController.brojRelacija;i<relacijeObsList.size();++i) {
					final int x = i;
					LocalTime vrijemePolaskaPovratna = relacijeObsList.stream().filter(r -> r.getPolaziste().equals(relacijeObsList.get(x).getPolaziste())).findFirst().get().getVrijemePolaskaPovratna().toLocalTime();
					relacijeObsList.get(x).setVrijemePolaskaPovratna(Time.valueOf(vrijemePolaskaPovratna));
					LocalTime vrijemePolaskaPovratnaPlusHours = vrijemePolaskaPovratna.plusHours(relacijeObsList.get(x).getDuzinaPuta().getHour());
					LocalTime vrijemePolaskaPovratnaPlusMinutes = vrijemePolaskaPovratnaPlusHours.plusMinutes(relacijeObsList.get(x).getDuzinaPuta().getMinute());
					relacijeObsList.get(x).setVrijemeDolaskaPovratna(Time.valueOf(vrijemePolaskaPovratnaPlusMinutes));
				}
				relacijeTableView.refresh();	
				}
			}
		});
		

		


		//relacijeObsList.addAll(Relacija.getRelacije(ListaLinijaController.odabranaLinija.getIdLinije(),ListaLinijaController.odabranaLinija.getpStajaliste(),ListaLinijaController.odabranaLinija.getoStajaliste()));

		
		linijaAktivnaCheckBox.setSelected(ListaLinijaController.odabranaLinija.getStanje().equals("Blokirano")? false: true);
		nazivLinijeTextField.setText(ListaLinijaController.odabranaLinija.getNazivLinije());
		prevoznikComboBox.getSelectionModel().select(ListaLinijaController.odabranaLinija.getPrevoznik());
		peronComboBox.getSelectionModel().select(ListaLinijaController.odabranaLinija.getPeron() - 1);
		prazniciComboBox.getSelectionModel().select(ListaLinijaController.odabranaLinija.getVoznjaPraznikom() - 1);
		
		/*relacijeTableView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue!=null) {
				postavljanjeCheckBox(relacijeTableView.getItems().get((int)newValue).getDani());
			}
		});*/
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
			Util.getNotifications("Gre�ka", "Odaberite bar jedan dan!", "Warning").show();
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
			relacijeTableView.getSelectionModel().getSelectedItem().setCijenaJednokratna(Double.parseDouble(cijenaJednokratnaTextField.getText()));
			
			if(!cijenaMjesecnaTextField.getText().isEmpty()) 
				relacijeTableView.getSelectionModel().getSelectedItem().setCijenaMjesecna(Double.parseDouble(cijenaMjesecnaTextField.getText()));
			else
				relacijeTableView.getSelectionModel().getSelectedItem().setCijenaMjesecna(null);
			
			Relacija novaRelacija = relacijeTableView.getSelectionModel().getSelectedItem();
			
			MaskerPane progressPane = Util.getMaskerPane(anchorPane);
			Task<Void> task = new Task<Void>() {
				
	            @Override
	            protected Void call() /*throws Exception*/ {
	                progressPane.setVisible(true);
	                String dani = mapiranjeDana();
					String dani2 = dani.substring(0, dani.length()-1);
					UnosRelacijaController.relacijeList.forEach(r -> r.setDani(dani2));
					
					if(Relacija.izmijeniRelaciju(novaRelacija)) {
						Platform.runLater(() -> {
							Util.getNotifications("Obavje�tenje", "Relacija izmjenjena.", "Information").show();
							relacijeTableView.refresh();
							
						});
					}
	                return null;
	            }
	            @Override
	            protected void succeeded(){
	                super.succeeded();
	                Platform.runLater(() -> {
	                	progressPane.setVisible(false);
	                	//Util.getNotifications("Obavje�tenje", "Relacije dodane.", "Information").show();
	                });
	            }
	        };
	        new Thread(task).start();
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
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
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
	

	
}
