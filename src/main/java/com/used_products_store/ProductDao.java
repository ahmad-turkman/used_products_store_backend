package com.used_products_store;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
public class ProductDao {

	public String getAllProducts() throws SQLException {
		Utility.connect();
		String query = "SELECT p.product_id, p.name, description, c.name , user_name, price, quality, details, accepted, image, upload_date FROM products p, categories c ";
		query += "WHERE p.category_id=c.category_id";
		PreparedStatement ps = Utility.conn.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		JsonArray out = new JsonArray();

		while (rs.next()) {
			JsonObject temp = new JsonObject();
			temp.addProperty("product_id", rs.getString("p.product_id"));
			temp.addProperty("name", rs.getString("p.name"));
			temp.addProperty("description", rs.getString("description"));
			temp.addProperty("category", rs.getString("c.name"));
			temp.addProperty("user_name", rs.getString("user_name"));
			temp.addProperty("price", rs.getString("price"));
			temp.addProperty("quality", rs.getString("quality"));
			temp.addProperty("details", rs.getString("details"));
			temp.addProperty("accepted", rs.getString("accepted"));
			temp.addProperty("upload_date", rs.getString("upload_date"));
			if (rs.getBlob("image") != null) {
				Blob blob = rs.getBlob("image");
				byte[] input = blob.getBytes(1, (int) blob.length());
				String s = Base64.getEncoder().withoutPadding().encodeToString(input);
				temp.addProperty("image", s);
			}
			out.add(temp);
		}
		Utility.disconnect();
		return out.toString();
	}

	public String getProductByName(String name) throws SQLException {
		Utility.connect();
		String query = "SELECT p.product_id, p.name, description, c.name , user_name, price, quality, details, accepted FROM products p, categories c ";
		query += "WHERE p.category_id=c.category_id and p.name = ?";
		PreparedStatement ps = Utility.conn.prepareStatement(query);
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
		rs.next();
		JsonObject temp = new JsonObject();
		temp.addProperty("product_id", rs.getString("p.product_id"));
		temp.addProperty("name", rs.getString("p.name"));
		temp.addProperty("description", rs.getString("description"));
		temp.addProperty("category", rs.getString("c.name"));
		temp.addProperty("user_name", rs.getString("user_name"));
		temp.addProperty("price", rs.getString("price"));
		temp.addProperty("quality", rs.getString("quality"));
		temp.addProperty("details", rs.getString("details"));
		temp.addProperty("accepted", rs.getString("accepted"));
		Utility.disconnect();
		return temp.toString();
	}

	public String getProductById(String id) throws SQLException {
		Utility.connect();
		String query = "SELECT p.product_id, p.name, description, c.name , user_name, price, quality, details, accepted FROM products p, categories c ";
		query += "WHERE p.category_id=c.category_id and p.product_id= ?";
		PreparedStatement ps = Utility.conn.prepareStatement(query);
		ps.setString(1, id);
		ResultSet rs = ps.executeQuery();
		rs.next();
		JsonObject temp = new JsonObject();
		temp.addProperty("product_id", rs.getString("p.product_id"));
		temp.addProperty("name", rs.getString("p.name"));
		temp.addProperty("description", rs.getString("description"));
		temp.addProperty("category", rs.getString("c.name"));
		temp.addProperty("user_name", rs.getString("user_name"));
		temp.addProperty("price", rs.getString("price"));
		temp.addProperty("quality", rs.getString("quality"));
		temp.addProperty("details", rs.getString("details"));
		temp.addProperty("accepted", rs.getString("accepted"));
		Utility.disconnect();
		return temp.toString();
	}

	public String getCategoryProducts(String id) throws SQLException {
		Utility.connect();
		String query = "SELECT p.product_id, p.name, description, c.name , user_name, price, quality, details FROM products p, categories c ";
		query += "WHERE p.category_id=c.category_id and accepted=1 and p.category_id = ?";
		PreparedStatement ps = Utility.conn.prepareStatement(query);
		ps.setString(1, id);
		ResultSet rs = ps.executeQuery();
		JsonArray out = new JsonArray();

		while (rs.next()) {
			JsonObject temp = new JsonObject();
			temp.addProperty("product_id", rs.getString("p.product_id"));
			temp.addProperty("name", rs.getString("p.name"));
			temp.addProperty("description", rs.getString("description"));
			temp.addProperty("category", rs.getString("c.name"));
			temp.addProperty("user_name", rs.getString("user_name"));
			temp.addProperty("price", rs.getString("price"));
			temp.addProperty("quality", rs.getString("quality"));
			temp.addProperty("details", rs.getString("details"));
			out.add(temp);
		}
		Utility.disconnect();
		return out.toString();
	}

	public String addproduct(String model, MultipartFile file) throws SQLException, IOException {
		Utility.connect();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> hmap = (Map<String, String>) mapper.readValue(model, Map.class);
		// get category_id of the category name
		String query2 = "SELECT category_id FROM categories WHERE name=?";
		PreparedStatement ps2 = Utility.conn.prepareStatement(query2);
		ps2.setString(1, hmap.get("category"));
		ResultSet rs2 = ps2.executeQuery();
		rs2.next();
		int cat_id = rs2.getInt("category_id");

		String details = "No Details";
		if (!(hmap.get("details") == null))
			if (!hmap.get("details").equals(""))
				details = hmap.get("details");

		String query1 = "INSERT INTO products (name, description, user_name, price, quality, accepted, category_id, details, image, upload_date) VALUES(?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps1 = Utility.conn.prepareStatement(query1);
		ps1.setString(1, hmap.get("name"));
		ps1.setString(2, hmap.get("description"));
		ps1.setString(3, hmap.get("user_name"));
		ps1.setString(4, hmap.get("price") + " SYP");
		ps1.setString(5, hmap.get("quality") + "%");
		ps1.setInt(6, 0);
		ps1.setInt(7, cat_id);
		ps1.setString(8, details);
		ps1.setBytes(9, file.getBytes());
		String date = new Date(System.currentTimeMillis()).toString();
		ps1.setString(10, date);

		ps1.executeUpdate();

		String getId = "SELECT MAX(product_id) as id FROM products";
		PreparedStatement ps = Utility.conn.prepareStatement(getId);
		ResultSet rs = ps.executeQuery();
		rs.next();
		int id = rs.getInt("id");

		JsonObject temp = new JsonObject();
		temp.addProperty("product_id", id);
		temp.addProperty("name", hmap.get("name"));
		temp.addProperty("description", hmap.get("description"));
		temp.addProperty("category", hmap.get("category"));
		temp.addProperty("user_name", hmap.get("user_name"));
		temp.addProperty("price", "$" + hmap.get("price"));
		temp.addProperty("quality", hmap.get("quality") + "%");
		temp.addProperty("details", details);
		temp.addProperty("upload_date", date);
		return temp.toString();

	}

	public String updateproduct(Map<String, String> hmap) throws SQLException {
		Utility.connect();
		
		String query = "";
		StringBuilder builder = new StringBuilder("UPDATE products SET ");
		for (Map.Entry<String, String> entry : hmap.entrySet()) {
			if (entry.getKey().equals("id") || entry.getKey().equals("category"))
				continue;
			builder.append(entry.getKey() + "= '" + entry.getValue() + "',");
		}
		builder.deleteCharAt(builder.length() - 1);
		if(hmap.get("category")!=null) {
			String a = "SELECT category_id FROM categories WHERE name = ?";
			PreparedStatement b = Utility.conn.prepareStatement(a);
			b.setString(1, hmap.get("category"));
			ResultSet c = b.executeQuery();
			c.next();
			String category_id = c.getString(1);
			if(hmap.size()>2)
				builder.append(", category_id = '" + category_id + "'");
			else 
				builder.append(" category_id = '" + category_id + "'");
		}
		builder.append(" WHERE product_id= '" + hmap.get("id") + "'");
		query = builder.toString();
		System.out.println(query);
		PreparedStatement ps = Utility.conn.prepareStatement(query);

		int i = ps.executeUpdate();
		Utility.disconnect();
		if (i < 1)
			throw new SQLException();
		return getProductById(hmap.get("id"));
	}

	public String deleteproduct(Map<String, String> hmap) throws SQLException {
		Utility.connect();
//		String deleted = getProductById(hmap.get("product_id"));
		String query = "DELETE FROM products WHERE product_id=?";
		PreparedStatement ps = Utility.conn.prepareStatement(query);
		ps.setString(1, hmap.get("product_id"));
		int i = ps.executeUpdate();
		Utility.disconnect();
		if (i < 1)
			throw new SQLException();
		return "Deleted";

	}

}
