package gui;

import model.User;
import model.Library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserPanel extends JPanel {
    private Library library;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField tfName, tfEmail, tfPhone, tfBorrowLimit, tfBorrowedCount;
    private JTextField tfSearch;
    private JComboBox<String> cbFilter;

    private JButton btnAdd, btnUpdate, btnRemove, btnSearch, btnReset;

    private int selectedId = -1;

    public UserPanel() {
        library = new Library();
        setupGUI();
        loadAllUsers();
        setupEvents();
    }

    private void setupGUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // === Panel Search + Filter ===
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        cbFilter = new JComboBox<>(new String[]{
                "All Fields", "Name", "Email", "Phone", "Borrow Limit", "Borrowed Count"
        });
        tfSearch = new JTextField(35);
        btnSearch = new JButton("🔍 Search");
        btnReset = new JButton("Reset");

        searchPanel.add(new JLabel("Filter by:"));
        searchPanel.add(cbFilter);
        searchPanel.add(tfSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnReset);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        add(searchPanel, BorderLayout.NORTH);

        // === Form nhập dữ liệu ===
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 10));

        tfName = createField("Name:", formPanel, 250);
        tfEmail = createField("Email:", formPanel, 250);
        tfPhone = createField("Phone:", formPanel, 250);
        tfBorrowLimit = createField("Borrow Limit:", formPanel, 250);
        tfBorrowedCount = createField("Borrowed Count:", formPanel, 250);

        // === Panel chứa form + nút ===
        JPanel formAndButtonPanel = new JPanel(new BorderLayout(5, 5));
        formAndButtonPanel.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnAdd = new JButton("➕ Add");
        btnUpdate = new JButton("✏️ Update");
        btnRemove = new JButton("🗑️ Remove");
        JButton btnClear = new JButton("Clear Form");

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnRemove);
        btnPanel.add(btnClear);

        formAndButtonPanel.add(btnPanel, BorderLayout.SOUTH);
        formAndButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        add(formAndButtonPanel, BorderLayout.CENTER);

        // === Bảng dữ liệu ===
        String[] columns = {"ID", "Name", "Email", "Phone", "Borrow Limit", "Borrowed Count"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(22);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 200));
        add(scrollPane, BorderLayout.SOUTH);

        // Clear form event
        btnClear.addActionListener(e -> clearForm());
    }

    private JTextField createField(String label, JPanel parent, int width) {
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        JLabel lb = new JLabel(label);
        lb.setPreferredSize(new Dimension(110, 36));
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(width, 28));

        fieldPanel.add(lb);
        fieldPanel.add(tf);
        parent.add(fieldPanel);
        return tf;
    }

    private void loadAllUsers() {
        tableModel.setRowCount(0);
        List<User> users = library.getAllUsers();
        for (User user : users) {
            tableModel.addRow(new Object[]{
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getBorrowLimit(),
                    user.getBorrowedBooksCount()
            });
        }
    }

    private boolean validateInput() {
        if (tfName.getText().trim().isEmpty() ||
                tfEmail.getText().trim().isEmpty() ||
                tfPhone.getText().trim().isEmpty() ||
                tfBorrowLimit.getText().trim().isEmpty() ||
                tfBorrowedCount.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return false;
        }
        try {
            Integer.parseInt(tfBorrowLimit.getText().trim());
            Integer.parseInt(tfBorrowedCount.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Borrow Limit and Borrowed Count must be numbers.");
            return false;
        }
        return true;
    }

    private void clearForm() {
        tfName.setText("");
        tfEmail.setText("");
        tfPhone.setText("");
        tfBorrowLimit.setText("");
        tfBorrowedCount.setText("");
        selectedId = -1;
        table.clearSelection();
    }

    private void setupEvents() {
        // Add user
        btnAdd.addActionListener(e -> {
            if (validateInput()) {
                User user = new User(   
                        tfName.getText().trim(),
                        tfEmail.getText().trim(),
                        tfPhone.getText().trim(),
                        Integer.parseInt(tfBorrowLimit.getText().trim()),
                        Integer.parseInt(tfBorrowedCount.getText().trim())
                );
                if (library.addUser(user)) {
                    JOptionPane.showMessageDialog(this, "User added successfully.");
                    loadAllUsers();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add user.");
                }
            }
        });

        // Update user
        btnUpdate.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to update.");
                return;
            }
            if (validateInput()) {
                User user = new User(
                        selectedId,
                        tfName.getText().trim(),
                        tfEmail.getText().trim(),
                        tfPhone.getText().trim(),
                        Integer.parseInt(tfBorrowLimit.getText().trim()),
                        Integer.parseInt(tfBorrowedCount.getText().trim())
                );
                if (library.updateUser(user)) {
                    JOptionPane.showMessageDialog(this, "User updated successfully.");
                    loadAllUsers();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update user.");
                }
            }
        });

        // Remove user
        btnRemove.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to remove.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(
                    this, "Are you sure to delete this user?", "Confirm", JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                if (library.deleteUser(selectedId)) {
                    JOptionPane.showMessageDialog(this, "User removed successfully.");
                    loadAllUsers();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to remove user.");
                }
            }
        });

        // Search user
        btnSearch.addActionListener(e -> {
            String keyword = tfSearch.getText().trim();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search keyword.");
                return;
            }
            List<User> users = library.findUsers(keyword); // cần viết findUsers trong Library
            tableModel.setRowCount(0);
            for (User user : users) {
                tableModel.addRow(new Object[]{
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getBorrowLimit(),
                        user.getBorrowedBooksCount()
                });
            }
        });

        // Reset
        btnReset.addActionListener(e -> {
            tfSearch.setText("");
            loadAllUsers();
        });

        // Click vào bảng load form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                selectedId = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
                tfName.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
                tfEmail.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
                tfPhone.setText(table.getValueAt(table.getSelectedRow(), 3).toString());
                tfBorrowLimit.setText(table.getValueAt(table.getSelectedRow(), 4).toString());
                tfBorrowedCount.setText(table.getValueAt(table.getSelectedRow(), 5).toString());
            }
        });
    }
}
