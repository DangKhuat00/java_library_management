package librarymanage.java_library_management.src.model;

public class Magazine extends Document {
    private static long page = 0;
    private long dem;
    public Magazine(String id, String title, String author, String publisher, String category, int year, int numbers, boolean isAvailable) {
        super(id, title, author, publisher, category, year, numbers, isAvailable);
        dem = page +1;
    }

    public static void upPage(){
        page++;
    }
    public  void printFor(Document doc) {
        String result = String.format("Magazine%03d",dem);
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
