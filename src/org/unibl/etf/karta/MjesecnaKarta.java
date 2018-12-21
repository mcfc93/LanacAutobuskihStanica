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

	
	public static void kreirajKartu(Karta karta, int brojSjedista, LocalDate datum, String ime, String prezime, TipKarte tip,	String slikaPath) {
	//	Karta.kreirajKartu(karta, brojSjedista, datum);
		Connection c = null;
		karta.setIdKarte(ProdajaKarataController.idKarte);
		System.out.println("Id karte obicne: ");
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
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Util.close(r, s, c);
		}
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
		sb.append(String.format("Serijski broj: %06d%s", ProdajaKarataController.idKarte, System.lineSeparator()));
		sb.append(String.format("Relacija: %s - %s%s", karta.getRelacija().getPolaziste(), karta.getRelacija().getOdrediste(), System.lineSeparator()));
		sb.append(String.format("Peron: %d%s", karta.getPeron(), System.lineSeparator()));
		sb.append("Izdata: " + LocalDate.now() + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + System.lineSeparator());
		sb.append(String.format("Mjesec vazenja: %02d/%d%s", mjesecVazenja, localDate.getYear(), System.lineSeparator()));
		sb.append(String.format("%20s %.2f KM%s%s", "Cijena:", karta.getCijena(), System.lineSeparator(),System.lineSeparator()));
		sb.append(String.format("Na zahtjev kontrolora pokazati kartu!%s", System.lineSeparator()));
		sb.append(String.format("Biletar: %s%s%s", karta.getImeZaposlenog(), System.lineSeparator(),System.lineSeparator()));
		sb.append(String.format("%10sHvala na povjerenju!", " "));
		
		File file = new File("src\\mjesecnekarte\\karta" + String.format("%06d", ProdajaKarataController.idKarte)+".txt");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		    writer.append(sb);
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sb.toString());
	}
	
	
	
	
		
	
}
