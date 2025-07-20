package service;

public class DocumentValidator {
  public static boolean isValid(Document doc) {
    if (doc == null) {
      System.out.println(" Tài liệu không được null.");
      return false;
    }

    if (doc.getId() == null || doc.getId().isEmpty()) {
      System.out.println(" ID tài liệu không được để trống.");
      return false;
    }

    if (doc.getTitle() == null || doc.getTitle().isEmpty()) {
      System.out.println(" Tiêu đề tài liệu không được để trống.");
      return false;
    }

    if (doc.getAuthor() == null || doc.getAuthor().isEmpty()) {
      System.out.println(" Tác giả không được để trống.");
      return false;
    }

    if (doc.getCategory() == null || doc.getCategory().isEmpty()) {
      System.out.println(" Thể loại không được để trống.");
      return false;
    }

    if (doc.getYear() < 0) {
      System.out.println(" Năm xuất bản không hợp lệ.");
      return false;
    }

    if (doc.getNumbers() < 0) {
      System.out.println(" Số lượng không được âm.");
      return false;
    }

    return true;
  }

  public static boolean isDuplicateId(Library library, String id) {
    if (library == null || id == null) return false;

    return library.getDocuments().stream().anyMatch(doc -> doc.getId().equals(id));
  }
}
