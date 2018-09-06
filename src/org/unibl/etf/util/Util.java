package org.unibl.etf.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 
 * @author mcfc93
 *
 */
//klasa koja sadrzi sve pomocne alate
public class Util {
	public static Properties PROPERTY = new Properties();
	public static final Logger LOGGER = Logger.getLogger("Logger");
	
	
	static {
		//postavljanje Logger-a
	    try {
	    	//FileHandler fileHandler = new FileHandler(System.getProperty("user.home") + File.separator + "error.log", true);
	    	FileHandler fileHandler = new FileHandler("logs/error.log", true);	//append
	        LOGGER.addHandler(fileHandler);
	        SimpleFormatter formatter = new SimpleFormatter();	//za formatiran ispis
	        fileHandler.setFormatter(formatter);
	        //LOGGER.setUseParentHandlers(false);	//da ne prikazuje Exception na Console
	    } catch (SecurityException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		
	    //ucitavanje properties fajla
		try {
			//p.load(new FileInputStream(System.getProperty("user.home") + File.separator + "config.properties"));
			PROPERTY.load(new FileInputStream("config.properties"));
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		
//System.out.println(PROPERTY);
	}
}
