package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.unibl.etf.util.Util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class DodavanjePrevoznikaController implements Initializable{

	@FXML
	private ImageView successImageView = new ImageView();
	@FXML
	private JFXButton dodajPrevoznikaButton = new JFXButton();
	@FXML
	private JFXTextField jibTextField = new JFXTextField();
	@FXML
	private JFXTextField nazivTextField = new JFXTextField();
	@FXML
	private JFXTextField telefonTextField = new JFXTextField();
	@FXML
	private JFXTextField emailTextField = new JFXTextField();
	@FXML
	private JFXTextField webAdresaTextField = new JFXTextField();
	@FXML
	private JFXTextField tekuciRacunTextField = new JFXTextField();
	@FXML
	private JFXTextField postanskiBrojTextField = new JFXTextField();
	@FXML
	private JFXTextField adresaTextField = new JFXTextField();
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		jibTextField.getValidators().add(requredFieldValidator(jibTextField));
		nazivTextField.getValidators().add(requredFieldValidator(nazivTextField));
		telefonTextField.getValidators().add(requredFieldValidator(telefonTextField));
		emailTextField.getValidators().add(requredFieldValidator(emailTextField));
		tekuciRacunTextField.getValidators().add(requredFieldValidator(tekuciRacunTextField));
		adresaTextField.getValidators().add(requredFieldValidator(adresaTextField));
		webAdresaTextField.getValidators().add(requredFieldValidator(webAdresaTextField));
		postanskiBrojTextField.getValidators().add(requredFieldValidator(postanskiBrojTextField));
		successImageView.setVisible(false);
	}
	
	public ValidatorBase requredFieldValidator(JFXTextField textField) {
    	ValidatorBase requiredFieldValidator = new RequiredFieldValidator();
	    requiredFieldValidator.setMessage("Obavezan unos");
	    requiredFieldValidator.setIcon(new ImageView());
	    textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)->{
	        if(!newValue) {
	        	textField.validate();
	        }
	    });
	    textField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue)->{
	        	textField.validate();
	    });
	    return requiredFieldValidator;
    }
	
	@FXML
	public void dodajPrevoznika() {
		if(jibTextField.validate() &
				nazivTextField.validate() &
					telefonTextField.validate() &
						emailTextField.validate() &
							tekuciRacunTextField.validate() &
								adresaTextField.validate() &
									webAdresaTextField.validate() &
										postanskiBrojTextField.validate()) {
		Connection c = null;
		PreparedStatement s = null;
		String sql = "insert into prevoznik value (?,?,?,?,?,?,?,?,default)";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, jibTextField.getText(),nazivTextField.getText(),telefonTextField.getText(),emailTextField.getText(),webAdresaTextField.getText(),
					tekuciRacunTextField.getText(),adresaTextField.getText(),Integer.parseInt(postanskiBrojTextField.getText()));
			if(s.executeUpdate()==1) 
				showSuccess();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(s, c);
		}
	}

	}

	

	public void showSuccess() {
		// TODO Auto-generated method stub
		Timeline timeline = new Timeline();
		nazivTextField.clear();
		jibTextField.clear();
		postanskiBrojTextField.clear();
		adresaTextField.clear();
		webAdresaTextField.clear();
		telefonTextField.clear();
		emailTextField.clear();
		tekuciRacunTextField.clear();
		postanskiBrojTextField.clear();
		successImageView.setImage(new Image(getClass().getResource("img/checkmark.png").toExternalForm()));
		successImageView.setVisible(true);
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(3),
		    new EventHandler<ActionEvent>() {
		        @Override
		        public void handle(ActionEvent event) {
		        	successImageView.setVisible(false);
		        }
		    }));
		timeline.play();
	}

}
