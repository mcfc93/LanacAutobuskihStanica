package org.unibl.etf.prijava;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.controlsfx.control.MaskerPane;
import org.unibl.etf.autobuska_stanica.AutobuskaStanica;
import org.unibl.etf.util.Mjesto;
import org.unibl.etf.util.Praznik;
import org.unibl.etf.util.Stajaliste;
import org.unibl.etf.util.Util;
import org.unibl.etf.zaposleni.AdministrativniRadnik;
import org.unibl.etf.zaposleni.Administrator;
import com.jfoenix.controls.JFXCheckBox;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PrijavaController implements Initializable {
	
	public static Nalog nalog=null;
	public static AutobuskaStanica autobuskaStanica=null;
	
	private static final String SER_FILE="logs/nalog.ser";
	
	@FXML
	private AnchorPane anchorPane;
	
	@FXML
    private TextField korisnickoImeTextField;

    @FXML
    private PasswordField lozinkaTextField;
    
    @FXML
    private JFXCheckBox zapamtiMeCheckBox;
    
    @FXML
    private Label greskaTextLabel;
    
    @FXML
    private Label greskaBackgroundLabel;

    @FXML
    private Button prijavaButton;
    
    private double xOffset=0;
    private double yOffset=0;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		//ucitavanje nalog.ser, ako postojis
    	File f=new File(SER_FILE);
    	if(f.exists()) {
    		try (ObjectInputStream ois = 
    				new ObjectInputStream(
    						new FileInputStream(f.getAbsolutePath())
    				)
    			)
    		{
    			PrijavaController.nalog=(Nalog)ois.readObject();
    			korisnickoImeTextField.setText(nalog.getKorisnickoIme());
    			lozinkaTextField.setText(nalog.getLozinka());
System.out.println("nalog.ser");
    		} catch(IOException e) {
    			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
    		} catch(ClassNotFoundException e) {
    			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
    		}
    	}
		
		greskaTextLabel.setVisible(false);
		greskaBackgroundLabel.setVisible(false);
		zapamtiMeCheckBox.setSelected(true);
		prijavaButton.setDefaultButton(true);
		
		if(korisnickoImeTextField.getText().trim().isEmpty()) {
			//focus se moze traziti samo nakon sto se Stage inicijalizuje
			Platform.runLater(() -> korisnickoImeTextField.requestFocus());
		} else {
			Platform.runLater(() -> lozinkaTextField.requestFocus());
		}
		
		//DragAndDrop
		anchorPane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
				xOffset = stage.getX() - event.getScreenX();
		        yOffset = stage.getY() - event.getScreenY();
			}
		});
				
		anchorPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
	       @Override
		   public void handle(MouseEvent event) {
	    	   Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
		       stage.setX(event.getScreenX() + xOffset);
		       stage.setY(event.getScreenY() + yOffset);
		       stage.setOpacity(0.8);
		   }
		});
				
		anchorPane.setOnMouseReleased((event) -> {
			Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
		    stage.setOpacity(1.0);
		});
		
		
		/*
		prijavaButton.disableProperty().bind(Bindings.createBooleanBinding(
	    		() -> korisnickoImeTextField.getText().trim().isEmpty()
	    				|| lozinkaTextField.getText().trim().isEmpty()
	    						, korisnickoImeTextField.textProperty()
	    							, lozinkaTextField.textProperty()
	    									));
	   */
		/*
		prijavaButton.setDisable(true);
	    
	    ChangeListener<String> changeListener=new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable,
	        						String oldValue, String newValue) {
	        	if((korisnickoImeTextField.getText().trim().length() < 6)
		        	|| (lozinkaTextField.getText().trim().length() < 6)) {
		        	prijavaButton.setDisable(true);
		        } else {
		        	prijavaButton.setDisable(false);
		        }
	        }
	    };
	    
	    korisnickoImeTextField.textProperty().addListener(changeListener);
	    lozinkaTextField.textProperty().addListener(changeListener);
	    */
		prijavaButton.disableProperty().bind(
	    		korisnickoImeTextField.textProperty().isEmpty()
	    			.or(lozinkaTextField.textProperty().isEmpty())
	    		);
	}
	
	@FXML
    void enterPressed(KeyEvent event) {
		/*
		if(!korisnickoImeTextField.getText().isEmpty()
				&& !lozinkaTextField.getText().isEmpty()
					&& event.getCode().equals(KeyCode.ENTER)) {
			prijava(new ActionEvent(event.getSource(), prijavaButton));
		}
		*/
    }
	
	@FXML
    void prijava(ActionEvent event) {
		try {
			if ((nalog=Nalog.prijava(korisnickoImeTextField.getText(), lozinkaTextField.getText())) != null) {
				//((Stage)anchorPane.getScene().getWindow()).close();
				//((Stage)anchorPane.getScene().getWindow()).hide();
				//((Node)event.getSource()).getScene().getWindow().hide();
				
				
				//nalog.setKorisnickoIme(korisnickoImeTextField.getText());
				//nalog.setLozinka(lozinkaTextField.getText());
				
				

				//Serijalizacija ako je cekirano Remember me
				if(zapamtiMeCheckBox.isSelected()) {
					System.out.println("REMEMBER ME");
					try (ObjectOutputStream oos = 
							new ObjectOutputStream(
									new FileOutputStream(SER_FILE)
							)
					)
					{
						/****************************
						 * TREBA NESTO DRUGO ODRADITI
						 * TRENUTNO DOSTUPNA LOZINKA
						 * 
						 ***************************/
						nalog.setLozinka(lozinkaTextField.getText());
						
						oos.writeObject(nalog);
					} catch(IOException e) {
						Util.LOGGER.log(Level.SEVERE, e.toString(), e);
					}
				} else {
					File f=new File(SER_FILE);
			    	if(f.exists()) {
			    		f.delete();
			    	}
				}
				
				nalog.setLozinka(Nalog.hash(lozinkaTextField.getText()));
				korisnickoImeTextField.clear();
				lozinkaTextField.clear();
				
				

				



				MaskerPane progressPane = new MaskerPane();
				progressPane.setText("Molimo sačekajte...");
				progressPane.setVisible(false);
				anchorPane.getChildren().add(progressPane);
				AnchorPane.setTopAnchor(progressPane,0.0);
				AnchorPane.setBottomAnchor(progressPane,0.0);
				AnchorPane.setLeftAnchor(progressPane,0.0);
				AnchorPane.setRightAnchor(progressPane,0.0);

				//ucitavanje postanskih brojeva i praznika
				/*
				Platform.runLater(() -> {
					loadPostalCodes();
					System.out.println(getPostalCodeList());
				});
				*/
				Task<Void> task = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						System.out.println(Thread.currentThread());
			          	progressPane.setVisible(true);
			          	//Mjesto.loadPostalCodes();
			          	//Mjesto.loadCities();
			          	Mjesto.loadPlaces();
			          	Praznik.loadHolidays();
			          	Stajaliste.loadStajalista();
			          	Thread.sleep(1000);
			            return null;
			        }
			        @Override
			        protected void succeeded(){
			        super.succeeded();
			        	progressPane.setVisible(false);
System.out.println(Mjesto.getPostalCodeList());
System.out.println(Mjesto.getCityList());
System.out.println(Mjesto.getPlaceList());
System.out.println(Mjesto.getCityPostalCodeList());
System.out.println(Praznik.getHolidayList());
System.out.println(Stajaliste.getStajalisteList());

						autobuskaStanica=AutobuskaStanica.getAutobuskaStanica(nalog.getIdStanice());
System.out.println(autobuskaStanica);

						((Stage)((Node)event.getSource()).getScene().getWindow()).close();
		          
				
				
				
				
				
				
				if(nalog.getZaposleni() instanceof Administrator) {
					//administrator
	    			try {
	    				Parent root = (AnchorPane)FXMLLoader.load(getClass().getResource("/org/unibl/etf/administrator/AdministratorView.fxml"));
	    				Scene scene = new Scene(root);
	    				scene.getStylesheets().add(getClass().getResource("/org/unibl/etf/application.css").toExternalForm());
	    				Stage stage=new Stage();
	    				stage.setScene(scene);
	    				//stage.setTitle("Administrator");
	    				stage.setResizable(false);
	    				stage.initStyle(StageStyle.UNDECORATED);
	    				stage.show();
	    			} catch(IOException e) {
	    				//e.printStackTrace();
	    				Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    			}
				} else if(nalog.getZaposleni() instanceof AdministrativniRadnik) {
					//administrativni radnik
	    			try {
	    				Parent root = (AnchorPane)FXMLLoader.load(getClass().getResource("/org/unibl/etf/administrativni_radnik/AdministrativniRadnikView.fxml"));
	    				Scene scene = new Scene(root);
	    				scene.getStylesheets().add(getClass().getResource("/org/unibl/etf/application.css").toExternalForm());
	    				Stage stage=new Stage();
	    				stage.setScene(scene);
	    				//stage.setTitle("Administrativni radnik");
	    				stage.setResizable(false);
	    				stage.initStyle(StageStyle.UNDECORATED);
	    				stage.show();
	    			} catch(IOException e) {
	    				//e.printStackTrace();
	    				Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    			}
				} else {
					//if("SalterskiRadnik".equals(r.getString("Tip"))) {
	        		//salterski radnik
	               	try {
	            		Parent root = (AnchorPane)FXMLLoader.load(getClass().getResource("/org/unibl/etf/salterski_radnik/SalterskiRadnikView.fxml"));
	           			Scene scene = new Scene(root);
	           			scene.getStylesheets().add(getClass().getResource("/org/unibl/etf/application.css").toExternalForm());
	           			Stage stage=new Stage();
	           			stage.setScene(scene);
	           			//stage.setTitle("Šalterski radnik");
	           			stage.setResizable(false);
	           			stage.initStyle(StageStyle.UNDECORATED);
	           			stage.show();
	           		} catch(IOException e) {
	           			//e.printStackTrace();
	           			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	            	}
				}
				
			        }
				};
				new Thread(task).start();

			} else {
				greskaTextLabel.setText("Korisničko ime ili lozinka pogrešni!");
				greskaTextLabel.setVisible(true);
				greskaBackgroundLabel.setVisible(true);
				korisnickoImeTextField.clear();
				lozinkaTextField.clear();
				//korisnickoImeTextField.requestFocus();
			}
		} catch(Exception e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			Alert alert=new Alert(AlertType.ERROR);
    		alert.setTitle("Greška");
    		alert.setHeaderText(null);
    		alert.setContentText("Neuspješno povezivanje sa bazom.");
    		
    		alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/unibl/etf/application.css").toExternalForm());
			alert.getDialogPane().getStyleClass().add("alert");
		  	
    		alert.showAndWait();
		}
    }
	
	void odjava(ActionEvent event) {
		((Stage)anchorPane.getScene().getWindow()).show();
	}
	
	@FXML
	void sakrijLabelu(MouseEvent event) {
		greskaTextLabel.setVisible(false);
		greskaBackgroundLabel.setVisible(false);
	}
	
	@FXML
	void close(MouseEvent event) {
		//System.exit(0);
		Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
		stage.close();
	}
	
	@FXML
	void minimize(MouseEvent event) {
		Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
		stage.setIconified(true);
	}
}
