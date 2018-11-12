package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import org.unibl.etf.karta.Linija;
import org.unibl.etf.salterski_radnik.SalterskiRadnikController;
import org.unibl.etf.util.Util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class DodajIliUkloniLinijeController implements Initializable {

	private static List<String> daniUSedmiciList = new ArrayList<>();
	private static String daniString="";
	private static ObservableList<Linija> linijeList = FXCollections.observableArrayList();
	
	@FXML
	private TableView<Linija> linijeTableView = new TableView<>();
	@FXML
	private TableColumn<Linija,String> nazivLinijeColumn = new TableColumn<Linija, String>();
	@FXML
	private TableColumn<Linija,Integer> idLinijeLinijeColumn = new TableColumn<Linija, Integer>();
	@FXML
	private TableColumn<Linija,String> nazivPrevoznikaColumn = new TableColumn<Linija, String>();
	@FXML
	private TableColumn<Linija,Integer> peronLinijeColumn = new TableColumn<Linija, Integer>();
	@FXML
	private TableColumn<Linija,String> daniUSedmiciColumn = new TableColumn<Linija, String>();
	
	@FXML
	private TextField peronTextField = new JFXTextField();
	@FXML
	private JFXButton pretragaButton = new JFXButton();
	@FXML
	private JFXButton ukloniLinijuButton = new JFXButton();
	@FXML
	private JFXButton dodajLinijuButton = new JFXButton();
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
		// TODO Auto-generated method stub
		loadCBListeners();
		
	}
	
	private void loadCBListeners() {
		// TODO Auto-generated method stub
		ponedjeljakCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if(newValue) 
		        	System.out.println(daniUSedmiciList.add(DayOfWeek.MONDAY.toString()));
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
		        	System.out.println(daniUSedmiciList.remove(DayOfWeek.TUESDAY.toString()));
		        
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
	public void dodajLiniju() {
		mapiranjeDana();
		
	}
	@FXML
	public void pretragaLinija() {
		
	}
	
	
	public boolean showPotvrda() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("");
		alert.setHeaderText("Da li ste sigurni?");
		Optional<ButtonType> action = alert.showAndWait();
		return action.get().equals(ButtonType.OK);
	}
	
	@FXML
	public void ukloniLiniju() {
		
	}


	private void showUspjesnoUklonjenaLinija() {
		// TODO Auto-generated method stub
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Uspjeh");
		alert.setHeaderText("Uspjesno uklonjena linija");
		alert.showAndWait();
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
}
