package org.unibl.etf.karta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.unibl.etf.administrativni_radnik.IzmjenaLinijeController;
import org.unibl.etf.administrativni_radnik.ListaLinijaController;
import org.unibl.etf.util.Stajaliste;
import org.unibl.etf.util.Util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Relacija {
	
	private int idRelacije;
	private Linija linija;
	private Stajaliste polaziste;
	private Stajaliste odrediste;
	private Time vrijemePolaska;
	private Time vrijemeDolaska;
	private Time vrijemePolaskaPovratna;
	private Time vrijemeDolaskaPovratna;
	private Double cijenaJednokratna;
	private Double cijenaMjesecna;
	private String dani;
	private LocalTime duzinaPuta;
	
	/*
	 * 
	 * konstruktor za ucitavanje relacija
	 */
	//relacijeList.add(new Relacija(r.getInt("IdRelacije"), r.getInt("IdLinije"), polaziste, odrediste, r.getDouble("CijenaJednokratna"), r.getDouble("CijenaMjesecna")));
	
	


	public Relacija(int idRelacije, int idLinije, Stajaliste polaziste, Stajaliste odrediste, double cijenaJednokratna, Double cijenaMjesecna) {
		this.idRelacije = idRelacije;
		this.linija = new Linija(idLinije);
		this.polaziste = polaziste;
		this.odrediste = odrediste;
		this.cijenaJednokratna = cijenaJednokratna;
		this.cijenaMjesecna = cijenaMjesecna;
		
		
	}
	
	
	/**
	 * 
	 * TEMP
	 * 
	 */
	
	public Relacija(int idRelacije, int idLinije, String nazivLinije, int peron, int voznjaPraznikom, Stajaliste polaziste, Stajaliste odrediste, double cijenaJednokratna, double cijenaMjesecna) {
		this.idRelacije = idRelacije;
		this.linija = new Linija(idLinije, nazivLinije, peron, new Prevoznik("Pavlovic"), voznjaPraznikom);
		this.polaziste = polaziste;
		this.odrediste = odrediste;
		this.cijenaJednokratna = cijenaJednokratna;
		this.cijenaMjesecna = cijenaMjesecna;
		this.cijenaJednokratna = cijenaJednokratna;
		this.cijenaMjesecna = cijenaMjesecna;
	}
	
	
	
	
	/*
	 * konstruktor za kartu
	 */
	public Relacija(int idRelacije, Linija linija, Stajaliste polaziste, Stajaliste odrediste, Time vrijemePolaska,
			Time vrijemeDolaska, double cijenaJednokratna, String dani) {
		super();
		this.idRelacije = idRelacije;
		this.linija = linija;
		this.polaziste = polaziste;
		this.odrediste = odrediste;
		this.vrijemePolaska = vrijemePolaska;
		this.vrijemeDolaska = vrijemeDolaska;
		this.cijenaJednokratna = cijenaJednokratna;
		this.cijenaJednokratna = cijenaJednokratna;
		this.dani = dani;
	}
	
	/*
	 * konstruktor za ucitavanje mjesecnih karata
	 */
	public Relacija(int idRelacije, Linija linija, Stajaliste polaziste, Stajaliste odrediste, double cijenaMjesecna) {
		super();
		this.idRelacije = idRelacije;
		this.linija = linija;
		this.polaziste = polaziste;
		this.odrediste = odrediste;
		this.cijenaMjesecna = cijenaMjesecna;
		this.cijenaMjesecna = cijenaMjesecna;	
		}
	
	
	/*
	 * konstruktor za pronalazak karte
	 */
	public Relacija(String nazivPolazista, String nazivOdredista) {
		this.polaziste = new Stajaliste(nazivPolazista);
		this.odrediste = new Stajaliste(nazivOdredista);
	}
	
	public Relacija() {
		super();
	}

	/*
	 * konstruktor za produzavanje karte*/
	public Relacija(int idRelacije,Linija linija, Prevoznik prevoznik, Stajaliste polaziste, Stajaliste odrediste) {
		this.idRelacije = idRelacije;
		this.linija = linija;
		this.linija.setPrevoznik(prevoznik);
		this.polaziste = polaziste;
		this.odrediste = odrediste;
	}


	public Relacija(Stajaliste polaziste, Stajaliste odrediste, double cijena) {
		this.polaziste = polaziste;
		this.odrediste = odrediste;
		this.cijenaJednokratna = cijena;
		
	}

/*
 * konstruktor za kreiranje linije*/
	public Relacija(Stajaliste polaziste, Stajaliste odrediste, LocalTime duzinaPuta) {
		this.polaziste = polaziste;
		this.odrediste = odrediste;
		this.duzinaPuta = duzinaPuta;
	}

	public LocalTime getDuzinaPuta() {
		return duzinaPuta;
	}

	public String getNazivPolazista() {
		return polaziste.getNazivStajalista();
	}
	public String getNazivOdredista() {
		return odrediste.getNazivStajalista();
	}
	

	public void setDuzinaPuta(LocalTime duzinaPuta) {
		this.duzinaPuta = duzinaPuta;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idRelacije;
		return result;
	}

	

	public Time getVrijemePolaskaPovratna() {
		return vrijemePolaskaPovratna;
	}


	public void setVrijemePolaskaPovratna(Time vrijemePolaskaPovratna) {
		this.vrijemePolaskaPovratna = vrijemePolaskaPovratna;
	}


	public Time getVrijemeDolaskaPovratna() {
		return vrijemeDolaskaPovratna;
	}


	public void setVrijemeDolaskaPovratna(Time vrijemeDolaskaPovratna) {
		this.vrijemeDolaskaPovratna = vrijemeDolaskaPovratna;
	}


	public void setCijenaJednokratna(Double cijenaJednokratna) {
		this.cijenaJednokratna = cijenaJednokratna;
	}


	public void setCijenaMjesecna(Double cijenaMjesecna) {
		this.cijenaMjesecna = cijenaMjesecna;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Relacija other = (Relacija) obj;
		if (idRelacije != other.idRelacije)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return polaziste.getNazivStajalista() + " - " + odrediste.getNazivStajalista();
	}

	public Linija getLinija() {
		return linija;
	}
	public void setLinija(Linija linija) {
		this.linija = linija;
	}
	public int getIdRelacije() {
		return idRelacije;
	}
	public void setIdRelacije(int idRelacije) {
		this.idRelacije = idRelacije;
	}
	public Stajaliste getPolaziste() {
		return polaziste;
	}
	public void setPolaziste(Stajaliste polaziste) {
		this.polaziste = polaziste;
	}
	public Stajaliste getOdrediste() {
		return odrediste;
	}
	public void setOdrediste(Stajaliste odrediste) {
		this.odrediste = odrediste;
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
	
	public String getDani() {
		return dani;
	}
	public void setDani(String dani) {
		this.dani= dani;
	}
	

	public static int dodajRelaciju(Relacija relacija) {
		String sql = "INSERT INTO relacija VALUE (default,?,?,?,?,?,?)";
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r =null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, true, relacija.getLinija().getIdLinije(), relacija.getPolaziste().getIdStajalista(), relacija.getOdrediste().getIdStajalista(), relacija.getCijenaJednokratna(),Time.valueOf(relacija.getDuzinaPuta()),relacija.getCijenaMjesecna());
			s.executeUpdate();
			r = s.getGeneratedKeys();
			if(r.next()) {
				return r.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		finally {
			Util.close(r, s, c);
		}
		return 0;
	}
	
	public static int dodajRedVoznje(Relacija relacija, int idPolaska, int stanicnoZadrzavanjeUMinutama) {
		String sql = "INSERT INTO red_voznje VALUE (default,?,?,?,?,?,?,?,default)"; 
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r =null;
		if(Stajaliste.getStajalistaStanicaList().contains(relacija.getPolaziste())) {
			//
		}
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, true, relacija.getVrijemePolaska(), relacija.getVrijemeDolaska(), relacija.getVrijemePolaskaPovratna(), relacija.getVrijemeDolaskaPovratna(), relacija.getDani(), relacija.getIdRelacije(), idPolaska);
			s.executeUpdate();
			r = s.getGeneratedKeys();

			if(r.next()) {
				return r.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}finally {
			Util.close(r, s, c);
		}
		return 0;
	}
	
	public static boolean izmijeniRelaciju(Relacija relacija, int idPolaska, Linija linija) {
		Connection c = null;
		String sqlRelacija = "update relacija set CijenaJednokratna=?,CijenaMjesecna=? where IdRelacije=?";
		String sqlRedVoznje = "update red_voznje set Dani=?,VrijemePolaska=?,VrijemeDolaska=?, VrijemePolaskaPovratna=?,VrijemeDolaskaPovratna=? where IdRelacije=? and IdPolaska=?";
		String sqlLinija = "update Linija set Stanje=?,NazivLinije=?,JIBPrevoznika=?,Peron=?,VoznjaPraznikom=? where IdLinije=?;";
		PreparedStatement s = null;
	    try {
	       	c = Util.getConnection();
	    	s = Util.prepareStatement(c, sqlRelacija, false, relacija.getCijenaJednokratna(), relacija.getCijenaMjesecna(), relacija.getIdRelacije());
	    	s.executeUpdate();
	    	s.close();
	    	s = Util.prepareStatement(c, sqlRedVoznje, false, relacija.getDani(), relacija.getVrijemePolaska(), relacija.getVrijemeDolaska(), relacija.getVrijemePolaskaPovratna(), relacija.getVrijemeDolaskaPovratna(), relacija.getIdRelacije(), idPolaska);
	    	s.executeUpdate();
	    	s.close();
	    	s = Util.prepareStatement(c, sqlLinija, false, linija.getStanje(), linija.getNazivLinije(), linija.getPrevoznik().getJIBPrevoznika(), linija.getPeron(), linija.getVoznjaPraznikom(), linija.getIdLinije());
	    	s.executeUpdate();
	    	return true;
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(s,c);
	    }
	    return false;
	}
	
	/*public static List<Relacija> getRelacije(int idLinije) {
		List<Relacija> relacijeList = new ArrayList<>();
		Connection c = null;
		ResultSet r =null;
		PreparedStatement s = null;
		String sql = "select * from relacija left join (linija,cijena_mjesecne_karte) on (relacija.IdLinije=linija.IdLinije and relacija.IdRelacije=cijena_mjesecne_karte.IdRelacije) "
				+ "where relacija.IdLinije=?";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, idLinije);
			r = s.executeQuery();
			while(r.next()) {
				
				int idPolazista = r.getInt("Polaziste");
				int idOdredista = r.getInt("Odrediste");
				System.out.println("Cijena mjesecna:" + r.getDouble("CijenaMjesecna"));
				Stajaliste polaziste = ListaLinijaController.stajalistaList.stream().filter(x -> x.getIdStajalista()==idPolazista).findFirst().get();
				System.out.println(polaziste);
				Stajaliste odrediste = ListaLinijaController.stajalistaList.stream().filter(y -> y.getIdStajalista()==idOdredista).findFirst().get();
				System.out.println(odrediste);

				Relacija relacija = new Relacija(r.getInt("IdRelacije"), idLinije, polaziste, odrediste, r.getDouble("CijenaJednokratna"), r.getDouble("CijenaMjesecna"));
				System.out.println(relacijeList.add(relacija));
			}
			return relacijeList;
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			Util.showBugAlert();
		}
		return null;
	}*/
	public static void izbrisiRedVoznje(Relacija relacija, int idPolaska) {
		Connection c = null;
		ResultSet r =null;
		PreparedStatement s = null;
		String sql = "update red_voznje set Stanje='Izbrisano' where IdPolaska=? and IdRelacije=?";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, idPolaska, relacija.getIdRelacije());
			s.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		finally {
			Util.close(r, s, c);
		}
	}

	public static ObservableList<String> getVremenaPolazaka(int pStajaliste, int oStajaliste) {
		ObservableList<String> datumList = FXCollections.observableArrayList();
		Connection c = null;
		ResultSet r =null;
		PreparedStatement s = null;
		String sql = "select IdPolaska,VrijemePolaska,VrijemePolaskaPovratna from linija join (red_voznje,relacija) on (relacija.IdRelacije=red_voznje.IdRelacije and relacija.IdLinije=linija.IdLinije and Polaziste=PStajaliste and Odrediste=OStajaliste) where PStajaliste=? and OStajaliste=? and linija.Stanje!='Izbrisano' and red_voznje.Stanje='Aktivno'";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, pStajaliste, oStajaliste);
			r = s.executeQuery();
			while(r.next()) {
				datumList.add( r.getTime("VrijemePolaska").toLocalTime().toString() + " - " + r.getTime("VrijemePolaskaPovratna").toLocalTime().toString());
				IzmjenaLinijeController.idPolaskaList.add(r.getInt("IdPolaska"));
			}
			return datumList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			Util.showBugAlert();
			return datumList;
		}
		finally {
			Util.close(r, s, c);
		}

	}
	
	public static int getSljedeciIdPolaska(int pStajaliste, int oStajaliste) {
		Connection c = null;
		ResultSet r =null;
		PreparedStatement s = null;
		String sql = "select count(IdPolaska) from linija join (red_voznje,relacija) on (relacija.IdRelacije=red_voznje.IdRelacije and relacija.IdLinije=linija.IdLinije and Polaziste=PStajaliste and Odrediste=OStajaliste) where PStajaliste=? and OStajaliste=? and linija.Stanje!='Izbrisano'";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, pStajaliste, oStajaliste);
			r = s.executeQuery();
			while(r.next()) {
				return r.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			Util.showBugAlert();
		}
		finally {
			Util.close(r, s, c);
		}
		return 0;

	}
	public static List<Relacija> getRelacije(int idLinije, int idPolaska) {
		List<Relacija> relacijeList = new ArrayList<>();
		Connection c = null;
		ResultSet r =null;
		PreparedStatement s = null;
		String sql = "select relacija.IdRelacije,Dani,Polaziste,Odrediste,VrijemePolaska,VrijemeDolaska,VrijemePolaskaPovratna,VrijemeDolaskaPovratna,CijenaJednokratna,CijenaMjesecna,TrajanjePuta from relacija left join (red_voznje,linija) on (relacija.IdRelacije=red_voznje.IdRelacije and relacija.IdLinije=linija.IdLinije) where relacija.IdLinije=? and IdPolaska=? and red_voznje.Stanje='Aktivno'";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, idLinije, idPolaska);
			r = s.executeQuery();
			while(r.next()) {
				
				int idPolazista = r.getInt("Polaziste");
				int idOdredista = r.getInt("Odrediste");
				Stajaliste polaziste = ListaLinijaController.stajalistaList.stream().filter(x -> x.getIdStajalista()==idPolazista).findFirst().get();
				Stajaliste odrediste = ListaLinijaController.stajalistaList.stream().filter(y -> y.getIdStajalista()==idOdredista).findFirst().get();
				Relacija relacija = new Relacija(r.getInt("IdRelacije"), idLinije, polaziste, odrediste, r.getDouble("CijenaJednokratna"), (r.getDouble("CijenaMjesecna")==0.0)? null:r.getDouble("CijenaMjesecna"));
				relacija.setVrijemeDolaska(r.getTime("VrijemeDolaska"));
				relacija.setVrijemePolaska(r.getTime("VrijemePolaska"));
				relacija.setVrijemeDolaskaPovratna(r.getTime("VrijemeDolaskaPovratna"));
				relacija.setVrijemePolaskaPovratna(r.getTime("VrijemePolaskaPovratna"));
				relacija.setDani(r.getString("Dani"));
				relacija.setDuzinaPuta(r.getTime("TrajanjePuta").toLocalTime());
				relacijeList.add(relacija);
			}
			return relacijeList;
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			Util.showBugAlert();
		}
		return null;
	}


	public Double getCijenaJednokratna() {
		return cijenaJednokratna;
	}
	public void setCijenaJednokratna(double cijenaJednokratna) {
		this.cijenaJednokratna = cijenaJednokratna;
	}
	public Double getCijenaMjesecna() {
		return cijenaMjesecna;
	}
	public void setCijenaMjesecna(double cijenaMjesecna) {
		this.cijenaMjesecna = cijenaMjesecna;
	}

	public static int getBrojStajalistaLinije(int idLinije) {
		Connection c = null;
		ResultSet r =null;
		PreparedStatement s = null;
		String sql = "select count(DISTINCT Odrediste) AS TotalRows from relacija where IdLinije=?";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, idLinije);
			r = s.executeQuery();
			if(r.next()) {
				return r.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			Util.showBugAlert();
		}
		finally {
			Util.close(r, s, c);
		}
		return 0;
		
	}

	public static List<Relacija> getPrveUneseneRelacije(int idLinije) {
		List<Relacija> relacijeList = new ArrayList<>();
		Connection c = null;
		ResultSet r =null;
		PreparedStatement s = null;
		String sql = "select relacija.IdRelacije,Dani,Polaziste,Odrediste,CijenaJednokratna,CijenaMjesecna,TrajanjePuta from relacija left join (red_voznje,linija) on (relacija.IdRelacije=red_voznje.IdRelacije and relacija.IdLinije=linija.IdLinije) where relacija.IdLinije=? and IdPolaska=1";
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, sql, false, idLinije);
			r = s.executeQuery();
			while(r.next()) {
				
				int idPolazista = r.getInt("Polaziste");
				int idOdredista = r.getInt("Odrediste");
				Stajaliste polaziste = ListaLinijaController.stajalistaList.stream().filter(x -> x.getIdStajalista()==idPolazista).findFirst().get();
				Stajaliste odrediste = ListaLinijaController.stajalistaList.stream().filter(y -> y.getIdStajalista()==idOdredista).findFirst().get();
				Relacija relacija = new Relacija(r.getInt("IdRelacije"), idLinije, polaziste, odrediste, r.getDouble("CijenaJednokratna"), (r.getDouble("CijenaMjesecna")==0.0)? null:r.getDouble("CijenaMjesecna"));
				relacija.setDani(r.getString("Dani"));
				relacija.setDuzinaPuta(r.getTime("TrajanjePuta").toLocalTime());
				relacija.setVrijemePolaska(Time.valueOf(LocalTime.now()));
				relacija.setVrijemeDolaska(Time.valueOf(LocalTime.now()));
				relacija.setVrijemePolaskaPovratna(Time.valueOf(LocalTime.now()));
				relacija.setVrijemeDolaskaPovratna(Time.valueOf(LocalTime.now()));
				
				relacijeList.add(relacija);
			}
			return relacijeList;
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			Util.showBugAlert();
		}
		return null;
	}
	
	

	
	
}
