package org.unibl.etf.administrator;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import org.controlsfx.control.MaskerPane;
import org.unibl.etf.autobuska_stanica.*;


public class ListaStanicaController implements Initializable {
	
	@FXML
	AnchorPane anchorPane;
	
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
    private TableColumn<AutobuskaStanica, AutobuskaStanica> izmijeniColumn;
    
    @FXML
    private TableColumn<AutobuskaStanica, AutobuskaStanica> obrisiColumn;
    
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
        brojPosteColumn.setCellValueFactory(new PropertyValueFactory<>("postanskiBroj"));
        brojTelefonaColumn.setCellValueFactory(new PropertyValueFactory<>("brojTelefona"));
        brojPeronaColumn.setCellValueFactory(new PropertyValueFactory<>("brojPerona"));

        
        izmijeniColumn.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
            );
        
        izmijeniColumn.setCellFactory(tableCell -> {
            TableCell<AutobuskaStanica, AutobuskaStanica> cell = new TableCell<AutobuskaStanica, AutobuskaStanica>() {
                private Button button = new Button("");
            	//postaviti dimenzije
                @Override
                protected void updateItem(AutobuskaStanica item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                    	//System.out.println(item);
                    	button.getStyleClass().addAll("buttonTable", "buttonTableEdit");
                    	button.setTooltip(new Tooltip("Izmjeni?"));
                    	button.getTooltip().setAutoHide(false);
                    	button.getTooltip().setShowDelay(Duration.seconds(0.5));
                    	setGraphic(button);
                    	button.setOnMouseClicked(
                    			event -> getTableView().getItems().remove(item)
                    		);
                    } else {
                    	setGraphic(null);
                    }
                }
            };
            return cell;
        });
        
        obrisiColumn.setCellValueFactory(
        		param -> new ReadOnlyObjectWrapper<>(param.getValue())
        	);
        
        obrisiColumn.setCellFactory(tableCell -> {
            TableCell<AutobuskaStanica, AutobuskaStanica> cell = new TableCell<AutobuskaStanica, AutobuskaStanica>() {
                private Button button = new Button("");
            	//postaviti dimenzije
                @Override
                protected void updateItem(AutobuskaStanica item, boolean empty) {
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
                    			event -> getTableView().getItems().remove(item)
                        );
                    } else {
                    	setGraphic(null);
                    }
                }
            };
            return cell;
        });
        
        for(TableColumn<?,?> column:autobuskeStaniceTable.getColumns()) {
        	column.setReorderable(false);
        }
        
        nazivColumn.setMinWidth(100);
        nazivColumn.setReorderable(false);
        
        adresaColumn.setMinWidth(100);
        
       // brojPosteColumn.set ;
        
        brojTelefonaColumn.setMinWidth(100);
        brojTelefonaColumn.setMaxWidth(100);
        
        brojPeronaColumn.setMinWidth(50);
        brojPeronaColumn.setMaxWidth(50);
        
        
        izmijeniColumn.setText("");
        //izmijeniColumn.setPrefWidth(50);
        izmijeniColumn.setMinWidth(50);
        izmijeniColumn.setMaxWidth(50);
        izmijeniColumn.setResizable(false);
        izmijeniColumn.setSortable(false);
        obrisiColumn.setText("");
        obrisiColumn.setMinWidth(50);
        obrisiColumn.setMaxWidth(50);
        obrisiColumn.setResizable(false);
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
        /*
		new Thread() {
        	@Override
        	public void run() {
        		Platform.runLater(() -> {
        			System.out.println(Thread.currentThread());
			        listaAutobuskihStanica.addAll(AutobuskaStanica.listaStanica());
			        progressPane.setVisible(false);
        		});
        	}
        }.run();
        */
		
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() /*throws Exception*/ {
            	System.out.println(Thread.currentThread());
                progressPane.setVisible(true);
                //for(int i=0; i<30; i++) {
                listaAutobuskihStanica.addAll(AutobuskaStanica.listaStanica());
            	//}
                //Thread.sleep(2000);
                return null;
            }
            @Override
            protected void succeeded(){
                super.succeeded();
                progressPane.setVisible(false);
            }
        };
        new Thread(task).start();
        
        /*
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {           
                    @Override
                    protected Void call() {
                    	System.out.println(Thread.currentThread());
                        progressPane.setVisible(true);
                        for(int i=0; i<30; i++) {
                        listaAutobuskihStanica.addAll(AutobuskaStanica.listaStanica());
                    	}
                        return null;
                    }
                    @Override
                    protected void succeeded(){
                        super.succeeded();
                        progressPane.setVisible(false);
                    }
                };
            }
        };
        service.start();
        */
	}
	
	/*
	public <S, T> void setButtonInColumn(TableColumn<AutobuskaStanica, AutobuskaStanica> tableColumn, ImageView url) {
		tableColumn.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
            );
        
        tableColumn.setCellFactory(tableCell -> {
            TableCell<AutobuskaStanica, AutobuskaStanica> cell = new TableCell<AutobuskaStanica, AutobuskaStanica>() {
                //private ImageView imageView = new ImageView("org/unibl/etf/administrator/img/edit.png");
            	private Button imageView = new Button("");
            	//postaviti dimenzije
                @Override
                protected void updateItem(AutobuskaStanica item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                    	imageView.getStyleClass().addAll("buttonTable", "buttonTableEdit");
                    	//imageView.setStyle("-fx-background-image: url('org/unibl/etf/administrator/img/edit.png')");
                    	setGraphic(imageView);
                    	imageView.setOnMouseClicked(
                    			event -> getTableView().getItems().remove(item)
                    		);
                    } else {
                    	setGraphic(null);
                    }
                }
            };
            return cell;
        });
	}
	*/
}
