package org.unibl.etf.prijava;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.Base64;
import java.util.logging.Level;

import org.unibl.etf.zaposleni.AdministrativniRadnik;
import org.unibl.etf.zaposleni.Administrator;
import org.unibl.etf.zaposleni.SalterskiRadnik;
import org.unibl.etf.zaposleni.Zaposleni;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.unibl.etf.util.Util;

public class Nalog {
	private String korisnickoIme;
	private String lozinka;
	private int idStanice;
	private boolean prijavljen;		//da li je trenutni korisnik prijavljen
	private Zaposleni zaposleni;	//podaci o vlasniku naloga
	
	public Nalog() {
		super();
	}
	
	public Nalog(String korisnickoIme, String lozinka, int idStanice, boolean prijavljen, Zaposleni zaposleni) {
		super();
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.idStanice = idStanice;
		this.prijavljen = prijavljen;
		this.zaposleni=zaposleni;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}
	
	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}
	
	public String getLozinka() {
		return lozinka;
	}
	
	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}
	
	public int getIdStanice() {
		return idStanice;
	}
	
	public void setIdStanice(int idStanice) {
		this.idStanice = idStanice;
	}
	
	public boolean isPrijavljen() {
		return prijavljen;
	}
	
	public void setPrijavljen(boolean prijavljen) {
		this.prijavljen = prijavljen;
	}
	
	public Zaposleni getZaposleni() {
		return zaposleni;
	}

	public void setZaposleni(Zaposleni zaposleni) {
		this.zaposleni = zaposleni;
	}

	@Override
	public String toString() {
		return "Nalog [korisnickoIme=" + korisnickoIme + ", lozinka=" + lozinka + ", idStanice=" + idStanice
				+ ", prijavljen=" + prijavljen + "]";
	}
	
	public static Nalog prijava(String korisnickoIme, String lozinka) {
		Nalog nalog=null;
		//trazenje hash vrijednosti lozinke
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(lozinka.getBytes(StandardCharsets.UTF_8));
			//lozinka = Base64.getEncoder().encodeToString(hash);
			StringBuilder sb = new StringBuilder();
		    for (byte b : hash) {
		        sb.append(String.format("%02X", b));
		    }
		    lozinka=sb.toString();
		    //System.out.println(lozinka);
		} catch(NoSuchAlgorithmException e) {
			
		}
		//uspostavljanje veze sa bazom i autentifikacija korisnika
		Connection c = null;
		CallableStatement s = null;
		ResultSet r = null;
	    try {
	       	// 1.Get a connection to database
	       	c = DriverManager.getConnection("jdbc:mysql://localhost:3306/bus?autoReconnect=true&useSSL=false","root","student");
	       	// 2.Create a statement
	       	s = c.prepareCall("{call checkAuthentication(?,?)}");
	       	s.setString(1,korisnickoIme);
	       	s.setString(2,lozinka);
	       	// 3.Execute a SQL query
	       	r = s.executeQuery();
	        // 4.Process the result set
	       	if(r.next()) {
	       		
	       		
//ucitavanje podataka o zaposlenom iz baze
//potrebno za sad samo Ime i Prezime
//u konstruktore proslijediti Ime, Prezime a za sve ostale parametre staviti null
//prvo potrebno definisati polja klase Zaposleni koja ce se sigurno nalaziti u njoj
	       		
	       		//privremeno rjesenje
	       		String jmbg=null;
	       		Statement ss = null;
	    		ResultSet rr = null;
	       		
	    		ss=c.createStatement();
	    		rr=ss.executeQuery("select JMBG from Nalog where KorisnickoIme='" + korisnickoIme + "'");
	    		rr.next();
	    		jmbg=rr.getString("JMBG");
	    		rr=ss.executeQuery("select Ime, Prezime from Zaposleni where JMBG=" + jmbg);
	    		rr.next();
	    		//System.out.println(rr.getString("Ime") + " " + rr.getString("Prezime"));
	    		
	    		
	    		
	    		
	    		
	    		
	    		
	    		nalog=new Nalog(korisnickoIme, lozinka, 0, true, null);
	       		if("Administrator".equals(r.getString("Tip"))) {
	       			nalog.zaposleni=new Administrator(rr.getString("Ime"), rr.getString("Prezime"));
	       			//System.out.println(nalog.zaposleni.getIme());
	       		} else if("AdministrativniRadnik".equals(r.getString("Tip"))) {
	       			nalog.zaposleni=new AdministrativniRadnik();
	        	} else {
	        		nalog.zaposleni=new SalterskiRadnik();
	        	}
	        }/* else {
	        	return null;
            }*/
	    } catch(SQLException e) {
	       	e.printStackTrace();
	    } finally {
	    	if(r != null) {
	    		try {
	    			r.close();
	    		} catch (SQLException e) {
	    			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    		}
	    	}
	        if( s != null) {
	            try {
	            	s.close();
	            } catch (SQLException e) {
	            	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	            }
	        }
	        if(c != null) {
	            try {
	            	c.close();
	            } catch (SQLException e) {
	            	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	            }
	        }
	    }
	    return nalog;
	}
	
	public boolean odjava() {
		prijavljen=false;
		zaposleni=null;
		return true;
	}
	
	//boolean provjeriKorisnickoIme(String korisnickoIme)
	//boolean provjeriLozinku(String lozinka)
	//hash code
}
