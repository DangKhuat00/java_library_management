package dao;

import model.Document;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentDAO {

    public boolean insertDocument(Document document) {
        String sql = "INSERT INTO documents (title, language, pages, author, publication_year, remain_docs) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, document.getTitle());
            stmt.setString(2, document.getLanguage());
            stmt.setInt(3, document.getPages());
            stmt.setString(4, document.getAuthor());
            stmt.setInt(5, document.getPublicationYear());
            stmt.setInt(6, document.getRemainDocs());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting document: " + e.getMessage());
            return false;
        }
    }

    public List<Document> getAllDocuments() {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM documents ORDER BY id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Document doc = new Document(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("language"),
                    rs.getInt("pages"),
                    rs.getString("author"),
                    rs.getInt("publication_year"),
                    rs.getInt("remain_docs")
                );
                documents.add(doc);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving documents: " + e.getMessage());
        }

        return documents;
    }

    public boolean updateDocument(Document document) {
        String sql = "UPDATE documents SET title = ?, language = ?, pages = ?, author = ?, publication_year = ?, remain_docs = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, document.getTitle());
            stmt.setString(2, document.getLanguage());
            stmt.setInt(3, document.getPages());
            stmt.setString(4, document.getAuthor());
            stmt.setInt(5, document.getPublicationYear());
            stmt.setInt(6, document.getRemainDocs());
            stmt.setInt(7, document.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating document: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteDocument(int id) {
        String sql = "DELETE FROM documents WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting document: " + e.getMessage());
            return false;
        }
    }

    public List<Document> findDocument(String keyword) {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM documents WHERE title LIKE ? OR author LIKE ? ORDER BY title";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Document doc = new Document(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("language"),
                    rs.getInt("pages"),
                    rs.getString("author"),
                    rs.getInt("publication_year"),
                    rs.getInt("remain_docs")
                );
                documents.add(doc);
            }

        } catch (SQLException e) {
            System.err.println("Error searching documents: " + e.getMessage());
        }

        return documents;
    }

    public List<Document> findDocumentsByField(String field, String keyword) {
        List<Document> list = new ArrayList<>();
        String sql = "SELECT * FROM documents WHERE " + field + " LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Document doc = new Document(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("language"),
                        rs.getInt("pages"),
                        rs.getString("author"),
                        rs.getInt("year"),
                        rs.getInt("remain_docs")
                );
                list.add(doc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
