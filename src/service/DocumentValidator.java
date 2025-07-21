package service;

import model.Document;
import java.util.List;


public class DocumentValidator {
  public static boolean isValid(Document doc, List<Document> existingDocs) {
    if (doc == null) {
      System.out.println(" Tài liệu không được null.");
      return false;
    }

    if (!isNonEmpty(doc.getId())) {
      System.out.println(" ID tài liệu không được để trống.");
      return false;
    }

    if (isDuplicateId(doc.getId(), existingDocs)) {
      System.out.println(" ID tài liệu đã tồn tại.");
      return false;
    }

    if (!isNonEmpty(doc.getTitle())) {
      System.out.println(" Tiêu đề không được để trống.");
      return false;
    }

    if (!isNonEmpty(doc.getAuthor())) {
      System.out.println(" Tác giả không được để trống.");
      return false;
    }

    if (!isNonEmpty(doc.getCategory())) {
      System.out.println(" Thể loại không được để trống.");
      return false;
    }

    if (doc.getYear() <= 0) {
      System.out.println(" Năm xuất bản phải lớn hơn 0.");
      return false;
    }

    if (doc.getNumbers() < 0) {
      System.out.println(" Số lượng tài liệu không được âm.");
      return false;
    }

    return true;
  }

  /**
   * Kiểm tra ID đã tồn tại chưa trong danh sách tài liệu.
   */
  public static boolean isDuplicateId(String id, List<Document> docs) {
    for (Document d : docs) {
      if (d.getId().equalsIgnoreCase(id)) {
        return true;
      }
    }
    return false;
  }
  private static boolean isNonEmpty(String s) {
    return s != null && !s.trim().isEmpty();
  }
}
