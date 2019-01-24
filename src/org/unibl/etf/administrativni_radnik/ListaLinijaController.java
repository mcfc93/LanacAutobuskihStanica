package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.controlsfx.control.MaskerPane;
import org.unibl.etf.karta.Linija;
import org.unibl.etf.util.Stajaliste;
import org.unibl.etf.util.Util;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class ListaLinijaController implements Initializable {

	public static ObservableList<Linija> linijeObsList;
	public static List<Stajaliste> stajalistaList = new ArrayList<>();
	public static Linija odabranaLinija;
	public static int idLinije;

	@FXML
	private AnchorPane anchorPane = new AnchorPane();
	@FXML
	private TableView<Linija> linijeTableView = new TableView<>();
	@FXML
	private TableColumn<Linija,String> nazivLinijeColumn = new TableColumn<Linija, String>();
	@FXML
	private TableColumn<Linija,Integer> idLinijeLinijeColumn = new TableColumn<Linija, Integer>();
	@FXML
	private TableColumn<Linija,String> nazivPrevoznikaColumn = new TableColumn<Linija, String>();
	@FXML
	private TableColumn<Linija,Integer> peronLinijeColumn = new TableColumn<Linija, Integer>();
	@FXML
	private TableColumn<Linija,Linija> blokirajColumn = new TableColumn<Linija,Linija>();
	@FXML
	private TableColumn<Linija,Linija> izmjeniColumn = new TableColumn<Linija,Linija>();
	@FXML
	private TableColumn<Linija,Linija> obrisiColumn = new TableColumn<Linija,Linija>();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		stajalistaList = Stajaliste.getStajalisteList();
		
		linijeObsList = FXCollections.observableArrayList();
		linijeTableView.setItems(linijeObsList);
		linijeTableView.setPlaceholder(new Label("Nema linija u tabeli."));
    	nazivLinijeColumn.setCellValueFactory(new PropertyValueFactory<>("nazivLinije"));
    	idLinijeLinijeColumn.setCellValueFactory(new PropertyValueFactory<>("idLinije"));
    	nazivPrevoznikaColumn.setCellValueFactory(new PropertyValueFactory<>("nazivPrevoznika"));
    	peronLinijeColumn.setCellValueFactory(new PropertyValueFactory<>("peron"));
    	
    	blokirajColumn.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
            );
        
        blokirajColumn.setCellFactory(tableCell -> {
            TableCell<Linija, Linija> cell = new TableCell<Linija, Linija>() {
                private Button button = new Button("");
            	//postaviti dimenzije
                @Override
                protected void updateItem(Linija item, boolean empty) {
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
                    					if(Linija.blokiranjeLinije(item.getIdLinije(), "Aktivno")) {
                    						item.setStanje("Aktivno");
                    						button.getStyleClass().remove("buttonTableUnblock");
                    						button.getStyleClass().add("buttonTableBlock");
                    						button.getTooltip().setText("Blokiraj?");
                    						
                    						Util.getNotifications("Obavještenje", "Linija odblokirana.", "Information").show();
                    					} else {
                    						//NASTALA GRESKA
                    						Util.showBugAlert();
                    					}
                    				} else {
                    					if(Linija.blokiranjeLinije(item.getIdLinije(), "Blokirano")) {
	                    					item.setStanje("Blokirano");
	                    					button.getStyleClass().remove("buttonTableBlock");
	                    					button.getStyleClass().add("buttonTableUnblock");
	                    					button.getTooltip().setText("Odblokiraj?");
	                    					
	                    					Util.getNotifications("Obavještenje", "Linija blokirana.", "Information").show();
                    					} else {
                    						//NASTALA GRESKA
                    						Util.showBugAlert();
                    					}
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
    	linijeTableView.setFocusTraversable(false);
    	izmjeniColumn.setCellFactory(tableCell -> {
            TableCell<Linija, Linija> cell = new TableCell<Linija, Linija>() {
                private Button button = new Button("");
                @Override
                protected void updateItem(Linija item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                    	button.getStyleClass().addAll("buttonTable", "buttonTableEdit");
                    	button.setTooltip(new Tooltip("Izmijeni?"));
                    	button.getTooltip().setAutoHide(false);
                    	button.getTooltip().setShowDelay(Duration.seconds(0.5));
                    	setGraphic(button);
                    	button.setOnMouseClicked(
                    			event -> showIzmjenaLinije(item) 
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
             TableCell<Linija, Linija> cell = new TableCell<Linija, Linija>() {
                 private Button button = new Button("");
                 @Override
                 protected void updateItem(Linija item, boolean empty) {
                     super.updateItem(item, empty);
                     if (!empty) {
                     	//postavljanje CSS
                     	button.getStyleClass().addAll("buttonTable", "buttonTableDelete");
                     	//postavljanje opisa
                     	button.setTooltip(new Tooltip("Obriši?"));
                     	button.getTooltip().setAutoHide(false);
                     	button.getTooltip().setShowDelay(Duration.seconds(0.5));
                     	//dodavanje u kolonu
                     	setGraphic(button);
                     	button.setOnMouseClicked(
                     			event ->  {  
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
                     					MaskerPane progressPane=Util.getMaskerPane(anchorPane);
            							Task<Void> task = new Task<Void>() {
            					            @Override
            					            protected Void call() /*throws Exception*/ {
            					                progressPane.setVisible(true);
                             					if(Linija.izbrisiLiniju(item)) {
                             						//linijeObsList.remove(item);
                             						getTableView().getItems().remove(item);
            		                				System.out.println("Obrisano: " + item);
            		                				Platform.runLater(() -> {
            		                					Util.getNotifications("Obavještenje", "Linija obrisana.", "Information").show();
            		                				});
            		                				progressPane.setVisible(false);
            		                			} else {
            	                    				//NASTALA GRESKA
            	                    				Util.showBugAlert();
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
                         );
                     } else {
                     	setGraphic(null);
                     }
                 }
				
             };
             return cell;
         });
         for(TableColumn<?,?> column:linijeTableView.getColumns()) 
         	column.setReorderable(false);
         idLinijeLinijeColumn.setMinWidth(50);
         idLinijeLinijeColumn.setMaxWidth(70);
         peronLinijeColumn.setMinWidth(50);
         peronLinijeColumn.setMaxWidth(70);
         
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
         
         MaskerPane progressPane = Util.getMaskerPane(anchorPane);
         Task<Void> task = new Task<Void>() {
             @Override
             protected Void call() /*throws Exception*/ {
             	System.out.println(Thread.currentThread());
                 progressPane.setVisible(true);
                 linijeObsList.addAll(Linija.getLinije());
                 return null;
             }
             @Override
             protected void succeeded(){
                 super.succeeded();
                 progressPane.setVisible(false);
             }
         };
         new Thread(task).start();
         
        linijeTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Linija>() {

			@Override
			public void changed(ObservableValue<? extends Linija> observable, Linija oldValue, Linija newValue) {
				System.out.println("new: " + newValue);
				System.out.println("old: " + oldValue);
			}
		});
	}
	
	public void showIzmjenaLinije(Linija linija) {
		try {
			odabranaLinija = linija;
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/unibl/etf/administrativni_radnik/IzmjenaLinije.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
        	stage.setResizable(false);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(linija.getNazivLinije());
            stage.setScene(new Scene(root1));  
            stage.showAndWait();
            int index = linijeObsList.indexOf(linija);
            linijeObsList.remove(linija);
            linijeObsList.add(index, odabranaLinija);
            linijeTableView.refresh();
		} catch(Exception e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}

}
