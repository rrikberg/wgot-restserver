package wgot;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class DBHandler {
	private Connection conn;
	private DataSource ds;
	
	public DBHandler(){
		try {
	        Context initContext = new InitialContext();
	        Context envContext = (Context) initContext.lookup("java:comp/env");
	        this.ds = (DataSource) envContext.lookup("jdbc/WGOT");
	  
	    } catch (NamingException e) {
	        System.err.println(e);
	    }
	}
	
	//return -1 if failed, return id of added event if success
	public int createEvent(Event ev) {
		int addedEventId = 0;
        try {
			conn = ds.getConnection();
			
			String sql = "INSERT INTO WGOT.Event " +
						"(title,promoter,location,entranceFee,startDate,endDate,category) " +
						"VALUES (?,?,?,?,?,?,?)";
			
			PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, ev.getTitle());
			ps.setInt(2, ev.getIdPromoter());
			ps.setInt(3, ev.getIdLocation());
			ps.setFloat(4, ev.getEntranceFee());
			ps.setTimestamp(5, ev.getStartDate());
			ps.setTimestamp(6, ev.getEndDate());
			ps.setInt(7, ev.getIdCategory());
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()){
				addedEventId = rs.getInt(1);
			}
			
			conn.close();

        } catch (SQLException e) {
        	System.out.println(e);
        	return -1;
        }
        return addedEventId;
	}
	
	public String createUser(User u){
		try {
			//first check if the username is already used by someone
			if (usernameInDB(u.getUsername()) == true){
				return "username is already being used, please choose a different one";
			}
			
			conn = ds.getConnection();
			
			String sql = "INSERT INTO WGOT.User " +
						"(firstname,lastname,username,email,password,idUserType)"+
						"VALUES (?,?,?,?,?,?)";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, u.getFirstname());
			ps.setString(2, u.getLastname());
			ps.setString(3, u.getUsername());
			ps.setString(4, u.getEmail());
			ps.setString(5, encryptPassword(u.getPassword()));
			ps.setInt(6, u.getIdUserType());
			ps.executeUpdate();
			
			conn.close();

		} catch (SQLException e) {
        	return "something went wrong: " + e.toString();
        	
        }
		return "user added";
	}
	
	private static String encryptPassword(String password)
	{
	    String sha1 = "";
	    try
	    {
	        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	        crypt.reset();
	        crypt.update(password.getBytes("UTF-8"));
	        sha1 = byteToHex(crypt.digest());
	    }
	    catch(NoSuchAlgorithmException e)
	    {
	        e.printStackTrace();
	    }
	    catch(UnsupportedEncodingException e)
	    {
	        e.printStackTrace();
	    }
	    return sha1;
	}

	private static String byteToHex(final byte[] hash)
	{
	    Formatter formatter = new Formatter();
	    for (byte b : hash)
	    {
	        formatter.format("%02x", b);
	    }
	    String result = formatter.toString();
	    formatter.close();
	    return result;
	}
	
	public boolean verifyPassword(String username, String password){
		boolean isPasswordCorrect = false;
		
		try{
			String encPassword = encryptPassword(password); 
			
			conn = ds.getConnection();
			String sql = "SELECT Password FROM User WHERE username = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			rs.next();
			if(rs.getString("password").equals(encPassword)){
				isPasswordCorrect = true;
			}
			conn.close();
		} catch (SQLException e){
			return false;
		}
		return isPasswordCorrect;
	}
	
	public Event getEventById(int id) {
		Event ev = null;
        try {
			conn = ds.getConnection();
			
			String sql = "SELECT * FROM Event WHERE idEvent = ?";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			ev = new Event(rs.getInt("idEvent"),
								rs.getString("title"),
								rs.getInt("promoter"),
								rs.getInt("location"),
								rs.getFloat("entranceFee"),
								rs.getTimestamp("startDate"),
								rs.getTimestamp("endDate"),
								rs.getInt("category"));
			
			conn.close();

	    } catch (SQLException e) {
	    	System.err.println(e);
	    } catch (ParseException e){
	    	System.err.println(e);
	    }
		return ev;
	}

	public List<Event> getEventList(List<Integer> catList, String searchPhrase) {
		List<Event> events = new ArrayList<Event>();
		try {
			conn = ds.getConnection();
			
			String sql = "SELECT * FROM Event";
					

			//modify query if we're filtering some events
			String searchContains = null;
			
			if (!catList.isEmpty() || "".equals(searchPhrase) == false && searchPhrase != null){
				sql += " WHERE";
			}
			if (!catList.isEmpty()){
				StringBuilder builder = new StringBuilder();
				
				for ( int i = 0; i <catList.size(); i++){
					builder.append("?,");
				}
				
				sql += " category in (" + builder.deleteCharAt( builder.length()-1).toString() + ")";

			}
			if (!catList.isEmpty() && "".equals(searchPhrase) == false && searchPhrase != null){
				sql += " AND";
			}
			if ("".equals(searchPhrase) == false && searchPhrase != null){
				sql += " title LIKE ?";
				//add % characters to search phrase to get events that containing the search phrase in their title
				searchContains = "%" + searchPhrase + "%";
			}
			
			PreparedStatement ps = conn.prepareStatement(sql);
			int psIndex = 1;
			
			if (!catList.isEmpty()){

				for(Integer cat : catList) {
					   ps.setInt(psIndex++, cat);
					}
			}
			if ("".equals(searchPhrase) == false && searchPhrase != null){
				ps.setString(psIndex, searchContains);
			}
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Event ev = new Event(rs.getInt("idEvent"),
									rs.getString("title"),
									rs.getInt("promoter"),
									rs.getInt("location"),
									rs.getFloat("entranceFee"),
									rs.getTimestamp("startDate"),
									rs.getTimestamp("endDate"),
									rs.getInt("category"));
				events.add(ev);
			}
			
			conn.close();

	    } catch (SQLException e) {
	    	System.err.println(e);
	    } catch (ParseException e){
	    	System.err.println(e);
	    }
		return events;
	}
	
	public boolean usernameInDB(String subject) {
		boolean isNameInDB = false;
		
		try{
			conn = ds.getConnection();
			
			String sql = "SELECT username FROM User WHERE username = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, subject);
			
			ResultSet rs = ps.executeQuery();
			if (rs.next() != false){
				return true;
			}
			
			conn.close();
		}catch (SQLException e){
	    	System.err.println(e);
		}
		return isNameInDB;
	}
	
	public int createLocation(Location loc, int idPromo) {
		int addedLocationId = 0;
        try {
			conn = ds.getConnection();
			
			String sql = "INSERT INTO WGOT.Location " +
						"(name, city, zipCode, address) " +
						"VALUES (?,?,?,?)";
			
			PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, loc.getName());
			ps.setString(2, loc.getCity());
			ps.setInt(3, loc.getZipCode());
			ps.setString(4, loc.getAddress());
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()){
				addedLocationId = rs.getInt(1);
			}
			// add location/promoter id pair to db
			if (addedLocationId != 0){
				sql = "INSERT INTO WGOT.Promoter (idPromoter, idLocation) VALUES (?,?)";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, idPromo);
				ps.setInt(2, addedLocationId);
				ps.executeUpdate();
			}
			
			conn.close();

        } catch (SQLException e) {
        	System.out.println(e);
        	return -1;
        }
        return addedLocationId;
	}
	public List<Location> getLocationsByPromoter(int idPromo) {
		List<Location> locations = new ArrayList<Location>();
        try {
			conn = ds.getConnection();
			
			//first find the ids of the locations belonging to the promoter we're interested in from the Promoter table
			String sql = "SELECT idLocation FROM Promoter WHERE idPromoter = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, idPromo);

			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				// then get location info from the Location table
				String sql2 = "SELECT * FROM Location WHERE idLocation = ?";
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				ps2.setInt(1, rs.getInt("idLocation"));
				ResultSet rs2 = ps2.executeQuery();
				// loop over locations and add them to the list
				while (rs2.next()){
					Location loc = new Location(rs2.getInt("idLocation"),
												rs2.getString("name"),
												rs2.getString("city"),
												rs2.getInt("zipCode"),
												rs2.getString("address"));
					locations.add(loc);
				}
			}
			
			conn.close();

	    } catch (SQLException e) {
	    	System.err.println(e);
	    }
		return locations;
	}
	public List<Location> getAllLocations() {
		List<Location> locations = new ArrayList<Location>();
        try {
			conn = ds.getConnection();
			
			String sql = "SELECT * FROM Location";
			PreparedStatement ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			// loop over locations and add them to the list
			while (rs.next()){
				Location loc = new Location(rs.getInt("idLocation"),
											rs.getString("name"),
											rs.getString("city"),
											rs.getInt("zipCode"),
											rs.getString("address"));
				locations.add(loc);
			}

			conn.close();

	    } catch (SQLException e) {
	    	System.err.println(e);
	    }
		return locations;
	}
	public Location getLocationById(int id) {
		Location loc = null;
        try {
			conn = ds.getConnection();
			
			String sql = "SELECT * FROM Location WHERE idLocation = ?";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			loc = new Location(rs.getInt("idLocation"),
								rs.getString("name"),
								rs.getString("city"),
								rs.getInt("zipCode"),
								rs.getString("address"));
			
			conn.close();

	    } catch (SQLException e) {
	    	System.err.println(e);
	    }
		return loc;
	}

	public User getUserByName(String username) {
		User user = null;
        try {
			conn = ds.getConnection();
			
			String sql = "SELECT * FROM User WHERE username = ?";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			user = new User(rs.getInt("idUser"),
							rs.getString("firstname"),
							rs.getString("lastname"),
							rs.getString("username"),
							rs.getString("email"),
							null,
							rs.getInt("idUserType"));
			
			conn.close();

	    } catch (SQLException e) {
	    	System.err.println(e);
	    } 
		return user;
	}
	
}