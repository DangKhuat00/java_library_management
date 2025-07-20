package service;

import model.User;
import model.Document;

public class BorrowManager {

  public static boolean borrow(User user, Document document) {

    if (!BorrowValidator.canBorrow(user, document)) return false;

    user.getBorrowedDocuments().add(document);

    document.setNumbers(document.getNumbers() - 1);
    if (document.getNumbers() == 0) {
      document.setAvailable(false);
    }

    System.out.println(" Mượn tài liệu thành công: " + document.getTitle());
    return true;
  }
}
