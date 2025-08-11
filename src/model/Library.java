package model;

import dao.DocumentDAO;
import dao.UserDAO;
import dao.BorrowDAO;

import java.util.Scanner;

/**
 * Lớp {@code Library} quản lý các chức năng chính của hệ thống thư viện, bao gồm quản lý tài liệu,
 * người dùng, và xử lý các yêu cầu mượn/trả tài liệu.
 *
 * <p>Lớp này sử dụng các lớp DAO ({@link DocumentDAO}, {@link UserDAO}, {@link BorrowDAO}) để thao
 * tác dữ liệu trong cơ sở dữ liệu.
 */
public class Library {

  /** Đối tượng DAO quản lý các tài liệu trong thư viện. */
  private DocumentDAO documentDAO;

  /** Đối tượng DAO quản lý thông tin người dùng. */
  private UserDAO userDAO;

  /** Đối tượng DAO quản lý hoạt động mượn và trả tài liệu. */
  private BorrowDAO borrowDAO;

  /** Khởi tạo đối tượng {@code Library} với các DAO mặc định. */
  public Library() {
    this.documentDAO = new DocumentDAO();
    this.userDAO = new UserDAO();
    this.borrowDAO = new BorrowDAO();
  }

  /**
   * Thêm một tài liệu mới vào thư viện. Người dùng sẽ được yêu cầu nhập thông tin chi tiết qua
   * {@link Scanner}.
   *
   * @param scanner đối tượng {@link Scanner} để nhập dữ liệu từ bàn phím.
   */
  public void addDocument(Scanner scanner) {
    System.out.print("Enter title: ");
    String title = scanner.nextLine();
    System.out.print("Enter author: ");
    String author = scanner.nextLine();
    System.out.print("Enter publication year: ");
    int year = Integer.parseInt(scanner.nextLine());
    System.out.print("Enter type (BOOK/MAGAZINE): ");
    String type = scanner.nextLine().trim().toUpperCase();

    Document document = null;

    if (type.equals("BOOK")) {
      System.out.print("Enter number of pages: ");
      int pages = Integer.parseInt(scanner.nextLine());
      document = new Book(title, author, year, pages);
    } else if (type.equals("MAGAZINE")) {
      System.out.print("Enter issue number: ");
      int issue = Integer.parseInt(scanner.nextLine());
      document = new Magazine(title, author, year, issue);
    } else {
      System.out.println("Invalid document type.");
      return;
    }

    if (documentDAO.insertDocument(document)) {
      System.out.println("Document added successfully.");
    } else {
      System.out.println("Failed to add document.");
    }
  }

  /**
   * Xóa một tài liệu khỏi thư viện theo mã ID.
   *
   * @param scanner đối tượng {@link Scanner} để nhập ID từ bàn phím.
   */
  public void removeDocument(Scanner scanner) {
    System.out.print("Enter document ID to remove: ");
    String id = scanner.nextLine();
    if (documentDAO.deleteDocument(id)) {
      System.out.println("Document removed successfully.");
    } else {
      System.out.println("Failed to remove document.");
    }
  }

  /**
   * Cập nhật thông tin của một tài liệu. Người dùng nhập ID của tài liệu và thông tin mới.
   *
   * @param scanner đối tượng {@link Scanner} để nhập thông tin từ bàn phím.
   */
  public void updateDocument(Scanner scanner) {
    System.out.print("Enter ID of document to update: ");
    String id = scanner.nextLine();
    System.out.print("Enter new title: ");
    String title = scanner.nextLine();
    System.out.print("Enter new author: ");
    String author = scanner.nextLine();
    System.out.print("Enter new publication year: ");
    int year = Integer.parseInt(scanner.nextLine());
    System.out.print("Enter new type (BOOK/MAGAZINE): ");
    String type = scanner.nextLine().trim().toUpperCase();

    Document updatedDocument = null;

    if (type.equals("BOOK")) {
      System.out.print("Enter number of pages: ");
      int pages = Integer.parseInt(scanner.nextLine());
      updatedDocument = new Book(id, title, author, year, pages);
    } else if (type.equals("MAGAZINE")) {
      System.out.print("Enter issue number: ");
      int issue = Integer.parseInt(scanner.nextLine());
      updatedDocument = new Magazine(id, title, author, year, issue);
    } else {
      System.out.println("Invalid document type.");
      return;
    }

    if (documentDAO.updateDocument(updatedDocument)) {
      System.out.println("Document updated successfully.");
    } else {
      System.out.println("Failed to update document.");
    }
  }

  /**
   * Tìm kiếm một tài liệu theo mã ID và hiển thị thông tin.
   *
   * @param scanner đối tượng {@link Scanner} để nhập ID từ bàn phím.
   */
  public void findDocument(Scanner scanner) {
    System.out.print("Enter document ID to find: ");
    String id = scanner.nextLine();
    Document document = documentDAO.findDocumentById(id);
    if (document != null) {
      System.out.println(document);
    } else {
      System.out.println("Document not found.");
    }
  }

  /** Hiển thị thông tin của tất cả tài liệu trong thư viện. */
  public void displayAllDocuments() {
    documentDAO.getAllDocuments().forEach(System.out::println);
  }

  /**
   * Thêm một người dùng mới vào hệ thống.
   *
   * @param scanner đối tượng {@link Scanner} để nhập thông tin người dùng.
   */
  public void addUser(Scanner scanner) {
    System.out.print("Enter name: ");
    String name = scanner.nextLine();
    System.out.print("Enter email: ");
    String email = scanner.nextLine();
    System.out.print("Enter phoneNumber: ");
    String phoneNumber = scanner.nextLine();

    User user = new User(name, email, phoneNumber);
    if (userDAO.insertUser(user)) {
      System.out.println("User added successfully.");
    } else {
      System.out.println("Failed to add user.");
    }
  }

  /**
   * Hiển thị thông tin của một người dùng theo mã ID.
   *
   * @param scanner đối tượng {@link Scanner} để nhập ID từ bàn phím.
   */
  public void displayUserInfo(Scanner scanner) {
    System.out.print("Enter user ID to display: ");
    String id = scanner.nextLine();
    User user = userDAO.getUserById(id);
    if (user != null) {
      System.out.println(user);
    } else {
      System.out.println("User not found.");
    }
  }

  /** Hiển thị danh sách tất cả người dùng trong hệ thống. */
  public void displayAllUsers() {
    userDAO.getAllUsers().forEach(System.out::println);
  }

  /**
   * Xử lý yêu cầu mượn tài liệu của một người dùng.
   *
   * @param scanner đối tượng {@link Scanner} để nhập ID người dùng và ID tài liệu.
   */
  public void borrowDocument(Scanner scanner) {
    System.out.print("Enter user ID: ");
    String userId = scanner.nextLine();
    System.out.print("Enter document ID: ");
    String documentId = scanner.nextLine();

    if (borrowDAO.borrowDocument(userId, documentId)) {
      System.out.println("Document borrowed successfully.");
    } else {
      System.out.println("Failed to borrow document.");
    }
  }

  /**
   * Xử lý yêu cầu trả tài liệu của một người dùng.
   *
   * @param scanner đối tượng {@link Scanner} để nhập ID người dùng và ID tài liệu.
   */
  public void returnDocument(Scanner scanner) {
    System.out.print("Enter user ID: ");
    String userId = scanner.nextLine();
    System.out.print("Enter document ID: ");
    String documentId = scanner.nextLine();

    if (borrowDAO.returnDocument(userId, documentId)) {
      System.out.println("Document returned successfully.");
    } else {
      System.out.println("Failed to return document.");
    }
  }
}
