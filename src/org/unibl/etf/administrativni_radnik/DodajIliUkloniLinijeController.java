package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class DodajIliUkloniLinijeController implements Initializable {

	private static List<String> daniUSedmiciList = new ArrayList<>();
	private static String daniString="";
	private static ObservableList<Linija> linijeObsList = FXCollections.observableArrayList();
	public static Linija odabranaLinija;
	public static ObservableList<Integer> peroniObs = FXCollections.observableArrayList();
	private int brojPerona;
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
	private JFXComboBox<Integer> peronComboBox = new JFXComboBox();
	@FXML
	private JFXTextField nazivPrevoznikaTextField = new JFXTextField();
	@FXML
	private JFXTextField nazivLinijeTextField = new JFXTextField();
	@FXML
	private JFXButton dodajLinijuButton = new JFXButton();
	@FXML
	private JFXCheckBox ponedjeljakCB = new JFXCheckBox();
	@FXML
	private JFXCheckBox utorakCB = new JFXCheckBox();
	@FXML
	private JFXCheckBox srijedaCB = new JFXCheckBox();
	@FXML
	private JFXCheckBox cetvrtakCB = new JFXCheckBox();
	@FXML
	private JFXCheckBox petakCB = new JFXCheckBox();
	@FXML
	private JFXCheckBox subotaCB = new JFXCheckBox();
	@FXML
	private JFXCheckBox nedjeljaCB = new JFXCheckBox();
	
	private Set<String> prevozniciSet = new HashSet<>();
	public static int idLinije;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ucitajPrevoznike();
		autoComplete();
		// TODO Auto-generated method stub
		loadCBListeners();
		brojPerona = getBrojPerona();
		for(int i=1;i<=brojPerona;++i)
			peronComboBox.getItems().add(i);
		peronComboBox.setVisibleRowCount(3);
		peronComboBox.getSelectionModel().selectFirst();
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
    	
    	
    	
    	String sqlQuery = "select IdLinije,NazivLinije,Peron,NazivPrevoznika,DaniUSedmici from linija join prevoznik on (linija.JIBPrevoznika=prevoznik.JIBPrevoznika)";
    	Connection c = null;
    	ResultSet r = null;
    	PreparedStatement s = null;
    	try {
			c = Util.getConnection();
			s = c.prepareStatement(sqlQuery);
			r = s.executeQuery();
			while(r.next()) {
				Linija linija = new Linija(r.getInt("IdLinije"), r.getString("NazivLinije"), r.getString("DaniUSedmici"), r.getInt("Peron"),r.getString("NazivPrevoznika"));
				linijeObsList.add(linija);
			}
		} catch (SQLException e) {
			// TODO: handle exception
		}
	}
	
	public void ucitajPrevoznike() {
		// TODO Auto-generated method stub
		Connection c = null;
		String sql = "select NazivPrevoznika from prevoznik";
		PreparedStatement s = null;
		ResultSet r =null;
		try {
			c = Util.getConnection();
			s = c.prepareStatement(sql);
			r = s.executeQuery();
			while(r.next())
				prevozniciSet.add(r.getString("NazivPrevoznika"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(r, s, c);
		}
	}

	public void autoComplete() {
		// TODO Auto-generated method stub
				JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
			    autoCompletePopup.getSuggestions().addAll(prevozniciSet );
			    autoCompletePopup.setSelectionHandler(event -> {
			        nazivPrevoznikaTextField.setText(event.getObject());
			    });
			    nazivPrevoznikaTextField.textProperty().addListener(observable -> {
			        autoCompletePopup.filter(string -> string.toLowerCase().contains(nazivPrevoznikaTextField.getText().toLowerCase()));
			        if (autoCompletePopup.getFilteredSuggestions().isEmpty() || nazivPrevoznikaTextField.getText().isEmpty()) {
			            autoCompletePopup.hide();
			        } else {
			            autoCompletePopup.show(nazivPrevoznikaTextField);
			        }
			    });
			    
	}

	private int getBrojPerona() {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		System.out.println("JIB stanice: " + PrijavaController.nalog.getIdStanice());
		String sql = "select BrojPerona from autobuska_stanica where JIBStanice=?";
		try {
			c = Util.getConnection();
			s = c.prepareStatement(sql);
			s.setString(1, PrijavaController.nalog.getIdStanice());
			r = s.executeQuery();
			if(r.next()) {
				return r.getInt(1);
			}
			else
				return 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(r, s, c);
		}
		return 0;
	}

	private void izbrisiLiniju(Linija item) {
		linijeObsList.remove(item);
		Connection c = null;
		PreparedStatement s = null;
		String sql = "delete from linija where IdLinije=?";
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
	


	private void showIzmjenaLinije(Linija linija) {
		odabranaLinija = linija;
		for (Linija linijaObs : linijeObsList) {
			if(linijaObs.equals(linija))
				linijeTableView.getSelectionModel().select(linijaObs);
		}
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/unibl/etf/administrativni_radnik/IzmjenaLinije.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(linija.getNazivLinije());
            stage.setScene(new Scene(root1));  
            stage.show();
		} catch(Exception e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
	
	public void loadCBListeners() {
		// TODO Auto-generated method stub
		ponedjeljakCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if(newValue) 
		        	System.out.println(daniUSedmiciList.add(DayOfWeek.MONDAY.toString()));
		        else 
		        	daniUSedmiciList.remove(DayOfWeek.MONDAY.toString());
		        
		    }
		});
		utorakCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if(newValue) 
		        	daniUSedmiciList.add(DayOfWeek.TUESDAY.toString());
		        else 
		        	System.out.println(daniUSedmiciList.remove(DayOfWeek.TUESDAY.toString()));
		        
		    }
		});
		srijedaCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if(newValue) 
		        	daniUSedmiciList.add(DayOfWeek.WEDNESDAY.toString());
		        else 
		        	daniUSedmiciList.remove(DayOfWeek.WEDNESDAY.toString());
		        
		    }
		});
		cetvrtakCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if(newValue) 
		        	daniUSedmiciList.add(DayOfWeek.THURSDAY.toString());
		        else 
		        	daniUSedmiciList.remove(DayOfWeek.THURSDAY.toString());
		        
		    }
		});
		petakCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if(newValue) 
		        	daniUSedmiciList.add(DayOfWeek.FRIDAY.toString());
		        else 
		        	daniUSedmiciList.remove(DayOfWeek.FRIDAY.toString());
		        
		    }
		});
		subotaCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if(newValue) 
		        	daniUSedmiciList.add(DayOfWeek.SATURDAY.toString());
		        else 
		        	daniUSedmiciList.remove(DayOfWeek.SATURDAY.toString());
		        
		    }
		});
		nedjeljaCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if(newValue) 
		        	daniUSedmiciList.add(DayOfWeek.SUNDAY.toString());
		        else 
		        	daniUSedmiciList.remove(DayOfWeek.SUNDAY.toString());
		        
		    }
		});
		
	}

	@FXML
	public void dodajLiniju() {
		if(showPotvrda()) {
		String jibPrevoznika = getJibPrevoznika();
		mapiranjeDana();
		System.out.println("Dani: " + daniString);
		System.out.println("TRazeni jib:" + jibPrevoznika);
		String sql = "insert into linija value (default,?,?,?,?)";
		Connection c = null;
		PreparedStatement s = null;
		try {
			 c = Util.getConnection();
			 s  = c.prepareStatement(sql);
			 s.setString(1, nazivLinijeTextField.getText());
			 s.setInt(2, peronComboBox.getValue());
			 s.setString(3, jibPrevoznika);
			 s.setString(4, daniString);
			 System.out.println(s.executeUpdate());
			 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(s, c);
		}
		}
	}

	
	
	public String getJibPrevoznika() {
		String sql = "select JIBPrevoznika from prevoznik where NazivPrevoznika=?";
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r= null;
		try {
			 c = Util.getConnection();
			 s  = c.prepareStatement(sql);
			 s.setString(1, nazivPrevoznikaTextField.getText());
			 r = s.executeQuery();
			 if(r.next()) {
				 System.out.println("JIB PRE: " + r.getString(1));
				 return r.getString(1);
			 }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(r, s, c);
		}
		return null;
	}

	public boolean showPotvrda() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("");
		alert.setHeaderText("Da li ste sigurni?");
		Optional<ButtonType> action = alert.showAndWait();
		return action.get().equals(ButtonType.OK);
	}
	
	@FXML
	public void ukloniLiniju() {
		
	}


	private void showUspjesnoUklonjenaLinija() {
		// TODO Auto-generated method stub
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Uspjeh");
		alert.setHeaderText("Uspjesno uklonjena linija");
		alert.showAndWait();
	}
	
	public void mapiranjeDana() {
		for(int i=0;i<daniUSedmiciList.size()-1;++i)
			daniString += daniUSedmiciList.get(i) + ",";
		daniString += daniUSedmiciList.get(daniUSedmiciList.size()-1);
		System.out.println(daniString);
	}

	public void clearCB() {
		ponedjeljakCB.setSelected(false);
		utorakCB.setSelected(false);
		srijedaCB.setSelected(false);
		cetvrtakCB.setSelected(false);
		petakCB.setSelected(false);
		subotaCB.setSelected(false);
		nedjeljaCB.setSelected(false);
		daniUSedmiciList.clear();
		daniString="";
	}

}
