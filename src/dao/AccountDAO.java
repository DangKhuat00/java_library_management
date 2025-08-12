package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Account;

public class AccountDAO {

  public boolean register(Account account) {
    String sql = "INSERT INTO accounts (username, password, role) VALUES (?, ?, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, account.getUsername());
      stmt.setString(2, account.getPassword()); // Có thể mã hóa password ở đây
      stmt.setString(3, account.getRole());

      return stmt.executeUpdate() > 0;
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
        acc.setRole(rs.getString("role"));
        return acc;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
}
