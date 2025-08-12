package gui;

import javax.swing.*;
import java.awt.*;

public class LibraryAppGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 12));
                UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 11));
            } catch (Exception e) {
                System.err.println("Không thể cài đặt giao diện hệ thống.");
                e.printStackTrace();
            }

            try {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            } catch (Exception e) {
                System.err.println("Lỗi khởi động giao diện đăng nhập:");
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Lỗi khởi động ứng dụng!\n" +
                        "Error: " + e.getMessage(),
                    "Lỗi khởi động",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
