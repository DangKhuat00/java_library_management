package gui;

import model.Document;
import dao.DocumentDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * JPanel hien thi danh sach tai lieu. Su dung JTextArea de hien thi thong tin tai lieu o dang text
 * cuon duoc.
 */
public class DocumentPanel extends JPanel {
  private final JTextArea displayArea;
  private final DocumentDAO documentDAO;

  /**
   * Khoi tao panel hien thi danh sach tai lieu. Thiet lap layout, tao text area va goi
   * updateDisplay() de tai du lieu ban dau.
   */
  public DocumentPanel() {
    setLayout(new BorderLayout());

    documentDAO = new DocumentDAO();
    displayArea = new JTextArea(20, 40);
    displayArea.setEditable(false);

    JScrollPane scrollPane = new JScrollPane(displayArea);
    add(scrollPane, BorderLayout.CENTER);

    updateDisplay();
  }

  /**
   * Cap nhat noi dung hien thi danh sach tai lieu. Lay danh sach tai lieu tu DocumentDAO va in ra
   * man hinh. Neu khong co tai lieu nao, hien thong bao tuong ung.
   */
  private void updateDisplay() {
    displayArea.setText("");

    List<Document> documents = documentDAO.getAllDocuments();
    if (documents.isEmpty()) {
      displayArea.append("Khong co tai lieu nao.\n");
    } else {
      for (Document doc : documents) {
        displayArea.append(doc.toString() + "\n");
      }
    }
  }
}
