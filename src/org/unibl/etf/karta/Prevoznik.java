package org.unibl.etf.karta;

public class Prevoznik {
	private int idPrevoznika;
	private String naziv;
	private String email;
	private String adresa;
	private String telefon;
	
	public Prevoznik(int idPrevoznika, String naziv, String email, String adresa, String telefon) {
		super();
		this.idPrevoznika = idPrevoznika;
		this.naziv = naziv;
		this.email = email;
		this.adresa = adresa;
		this.telefon = telefon;
	}
	
	public Prevoznik() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getIdPrevoznika() {
		return idPrevoznika;
	}
	public void setIdPrevoznika(int idPrevoznika) {
		this.idPrevoznika = idPrevoznika;
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
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idPrevoznika;
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
		Prevoznik other = (Prevoznik) obj;
		if (idPrevoznika != other.idPrevoznika)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Prevoznik [idPrevoznika=" + idPrevoznika + ", naziv=" + naziv + ", email=" + email + ", adresa="
				+ adresa + ", telefon=" + telefon + "]";
	}
	
}
