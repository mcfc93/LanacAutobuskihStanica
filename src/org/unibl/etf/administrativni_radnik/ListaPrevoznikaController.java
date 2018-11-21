package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import org.unibl.etf.karta.Prevoznik;
import org.unibl.etf.util.Util;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ListaPrevoznikaController implements Initializable {

	public static ObservableList<Prevoznik> prevozniciObsList = FXCollections.observableArrayList();
	
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
	private TableColumn<Prevoznik,Prevoznik> izmijeniColumn = new TableColumn<>();
	@FXML
	private TableColumn<Prevoznik,Prevoznik> izbrisiColumn = new TableColumn<>();

	public static Prevoznik odabraniPrevoznik;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prevozniciObsList.clear();
		prevozniciObsList.setAll(Prevoznik.getPrevozniciList());
		nazivColumn.setCellValueFactory(new PropertyValueFactory<>("naziv"));
		adresaColumn.setCellValueFactory(new PropertyValueFactory<>("adresa"));
		telefonColumn.setCellValueFactory(new PropertyValueFactory<>("telefon"));
		racunColumn.setCellValueFactory(new PropertyValueFactory<>("racun"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
		prevozniciTableView.setItems(prevozniciObsList);
		izmijeniColumn.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
            );
    	izmijeniColumn.setCellFactory(tableCell -> {
            TableCell<Prevoznik, Prevoznik> cell = new TableCell<Prevoznik, Prevoznik>() {
                private Button button = new Button("");
            	//postaviti dimenzije
                @Override
                protected void updateItem(Prevoznik item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                    	button.getStyleClass().addAll("buttonTable", "buttonTableEdit");
                    	button.setTooltip(new Tooltip("Izmijeni?"));
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
    	
    	 izbrisiColumn.setCellValueFactory(
          		param -> new ReadOnlyObjectWrapper<>(param.getValue())
          	);
          
          izbrisiColumn.setCellFactory(tableCell -> {
              TableCell<Prevoznik, Prevoznik> cell = new TableCell<Prevoznik, Prevoznik>() {
                  private Button button = new Button("");
              	//postaviti dimenzije
                  @Override
                  protected void updateItem(Prevoznik item, boolean empty) {
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
                      			event ->  { if(Prevoznik.izbrisiPrevoznika(item))
                      							showUspjesnoUklonjenPrevoznik();
                      						prevozniciObsList.remove(item);
                      						
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
	
	public void showUspjesnoUklonjenPrevoznik() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText("Uspjesno uklonjen prevoznik");
		alert.showAndWait();
	}

	public void showIzmjenaPrevoznika(Prevoznik prevoznik) {
		odabraniPrevoznik = prevoznik;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/unibl/etf/administrativni_radnik/IzmjenaPrevoznika.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(prevoznik.getNaziv());
            stage.setScene(new Scene(root1));  
            stage.showAndWait();
            int index = prevozniciObsList.indexOf(prevoznik);
            prevozniciObsList.remove(prevoznik);
            prevozniciObsList.add(index, odabraniPrevoznik);
            prevozniciTableView.refresh();
		} catch(Exception e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}


}
