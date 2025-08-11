package gui;

import model.User;
import model.Document;
import dao.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * JPanel hien thi danh sach nguoi dung va cac tai lieu ho dang muon. Su dung JTextArea de hien thi
 * thong tin o dang text cuon duoc.
 */
public class BorrowPanel extends JPanel {

  private JTextArea borrowTextArea;

  /**
   * Khoi tao panel hien thi danh sach muon tai lieu. Thiet lap layout, tieu de va goi ham
   * loadBorrowedData() de tai du lieu.
   */
  public BorrowPanel() {
    setLayout(new BorderLayout());

    JLabel titleLabel = new JLabel("Danh sach muon tai lieu");
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

  /**
   * Tai du lieu nguoi dung va tai lieu ho muon tu co so du lieu. Su dung UserDAO de lay danh sach
   * nguoi dung va in ra thong tin neu nguoi dung co tai lieu dang muon.
   */
  private void loadBorrowedData() {
    UserDAO userDAO = new UserDAO();
    List<User> users = userDAO.getAllUsers(); // Goi phuong thuc lay danh sach nguoi dung
    StringBuilder sb = new StringBuilder();

    for (User user : users) {
      List<Document> borrowedDocs = user.getBorrowedDocuments();
      if (!borrowedDocs.isEmpty()) {
        sb.append("Nguoi dung: ")
            .append(user.getName())
            .append(" (ID: ")
            .append(user.getId())
            .append(")\n");
        for (Document doc : borrowedDocs) {
          sb.append("   - ")
              .append(doc.getTitle())
              .append(" (ID: ")
              .append(doc.getId())
              .append(")\n");
        }
        sb.append("\n");
      }
    }

    if (sb.length() == 0) {
      sb.append("Khong co tai lieu nao duoc muon.");
    }

    borrowTextArea.setText(sb.toString());
  }
}
