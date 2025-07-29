package librarymanage.java_library_management;

import java.util.*;

public class documentManager {
    private ArrayList<Document> documents = new ArrayList<>();


    // thuc hien cac chuc nang quan ly document====================================================================
    public void addDocument() {
        Scanner sc = new Scanner(System.in);
        boolean tmp;
        String id = "",title = "",author = "",publisher = "",category = "";
        int year = 0,numbers = 0;
        boolean isAvailable;
        do {
            tmp = false;
            try {
                System.out.println("Enter ID: ");
                id = sc.nextLine();
                System.out.println("Enter name title: ");
                title = new Scanner(System.in).nextLine();
                System.out.println("Enter name author: ");
                author = sc.nextLine();
                System.out.println("Enter name publisher: ");
                publisher = sc.nextLine();
                System.out.println("Enter name category: ");
                category = sc.nextLine();
                System.out.println("Enter year: ");
                year = sc.nextInt();
                System.out.println("Enter numbers of book: ");
                numbers = sc.nextInt();
            } catch (InputMismatchException imp) {
                System.out.println("Enter correct data,please");
                tmp = true;
            }
        } while (tmp);
        if (numbers != 0) {
            isAvailable = true;
        } else {
            isAvailable = false;
        }
        documents.add(new Document(id,title,author,publisher,category,year,numbers,isAvailable));
    }
    public void deleteDocument() {
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
    public void updateDocument() {
        System.out.print("Enter the ID you want to update: ");
        Scanner sc = new Scanner(System.in);
        String id = sc.nextLine();
        System.out.println("Enter the new document: ");
        for (Document doc : documents) {
            if (doc.getId().equals(id)) {
                System.out.print("-Enter the new title: ");
                doc.setTitle(sc.nextLine());
                System.out.print("-Enter name author:");
                doc.setAuthor(sc.nextLine());
                System.out.println("-Enter name publisher: ");
                doc.setPublisher(sc.nextLine());
                System.out.print("-Enter category: ");
                doc.setCategory(sc.nextLine());
                System.out.print("-Enter year: ");
                doc.setYear(sc.nextInt());
                System.out.print("-Enter quantity: ");
                doc.setNumbers(sc.nextInt());
                if(doc.getNumbers() == 0){
                    doc.setAvailable(false);
                }
                else{
                    doc.setAvailable(true);
                }
                System.out.println("Already update");
                return;
            }
        }
        System.out.println("Document not exist");
    }
    public void findDocument() {
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

    // ======================= Tra ve danh sach document cho libray =====================
    public ArrayList<Document> getDocuments(){
        return new ArrayList<>(documents);
    }

}
