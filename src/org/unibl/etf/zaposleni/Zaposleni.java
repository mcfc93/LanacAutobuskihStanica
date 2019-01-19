package org.unibl.etf.zaposleni;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.unibl.etf.util.Util;

public abstract class Zaposleni implements Serializable {
	private static final long serialVersionUID = 1L;
	private String ime;
	private String prezime;
	private String jmbg;
	private String adresa;
	private String strucnaSprema;
	private String brojTelefona;
	private String pol;
	//private Date datumRodjenja;
	private String email;
	
	public Zaposleni() {
		super();
	}
	
	public Zaposleni(String ime, String prezime) {
		this.ime=ime;
		this.prezime=prezime;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	
	public String getJmbg() {
		return jmbg;
	}

	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}

	public String getPol() {
		return pol;
	}

	public void setPol(String pol) {
		this.pol = pol;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getBrojTelefona() {
		return brojTelefona;
	}

	public void setBrojTelefona(String brojTelefona) {
		this.brojTelefona = brojTelefona;
	}

	public String getStrucnaSprema() {
		return strucnaSprema;
	}

	public void setStrucnaSprema(String strucnaSprema) {
		this.strucnaSprema = strucnaSprema;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Zaposleni [ime=" + ime + ", prezime=" + prezime + ", jmbg=" + jmbg + ", adresa=" + adresa
				+ ", strucnaSprema=" + strucnaSprema + ", brojTelefona=" + brojTelefona + ", email=" + email + "]";
	}

	public void selectZaposleni(String korisnickoIme) {
		Connection c = null;
		CallableStatement s = null;
		ResultSet r = null;
	    try {
	       	c=Util.getConnection();
	    	s = c.prepareCall("{call selectZaposleni(?)}");
	       	s.setString(1, korisnickoIme);
	       	r = s.executeQuery();
	        if(r.next()) {
	       		this.setIme(r.getString("Ime"));
	       		this.setPrezime(r.getString("Prezime"));
	       		this.setJmbg(r.getString("JMBG"));
	       		this.setPol(r.getString("Pol"));
	       		this.setAdresa(r.getString("Adresa") + ", " + r.getString("PostanskiBroj") + " " + r.getString("mjesto.Naziv"));
	       		this.setBrojTelefona(r.getString("BrojTelefona"));
	       		this.setEmail(r.getString("Email"));
	       		this.setStrucnaSprema(r.getString("StrucnaSprema"));
	       	}
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(r,s,c);
	    }
	}
	
	private static List<String> jmbgList = new ArrayList<>();
	
	public static List<String> getJmbgList() {
		return jmbgList;
	}
	/*
	public static void setJmbgList(List<String> jmbgList) {
		Zaposleni.jmbgList = jmbgList;
	}
	*/
	public static void loadJmbgs() {
		getJmbgList().clear();
		
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, "select JMBG from zaposleni", false);
			r = s.executeQuery();
			while(r.next()) {
				getJmbgList().add(r.getString(1));
			}
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
}
