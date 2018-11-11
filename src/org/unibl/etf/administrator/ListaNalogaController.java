package org.unibl.etf.administrator;

import java.net.URL;
import java.util.ResourceBundle;
import org.controlsfx.control.MaskerPane;
import org.unibl.etf.prijava.Nalog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class ListaNalogaController implements Initializable {
	
	@FXML
	private AnchorPane anchorPane;
	
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
		
		for(TableColumn<?,?> column: naloziTable.getColumns()) {
        	column.setReorderable(false);
        }
		
		
		
		MaskerPane progressPane = new MaskerPane();
		progressPane.setText("Molimo sačekajte...");
		progressPane.setVisible(false);
		anchorPane.getChildren().add(progressPane);
		AnchorPane.setTopAnchor(progressPane,0.0);
		AnchorPane.setBottomAnchor(progressPane,0.0);
		AnchorPane.setLeftAnchor(progressPane,0.0);
		AnchorPane.setRightAnchor(progressPane,0.0);
		
		System.out.println(Thread.currentThread());
		
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
            	System.out.println(Thread.currentThread());
                progressPane.setVisible(true);
                for(int i=0; i<30; i++) {
                //listaNaloga.addAll(Nalog.listaNaloga());
            	}
                return null;
            }
            @Override
            protected void succeeded(){
                super.succeeded();
                progressPane.setVisible(false);
            }
        };
        new Thread(task).start();
	}
}
