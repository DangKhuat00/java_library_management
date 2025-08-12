
// Goi package dao
package dao;

// Import cac thu vien can thiet
import config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Quan ly ket noi co so du lieu
 * Xu ly ket noi den co so du lieu MySQL
 */
public class DatabaseConnection {
    // Bien ket noi co so du lieu
    private static Connection connection = null;

    static {
        try {
            // Nap driver MySQL JDBC
            Class.forName(DatabaseConfig.DB_DRIVER);
        } catch (ClassNotFoundException e) {
            // In ra loi neu khong tim thay driver
            System.err.println("Khong tim thay MySQL JDBC Driver!");
            e.printStackTrace();
        }
    }

    /**
     * Lay ket noi co so du lieu
     * 
     * @return Doi tuong Connection
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                        DatabaseConfig.DB_URL,
                        DatabaseConfig.DB_USERNAME,
                        DatabaseConfig.DB_PASSWORD);
                // In ra thong bao ket noi thanh cong
                System.out.println("Ket noi toi MySQL thanh cong!");
            }
        } catch (SQLException e) {
            // In ra thong bao loi ket noi
            System.err.println("Ket noi toi co so du lieu that bai!");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Dong ket noi co so du lieu
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                // In ra thong bao dong ket noi
                System.out.println("Da dong ket noi co so du lieu.");
            }
        } catch (SQLException e) {
            // In ra loi khi dong ket noi
            System.err.println("Loi dong ket noi co so du lieu!");
            e.printStackTrace();
        }
    }

    /**
     * Kiem tra ket noi co so du lieu
     * 
     * @return true neu ket noi thanh cong
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
