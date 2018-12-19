package org.unibl.etf.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Mjesto {
	int postanskiBroj;
	String naziv;
	
	public Mjesto() {
		super();
	}
	
	public Mjesto(int postanskiBroj, String naziv) {
		super();
		this.postanskiBroj = postanskiBroj;
		this.naziv = naziv;
	}
	
	public int getPostanskiBroj() {
		return postanskiBroj;
	}
	
	public void setPostanskiBroj(int postanskiBroj) {
		this.postanskiBroj = postanskiBroj;
	}
	
	public String getNaziv() {
		return naziv;
	}
	
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	
	@Override
	public String toString() {
		return "Mjesto [postanskiBroj=" + postanskiBroj + ", naziv=" + naziv + "]";
	}
	
	private static List<String> postalCodeList = new ArrayList<>();
	
	public static List<String> getPostalCodeList() {
		return postalCodeList;
	}
	/*
	public static void setPostalCodeList(List<String> postalCodeList) {
		Util.postalCodeList = postalCodeList;
	}
	*/
/*
	public static void loadPostalCodes() {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, "select PostanskiBroj from mjesto", false);
			r = s.executeQuery();
			while(r.next())
				getPostalCodeList().add(String.valueOf(r.getInt(1)));
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
*/
	private static List<String> cityList = new ArrayList<>();
	
	public static List<String> getCityList() {
		return cityList;
	}
/*
	public static void loadCities() {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, "select Naziv from mjesto", false);
			r = s.executeQuery();
			while(r.next())
				getCityList().add(r.getString(1));
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
*/
	private static List<String> cityPostalCodeList = new ArrayList<>();
	
	public static List<String> getCityPostalCodeList() {
		return cityPostalCodeList;
	}
	
	private static List<Mjesto> placeList = new ArrayList<>();
	
	public static List<Mjesto> getPlaceList() {
		return placeList;
	}
	
	public static void loadPlaces() {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, "select PostanskiBroj, Naziv from mjesto", false);
			r = s.executeQuery();
			while(r.next()) {
				int postanskiBroj=r.getInt(1);
				String naziv=r.getString(2);
				getPlaceList().add(new Mjesto(postanskiBroj, naziv));
				getPostalCodeList().add(String.valueOf(postanskiBroj));
				getCityList().add(naziv);
				getCityPostalCodeList().add(postanskiBroj + " - " + naziv);
			}
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
}
