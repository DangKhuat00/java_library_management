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
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "root";
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
}