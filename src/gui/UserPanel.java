package gui;

import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import model.Library;


public class UserPanel extends JPanel {
    private JTextArea display;
    private Library library;

    public UserPanel() {
        library = new Library();
        setupGUI();
    }

    private void setupGUI() {
        setLayout(new BorderLayout());

        display = new JTextArea("Welcome to User Management!\n");
        display.setEditable(false);
        display.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(display);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 5, 5));
        buttons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        buttons.add(createButton("👤 Add User", this::addUser));
        buttons.add(createButton("📋 View All Users", this::viewAllUsers));

        add(scroll, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.addActionListener(action);
        return btn;
    }

    private void addUser(ActionEvent e) {
        String name = JOptionPane.showInputDialog("User Name:");
        if (name == null || name.trim().isEmpty()) return;

        String email = JOptionPane.showInputDialog("Email:");
        if (email == null || email.trim().isEmpty()) return;

        String phone = JOptionPane.showInputDialog("Phone Number:");
        if (phone == null) phone = "";

        if (library.addUser(name, email, phone, 10)) {
            display.append("✅ User added: " + name + "\n");
        } else {
            display.append("❌ Failed to add user\n");
        }
    }

    private void viewAllUsers(ActionEvent e) {
        display.setText("👥 ALL USERS\n" + "=".repeat(40) + "\n");
        List<User> users = library.getAllUsers();
        if (users.isEmpty()) {
            display.append("No users found\n");
        } else {
            for (User user : users) {
                display.append(String.format("ID: %s | %s (%s)\n",
                        user.getId(), user.getName(), user.getEmail()));
            }
        }
    }
}
