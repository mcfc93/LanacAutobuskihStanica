package org.unibl.etf.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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
	}

	public Stajaliste(int idStajalista, int postanskiBroj, String nazivMjesta, String nazivStajalista) {
		super(nazivMjesta,postanskiBroj);
		this.nazivStajalista = nazivStajalista;
		this.idStajalista = idStajalista;
	}

	public Stajaliste(String nazivMjesta, String nazivStajalista) {
		super(nazivMjesta);
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
			Util.showBugAlert();
		}
	}

	public static Stajaliste getStajaliste(int idStajalista) {
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
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			Util.showBugAlert();
		}
		return null;
	}
	
	public static List<Stajaliste> getStajalistaStanicaList() {
		List<Stajaliste> stajalistaStanicaList = new ArrayList<>();
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select autobuska_stanica.IdStajalista,autobusko_stajaliste.Naziv,mjesto.PostanskiBroj,mjesto.Naziv from autobuska_stanica join "
				+ "(autobusko_stajaliste,mjesto) on "
				+ "(autobuska_stanica.IdStajalista=autobusko_stajaliste.IdStajalista and autobusko_stajaliste.PostanskiBroj=mjesto.PostanskiBroj)";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false);
			r = s.executeQuery();
			while(r.next()) {
				Stajaliste stajaliste = new Stajaliste(r.getString("mjesto.Naziv"), r.getInt("mjesto.PostanskiBroj"), r.getString("autobusko_stajaliste.Naziv"), r.getInt("autobuska_stanica.IdStajalista"));
				stajalistaStanicaList.add(stajaliste);
			}
			return stajalistaStanicaList;
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			Util.showBugAlert();
		}
		return null;
	}

	public static List<Stajaliste> getStajalistaBezStanicaList() {
		List<Stajaliste> stajalistaBezStanicaList = new ArrayList<>();
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select autobuska_stanica.IdStajalista,autobusko_stajaliste.Naziv,mjesto.PostanskiBroj,mjesto.Naziv from autobuska_stanica join "
				+ "(autobusko_stajaliste,mjesto) on "
				+ "(autobuska_stanica.IdStajalista=autobusko_stajaliste.IdStajalista and autobusko_stajaliste.PostanskiBroj=mjesto.PostanskiBroj)";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false);
			r = s.executeQuery();
			while(r.next()) {
				Stajaliste stajaliste = new Stajaliste(r.getString("mjesto.Naziv"), r.getInt("mjesto.PostanskiBroj"), r.getString("autobusko_stajaliste.Naziv"), r.getInt("autobuska_stanica.IdStajalista"));
				stajalistaBezStanicaList.add(stajaliste);
			}
			return stajalistaBezStanicaList;
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			Util.showBugAlert();
		}
		return null;
	}

	@Override
	public String toString() {
		if(nazivStajalista.equals(naziv))
			return nazivStajalista;
		return nazivStajalista + " ("+ naziv+")";

	}
	
	public static int dodajStajaliste(Stajaliste stajaliste) {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "insert into autobusko_stajaliste value (default,?,?)";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, true, stajaliste.getNazivStajalista(), stajaliste.getPostanskiBroj());
			s.executeUpdate();
			r = s.getGeneratedKeys();
			if(r.next())
				return r.getInt(1);
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return 0;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + idStajalista;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stajaliste other = (Stajaliste) obj;
		if (idStajalista != other.idStajalista)
			return false;
		return true;
	}
	
}
