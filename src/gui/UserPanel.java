package gui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UserPanel extends JPanel {
  private JTextArea displayArea;
  private UserDAO userDAO;

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

  private void updateDisplay() {
    List<User> users = userDAO.getAllUsers();
    displayArea.setText("");

    if (users.isEmpty()) {
      displayArea.setText("Không có người dùng nào trong hệ thống.");
    } else {
      for (User user : users) {
        displayArea.append(user.toString() + "\n");
      }
    }
  }
}