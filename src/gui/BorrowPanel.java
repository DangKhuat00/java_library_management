package gui;

import dao.BorrowDAO.BorrowRecord;
import model.Document;
import model.Library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

// ========================== BorrowPanel ==========================
public class BorrowPanel extends JPanel {
    private final Library library;
    private JTable table;
    private DefaultTableModel tableModel;

    private RoundedTextField tfUserId, tfDocId, tfDocTitle, tfBorrowDate, tfReturnDate, tfSearch;
    private JComboBox<String> cbFilter;
    private RoundedButton btnBorrow, btnReturn, btnClear, btnSearch, btnReset;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public BorrowPanel() {
        library = new Library();
        setupGUI();
        loadBorrowData();
        setupEvents();
    }

    private void setupGUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // ===== Form nhập (bên trái) =====
        JPanel formPanel = new BackgroundPanel("assets/bg_form.png"); // Thêm ảnh nền
        formPanel.setLayout(new GridLayout(5, 1, 0, 8));

        tfUserId = createField("User ID:", formPanel, 200, true);
        tfDocId = createField("Document ID:", formPanel, 200, true);
        tfDocTitle = createField("Document Title:", formPanel, 200, false);
        tfBorrowDate = createField("Borrow Date:", formPanel, 200, false);
        tfReturnDate = createField("Return Date:", formPanel, 200, false);

        // ===== Khu vực điều khiển (bên phải) =====
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        // --- Panel Lọc và Tìm kiếm ---
        JPanel searchAndFilterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        tfSearch = new RoundedTextField();
        tfSearch.setPreferredSize(new Dimension(250, 28));
        btnSearch = new RoundedButton("🔍 Search");
        btnReset = new RoundedButton("Reset");
        cbFilter = new JComboBox<>(new String[]{"All Fields", "User ID", "Document ID", "Document Title"});
        JLabel lblFilter = new JLabel("Filter by:");
        lblFilter.setFont(new Font("Segoe UI", Font.BOLD, 13));

        searchAndFilterPanel.add(lblFilter);
        searchAndFilterPanel.add(cbFilter);
        searchAndFilterPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        searchAndFilterPanel.add(tfSearch);
        searchAndFilterPanel.add(btnSearch);
        searchAndFilterPanel.add(btnReset);

        // --- Panel Nút chức năng ---
        JPanel actionButtonPanel = new JPanel(new GridLayout(1, 3, 10, 5));
        btnBorrow = new RoundedButton("Borrow");
        btnReturn = new RoundedButton("Return");
        btnClear = new RoundedButton("Clear Form");
        actionButtonPanel.add(btnBorrow);
        actionButtonPanel.add(btnReturn);
        actionButtonPanel.add(btnClear);

        JPanel buttonWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrapperPanel.add(actionButtonPanel);

        // Căn giữa các khối điều khiển
        searchAndFilterPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonWrapperPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(searchAndFilterPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPanel.add(buttonWrapperPanel);
        rightPanel.add(Box.createVerticalGlue());

        JPanel northPanel = new JPanel(new BorderLayout(5, 5));
        northPanel.add(formPanel, BorderLayout.WEST);
        northPanel.add(rightPanel, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        // ===== Bảng =====
        tableModel = new DefaultTableModel(
                new String[]{"User ID", "Doc ID", "Document Title", "Borrow Date", "Return Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(22);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupEvents() {
        btnBorrow.addActionListener(e -> borrowDocument());
        btnReturn.addActionListener(e -> returnDocument());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> searchBorrowRecords());
        btnReset.addActionListener(e -> resetSearch());

        tfDocId.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                autoFillDocumentTitle();
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                tfUserId.setText(tableModel.getValueAt(row, 0).toString());
                tfDocId.setText(tableModel.getValueAt(row, 1).toString());
                tfDocTitle.setText(tableModel.getValueAt(row, 2).toString());
                tfBorrowDate.setText(tableModel.getValueAt(row, 3).toString());
                Object returnDate = tableModel.getValueAt(row, 4);
                tfReturnDate.setText(returnDate != null ? returnDate.toString() : "");
            }
        });
    }

    // ========================== Logic ==========================
    private void borrowDocument() {
        String userId = tfUserId.getText().trim();
        String docId = tfDocId.getText().trim();
        if (userId.isEmpty() || docId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "User ID and Document ID cannot be empty.", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (library.borrowDocument(userId, docId)) {
            loadBorrowData();
            clearForm();
        }
    }

    private void returnDocument() {
        String userId = tfUserId.getText().trim();
        String docId = tfDocId.getText().trim();
        if (userId.isEmpty() || docId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a record from the table to return.", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (library.returnDocument(userId, docId)) {
            loadBorrowData();
            clearForm();
        }
    }

    private void autoFillDocumentTitle() {
        String docIdText = tfDocId.getText().trim();
        if (!docIdText.isEmpty()) {
            try {
                int docId = Integer.parseInt(docIdText);
                Document doc = library.getDocumentById(docId);
                if (doc != null) {
                    tfDocTitle.setText(doc.getTitle());
                    tfBorrowDate.setText(sdf.format(new Date()));
                } else {
                    tfDocTitle.setText("Document not found");
                }
            } catch (NumberFormatException ex) {
                tfDocTitle.setText("Invalid Document ID");
            }
        }
    }

    private void searchBorrowRecords() {
        String keyword = tfSearch.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search keyword.");
            return;
        }
        List<BorrowRecord> allRecords = library.getAllBorrowRecords();
        String filter = cbFilter.getSelectedItem().toString();

        tableModel.setRowCount(0);
        allRecords.stream()
                .filter(record -> matchesFilter(record, keyword, filter))
                .forEach(this::addBorrowRecordToTable);
    }

    private void resetSearch() {
        tfSearch.setText("");
        cbFilter.setSelectedIndex(0);
        loadBorrowData();
    }

    private RoundedTextField createField(String label, JPanel parent, int width, boolean editable) {
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JLabel lb = new JLabel(label);
        lb.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lb.setPreferredSize(new Dimension(150, 28));
        RoundedTextField tf = new RoundedTextField();
        tf.setPreferredSize(new Dimension(width, 28));
        tf.setEditable(editable);
        if (!editable) {
            tf.setBackground(new Color(230, 230, 230));
        }
        fieldPanel.setOpaque(false);
        fieldPanel.add(lb);
        fieldPanel.add(tf);
        parent.add(fieldPanel);
        return tf;
    }

    public void loadBorrowData() {
        tableModel.setRowCount(0);
        List<BorrowRecord> borrows = library.getAllBorrowRecords();
        borrows.forEach(this::addBorrowRecordToTable);
    }

    private void addBorrowRecordToTable(BorrowRecord b) {
        tableModel.addRow(new Object[]{
                b.userId,
                b.documentId,
                b.documentTitle,
                b.borrowedDate != null ? sdf.format(b.borrowedDate) : "",
                b.returnDate != null ? sdf.format(b.returnDate) : ""
        });
    }

    private boolean matchesFilter(BorrowRecord record, String keyword, String filter) {
        String lowerKeyword = keyword.toLowerCase();
        switch (filter) {
            case "User ID":
                return record.userId.toLowerCase().contains(lowerKeyword);
            case "Document ID":
                return record.documentId.toLowerCase().contains(lowerKeyword);
            case "Document Title":
                return record.documentTitle.toLowerCase().contains(lowerKeyword);
            default:
                return record.userId.toLowerCase().contains(lowerKeyword) ||
                        record.documentId.toLowerCase().contains(lowerKeyword) ||
                        record.documentTitle.toLowerCase().contains(lowerKeyword);
        }
    }

    private void clearForm() {
        tfUserId.setText("");
        tfDocId.setText("");
        tfDocTitle.setText("");
        tfBorrowDate.setText("");
        tfReturnDate.setText("");
        table.clearSelection();
    }
}

// ========================== RoundedButton ==========================
class RoundedButton extends JButton {
    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gp = new GradientPaint(0, 0, new Color(66, 133, 244),
                                             0, getHeight(), new Color(30, 87, 153));
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        super.paintComponent(g);
        g2.dispose();
    }
}

// ========================== RoundedTextField ==========================
class RoundedTextField extends JTextField {
    public RoundedTextField() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    public RoundedTextField(int columns) {
        this();
        setColumns(columns);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2.setColor(Color.GRAY);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
        super.paintComponent(g);
        g2.dispose();
    }
}

// ========================== BackgroundPanel ==========================
class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        try {
            backgroundImage = new ImageIcon(imagePath).getImage();
        } catch (Exception e) {
            System.out.println("Không thể tải ảnh nền: " + imagePath);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
