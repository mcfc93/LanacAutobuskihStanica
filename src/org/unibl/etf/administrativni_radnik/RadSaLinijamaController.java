package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;

import org.unibl.etf.autobuska_stanica.AutobuskaStanica;
import org.unibl.etf.karta.Linija;
import org.unibl.etf.karta.Prevoznik;
import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.salterski_radnik.SalterskiRadnikController;
import org.unibl.etf.util.Util;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RadSaLinijamaController implements Initializable {

	public static ObservableList<Linija> linijeObsList = FXCollections.observableArrayList();
	public static Linija odabranaLinija;

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
		linijeObsList.clear();
		ucitajLinije();
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
            	//postaviti dimenzije
                @Override
                protected void updateItem(Linija item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                    	//System.out.println(item);
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
             	//postaviti dimenzije
                 @Override
                 protected void updateItem(Linija item, boolean empty) {
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
                     			event -> izbrisiLiniju(item)
                         );
                     } else {
                     	setGraphic(null);
                     }
                 }
             };
             return cell;
         });
         
        /* deaktivirajColumn.setCellFactory(tableCell -> {
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
         });*/
    	
	}
	
	
	public boolean showPotvrda() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("");
		alert.setHeaderText("Da li ste sigurni?");
		Optional<ButtonType> action = alert.showAndWait();
		return action.get().equals(ButtonType.OK);
	}
	
	public void ucitajLinije() {
    	String sqlQuery = "select IdLinije,NazivLinije,Peron,NazivPrevoznika,DaniUSedmici,linija.Stanje from linija join prevoznik on (linija.JIBPrevoznika=prevoznik.JIBPrevoznika) where linija.Stanje!='Izbrisano'";
    	Connection c = null;
    	ResultSet r = null;
    	PreparedStatement s = null;
    	try {
			c = Util.getConnection();
			s = c.prepareStatement(sqlQuery);
			r = s.executeQuery();
			while(r.next()) {
				Linija linija = new Linija(r.getInt("IdLinije"), r.getString("NazivLinije"), r.getString("DaniUSedmici"), r.getInt("Peron"),r.getString("NazivPrevoznika"),r.getString("Stanje"));
				linijeObsList.add(linija);
			}
		} catch (SQLException e) {
			// TODO: handle exception
		}
    	finally {
			Util.close(r, s, c);
		}
    	
	}
	

	private void izbrisiLiniju(Linija item) {
		if(showPotvrda()) {
		linijeObsList.remove(item);
		Connection c = null;
		PreparedStatement s = null;
		String sql = "update linija set Stanje='Izbrisano' where IdLinije=?";
		try {
			c = Util.getConnection();
			s = c.prepareStatement(sql);
			s.setInt(1, item.getIdLinije());
			if(s.executeUpdate()==1)
				showUspjesnoUklonjenaLinija();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(s, c);
		}
		}
	}
	

	public void showIzmjenaLinije(Linija linija) {
		try {
			odabranaLinija = linija;
			System.out.println("Odabrana linija:" + odabranaLinija);
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/unibl/etf/administrativni_radnik/IzmjenaLinije.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(linija.getNazivLinije());
            stage.setScene(new Scene(root1));  
            stage.showAndWait();
            int index = linijeObsList.indexOf(linija);
            linijeObsList.remove(linija);
            System.out.println("Novo stanje: " + odabranaLinija.getStanje());
            linijeObsList.add(index, odabranaLinija);
            linijeTableView.refresh();
		} catch(Exception e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}

	private void showUspjesnoUklonjenaLinija() {
		// TODO Auto-generated method stub
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Uspjeh");
		alert.setHeaderText("Uspjesno uklonjena linija");
		alert.showAndWait();
	}
	
	

}
