package model;

public class Book extends Document {
    
    public Book(String id, String title, String author, String publisher, String category, int year, int numbers, boolean isAvailable) {
        super(id, title, author, publisher, category, year, numbers, isAvailable);
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
