package org.unibl.etf.karta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.unibl.etf.util.Util;

public class Prevoznik {
	private String naziv;
	private String email;
	private String adresa;
	private String telefon;
	private int postanskiBroj;
	private String webAdresa;
	private String JIBPrevoznika;
	private String racun;
	
	public String getRacun() {
		return racun;
	}

	public void setRacun(String racun) {
		this.racun = racun;
	}

	public Prevoznik(String naziv, String email, String adresa, String telefon, int postanskiBroj, String webAdresa,
			String jIBPrevoznika, String racun) {
		super();
		this.naziv = naziv;
		this.email = email;
		this.adresa = adresa;
		this.telefon = telefon;
		this.postanskiBroj = postanskiBroj;
		this.webAdresa = webAdresa;
		JIBPrevoznika = jIBPrevoznika;
		this.racun = racun;
	}

	public Prevoznik(String naziv, String email, String adresa, String webAdresa,String telefon, int postanskiBroj) {
		super();
		this.naziv = naziv;
		this.email = email;
		this.webAdresa = webAdresa;
		this.adresa = adresa;
		this.telefon = telefon;
		this.postanskiBroj = postanskiBroj;
	}
	
	public Prevoznik(String naziv, String jib) {
		this.naziv = naziv;
		this.JIBPrevoznika = jib;
	}
	

	public Prevoznik(String nazivPrevoznika) {
		this.naziv = nazivPrevoznika;
	}

	public Prevoznik() {
		// TODO Auto-generated constructor stub
	}

	public String getJIBPrevoznika() {
		return JIBPrevoznika;
	}

	public void setJIBPrevoznika(String jIBPrevoznika) {
		JIBPrevoznika = jIBPrevoznika;
	}

	public String getWebAdresa() {
		return webAdresa;
	}

	public void setWebAdresa(String webAdresa) {
		this.webAdresa = webAdresa;
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
	
	public int getPostanskiBroj() {
		return postanskiBroj;
	}

	public void setPostanskiBroj(int postanskiBroj) {
		this.postanskiBroj = postanskiBroj;
	}
	

	@Override
	public String toString() {
		return naziv;
	}

	public static List<Prevoznik> getPrevozniciList() {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		List<Prevoznik> prevoznikList = new ArrayList<>();
		String sql =  "select JIBPrevoznika,NazivPrevoznika,Telefon,Email,WebAdresa,TekuciRacun,Adresa,prevoznik.PostanskiBroj,Naziv from prevoznik join mjesto on (prevoznik.PostanskiBroj=mjesto.PostanskiBroj) where Stanje='Aktivno'";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false);
			r = s.executeQuery();
			while(r.next()) {
				prevoznikList.add(new Prevoznik(r.getString("NazivPrevoznika"), r.getString("Email"), r.getString("Adresa"), r.getString("Telefon"), r.getInt("prevoznik.PostanskiBroj"), r.getString("WebAdresa"), r.getString("JIBPrevoznika"), r.getString("TekuciRacun")));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(r, s, c);
		}
		return prevoznikList;
	}
	
	public static boolean izbrisiPrevoznika(Prevoznik prevoznik) {
		Connection c = null;
		PreparedStatement s = null;
		String sql = "update prevoznik set Stanje='Izbrisano' where JIBPrevoznika=?";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, prevoznik.getJIBPrevoznika());
			if(s.executeUpdate()==1)
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {
			Util.close(s, c);
		}
		return false;
	}
	
	public static boolean izmjeniPrevoznika(String naziv,String telefon,String email,String webAdresa,String tekuciRacun,String adresa,String postanskiBroj,String jib) {
		Connection c = null;
		String sql = "update prevoznik "
				+ "set NazivPrevoznika=?,Telefon=?,Email=?,WebAdresa=?,TekuciRacun=?,Adresa=?,PostanskiBroj=? "
				+ "where JIBPrevoznika=?";
		PreparedStatement s = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, naziv,telefon,email,
					webAdresa,tekuciRacun,adresa,Integer.parseInt(postanskiBroj),jib);
			if(s.executeUpdate()==1)
				return true;
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
			String tekuciRacun, String adresa, int postanskiBroj) {
		
		String sql = "insert into prevoznik value (?,?,?,?,?,?,?,?,default)";
		Connection c = null;
		PreparedStatement s = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, jib, naziv, telefon, email, webAdresa, tekuciRacun, adresa, postanskiBroj);
			s.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return false;
	}
	
}
