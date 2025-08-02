package dao;

import model.Document;
import model.Book;
import model.Magazine;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import config.DatabaseConfig;

/**
 * Data Access Object for Document operations
 * Handles database CRUD operations for documents
 */
public class DocumentDAO {
    
    /**
     * Insert a new document into database
     */
    public boolean insertDocument(Document document) {
        String sql = "INSERT INTO documents (id, title, author, publication_year, document_type) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, document.getId());
            stmt.setString(2, document.getTitle());
            stmt.setString(3, document.getAuthor());
            stmt.setInt(4, document.getYear());
            
            // Determine document type
            String type = (document instanceof Book) ? "BOOK" : "MAGAZINE";
            stmt.setString(5, type);
            
            int result = stmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error inserting document: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get all documents from database
     */
    public List<Document> getAllDocuments() {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM documents ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Document doc = createDocumentFromResultSet(rs);
                if (doc != null) {
                    documents.add(doc);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving documents: " + e.getMessage());
        }
        
        return documents;
    }
    
    public Document getDocumentById(String id) {
        String sql = "SELECT * FROM documents WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(
                 DatabaseConfig.DB_URL,
                 DatabaseConfig.DB_USERNAME,
                 DatabaseConfig.DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String type = rs.getString("document_type");
                    return type.equals("BOOK")
                        ? new Book(
                            rs.getString("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            "", "",
                            rs.getInt("publication_year"),
                            1,
                            true)
                        : new Magazine(
                            rs.getString("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            "", "",
                            rs.getInt("publication_year"),
                            1,
                            true);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Find document by ID
     */
    public Document findDocumentById(String id) {
        String sql = "SELECT * FROM documents WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return createDocumentFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding document: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Update document in database
     */
    public boolean updateDocument(Document document) {
        String sql = "UPDATE documents SET title = ?, author = ?, publication_year = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, document.getTitle());
            stmt.setString(2, document.getAuthor());
            stmt.setInt(3, document.getYear());
            stmt.setString(4, document.getId());
            
            int result = stmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating document: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete document from database
     */
    public boolean deleteDocument(String id) {
        String sql = "DELETE FROM documents WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            int result = stmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting document: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Search documents by title or author
     */
    public List<Document> searchDocuments(String keyword) {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM documents WHERE title LIKE ? OR author LIKE ? ORDER BY title";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Document doc = createDocumentFromResultSet(rs);
                if (doc != null) {
                    documents.add(doc);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching documents: " + e.getMessage());
        }
        
        return documents;
    }
    
    /**
     * Create Document object from ResultSet
     */
    private Document createDocumentFromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String title = rs.getString("title");
        String author = rs.getString("author");
        int year = rs.getInt("publication_year");
        String type = rs.getString("document_type");
        
        if ("BOOK".equals(type)) {
            return new Book(id, title, author, "", "", year, 1, true);
        } else if ("MAGAZINE".equals(type)) {
            return new Magazine(id, title, author, "", "", year, 1, true);
        }
        
        return null;
    }
}
