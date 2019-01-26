package org.unibl.etf.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Praznik {
	int id;
	int dan;
	int mjesec;
	String opis;
	
	public Praznik() {
		super();
	}
	
	public Praznik(int id, int dan, int mjesec, String opis) {
		super();
		this.id =id;
		this.dan = dan;
		this.mjesec = mjesec;
		this.opis = opis;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
		//return "Praznik [dan=" + dan + ", mjesec=" + mjesec + ", opis=" + opis + "]";
		return String.format("%02d/%02d - %s", dan, mjesec, opis);
	}
	
	private static List<Praznik> holidayList = new ArrayList<>();
	
	public static List<Praznik> getHolidayList() {
		return holidayList;
	}
	
	public static void loadHolidays() {
		getHolidayList().clear();
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, "select IdPraznika, Datum, Opis from praznik", false);
			r = s.executeQuery();
			while(r.next()) {
				getHolidayList().add(new Praznik(r.getInt(1), Integer.valueOf(r.getString(2).split("-")[1]), Integer.valueOf(r.getString(2).split("-")[0]), r.getString(3)));
			}
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
	
	public static boolean dodavanjePraznika(int dan, int mjesec, String opis) {
		String sql="insert into praznik set Datum=?, Opis=?;";
		Connection c = null;
		PreparedStatement s = null;
	    try {
	       	c=Util.getConnection();
	    	s = Util.prepareStatement(c, sql, false, mjesec + "-" + dan, opis);
	       	s.execute();
	       	return true;
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(s,c);
	    }
	    return false;
	}
	
	public static boolean izmjenaPraznika(int id, int dan, int mjesec, String opis) {
		System.out.println(id + " " + dan + " " + mjesec + " " + opis );
		String sql="update praznik set Datum=?, Opis=? where IdPraznika=?;";
		Connection c = null;
		PreparedStatement s = null;
	    try {
	       	c=Util.getConnection();
	    	s = Util.prepareStatement(c, sql, false, mjesec + "-" + dan, opis, id);
	       	s.execute();
	       	return true;
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(s,c);
	    }
	    return false;
	}
	
	public static boolean brisanjePraznika(int id) {
		String sql="delete from praznik where IdPraznika=?;";
		Connection c = null;
		PreparedStatement s = null;
	    try {
	       	c=Util.getConnection();
	    	s = Util.prepareStatement(c, sql, false, id);
	       	s.execute();
	       	return true;
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(s,c);
	    }
	    return false;
	}
}