package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Library {
    private List<Document> documents;
    private List<User> users;
    
    public Library() {
        this.documents = new ArrayList<>();
        this.users = new ArrayList<>();
    }
    
    // ========== DOCUMENT MANAGEMENT ==========
    
    public void addDocument(Scanner scanner) {
        System.out.print("Enter document type (book/magazine): ");
        String type = scanner.nextLine().toLowerCase();
        
        String id = generateDocumentId();
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter year: ");
        int year = scanner.nextInt();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        Document document;
        if (type.equals("book")) {
            document = new Book(id, title, author, publisher, category, year, quantity, quantity > 0);
        } else {
            document = new Magazine(id, title, author, publisher, category, year, quantity, quantity > 0);
        }
        
        documents.add(document);
        System.out.println("✅ Document added successfully with ID: " + id);
    }
    
    public void removeDocument(Scanner scanner) {
        displayAllDocuments();
        System.out.print("Enter document ID to remove: ");
        String id = scanner.nextLine();
        
        for (int i = 0; i < documents.size(); i++) {
            if (documents.get(i).getId().equals(id)) {
                documents.remove(i);
                System.out.println("✅ Document removed successfully!");
                return;
            }
        }
        System.out.println("❌ Document not found!");
    }
    
    public void updateDocument(Scanner scanner) {
        displayAllDocuments();
        System.out.print("Enter document ID to update: ");
        String id = scanner.nextLine();
        
        for (Document doc : documents) {
            if (doc.getId().equals(id)) {
                System.out.print("Enter new title: ");
                doc.setTitle(scanner.nextLine());
                System.out.print("Enter new author: ");
                doc.setAuthor(scanner.nextLine());
                System.out.print("Enter new publisher: ");
                doc.setPublisher(scanner.nextLine());
                System.out.print("Enter new category: ");
                doc.setCategory(scanner.nextLine());
                System.out.print("Enter new year: ");
                doc.setYear(scanner.nextInt());
                System.out.print("Enter new quantity: ");
                doc.setNumbers(scanner.nextInt());
                scanner.nextLine(); // consume newline
                
                doc.setAvailable(doc.getNumbers() > 0);
                System.out.println("✅ Document updated successfully!");
                return;
            }
        }
        System.out.println("❌ Document not found!");
    }
    
    public void findDocument(Scanner scanner) {
        System.out.println("Search by:\n1. Title\n2. Author\n3. Category");
        System.out.print("Choose option: ");
        String choice = scanner.nextLine();
        
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine().toLowerCase();
        
        boolean found = false;
        for (Document doc : documents) {
            boolean match = false;
            switch (choice) {
                case "1":
                    match = doc.getTitle().toLowerCase().contains(searchTerm);
                    break;
                case "2":
                    match = doc.getAuthor().toLowerCase().contains(searchTerm);
                    break;
                case "3":
                    match = doc.getCategory().toLowerCase().contains(searchTerm);
                    break;
            }
            
            if (match) {
                doc.displayInfo();
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("❌ No documents found!");
        }
    }
    
    public void displayAllDocuments() {
        if (documents.isEmpty()) {
            System.out.println("❌ No documents available.");
            return;
        }
        
        System.out.println("===== ALL DOCUMENTS =====");
        for (Document doc : documents) {
            doc.displayInfo();
        }
    }
    
    // ========== USER MANAGEMENT ==========
    
    public void addUser(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter borrow limit: ");
        int borrowLimit = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        String userId = generateUserId();
        User user = new User(userId, name, email, phone, borrowLimit);
        users.add(user);
        
        System.out.println("✅ User added successfully with ID: " + userId);
    }
    
    public void borrowDocument(Scanner scanner) {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        
        displayAllDocuments();
        System.out.print("Enter document ID: ");
        String docId = scanner.nextLine();
        
        User user = findUserById(userId);
        Document document = findDocumentById(docId);
        
        if (user == null) {
            System.out.println("❌ User not found!");
            return;
        }
        
        if (document == null) {
            System.out.println("❌ Document not found!");
            return;
        }
        
        if (user.borrowDocument(document)) {
            System.out.println("✅ Document borrowed successfully!");
        } else {
            System.out.println("❌ Cannot borrow document!");
        }
    }
    
    public void returnDocument(Scanner scanner) {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter document ID: ");
        String docId = scanner.nextLine();
        
        User user = findUserById(userId);
        Document document = findDocumentById(docId);
        
        if (user == null) {
            System.out.println("❌ User not found!");
            return;
        }
        
        if (document == null) {
            System.out.println("❌ Document not found!");
            return;
        }
        
        if (user.returnDocument(document)) {
            System.out.println("✅ Document returned successfully!");
        } else {
            System.out.println("❌ Cannot return document!");
        }
    }
    
    public void displayUserInfo(Scanner scanner) {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        
        User user = findUserById(userId);
        if (user == null) {
            System.out.println("❌ User not found!");
            return;
        }
        
        System.out.println("===== USER INFORMATION =====");
        System.out.println("ID: " + user.getId());
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Phone: " + user.getPhoneNumber());
        System.out.println("Borrow Limit: " + user.getBorrowLimit());
        System.out.println("Currently Borrowed: " + user.getBorrowedCount());
        
        if (user.getBorrowedCount() > 0) {
            System.out.println("Borrowed Documents:");
            for (Document doc : user.getBorrowedDocuments()) {
                System.out.println("- " + doc.toString() + " (ID: " + doc.getId() + ")");
            }
        }
        System.out.println("=============================");
    }
    
    // ========== HELPER METHODS ==========
    
    private User findUserById(String userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
    
    private Document findDocumentById(String docId) {
        for (Document doc : documents) {
            if (doc.getId().equals(docId)) {
                return doc;
            }
        }
        return null;
    }
    
    private String generateUserId() {
        return "USER" + String.format("%03d", users.size() + 1);
    }
    
    private String generateDocumentId() {
        return "DOC" + String.format("%03d", documents.size() + 1);
    }
    
    // ========== INITIALIZATION ==========
    
    public void initializeSampleData() {
        // Sample documents
        documents.add(new Book("DOC001", "Java Programming", "John Smith", "Tech Publisher", "Programming", 2023, 5, true));
        documents.add(new Book("DOC002", "Database Design", "Jane Doe", "Education Press", "Database", 2022, 3, true));
        documents.add(new Magazine("DOC003", "Tech Weekly", "Tech Team", "Weekly Press", "Technology", 2024, 10, true));
        
        // Sample users
        users.add(new User("USER001", "Alice Johnson", "alice@email.com", "123456789", 3));
        users.add(new User("USER002", "Bob Wilson", "bob@email.com", "987654321", 5));
        
        System.out.println("Sample data initialized:");
        System.out.println("- Users: USER001 (Alice Johnson), USER002 (Bob Wilson)");
        System.out.println("- Documents: DOC001, DOC002, DOC003");
    }
}
