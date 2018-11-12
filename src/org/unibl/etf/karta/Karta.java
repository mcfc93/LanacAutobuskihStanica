package org.unibl.etf.karta;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

import org.unibl.etf.zaposleni.Zaposleni;

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

	
	
	
	
}
