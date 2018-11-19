package org.unibl.etf.karta;

public class Prevoznik {
	private String naziv;
	private String email;
	private String adresa;
	private String telefon;
	private int postanskiBroj;
	private String webAdresa;
	private String JIBPrevoznika;
	private String racun;
	
	public String getRacun() {
		return racun;
	}

	public void setRacun(String racun) {
		this.racun = racun;
	}

	public Prevoznik(String naziv, String email, String adresa, String telefon, int postanskiBroj, String webAdresa,
			String jIBPrevoznika, String racun) {
		super();
		this.naziv = naziv;
		this.email = email;
		this.adresa = adresa;
		this.telefon = telefon;
		this.postanskiBroj = postanskiBroj;
		this.webAdresa = webAdresa;
		JIBPrevoznika = jIBPrevoznika;
		this.racun = racun;
	}

	public Prevoznik(String naziv, String email, String adresa, String webAdresa,String telefon, int postanskiBroj) {
		super();
		this.naziv = naziv;
		this.email = email;
		this.webAdresa = webAdresa;
		this.adresa = adresa;
		this.telefon = telefon;
		this.postanskiBroj = postanskiBroj;
	}
	
	public Prevoznik(String naziv, String jib) {
		// TODO Auto-generated constructor stub
		this.naziv = naziv;
		this.JIBPrevoznika = jib;
	}
	

	public Prevoznik(String nazivPrevoznika) {
		this.naziv = nazivPrevoznika;
	}

	public String getJIBPrevoznika() {
		return JIBPrevoznika;
	}

	public void setJIBPrevoznika(String jIBPrevoznika) {
		JIBPrevoznika = jIBPrevoznika;
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
	
	public int getPostanskiBroj() {
		return postanskiBroj;
	}

	public void setPostanskiBroj(int postanskiBroj) {
		this.postanskiBroj = postanskiBroj;
	}
	

	@Override
	public String toString() {
		return "Prevoznik [naziv=" + naziv + ", email=" + email + ", adresa=" + adresa + ", telefon=" + telefon
				+ ", postanskiBroj=" + postanskiBroj + ", webAdresa=" + webAdresa + ", JIBPrevoznika=" + JIBPrevoznika
				+ ", racun=" + racun + "]";
	}

	
	
	
}
