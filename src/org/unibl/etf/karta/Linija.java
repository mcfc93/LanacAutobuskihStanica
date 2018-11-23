package org.unibl.etf.karta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.unibl.etf.util.Util;

public class Linija {
	private int idLinije;
	private String nazivLinije;
	private int idStanice;
	private String daniUSedmici;
	private int peron;
	private String nazivPrevoznika;
	private String stanje;
	
	public Linija(int idLinije, String nazivLinije) {
		super();
		this.idLinije = idLinije;
		this.nazivLinije = nazivLinije;
	}
	
	public Linija(int idLinije,String nazivLinije,String daniUSedmici,int peron,String nazivPrevoznika,String stanje) {
		super();
		this.idLinije = idLinije;
		this.nazivLinije = nazivLinije;
		this.daniUSedmici = daniUSedmici;
		this.peron = peron;
		this.nazivPrevoznika = nazivPrevoznika;
		this.stanje = stanje;
	}

	public String getStanje() {
		return stanje;
	}

	public void setStanje(String stanje) {
		this.stanje = stanje;
	}

	public Linija(int idLinije,String nazivLinije,String daniUSedmici,int peron,String nazivPrevoznika) {
		super();
		this.idLinije = idLinije;
		this.nazivLinije = nazivLinije;
		this.daniUSedmici = daniUSedmici;
		this.peron = peron;
		this.nazivPrevoznika = nazivPrevoznika;
	}
	public Linija() {
	}

	public String getNazivPrevoznika() {
		return nazivPrevoznika;
	}


	public void setNazivPrevoznika(String nazivPrevoznika) {
		this.nazivPrevoznika = nazivPrevoznika;
	}


	public int getIdLinije() {
		return idLinije;
	}
	public void setIdLinije(int idLinije) {
		this.idLinije = idLinije;
	}
	public String getNazivLinije() {
		return nazivLinije;
	}
	public void setNazivLinije(String nazivLinije) {
		this.nazivLinije = nazivLinije;
	}
	public int getIdStanice() {
		return idStanice;
	}
	public void setIdStanice(int idStanice) {
		this.idStanice = idStanice;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idLinije;
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
		Linija other = (Linija) obj;
		if (idLinije != other.idLinije)
			return false;
		return true;
	}


	public int getPeron() {
		return peron;
	}


	public void setPeron(int peron) {
		this.peron = peron;
	}


	public String getDaniUSedmici() {
		return daniUSedmici;
	}


	public void setDaniUSedmici(String daniUSedmici) {
		this.daniUSedmici = daniUSedmici;
	}



	@Override
	public String toString() {
		return "Linija [idLinije=" + idLinije + ", nazivLinije=" + nazivLinije + ", idStanice=" + idStanice
				+ ", daniUSedmici=" + daniUSedmici + ", peron=" + peron + ", nazivPrevoznika=" + nazivPrevoznika
				+ ", stanje=" + stanje + "]";
	}

	public static int dodajLiniju(String naziv, int peron, String jibPrevoznika, String daniString) {
		String sql = "insert into linija value (default,?,?,?,?,default)";
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, true, naziv,peron,jibPrevoznika,daniString);
			if(s.executeUpdate()==1) { 
				r = s.getGeneratedKeys();
				if(r.next()) 
					return(r.getInt(1));
				else
					return 0;
			}
		} catch (SQLException e) {
		e.printStackTrace();
		}
		finally {
			Util.close(r,s, c);
		}
		return 0;
	}

	public static boolean izmjeniLiniju(Linija odabranaLinija, String naziv, int peron, String daniString,
			String stanje) {
		String sql = "update linija set NazivLinije=?, Peron=?, DaniUSedmici=?, Stanje=? where IdLinije=?";
		Connection c = null;
		PreparedStatement s = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, naziv, peron, daniString, stanje, odabranaLinija.getIdLinije());
			if(s.executeUpdate()==1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(s, c);
		}
		
		
		return false;
	}


	public static boolean deaktivirajLiniju(int idLinije,String stanje) {
		String sql = "update linija set Stanje=? where IdLinije=?";
		Connection c = null;
		PreparedStatement s = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql,false, stanje, idLinije);
			System.out.println(s.execute());
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean izbrisiLiniju(Linija linija) {
		String sql = "update linija set Stanje='Izbrisano' where IdLinije=?";
		Connection c = null;
		PreparedStatement s = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, linija.getIdLinije());
			System.out.println(s.execute());
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static List<Linija> getLinije() {
		// TODO Auto-generated method stub
		String sqlQuery = "select IdLinije,NazivLinije,Peron,NazivPrevoznika,DaniUSedmici,linija.Stanje from linija join prevoznik on (linija.JIBPrevoznika=prevoznik.JIBPrevoznika) where linija.Stanje!='Izbrisano'";
    	Connection c = null;
    	ResultSet r = null;
    	PreparedStatement s = null;
    	List<Linija> linijeList = new ArrayList<>();
    	try {
			c = Util.getConnection();
			s = c.prepareStatement(sqlQuery);
			r = s.executeQuery();
			while(r.next()) 
				linijeList.add( new Linija(r.getInt("IdLinije"), r.getString("NazivLinije"), r.getString("DaniUSedmici"), r.getInt("Peron"),r.getString("NazivPrevoznika"),r.getString("Stanje")));
			return linijeList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	finally {
			Util.close(r, s, c);
		}
		
		return null;
	}
	
	
	
	
}

