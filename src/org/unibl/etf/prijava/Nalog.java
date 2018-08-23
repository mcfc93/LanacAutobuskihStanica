package org.unibl.etf.prijava;

public class Nalog {
	private String korisnickoIme;
	private String lozinka;
	private int idStanice;
	private boolean prijavljen;		//da li je trenutni korisnik prijavljen
	
	
	public Nalog() {
		super();
	}
	
	public Nalog(String korisnickoIme, String lozinka, int idStanice, boolean prijavljen) {
		super();
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.idStanice = idStanice;
		this.prijavljen = prijavljen;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}
	
	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}
	
	public String getLozinka() {
		return lozinka;
	}
	
	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}
	
	public int getIdStanice() {
		return idStanice;
	}
	
	public void setIdStanice(int idStanice) {
		this.idStanice = idStanice;
	}
	
	public boolean isPrijavljen() {
		return prijavljen;
	}
	
	public void setPrijavljen(boolean prijavljen) {
		this.prijavljen = prijavljen;
	}
	
	@Override
	public String toString() {
		return "Nalog [korisnickoIme=" + korisnickoIme + ", lozinka=" + lozinka + ", idStanice=" + idStanice
				+ ", prijavljen=" + prijavljen + "]";
	}
	
	public boolean prijava(String korisnickoIme, String lozinka) {
//BAZA
		if(true) {
			this.prijavljen=true;
		} else {
//AZURIRANJE LABELE
		}
		return true;
	}
	
	public boolean odjava() {
		prijavljen=false;
		return true;
	}
	
	//boolean provjeriKorisnickoIme(String korisnickoIme)
	//boolean provjeriLozinku(String lozinka)
	//hash code
}
