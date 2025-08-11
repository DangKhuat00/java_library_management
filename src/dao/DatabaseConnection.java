package dao;

import config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Quan ly ket noi toi co so du lieu MySQL.
 *
 * <p>Class nay chiu trach nhiem tao, dong va kiem tra ket noi toi database su dung JDBC voi cau
 * hinh trong {@link config.DatabaseConfig}.
 */
public class DatabaseConnection {
  /** Doi tuong ket noi JDBC toi database. */
  private static Connection connection = null;

  static {
    try {
      // Nap driver JDBC cho MySQL
      Class.forName(DatabaseConfig.DB_DRIVER);
    } catch (ClassNotFoundException e) {
      System.err.println("MySQL JDBC Driver not found!");
      e.printStackTrace();
    }
  }

  /**
   * Lay ket noi toi co so du lieu.
   *
   * @return doi tuong {@link Connection} neu ket noi thanh cong, null neu that bai
   */
  public static Connection getConnection() {
    try {
      if (connection == null || connection.isClosed()) {
        connection =
            DriverManager.getConnection(
                DatabaseConfig.DB_URL, DatabaseConfig.DB_USERNAME, DatabaseConfig.DB_PASSWORD);
        System.out.println("✅ Connected to MySQL database successfully!");
      }
    } catch (SQLException e) {
      System.err.println("❌ Failed to connect to database!");
      e.printStackTrace();
    }
    return connection;
  }

  /** Dong ket noi toi co so du lieu neu dang mo. */
  public static void closeConnection() {
    try {
      if (connection != null && !connection.isClosed()) {
        connection.close();
        System.out.println("🔒 Database connection closed.");
      }
    } catch (SQLException e) {
      System.err.println("Error closing database connection!");
      e.printStackTrace();
    }
  }

  /**
   * Kiem tra ket noi toi co so du lieu.
   *
   * @return true neu ket noi hop le va dang mo, false neu khong
   */
  public static boolean testConnection() {
    try {
      Connection conn = getConnection();
      return conn != null && !conn.isClosed();
    } catch (SQLException e) {
      return false;
    }
  }
}
