package org.unibl.etf.administrator;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class DodajNalogController implements Initializable {
	
	@FXML
	private JFXButton potvrdiButton;
	
	@FXML
    private JFXTextField korisnickoImeTextField;

    @FXML
    private JFXTextField lozinkaTextField;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		potvrdiButton.setDisable(true);
	    
	    ChangeListener<String> changeListener=new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable,
	        						String oldValue, String newValue) {
	        	if((korisnickoImeTextField.getText().trim().length() < 6)
		        	|| (lozinkaTextField.getText().trim().length() < 6)) {
		        	potvrdiButton.setDisable(true);
		        } else {
		        	potvrdiButton.setDisable(false);
		        }
	        }
	    };
	    
	    korisnickoImeTextField.textProperty().addListener(changeListener);
	    lozinkaTextField.textProperty().addListener(changeListener);
	    
		/*
		potvrdiButton.disableProperty().bind(
	    		korisnickoImeTextField.textProperty().isEmpty()
	    			.or(lozinkaTextField.textProperty().isEmpty())
	    		);
	   	*/
	}
	
}
