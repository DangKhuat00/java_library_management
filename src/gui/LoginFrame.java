package gui;

import dao.AccountDAO;
import model.Account;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;

public class LoginFrame extends JFrame {
  private JTextField usernameField;
  private JPasswordField passwordField;

  public LoginFrame() {
    setTitle("Đăng nhập hệ thống thư viện");
    setSize(800, 500); // Tăng kích thước cửa sổ
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(false);

    JPanel backgroundPanel =
        new JPanel() {
          ImageIcon backgroundImage =
              new ImageIcon(getClass().getResource("/images/background.jpg"));

          @Override
          protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
              g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
          }
        };

    // Panel trắng bo tròn
    JPanel formPanel =
        new JPanel(new GridBagLayout()) {
          @Override
          protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.dispose();
          }
        };
    formPanel.setOpaque(false);
    formPanel.setPreferredSize(new Dimension(450, 300)); // Tăng kích thước form

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 20, 10, 20);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Tạo border bo tròn màu xám nhạt
    Border roundedBorder =
        BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10) // padding trong
            );

    // Tiêu đề
    JLabel title = new JLabel("Sign In", SwingConstants.CENTER);
    title.setFont(new Font("Arial", Font.BOLD, 28));
    title.setForeground(new Color(0, 102, 255));
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    formPanel.add(title, gbc);

    // Label Username
    JLabel userLabel = new JLabel("Username:");
    userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    formPanel.add(userLabel, gbc);

    // Ô nhập Username
    usernameField = new JTextField();
    usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
    usernameField.setBackground(Color.WHITE);
    usernameField.setPreferredSize(new Dimension(220, 30)); // giống đăng ký
    usernameField.setBorder(roundedBorder);
    gbc.gridx = 1;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    formPanel.add(usernameField, gbc);

    // Label Password
    JLabel passLabel = new JLabel("Password:");
    passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    gbc.gridx = 0;
    gbc.gridy = 2;
    formPanel.add(passLabel, gbc);

    // Ô nhập Password
    passwordField = new JPasswordField();
    passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
    passwordField.setBackground(Color.WHITE);
    passwordField.setPreferredSize(new Dimension(220, 30)); // giống đăng ký
    passwordField.setBorder(roundedBorder);
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    formPanel.add(passwordField, gbc);

    // Nút Login
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

    // Thêm form vào nền, căn giữa nhưng lùi xuống một ít
    backgroundPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 60)); // 60px top margin
    backgroundPanel.add(formPanel);
    add(backgroundPanel);

    // Nút Đăng ký
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

    // Sự kiện bấm Đăng ký
    registerButton.addActionListener(
        e -> {
          new RegisterFrame().setVisible(true);
          dispose();
        });

    // Xử lý đăng nhập
    loginButton.addActionListener(
        e -> {
          String username = usernameField.getText();
          String password = new String(passwordField.getPassword());

          AccountDAO dao = new AccountDAO();
          Account acc = dao.login(username, password);

          if (acc != null) {
            JOptionPane.showMessageDialog(
                this,
                "Đăng nhập thành công! Xin chào " + acc.getUsername() + " (" + acc.getRole() + ")");
            new MainFrame().setVisible(true);
            dispose();
          } else {
            JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!");
          }
        });
  }
}
