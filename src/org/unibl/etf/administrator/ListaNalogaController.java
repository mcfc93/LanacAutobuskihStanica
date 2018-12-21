package org.unibl.etf.administrator;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import org.controlsfx.control.MaskerPane;
import org.unibl.etf.prijava.Nalog;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Duration;

public class ListaNalogaController implements Initializable {
	
	@FXML
	private AnchorPane anchorPane;
	
	@FXML
    private TableView<Nalog> naloziTable;

    @FXML
    private TableColumn<?, ?> korisnickoImeColumn;
    
    @FXML
    private TableColumn<?, ?> imeColumn;

    @FXML
    private TableColumn<?, ?> prezimeColumn;

    @FXML
    private TableColumn<?, ?> jmbgColumn;

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
		
		naloziTable.setPlaceholder(new Label("Nema korisničkih naloga u tabeli."));
		naloziTable.setFocusTraversable(false);
		
		korisnickoImeColumn.setCellValueFactory(new PropertyValueFactory<>("korisnickoIme"));
		imeColumn.setCellValueFactory(new PropertyValueFactory<>("ime"));
		prezimeColumn.setCellValueFactory(new PropertyValueFactory<>("prezime"));
		jmbgColumn.setCellValueFactory(new PropertyValueFactory<>("jmbg"));
		tipColumn.setCellValueFactory(new PropertyValueFactory<>("tip"));
		idStaniceColumn.setCellValueFactory(new PropertyValueFactory<>("idStanice"));
		
		
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
                    			Alert alert=new Alert(AlertType.CONFIRMATION);
            					alert.setTitle("Brisanje korisničkog naloga");
            					alert.setHeaderText(null);
            					alert.setContentText("Obrši?");
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
            						if(!"Administrator".equals(item.getTip())) {
		                    			Nalog.brisanjeNaloga(item.getZaposleni().getJmbg());
		                    			getTableView().getItems().remove(item);
		                				System.out.println("Obrisano: " + item);
            						} else {
            							Alert alertWarning=new Alert(AlertType.WARNING);
            				    		alertWarning.setTitle("Greška");
            				    		alertWarning.setHeaderText(null);
            				    		alertWarning.setContentText("Nije moguće obrisati Administratorski nalog.");
            				    		/*
            				    		DialogPane dialogPaneWarning = alertWarning.getDialogPane();
            							dialogPaneWarning.getStylesheets().add(getClass().getResource("/org/unibl/etf/application.css").toExternalForm());
            							dialogPaneWarning.getStyleClass().add("alert");
            							*/
            							alertWarning.getDialogPane().getStylesheets().add(getClass().getResource("/org/unibl/etf/application.css").toExternalForm());
            							alertWarning.getDialogPane().getStyleClass().add("alert");
            							/*
            				    		Label label = new Label("Label:");
            				    		
            				    		TextArea textArea=new TextArea("Detalji");
            				    		textArea.setFocusTraversable(false);
            				    		textArea.setEditable(false);
            				    		textArea.setWrapText(true);
            				    		
            				    		textArea.setMaxWidth(Double.MAX_VALUE);
            				    	    textArea.setMaxHeight(Double.MAX_VALUE);
            				    	    GridPane.setVgrow(textArea, Priority.ALWAYS);
            				    	    GridPane.setHgrow(textArea, Priority.ALWAYS);

            				    	    GridPane expContent = new GridPane();
            				    	    expContent.setMaxWidth(Double.MAX_VALUE);
            				    	    expContent.add(label, 0, 0);
            				    		expContent.add(textArea, 0, 1);
            				    		
            				    		alertWarning.getDialogPane().setExpandableContent(expContent);
            				    		*/
            				    		alertWarning.showAndWait();
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
		
		for(TableColumn<?,?> column: naloziTable.getColumns()) {
        	column.setReorderable(false);
		}
		
		korisnickoImeColumn.setMinWidth(100);
		korisnickoImeColumn.setMaxWidth(350);
		
		imeColumn.setMinWidth(70);
		imeColumn.setMaxWidth(350);
		
		prezimeColumn.setMinWidth(70);
		prezimeColumn.setMaxWidth(350);
		
		jmbgColumn.setMinWidth(80);
		jmbgColumn.setMaxWidth(125);
		
		tipColumn.setMinWidth(95);
		tipColumn.setMaxWidth(150);
		
		idStaniceColumn.setMinWidth(80);
		idStaniceColumn.setMaxWidth(125);
		
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
