package org.unibl.etf.autobuska_stanica;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.unibl.etf.util.Util;

public class AutobuskaStanica {
	private String jib;
	private String naziv;
	private String adresa;
	private int brojPoste;
	private String brojTelefona;
	private int brojPerona;
	
	public AutobuskaStanica() {
		super();
	}
	
	public AutobuskaStanica(String jib, String naziv, String adresa, int brojPoste, String brojTelefona, int brojPerona) {
		super();
		this.jib= jib;
		this.naziv = naziv;
		this.adresa = adresa;
		this.brojPoste = brojPoste;
		this.brojTelefona = brojTelefona;
		this.brojPerona = brojPerona;
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

	public int getBrojPoste() {
		return brojPoste;
	}

	public void setBrojPoste(int brojPoste) {
		this.brojPoste = brojPoste;
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

	@Override
	public String toString() {
		return "AutobuskaStanica [jib=" + jib + ", naziv=" + naziv + ", adresa=" + adresa + ", brojPoste=" + brojPoste
				+ ", brojTelefona=" + brojTelefona + ", brojPerona=" + brojPerona + "]";
	}
	
	public static List<AutobuskaStanica> listaStanica() {
		//AutobuskaStanica as=new AutobuskaStanica();
		
		List<AutobuskaStanica> listaAutobuskihStanica = new ArrayList<>();
		
		Connection c = null;
		CallableStatement s = null;
		ResultSet r = null;
	    try {
	       	// 1.Get a connection to database
	       	//c = DriverManager.getConnection("jdbc:mysql://localhost:3306/bus?autoReconnect=true&useSSL=false","root","student");
	    	//c = DriverManager.getConnection(Util.PROPERTY.getProperty("jdbc.url"), Util.PROPERTY.getProperty("db.username"), Util.PROPERTY.getProperty("db.password"));
	       	c=Util.getConnection();
	    	// 2.Create a statement
	       	s = c.prepareCall("{call showBusStations()}");
	       	// 3.Execute a SQL query
	       	r = s.executeQuery();
	        // 4.Process the result set
	       	while(r.next()) {
		       	//as.setNaziv(r.getString("Naziv"));
		       	//as.setAdresa(r.getString("Adresa"));
		       	//as.setBrojPoste(r.getInt("BrojPoste"));
		       	//as.setBrojTelefona(r.getString("BrojTelefona"));
		       	//as.setBrojPerona(r.getInt("BrojPerona"));
		       	
	    		//System.out.println(as);
	    		
	    		listaAutobuskihStanica.add(new AutobuskaStanica("TBD", r.getString("Naziv"), r.getString("Adresa"), r.getInt("PostanskiBroj"), r.getString("BrojTelefona"), r.getInt("BrojPerona")));
	        }
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(r,s,c);
	    }
	    
	    
	    return listaAutobuskihStanica;
	}
}
