package model;

import java.util.ArrayList;
import java.util.List;

public class Library{
    private List<Document> documents;
    private List<User> users;
    
    public Library() {
        this.documents = new ArrayList<>();
        this.users = new ArrayList<>();
    }
}