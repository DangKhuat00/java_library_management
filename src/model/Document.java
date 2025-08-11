package model;

/**
 * Lop truu tuong Document dai dien cho mot tai lieu trong thu vien. Moi tai lieu co cac thong tin
 * co ban nhu tieu de, tac gia, nam xuat ban, tinh trang su dung va loai tai lieu.
 */
public abstract class Document {
  /** Ma dinh danh duy nhat cua tai lieu */
  protected String id;

  /** Tieu de tai lieu */
  protected String title;

  /** Tac gia tai lieu */
  protected String author;

  /** Nam xuat ban */
  protected int publication_year;

  /** Trang thai kha dung cua tai lieu */
  protected boolean isAvailable;

  /** Loai tai lieu (BOOK, MAGAZINE, v.v.) */
  protected DocumentType documentType;

  /** Bien dem toan cuc de tao ID tu dong */
  private static int docCounter = 1;

  /**
   * Khoi tao tai lieu moi voi ID tu dong tao.
   *
   * @param title Tieu de tai lieu
   * @param author Tac gia
   * @param publication_year Nam xuat ban
   * @param documentType Loai tai lieu
   */
  public Document(String title, String author, int publication_year, DocumentType documentType) {
    this.id = "DOC" + String.format("%04d", docCounter++);
    this.title = title;
    this.author = author;
    this.publication_year = publication_year;
    this.documentType = documentType;
    this.isAvailable = false;
  }

  /**
   * Khoi tao tai lieu moi voi ID cu the.
   *
   * @param id Ma dinh danh cua tai lieu
   * @param title Tieu de tai lieu
   * @param author Tac gia
   * @param publication_year Nam xuat ban
   * @param documentType Loai tai lieu
   */
  public Document(
      String id, String title, String author, int publication_year, DocumentType documentType) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.publication_year = publication_year;
    this.documentType = documentType;
    this.isAvailable = false;
  }

  /**
   * Hien thi thong tin chi tiet cua tai lieu. Phuong thuc nay duoc trien khai cu the o cac lop con.
   */
  public abstract void displayInfo();

  /**
   * @return Ma dinh danh tai lieu
   */
  public String getId() {
    return id;
  }

  /**
   * @return Tieu de tai lieu
   */
  public String getTitle() {
    return title;
  }

  /**
   * @return Tac gia tai lieu
   */
  public String getAuthor() {
    return author;
  }

  /**
   * @return Nam xuat ban
   */
  public int getYear() {
    return publication_year;
  }

  /**
   * @return true neu tai lieu dang kha dung, nguoc lai false
   */
  public boolean isAvailable() {
    return isAvailable;
  }

  /**
   * @return Loai tai lieu
   */
  public DocumentType getDocumentType() {
    return documentType;
  }

  /**
   * Dat tieu de moi cho tai lieu.
   *
   * @param title Tieu de moi
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Dat tac gia moi cho tai lieu.
   *
   * @param author Tac gia moi
   */
  public void setAuthor(String author) {
    this.author = author;
  }

  /**
   * Dat nam xuat ban moi cho tai lieu.
   *
   * @param publication_year Nam xuat ban moi
   */
  public void setYear(int publication_year) {
    this.publication_year = publication_year;
  }

  /**
   * Dat trang thai kha dung cho tai lieu.
   *
   * @param available true neu tai lieu kha dung, false neu khong
   */
  public void setAvailable(boolean available) {
    this.isAvailable = available;
  }

  /**
   * Dat loai tai lieu moi.
   *
   * @param documentType Loai tai lieu moi
   */
  public void setDocumentType(DocumentType documentType) {
    this.documentType = documentType;
  }

  /**
   * Tra ve chuoi mo ta ngan gon cua tai lieu.
   *
   * @return Chuoi mo ta co dang: [Loai] Tieu de by Tac gia
   */
  @Override
  public String toString() {
    return "[" + documentType + "] " + title + " by " + author;
  }
}
