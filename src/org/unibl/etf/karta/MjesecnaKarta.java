package org.unibl.etf.karta;

import java.io.File;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

import org.unibl.etf.zaposleni.Zaposleni;

public class MjesecnaKarta extends Karta {
	private String ime;
	private String prezime;
	private File slika;
	private TipKarte tip;
	
	public MjesecnaKarta(int idKarte, Linija linija, Relacija relacija, Time vrijemePolaska, Time vrijemeDolaska,
			double cijena, LocalDate datumIzdavanja, Prevoznik prevoznik, String imeZaposlenog,String ime,String prezime,TipKarte tip,File slika,String JIBStanice) {
		super(linija, relacija, vrijemePolaska, vrijemeDolaska, cijena, datumIzdavanja, prevoznik, imeZaposlenog,JIBStanice);
		// TODO Auto-generated constructor stub
		this.ime = ime;
		this.prezime = prezime;
		this.tip = tip;
		this.slika = slika;
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

	@Override
	public String toString() {
		return "MjesecnaKarta [ime=" + ime + ", prezime=" + prezime + ", slika=" + slika + ", tip=" + tip + ", idKarte="
				+ idKarte + ", linija=" + linija + ", relacija=" + relacija + ", vrijemePolaska=" + vrijemePolaska
				+ ", vrijemeDolaska=" + vrijemeDolaska + ", cijena=" + cijena + ", datumIzdavanja=" + datumIzdavanja
				+ ", prevoznik=" + prevoznik + ", imeZaposlenog=" + imeZaposlenog + ", peron=" + peron
				+ ", nazivPrevoznika=" + nazivPrevoznika + ", nazivLinije=" + nazivLinije + "]";
	}
	
	
		
	
}
