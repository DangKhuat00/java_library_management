package gui;

import model.Document;
import model.Library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DocumentPanel extends JPanel {
    private final Library library;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField tfTitle, tfAuthor, tfLanguage, tfYear, tfPages, tfRemain;
    private JTextField tfSearch;
    private JComboBox<String> cbFilter;

    private JButton btnAdd, btnUpdate, btnRemove, btnSearch, btnReset, btnClear;

    private int selectedId = -1;

    public DocumentPanel() {
        library = new Library();
        initUI();
        loadAllDocuments();
        initEvents();
    }

    /** ===================== UI SETUP ===================== */
    private void initUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        add(createSearchPanel(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.CENTER);
        add(createTablePanel(), BorderLayout.SOUTH);
    }

    /** Panel tìm kiếm + filter */
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        cbFilter = new JComboBox<>(new String[]{
                "All Fields", "Title", "Author", "Language", "Year", "Pages", "Remain Docs"
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

        return searchPanel;
    }

    /** Panel nhập dữ liệu + nút thêm/sửa/xóa */
    private JPanel createFormPanel() {
        // Giảm khoảng cách dọc (vgap) giữa các hàng trong lưới
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 2));

        tfTitle = createField("Title:", formPanel, 250);
        tfAuthor = createField("Author:", formPanel, 250);
        tfLanguage = createField("Language:", formPanel, 250);
        tfYear = createField("Year:", formPanel, 250);
        tfPages = createField("Pages:", formPanel, 250);
        tfRemain = createField("Remain Docs:", formPanel, 250);

        JPanel formAndButtonPanel = new JPanel(new BorderLayout(5, 5));
        formAndButtonPanel.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnAdd = new JButton("➕ Add");
        btnUpdate = new JButton("✏️ Update");
        btnRemove = new JButton("🗑️ Remove");
        btnClear = new JButton("Clear Form");

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnRemove);
        btnPanel.add(btnClear);

        formAndButtonPanel.add(btnPanel, BorderLayout.SOUTH);
        // Giảm khoảng trống dưới cùng để tiết kiệm không gian
        formAndButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        return formAndButtonPanel;
    }

    /** Tạo một trường nhập có label - Đã tối ưu hóa chiều cao */
    private JTextField createField(String label, JPanel parent, int width) {
        // Giảm khoảng cách dọc (vgap) về 0
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        JLabel lb = new JLabel(label);
        // Giảm chiều cao label
        lb.setPreferredSize(new Dimension(90, 28));

        JTextField tf = new JTextField();
        // Giảm chiều cao text field
        tf.setPreferredSize(new Dimension(width, 28));

        fieldPanel.add(lb);
        fieldPanel.add(tf);
        parent.add(fieldPanel);

        return tf;
    }

    /** Panel chứa bảng dữ liệu */
    private JScrollPane createTablePanel() {
        String[] columns = {"ID", "Title", "Author", "Year", "Language", "Pages", "Remain Docs"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // Không cho chỉnh sửa trực tiếp
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(22);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 250)); // Tăng chiều cao cho bảng
        return scrollPane;
    }

    /** ===================== DATA LOADING ===================== */
    private void loadAllDocuments() {
        tableModel.setRowCount(0);
        List<Document> docs = library.getAllDocuments();
        for (Document doc : docs) {
            tableModel.addRow(new Object[]{
                    doc.getId(),
                    doc.getTitle(),
                    doc.getAuthor(),
                    doc.getPublicationYear(),
                    doc.getLanguage(),
                    doc.getPages(),
                    doc.getRemainDocs()
            });
        }
    }

    private void loadDocuments(List<Document> docs) {
        tableModel.setRowCount(0);
        for (Document doc : docs) {
            tableModel.addRow(new Object[]{
                    doc.getId(),
                    doc.getTitle(),
                    doc.getAuthor(),
                    doc.getPublicationYear(),
                    doc.getLanguage(),
                    doc.getPages(),
                    doc.getRemainDocs()
            });
        }
    }

    /** ===================== VALIDATION ===================== */
    private boolean validateInput() {
        if (tfTitle.getText().trim().isEmpty() ||
                tfAuthor.getText().trim().isEmpty() ||
                tfLanguage.getText().trim().isEmpty() ||
                tfYear.getText().trim().isEmpty() ||
                tfPages.getText().trim().isEmpty() ||
                tfRemain.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return false;
        }
        try {
            Integer.parseInt(tfYear.getText().trim());
            Integer.parseInt(tfPages.getText().trim());
            Integer.parseInt(tfRemain.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Year, Pages, and Remain Docs must be numbers.");
            return false;
        }
        return true;
    }

    private void clearForm() {
        tfTitle.setText("");
        tfAuthor.setText("");
        tfLanguage.setText("");
        tfYear.setText("");
        tfPages.setText("");
        tfRemain.setText("");
        selectedId = -1;
        table.clearSelection();
    }

    /** ===================== EVENT HANDLING ===================== */
    private void initEvents() {
        // Thêm tài liệu
        btnAdd.addActionListener(e -> {
            if (validateInput()) {
                Document doc = new Document(
                        tfTitle.getText().trim(),
                        tfLanguage.getText().trim(),
                        Integer.parseInt(tfPages.getText().trim()),
                        tfAuthor.getText().trim(),
                        Integer.parseInt(tfYear.getText().trim()),
                        Integer.parseInt(tfRemain.getText().trim())
                );
                if (library.addDocument(doc)) {
                    JOptionPane.showMessageDialog(this, "Document added successfully.");
                    loadAllDocuments();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add document.");
                }
            }
        });

        // Cập nhật tài liệu
        btnUpdate.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a document to update.");
                return;
            }
            if (validateInput()) {
                Document doc = new Document(
                        selectedId,
                        tfTitle.getText().trim(),
                        tfLanguage.getText().trim(),
                        Integer.parseInt(tfPages.getText().trim()),
                        tfAuthor.getText().trim(),
                        Integer.parseInt(tfYear.getText().trim()),
                        Integer.parseInt(tfRemain.getText().trim())
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

        // Xóa tài liệu
        btnRemove.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a document to remove.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(
                    this, "Are you sure to delete this document?", "Confirm", JOptionPane.YES_NO_OPTION
            );
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

        // Tìm kiếm
        btnSearch.addActionListener(e -> {
            String keyword = tfSearch.getText().trim();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search keyword.");
                return;
            }

            String filter = cbFilter.getSelectedItem().toString();
            List<Document> docs;
            if (filter.equals("All Fields")) {
                docs = library.findDocuments(keyword);
            } else {
                docs = library.findDocumentsByField(filter, keyword);
            }
            loadDocuments(docs);
        });

        // Reset
        btnReset.addActionListener(e -> {
            tfSearch.setText("");
            cbFilter.setSelectedIndex(0);
            loadAllDocuments();
        });

        // Clear form
        btnClear.addActionListener(e -> clearForm());

        // Chọn bảng -> load form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                selectedId = (int) table.getValueAt(table.getSelectedRow(), 0);
                tfTitle.setText((String) table.getValueAt(table.getSelectedRow(), 1));
                tfAuthor.setText((String) table.getValueAt(table.getSelectedRow(), 2));
                tfYear.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 3)));
                tfLanguage.setText((String) table.getValueAt(table.getSelectedRow(), 4));
                tfPages.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 5)));
                tfRemain.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 6)));
            }
        });
    }
}