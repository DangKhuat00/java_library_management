package librarymanage.java_library_management;
import java.util.*;

public class Document {
    static ArrayList<Document> documents = new ArrayList<>();
    private String id;
    private String title;
    private String author;
    private String publisher;
    private String category;
    private int year;
    private int numbers;
    private boolean isAvailable;
    public Document(){
    }
    public Document(String id,String title,String author,String publisher,String category,int year,int numbers,boolean isAvailable){
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
        this.year = year;
        this.numbers = numbers;
        this.isAvailable = isAvailable;
    }

    public void addDocument(){
        Scanner sc =new Scanner(System.in);
        boolean tmp;
        do{
            tmp = false;
            try {
                System.out.println("Enter ID: ");
                this.id = sc.nextLine();
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
    public String getId(){
        return id;
    }


    public static void deleteDocument(){
        System.out.println("Enter ID you want to remove: ");
        String id = new Scanner(System.in).nextLine();
        Document doc= new Document(id,"","","","",0,0,true);
        int tmp = 0;
        for(Document x : documents){
            if(Objects.equals(x.getId(), doc.getId())){
                break;
            }
            tmp++;
        }
        if(tmp == documents.size()){
            System.out.println("Document not exist");
        }
        else{
            documents.remove(tmp);
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
        System.out.println("Enter numbers of document: ");// nhap mot danh sach document bat ky
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
