package gui;

import javax.swing.*;

/**
 * Giao dien chinh cua chuong trinh quan ly thu vien
 * Chua cac tab quan ly tai lieu, nguoi dung va muon/tra
 */
public class MainFrame extends JFrame {
    /**
     * Khoi tao cua so chinh va cac tab chuc nang
     */
    public MainFrame() {
        // Dat tieu de cua so
        setTitle("📚 Library Management System");
        // Thoat chuong trinh khi dong cua so
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Dat kich thuoc cua so
        setSize(900, 600);
        // Can giua cua so tren man hinh
        setLocationRelativeTo(null);

        // Tao tab chuc nang
        JTabbedPane tabbedPane = new JTabbedPane();

        // Them cac tab vao giao dien
        tabbedPane.addTab("Documents", new DocumentPanel());
        tabbedPane.addTab("Users", new UserPanel());
        tabbedPane.addTab("Borrow/Return", new BorrowPanel());

        // Them tabbedPane vao cua so
        add(tabbedPane);
    }
}
