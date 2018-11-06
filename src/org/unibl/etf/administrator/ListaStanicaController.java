package org.unibl.etf.administrator;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.beans.property.ReadOnlyObjectWrapper;

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
        brojPosteColumn.setCellValueFactory(new PropertyValueFactory<>("brojPoste"));
        brojTelefonaColumn.setCellValueFactory(new PropertyValueFactory<>("brojTelefona"));
        brojPeronaColumn.setCellValueFactory(new PropertyValueFactory<>("brojPerona"));

        
        izmijeniColumn.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
            );
        
        izmijeniColumn.setCellFactory(tableCell -> {
            TableCell<AutobuskaStanica, AutobuskaStanica> cell = new TableCell<AutobuskaStanica, AutobuskaStanica>() {
                //private ImageView imageView = new ImageView("org/unibl/etf/administrator/img/edit.png");
            	private Button imageView = new Button("");
            	//postaviti dimenzije
                @Override
                protected void updateItem(AutobuskaStanica item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                    	//imageView.setStyle("-fx-background-image: url('org/unibl/etf/administrator/img/edit.png')");
                    	imageView.getStyleClass().addAll("buttonTable", "buttonTableEdit");
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
        
        obrisiColumn.setCellValueFactory(
        		param -> new ReadOnlyObjectWrapper<>(param.getValue())
        	);
        
        obrisiColumn.setCellFactory(tableCell -> {
            TableCell<AutobuskaStanica, AutobuskaStanica> cell = new TableCell<AutobuskaStanica, AutobuskaStanica>() {
                //private ImageView imageView = new ImageView("org/unibl/etf/administrator/img/delete.png");
            	private Button imageView = new Button("");
            	//postaviti dimenzije
                @Override
                protected void updateItem(AutobuskaStanica item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                    	//imageView.setStyle("-fx-background-image: url('org/unibl/etf/administrator/img/delete.png')");
                    	imageView.getStyleClass().addAll("buttonTable", "buttonTableDelete");
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
        
        nazivColumn.setMinWidth(100);
        
        adresaColumn.setMinWidth(100);
        
        
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
        
        new Thread() {
        	@Override
        	public void run() {
		        listaAutobuskihStanica.addAll(AutobuskaStanica.listaStanica());
		        listaAutobuskihStanica.addAll(AutobuskaStanica.listaStanica());
		        listaAutobuskihStanica.addAll(AutobuskaStanica.listaStanica());
		        listaAutobuskihStanica.addAll(AutobuskaStanica.listaStanica());
		        listaAutobuskihStanica.addAll(AutobuskaStanica.listaStanica());
		        listaAutobuskihStanica.addAll(AutobuskaStanica.listaStanica());
		        listaAutobuskihStanica.addAll(AutobuskaStanica.listaStanica());
		        listaAutobuskihStanica.addAll(AutobuskaStanica.listaStanica());
		        listaAutobuskihStanica.addAll(AutobuskaStanica.listaStanica());
		        
        	}
        }.run();
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
                    	//imageView.setStyle("-fx-background-image: url('org/unibl/etf/administrator/img/edit.png')");
                    	imageView.getStyleClass().addAll("buttonTable", "buttonTableEdit");
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
