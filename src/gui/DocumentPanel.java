package gui;

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

public class DocumentPanel extends JPanel {
  private final Library library;
  private JTable table;
  private DefaultTableModel tableModel;

  private JTextField tfTitle, tfAuthor, tfLanguage, tfYear, tfPages;
  private JCheckBox chkIsAvailable;
  private JTextField tfSearch;
  private JComboBox<String> cbFilter;

  private RoundedButton btnAdd, btnUpdate, btnRemove, btnSearch, btnReset, btnClear;

  private int selectedId = -1;

  private static final Dimension BTN_SIZE_PRIMARY = new Dimension(140, 36);
  private static final Dimension BTN_SIZE_SMALL = new Dimension(110, 34);

  public DocumentPanel() {
    library = new Library();
    setupGUI();
    loadAllDocuments();
    setupEvents();
  }

  private void setupGUI() {
    setLayout(new BorderLayout(5, 5));
    setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

    JPanel northPanel = new JPanel(new BorderLayout(5, 5));

    // ===== Form nhập liệu =====
    JPanel formPanel = new JPanel(new GridLayout(6, 1, 0, 8));
    tfTitle = createField("Title:", formPanel, 250);
    tfAuthor = createField("Author:", formPanel, 250);
    tfLanguage = createField("Language:", formPanel, 250);
    tfYear = createField("Year:", formPanel, 250);
    tfPages = createField("Pages:", formPanel, 250);

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

    // ===== Panel bên phải =====
    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

    // ==== Thanh tìm kiếm & lọc ====
    JPanel searchAndFilterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
    tfSearch = new JTextField();
    tfSearch.setPreferredSize(new Dimension(250, 28));

    btnSearch = new RoundedButton("Search");
    btnSearch.setPreferredSize(BTN_SIZE_SMALL);
    btnReset = new RoundedButton("Reset");
    btnReset.setPreferredSize(BTN_SIZE_SMALL);

    cbFilter =
        new JComboBox<>(
            new String[] {
                "All Fields",
                "Id",
                "Title",
                "Language",
                "Pages",
                "Author",
                "PublicationYear",
                "Available"
            });
    cbFilter.setPreferredSize(new Dimension(140, 30));
    JLabel lblFilter = new JLabel("Filter by:");
    lblFilter.setFont(new Font("Segoe UI", Font.BOLD, 13));

    searchAndFilterPanel.add(lblFilter);
    searchAndFilterPanel.add(cbFilter);
    searchAndFilterPanel.add(Box.createRigidArea(new Dimension(10, 0)));
    searchAndFilterPanel.add(tfSearch);
    searchAndFilterPanel.add(btnSearch);
    searchAndFilterPanel.add(btnReset);

    // ==== Nhóm nút hành động ====
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

    rightPanel.add(Box.createVerticalGlue());
    rightPanel.add(searchAndFilterPanel);
    rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
    rightPanel.add(buttonWrapperPanel);
    rightPanel.add(Box.createVerticalGlue());

    northPanel.add(rightPanel, BorderLayout.CENTER);
    add(northPanel, BorderLayout.NORTH);

    // ==== Bảng hiển thị ====
    String[] columns = {
        "Id", "Title", "Author", "Language", "Pages", "PublicationYear", "Available"
    };
    tableModel =
        new DefaultTableModel(columns, 0) {
          @Override
          public boolean isCellEditable(int row, int col) {
            return false;
          }

          @Override
          public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 6) return Boolean.class;
            return super.getColumnClass(columnIndex);
          }
        };
    table = new JTable(tableModel);
    table.setRowHeight(25);
    table.setIntercellSpacing(new Dimension(0, 0));
    table.setShowGrid(false);
    table.setFillsViewportHeight(true);

    // Renderer cho dữ liệu và Boolean
    table.setDefaultRenderer(Object.class, new GridCellRenderer());
    table.setDefaultRenderer(Boolean.class, new CheckBoxCellRenderer());
    table.getTableHeader().setDefaultRenderer(new HeaderCellRenderer(table));

    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.getViewport().setBackground(Color.WHITE);
    // KHÔNG set border cho scrollPane!
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

  private void loadAllDocuments() {
    tableModel.setRowCount(0);
    List<Document> docs = library.getAllDocuments();
    loadDocumentsToTable(docs);
  }

  private void loadDocumentsToTable(List<Document> docs) {
    tableModel.setRowCount(0);
    for (Document doc : docs) {
      tableModel.addRow(
          new Object[] {
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
      Integer.parseInt(tfYear.getText().trim());
      Integer.parseInt(tfPages.getText().trim());
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this, "Year and Pages must be numbers.");
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
    chkIsAvailable.setSelected(true);
    selectedId = -1;
    table.clearSelection();
  }

  private void setupEvents() {
    btnAdd.addActionListener(
        e -> {
          if (validateInput()) {
            Document doc =
                new Document(
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

    btnUpdate.addActionListener(
        e -> {
          if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a document to update.");
            return;
          }
          if (validateInput()) {
            Document doc =
                new Document(
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

    btnRemove.addActionListener(
        e -> {
          if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a document to remove.");
            return;
          }
          int confirm =
              JOptionPane.showConfirmDialog(
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

    btnSearch.addActionListener(
        e -> {
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
              break;
            default:
              filter = DocumentFilter.ALL_FIELDS;
              break;
          }

          List<Document> docs = library.findDocuments(keyword, filter);
          loadDocumentsToTable(docs);
        });

    btnReset.addActionListener(
        e -> {
          tfSearch.setText("");
          cbFilter.setSelectedIndex(0);
          loadAllDocuments();
        });

    btnClear.addActionListener(e -> clearForm());

    table
        .getSelectionModel()
        .addListSelectionListener(
            e -> {
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

    // Double-click actions: image (default) or author info when double-clicking the Author column
    table.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            int row = table.rowAtPoint(e.getPoint());
            int col = table.columnAtPoint(e.getPoint());
            if (row == -1) return;

            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
              if (col == 2) { // Author column
                openAuthorInfoForRow(row);
              } else {
                openImageForRow(row);
              }
            }
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
    public Component getTableCellRendererComponent(
        JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
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

  // === Renderer cho cell: căn giữa dữ liệu, viền phải VÀ dưới (hàng ngang!) ===
  private static class GridCellRenderer extends DefaultTableCellRenderer {
    private static final Color GRID = new Color(160, 160, 160);
    private static final Color ALT_ROW = new Color(248, 248, 248);

    public GridCellRenderer() {
      setHorizontalAlignment(CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      Component c =
          super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      if (c instanceof JComponent) {
        JComponent jc = (JComponent) c;
        jc.setOpaque(true);

        if (!isSelected) {
          jc.setBackground((row % 2 == 0) ? ALT_ROW : Color.WHITE);
        }

        int right = (column < table.getColumnCount() - 1) ? 1 : 0;
        int bottom = 1; // Luôn vẽ viền dưới để luôn có hàng ngang!
        jc.setBorder(BorderFactory.createMatteBorder(0, 0, bottom, right, GRID));
      }
      if (c instanceof JLabel) {
        ((JLabel) c).setHorizontalAlignment(CENTER);
      }
      return c;
    }
  }

  // Renderer cho cell kiểu Boolean có border giống các cell khác
  // Renderer cho cell kiểu Boolean có border giống các cell khác (bọc vào JPanel)
  private static class CheckBoxCellRenderer extends JPanel
      implements javax.swing.table.TableCellRenderer {
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
      int bottom = 1; // luôn vẽ hàng ngang!
      setBorder(BorderFactory.createMatteBorder(0, 0, bottom, right, GRID));
      return this;
    }
  }

  // ===== Image viewing helpers =====
  private void openImageForRow(int row) {
    try {
      int id = (int) table.getValueAt(row, 0);
      String title = String.valueOf(table.getValueAt(row, 1));
      showDocumentImage(id, title);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "Cannot open image for this row.");
    }
  }

  private void showDocumentImage(int documentId, String title) {
    File imageFile = tryFindImageFile(documentId, title);
    if (imageFile == null) {
      // Fallback to a known placeholder if exists
      File placeholder = new File("src/images/background.jpg");
      if (placeholder.exists()) {
        imageFile = placeholder;
      }
    }

    if (imageFile == null || !imageFile.exists()) {
      JOptionPane.showMessageDialog(
          this,
          "Không tìm thấy hình ảnh cho tài liệu này.",
          "Không có hình ảnh",
          JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      BufferedImage buffered = ImageIO.read(imageFile);
      if (buffered == null) throw new IOException("Unsupported image format");
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
          "Hình ảnh: " + title,
          JOptionPane.PLAIN_MESSAGE);
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(this, "Lỗi khi tải hình ảnh: " + ex.getMessage());
    }
  }

  private Image scaleToFit(BufferedImage img, int maxWidth, int maxHeight) {
    int w = img.getWidth();
    int h = img.getHeight();
    double scale = Math.min(1.0, Math.min((double) maxWidth / w, (double) maxHeight / h));
    if (scale >= 1.0) return img;
    int newW = Math.max(1, (int) Math.round(w * scale));
    int newH = Math.max(1, (int) Math.round(h * scale));
    return img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
  }

  private File tryFindImageFile(int documentId, String title) {
    String[] directories = {
        "src/images/documents",
        "src/images",
        "images/documents",
        "images",
        "."
    };
    String[] extensions = {"png", "jpg", "jpeg", "gif", "bmp"};

    String baseById = String.valueOf(documentId);
    String baseByTitle = sanitizeBaseName(title);

    for (String dir : directories) {
      for (String ext : extensions) {
        File byId = new File(dir, baseById + "." + ext);
        if (byId.exists()) return byId;
        File byTitle = new File(dir, baseByTitle + "." + ext);
        if (byTitle.exists()) return byTitle;
      }
    }
    return null;
  }

  private String sanitizeBaseName(String input) {
    if (input == null) return "";
    String s = input.trim();
    // Xử lý riêng ký tự Đ/đ rồi loại bỏ dấu bằng Normalizer
    s = s.replace('Đ', 'D').replace('đ', 'd');
    String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
    String withoutDiacritics = normalized.replaceAll("\\p{M}+", "");
    s = withoutDiacritics.toLowerCase();
    s = s.replaceAll("\\s+", "_");
    s = s.replaceAll("[^a-z0-9_\\-]", "");
    return s;
  }

  private void openAuthorInfoForRow(int row) {
    String author = String.valueOf(table.getValueAt(row, 2));
    showAuthorInfo(author);
  }

  private void showAuthorInfo(String author) {
    List<Document> docsByAuthor = library.findDocuments(author, DocumentFilter.AUTHOR);
    StringBuilder sb = new StringBuilder();
    sb.append("Tác giả: ").append(author).append('\n');
    sb.append("Số tài liệu: ").append(docsByAuthor.size()).append('\n');
    sb.append("\nDanh sách tài liệu:\n");
    for (Document d : docsByAuthor) {
      sb.append("- [").append(d.getId()).append("] ")
          .append(d.getTitle()).append(" (")
          .append(d.getPublicationYear()).append(")\n");
    }

    JTextArea ta = new JTextArea(sb.toString());
    ta.setEditable(false);
    ta.setLineWrap(true);
    ta.setWrapStyleWord(true);
    ta.setFont(new Font("Segoe UI", Font.PLAIN, 13));

    JScrollPane textScroll = new JScrollPane(ta);
    textScroll.setPreferredSize(new Dimension(420, 320));
    textScroll.getViewport().setBackground(Color.WHITE);

    JLabel imageLabel = new JLabel();
    imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    imageLabel.setVerticalAlignment(SwingConstants.TOP);
    imageLabel.setOpaque(true);
    imageLabel.setBackground(Color.WHITE);
    ImageIcon authorIcon = loadAuthorIcon(author);
    if (authorIcon != null) {
      imageLabel.setIcon(authorIcon);
    } else {
      imageLabel.setText("(Không có ảnh)");
    }
    JPanel imagePanel = new JPanel(new BorderLayout());
    imagePanel.setBackground(Color.WHITE);
    imagePanel.add(imageLabel, BorderLayout.CENTER);
    imagePanel.setPreferredSize(new Dimension(220, 320));

    JPanel container = new JPanel(new BorderLayout(10, 0));
    container.add(imagePanel, BorderLayout.WEST);
    container.add(textScroll, BorderLayout.CENTER);
    container.setPreferredSize(new Dimension(660, 340));

    JOptionPane.showMessageDialog(
        this,
        container,
        "Thông tin tác giả",
        JOptionPane.INFORMATION_MESSAGE);
  }

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
      if (buffered == null) return null;
      Image scaled = scaleToFit(buffered, 200, 260);
      return new ImageIcon(scaled);
    } catch (IOException e) {
      return null;
    }
  }

  private File tryFindAuthorImageFile(String author) {
    String[] directories = {
        "src/images/authors",
        "src/images",
        "images/authors",
        "images",
        "."
    };
    String[] extensions = {"png", "jpg", "jpeg", "gif", "bmp"};
    String base = sanitizeBaseName(author);
    for (String dir : directories) {
      for (String ext : extensions) {
        File f = new File(dir, base + "." + ext);
        if (f.exists()) return f;
      }
    }
    return null;
  }
}