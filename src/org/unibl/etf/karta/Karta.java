package org.unibl.etf.karta;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.unibl.etf.autobuska_stanica.AutobuskaStanica;
import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.salterski_radnik.InformacijeController;
import org.unibl.etf.salterski_radnik.ProdajaKarataController;
import org.unibl.etf.util.Praznik;
import org.unibl.etf.util.Stajaliste;
import org.unibl.etf.util.Util;

import javafx.application.Platform;

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
		super();
		this.relacija = relacija;
		this.relacija.setCijenaMjesecna(cijenaMjesecna);
	}


	public Double getCijena() {
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
		
		sb.append(PrijavaController.autobuskaStanica.getGrad() + System.lineSeparator());
		sb.append(PrijavaController.autobuskaStanica.getNaziv() + System.lineSeparator());
		sb.append("Informacije: " + PrijavaController.autobuskaStanica.getBrojTelefona() + System.lineSeparator());
		sb.append("Web adresa: " + PrijavaController.autobuskaStanica.getWebStranica() + System.lineSeparator());
		sb.append("========================================" + System.lineSeparator() + System.lineSeparator());
		
		sb.append("Prevoznik: " + relacija.getLinija().getPrevoznik().getNaziv() + System.lineSeparator());
		sb.append("Linija: " + relacija.getLinija().getNazivLinije() + System.lineSeparator() + System.lineSeparator());
		sb.append("            AUTOBUSKA  KARTA" + System.lineSeparator());
		/*
		String temp = povratna? "(povratna)":"(jednosmjerna)";
		String temp2 = "%"+( (int)(40-temp.length())/2)+"s%s%s";
		System.out.println(temp2);
		sb.append(String.format(temp2," ", temp, System.lineSeparator()));
		sb.append(System.lineSeparator());
		*/
		sb.append((povratna? "               (povratna)":"             (jednosmjerna)") + System.lineSeparator() + System.lineSeparator());
		
		sb.append("Prodajno mjesto: " + PrijavaController.autobuskaStanica.getNaziv() + System.lineSeparator());
		sb.append("Serijski broj: " + String.format("%013d", serijskiBroj) + System.lineSeparator());
		sb.append("Peron:    " + relacija.getLinija().getPeron() + System.lineSeparator());
		sb.append("Sjedište: " + brojSjedista + System.lineSeparator());
		
		sb.append("Relacija: " + relacija.getPolaziste().getNazivStajalista() + " -");
		if(relacija.toString().length()>30) {
			sb.append(System.lineSeparator() + "          ");
		}
		sb.append(relacija.getOdrediste().getNazivStajalista() + System.lineSeparator());

		sb.append("Izdata:   " + LocalDate.now().toString() + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")).toString() + System.lineSeparator());
		sb.append("Polazak:  " + datumPolaska.toString() + " " + relacija.getVrijemePolaska().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")).toString() + System.lineSeparator());
		sb.append("Dolazak:  " + (relacija.getVrijemeDolaska().toLocalTime().isBefore(relacija.getVrijemePolaska().toLocalTime()) ? datumPolaska.toLocalDate().plusDays(1).toString() : datumPolaska.toLocalDate().toString()) + " "  + relacija.getVrijemeDolaska().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")).toString() + System.lineSeparator());
		
		sb.append(String.format("%40s%s", ("Cijena: " + String.format("%.2f", relacija.getCijenaJednokratna()) + " KM"), System.lineSeparator()));
		sb.append(String.format("%40s%s", ("Rezervacija: " + String.format("%.2f", rezervacija? ProdajaKarataController.REZERVACIJA: 0) + " KM"), System.lineSeparator()));
		sb.append(String.format("%40s%s", ("Stanična usluga: " + String.format("%.2f", ProdajaKarataController.STANICNA_USLUGA) + " KM"), System.lineSeparator()));
		sb.append(String.format("%40s%s", ("Ukupna cijena: " + String.format("%.2f", (relacija.getCijenaJednokratna()+ (rezervacija? ProdajaKarataController.REZERVACIJA: 0) + ProdajaKarataController.STANICNA_USLUGA)) + " KM"), System.lineSeparator()));
		sb.append(System.lineSeparator());
		sb.append("========================================" + System.lineSeparator());
		sb.append("Na zahtjev kontrolora pokazati kartu!" + System.lineSeparator());
		sb.append("Biletar: " + PrijavaController.nalog.getZaposleni().getIme() + System.lineSeparator() + System.lineSeparator());
		sb.append(String.format("%40s%s", "Hvala na povjerenju!", System.lineSeparator()));
		//System.out.println(sb.toString());
		String value = new String(sb.toString().getBytes(Charset.forName("UTF-8")));
		System.out.println(value);
		File file = new File("karte\\karta_" + String.format("%013d", serijskiBroj)+".txt");
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		    writer.append(value);
		    
		} catch (IOException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		
		/*
		try (Writer out = new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream(
								"karte\\karta_" + String.format("%013d", serijskiBroj)+".txt"), "UTF-8")
				)
			) {
			    out.write(value);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}

	public static List<Karta> getKarteListDolasci(Stajaliste polaziste, Stajaliste odrediste) {
		List<Karta> karteList = new ArrayList<>();
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select VrijemeDolaska,VrijemePolaska,CijenaJednokratna,Dani,relacija.IdRelacije,linija.IdLinije,linija.NazivLinije,linija.Peron,linija.VoznjaPraznikom,NazivPrevoznika,Email,Telefon,prevoznik.JIBPrevoznika,DjackiPopust from relacija join (linija,prevoznik,popust_prevoznika,red_voznje)" + 
				" on (relacija.IdRelacije=red_voznje.IdRelacije and relacija.IdLinije=linija.IdLinije and linija.JIBPrevoznika=prevoznik.JIBPrevoznika and popust_prevoznika.JIBPrevoznika=prevoznik.JIBPrevoznika and linija.Stanje='Aktivno')" + 
				" where (Polaziste=? and Odrediste=? and red_voznje.Stanje='Aktivno')" + //group by VrijemeDolaska" + 
				" union" + 
				" select VrijemeDolaskaPovratna,VrijemePolaskaPovratna,CijenaJednokratna,Dani,relacija.IdRelacije,linija.IdLinije,linija.NazivLinije,linija.Peron,linija.VoznjaPraznikom,NazivPrevoznika,prevoznik.Email,prevoznik.Telefon,prevoznik.JIBPrevoznika,DjackiPopust from relacija join (red_voznje,linija,prevoznik,popust_prevoznika)" + 
				" on (relacija.IdRelacije=red_voznje.IdRelacije and relacija.IdLinije=linija.IdLinije and linija.JIBPrevoznika=prevoznik.JIBPrevoznika and popust_prevoznika.JIBPrevoznika=prevoznik.JIBPrevoznika and linija.Stanje='Aktivno')" + 
				" where (Polaziste=? and Odrediste=? and red_voznje.Stanje='Aktivno')" + //group by VrijemeDolaskaPovratna" +
				" order by VrijemeDolaska;";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, polaziste.getIdStajalista(), odrediste.getIdStajalista(), odrediste.getIdStajalista(), polaziste.getIdStajalista());
			r = s.executeQuery();
			while(r.next()) {
	       		Prevoznik prevoznik = new Prevoznik(r.getString("NazivPrevoznika"), r.getString("Email"), r.getString("Telefon"),  r.getString("JIBPrevoznika"), r.getDouble("DjackiPopust"));
	       		Linija linija = new Linija(r.getInt("IdLinije"), r.getString("NazivLinije"), r.getInt("Peron"), prevoznik, r.getInt("VoznjaPraznikom"));
	       		Relacija relacija = new Relacija(r.getInt("IdRelacije"), linija, polaziste, odrediste, r.getTime("VrijemePolaska"), r.getTime("VrijemeDolaska"), r.getDouble("CijenaJednokratna"), r.getString("Dani"));
	       		Karta karta = new Karta(relacija);
	       		karteList.add(karta);
	       	}
			//trazenje stanica na odredistu
			if(odrediste.getNazivStajalista().equals(odrediste.getNaziv())) {
				System.out.println("ODREDISTE JESTE STANICA");
       			for(Stajaliste stajaliste : InformacijeController.stajalistaStanica) {
       				if(stajaliste.getPostanskiBroj()==odrediste.getPostanskiBroj()) {
       					s.close();
       					s = Util.prepareStatement(c, sql, false, polaziste.getIdStajalista(), stajaliste.getIdStajalista(), stajaliste.getIdStajalista(), polaziste.getIdStajalista());
       					r = s.executeQuery();
       					while(r.next()) {
       			       		Prevoznik prevoznik = new Prevoznik(r.getString("NazivPrevoznika"), r.getString("Email"), r.getString("Telefon"),  r.getString("JIBPrevoznika"), r.getDouble("DjackiPopust"));
       			       		Linija linija = new Linija(r.getInt("IdLinije"), r.getString("NazivLinije"), r.getInt("Peron"), prevoznik, r.getInt("VoznjaPraznikom"));
       			       		// -- PREPRAVITI
       			       		Relacija relacija = new Relacija(r.getInt("IdRelacije"), linija, polaziste, stajaliste, r.getTime("VrijemePolaska"), r.getTime("VrijemeDolaska"), r.getDouble("CijenaJednokratna"), r.getString("Dani"));
       			       		Karta karta = new Karta(relacija);
       			       		System.out.println(karta);
       			       		
       			       		karteList.add(karta);
       			       	}
       				}
       					
       			}
       				
       		} else {
				System.out.println("ODREDISTE NIJE STANICA");
			}
			return karteList;
		}
		catch(SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			Util.showBugAlert();
			return karteList;
		}
		finally {
			Util.close(r, s, c);
		}
	}
	
	public static List<Karta> getKarteList(Stajaliste polaziste, Stajaliste odrediste/*, String dan*/) {
		List<Karta> karteList = new ArrayList<>();
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		//String sql = "select * from linija join (relacija,prevoznik,popust_prevoznika) on (linija.IdLinije=relacija.IdLinije) and (linija.JIBPrevoznika=prevoznik.JIBPrevoznika)"
		//		+ "and (prevoznik.JIBPrevoznika=popust_prevoznika.JIBPrevoznika) where (linija.IdLinije=relacija.IdLinije) and ((Polaziste=? and Odrediste=?) or (Polaziste=? and Odrediste=?)) and (linija.Stanje='Aktivno') order by VrijemePolaska";
		
		String sql = "select VrijemePolaska,VrijemeDolaska,CijenaJednokratna,Dani,relacija.IdRelacije,linija.IdLinije,linija.NazivLinije,linija.Peron,linija.VoznjaPraznikom,NazivPrevoznika,Email,Telefon,prevoznik.JIBPrevoznika,DjackiPopust from relacija join (red_voznje,linija,prevoznik,popust_prevoznika)" + 
				" on (relacija.IdRelacije=red_voznje.IdRelacije and relacija.IdLinije=linija.IdLinije and linija.JIBPrevoznika=prevoznik.JIBPrevoznika and popust_prevoznika.JIBPrevoznika=prevoznik.JIBPrevoznika and linija.Stanje='Aktivno')" + 
				" where (Polaziste=? and Odrediste=? and red_voznje.Stanje='Aktivno') " + //and FIND_IN_SET(?,Dani)>0)" + //group by VrijemePolaska" + 
				" union" + 
				" select VrijemePolaskaPovratna,VrijemeDolaskaPovratna,CijenaJednokratna,Dani,relacija.IdRelacije,linija.IdLinije,linija.NazivLinije,linija.Peron,linija.VoznjaPraznikom,NazivPrevoznika,prevoznik.Email,prevoznik.Telefon,prevoznik.JIBPrevoznika,DjackiPopust from relacija join (red_voznje,linija,prevoznik,popust_prevoznika)" + 
				" on (relacija.IdRelacije=red_voznje.IdRelacije and relacija.IdLinije=linija.IdLinije and linija.JIBPrevoznika=prevoznik.JIBPrevoznika and popust_prevoznika.JIBPrevoznika=prevoznik.JIBPrevoznika and linija.Stanje='Aktivno')" + 
				" where (Polaziste=? and Odrediste=? and red_voznje.Stanje='Aktivno') " + //and FIND_IN_SET(?,Dani)>0)" + //group by VrijemePolaskaPovratna" +
				" order by VrijemePolaska;";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, polaziste.getIdStajalista(), odrediste.getIdStajalista(), /*dan,*/ odrediste.getIdStajalista(), polaziste.getIdStajalista()/*, dan*/);
			r = s.executeQuery();		
			while(r.next()) {
	       		Prevoznik prevoznik = new Prevoznik(r.getString("NazivPrevoznika"), r.getString("Email"), r.getString("Telefon"),  r.getString("JIBPrevoznika"), r.getDouble("DjackiPopust"));
	       		Linija linija = new Linija(r.getInt("IdLinije"), r.getString("NazivLinije"), r.getInt("Peron"), prevoznik, r.getInt("VoznjaPraznikom"));
	       		Relacija relacija = new Relacija(r.getInt("IdRelacije"), linija, polaziste, odrediste, r.getTime("VrijemePolaska"), r.getTime("VrijemeDolaska"), r.getDouble("CijenaJednokratna"), r.getString("Dani"));
	       		Karta karta = new Karta(relacija);
	       		karteList.add(karta);
	       	}
			//trazenje stanica na odredistu
			if(odrediste.getNazivStajalista().equals(odrediste.getNaziv())) {
				System.out.println("ODREDISTE JESTE STANICA");
       			for(Stajaliste stajaliste : InformacijeController.stajalistaStanica) {
       				if(stajaliste.getPostanskiBroj()==odrediste.getPostanskiBroj()) {
       					s.close();
       					s = Util.prepareStatement(c, sql, false, polaziste.getIdStajalista(), stajaliste.getIdStajalista(), /*dan, */stajaliste.getIdStajalista(), polaziste.getIdStajalista()/*, dan*/);
       					r = s.executeQuery();
       					while(r.next()) {
       			       		Prevoznik prevoznik = new Prevoznik(r.getString("NazivPrevoznika"), r.getString("Email"), r.getString("Telefon"),  r.getString("JIBPrevoznika"), r.getDouble("DjackiPopust"));
       			       		Linija linija = new Linija(r.getInt("IdLinije"), r.getString("NazivLinije"), r.getInt("Peron"), prevoznik, r.getInt("VoznjaPraznikom"));
       			       		// -- PREPRAVITI
       			       		Relacija relacija = new Relacija(r.getInt("IdRelacije"), linija, polaziste, stajaliste, r.getTime("VrijemePolaska"), r.getTime("VrijemeDolaska"), r.getDouble("CijenaJednokratna"), r.getString("Dani"));
       			       		Karta karta = new Karta(relacija);
       			       		System.out.println(karta);
       			       		
       			       		karteList.add(karta);
       			       	}
       				}
       					
       			}
       				
       		} else {
				System.out.println("ODREDISTE NIJE STANICA");
			}
			return karteList/*.stream().sorted((k1,k2) -> k1.getVrijemePolaska().toLocalTime().compareTo(k2.getVrijemePolaska().toLocalTime())).collect(Collectors.toList())*/;
		}
		catch(SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			Util.showBugAlert();
			return karteList;
		}
		finally {
			Util.close(r, s, c);
		}
	}

	public static int kreirajKartu(Karta karta,LocalDate datum) {
		karta.setJIBStanice(PrijavaController.autobuskaStanica.getJib());
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
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			Util.showBugAlert();
		} finally {
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
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			Util.showBugAlert();
		}
	}
	
	public static Karta pronadjiKartu(int serijskiBroj) {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select linija.NazivLinije, karta.Cijena, relacija.Polaziste, relacija.Odrediste, karta.DatumIzdavanja from karta join (relacija,linija) on "
				+ "(karta.IdRelacije=relacija.IdRelacije) and (relacija.IdLinije=linija.IdLinije) where karta.SerijskiBroj=? and karta.Stanje='Aktivno'";
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
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			Util.showBugAlert();
		} finally {
			Util.close(r, s, c);
		}
		
		return null;
	}
	
	public static boolean stornirajKartu(int serijskiBroj) {
		Connection c = null;
		PreparedStatement s = null;
		String sqlKarta = "update karta set karta.Stanje='Stornirano' where karta.SerijskiBroj=?";
		String sqlRezervacija = "update rezervacija set Stanje='Stornirano' where SerijskiBroj=?";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sqlKarta, false, serijskiBroj);
			s.executeUpdate();
			s.close();
			s = Util.prepareStatement(c, sqlRezervacija, false, serijskiBroj);
			s.executeUpdate();
			File file = new File("karte\\karta_" + String.format("%013d", serijskiBroj)+".txt");
			if(file.exists())
				file.renameTo(new File("karte\\karta_" + String.format("%013d", serijskiBroj) + "_STORNIRANO.txt"));
			return true;
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			Util.showBugAlert();
		} finally {
			Util.close(s, c);
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
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			Util.showBugAlert();
			return 0;
		} finally {
			Util.close(r, s, c);
		}
		return 0;
	}

	public static List<Karta> getInfoList(String polazakDolazak) {
		Connection c = null;
		ResultSet r = null;
		List<Karta> karteList = new ArrayList<>();
		CallableStatement s = null;
		try {
	       	c=Util.getConnection();
	    	s = c.prepareCall("{call getInfoList(?,?)}");
	       	s.setInt(1, "POLASCI".equals(polazakDolazak)? 1:0);
	    	s.setInt(2, PrijavaController.autobuskaStanica.getIdStajalista());
	       	r = s.executeQuery();
	        while(r.next()) {
	        	Prevoznik prevoznik = new Prevoznik(r.getString("NazivPrevoznika"));
				Linija linija = new Linija(0, r.getString("NazivLinije"), r.getInt("Peron"), prevoznik, r.getInt("VoznjaPraznikom"));
				Relacija relacija = new Relacija(0, linija, prevoznik, null, null);
				Karta karta = new Karta(relacija);
				karta.getRelacija().setDani(r.getString("Dani"));
				/*
				 * polasci*/
				if(polazakDolazak.equals("POLASCI")) {
					karta.getRelacija().setVrijemePolaska(r.getTime("VrijemePolaska")); 
					//if(karta.getRelacija().getDani().contains(String.valueOf(LocalDate.now().getDayOfWeek().getValue()))) {
						//if(karta.getRelacija().getVrijemePolaska().toLocalTime().isAfter(LocalTime.now())) {
							karteList.add(karta);			
						//}
					//}
				}
				else {
					karta.getRelacija().setVrijemeDolaska(r.getTime("VrijemeDolaska"));
					//if(karta.getRelacija().getDani().contains(String.valueOf(LocalDate.now().getDayOfWeek().getValue()))) {
						//if(karta.getRelacija().getVrijemeDolaska().toLocalTime().isAfter(LocalTime.now())) {
							karteList.add(karta);			
						//}
					//}
				}
				/*
				if(Praznik.getHolidayList().stream().anyMatch(p -> MonthDay.from(LocalDate.now()).equals(MonthDay.of(p.getMjesec(), p.getDan())))) {
					karteList.removeIf(k -> k.getRelacija().getLinija().getVoznjaPraznikom() == 9 || !k.getRelacija().getDani().contains(k.getRelacija().getLinija().getVoznjaPraznikom() + ""));
				} else {
					karteList.removeIf(k -> !k.getRelacija().getDani().contains(String.valueOf(LocalDate.now().getDayOfWeek().getValue())));
				}
				*/
	        }
	        
	       // return karteList;
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    	Util.showBugAlert();
	    } finally {
	    	Util.close(r,s,c);
	    }
		return karteList;
	}
}
