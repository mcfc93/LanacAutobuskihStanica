package org.unibl.etf.karta;

public class Prevoznik {
	private String naziv;
	private String email;
	private String adresa;
	private String telefon;
	private String mjesto;
	private String webAdresa;
	public Prevoznik(String naziv, String email, String adresa, String webAdresa,String telefon, String mjesto) {
		super();
		this.naziv = naziv;
		this.email = email;
		this.webAdresa = webAdresa;
		this.adresa = adresa;
		this.telefon = telefon;
		this.mjesto = mjesto;
	}
	
	public String getWebAdresa() {
		return webAdresa;
	}

	public void setWebAdresa(String webAdresa) {
		this.webAdresa = webAdresa;
	}

	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAdresa() {
		return adresa;
	}
	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}
	public String getTelefon() {
		return telefon;
	}
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	public String getMjesto() {
		return mjesto;
	}
	public void setMjesto(String mjesto) {
		this.mjesto = mjesto;
	}
	@Override
	public String toString() {
		return "Prevoznik [naziv=" + naziv + ", email=" + email + ", adresa=" + adresa + ", telefon=" + telefon
				+ ", mjesto=" + mjesto + "]";
	}
	
	
	
}
