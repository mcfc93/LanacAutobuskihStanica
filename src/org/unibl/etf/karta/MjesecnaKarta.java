package org.unibl.etf.karta;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.salterski_radnik.ProdajaKarataController;
import org.unibl.etf.util.Util;

public class MjesecnaKarta extends Karta {
	private String ime;
	private String prezime;
	private File slika;
	private TipKarte tip;
	private int serijskiBroj;
	public MjesecnaKarta(int serijskiBroj, Linija linija, Relacija relacija, Time vrijemePolaska, Time vrijemeDolaska,
			double cijena, LocalDate datumIzdavanja, Prevoznik prevoznik, String imeZaposlenog,String ime,String prezime,TipKarte tip,File slika,String JIBStanice) {
		super(linija, relacija, vrijemePolaska, vrijemeDolaska, cijena, datumIzdavanja, prevoznik, imeZaposlenog,JIBStanice);
		this.ime = ime;
		this.prezime = prezime;
		this.tip = tip;
		this.slika = slika;
		this.serijskiBroj = serijskiBroj;
	}
	
	public MjesecnaKarta() {
		// TODO Auto-generated constructor stub
	}

	public MjesecnaKarta(Linija linija, Relacija relacija, String nazivPrevoznika, TipKarte value) {
		this.linija = linija;
		this.relacija = relacija;
		this.nazivPrevoznika = nazivPrevoznika;
		this.tip = value;
	}

	public MjesecnaKarta(String ime, String prezime, String nazivLinije, String nazivPrevoznika, String tip, int serijskiBroj, String slikaPath) {
		super();
		this.ime = ime;
		this.prezime = prezime;
		this.linija.setNazivLinije(nazivLinije);
		this.prevoznik.setNaziv(nazivPrevoznika);
		this.tip = TipKarte.valueOf(tip);
		this.serijskiBroj = serijskiBroj;
		this.slika = new File(slikaPath);
	}
	public MjesecnaKarta(Linija linija, Relacija relacija, String ime, String prezime, File odabranaSlika,
			String nazivPrevoznika, TipKarte value) {
		this.linija = linija;
		this.relacija = relacija;
		this.nazivPrevoznika = nazivPrevoznika;
		this.tip = value;
		this.ime = ime;
		this.prezime = prezime;
		this.slika = odabranaSlika;
	}

	public int getSerijskiBroj() {
		return serijskiBroj;
	}

	public void setSerijskiBroj(int serijskiBroj) {
		this.serijskiBroj = serijskiBroj;
	}

	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	public File getSlika() {
		return slika;
	}
	public void setSlika(File slika) {
		this.slika = slika;
	}
	public TipKarte getTip() {
		return tip;
	}
	public void setTip(TipKarte tip) {
		this.tip = tip;
	}

	public void stampajKartu() {
		
	}
	@Override
	public String toString() {
		return "MjesecnaKarta [ime=" + ime + ", prezime=" + prezime + ", slika=" + slika + ", tip=" + tip + ", idKarte="
				+ idKarte + ", linija=" + linija + ", relacija=" + relacija + ", vrijemePolaska=" + vrijemePolaska
				+ ", vrijemeDolaska=" + vrijemeDolaska + ", cijena=" + cijena + ", datumIzdavanja=" + datumIzdavanja
				+ ", prevoznik=" + prevoznik + ", imeZaposlenog=" + imeZaposlenog + ", peron=" + peron
				+ ", nazivPrevoznika=" + nazivPrevoznika + ", nazivLinije=" + nazivLinije + "]";
	}

	
	public static int kreirajKartu(Karta karta, int brojSjedista, LocalDate datum, String ime, String prezime, TipKarte tip,	String slikaPath) {
	//	Karta.kreirajKartu(karta, brojSjedista, datum);
		Connection c = null;
		karta.setIdKarte(ProdajaKarataController.idKarte);
		PreparedStatement s = null;
		String sql = "insert into mjesecna_karta value (default,?,?,?,?,?,?,default)";
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, true, karta.getCijena(), ime, prezime, slikaPath, tip.toString(), ProdajaKarataController.idKarte);
			s.executeUpdate();
			r = s.getGeneratedKeys();
			if(r.next()) {
				ProdajaKarataController.idMjesecneKarte = r.getInt(1);
				System.out.println("ID mjesecne: " + r.getInt(1));
				return r.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Util.close(r, s, c);
		}
		return 0;
	}

	public static void stampajKartu(Karta karta, int i, LocalDate value, String naziv, TipKarte tip) {
		// TODO Auto-generated method stub
		LocalDate localDate = LocalDate.now();
		int mjesecVazenja = (localDate.getDayOfMonth()>25) ? localDate.getMonthValue()+1: localDate.getMonthValue();
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s%s", PrijavaController.autobuskaStanica.getGrad(), System.lineSeparator()));
		sb.append(String.format("%s%s", PrijavaController.autobuskaStanica.getNaziv(), System.lineSeparator()));
		sb.append(String.format("Informacije: %s%s", PrijavaController.autobuskaStanica.getBrojTelefona(), System.lineSeparator()));
		sb.append(String.format("WEB stranica: %s%s", PrijavaController.autobuskaStanica.getWebStranica(), System.lineSeparator()));
		sb.append(String.format("======================================%s", System.lineSeparator()));
		sb.append(System.lineSeparator());
		sb.append(String.format("Prevoznik: %s%s", karta.getNazivPrevoznika(), System.lineSeparator()));
		sb.append(String.format("Naziv linije: %s%s", karta.getNazivLinije(), System.lineSeparator()));
		sb.append(System.lineSeparator());
		sb.append(String.format("%10s %s %10s%s", " ", "AUTOBUSKA KARTA", " ", System.lineSeparator()));
		sb.append(String.format("%10s (%s) %10s%s", " ", "mjesecna" , " ", System.lineSeparator()));
		sb.append(System.lineSeparator());
		sb.append(String.format("Prodajno mjesto: %s%s", PrijavaController.autobuskaStanica.getGrad(), System.lineSeparator()));
		sb.append(String.format("Serijski broj: %013d%s", ProdajaKarataController.idMjesecneKarte, System.lineSeparator()));
		sb.append(String.format("Relacija: %s - %s%s", karta.getRelacija().getPolaziste(), karta.getRelacija().getOdrediste(), System.lineSeparator()));
		sb.append(String.format("Peron: %d%s", karta.getPeron(), System.lineSeparator()));
		sb.append("Izdata: " + LocalDate.now() + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + System.lineSeparator());
		sb.append(String.format("Mjesec vazenja: %02d/%d%s", mjesecVazenja, localDate.getYear(), System.lineSeparator()));
		sb.append(String.format("Tip: %s%s", tip.toString(),System.lineSeparator())); 
		sb.append(String.format("%20s %.2f KM%s%s", "Cijena:", karta.getCijena(), System.lineSeparator(),System.lineSeparator()));
		sb.append(String.format("Na zahtjev kontrolora pokazati kartu!%s", System.lineSeparator()));
		sb.append(String.format("Biletar: %s%s%s", karta.getImeZaposlenog(), System.lineSeparator(),System.lineSeparator()));
		sb.append(String.format("%10sHvala na povjerenju!", " "));
		System.out.println("Scrrenshot, id mjesecne: " + ProdajaKarataController.idMjesecneKarte);
		
		File file = new File("src\\mjesecnekarte\\karta" + String.format("%013d", ProdajaKarataController.idMjesecneKarte) + ".txt");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		    writer.append(sb);
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void storniraj(int serijskiBroj) {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "update mjesecna_karta,karta set karta.Stanje='Stornirano',mjesecna_karta.Stanje='Stornirano' where mjesecna_karta.IdMjesecneKarte=? and karta.SerijskiBroj=mjesecna_karta.SerijskiBroj";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, serijskiBroj);
			s.executeUpdate();
			File file = new File("src\\mjesecnekarte\\karta" + String.format("%013d", serijskiBroj)+".txt");
			if(file.exists())
				System.out.println(file.delete());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
	
	
	public static MjesecnaKarta pronadjiKartu(int serijskiBroj) {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select Datum,Polaziste,Odrediste,Ime,Prezime,NazivLinije,NazivPrevoznika,Tip,karta.SerijskiBroj,Slika,karta.Cijena from mjesecna_karta join (relacija,karta,linija,prevoznik) on (mjesecna_karta.SerijskiBroj=karta.SerijskiBroj and karta.IdRelacije=relacija.IdRelacije "
				+ " and relacija.IdLinije=linija.IdLinije and linija.JIBPrevoznika=prevoznik.JIBPrevoznika) where mjesecna_karta.IdMjesecneKarte=? and mjesecna_karta.Stanje='Aktivno'";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, serijskiBroj);
			r = s.executeQuery();
			if(r.next()) {
				MjesecnaKarta mk = new MjesecnaKarta(r.getString("Ime"), r.getString("Prezime"), r.getString("NazivLinije"), r.getString("NazivPrevoznika"), r.getString("Tip"), r.getInt("karta.SerijskiBroj"), r.getString("Slika"));
				mk.getRelacija().setPolaziste(r.getString("Polaziste"));
				mk.getRelacija().setOdrediste(r.getString("Odrediste"));
				mk.setDatumIzdavanja(r.getDate("Datum").toLocalDate());
				mk.setCijena(r.getDouble("karta.Cijena"));
				return mk ;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			Util.close(r, s, c);
		}
		return null;
	}

	public static void produziKartu(MjesecnaKarta karta) {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
	}

	public static int kreiraj2(MjesecnaKarta karta) {
		// TODO Auto-generated method stub
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql  = "insert into mjesecna_karta value (default,?,?,?,?,?,?,default)";
		try {
			c = Util.getConnection();
			//s = Util.prepareStatement(c, sql, true, karta.getCijena(), ime, prezime, slikaPath, tip.toString(), ProdajaKarataController.idKarte);
			s = Util.prepareStatement(c, sql, true, karta.getCijena(), karta.getIme(), karta.getPrezime(), karta.getSlika().getAbsolutePath(), karta.getTip().toString(),ProdajaKarataController.idKarte);
			s.executeUpdate();
			r = s.getGeneratedKeys();
			if(r.next()) 
				return r.getInt(1);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
		
	
}
