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
	private int peron;
	private Prevoznik prevoznik;
	private String stanje;
	private int voznjaPraznikom;

	/*
	 * konstruktor za ucitavanje relacije*/
	public Linija(int idLinije) {
		this.idLinije = idLinije;
	}
	/*
	 * konstruktor za storniranje karte*/
	public Linija(String nazivLinije) {
		// TODO Auto-generated constructor stub
		this.nazivLinije = nazivLinije;
	}

	
	/*
	 * konstruktor za kartu
	 * 
	 */
	public Linija(int idLinije, String nazivLinije, int peron, Prevoznik prevoznik, int voznjaPraznikom) {
		super();
		this.idLinije = idLinije;
		this.nazivLinije = nazivLinije;
		this.peron = peron;
		this.prevoznik = prevoznik;
		this.voznjaPraznikom = voznjaPraznikom;
		this.stanje = "Aktivno";
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

	public int getPeron() {
		return peron;
	}

	public void setPeron(int peron) {
		this.peron = peron;
	}

	public Prevoznik getPrevoznik() {
		return prevoznik;
	}

	public void setPrevoznik(Prevoznik prevoznik) {
		this.prevoznik = prevoznik;
	}

	public String getStanje() {
		return stanje;
	}

	public void setStanje(String stanje) {
		this.stanje = stanje;
	}

	public int getVoznjaPraznikom() {
		return voznjaPraznikom;
	}

	public void setVoznjaPraznikom(int voznjaPraznikom) {
		this.voznjaPraznikom = voznjaPraznikom;
	}
	public String getNazivPrevoznika() {
		return prevoznik.getNaziv();
	}

	@Override
	public String toString() {
		return "Linija [idLinije=" + idLinije + ", nazivLinije=" + nazivLinije + ", peron=" + peron + ", prevoznik="
				+ prevoznik + ", stanje=" + stanje + ", voznjaPraznikom=" + voznjaPraznikom + "]";
	}
	public static int dodajLiniju(Linija linija) {
		String sql = "insert into linija value (default,?,?,?,default,?)";
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, true, linija.getNazivLinije(),linija.getPeron(),linija.getPrevoznik().getJIBPrevoznika(),linija.getVoznjaPraznikom());
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
	
	
/*	public static int dodajLiniju(Linija linija) {
		String sql = "insert into linija value (default,?,?,?,default)";
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, true, linija.getNazivLinije(),linija.getPeron(),linija.getPrevoznik().getJIBPrevoznika());
			System.out.println(s.executeUpdate());
			r = s.getGeneratedKeys();
			if(r.next()) {
				return r.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	
		
	}*/

	public static boolean izmjeniLiniju(Linija linija) {
		String sql = "update linija set NazivLinije=?, Peron=?, where IdLinije=?";
		Connection c = null;
		PreparedStatement s = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, linija.getNazivLinije(), linija.getPeron(), linija.getIdLinije());
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
		String sqlQuery = "select IdLinije,NazivLinije,Peron,NazivPrevoznika,linija.Stanje,VoznjaPraznikom from linija join prevoznik on (linija.JIBPrevoznika=prevoznik.JIBPrevoznika) where linija.Stanje!='Izbrisano'";
    	Connection c = null;
    	ResultSet r = null;
    	PreparedStatement s = null;
    	List<Linija> linijeList = new ArrayList<>();
    	try {
			c = Util.getConnection();
			s = c.prepareStatement(sqlQuery);
			r = s.executeQuery();
			while(r.next()) {
				Prevoznik prevoznik = new Prevoznik(r.getString("NazivPrevoznika"));
				linijeList.add(new Linija(r.getInt("IdLinije"), r.getString("NazivLinije"), r.getInt("Peron"),prevoznik, r.getInt("VoznjaPraznikom")));	
			}
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

