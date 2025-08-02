package model;

public class Magazine extends Document {
    
    public Magazine(String id, String title, String author, String publisher, String category, int year, int numbers, boolean isAvailable) {
        super(id, title, author, publisher, category, year, numbers, isAvailable);
    }

    // constructor rút gọn cho chỗ gọi new Magazine(id, title, author, year, "MAGAZINE")
    public Magazine(String id, String title, String author, int year, String category) {
        super(id, title, author, "", category, year, 1, true);
    }
    
    @Override
    public void displayInfo() {
        System.out.println("===== MAGAZINE INFORMATION =====");
        System.out.println("ID: " + id);
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Publisher: " + publisher);
        System.out.println("Category: " + category);
        System.out.println("Year: " + year);
        System.out.println("Quantity: " + numbers);
        System.out.println("Available: " + (isAvailable ? "Yes" : "No"));
        System.out.println("=================================");
    }
}