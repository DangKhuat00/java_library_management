package model;

public class Account {
  private int id;
  private String username;
  private String password;
  private String role;
  private String phone;

  public Account() {}

  // Constructor đầy đủ
  public Account(String username, String password, String phone, String role) {
    this.username = username;
    this.password = password;
    this.phone = phone;
    this.role = role;
  }

  public Account(String username, String password, String role) {
    this.username = username;
    this.password = password;
    this.role = role;
  }

  // Getter và Setter
  public int getId() {
    return id;
  }

  public String getPhone() {
    return phone;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
