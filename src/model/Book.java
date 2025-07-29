package librarymanage.java_library_management.src.model;

public class Book extends Document {
    private static long count = 0;
    public Book(String id, String title, String author, String publisher, String category, int year, int numbers, boolean isAvailable) {
        super(id, title, author, publisher, category, year, numbers, isAvailable);
    }
    public void upCount(){
        count++;
    }
    public void printFor(Document doc) {
        String result = String.format("BOOK%03d",count);
        System.out.println("================================BOOK" +result +"=========================================");
        System.out.println("\n");
        System.out.println("Id: " + doc.getId());
        System.out.println(" - Title: " + doc.getTitle());
        System.out.println(" - Author: " + doc.getAuthor());
        System.out.println(" - Publisher: " + doc.getPublisher());
        System.out.println(" - Category: " + doc.getCategory());
        System.out.println(" - Year: " + doc.getYear());
        System.out.println(" - Numbers: " + doc.getNumbers());
        System.out.println(" - Is Available: " + doc.isAvailable());
    }


}
