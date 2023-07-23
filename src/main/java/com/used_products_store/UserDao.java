package com.used_products_store;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
	public class UserDao {
	public String getAllUsers() throws SQLException {
			Utility.connect();
			String query = "SELECT user_name, first_name, last_name, email, phone_number, is_admin, rate FROM users";
			PreparedStatement ps = Utility.conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			JsonArray out = new JsonArray();
			
			while (rs.next()) {
				JsonObject temp = new JsonObject();
				temp.addProperty("user_name", rs.getString("user_name"));
				temp.addProperty("first_name", rs.getString("first_name"));
				temp.addProperty("last_name", rs.getString("last_name"));
				temp.addProperty("email", rs.getString("email"));
				temp.addProperty("phone_number", rs.getString("phone_number"));
				temp.addProperty("is_admin", rs.getString("is_admin"));
				temp.addProperty("rate", rs.getString("rate"));
				out.add(temp);
			}
			Utility.disconnect();
			return out.toString();
	}
	
	public String getUser(String user_name) throws SQLException {
		Utility.connect();
		String query = "SELECT user_name, first_name, last_name, email, phone_number, is_admin, rate FROM users WHERE user_name=?";
		PreparedStatement ps = Utility.conn.prepareStatement(query);
		ps.setString(1, user_name);
		ResultSet rs = ps.executeQuery();
		rs.next();
		JsonObject temp = new JsonObject();
		temp.addProperty("user_name", rs.getString("user_name"));
		temp.addProperty("first_name", rs.getString("first_name"));
		temp.addProperty("last_name", rs.getString("last_name"));
		temp.addProperty("email", rs.getString("email"));
		temp.addProperty("phone_number", rs.getString("phone_number"));
		temp.addProperty("is_admin", rs.getString("is_admin"));
		temp.addProperty("rate", rs.getString("rate"));
		Utility.disconnect();
		return temp.toString();
	
	}
	
	public String register(Map<String, String> hmap) throws SQLException {
		Utility.connect();
		String q = "SELECT COUNT(*) FROM users WHERE user_name=?";
		PreparedStatement prs = Utility.conn.prepareStatement(q);
		prs.setString(1, hmap.get("user_name"));
		ResultSet res = prs.executeQuery();
		res.next();
		if(res.getInt(1)>0) {
			throw new SQLException("user name already exists");
		}
		String query = "INSERT INTO users (user_name, first_name, last_name, email, password, phone_number, is_admin) VALUES(?,?,?,?,?,?,?)";
		PreparedStatement ps = Utility.conn.prepareStatement(query);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String password = encoder.encode(hmap.get("password"));
		ps.setString(1, hmap.get("user_name"));
		ps.setString(2, hmap.get("first_name"));
		ps.setString(3, hmap.get("last_name"));
		ps.setString(4, hmap.get("email"));
		ps.setString(5, password);
		ps.setString(6, hmap.get("phone_number"));
		ps.setString(7, hmap.get("is_admin"));
		ps.executeUpdate();
		return getUser(hmap.get("user_name"));
	
}
	
	public String updateUser(Map<String, String> hmap) throws SQLException {
		Utility.connect();
		String query = "";
		StringBuilder builder = new StringBuilder("UPDATE users SET ");
		for (Map.Entry<String,String> entry : hmap.entrySet()) {
			if(entry.getKey().equals("id")) continue;
			builder.append(entry.getKey() + "= '" + entry.getValue() + "',");
		}
		builder.deleteCharAt(builder.length()-1);
		builder.append(" WHERE user_name= '" + hmap.get("id")+"'");
		query = builder.toString();
		System.out.println(query);
		PreparedStatement ps = Utility.conn.prepareStatement(query);
		
		int i = ps.executeUpdate();
		Utility.disconnect();
		if(i<1) throw new SQLException();
		return getUser(hmap.get("id"));	
	}

	public String deleteUser(Map<String, String> hmap) throws SQLException {
		Utility.connect();
		String query = "DELETE FROM users WHERE user_name=?";
		PreparedStatement ps = Utility.conn.prepareStatement(query);
		ps.setString(1, hmap.get("user_name"));
		int i = ps.executeUpdate();
		Utility.disconnect();
		if (i<1) throw new SQLException();
		return i + "successfully";

	}

	public String login(Map<String, String> hmap) throws SQLException{
		Utility.connect();
		String q = "SELECT COUNT(*), password FROM users WHERE user_name=?";
		PreparedStatement prs = Utility.conn.prepareStatement(q);
		prs.setString(1, hmap.get("user_name"));
		ResultSet res = prs.executeQuery();
		res.next();
		if(res.getInt(1)>0) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			if(encoder.matches(hmap.get("password"), res.getString(2))) {
				return getUser(hmap.get("user_name"));
		}
			else {
				throw new SQLException("Unauthorized");
			}
			}
		else {
			throw new SQLException("No account found!");
		}
	}
	
//	private String generateString() {
//        String uuid = UUID.randomUUID().toString();
//        return uuid;
//    }

	public String resetPassword(Map<String, String> hmap) throws SQLException {
		Utility.connect();
		String query = "UPDATE users SET password = ? WHERE user_name=?";
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String password = encoder.encode(hmap.get("password"));
		PreparedStatement ps = Utility.conn.prepareStatement(query);
		ps.setString(1, password);
		ps.setString(2, hmap.get("user_name"));
		int i = ps.executeUpdate();
		Utility.disconnect();
		if (i<1) throw new SQLException();
		return getUser(hmap.get("user_name"));

	}
}















