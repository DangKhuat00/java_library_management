package gui;

import javax.swing.*;
import java.awt.*;

public class DocumentPanel extends JPanel {
  public DocumentPanel() {
    setLayout(new BorderLayout());

    JLabel label = new JLabel("Danh sách tài liệu sẽ hiển thị ở đây");
    label.setHorizontalAlignment(SwingConstants.CENTER);
    add(label, BorderLayout.CENTER);
  }
}