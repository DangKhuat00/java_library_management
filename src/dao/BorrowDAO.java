package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object cho cac thao tac muon tra tai lieu.
 *
 * <p>Class nay thuc hien cac tac vu lam viec voi co so du lieu lien quan den viec muon tai lieu,
 * tra tai lieu va lay thong tin lich su muon.
 */
public class BorrowDAO {

  /**
   * Ghi nhan viec muon tai lieu moi.
   *
   * @param userId ma nguoi dung
   * @param documentId ma tai lieu
   * @return true neu ghi nhan thanh cong, false neu that bai
   */
  public boolean borrowDocument(String userId, String documentId) {
    String sql =
        "INSERT INTO borrowed_documents (user_id, document_id, borrow_date) VALUES (?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, userId);
      stmt.setString(2, documentId);
      stmt.setDate(3, new java.sql.Date(System.currentTimeMillis())); // Ngay muon hien tai

      int result = stmt.executeUpdate();
      return result > 0;

    } catch (SQLException e) {
      System.err.println("Error recording borrow: " + e.getMessage());
      return false;
    }
  }

  /**
   * Tra lai tai lieu da muon.
   *
   * @param userId ma nguoi dung
   * @param documentId ma tai lieu
   * @return true neu tra thanh cong, false neu that bai
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
   * Lay danh sach ma tai lieu ma nguoi dung dang muon.
   *
   * @param userId ma nguoi dung
   * @return danh sach ma tai lieu
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
   * Kiem tra xem tai lieu co dang duoc muon hay khong.
   *
   * @param documentId ma tai lieu
   * @return true neu tai lieu dang duoc muon, false neu khong
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
   * Lay so luong tai lieu dang duoc muon boi nguoi dung.
   *
   * @param userId ma nguoi dung
   * @return so luong tai lieu dang muon
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
   * Lay lich su muon tai lieu cua nguoi dung.
   *
   * @param userId ma nguoi dung
   * @return danh sach {@link BorrowRecord} chua thong tin muon tra
   */
  public List<BorrowRecord> getUserBorrowHistory(String userId) {
    List<BorrowRecord> records = new ArrayList<>();
    String sql =
        "SELECT bd.*, d.title FROM borrowed_documents bd "
            + "JOIN documents d ON bd.document_id = d.id "
            + "WHERE bd.user_id = ? ORDER BY bd.borrowed_date DESC";

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

  /** Lop noi bo luu tru thong tin 1 lan muon tai lieu. */
  public static class BorrowRecord {
    /** Ma tai lieu. */
    public String documentId;

    /** Tieu de tai lieu. */
    public String documentTitle;

    /** Thoi gian muon tai lieu. */
    public Timestamp borrowedDate;

    /** Thoi gian tra tai lieu. */
    public Timestamp returnDate;

    /** Trang thai da tra hay chua. */
    public boolean isReturned;

    @Override
    public String toString() {
      return String.format(
          "%s (Borrowed: %s, Returned: %s)",
          documentTitle,
          borrowedDate.toString(),
          isReturned ? returnDate.toString() : "Not returned");
    }
  }
}
