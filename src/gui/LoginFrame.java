// Goi package gui
package gui;

// Import cac thu vien can thiet
import dao.AccountDAO;
import model.Account;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;

/**
 * Giao dien dang nhap he thong
 * Cho phep nguoi dung dang nhap vao he thong quan ly thu vien
 */
public class LoginFrame extends JFrame {
  // Cac truong nhap lieu
  private JTextField usernameField;
  private JPasswordField passwordField;

  /**
   * Khoi tao cua so dang nhap
   */
  public LoginFrame() {
    // Thiet lap thuoc tinh cua so
    setTitle("Đăng nhập hệ thống thư viện");
    setSize(800, 500); // Tang kich thuoc cua so
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(false);

    // Tao panel nen voi hinh anh
    JPanel backgroundPanel =
        new JPanel() {
          ImageIcon backgroundImage =
              new ImageIcon(getClass().getResource("/images/background.jpg"));

          @Override
          protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Ve hinh nen neu co
            if (backgroundImage != null) {
              g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
          }
        };

    // Tao panel form dang nhap bo tron mau trang
    JPanel formPanel =
        new JPanel(new GridBagLayout()) {
          @Override
          protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Ve nen trang bo tron
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.dispose();
          }
        };
    formPanel.setOpaque(false);
    formPanel.setPreferredSize(new Dimension(450, 300)); // Tang kich thuoc form

    // Thiet lap layout va khoang cach
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 20, 10, 20);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Tao vien bo tron mau xam nhat
    Border roundedBorder =
        BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10) // padding trong
            );

    // Tieu de form dang nhap
    JLabel title = new JLabel("Sign In", SwingConstants.CENTER);
    title.setFont(new Font("Arial", Font.BOLD, 28));
    title.setForeground(new Color(0, 102, 255));
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    formPanel.add(title, gbc);

    // Label cho truong ten dang nhap
    JLabel userLabel = new JLabel("Username:");
    userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    formPanel.add(userLabel, gbc);

    // Truong nhap ten dang nhap
    usernameField = new JTextField();
    usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
    usernameField.setBackground(Color.WHITE);
    usernameField.setPreferredSize(new Dimension(220, 30)); // giong dang ky
    usernameField.setBorder(roundedBorder);
    gbc.gridx = 1;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    formPanel.add(usernameField, gbc);

    // Label cho truong mat khau
    JLabel passLabel = new JLabel("Password:");
    passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    gbc.gridx = 0;
    gbc.gridy = 2;
    formPanel.add(passLabel, gbc);

    // Truong nhap mat khau
    passwordField = new JPasswordField();
    passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
    passwordField.setBackground(Color.WHITE);
    passwordField.setPreferredSize(new Dimension(220, 30)); // giong dang ky
    passwordField.setBorder(roundedBorder);
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    formPanel.add(passwordField, gbc);

    // Nut dang nhap
    JButton loginButton = new JButton("Login");
    loginButton.setBackground(Color.WHITE);
    loginButton.setForeground(new Color(0, 102, 255));
    loginButton.setFont(new Font("Arial", Font.BOLD, 14));
    loginButton.setFocusPainted(false);
    loginButton.setPreferredSize(new Dimension(120, 35));
    loginButton.setBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    formPanel.add(loginButton, gbc);

    // Them form vao nen, can giua nhung lui xuong mot it
    backgroundPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 60)); // 60px top margin
    backgroundPanel.add(formPanel);
    add(backgroundPanel);

    // Nut dang ky tai khoan moi
    JButton registerButton = new JButton("Đăng ký");
    registerButton.setBackground(Color.WHITE);
    registerButton.setForeground(new Color(0, 102, 255));
    registerButton.setFont(new Font("Arial", Font.BOLD, 14));
    registerButton.setFocusPainted(false);
    registerButton.setPreferredSize(new Dimension(120, 35));
    registerButton.setBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    gbc.gridy = 4;
    formPanel.add(registerButton, gbc);

    // Su kien bam nut dang ky
    registerButton.addActionListener(
        e -> {
          // Mo cua so dang ky va dong cua so hien tai
          new RegisterFrame().setVisible(true);
          dispose();
        });

    // Su kien bam nut dang nhap
    loginButton.addActionListener(
        e -> {
          // Lay thong tin nhap vao
          String username = usernameField.getText();
          String password = new String(passwordField.getPassword());

          // Kiem tra dang nhap thong qua DAO
          AccountDAO dao = new AccountDAO();
          Account acc = dao.login(username, password);

          // Xu ly ket qua dang nhap
          if (acc != null) {
            // Dang nhap thanh cong
            JOptionPane.showMessageDialog(
                this,
                "Đăng nhập thành công! Xin chào " + acc.getUsername() + " (" + acc.getRole() + ")");
            // Mo cua so chinh va dong cua so dang nhap
            new MainFrame().setVisible(true);
            dispose();
          } else {
            // Dang nhap that bai
            JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!");
          }
        });
  }
}