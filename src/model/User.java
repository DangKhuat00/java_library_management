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
}
