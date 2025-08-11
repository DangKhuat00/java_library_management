package dao;

import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import config.DatabaseConfig;

/**
 * Lop Data Access Object (DAO) xu ly cac thao tac CRUD voi bang users trong co so du lieu. Chiu
 * trach nhiem them, lay, sua, xoa va tim kiem nguoi dung.
 */
public class UserDAO {

  /**
   * Them mot nguoi dung moi vao co so du lieu.
   *
   * @param user doi tuong User chua thong tin nguoi dung
   * @return true neu them thanh cong, nguoc lai false
   */
  public boolean insertUser(User user) {
    String sql =
        "INSERT INTO users (id, name, email, phoneNumber, borrowLimit) VALUES (?, ?, ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, user.getId());
      stmt.setString(2, user.getName());
      stmt.setString(3, user.getEmail());
      stmt.setString(4, user.getPhoneNumber());
      stmt.setInt(5, user.getBorrowLimit());

      int result = stmt.executeUpdate();
      return result > 0;

    } catch (SQLException e) {
      System.err.println("Error inserting user: " + e.getMessage());
      return false;
    }
  }

  /**
   * Lay tat ca nguoi dung tu co so du lieu.
   *
   * @return danh sach nguoi dung
   */
  public List<User> getAllUsers() {
    List<User> users = new ArrayList<>();
    String sql = "SELECT * FROM users ORDER BY name";

    try (Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        User user = new User(rs.getString("id"), rs.getString("name"), rs.getString("email"));
        users.add(user);
      }

    } catch (SQLException e) {
      System.err.println("Error retrieving users: " + e.getMessage());
    }

    return users;
  }

  /**
   * Lay nguoi dung theo ID.
   *
   * @param id ma nguoi dung
   * @return doi tuong User neu tim thay, nguoc lai null
   */
  public User getUserById(String id) {
    String sql = "SELECT * FROM users WHERE id = ?";
    try (Connection conn =
            DriverManager.getConnection(
                DatabaseConfig.DB_URL, DatabaseConfig.DB_USERNAME, DatabaseConfig.DB_PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, id);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return new User(rs.getString("id"), rs.getString("name"), rs.getString("email"));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Tim nguoi dung theo ID.
   *
   * @param id ma nguoi dung
   * @return doi tuong User neu tim thay, nguoc lai null
   */
  public User findUserById(String id) {
    String sql = "SELECT * FROM users WHERE id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, id);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        return new User(rs.getString("id"), rs.getString("name"), rs.getString("email"));
      }

    } catch (SQLException e) {
      System.err.println("Error finding user: " + e.getMessage());
    }

    return null;
  }

  /**
   * Cap nhat thong tin nguoi dung.
   *
   * @param user doi tuong User chua thong tin can cap nhat
   * @return true neu cap nhat thanh cong, nguoc lai false
   */
  public boolean updateUser(User user) {
    String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, user.getName());
      stmt.setString(2, user.getEmail());
      stmt.setString(3, user.getId());

      int result = stmt.executeUpdate();
      return result > 0;

    } catch (SQLException e) {
      System.err.println("Error updating user: " + e.getMessage());
      return false;
    }
  }

  /**
   * Xoa nguoi dung khoi co so du lieu.
   *
   * @param id ma nguoi dung
   * @return true neu xoa thanh cong, nguoc lai false
   */
  public boolean deleteUser(String id) {
    String sql = "DELETE FROM users WHERE id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, id);
      int result = stmt.executeUpdate();
      return result > 0;

    } catch (SQLException e) {
      System.err.println("Error deleting user: " + e.getMessage());
      return false;
    }
  }
}
