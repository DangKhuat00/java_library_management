package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private int borrowLimit;
    private List<Document> borrowedDocuments;

    public User(int id, String name, String email, String phoneNumber) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.borrowLimit = borrowLimit;
    this.borrowedDocuments = new ArrayList<>();
}

    // Constructor to match parameters used in UserDAO
    public User(String name, String email, String phoneNumber) {
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.borrowLimit = 10;
    this.borrowedDocuments = new ArrayList<>();
}
    
    // ========== GETTERS AND SETTERS ==========
    
    public int getId() { return id; }
    
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

