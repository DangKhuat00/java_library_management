package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import model.Library;


public class BorrowPanel extends JPanel {
    private JTextArea display;
    private Library library;

    public BorrowPanel() {
        library = new Library();
        setupGUI();
    }

    private void setupGUI() {
        setLayout(new BorderLayout());

        display = new JTextArea("Welcome to Borrow/Return Management!\n");
        display.setEditable(false);
        display.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(display);

        JPanel buttons = new JPanel(new GridLayout(1, 3, 5, 5));
        buttons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        buttons.add(createButton("📤 Borrow", this::borrowDocument));
        buttons.add(createButton("📥 Return", this::returnDocument));
        buttons.add(createButton("📋 View All", this::viewAll));

        add(scroll, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.addActionListener(action);
        return btn;
    }

    private void borrowDocument(ActionEvent e) {
        String userId = JOptionPane.showInputDialog("Enter User ID:");
        if (userId == null || userId.trim().isEmpty()) return;

        String docId = JOptionPane.showInputDialog("Enter Document ID:");
        if (docId == null || docId.trim().isEmpty()) return;

        if (library.borrowDocument(userId.trim(), docId.trim())) {
            display.append("✅ Document borrowed successfully\n");
        } else {
            display.append("❌ Failed to borrow document\n");
        }
    }

    private void returnDocument(ActionEvent e) {
        String userId = JOptionPane.showInputDialog("Enter User ID:");
        if (userId == null || userId.trim().isEmpty()) return;

        String docId = JOptionPane.showInputDialog("Enter Document ID:");
        if (docId == null || docId.trim().isEmpty()) return;

        if (library.returnDocument(userId.trim(), docId.trim())) {
            display.append("✅ Document returned successfully\n");
        } else {
            display.append("❌ Failed to return document\n");
        }
    }

    private void viewAll(ActionEvent e) {
        display.setText("📋 All borrow/return records feature not implemented yet\n");
        // Bạn có thể triển khai thêm tính năng xem lịch sử mượn trả ở đây
    }
}
