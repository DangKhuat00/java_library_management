package gui;

import javax.swing.*;
import java.awt.*;

public class BorrowPanel extends JPanel {
  private JComboBox<String> userComboBox;
  private JComboBox<String> documentComboBox;
  private JButton borrowButton;
  private JButton returnButton;
  private JTextArea messageArea;

  public BorrowPanel() {
    setLayout(new BorderLayout());

    // === Top panel: chọn user và tài liệu ===
    JPanel topPanel = new JPanel(new GridLayout(2, 2, 10, 10));
    topPanel.setBorder(BorderFactory.createTitledBorder("Borrow/Return"));

    userComboBox = new JComboBox<>(new String[]{"User 1", "User 2", "User 3"});
    documentComboBox = new JComboBox<>(new String[]{"Document A", "Document B", "Document C"});

    topPanel.add(new JLabel("Select User:"));
    topPanel.add(userComboBox);
    topPanel.add(new JLabel("Select Document:"));
    topPanel.add(documentComboBox);

    // === Middle panel: các nút ===
    JPanel buttonPanel = new JPanel();
    borrowButton = new JButton("Borrow");
    returnButton = new JButton("Return");

    buttonPanel.add(borrowButton);
    buttonPanel.add(returnButton);

    // === Bottom panel: hiển thị thông báo ===
    messageArea = new JTextArea(5, 40);
    messageArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(messageArea);

    // === Action listeners đơn giản ===
    borrowButton.addActionListener(e -> {
      String user = (String) userComboBox.getSelectedItem();
      String doc = (String) documentComboBox.getSelectedItem();
      messageArea.append("✔ " + user + " borrowed " + doc + "\n");
    });

    returnButton.addActionListener(e -> {
      String user = (String) userComboBox.getSelectedItem();
      String doc = (String) documentComboBox.getSelectedItem();
      messageArea.append("↩ " + user + " returned " + doc + "\n");
    });

    // Add everything to main panel
    add(topPanel, BorderLayout.NORTH);
    add(buttonPanel, BorderLayout.CENTER);
    add(scrollPane, BorderLayout.SOUTH);
  }
}