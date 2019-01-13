package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.controlsfx.control.MaskerPane;
import org.unibl.etf.karta.Prevoznik;
import org.unibl.etf.util.Util;
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

public class ListaPrevoznikaController implements Initializable {

	public static ObservableList<Prevoznik> prevozniciObsList;
	
	@FXML
	private AnchorPane anchorPane = new AnchorPane();
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
	@FXML
	private TableColumn<Prevoznik,Prevoznik> izmjeniColumn = new TableColumn<>();
	@FXML
	private TableColumn<Prevoznik,Prevoznik> obrisiColumn = new TableColumn<>();

	public static Prevoznik odabraniPrevoznik;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prevozniciObsList = FXCollections.observableArrayList();
		prevozniciTableView.setItems(prevozniciObsList);
		
		//prevozniciObsList.setAll(Prevoznik.getPrevozniciList());
		nazivColumn.setCellValueFactory(new PropertyValueFactory<>("naziv"));
		adresaColumn.setCellValueFactory(new PropertyValueFactory<>("adresa"));
		telefonColumn.setCellValueFactory(new PropertyValueFactory<>("telefon"));
		racunColumn.setCellValueFactory(new PropertyValueFactory<>("racun"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
		
		izmjeniColumn.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
            );
    	izmjeniColumn.setCellFactory(tableCell -> {
            TableCell<Prevoznik, Prevoznik> cell = new TableCell<Prevoznik, Prevoznik>() {
                private Button button = new Button("");
            	//postaviti dimenzije
                @Override
                protected void updateItem(Prevoznik item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                    	button.getStyleClass().addAll("buttonTable", "buttonTableEdit");
                    	button.setTooltip(new Tooltip("Izmjeni?"));
                    	button.getTooltip().setAutoHide(false);
                    	button.getTooltip().setShowDelay(Duration.seconds(0.5));
                    	setGraphic(button);
                    	button.setOnMouseClicked(
                    			event -> showIzmjenaPrevoznika(item) 
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
    		TableCell<Prevoznik, Prevoznik> cell = new TableCell<Prevoznik, Prevoznik>() {
        		private Button button = new Button("");
        		//postaviti dimenzije
                @Override
                protected void updateItem(Prevoznik item, boolean empty) {
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
							event -> {
								Alert alert=new Alert(AlertType.CONFIRMATION);
								alert.setTitle("Brisanje prevoznika");
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
									if(Prevoznik.izbrisiPrevoznika(item)) {
										getTableView().getItems().remove(item);
										//prevozniciObsList.remove(item);

								        Util.getNotifications("Obavještenje", "Prevoznik obrisan.", "Information").show();
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
    	
    	for(TableColumn<?,?> column:prevozniciTableView.getColumns()) {
        	column.setReorderable(false);
        }
    	
    	//jibColumn.setMinWidth(95);
        //jibColumn.setMaxWidth(150);
        
        nazivColumn.setMinWidth(85);
        //nazivColumn.setMaxWidth(200);
        
        racunColumn.setMinWidth(100);
        //racunColumn.setMaxWidth(175);
        
        adresaColumn.setMinWidth(85);
        //adresaColumn.setMaxWidth(200);
        
        telefonColumn.setMinWidth(100);
        telefonColumn.setMaxWidth(150);
        
        emailColumn.setMinWidth(100);
        //emailColumn.setMaxWidth(175);
        
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
        
        MaskerPane progressPane = Util.getMaskerPane(anchorPane);/*= new MaskerPane();
		progressPane.setText("Molimo sačekajte...");
		progressPane.setVisible(false);
		anchorPane.getChildren().add(progressPane);
		AnchorPane.setTopAnchor(progressPane,0.0);
		AnchorPane.setBottomAnchor(progressPane,0.0);
		AnchorPane.setLeftAnchor(progressPane,0.0);
		AnchorPane.setRightAnchor(progressPane,0.0);*/
		
		Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() /*throws Exception*/ {
            	System.out.println(Thread.currentThread());
                progressPane.setVisible(true);
                prevozniciObsList.addAll(Prevoznik.getPrevozniciList());
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
	
	public void showIzmjenaPrevoznika(Prevoznik prevoznik) {
		try {
			odabraniPrevoznik = prevoznik;
			prevozniciTableView.getSelectionModel().select(prevoznik);
			int index = prevozniciObsList.indexOf(prevoznik);
			
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/unibl/etf/administrativni_radnik/IzmjenaPrevoznika.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(prevoznik.getNaziv());
            stage.setScene(new Scene(root1));  
            stage.setResizable(false);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            prevozniciObsList.remove(prevoznik);
            prevozniciObsList.add(index, odabraniPrevoznik);
            prevozniciTableView.refresh();
            prevozniciTableView.getSelectionModel().select(prevoznik);
		} catch(Exception e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}

}
