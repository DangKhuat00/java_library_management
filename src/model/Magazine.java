package librarymanage.java_library_management.src.model;

public class Magazine extends Document {
    private static long page = 0;
    public Magazine(String id, String title, String author, String publisher, String category, int year, int numbers, boolean isAvailable) {
        super(id, title, author, publisher, category, year, numbers, isAvailable);
    }

    public void upPage(){
        page++;
    }
    public  void printFor(Document doc) {
        String result = String.format("Magazine%03d",page);
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
