package org.unibl.etf.zaposleni;

import java.io.Serializable;
import java.util.Date;

public abstract class Zaposleni implements Serializable {
	private static final long serialVersionUID = 1L;
	private String ime;
	private String prezime;
	private String adresa;
	private String brojTelefona;
	private Date datumRodjenja;
	private String jmbg;
	private String mjestoPrebivalista;
	private String strucnaSprema;
	private String email;
	
	public Zaposleni() {
		super();
	}
	
	public Zaposleni(String ime, String prezime) {
		this.ime=ime;
		this.prezime=prezime;
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
	
	
}
