import model.Library;
import java.util.Scanner;

/**
 * Main application class - Library Management System
 * Command line version with basic OOP implementation
 */
public class LibraryApp {
    private static Library library = new Library();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        // Initialize sample data
        library.initializeSampleData();
        
        System.out.println("Welcome to My Application!");
        
        // Main application loop
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
                
                if (input.isEmpty()) {
                    System.out.println("Action is not supported");
                    continue;
                }
                
                if (input.length() != 1) {
                    System.out.println("Action is not supported");
                    continue;
                }
                
                char c = input.charAt(0);
                
                if (!Character.isDigit(c)) {
                    System.out.println("Action is not supported");
                    continue;
                }
                
                int choice = Character.getNumericValue(c);
                
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
}
