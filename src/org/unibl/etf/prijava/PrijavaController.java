package org.unibl.etf.prijava;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.unibl.etf.util.Util;
import org.unibl.etf.zaposleni.AdministrativniRadnik;
import org.unibl.etf.zaposleni.Administrator;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

//import com.jfoenix.controls.JFXSpinner;

//import java.util.logging.Level;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PrijavaController implements Initializable {
	
	public static Nalog nalog=null;
	
	@FXML
	private AnchorPane anchorPane;
	
	@FXML
    private TextField korisnickoImeTextField;

    @FXML
    private PasswordField lozinkaTextField;
    
    @FXML
    private Label greskaTextLabel;
    
    @FXML
    private Label greskaBackgroundLabel;

    @FXML
    private Button prijavaButton;
    
    //@FXML
    //private JFXSpinner spinner;
    
    private double xOffset=0;
    private double yOffset=0;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		greskaTextLabel.setVisible(false);
		greskaBackgroundLabel.setVisible(false);
		//spinner.setVisible(false);
		
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
		if(!korisnickoImeTextField.getText().isEmpty()
				&& !lozinkaTextField.getText().isEmpty()
					&& event.getCode().equals(KeyCode.ENTER)) {
			prijava(new ActionEvent(event.getSource(), prijavaButton));
		}
    }
	
	@FXML
    void prijava(ActionEvent event) {
		
		//prijavaButton.setText("");
		//spinner.setVisible(true);
		
		if ((nalog=Nalog.prijava(korisnickoImeTextField.getText(), lozinkaTextField.getText())) != null) {
			//((Stage)anchorPane.getScene().getWindow()).close();
			//((Stage)anchorPane.getScene().getWindow()).hide();
			//((Node)event.getSource()).getScene().getWindow().hide();
			((Stage)((Node)event.getSource()).getScene().getWindow()).close();
			
			//nalog.setKorisnickoIme(korisnickoImeTextField.getText());
			//nalog.setLozinka(lozinkaTextField.getText());
			
			
			
			
			
			
			//
			
			//MOZDA BOLJE DA SE UCITA PREKO LOGIN FORME
			//A NE OTVARANJE NOVOG PROZORA ZA ADMINISTRATORA,...
			
			//
			
			
			
			
			
			if(nalog.getZaposleni() instanceof Administrator) {
				//administrator
    			try {
    				Parent root = (AnchorPane)FXMLLoader.load(getClass().getResource("/org/unibl/etf/administrator/AdministratorView.fxml"));
    				Scene scene = new Scene(root);
    				Stage stage=new Stage();
    				stage.setScene(scene);
    				//stage.setTitle("Administrator");
    				stage.setResizable(false);
    				stage.initStyle(StageStyle.UNDECORATED);
    				stage.show();

    				//Modality.NONE, Modality.WINDOW_MODAL, Modality.APPLICATION_MODAL
    				//stage.initModality(Modality.APPLICATION_MODAL);
    				//stage.showAndWait();
    			} catch(Exception e) {
    				//e.printStackTrace();
    				Util.LOGGER.log(Level.SEVERE, e.toString(), e);
    			}
			} else if(nalog.getZaposleni() instanceof AdministrativniRadnik) {
				//administrativni radnik
    			try {
    				Parent root = (AnchorPane)FXMLLoader.load(getClass().getResource("/org/unibl/etf/administrativni_radnik/AdministrativniRadnikView.fxml"));
    				Scene scene = new Scene(root);
    				Stage stage=new Stage();
    				stage.setScene(scene);
    				//stage.setTitle("Administrativni radnik");
    				stage.setResizable(false);
    				stage.initStyle(StageStyle.UNDECORATED);
    				//primaryStage.initStyle(StageStyle.DECORATED);
    				//primaryStage.initStyle(StageStyle.UNDECORATED);    //brisanje _ [] X
    				//primaryStage.initStyle(StageStyle.UNIFIED);
    				//primaryStage.initStyle(StageStyle.UTILITY);

    				stage.show();

    				//Modality.NONE, Modality.WINDOW_MODAL, Modality.APPLICATION_MODAL
    				//stage.initModality(Modality.APPLICATION_MODAL);
    				//stage.showAndWait();
    			} catch(Exception e) {
    				//e.printStackTrace();
    				Util.LOGGER.log(Level.SEVERE, e.toString(), e);
    			}
			} else {
				//if("SalterskiRadnik".equals(r.getString("Tip"))) {
        		//salterski radnik
               	try {
            		Parent root = (AnchorPane)FXMLLoader.load(getClass().getResource("/org/unibl/etf/salterski_radnik/SalterskiRadnikView.fxml"));
           			Scene scene = new Scene(root);
           			Stage stage=new Stage();
           			stage.setScene(scene);
           			//stage.setTitle("Šalterski radnik");
           			stage.setResizable(false);
           			stage.initStyle(StageStyle.UNDECORATED);
           			//primaryStage.initStyle(StageStyle.DECORATED);
           			//primaryStage.initStyle(StageStyle.UNDECORATED);    //brisanje _ [] X
           			//primaryStage.initStyle(StageStyle.UNIFIED);
           			//primaryStage.initStyle(StageStyle.UTILITY);
           			
           			stage.show();
           			
            		//Modality.NONE, Modality.WINDOW_MODAL, Modality.APPLICATION_MODAL
            		//stage.initModality(Modality.APPLICATION_MODAL);
            		//stage.showAndWait();
           		} catch(Exception e) {
           			//e.printStackTrace();
           			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
            	}
			}

		} else {
			greskaTextLabel.setText("Korisničko ime ili lozinka pogrešni!");
			greskaTextLabel.setVisible(true);
			greskaBackgroundLabel.setVisible(true);
			korisnickoImeTextField.clear();
			lozinkaTextField.clear();
			//korisnickoImeTextField.requestFocus();
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
	
	/*
	 		Alert alert=new Alert(AlertType.ERROR);
    		alert.setTitle("Greška");
    		alert.setHeaderText(null);
    		alert.setContentText("Poruka.");
    		alert.showAndWait();
	 */
}
