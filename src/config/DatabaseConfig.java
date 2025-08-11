package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Cung cap cau hinh ket noi toi co so du lieu MySQL su dung trong he thong quan ly thu vien.
 *
 * <p>Class nay chua cac hang so cau hinh ket noi, nap driver va phuong thuc kiem tra ket noi.
 */
public class DatabaseConfig {

  /** JDBC URL ket noi toi MySQL database. */
  public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/library_management";

  /** Ten nguoi dung de dang nhap vao MySQL database. */
  public static final String DB_USERNAME = "root";

  /** Mat khau dang nhap vao MySQL database. */
  public static final String DB_PASSWORD = "root";

  /** Ten day du cua class driver JDBC cho MySQL. */
  public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

  /** So ket noi toi da trong connection pool. */
  public static final int MAX_CONNECTIONS = 10;

  /** Thoi gian timeout (ms) khi lay ket noi tu pool. */
  public static final int CONNECTION_TIMEOUT = 30000; // 30 giay

  // Nap driver MySQL JDBC khi class duoc load
  static {
    try {
      Class.forName(DB_DRIVER);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * Kiem tra ket noi toi MySQL database voi cau hinh hien tai.
   *
   * <p>Phuong thuc nay thu ket noi toi database va in thong bao thanh cong hoac that bai ra
   * console.
   *
   * @throws SQLException neu xay ra loi truy cap co so du lieu
   */
  public static void testConnection() {
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
      if (conn != null) {
        System.out.println("Connection to MySQL database successful!");
      }
    } catch (SQLException e) {
      System.err.println("Failed to connect to MySQL database: " + e.getMessage());
    }
  }
}
