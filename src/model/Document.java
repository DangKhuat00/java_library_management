package model;

public class Document {
    protected int id;
    protected String title;
    protected String language;
    protected int pages;
    protected String author;
    protected int publicationYear;
    protected int remainDocs;

    public Document(String title, String language, int pages, String author, int publicationYear, int remainDocs ) {
        this.title = title;
        this.language = language;
        this.pages = pages;
        this.author = author;
        this.publicationYear = publicationYear;
        this.remainDocs = remainDocs;
    }

     public Document(int id, String title, String language, int pages, String author, int publicationYear, int remainDocs ) {
        this.id = id;
        this.title = title;
        this.language = language;
        this.pages = pages;
        this.author = author;
        this.publicationYear = publicationYear;
        this.remainDocs = remainDocs;
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

    public int getRemainDocs() {
        return remainDocs;
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

    public void setRemainDocs(int remainDocs) {
        this.remainDocs = remainDocs;
    }

     public void displayInfo() {
        System.out.println("ID: " + id);
        System.out.println("Title: " + title);
        System.out.println("Language: " + language);
        System.out.println("Pages: " + pages);
        System.out.println("Author: " + author);
        System.out.println("Publication Year: " + publicationYear);
        System.out.println("Remain Docs: " + remainDocs);
    }
}
