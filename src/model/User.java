package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp {@code User} đại diện cho người dùng trong hệ thống thư viện.
 *
 * <p>Mỗi người dùng có ID duy nhất, thông tin cá nhân và danh sách tài liệu đã mượn.
 */
public class User {

  /** Mã định danh duy nhất của người dùng. */
  private String id;

  /** Tên người dùng. */
  private String name;

  /** Địa chỉ email của người dùng. */
  private String email;

  /** Số điện thoại của người dùng. */
  private String phoneNumber;

  /** Giới hạn số lượng tài liệu có thể mượn. */
  private int borrowLimit;

  /** Danh sách tài liệu người dùng đang mượn. */
  private List<Document> borrowedDocuments;

  /** Biến đếm để tự động tạo ID cho người dùng. */
  private static int userCounter = 1;

  /**
   * Khởi tạo {@code User} mới với thông tin cơ bản.
   *
   * <p>Giới hạn mượn mặc định là 10 tài liệu.
   *
   * @param name tên người dùng
   * @param email địa chỉ email
   * @param phoneNumber số điện thoại
   */
  public User(String name, String email, String phoneNumber) {
    this.id = "USER" + String.format("%04d", userCounter++);
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.borrowLimit = 10;
    this.borrowedDocuments = new ArrayList<>();
  }

  // ===== GETTERS & SETTERS =====

  /**
   * Lấy mã định danh người dùng.
   *
   * @return ID của người dùng
   */
  public String getId() {
    return id;
  }

  /**
   * Lấy tên người dùng.
   *
   * @return tên người dùng
   */
  public String getName() {
    return name;
  }

  /**
   * Cập nhật tên người dùng.
   *
   * @param name tên mới
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Lấy email người dùng.
   *
   * @return email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Cập nhật email người dùng.
   *
   * @param email email mới
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Lấy số điện thoại.
   *
   * @return số điện thoại
   */
  public String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * Cập nhật số điện thoại.
   *
   * @param phoneNumber số điện thoại mới
   */
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /**
   * Lấy giới hạn số lượng tài liệu được mượn.
   *
   * @return giới hạn mượn
   */
  public int getBorrowLimit() {
    return borrowLimit;
  }

  /**
   * Cập nhật giới hạn mượn tài liệu.
   *
   * @param borrowLimit giới hạn mới
   */
  public void setBorrowLimit(int borrowLimit) {
    this.borrowLimit = borrowLimit;
  }

  /**
   * Lấy danh sách tài liệu đang mượn.
   *
   * <p>Trả về bản sao để tránh thay đổi trực tiếp từ bên ngoài.
   *
   * @return danh sách tài liệu đã mượn
   */
  public List<Document> getBorrowedDocuments() {
    return new ArrayList<>(borrowedDocuments);
  }

  /**
   * Trả về chuỗi mô tả thông tin người dùng.
   *
   * @return thông tin định dạng chuỗi
   */
  @Override
  public String toString() {
    return String.format(
        "ID: %s | Tên: %s | Email: %s | SĐT: %s | Số tài liệu tối đa: %d",
        id, name, email, phoneNumber, borrowLimit);
  }
}
