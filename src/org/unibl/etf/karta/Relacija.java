package org.unibl.etf.karta;

public class Relacija {
	
	private int idLinije;
	private int idRelacije;
	private int polaziste;
	private int odrediste;
	
	public Relacija() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Relacija(int idRelacije,int idLinije,int polaziste, int odrediste) {
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
	public int getIshodiste() {
		return polaziste;
	}
	public void setIshodiste(int ishodiste) {
		this.polaziste = ishodiste;
	}
	public int getOdrediste() {
		return odrediste;
	}
	public void setOdrediste(int odrediste) {
		this.odrediste = odrediste;
	}
	
	@Override
	public String toString() {
		return "Relacija [idLinije=" + idLinije + ", idRelacije=" + idRelacije + ", polaziste=" + polaziste
				+ ", odrediste=" + odrediste + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idRelacije;
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
		Relacija other = (Relacija) obj;
		if (idRelacije != other.idRelacije)
			return false;
		return true;
	}

	
	
	
}
