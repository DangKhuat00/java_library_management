package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Databaseconnect {
  private static final String URL = "jdbc:sqlite:library.db"; // tên file db

  public static Connection connect() {
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(URL);
    } catch (SQLException e) {
      System.out.println("Connection error: " + e.getMessage());
    }
    return conn;
  }
}
