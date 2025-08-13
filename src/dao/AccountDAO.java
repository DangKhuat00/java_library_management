package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Account;

public class AccountDAO {

  public boolean register(Account account) {
    String sqlAccount = "INSERT INTO accounts (username, password, phone, email, role) VALUES (?, ?, ?, ?, ?)";
    String sqlUser = "INSERT INTO users (name, email, phoneNumber, borrowLimit, borrowedBooksCount) VALUES (?, ?, ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection()) {
      conn.setAutoCommit(false); // Bắt đầu transaction

      // 1. Lưu vào bảng accounts
      try (PreparedStatement stmtAcc = conn.prepareStatement(sqlAccount)) {
        stmtAcc.setString(1, account.getUsername());
        stmtAcc.setString(2, account.getPassword()); // Có thể mã hóa password ở đây
        stmtAcc.setString(3, account.getPhone());
        stmtAcc.setString(4, account.getEmail());
        stmtAcc.setString(5, account.getRole());
        stmtAcc.executeUpdate();
      }

      // 2. Lưu vào bảng users
      try (PreparedStatement stmtUser = conn.prepareStatement(sqlUser)) {
        stmtUser.setString(1, account.getUsername()); // name
        stmtUser.setString(2, account.getEmail()); // email
        stmtUser.setString(3, account.getPhone()); // phoneNumber
        stmtUser.setInt(4, 10); // borrowLimit
        stmtUser.setInt(5, 0); // borrowedBooksCount
        stmtUser.executeUpdate();
      }

      conn.commit(); // Xác nhận lưu cả hai bảng
      return true;

    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public Account login(String username, String password) {
    String sql = "SELECT * FROM accounts WHERE username = ? AND password = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, username);
      stmt.setString(2, password);

      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
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
      e.printStackTrace();
    }
    return null;
  }

}
