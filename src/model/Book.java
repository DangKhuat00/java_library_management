package model;

public class Book extends Document {
    
    public Book(String id, String title, String author, String publisher, String category, int year, int numbers, boolean isAvailable) {
        super(id, title, author, publisher, category, year, numbers, isAvailable);
    }
    
    // thêm constructor mới cho chỗ gọi new Book(id, title, author, year, "BOOK")
    public Book(String id, String title, String author, int year, String category) {
        super(id, title, author, "", category, year, 1, true);
    }

    @Override
    public void displayInfo() {
        System.out.println("===== BOOK INFORMATION =====");
        System.out.println("ID: " + id);
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Publisher: " + publisher);
        System.out.println("Category: " + category);
        System.out.println("Year: " + year);
        System.out.println("Quantity: " + numbers);
        System.out.println("Available: " + (isAvailable ? "Yes" : "No"));
        System.out.println("=============================");
    }
}