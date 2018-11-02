package org.unibl.etf.administrator;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import org.unibl.etf.autobuska_stanica.*;

public class ListaStanicaController implements Initializable {
	
	@FXML
	private TableView<AutobuskaStanica> autobuskeStaniceTable;
	
	@FXML
    private TableColumn<?, ?> nazivColumn;

    @FXML
    private TableColumn<?, ?> adresaColumn;

    @FXML
    private TableColumn<?, ?> brojPosteColumn;

    @FXML
    private TableColumn<?, ?> brojTelefonaColumn;
    
    @FXML
    private TableColumn<?, ?> brojPeronaColumn;
    
    @FXML
    private TableColumn<?, ?> izmijeniColumn;
    
    public static ObservableList<AutobuskaStanica> listaAutobuskihStanica;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//tabela
    	listaAutobuskihStanica=FXCollections.observableArrayList();
    	autobuskeStaniceTable.setItems(listaAutobuskihStanica);
    	
    	autobuskeStaniceTable.setPlaceholder(new Label("Nema autobuskih stanica u tabeli."));
    	autobuskeStaniceTable.setFocusTraversable(false);
    	
    	nazivColumn.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        adresaColumn.setCellValueFactory(new PropertyValueFactory<>("adresa"));
        brojPosteColumn.setCellValueFactory(new PropertyValueFactory<>("brojPoste"));
        brojTelefonaColumn.setCellValueFactory(new PropertyValueFactory<>("brojTelefona"));
        brojPeronaColumn.setCellValueFactory(new PropertyValueFactory<>("brojPerona"));
        
        
        listaAutobuskihStanica.addAll(AutobuskaStanica.listaStanica());
	}

}
