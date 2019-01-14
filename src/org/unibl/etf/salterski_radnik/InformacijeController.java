package org.unibl.etf.salterski_radnik;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.controlsfx.control.MaskerPane;
import org.unibl.etf.karta.Karta;
import org.unibl.etf.karta.Relacija;
import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.util.Praznik;
import org.unibl.etf.util.Stajaliste;
import org.unibl.etf.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class InformacijeController implements Initializable{
	
	public static List<Stajaliste> stajalistaList = new ArrayList<>();
	private static ObservableList<Karta> karteObs = FXCollections.observableArrayList();
	public static Stajaliste stajaliste = new Stajaliste();
	
	@FXML
	private JFXButton pretragaButton = new JFXButton();
	@FXML
	private DatePicker datum = new DatePicker();
	@FXML
	private ToggleGroup toggleGroup = new ToggleGroup();
	@FXML
	private JFXRadioButton polasciRadioButton;
	@FXML
	private JFXRadioButton dolasciRadioButton;
	@FXML
	private JFXTextField mjestoTextField = new JFXTextField();
	private static String daniUSedmici;
	
	
	
	@FXML
	private GridPane gridPane;
	
	@FXML
	private AnchorPane tableAnchorPane;
	
	@FXML
    private TableView<Karta> karteTable;

    @FXML
    private TableColumn<Karta, String> nazivLinijeColumn;

    @FXML
    private TableColumn<Karta, LocalTime> vrijemePolaskaColumn;

    @FXML
    private TableColumn<Karta, LocalTime> vrijemeDolaskaColumn;

    @FXML
    private TableColumn<Karta, Double> cijenaColumn;

    @FXML
    private TableColumn<Karta, String> prevoznikColumn;

    @FXML
    private TableColumn<Karta, Integer> peronColumn;
	
	@FXML
    private TextField traziTextField;

    @FXML
    private ImageView clearImageView;
    
    @FXML
    private JFXComboBox<String> polasciDolasciComboBox;
    
    
    private static Thread thread;
	private static MaskerPane progressPane;
	private static volatile boolean kraj=false;
	private static volatile boolean pauza=false;
	public static Object lock=new Object();
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		stajalistaList = Stajaliste.getStajalisteList();
		
		// stajaliste = stajalistaList.stream().filter(s -> s.get()==)
		
		System.out.println("stajaliste stanice: " + PrijavaController.autobuskaStanica.getIdStajalista());
		clearImageView.setVisible(false);
		
		mjestoTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
        	//if(!newValue.isEmpty()) {
			if(!mjestoTextField.getText().isEmpty()) {
        		clearImageView.setVisible(true);
        		
        		pauza=true;
        		//System.out.println(pauza);
        		
        	} else {
        		clearImageView.setVisible(false);

        		pauza=false;
        		synchronized(lock) {
        			lock.notify();
        		}
        		//System.out.println(pauza);
        		
        	}
		});
		
		datum.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.compareTo(LocalDate.now()) < 0 );
                for(Praznik p: Praznik.getHolidayList()) {
	                if (MonthDay.from(item).equals(MonthDay.of(p.getMjesec(), p.getDan()))) {
	                    setTooltip(new Tooltip(p.getOpis()));
	                    //setStyle("-fx-background-color: #ff4444;");
	                    setTextFill(Color.RED);
	                }
                }
            }
        });
        datum.setEditable(false);
		
		
		
		
		
		
		
		
		
		karteTable.setItems(karteObs);
		datum.setValue(LocalDate.now());
		//nazivMjesta = getNazivMjesta();
	//	ucitajMjesta();
		karteTable.setPlaceholder(new Label("Odaberite relaciju i datum"));
		polasciRadioButton.setSelected(true);
		mjestoTextField.setPromptText("Destinacija");
		
		
		toggleSetUp();
		polasciRadioButton.setToggleGroup(toggleGroup);
		dolasciRadioButton.setToggleGroup(toggleGroup);
		nazivLinijeColumn.setCellValueFactory(new PropertyValueFactory<>("nazivLinije"));
		vrijemePolaskaColumn.setCellValueFactory(new PropertyValueFactory<>("vrijemePolaska"));
		vrijemeDolaskaColumn.setCellValueFactory(new PropertyValueFactory<>("vrijemeDolaska"));
		prevoznikColumn.setCellValueFactory(new PropertyValueFactory<>("nazivPrevoznika"));
		cijenaColumn.setCellValueFactory(new PropertyValueFactory<>("cijena"));
		peronColumn.setCellValueFactory(new PropertyValueFactory<>("peron"));
		
		Util.setAutocompleteList(mjestoTextField, stajalistaList.stream().map(Stajaliste::toString).collect(Collectors.toList()));
		//mjestoTextField.getValidators().addAll(Util.requredFieldValidator(mjestoTextField),Util.collectionValidator(mjestoTextField, mjestaSet, true, "Unesite mjesto"));
		//mjestoTextField.getValidators().add(Util.requiredFieldValidator(mjestoTextField));
		//mjestoTextField.getValidators().add(Util.collectionValidator(mjestoTextField, stajalistaList.stream().map(Stajaliste::toString).collect(Collectors.toList()), true, "Unesite mjesto"));
		polasciDolasciComboBox.getItems().addAll("POLASCI", "DOLASCI");
		polasciDolasciComboBox.getSelectionModel().selectFirst();
		polasciDolasciComboBox.setStyle("-fx-font-weight: bold;");
		
		
		
		Platform.runLater(() -> {
			((Stage)gridPane.getScene().getWindow()).setOnHiding(event -> {
				System.out.println("Closing Stage");
				kraj=true;
			});
		});
		
		
		kraj=false;
		progressPane = Util.getMaskerPane(tableAnchorPane);
		progressPane.setVisible(false);
		if(thread==null || !thread.isAlive()) {
			System.out.println("START - " + (thread==null? "NULL": thread.getState()));
			Task<Void> task = new Task<Void>() {
	            @Override
	            protected Void call() {
	            	System.out.println(Thread.currentThread());
	                //progressPane.setVisible(true);
	                //thread.start();
	                
	                while(!kraj) {
	    				if(pauza) {
	    					System.out.println("PAUZA");
	    					synchronized(lock) {
	    						try {
	    							lock.wait();
	    						} catch(InterruptedException e) {
	    							Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    						}
	    					}
	    					System.out.println("NASTAVAK");
	    				}
	    				
	    				
	    				Platform.runLater(() -> {
	    					progressPane.setVisible(true);
	    				});
	    				
	    				/**
	    				 * UCITAVANJE
	    				 */
	    				System.out.println("UCITAVANJE");
	    				try {
							Thread.sleep(2000);
						}catch (InterruptedException e) {
							Util.LOGGER.log(Level.SEVERE, e.toString(), e);
						}
	    				//karteObs.clear();
	    				karteObs.add(new Karta(new Relacija(1, 1, "BL - GR", 5, 7, new Stajaliste("Banja Luka"), new Stajaliste("Gradiska"), 7.5, 135.0)));
	    				
	    				Platform.runLater(() -> {
							progressPane.setVisible(false);
						});
	    				
	    				try {
	    					Thread.sleep(2000);
	    				}catch (InterruptedException e) {
	    					Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    				}
	    			}
	                
	                return null;
	            }
	            @Override
	            protected void succeeded(){
	                super.succeeded();
	                progressPane.setVisible(false);
	                System.out.println("SUCCEEDED");
	            }
	            
	            @Override
	            protected void cancelled() {
	            	super.cancelled();
	            	progressPane.setVisible(false);
	                System.out.println("CANCELLED");
	            }
	        };
	        thread = new Thread(task);
	        thread.setDaemon(true);
	        thread.start();
		}
        
	}
	
	public static void endTask() {
		kraj=true;
		pauza=false;
		synchronized(lock) {
			lock.notify();
		}
	}
	
	public static void startTask() {
		kraj=false;
		pauza=false;
	}
	
	@FXML
    void clear(MouseEvent event) {
		mjestoTextField.clear();
    }
	
	@FXML
    void ucitajLinije(KeyEvent event) {
		if(event.getCode().equals(KeyCode.ENTER)) {
			Platform.runLater(() -> {
				mjestoTextField.end();
			});
			
			try {
			
				karteObs.clear();
				if(polasciRadioButton.isSelected()) {
					Stajaliste odrediste = InformacijeController.stajalistaList.stream().filter(s -> s.toString().equals(mjestoTextField.getText())).findFirst().get();
					Stajaliste polaziste = stajalistaList.stream().filter(s -> s.getIdStajalista()==PrijavaController.autobuskaStanica.getIdStajalista()).findFirst().get();
						for(Karta karta : Karta.getKarteList(polaziste,odrediste)) {
							daniUSedmici = karta.getRelacija().getDani();
							System.out.println("Dani: " + daniUSedmici);
							if(daniUSedmici.contains(String.valueOf(datum.getValue().getDayOfWeek().getValue())))
								karteObs.add(karta);
						}	
				}
				else {
					Stajaliste polaziste = InformacijeController.stajalistaList.stream().filter(s -> s.toString().equals(mjestoTextField.getText())).findFirst().get();
					Stajaliste odrediste = stajalistaList.stream().filter(s -> s.getIdStajalista()==PrijavaController.autobuskaStanica.getIdStajalista()).findFirst().get();
					for(Karta karta : Karta.getKarteList(polaziste, odrediste)) {
						daniUSedmici = karta.getRelacija().getDani();
						karteObs.add(karta);
					}
				}
				if(karteObs.isEmpty())
					showPrazanSetAlert();
				
				
			} catch(NoSuchElementException e) {
				Util.getNotifications("Greška", "Nepoznato stajalište!", "ERROR").show();
			}
			
		}
    }
	
	
	
	
	
	public void toggleSetUp() {
		toggleGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
		    if (newValue == null) {
		        oldValue.setSelected(true);
		    }
		    else
		    	if(newValue.equals(polasciRadioButton)) {
		    		karteObs.clear();
		    		mjestoTextField.clear();
		    		mjestoTextField.setPromptText("Destinacija");
		    		mjestoTextField.resetValidation();
		    		
		    	}
		    	else {
		    		mjestoTextField.clear();
		    		karteObs.clear();
		    		mjestoTextField.setPromptText("Polaziste");		    		
		    		mjestoTextField.resetValidation();
		    	}
		});	
	}
	
	/*
	public boolean zadovoljavaDatumVrijeme(String daniUSedmici,Time vrijemePolaska) {
		LocalTime localTime = LocalTime.now();
		if(datum.getValue().equals(LocalDate.now())) {
			return (localTime.compareTo(vrijemePolaska.toLocalTime())<0);
			}
		else
			return (daniUSedmici.contains(datum.getValue().getDayOfWeek().toString())) && daniUSedmici.contains(datum.getValue().getDayOfWeek().toString());
	}*/
	/*
	@FXML
	public void getKarte() {
		if(mjestoTextField.validate()){
			karteObs.clear();
			if(polasciRadioButton.isSelected()) {
					for(Karta karta : Karta.getKarteList(nazivMjesta, mjestoTextField.getText())) {
						daniUSedmici = karta.getLinija().getDaniUSedmici();
						if(daniUSedmici.contains(datum.getValue().getDayOfWeek().toString()))
							karteObs.add(karta);
					}	
			}
			else {
				for(Karta karta : Karta.getKarteList(mjestoTextField.getText(),nazivMjesta)) {
					daniUSedmici = karta.getLinija().getDaniUSedmici();
					karteObs.add(karta);
				}
			}
			if(karteObs.isEmpty())
				showPrazanSetAlert();
		}
	}
	*/
	public void showPrazanSetAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Obavjestenje");
		alert.setHeaderText("Nema linija");
		alert.setContentText("Za odabranu relaciju i datum nema linija ili su vec otisle.");
		alert.showAndWait();
	
	}
	/*public void ucitajMjesta() {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select distinct Naziv from mjesto where Naziv!=?";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, nazivMjesta);
			r = s.executeQuery();
			while(r.next()) {
				mjestaSet.add(r.getString(1));
			}
	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(r, s, c);
		}
	}*/

}
