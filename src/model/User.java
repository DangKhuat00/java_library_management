package model;

public class User {
    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private int borrowLimit;
    private int borrowedBooksCount;

    public User(int id, String name, String email, String phoneNumber, int borrowedBooksCount) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        borrowLimit = 10;
        this.borrowedBooksCount = borrowedBooksCount;
    }

    // Constructor to match parameters used in UserDAO
    public User(String name, String email, String phoneNumber, int borrowedBooksCount) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.borrowLimit = 10;
        this.borrowedBooksCount = borrowedBooksCount;
    }

    public User(String name, String email, String phoneNumber, int borrowLimit, int borrowedBooksCount) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.borrowLimit = borrowLimit;
        this.borrowedBooksCount = borrowedBooksCount;
    }

    public User(int id, String name, String email, String phoneNumber, int borrowLimit, int borrowedBooksCount) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.borrowLimit = borrowLimit;
        this.borrowedBooksCount = borrowedBooksCount;
    }

    // ========== GETTERS AND SETTERS ==========

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getBorrowLimit() {
        return borrowLimit;
    }

    public void setBorrowLimit(int borrowLimit) {
        this.borrowLimit = borrowLimit;
    }

    public void setBorrowedBooksCount(int borrowedBooksCount) {
        this.borrowedBooksCount = borrowedBooksCount;
    }

    public int getBorrowedBooksCount() {
        return borrowedBooksCount;
    }
}
