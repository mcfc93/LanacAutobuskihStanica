package org.unibl.etf.karta;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.salterski_radnik.ProdajaKarataController;
import org.unibl.etf.salterski_radnik.SalterskiRadnikController;
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
	protected Date datumPolaska;
	protected int brojSjedista;
	private boolean povratna;
	private boolean rezervacija;
	
	public boolean isRezervacija() {
		return rezervacija;
	}

	public void setRezervacija(boolean rezervacija) {
		this.rezervacija = rezervacija;
	}

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
	
	public int getBrojSjedista() {
		return brojSjedista;
	}

	public void setBrojSjedista(int brojSjedista) {
		this.brojSjedista = brojSjedista;
	}

	public Date getDatumPolaska() {
		return datumPolaska;
	}

	public void setDatumPolaska(Date datumPolaska) {
		this.datumPolaska = datumPolaska;
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
	
	
	public Karta() {
		// TODO Auto-generated constructor stub
	}

	public String getJIBStanice() {
		return JIBStanice;
	}
	public void setJIBStanice(String jIBStanice) {
		JIBStanice = jIBStanice;
	}
	public String getIdKarte() {
		return String.format("%06d",idKarte);
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
		return  PrijavaController.nalog.getIme();
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
				+ nazivLinije + ", datumPolaska=" + datumPolaska + "]";
	}

	public boolean isPovratna() {
		return povratna;
	}

	public void setPovratna(boolean povratna) {
		this.povratna = povratna;
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

	public void stampajKartu()  {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s%s", PrijavaController.autobuskaStanica.getGrad(), System.lineSeparator()));
		sb.append(String.format("%s%s", PrijavaController.autobuskaStanica.getNaziv(), System.lineSeparator()));
		sb.append(String.format("Informacije: %s%s", PrijavaController.autobuskaStanica.getBrojTelefona(), System.lineSeparator()));
		sb.append(String.format("WEB stranica: %s%s", PrijavaController.autobuskaStanica.getWebStranica(), System.lineSeparator()));
		sb.append(String.format("======================================%s%s", System.lineSeparator(),System.lineSeparator()));
		sb.append(String.format("Prevoznik: %s%s", nazivPrevoznika, System.lineSeparator()));
		sb.append(String.format("%s : %s%s", "Naziv linije:", nazivLinije, System.lineSeparator()));
		sb.append(System.lineSeparator());
		sb.append(String.format("%10s %s %10s%s", " ", "AUTOBUSKA KARTA", " ", System.lineSeparator()));
		sb.append(String.format("%10s (%s) %10s%s", " ", povratna? "povratna":"jednosmjerna" , " ", System.lineSeparator()));
		sb.append(System.lineSeparator());

		sb.append(String.format("Prodajno mjesto: %s%s", PrijavaController.autobuskaStanica.getGrad(), System.lineSeparator()));
		sb.append(String.format("Serijski broj: %06d%s", idKarte, System.lineSeparator()));
		sb.append(String.format("Peron: %d%s", peron, System.lineSeparator()));
		sb.append(String.format("Sjediste: %d%s", brojSjedista, System.lineSeparator()));
		sb.append(String.format("Relacija: %s - %s%s" , relacija.getPolaziste(), relacija.getOdrediste(), System.lineSeparator()));
		sb.append("Izdata: " + LocalDate.now() + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + System.lineSeparator());
		sb.append("Polazak: " + datumPolaska + " " + vrijemePolaska + System.lineSeparator());
		sb.append("Dolazak: " + vrijemeDolaska + System.lineSeparator());
		sb.append(String.format("%20s %.2f KM%s", "Cijena:", cijena, System.lineSeparator()));
		sb.append(String.format("%20s %.2f KM%s", "Rezervacija:", rezervacija? ProdajaKarataController.REZERVACIJA: 0, System.lineSeparator()));
		sb.append(String.format("%20s %.2f KM%s", "Stanicna usluga:", ProdajaKarataController.STANICNA_USLUGA,System.lineSeparator()));
		sb.append(String.format("%20s %.2f KM%s%s", "Ukupna cijena:", cijena+ProdajaKarataController.STANICNA_USLUGA+ (rezervacija? ProdajaKarataController.REZERVACIJA: 0),System.lineSeparator(),System.lineSeparator()));

		sb.append(String.format("Na zahtjev kontrolora pokazati kartu!%s", System.lineSeparator()));
		sb.append(String.format("Biletar: %s%s%s", imeZaposlenog, System.lineSeparator(),System.lineSeparator()));
		sb.append(String.format("%10sHvala na povjerenju!", " "));
		
		File file = new File("src\\jednokratnekarte\\karta" + String.format("%06d", idKarte)+".txt");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		    writer.append(sb);
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static List<Karta> getMjesecneKarteList(String polaziste,String odrediste) {
		List<Karta> karteList = new ArrayList<>();
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select DaniUSedmici,VrijemePolaska,NazivPrevoznika,Email,prevoznik.Adresa,WEBAdresa,Telefon,prevoznik.PostanskiBroj,NazivLinije,Peron,Polaziste,Odrediste,VrijemeDolaska,CijenaMjesecna,relacija.IdRelacije,relacija.IdLinije from linija join (relacija,prevoznik) "
				+ "on (linija.IdLinije=relacija.IdLinije) and (linija.JIBPrevoznika=prevoznik.JIBPrevoznika) where (linija.IdLinije=relacija.IdLinije) and ( (Polaziste=? && Odrediste=?) or (Odrediste=? && Polaziste=?) )";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, polaziste, odrediste, polaziste, odrediste);
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
			if(r.next()) {
				ProdajaKarataController.idKarte = r.getInt(1);
				System.out.println("ID karte: " + ProdajaKarataController.idKarte);
			}
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
	
	public static boolean stornirajKartu(int serijskiBroj) {
		Connection c = null;
		PreparedStatement s1 = null;
		PreparedStatement s2 = null;
		PreparedStatement s3 = null;
		String sqlKarta = "update karta set Stanje='Stornirano' where SerijskiBroj=?";
		String sqlRezervacija = "update rezervacija set Stanje='Stornirano' where SerijskiBroj=?";
		String sqlMjesecna = "update mjesecna_karta set Stanje='Stornirano' where SerijskiBroj=?";
		try {
			c = Util.getConnection();
			s1 = Util.prepareStatement(c, sqlKarta, false, serijskiBroj);
			s2 = Util.prepareStatement(c, sqlRezervacija, false, serijskiBroj);
			s3 = Util.prepareStatement(c, sqlMjesecna, false, serijskiBroj);
			s2.executeUpdate();
			s3.executeUpdate();
			if(s1.executeUpdate()==1) {
				s2.close();
				s3.close();
				File file = new File("src\\jednokratnekarte\\karta" + String.format("%06d", serijskiBroj)+".txt");
				File file2 = new File("src\\mjesecnekarte\\karta" + String.format("%06d", serijskiBroj)+".txt");
				if(file2.exists())
					file2.delete();
				if(file.exists())
					file.delete();
				return true;
			}
			s2.close();
			s3.close();
			return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(s1, c);
		}
		return false;
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
