package org.unibl.etf.karta;

import java.io.File;

public class MjesecnaKarta extends Karta {
	private String ime;
	private String prezime;
	private File slika;
	private double cijena;
	private MjesecVazenja mjesecVazenja;
	private TipKarte tip;
	
	public MjesecnaKarta() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MjesecnaKarta(int idKarte, Prevoznik prevozink, Relacija relacija, String ime, String prezime, File slika, double cijena,
			MjesecVazenja mjesecVazenja, TipKarte tip) {
		super(idKarte, prevozink, relacija);
		this.ime = ime;
		this.prezime = prezime;
		this.slika = slika;
		this.cijena = cijena;
		this.mjesecVazenja = mjesecVazenja;
		this.tip = tip;
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
	public double getCijena() {
		return cijena;
	}
	public void setCijena(double cijena) {
		this.cijena = cijena;
	}
	public MjesecVazenja getMjesecVazenja() {
		return mjesecVazenja;
	}
	public void setMjesecVazenja(MjesecVazenja mjesecVazenja) {
		this.mjesecVazenja = mjesecVazenja;
	}
	public TipKarte getTip() {
		return tip;
	}
	public void setTip(TipKarte tip) {
		this.tip = tip;
	}
	

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	@Override
	public String toString() {
		return "MjesecnaKarta [ime=" + ime + ", prezime=" + prezime + ", slika=" + slika + ", cijena=" + cijena
				+ ", mjesecVazenja=" + mjesecVazenja + ", tip=" + tip + "]";
	}
	
	
	
}
