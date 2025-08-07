package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Main GUI Application Launcher for Library Management System
 * Provides a modern, user-friendly interface for library operations
 */
public class LibraryAppGUI {
    public static void main(String[] args) {
        // Set system look and feel for better integration
        SwingUtilities.invokeLater(() -> {
            try {
                // Try to use system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // Optional: Set some global UI properties
                UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 12));
                UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 11));
                
            } catch (Exception e) {
                System.err.println("Warning: Could not set system look and feel. Using default.");
                e.printStackTrace();
            }
            
            try {
                // Create and show the main frame
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
                
                // Center on screen
                mainFrame.setLocationRelativeTo(null);
                
                System.out.println("✅ Library Management System GUI launched successfully!");
                
            } catch (Exception e) {
                // Handle any startup errors gracefully
                System.err.println("❌ Failed to launch Library Management System GUI:");
                e.printStackTrace();
                
                JOptionPane.showMessageDialog(null, 
                    "❌ Failed to start the Library Management System!\n" +
                    "Error: " + e.getMessage() + "\n\n" +
                    "Please check your database connection and try again.",
                    "Startup Error", 
                    JOptionPane.ERROR_MESSAGE);
                
                System.exit(1);
            }
        });
    }
}