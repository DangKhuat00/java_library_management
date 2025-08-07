package gui;

import dao.DatabaseConnection;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MainFrame extends JFrame {
    private JTextArea display;
    private Library library;
    
    public MainFrame() {
        if (!DatabaseConnection.testConnection()) {
            JOptionPane.showMessageDialog(null, "❌ Database connection failed!");
            System.exit(1);
        }
        
        library = new Library();
        setupGUI();
    }
    
    private void setupGUI() {
        setTitle("📚 Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        // Main layout
        JPanel main = new JPanel(new BorderLayout());
        
        // Header
        JLabel header = new JLabel("📚 Library Management System", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.setOpaque(true);
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        
        // Display area
        display = new JTextArea("Welcome to Library Management System!\n");
        display.setEditable(false);
        display.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(display);
        
        // Buttons
        JPanel buttons = createButtonPanel();
        
        main.add(header, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);
        main.add(buttons, BorderLayout.SOUTH);
        
        add(main);
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 3, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Row 1: Document operations
        panel.add(createButton("📖 Add Book", this::addBook));
        panel.add(createButton("📰 Add Magazine", this::addMagazine));
        panel.add(createButton("✏️ Update Doc", this::updateDocument));
        
        // Row 2: More document operations
        panel.add(createButton("🗑️ Remove Doc", this::removeDocument));
        panel.add(createButton("🔍 Find Doc", this::findDocument));
        panel.add(createButton("👤 Add User", this::addUser));
        
        // Row 3: Borrow operations & View
        panel.add(createButton("📤 Borrow", this::borrowDocument));
        panel.add(createButton("📥 Return", this::returnDocument));
        panel.add(createButton("📋 View All", this::viewAll));
        
        return panel;
    }
    
    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.addActionListener(action);
        return btn;
    }
    
    // Document operations
    private void addBook(ActionEvent e) {
        String title = JOptionPane.showInputDialog("Book Title:");
        if (title == null || title.trim().isEmpty()) return;
        
        String author = JOptionPane.showInputDialog("Author:");
        if (author == null || author.trim().isEmpty()) return;
        
        String yearStr = JOptionPane.showInputDialog("Publication Year:");
        String pagesStr = JOptionPane.showInputDialog("Number of Pages:");
        
        try {
            int year = Integer.parseInt(yearStr);
            int pages = Integer.parseInt(pagesStr);
            
            if (library.addDocument(title, author, year, "BOOK", pages, 0)) {
                display.append("✅ Book added: " + title + "\n");
            } else {
                display.append("❌ Failed to add book\n");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format!");
        }
    }
    
    private void addMagazine(ActionEvent e) {
        String title = JOptionPane.showInputDialog("Magazine Title:");
        if (title == null || title.trim().isEmpty()) return;
        
        String author = JOptionPane.showInputDialog("Author:");
        if (author == null || author.trim().isEmpty()) return;
        
        String yearStr = JOptionPane.showInputDialog("Publication Year:");
        String issueStr = JOptionPane.showInputDialog("Issue Number:");
        
        try {
            int year = Integer.parseInt(yearStr);
            int issue = Integer.parseInt(issueStr);
            
            if (library.addDocument(title, author, year, "MAGAZINE", 0, issue)) {
                display.append("✅ Magazine added: " + title + "\n");
            } else {
                display.append("❌ Failed to add magazine\n");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format!");
        }    }
    
    private void updateDocument(ActionEvent e) {
        String docId = JOptionPane.showInputDialog("Enter Document ID to update:");
        if (docId == null || docId.trim().isEmpty()) return;
        
        String title = JOptionPane.showInputDialog("New Title:");
        if (title == null || title.trim().isEmpty()) return;
        
        String author = JOptionPane.showInputDialog("New Author:");
        if (author == null || author.trim().isEmpty()) return;
        
        String yearStr = JOptionPane.showInputDialog("New Publication Year:");
        String type = JOptionPane.showInputDialog("Type (BOOK/MAGAZINE):");
        if (type == null || type.trim().isEmpty()) return;
        
        try {
            int year = Integer.parseInt(yearStr);
            type = type.trim().toUpperCase();
            
            int pages = 0, issue = 0;
            if (type.equals("BOOK")) {
                String pagesStr = JOptionPane.showInputDialog("Number of Pages:");
                pages = Integer.parseInt(pagesStr);
            } else if (type.equals("MAGAZINE")) {
                String issueStr = JOptionPane.showInputDialog("Issue Number:");
                issue = Integer.parseInt(issueStr);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid document type!");
                return;
            }
            
            if (library.updateDocument(docId.trim(), title, author, year, type, pages, issue)) {
                display.append("✅ Document " + docId + " updated successfully\n");
            } else {
                display.append("❌ Failed to update document " + docId + "\n");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format!");
        }
    }

    private void removeDocument(ActionEvent e) {
        String docId = JOptionPane.showInputDialog("Enter Document ID to remove:");
        if (docId != null && !docId.trim().isEmpty()) {
            if (library.removeDocument(docId.trim())) {
                display.append("✅ Document " + docId + " removed\n");
            } else {
                display.append("❌ Failed to remove document " + docId + "\n");
            }
        }
    }
    
    private void findDocument(ActionEvent e) {
        String keyword = JOptionPane.showInputDialog("Enter search keyword:");
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<Document> docs = library.findDocuments(keyword.trim());
            display.append("\n🔍 Search results for: " + keyword + "\n");
            if (docs.isEmpty()) {
                display.append("No documents found\n");
            } else {
                for (Document doc : docs) {
                    display.append("- " + doc.getId() + ": " + doc.getTitle() + " by " + doc.getAuthor() + "\n");
                }
            }
        }
    }
    
    // User operations
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
    
    // Borrow operations
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
    
    // View operations
    private void viewAll(ActionEvent e) {
        String[] options = {"Documents", "Users", "Clear Display"};
        int choice = JOptionPane.showOptionDialog(this, "What do you want to view?", "View All",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        switch (choice) {
            case 0: // Documents
                display.setText("📚 ALL DOCUMENTS\n" + "=".repeat(40) + "\n");
                List<Document> docs = library.getAllDocuments();
                if (docs.isEmpty()) {
                    display.append("No documents found\n");
                } else {
                    for (Document doc : docs) {
                        display.append(String.format("ID: %s | %s by %s (%d)\n", 
                                doc.getId(), doc.getTitle(), doc.getAuthor(), doc.getYear()));
                    }
                }
                break;
                
            case 1: // Users
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
                break;
                
            case 2: // Clear
                display.setText("Welcome to Library Management System!\n");
                break;
        }
    }
}