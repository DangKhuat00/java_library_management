package librarymanage.java_library_management.src.model;

public class Document {
    private String id;
    private String title;
    private String author;
    private String publisher;
    private String category;
    private int year;
    private int numbers;
    private boolean isAvailable;

    public Document() {
    }

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
    // =====================================================================================================
    public String getId() { return id;}
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public int getYear() { return year; }
    public String getCategory() { return category; }
    public int getNumbers(){ return numbers; }
    public boolean isAvailable() { return isAvailable; }

    // ==========================================================================================

    public void setId(String id) {this.id = id;}
    public void setPublisher(String publisher) {this.publisher = publisher;}
    public void setNumbers(int numbers) {this.numbers = numbers;}
    public void setYear(int year) {this.year = year;}
    public void setTitle(String title) {this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category;}
    public void setAvailable(boolean available) {isAvailable = available;}

    // ======================= Print Output =====================================
    public void printFor() {
        System.out.print("Id: " + this.id);
        System.out.print(" - Title: " + this.title);
        System.out.print(" - Author: " + this.author);
        System.out.print(" - Publisher: " + this.publisher);
        System.out.print(" - Category: " + this.category);
        System.out.print(" - Year: " + this.year);
        System.out.print(" - Numbers: " + this.numbers);
        System.out.print(" - Is Available: " + this.isAvailable + "\n");
    }
    public static void main(String[] args) {
    }
}