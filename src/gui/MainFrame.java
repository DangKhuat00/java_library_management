package gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
  public MainFrame() {
    setTitle("Library Management System - GUI");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    JTabbedPane tabs = new JTabbedPane();
    tabs.add("Documents", new DocumentPanel());
    tabs.add("Users", new UserPanel());
    tabs.add("Borrow/Return", new BorrowPanel());

    add(tabs);
  }
}