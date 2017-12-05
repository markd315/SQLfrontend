package JDBCSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
/**
 * 
 * 
 * @author markd315
 *	This defines the API for database calls.
 */
public class DatabaseAPI {
	
	private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:db/a4.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
	public String[] listSightings(String queryName) {
    	String sql = "SELECT * FROM sightings WHERE \"" + queryName + "\"=name ORDER BY sighted desc LIMIT 10";
        ArrayList<String> resList = new ArrayList<String>();
    	
        try (Connection conn = this.connect();
            	Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                resList.add(rs.getString("name") +  "\t" + 
                                   rs.getString("person") + "\t" +
                                   rs.getString("location") + "\t" /*+
                                   rs.getDate("sighted")*/); //Commented date out because I was getting "error parsing time stamp"
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        String[] arr = (String[]) resList.toArray(new String[resList.size()]);
    	return arr;
    }
	public String[] listFlowers() {
    	String sql = "SELECT comname FROM flowers;";
        ArrayList<String> resList = new ArrayList<String>();
    	
        try (Connection conn = this.connect();
        	Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                resList.add(rs.getString("comname"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        String[] arr = resList.toArray(new String[resList.size()]);
    	return arr;
	}
	public String insert(String name, String person, String location, String date) {
    	String sql = "INSERT INTO sightings(name,person,location,sighted) VALUES(?,?,?,?)";
    	 
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, person);
            pstmt.setString(3, location);
            pstmt.setString(4, date);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    	return "Inserted item: " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
    }
	public String update(String genus, String species, String comname, String comnameToUpdate) {
    	String sql = "UPDATE flowers SET genus = ? , "
                + "species = ? ,"
    			+ "comname = ?"
                + "WHERE comname = ?";
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            // set the corresponding param
            pstmt.setString(1, genus);
            pstmt.setString(2, species);
            pstmt.setString(3, comname);
            pstmt.setString(4, comnameToUpdate);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    	return "Updated item: " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
    }
}
