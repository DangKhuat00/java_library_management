package gui;

import model.User;
import model.UserFilter;
import model.Library;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import gui.RoundedButton;
public class UserPanel extends JPanel {
    private Library library;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField tfName, tfEmail, tfPhone, tfBorrowLimit, tfBorrowedCount;
    private JTextField tfSearch;
    private JComboBox<String> cbFilter;

    private RoundedButton btnAdd, btnUpdate, btnRemove, btnSearch, btnReset, btnClear;

    private int selectedId = -1;

    private static final Dimension BTN_SIZE_PRIMARY = new Dimension(140, 36);
    private static final Dimension BTN_SIZE_SMALL = new Dimension(110, 34);

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

        tfBorrowedCount.setText("0");
        tfBorrowedCount.setEditable(false);

        northPanel.add(formPanel, BorderLayout.WEST);

        // ===== Khu vực điều khiển (bên phải) =====
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        // --- Panel Lọc và Tìm kiếm ---
        JPanel searchAndFilterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        tfSearch = new JTextField();
        tfSearch.setPreferredSize(new Dimension(250, 28));
        btnSearch = new RoundedButton("Search");
        btnSearch.setPreferredSize(BTN_SIZE_SMALL);
        btnReset = new RoundedButton("Reset");
        btnReset.setPreferredSize(BTN_SIZE_SMALL);
        cbFilter = new JComboBox<>(new String[] { "All Fields", "Id", "Name", "Email", "PhoneNumber", "BorrowLimit", "BorrowedCount" });
        cbFilter.setPreferredSize(new Dimension(140, 30));
        JLabel lblFilter = new JLabel("Filter by:");
        lblFilter.setFont(new Font("Segoe UI", Font.BOLD, 13));

        searchAndFilterPanel.add(lblFilter);
        searchAndFilterPanel.add(cbFilter);
        searchAndFilterPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        searchAndFilterPanel.add(tfSearch);
        searchAndFilterPanel.add(btnSearch);
        searchAndFilterPanel.add(btnReset);

        // --- Panel Nút chức năng ---
        JPanel actionButtonPanel = new JPanel(new GridLayout(1, 4, 12, 0));
        btnAdd = new RoundedButton("Add");
        btnAdd.setPreferredSize(BTN_SIZE_PRIMARY);
        btnUpdate = new RoundedButton("Update");
        btnUpdate.setPreferredSize(BTN_SIZE_PRIMARY);
        btnRemove = new RoundedButton("Remove");
        btnRemove.setPreferredSize(BTN_SIZE_PRIMARY);
        btnClear = new RoundedButton("Clear Form");
        btnClear.setPreferredSize(BTN_SIZE_PRIMARY);

        actionButtonPanel.add(btnAdd);
        actionButtonPanel.add(btnUpdate);
        actionButtonPanel.add(btnRemove);
        actionButtonPanel.add(btnClear);

        JPanel buttonWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonWrapperPanel.add(actionButtonPanel);

        searchAndFilterPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonWrapperPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(searchAndFilterPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPanel.add(buttonWrapperPanel);
        rightPanel.add(Box.createVerticalGlue());

        northPanel.add(rightPanel, BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);

        // ===== Bảng dữ liệu =====
        String[] columns = { "ID", "Name", "Email", "PhoneNumber", "BorrowLimit", "BorrowedCount" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(22);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setShowGrid(false);
        table.setDefaultRenderer(Object.class, new GridCellRenderer());

        // Header: in đậm, chữ hoa, căn giữa, kẻ vạch nhẹ
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new HeaderCellRenderer(table));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JTextField createField(String label, JPanel parent, int width) {
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JLabel lb = new JLabel(label);
        lb.setFont(new Font("Segoe UI", Font.BOLD, 13));
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
        if (tfName.getText().trim().isEmpty() ||
            tfEmail.getText().trim().isEmpty() ||
            tfPhone.getText().trim().isEmpty() ||
            tfBorrowLimit.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields except Borrowed Count.");
            return false;
        }
        try {
            Integer.parseInt(tfBorrowLimit.getText().trim());
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
        tfBorrowedCount.setText("0");
        selectedId = -1;
        table.clearSelection();
    }

    private void setupEvents() {
        btnClear.addActionListener(e -> clearForm());

        btnAdd.addActionListener(e -> {
            if (!validateInput())
                return;

            User user = new User(
                tfName.getText().trim(),
                tfEmail.getText().trim(),
                tfPhone.getText().trim(),
                Integer.parseInt(tfBorrowLimit.getText().trim()),
                0);

            if (library.addUser(user)) {
                JOptionPane.showMessageDialog(this, "User added successfully.");
                loadAllUsers();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Phone number already exists or failed to add user.");
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

            UserFilter filter;
            switch (cbFilter.getSelectedItem().toString()) {
                case "Id":
                    filter = UserFilter.ID;
                    break;
                case "Name":
                    filter = UserFilter.NAME;
                    break;
                case "Email":
                    filter = UserFilter.EMAIL;
                    break;
                case "PhoneNumber":
                    filter = UserFilter.PHONE_NUMBER;
                    break;
                case "BorrowLimit":
                    filter = UserFilter.BORROW_LIMIT;
                    break;
                case "BorrowedCount":
                    filter = UserFilter.BORROWED_COUNT;
                    break;
                default:
                    filter = UserFilter.ALL_FIELDS;
            }

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

    // === Renderer cho header: in đậm, chữ hoa, căn giữa, viền nhẹ ===
    private static class HeaderCellRenderer extends DefaultTableCellRenderer {
        private static final Color GRID = new Color(160, 160, 160);
        private static final Color BG = new Color(240, 240, 240);
        private final JTable table;

        public HeaderCellRenderer(JTable table) {
            this.table = table;
            setHorizontalAlignment(CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
            setBackground(BG);

            if (value != null) {
                setText(value.toString().toUpperCase());
            }
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            int right = (column < table.getColumnCount() - 1) ? 1 : 0;
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, right, GRID));
            return this;
        }
    }

    // === Renderer cho cell: căn giữa dữ liệu, viền nhẹ ===
    private static class GridCellRenderer extends DefaultTableCellRenderer {
        private static final Color GRID = new Color(160, 160, 160);
        private static final Color ALT_ROW = new Color(248, 248, 248);

        public GridCellRenderer() {
            setHorizontalAlignment(CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (c instanceof JComponent) {
                JComponent jc = (JComponent) c;
                jc.setOpaque(true);

                if (!isSelected) {
                    jc.setBackground((row % 2 == 0) ? ALT_ROW : Color.WHITE);
                }

                int right = (column < table.getColumnCount() - 1) ? 1 : 0;
                jc.setBorder(BorderFactory.createMatteBorder(0, 0, 1, right, GRID));
            }
            if (c instanceof JLabel) {
                ((JLabel) c).setHorizontalAlignment(CENTER);
            }
            return c;
        }
    }
}

