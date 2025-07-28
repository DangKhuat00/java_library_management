package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Documentsql {

  public void createTableIfNotExists() {
    String sql =
        "CREATE TABLE IF NOT EXISTS documents (" +
            "id TEXT PRIMARY KEY, " +
            "title TEXT NOT NULL, " +
            "author TEXT NOT NULL, " +
            "publisher TEXT, " +
            "category TEXT, " +
            "year INTEGER NOT NULL, " +
            "numbers INTEGER NOT NULL, " +
            "isAvailable BOOLEAN NOT NULL" +
            ");";
    try (Connection conn = Databaseconnect.connect();
        Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println("Create table error: " + e.getMessage());
    }
  }


  public void insert(Document doc) {
    String sql = "INSERT INTO documents(title, author, year) VALUES(?, ?, ?)";
    try (Connection conn = Databaseconnect.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, doc.getTitle());
      pstmt.setString(2, doc.getAuthor());
      pstmt.setInt(3, doc.getYear());
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Insert error: " + e.getMessage());
    }
  }

  public List<Document> getAllDocuments() {
    List<Document> documents = new ArrayList<>();

    try (Connection conn = Databaseconnect.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id, title, author, publisher, category, year, numbers, isAvailable FROM Documents")) {

      while (rs.next()) {
        Document doc = new Document(
            rs.getString("id"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getString("publisher"),
            rs.getString("category"),
            rs.getInt("year"),
            rs.getInt("numbers"),
            rs.getBoolean("isAvailable")
        );
        documents.add(doc);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return documents;
  }

  public void deleteAll() {
    String sql = "DELETE FROM documents";
    try (Connection conn = Databaseconnect.connect();
        Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      System.out.println("Delete error: " + e.getMessage());
    }
  }
  public boolean addDocument(Document document) {
    String sql = "INSERT INTO documents(id, title, author, publisher, category, year, numbers, isAvailable) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = Databaseconnect.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, document.getId());
      pstmt.setString(2, document.getTitle());
      pstmt.setString(3, document.getAuthor());
      pstmt.setString(4, document.getPublisher());
      pstmt.setString(5, document.getCategory());
      pstmt.setInt(6, document.getYear());
      pstmt.setInt(7, document.getNumbers());
      pstmt.setBoolean(8, document.isAvailable());

      int rowsAffected = pstmt.executeUpdate();
      return rowsAffected > 0;

    } catch (SQLException e) {
      System.out.println("Add document error: " + e.getMessage());
      return false;
    }
  }
  public boolean deleteDocument(String id) {
    String sql = "DELETE FROM documents WHERE id = ?";

    try (Connection conn = Databaseconnect.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, id);
      int rowsAffected = pstmt.executeUpdate();
      return rowsAffected > 0;

    } catch (SQLException e) {
      System.out.println("Delete document error: " + e.getMessage());
      return false;
    }
  }
  public boolean updateDocument(Document document) {
    String sql = "UPDATE documents SET title = ?, author = ?, publisher = ?, category = ?, year = ?, numbers = ?, isAvailable = ? WHERE id = ?";

    try (Connection conn = Databaseconnect.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, document.getTitle());
      pstmt.setString(2, document.getAuthor());
      pstmt.setString(3, document.getPublisher());
      pstmt.setString(4, document.getCategory());
      pstmt.setInt(5, document.getYear());
      pstmt.setInt(6, document.getNumbers());
      pstmt.setBoolean(7, document.isAvailable());
      pstmt.setString(8, document.getId());

      int rowsAffected = pstmt.executeUpdate();
      return rowsAffected > 0;

    } catch (SQLException e) {
      System.out.println("Update document error: " + e.getMessage());
      return false;
    }
  }

}
