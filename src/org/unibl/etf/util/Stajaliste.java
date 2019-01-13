package org.unibl.etf.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.unibl.etf.karta.Relacija;
import org.unibl.etf.salterski_radnik.InformacijeController;

public class Stajaliste extends Mjesto {
	String nazivStajalista;
	int idStajalista;

	/*
	 * konstruktor za ucitavanje relacija po id linije
	 */
	
	public Stajaliste(int idStajalista) {
		this.idStajalista = idStajalista;
	}
	
	
	public Stajaliste(String naziv, int postanskiBroj, String nazivStajalista, int idStajalista) {
		super(naziv, postanskiBroj);
		// TODO Auto-generated constructor stub
		this.nazivStajalista = nazivStajalista;
		this.idStajalista = idStajalista;
	}

	/*
	 * konstruktor za ucitavanje i storniranje karata
	 */
	public Stajaliste(String nazivStajalista) {
		this.nazivStajalista = nazivStajalista;
	}
	
	public Stajaliste(int idStajalista, String nazivStajalista) {
		super();
		this.nazivStajalista = nazivStajalista;
		this.idStajalista = idStajalista;
	}

	public int getIdStajalista() {
		return idStajalista;
	}

	public void setIdStajalista(int idStajalista) {
		this.idStajalista = idStajalista;
	}

public String getNazivStajalista() {
		return nazivStajalista;
	}

	public void setNazivStajalista(String nazivStajalista) {
		this.nazivStajalista = nazivStajalista;
	}

	public static void setStajalisteList(List<Stajaliste> stajalisteList) {
		Stajaliste.stajalisteList = stajalisteList;
	}

public Stajaliste() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Stajaliste(int idStajalista, int postanskiBroj, String nazivMjesta, String nazivStajalista) {
		super(nazivMjesta,postanskiBroj);
		// TODO Auto-generated constructor stub
		this.nazivStajalista = nazivStajalista;
		this.idStajalista = idStajalista;
	}

	public Stajaliste(String nazivMjesta, String nazivStajalista) {
		super(nazivMjesta);
		// TODO Auto-generated constructor stub
		this.nazivStajalista = nazivStajalista;		
	}

/*private static List<Stajaliste> stajalisteList = new ArrayList<>();
	
	public static List<Stajaliste> getStajalisteList() {
		return stajalisteList;
	}
	
	public static void loadStajalista() {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, "select IdStajalista,autobusko_stajaliste.Naziv,autobusko_stajaliste.PostanskiBroj,mjesto.Naziv from autobusko_stajaliste join mjesto on mjesto.PostanskiBroj=autobusko_stajaliste.PostanskiBroj", false);
			r = s.executeQuery();
			while(r.next())
				getStajalisteList().add(new Stajaliste(r.getInt("autobusko_stajaliste.IdStajalista"), r.getInt("autobusko_stajaliste.PostanskiBroj"), r.getString("mjesto.Naziv"), r.getString("autobusko_stajaliste.Naziv")));
			
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}*/
	
private static List<Stajaliste> stajalisteList = new ArrayList<>();
	
	public static List<Stajaliste> getStajalisteList() {
		return stajalisteList;
	}
	
	public static void loadStajalista() {
		getStajalisteList().clear();

		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, "select IdStajalista,autobusko_stajaliste.Naziv,autobusko_stajaliste.PostanskiBroj,mjesto.Naziv from autobusko_stajaliste join mjesto on mjesto.PostanskiBroj=autobusko_stajaliste.PostanskiBroj", false);
			r = s.executeQuery();
			while(r.next())
				getStajalisteList().add(new Stajaliste(r.getInt("autobusko_stajaliste.IdStajalista"), r.getInt("autobusko_stajaliste.PostanskiBroj"), r.getString("mjesto.Naziv"), r.getString("autobusko_stajaliste.Naziv")));
			
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}

	public static Stajaliste getStajaliste(int idStajalista) {
		// TODO Auto-generated method stub
		Connection c = null;
		ResultSet r = null;
		PreparedStatement s = null;
		String sql = "select * from autobusko_stajaliste where IdStajalista=?";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, idStajalista);
			r = s.executeQuery();
			if(r.next()) {
				System.out.println("nasao");
				return new Stajaliste(r.getInt("IdStajalista"), r.getString("NazivStajalista"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public String toString() {
		if(nazivStajalista.equals(naziv))
			return nazivStajalista;
		return naziv + " (" + nazivStajalista +")";

	}
	
	
}
