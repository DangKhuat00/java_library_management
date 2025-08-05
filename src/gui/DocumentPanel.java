package gui;

import model.Document;
import dao.DocumentDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DocumentPanel extends JPanel {
  private final JTextArea displayArea;
  private final DocumentDAO documentDAO;

  public DocumentPanel() {
    setLayout(new BorderLayout());

    documentDAO = new DocumentDAO();
    displayArea = new JTextArea(20, 40);
    displayArea.setEditable(false);

    JScrollPane scrollPane = new JScrollPane(displayArea);
    add(scrollPane, BorderLayout.CENTER);

    updateDisplay();
  }

  private void updateDisplay() {
    displayArea.setText("");

    List<Document> documents = documentDAO.getAllDocuments();
    if (documents.isEmpty()) {
      displayArea.append("Không có tài liệu nào.\n");
    } else {
      for (Document doc : documents) {
        displayArea.append(doc.toString() + "\n");
      }
    }
  }
}