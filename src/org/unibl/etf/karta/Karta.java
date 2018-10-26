package org.unibl.etf.karta;

public abstract class Karta {
	protected int idKarte;
	protected Prevoznik prevozink;
	protected Relacija relacija;
	
	public Karta() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Karta(int idKarte, Prevoznik prevozink, Relacija relacija) {
		super();
		this.idKarte = idKarte;
		this.prevozink = prevozink;
		this.relacija = relacija;
	}
	public int getIdKarte() {
		return idKarte;
	}
	public void setIdKarte(int idKarte) {
		this.idKarte = idKarte;
	}
	public Prevoznik getPrevozink() {
		return prevozink;
	}
	public void setPrevozink(Prevoznik prevozink) {
		this.prevozink = prevozink;
	}
	public Relacija getRelacija() {
		return relacija;
	}
	public void setRelacija(Relacija relacija) {
		this.relacija = relacija;
	}
	
	@Override
	public String toString() {
		return "Karta [idKarte=" + idKarte + ", prevozink=" + prevozink + ", relacija=" + relacija + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idKarte;
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
		Karta other = (Karta) obj;
		if (idKarte != other.idKarte)
			return false;
		return true;
	}
	
	
	
}
