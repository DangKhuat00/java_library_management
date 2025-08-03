package gui;

import javax.swing.*;

public class LibraryAppGUI {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      new MainFrame().setVisible(true);
    });
  }
}