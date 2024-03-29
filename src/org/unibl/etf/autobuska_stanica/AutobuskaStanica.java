package org.unibl.etf.autobuska_stanica;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.unibl.etf.prijava.PrijavaController;
import org.unibl.etf.util.Util;

public class AutobuskaStanica {
	private String jib;
	private String naziv;
	private String ulicaIBroj;
	private int postanskiBroj;
	private String grad;
	private String brojTelefona;
	private int brojPerona;
	private String webStranica;
	private String email;
	private int idStajalista;
	private String stanje;
	
	public AutobuskaStanica() {
		super();
	}
	
	public AutobuskaStanica(String jib, String naziv, String ulicaIBroj, int postanskiBroj, String grad, String brojTelefona, int brojPerona, String webStranica, String email, int idStajalista, String stanje) {
		super();
		this.jib= jib;
		this.naziv = naziv;
		this.ulicaIBroj = ulicaIBroj;
		this.postanskiBroj = postanskiBroj;
		this.grad = grad;
		this.brojTelefona = brojTelefona;
		this.brojPerona = brojPerona;
		this.webStranica = webStranica;
		this.email=email;
		this.idStajalista=idStajalista;
		this.stanje=stanje;
	}
	
	public String getJib() {
		return jib;
	}

	public void setJib(String jib) {
		this.jib = jib;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getUlicaIBroj() {
		return ulicaIBroj;
	}

	public void setUlicaIBroj(String ulicaIBroj) {
		this.ulicaIBroj = ulicaIBroj;
	}

	public int getPostanskiBroj() {
		return postanskiBroj;
	}

	public void setPostanskiBroj(int postanskiBroj) {
		this.postanskiBroj = postanskiBroj;
	}

	public String getGrad() {
		return grad;
	}

	public void setGrad(String grad) {
		this.grad = grad;
	}

	public String getBrojTelefona() {
		return brojTelefona;
	}

	public void setBrojTelefona(String brojTelefona) {
		this.brojTelefona = brojTelefona;
	}

	public int getBrojPerona() {
		return brojPerona;
	}

	public void setBrojPerona(int brojPerona) {
		this.brojPerona = brojPerona;
	}
	
	public String getWebStranica() {
		return webStranica;
	}

	public void setWebStranica(String webStranica) {
		this.webStranica = webStranica;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getIdStajalista() {
		return idStajalista;
	}

	public void setIdStajalista(int idStajalista) {
		this.idStajalista = idStajalista;
	}

	public String getStanje() {
		return stanje;
	}

	public void setStanje(String stanje) {
		this.stanje = stanje;
	}

	public String getAdresa() {
		return ulicaIBroj + ", " + postanskiBroj + " " + grad;
	}
/*
	@Override
	public String toString() {
		return "AutobuskaStanica [jib=" + jib + ", naziv=" + naziv + ", adresa=" + ulicaIBroj + ", "
				+ postanskiBroj + " " + grad + ", brojTelefona=" + brojTelefona + ", brojPerona=" + brojPerona
				+ ", webStranica=" + webStranica + ", email=" + email + "]";
	}
*/
	@Override
	public String toString() {
		return "AutobuskaStanica [jib=" + jib + ", naziv=" + naziv + ", ulicaIBroj=" + ulicaIBroj + ", postanskiBroj="
				+ postanskiBroj + ", grad=" + grad + ", brojTelefona=" + brojTelefona + ", brojPerona=" + brojPerona
				+ ", webStranica=" + webStranica + ", email=" + email + ", idStajalista=" + idStajalista + ", stanje="
				+ stanje + "]";
	}
	
	public static List<AutobuskaStanica> listaStanica() {
		List<AutobuskaStanica> listaAutobuskihStanica = new ArrayList<>();
		Connection c = null;
		CallableStatement s = null;
		ResultSet r = null;
	    try {
	       	c=Util.getConnection();
	    	s = c.prepareCall("{call showBusStations()}");
	       	r = s.executeQuery();
	        while(r.next()) {
		       	listaAutobuskihStanica.add(new AutobuskaStanica(r.getString("JIBStanice"), r.getString("Naziv"), r.getString("Adresa"), r.getInt("PostanskiBroj"), r.getString("mjesto.naziv"), r.getString("BrojTelefona"), r.getInt("BrojPerona"), r.getString("WebStranica"), r.getString("Email"), r.getInt("IdStajalista"), r.getString("Stanje")));
	        }
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(r,s,c);
	    }
	    return listaAutobuskihStanica;
	}

	public static AutobuskaStanica getAutobuskaStanica(String jib) {
		AutobuskaStanica autobuskaStanica=null;
	    String sql="select JIBStanice,autobuska_stanica.Naziv,Adresa,autobuska_stanica.PostanskiBroj,mjesto.Naziv,BrojTelefona,BrojPerona,WebStranica,IdStajalista,Email,Stanje from autobuska_stanica join mjesto on (autobuska_stanica.PostanskiBroj=mjesto.PostanskiBroj) where Stanje!='Izbrisano' and JIBStanice=?;";
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
	    try {
	       	c=Util.getConnection();
	    	s = Util.prepareStatement(c, sql, false, jib);
	    	r = s.executeQuery();
	       	if(r.next()) {
	       		autobuskaStanica=new AutobuskaStanica(r.getString("JIBStanice"), r.getString("Naziv"), r.getString("Adresa"), r.getInt("PostanskiBroj"), r.getString("mjesto.naziv"), r.getString("BrojTelefona"), r.getInt("BrojPerona"), r.getString("WebStranica"), r.getString("Email"), r.getInt("IdStajalista"), r.getString("Stanje"));
	       	}
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(s,c);
	    }
	    return autobuskaStanica;
	}
	
	public static boolean dodavanjeAutobuskeStanice(String jib, String naziv, String adresa, int postanskiBroj, String brojTelefona, int brojPerona, String webStranica, String email) {
		Connection c = null;
		CallableStatement s = null;
		ResultSet r = null;
	    try {
	       	c=Util.getConnection();
	    	s = c.prepareCall("{call addBusStation(?,?,?,?,?,?,?,?)}");
	    	s.setString(1, jib);
	       	s.setString(2, naziv);
	       	s.setString(3, adresa);
	       	s.setInt(4, postanskiBroj);
	       	s.setString(5, brojTelefona);
	       	s.setInt(6, brojPerona);
	       	s.setString(7, webStranica);
	       	s.setString(8, email);
	       	r = s.executeQuery();
	       	return true;
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(r,s,c);
	    }
	    return false;
	}
	
	public static boolean brisanjeAutobuskeStanice(String jib) {
		
		Connection c = null;
		CallableStatement s = null;
	    try {
	       	c=Util.getConnection();
	    	s = c.prepareCall("{call removeBusStation(?)}");
	    	s.setString(1, jib);
	       	s.execute();
	       	return true;
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(s,c);
	    }
	    return false;
	    /*
		String sql="update autobuska_stanica, nalog, zaposleni set autobuska_stanica.Stanje=?, nalog.Stanje=?, zaposleni.Stanje=? where autobuska_stanica.JIBStanice=? and nalog.JIBStanice=autobuska_stanica.JIBStanice and nalog.JMBG=zaposleni.JMBG;";
		Connection c = null;
		PreparedStatement s = null;
	    try {
	       	c=Util.getConnection();
	    	s = Util.prepareStatement(c, sql, false, "Izbrisano", "Izbrisano", "Izbrisano", jib);
	       	s.execute();
	       	return true;
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(s,c);
	    }
	    return false;
	    */
	}
	
	public static boolean blokiranjeAutobuskeStanice(String jib, String stanje) {
		/*
		//String sql="update autobuska_stanica set Stanje=? where JIBStanice=?;";
		//String sql="update autobuska_stanica as a, nalog as n, zaposleni as z set a.Stanje=?, n.Stanje=?, z.Stanje=? where a.JIBStanice=? and a.JIBStanice=n.JIBStanice and n.JMBG=z.JMBG;";
		String sql="update autobuska_stanica, nalog, zaposleni set autobuska_stanica.Stanje=?, nalog.Stanje=?, zaposleni.Stanje=? where autobuska_stanica.JIBStanice=? and nalog.JIBStanice=autobuska_stanica.JIBStanice and nalog.JMBG=zaposleni.JMBG;";
		Connection c = null;
		PreparedStatement s = null;
	    try {
	       	c=Util.getConnection();
	    	s = Util.prepareStatement(c, sql, false, stanje, stanje, stanje, jib);
	       	s.execute();
	       	return true;
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(s,c);
	    }
	    return false;
	    */
		Connection c = null;
		CallableStatement s = null;
	    try {
	       	c=Util.getConnection();
	    	s = c.prepareCall("{call blockBusStation(?, ?)}");
	    	s.setString(1, jib);
	    	s.setString(2, stanje);
	       	s.execute();
	       	return true;
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(s,c);
	    }
	    return false;
	}
	
	public static boolean izmjenaAutobuskeStanice(String jib, String naziv, String adresa, int brojPerona, String brojTelefona, String webStranica, String email, int idStajalista) {
		String sql="update autobuska_stanica, autobusko_stajaliste set autobuska_stanica.Naziv=?, autobusko_stajaliste.Naziv=?, Adresa=?, BrojPerona=?, BrojTelefona=?, WebStranica=?, Email=? where JIBStanice=? and autobusko_stajaliste.IdStajalista=?;";
		Connection c = null;
		PreparedStatement s = null;
	    try {
	       	c=Util.getConnection();
	    	s = Util.prepareStatement(c, sql, false, naziv, naziv, adresa, brojPerona, brojTelefona, webStranica, email, jib, idStajalista);
	       	s.execute();
	       	return true;
	    } catch(SQLException e) {
	    	Util.LOGGER.log(Level.SEVERE, e.toString(), e);
	    } finally {
	    	Util.close(s,c);
	    }
	    return false;
	}
	
	public static int getBrojPeronaStanice() {
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		String sql = "select BrojPerona from autobuska_stanica where JIBStanice=?";
		try {
			c = Util.getConnection();
			s = c.prepareStatement(sql);
			s.setString(1, PrijavaController.nalog.getIdStanice());
			r = s.executeQuery();
			if(r.next()) 
				return r.getInt(1);
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		finally {
			Util.close(r, s, c);
		}
		return 0;
	}
	
	private static List<String> jibList = new ArrayList<>();
	
	public static List<String> getJibList() {
		return jibList;
	}
	/*
	public static void setJibList(List<String> jibList) {
		AutobuskaStanica.jibList = jibList;
	}
	*/
	
	public static void loadJibs() {
		getJibList().clear();
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, "select JIBStanice from autobuska_stanica", false);
			r = s.executeQuery();
			while(r.next()) {
				getJibList().add(r.getString(1));
			}
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
	
	private static List<String> activeJibList = new ArrayList<>();
	
	public static List<String> getActiveJibList() {
		return activeJibList;
	}
	
	public static void loadActiveJibs() {
		getActiveJibList().clear();
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, "select JIBStanice from autobuska_stanica where Stanje='Aktivno'", false);
			r = s.executeQuery();
			while(r.next()) {
				getActiveJibList().add(r.getString(1));
			}
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
	
private static List<String> stajalistaList = new ArrayList<>();
	
	public static List<String> getStajalistaList() {
		return stajalistaList;
	}
	
	public static void loadStajalista() {
		getStajalistaList().clear();
		Connection c = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try {
			c = Util.getConnection();
			s = Util.prepareStatement(c, "select IdStajalista from autobusko_stajaliste", false);
			r = s.executeQuery();
			while(r.next()) {
				getStajalistaList().add(r.getString(1));
			}
		} catch (SQLException e) {
			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
	
	
}
