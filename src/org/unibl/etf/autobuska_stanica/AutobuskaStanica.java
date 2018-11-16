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
	private String adresa;
	private int postanskiBroj;
	private String brojTelefona;
	private int brojPerona;
	private String webStranica;
	//private String email;
	
	public AutobuskaStanica() {
		super();
	}
	
	public AutobuskaStanica(String jib, String naziv, String adresa, int postanskiBroj, String brojTelefona, int brojPerona, String webStranica) {
		super();
		this.jib= jib;
		this.naziv = naziv;
		this.adresa = adresa;
		this.postanskiBroj = postanskiBroj;
		this.brojTelefona = brojTelefona;
		this.brojPerona = brojPerona;
		this.webStranica = webStranica;
	}
	
	public String getJib() {
		return naziv;
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

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public int getPostanskiBroj() {
		return postanskiBroj;
	}

	public void setPostanskiBroj(int postanskiBroj) {
		this.postanskiBroj = postanskiBroj;
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

	@Override
	public String toString() {
		return "AutobuskaStanica [jib=" + jib + ", naziv=" + naziv + ", adresa=" + adresa + ", postanskiBroj=" + postanskiBroj
				+ ", brojTelefona=" + brojTelefona + ", brojPerona=" + brojPerona + "]";
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
		       	listaAutobuskihStanica.add(new AutobuskaStanica("TBD", r.getString("Naziv"), r.getString("Adresa"), r.getInt("PostanskiBroj"), r.getString("BrojTelefona"), r.getInt("BrojPerona"), "TBD"));
	        }
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(r,s,c);
	    }
	    return listaAutobuskihStanica;
	}
	
	public static boolean dodavanjeAutobuskeStanice(String jib, String naziv, String adresa, int postanskiBroj, String brojTelefona, int brojPerona, String webStranica) {
		Connection c = null;
		CallableStatement s = null;
		ResultSet r = null;
	    try {
	       	c=Util.getConnection();
	    	s = c.prepareCall("{call addBusStation(?,?,?,?,?,?,?)}");
	    	s.setString(1, jib);
	       	s.setString(2, naziv);
	       	s.setString(3, adresa);
	       	s.setInt(4, postanskiBroj);
	       	s.setString(5, brojTelefona);
	       	s.setInt(6, brojPerona);
	       	s.setString(7, webStranica);
	       	r = s.executeQuery();
	       	return true;
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(r,s,c);
	    }
	    return false;
	}
}
