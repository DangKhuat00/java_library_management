package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAO {

    // Mượn tài liệu
    public boolean borrowDocument(String userId, String documentId) {
        String sql = "INSERT INTO borrowed_documents (user_id, document_id, borrow_date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setString(2, documentId);
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error recording borrow: " + e.getMessage());
            return false;
        }
    }

    // Trả tài liệu
    public boolean returnDocument(String userId, String documentId) {
        String sql = "UPDATE borrowed_documents SET return_date = ? " +
                     "WHERE user_id = ? AND document_id = ? AND return_date IS NULL";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            stmt.setString(2, userId);
            stmt.setString(3, documentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error recording return: " + e.getMessage());
            return false;
        }
    }

    // Lịch sử mượn của 1 user
    public List<BorrowRecord> getUserBorrowHistory(String userId) {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = "SELECT bd.id, bd.user_id, bd.document_id, d.title, bd.borrow_date, bd.return_date " +
                     "FROM borrowed_documents bd " +
                     "JOIN documents d ON bd.document_id = d.id " +
                     "WHERE bd.user_id = ? ORDER BY bd.borrow_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                records.add(extractBorrowRecord(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting borrow history: " + e.getMessage());
        }
        return records;
    }

    // Lấy tất cả lịch sử mượn
    public List<BorrowRecord> getAllBorrowRecords() {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = "SELECT bd.id, bd.user_id, bd.document_id, d.title, bd.borrow_date, bd.return_date " +
                     "FROM borrowed_documents bd " +
                     "JOIN documents d ON bd.document_id = d.id " +
                     "ORDER BY bd.borrow_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                records.add(extractBorrowRecord(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all borrow records: " + e.getMessage());
        }
        return records;
    }

    private BorrowRecord extractBorrowRecord(ResultSet rs) throws SQLException {
        BorrowRecord record = new BorrowRecord();
        record.borrowId = rs.getString("id");
        record.userId = rs.getString("user_id");
        record.documentId = rs.getString("document_id");
        record.documentTitle = rs.getString("title");
        record.borrowedDate = rs.getTimestamp("borrow_date");
        record.returnDate = rs.getTimestamp("return_date");
        return record;
    }

    public static class BorrowRecord {
        public String borrowId;
        public String userId;
        public String documentId;
        public String documentTitle;
        public Timestamp borrowedDate;
        public Timestamp returnDate;
    }
}
