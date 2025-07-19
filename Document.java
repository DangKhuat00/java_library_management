package librarymanage.java_library_management;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

public class Document {
    //ArrayList<Document> documents = new ArrayList<>();
    public static int count = 0;
    private String id;
    private String title;
    private String author;
    private String publisher;
    private String category;
    private int year;
    private int numbers;
    private boolean isAvailable;
    public Document(String id,String title,String author,String publisher,int year,String category,int numbers,boolean isAvailable){
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.category = category;
        this.numbers = numbers;
        if(numbers != 0){
            this.isAvailable = true;
        }
        else{
            this.isAvailable = false;
        }
    }
//    public void addDocument(){
//        Scanner sc =new Scanner(System.in);
//
//        System.out.print("Nhap ten tac gia");
//
//    }



    public static void main(String[] args) {

    }


}
