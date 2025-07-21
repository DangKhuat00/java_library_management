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
    
    // ========== CHỨC NĂNG 1: MƯỢN TÀI LIỆU ==========
    
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
    
    // ========== CHỨC NĂNG 2: TRẢ TÀI LIỆU ==========
    
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
    
    // ========== CHỨC NĂNG 3: QUẢN LÝ THÔNG TIN THÀNH VIÊN ==========
    
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
    
    // ========== UTILITY METHODS ==========
    
    private User findUserById(String userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
    
    private User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }
    
    private Document findDocumentById(String documentId) {
        for (Document document : documents) {
            if (document.getId().equals(documentId)) {
                return document;
            }
        }
        return null;
    }
    
    private String generateUserId() {
        return "U" + String.format("%03d", users.size() + 1);
    }
    
    // ========== BASIC GETTERS ==========
    
    public List<User> getUsers() {
        return new ArrayList<>(users);
    }
    
    public List<Document> getDocuments() {
        return new ArrayList<>(documents);
    }
    
    // Phương thức để thêm tài liệu (cần thiết cho hệ thống hoạt động)
    public boolean addDocument(String title, String author, String publisher, int year, String category) {
        String docId = "D" + String.format("%03d", documents.size() + 1);
        Document newDoc = new Document(docId, title, author, publisher, year, category);
        
        if (newDoc.isValidDocument()) {
            documents.add(newDoc);
            return true;
        }
        return false;
    }
}