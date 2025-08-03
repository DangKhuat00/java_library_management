package Ui;

import model.Document;
import model.Library;

import java.util.List;
import java.util.Scanner;

public class LibraryUi {
  private Library library;
  private Scanner scanner;

  public LibraryUi() {
    library = new Library();
    scanner = new Scanner(System.in);
  }

  public void start() {
    while (true) {
      System.out.println("\n===== LIBRARY MENU =====");
      System.out.println("1. Thêm tài liệu");
      System.out.println("2. Hiển thị tất cả tài liệu");
      System.out.println("3. Xóa tài liệu theo ID");
      System.out.println("4. Thoát");
      System.out.print("Chọn: ");
      String choice = scanner.nextLine();

      switch (choice) {
        case "1":
          addDocument();
          break;
        case "2":
          listDocuments();
          break;
        case "3":
          deleteDocument();
          break;
        case "4":
          System.out.println("Thoát chương trình...");
          return;
        default:
          System.out.println("Lựa chọn không hợp lệ.");
      }
    }
  }

  private void addDocument() {
    System.out.print("Nhập tiêu đề: ");
    String title = scanner.nextLine();
    System.out.print("Nhập tác giả: ");
    String author = scanner.nextLine();
    System.out.print("Nhập năm xuất bản: ");
    int year = Integer.parseInt(scanner.nextLine());

    Document doc = new Document();
    library.addDocument(doc);
    System.out.println("Đã thêm tài liệu.");
  }

  private void listDocuments() {
    List<Document> documents = library.getAllDocuments();
    if (documents.isEmpty()) {
      System.out.println("Không có tài liệu.");
    } else {
      System.out.println("Danh sách tài liệu:");
      for (Document doc : documents) {
        System.out.println(doc);
      }
    }
  }

  private void deleteDocument() {
    System.out.print("Nhập ID tài liệu cần xóa: ");
    int id = Integer.parseInt(scanner.nextLine());
    library.deleteDocument(id);
    System.out.println("Đã xóa tài liệu (nếu tồn tại).");
  }
}
