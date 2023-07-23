package com.used_products_store;
import java.sql.Connection;
import java.sql.DriverManager;

public class Utility {
  static Connection conn;
  static String URL = "jdbc:mysql://localhost:3306/used_products_store";
  static String USERNAME = "root";
  static String PASSWORD = "zeropp";
  
  public static void connect () {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
      System.out.println("OK!!!");
    }catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println("Connect Failed !");
    }
  }
  
  public static void disconnect() {
      try {
        if (conn != null)
            conn.close();
      }catch (Exception e) {
        System.out.println("Disconnect Failed !");
      }
    }
  
}