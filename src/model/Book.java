package model;

public class Book extends Document {
    private int numberOfPages;

    public Book(String id, String title, String author, int publication_year, int numberOfPages) {
        super(id, title, author, publication_year, DocumentType.BOOK);
        this.numberOfPages = numberOfPages;
    }

    @Override
    public void displayInfo() {
        System.out.println("===== BOOK INFORMATION =====");
        System.out.println("ID: " + id);
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Publication_year: " + publication_year);
        System.out.println("Pages: " + numberOfPages);
        System.out.println("Type: " + documentType);
        System.out.println("Available: " + (isAvailable ? "Yes" : "No"));
        System.out.println("=============================");
    }
    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }
}
