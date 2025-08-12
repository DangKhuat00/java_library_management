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

    private JButton btnAdd, btnUpdate, btnRemove, btnSearch, btnReset, btnClear;

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

        // ===== Panel phía trên (chứa Form và Điều khiển) =====
        JPanel northPanel = new JPanel(new BorderLayout(5, 5));

        // ===== Form nhập (bên trái) =====
        JPanel formPanel = new JPanel(new GridLayout(5, 1, 0, 8));
        tfName = createField("Name:", formPanel, 250);
        tfEmail = createField("Email:", formPanel, 250);
        tfPhone = createField("Phone:", formPanel, 250);
        tfBorrowLimit = createField("Borrow Limit:", formPanel, 250);
        tfBorrowLimit.setText("10"); // Mặc định giới hạn mượn
        tfBorrowedCount = createField("Borrowed Count:", formPanel, 250);

        // THAY ĐỔI: Khởi tạo giá trị và làm cho không thể sửa
        tfBorrowedCount.setText("0");
        tfBorrowedCount.setEditable(false);

        northPanel.add(formPanel, BorderLayout.WEST);

        // ===== Khu vực điều khiển (bên phải) =====
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        // --- Panel Lọc và Tìm kiếm ---
        JPanel searchAndFilterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));

        // Khai báo các thành phần
        tfSearch = new JTextField();
        tfSearch.setPreferredSize(new Dimension(250, 28)); // Giảm chiều rộng để cân đối
        btnSearch = new JButton("🔍 Search");
        btnReset = new JButton("Reset");
        cbFilter = new JComboBox<>(new String[] { "All Fields", "Name", "Email", "Phone" });
        JLabel lblFilter = new JLabel("Filter by:");

        // Thêm các thành phần theo thứ tự mới: Filter -> Search
        searchAndFilterPanel.add(lblFilter);
        searchAndFilterPanel.add(cbFilter);
        searchAndFilterPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Thêm khoảng cách
        searchAndFilterPanel.add(tfSearch);
        searchAndFilterPanel.add(btnSearch);
        searchAndFilterPanel.add(btnReset);

        // --- Panel Nút chức năng ---
        JPanel actionButtonPanel = new JPanel(new GridLayout(1, 4, 10, 5));
        btnAdd = new JButton("➕ Add");
        btnUpdate = new JButton("✏️ Update");
        btnRemove = new JButton("🗑️ Remove");
        btnClear = new JButton("Clear Form");
        actionButtonPanel.add(btnAdd);
        actionButtonPanel.add(btnUpdate);
        actionButtonPanel.add(btnRemove);
        actionButtonPanel.add(btnClear);

        JPanel buttonWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrapperPanel.add(actionButtonPanel);

        // Căn giữa các khối điều khiển
        searchAndFilterPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonWrapperPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Thêm các khối vào rightPanel với "keo" để căn giữa theo chiều dọc
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(searchAndFilterPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPanel.add(buttonWrapperPanel);
        rightPanel.add(Box.createVerticalGlue());

        northPanel.add(rightPanel, BorderLayout.CENTER);

        // Thêm northPanel vào panel chính
        add(northPanel, BorderLayout.NORTH);

        // ===== Bảng dữ liệu =====
        String[] columns = { "ID", "Name", "Email", "Phone", "Borrow Limit", "Borrowed Count" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(22);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JTextField createField(String label, JPanel parent, int width) {
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JLabel lb = new JLabel(label);
        lb.setPreferredSize(new Dimension(110, 28));
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
            tableModel.addRow(new Object[] {
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
        // THAY ĐỔI: Bỏ kiểm tra trường tfBorrowedCount vì nó không thể rỗng
        if (tfName.getText().trim().isEmpty() ||
                tfEmail.getText().trim().isEmpty() ||
                tfPhone.getText().trim().isEmpty() ||
                tfBorrowLimit.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields except Borrowed Count.");
            return false;
        }
        try {
            Integer.parseInt(tfBorrowLimit.getText().trim());
            // Không cần parse tfBorrowedCount ở đây nữa vì nó do hệ thống quản lý
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Borrow Limit must be a number.");
            return false;
        }
        return true;
    }

    private void clearForm() {
        tfName.setText("");
        tfEmail.setText("");
        tfPhone.setText("");
        tfBorrowLimit.setText("10");
        tfBorrowedCount.setText("0"); // THAY ĐỔI: Đặt lại là "0"
        selectedId = -1;
        table.clearSelection();
    }

    private void setupEvents() {
        btnClear.addActionListener(e -> clearForm());

        btnAdd.addActionListener(e -> {
            if (validateInput()) {
                User user = new User(
                        tfName.getText().trim(),
                        tfEmail.getText().trim(),
                        tfPhone.getText().trim(),
                        Integer.parseInt(tfBorrowLimit.getText().trim()),
                        0 // THAY ĐỔI: Người dùng mới luôn có 0 sách đã mượn
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
                        // Giữ nguyên số sách đã mượn khi cập nhật thông tin khác
                        Integer.parseInt(tfBorrowedCount.getText().trim()));
                if (library.updateUser(user)) {
                    JOptionPane.showMessageDialog(this, "User updated successfully.");
                    loadAllUsers();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update user.");
                }
            }
        });

        btnRemove.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to remove.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(
                    this, "Are you sure to delete this user?", "Confirm", JOptionPane.YES_NO_OPTION);
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

        btnSearch.addActionListener(e -> {
            String keyword = tfSearch.getText().trim();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search keyword.");
                return;
            }
            String filter = cbFilter.getSelectedItem().toString();
            List<User> users = library.findUsers(keyword, filter);

            tableModel.setRowCount(0);
            for (User user : users) {
                tableModel.addRow(new Object[] {
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getBorrowLimit(),
                        user.getBorrowedBooksCount()
                });
            }
        });

        btnReset.addActionListener(e -> {
            tfSearch.setText("");
            cbFilter.setSelectedIndex(0);
            loadAllUsers();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                selectedId = (int) table.getValueAt(row, 0);
                tfName.setText((String) table.getValueAt(row, 1));
                tfEmail.setText((String) table.getValueAt(row, 2));
                tfPhone.setText((String) table.getValueAt(row, 3));
                tfBorrowLimit.setText(String.valueOf(table.getValueAt(row, 4)));
                tfBorrowedCount.setText(String.valueOf(table.getValueAt(row, 5)));
            }
        });
    }
}