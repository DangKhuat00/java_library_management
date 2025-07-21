package model;

public class Document {
    private String id;
    private String title;
    private String author;
    private String publisher;
    private int year;
    private String category;
    private boolean isAvailable;
    
    // Constructor
    public Document(String id, String title, String author, String publisher, int year, String category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.category = category;
        this.isAvailable = true; // Mặc định là có sẵn
    }
    
    // ========== CORE METHODS ==========
    
    public void setAvailable(boolean available) { 
        this.isAvailable = available; 
    }
    
    public boolean isValidDocument() {
        return id != null && !id.trim().isEmpty() &&
               title != null && !title.trim().isEmpty() &&
               author != null && !author.trim().isEmpty() &&
               publisher != null && !publisher.trim().isEmpty() &&
               year > 0 && year <= java.time.Year.now().getValue() &&
               category != null && !category.trim().isEmpty();
    }
    
    // ========== GETTERS ==========
    
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public int getYear() { return year; }
    public String getCategory() { return category; }
    public boolean isAvailable() { return isAvailable; }
    
    // ========== OVERRIDE METHODS ==========
    
    @Override
    public String toString() {
        return title + " - " + author + " (" + year + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Document document = (Document) obj;
        return id.equals(document.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
