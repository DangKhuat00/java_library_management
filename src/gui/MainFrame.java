package gui;

import model.Library;
import javax.swing.*;

/**
 * MainFrame la cua so chinh cua ung dung quan ly thu vien. Chua cac tab chinh nhu quan ly tai lieu,
 * quan ly nguoi dung va muon/tra tai lieu.
 */
public class MainFrame extends JFrame {

  /**
   * Khoi tao MainFrame voi cac tab chinh va cau hinh cua so. Mac dinh gom 3 tab: Documents, Users,
   * Borrow/Return.
   */
  public MainFrame() {
    setTitle("Library Management System");
    setSize(600, 400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    Library library = new Library(); // tao mot thu vien chung

    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.addTab("Documents", new DocumentPanel()); // ✅ khong truyen Library
    tabbedPane.addTab("Users", new UserPanel()); // ✅ van can
    tabbedPane.addTab("Borrow/Return", new BorrowPanel()); // ✅ van can

    add(tabbedPane);
  }
}
