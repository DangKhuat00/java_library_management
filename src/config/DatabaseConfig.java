package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database configuration class
 * Contains MySQL connection settings
 */
public class DatabaseConfig {
    // Database connection settings
    public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/library_management";
    public static final String DB_USERNAME = "root";  // Change to your MySQL username
    public static final String DB_PASSWORD = "root";      // Change to your MySQL password
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Connection pool settings
    public static final int MAX_CONNECTIONS = 10;
    public static final int CONNECTION_TIMEOUT = 30000; // 30 seconds

    // Load MySQL JDBC driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Test MySQL connection
    public static void testConnection() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            if (conn != null) {
                System.out.println("Connection to MySQL database successful!");
            }
        } catch (SQLException e) {
            System.err.println("Failed to connect to MySQL database: " + e.getMessage());
        }
    }

    // Initialize database tables
    public static void initializeDatabase() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id VARCHAR(255) PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "email VARCHAR(255) NOT NULL UNIQUE" +
                ")";

        String createDocumentsTable = "CREATE TABLE IF NOT EXISTS documents (" +
                "id VARCHAR(255) PRIMARY KEY," +
                "title VARCHAR(255) NOT NULL," +
                "author VARCHAR(255) NOT NULL," +
                "publication_year INT NOT NULL," +
                "document_type ENUM('BOOK', 'MAGAZINE') NOT NULL" +
                ")";

        String createBorrowsTable = "CREATE TABLE IF NOT EXISTS borrows (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id VARCHAR(255) NOT NULL," +
                "document_id VARCHAR(255) NOT NULL," +
                "borrow_date DATE NOT NULL," +
                "return_date DATE," +
                "FOREIGN KEY (user_id) REFERENCES users(id)," +
                "FOREIGN KEY (document_id) REFERENCES documents(id)" +
                ")";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.execute(createUsersTable);
            stmt.execute(createDocumentsTable);
            stmt.execute(createBorrowsTable);

            System.out.println("Database tables initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Failed to initialize database tables: " + e.getMessage());
        }
    }
}
