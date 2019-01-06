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

import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.salterski_radnik.MjesecnaKartaController;
import org.unibl.etf.salterski_radnik.ProdajaKarataController;
import org.unibl.etf.util.Stajaliste;
import org.unibl.etf.util.Util;

public class MjesecnaKarta extends Karta {
	
	private int idMjesecneKarte;
	private String ime;
	private String prezime;
	private File slika;
	private TipKarte tip;
	
	/*
	 * konstruktor za pronalazak mjesecnih karata za storniranje
	 * /*
	 * konstruktor za pronalazak obicnih karata
	 * new Karta(r.getInt("karta.SerijskiBroj"), r.getDouble("karta.Cijena"), r.getString("relacija.Polaziste"),
						r.getString("relacija.Odrediste"), r.getString("linija.NazivLinije"), r.getDate("karta.Datum"))*/
	
	 
	
	 public MjesecnaKarta(int serijskiBroj, double cijenaMjesecna,Relacija relacija, Date datumIzdavanja) {
		 super();
		 this.serijskiBroj = serijskiBroj;
		 this.relacija = relacija;
		 this.relacija.setCijenaMjesecna(cijenaMjesecna);
		 this.datumIzdavanja = datumIzdavanja;
	 }
	
	public MjesecnaKarta(Relacija relacija, String ime, String prezime, File slika, TipKarte tip) {
		super(relacija);
		// TODO Auto-generated constructor stub
		this.ime = ime;
		this.prezime = prezime;
		this.slika = slika;
		this.tip = tip;
	}
	/*
	 * 
	 * 					MjesecnaKartaController.karta = new MjesecnaKarta(karta.getRelacija().getLinija(),karta.getRelacija(),
	 * 					imeTextField.getText(),prezimeTextField.getText(),
	 *		 	odabranaSlika,karta.getRelacija().getLinija().getPrevoznik().getNaziv(),tipKarteComboBox.getValue());
*/	
	
	public MjesecnaKarta(Relacija relacija, String ime, String prezime, File odabranaSlika, String nazivPrevoznika,
			TipKarte tip) {
		// TODO Auto-generated constructor stub
		this.relacija = relacija;
		this.ime = ime;
		this.prezime = prezime;
		this.slika = odabranaSlika;
		this.relacija.getLinija().getPrevoznik().setNaziv(nazivPrevoznika);
		this.tip = tip;
	}

	public int getIdMjesecneKarte() {
		return idMjesecneKarte;
	}

	public void setIdMjesecneKarte(int idMjesecneKarte) {
		this.idMjesecneKarte = idMjesecneKarte;
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



	public static int kreirajKartu(MjesecnaKarta karta) {
	//	Karta.kreirajKartu(karta, brojSjedista, datum);
		/*Connection c = null;
		karta.setSerijskiBroj(ProdajaKarataController.idKarte);
		PreparedStatement s = null;
		String sql = "insert into mjesecna_karta value (default,?,?,?,?,?,?,default)";
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, true, karta.getRelacija().getCijenaMjesecna(), ime, prezime, slikaPath, tip.toString(), ProdajaKarataController.idKarte);
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
		return 0;*/
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
		sb.append(String.format("Prevoznik: %s%s", karta.getRelacija().getLinija().getPrevoznik().getNaziv(),System.lineSeparator()));
		sb.append(String.format("Naziv linije: %s%s", karta.getRelacija().getLinija().getNazivLinije(), System.lineSeparator()));
		sb.append(System.lineSeparator());
		sb.append(String.format("%10s %s %10s%s", " ", "AUTOBUSKA KARTA", " ", System.lineSeparator()));
		sb.append(String.format("%10s (%s) %10s%s", " ", "mjesecna" , " ", System.lineSeparator()));
		sb.append(System.lineSeparator());
		sb.append(String.format("Prodajno mjesto: %s%s", PrijavaController.autobuskaStanica.getGrad(), System.lineSeparator()));
		sb.append(String.format("Serijski broj: %013d%s", ProdajaKarataController.idMjesecneKarte, System.lineSeparator()));
		sb.append(String.format("Relacija: %s - %s%s", karta.getRelacija().getPolaziste(), karta.getRelacija().getOdrediste(), System.lineSeparator()));
		sb.append(String.format("Peron: %d%s", karta.getRelacija().getLinija().getPeron(), System.lineSeparator()));
		sb.append("Izdata: " + LocalDate.now() + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + System.lineSeparator());
		sb.append(String.format("Mjesec vazenja: %02d/%d%s", mjesecVazenja, localDate.getYear(), System.lineSeparator()));
		sb.append(String.format("Tip: %s%s", tip.toString(),System.lineSeparator())); 
		sb.append(String.format("%20s %.2f KM%s%s", "Cijena:", karta.getRelacija().getCijenaMjesecna(), System.lineSeparator(),System.lineSeparator()));
		sb.append(String.format("Na zahtjev kontrolora pokazati kartu!%s", System.lineSeparator()));
		sb.append(String.format("Biletar: %s%s%s", PrijavaController.nalog.getZaposleni().getIme(), System.lineSeparator(),System.lineSeparator()));
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
	
	
	public static int dodajMjesecnuCijenu(Relacija relacija) {
		Connection c = null;
		ResultSet r =null;
		PreparedStatement s = null;
		String sql = "insert into cijena_mjesecne_karte value (?,?)";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, true, relacija.getIdRelacije(), relacija.getCijenaMjesecna());
			System.out.println(s.executeUpdate());
			r = s.getGeneratedKeys();
			if(r.next())
				return r.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	
	
	public static List<Karta> getMjesecneKarteList(Stajaliste polaziste, Stajaliste odrediste) {
		List<Karta> karteList = new ArrayList<>();
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select * from cijena_mjesecne_karte join (relacija,linija,prevoznik) on cijena_mjesecne_karte.IdRelacije=relacija.IdRelacije and prevoznik.Stanje='Aktivno' where relacija.Polaziste=? and relacija.Odrediste=?";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, polaziste.getIdStajalista(), odrediste.getIdStajalista());
			r = s.executeQuery();
			while(r.next()) {
	       		Prevoznik prevoznik = new Prevoznik(r.getString("JIBPrevoznika"), r.getString("prevoznik.NazivPrevoznika"), r.getString("prevoznik.Telefon"), r.getString("prevoznik.Email"));
	       		Linija linija = new Linija(r.getInt("linija.IdLinije"), r.getString("linija.NazivLinije"), r.getInt("linija.Peron"), prevoznik, r.getInt("linija.VoznjaPraznikom"));
	       		Relacija relacija = new Relacija(r.getInt("relacija.IdRelacije"), linija, polaziste, odrediste, r.getTime("VrijemePolaska"), r.getTime("VrijemeDolaska"), r.getDouble("CijenaJednokratna"), r.getString("Dani"));
	       		Karta karta = new Karta(relacija, r.getDouble("cijena_mjesecne_karte"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
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


	
		
	
}
