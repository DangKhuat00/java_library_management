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
    // ================================ them tai lieu ==========================================================
    public void addDocument() {
        Scanner sc = new Scanner(System.in);
        boolean tmp;
        do {
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
            } catch (InputMismatchException imp) {
                System.out.println("Enter correct data,please");
                tmp = true;
            }
        } while (tmp);
        if (numbers != 0) {
            this.isAvailable = true;
        } else {
            this.isAvailable = false;
        }
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




    // ================================ Cac chuc nang cua chuong trinh =========================================
    public static void deleteDocument() {
        System.out.println("Enter ID you want to remove: ");
        String id = new Scanner(System.in).nextLine();
        Document doc = new Document(id, "", "", "", "", 0, 0, true);
        int tmp = 0;
        for (Document x : documents) {
            if (Objects.equals(x.getId(), doc.getId())) {
                break;
            }
            tmp++;
        }
        if (tmp == documents.size()) {
            System.out.println("Document not exist");
        } else {
            documents.remove(tmp);
        }
    }
    public static void updateDocument() {
        Document doc = new Document(); // chuc nang sua document
        System.out.println("Enter document you want to update");
        doc.addDocument();
        int tmp = 0;
        boolean error = true;
        for (Document x : documents) {
            if (x.getId().equals(doc.getId())) {
                documents.set(tmp, doc);
                error = false;
            }
            tmp++;
        }
        if (error) {
            System.out.println("Document not exist");
        }
    }
    public static void findDocument() {
        System.out.println("You want to search for document by:\n1.title\n2.author\n3.category");
        Scanner sc = new Scanner(System.in);
        String luachon = sc.nextLine();
        Document doc = new Document();
        switch (luachon) {
            case "1":
                String name = sc.nextLine();
                doc.setTitle(name);
                for (Document x : documents) {
                    if (x.getTitle().equals(doc.getTitle())) {
                        x.printFor();
                    }
                }
                break;
            case "2":
                String author = sc.nextLine();
                doc.setAuthor(author);
                for (Document x : documents) {
                    if (x.getAuthor().equals(doc.getAuthor())) {
                        x.printFor();
                    }
                }
                break;
            case "3":
                String category = sc.nextLine();
                doc.setCategory(category);
                for (Document x : documents) {
                    if (x.getCategory().equals(doc.getCategory())) {
                        x.printFor();
                    }
                }
                break;


        }
    }



    // ======================= in ra =====================================
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