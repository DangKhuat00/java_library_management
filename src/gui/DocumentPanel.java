package gui;

import model.Document;
import model.Library;
import model.DocumentFilter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Quan ly giao dien tai lieu
 * Xu ly cac chuc nang them, sua, xoa, tim kiem va hien thi danh sach tai lieu
 */
public class DocumentPanel extends JPanel {
    private final Library library;
    private JTable table;
    private DefaultTableModel tableModel;

    // Thay thế JTextField bằng JCheckBox
    private JTextField tfTitle, tfAuthor, tfLanguage, tfYear, tfPages;
    private JCheckBox chkIsAvailable;
    private JTextField tfSearch;
    private JComboBox<String> cbFilter;

    private JButton btnAdd, btnUpdate, btnRemove, btnSearch, btnReset, btnClear;

    private int selectedId = -1;

    /**
     * Khoi tao giao dien quan ly tai lieu
     */
    public DocumentPanel() {
        library = new Library();
        setupGUI();
        loadAllDocuments();
        setupEvents();
    }

    /**
     * Thiet lap giao dien nguoi dung
     */
    private void setupGUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel northPanel = new JPanel(new BorderLayout(5, 5));

        JPanel formPanel = new JPanel(new GridLayout(6, 1, 0, 8));
        tfTitle = createField("Title:", formPanel, 250);
        tfAuthor = createField("Author:", formPanel, 250);
        tfLanguage = createField("Language:", formPanel, 250);
        tfYear = createField("Year:", formPanel, 250);
        tfPages = createField("Pages:", formPanel, 250);

        // Tạo JCheckBox cho trạng thái 'Available'
        chkIsAvailable = new JCheckBox("Is Available");
        chkIsAvailable.setSelected(true); // Mặc định là có sẵn
        JPanel chkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JLabel lblAvailable = new JLabel("Status:");
        lblAvailable.setPreferredSize(new Dimension(110, 28));
        chkPanel.add(lblAvailable);
        chkPanel.add(chkIsAvailable);
        formPanel.add(chkPanel);

        northPanel.add(formPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JPanel searchAndFilterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        tfSearch = new JTextField();
        tfSearch.setPreferredSize(new Dimension(250, 28));
        btnSearch = new JButton("🔍 Search");
        btnReset = new JButton("Reset");
        cbFilter = new JComboBox<>(
                new String[] { "All Fields", "Id", "Title", "Language", "Pages", "Author", "PublicationYear",
                        "Available" });
        JLabel lblFilter = new JLabel("Filter by:");

        searchAndFilterPanel.add(lblFilter);
        searchAndFilterPanel.add(cbFilter);
        searchAndFilterPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        searchAndFilterPanel.add(tfSearch);
        searchAndFilterPanel.add(btnSearch);
        searchAndFilterPanel.add(btnReset);

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

        searchAndFilterPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonWrapperPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(searchAndFilterPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPanel.add(buttonWrapperPanel);
        rightPanel.add(Box.createVerticalGlue());

        northPanel.add(rightPanel, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        // Cập nhật tên cột
        String[] columns = { "Id", "Title", "Language", "Pages", "Author", "PublicationYear", "Available" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }

            // Hiển thị kiểu Boolean dưới dạng checkbox trong bảng
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 6)
                    return Boolean.class;
                return super.getColumnClass(columnIndex);
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(22);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Tao mot truong nhap lieu voi label va do rong tuy chinh
     */
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

    /**
     * Tai va hien thi tat ca tai lieu len bang
     */
    private void loadAllDocuments() {
        tableModel.setRowCount(0);
        List<Document> docs = library.getAllDocuments();
        loadDocumentsToTable(docs);
    }

    /**
     * Dua danh sach tai lieu vao bang
     */
    private void loadDocumentsToTable(List<Document> docs) {
        tableModel.setRowCount(0);
        for (Document doc : docs) {
            tableModel.addRow(new Object[] {
                    doc.getId(), // Id
                    doc.getTitle(), // Title
                    doc.getLanguage(), // Language
                    doc.getPages(), // Pages
                    doc.getAuthor(), // Author
                    doc.getPublicationYear(), // PublicationYear
                    doc.isAvailable() // Available
            });

        }
    }

    /**
     * Kiem tra du lieu nhap vao hop le
     */
    private boolean validateInput() {
        // Bỏ kiểm tra cho trường "remain docs" cũ
        if (tfTitle.getText().trim().isEmpty() ||
                tfAuthor.getText().trim().isEmpty() ||
                tfLanguage.getText().trim().isEmpty() ||
                tfYear.getText().trim().isEmpty() ||
                tfPages.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return false;
        }
        try {
            Integer.parseInt(tfYear.getText().trim());
            Integer.parseInt(tfPages.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Year and Pages must be numbers.");
            return false;
        }
        return true;
    }

    /**
     * Xoa trang cac truong nhap lieu va bo chon tren bang
     */
    private void clearForm() {
        tfTitle.setText("");
        tfAuthor.setText("");
        tfLanguage.setText("");
        tfYear.setText("");
        tfPages.setText("");
        chkIsAvailable.setSelected(true); // Đặt lại checkbox
        selectedId = -1;
        table.clearSelection();
    }

    /**
     * Thiet lap cac su kien cho cac nut va bang
     */
    private void setupEvents() {
        btnAdd.addActionListener(e -> {
            if (validateInput()) {
                // Sử dụng constructor mới, không cần isAvailable vì nó mặc định là true
                Document doc = new Document(
                        tfTitle.getText().trim(),
                        tfLanguage.getText().trim(),
                        Integer.parseInt(tfPages.getText().trim()),
                        tfAuthor.getText().trim(),
                        Integer.parseInt(tfYear.getText().trim()));
                if (library.addDocument(doc)) {
                    JOptionPane.showMessageDialog(this, "Document added successfully.");
                    loadAllDocuments();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add document.");
                }
            }
        });

        btnUpdate.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a document to update.");
                return;
            }
            if (validateInput()) {
                // Sử dụng constructor mới với isAvailable
                Document doc = new Document(
                        selectedId,
                        tfTitle.getText().trim(),
                        tfLanguage.getText().trim(),
                        Integer.parseInt(tfPages.getText().trim()),
                        tfAuthor.getText().trim(),
                        Integer.parseInt(tfYear.getText().trim()),
                        chkIsAvailable.isSelected() // Lấy giá trị từ checkbox
                );
                if (library.updateDocument(doc)) {
                    JOptionPane.showMessageDialog(this, "Document updated successfully.");
                    loadAllDocuments();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update document.");
                }
            }
        });

        btnRemove.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a document to remove.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(
                    this, "Are you sure to delete this document?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (library.removeDocument(String.valueOf(selectedId))) {
                    JOptionPane.showMessageDialog(this, "Document removed successfully.");
                    loadAllDocuments();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to remove document.");
                }
            }
        });

        btnSearch.addActionListener(e -> {
            String keyword = tfSearch.getText().trim();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search keyword.");
                return;
            }

            DocumentFilter filter;
            switch (cbFilter.getSelectedItem().toString()) {
                case "Id":
                    filter = DocumentFilter.ID;
                    break;
                case "Title":
                    filter = DocumentFilter.TITLE;
                    break;
                case "Language":
                    filter = DocumentFilter.LANGUAGE;
                    break;
                case "Pages":
                    filter = DocumentFilter.PAGES;
                    break;
                case "Author":
                    filter = DocumentFilter.AUTHOR;
                    break;
                case "PublicationYear":
                    filter = DocumentFilter.PUBLICATION_YEAR;
                    break;
                case "Available":
                    filter = DocumentFilter.IS_AVAILABLE;
                default:
                    filter = DocumentFilter.ALL_FIELDS;
                    break;
            }

            // Tìm kiếm document
            List<Document> docs = library.findDocuments(keyword, filter);

            // Cập nhật bảng
            tableModel.setRowCount(0);
            for (Document doc : docs) {
                tableModel.addRow(new Object[] {
                        doc.getId(), // Id
                        doc.getTitle(), // Title
                        doc.getLanguage(), // Language
                        doc.getPages(), // Pages
                        doc.getAuthor(), // Author
                        doc.getPublicationYear(), // PublicationYear
                        doc.isAvailable() // Available
                });

            }

            // Highlight
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i)
                        .setCellRenderer(new HighlightRenderer(keyword, cbFilter.getSelectedItem().toString()));
            }

            table.repaint();
        });

        btnReset.addActionListener(e -> {
            tfSearch.setText("");
            cbFilter.setSelectedIndex(0);

            // Tải lại toàn bộ dữ liệu
            loadAllDocuments();

            // Xóa highlight bằng cách set renderer keyword rỗng
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i)
                        .setCellRenderer(new HighlightRenderer("", ""));
            }

            table.repaint();
        });

        btnClear.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int selectedRow = table.getSelectedRow();
                selectedId = (int) table.getValueAt(selectedRow, 0);
                tfTitle.setText((String) table.getValueAt(selectedRow, 1));
                tfAuthor.setText((String) table.getValueAt(selectedRow, 2));
                tfYear.setText(String.valueOf(table.getValueAt(selectedRow, 3)));
                tfLanguage.setText((String) table.getValueAt(selectedRow, 4));
                tfPages.setText(String.valueOf(table.getValueAt(selectedRow, 5)));
                // Cập nhật checkbox
                chkIsAvailable.setSelected((Boolean) table.getValueAt(selectedRow, 6));
            }
        });
    }
}