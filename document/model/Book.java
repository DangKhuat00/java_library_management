package librarymanage.java_library_management.document.model;

public class Book extends Document {
    private static long count = 0;
    private long dem;
    public Book(String id, String title, String author, String publisher, String category, int year, int numbers, boolean isAvailable) {
        super(id, title, author, publisher, category, year, numbers, isAvailable);
        dem =count + 1;
    }
    public static void upCount(){
        count++;
    }
    public void printFor(Document doc) {
        String result = String.format("BOOK%03d",dem);
        System.out.println("================================" +result +"=========================================");
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
