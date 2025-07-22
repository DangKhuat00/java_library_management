package model;

public class Document {
    private String id;
    private String title;
    private String author;
    private String publisher;
    private String category;
    private int year;
    private int numbers;
    private boolean isAvailable;
    
    public Document(String id, String title, String author, String publisher, String category, int year, int numbers, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
        this.year = year;
        this.numbers = numbers;
        this.isAvailable = isAvailable;
    }

    public boolean isAvailable(){
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    
    // Getter cho ID
    public String getId() {
        return id;
    }
    
    // Getter cho title
    public String getTitle() {
        return title;
    }
    
    // Getter cho author
    public String getAuthor() {
        return author;
    }
}
