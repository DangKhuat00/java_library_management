
// Goi package dao
package dao;

// Import cac thu vien can thiet
import model.User;
import model.UserFilter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Lop xu ly cac thao tac voi nguoi dung
public class UserDAO {

    /**
     * Them nguoi dung moi vao co so du lieu
     */
    // Ham them nguoi dung moi
    public boolean insertUser(User user) {
        String sql = "INSERT INTO users (name, email, phoneNumber, borrowLimit, borrowedBooksCount) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Gan gia tri cho cac truong
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhoneNumber());
            stmt.setInt(4, user.getBorrowLimit());
            stmt.setInt(5, user.getBorrowedBooksCount());

            // Tra ve true neu them thanh cong
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // In ra loi neu co loi khi them
            System.err.println("Error inserting user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lay tat ca nguoi dung tu co so du lieu
     */
    // Ham lay tat ca nguoi dung
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>(); // Danh sach nguoi dung
        String sql = "SELECT * FROM users ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // Duyet tung dong va tao doi tuong User
            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phoneNumber"),
                        rs.getInt("borrowedBooksCount"));
                user.setBorrowLimit(rs.getInt("borrowLimit"));
                users.add(user);
            }

        } catch (SQLException e) {
            // In ra loi neu co loi khi lay du lieu
            System.err.println("Error retrieving users: " + e.getMessage());
        }

        return users;
    }

    /**
     * Lay nguoi dung theo ID
     */
    // Ham lay nguoi dung theo ID
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Gan id vao cau lenh
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phoneNumber"),
                            rs.getInt("borrowedBooksCount"));
                    user.setBorrowLimit(rs.getInt("borrowLimit"));
                    return user;
                }
            }

        } catch (SQLException e) {
            // In ra loi neu co loi khi lay theo ID
            System.err.println("Error getting user by ID: " + e.getMessage());
        }

        return null;
    }

    /**
     * Cap nhat thong tin nguoi dung
     */
    // Ham cap nhat nguoi dung
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, phoneNumber = ?, borrowLimit = ?, borrowedBooksCount = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Gan gia tri cho cac truong
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhoneNumber());
            stmt.setInt(4, user.getBorrowLimit());
            stmt.setInt(5, user.getBorrowedBooksCount());
            stmt.setInt(6, user.getId());

            // Tra ve true neu cap nhat thanh cong
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // In ra loi neu co loi khi cap nhat
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Xoa nguoi dung khoi co so du lieu
     */
    // Ham xoa nguoi dung
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Gan id va xoa
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // In ra loi neu co loi khi xoa
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }

    // Ham tim kiem nguoi dung theo tu khoa
    public List<User> searchUsers(String keyword) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE name LIKE ? OR email LIKE ? OR phone LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("borrowedBooksCount"));
                user.setBorrowLimit(rs.getInt("maxBorrowLimit"));
                users.add(user);
            }
        } catch (SQLException e) {
            // In ra loi neu co loi khi tim kiem
            e.printStackTrace();
        }
        return users;
    }

    // Ham tim kiem nguoi dung theo tu khoa va bo loc
    public List<User> searchUsers(String keyword, UserFilter filter) {
        List<User> users = new ArrayList<>();
        String sql;

        switch (filter) {
            case ID:
                sql = "SELECT * FROM users WHERE CAST(id AS CHAR) LIKE ?";
                break;
            case NAME:
                sql = "SELECT * FROM users WHERE LOWER(name) LIKE ?";
                break;
            case EMAIL:
                sql = "SELECT * FROM users WHERE LOWER(email) LIKE ?";
                break;
            case PHONE_NUMBER:
                sql = "SELECT * FROM users WHERE LOWER(phoneNumber) LIKE ?";
                break;
            case BORROW_LIMIT:
                sql = "SELECT * FROM users WHERE CAST(borrowLimit AS CHAR) LIKE ?";
                break;
            case BORROWED_COUNT:
                sql = "SELECT * FROM users WHERE CAST(borrowedBooksCount AS CHAR) LIKE ?";
                break;
            case ALL_FIELDS:
            default:
                sql = "SELECT * FROM users WHERE " +
                        "CAST(id AS CHAR) LIKE ? OR " +
                        "LOWER(name) LIKE ? OR " +
                        "LOWER(email) LIKE ? OR " +
                        "LOWER(phoneNumber) LIKE ? OR " +
                        "CAST(borrowLimit AS CHAR) LIKE ? OR " +
                        "CAST(borrowedBooksCount AS CHAR) LIKE ?";
                break;
        }

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchKeyword = "%" + keyword.toLowerCase() + "%";

            if (filter == UserFilter.ALL_FIELDS) {
                pstmt.setString(1, searchKeyword);
                pstmt.setString(2, searchKeyword);
                pstmt.setString(3, searchKeyword);
                pstmt.setString(4, searchKeyword);
                pstmt.setString(5, searchKeyword);
                pstmt.setString(6, searchKeyword);
            } else {
                pstmt.setString(1, searchKeyword);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phoneNumber"),
                            rs.getInt("borrowLimit"),
                            rs.getInt("borrowedBooksCount")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

}
