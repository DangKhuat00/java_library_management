package model;

/**
 * Lop Book mo ta thong tin ve mot quyen sach. Ke thua tu {@link Document} va bo sung thuoc tinh so
 * trang.
 */
public class Book extends Document {
  /** So trang cua sach */
  private int numberOfPages;

  /**
   * Khoi tao mot quyen sach moi (khong co ID, ID se duoc tu dong tao).
   *
   * @param title Tieu de sach
   * @param author Tac gia
   * @param publication_year Nam xuat ban
   * @param numberOfPages So trang cua sach
   */
  public Book(String title, String author, int publication_year, int numberOfPages) {
    super(title, author, publication_year, DocumentType.BOOK);
    this.numberOfPages = numberOfPages;
  }

  /**
   * Khoi tao mot quyen sach voi ID cu the.
   *
   * @param id Ma dinh danh cua sach
   * @param title Tieu de sach
   * @param author Tac gia
   * @param publication_year Nam xuat ban
   * @param numberOfPages So trang cua sach
   */
  public Book(String id, String title, String author, int publication_year, int numberOfPages) {
    super(id, title, author, publication_year, DocumentType.BOOK);
    this.numberOfPages = numberOfPages;
  }

  /** {@inheritDoc} */
  @Override
  public void displayInfo() {
    System.out.println("===== BOOK INFORMATION =====");
    System.out.println("ID: " + id);
    System.out.println("Title: " + title);
    System.out.println("Author: " + author);
    System.out.println("Publication_year: " + publication_year);
    System.out.println("Pages: " + numberOfPages);
    System.out.println("Type: " + documentType);
    System.out.println("Available: " + (isAvailable ? "Yes" : "No"));
    System.out.println("=============================");
  }

  /**
   * Lay so trang cua sach.
   *
   * @return So trang
   */
  public int getNumberOfPages() {
    return numberOfPages;
  }

  /**
   * Dat so trang cua sach.
   *
   * @param numberOfPages So trang moi
   */
  public void setNumberOfPages(int numberOfPages) {
    this.numberOfPages = numberOfPages;
  }
}
