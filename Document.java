package librarymanage.java_library_management;
import java.util.*;

public class Document {
    static ArrayList<Document> documents = new ArrayList<>();
    public static int count = 0;
    private String id;
    private String title;
    private String author;
    private String publisher;
    private String category;
    private int year;
    private int numbers;
    private boolean isAvailable;
    public void addDocument(){
        Scanner sc =new Scanner(System.in);
        count++;
        boolean tmp;
        this.id = String.valueOf(count);
        do{
            tmp = false;
            try {
                System.out.println("Enter name title: ");
                this.title = new Scanner(System.in).nextLine();
                System.out.println("Enter name author: ");
                this.author = sc.nextLine();
                System.out.println("Enter name publisher: ");
                this.publisher = sc.nextLine();
                System.out.println("Enter name category: ");
                this.category = sc.nextLine();
                System.out.println("Enter year: ");
                this.year = sc.nextInt();
                System.out.println("Enter numbers of book: ");
                this.numbers = sc.nextInt();
            }
            catch(InputMismatchException imp){
                System.out.println("Enter correct data,please");
                tmp = true;
            }
        }while(tmp);
        if(numbers != 0){
            this.isAvailable = true;
        }
        else{
            this.isAvailable = false;
        }
    }

    public static void deleteDocument(){
        for(Document x : documents){
            x.inFor();
        }
        System.out.print("Enter ID you want to remove: "); // xoa document theo id
        int idDelete = new Scanner(System.in).nextInt();
        if(idDelete > documents.size()){
            System.out.println("Document not exist");
        }
        else if(documents.isEmpty()){
            System.out.println("Enter document");
        }
        else
        {
            documents.remove( idDelete- 1);
        }
    }

    public void inFor(){
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
        System.out.println("Enter numbers of document: "); // thu nhap vao mot danh sach document bat ky
        int n = new Scanner(System.in).nextInt();
        int i = 0;
        while(i < n) {
            Document document = new Document();
            document.addDocument();
            documents.add(document);
            i++;
        }
        deleteDocument();
        for(Document x : documents){
            x.inFor();
        }
    }
}
