package org.unibl.etf.karta;

import java.sql.Time;

public class Relacija {
	
	private int idLinije;
	private int idRelacije;
	private String polaziste;
	private String odrediste;
	private Time vrijemePolaska;
	private Time vrijemeDolaska;
	private double cijenaJednokratna;
	private double cijenaMjesecna;
	public Relacija() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public Relacija(int idLinije, int idRelacije, String polaziste, String odrediste, Time vrijemePolaska,
			Time vrijemeDolaska, double cijenaJednokratna, double cijenaMjesecna) {
		super();
		this.idLinije = idLinije;
		this.idRelacije = idRelacije;
		this.polaziste = polaziste;
		this.odrediste = odrediste;
		this.vrijemePolaska = vrijemePolaska;
		this.vrijemeDolaska = vrijemeDolaska;
		this.cijenaJednokratna = cijenaJednokratna;
		this.cijenaMjesecna = cijenaMjesecna;
	}


	public Relacija(int idRelacije,int idLinije,String polaziste, String odrediste) {
		super();
		this.idRelacije = idRelacije;
		this.idLinije = idLinije;
		this.polaziste = polaziste;
		this.odrediste = odrediste;
	}
	
	public int getIdLinije() {
		return idLinije;
	}
	public void setIdLinije(int idLinije) {
		this.idLinije = idLinije;
	}
	public int getIdRelacije() {
		return idRelacije;
	}
	public void setIdRelacije(int idRelacije) {
		this.idRelacije = idRelacije;
	}

	
	
	public String getPolaziste() {
		return polaziste;
	}
	public void setPolaziste(String polaziste) {
		this.polaziste = polaziste;
	}
	public String getOdrediste() {
		return odrediste;
	}
	public void setOdrediste(String odrediste) {
		this.odrediste = odrediste;
	}
	@Override
	public String toString() {
		return polaziste + " - " + odrediste;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idRelacije;
		return result;
	}
	
	public Time getVrijemePolaska() {
		return vrijemePolaska;
	}


	public void setVrijemePolaska(Time vrijemePolaska) {
		this.vrijemePolaska = vrijemePolaska;
	}


	public Time getVrijemeDolaska() {
		return vrijemeDolaska;
	}


	public void setVrijemeDolaska(Time vrijemeDolaska) {
		this.vrijemeDolaska = vrijemeDolaska;
	}


	public double getCijenaJednokratna() {
		return cijenaJednokratna;
	}


	public void setCijenaJednokratna(double cijenaJednokratna) {
		this.cijenaJednokratna = cijenaJednokratna;
	}


	public double getCijenaMjesecna() {
		return cijenaMjesecna;
	}


	public void setCijenaMjesecna(double cijenaMjesecna) {
		this.cijenaMjesecna = cijenaMjesecna;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Relacija other = (Relacija) obj;
		if (idRelacije != other.idRelacije)
			return false;
		return true;
	}

	
	
	
}
