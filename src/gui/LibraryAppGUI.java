package gui;

import javax.swing.*;

/**
 * Lop chinh de chay ung dung thu vien voi giao dien nguoi dung. Su dung SwingUtilities.invokeLater
 * de dam bao tao giao dien tren EDT (Event Dispatch Thread).
 */
public class LibraryAppGUI {

  /**
   * Ham main khoi dong ung dung.
   *
   * @param args doi so dong lenh (khong su dung trong chuong trinh nay)
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(
        () -> {
          new MainFrame().setVisible(true);
        });
  }
}
