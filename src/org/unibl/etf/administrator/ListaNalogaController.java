package org.unibl.etf.administrator;

import java.net.URL;
import java.util.ResourceBundle;
import org.controlsfx.control.MaskerPane;
import org.unibl.etf.prijava.Nalog;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

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
    private TableColumn<?, ?> imeColumn;

    @FXML
    private TableColumn<?, ?> prezimeColumn;
    
    @FXML
    private TableColumn<Nalog, Nalog> izmijeniColumn;
    
    @FXML
    private TableColumn<Nalog, Nalog> obrisiColumn;
    
    public static ObservableList<Nalog> listaNaloga;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listaNaloga=FXCollections.observableArrayList();
		naloziTable.setItems(listaNaloga);
		
		naloziTable.setPlaceholder(new Label("Nema korisničkih naloga u tabeli."));
		naloziTable.setFocusTraversable(false);
		
		korisnickoImeColumn.setCellValueFactory(new PropertyValueFactory<>("korisnickoIme"));
		lozinkaColumn.setCellValueFactory(new PropertyValueFactory<>("lozinka"));
		tipColumn.setCellValueFactory(new PropertyValueFactory<>("tip"));
		idStaniceColumn.setCellValueFactory(new PropertyValueFactory<>("idStanice"));
		imeColumn.setCellValueFactory(new PropertyValueFactory<>("ime"));
		prezimeColumn.setCellValueFactory(new PropertyValueFactory<>("prezime"));
		
		obrisiColumn.setCellValueFactory(
        		param -> new ReadOnlyObjectWrapper<>(param.getValue())
        	);
		
		obrisiColumn.setCellFactory(tableCell -> {
            TableCell<Nalog, Nalog> cell = new TableCell<Nalog, Nalog>() {
                private Button button = new Button("");
            	//postaviti dimenzije
                @Override
                protected void updateItem(Nalog item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                       	//System.out.println(item);
                    	//postavljanje CSS
                    	button.getStyleClass().addAll("buttonTable", "buttonTableDelete");
                    	//postavljanje opisa
                    	button.setTooltip(new Tooltip("Obriši?"));
                    	button.getTooltip().setAutoHide(false);
                    	button.getTooltip().setShowDelay(Duration.seconds(0.5));
                    	//dodavanje u kolonu
                    	setGraphic(button);
                    	button.setOnMouseClicked(
                    		event -> {
                    			Nalog.brisanjeNaloga(item.getZaposleni().getJmbg());
                    			getTableView().getItems().remove(item);
                				System.out.println("Obrisano: " + item);
                    		}
                        );
                    } else {
                    	setGraphic(null);
                    }
                }
            };
            return cell;
        });
		
		for(TableColumn<?,?> column: naloziTable.getColumns()) {
        	column.setReorderable(false);
		}
		
		korisnickoImeColumn.setMinWidth(100);
		korisnickoImeColumn.setMaxWidth(250);
		
		lozinkaColumn.setMinWidth(60);
		
		tipColumn.setMinWidth(125);
		tipColumn.setMaxWidth(150);
		
		idStaniceColumn.setMinWidth(75);
		idStaniceColumn.setMaxWidth(85);
		
		imeColumn.setMinWidth(70);
		imeColumn.setMaxWidth(150);
		
		prezimeColumn.setMinWidth(70);
		prezimeColumn.setMaxWidth(150);
		
		obrisiColumn.setText("");
        obrisiColumn.setMinWidth(35);
        obrisiColumn.setMaxWidth(35);
        obrisiColumn.setSortable(false);
		
		
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
                //for(int i=0; i<30; i++) {
                listaNaloga.addAll(Nalog.listaNaloga());
            	//}
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
