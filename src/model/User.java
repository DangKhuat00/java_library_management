package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private int borrowLimit;
    private List<Document> borrowedDocuments;

    // Constructor - Thêm thành viên đã khởi tạo
    public User(String id, String name, String email, String phoneNumber, int borrowLimit){
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.borrowLimit = borrowLimit;
        this.borrowedDocuments = new ArrayList<>();
    }
    
    // ========== CHỨC NĂNG MƯỢN TÀI LIỆU ==========
    
    public boolean borrowDocument(Document document) {
        // Kiểm tra giới hạn mượn
        if (borrowedDocuments.size() >= borrowLimit) {
            return false;
        }
        
        // Kiểm tra tài liệu có sẵn
        if (!document.isAvailable()) {
            return false;
        }
        
        // Kiểm tra đã mượn tài liệu này chưa
        if (borrowedDocuments.contains(document)) {
            return false;
        }
        
        // Thực hiện mượn
        borrowedDocuments.add(document);
        document.setAvailable(false);
        return true;
    }
    
    // ========== CHỨC NĂNG TRẢ TÀI LIỆU ==========
    
    public boolean returnDocument(Document document) {
        // Kiểm tra có mượn tài liệu này không
        if (!borrowedDocuments.contains(document)) {
            return false;
        }
        
        // Thực hiện trả
        borrowedDocuments.remove(document);
        document.setAvailable(true);
        return true;
    }
    
    public boolean canBorrowMore() {
        return borrowedDocuments.size() < borrowLimit;
    }
    
    public int getBorrowedCount() {
        return borrowedDocuments.size();
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public int getBorrowLimit() { return borrowLimit; }
    public List<Document> getBorrowedDocuments() { return new ArrayList<>(borrowedDocuments); }
}
