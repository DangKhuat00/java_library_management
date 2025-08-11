package model;

/**
 * Lớp {@code Magazine} đại diện cho tạp chí trong hệ thống thư viện.
 *
 * <p>Kế thừa từ {@link Document} và bổ sung thuộc tính {@code issueNumber} để lưu số phát hành.
 */
public class Magazine extends Document {

  /** Số phát hành của tạp chí. */
  private int issueNumber;

  /**
   * Khởi tạo {@code Magazine} mới với thông tin cơ bản.
   *
   * @param title tiêu đề tạp chí
   * @param author tác giả hoặc nhà xuất bản
   * @param publication_year năm phát hành
   * @param issueNumber số phát hành
   */
  public Magazine(String title, String author, int publication_year, int issueNumber) {
    super(title, author, publication_year, DocumentType.MAGAZINE);
    this.issueNumber = issueNumber;
  }

  /**
   * Khởi tạo {@code Magazine} mới với mã ID và thông tin chi tiết.
   *
   * @param id mã định danh duy nhất của tạp chí
   * @param title tiêu đề tạp chí
   * @param author tác giả hoặc nhà xuất bản
   * @param publication_year năm phát hành
   * @param issueNumber số phát hành
   */
  public Magazine(String id, String title, String author, int publication_year, int issueNumber) {
    super(id, title, author, publication_year, DocumentType.MAGAZINE);
    this.issueNumber = issueNumber;
  }

  /**
   * Hiển thị thông tin chi tiết của tạp chí ra console.
   *
   * <p>Gồm các thông tin: ID, tiêu đề, tác giả, năm xuất bản, số phát hành, loại tài liệu, trạng
   * thái khả dụng.
   */
  @Override
  public void displayInfo() {
    System.out.println("===== MAGAZINE INFORMATION =====");
    System.out.println("ID: " + id);
    System.out.println("Title: " + title);
    System.out.println("Author: " + author);
    System.out.println("Publication_year: " + publication_year);
    System.out.println("Issue No.: " + issueNumber);
    System.out.println("Type: " + documentType);
    System.out.println("Available: " + (isAvailable ? "Yes" : "No"));
    System.out.println("=================================");
  }

  /**
   * Lấy số phát hành của tạp chí.
   *
   * @return số phát hành
   */
  public int getIssueNumber() {
    return issueNumber;
  }

  /**
   * Cập nhật số phát hành của tạp chí.
   *
   * @param issueNumber số phát hành mới
   */
  public void setIssueNumber(int issueNumber) {
    this.issueNumber = issueNumber;
  }
}
