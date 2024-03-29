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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.salterski_radnik.InformacijeController;
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
		this.relacija = relacija;
		this.ime = ime;
		this.prezime = prezime;
		this.slika = odabranaSlika;
		this.relacija.getLinija().getPrevoznik().setNaziv(nazivPrevoznika);
		this.tip = tip;
	}
	
	@Override
	public void setCijena(double cijenaMjesecna) {
		relacija.setCijenaMjesecna(cijenaMjesecna);
	}
	public MjesecnaKarta(double cijenaMjesecna,Date datumIzdavanja,int idMjesecneKarte,Relacija relacija, int serijskiBroj, String tip, String ime, String prezime, String slikaPath) {
		this.setCijena(cijenaMjesecna);
		this.datumIzdavanja = datumIzdavanja;
		this.idMjesecneKarte = idMjesecneKarte;
		this.relacija = relacija;
		this.serijskiBroj = serijskiBroj;
		this.tip = TipKarte.valueOf(tip);
		this.ime = ime;
		this.prezime = prezime;
		this.slika = new File(slikaPath);
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
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
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
		//	s = Util.prepareStatement(c, sql, true, karta.getCijena(), karta.getIme(), karta.getPrezime(), karta.getSlika().getAbsolutePath(), karta.getTip().toString(),ProdajaKarataController.idKarte);
			s = Util.prepareStatement(c, sql, true, karta.getCijena(), karta.getIme(), karta.getPrezime(), karta.getSlika().getAbsolutePath(), karta.getTip().toString(),karta.getSerijskiBroj());

			s.executeUpdate();
			r = s.getGeneratedKeys();
			if(r.next()) 
				return r.getInt(1);
			
			
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			Util.showBugAlert();
			
		}
		
		return 0;
		
	}

	public static void stampajKartu(Karta karta, int i, LocalDate value, String naziv, TipKarte tip) {
		LocalDate localDate = LocalDate.now();
		//int mjesecVazenja = (localDate.getDayOfMonth()>25) ? localDate.getMonthValue()+1: localDate.getMonthValue();
		StringBuilder sb = new StringBuilder();
		
		sb.append(PrijavaController.autobuskaStanica.getGrad() + System.lineSeparator());
		sb.append(PrijavaController.autobuskaStanica.getNaziv() + System.lineSeparator());
		sb.append("Informacije: " + PrijavaController.autobuskaStanica.getBrojTelefona() + System.lineSeparator());
		sb.append("Web adresa: " + PrijavaController.autobuskaStanica.getWebStranica() + System.lineSeparator());
		sb.append("========================================" + System.lineSeparator() + System.lineSeparator());
		
		sb.append("Prevoznik: " + karta.getRelacija().getLinija().getPrevoznik().getNaziv() + System.lineSeparator());
		sb.append("Linija: " + karta.getRelacija().getLinija().getNazivLinije() + System.lineSeparator() + System.lineSeparator());

		sb.append("            AUTOBUSKA  KARTA" + System.lineSeparator());
		sb.append("               (mjesecna)" + System.lineSeparator() + System.lineSeparator());
		
		sb.append("Prodajno mjesto: " + PrijavaController.autobuskaStanica.getNaziv() + System.lineSeparator());
		sb.append("Serijski broj: " + String.format("%013d", ProdajaKarataController.idMjesecneKarte) + System.lineSeparator());
		sb.append("Peron:    " + karta.getRelacija().getLinija().getPeron() + System.lineSeparator());
		
		sb.append("Relacija: " + karta.getRelacija().getPolaziste().getNazivStajalista() + " -");
		if(karta.getRelacija().toString().length()>30) {
			sb.append(System.lineSeparator() + "          ");
		}
		sb.append(karta.getRelacija().getOdrediste().getNazivStajalista() + System.lineSeparator());
		
		sb.append("Izdata: " + LocalDate.now() + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + System.lineSeparator());
		
		String mjesecVazenjaString =
	    		(localDate.getDayOfMonth() >= 25?
	    			(((localDate.getMonthValue()+1)%12) + "-" + (localDate.getMonthValue()==1?localDate.getYear()+1:localDate.getYear())):
	    				(localDate.getMonthValue() + "-" + localDate.getYear()));
		sb.append("Mjesec važenja: " + mjesecVazenjaString + System.lineSeparator());
		//sb.append(String.format("Mjesec važenja: %02d/%d%s", mjesecVazenja, localDate.getYear(), System.lineSeparator()));
		sb.append("Tip:    " + tip.toString() + System.lineSeparator()); 
		switch(tip) {
		case ĐAČKA:
			sb.append(String.format("%40s%s", ("Popust: " + String.format("%.2f KM", ((karta.getRelacija().getCijenaMjesecna() / karta.getRelacija().getLinija().getPrevoznik().getDjackiPopust()) - karta.getRelacija().getCijenaMjesecna()))), System.lineSeparator()));
			break;
		case PENZIONERSKA:
			sb.append(String.format("%40s%s", ("Popust: " + String.format("%.2f KM", ((karta.getRelacija().getCijenaMjesecna() / karta.getRelacija().getLinija().getPrevoznik().getPenzionerskiPopust()) - karta.getRelacija().getCijenaMjesecna()))), System.lineSeparator()));
			break;
		default:
			sb.append(String.format("%40s%s", "Popust: 0.00 KM",  System.lineSeparator()));
			break;
			
		};
		sb.append(String.format("%40s%s", ("Cijena: " + String.format("%.2f KM", karta.getRelacija().getCijenaMjesecna())), System.lineSeparator()));
		
		sb.append("========================================" + System.lineSeparator());
		sb.append("Na zahtjev kontrolora pokazati kartu!" + System.lineSeparator());
		sb.append("Biletar: " + PrijavaController.nalog.getZaposleni().getIme() + System.lineSeparator() + System.lineSeparator());
		sb.append(String.format("%40s%s", "Hvala na povjerenju!", System.lineSeparator()));
		
		File file = new File("karte\\mjesecna_" + String.format("%013d", ProdajaKarataController.idMjesecneKarte) + "-" + mjesecVazenjaString + ".txt");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		    writer.append(sb);
		    
		} catch (IOException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
	
	public static void storniraj(MjesecnaKarta mjesecnaKarta) {
		Connection c = null;
		PreparedStatement s = null;
		String sql = "update karta set karta.Stanje='Stornirano' where SerijskiBroj=?";
		String sqlMjesecna = "update mjesecna_karta set Stanje='Stornirano' where IdMjesecneKarte=?";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, mjesecnaKarta.getSerijskiBroj());
			s.executeUpdate();
			s.close();
			s = Util.prepareStatement(c, sqlMjesecna, false, mjesecnaKarta.getIdMjesecneKarte());
			 s.executeUpdate();
			LocalDate localDate = mjesecnaKarta.getDatumIzdavanja().toLocalDate();
			String mjesecVazenjaString =
		    		(localDate.getDayOfMonth() >= 25?
		    			(((localDate.getMonthValue()+1)%12) + "-" + (localDate.getMonthValue()==12?localDate.getYear()+1:localDate.getYear())):
		    				(localDate.getMonthValue() + "-" + localDate.getYear()));
			System.out.println(mjesecVazenjaString);
			File file = new File("karte\\mjesecna_" + String.format("%013d", mjesecnaKarta.getIdMjesecneKarte()) + "-" + mjesecVazenjaString +".txt");
			if(file.exists())
				file.renameTo(new File("karte\\mjesecna_" + String.format("%013d", mjesecnaKarta.getIdMjesecneKarte()) + "-" + mjesecVazenjaString + "_STORNIRANO.txt"));
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			Util.showBugAlert();
		}
		finally {
			Util.close(s, c);
		}
	
	}
	
	public static List<Karta> getMjesecneKarteList(Stajaliste polaziste, Stajaliste odrediste) {
		List<Karta> karteList = new ArrayList<>();
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select * from relacija join (red_voznje,linija,prevoznik,popust_prevoznika) on (relacija.IdRelacije=red_voznje.IdRelacije and relacija.IdLinije=linija.IdLinije and linija.JIBPrevoznika=prevoznik.JIBPrevoznika and prevoznik.JIBPrevoznika=popust_prevoznika.JIBPrevoznika) "
				+ "where ((relacija.Polaziste=? and relacija.Odrediste=?) or (relacija.Polaziste=? and relacija.Odrediste=?)) and CijenaMjesecna is not NULL and linija.Stanje='Aktivno' and red_voznje.Stanje='Aktivno' group by prevoznik.JIBPrevoznika";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, polaziste.getIdStajalista(), odrediste.getIdStajalista(), odrediste.getIdStajalista(), polaziste.getIdStajalista());
			r = s.executeQuery();
			while(r.next()) {
	       		Prevoznik prevoznik = new Prevoznik(r.getString("NazivPrevoznika"), r.getString("prevoznik.Email"), r.getString("prevoznik.Telefon"), r.getString("prevoznik.JIBPrevoznika"), r.getDouble("DjackiPopust"), r.getDouble("RadnickiPopust"), r.getDouble("PenzionerskiPopust"));
	       		Linija linija = new Linija(r.getInt("linija.IdLinije"), r.getString("linija.NazivLinije"), r.getInt("linija.Peron"), prevoznik, r.getInt("linija.VoznjaPraznikom"));
	       		Relacija relacija = new Relacija(r.getInt("relacija.IdRelacije"), linija, polaziste, odrediste, r.getTime("VrijemePolaska"), r.getTime("VrijemeDolaska"), r.getDouble("CijenaJednokratna"), r.getString("Dani"));
	       		Karta karta = new Karta(relacija, r.getDouble("CijenaMjesecna"));
	       		karteList.add(karta);
	       		
			}
			
			return karteList;
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			Util.showBugAlert();
			return karteList;
		} finally {
			Util.close(r, s, c);
		}
	}
	
	public Double getCijena() {
		return relacija.getCijenaMjesecna();
	}
	
	public static MjesecnaKarta pronadjiKartu(int idMjesecneKarte) {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select * from mjesecna_karta join (karta,relacija,prevoznik,linija) on (mjesecna_karta.SerijskiBroj=karta.SerijskiBroj "
				+ "and karta.IdRelacije=relacija.IdRelacije and relacija.IdLinije=linija.IdLinije and "
				+ "linija.JIBPrevoznika=prevoznik.JIBPrevoznika) where mjesecna_karta.IdMjesecneKarte=? and mjesecna_karta.Stanje='Aktivno'";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, idMjesecneKarte);
			r = s.executeQuery();
			if(r.next()) {
				Prevoznik prevoznik = new Prevoznik(r.getString("prevoznik.NazivPrevoznika"));
				int idPolazista = r.getInt("Polaziste");
				int idOdredista = r.getInt("Odrediste");
				Stajaliste polaziste = InformacijeController.stajalistaList.stream().filter(p -> p.getIdStajalista()==idPolazista).findFirst().get();
				Stajaliste odrediste = InformacijeController.stajalistaList.stream().filter(o -> o.getIdStajalista()==idOdredista).findFirst().get();
				Linija linija = new Linija(r.getInt("linija.IdLinije"), r.getString("linija.NazivLinije"), r.getInt("linija.Peron"), prevoznik, r.getInt("VoznjaPraznikom"));
				Relacija relacija = new Relacija(r.getInt("relacija.IdRelacije"),linija,prevoznik,polaziste,odrediste);
				MjesecnaKarta mjesecnaKarta = new MjesecnaKarta(r.getDouble("karta.Cijena"), r.getDate("DatumIzdavanja"),r.getInt("IdMjesecneKarte"),relacija, r.getInt("karta.SerijskiBroj"), r.getString("Tip"), r.getString("Ime"), r.getString("Prezime"), r.getString("Slika"));
				mjesecnaKarta.getRelacija().setCijenaMjesecna(r.getDouble("karta.Cijena"));		
				mjesecnaKarta.setSerijskiBroj(r.getInt("karta.SerijskiBroj"));
				return mjesecnaKarta;
			}
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			Util.showBugAlert();
		}
		finally {
			Util.close(r, s, c);
		}
		return null;
	}

	@Override
	public String toString() {
		return "MjesecnaKarta [idMjesecneKarte=" + idMjesecneKarte + ", ime=" + ime + ", prezime=" + prezime
				+ ", slika=" + slika + ", tip=" + tip + "]" + super.toString();
	}


	
		
	
}
