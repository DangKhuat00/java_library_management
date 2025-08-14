// Goi package gui
package gui;

// Import cac thu vien can thiet
import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import dao.AccountDAO;
import model.Account;

/**
 * Giao dien dang ky tai khoan moi
 * Cho phep nguoi dung tao tai khoan moi trong he thong
 */
public class RegisterFrame extends JFrame {
  // Cac truong nhap lieu
  private JTextField txtUsername;
  private JPasswordField txtPassword;
  private JTextField txtPhone;
  private JTextField txtEmail;
  private JComboBox<String> cbRole;

  /**
   * Khoi tao cua so dang ky
   */
  public RegisterFrame() {
    // Thiet lap thuoc tinh cua so
    setTitle("Đăng ký tài khoản");
    setSize(800, 600); // tang chieu cao de khong mat nut
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(false);

    // Panel nen voi hinh anh
    JPanel backgroundPanel = new JPanel() {
      ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/images/background.jpg"));
      
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Ve hinh nen neu co
        if (backgroundImage != null) {
          g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
      }
    };
    backgroundPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 60));

    // Form trang bo tron
    JPanel formPanel = new JPanel(new GridBagLayout()) {
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
    formPanel.setPreferredSize(new Dimension(450, 540)); // tang chieu cao form

    // Thiet lap layout va khoang cach
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 20, 10, 20);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Tao vien bo tron
    Border roundedBorder = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    );

    // Tieu de form dang ky
    JLabel lblTitle = new JLabel("Register", SwingConstants.CENTER);
    lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
    lblTitle.setForeground(new Color(0, 102, 255));
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    formPanel.add(lblTitle, gbc);

    // Label va truong nhap ten dang nhap
    JLabel lblUsername = new JLabel("Username:");
    lblUsername.setFont(new Font("Arial", Font.PLAIN, 14));
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridx = 0;
    formPanel.add(lblUsername, gbc);

    txtUsername = new JTextField();
    txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
    txtUsername.setPreferredSize(new Dimension(240, 30)); // rong hon
    txtUsername.setBorder(roundedBorder);
    gbc.gridx = 1;
    formPanel.add(txtUsername, gbc);

    // Label va truong nhap mat khau
    JLabel lblPassword = new JLabel("Password:");
    lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
    gbc.gridy = 2;
    gbc.gridx = 0;
    formPanel.add(lblPassword, gbc);

    txtPassword = new JPasswordField();
    txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
    txtPassword.setPreferredSize(new Dimension(240, 30)); // rong hon
    txtPassword.setBorder(roundedBorder);
    gbc.gridx = 1;
    formPanel.add(txtPassword, gbc);

    // Label va truong nhap so dien thoai
    JLabel lblPhone = new JLabel("Phone:");
    lblPhone.setFont(new Font("Arial", Font.PLAIN, 14));
    gbc.gridy = 3;
    gbc.gridx = 0;
    formPanel.add(lblPhone, gbc);

    txtPhone = new JTextField();
    txtPhone.setFont(new Font("Arial", Font.PLAIN, 14));
    txtPhone.setPreferredSize(new Dimension(240, 30));
    txtPhone.setBorder(roundedBorder);
    gbc.gridx = 1;
    formPanel.add(txtPhone, gbc);

    // Label va truong nhap email
    JLabel lblEmail = new JLabel("Email:");
    lblEmail.setFont(new Font("Arial", Font.PLAIN, 14));
    gbc.gridy = 4;
    gbc.gridx = 0;
    formPanel.add(lblEmail, gbc);

    txtEmail = new JTextField();
    txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
    txtEmail.setPreferredSize(new Dimension(240, 30));
    txtEmail.setBorder(roundedBorder);
    gbc.gridx = 1;
    formPanel.add(txtEmail, gbc);

    // Label va dropdown lua chon vai tro
    JLabel lblRole = new JLabel("Role:");
    lblRole.setFont(new Font("Arial", Font.PLAIN, 14));
    gbc.gridy = 5;
    gbc.gridx = 0;
    formPanel.add(lblRole, gbc);

    cbRole = new JComboBox<>(new String[]{"user", "admin"});
    cbRole.setFont(new Font("Arial", Font.PLAIN, 14));
    cbRole.setPreferredSize(new Dimension(240, 30));
    cbRole.setBorder(roundedBorder);
    gbc.gridx = 1;
    formPanel.add(cbRole, gbc);

    // Nut dang ky
    JButton btnRegister = new JButton("Đăng ký");
    btnRegister.setForeground(new Color(0, 102, 255));
    btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
    btnRegister.setPreferredSize(new Dimension(140, 35));
    btnRegister.setBorder(roundedBorder);
    gbc.gridx = 0;
    gbc.gridy = 6;
    gbc.gridwidth = 2;
    formPanel.add(btnRegister, gbc);

    // Nut quay lai
    JButton btnBack = new JButton("Quay lại");
    btnBack.setForeground(new Color(0, 102, 255));
    btnBack.setFont(new Font("Arial", Font.BOLD, 14));
    btnBack.setPreferredSize(new Dimension(140, 35));
    btnBack.setBorder(roundedBorder);
    gbc.gridy = 7;
    formPanel.add(btnBack, gbc);

    // Them form vao nen
    backgroundPanel.add(formPanel);
    add(backgroundPanel);

    // Su kien nut dang ky
    btnRegister.addActionListener(e -> {
      // Lay thong tin tu cac truong nhap lieu
      String username = txtUsername.getText().trim();
      String password = new String(txtPassword.getPassword()).trim();
      String phone = txtPhone.getText().trim();
      String email = txtEmail.getText().trim();
      String role = cbRole.getSelectedItem().toString();

      // Kiem tra tinh hop le cua du lieu
      if (username.isEmpty() || password.isEmpty() || phone.isEmpty() || email.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
        return;
      }

      // Thuc hien dang ky thong qua DAO
      AccountDAO dao = new AccountDAO();
      boolean success = dao.register(new Account(username, password, phone, email, role));

      // Xu ly ket qua dang ky
      if (success) {
        JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
        // Chuyen ve cua so dang nhap
        new LoginFrame().setVisible(true);
        dispose();
      } else {
        JOptionPane.showMessageDialog(this, "Đăng ký thất bại! Tài khoản có thể đã tồn tại.");
      }
    });

    // Su kien nut quay lai
    btnBack.addActionListener(e -> {
      // Quay ve cua so dang nhap
      new LoginFrame().setVisible(true);
      dispose();
    });
  }
}