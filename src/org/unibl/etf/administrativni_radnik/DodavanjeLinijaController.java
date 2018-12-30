package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import org.unibl.etf.autobuska_stanica.AutobuskaStanica;
import org.unibl.etf.karta.Linija;
import org.unibl.etf.karta.Prevoznik;
import org.unibl.etf.karta.Relacija;
import org.unibl.etf.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.validation.base.ValidatorBase;
import com.sun.javafx.collections.ObservableListWrapper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.scene.control.Alert.AlertType;

public class DodavanjeLinijaController implements Initializable {
	/*
	private static List<String> daniUSedmiciList = new ArrayList<>();
	public static ObservableList<Prevoznik> prevozniciObs = FXCollections.observableArrayList();
	public static String daniString="";
	public static ObservableList<Integer> peroni = FXCollections.observableArrayList();
	public static List<String> mjestaList = new ArrayList<>();
	private static int idLinije;

	@FXML
	private ImageView questionMarkImageView = new ImageView();
	@FXML
	private JFXButton gotovoButton = new JFXButton();
	@FXML
	private Label dodajteRelacijeLabel = new Label();
	@FXML
	private JFXButton dodajLinijuButton = new JFXButton();
	@FXML
	private JFXTextField nazivLinijeTextField = new JFXTextField();
	@FXML
	private JFXComboBox<Prevoznik> prevozniciComboBox = new JFXComboBox<>();
	@FXML
	private JFXComboBox<Integer> peroniComboBox = new JFXComboBox<>();
	@FXML
	private JFXButton dodajRelacijuButton = new JFXButton();
	@FXML
	private JFXTextField polazisteTextField = new JFXTextField();
	@FXML
	private JFXTextField odredisteTextField = new JFXTextField();
	@FXML
	private JFXTextField cijenaJednokratnaTextField = new JFXTextField();
	@FXML
	private JFXTextField cijenaMjesecnaTextField = new JFXTextField();
	@FXML
	private JFXTimePicker vrijemePolaska = new JFXTimePicker();
	@FXML
	private JFXTimePicker vrijemeDolaska = new JFXTimePicker();
	@FXML
	private ImageView checkMarkImageView = new ImageView();
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
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		questionMarkImageView.setPickOnBounds(true);
		Tooltip.install(questionMarkImageView, new Tooltip("Ostavite prazno ako nema u ponudi."));
		dodajRelacijuButton.setDisable(true);
		checkMarkImageView.setVisible(false);
		gotovoButton.setVisible(false);
		relacijeDisable(true);
		ucitajMjesta();
		checkBoxInit();
		loadCBListeners();
		dodajteRelacijeLabel.setVisible(false);
		int brojPerona = AutobuskaStanica.getBrojPeronaStanice();
		for(int i=1;i<=brojPerona;++i)
			peroni.add(i);
		peroniComboBox.setItems(peroni);
		peroniComboBox.getSelectionModel().selectFirst();
		prevozniciObs.setAll(Prevoznik.getPrevozniciList());
		prevozniciComboBox.setItems(prevozniciObs);
		prevozniciComboBox.getSelectionModel().selectFirst();		
		validateSetUp();
		
	}

	public void validateSetUp() {
		nazivLinijeTextField.getValidators().add(Util.requredFieldValidator(nazivLinijeTextField));
		polazisteTextField.getValidators().add(Util.requredFieldValidator(polazisteTextField));
		odredisteTextField.getValidators().add(Util.requredFieldValidator(odredisteTextField));
		cijenaJednokratnaTextField.getValidators().addAll(Util.requredFieldValidator(cijenaJednokratnaTextField),Util.doubleValidator(cijenaJednokratnaTextField));
		//cijenaMjesecnaTextField.getValidators().add(Util.doubleValidator(cijenaMjesecnaTextField));
		
		cijenaMjesecnaTextField.getValidators().add(new ValidatorBase("Nije broj") {
			
			@Override
			protected void eval() {
				if(!cijenaMjesecnaTextField.getText().isEmpty() && !cijenaMjesecnaTextField.getText().matches("[0-9]+\\.[0-9]+"))
					hasErrors.set(true);
				else
					hasErrors.set(false);
				
			}
		});
		
		ValidatorBase odredisteValidator = new ValidatorBase("Nekorektan unos") {
			@Override
			protected void eval() {
				if(!odredisteTextField.getText().isEmpty() && !mjestaList.contains(odredisteTextField.getText())) {
					hasErrors.set(true);
				} else {
					hasErrors.set(false);
				}
			}
		};
		ValidatorBase polazisteValidator = new ValidatorBase("Nekorektan unos") {
			@Override
			protected void eval() {
				if((!polazisteTextField.getText().isEmpty() && !mjestaList.contains(polazisteTextField.getText()))) {
					hasErrors.set(true);
				} else {
					hasErrors.set(false);
				}
			}
		};
		Util.setAutocompleteList(polazisteTextField, mjestaList);
		Util.setAutocompleteList(odredisteTextField, mjestaList);
		
		polazisteTextField.getValidators().addAll(Util.requredFieldValidator(polazisteTextField),polazisteValidator);
		odredisteTextField.getValidators().addAll(Util.requredFieldValidator(odredisteTextField),odredisteValidator);
	}

	public void relacijeDisable(boolean b) {
		polazisteTextField.setDisable(b);
		odredisteTextField.setDisable(b);
		cijenaJednokratnaTextField.setDisable(b);
		cijenaMjesecnaTextField.setDisable(b);
		vrijemeDolaska.setDisable(b);
		vrijemePolaska.setDisable(b);
		dodajRelacijuButton.setDisable(b);
		questionMarkImageView.setDisable(b);
	}

	public void ucitajMjesta() {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select Naziv from mjesto";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false);
			r = s.executeQuery();
			while(r.next()) {
				mjestaList.add(r.getString("Naziv"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			Util.close(r, s, c);
		}
	}

	public void checkBoxInit() {
		ponedjeljakCB.setSelected(true);
		utorakCB.setSelected(true);
		srijedaCB.setSelected(true);
		cetvrtakCB.setSelected(true);
		petakCB.setSelected(true);
		subotaCB.setSelected(true);
		nedjeljaCB.setSelected(true);
		daniUSedmiciList.add("MONDAY");
		daniUSedmiciList.add("TUESDAY");
		daniUSedmiciList.add("WEDNESDAY");
		daniUSedmiciList.add("THURSDAY");
		daniUSedmiciList.add("FRIDAY");
		daniUSedmiciList.add("SATURDAY");
		daniUSedmiciList.add("SUNDAY");
	}

	public void mapiranjeDana() {
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

	public void setCB(boolean b) {
		ponedjeljakCB.setSelected(b);
		utorakCB.setSelected(b);
		srijedaCB.setSelected(b);
		cetvrtakCB.setSelected(b);
		petakCB.setSelected(b);
		subotaCB.setSelected(b);
		nedjeljaCB.setSelected(b);
		daniUSedmiciList.clear();
		if(b==false)
			daniString = "";
		else
			daniUSedmiciList.add("MONDAY");
			daniUSedmiciList.add("TUESDAY");
			daniUSedmiciList.add("WEDNESDAY");
			daniUSedmiciList.add("THURSDAY");
			daniUSedmiciList.add("FRIDAY");
			daniUSedmiciList.add("SATURDAY");
			daniUSedmiciList.add("SUNDAY");
		
	}
	
	public boolean showPotvrda() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("");
		alert.setHeaderText("Da li ste sigurni?");
		Optional<ButtonType> action = alert.showAndWait();
		return action.get().equals(ButtonType.OK);
	}
	
	public void disableCheckBox(boolean b) {
		ponedjeljakCB.setDisable(b);
		utorakCB.setDisable(b);
		srijedaCB.setDisable(b);
		cetvrtakCB.setDisable(b);
		petakCB.setDisable(b);
		subotaCB.setDisable(b);
		nedjeljaCB.setDisable(b);
		
	}
	@FXML
	public void dodajLiniju() {
		if(nazivLinijeTextField.validate()) {
			if(showPotvrda()) {
				disableCheckBox(true);
				mapiranjeDana();
				dodajRelacijuButton.setDisable(false);
				dodajteRelacijeLabel.setVisible(true);
				dodajLinijuButton.setDisable(true);
				idLinije = 	Linija.dodajLiniju(nazivLinijeTextField.getText(),peroniComboBox.getValue(),prevozniciComboBox.getValue().getJIBPrevoznika(),daniString);
				nazivLinijeTextField.setDisable(true);
				daniString="";
				showDodajteRelacije();
				relacijeDisable(false);
				nazivLinijeTextField.resetValidation();
			}
		}
	}
	
	public void showDodajteRelacije() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Uspjesno dodana linija");
		alert.setHeaderText("Unesite relacije na dodatoj liniji.");
		alert.showAndWait();
		gotovoButton.setVisible(true);
		nazivLinijeTextField.setDisable(true);
		prevozniciComboBox.setDisable(true);
		peroniComboBox.setDisable(true);
	}

	public void gotovUnos() {
		disableCheckBox(false);
		dodajRelacijuButton.setDisable(true);
		checkBoxInit();
		gotovoButton.setVisible(false);
		nazivLinijeTextField.setDisable(false);
		prevozniciComboBox.setDisable(false);
		peroniComboBox.setDisable(false);
		nazivLinijeTextField.clear();
		prevozniciComboBox.getSelectionModel().selectFirst();
		peroniComboBox.setValue(1);
		polazisteTextField.clear();
		odredisteTextField.clear();
		cijenaJednokratnaTextField.clear();
		cijenaMjesecnaTextField.clear();
		relacijeDisable(true);
		dodajLinijuButton.setDisable(false);
		polazisteTextField.resetValidation();
		odredisteTextField.resetValidation();
		cijenaJednokratnaTextField.resetValidation();
		cijenaMjesecnaTextField.resetValidation();
		nazivLinijeTextField.resetValidation();
	}

	public void loadCBListeners() {
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
		
	}
	
	public boolean vrijemeValidno() {
		return vrijemePolaska.getValue()!=null && vrijemeDolaska.getValue()!=null && !vrijemeDolaska.getValue().equals(vrijemePolaska.getValue());
	}
	
	@FXML
	public void dodajRelaciju() {
		if(!vrijemeValidno()) {
			showVrijemeNijeValidno();
			return;
		}
		if(polazisteTextField.validate() & 
				odredisteTextField.validate() & 
					cijenaJednokratnaTextField.validate() & 
						((cijenaMjesecnaTextField.getText().isEmpty())? true : cijenaMjesecnaTextField.validate())){
			mapiranjeDana();
			Relacija.dodajRelaciju(idLinije,polazisteTextField.getText(),odredisteTextField.getText(),Time.valueOf(vrijemePolaska.getValue()),
								Time.valueOf(vrijemeDolaska.getValue()),Double.parseDouble(cijenaJednokratnaTextField.getText()),
								cijenaMjesecnaTextField.getText());	
			checkMarkImageView.setVisible(true);
			Timeline timeline = new Timeline();
			timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(3),
					new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							checkMarkImageView.setVisible(false);
		        }
		    }));
			timeline.play();
			
			vrijemePolaska.setValue(null);
			vrijemeDolaska.setValue(null);
			polazisteTextField.clear();
			odredisteTextField.clear();
			cijenaJednokratnaTextField.clear();
			cijenaMjesecnaTextField.clear();
			cijenaJednokratnaTextField.resetValidation();
			cijenaMjesecnaTextField.resetValidation();
			polazisteTextField.resetValidation();
			odredisteTextField.resetValidation();
	}
	}

	public void showVrijemeNijeValidno() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Greska");
		alert.setHeaderText("Unosi o vremenima polaska i dolaska nisu validni.");
		alert.showAndWait();
	}
	*/
	public static ObservableList<Prevoznik> prevozniciObs = FXCollections.observableArrayList();
	
	@FXML
    private GridPane gridPane;
	
	@FXML
	private TableView<?> relacijeTableView;
	
	@FXML
    private JFXTextField nazivLinijeTextField;

    @FXML
    private JFXComboBox<Prevoznik> prevoznikComboBox;

    @FXML
    private JFXComboBox<Integer> peronComboBox;

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
    private JFXComboBox<?> zadrzavanjeComboBox;
    
    @FXML
    private JFXTextField cijenaObicneKarteTextField;

    @FXML
    private JFXTextField cijenaMjesecneKarteTextField;

    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
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
		prevoznikComboBox.setItems(new ObservableListWrapper<>(Prevoznik.getPrevozniciList()));	
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
			
			Util.getNotifications("Obavještenje", "Linija dodana.", "Information").show();
		}
	}
	
	@FXML
    void sledeciPolazak(ActionEvent event) {
		if(vrijemePolaska1TimePicker.validate()
				& vrijemeDolaska1TimePicker.validate()
					&vrijemePolaska1TimePicker.validate()
						& vrijemeDolaska1TimePicker.validate()) {
			
		}
	}
	
	@FXML
	void krajUnosa(ActionEvent event) {
		
	}
}
