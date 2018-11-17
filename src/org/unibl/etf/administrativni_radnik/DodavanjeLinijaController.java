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

import org.unibl.etf.karta.Linija;
import org.unibl.etf.karta.Prevoznik;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class DodavanjeLinijaController implements Initializable {
	
	private static List<String> daniUSedmiciList = new ArrayList<>();
	public static ObservableList<Prevoznik> prevozniciObs = FXCollections.observableArrayList();
	public static String daniString="";
	public static ObservableList<Integer> peroni = FXCollections.observableArrayList();

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
		loadCBListeners();
		clearCB();
		ucitajPrevoznike();
		int brojPerona = getBrojPerona();
		for(int i=0;i<brojPerona;++i)
			peroni.add(i);
		peroniComboBox.setItems(peroni);
		prevozniciComboBox.setItems(prevozniciObs);
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
		for(int i=0;i<daniUSedmiciList.size()-1;++i)
			daniString += daniUSedmiciList.get(i) + ",";
		daniString += daniUSedmiciList.get(daniUSedmiciList.size()-1);
		System.out.println(daniString);
	}

	public void clearCB() {
		ponedjeljakCB.setSelected(false);
		utorakCB.setSelected(false);
		srijedaCB.setSelected(false);
		cetvrtakCB.setSelected(false);
		petakCB.setSelected(false);
		subotaCB.setSelected(false);
		nedjeljaCB.setSelected(false);
		daniUSedmiciList.clear();
		daniString="";
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
		}
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
	
	@FXML
	public void dodajRelaciju() {
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
				s.setDouble(7, Double.parseDouble(cijenaJednokratnaTextField.getText()));
			System.out.println(s.executeUpdate());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(s, c);
		}
	}

}
