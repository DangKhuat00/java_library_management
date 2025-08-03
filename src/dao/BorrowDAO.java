package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Borrow operations
 * Handles database operations for borrowed documents
 */
public class BorrowDAO {
    
    /**
     * Record a document borrowing
     */
    public boolean borrowDocument(String userId, String documentId) {
    String sql = "INSERT INTO borrowed_documents (user_id, document_id, borrow_date) VALUES (?, ?, ?)";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, userId);
        stmt.setString(2, documentId);
        stmt.setDate(3, new java.sql.Date(System.currentTimeMillis())); // Ngày mượn hiện tại
        
        int result = stmt.executeUpdate();
        return result > 0;
        
    } catch (SQLException e) {
        System.err.println("Error recording borrow: " + e.getMessage());
        return false;
    }
}
    
    /**
     * Return a borrowed document
     */
    public boolean returnDocument(String userId, String documentId) {
        String sql = "DELETE FROM borrowed_documents WHERE user_id = ? AND document_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, userId);
            stmt.setString(2, documentId);
            
            int result = stmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error recording return: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get borrowed documents for a user
     */
    public List<String> getBorrowedDocuments(String userId) {
        List<String> documentIds = new ArrayList<>();
        String sql = "SELECT document_id FROM borrowed_documents WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                documentIds.add(rs.getString("document_id"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting borrowed documents: " + e.getMessage());
        }
        
        return documentIds;
    }
    
    /**
     * Check if a document is currently borrowed
     */
    public boolean isDocumentBorrowed(String documentId) {
        String sql = "SELECT COUNT(*) FROM borrowed_documents WHERE document_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, documentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking document availability: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Get borrow count for a user
     */
    public int getUserBorrowCount(String userId) {
        String sql = "SELECT COUNT(*) FROM borrowed_documents WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user borrow count: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Get complete borrow history for a user
     */
    public List<BorrowRecord> getUserBorrowHistory(String userId) {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = "SELECT bd.*, d.title FROM borrowed_documents bd " +
                    "JOIN documents d ON bd.document_id = d.id " +
                    "WHERE bd.user_id = ? ORDER BY bd.borrowed_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                BorrowRecord record = new BorrowRecord();
                record.documentId = rs.getString("document_id");
                record.documentTitle = rs.getString("title");
                record.borrowedDate = rs.getTimestamp("borrowed_date");
                record.returnDate = rs.getTimestamp("return_date");
                record.isReturned = rs.getBoolean("is_returned");
                records.add(record);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting borrow history: " + e.getMessage());
        }
        
        return records;
    }
    
    /**
     * Inner class to represent borrow record
     */
    public static class BorrowRecord {
        public String documentId;
        public String documentTitle;
        public Timestamp borrowedDate;
        public Timestamp returnDate;
        public boolean isReturned;
        
        @Override
        public String toString() {
            return String.format("%s (Borrowed: %s, Returned: %s)", 
                documentTitle, 
                borrowedDate.toString(),
                isReturned ? returnDate.toString() : "Not returned"
            );
        }
    }
}
