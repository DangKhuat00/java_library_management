package service;

import model.Document;
import model.User;

public class BorrowValidator {

  public static String validate(User user, Document document) {
    if (user == null) {
      return "Người dùng không tồn tại.";
    }

    if (document == null) {
      return "Tài liệu không tồn tại.";
    }

    if (!document.isAvailable()) {
      return "Tài liệu không còn sẵn để mượn.";
    }

    if (user.getBorrowedDocuments().contains(document)) {
      return "Bạn đã mượn tài liệu này rồi.";
    }

    if (user.getBorrowedDocuments().size() >= user.getBorrowLimit()) {
      return "Bạn đã đạt giới hạn mượn tài liệu (" + user.getBorrowLimit() + ")";
    }

    return null; // không có lỗi
  }
}
