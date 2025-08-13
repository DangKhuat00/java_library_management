// BorrowPanel.java
package gui;

import dao.BorrowDAO.BorrowRecord;
import model.Document;
import model.Library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

// ========================== BorrowPanel ==========================
public class BorrowPanel extends JPanel {
    private final Library library;
    private JTable table;
    private DefaultTableModel tableModel;

    private RoundedTextField tfUserId, tfDocId, tfDocTitle, tfBorrowDate, tfReturnDate, tfSearch;
    private JComboBox<String> cbFilter;
    private RoundedButton btnBorrow, btnReturn, btnClear, btnSearch, btnReset;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static final Dimension BTN_SIZE_PRIMARY = new Dimension(140, 36);
    private static final Dimension BTN_SIZE_SMALL = new Dimension(110, 34);

    public BorrowPanel() {
        library = new Library();
        setupGUI();
        loadBorrowData();
        setupEvents();
    }

    private void setupGUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel formPanel = new BackgroundPanel("assets/bg_form.png");
        formPanel.setLayout(new GridLayout(5, 1, 0, 8));

        tfUserId = createField("User ID:", formPanel, 200, true);
        tfDocId = createField("Document ID:", formPanel, 200, true);
        tfDocTitle = createField("Document Title:", formPanel, 200, false);
        tfBorrowDate = createField("Borrow Date:", formPanel, 200, false);
        tfReturnDate = createField("Return Date:", formPanel, 200, false);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JPanel searchAndFilterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        tfSearch = new RoundedTextField();
        tfSearch.setPreferredSize(new Dimension(250, 30));

        btnSearch = new RoundedButton("Search");
        btnSearch.setPreferredSize(BTN_SIZE_SMALL);

        btnReset = new RoundedButton("Reset");
        btnReset.setPreferredSize(BTN_SIZE_SMALL);

        cbFilter = new JComboBox<>(new String[] { "All Fields", "User ID", "Document ID", "Document Title" });
        cbFilter.setPreferredSize(new Dimension(140, 30));
        JLabel lblFilter = new JLabel("Filter by:");
        lblFilter.setFont(new Font("Segoe UI", Font.BOLD, 13));

        searchAndFilterPanel.add(lblFilter);
        searchAndFilterPanel.add(cbFilter);
        searchAndFilterPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        searchAndFilterPanel.add(tfSearch);
        searchAndFilterPanel.add(btnSearch);
        searchAndFilterPanel.add(btnReset);

        JPanel actionButtonPanel = new JPanel(new GridLayout(1, 3, 12, 0));
        btnBorrow = new RoundedButton("Borrow");
        btnBorrow.setPreferredSize(BTN_SIZE_PRIMARY);
        btnReturn = new RoundedButton("Return");
        btnReturn.setPreferredSize(BTN_SIZE_PRIMARY);
        btnClear = new RoundedButton("Clear Form");
        btnClear.setPreferredSize(BTN_SIZE_PRIMARY);

        actionButtonPanel.add(btnBorrow);
        actionButtonPanel.add(btnReturn);
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

        JPanel northPanel = new JPanel(new BorderLayout(5, 5));
        northPanel.add(formPanel, BorderLayout.WEST);
        northPanel.add(rightPanel, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        // Header labels in uppercase
        tableModel = new DefaultTableModel(
                new String[] { "USER ID", "DOC ID", "DOCUMENT TITLE", "BORROW DATE", "RETURN DATE" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(22);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setShowGrid(false); // dùng renderer để kẻ vạch, tránh trùng vạch
        table.setDefaultRenderer(Object.class, new GridCellRenderer());

        table.setFillsViewportHeight(true);
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(Color.BLACK);

        // Header: thêm vạch dọc ngăn từng cột bằng renderer tùy biến
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new HeaderCellRenderer(table));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupEvents() {
        btnBorrow.addActionListener(e -> borrowDocument());
        btnReturn.addActionListener(e -> returnDocument());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> searchBorrowRecords());
        btnReset.addActionListener(e -> resetSearch());

        tfDocId.addFocusListener(
                new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        autoFillDocumentTitle();
                    }
                });

        table
                .getSelectionModel()
                .addListSelectionListener(
                        e -> {
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

    private void borrowDocument() {
        String userId = tfUserId.getText().trim();
        String docId = tfDocId.getText().trim();
        if (userId.isEmpty() || docId.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "User ID and Document ID cannot be empty.",
                    "Input Error",
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
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a record from the table to return.",
                    "Input Error",
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

        // Set the custom renderer for highlighting
        String filter = cbFilter.getSelectedItem().toString();
        table.setDefaultRenderer(Object.class, new HighlightRenderer(keyword, filter));

        List<BorrowRecord> allRecords = library.getAllBorrowRecords();
        tableModel.setRowCount(0);
        allRecords.stream()
                .filter(record -> matchesFilter(record, keyword, filter))
                .forEach(this::addBorrowRecordToTable);
    }

    private void resetSearch() {
        tfSearch.setText("");
        cbFilter.setSelectedIndex(0);
        // Reset to default renderer
        table.setDefaultRenderer(Object.class, new GridCellRenderer());
        loadBorrowData();
    }

    private RoundedTextField createField(String label, JPanel parent, int width, boolean editable) {
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
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
        // Keep the current renderer (either highlight or default)
        TableCellRenderer currentRenderer = table.getDefaultRenderer(Object.class);

        tableModel.setRowCount(0);
        List<BorrowRecord> borrows = library.getAllBorrowRecords();
        borrows.forEach(this::addBorrowRecordToTable);

        // Restore the renderer
        table.setDefaultRenderer(Object.class, currentRenderer);
    }

    private void addBorrowRecordToTable(BorrowRecord b) {
        tableModel.addRow(
                new Object[] {
                        b.userId,
                        b.documentId,
                        b.documentTitle,
                        b.borrowedDate != null ? sdf.format(b.borrowedDate) : "",
                        b.returnDate != null ? sdf.format(b.returnDate) : ""
                });
    }

    private boolean matchesFilter(BorrowRecord record, String keyword, String filter) {
        String lowerKeyword = removeDiacritics(keyword.toLowerCase());
        switch (filter) {
            case "User ID":
                return removeDiacritics(record.userId.toLowerCase()).contains(lowerKeyword);
            case "Document ID":
                return removeDiacritics(record.documentId.toLowerCase()).contains(lowerKeyword);
            case "Document Title":
                return removeDiacritics(record.documentTitle.toLowerCase()).contains(lowerKeyword);
            default:
                return removeDiacritics(record.userId.toLowerCase()).contains(lowerKeyword)
                        || removeDiacritics(record.documentId.toLowerCase()).contains(lowerKeyword)
                        || removeDiacritics(record.documentTitle.toLowerCase()).contains(lowerKeyword);
        }
    }

    // Helper method to remove diacritics (same as in HighlightRenderer)
    private String removeDiacritics(String s) {
        String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

    private void clearForm() {
        tfUserId.setText("");
        tfDocId.setText("");
        tfDocTitle.setText("");
        tfBorrowDate.setText("");
        tfReturnDate.setText("");
        table.clearSelection();
    }

    // ========================== HeaderCellRenderer (vạch dọc header)
    // ==========================
    // ========================== HeaderCellRenderer (vạch dọc header, in đậm, căn
    // giữa)
    // ==========================
    private static class HeaderCellRenderer extends DefaultTableCellRenderer {
        private static final Color GRID = new Color(160, 160, 160);
        private static final Color BG = new Color(240, 240, 240);
        private final JTable table;

        public HeaderCellRenderer(JTable table) {
            this.table = table;
            setHorizontalAlignment(CENTER); // căn giữa header
            setFont(new Font("Segoe UI", Font.BOLD, 13)); // giống label chỗ nhập
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
            setBackground(BG);

            // Header luôn viết hoa
            if (value != null) {
                setText(value.toString().toUpperCase());
            }

            // Đảm bảo font luôn đậm và rõ
            setFont(new Font("Segoe UI", Font.BOLD, 13));

            // Vạch dọc ngăn từng header (kẻ mép phải cho mọi cột trừ cột cuối)
            int right = (column < table.getColumnCount() - 1) ? 1 : 0;
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, right, GRID));
            return this;
        }
    }

    // ========================== GridCellRenderer (căn giữa nội dung bảng)
    // ==========================
    private static class GridCellRenderer extends DefaultTableCellRenderer {
        private static final Color GRID = new Color(160, 160, 160);
        private static final Color ALT_ROW = new Color(248, 248, 248);

        public GridCellRenderer() {
            setHorizontalAlignment(CENTER); // căn giữa các cell
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (c instanceof JComponent) {
                JComponent jc = (JComponent) c;
                jc.setOpaque(true);

                if (!isSelected) {
                    jc.setBackground((row % 2 == 0) ? ALT_ROW : Color.WHITE);
                }

                // kẻ vạch ngang và vạch dọc giữa các ô
                int right = (column < table.getColumnCount() - 1) ? 1 : 0;
                jc.setBorder(BorderFactory.createMatteBorder(0, 0, 1, right, GRID));
            }
            // Đảm bảo căn giữa cả khi ở đây
            if (c instanceof JLabel) {
                ((JLabel) c).setHorizontalAlignment(CENTER);
            }
            return c;
        }
    }
}

// ========================== RoundedButton ==========================
class RoundedButton extends JButton {
    public RoundedButton(String text) {
        super(text);
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension base = super.getPreferredSize();
        if (base.height < 34)
            base.height = 34;
        return base;
    }

    @Override
    protected void paintComponent(Graphics g) {
        ButtonModel model = getModel();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color cTop, cBottom;
        if (!isEnabled()) {
            cTop = new Color(180, 190, 205);
            cBottom = new Color(150, 160, 175);
        } else if (model.isPressed()) {
            cTop = new Color(30, 87, 153);
            cBottom = new Color(25, 75, 134);
        } else if (model.isRollover()) {
            cTop = new Color(76, 143, 254);
            cBottom = new Color(46, 107, 193);
        } else {
            cTop = new Color(66, 133, 244);
            cBottom = new Color(30, 87, 153);
        }

        GradientPaint gp = new GradientPaint(0, 0, cTop, 0, getHeight(), cBottom);
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        g2.setColor(new Color(0, 0, 0, 30));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

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
        g2.setColor(new Color(180, 180, 180));
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
