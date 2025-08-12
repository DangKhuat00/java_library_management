
// Goi package dao
package dao;

// Import cac thu vien can thiet
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Lop xu ly cac chuc nang muon va tra tai lieu
public class BorrowDAO {

    // Ham muon tai lieu
    public boolean borrowDocument(String userId, String documentId) {
        // Cau lenh SQL de them thong tin muon tai lieu
        String sql = "INSERT INTO borrowed_documents (user_id, document_id, borrow_date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Gan gia tri cho cac tham so
            stmt.setString(1, userId);
            stmt.setString(2, documentId);
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            // Tra ve true neu muon thanh cong
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // In ra loi neu co loi xay ra khi muon
            System.err.println("Error recording borrow: " + e.getMessage());
            return false;
        }
    }

    // Ham tra tai lieu
    public boolean returnDocument(String userId, String documentId) {
        // Cau lenh SQL de cap nhat ngay tra tai lieu
        String sql = "UPDATE borrowed_documents SET return_date = ? " +
                "WHERE user_id = ? AND document_id = ? AND return_date IS NULL";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Gan gia tri cho cac tham so
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            stmt.setString(2, userId);
            stmt.setString(3, documentId);
            // Tra ve true neu tra thanh cong
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // In ra loi neu co loi xay ra khi tra
            System.err.println("Error recording return: " + e.getMessage());
            return false;
        }
    }

    // Lay lich su muon cua mot nguoi dung
    public List<BorrowRecord> getUserBorrowHistory(String userId) {
        List<BorrowRecord> records = new ArrayList<>(); // Danh sach luu lich su muon
        // Cau lenh SQL lay lich su muon cua user
        String sql = "SELECT bd.id, bd.user_id, bd.document_id, d.title, bd.borrow_date, bd.return_date " +
                "FROM borrowed_documents bd " +
                "JOIN documents d ON bd.document_id = d.id " +
                "WHERE bd.user_id = ? ORDER BY bd.borrow_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Gan gia tri cho tham so va lay du lieu
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                records.add(extractBorrowRecord(rs));
            }
        } catch (SQLException e) {
            // In ra loi neu co loi khi lay lich su muon
            System.err.println("Error getting borrow history: " + e.getMessage());
        }
        return records;
    }

    // Lay tat ca lich su muon
    public List<BorrowRecord> getAllBorrowRecords() {
        List<BorrowRecord> records = new ArrayList<>(); // Danh sach luu lich su muon
        // Cau lenh SQL lay tat ca lich su muon
        String sql = "SELECT bd.id, bd.user_id, bd.document_id, d.title, bd.borrow_date, bd.return_date " +
                "FROM borrowed_documents bd " +
                "JOIN documents d ON bd.document_id = d.id " +
                "ORDER BY bd.borrow_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Lay du lieu va them vao danh sach
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                records.add(extractBorrowRecord(rs));
            }
        } catch (SQLException e) {
            // In ra loi neu co loi khi lay tat ca lich su muon
            System.err.println("Error getting all borrow records: " + e.getMessage());
        }
        return records;
    }

    // Ham tao doi tuong BorrowRecord tu ResultSet
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

    // Lop luu thong tin mot lan muon tra tai lieu
    public static class BorrowRecord {
        public String borrowId; // Ma muon
        public String userId; // Ma nguoi dung
        public String documentId; // Ma tai lieu
        public String documentTitle; // Tieu de tai lieu
        public Timestamp borrowedDate; // Ngay muon
        public Timestamp returnDate; // Ngay tra
    }
}
