package org.unibl.etf.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.DoubleValidator;
import com.jfoenix.validation.IntegerValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.image.ImageView;

//klasa koja sadrzi sve pomocne alate
public class Util {
	public static Properties PROPERTY = new Properties();
	public static final Logger LOGGER = Logger.getLogger("Logger");
	
	
	static {
		//postavljanje Logger-a
		FileHandler fileHandler=null;
	    try {
	    	//FileHandler fileHandler = new FileHandler(System.getProperty("user.home") + File.separator + "error.log", true);
	    	fileHandler = new FileHandler("logs/error.log", true);	//append
	        LOGGER.addHandler(fileHandler);
	        SimpleFormatter formatter = new SimpleFormatter();	//za formatiran ispis
	        fileHandler.setFormatter(formatter);
	        //LOGGER.setUseParentHandlers(false);	//da ne prikazuje Exception na Console
	    } catch (SecurityException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	    	fileHandler.close();
	    }
		
	    //ucitavanje properties fajla
		try {
			//p.load(new FileInputStream(System.getProperty("user.home") + File.separator + "config.properties"));
			PROPERTY.load(new FileInputStream("config.properties"));
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		
		
System.out.println(PROPERTY);
		

		//ucitavanje postanskih brojeva
		/*
		Platform.runLater(() -> {
			loadPostalCodes();
			System.out.println(getPostalCodeList());
		});
		*/
		Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
            	System.out.println(Thread.currentThread());
            	loadPostalCodes();
                return null;
            }
            @Override
            protected void succeeded(){
                super.succeeded();
System.out.println(getPostalCodeList());
            }
        };
        new Thread(task).start();
	}
	
	//BAZA
	public static Connection getConnection() {
		Connection c = null;
		try {
			c = DriverManager.getConnection(Util.PROPERTY.getProperty("jdbc.url"), Util.PROPERTY.getProperty("db.username"), Util.PROPERTY.getProperty("db.password"));
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString()/*, e*/);
		}
		return c;
	}
	
	public static void close(Connection c) {
		if (c != null)
			try {
				c.close();
			} catch (SQLException e) {
				Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			}
	}

	public static void close(Statement s) {
		if (s != null)
			try {
				s.close();
			} catch (SQLException e) {
				Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			}
	}

	public static void close(ResultSet rs) {
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException e) {
				Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			}
	}
	
	public static void close(ResultSet rs, Statement s, Connection c) {
		close(rs);
		close(s);
		close(c);
	}

	public static void close(Statement s, Connection c) {
		close(s);
		close(c);
	}
	
	public static PreparedStatement prepareStatement(Connection c, String sql,
			boolean retGenKeys, Object... values) throws SQLException {
		PreparedStatement ps = c.prepareStatement(sql,
				retGenKeys ? Statement.RETURN_GENERATED_KEYS
						: Statement.NO_GENERATED_KEYS);

		for (int i = 0; i < values.length; i++)
			ps.setObject(i + 1, values[i]);

		return ps;
	}
	
	//VALIDATORI
	public static ValidatorBase requredFieldValidator(JFXTextField textField) {
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
	
	public static ValidatorBase requredFieldValidator(JFXPasswordField passwordField) {
    	ValidatorBase requiredFieldValidator = new RequiredFieldValidator();
	    requiredFieldValidator.setMessage("Obavezan unos");
	    requiredFieldValidator.setIcon(new ImageView());
	    
	    
	    passwordField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)->{
	        if(!newValue) {
	        	passwordField.validate();
	        }
	    });
	    
	    
	    passwordField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue)->{
	        //if(textField.getText().trim().isEmpty()) {
	        	passwordField.validate();
	        //} else {
	        //	textField.resetValidation();
	        //}
	    });
	    
	    
	    return requiredFieldValidator;
    }
	
	public static ValidatorBase integerValidator(JFXTextField textField) {
		ValidatorBase integerValidator = new IntegerValidator();
	    integerValidator.setMessage("Nije broj");
	    integerValidator.setIcon(new ImageView());
	    textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)->{
	        if(!newValue) {
	        	textField.validate();
	        }
	    });
	    textField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue)->{
	        	textField.validate();
	    });
	    return integerValidator;
	}
	
	public static ValidatorBase doubleValidator(JFXTextField textField) {
		ValidatorBase doubleValidator = new DoubleValidator();
		doubleValidator.setMessage("Nije broj");
		doubleValidator.setIcon(new ImageView());
	    textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)->{
	        if(!newValue) {
	        	textField.validate();
	        }
	    });
	    textField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue)->{
	        	textField.validate();
	    });
	    return doubleValidator;
	}
	
	public static ValidatorBase jmbgValidator(JFXTextField textField) {
		ValidatorBase jmbgValidator = new ValidatorBase("Nekorektan unos") {
			@Override
			protected void eval() {
				/*
				if(!jmbgTextField.getText().isEmpty()
						&& jmbgTextField.getText().length() != 13) {
		        	 hasErrors.set(true);
		       	*/
				if(!textField.getText().isEmpty()
	        			&& !textField.getText().matches("^[0-9]{13}$")) {
	        		hasErrors.set(true);
		        } else {
		        	 hasErrors.set(false);
		        	 /*
		        	 try {
		        		 Long.parseLong(jmbgTextField.getText());
		        	 } catch(NumberFormatException e) {
		        		 hasErrors.set(true);
		        	 }
		        	 */
		        }
			}
		};
		jmbgValidator.setIcon(new ImageView());
		return jmbgValidator;
	}
	
	public static ValidatorBase jibValidator(JFXTextField textField) {
		ValidatorBase jibValidator = new ValidatorBase("Nekorektan unos") {
			@Override
			protected void eval() {
				if(!textField.getText().isEmpty()
	        			&& !textField.getText().matches("^[0-9]{13}$")) {
	        		hasErrors.set(true);
		        } else {
		        	 hasErrors.set(false);
		        }
			}
		};
		jibValidator.setIcon(new ImageView());
		return jibValidator;
	}
	
	public static ValidatorBase samePasswordValidator(JFXPasswordField passwordField1, JFXPasswordField passwordField2) {
		ValidatorBase samePasswordValidator = new ValidatorBase("Ne poklapa se") {
			@Override
			protected void eval() {
				if(!passwordField1.getText().trim().isEmpty()
		        		&& !passwordField2.getText().trim().isEmpty()
		        			&& !passwordField1.getText().equals(passwordField2.getText())) {
		        	 hasErrors.set(true);
		        } else {
		        	 hasErrors.set(false);
		        }
			}
		};
		samePasswordValidator.setIcon(new ImageView());
		return samePasswordValidator;
	}
	
	public static ValidatorBase emailValidator(JFXTextField textField) {
		ValidatorBase emailValidator = new ValidatorBase("Nekorektan unos") {
			@Override
			protected void eval() {
				if(!textField.getText().isEmpty()
						&& !textField.getText().matches("^[a-z0-9]{3,}@[a-z0-9]{2,}\\.[a-z]{2,}$")) {
					hasErrors.set(true);
				} else {
					hasErrors.set(false);
				}
			}
		};
		emailValidator.setIcon(new ImageView());
		return emailValidator;
	}
	
	private static List<String> postalCodeList = new ArrayList<>();
	
	public static List<String> getPostalCodeList() {
		return postalCodeList;
	}
	/*
	public static void setPostalCodeList(List<String> postalCodeList) {
		Util.postalCodeList = postalCodeList;
	}
	*/
	public static ValidatorBase postalCodeValidator(JFXTextField textField) {
		System.out.println(getPostalCodeList());
		ValidatorBase postalCodeValidator = new ValidatorBase("Nekorektan unos") {
			@Override
			protected void eval() {
				if(!textField.getText().isEmpty()
						&& !getPostalCodeList().contains(textField.getText())) {
					hasErrors.set(true);
				} else {
					hasErrors.set(false);
				}
			}
		};
		postalCodeValidator.setIcon(new ImageView());
		return postalCodeValidator;
	}

	private static void loadPostalCodes() {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, "select PostanskiBroj from mjesto", false);
			r = s.executeQuery();
			while(r.next())
				getPostalCodeList().add(String.valueOf(r.getInt(1)));
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
	
	public static ValidatorBase webValidator(JFXTextField textField) {
		ValidatorBase webValidator = new ValidatorBase("Nekorektan unos") {
			@Override
			protected void eval() {
				if(!textField.getText().isEmpty()
						&& !textField.getText().matches("https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]\\.[^\\s]{2,}")) {
					hasErrors.set(true);
				} else {
					hasErrors.set(false);
				}
			}
		};
		webValidator.setIcon(new ImageView());
		return webValidator;
	}
		
	public static ValidatorBase phoneValidator(JFXTextField textField) {
		ValidatorBase phoneValidator = new ValidatorBase("Nekorektan unos") {	
			@Override
			protected void eval() {
				if(!textField.getText().isEmpty()
						&& !textField.getText().matches("^\\+(?:[0-9] ?){6,14}[0-9]$")) {
						//!textField.getText().matches("^((\\+)[0-9]{2})?[0-9]{3}(/)[0-9]{3}(-)[0-9]{3}[0-9]*")) {
					hasErrors.set(true);
				} else {
					hasErrors.set(false);
				}
			}
		};
		phoneValidator.setIcon(new ImageView());
		return phoneValidator;
	}
}
