package org.unibl.etf.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Praznik {
	int dan;
	int mjesec;
	String opis;
	
	public Praznik() {
		super();
	}
	
	public Praznik(int dan, int mjesec, String opis) {
		super();
		this.dan = dan;
		this.mjesec = mjesec;
		this.opis = opis;
	}
	
	public int getDan() {
		return dan;
	}
	
	public void setDan(int dan) {
		this.dan = dan;
	}
	
	public int getMjesec() {
		return mjesec;
	}
	
	public void setMjesec(int mjesec) {
		this.mjesec = mjesec;
	}
	
	public String getOpis() {
		return opis;
	}
	
	public void setOpis(String opis) {
		this.opis = opis;
	}
	
	@Override
	public String toString() {
		return "Praznik [dan=" + dan + ", mjesec=" + mjesec + ", opis=" + opis + "]";
	}
	
	private static List<Praznik> holidayList = new ArrayList<>();
	
	public static List<Praznik> getHolidayList() {
		return holidayList;
	}
	
	public static void loadHolidays() {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, "select Datum, Opis from praznik", false);
			r = s.executeQuery();
			while(r.next())
				getHolidayList().add(new Praznik(Integer.valueOf(r.getString(1).split("-")[1]), Integer.valueOf(r.getString(1).split("-")[0]), r.getString(2)));
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
}