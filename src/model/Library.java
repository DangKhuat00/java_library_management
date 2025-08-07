package model;

import dao.DocumentDAO;
import dao.UserDAO;
import dao.BorrowDAO;

import java.util.List;
import java.util.Scanner;

/**
 * Represents the Library with 9 core functions
 */
public class Library {

    private DocumentDAO documentDAO;
    private UserDAO userDAO;
    private BorrowDAO borrowDAO;

    public Library() {
        this.documentDAO = new DocumentDAO();
        this.userDAO = new UserDAO();
        this.borrowDAO = new BorrowDAO();
    }

    // ========== GUI-COMPATIBLE METHODS (for MainFrame) ==========
    
    /**
     * 1. Add Document (GUI version)
     */
    public boolean addDocument(String title, String author, int year, String type, int pages, int issue) {
        Document document = null;
        
        if (type.equals("BOOK")) {
            document = new Book(title, author, year, pages);
        } else if (type.equals("MAGAZINE")) {
            document = new Magazine(title, author, year, issue);
        } else {
            return false;
        }
        
        return documentDAO.insertDocument(document);
    }
    
    /**
     * 2. Remove Document (GUI version)
     */
    public boolean removeDocument(String documentId) {
        return documentDAO.deleteDocument(documentId);
    }
    
    /**
     * 3. Find Documents (GUI version)
     */
    public List<Document> findDocuments(String keyword) {
        return documentDAO.findDocument(keyword);
    }
    
    /**
     * 4. Get All Documents (GUI version)
     */
    public List<Document> getAllDocuments() {
        return documentDAO.getAllDocuments();
    }
    
    /**
     * 5. Add User (GUI version)
     */
    public boolean addUser(String name, String email, String phone, int maxBorrowLimit) {
        User user = new User(name, email, phone);
        user.setBorrowLimit(maxBorrowLimit);
        return userDAO.insertUser(user);
    }
    
    /**
     * 6. Get All Users (GUI version)
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    /**
     * 7. Borrow Document (GUI version)
     */
    public boolean borrowDocument(String userId, String documentId) {
        return borrowDAO.borrowDocument(userId, documentId);
    }
    
    /**
     * 8. Return Document (GUI version)
     */
    public boolean returnDocument(String userId, String documentId) {
        return borrowDAO.returnDocument(userId, documentId);
    }
    
    /**
     * 9. Update Document (GUI version)
     */
    public boolean updateDocument(String documentId, String title, String author, int year, String type, int pages, int issue) {
        Document document = null;
        
        try {
            int id = Integer.parseInt(documentId);
            if (type.equals("BOOK")) {
                document = new Book(id, title, author, year, pages);
            } else if (type.equals("MAGAZINE")) {
                document = new Magazine(id, title, author, year, issue);
            } else {
                return false;
            }
            
            return documentDAO.updateDocument(document);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // ========== CONSOLE-BASED METHODS (for backward compatibility) ==========
    
    public void addDocument(Scanner scanner) {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter publication year: ");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter type (BOOK/MAGAZINE): ");
        String type = scanner.nextLine().trim().toUpperCase();

        Document document = null;

        if (type.equals("BOOK")) {
            System.out.print("Enter number of pages: ");
            int pages = Integer.parseInt(scanner.nextLine());
            document = new Book(title, author, year, pages);
        } else if (type.equals("MAGAZINE")) {
            System.out.print("Enter issue number: ");
            int issue = Integer.parseInt(scanner.nextLine());
            document = new Magazine(title, author, year, issue);
        } else {
            System.out.println("Invalid document type.");
            return;
        }

        if (documentDAO.insertDocument(document)) {
            System.out.println("Document added successfully.");
        } else {
            System.out.println("Failed to add document.");
        }
    }

    public void removeDocument(Scanner scanner) {
        System.out.print("Enter document ID to remove: ");
        String id = scanner.nextLine();
        if (documentDAO.deleteDocument(id)) {
            System.out.println("Document removed successfully.");
        } else {
            System.out.println("Failed to remove document.");
        }
    }

    public void updateDocument(Scanner scanner) {
        System.out.print("Enter ID of document to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new title: ");
        String title = scanner.nextLine();
        System.out.print("Enter new author: ");
        String author = scanner.nextLine();
        System.out.print("Enter new publication year: ");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new type (BOOK/MAGAZINE): ");
        String type = scanner.nextLine().trim().toUpperCase();

        Document updatedDocument = null;

        if (type.equals("BOOK")) {
            System.out.print("Enter number of pages: ");
            int pages = Integer.parseInt(scanner.nextLine());
            updatedDocument = new Book(id, title, author, year, pages);
        } else if (type.equals("MAGAZINE")) {
            System.out.print("Enter issue number: ");
            int issue = Integer.parseInt(scanner.nextLine());
            updatedDocument = new Magazine(id, title, author, year, issue);
        } else {
            System.out.println("Invalid document type.");
            return;
        }

        if (documentDAO.updateDocument(updatedDocument)) {
            System.out.println("Document updated successfully.");
        } else {
            System.out.println("Failed to update document.");
        }
    }

    public void findDocument(Scanner scanner) {
        System.out.print("Enter document keyword(title or author) to find: ");
        String keyword = scanner.nextLine();
        List<Document> documents = documentDAO.findDocument(keyword);

        if (documents.isEmpty()) {
            System.out.println("Document not found.");
        } else {
            // Hiển thị toàn bộ tài liệu tìm thấy
            for (Document doc : documents) {
                System.out.println(doc);
            }
        }
    }

    public void displayAllDocuments() {
        documentDAO.getAllDocuments().forEach(System.out::println);
    }

    public void addUser(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phoneNumber: ");
        String phoneNumber = scanner.nextLine();

        User user = new User(name, email, phoneNumber);
        if (userDAO.insertUser(user)) {
            System.out.println("User added successfully.");
        } else {
            System.out.println("Failed to add user.");
        }
    }

    public void displayUserInfo(Scanner scanner) {
        System.out.print("Enter user ID to display: ");
        int id = Integer.parseInt(scanner.nextLine());
        User user = userDAO.getUserById(id);
        if (user != null) {
            System.out.println(user);
        } else {
            System.out.println("User not found.");
        }
    }

    public void displayAllUsers() {
        userDAO.getAllUsers().forEach(System.out::println);
    }

    public void borrowDocument(Scanner scanner) {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter document ID: ");
        String documentId = scanner.nextLine();

        if (borrowDAO.borrowDocument(userId, documentId)) {
            System.out.println("Document borrowed successfully.");
        } else {
            System.out.println("Failed to borrow document.");
        }
    }

    public void returnDocument(Scanner scanner) {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter document ID: ");
        String documentId = scanner.nextLine();

        if (borrowDAO.returnDocument(userId, documentId)) {
            System.out.println("Document returned successfully.");
        } else {
            System.out.println("Failed to return document.");
        }
    }
}
