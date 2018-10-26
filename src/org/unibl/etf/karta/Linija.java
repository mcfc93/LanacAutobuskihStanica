package org.unibl.etf.karta;

import java.util.ArrayList;

public class Linija {
	
	private ArrayList<Relacija> relacije = new ArrayList<>();
	private int idLinije;
	private String nazivLinije;
	private int idStanice;
	
	public Linija(ArrayList<Relacija> relacije, int idLinije, String nazivLinije, int idStanice) {
		super();
		this.relacije = relacije;
		this.idLinije = idLinije;
		this.nazivLinije = nazivLinije;
		this.idStanice = idStanice;
	}
	
	public ArrayList<Relacija> getRelacije() {
		return relacije;
	}
	public void setRelacije(ArrayList<Relacija> relacije) {
		this.relacije = relacije;
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
	
	public boolean dodajRelaciju(Relacija relacija) {
		return relacije.add(relacija);
	}
	
	public boolean ukloniRelaciju(Relacija relacija) {
		return relacije.remove(relacija);
	}

	@Override
	public String toString() {
		return "Linija [relacije=" + relacije + ", idLinije=" + idLinije + ", nazivLinije=" + nazivLinije
				+ ", idStanice=" + idStanice + "]";
	}
	
	
}

