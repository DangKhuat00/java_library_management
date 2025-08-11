package model;

public class Document {
    protected int id;
    protected String title;
    protected String language;
    protected int pages;
    protected String author;
    protected int publicationYear;
    protected boolean isAvailable;


    public Document(String title, String language, int pages, String author, int publicationYear) {
        this.title = title;
        this.language = language;
        this.pages = pages;
        this.author = author;
        this.publicationYear = publicationYear;
        this.isAvailable = true; 
    }

     public Document(int id, String title, String language, int pages, String author, int publicationYear, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.language = language;
        this.pages = pages;
        this.author = author;
        this.publicationYear = publicationYear;
        this.isAvailable = isAvailable; 
    }

    // Getters
    public int getId() { 
        return id; 
    }

    public String getTitle() {
        return title;
    }

    public String getLanguage() {
        return language; 
    }

    public int getPages() {
        return pages; 
    }

    public String getAuthor() {
        return author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    /**
     * Getter cho trạng thái có sẵn của sách.
     * @return true nếu sách có sẵn, false nếu đã được mượn.
     */
    public boolean isAvailable() {
        return isAvailable;
    }
    
    // Setters
    public void setId(int id) {
        this.id = id;
    }
    
    public void setTitle(String title) {
        this.title = title; 
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    /**
     * Setter cho trạng thái có sẵn của sách.
     * @param available true để đặt sách là có sẵn, false để đặt là đã được mượn.
     */
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}