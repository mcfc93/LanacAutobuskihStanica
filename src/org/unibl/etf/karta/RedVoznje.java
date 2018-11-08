package org.unibl.etf.karta;

import java.sql.Date;
import java.sql.Time;

public class RedVoznje {
	private String nazivLinije;
	private Time vrijemePolaska;
	private Time vrijemeDolaska;
	private double cijena;
	private String nazivPrevoznika;
	private int idLinije;
	private int peron;
	public RedVoznje(String nazivLinije, Time vrijemePolaska, Time vrijemeDolaska, double cijena,
			String nazivPrevoznika, int idLinije, int peron) {
		super();
		this.nazivLinije = nazivLinije;
		this.vrijemePolaska = vrijemePolaska;
		this.vrijemeDolaska = vrijemeDolaska;
		this.cijena = cijena;
		this.nazivPrevoznika = nazivPrevoznika;
		this.idLinije = idLinije;
		this.peron = peron;
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
	public double getCijena() {
		return cijena;
	}
	public void setCijena(double cijena) {
		this.cijena = cijena;
	}
	public String getNazivPrevoznika() {
		return nazivPrevoznika;
	}
	public void setNazivPrevoznika(String nazivPrevoznika) {
		this.nazivPrevoznika = nazivPrevoznika;
	}
	public int getIdLinije() {
		return idLinije;
	}
	public void setIdLinije(int idLinije) {
		this.idLinije = idLinije;
	}
	public int getPeron() {
		return peron;
	}
	public void setPeron(int peron) {
		this.peron = peron;
	}
	@Override
	public String toString() {
		return "RedVoznje [nazivLinije=" + nazivLinije + ", vrijemePolaska=" + vrijemePolaska + ", vrijemeDolaska="
				+ vrijemeDolaska + ", cijena=" + cijena + ", nazivPrevoznika=" + nazivPrevoznika + ", idLinije="
				+ idLinije + ", peron=" + peron + "]";
	}
	
	

	
	
	
}
