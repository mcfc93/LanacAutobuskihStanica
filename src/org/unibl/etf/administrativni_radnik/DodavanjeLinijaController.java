package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.unibl.etf.karta.Linija;
import org.unibl.etf.karta.Prevoznik;
import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.util.Util;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.control.Alert.AlertType;

public class DodavanjeLinijaController implements Initializable {
	
	private static List<String> daniUSedmiciList = new ArrayList<>();
	public static ObservableList<Prevoznik> prevozniciObs = FXCollections.observableArrayList();
	public static String daniString="";
	public static ObservableList<Integer> peroni = FXCollections.observableArrayList();
	public static List<String> mjestaList = new ArrayList<>();
	
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
	private ImageView checkMark = new ImageView();
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
	private static int idLinije;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//setCB(true);
		dodajLinijuButton.disableProperty().bind(Bindings.isEmpty(nazivLinijeTextField.textProperty()));
		cijenaJednokratnaTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    cijenaJednokratnaTextField.setText(oldValue);
                }
            }
        });
		cijenaMjesecnaTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    cijenaMjesecnaTextField.setText(oldValue);
                }
            }
        });
		checkMark.setVisible(false);
		gotovoButton.setVisible(false);
		relacijeDisable(true);
		ucitajMjesta();
		checkBoxInit();
		loadCBListeners();
		dodajteRelacijeLabel.setVisible(false);
		ucitajPrevoznike();
		int brojPerona = getBrojPerona();
		for(int i=1;i<=brojPerona;++i)
			peroni.add(i);
		peroniComboBox.setItems(peroni);
		peroniComboBox.getSelectionModel().selectFirst();
		prevozniciComboBox.setItems(prevozniciObs);
		prevozniciComboBox.getSelectionModel().selectFirst();
		
		dodajRelacijuButton.disableProperty().bind(Bindings.createBooleanBinding(
			    () -> !mjestaList.contains(odredisteTextField.getText()) || 
			    		!mjestaList.contains(polazisteTextField.getText()) ||
			    		cijenaJednokratnaTextField.getText().isEmpty() ||
			    		cijenaMjesecnaTextField.getText().isEmpty() ||
			    		odredisteTextField.getText().equals(polazisteTextField.getText()),
			    		
			    	odredisteTextField.textProperty(),
			    	polazisteTextField.textProperty(),
			    	cijenaJednokratnaTextField.textProperty(),
			    	cijenaMjesecnaTextField.textProperty()
			    ));	}


	public void relacijeDisable(boolean b) {
		// TODO Auto-generated method stub
		polazisteTextField.setDisable(b);
		odredisteTextField.setDisable(b);
		cijenaJednokratnaTextField.setDisable(b);
		cijenaMjesecnaTextField.setDisable(b);
		vrijemeDolaska.setDisable(b);
		vrijemePolaska.setDisable(b);
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
		autoCompletePolaziste();
		autoCompleteOdrediste();
	}


	private void autoCompleteOdrediste() {
		// TODO Auto-generated method stub
		JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
		autoCompletePopup.getSuggestions().clear();
	    autoCompletePopup.getSuggestions().addAll(mjestaList);
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
	}


	public void autoCompletePolaziste() {
		// TODO Auto-generated method stub
		JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
		autoCompletePopup.getSuggestions().clear();
	    autoCompletePopup.getSuggestions().addAll(mjestaList);
	    autoCompletePopup.setSelectionHandler(event -> {
	        polazisteTextField.setText(event.getObject());
	    });
	     
	    polazisteTextField.textProperty().addListener(observable -> {
	        autoCompletePopup.filter(string -> string.toLowerCase().contains(polazisteTextField.getText().toLowerCase()));
	        if (autoCompletePopup.getFilteredSuggestions().isEmpty() || polazisteTextField.getText().isEmpty()) {
	            autoCompletePopup.hide();
	        } else {
	            autoCompletePopup.show(polazisteTextField);
	        }
	    });
	
	    
	    
	}


	public void checkBoxInit() {
		// TODO Auto-generated method stub
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


	private int getBrojPerona() {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select BrojPerona from autobuska_stanica where JIBStanice=?";
		try {
			c = Util.getConnection();
			s = c.prepareStatement(sql);
			s.setString(1, PrijavaController.nalog.getIdStanice());
			r = s.executeQuery();
			if(r.next()) {
				return r.getInt(1);
			}
			else
				return 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(r, s, c);
		}
		return 0;
	}


	public void ucitajPrevoznike() {
		String sql = "select NazivPrevoznika,JIBPrevoznika from prevoznik where Stanje='Aktivno'";
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false);
			r = s.executeQuery();
			while(r.next()) {
				prevozniciObs.add(new Prevoznik(r.getString("NazivPrevoznika"),r.getString("JIBPrevoznika")));
			}
		} catch (SQLException e) {
			// TODO: handle exception
		}
		finally {
			Util.close(r, s, c);
		}
	}


	public void mapiranjeDana() {
		System.out.println("Velicina niza: " + daniUSedmiciList.size());
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
	
	@FXML
	public void dodajLiniju() {
		if(showPotvrda()) {
			mapiranjeDana();
			dodajteRelacijeLabel.setVisible(true);
			System.out.println("Dani: " + daniString);
			String sql = "insert into linija value (default,?,?,?,?,default)";
			Connection c = null;
			PreparedStatement s = null;
			ResultSet r = null;
			try {
				c = Util.getConnection();
				s = Util.prepareStatement(c, sql, true, nazivLinijeTextField.getText(),peroniComboBox.getValue(),prevozniciComboBox.getValue().getJIBPrevoznika(),daniString);
				System.out.println(s.executeUpdate());
				r = s.getGeneratedKeys();
				if(r.next()) {
					System.out.println("Kljuc: " + r.getInt(1));
					idLinije = r.getInt(1);
				}
			} catch (SQLException e) {
			e.printStackTrace();
			}
			finally {
				Util.close(r,s, c);
			}
			setCB(true);
			nazivLinijeTextField.clear();
			daniString="";
			showDodajteRelacije();
			relacijeDisable(false);
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
		checkBoxInit();
		gotovoButton.setVisible(false);
		nazivLinijeTextField.setDisable(false);
		prevozniciComboBox.setDisable(false);
		peroniComboBox.setDisable(false);
		nazivLinijeTextField.clear();
		prevozniciComboBox.setValue(null);
		peroniComboBox.setValue(1);
		polazisteTextField.clear();
		odredisteTextField.clear();
		cijenaJednokratnaTextField.clear();
		cijenaMjesecnaTextField.clear();
		relacijeDisable(true);
	}

	public void loadCBListeners() {
		// TODO Auto-generated method stub
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
		return vrijemePolaska.getValue()!=null && vrijemeDolaska.getValue()!=null && !vrijemeDolaska.getValue().equals(vrijemePolaska.getValue()) && !vrijemeDolaska.getValue().isBefore(vrijemePolaska.getValue());
	}
	@FXML
	public void dodajRelaciju() {
		if(!vrijemeValidno()) {
			showVrijemeNijeValidno();
			return;
		}
		mapiranjeDana();
		String sql = "insert into relacija value (default,?,?,?,?,?,?,?)";
		Connection c = null;
		PreparedStatement s = null;
		try {
			c = Util.getConnection();
			s = c.prepareStatement(sql);
			s.setInt(1, idLinije);
			s.setString(2, polazisteTextField.getText());
			s.setString(3, odredisteTextField.getText());
			s.setTime(4, Time.valueOf(vrijemePolaska.getValue()));
			s.setTime(5, Time.valueOf(vrijemeDolaska.getValue()));
			s.setDouble(6, Double.parseDouble(cijenaJednokratnaTextField.getText()));
			if(cijenaMjesecnaTextField.getText().isEmpty())
				s.setDouble(7, 0);
			else
				s.setDouble(7, Double.parseDouble(cijenaMjesecnaTextField.getText()));
			System.out.println(s.executeUpdate());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(s, c);
		}
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
		polazisteTextField.clear();
		odredisteTextField.clear();
		vrijemePolaska.setValue(null);
		vrijemeDolaska.setValue(null);
		cijenaJednokratnaTextField.clear();
		cijenaMjesecnaTextField.clear();
	}


	public void showVrijemeNijeValidno() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Greska");
		alert.setHeaderText("Unosi o vremenima polaska i dolaska nisu validni.");
		alert.showAndWait();
	}

}
