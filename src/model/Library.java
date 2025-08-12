package model;

import dao.BorrowDAO;
import dao.BorrowDAO.BorrowRecord;
import dao.DocumentDAO;
import dao.UserDAO;

import javax.swing.JOptionPane;
import java.util.List;

/**
 * Lớp trung tâm điều phối logic nghiệp vụ của thư viện.
 * Nó hoạt động như một cầu nối giữa Giao diện người dùng (GUI) và các lớp Truy
 * cập Dữ liệu (DAO).
 */
public class Library {

    private final DocumentDAO documentDAO;
    private final UserDAO userDAO;
    private final BorrowDAO borrowDAO;

    public Library() {
        this.documentDAO = new DocumentDAO();
        this.userDAO = new UserDAO();
        this.borrowDAO = new BorrowDAO();
    }

    // ========== DOCUMENT METHODS ==========

    public boolean addDocument(Document document) {
        return documentDAO.insertDocument(document);
    }

    public boolean removeDocument(String documentId) {
        try {
            int id = Integer.parseInt(documentId);
            return documentDAO.deleteDocument(id);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing document ID for deletion: " + e.getMessage());
            return false;
        }
    }

    public boolean updateDocument(Document document) {
        return documentDAO.updateDocument(document);
    }

    public List<Document> getAllDocuments() {
        return documentDAO.getAllDocuments();
    }

    public Document getDocumentById(int id) {
        // Tối ưu hơn là tạo hàm getById trong DAO, nhưng cách này vẫn hoạt động
        return documentDAO.getAllDocuments().stream()
                .filter(doc -> doc.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Document> findDocuments(String keyword) {
        return documentDAO.findDocument(keyword);
    }

    public List<Document> findDocumentsByField(String filter, String keyword) {
        String dbField;
        switch (filter) {
            case "Title":
                dbField = "title";
                break;
            case "Author":
                dbField = "author";
                break;
            case "Language":
                dbField = "language";
                break;
            case "Year":
                dbField = "publication_year"; // Tên cột trong DB
                break;
            default: // "All Fields"
                return findDocuments(keyword);
        }
        return documentDAO.findDocumentsByField(dbField, keyword);
    }

    // ========== USER METHODS ==========

    public boolean addUser(User user) {
        return userDAO.insertUser(user);
    }

    public boolean deleteUser(int id) {
        return userDAO.deleteUser(id);
    }

    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }

    public List<User> findUsers(String keyword, String filter) {
        return userDAO.searchUsers(keyword, filter);
    }

    // ========== BORROW/RETURN METHODS (LOGIC HOÀN CHỈNH) ==========

    /**
     * Xử lý logic mượn một tài liệu.
     * Cập nhật trạng thái của tài liệu và số sách đã mượn của người dùng.
     * 
     * @param userIdStr     ID của người dùng
     * @param documentIdStr ID của tài liệu
     * @return true nếu mượn thành công, false nếu thất bại.
     */
    public boolean borrowDocument(String userIdStr, String documentIdStr) {
        try {
            int userId = Integer.parseInt(userIdStr);
            int documentId = Integer.parseInt(documentIdStr);

            User user = getUserById(userId);
            Document doc = getDocumentById(documentId);

            // --- 1. Kiểm tra các điều kiện tiên quyết ---
            if (user == null) {
                JOptionPane.showMessageDialog(null, "User with ID " + userId + " not found.", "Borrow Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (doc == null) {
                JOptionPane.showMessageDialog(null, "Document with ID " + documentId + " not found.", "Borrow Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (!doc.isAvailable()) {
                JOptionPane.showMessageDialog(null, "This document is currently unavailable.", "Borrow Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (user.getBorrowedBooksCount() >= user.getBorrowLimit()) {
                JOptionPane.showMessageDialog(null,
                        "User has reached the borrow limit of " + user.getBorrowLimit() + " items.", "Borrow Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // --- 2. Thực hiện các hành động ---
            if (borrowDAO.borrowDocument(userIdStr, documentIdStr)) {
                // Cập nhật trạng thái sách thành "đã mượn"
                doc.setAvailable(false);
                documentDAO.updateDocument(doc);

                // Tăng số lượng sách đã mượn của người dùng
                user.setBorrowedBooksCount(user.getBorrowedBooksCount() + 1);
                userDAO.updateUser(user);

                return true; // Mượn thành công
            }
            return false; // Lỗi từ borrowDAO
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid User ID or Document ID format.", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Xử lý logic trả một tài liệu.
     * Cập nhật trạng thái của tài liệu và số sách đã mượn của người dùng.
     * 
     * @param userIdStr     ID của người dùng
     * @param documentIdStr ID của tài liệu
     * @return true nếu trả thành công, false nếu thất bại.
     */
    public boolean returnDocument(String userIdStr, String documentIdStr) {
        try {
            int userId = Integer.parseInt(userIdStr);
            int documentId = Integer.parseInt(documentIdStr);

            User user = getUserById(userId);
            Document doc = getDocumentById(documentId);

            // --- 1. Kiểm tra điều kiện ---
            if (user == null || doc == null) {
                JOptionPane.showMessageDialog(null, "User or Document not found.", "Return Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // --- 2. Thực hiện hành động trả sách ---
            if (borrowDAO.returnDocument(userIdStr, documentIdStr)) {
                // Cập nhật trạng thái sách thành "có sẵn"
                doc.setAvailable(true);
                documentDAO.updateDocument(doc);

                // Giảm số lượng sách đã mượn của người dùng (đảm bảo không âm)
                int currentBorrowedCount = user.getBorrowedBooksCount();
                if (currentBorrowedCount > 0) {
                    user.setBorrowedBooksCount(currentBorrowedCount - 1);
                    userDAO.updateUser(user);
                }

                return true; // Trả thành công
            } else {
                // Lỗi này thường xảy ra khi bản ghi mượn không tồn tại hoặc đã được trả.
                JOptionPane.showMessageDialog(null,
                        "Failed to return. The borrow record might not exist or was already updated.", "Return Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid User ID or Document ID format.", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowDAO.getAllBorrowRecords();
    }
}