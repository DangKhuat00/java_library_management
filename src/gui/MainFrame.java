package gui;

import model.Library;
import javax.swing.*;

public class MainFrame extends JFrame {
  public MainFrame() {
    setTitle("Library Management System");
    setSize(600, 400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    Library library = new Library(); // tạo một thư viện chung

    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.addTab("Documents", new DocumentPanel()); // ✅ KHÔNG truyền Library
    tabbedPane.addTab("Users", new UserPanel());  // ✅ Vẫn cần
    tabbedPane.addTab("Borrow/Return", new BorrowPanel()); // ✅ Vẫn cần

    add(tabbedPane);
  }
}