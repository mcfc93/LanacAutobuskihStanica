package org.unibl.etf.karta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.unibl.etf.util.Stajaliste;
import org.unibl.etf.util.Util;

public class Relacija {
	
	private int idRelacije;
	private Linija linija;
	private Stajaliste polaziste;
	private Stajaliste odrediste;
	private Time vrijemePolaska;
	private Time vrijemeDolaska;
	private double cijenaJednokratna;
	private double cijenaMjesecna;
	private String dani;
	
	/*
	 * 
	 * konstruktor za ucitavanje relacija
	 */
	//relacijeList.add(new Relacija(r.getInt("IdRelacije"), r.getInt("IdLinije"), polaziste, odrediste, r.getDouble("CijenaJednokratna"), r.getDouble("CijenaMjesecna")));
	
	public Relacija(int idRelacije, int idLinije, Stajaliste polaziste, Stajaliste odrediste, double cijenaJednokratna, double cijenaMjesecna) {
		this.idRelacije = idRelacije;
		this.linija = new Linija(idLinije);
		this.polaziste = polaziste;
		this.odrediste = odrediste;
		this.cijenaJednokratna = cijenaJednokratna;
		this.cijenaMjesecna = cijenaMjesecna;
				
	}
	
	
	/*
	 * konstruktor za kartu
	 */
	public Relacija(int idRelacije, Linija linija, Stajaliste polaziste, Stajaliste odrediste, Time vrijemePolaska,
			Time vrijemeDolaska, double cijenaJednokratna, String dani) {
		super();
		this.idRelacije = idRelacije;
		this.linija = linija;
		this.polaziste = polaziste;
		this.odrediste = odrediste;
		this.vrijemePolaska = vrijemePolaska;
		this.vrijemeDolaska = vrijemeDolaska;
		this.cijenaJednokratna = cijenaJednokratna;
		this.dani = dani;
	}
	
	/*
	 * konstruktor za ucitavanje mjesecnih karata
	 */
	public Relacija(int idRelacije, Linija linija, Stajaliste polaziste, Stajaliste odrediste, double cijenaMjesecna) {
		super();
		this.idRelacije = idRelacije;
		this.linija = linija;
		this.polaziste = polaziste;
		this.odrediste = odrediste;
		this.cijenaMjesecna = cijenaMjesecna;
	}
	
	
	/*
	 * konstruktor za pronalazak karte
	 */
	public Relacija(String nazivPolazista, String nazivOdredista) {
		this.polaziste = new Stajaliste(nazivPolazista);
		this.odrediste = new Stajaliste(nazivOdredista);
	}
	
	public Relacija() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * konstruktor za produzavanje karte*/
	public Relacija(Linija linija, Prevoznik prevoznik, Stajaliste polaziste, Stajaliste odrediste) {
		// TODO Auto-generated constructor stub
		this.linija = linija;
		this.linija.setPrevoznik(prevoznik);
		this.polaziste = polaziste;
		this.odrediste = odrediste;
	}


	public Relacija(Stajaliste polaziste, Stajaliste odrediste, double cijena) {
		// TODO Auto-generated constructor stub
		this.polaziste = polaziste;
		this.odrediste = odrediste;
		this.cijenaJednokratna = cijena;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idRelacije;
		return result;
	}

	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Relacija other = (Relacija) obj;
		if (idRelacije != other.idRelacije)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return polaziste.getNazivStajalista() + " - " + odrediste.getNazivStajalista();
	}

	public Linija getLinija() {
		return linija;
	}
	public void setLinija(Linija linija) {
		this.linija = linija;
	}
	public int getIdRelacije() {
		return idRelacije;
	}
	public void setIdRelacije(int idRelacije) {
		this.idRelacije = idRelacije;
	}
	public Stajaliste getPolaziste() {
		return polaziste;
	}
	public void setPolaziste(Stajaliste polaziste) {
		this.polaziste = polaziste;
	}
	public Stajaliste getOdrediste() {
		return odrediste;
	}
	public void setOdrediste(Stajaliste odrediste) {
		this.odrediste = odrediste;
	}
	public Time getVrijemePolaska() {
		return vrijemePolaska;
	}
	public void setVrijemePolaska(Time vrijemePolaska) {
		this.vrijemePolaska = vrijemePolaska;
	}
	public Time getVrijemeDolaska() {
		return vrijemeDolaska;
	}
	public void setVrijemeDolaska(Time vrijemeDolaska) {
		this.vrijemeDolaska = vrijemeDolaska;
	}
	public double getCijenaJednokratna() {
		return cijenaJednokratna;
	}
	public void setCijenaJednokratna(double cijenaJednokratna) {
		this.cijenaJednokratna = cijenaJednokratna;
	}
	public double getCijenaMjesecna() {
		return cijenaMjesecna;
	}
	public void setCijenaMjesecna(double cijenaMjesecna) {
		this.cijenaMjesecna = cijenaMjesecna;
	}
	public String getDani() {
		return dani;
	}
	public void setDani(String dani) {
		this.dani= dani;
	}
	

	public static int dodajRelaciju(Relacija relacija) {
		Connection c = null;
		ResultSet r =null;
		PreparedStatement s = null;
		String sql = "insert into relacija value (default,?,?,?,?,?,?,?)";
		String sqlCijenaMjesecna = "insert into cijena_mjesecne_karte value (default,?,?)";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, true, relacija.getLinija().getIdLinije(), relacija.getPolaziste().getIdStajalista(), relacija.getOdrediste().getIdStajalista(), relacija.getVrijemePolaska(),relacija.getVrijemeDolaska(),relacija.getCijenaJednokratna(), relacija.getDani());
			System.out.println(s.executeUpdate());
			r = s.getGeneratedKeys();
			if(r.next()) {
				if(relacija.getCijenaMjesecna()==0)
					return r.getInt(1);
				s = Util.prepareStatement(c, sqlCijenaMjesecna, false, relacija.getIdRelacije(), relacija.getCijenaMjesecna());
				s.executeUpdate();
				return r.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public static boolean izmijeniRelaciju(Relacija relacija) {
		Connection c = null;
		ResultSet r =null;
		PreparedStatement s = null;
		String sql = "update relacija set CijenaJednokratna=?, VrijemePolaska=?, VrijemeDolaska=? where IdRelacije=?";
		String sqlPolazak = "update relacija set relacija.VrijemePolaska=? where relacija.IdLinije=? and relacija.Polaziste=?";
		String sqlDolazak = "update relacija set relacija.VrijemeDolaska=? where relacija.IdLinije=? and relacija.Odrediste=?";
		String sqlCijenaMjesecna = "update cijena_mjesecne_karte set CijenaMjesecna=12 where IdRelacije=?";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, relacija.getCijenaJednokratna(), relacija.getVrijemePolaska(),
					relacija.getVrijemeDolaska(), relacija.getIdRelacije());
			s.executeUpdate();
			s = Util.prepareStatement(c, sqlPolazak, false, relacija.getVrijemePolaska(), relacija.getLinija().getIdLinije(), relacija.getPolaziste().getIdStajalista());
			s.executeUpdate();
			s = Util.prepareStatement(c, sqlDolazak, false, relacija.getVrijemeDolaska(), relacija.getLinija().getIdLinije(), relacija.getPolaziste().getIdStajalista());
			s.executeUpdate();
			s = Util.prepareStatement(c, sqlCijenaMjesecna, false, relacija.getLinija().getIdLinije());
			s.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			Util.close(r, s, c);
		}
		return false;
	}
	
	public static List<Relacija> getRelacije(int idLinije) {
		List<Relacija> relacijeList = new ArrayList<>();
		Connection c = null;
		ResultSet r =null;
		PreparedStatement s = null;
		String sql = "select * from relacija join cijena_mjesecne_karte where relacija.IdLinije=161 and relacija.IdRelacije=cijena_mjesecne_karte.IdRelacije";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, idLinije);
			r = s.executeQuery();
			while(r.next()) {
				Stajaliste polaziste = new Stajaliste(r.getInt("Polaziste"));
				Stajaliste odrediste = new Stajaliste(r.getInt("Odrediste"));
				relacijeList.add(new Relacija(r.getInt("IdRelacije"), r.getInt("IdLinije"), polaziste, odrediste, r.getDouble("CijenaJednokratna"), r.getDouble("CijenaMjesecna")));
			}
			return relacijeList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	
	
}
