package gui;

import model.Book;
import model.Document;
import model.Library;
import model.Magazine;
import model.DocumentType;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class DocumentPanel extends JPanel {
    private Library library;

    private JTable table;
    private DefaultTableModel tableModel;

    public DocumentPanel() {
        library = new Library();
        setupGUI();
    }

    private void setupGUI() {
        setLayout(new BorderLayout());

        // Các cột bảng
        String[] columns = {"ID", "Title", "Author", "Year", "Type", "Pages", "Issue"};
        tableModel = new DefaultTableModel(columns, 0) {
            // Không cho sửa dữ liệu trực tiếp trên bảng
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);

        // Panel chứa các nút
        JPanel buttons = new JPanel(new GridLayout(2, 3, 5, 5));
        buttons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        buttons.add(createButton("📖 Add Book", this::addBook));
        buttons.add(createButton("📰 Add Magazine", this::addMagazine));
        buttons.add(createButton("✏️ Update Doc", this::updateDocument));
        buttons.add(createButton("🗑️ Remove Doc", this::removeDocument));
        buttons.add(createButton("🔍 Find Doc", this::findDocument));
        buttons.add(createButton("📋 View All Docs", e -> loadAllDocuments()));

        add(scroll, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.addActionListener(action);
        return btn;
    }

    private void loadAllDocuments() {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ

        List<Document> docs = library.getAllDocuments();

        if (docs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No documents found!");
            return;
        }

        for (Document doc : docs) {
            Object pages = null;
            Object issue = null;
            System.out.println(doc.getDocumentType());
            if(doc.getDocumentType() == DocumentType.BOOK) {
              pages = ((Book) doc).getNumberOfPages();
              System.out.println("Book pages: " + pages);
            } else if (doc.getDocumentType() == DocumentType.MAGAZINE) {
              issue = ((Magazine) doc).getIssueNumber();
              System.out.println("Magazine issue: " + issue);
            }
            Object[] row = {
                doc.getId(),
                doc.getTitle(),
                doc.getAuthor(),
                doc.getYear(),
                doc.getDocumentType(),
                pages,
                issue
            };
            tableModel.addRow(row);
        }
    }

    // Các phương thức xử lý tương tự của bạn, nhưng nhớ gọi loadAllDocuments() sau khi thêm/sửa/xóa thành công để cập nhật bảng

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
                JOptionPane.showMessageDialog(this, "Book added: " + title);
                loadAllDocuments();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add book.");
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
                JOptionPane.showMessageDialog(this, "Magazine added: " + title);
                loadAllDocuments();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add magazine.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format!");
        }
    }

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
                JOptionPane.showMessageDialog(this, "Document " + docId + " updated successfully.");
                loadAllDocuments();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update document " + docId);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format!");
        }
    }

    private void removeDocument(ActionEvent e) {
        String docId = JOptionPane.showInputDialog("Enter Document ID to remove:");
        if (docId != null && !docId.trim().isEmpty()) {
            if (library.removeDocument(docId.trim())) {
                JOptionPane.showMessageDialog(this, "Document " + docId + " removed.");
                loadAllDocuments();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to remove document " + docId);
            }
        }
    }

    private void findDocument(ActionEvent e) {
        String keyword = JOptionPane.showInputDialog("Enter search keyword:");
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<Document> docs = library.findDocuments(keyword.trim());

            if (docs.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No documents found!");
                return;
            }

            // Xóa bảng hiện tại và hiển thị kết quả tìm kiếm
        tableModel.setRowCount(0);
          for (Document doc : docs) {
            Object pages = null;
            Object issue = null;
            if("BOOK".equals(doc.getDocumentType()) && doc instanceof Book) {
              pages = ((Book) doc).getNumberOfPages();
            } else if ("MAGAZINE".equals(doc.getDocumentType()) && doc instanceof Magazine) {
              issue = ((Magazine) doc).getIssueNumber();
            }
            Object[] row = {
              doc.getId(),
              doc.getTitle(),
              doc.getAuthor(),
              doc.getYear(),
              doc.getDocumentType(),
              pages,
              issue
            };
          tableModel.addRow(row);
      }
    }
  }
}
