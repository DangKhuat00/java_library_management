package gui;

import dao.BorrowDAO.BorrowRecord;
import model.Library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BorrowPanel extends JPanel {
    private Library library;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField tfUserId, tfDocId, tfDocTitle, tfBorrowDate, tfReturnDate, tfSearch;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public BorrowPanel() {
        library = new Library();
        setLayout(new BorderLayout());

        // ===== Form nhập =====
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.add(new JLabel("User ID:"));
        tfUserId = new JTextField();
        formPanel.add(tfUserId);

        formPanel.add(new JLabel("Document ID:"));
        tfDocId = new JTextField();
        formPanel.add(tfDocId);

        formPanel.add(new JLabel("Document Title:"));
        tfDocTitle = new JTextField();
        formPanel.add(tfDocTitle);

        formPanel.add(new JLabel("Borrow Date (yyyy-MM-dd):"));
        tfBorrowDate = new JTextField();
        formPanel.add(tfBorrowDate);

        formPanel.add(new JLabel("Return Date (yyyy-MM-dd):"));
        tfReturnDate = new JTextField();
        formPanel.add(tfReturnDate);

        // ===== Nút chức năng =====
        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnClear = new JButton("Clear Form");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnClear);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(formPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);

        // ===== Bảng hiển thị =====
        tableModel = new DefaultTableModel(
                new String[]{"User ID", "Document ID", "Document Title", "Borrow Date", "Return Date"}, 0
        );
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // ===== Thanh tìm kiếm =====
        JPanel searchPanel = new JPanel(new BorderLayout());
        tfSearch = new JTextField();
        JButton btnSearch = new JButton("Search");
        searchPanel.add(tfSearch, BorderLayout.CENTER);
        searchPanel.add(btnSearch, BorderLayout.EAST);
        add(searchPanel, BorderLayout.SOUTH);

        // ===== Sự kiện =====
        btnAdd.addActionListener(e -> {
            if (library.borrowDocument(tfUserId.getText(), tfDocId.getText())) {
                loadBorrowData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Mượn tài liệu thất bại.");
            }
        });


        btnUpdate.addActionListener(e -> {
            if (library.returnDocument(tfUserId.getText(), tfDocId.getText())) {
                loadBorrowData();
            } else {
                JOptionPane.showMessageDialog(this, "Trả tài liệu thất bại.");
            }
        });


        btnClear.addActionListener(e -> clearForm());

        btnSearch.addActionListener(e -> {
            String keyword = tfSearch.getText().trim().toLowerCase();
            searchBorrow(keyword);
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                tfUserId.setText(tableModel.getValueAt(row, 0).toString());
                tfDocId.setText(tableModel.getValueAt(row, 1).toString());
                tfDocTitle.setText(tableModel.getValueAt(row, 2).toString());
                tfBorrowDate.setText(tableModel.getValueAt(row, 3).toString());
                tfReturnDate.setText(tableModel.getValueAt(row, 4).toString());
            }
        });

        // Load dữ liệu ban đầu
        loadBorrowData();
    }

    private void loadBorrowData() {
        tableModel.setRowCount(0);
        List<BorrowRecord> borrows = library.getAllBorrowRecords();
        for (BorrowRecord b : borrows) {
            tableModel.addRow(new Object[]{
                b.userId,
                b.documentId,
                b.documentTitle,
                b.borrowedDate != null ? sdf.format(b.borrowedDate) : "",
                b.returnDate != null ? sdf.format(b.returnDate) : ""
            });
        }
    }


    private void searchBorrow(String keyword) {
        tableModel.setRowCount(0);
        for (BorrowRecord b : library.getAllBorrowRecords()) {
            if (b.userId.toLowerCase().contains(keyword) ||
                b.documentId.toLowerCase().contains(keyword) ||
                b.documentTitle.toLowerCase().contains(keyword)) {
                tableModel.addRow(new Object[]{
                    b.userId,
                    b.documentId,
                    b.documentTitle,
                    b.borrowedDate != null ? sdf.format(b.borrowedDate) : "",
                    b.returnDate != null ? sdf.format(b.returnDate) : ""
                });
            }
        }
    }


    private void clearForm() {
        tfUserId.setText("");
        tfDocId.setText("");
        tfDocTitle.setText("");
        tfBorrowDate.setText("");
        tfReturnDate.setText("");
    }
}
