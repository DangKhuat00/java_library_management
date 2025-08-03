import config.DatabaseConfig;
import dao.DocumentDAO;
import dao.UserDAO;
import model.Book;
import model.Document;
import model.DocumentType;
import model.Library;
import model.Magazine;
import model.User;
import java.util.Scanner;

public class LibraryApp {
    private static Library library = new Library();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    LIBRARY MANAGEMENT SYSTEM");
        System.out.println("         MySQL Database Edition");
        System.out.println("========================================");

        // Optional: Initialize database tables
        // DatabaseConfig.initializeDatabase();

        System.out.println("Welcome to Library Management System!");

        while (true) {
            showMainMenu();
            int choice = getValidChoice();
            handleMenuChoice(choice);
        }
    }

    private static void showMainMenu() {
        System.out.println("\n[0] Exit");
        System.out.println("[1] Add Document");
        System.out.println("[2] Remove Document");
        System.out.println("[3] Update Document");
        System.out.println("[4] Find Document");
        System.out.println("[5] Display Document");
        System.out.println("[6] Add User");
        System.out.println("[7] Borrow Document");
        System.out.println("[8] Return Document");
        System.out.println("[9] Display User Info");
        System.out.print("Enter your choice: ");
    }

    private static void handleMenuChoice(int choice) {
        switch (choice) {
            case 0:
                System.out.println("Thank you for using the Library Management System!");
                System.exit(0);
                break;
            case 1:
                library.addDocument(scanner);
                break;
            case 2:
                library.removeDocument(scanner);
                break;
            case 3:
                library.updateDocument(scanner);
                break;
            case 4:
                library.findDocument(scanner);
                break;
            case 5:
                library.displayAllDocuments();
                break;
            case 6:
                library.addUser(scanner);
                break;
            case 7:
                library.borrowDocument(scanner);
                break;
            case 8:
                library.returnDocument(scanner);
                break;
            case 9:
                library.displayUserInfo(scanner);
                break;
            default:
                System.out.println("Action is not supported");
        }
    }

    private static int getValidChoice() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (input.length() != 1 || !Character.isDigit(input.charAt(0))) {
                    System.out.println("Action is not supported");
                    continue;
                }
                int choice = Character.getNumericValue(input.charAt(0));
                if (choice < 0 || choice > 9) {
                    System.out.println("Action is not supported");
                    continue;
                }
                return choice;
            } catch (Exception e) {
                System.out.println("Action is not supported");
            }
        }
    }

    private static void manageDocuments(Scanner scanner, DocumentDAO documentDAO) {
        System.out.println("\nDocument Management");
        System.out.println("1. View All Documents");
        System.out.println("2. Search Documents");
        System.out.println("3. Add Document");
        System.out.println("4. Exit");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                documentDAO.getAllDocuments().forEach(System.out::println);
                break;
            case 2:
                System.out.print("Enter keyword: ");
                String keyword = scanner.nextLine();
                documentDAO.searchDocuments(keyword).forEach(System.out::println);
                break;
            case 3:
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
                String typeStr = scanner.nextLine();

                DocumentType type;
                try {
                    type = DocumentType.valueOf(typeStr.toUpperCase());
                } catch (IllegalArgumentException | NullPointerException e) {
                    System.out.println("Invalid document type.");
                    return;
                }

                Document document = null;
                if (type == DocumentType.BOOK) {
                    System.out.print("Enter number of pages: ");
                    int pages = scanner.nextInt();
                    scanner.nextLine();
                    document = new Book( title, author, year, pages);
                } else if (type == DocumentType.MAGAZINE) {
                    System.out.print("Enter issue number: ");
                    int issue = scanner.nextInt();
                    scanner.nextLine();
                    document = new Magazine( title, author, year, issue);
                }

                if (documentDAO.insertDocument(document)) {
                    System.out.println("Document added successfully.");
                } else {
                    System.out.println("Failed to add document.");
                }
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void manageUsers(Scanner scanner, UserDAO userDAO) {
        System.out.println("\nUser Management");
        System.out.println("1. View All Users");
        System.out.println("2. Add User");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                userDAO.getAllUsers().forEach(System.out::println);
                break;
            case 2:
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
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }
}
