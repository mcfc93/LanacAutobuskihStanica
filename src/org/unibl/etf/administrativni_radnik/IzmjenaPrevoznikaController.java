package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.unibl.etf.karta.Prevoznik;
import org.unibl.etf.util.Util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class IzmjenaPrevoznikaController implements Initializable {

	public static ObservableList<Prevoznik> prevozniciObsList = FXCollections.observableArrayList();
	
	@FXML
	private TableView<Prevoznik> prevozniciTableView = new TableView<>();
	@FXML
	private TableColumn<Prevoznik,String> nazivColumn = new TableColumn<>();
	@FXML
	private TableColumn<Prevoznik,String> adresaColumn = new TableColumn<>();
	@FXML
	private TableColumn<Prevoznik,String> telefonColumn = new TableColumn<>();
	@FXML
	private TableColumn<Prevoznik,String> racunColumn = new TableColumn<>();
	@FXML
	private TableColumn<Prevoznik,String> emailColumn = new TableColumn<>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nazivColumn.setCellValueFactory(new PropertyValueFactory<>("naziv"));
		adresaColumn.setCellValueFactory(new PropertyValueFactory<>("adresa"));
		telefonColumn.setCellValueFactory(new PropertyValueFactory<>("telefon"));
		racunColumn.setCellValueFactory(new PropertyValueFactory<>("racun"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
		ucitajPrevoznike();
		
	}

	public void ucitajPrevoznike() {
		// TODO Auto-generated method stub
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql =  "select JIBPrevoznika,NazivPrevoznika,Telefon,Email,WebAdresa,TekuciRacun,Adresa,prevoznik.PostanskiBroj,Naziv from prevoznik join mjesto on (prevoznik.PostanskiBroj=mjesto.PostanskiBroj)";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false);
			r = s.executeQuery();
			while(r.next()) {
				System.out.println("Aa");
				prevozniciObsList.add(new Prevoznik(r.getString("NazivPrevoznika"), r.getString("Email"), r.getString("Adresa"), r.getString("Telefon"), r.getString("Naziv"), r.getString("WebAdresa"), r.getString("JIBPrevoznika"), r.getString("TekuciRacun")));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(r, s, c);
			prevozniciTableView.setItems(prevozniciObsList);

		}
	}

}
