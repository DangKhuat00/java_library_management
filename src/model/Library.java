package model;

import dao.DocumentDAO;
import dao.UserDAO;
import dao.BorrowDAO;

import java.util.List;
import java.util.Scanner;

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

    // Thêm tài liệu mới
    public boolean addDocument(Document document) {
        return documentDAO.insertDocument(document);
    }

    // Xóa tài liệu theo id
    public boolean removeDocument(String documentId) {
        try {
            int id = Integer.parseInt(documentId);
            return documentDAO.deleteDocument(id);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Tìm tài liệu theo keyword (title hoặc author)
    public List<Document> findDocuments(String keyword) {
        return documentDAO.findDocument(keyword);
    }

    // Lấy tất cả tài liệu
    public List<Document> getAllDocuments() {
        return documentDAO.getAllDocuments();
    }

    // Cập nhật tài liệu
    public boolean updateDocument(Document document) {
        return documentDAO.updateDocument(document);
    }

    // Lấy tài liệu theo id
    public Document getDocumentById(int id) {
        List<Document> docs = documentDAO.getAllDocuments();
        for (Document d : docs) {
            if (d.getId() == id) return d;
        }
        return null;
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
    
    // ========== GUI methods tương tự các hàm console có thể gọi thêm ==========

    // Còn các hàm về User, Borrow giữ nguyên, không liên quan Document

    // Các hàm Console bạn có thể tự chỉnh sửa tương tự:
    // - Thay vì phân biệt BOOK/MAGAZINE, dùng trực tiếp Document
    // - Ví dụ:

    public void addDocument(Scanner scanner) {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter publication year: ");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter language: ");
        String language = scanner.nextLine();
        System.out.print("Enter number of pages: ");
        int pages = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter remaining documents: ");
        int remainDocs = Integer.parseInt(scanner.nextLine());

        Document document = new Document(title, language, pages, author, year, remainDocs) {
            @Override
            public void displayInfo() {
                // Có thể implement hoặc để trống
            }
        };

        if (documentDAO.insertDocument(document)) {
            System.out.println("Document added successfully.");
        } else {
            System.out.println("Failed to add document.");
        }
    }

    public boolean removeDocument(int id) {
        return documentDAO.deleteDocument(id);
    }

    public void updateDocument(Scanner scanner) {
        try {
            System.out.print("Enter ID of document to update: ");
            int id = Integer.parseInt(scanner.nextLine());

            Document oldDoc = getDocumentById(id);
            if (oldDoc == null) {
                System.out.println("Document not found.");
                return;
            }

            System.out.print("Enter new title: ");
            String title = scanner.nextLine();
            System.out.print("Enter new author: ");
            String author = scanner.nextLine();
            System.out.print("Enter new publication year: ");
            int year = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter new language: ");
            String language = scanner.nextLine();
            System.out.print("Enter number of pages: ");
            int pages = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter remaining documents: ");
            int remainDocs = Integer.parseInt(scanner.nextLine());

            Document updatedDocument = new Document(id, title, language, pages, author, year, remainDocs) {
                @Override
                public void displayInfo() { }
            };

            if (documentDAO.updateDocument(updatedDocument)) {
                System.out.println("Document updated successfully.");
            } else {
                System.out.println("Failed to update document.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format.");
        }
    }

     public void removeDocument(Scanner scanner) {
        System.out.print("Enter document ID to remove: ");
        int id = Integer.parseInt(scanner.nextLine());
        if (documentDAO.deleteDocument(id)) {
            System.out.println("Document removed successfully.");
        } else {
            System.out.println("Failed to remove document.");
        }
    }

    public void findDocument(Scanner scanner) {
        System.out.print("Enter document keyword (title or author) to find: ");
        String keyword = scanner.nextLine();
        List<Document> documents = documentDAO.findDocument(keyword);

        if (documents.isEmpty()) {
            System.out.println("Document not found.");
        } else {
            for (Document doc : documents) {
                System.out.println(doc);
            }
        }
    }

    public void displayAllDocuments() {
        List<Document> docs = documentDAO.getAllDocuments();
        for (Document d : docs) {
            System.out.println(d);
        }
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

