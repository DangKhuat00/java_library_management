package gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import dao.AccountDAO;
import model.Account;

public class RegisterFrame extends JFrame {
  private JTextField txtUsername;
  private JPasswordField txtPassword;
  private JTextField txtPhone;
  private JComboBox<String> cbRole;

  public RegisterFrame() {
    setTitle("Đăng ký tài khoản");
    setSize(800, 500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(false);

    // Nền ảnh
    JPanel backgroundPanel = new JPanel() {
      ImageIcon backgroundImage = new ImageIcon(
          getClass().getResource("/images/background.jpg")
      );

      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
          g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
      }
    };
    backgroundPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 60));

    // Form trắng bo tròn
    JPanel formPanel = new JPanel(new GridBagLayout()) {
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
    formPanel.setPreferredSize(new Dimension(450, 380));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 20, 10, 20);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    Border roundedBorder = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    );

    // Tiêu đề
    JLabel lblTitle = new JLabel("Register", SwingConstants.CENTER);
    lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
    lblTitle.setForeground(new Color(0, 102, 255));
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    formPanel.add(lblTitle, gbc);

    // Username
    JLabel lblUsername = new JLabel("Username:");
    lblUsername.setFont(new Font("Arial", Font.PLAIN, 14));
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridx = 0;
    formPanel.add(lblUsername, gbc);

    txtUsername = new JTextField();
    txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
    txtUsername.setBackground(Color.WHITE);
    txtUsername.setPreferredSize(new Dimension(220, 30));
    txtUsername.setBorder(roundedBorder);
    gbc.gridx = 1;
    formPanel.add(txtUsername, gbc);

    // Password
    JLabel lblPassword = new JLabel("Password:");
    lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
    gbc.gridy = 2;
    gbc.gridx = 0;
    formPanel.add(lblPassword, gbc);

    txtPassword = new JPasswordField();
    txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
    txtPassword.setBackground(Color.WHITE);
    txtPassword.setPreferredSize(new Dimension(220, 30));
    txtPassword.setBorder(roundedBorder);
    gbc.gridx = 1;
    formPanel.add(txtPassword, gbc);

    // Phone
    JLabel lblPhone = new JLabel("Phone:");
    lblPhone.setFont(new Font("Arial", Font.PLAIN, 14));
    gbc.gridy = 3;
    gbc.gridx = 0;
    formPanel.add(lblPhone, gbc);

    txtPhone = new JTextField();
    txtPhone.setFont(new Font("Arial", Font.PLAIN, 14));
    txtPhone.setBackground(Color.WHITE);
    txtPhone.setPreferredSize(new Dimension(220, 30));
    txtPhone.setBorder(roundedBorder);
    gbc.gridx = 1;
    formPanel.add(txtPhone, gbc);

    // Role
    JLabel lblRole = new JLabel("Role:");
    lblRole.setFont(new Font("Arial", Font.PLAIN, 14));
    gbc.gridy = 4;
    gbc.gridx = 0;
    formPanel.add(lblRole, gbc);

    cbRole = new JComboBox<>(new String[]{"user", "admin"});
    cbRole.setFont(new Font("Arial", Font.PLAIN, 14));
    cbRole.setBackground(Color.WHITE);
    cbRole.setBorder(roundedBorder);
    gbc.gridx = 1;
    formPanel.add(cbRole, gbc);

    // Nút Đăng ký
    JButton btnRegister = new JButton("Đăng ký");
    btnRegister.setBackground(Color.WHITE);
    btnRegister.setForeground(new Color(0, 102, 255));
    btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
    btnRegister.setFocusPainted(false);
    btnRegister.setPreferredSize(new Dimension(120, 35));
    btnRegister.setBorder(roundedBorder);
    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    formPanel.add(btnRegister, gbc);

    // Nút Quay lại
    JButton btnBack = new JButton("Quay lại");
    btnBack.setBackground(Color.WHITE);
    btnBack.setForeground(new Color(0, 102, 255));
    btnBack.setFont(new Font("Arial", Font.BOLD, 14));
    btnBack.setFocusPainted(false);
    btnBack.setPreferredSize(new Dimension(120, 35));
    btnBack.setBorder(roundedBorder);
    gbc.gridy = 6;
    formPanel.add(btnBack, gbc);

    // Thêm form vào nền
    backgroundPanel.add(formPanel);
    add(backgroundPanel);

    // Sự kiện nút Đăng ký
    btnRegister.addActionListener(e -> {
      String username = txtUsername.getText().trim();
      String password = new String(txtPassword.getPassword()).trim();
      String phone = txtPhone.getText().trim();
      String role = cbRole.getSelectedItem().toString();

      if (username.isEmpty() || password.isEmpty() || phone.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
        return;
      }

      AccountDAO dao = new AccountDAO();
      boolean success = dao.register(new Account(username, password, phone, role));

      if (success) {
        JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
        new LoginFrame().setVisible(true);
        dispose();
      } else {
        JOptionPane.showMessageDialog(this, "Đăng ký thất bại! Tài khoản có thể đã tồn tại.");
      }
    });

    // Sự kiện nút Quay lại
    btnBack.addActionListener(e -> {
      new LoginFrame().setVisible(true);
      dispose();
    });
  }
}
