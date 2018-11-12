package org.unibl.etf.salterski_radnik;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.unibl.etf.util.Util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class OtkazivanjeRezervacijeController implements Initializable {

	private int serijskiBroj;
	@FXML
	private JFXTextField serijskiBrojTextField = new JFXTextField();
	@FXML
	private JFXButton pretragaKarataButton = new JFXButton();
	@FXML
	private JFXButton stornirajButton = new JFXButton();
	@FXML
	private TextField linijaTextField = new TextField();
	@FXML
	private TextField relacijaTextField = new TextField();
	@FXML
	private TextField datumTextField = new TextField();
	@FXML
	private TextField cijenaTextField  = new TextField();
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	public void pretragaKarata() {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select karta.SerijskiBroj,karta.Cijena,relacija.Polaziste,relacija.Odrediste,linija.NazivLinije,karta.Datum from karta "
				+ "join (relacija,linija) on (karta.IdRelacije=relacija.IdRelacije) and (relacija.IdLinije=linija.IdLinije) "
				+ " where karta.SerijskiBroj=?";
		try {
			c = Util.getConnection();
			s = c.prepareStatement(sql);
			System.out.println("Serijski broj: " + serijskiBrojTextField.getText());
			s.setInt(1, Integer.parseInt(serijskiBrojTextField.getText()));
			r = s.executeQuery();
			if(r.next()) {
				serijskiBroj = r.getInt(1);
				System.out.println("Serial : " + serijskiBroj);
				cijenaTextField.setText(String.valueOf(r.getDouble(2)));
				relacijaTextField.setText(r.getString(4) + " - " + r.getString(3));
				datumTextField.setText(r.getDate(6).toString());
				linijaTextField.setText(r.getString(5));
				
			}
			else {
				showAlertPogresanSerijskiBroj();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void showAlertPogresanSerijskiBroj() {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("GRESKA");
			alert.setHeaderText("Pogresan serijski broj!");
			alert.showAndWait();
		}
		
	

	@FXML
	public void storniraj() {
		Connection c = null;
		PreparedStatement s = null;
		String sql = "delete from karta where SerijskiBroj=?";
		try {
			c = Util.getConnection();
			s = c.prepareStatement(sql);
			s.setInt(1, serijskiBroj);
			System.out.println(s.executeUpdate());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(s, c);
		}
		cijenaTextField.setText("");
		relacijaTextField.setText("");
		datumTextField.setText("");
		linijaTextField.setText("");
		serijskiBrojTextField.setText("");
	}
}
