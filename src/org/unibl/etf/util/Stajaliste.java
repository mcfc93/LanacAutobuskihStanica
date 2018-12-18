package org.unibl.etf.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Stajaliste {
	String naziv;

	public Stajaliste() {
		super();
	}

	public Stajaliste(String naziv) {
		super();
		this.naziv = naziv;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	@Override
	public String toString() {
		return "Stajaliste [naziv=" + naziv + "]";
	}
	
private static List<String> stajalisteList = new ArrayList<>();
	
	public static List<String> getStajalisteList() {
		return stajalisteList;
	}
	
	public static void loadStajalista() {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, "select Naziv from autobusko_stajaliste", false);
			r = s.executeQuery();
			while(r.next())
				getStajalisteList().add(r.getString(1));
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
}
