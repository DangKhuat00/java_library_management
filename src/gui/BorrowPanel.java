// Goi package gui
package gui;

// Import cac thu vien can thiet
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

/**
 * Quan ly giao dien muon va tra tai lieu
 * Xu ly cac chuc nang muon, tra, tim kiem va hien thi lich su muon tra
 */
public class BorrowPanel extends JPanel {
    // Bien quan ly thu vien va cac thanh phan giao dien
    private final Library library;
    private JTable table;
    private DefaultTableModel tableModel;

    // Cac truong nhap lieu va bo loc
    private RoundedTextField tfUserId, tfDocId, tfDocTitle, tfBorrowDate, tfReturnDate, tfSearch;
    private JComboBox<String> cbFilter;
    private RoundedButton btnBorrow, btnReturn, btnClear, btnSearch, btnReset;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // Kich thuoc cac nut
    private static final Dimension BTN_SIZE_PRIMARY = new Dimension(140, 36);
    private static final Dimension BTN_SIZE_SMALL = new Dimension(110, 34);

    /**
     * Khoi tao giao dien muon tra
     */
    public BorrowPanel() {
        library = new Library();
        setupGUI();
        loadBorrowData();
        setupEvents();
    }

    /**
     * Thiet lap giao dien nguoi dung
     */
    private void setupGUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Panel form nhap lieu ben trai
        JPanel formPanel = new BackgroundPanel("assets/bg_form.png");
        formPanel.setLayout(new GridLayout(5, 1, 0, 8));

        // Tao cac truong nhap lieu
        tfUserId = createField("User ID:", formPanel, 200, true);
        tfDocId = createField("Document ID:", formPanel, 200, true);
        tfDocTitle = createField("Document Title:", formPanel, 200, false);
        tfBorrowDate = createField("Borrow Date:", formPanel, 200, false);
        tfReturnDate = createField("Return Date:", formPanel, 200, false);

        // Panel dieu khien ben phai
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        // Panel tim kiem va bo loc
        JPanel searchAndFilterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        tfSearch = new RoundedTextField();
        tfSearch.setPreferredSize(new Dimension(250, 30));

        // Cac nut tim kiem va reset
        btnSearch = new RoundedButton("Search");
        btnSearch.setPreferredSize(BTN_SIZE_SMALL);

        btnReset = new RoundedButton("Reset");
        btnReset.setPreferredSize(BTN_SIZE_SMALL);

        // ComboBox bo loc va label
        cbFilter = new JComboBox<>(new String[] { "All Fields", "User ID", "Document ID", "Document Title" });
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

        // Panel cac nut chuc nang chinh
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

        // Can giua cac khoi dieu khien
        searchAndFilterPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonWrapperPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Them cac thanh phan vao panel phai
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(searchAndFilterPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPanel.add(buttonWrapperPanel);
        rightPanel.add(Box.createVerticalGlue());

        // Ket hop panel form va panel dieu khien
        JPanel northPanel = new JPanel(new BorderLayout(5, 5));
        northPanel.add(formPanel, BorderLayout.WEST);
        northPanel.add(rightPanel, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        // Tao bang du lieu voi header in hoa
        tableModel = new DefaultTableModel(
                new String[] { "USER ID", "DOC ID", "DOCUMENT TITLE", "BORROW DATE", "RETURN DATE" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Khong cho phep sua trong bang
            }
        };

        // Thiet lap table va cac thuoc tinh
        table = new JTable(tableModel);
        table.setRowHeight(22);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setShowGrid(false); // Su dung renderer de ke vach
        table.setDefaultRenderer(Object.class, new GridCellRenderer());

        table.setFillsViewportHeight(true);
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(Color.BLACK);

        // Thiet lap header voi renderer tuy bien
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new HeaderCellRenderer(table));

        // Them bang vao scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Thiet lap cac su kien cho cac nut va bang
     */
    private void setupEvents() {
        // Su kien cho cac nut
        btnBorrow.addActionListener(e -> borrowDocument());
        btnReturn.addActionListener(e -> returnDocument());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> searchBorrowRecords());
        btnReset.addActionListener(e -> resetSearch());

        // Tu dong dien tieu de khi roi khoi truong Document ID
        tfDocId.addFocusListener(
                new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        autoFillDocumentTitle();
                    }
                });

        // Dien form khi chon dong trong bang
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

    /**
     * Xu ly muon tai lieu
     */
    private void borrowDocument() {
        String userId = tfUserId.getText().trim();
        String docId = tfDocId.getText().trim();

        // Kiem tra cac truong bat buoc
        if (userId.isEmpty() || docId.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "User ID and Document ID cannot be empty.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Thuc hien muon tai lieu
        if (library.borrowDocument(userId, docId)) {
            loadBorrowData();
            clearForm();
        }
    }

    /**
     * Xu ly tra tai lieu
     */
    private void returnDocument() {
        String userId = tfUserId.getText().trim();
        String docId = tfDocId.getText().trim();

        // Kiem tra cac truong bat buoc
        if (userId.isEmpty() || docId.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a record from the table to return.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Thuc hien tra tai lieu
        if (library.returnDocument(userId, docId)) {
            loadBorrowData();
            clearForm();
        }
    }

    /**
     * Tu dong dien tieu de tai lieu va ngay muon khi nhap Document ID
     */
    private void autoFillDocumentTitle() {
        String docIdText = tfDocId.getText().trim();
        if (!docIdText.isEmpty()) {
            try {
                int docId = Integer.parseInt(docIdText);
                Document doc = library.getDocumentById(docId);
                if (doc != null) {
                    tfDocTitle.setText(doc.getTitle());
                    tfBorrowDate.setText(sdf.format(new Date())); // Dien ngay hien tai
                } else {
                    tfDocTitle.setText("Document not found");
                }
            } catch (NumberFormatException ex) {
                tfDocTitle.setText("Invalid Document ID");
            }
        }
    }

    /**
     * Tim kiem lich su muon tra theo tu khoa va bo loc
     */
    private void searchBorrowRecords() {
        String keyword = tfSearch.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search keyword.");
            return;
        }

        // Dat renderer de lam noi bat tu khoa tim kiem
        String filter = cbFilter.getSelectedItem().toString();
        table.setDefaultRenderer(Object.class, new HighlightRenderer(keyword, filter));

        // Loc du lieu theo tu khoa
        List<BorrowRecord> allRecords = library.getAllBorrowRecords();
        tableModel.setRowCount(0);
        allRecords.stream()
                .filter(record -> matchesFilter(record, keyword, filter))
                .forEach(this::addBorrowRecordToTable);
    }

    /**
     * Dat lai tim kiem va hien thi toan bo lich su muon tra
     */
    private void resetSearch() {
        tfSearch.setText("");
        cbFilter.setSelectedIndex(0);
        // Dat lai renderer mac dinh
        table.setDefaultRenderer(Object.class, new GridCellRenderer());
        loadBorrowData();
    }

    /**
     * Tao mot truong nhap lieu voi label va tuy chinh
     */
    private RoundedTextField createField(String label, JPanel parent, int width, boolean editable) {
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        JLabel lb = new JLabel(label);
        lb.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lb.setPreferredSize(new Dimension(150, 28));
        RoundedTextField tf = new RoundedTextField();
        tf.setPreferredSize(new Dimension(width, 28));
        tf.setEditable(editable);
        if (!editable) {
            tf.setBackground(new Color(230, 230, 230)); // Mau xam cho truong khong the sua
        }
        fieldPanel.setOpaque(false);
        fieldPanel.add(lb);
        fieldPanel.add(tf);
        parent.add(fieldPanel);
        return tf;
    }

    /**
     * Tai va hien thi du lieu muon tra len bang
     */
    public void loadBorrowData() {
        // Giu lai renderer hien tai
        TableCellRenderer currentRenderer = table.getDefaultRenderer(Object.class);

        tableModel.setRowCount(0);
        List<BorrowRecord> borrows = library.getAllBorrowRecords();
        borrows.forEach(this::addBorrowRecordToTable);

        // Khoi phuc renderer
        table.setDefaultRenderer(Object.class, currentRenderer);
    }

    /**
     * Them mot ban ghi muon tra vao bang
     */
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

    /**
     * Kiem tra ban ghi co phu hop voi tu khoa va bo loc khong
     */
    private boolean matchesFilter(BorrowRecord record, String keyword, String filter) {
        String lowerKeyword = removeDiacritics(keyword.toLowerCase());
        switch (filter) {
            case "User ID":
                return removeDiacritics(record.userId.toLowerCase()).contains(lowerKeyword);
            case "Document ID":
                return removeDiacritics(record.documentId.toLowerCase()).contains(lowerKeyword);
            case "Document Title":
                return removeDiacritics(record.documentTitle.toLowerCase()).contains(lowerKeyword);
            default: // Tat ca cac truong
                return removeDiacritics(record.userId.toLowerCase()).contains(lowerKeyword)
                        || removeDiacritics(record.documentId.toLowerCase()).contains(lowerKeyword)
                        || removeDiacritics(record.documentTitle.toLowerCase()).contains(lowerKeyword);
        }
    }

    /**
     * Loai bo dau thanh dieu khoi chuoi
     */
    private String removeDiacritics(String s) {
        String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

    /**
     * Xoa trang cac truong nhap lieu va bo chon tren bang
     */
    private void clearForm() {
        tfUserId.setText("");
        tfDocId.setText("");
        tfDocTitle.setText("");
        tfBorrowDate.setText("");
        tfReturnDate.setText("");
        table.clearSelection();
    }

    // ========================== Cac lop Renderer tuy bien
    // ==========================

    /**
     * Renderer cho header cua bang voi vach doc va chu in dam
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
        public Component getTableCellRendererComponent(
                JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
            setBackground(BG);

            // Chuyen chu thanh in hoa
            if (value != null) {
                setText(value.toString().toUpperCase());
            }
            setFont(new Font("Segoe UI", Font.BOLD, 13));

            // Ve vach doc ngan cach cac cot
            int right = (column < table.getColumnCount() - 1) ? 1 : 0;
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, right, GRID));
            return this;
        }
    }

    /**
     * Renderer mac dinh cho cac o trong bang voi vach chia
     */
    private static class GridCellRenderer extends DefaultTableCellRenderer {
        private static final Color GRID = new Color(160, 160, 160);
        private static final Color ALT_ROW = new Color(248, 248, 248);

        public GridCellRenderer() {
            setHorizontalAlignment(CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (c instanceof JComponent) {
                JComponent jc = (JComponent) c;
                jc.setOpaque(true);

                // Dat mau nen xen ke cho cac hang
                if (!isSelected) {
                    jc.setBackground((row % 2 == 0) ? ALT_ROW : Color.WHITE);
                }

                // Ve vach chia
                int right = (column < table.getColumnCount() - 1) ? 1 : 0;
                int bottom = 1;
                jc.setBorder(BorderFactory.createMatteBorder(0, 0, bottom, right, GRID));
            }
            if (c instanceof JLabel) {
                ((JLabel) c).setHorizontalAlignment(CENTER);
            }
            return c;
        }
    }

    /**
     * Renderer cho checkbox trong bang
     */
    private static class CheckBoxCellRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private final JCheckBox checkBox = new JCheckBox();
        private static final Color GRID = new Color(160, 160, 160);
        private static final Color ALT_ROW = new Color(248, 248, 248);

        public CheckBoxCellRenderer() {
            setLayout(new GridBagLayout());
            checkBox.setHorizontalAlignment(JCheckBox.CENTER);
            checkBox.setOpaque(false);
            setOpaque(true);
            add(checkBox);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            checkBox.setSelected(Boolean.TRUE.equals(value));
            setBackground(!isSelected ? (row % 2 == 0 ? ALT_ROW : Color.WHITE) : table.getSelectionBackground());

            // Ve vach chia
            int right = (column < table.getColumnCount() - 1) ? 1 : 0;
            int bottom = 1;
            setBorder(BorderFactory.createMatteBorder(0, 0, bottom, right, GRID));
            return this;
        }
    }

    /**
     * Renderer de lam noi bat tu khoa tim kiem
     */
    private static class HighlightRenderer extends GridCellRenderer {
        private String keyword;
        private final String context;

        public HighlightRenderer(String keyword, String context) {
            setKeyword(keyword);
            this.context = context;
        }

        public void setKeyword(String keyword) {
            this.keyword = (keyword != null) ? keyword.trim().toLowerCase() : "";
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            String columnName = table.getColumnName(column);

            // Chi lam noi bat neu dung voi bo loc
            if (!"All Fields".equalsIgnoreCase(context) &&
                    !columnName.equalsIgnoreCase(context)) {
                label.setText(value != null ? value.toString() : "");
                return label;
            }

            // Lam noi bat tu khoa tim kiem
            if (value != null && keyword != null && !keyword.isEmpty()) {
                String cellText = value.toString();
                String cellTextNoAccent = removeDiacritics(cellText).toLowerCase();
                String keywordNoAccent = removeDiacritics(keyword).toLowerCase();

                int index = cellTextNoAccent.indexOf(keywordNoAccent);

                if (index >= 0) {
                    String before = cellText.substring(0, index);
                    String match = cellText.substring(index, index + keyword.length());
                    String after = cellText.substring(index + keyword.length());

                    // Dung HTML de lam noi bat
                    label.setText("<html>" + escapeHtml(before)
                            + "<span style='background-color:yellow; font-weight:bold;'>" + escapeHtml(match)
                            + "</span>"
                            + escapeHtml(after) + "</html>");
                } else {
                    label.setText(cellText);
                }
            } else if (value != null) {
                label.setText(value.toString());
            }

            return label;
        }

        /**
         * Loai bo dau thanh dieu khoi chuoi
         */
        private String removeDiacritics(String s) {
            String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(normalized).replaceAll("");
        }

        /**
         * Escape cac ky tu dac biet trong HTML
         */
        private String escapeHtml(String s) {
            return s.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;");
        }
    }
}

// ========================== Cac lop thanh phan tuy bien
// ==========================

/**
 * Nut bo goc tuy bien voi mau gradient
 */
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

        // Chon mau theo trang thai nut
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

        // Ve gradient background
        GradientPaint gp = new GradientPaint(0, 0, cTop, 0, getHeight(), cBottom);
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        // Ve vien
        g2.setColor(new Color(0, 0, 0, 30));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

        super.paintComponent(g);
        g2.dispose();
    }
}

/**
 * Truong nhap lieu bo goc tuy bien
 */
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

        // Ve nen trang
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        // Ve vien
        g2.setColor(new Color(180, 180, 180));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

        super.paintComponent(g);
        g2.dispose();
    }
}

/**
 * Panel voi anh nen tuy bien
 */
class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        try {
            backgroundImage = new ImageIcon(imagePath).getImage();
        } catch (Exception e) {
            System.out.println("Khong the tai anh nen: " + imagePath);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Ve anh nen theo kich thuoc panel
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}