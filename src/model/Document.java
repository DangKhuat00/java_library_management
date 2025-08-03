package model;

public abstract class Document {
    protected int id;
    protected String title;
    protected String author;
    protected int publication_year;
    protected boolean isAvailable;
    protected DocumentType documentType; // ← Loại tài liệu

    public Document( String title, String author, int publication_year, DocumentType documentType) {
        this.title = title;
        this.author = author;
        this.publication_year = publication_year;
        this.documentType = documentType;
        this.isAvailable = false;
    }

    public Document( int id, String title, String author, int publication_year, DocumentType documentType) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publication_year = publication_year;
        this.documentType = documentType;
        this.isAvailable = false;
    }
    // Abstract method
    public abstract void displayInfo();

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return publication_year; }
    public boolean isAvailable() { return isAvailable; }
    public DocumentType getDocumentType() { return documentType; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setYear(int publication_year) { this.publication_year = publication_year; }
    public void setAvailable(boolean available) { this.isAvailable = available; }
    public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }

    @Override
    public String toString() {
        return "[" + documentType + "] " + " " + id + " " +
        title + " " + author + " " + publication_year + " " +isAvailable;
    }
}
