package org.unibl.etf.administrator;

import java.net.URL;
import java.util.ResourceBundle;

import org.unibl.etf.prijava.Nalog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ListaNalogaController implements Initializable {
	
	@FXML
    private TableView<Nalog> naloziTable;

    @FXML
    private TableColumn<?, ?> korisnickoImeColumn;

    @FXML
    private TableColumn<?, ?> lozinkaColumn;

    @FXML
    private TableColumn<?, ?> tipColumn;

    @FXML
    private TableColumn<?, ?> idStaniceColumn;
    
    @FXML
    private TableColumn<Nalog, Nalog> izmijeniColumn;
    
    @FXML
    private TableColumn<Nalog, Nalog> obrisiColumn;
    
    public static ObservableList<Nalog> listaNaloga;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listaNaloga=FXCollections.observableArrayList();
		naloziTable.setItems(listaNaloga);
		
		naloziTable.setPlaceholder(new Label("Nema naloga u tabeli."));
		naloziTable.setFocusTraversable(false);
		
		
	}

}
