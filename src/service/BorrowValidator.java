package service;

import model.User;
import model.Document;
import model.Library;

public class BorrowValidator {
  /** Tìm người dùng trong thư viện theo ID */
  private static User findUserById(Library library, String id) {
    for (User user : library.getUsers()) {
      if (user.getId().equalsIgnoreCase(id)) {
        return user;
      }
    }
    return null;
  }

  /** Tìm tài liệu trong thư viện theo ID */
  private static Document findDocumentById(Library library, String id) {
    for (Document doc : library.getDocuments()) {
      if (doc.getId().equalsIgnoreCase(id)) {
        return doc;
      }
    }
    return null;
  }

  /**
   * Kiểm tra xem người dùng có thể mượn tài liệu không, dựa trên ID. Gồm cả kiểm tra ID có tồn tại
   * không và điều kiện mượn.
   */
  public static boolean canBorrow(Library library, String userId, String docId) {
    if (library == null) {
      System.out.println(" Hệ thống thư viện chưa được khởi tạo.");
      return false;
    }

    User user = findUserById(library, userId);
    if (user == null) {
      System.out.println(" Không tìm thấy người dùng với ID: " + userId);
      return false;
    }

    Document doc = findDocumentById(library, docId);
    if (doc == null) {
      System.out.println(" Không tìm thấy tài liệu với ID: " + docId);
      return false;
    }

    return canBorrow(user, doc); // Nếu tìm thấy, gọi tiếp kiểm tra nghiệp vụ
  }

  /** Kiểm tra các điều kiện nghiệp vụ khi mượn (gọi nội bộ) */
  public static boolean canBorrow(User user, Document document) {
    if (!document.isAvailable()) {
      System.out.println(" Tài liệu không khả dụng (hết sách hoặc đang bị khoá).");
      return false;
    }

    if (user.getBorrowedDocuments().contains(document)) {
      System.out.println(" Bạn đã mượn tài liệu này rồi.");
      return false;
    }

    if (user.getBorrowedDocuments().size() >= user.getBorrowLimit()) {
      System.out.println(" Vượt quá giới hạn mượn: " + user.getBorrowLimit() + " tài liệu.");
      return false;
    }

    return true;
  }
}
