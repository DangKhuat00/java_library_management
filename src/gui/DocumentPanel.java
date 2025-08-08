package gui;

import model.Document;
import model.Library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DocumentPanel extends JPanel {
    private Library library;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField tfTitle, tfAuthor, tfLanguage, tfYear, tfPages, tfRemain;
    private JTextField tfSearch;
    private JComboBox<String> cbFilter;

    private JButton btnAdd, btnUpdate, btnRemove, btnSearch, btnReset;

    private int selectedId = -1;

    public DocumentPanel() {
        library = new Library();
        setupGUI();
        loadAllDocuments();
    }

    private void setupGUI() {
    setLayout(new BorderLayout(5, 5));
    setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

    // === Panel Search + Filter ===
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
    searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // thêm khoảng trống dưới

    add(searchPanel, BorderLayout.NORTH);

    // === Panel nhập dữ liệu thành 3 hàng × 2 cột ===
    JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 10));

    tfTitle = createField("Title:", formPanel, 250);
    tfAuthor = createField("Author:", formPanel, 250);
    tfLanguage = createField("Language:", formPanel, 250);
    tfYear = createField("Year:", formPanel, 250);
    tfPages = createField("Pages:", formPanel, 250);
    tfRemain = createField("Remain Docs:", formPanel, 250);

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
    formAndButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); // thêm khoảng trống dưới

    add(formAndButtonPanel, BorderLayout.CENTER);

    // === Bảng dữ liệu ===
    String[] columns = {"ID", "Title", "Author", "Year", "Language", "Pages", "Remain Docs"};
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

    // Sự kiện clear form
    btnClear.addActionListener(e -> clearForm());
}

// Phiên bản mới của createField
private JTextField createField(String label, JPanel parent, int width) {
    JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

    JLabel lb = new JLabel(label);
    lb.setPreferredSize(new Dimension(90, 36)); // label cao 36
    JTextField tf = new JTextField();
    tf.setPreferredSize(new Dimension(width, 28)); // text field cao 36

    fieldPanel.add(lb);
    fieldPanel.add(tf);
    parent.add(fieldPanel);
    return tf;
}


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
}
