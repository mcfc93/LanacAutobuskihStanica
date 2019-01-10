package org.unibl.etf.prijava;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import org.unibl.etf.zaposleni.Zaposleni;

import org.unibl.etf.zaposleni.Administrator;
import org.unibl.etf.zaposleni.AdministrativniRadnik;
import org.unibl.etf.zaposleni.SalterskiRadnik;
import org.unibl.etf.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Nalog implements Serializable {
	private static final long serialVersionUID = 1L;
	private String korisnickoIme;
	private String lozinka;
	private String idStanice;
	private String tip;
	private boolean prijavljen;		//da li je trenutni korisnik prijavljen
	private Zaposleni zaposleni;	//podaci o vlasniku naloga
	
	public Nalog() {
		super();
	}
	
	public Nalog(String korisnickoIme, String lozinka, String idStanice, String tip, boolean prijavljen, Zaposleni zaposleni) {
		super();
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.idStanice = idStanice;
		this.tip=tip;
		this.prijavljen = prijavljen;
		this.zaposleni=zaposleni;
	}
	
	public Nalog(String korisnickoIme, String lozinka, String idStanice, String tip, String ime, String prezime, String jmbg) {
		super();
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.idStanice = idStanice;
		this.tip=tip;
		this.zaposleni=new Administrator();
		zaposleni.setIme(ime);
		zaposleni.setPrezime(prezime);
		zaposleni.setJmbg(jmbg);
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
	
	public String getIdStanice() {
		return idStanice;
	}
	
	public void setIdStanice(String idStanice) {
		this.idStanice = idStanice;
	}
	
	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
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
	
	public String getIme() {
		return zaposleni.getIme();
	}
	
	public String getPrezime() {
		return zaposleni.getPrezime();
	}
	
	public String getJmbg() {
		return zaposleni.getJmbg();
	}

	@Override
	public String toString() {
		return "Nalog [korisnickoIme=" + korisnickoIme + ", lozinka=" + lozinka + ", idStanice=" + idStanice
				+ ", tip=" + tip + ", prijavljen=" + prijavljen + "]";
	}
	
	public static String hash(String lozinka/*, String salt*/) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(lozinka/* + salt)*/.getBytes(StandardCharsets.UTF_8));
			//lozinka = Base64.getEncoder().encodeToString(hash);
			StringBuilder sb = new StringBuilder();
			//sb.append(salt + "#");
		    for (byte b : hash) {
		        sb.append(String.format("%02X", b));
		    }
		    lozinka=sb.toString();
		    //System.out.println(lozinka);
		} catch(NoSuchAlgorithmException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return lozinka;
	}
	
	public static Nalog prijava(String korisnickoIme, String lozinka) throws SQLException {
		Nalog nalog=null;
		//trazenje hash vrijednosti lozinke
		lozinka=hash(lozinka);
		//System.out.println(lozinka);
		//uspostavljanje veze sa bazom i autentifikacija korisnika
		Connection c = null;
		CallableStatement s = null;
		ResultSet r = null;
	    try {
	       	// 1.Get a connection to database
	       	c=Util.getConnection();
	    	// 2.Create a statement
	       	s = c.prepareCall("{call checkAuthentication(?,?)}");
	       	s.setString(1, korisnickoIme);
	       	s.setString(2, lozinka);
	       	// 3.Execute a SQL query
	       	r = s.executeQuery();
	        // 4.Process the result set
	       	if(r.next()) {
	        	//ucitavanje podataka o zaposlenom iz baze
	    		nalog=new Nalog(korisnickoIme, lozinka, r.getString("JIBStanice"), r.getString("Tip") , true, null);
	       		
	    		System.out.println(nalog);
	    		
	    		if("Administrator".equals(r.getString("Tip"))) {
	       			nalog.zaposleni=new Administrator();
	       			nalog.zaposleni.selectZaposleni(nalog.korisnickoIme);
	       		} else if("Administrativni radnik".equals(r.getString("Tip"))) {
	       			nalog.zaposleni=new AdministrativniRadnik();
	       			nalog.zaposleni.selectZaposleni(nalog.korisnickoIme);
	        	} else {
	        		nalog.zaposleni=new SalterskiRadnik();
	        		nalog.zaposleni.selectZaposleni(nalog.korisnickoIme);
	        	}
	    		
System.out.println(nalog.zaposleni);
	        }/* else {
	        	return null;
            }*/
	    /*
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    */
	    } finally {
	    	Util.close(r,s,c);
	    }
	    return nalog;
	}
	
	public boolean odjava() {
		prijavljen=false;
		zaposleni=null;
		return true;
	}

	//public static boolean provjeriKorisnickoIme(String korisnickoIme)
	
	
	public static boolean provjeraLozinke(String lozinka) {
		return PrijavaController.nalog.getLozinka().equals(lozinka);
	}
	
	public static boolean promjenaLozinke(String novaLozinka) {
		Connection c = null;
		CallableStatement s = null;
	    try {
	       	c=Util.getConnection();
	    	s = c.prepareCall("{call updatePassword(?,?,?)}");
	       	s.setString(1, PrijavaController.nalog.getKorisnickoIme());
	       	s.setString(2, novaLozinka);
	       	s.registerOutParameter(3, java.sql.Types.INTEGER);
	       	s.execute();
	        if(s.getInt(3) == 1) {
	        	return true;
	       	}
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(s,c);
	    }
		return false;
	}
	
	public static boolean dodavanjeNaloga(String korisnickoIme, String lozinka, String idStanice, String tipNaloga, String ime, String prezime, String jmbg, String pol, String adresa, int postanskiBroj, String strucnaSprema, String brojTelefona, String email) {
		Connection c = null;
		CallableStatement s = null;
	    try {
	       	c=Util.getConnection();
	    	s = c.prepareCall("{call addNewEmployee(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
	       	s.setString(1, korisnickoIme);
	       	s.setString(2, lozinka);
	       	s.setString(3, idStanice);
	       	s.setString(4, tipNaloga);
	       	s.setString(5, ime);
	       	s.setString(6, prezime);
	       	s.setString(7, jmbg);
	       	s.setString(8, pol);
	       	s.setString(9, adresa);
	       	s.setInt(10, postanskiBroj);
	       	s.setString(11, strucnaSprema);
	       	s.setString(12, brojTelefona);
	       	s.setString(13, email);
	       	s.execute();
	       	return true;
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(s,c);
	    }
		return false;
	}
	
	public static boolean izmjenaNaloga() {
		return false;
	}
	
	public static boolean brisanjeNaloga(String jmbg) {
		Connection c = null;
		CallableStatement s = null;
	    try {
	       	c=Util.getConnection();
	    	s = c.prepareCall("{call removeEmployee(?)}");
	    	s.setString(1, jmbg);
	       	s.execute();
	       	return true;
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(s,c);
	    }
	    return false;
	}
	
	public static List<Nalog> listaNaloga() {
		List<Nalog> listaNaloga = new ArrayList<>();
		Connection c = null;
		CallableStatement s = null;
		ResultSet r = null;
	    try {
	       	c=Util.getConnection();
	    	s = c.prepareCall("{call showEmployees()}");
	       	r = s.executeQuery();
	        while(r.next()) {
		       	listaNaloga.add(new Nalog(r.getString("KorisnickoIme"), r.getString("Lozinka"), r.getString("JIBStanice"), r.getString("Tip"), r.getString("Ime"), r.getString("Prezime"), r.getString("JMBG")));
	        }
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(r,s,c);
	    }
	    return listaNaloga;
	}
	
	private static List<String> usernameList = new ArrayList<>();
	
	public static List<String> getUsernameList() {
		return usernameList;
	}
	/*
	public static void setUsernameList(List<String> usernameList) {
		Nalog.usernameList = usernameList;
	}
	*/
	public static void loadUsernames() {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, "select KorisnickoIme from nalog", false);
			r = s.executeQuery();
			while(r.next()) {
				getUsernameList().add(r.getString(1));
			}
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
}
