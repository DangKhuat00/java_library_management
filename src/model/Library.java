package model;

import dao.DocumentDAO;
import dao.UserDAO;
import dao.BorrowDAO;

import java.util.Scanner;

/**
 * Represents the Library
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

    public void addDocument(Scanner scanner) {
        System.out.print("Enter document ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter publication year: ");
        int year = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter type (BOOK/MAGAZINE): ");
        String type = scanner.nextLine();

       Document document = type.equalsIgnoreCase("BOOK") ?
        new Book(id, title, author, "", "", year, 1, true) :
        new Magazine(id, title, author, "", "", year, 1, true);

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
        System.out.print("Enter document ID to update: ");
        String id = scanner.nextLine();
        System.out.print("Enter new title: ");
        String title = scanner.nextLine();
        System.out.print("Enter new author: ");
        String author = scanner.nextLine();
        System.out.print("Enter new publication year: ");
        int year = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new type (BOOK/MAGAZINE): ");
        String type = scanner.nextLine();

        Document updatedDocument = type.equalsIgnoreCase("BOOK") ?
                new Book(id, title, author, year, "BOOK") :
                new Magazine(id, title, author, year, "MAGAZINE");

        if (documentDAO.updateDocument(updatedDocument)) {
            System.out.println("Document updated successfully.");
        } else {
            System.out.println("Failed to update document.");
        }
    }

    public void findDocument(Scanner scanner) {
        System.out.print("Enter document ID to find: ");
        String id = scanner.nextLine();
        Document document = documentDAO.getDocumentById(id);
        if (document != null) {
            System.out.println(document);
        } else {
            System.out.println("Document not found.");
        }
    }

    public void displayAllDocuments() {
        documentDAO.getAllDocuments().forEach(System.out::println);
    }

    public void addUser(Scanner scanner) {
        System.out.print("Enter user ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        User user = new User(id, name, email);
        if (userDAO.insertUser(user)) {
            System.out.println("User added successfully.");
        } else {
            System.out.println("Failed to add user.");
        }
    }

    public void displayUserInfo(Scanner scanner) {
        System.out.print("Enter user ID to display: ");
        String id = scanner.nextLine();
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