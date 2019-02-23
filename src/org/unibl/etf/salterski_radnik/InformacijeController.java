package org.unibl.etf.salterski_radnik;

import java.net.URL;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.controlsfx.control.MaskerPane;
import org.unibl.etf.karta.Karta;
import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.util.Praznik;
import org.unibl.etf.util.Stajaliste;
import org.unibl.etf.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
	
	public static List<Stajaliste> stajalistaList;
	public static List<Stajaliste> stajalistaBezStanica;
	public static List<Stajaliste> stajalistaStanica;
	public static Stajaliste pocetnoStajaliste;

	
	private static ObservableList<Karta> karteObs = FXCollections.observableArrayList();
	@FXML
	private JFXButton pretragaButton = new JFXButton();
	@FXML
	private DatePicker datum = new DatePicker();
	@FXML
	private JFXTextField mjestoTextField = new JFXTextField();

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
    
    @FXML
    private Label vrijemeLabel;
    
    
    private static Thread thread;
	private static MaskerPane progressPane;
	private static volatile boolean kraj=false;
	private static volatile boolean pauza=false;
	private static volatile boolean cekaj=false;
	public static Object lock=new Object();
	
	
	private static Thread vrijemeThread;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		stajalistaStanica = Stajaliste.getStajalistaStanicaList();
		stajalistaList = Stajaliste.getStajalisteList();
		//stajalistaBezStanica = Stajaliste.getStajalisteList();
		stajalistaBezStanica = new ArrayList<>();
		stajalistaBezStanica.addAll(stajalistaList);
		
		pocetnoStajaliste = stajalistaList.stream().filter(s -> s.getIdStajalista()==PrijavaController.autobuskaStanica.getIdStajalista()).findFirst().get();
		stajalistaBezStanica.removeIf(s -> stajalistaStanica.contains(s));
		/*
		for (Stajaliste s : stajalistaStanica) {
			if(stajalistaBezStanica.contains(s))
				stajalistaBezStanica.remove(s);
		}
		*/
		// stajaliste = stajalistaList.stream().filter(s -> s.get()==)
		
		clearImageView.setVisible(false);
		
		mjestoTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
        	//if(!newValue.isEmpty()) {
			if(!mjestoTextField.getText().isEmpty()) {
        		clearImageView.setVisible(true);
        		
        		pauza=true;
        		
        	} else {
        		clearImageView.setVisible(false);

        		pauza=false;
        		synchronized(lock) {
        			lock.notify();
        		}
        		
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
		datum.valueProperty().addListener((observable, oldValue, newValue) -> {
        	System.out.println("DATUM PROMJENJEN");
        	ucitajLinije(new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.ENTER.toString(), KeyCode.ENTER.toString(), KeyCode.ENTER, false, false, false, false));
        });
		//nazivMjesta = getNazivMjesta();
	//	ucitajMjesta();
		karteTable.setPlaceholder(new Label("Odaberite relaciju i datum"));
		mjestoTextField.setPromptText("Destinacija");
		
		nazivLinijeColumn.setCellValueFactory(new PropertyValueFactory<>("nazivLinije"));
		vrijemePolaskaColumn.setCellValueFactory(new PropertyValueFactory<>("vrijemePolaska"));
		vrijemeDolaskaColumn.setCellValueFactory(new PropertyValueFactory<>("vrijemeDolaska"));
		prevoznikColumn.setCellValueFactory(new PropertyValueFactory<>("nazivPrevoznika"));
		cijenaColumn.setCellValueFactory(new PropertyValueFactory<>("cijena"));
		peronColumn.setCellValueFactory(new PropertyValueFactory<>("peron"));
		
		nazivLinijeColumn.setMinWidth(150);
		
		prevoznikColumn.setMinWidth(80);
		prevoznikColumn.setMinWidth(120);
        
        vrijemePolaskaColumn.setMinWidth(70);
        vrijemePolaskaColumn.setMaxWidth(70);
        
        vrijemeDolaskaColumn.setMinWidth(70);
        vrijemeDolaskaColumn.setMaxWidth(70);
        
        cijenaColumn.setMinWidth(75);
        cijenaColumn.setMaxWidth(75);
        
        peronColumn.setMinWidth(40);
        peronColumn.setMaxWidth(40);
		
		Util.setAutocompleteList(mjestoTextField, stajalistaBezStanica.stream().map(Stajaliste::toString).collect(Collectors.toList()));
		//mjestoTextField.getValidators().addAll(Util.requredFieldValidator(mjestoTextField),Util.collectionValidator(mjestoTextField, mjestaSet, true, "Unesite mjesto"));
		//mjestoTextField.getValidators().add(Util.requiredFieldValidator(mjestoTextField));
		//mjestoTextField.getValidators().add(Util.collectionValidator(mjestoTextField, stajalistaList.stream().map(Stajaliste::toString).collect(Collectors.toList()), true, "Unesite mjesto"));
		polasciDolasciComboBox.getItems().addAll("POLASCI", "DOLASCI");
		polasciDolasciComboBox.getSelectionModel().selectFirst();
		polasciDolasciComboBox.setStyle("-fx-font-weight: bold;");
		
		polasciDolasciComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
//			if(newValue == null) {
//				polasciDolasciComboBox.getSelectionModel().selectFirst();
//			} else {
				mjestoTextField.clear();
				thread.interrupt();
				System.out.println("INTERRUPT - ComboBox");
//			}
		});
		
		
		
		Platform.runLater(() -> {
			((Stage)gridPane.getScene().getWindow()).setOnHiding(event -> {
				System.out.println("Closing Stage");
				kraj=true;
			});
		});
		
		
		vrijemeDolaskaColumn.setVisible(false);
		cijenaColumn.setVisible(false);
		
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
	                	//try {
		    				if(pauza) {
		    					System.out.println("PAUZA");
		    					synchronized(lock) {
		    						try {
		    							lock.wait();
		    						} catch(InterruptedException e) {
		    							//Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		    							//System.out.println("WAIT - CONTINUE");
		    						}
		    					}
		    					System.out.println("NASTAVAK");
		    				}
		    				cekaj=true;
		    				
		    				Platform.runLater(() -> {
		    					progressPane.setVisible(true);
		    				});
		    				
		    				synchronized(karteObs) {
			    				System.out.println("UCITAVANJE: " + polasciDolasciComboBox.getValue());
			    				try {
									Thread.sleep(1000);
								}catch (InterruptedException e) {
									//Util.LOGGER.log(Level.SEVERE, e.toString(), e);
									System.out.println("SLEEP_5 - CONTINUE");
								}
			    				
			    				
		    					List<Karta> tmpKarte=new ArrayList<>();
		    					
		    					for (Karta karta : Karta.getInfoList(polasciDolasciComboBox.getValue())) {
		    						if(polasciDolasciComboBox.getSelectionModel().getSelectedIndex() == 0) {
		    							if(zadovoljavaVrijeme(karta.getRelacija().getVrijemePolaska())) {
		    								tmpKarte.add(karta);
		    							}
		    						} else {
		    							if(zadovoljavaVrijeme(karta.getRelacija().getVrijemeDolaska())) {
		    								tmpKarte.add(karta);
		    							}
		    						}
		    					}
		    					if(Praznik.getHolidayList().stream().anyMatch(p -> MonthDay.from(datum.getValue()).equals(MonthDay.of(p.getMjesec(), p.getDan())))) {
		    						tmpKarte.removeIf(k -> k.getRelacija().getLinija().getVoznjaPraznikom() == 9 || !k.getRelacija().getDani().contains(k.getRelacija().getLinija().getVoznjaPraznikom() + ""));
		    					} else {
		    						tmpKarte.removeIf(k -> !k.getRelacija().getDani().contains(String.valueOf(datum.getValue().getDayOfWeek().getValue())));
		    					}
		    					
			    				Platform.runLater(() -> {
			    					karteObs.clear();
				    				karteObs.addAll(tmpKarte);
									cijenaColumn.setVisible(false);
									if(polasciDolasciComboBox.getSelectionModel().isSelected(0)) {
										vrijemePolaskaColumn.setVisible(true);
										vrijemeDolaskaColumn.setVisible(false);
									} else {
										vrijemePolaskaColumn.setVisible(false);
										vrijemeDolaskaColumn.setVisible(true);
									}
									progressPane.setVisible(false);
								});
		    				}
		    				
		    				synchronized(lock) {
		    					cekaj=false;
		    					lock.notify();
		    				}
		    				
		    				try {
		    					Thread.sleep(10000);
		    				}catch (InterruptedException e) {
		    					//Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		    					//System.out.println("SLEEP_2 - CONTINUE");
		    				}
		    			/*
	                	} catch(InterruptedException e) {
							//Util.LOGGER.log(Level.SEVERE, e.toString(), e);
							System.out.println("CATCH - INTERRUPT");
							Platform.runLater(() -> {
								progressPane.setVisible(false);
							});
						}
						*/
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
		
		
		if(vrijemeThread==null || !vrijemeThread.isAlive()) {
			System.out.println("START [vrijeme] - " + (vrijemeThread==null? "NULL": vrijemeThread.getState()));
			vrijemeThread=new Thread() {
				@Override
				public void run() {
					while(!kraj) {
						//System.out.println("Azuriranje vremena");
						String vrijeme = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")).toString();
						Platform.runLater(() -> {
							vrijemeLabel.setText(vrijeme);
						});
						try {
							Thread.sleep(1000);
						}catch (InterruptedException e) {
							Util.LOGGER.log(Level.SEVERE, e.toString(), e);
						}
					}
					System.out.println("STOP [vrijeme]");
				}
			};
			vrijemeThread.start();
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
			if(!mjestoTextField.getText().isEmpty()) {
				try {
					Stajaliste odrediste = stajalistaList.stream().filter(s -> s.toString().equals(mjestoTextField.getText())).findFirst().get();
					Stajaliste polaziste = stajalistaList.stream().filter(s -> s.getIdStajalista()==PrijavaController.autobuskaStanica.getIdStajalista()).findFirst().get();
					
					new Thread() {
						@Override
						public void run() {
							//pauza=true;
							//thread.interrupt();
							synchronized(karteObs) {
								System.out.println("UCITAVANJE: " + Thread.currentThread());
								List<Karta> tmp = new ArrayList<>();
								if(polasciDolasciComboBox.getSelectionModel().isSelected(0)) {
			    					//POLASCI
			    					for (Karta karta : Karta.getKarteList(polaziste,odrediste)) {
		    							if(zadovoljavaVrijeme(karta.getRelacija().getVrijemePolaska())) {
		    								tmp.add(karta);
		    							}
			    					}
									
									//tmp.addAll(Karta.getKarteList(polaziste,odrediste));
									if(Praznik.getHolidayList().stream().anyMatch(p -> MonthDay.from(datum.getValue()).equals(MonthDay.of(p.getMjesec(), p.getDan())))) {
										tmp.removeIf(k -> k.getRelacija().getLinija().getVoznjaPraznikom() == 9 || !k.getRelacija().getDani().contains(k.getRelacija().getLinija().getVoznjaPraznikom() + ""));
									} else {
										tmp.removeIf(k -> !k.getRelacija().getDani().contains(String.valueOf(datum.getValue().getDayOfWeek().getValue())));
									}
								} else {
									//DOLASCI
									for (Karta karta : Karta.getKarteList(polaziste,odrediste)) {
										if(zadovoljavaVrijeme(karta.getRelacija().getVrijemeDolaska())) {
		    								tmp.add(karta);
		    							}
									}
									
									//tmp.addAll(Karta.getKarteListDolasci(polaziste,odrediste));
									if(Praznik.getHolidayList().stream().anyMatch(p -> MonthDay.from(datum.getValue()).equals(MonthDay.of(p.getMjesec(), p.getDan())))) {
										//System.out.println("JESTE PRAZNIK - DOLASCI");
										//tmp.forEach(k -> System.out.println(k.getRelacija().getLinija().getVoznjaPraznikom() + " " + k.getRelacija().getDani()));
										tmp.removeIf(k -> k.getRelacija().getLinija().getVoznjaPraznikom() == 9 || !k.getRelacija().getDani().contains(k.getRelacija().getLinija().getVoznjaPraznikom() + ""));
										//System.out.println("NAKON BRISANJA");
										//tmp.forEach(k -> System.out.println(k.getRelacija().getLinija().getVoznjaPraznikom() + " " + k.getRelacija().getDani()));
									} else {
										//System.out.println("NIJE PRAZNIK - DOLASCI");
										tmp.removeIf(k -> !k.getRelacija().getDani().contains(String.valueOf(datum.getValue().getDayOfWeek().getValue())));
									}
								}
								
								if(cekaj) {
			    					System.out.println("CEKANJE");
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
									vrijemePolaskaColumn.setVisible(true);
									vrijemeDolaskaColumn.setVisible(true);
									cijenaColumn.setVisible(true);
									//karteTable.getSortOrder().s
									karteObs.clear();
									karteObs.addAll(tmp);
									if(karteObs.isEmpty()) {
										Util.getNotifications("Obavještenje", "Nema linija za odabranu relaciju i dan!", "Information").show();
									}
								});
							}
						}
					}.start();
				} catch(NoSuchElementException e) {
					Util.getNotifications("Greška", "Nepoznato stajalište!", "ERROR").show();
				}
			}
		}
    }
	
	public boolean zadovoljavaVrijeme(Time vrijemePolaska) {
		if(datum.getValue().equals(LocalDate.now())) {
			return (LocalTime.now().compareTo(vrijemePolaska.toLocalTime())<0);
		}
		return true;
	}
	
}
