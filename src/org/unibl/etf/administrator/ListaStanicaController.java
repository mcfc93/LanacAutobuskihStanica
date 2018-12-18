package org.unibl.etf.administrator;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import org.controlsfx.control.MaskerPane;
import org.unibl.etf.autobuska_stanica.*;
import org.unibl.etf.util.Util;


public class ListaStanicaController implements Initializable {
	
	@FXML
	AnchorPane anchorPane;
	
	@FXML
	private TableView<AutobuskaStanica> autobuskeStaniceTable;
	
	@FXML
	private TableColumn<?, ?> jibColumn;
	
	@FXML
    private TableColumn<?, ?> nazivColumn;

    @FXML
    private TableColumn<?, ?> adresaColumn;

    @FXML
    private TableColumn<?, ?> brojTelefonaColumn;
    
    @FXML
    private TableColumn<?, ?> brojPeronaColumn;
    
    @FXML
    private TableColumn<AutobuskaStanica, AutobuskaStanica> blokirajColumn;
    
    @FXML
    private TableColumn<AutobuskaStanica, AutobuskaStanica> izmjeniColumn;
    
    @FXML
    private TableColumn<AutobuskaStanica, AutobuskaStanica> obrisiColumn;
    
    private ObservableList<AutobuskaStanica> listaAutobuskihStanica;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//tabela
    	listaAutobuskihStanica=FXCollections.observableArrayList();
    	autobuskeStaniceTable.setItems(listaAutobuskihStanica);
    	
    	autobuskeStaniceTable.setPlaceholder(new Label("Nema autobuskih stanica u tabeli."));
    	autobuskeStaniceTable.setFocusTraversable(false);
    	
    	jibColumn.setCellValueFactory(new PropertyValueFactory<>("jib"));
    	nazivColumn.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        adresaColumn.setCellValueFactory(new PropertyValueFactory<>("adresa"));
        brojTelefonaColumn.setCellValueFactory(new PropertyValueFactory<>("brojTelefona"));
        brojPeronaColumn.setCellValueFactory(new PropertyValueFactory<>("brojPerona"));

        blokirajColumn.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
            );
        
        blokirajColumn.setCellFactory(tableCell -> {
            TableCell<AutobuskaStanica, AutobuskaStanica> cell = new TableCell<AutobuskaStanica, AutobuskaStanica>() {
                private Button button = new Button("");
            	//postaviti dimenzije
                @Override
                protected void updateItem(AutobuskaStanica item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                    	//System.out.println(item);
                    	if("Blokirano".equals(item.getStanje())) {
                    		button.getStyleClass().addAll("buttonTable", "buttonTableUnblock");
                    		button.setTooltip(new Tooltip("Odblokiraj?"));
                    	} else {
                    		button.getStyleClass().addAll("buttonTable", "buttonTableBlock");
                    		button.setTooltip(new Tooltip("Blokiraj?"));
                    	}
                    	button.getTooltip().setAutoHide(false);
                    	button.getTooltip().setShowDelay(Duration.seconds(0.5));
                    	setGraphic(button);
                    	button.setOnMouseClicked(
                    			event -> {
                    				if("Blokirano".equals(item.getStanje())) {
                    					AutobuskaStanica.blokiranjeAutobuskeStanice(item.getJib(), "Aktivno");
                    					item.setStanje("Aktivno");
                    					button.getStyleClass().remove("buttonTableUnblock");
                    					button.getStyleClass().add("buttonTableBlock");
                    				} else {
                    					AutobuskaStanica.blokiranjeAutobuskeStanice(item.getJib(), "Blokirano");
                    					item.setStanje("Blokirano");
                    					button.getStyleClass().remove("buttonTableBlock");
                    					button.getStyleClass().add("buttonTableUnblock");
                    				}
                    			}
                    	);
                    } else {
                    	setGraphic(null);
                    }
                }
            };
            return cell;
        });
        
        izmjeniColumn.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
            );
        
        izmjeniColumn.setCellFactory(tableCell -> {
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
                    			event -> {
                    				try {
                    					IzmjeniStanicuController.autobuskaStanica=item;
                    					
                    					Parent root = FXMLLoader.load(getClass().getResource("/org/unibl/etf/administrator/IzmjeniStanicu.fxml"));
                    					Scene scene = new Scene(root);
                    					Stage stage=new Stage();
                    					stage.setScene(scene);
                    					stage.setResizable(false);
                    					stage.initStyle(StageStyle.UNDECORATED);
                    					stage.initModality(Modality.APPLICATION_MODAL);
                    					stage.showAndWait();
                    					
                    					int index=listaAutobuskihStanica.indexOf(item);
                    					listaAutobuskihStanica.remove(item);
                    			    	listaAutobuskihStanica.add(index, IzmjeniStanicuController.autobuskaStanica);
                    					autobuskeStaniceTable.refresh();
                    				} catch(Exception e) {
                    					Util.LOGGER.log(Level.SEVERE, e.toString(), e);
                    				}
                    			}
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
                    		event -> {
                    			Alert alert=new Alert(AlertType.CONFIRMATION);
            					alert.setTitle("Brisanje autobuske stanice");
            					alert.setHeaderText(null);
            					alert.setContentText("Obriši?");
            					alert.getButtonTypes().clear();
            				    alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
            					Button yesButton=(Button)alert.getDialogPane().lookupButton(ButtonType.YES);
            					yesButton.setText("Da");
            					yesButton.setDefaultButton(false);
            					Button noButton=(Button)alert.getDialogPane().lookupButton(ButtonType.NO);
            					noButton.setText("Ne");
            					noButton.setDefaultButton(true);
            					
            					alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/unibl/etf/application.css").toExternalForm());
            					alert.getDialogPane().getStyleClass().addAll("alert", "alertDelete");
            					            					
            					Optional<ButtonType> rezultat = alert.showAndWait();

            					if (rezultat.get() == ButtonType.YES) {
	                    			AutobuskaStanica.brisanjeAutobuskeStanice(item.getJib());
	                				getTableView().getItems().remove(item);
	                				System.out.println("Obrisano: " + item);
            					}
                    		}
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
        
        jibColumn.setMinWidth(95);
        jibColumn.setMaxWidth(150);
        
        nazivColumn.setMinWidth(100);
        
        adresaColumn.setMinWidth(95);
        
        brojTelefonaColumn.setMinWidth(100);
        brojTelefonaColumn.setMaxWidth(150);
        
        brojPeronaColumn.setMinWidth(45);
        brojPeronaColumn.setMaxWidth(75);
        
        blokirajColumn.setText("");
        blokirajColumn.setMinWidth(35);
        blokirajColumn.setMaxWidth(35);
        blokirajColumn.setResizable(false);
        blokirajColumn.setSortable(false);
        izmjeniColumn.setText("");
        izmjeniColumn.setMinWidth(35);
        izmjeniColumn.setMaxWidth(35);
        izmjeniColumn.setResizable(false);
        izmjeniColumn.setSortable(false);
        obrisiColumn.setText("");
        obrisiColumn.setMinWidth(35);
        obrisiColumn.setMaxWidth(35);
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
