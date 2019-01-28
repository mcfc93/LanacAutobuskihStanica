package org.unibl.etf.karta;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.unibl.etf.util.Mjesto;
import org.unibl.etf.util.Util;

public class Prevoznik {
	private String naziv;
	private String email;
	private String adresa;
	private String telefon;
	private Mjesto mjesto;
	private String webAdresa;
	private String JIBPrevoznika;
	private String racun;
	private double djackiPopust;
	private double penzionerskiPopust;
	private double radnickiPopust;
	
	

	/*
	 * konstruktor za ucitavanje linije
	 * 
	 */
	
	public double getPenzionerskiPopust() {
		return penzionerskiPopust;
	}


	public void setPenzionerskiPopust(double penzionerskiPopust) {
		this.penzionerskiPopust = penzionerskiPopust;
	}


	public double getRadnickiPopust() {
		return radnickiPopust;
	}


	public void setRadnickiPopust(double radnickiPopust) {
		this.radnickiPopust = radnickiPopust;
	}


	public double getDjackiPopust() {
		return djackiPopust;
	}


	public void setDjackiPopust(double djackiPopust) {
		this.djackiPopust = djackiPopust;
	}


	public Prevoznik(String naziv) {
		super();
		this.naziv = naziv;
	}
	
	
	/*
	 * konstruktor za kreiranje karte
	 * 
	 */
	
	public Prevoznik(String naziv, String email, String telefon, String jIBPrevoznika, double djackiPopust) {
		super();
		this.naziv = naziv;
		this.email = email;
		this.telefon = telefon;
		this.JIBPrevoznika = jIBPrevoznika;
		this.djackiPopust = djackiPopust;
	}
	
	/*
	 * konstruktor za mjesecnu kartu*/
	
	public Prevoznik(String naziv, String email, String telefon, String jIBPrevoznika, double djackiPopust, double radnickiPopust, double penzionerskiPopust) {
		super();
		this.naziv = naziv;
		this.email = email;
		this.telefon = telefon;
		this.JIBPrevoznika = jIBPrevoznika;
		this.djackiPopust = djackiPopust;
		this.radnickiPopust = radnickiPopust;
		this.penzionerskiPopust = penzionerskiPopust;
	}
	
	/*
	 * konstruktor za ucitavanje liste prevoznika
	 * 	r.getString("JIBPrevoznika"), r.getString("NazivPrevoznika"), r.getString("Telefon"), 
	 * r.getString("Email"), r.getString("WEBAdresa"), r.getString("TekuciRacun"), r.getInt("Adresa"), mjesto));

	 */

	public Prevoznik(String JIBPrevoznika, String naziv, String telefon, String email ,String webAdresa,
			String racun, String adresa, Mjesto mjesto) {
		super();
		this.naziv = naziv;
		this.email = email;
		this.adresa = adresa;
		this.telefon = telefon;
		this.mjesto = mjesto;
		this.webAdresa = webAdresa;
		this.JIBPrevoznika = JIBPrevoznika;
		this.racun = racun;
	}	

	public static List<Prevoznik> getPrevozniciList() {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		List<Prevoznik> prevoznikList = new ArrayList<>();
		String sql =  "select * from prevoznik join (mjesto,popust_prevoznika) on (prevoznik.PostanskiBroj=mjesto.PostanskiBroj and prevoznik.JIBPrevoznika=popust_prevoznika.JIBPrevoznika) where Stanje='Aktivno'";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false);
			r = s.executeQuery();
			while(r.next()) {
			//	prevoznikList.add(new Prevoznik(r.getString("NazivPrevoznika"), r.getString("Email"), r.getString("Adresa"), r.getString("Telefon"), r.getInt("prevoznik.PostanskiBroj"), r.getString("WebAdresa"), r.getString("JIBPrevoznika"), r.getString("TekuciRacun")));
				Mjesto mjesto = new Mjesto(r.getString("mjesto.Naziv"), r.getInt("prevoznik.PostanskiBroj"));
				Prevoznik prevoznik = new Prevoznik(r.getString("JIBPrevoznika"), r.getString("NazivPrevoznika"), r.getString("Telefon"), r.getString("Email"), r.getString("WEBAdresa"), r.getString("TekuciRacun"), r.getString("Adresa"), mjesto);
				prevoznik.setDjackiPopust(r.getDouble("DjackiPopust"));
				prevoznik.setPenzionerskiPopust(r.getDouble("PenzionerskiPopust"));
				prevoznik.setRadnickiPopust(r.getDouble("RadnickiPopust"));
				prevoznikList.add(prevoznik);
			}
			return prevoznikList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(r, s, c);
		}
		return null;
	}



	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public Mjesto getMjesto() {
		return mjesto;
	}

	public void setMjesto(Mjesto mjesto) {
		this.mjesto = mjesto;
	}

	public String getWebAdresa() {
		return webAdresa;
	}

	public void setWebAdresa(String webAdresa) {
		this.webAdresa = webAdresa;
	}

	public String getJIBPrevoznika() {
		return JIBPrevoznika;
	}

	public void setJIBPrevoznika(String jIBPrevoznika) {
		JIBPrevoznika = jIBPrevoznika;
	}

	public String getRacun() {
		return racun;
	}

	public void setRacun(String racun) {
		this.racun = racun;
	}

	public static boolean izbrisiPrevoznika(Prevoznik prevoznik) {
		Connection c = null;
		PreparedStatement s = null;
		String sql = "update prevoznik set Stanje='Izbrisano' where JIBPrevoznika=?";
		String sqlLinija = "update linija set Stanje='Izbrisano' where JIBPrevoznika=?";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, prevoznik.getJIBPrevoznika());
			s.executeUpdate();
			s.close();
			s = Util.prepareStatement(c, sqlLinija, false, prevoznik.getJIBPrevoznika());
			s.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {
			Util.close(s, c);
		}
		return false;
		
	/*	Connection c = null;
		CallableStatement s = null;
	    try {
	       	c=Util.getConnection();
	    	s = c.prepareCall("{call removePrevoznik(?)}");
	    	s.setString(1, prevoznik.getJIBPrevoznika());
	       	s.execute();
	       	return true;
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(s,c);
	    }
	    return false;*/
	}
	
	public static boolean izmjeniPrevoznika(String naziv,String telefon,String email,String webAdresa,String tekuciRacun,String adresa,String postanskiBroj,String jib, int djackiPopust, int penzionerskiPopust, int radnickiPopust) {
		Connection c = null;
		String sql = "update prevoznik "
				+ "set NazivPrevoznika=?,Telefon=?,Email=?,WebAdresa=?,TekuciRacun=?,Adresa=?,PostanskiBroj=? "
				+ "where JIBPrevoznika=?";
		String sqlPopust = "update popust_prevoznika set DjackiPopust=?,RadnickiPopust=?,PenzionerskiPopust=? where JIBPrevoznika=?";
		PreparedStatement s = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, naziv,telefon,email,
					webAdresa,tekuciRacun,adresa,Integer.parseInt(postanskiBroj),jib);
			if(s.executeUpdate()==1) {
				s.close();
				s = Util.prepareStatement(c, sqlPopust, false, (double)(100-djackiPopust)/100, (double)(100-radnickiPopust)/100, (double)(100-penzionerskiPopust)/100, jib);
				System.out.println(s.executeUpdate());
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(s, c);
		}
		return false;
	}

	public static boolean dodajPrevoznika(String jib, String naziv, String telefon, String email, String webAdresa,
			String tekuciRacun, String adresa, int postanskiBroj, int djackiPopust, int radnickiPopust, int penzionerskiPopust) {
		
		String sql = "insert into prevoznik value (?,?,?,?,?,?,?,?,default)";
		String sqlPopust = "insert into popust_prevoznika value (default,?,?,?,?)";
		Connection c = null;
		PreparedStatement s = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, jib, naziv, telefon, email, webAdresa, tekuciRacun, adresa, postanskiBroj);
			s.execute();
			s.close();
			s = Util.prepareStatement(c, sqlPopust, false, (double)(100-djackiPopust)/100, (double)(100-penzionerskiPopust)/100, (double)(100-radnickiPopust)/100, jib);
			System.out.println(s.executeUpdate());
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}


	@Override
	public String toString() {
		return naziv;
	}
	
	
}
