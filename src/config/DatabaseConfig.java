
// Goi package config
package config;

// Import cac thu vien can thiet de ket noi co so du lieu
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lop cau hinh ket noi co so du lieu
 * Chua thong tin ket noi va cau hinh MySQL
 */
public class DatabaseConfig {
    // Thong tin ket noi co so du lieu
    public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/library_management"; // Duong dan ket noi
    public static final String DB_USERNAME = "root"; // Ten dang nhap
    public static final String DB_PASSWORD = "root"; // Mat khau
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver"; // Ten driver

    // Cau hinh pool ket noi
    public static final int MAX_CONNECTIONS = 10; // So ket noi toi da
    public static final int CONNECTION_TIMEOUT = 30000; // Thoi gian cho ket noi (ms)

    // Khoi tao driver MySQL
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // In ra loi neu khong tim thay driver
            e.printStackTrace();
        }
    }

    // Ham kiem tra ket noi MySQL
    public static void testConnection() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            if (conn != null) {
                // In ra thong bao ket noi thanh cong
                System.out.println("Ket noi toi MySQL thanh cong!");
            }
        } catch (SQLException e) {
            // In ra thong bao loi ket noi
            System.err.println("Ket noi toi MySQL that bai: " + e.getMessage());
        }
    }
}