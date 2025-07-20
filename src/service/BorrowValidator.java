package service;

import model.Document;
import model.User;

public class BorrowValidator {

  public static boolean validate(User user, Document document) {
    if (user == null) {
      System.out.println("Lỗi: User không tồn tại");
      return false;
    }

    if (document == null) {
      System.out.println("Lỗi: document không tồn tại");
      return false;
    }

    if (!document.isAvailable()) {
      System.out.println("Tài liệu hiện không khả dụng.");
      return false;
    }

    if (user.getBorrowedDocuments().contains(document)) {
      System.out.println("Bạn đã mượn tài liệu này rồi.");
      return false;
    }

    if (user.getBorrowedDocuments().size() >= user.getBorrowLimit()) {
      System.out.println("Bạn đã đạt giới hạn mượn: " + user.getBorrowLimit());
      return false;
    }

    return true;
  }
}
