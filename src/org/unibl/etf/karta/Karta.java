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
import org.unibl.etf.salterski_radnik.InformacijeController;
import org.unibl.etf.salterski_radnik.ProdajaKarataController;
import org.unibl.etf.salterski_radnik.SalterskiRadnikController;
import org.unibl.etf.util.Mjesto;
import org.unibl.etf.util.Stajaliste;
import org.unibl.etf.util.Util;

public class Karta {
	protected int serijskiBroj;
	protected Relacija relacija;
	protected Date datumIzdavanja;
	protected Date datumPolaska;
	protected int brojSjedista;
	protected String JIBStanice;
	private boolean povratna;
	private boolean rezervacija;
	
	public String getNazivRelacije() {
		return relacija.toString();
	}
	
	public String getNazivLinije() {
		return relacija.getLinija().getNazivLinije();
	}
	public void setNazivLinije(String nazivLinije) {
		relacija.getLinija().setNazivLinije(nazivLinije);
	}
	public int getPeron() {
		return relacija.getLinija().getPeron();
	}
	public void setPeron(int peron) {
		relacija.getLinija().setPeron(peron);
	}
	public Time getVrijemePolaska() {
		return relacija.getVrijemePolaska();
	}
	public void setVrijemePolaska(Time vrijemePolaska) {
		relacija.setVrijemePolaska(vrijemePolaska);
	}
	public Time getVrijemeDolaska() {
		return relacija.getVrijemeDolaska();
	}
	public void setVrijemeDolaska(Time vrijemeDolaska) {
		relacija.setVrijemeDolaska(vrijemeDolaska);
	}
	public String getNazivPrevoznika() {
		return relacija.getLinija().getPrevoznik().getNaziv();
	}
	public void setNazivPrevoznika(String naziv) {
		relacija.getLinija().getPrevoznik().setNaziv(naziv);
	}

	public Karta() {
		super();
		this.relacija = new Relacija();
	}
	/*
	 * konstruktor za pronalazak obicnih karata
	 * */
	

	public Karta(int serijskiBroj, Linija linija, Relacija relacija, Date datumIzdavanja) {
		// TODO Auto-generated constructor stub
		this.serijskiBroj = serijskiBroj;
		this.setRelacija(relacija);
		this.getRelacija().setLinija(linija);
		this.datumIzdavanja = datumIzdavanja;
	}
	

	public Karta(Relacija relacija) {
		super();
		this.relacija = relacija;
	}

	/*
	 * konstruktor za ucitavanje mjesecnih karata
	 */
	
	public Karta(Relacija relacija, double cijenaMjesecna) {
		// TODO Auto-generated constructor stub
		super();
		this.relacija = relacija;
		this.relacija.setCijenaMjesecna(cijenaMjesecna);
	}


	public double getCijena() {
		return ProdajaKarataController.kupovinaMjesecne? relacija.getCijenaMjesecna(): relacija.getCijenaJednokratna();
	}
	public void setCijena(double cijena) {
//		(ProdajaKarataController.kupovinaMjesecne==true) ? relacija.setCijenaMjesecna(cijena) : relacija.setCijenaJednokratna(cijena);
	if(ProdajaKarataController.kupovinaMjesecne)
		relacija.setCijenaMjesecna(cijena);
	else
		relacija.setCijenaJednokratna(cijena);
	}
	public int getSerijskiBroj() {
		return serijskiBroj;
	}

	public void setSerijskiBroj(int serijskiBroj) {
		this.serijskiBroj = serijskiBroj;
	}

	public Relacija getRelacija() {
		return relacija;
	}

	public void setRelacija(Relacija relacija) {
		this.relacija = relacija;
	}

	public Date getDatumIzdavanja() {
		return datumIzdavanja;
	}

	public void setDatumIzdavanja(Date datumIzdavanja) {
		this.datumIzdavanja = datumIzdavanja;
	}

	public Date getDatumPolaska() {
		return datumPolaska;
	}

	public void setDatumPolaska(Date datumPolaska) {
		this.datumPolaska = datumPolaska;
	}

	public int getBrojSjedista() {
		return brojSjedista;
	}

	public void setBrojSjedista(int brojSjedista) {
		this.brojSjedista = brojSjedista;
	}

	public String getJIBStanice() {
		return JIBStanice;
	}

	public void setJIBStanice(String jIBStanice) {
		JIBStanice = jIBStanice;
	}

	public boolean isPovratna() {
		return povratna;
	}

	public void setPovratna(boolean povratna) {
		this.povratna = povratna;
	}

	public boolean isRezervacija() {
		return rezervacija;
	}

	public void setRezervacija(boolean rezervacija) {
		this.rezervacija = rezervacija;
	}



	@Override
	public String toString() {
		return "Karta [serijskiBroj=" + serijskiBroj + ", relacija=" + relacija + ", datumIzdavanja=" + datumIzdavanja
				+ ", datumPolaska=" + datumPolaska + ", brojSjedista=" + brojSjedista + ", JIBStanice=" + JIBStanice
				+ ", povratna=" + povratna + ", rezervacija=" + rezervacija + "]";
	}
	public void stampajKartu()  {
		setDatumIzdavanja(Date.valueOf(LocalDate.now()));
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s%s", PrijavaController.autobuskaStanica.getGrad(), System.lineSeparator()));
		sb.append(String.format("%s%s", PrijavaController.autobuskaStanica.getNaziv(), System.lineSeparator()));
		sb.append(String.format("Informacije: %s%s", PrijavaController.autobuskaStanica.getBrojTelefona(), System.lineSeparator()));
		sb.append(String.format("WEB stranica: %s%s", PrijavaController.autobuskaStanica.getWebStranica(), System.lineSeparator()));
		sb.append(String.format("======================================%s%s", System.lineSeparator(),System.lineSeparator()));
		sb.append(String.format("Prevoznik: %s%s", relacija.getLinija().getPrevoznik().getNaziv(), System.lineSeparator()));
		sb.append(String.format("%s : %s%s", "Naziv linije:", relacija.getLinija().getNazivLinije(), System.lineSeparator()));
		sb.append(System.lineSeparator());
		sb.append(String.format("%10s %s %10s%s", " ", "AUTOBUSKA KARTA", " ", System.lineSeparator()));
		sb.append(String.format("%10s (%s) %10s%s", " ", povratna? "povratna":"jednosmjerna" , " ", System.lineSeparator()));
		sb.append(System.lineSeparator());
		sb.append(String.format("Prodajno mjesto: %s%s", PrijavaController.autobuskaStanica.getGrad(), System.lineSeparator()));
		sb.append(String.format("Serijski broj: %06d%s", serijskiBroj, System.lineSeparator()));
		sb.append(String.format("Peron: %d%s", relacija.getLinija().getPeron() , System.lineSeparator()));
		sb.append(String.format("Sjediste: %d%s", brojSjedista, System.lineSeparator()));
		sb.append(String.format("Relacija: %s - %s%s" , relacija.getPolaziste(), relacija.getOdrediste(), System.lineSeparator()));
		sb.append(String.format("Izdata: %s %s %s", LocalDate.now().toString(), LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")).toString(), System.lineSeparator()));
		sb.append(String.format("Polazak: %s %s%s", datumPolaska.toString(), relacija.getVrijemePolaska().toString(), System.lineSeparator()));
		sb.append(String.format("Dolazak: %s%s", relacija.getVrijemeDolaska().toString(), System.lineSeparator()));
		sb.append(String.format("%20s %.2f KM%s", "Cijena:",  relacija.getCijenaJednokratna(), System.lineSeparator()));
		sb.append(String.format("%20s %.2f KM%s", "Rezervacija:", rezervacija? ProdajaKarataController.REZERVACIJA: 0, System.lineSeparator()));
		sb.append(String.format("%20s %.2f KM%s", "Stanicna usluga:", ProdajaKarataController.STANICNA_USLUGA, System.lineSeparator()));
		sb.append(String.format("%20s %.2f KM%s", "Ukupna cijena:", (relacija.getCijenaJednokratna()+ (rezervacija? ProdajaKarataController.REZERVACIJA: 0) + ProdajaKarataController.STANICNA_USLUGA),System.lineSeparator()));
		sb.append(System.lineSeparator());
		sb.append(String.format("Na zahtjev kontrolora pokazati kartu!%s", System.lineSeparator()));
		sb.append(String.format("Biletar: %s%s%s", PrijavaController.nalog.getZaposleni().getIme(), System.lineSeparator(), System.lineSeparator()));
		sb.append(String.format("%10sHvala na povjerenju!%s%s", " ", " ", System.lineSeparator()));
		System.out.println(sb.toString());
		File file = new File("karte\\karta" + String.format("%06d", serijskiBroj)+".txt");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		    writer.append(sb);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static List<Karta> getKarteList(Stajaliste polaziste,Stajaliste odrediste) {
		List<Karta> karteList = new ArrayList<>();
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		//String sql = "select * from linija join (relacija,prevoznik) on (linija.IdLinije=relacija.IdLinije) and (linija.JIBPrevoznika=prevoznik.JIBPrevoznika) where (linija.IdLinije=relacija.IdLinije) and (Polaziste=? && Odrediste=?) and (linija.Stanje='Aktivno')";
		String sql = "select * from linija join (relacija,prevoznik) on (linija.IdLinije=relacija.IdLinije) and (linija.JIBPrevoznika=prevoznik.JIBPrevoznika) where (linija.IdLinije=relacija.IdLinije) and (Polaziste=? && Odrediste=?) and (linija.Stanje='Aktivno')";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, polaziste.getIdStajalista(), odrediste.getIdStajalista());
			r = s.executeQuery();		
	       	while(r.next()) {
	       		Prevoznik prevoznik = new Prevoznik(r.getString("JIBPrevoznika"), r.getString("prevoznik.NazivPrevoznika"), r.getString("prevoznik.Telefon"), r.getString("prevoznik.Email"));
	       		Linija linija = new Linija(r.getInt("linija.IdLinije"), r.getString("linija.NazivLinije"), r.getInt("linija.Peron"), prevoznik, r.getInt("linija.VoznjaPraznikom"));
	       		// -- PREPRAVITI
	       		Relacija relacija = new Relacija(r.getInt("relacija.IdRelacije"), linija, polaziste, odrediste, r.getTime("VrijemePolaska"), r.getTime("VrijemeDolaska"), r.getDouble("CijenaJednokratna"), r.getString("Dani"));
	       		Karta karta = new Karta(relacija);
	       		karteList.add(karta);
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
	
	public static int kreirajKartu(Karta karta,LocalDate datum) {
		String sql = "insert into karta value (DEFAULT,?,?,?,?,?,?,DEFAULT)";
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, true, karta.getRelacija().getIdRelacije(), Date.valueOf(datum), Date.valueOf(LocalDate.now()),karta.getBrojSjedista(), karta.getJIBStanice(),karta.getCijena());
			System.out.println(s.executeUpdate());
			r = s.getGeneratedKeys();
			if(r.next()) {
				ProdajaKarataController.idKarte = r.getInt(1);
				return r.getInt(1);
			}
			} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(r, s, c);
		}
		return 0;
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
	
	public static Karta pronadjiKartu(int serijskiBroj) {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select linija.NazivLinije, karta.Cijena, relacija.Polaziste, relacija.Odrediste, karta.DatumIzdavanja from karta join (relacija,linija) on "
				+ "(karta.IdRelacije=relacija.IdRelacije) and (relacija.IdLinije=linija.IdLinije) where karta.SerijskiBroj=?";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, serijskiBroj);
			r = s.executeQuery();
			if(r.next()) {
				Linija linija = new Linija(r.getString("NazivLinije"));
				int idPolazista = r.getInt("relacija.Polaziste");
				int idOdredista = r.getInt("relacija.Odrediste");
				Stajaliste polaziste = InformacijeController.stajalistaList.stream().filter(x -> x.getIdStajalista()==idPolazista).findFirst().get();
				Stajaliste odrediste = InformacijeController.stajalistaList.stream().filter(x -> x.getIdStajalista()==idOdredista).findFirst().get();
				Relacija relacija = new Relacija(polaziste, odrediste, r.getDouble("karta.Cijena"));
				Karta karta = new Karta(serijskiBroj,linija,relacija,r.getDate("karta.DatumIzdavanja"));
				return karta;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(r, s, c);
		}
		
		return null;
	}
	
	public static boolean stornirajKartu(int serijskiBroj) {
		Connection c = null;
		PreparedStatement s1 = null;
		PreparedStatement s2 = null;
		PreparedStatement s3 = null;
		String sqlKarta = "update karta,rezervacija set karta.Stanje='Stornirano',rezervacija.Stanje='Stornirano' where karta.SerijskiBroj=? and rezervacija.SerijskiBroj=karta.SerijskiBroj";
		//String sqlRezervacija = "update rezervacija set Stanje='Stornirano' where SerijskiBroj=?";
		//String sqlMjesecna = "update mjesecna_karta set Stanje='Stornirano' where IdMjesecneKarte=?";
		try {
			c = Util.getConnection();
			s1 = Util.prepareStatement(c, sqlKarta, false, serijskiBroj);
		//	s2 = Util.prepareStatement(c, sqlRezervacija, false, serijskiBroj);
		//	s3 = Util.prepareStatement(c, sqlMjesecna, false, serijskiBroj);
			//System.out.println(s2.executeUpdate());
			
			if(s1.executeUpdate()>0) {
			//	s2.close();
			//	s3.close();
				File file = new File("src\\jednokratnekarte\\karta" + String.format("%06d", serijskiBroj)+".txt");
				//File file2 = new File("src\\mjesecnekarte\\karta" + String.format("%06d", serijskiBroj)+".txt");
				//if(file2.exists())
					//file2.delete();
				if(file.exists())
					System.out.println(file.delete());
				return true;
			}
			//s2.close();
		//	s3.close();
			return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			Util.close(s1, c);
		}
		return false;
	}
	
	
	public static int provjeriBrojKarata(Karta karta, Date datum) {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select count(*) from karta join (relacija,linija) on (karta.IdRelacije=relacija.IdRelacije) and (relacija.IdLinije=linija.IdLinije) "
				+ "where (linija.IdLinije=?) and (karta.DatumPolaska=?) and (karta.Stanje='Aktivno')";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, karta.getRelacija().getLinija().getIdLinije(), datum);
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
