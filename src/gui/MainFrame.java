package gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("📚 Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Documents", new DocumentPanel());
        tabbedPane.addTab("Users", new UserPanel());
        tabbedPane.addTab("Borrow/Return", new BorrowPanel());

        add(tabbedPane);
    }
}
