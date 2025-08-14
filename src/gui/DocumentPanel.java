// Goi package gui
package gui;

// Import cac thu vien can thiet
import model.Document;
import model.Library;
import model.DocumentFilter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.List;
import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Quan ly giao dien tai lieu
 * Xu ly cac chuc nang them, sua, xoa, tim kiem tai lieu va hien thi hinh anh
 */
public class DocumentPanel extends JPanel {
    // Bien quan ly thu vien va cac thanh phan giao dien
    private final Library library;
    private JTable table;
    private DefaultTableModel tableModel;

    // Cac truong nhap lieu va checkbox
    private JTextField tfTitle, tfAuthor, tfLanguage, tfYear, tfPages;
    private JCheckBox chkIsAvailable;
    private JTextField tfSearch;
    private JComboBox<String> cbFilter;

    // Cac nut chuc nang
    private RoundedButton btnAdd, btnUpdate, btnRemove, btnSearch, btnReset, btnClear;

    // ID cua ban ghi duoc chon
    private int selectedId = -1;

    // Kich thuoc cac nut
    private static final Dimension BTN_SIZE_PRIMARY = new Dimension(140, 36);
    private static final Dimension BTN_SIZE_SMALL = new Dimension(110, 34);

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

        // ===== Form nhap lieu =====
        JPanel formPanel = new JPanel(new GridLayout(6, 1, 0, 8));
        tfTitle = createField("Title:", formPanel, 250);
        tfAuthor = createField("Author:", formPanel, 250);
        tfLanguage = createField("Language:", formPanel, 250);
        tfYear = createField("Year:", formPanel, 250);
        tfPages = createField("Pages:", formPanel, 250);

        // Checkbox trang thai co san
        chkIsAvailable = new JCheckBox("Is Available");
        chkIsAvailable.setSelected(true);
        JPanel chkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JLabel lblAvailable = new JLabel("Status:");
        lblAvailable.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblAvailable.setPreferredSize(new Dimension(110, 28));
        chkPanel.add(lblAvailable);
        chkPanel.add(chkIsAvailable);
        formPanel.add(chkPanel);

        northPanel.add(formPanel, BorderLayout.WEST);

        // ===== Panel ben phai =====
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        // ==== Thanh tim kiem & loc ====
        JPanel searchAndFilterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        tfSearch = new JTextField();
        tfSearch.setPreferredSize(new Dimension(250, 28));

        // Cac nut tim kiem va reset
        btnSearch = new RoundedButton("Search");
        btnSearch.setPreferredSize(BTN_SIZE_SMALL);
        btnReset = new RoundedButton("Reset");
        btnReset.setPreferredSize(BTN_SIZE_SMALL);

        // ComboBox bo loc va label
        cbFilter = new JComboBox<>(
                new String[] { "All Fields", "Id", "Title", "Language", "Pages", "Author", "PublicationYear" });
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

        // ==== Nhom nut hanh dong ====
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
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(searchAndFilterPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPanel.add(buttonWrapperPanel);
        rightPanel.add(Box.createVerticalGlue());

        northPanel.add(rightPanel, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        // ==== Bang hien thi ====
        String[] columns = { "Id", "Title", "Author", "Language", "Pages", "PublicationYear", "Available" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // Khong cho phep chinh sua trong bang
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 6)
                    return Boolean.class; // Cot Available la Boolean
                return super.getColumnClass(columnIndex);
            }
        };

        // Thiet lap table va cac thuoc tinh
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setShowGrid(false);
        table.setFillsViewportHeight(true);

        // Renderer cho du lieu va Boolean
        table.setDefaultRenderer(Object.class, new GridCellRenderer());
        table.setDefaultRenderer(Boolean.class, new CheckBoxCellRenderer());
        table.getTableHeader().setDefaultRenderer(new HeaderCellRenderer(table));

        // Them bang vao scroll pane
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
     * Tai tat ca tai lieu va hien thi len bang
     */
    private void loadAllDocuments() {
        tableModel.setRowCount(0);
        List<Document> docs = library.getAllDocuments();
        loadDocumentsToTable(docs);
    }

    /**
     * Tai danh sach tai lieu len bang
     */
    private void loadDocumentsToTable(List<Document> docs) {
        tableModel.setRowCount(0);
        for (Document doc : docs) {
            tableModel.addRow(new Object[] {
                    doc.getId(),
                    doc.getTitle(),
                    doc.getAuthor(),
                    doc.getLanguage(),
                    doc.getPages(),
                    doc.getPublicationYear(),
                    doc.isAvailable()
            });
        }
    }

    /**
     * Kiem tra tinh hop le cua du lieu nhap vao
     */
    private boolean validateInput() {
        if (tfTitle.getText().trim().isEmpty()
                || tfAuthor.getText().trim().isEmpty()
                || tfLanguage.getText().trim().isEmpty()
                || tfYear.getText().trim().isEmpty()
                || tfPages.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return false;
        }
        try {
            // Kiem tra nam va so trang la so nguyen
            Integer.parseInt(tfYear.getText().trim());
            Integer.parseInt(tfPages.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Year and Pages must be numbers.");
            return false;
        }
        return true;
    }

    /**
     * Xoa trang form nhap lieu
     */
    private void clearForm() {
        tfTitle.setText("");
        tfAuthor.setText("");
        tfLanguage.setText("");
        tfYear.setText("");
        tfPages.setText("");
        chkIsAvailable.setSelected(true);
        selectedId = -1;
        table.clearSelection();
    }

    /**
     * Thiet lap cac su kien cho cac nut va bang
     */
    private void setupEvents() {
        // Su kien them tai lieu moi
        btnAdd.addActionListener(e -> {
            if (validateInput()) {
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

        // Su kien cap nhat tai lieu
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
                        chkIsAvailable.isSelected());
                if (library.updateDocument(doc)) {
                    JOptionPane.showMessageDialog(this, "Document updated successfully.");
                    loadAllDocuments();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update document.");
                }
            }
        });

        // Su kien xoa tai lieu
        btnRemove.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a document to remove.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure to delete this document?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION);
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

        // Su kien tim kiem tai lieu
        btnSearch.addActionListener(e -> {
            String keyword = tfSearch.getText().trim();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search keyword.");
                return;
            }

            // Xac dinh bo loc tim kiem
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
                default:
                    filter = DocumentFilter.ALL_FIELDS;
                    break;
            }

            // Thuc hien tim kiem va ap dung highlight
            List<Document> docs = library.findDocuments(keyword, filter);
            loadDocumentsToTable(docs);

            String context = cbFilter.getSelectedItem().toString();
            HighlightRenderer highlightRenderer = new HighlightRenderer(keyword, context);
            table.setDefaultRenderer(Object.class, highlightRenderer);
            table.setDefaultRenderer(String.class, highlightRenderer);
            table.repaint();
        });

        // Su kien reset tim kiem
        btnReset.addActionListener(e -> {
            tfSearch.setText("");
            cbFilter.setSelectedIndex(0);
            loadAllDocuments();
            // Khoi phuc renderer mac dinh
            table.setDefaultRenderer(Object.class, new GridCellRenderer());
            table.setDefaultRenderer(String.class, new GridCellRenderer());
            table.repaint();
        });

        // Su kien xoa form
        btnClear.addActionListener(e -> clearForm());

        // Su kien chon dong trong bang
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int selectedRow = table.getSelectedRow();
                selectedId = (int) table.getValueAt(selectedRow, 0);
                tfTitle.setText((String) table.getValueAt(selectedRow, 1));
                tfAuthor.setText((String) table.getValueAt(selectedRow, 2));
                tfLanguage.setText((String) table.getValueAt(selectedRow, 3));
                tfPages.setText(String.valueOf(table.getValueAt(selectedRow, 4)));
                tfYear.setText(String.valueOf(table.getValueAt(selectedRow, 5)));
                chkIsAvailable.setSelected((Boolean) table.getValueAt(selectedRow, 6));
            }
        });

        // Su kien double-click tren bang
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row == -1)
                    return;

                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    if (col == 2) { // Cot Author
                        openAuthorInfoForRow(row);
                    } else {
                        openImageForRow(row);
                    }
                }
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
        public Component getTableCellRendererComponent(
                JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
            setBackground(BG);

            // Chuyen chu thanh in hoa
            if (value != null) {
                setText(value.toString().toUpperCase());
            }
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            int right = (column < table.getColumnCount() - 1) ? 1 : 0;
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, right, GRID));
            return this;
        }
    }

    /**
     * Renderer mac dinh cho cac o trong bang
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

    // ===== Cac ham xu ly hinh anh =====

    /**
     * Mo hinh anh cho dong duoc chon
     */
    private void openImageForRow(int row) {
        try {
            int id = (int) table.getValueAt(row, 0);
            String title = String.valueOf(table.getValueAt(row, 1));
            showDocumentImage(id, title);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Cannot open image for this row.");
        }
    }

    /**
     * Hien thi hinh anh cua tai lieu
     */
    private void showDocumentImage(int documentId, String title) {
        File imageFile = tryFindImageFile(documentId, title);
        if (imageFile == null) {
            File placeholder = new File("src/images/background.jpg");
            if (placeholder.exists()) {
                imageFile = placeholder;
            }
        }

        if (imageFile == null || !imageFile.exists()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Khong tim thay hinh anh cho tai lieu nay.",
                    "Khong co hinh anh",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            BufferedImage buffered = ImageIO.read(imageFile);
            if (buffered == null)
                throw new IOException("Unsupported image format");
            Image scaled = scaleToFit(buffered, 350, 450);
            ImageIcon icon = new ImageIcon(scaled);
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JScrollPane sp = new JScrollPane(imageLabel);
            sp.setPreferredSize(new Dimension(380, 500));
            sp.getViewport().setBackground(Color.WHITE);

            JOptionPane.showMessageDialog(
                    this,
                    sp,
                    "Hinh anh: " + title,
                    JOptionPane.PLAIN_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Loi khi tai hinh anh: " + ex.getMessage());
        }
    }

    /**
     * Thu nho hinh anh cho vua khung hien thi
     */
    private Image scaleToFit(BufferedImage img, int maxWidth, int maxHeight) {
        int w = img.getWidth();
        int h = img.getHeight();
        double scale = Math.min(1.0, Math.min((double) maxWidth / w, (double) maxHeight / h));
        if (scale >= 1.0)
            return img;
        int newW = Math.max(1, (int) Math.round(w * scale));
        int newH = Math.max(1, (int) Math.round(h * scale));
        return img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
    }

    /**
     * Tim file hinh anh cua tai lieu theo ID hoac tieu de
     */
    private File tryFindImageFile(int documentId, String title) {
        String[] directories = {
                "src/images/documents",
                "src/images",
                "images/documents",
                "images",
                "."
        };
        String[] extensions = { "png", "jpg", "jpeg", "gif", "bmp" };

        String baseById = String.valueOf(documentId);
        String baseByTitle = sanitizeBaseName(title);

        // Tim theo ID truoc, sau do tim theo tieu de
        for (String dir : directories) {
            for (String ext : extensions) {
                File byId = new File(dir, baseById + "." + ext);
                if (byId.exists())
                    return byId;
                File byTitle = new File(dir, baseByTitle + "." + ext);
                if (byTitle.exists())
                    return byTitle;
            }
        }
        return null;
    }

    /**
     * Chuan hoa ten file tu chuoi dau vao
     */
    private String sanitizeBaseName(String input) {
        if (input == null)
            return "";
        String s = input.trim();
        s = s.replace('Đ', 'D').replace('đ', 'd');
        String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
        String withoutDiacritics = normalized.replaceAll("\\p{M}+", "");
        s = withoutDiacritics.toLowerCase();
        s = s.replaceAll("\\s+", "_");
        s = s.replaceAll("[^a-z0-9_\\-]", "");
        return s;
    }

    /**
     * Mo thong tin tac gia cho dong duoc chon
     */
    private void openAuthorInfoForRow(int row) {
        String author = String.valueOf(table.getValueAt(row, 2));
        showAuthorInfo(author);
    }

    /**
     * Hien thi thong tin chi tiet ve tac gia
     */
    private void showAuthorInfo(String author) {
        List<Document> docsByAuthor = library.findDocuments(author, DocumentFilter.AUTHOR);
        StringBuilder sb = new StringBuilder();
        sb.append("Tac gia: ").append(author).append('\n');
        sb.append("So tai lieu: ").append(docsByAuthor.size()).append('\n');
        sb.append("\nDanh sach tai lieu:\n");
        for (Document d : docsByAuthor) {
            sb.append("- [").append(d.getId()).append("] ")
                    .append(d.getTitle()).append(" (")
                    .append(d.getPublicationYear()).append(")\n");
        }

        // Tao text area cho thong tin tac gia
        JTextArea ta = new JTextArea(sb.toString());
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JScrollPane textScroll = new JScrollPane(ta);
        textScroll.setPreferredSize(new Dimension(420, 320));
        textScroll.getViewport().setBackground(Color.WHITE);

        // Tao label cho hinh anh tac gia
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.TOP);
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.WHITE);
        ImageIcon authorIcon = loadAuthorIcon(author);
        if (authorIcon != null) {
            imageLabel.setIcon(authorIcon);
        } else {
            imageLabel.setText("(Khong co anh)");
        }
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        imagePanel.setPreferredSize(new Dimension(220, 320));

        // Ket hop hinh anh va thong tin
        JPanel container = new JPanel(new BorderLayout(10, 0));
        container.add(imagePanel, BorderLayout.WEST);
        container.add(textScroll, BorderLayout.CENTER);
        container.setPreferredSize(new Dimension(660, 340));

        JOptionPane.showMessageDialog(
                this,
                container,
                "Thong tin tac gia",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Tai icon hinh anh cua tac gia
     */
    private ImageIcon loadAuthorIcon(String author) {
        File imageFile = tryFindAuthorImageFile(author);
        if (imageFile == null || !imageFile.exists()) {
            File placeholder = new File("src/images/background.jpg");
            if (placeholder.exists()) {
                imageFile = placeholder;
            } else {
                return null;
            }
        }
        try {
            BufferedImage buffered = ImageIO.read(imageFile);
            if (buffered == null)
                return null;
            Image scaled = scaleToFit(buffered, 200, 260);
            return new ImageIcon(scaled);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Tim file hinh anh cua tac gia
     */
    private File tryFindAuthorImageFile(String author) {
        String[] directories = {
                "src/images/authors",
                "src/images",
                "images/authors",
                "images",
                "."
        };
        String[] extensions = { "png", "jpg", "jpeg", "gif", "bmp" };
        String base = sanitizeBaseName(author);
        for (String dir : directories) {
            for (String ext : extensions) {
                File f = new File(dir, base + "." + ext);
                if (f.exists())
                    return f;
            }
        }
        return null;
    }
}