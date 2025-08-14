package dao;

// Import cac thu vien can thiet
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Account;

/**
 * Quan ly cac thao tac voi tai khoan nguoi dung
 * Xu ly dang ky, dang nhap va xac thuc tai khoan
 */
public class AccountDAO {

  /**
   * Dang ky tai khoan moi cho nguoi dung
   * Luu thong tin vao ca bang accounts va users
   * 
   * @param account doi tuong Account can dang ky
   * @return true neu dang ky thanh cong
   */
  public boolean register(Account account) {
    // Cau lenh SQL them vao bang accounts
    String sqlAccount = "INSERT INTO accounts (username, password, phone, email, role) VALUES (?, ?, ?, ?, ?)";
    // Cau lenh SQL them vao bang users
    String sqlUser = "INSERT INTO users (name, email, phoneNumber, borrowLimit, borrowedBooksCount) VALUES (?, ?, ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection()) {
      // Bat dau transaction de dam bao du lieu dong bo
      conn.setAutoCommit(false);

      // Buoc 1: Luu thong tin vao bang accounts
      try (PreparedStatement stmtAcc = conn.prepareStatement(sqlAccount)) {
        stmtAcc.setString(1, account.getUsername());
        stmtAcc.setString(2, account.getPassword()); // Co the ma hoa password o day
        stmtAcc.setString(3, account.getPhone());
        stmtAcc.setString(4, account.getEmail());
        stmtAcc.setString(5, account.getRole());
        stmtAcc.executeUpdate();
      }

      // Buoc 2: Luu thong tin vao bang users
      try (PreparedStatement stmtUser = conn.prepareStatement(sqlUser)) {
        stmtUser.setString(1, account.getUsername()); // Ten nguoi dung
        stmtUser.setString(2, account.getEmail()); // Email
        stmtUser.setString(3, account.getPhone()); // So dien thoai
        stmtUser.setInt(4, 10); // Gioi han muon mac dinh
        stmtUser.setInt(5, 0); // So sach da muon ban dau
        stmtUser.executeUpdate();
      }

      // Xac nhan luu ca hai bang thanh cong
      conn.commit();
      return true;

    } catch (SQLException e) {
      // In ra loi neu co loi khi dang ky
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Dang nhap vao he thong
   * Kiem tra thong tin tai khoan va mat khau
   * 
   * @param username ten dang nhap
   * @param password mat khau
   * @return doi tuong Account neu dang nhap thanh cong, null neu that bai
   */
  public Account login(String username, String password) {
    // Cau lenh SQL tim tai khoan theo username va password
    String sql = "SELECT * FROM accounts WHERE username = ? AND password = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      // Gan gia tri cho cac tham so
      stmt.setString(1, username);
      stmt.setString(2, password);

      // Thuc thi cau lenh va xu ly ket qua
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        // Tao doi tuong Account tu ket qua truy van
        Account acc = new Account();
        acc.setId(rs.getInt("id"));
        acc.setUsername(rs.getString("username"));
        acc.setPassword(rs.getString("password"));
        acc.setPhone(rs.getString("phone"));
        acc.setEmail(rs.getString("email"));
        acc.setRole(rs.getString("role"));
        return acc;
      }
    } catch (SQLException e) {
      // In ra loi neu co loi khi dang nhap
      e.printStackTrace();
    }
    // Tra ve null neu khong tim thay tai khoan
    return null;
  }
}