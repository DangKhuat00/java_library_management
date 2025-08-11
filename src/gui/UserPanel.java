package gui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * UserPanel la JPanel hien thi danh sach nguoi dung tu co so du lieu. Su dung {@link UserDAO} de
 * lay thong tin va hien thi len giao dien.
 */
public class UserPanel extends JPanel {
  /** Vung hien thi thong tin nguoi dung */
  private JTextArea displayArea;

  /** Doi tuong truy cap du lieu nguoi dung */
  private UserDAO userDAO;

  /**
   * Khoi tao UserPanel va hien thi danh sach nguoi dung. Mac dinh se goi {@link #updateDisplay()}
   * de nap du lieu tu co so du lieu.
   */
  public UserPanel() {
    setLayout(new BorderLayout());

    userDAO = new UserDAO();
    displayArea = new JTextArea();
    displayArea.setEditable(false);
    displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

    JScrollPane scrollPane = new JScrollPane(displayArea);
    add(scrollPane, BorderLayout.CENTER);

    updateDisplay();
  }

  /**
   * Cap nhat vung hien thi voi danh sach nguoi dung hien co. Neu khong co nguoi dung nao, se hien
   * thi thong bao tuong ung.
   */
  private void updateDisplay() {
    List<User> users = userDAO.getAllUsers();
    displayArea.setText("");

    if (users.isEmpty()) {
      displayArea.setText("Khong co nguoi dung nao trong he thong.");
    } else {
      for (User user : users) {
        displayArea.append(user.toString() + "\n");
      }
    }
  }
}
