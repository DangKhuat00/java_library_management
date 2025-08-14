// Goi package gui
package gui;

// Import cac thu vien can thiet
import model.User;
import model.UserFilter;
import model.Library;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * Quan ly giao dien nguoi dung
 * Xu ly cac chuc nang them, sua, xoa, tim kiem nguoi dung
 */
public class UserPanel extends JPanel {
    // Bien quan ly thu vien va cac thanh phan giao dien
    private Library library;
    private JTable table;
    private DefaultTableModel tableModel;

    // Cac truong nhap lieu
    private JTextField tfName, tfEmail, tfPhone, tfBorrowLimit, tfBorrowedCount;
    private JTextField tfSearch;
    private JComboBox<String> cbFilter;

    // Cac nut chuc nang
    private RoundedButton btnAdd, btnUpdate, btnRemove, btnSearch, btnReset, btnClear;

    // ID ban ghi duoc chon
    private int selectedId = -1;

    // Kich thuoc cac nut
    private static final Dimension BTN_SIZE_PRIMARY = new Dimension(140, 36);
    private static final Dimension BTN_SIZE_SMALL = new Dimension(110, 34);

    /**
     * Khoi tao giao dien quan ly nguoi dung
     */
    public UserPanel() {
        library = new Library();
        setupGUI();
        loadAllUsers();
        setupEvents();
    }

    /**
     * Thiet lap giao dien nguoi dung
     */
    private void setupGUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Panel phia tren chua Form va Dieu khien
        JPanel northPanel = new JPanel(new BorderLayout(5, 5));

        // Form nhap lieu ben trai
        JPanel formPanel = new JPanel(new GridLayout(5, 1, 0, 8));
        tfName = createField("Name:", formPanel, 250);
        tfEmail = createField("Email:", formPanel, 250);
        tfPhone = createField("Phone:", formPanel, 250);
        tfBorrowLimit = createField("Borrow Limit:", formPanel, 250);
        tfBorrowLimit.setText("10"); // Mac dinh gioi han muon
        tfBorrowedCount = createField("Borrowed Count:", formPanel, 250);
        tfBorrowedCount.setText("0"); // Mac dinh so sach da muon
        tfBorrowedCount.setEditable(false); // Khong cho phep sua

        northPanel.add(formPanel, BorderLayout.WEST);

        // Khu vuc dieu khien ben phai
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        // Panel Loc va Tim kiem
        JPanel searchAndFilterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        tfSearch = new JTextField();
        tfSearch.setPreferredSize(new Dimension(250, 28));
        btnSearch = new RoundedButton("Search");
        btnSearch.setPreferredSize(BTN_SIZE_SMALL);
        btnReset = new RoundedButton("Reset");
        btnReset.setPreferredSize(BTN_SIZE_SMALL);
        cbFilter = new JComboBox<>(
                new String[] { "All Fields", "Id", "Name", "Email", "PhoneNumber", "BorrowLimit", "BorrowedCount" });
        cbFilter.setPreferredSize(new Dimension(140, 30));
        JLabel lblFilter = new JLabel("Filter by:");
        lblFilter.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Them cac thanh phan vao panel tim kiem
        searchAndFilterPanel.add(lblFilter);
        searchAndFilterPanel.add(cbFilter);
        searchAndFilterPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        searchAndFilterPanel.add(tfSearch);
        searchAndFilterPanel.add(btnSearch);
        searchAndFilterPanel.add(btnReset);

        // Panel Nut chuc nang
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

        // Can giua cac khoi dieu khien
        searchAndFilterPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonWrapperPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(searchAndFilterPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPanel.add(buttonWrapperPanel);
        rightPanel.add(Box.createVerticalGlue());

        northPanel.add(rightPanel, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        // Bang du lieu
        String[] columns = { "ID", "Name", "Email", "PhoneNumber", "BorrowLimit", "BorrowedCount" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // Khong cho phep chinh sua trong bang
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(22);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setShowGrid(false);
        table.setDefaultRenderer(Object.class, new GridCellRenderer());

        // Thiet lap header
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new HeaderCellRenderer(table));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Tao mot truong nhap lieu voi label
     */
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

    /**
     * Tai tat ca nguoi dung va hien thi len bang
     */
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

    /**
     * Kiem tra tinh hop le cua du lieu nhap vao
     */
    private boolean validateInput() {
        if (tfName.getText().trim().isEmpty() ||
                tfEmail.getText().trim().isEmpty() ||
                tfPhone.getText().trim().isEmpty() ||
                tfBorrowLimit.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields except Borrowed Count.");
            return false;
        }
        try {
            // Kiem tra gioi han muon la so nguyen
            Integer.parseInt(tfBorrowLimit.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Borrow Limit must be a number.");
            return false;
        }
        return true;
    }

    /**
     * Xoa trang form nhap lieu
     */
    private void clearForm() {
        tfName.setText("");
        tfEmail.setText("");
        tfPhone.setText("");
        tfBorrowLimit.setText("10");
        tfBorrowedCount.setText("0");
        selectedId = -1;
        table.clearSelection();
    }

    /**
     * Thiet lap cac su kien cho cac nut va bang
     */
    private void setupEvents() {
        // Su kien xoa form
        btnClear.addActionListener(e -> clearForm());

        // Su kien them nguoi dung moi
        btnAdd.addActionListener(e -> {
            if (!validateInput())
                return;

            User user = new User(
                    tfName.getText().trim(),
                    tfEmail.getText().trim(),
                    tfPhone.getText().trim(),
                    Integer.parseInt(tfBorrowLimit.getText().trim()),
                    0); // Nguoi dung moi chua muon sach nao

            if (library.addUser(user)) {
                JOptionPane.showMessageDialog(this, "User added successfully.");
                loadAllUsers();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Phone number already exists or failed to add user.");
            }
        });

        // Su kien cap nhat nguoi dung
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

        // Su kien xoa nguoi dung
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

        // Su kien tim kiem nguoi dung
        btnSearch.addActionListener(e -> {
            String keyword = tfSearch.getText().trim();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search keyword.");
                return;
            }

            // Xac dinh bo loc tim kiem
            UserFilter filter;
            String filterText = cbFilter.getSelectedItem().toString();
            switch (filterText) {
                case "Id": filter = UserFilter.ID; break;
                case "Name": filter = UserFilter.NAME; break;
                case "Email": filter = UserFilter.EMAIL; break;
                case "PhoneNumber": filter = UserFilter.PHONE_NUMBER; break;
                case "BorrowLimit": filter = UserFilter.BORROW_LIMIT; break;
                case "BorrowedCount": filter = UserFilter.BORROWED_COUNT; break;
                default: filter = UserFilter.ALL_FIELDS;
            }

            // Thuc hien tim kiem
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

            // Ap dung HighlightRenderer voi grid style
            HighlightGridRenderer highlightRenderer = new HighlightGridRenderer(keyword, filterText);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(highlightRenderer);
            }
            table.repaint();
        });

        // Su kien reset tim kiem
        btnReset.addActionListener(e -> {
            tfSearch.setText("");
            cbFilter.setSelectedIndex(0);
            loadAllUsers();
            
            // Khoi phuc GridCellRenderer
            GridCellRenderer gridRenderer = new GridCellRenderer();
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(gridRenderer);
            }
            table.repaint();
        });

        // Su kien chon dong trong bang
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

    // ===== Cac lop renderer tuy bien =====

    /**
     * Renderer cho header cua bang
     */
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
                setText(value.toString().toUpperCase()); // Chuyen chu thanh in hoa
            }
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            int right = (column < table.getColumnCount() - 1) ? 1 : 0;
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, right, GRID));
            return this;
        }
    }

    /**
     * Renderer cho cell voi grid style
     */
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
                    // Dat mau nen xen ke cho cac hang
                    jc.setBackground((row % 2 == 0) ? ALT_ROW : Color.WHITE);
                }
                // Ve vach chia
                int right = (column < table.getColumnCount() - 1) ? 1 : 0;
                jc.setBorder(BorderFactory.createMatteBorder(0, 0, 1, right, GRID));
            }
            if (c instanceof JLabel) {
                ((JLabel) c).setHorizontalAlignment(CENTER);
            }
            return c;
        }
    }

    /**
     * Renderer ket hop highlight va grid style
     */
    private static class HighlightGridRenderer extends HighlightRenderer {
        private static final Color GRID = new Color(160, 160, 160);
        private static final Color ALT_ROW = new Color(248, 248, 248);

        public HighlightGridRenderer(String keyword, String context) {
            super(keyword, context);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // Ap dung style grid
            if (c instanceof JComponent) {
                JComponent jc = (JComponent)c;
                if (!isSelected) {
                    jc.setBackground((row % 2 == 0) ? ALT_ROW : Color.WHITE);
                }
                jc.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 
                    (column < table.getColumnCount() - 1) ? 1 : 0, GRID));
            }
            ((JLabel)c).setHorizontalAlignment(CENTER);
            return c;
        }
    }
}