package org.unibl.etf.karta;

import java.time.LocalDate;
import java.time.LocalTime;

import org.unibl.etf.zaposleni.Zaposleni;

public class JednokratnaKarta extends Karta {
	private LocalTime vrijemePolaska;
	private LocalDate datumPolaska;
	private double cijena;
	private LocalDate datumIzdavanja;
	private boolean povratna;
	private int peron;
	private int brojSjedista;
	private Zaposleni izdaoZaposleni;
	
	public JednokratnaKarta() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public JednokratnaKarta(int idKarte, Prevoznik prevozink, Relacija relacija, LocalTime vrijemePolaska, LocalDate datumPolaska,
			double cijena, LocalDate datumIzdavanja, boolean povratna, int peron, int brojSjedista, Zaposleni izdaoZaposleni) {
		super(idKarte, prevozink, relacija);
		// TODO Auto-generated constructor stub
		this.vrijemePolaska = vrijemePolaska;
		this.datumPolaska = datumPolaska;
		this.cijena = cijena;
		this.datumIzdavanja = datumIzdavanja;
		this.povratna = povratna;
		this.peron = peron;
		this.brojSjedista = brojSjedista;
		this.izdaoZaposleni = izdaoZaposleni;	
	}
	
	
	public LocalTime getVrijemePolaska() {
		return vrijemePolaska;
	}
	public void setVrijemePolaska(LocalTime vrijemePolaska) {
		this.vrijemePolaska = vrijemePolaska;
	}
	public LocalDate getDatumPolaska() {
		return datumPolaska;
	}
	public void setDatumPolaska(LocalDate datumPolaska) {
		this.datumPolaska = datumPolaska;
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
	public boolean isPovratna() {
		return povratna;
	}
	public void setPovratna(boolean povratna) {
		this.povratna = povratna;
	}
	public int getPeron() {
		return peron;
	}
	public void setPeron(int peron) {
		this.peron = peron;
	}
	public int getBrojSjedista() {
		return brojSjedista;
	}
	public void setBrojSjedista(int brojSjedista) {
		this.brojSjedista = brojSjedista;
	}
	public Zaposleni getIzdaoZaposleni() {
		return izdaoZaposleni;
	}
	public void setIzdaoZaposleni(Zaposleni izdaoZaposleni) {
		this.izdaoZaposleni = izdaoZaposleni;
	}
	
	
	@Override
	public String toString() {
		return "JednokratnaKarta [vrijemePolaska=" + vrijemePolaska + ", datumPolaska=" + datumPolaska + ", cijena="
				+ cijena + ", datumIzdavanja=" + datumIzdavanja + ", povratna=" + povratna + ", peron=" + peron
				+ ", brojSjedista=" + brojSjedista + ", izdaoZaposleni=" + izdaoZaposleni + "]";
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}



	
	
	
}
