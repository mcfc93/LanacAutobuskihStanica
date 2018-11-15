package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.unibl.etf.karta.Relacija;
import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.util.Util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class IzmjenaLinijeController implements Initializable {

	private static ObservableList<Relacija> relacijeObs = FXCollections.observableArrayList();
	private static List<String> daniUSedmiciList = new ArrayList<>();
	private static String daniString="";
	private int peroni;
	@FXML
	private JFXComboBox<Integer> brojPeronaComboBox = new JFXComboBox<>();
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
	private AnchorPane relacijeAnchorPane = new AnchorPane();
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
	private TextField polazisteTextField = new TextField();
	@FXML
	private TextField odredisteTextField = new TextField();
	@FXML
	private TextField cijenaMjesecnaTextField = new TextField();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nazivLinijeTextField.setText(DodajIliUkloniLinijeController.odabranaLinija.getNazivLinije());
		getRelacije();
		peroni = getBrojPerona();
		for(int i=1;i<=peroni;++i)
			brojPeronaComboBox.getItems().add(i);
		brojPeronaComboBox.setVisibleRowCount(3);
		brojPeronaComboBox.setValue(DodajIliUkloniLinijeController.odabranaLinija.getPeron());
		checkBoxInit();
		loadCBListeners();
		relacijeComboBox.getSelectionModel().selectFirst();
		polazisteTextField.setText(relacijeComboBox.getValue().getPolaziste());
		odredisteTextField.setText(relacijeComboBox.getValue().getOdrediste());
		vrijemePolaskaTimeChooser.setValue(relacijeComboBox.getValue().getVrijemePolaska().toLocalTime());
		vrijemeDolaskaTimeChooser.setValue(relacijeComboBox.getValue().getVrijemeDolaska().toLocalTime());
		cijenaJednokratnaTextField.setText(Double.toString(relacijeComboBox.getValue().getCijenaJednokratna()));
		if(relacijeComboBox.getValue().getCijenaMjesecna()==0)
			cijenaMjesecnaTextField.setText("Nema u ponudi");
		else
			cijenaMjesecnaTextField.setText(Double.toString(relacijeComboBox.getValue().getCijenaMjesecna()));
	}
	public  int getBrojPerona() {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		System.out.println("JIB stanice: " + PrijavaController.nalog.getIdStanice());
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
	private void checkBoxInit() {
		String daniUSedmici = DodajIliUkloniLinijeController.odabranaLinija.getDaniUSedmici();
		if(daniUSedmici.contains("MONDAY")) {
			ponedjeljakCB.setSelected(true);
			daniUSedmiciList.add("MONDAY");
		}
		if(daniUSedmici.contains("TUESDAY")) {
			utorakCB.setSelected(true);
			daniUSedmiciList.add("TUESDAY");
		}
		if(daniUSedmici.contains("WEDNESDAY")) {
			srijedaCB.setSelected(true);
			daniUSedmiciList.add("WEDNESDAY");
		}
		if(daniUSedmici.contains("THURSDAY")) {
			cetvrtakCB.setSelected(true);
			daniUSedmiciList.add("THURSDAY");
		}
		if(daniUSedmici.contains("FRIDAY")) {
			petakCB.setSelected(true);
			daniUSedmiciList.add("FRIDAY");
		}
		if(daniUSedmici.contains("SATURDAY")) {
			subotaCB.setSelected(true);
			daniUSedmiciList.add("SATURDAY");
		}
		if(daniUSedmici.contains("SUNDAY")) {
			nedjeljaCB.setSelected(true);
			daniUSedmiciList.add("SUNDAY");

		}
	}

	private void getRelacije() {
		// TODO Auto-generated method stub
		String sqlQuery = "select * from relacija where IdLinije=?";
		Connection c = null;
		PreparedStatement s = null;
		System.out.println("fas");
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s  = c.prepareStatement(sqlQuery);
			s.setInt(1, DodajIliUkloniLinijeController.odabranaLinija.getIdLinije());
			r = s.executeQuery();
			while(r.next()) {
				relacijeObs.add(new Relacija(r.getInt("IdLinije"), r.getInt("IdRelacije"), r.getString("Polaziste"), r.getString("Odrediste"), r.getTime("VrijemePolaska"), r.getTime("VrijemeDolaska"), r.getDouble("CijenaJednokratna"), r.getDouble("CijenaMjesecna")));
			}
			relacijeComboBox.setItems(relacijeObs);	
		} catch (SQLException e) {
			// TODO: handle exception
		}
		finally {
			Util.close(r, s, c);
		}
	}
	
	@FXML
	public void comboBoxChange(ActionEvent event) {
		polazisteTextField.setText(relacijeComboBox.getValue().getPolaziste());
		odredisteTextField.setText(relacijeComboBox.getValue().getOdrediste());
		vrijemePolaskaTimeChooser.setValue(relacijeComboBox.getValue().getVrijemePolaska().toLocalTime());
		vrijemeDolaskaTimeChooser.setValue(relacijeComboBox.getValue().getVrijemeDolaska().toLocalTime());
		cijenaJednokratnaTextField.setText(Double.toString(relacijeComboBox.getValue().getCijenaJednokratna()));
		if(relacijeComboBox.getValue().getCijenaMjesecna()==0)
			cijenaMjesecnaTextField.setText("Nema u ponudi");
		else
			cijenaMjesecnaTextField.setText(Double.toString(relacijeComboBox.getValue().getCijenaMjesecna()));
	}
	
	@FXML
	public void izmjeniRelaciju() {
		if(showPotvrda()) {
			String sqlQuery = "update relacija set CijenaJednokratna=?, CijenaMjesecna=?, VrijemePolaska=?, VrijemeDolaska=? where IdRelacije=?";
			Connection c = null;
			PreparedStatement s  = null;
			try {
				c = Util.getConnection();
				s = c.prepareStatement(sqlQuery);
				s.setDouble(1, Double.parseDouble(cijenaJednokratnaTextField.getText()));
				if(cijenaMjesecnaTextField.getText().equals("Nema u ponudi"))
					s.setDouble(2, 0);
				else
					s.setDouble(2, Double.parseDouble(cijenaMjesecnaTextField.getText()));
					s.setTime(3, Time.valueOf(vrijemePolaskaTimeChooser.getValue()));
					s.setTime(4, Time.valueOf(vrijemeDolaskaTimeChooser.getValue()));
					s.setInt(5, relacijeComboBox.getValue().getIdRelacije());
					System.out.println(s.executeUpdate());
			} catch (SQLException e) {
				// TODO: handle exception
			}
			finally {
				Util.close(s, c);
			}
		}
	}
	
	@FXML
	public void izmjenaLinije() {
		mapiranjeDana();
		System.out.println("ID odabrane linije: " + DodajIliUkloniLinijeController.odabranaLinija.getIdLinije());
		System.out.println("Dani: " + daniString);
		if(showPotvrda()) {
			String sqlQuery = "update linija set NazivLinije=?, Peron=?, DaniUSedmici=? where IdLinije=?";
			Connection c = null;
			PreparedStatement s  = null;
			try {
				c = Util.getConnection();
				s = c.prepareStatement(sqlQuery);
				s.setString(1, nazivLinijeTextField.getText());
				s.setInt(2, brojPeronaComboBox.getValue());
				s.setString(3, daniString);
				s.setInt(4, DodajIliUkloniLinijeController.odabranaLinija.getIdLinije());
				System.out.println(s.executeUpdate());
				daniString="";
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				Util.close(s, c);
			}
			 Stage stage = (Stage) okButton.getScene().getWindow();
			    stage.close();
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
	
	public boolean showPotvrda() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("");
		alert.setHeaderText("Da li ste sigurni?");
		Optional<ButtonType> action = alert.showAndWait();
		return action.get().equals(ButtonType.OK);
	}
}
