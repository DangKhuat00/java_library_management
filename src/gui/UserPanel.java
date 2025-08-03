package gui;

import javax.swing.*;
import java.awt.*;

public class UserPanel extends JPanel {
  public UserPanel() {
    setLayout(new BorderLayout());

    JLabel label = new JLabel("Danh sách người dùng sẽ hiển thị ở đây");
    label.setHorizontalAlignment(SwingConstants.CENTER);
    add(label, BorderLayout.CENTER);
  }
}