package org.unibl.etf.administrativni_radnik;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.unibl.etf.util.Util;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class DodavanjePrevoznikaController implements Initializable{
	
	public static List<String> postanskiBrojeviList = new ArrayList<>();
	public static final String webAdresaRegex = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?|^((http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
	public static final String emailRegex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+(?:\\\\.[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\\\.[a-zA-Z0-9-]+)*$";
	public static final String telefonRegex = "^\\+(?:[0-9] ?){6,14}[0-9]$";
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
		nazivTextField.getValidators().add(requredFieldValidator(nazivTextField));
		tekuciRacunTextField.getValidators().add(requredFieldValidator(tekuciRacunTextField));
		adresaTextField.getValidators().add(requredFieldValidator(adresaTextField));
		successImageView.setVisible(false);
		ucitajPostanskeBrojeve();
		ValidatorBase emailValidator = new ValidatorBase("Nekorektan unos") {
			@Override
			protected void eval() {
				// TODO Auto-generated method stub
				if(!emailTextField.getText().isEmpty() &&
					!emailTextField.getText().matches(emailRegex)) {
						hasErrors.set(true);
						}
					else
						hasErrors.set(false);
					
			}
		};
			
		ValidatorBase postanskiBrojValidator = new ValidatorBase("Nekorektan unos") {
			
			@Override
			protected void eval() {
				// TODO Auto-generated method stub
				if(!postanskiBrojTextField.getText().isEmpty() &&
					!postanskiBrojeviList.contains(postanskiBrojTextField.getText())) {
						hasErrors.set(true);
						}
					else
						hasErrors.set(false);
			}
		};
		ValidatorBase webAdresaValidator = new ValidatorBase("Nekorektan unos") {
			
			@Override
			protected void eval() {
				// TODO Auto-generated method stub
				if(!webAdresaTextField.getText().isEmpty() &&
					!webAdresaTextField.getText().matches(webAdresaRegex)) {
						hasErrors.set(true);
						}
					else
						hasErrors.set(false);
					
			}
		};
		ValidatorBase telefonValidator = new ValidatorBase("Nekorektan unos") {	
			@Override
			protected void eval() {
				// TODO Auto-generated method stub
				if(!telefonTextField.getText().isEmpty() &&
					!telefonTextField.getText().matches(telefonRegex)) {
						hasErrors.set(true);
						}
					else
						hasErrors.set(false);
					
			}
		};
		ValidatorBase jibValidator = new ValidatorBase("Nekorektan unos") {
			@Override
			protected void eval() {
				if(!jibTextField.getText().isEmpty()
	        			&& !jibTextField.getText().matches("^[0-9]{10}$")) {
	        		hasErrors.set(true);
		        } else {
		        	 hasErrors.set(false);
		    
		        }
			}
		};
		emailTextField.getValidators().addAll(requredFieldValidator(emailTextField),emailValidator);
		jibTextField.getValidators().addAll(requredFieldValidator(jibTextField),jibValidator);
		webAdresaTextField.getValidators().addAll(requredFieldValidator(webAdresaTextField),webAdresaValidator);
		telefonTextField.getValidators().addAll(requredFieldValidator(telefonTextField),telefonValidator);
		postanskiBrojTextField.getValidators().addAll(requredFieldValidator(postanskiBrojTextField),postanskiBrojValidator);
	}
	
	public void ucitajPostanskeBrojeve() {
		// TODO Auto-generated method stub
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, "select PostanskiBroj from mjesto", false);
			r = s.executeQuery();
			while(r.next())
				postanskiBrojeviList.add(String.valueOf(r.getInt(1)));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//autoComplete();
	}
	
	public void autoComplete() {
		// TODO Auto-generated method stub
		JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
	    autoCompletePopup.getSuggestions().addAll(postanskiBrojeviList);
	    autoCompletePopup.setSelectionHandler(event -> {
	        postanskiBrojTextField.setText(event.getObject());
	    });
	    postanskiBrojTextField.textProperty().addListener(observable -> {
	        autoCompletePopup.filter(string -> string.toLowerCase().contains(postanskiBrojTextField.getText().toLowerCase()));
	        if (autoCompletePopup.getFilteredSuggestions().isEmpty() || postanskiBrojTextField.getText().isEmpty()) {
	            autoCompletePopup.hide();
	        } else {
	            autoCompletePopup.show(postanskiBrojTextField);
	        }
	    });
	    
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
		
		telefonTextField.resetValidation();
		jibTextField.resetValidation();
		adresaTextField.resetValidation();
		webAdresaTextField.resetValidation();
		emailTextField.resetValidation();
		tekuciRacunTextField.resetValidation();
		postanskiBrojTextField.resetValidation();
		nazivTextField.resetValidation();
		
		//successImageView.setImage(new Image(getClass().getResource("img/checkmark.png").toExternalForm()));
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
