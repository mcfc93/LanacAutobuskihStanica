package org.unibl.etf.karta;

import java.util.ArrayList;

public class Linija {
	private int idLinije;
	private String nazivLinije;
	private int idStanice;
	private String daniUSedmici;
	private int peron;
	private String nazivPrevoznika;
	private String stanje;
	
	public Linija(int idLinije, String nazivLinije) {
		super();
		this.idLinije = idLinije;
		this.nazivLinije = nazivLinije;
	}
	
	public Linija(int idLinije,String nazivLinije,String daniUSedmici,int peron,String nazivPrevoznika,String stanje) {
		super();
		this.idLinije = idLinije;
		this.nazivLinije = nazivLinije;
		this.daniUSedmici = daniUSedmici;
		this.peron = peron;
		this.nazivPrevoznika = nazivPrevoznika;
		this.stanje = stanje;
	}

	public String getStanje() {
		return stanje;
	}

	public void setStanje(String stanje) {
		this.stanje = stanje;
	}

	public Linija(int idLinije,String nazivLinije,String daniUSedmici,int peron,String nazivPrevoznika) {
		super();
		this.idLinije = idLinije;
		this.nazivLinije = nazivLinije;
		this.daniUSedmici = daniUSedmici;
		this.peron = peron;
		this.nazivPrevoznika = nazivPrevoznika;
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
	public String getNazivLinije() {
		return nazivLinije;
	}
	public void setNazivLinije(String nazivLinije) {
		this.nazivLinije = nazivLinije;
	}
	public int getIdStanice() {
		return idStanice;
	}
	public void setIdStanice(int idStanice) {
		this.idStanice = idStanice;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idLinije;
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
		Linija other = (Linija) obj;
		if (idLinije != other.idLinije)
			return false;
		return true;
	}


	public int getPeron() {
		return peron;
	}


	public void setPeron(int peron) {
		this.peron = peron;
	}


	public String getDaniUSedmici() {
		return daniUSedmici;
	}


	public void setDaniUSedmici(String daniUSedmici) {
		this.daniUSedmici = daniUSedmici;
	}


	@Override
	public String toString() {
		return nazivLinije;
	}


	
	
	
	
	
}

