package com.used_products_store;

import java.sql.Blob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
public class OrderDao {

	private Set<String> status = new HashSet<>();

	public OrderDao() {
		status.add("requested");
		status.add("revised");
		status.add("confirmed");
		status.add("paid");
		status.add("received");
		status.add("rated");
	}

	public String getAllOrders() throws SQLException {
		Utility.connect();
		String query = "SELECT order_id, status, date, p.product_id, p.name, description, c.name , p.user_name, price, quality, details, accepted, upload_date, u.user_name, first_name, last_name, email, phone_number, image FROM products p, categories c, orders o, users u ";
		query += " WHERE p.product_id=o.product_id and c.category_id=p.category_id and o.user_name=u.user_name";
		PreparedStatement ps = Utility.conn.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		JsonArray out = new JsonArray();

		while (rs.next()) {
			JsonObject temp = new JsonObject();
			temp.addProperty("order_id", rs.getString("order_id"));
			temp.addProperty("status", rs.getString("status"));
			temp.addProperty("date", rs.getString("date"));
			
			temp.add("product", new JsonObject());
			temp.getAsJsonObject("product").addProperty("product_id", rs.getString("p.product_id"));
			temp.getAsJsonObject("product").addProperty("name", rs.getString("p.name"));
			temp.getAsJsonObject("product").addProperty("description", rs.getString("description"));
			temp.getAsJsonObject("product").addProperty("category", rs.getString("c.name"));
			temp.getAsJsonObject("product").addProperty("user_name", rs.getString("p.user_name"));
			temp.getAsJsonObject("product").addProperty("price", rs.getString("price"));
			temp.getAsJsonObject("product").addProperty("quality", rs.getString("quality"));
			temp.getAsJsonObject("product").addProperty("details", rs.getString("details"));
			temp.getAsJsonObject("product").addProperty("accepted", rs.getString("accepted"));
			temp.getAsJsonObject("product").addProperty("upload_date", rs.getString("upload_date"));
			if (rs.getBlob("image") != null) {
				Blob blob = rs.getBlob("image");
				byte[] input = blob.getBytes(1, (int) blob.length());
				String s = Base64.getEncoder().withoutPadding().encodeToString(input);
				temp.getAsJsonObject("product").addProperty("image", s);
			}

			temp.add("user", new JsonObject());
			temp.getAsJsonObject("user").addProperty("user_name", rs.getString("u.user_name"));
			temp.getAsJsonObject("user").addProperty("first_name", rs.getString("first_name"));
			temp.getAsJsonObject("user").addProperty("last_name", rs.getString("last_name"));
			temp.getAsJsonObject("user").addProperty("email", rs.getString("email"));
			temp.getAsJsonObject("user").addProperty("phone_number", rs.getString("phone_number"));
			out.add(temp);
		}
		Utility.disconnect();
		return out.toString();
	}

	public String getOrder(String id) throws SQLException {
		Utility.connect();
		String query = "SELECT order_id, status, date, p.product_id, p.name, description, c.name , p.user_name, price, quality, details, accepted, upload_date, u.user_name, first_name, last_name, email, phone_number FROM products p, categories c, orders o, users u ";
		query += " WHERE p.product_id=o.product_id and c.category_id=p.category_id and o.user_name=u.user_name and order_id=?";
		PreparedStatement ps = Utility.conn.prepareStatement(query);
		ps.setString(1, id);
		ResultSet rs = ps.executeQuery();
		rs.next();
		JsonObject temp = new JsonObject();
		temp.addProperty("order_id", rs.getString("order_id"));
		temp.addProperty("status", rs.getString("status"));
		temp.addProperty("date", rs.getString("date"));
		temp.add("product", new JsonObject());
		temp.getAsJsonObject("product").addProperty("product_id", rs.getString("p.product_id"));
		temp.getAsJsonObject("product").addProperty("name", rs.getString("p.name"));
		temp.getAsJsonObject("product").addProperty("description", rs.getString("description"));
		temp.getAsJsonObject("product").addProperty("category", rs.getString("c.name"));
		temp.getAsJsonObject("product").addProperty("user_name", rs.getString("p.user_name"));
		temp.getAsJsonObject("product").addProperty("price", rs.getString("price"));
		temp.getAsJsonObject("product").addProperty("quality", rs.getString("quality"));
		temp.getAsJsonObject("product").addProperty("details", rs.getString("details"));
		temp.getAsJsonObject("product").addProperty("accepted", rs.getString("accepted"));
		temp.getAsJsonObject("product").addProperty("upload_date", rs.getString("upload_date"));

		temp.add("user", new JsonObject());
		temp.getAsJsonObject("user").addProperty("user_name", rs.getString("u.user_name"));
		temp.getAsJsonObject("user").addProperty("first_name", rs.getString("first_name"));
		temp.getAsJsonObject("user").addProperty("last_name", rs.getString("last_name"));
		temp.getAsJsonObject("user").addProperty("email", rs.getString("email"));
		temp.getAsJsonObject("user").addProperty("phone_number", rs.getString("phone_number"));
		Utility.disconnect();
		return temp.toString();
	}

	public String addOrder(Map<String, String> hmap) throws SQLException {
		Utility.connect();
		
		//Check if product is already ordered
		String query = "SELECT COUNT(*) FROM orders WHERE product_id = ?";
		PreparedStatement pst = Utility.conn.prepareStatement(query);
		pst.setString(1, hmap.get("product_id"));
		ResultSet rst = pst.executeQuery();
		rst.next();
		if(rst.getInt(1)>0)
			throw new SQLException("Product Not Available!");
		
		String query1 = "INSERT INTO orders (status, date, product_id, user_name) VALUES(?, ?, ?, ?)";
		PreparedStatement ps1 = Utility.conn.prepareStatement(query1);
		ps1.setString(1, "requested");
		String date = new Date(System.currentTimeMillis()).toString();
		ps1.setString(2, date);
		ps1.setString(3, hmap.get("product_id"));
		ps1.setString(4, hmap.get("user_name"));

		ps1.executeUpdate();

		String getId = "SELECT MAX(order_id) as id FROM orders";
		PreparedStatement ps = Utility.conn.prepareStatement(getId);
		ResultSet rs = ps.executeQuery();
		rs.next();
		String id = rs.getString("id");

		return getOrder(id);

	}

	public String updateOrder(Map<String, String> hmap) throws SQLException {
		Utility.connect();
		if (hmap.get("status") != null)
			if (!status.contains(hmap.get("status")))
				throw new SQLException("Not allowed value for status");

		String query = "";
		StringBuilder builder = new StringBuilder("UPDATE orders SET ");
		for (Map.Entry<String, String> entry : hmap.entrySet()) {
			if (entry.getKey().equals("id"))
				continue;
			builder.append(entry.getKey() + "= '" + entry.getValue() + "',");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append(" WHERE order_id= '" + hmap.get("id") + "'");
		query = builder.toString();
		System.out.println(query);
		PreparedStatement ps = Utility.conn.prepareStatement(query);

		int i = ps.executeUpdate();
		Utility.disconnect();
		if (i < 1)
			throw new SQLException();
		return getOrder(hmap.get("id"));
	}

	public String deleteOrder(Map<String, String> hmap) throws SQLException {
		String deleted = getOrder(hmap.get("id"));
		Utility.connect();
		String query = "DELETE FROM orders WHERE order_id=?";
		PreparedStatement ps = Utility.conn.prepareStatement(query);
		ps.setString(1, hmap.get("id"));
		int i = ps.executeUpdate();
		Utility.disconnect();
		if (i < 1)
			throw new SQLException();
		return deleted;

	}

}
