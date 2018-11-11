package org.unibl.etf.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
	}
	
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
}
