package gui;

import model.User;
import model.Document;
import dao.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BorrowPanel extends JPanel {

  private JTextArea borrowTextArea;java -version


  public BorrowPanel() {
    setLayout(new BorderLayout());

    JLabel titleLabel = new JLabel("Danh sách mượn tài liệu");
    titleLabel.setHorizontalAlignment(JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

    borrowTextArea = new JTextArea();
    borrowTextArea.setEditable(false);
    borrowTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
    JScrollPane scrollPane = new JScrollPane(borrowTextArea);

    add(titleLabel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);

    loadBorrowedData();
  }

  private void loadBorrowedData() {
    UserDAO userDAO = new UserDAO();
    List<User> users = userDAO.getAllUsers(); // gọi non-static method đúng cách
    StringBuilder sb = new StringBuilder();

    for (User user : users) {
      List<Document> borrowedDocs = user.getBorrowedDocuments();
      if (!borrowedDocs.isEmpty()) {
        sb.append("Người dùng: ").append(user.getName()).append(" (ID: ").append(user.getId()).append(")\n");
        for (Document doc : borrowedDocs) {
          sb.append("   - ").append(doc.getTitle()).append(" (ID: ").append(doc.getId()).append(")\n");
        }
        sb.append("\n");
      }
    }

    if (sb.length() == 0) {
      sb.append("Không có tài liệu nào được mượn.");
    }

    borrowTextArea.setText(sb.toString());
  }
}