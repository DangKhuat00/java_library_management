package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Khoi dong ung dung giao dien quan ly thu vien
 * Cung cap giao dien hien dai, de su dung cho cac chuc nang thu vien
 */
public class LibraryAppGUI {
    /**
     * Ham main khoi dong giao dien thu vien
     */
    public static void main(String[] args) {
        // Dat giao dien theo he dieu hanh de dong bo voi may tinh
        SwingUtilities.invokeLater(() -> {
            try {
                // Co gang su dung giao dien he thong
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                // Cai dat mot so thuoc tinh UI toan cuc
                UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 12));
                UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 11));
            } catch (Exception e) {
                // Thong bao neu khong cai duoc giao dien he thong
                System.err.println("Khong the cai dat giao dien he thong. Su dung giao dien mac dinh.");
                e.printStackTrace();
            }
            try {
                // Tao va hien thi cua so chinh
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
                // Can giua man hinh
                mainFrame.setLocationRelativeTo(null);
                System.out.println("Khoi dong giao dien quan ly thu vien thanh cong!");
            } catch (Exception e) {
                // Xu ly loi khi khoi dong giao dien
                System.err.println("Loi khoi dong giao dien quan ly thu vien:");
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Loi khoi dong ung dung quan ly thu vien!\n" +
                    "Error: " + e.getMessage() + "\n\n" +
                    "Vui long kiem tra ket noi co so du lieu va thu lai.",
                    "Loi khoi dong", 
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}