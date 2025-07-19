package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private int borrowLimit;
    private List<Document> borrowedDocuments;

    public User(String id, String name, String emal, String phoneNumber, int borrowLimit){
        this.id = id;
        this.name=name;
        this.email=emal;
        this.phoneNumber=phoneNumber;
        this.borrowLimit=borrowLimit;
        this.borrowedDocuments=new ArrayList<>();
    }
    public void getUserInformation(String id){
        if(this.id.equals(id))
        {
            System.out.println("User ID: " + this.id);
            System.out.println("Name: " + this.name);
            System.out.println("Email: " + this.email);
            System.out.println("Phone: " + this.phoneNumber);
            System.out.println("Borrow Limit: " + this.borrowLimit);
            System.out.println("Borrowed Documents:");
            for(int i=0; i<borrowedDocuments.size(); i++){
                System.out.println("- " + borrowedDocuments.get(i));
            }
        }   
    }
    
    public boolean borrowDocument(Document document) {
        if (borrowedDocuments.size() >= borrowLimit) {
            System.out.println("Bạn đã đạt giới hạn mượn tài liệu (" + borrowLimit + " tài liệu)");
            return false;
        }
        
        if (borrowedDocuments.contains(document)) {
            System.out.println("Bạn đã mượn tài liệu này rồi");
            return false;
        }
        
        borrowedDocuments.add(document);
        System.out.println("Mượn tài liệu thành công: " + document);
        return true;
    }
    
    public boolean returnDocument(Document document) {
        if (!borrowedDocuments.contains(document)) {
            System.out.println("Bạn không có mượn tài liệu này");
            return false;
        }
        
        borrowedDocuments.remove(document);
        System.out.println("Trả tài liệu thành công: " + document);
        return true;
    }
    
    public void showBorrowedDocuments() {
        System.out.println("Danh sách tài liệu đã mượn:");
        if (borrowedDocuments.isEmpty()) {
            System.out.println("Không có tài liệu nào");
        } else {
            for (int i = 0; i < borrowedDocuments.size(); i++) {
                System.out.println((i + 1) + ". " + borrowedDocuments.get(i));
            }
        }
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public int getBorrowLimit() { return borrowLimit; }
    public List<Document> getBorrowedDocuments() { return borrowedDocuments; }
}
