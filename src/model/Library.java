package model;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Document> documents;
    private List<User> users;
    
    public Library() {
        this.documents = new ArrayList<>();
        this.users = new ArrayList<>();
    }
    /**
     * Mượn tài liệu - kiểm tra điều kiện trước khi cho mượn
     * @param userId ID của người dùng
     * @param documentId ID của tài liệu
     * @return true nếu mượn thành công, false nếu thất bại
     */
    public boolean borrowDocument(String userId, String documentId) {
        User user = findUserById(userId);
        Document document = findDocumentById(documentId);
        
        // Kiểm tra user và document tồn tại
        if (user == null || document == null) {
            return false;
        }
        
        // Kiểm tra tài liệu có sẵn không
        if (!document.isAvailable()) {
            return false;
        }
        
        // Kiểm tra user có thể mượn thêm không
        if (!user.canBorrowMore()) {
            return false;
        }
        
        // Thực hiện mượn
        return user.borrowDocument(document);
    }
    /**
     * Trả tài liệu - cập nhật trạng thái sau khi trả
     * @param userId ID của người dùng
     * @param documentId ID của tài liệu
     * @return true nếu trả thành công, false nếu thất bại
     */
    public boolean returnDocument(String userId, String documentId) {
        User user = findUserById(userId);
        Document document = findDocumentById(documentId);
        
        // Kiểm tra user và document tồn tại
        if (user == null || document == null) {
            return false;
        }
        
        // Thực hiện trả tài liệu
        return user.returnDocument(document);
    }
    
    /**
     * Thêm người dùng mới
     */
    public boolean addUser(String name, String email, String phoneNumber, int borrowLimit) {
        // Kiểm tra email đã tồn tại
        if (findUserByEmail(email) != null) {
            return false;
        }
        
        String userId = generateUserId();
        User newUser = new User(userId, name, email, phoneNumber, borrowLimit);
        
        if (newUser.isValidUser()) {
            users.add(newUser);
            return true;
        }
        return false;
    }
    
    /**
     * Cập nhật thông tin cá nhân của thành viên
     */
    public boolean updateUserInfo(String userId, String name, String email, String phoneNumber) {
        User user = findUserById(userId);
        if (user == null) {
            return false;
        }
        
        // Kiểm tra email mới có trùng với user khác không
        if (!user.getEmail().equals(email) && findUserByEmail(email) != null) {
            return false;
        }
        
        user.updatePersonalInfo(name, email, phoneNumber);
        return true;
    }
    
    /**
     * Lấy thông tin trạng thái mượn tài liệu của thành viên
     */
    public User getUserBorrowingStatus(String userId) {
        return findUserById(userId);
    }
    
    /**
     * Hiển thị thông tin chi tiết của user (chức năng Display User Info)
     */
    public String displayUserInfo(String userId) {
        User user = findUserById(userId);
        if (user == null) {
            return "Không tìm thấy người dùng với ID: " + userId;
        }
        
        StringBuilder info = new StringBuilder();
        info.append("===== THÔNG TIN THÀNH VIÊN =====\n");
        info.append("ID: ").append(user.getId()).append("\n");
        info.append("Tên: ").append(user.getName()).append("\n");
        info.append("Email: ").append(user.getEmail()).append("\n");
        info.append("Điện thoại: ").append(user.getPhoneNumber()).append("\n");
        info.append("Giới hạn mượn: ").append(user.getBorrowLimit()).append("\n");
        info.append("Đã mượn: ").append(user.getBorrowedCount()).append(" tài liệu\n");
        info.append("Có thể mượn thêm: ").append(user.getBorrowLimit() - user.getBorrowedCount()).append(" tài liệu\n");
        
        if (user.getBorrowedCount() > 0) {
            info.append("\nTài liệu đang mượn:\n");
            int count = 1;
            for (Document doc : user.getBorrowedDocuments()) {
                info.append(count++).append(". ").append(doc.toString()).append(" (ID: ").append(doc.getId()).append(")\n");
            }
        } else {
            info.append("\nChưa mượn tài liệu nào.\n");
        }
        
        return info.toString();
    }
    
    // ========== HELPER METHODS ==========
    
    /**
     * Tìm user theo ID
     */
    private User findUserById(String userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Tìm user theo email
     */
    private User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Tạo ID người dùng tự động
     */
    private String generateUserId() {
        return "USER" + String.format("%03d", users.size() + 1);
    }
    
    /**
     * Tìm tài liệu theo ID
     */
    private Document findDocumentById(String documentId) {
        for (Document doc : documents) {
            if (doc.getId().equals(documentId)) {
                return doc;
            }
        }
        return null;
    }
    
    /**
     * Thêm tài liệu vào thư viện
     */
    public void addDocument(Document document) {
        documents.add(document);
    }
    
}