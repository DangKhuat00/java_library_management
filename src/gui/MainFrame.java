package gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
  public MainFrame() {
    setTitle("📚 Library Management System");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(900, 600);
    setLocationRelativeTo(null);

    // Layout chính
    setLayout(new BorderLayout());

    // Tạo tab chức năng
    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.addTab("Documents", new DocumentPanel());
    tabbedPane.addTab("Users", new UserPanel());
    tabbedPane.addTab("Borrow/Return", new BorrowPanel());

    // Thêm tab vào giữa
    add(tabbedPane, BorderLayout.CENTER);

    // Panel chứa nút đăng xuất
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton logoutButton = new JButton("Đăng xuất");
    bottomPanel.add(logoutButton);
    add(bottomPanel, BorderLayout.SOUTH);

    // Xử lý nút đăng xuất
    logoutButton.addActionListener(
        e -> {
          int confirm =
              JOptionPane.showConfirmDialog(
                  this, "Bạn có chắc muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
          if (confirm == JOptionPane.YES_OPTION) {
            // Quay về màn hình đăng nhập
            new LoginFrame().setVisible(true);
            dispose(); // đóng cửa sổ MainFrame
          }
        });
  }
}
