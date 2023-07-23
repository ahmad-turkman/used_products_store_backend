package com.used_products_store;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
public class CategoryDao {

	public String getallcategories() throws SQLException {
		Utility.connect();
		String query = "SELECT category_id,name FROM categories";
		PreparedStatement ps = Utility.conn.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		JsonArray out = new JsonArray();

		while (rs.next()) {
			JsonObject temp = new JsonObject();
			temp.addProperty("category_id", rs.getString("category_id"));
			temp.addProperty("name", rs.getString("name"));
			out.add(temp);
		}
		Utility.disconnect();
		return out.toString();
	}

	public String getCategory(String id) throws SQLException {
		Utility.connect();
		String query = "SELECT category_id, name FROM categories WHERE category_id = ?";
		PreparedStatement ps = Utility.conn.prepareStatement(query);
		ps.setString(1, id);
		ResultSet rs = ps.executeQuery();
		rs.next();
		JsonObject temp = new JsonObject();
		temp.addProperty("category_id", rs.getString("category_id"));
		temp.addProperty("name", rs.getString("name"));

		Utility.disconnect();
		return temp.toString();
	}

	public String addcategory(Map<String, String> hmap) throws SQLException {
		Utility.connect();
		String query = "INSERT INTO categories (name) VALUES(?)";
		PreparedStatement ps = Utility.conn.prepareStatement(query);
		ps.setString(1, hmap.get("name"));
		ps.executeUpdate();
		String query2 = "SELECT MAX(category_id) as id FROM categories";
		PreparedStatement ps2 = Utility.conn.prepareStatement(query2);
		ResultSet rs = ps2.executeQuery();
		rs.next();
		return getCategory(rs.getString("id"));

	}

	public String updatecategory(Map<String, String> hmap) throws SQLException {
		Utility.connect();
		String query = "UPDATE categories SET name=? WHERE category_id=?";
		PreparedStatement ps = Utility.conn.prepareStatement(query);
		ps.setString(1, hmap.get("name"));
		ps.setString(2, hmap.get("id"));
		int i = ps.executeUpdate();
		if (i < 0)
			throw new SQLException("Something Went Wrong");
		return getCategory(hmap.get("id"));

	}

	public String deletecategory(Map<String, String> hmap) throws SQLException {
		Utility.connect();
		String query = "DELETE FROM categories WHERE category_id=?";
		PreparedStatement ps = Utility.conn.prepareStatement(query);
		ps.setString(1, hmap.get("category_id"));
		int i = ps.executeUpdate();
		Utility.disconnect();
		if (i < 1)
			throw new SQLException();
		return i + "successfully";

	}

}
