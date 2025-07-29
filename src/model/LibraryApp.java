package librarymanage.java_library_management.src.model;


import java.util.Scanner;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class LibraryApp {
    private static Library library = new Library();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        // Set up encoding for console
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // Fallback if UTF-8 not supported
        }
        
        // Initialize sample documents
        initializeSampleDocuments();
        // Initialize sample users
        initializeSampleUsers();
        
        System.out.println("Welcome to My Application!");
        
        while (true) {
            showMainMenu();
            int choice = getValidChoice(0, 9);
            
            switch (choice) {
                case 0:
                    System.out.println("Thank you for using the system!");
                    return;
                case 1:
                    library.addDocumentInteractive(scanner);
                    break;
                case 2:
                    library.deleteDocument(scanner);
                    break;
                case 3:
                    library.updateDocument(scanner);
                    break;
                case 4:
                    library.findDocument(scanner);
                    break;
                case 5:
                    library.displayAllDocuments(scanner);
                    break;
                case 6:
                    addUser();
                    break;
                case 7:
                    borrowDocument();
                    break;
                case 8:
                    returnDocument();
                    break;
                case 9:
                    displayUserInfo();
                    break;
                default:
                    System.out.println("Action is not supported");
            }
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
        System.out.print("\n");
    }
    
    // ========== INPUT VALIDATION ==========
    /**
     * Get valid input from user (only accept numbers 0-9)
     */
    private static int getValidChoice(int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                
                // Check empty input
                if (input.isEmpty()) {
                    System.out.println("Action is not supported");
                    continue;
                }
                
                // Check single character only
                if (input.length() != 1) {
                    System.out.println("Action is not supported");
                    continue;
                }
                
                char c = input.charAt(0);
                
                // Check if it's a digit
                if (!Character.isDigit(c)) {
                    System.out.println("Action is not supported");
                    continue;
                }
                
                int choice = Character.getNumericValue(c);
                
                // Check range 0-9
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
    
    // ========== USER FUNCTIONS ==========
    private static void addUser() {
        System.out.println("\n=== ADD USER ===");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();
        
        System.out.print("Enter borrow limit: ");
        int borrowLimit = scanner.nextInt();
        scanner.nextLine();
        
        String userId = library.addUser(name, email, phone, borrowLimit);
        if (userId != null) {
            System.out.println("✅ User added successfully!");
            System.out.println("🆔 User ID: " + userId);
            System.out.println("💡 You can use this ID for other functions!");
        } else {
            System.out.println("❌ Cannot add user! (Email already exists)");
        }
    }
    
    private static void borrowDocument() {
        System.out.println("\n=== BORROW DOCUMENT ===");
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        
        library.displayAllDocuments(scanner);
        System.out.print("Enter document ID to borrow: ");
        String documentId = scanner.nextLine();
        
        boolean result = library.borrowDocument(userId, documentId);
        if (result) {
            System.out.println("✅ Document borrowed successfully!");
        } else {
            System.out.println("❌ Cannot borrow document!");
        }
    }
    
    private static void returnDocument() {
        System.out.println("\n=== RETURN DOCUMENT ===");
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        
        System.out.print("Enter document ID to return: ");
        String documentId = scanner.nextLine();
        
        boolean result = library.returnDocument(userId, documentId);
        if (result) {
            System.out.println("✅ Document returned successfully!");
        } else {
            System.out.println("❌ Cannot return document!");
        }
    }
    
    private static void displayUserInfo() {
        System.out.println("\n=== USER INFORMATION ===");
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        
        String userInfo = library.displayUserInfo(userId);
        System.out.println(userInfo);
    }
    
    // Initialize sample documents
    private static void initializeSampleDocuments() {
        library.addDocument(new Book("DOC001", "Java Programming", "John Smith", "Education Publisher", "IT", 2023, 5, true));
        library.addDocument(new Book("DOC002", "Database Systems", "Jane Doe", "Science Publisher", "IT", 2022, 3, true));
        library.addDocument(new Magazine("DOC003", "Computer Networks", "Bob Wilson", "Technology Publisher", "IT", 2023, 2, true));
    }
    
    // Initialize sample users
    private static void initializeSampleUsers() {
        String user1Id = library.addUser("John Smith", "john@email.com", "0123456789", 3);
        String user2Id = library.addUser("Jane Doe", "jane@email.com", "0987654321", 5);
        System.out.println("Sample users created:");
        System.out.println("   - " + user1Id + ": John Smith");
        System.out.println("   - " + user2Id + ": Jane Doe");
    }
}