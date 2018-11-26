package org.unibl.etf.karta;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.salterski_radnik.ProdajaKarataController;
import org.unibl.etf.util.Util;

public class Karta {
	protected int idKarte;
	protected Linija linija;
	protected Relacija relacija;
	protected Time vrijemePolaska;
	protected Time vrijemeDolaska;
	protected double cijena; 
	protected LocalDate datumIzdavanja;
	protected Prevoznik prevoznik;
	protected String imeZaposlenog;
	protected String JIBStanice;
	protected int peron;
	protected String nazivPrevoznika;
	protected String nazivLinije;

	public Karta(int idKarte, double cijena, String polaziste, String odrediste, String nazivLinije, Date datum) {
		this.relacija = new Relacija();
		this.linija = new Linija();
		this.linija.setNazivLinije(nazivLinije);
		this.idKarte = idKarte;
		this.cijena = cijena;
		this.relacija.setPolaziste(polaziste);
		this.relacija.setOdrediste(odrediste);
		this.nazivLinije = nazivLinije;
		this.datumIzdavanja = datum.toLocalDate();
	}
	
	public String getNazivLinije() {
		return nazivLinije;
	}
	public void setNazivLinije(String nazivLinije) {
		this.nazivLinije = nazivLinije;
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
	public String getImeZaposlenog() {
		return imeZaposlenog;
	}
	public void setImeZaposlenog(String imeZaposlenog) {
		this.imeZaposlenog = imeZaposlenog;
	}
	public int getPeron() {
		return peron;
	}
	public void setPeron(int peron) {
		this.peron = peron;
	}
	public String getNazivPrevoznika() {
		return nazivPrevoznika;
	}
	public void setNazivPrevoznika(String nazivPrevoznika) {
		this.nazivPrevoznika = nazivPrevoznika;
	}
	public Karta(Linija linija, Relacija relacija, Time vrijemePolaska, Time vrijemeDolaska, double cijena,
			LocalDate datumIzdavanja, Prevoznik prevoznik, String imeZaposlenog, String JIBStanice) {
		super();
		this.linija = linija;
		this.relacija = relacija;
		this.vrijemePolaska = vrijemePolaska;
		this.vrijemeDolaska = vrijemeDolaska;
		this.cijena = cijena;
		this.datumIzdavanja = datumIzdavanja;
		this.prevoznik = prevoznik;
		this.imeZaposlenog = imeZaposlenog;
		this.nazivPrevoznika = prevoznik.getNaziv();
		this.peron = linija.getPeron();
		this.nazivLinije = linija.getNazivLinije();
		this.JIBStanice = JIBStanice;
	}
	
	
	public String getJIBStanice() {
		return JIBStanice;
	}
	public void setJIBStanice(String jIBStanice) {
		JIBStanice = jIBStanice;
	}
	public int getIdKarte() {
		return idKarte;
	}
	public void setIdKarte(int idKarte) {
		this.idKarte = idKarte;
	}
	public Linija getLinija() {
		return linija;
	}
	public void setLinija(Linija linija) {
		this.linija = linija;
	}
	public Relacija getRelacija() {
		return relacija;
	}
	public void setRelacija(Relacija relacija) {
		this.relacija = relacija;
	}
	public double getCijena() {
		return cijena;
	}
	public void setCijena(double cijena) {
		this.cijena = cijena;
	}
	public LocalDate getDatumIzdavanja() {
		return datumIzdavanja;
	}
	public void setDatumIzdavanja(LocalDate datumIzdavanja) {
		this.datumIzdavanja = datumIzdavanja;
	}
	public Prevoznik getPrevoznik() {
		return prevoznik;
	}
	public void setPrevoznik(Prevoznik prevoznik) {
		this.prevoznik = prevoznik;
	}
	public String getIzdaoZaposleni() {
		return imeZaposlenog;
	}
	public void setIzdaoZaposleni(String izdaoZaposleni) {
		this.imeZaposlenog = izdaoZaposleni;
	}
	
	
	@Override
	public String toString() {
		return "Karta [idKarte=" + idKarte + ", linija=" + linija + ", relacija=" + relacija + ", vrijemePolaska="
				+ vrijemePolaska + ", vrijemeDolaska=" + vrijemeDolaska + ", cijena=" + cijena + ", datumIzdavanja="
				+ datumIzdavanja + ", prevoznik=" + prevoznik + ", imeZaposlenog=" + imeZaposlenog + ", JIBStanice="
				+ JIBStanice + ", peron=" + peron + ", nazivPrevoznika=" + nazivPrevoznika + ", nazivLinije="
				+ nazivLinije + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idKarte;
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
		Karta other = (Karta) obj;
		if (idKarte != other.idKarte)
			return false;
		return true;
	}

	public static List<Karta> getMjesecneKarteList(String polaziste,String odrediste) {
		List<Karta> karteList = new ArrayList<>();
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select DaniUSedmici,VrijemePolaska,NazivPrevoznika,Email,prevoznik.Adresa,WEBAdresa,Telefon,prevoznik.PostanskiBroj,NazivLinije,Peron,Polaziste,Odrediste,VrijemeDolaska,CijenaMjesecna,relacija.IdRelacije,relacija.IdLinije from linija join (relacija,prevoznik) "
				+ "on (linija.IdLinije=relacija.IdLinije) and (linija.JIBPrevoznika=prevoznik.JIBPrevoznika) where (linija.IdLinije=relacija.IdLinije) and (Polaziste=? && Odrediste=?)";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, polaziste, odrediste);
			r = s.executeQuery();
			String daniUSedmici;
	       	while(r.next()) {
	       		daniUSedmici = r.getString(1);
				Time vrijemePolaska = r.getTime(2);
				Prevoznik prevoznik = new Prevoznik(r.getString("NazivPrevoznika"));
				Linija linija = new Linija(r.getInt(16),r.getString(9), daniUSedmici,r.getInt(10),r.getString(3));
				Relacija relacija = new Relacija(r.getInt(15),r.getInt(16),r.getString(11), r.getString(12));
	       		karteList.add(new Karta(linija, relacija, vrijemePolaska, r.getTime(13), r.getDouble(14), LocalDate.now(), prevoznik, PrijavaController.nalog.getKorisnickoIme(),PrijavaController.nalog.getIdStanice()));
	       	}
	       	return karteList;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(r, s, c);
		}
		return null;
	}
	
	public static List<Karta> getKarteList(String polaziste,String odrediste) {
		List<Karta> karteList = new ArrayList<>();
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select DaniUSedmici,VrijemePolaska,NazivPrevoznika,Email,prevoznik.Adresa,WEBAdresa,Telefon,prevoznik.PostanskiBroj,NazivLinije,Peron,Polaziste,Odrediste,VrijemeDolaska,CijenaJednokratna,relacija.IdRelacije,relacija.IdLinije from linija join (relacija,prevoznik) "
				+ "on (linija.IdLinije=relacija.IdLinije) and (linija.JIBPrevoznika=prevoznik.JIBPrevoznika) where (linija.IdLinije=relacija.IdLinije) and (Polaziste=? && Odrediste=?)";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, polaziste, odrediste);
			r = s.executeQuery();
			String daniUSedmici;
	       	while(r.next()) {
	       		daniUSedmici = r.getString(1);
				Time vrijemePolaska = r.getTime(2);
				Prevoznik prevoznik = new Prevoznik(r.getString("NazivPrevoznika"));
				Linija linija = new Linija(r.getInt(16),r.getString(9), daniUSedmici,r.getInt(10),r.getString(3));
				Relacija relacija = new Relacija(r.getInt(15),r.getInt(16),r.getString(11), r.getString(12));
	       		karteList.add(new Karta(linija, relacija, vrijemePolaska, r.getTime(13), r.getDouble(14), LocalDate.now(), prevoznik, PrijavaController.nalog.getKorisnickoIme(),PrijavaController.nalog.getIdStanice()));
	       	}
	       	return karteList;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(r, s, c);
		}
		return null;
	}
	
	public static void kreirajKartu(Karta karta,int brojSjedista, LocalDate datum) {
		String sql = "insert into karta value (DEFAULT,?,?,?,?,?,DEFAULT)";
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, true, karta.getRelacija().getIdRelacije(), Date.valueOf(datum), brojSjedista, karta.getJIBStanice(),karta.getCijena());
			s.executeUpdate();
			r = s.getGeneratedKeys();
			if(r.next()) 
				ProdajaKarataController.idKarte = r.getInt(1);
			} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(r, s, c);
		}
	}
	
	public static void kreirajRezervaciju(String ime, String prezime, String brojTelefona, int idKarte) {
		Connection c= null;
		PreparedStatement s = null;
		String sql = "insert into rezervacija value (default,?,?,?,?,'Aktivno')";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, Date.valueOf(LocalDate.now()), ime + " " +prezime, brojTelefona, idKarte);
			s.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Karta pronadjiKartu(int idKarte) {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select karta.SerijskiBroj,karta.Cijena,relacija.Polaziste,relacija.Odrediste,linija.NazivLinije,karta.Datum from karta "
				+ "join (relacija,linija) on (karta.IdRelacije=relacija.IdRelacije) and (relacija.IdLinije=linija.IdLinije) "
				+ " where karta.SerijskiBroj=?";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, idKarte);
			r = s.executeQuery();
			if(r.next()) {
				return new Karta(r.getInt("karta.SerijskiBroj"), r.getDouble("karta.Cijena"), r.getString("relacija.Polaziste"),
						r.getString("relacija.Odrediste"), r.getString("linija.NazivLinije"), r.getDate("karta.Datum"));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public static int provjeriBrojKarata(Karta k, Date datum) {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select count(*) from karta join (relacija,linija) on (karta.IdRelacije=relacija.IdRelacije) and (relacija.IdLinije=linija.IdLinije) "
				+ "where (linija.IdLinije=?) and (karta.Datum=?) and (karta.Stanje='Aktivno')";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, k.getLinija().getIdLinije(), datum);
			r = s.executeQuery();
			if(r.next())
				return r.getInt(1);
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		}
		finally {
			Util.close(r, s, c);
		}
		return 0;
	}
}
