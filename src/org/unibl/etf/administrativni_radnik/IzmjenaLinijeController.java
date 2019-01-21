package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.unibl.etf.autobuska_stanica.AutobuskaStanica;
import org.unibl.etf.karta.Linija;
import org.unibl.etf.karta.Relacija;
import org.unibl.etf.util.Util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;

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
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Alert.AlertType;

public class IzmjenaLinijeController implements Initializable {
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
		}
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

	public boolean showPotvrda() {
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

}
