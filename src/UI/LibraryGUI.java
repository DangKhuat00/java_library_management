package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibraryGUI extends JFrame {

  public LibraryGUI() {
    setTitle("Library Management System");
    setSize(600, 400);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null); // Căn giữa cửa sổ

    // Tạo panel chính và layout
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(0, 1, 10, 10));

    // Tạo các nút thao tác
    JButton addDocBtn = new JButton("Add Document");
    JButton removeDocBtn = new JButton("Remove Document");
    JButton updateDocBtn = new JButton("Update Document");
    JButton findDocBtn = new JButton("Find Document");
    JButton displayDocBtn = new JButton("Display All Documents");

    // Thêm các nút vào panel
    panel.add(addDocBtn);
    panel.add(removeDocBtn);
    panel.add(updateDocBtn);
    panel.add(findDocBtn);
    panel.add(displayDocBtn);

    // Gắn panel vào cửa sổ
    add(panel);

    // Thêm sự kiện ví dụ
    addDocBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Add Document Clicked"));
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new LibraryGUI().setVisible(true));
  }
}