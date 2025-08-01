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

    // Constructor - Add initialized member
    public User(String id, String name, String email, String phoneNumber, int borrowLimit){
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.borrowLimit = borrowLimit;
        this.borrowedDocuments = new ArrayList<>();
    }
    
    // ========== DOCUMENT BORROWING FUNCTIONS ==========
    
    public boolean borrowDocument(Document document) {
        // Check borrowing limit
        if (borrowedDocuments.size() >= borrowLimit) {
            return false;
        }
        
        // Check if document is available
        if (!document.isAvailable()) {
            return false;
        }
        
        // Check if already borrowed this document
        if (borrowedDocuments.contains(document)) {
            return false;
        }
        
        // Execute borrowing
        borrowedDocuments.add(document);
        document.setAvailable(false);
        return true;
    }
    
    // ========== DOCUMENT RETURN FUNCTIONS ==========
    
    public boolean returnDocument(Document document) {
        // Check if borrowed this document
        if (!borrowedDocuments.contains(document)) {
            return false;
        }
        
        // Execute return
        borrowedDocuments.remove(document);
        document.setAvailable(true);
        return true;
    }
    
    // ========== MEMBER INFORMATION MANAGEMENT FUNCTIONS ==========
    
    public void updatePersonalInfo(String name, String email, String phoneNumber) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
        if (email != null && !email.trim().isEmpty()) {
            this.email = email;
        }
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            this.phoneNumber = phoneNumber;
        }
    }
    
    // ========== UTILITY METHODS ==========
    
    public boolean canBorrowMore() {
        return borrowedDocuments.size() < borrowLimit;
    }
    
    public int getBorrowedCount() {
        return borrowedDocuments.size();
    }
    
    public boolean isValidUser() {
        return id != null && !id.trim().isEmpty() &&
               name != null && !name.trim().isEmpty() &&
               email != null && email.contains("@") &&
               phoneNumber != null && !phoneNumber.trim().isEmpty() &&
               borrowLimit > 0;
    }
    
    // ========== GETTERS AND SETTERS ==========
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public int getBorrowLimit() { return borrowLimit; }
    public void setBorrowLimit(int borrowLimit) { this.borrowLimit = borrowLimit; }
    
    public List<Document> getBorrowedDocuments() { return new ArrayList<>(borrowedDocuments); }
}
