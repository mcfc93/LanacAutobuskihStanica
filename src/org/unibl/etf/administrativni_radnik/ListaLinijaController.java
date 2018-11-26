package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.controlsfx.control.MaskerPane;
import org.unibl.etf.karta.Linija;
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
	public static Linija odabranaLinija;
	public static MaskerPane progressPane = new MaskerPane();

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
	private TableColumn<Linija,String> daniUSedmiciColumn = new TableColumn<Linija, String>();
	@FXML
	private TableColumn<Linija,Linija> izmijeniColumn = new TableColumn<Linija,Linija>();
	@FXML
	private TableColumn<Linija,Linija> izbrisiColumn = new TableColumn<Linija,Linija>();
	@FXML
	private TableColumn<Linija,Linija> deaktivirajColumn = new TableColumn<Linija,Linija>();
	
	
	public static int idLinije;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		linijeObsList = FXCollections.observableArrayList();
		linijeObsList.setAll(Linija.getLinije());
		linijeTableView.setItems(linijeObsList);
		linijeTableView.setPlaceholder(new Label("Nema linija u tabeli."));
    	nazivLinijeColumn.setCellValueFactory(new PropertyValueFactory<>("nazivLinije"));
    	idLinijeLinijeColumn.setCellValueFactory(new PropertyValueFactory<>("idLinije"));
    	nazivPrevoznikaColumn.setCellValueFactory(new PropertyValueFactory<>("nazivPrevoznika"));
    	peronLinijeColumn.setCellValueFactory(new PropertyValueFactory<>("peron"));
    	daniUSedmiciColumn.setCellValueFactory(new PropertyValueFactory<>("daniUSedmici"));
    	izmijeniColumn.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
            );
    	izmijeniColumn.setCellFactory(tableCell -> {
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
    	 izbrisiColumn.setCellValueFactory(
         		param -> new ReadOnlyObjectWrapper<>(param.getValue())
         	);
         
         izbrisiColumn.setCellFactory(tableCell -> {
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
                     				if(showPotvrda()) {
                     					maskerSetUp();
            							Task<Void> task = new Task<Void>() {
            					            @Override
            					            protected Void call() /*throws Exception*/ {
            					                progressPane.setVisible(true);
                             					Linija.izbrisiLiniju(item);
                             					linijeObsList.remove(item);
                             					return null;
            					            }
            					            @Override
            					            protected void succeeded(){
            					                super.succeeded();
            					                progressPane.setVisible(false);
            									showUspjesnoUklonjenaLinija();
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
        
	}
	public void maskerSetUp() {
	  	progressPane = new MaskerPane();
		progressPane.setText("Molimo sačekajte...");
		progressPane.setVisible(false);
		anchorPane.getChildren().add(progressPane);
		AnchorPane.setTopAnchor(progressPane,0.0);
		AnchorPane.setBottomAnchor(progressPane,0.0);
		AnchorPane.setLeftAnchor(progressPane,0.0);
		AnchorPane.setRightAnchor(progressPane,0.0);
		
	
}
	public boolean showPotvrda() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("");
		alert.setHeaderText("Da li ste sigurni?");
		Optional<ButtonType> action = alert.showAndWait();
		return action.get().equals(ButtonType.OK);
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

	private void showUspjesnoUklonjenaLinija() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Uspjeh");
		alert.setHeaderText("Uspjesno uklonjena linija");
		alert.showAndWait();
	}
	
	

}
