package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.unibl.etf.util.Util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

public class PrevoznikEditController implements Initializable{
	
	@FXML
	private JFXTextField nazivTextField = new JFXTextField();
	@FXML
	private JFXTextField adresaTextField = new JFXTextField();
	@FXML
	private JFXTextField emailTextField = new JFXTextField();
	@FXML
	private JFXTextField telefonTextField = new JFXTextField();
	@FXML
	private JFXTextField webAdresaTextField = new JFXTextField();
	@FXML
	private JFXTextField tekuciRacunTextField = new JFXTextField();
	@FXML
	private JFXTextField postanskiBrojTextField = new JFXTextField();
	@FXML
	private JFXTextField jibTextField = new JFXTextField();
	@FXML
	private JFXButton okButton = new JFXButton();
	@FXML
	private JFXButton otkaziButton = new JFXButton();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		jibTextField.setEditable(false);
		
		jibTextField.setText(IzmjenaPrevoznikaController.odabraniPrevoznik.getJIBPrevoznika());
		nazivTextField.setText(IzmjenaPrevoznikaController.odabraniPrevoznik.getNaziv());
		adresaTextField.setText(IzmjenaPrevoznikaController.odabraniPrevoznik.getAdresa());
		emailTextField.setText(IzmjenaPrevoznikaController.odabraniPrevoznik.getEmail());
		telefonTextField.setText(IzmjenaPrevoznikaController.odabraniPrevoznik.getTelefon());
		webAdresaTextField.setText(IzmjenaPrevoznikaController.odabraniPrevoznik.getWebAdresa());
		tekuciRacunTextField.setText(IzmjenaPrevoznikaController.odabraniPrevoznik.getRacun());
		postanskiBrojTextField.setText(String.valueOf(IzmjenaPrevoznikaController.odabraniPrevoznik.getPostanskiBroj()));
	}

	@FXML
	public void otkazi() {
		 Stage stage = (Stage) otkaziButton.getScene().getWindow();
		    stage.close();
	}
	@FXML
	public void izmjeniPrevoznika() {
		Connection c = null;
		String sql = "update prevoznik "
				+ "set NazivPrevoznika=?,Telefon=?,Email=?,WebAdresa=?,TekuciRacun=?,Adresa=?,PostanskiBroj=? "
				+ "where JIBPrevoznika=?";
		PreparedStatement s = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, nazivTextField.getText(),telefonTextField.getText(),emailTextField.getText(),
					webAdresaTextField.getText(),tekuciRacunTextField.getText(),adresaTextField.getText(),Integer.parseInt(postanskiBrojTextField.getText()),IzmjenaPrevoznikaController.odabraniPrevoznik.getJIBPrevoznika());
			IzmjenaPrevoznikaController.odabraniPrevoznik.setAdresa(adresaTextField.getText());
			IzmjenaPrevoznikaController.odabraniPrevoznik.setNaziv(nazivTextField.getText());
			IzmjenaPrevoznikaController.odabraniPrevoznik.setTelefon(telefonTextField.getText());
			IzmjenaPrevoznikaController.odabraniPrevoznik.setEmail(emailTextField.getText());
			IzmjenaPrevoznikaController.odabraniPrevoznik.setWebAdresa(webAdresaTextField.getText());
			IzmjenaPrevoznikaController.odabraniPrevoznik.setRacun(tekuciRacunTextField.getText());
			IzmjenaPrevoznikaController.odabraniPrevoznik.setPostanskiBroj(Integer.parseInt(postanskiBrojTextField.getText()));
			
			System.out.println(s.executeUpdate());
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
