package org.unibl.etf.autobuska_stanica;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.util.Util;

public class AutobuskaStanica {
	private String jib;
	private String naziv;
	private String ulicaIBroj;
	private int postanskiBroj;
	private String grad;
	private String brojTelefona;
	private int brojPerona;
	private String webStranica;
	private String email;
	
	public AutobuskaStanica() {
		super();
	}
	
	public AutobuskaStanica(String jib, String naziv, String ulicaIBroj, int postanskiBroj, String grad, String brojTelefona, int brojPerona, String webStranica, String email) {
		super();
		this.jib= jib;
		this.naziv = naziv;
		this.ulicaIBroj = ulicaIBroj;
		this.postanskiBroj = postanskiBroj;
		this.grad = grad;
		this.brojTelefona = brojTelefona;
		this.brojPerona = brojPerona;
		this.webStranica = webStranica;
		this.email=email;
	}
	
	public String getJib() {
		return jib;
	}

	public void setJib(String jib) {
		this.jib = jib;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getUlicaIBroj() {
		return ulicaIBroj;
	}

	public void setUlicaIBroj(String ulicaIBroj) {
		this.ulicaIBroj = ulicaIBroj;
	}

	public int getPostanskiBroj() {
		return postanskiBroj;
	}

	public void setPostanskiBroj(int postanskiBroj) {
		this.postanskiBroj = postanskiBroj;
	}

	public String getGrad() {
		return grad;
	}

	public void setGrad(String grad) {
		this.grad = grad;
	}

	public String getBrojTelefona() {
		return brojTelefona;
	}

	public void setBrojTelefona(String brojTelefona) {
		this.brojTelefona = brojTelefona;
	}

	public int getBrojPerona() {
		return brojPerona;
	}

	public void setBrojPerona(int brojPerona) {
		this.brojPerona = brojPerona;
	}
	
	public String getWebStranica() {
		return webStranica;
	}

	public void setWebStranica(String webStranica) {
		this.webStranica = webStranica;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAdresa() {
		return ulicaIBroj + ", " + postanskiBroj + " " + grad;
	}

	@Override
	public String toString() {
		return "AutobuskaStanica [jib=" + jib + ", naziv=" + naziv + ", adresa=" + ulicaIBroj + ", "
				+ postanskiBroj + " " + grad + ", brojTelefona=" + brojTelefona + ", brojPerona=" + brojPerona
				+ ", webStranica=" + webStranica + ", email=" + email + "]";
	}
	
	public static List<AutobuskaStanica> listaStanica() {
		List<AutobuskaStanica> listaAutobuskihStanica = new ArrayList<>();
		Connection c = null;
		CallableStatement s = null;
		ResultSet r = null;
	    try {
	       	c=Util.getConnection();
	    	s = c.prepareCall("{call showBusStations()}");
	       	r = s.executeQuery();
	        while(r.next()) {
		       	listaAutobuskihStanica.add(new AutobuskaStanica(r.getString("JIBStanice"), r.getString("Naziv"), r.getString("Adresa"), r.getInt("PostanskiBroj"), r.getString("mjesto.naziv"), r.getString("BrojTelefona"), r.getInt("BrojPerona"), r.getString("WebStranica"), r.getString("Email")));
	        }
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(r,s,c);
	    }
	    return listaAutobuskihStanica;
	}
	
	public static boolean dodavanjeAutobuskeStanice(String jib, String naziv, String adresa, int postanskiBroj, String brojTelefona, int brojPerona, String webStranica, String email) {
		Connection c = null;
		CallableStatement s = null;
		ResultSet r = null;
	    try {
	       	c=Util.getConnection();
	    	s = c.prepareCall("{call addBusStation(?,?,?,?,?,?,?,?)}");
	    	s.setString(1, jib);
	       	s.setString(2, naziv);
	       	s.setString(3, adresa);
	       	s.setInt(4, postanskiBroj);
	       	s.setString(5, brojTelefona);
	       	s.setInt(6, brojPerona);
	       	s.setString(7, webStranica);
	       	s.setString(8, email);
	       	r = s.executeQuery();
	       	return true;
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(r,s,c);
	    }
	    return false;
	}
	
	public static boolean brisanjeAutobuskeStanice(String jib) {
		Connection c = null;
		CallableStatement s = null;
	    try {
	       	c=Util.getConnection();
	    	s = c.prepareCall("{call removeBusStation(?)}");
	    	s.setString(1, jib);
	       	s.execute();
	       	return true;
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(s,c);
	    }
	    return false;
	}
}
