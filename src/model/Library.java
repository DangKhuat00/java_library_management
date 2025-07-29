package librarymanage.java_library_management.src.model;


import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
interface dfDocument{
    int TITLE = 1;
    int AUTHOR= 2;
    int CATEGORY = 3;
    int BOOK = 1;
    int MAGAZINE =2;
    int ALLDOC = 3;
}
public class Library implements dfDocument {
    private List<Document> documents;
    private List<User> users;
    
    public Library() {
        this.documents = new ArrayList<>();
        this.users = new ArrayList<>();
    }
    /**
     * Borrow document - check conditions before lending
     * @param userId User ID
     * @param documentId Document ID
     * @return true if borrowing successful, false if failed
     */
    public boolean borrowDocument(String userId, String documentId) {
        User user = findUserById(userId);
        Document document = findDocumentById(documentId);
        
        // Check if user and document exist
        if (user == null || document == null) {
            return false;
        }
        
        // Check if document is available
        if (!document.isAvailable()) {
            return false;
        }
        
        // Check if user can borrow more
        if (!user.canBorrowMore()) {
            return false;
        }
        
        // Execute borrowing
        return user.borrowDocument(document);
    }
    /**
     * Return document - update status after returning
     * @param userId User ID
     * @param documentId Document ID
     * @return true if return successful, false if failed
     */
    public boolean returnDocument(String userId, String documentId) {
        User user = findUserById(userId);
        Document document = findDocumentById(documentId);
        
        // Check if user and document exist
        if (user == null || document == null) {
            return false;
        }
        
        // Execute document return
        return user.returnDocument(document);
    }
    
    /**
     * Add new user
     */
    public String addUser(String name, String email, String phoneNumber, int borrowLimit) {
        // Check if email already exists
        if (findUserByEmail(email) != null) {
            return null; // Email already exists
        }
        
        String userId = generateUserId();
        User newUser = new User(userId, name, email, phoneNumber, borrowLimit);
        
        if (newUser.isValidUser()) {
            users.add(newUser);
            return userId; // Return new user ID
        }
        return null; // Failed
    }
    
    /**
     * Update member personal information
     */
    public boolean updateUserInfo(String userId, String name, String email, String phoneNumber) {
        User user = findUserById(userId);
        if (user == null) {
            return false;
        }
        
        // Check if new email conflicts with other users
        if (!user.getEmail().equals(email) && findUserByEmail(email) != null) {
            return false;
        }
        
        user.updatePersonalInfo(name, email, phoneNumber);
        return true;
    }
    
    /**
     * Get member's document borrowing status
     */
    public User getUserBorrowingStatus(String userId) {
        return findUserById(userId);
    }
    
    /**
     * Display detailed user information (Display User Info function)
     */
    public String displayUserInfo(String userId) {
        User user = findUserById(userId);
        if (user == null) {
            return "User not found with ID: " + userId;
        }
        
        StringBuilder info = new StringBuilder();
        info.append("===== MEMBER INFORMATION =====\n");
        info.append("ID: ").append(user.getId()).append("\n");
        info.append("Name: ").append(user.getName()).append("\n");
        info.append("Email: ").append(user.getEmail()).append("\n");
        info.append("Phone: ").append(user.getPhoneNumber()).append("\n");
        info.append("Borrow Limit: ").append(user.getBorrowLimit()).append("\n");
        info.append("Currently Borrowed: ").append(user.getBorrowedCount()).append(" documents\n");
        info.append("Can Borrow More: ").append(user.getBorrowLimit() - user.getBorrowedCount()).append(" documents\n");
        
        if (user.getBorrowedCount() > 0) {
            info.append("\nCurrently Borrowed Documents:\n");
            int count = 1;
            for (Document doc : user.getBorrowedDocuments()) {
                info.append(count++).append(". ").append(doc.toString()).append(" (ID: ").append(doc.getId()).append(")\n");
            }
        } else {
            info.append("\nNo documents currently borrowed.\n");
        }
        
        return info.toString();
    }
    
    // ========== HELPER METHODS ==========
    
    /**
     * Find user by ID
     */
    private User findUserById(String userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Find user by email
     */
    private User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Generate automatic user ID
     */
    private String generateUserId() {
        return "USER" + String.format("%03d", users.size() + 1);
    }
    
    /**
     * Find document by ID
     */
    private Document findDocumentById(String documentId) {
        for (Document doc : documents) {
            if (doc.getId().equals(documentId)) {
                return doc;
            }
        }
        return null;
    }
    
    /**
     * Add document to library
     */
    public void addDocument(Document document) {
        documents.add(document);
    }
    
    // ========== DOCUMENT MANAGEMENT FUNCTIONS =======================================================================\
    //==========================================================================\
     //==========================================================================\
     //==================================================================================\
     //===============================================================================\
     //======================================================================================================\
     //




    /**
     *================================================================
     */
    public String generateId(){
        return String.format("DOC%03d",documents.size()+1);
    }
    /**
     * Tao ding dang ID=================================================
     */


    /**
     * @param scanner khong nhap vao khoang trang===============================================
     */
    public String readScannerString(Scanner scanner,String message){
        String input;
        do {

            input = scanner.nextLine().trim();
            if (input.isEmpty()){
                System.out.println("Enter correct data,please");
                System.out.print(message);
            }

        }while(input.isEmpty());

        return input;
    }
     public int readScannerInt(Scanner scanner,String message){
        String input;
         do {
             input = scanner.nextLine().trim();
             if (input.isEmpty()){
                 System.out.println("Enter correct data,please");
                 System.out.print(message);
             }

         }while(input.isEmpty());
         return Integer.parseInt(input);
    }
    /**
    *===============================================================================
     */



    /**
     * Them tai lieu tu terminal=================================================================================
     *
     */

    public void addDocumentInteractive(Scanner scanner) {
        boolean tmp;
        String id, title = "", author = "", publisher = "", category = "";
        int year = 0, numbers = 0;
        boolean isAvailable;

        do {
            tmp = false;
            try {
                System.out.print("Enter title: ");
                title = readScannerString(scanner,"Enter title: ");
                System.out.print("Enter author: ");
                author = readScannerString(scanner,"Enter author: ");
                System.out.print("Enter publisher: ");
                publisher = readScannerString(scanner,"Enter publisher: ");
                System.out.print("Enter category: ");
                category = readScannerString(scanner,"Enter category: ");
                System.out.print("Enter year: ");
                year = readScannerInt(scanner,"Enter year: ");
                System.out.print("Enter numbers of book: ");
                numbers = readScannerInt(scanner,"Enter numbers of book: ");
                //scanner.nextLine(); // consume newline
            } catch (Exception e) {
                System.out.println("Enter correct data, please");
                scanner.nextLine(); // clear invalid input
                tmp = true;
            }
        } while (tmp);
        
        isAvailable = numbers > 0;
        if(!isValidDocument(title,author,publisher,category,year)){
            id = generateId(); //ID duoc tu dong dinh dang theo DOC001
            System.out.println("You want to enter document is Book or Magazine");
            String access;
            do {
                access = readScannerString(scanner, "You want to enter document is Book or Magazine");
                if(!(access.equals("Book")) && !(access.equals("Magazine"))){
                    System.out.println("You just enter Book or Magazine. Please, enter correct data!");
                }
            }while(!(access.equals("Book")) && !(access.equals("Magazine")));
            if(access.equals("Book")) {
                documents.add(new Book(id, title, author, publisher, category, year, numbers, isAvailable));
                Book.upCount();
            }
            if(access.equals("Magazine")){
                documents.add(new Magazine(id, title, author, publisher, category, year, numbers, isAvailable));
                Magazine.upPage();
            }
            System.out.println("✅ Document added successfully!");
        }
        else{
            System.out.println("Document existed in list document");
        }


    }

    /**
     *  Kiem tra xem da co tai lieu day chua
     */
    public boolean isValidDocument(String title,String author,String publisher,String category,int year){
        for(Document doc : documents){
            if(doc.getAuthor().equals(author) && doc.getTitle().equals(title)){
                if(doc.getCategory().equals(category) && doc.getPublisher().equals(publisher)){
                    if(doc.getYear() == year){
                        return true;
                    }
                }
            }
        }
        return false;
    }
//====================================================================================================================
    /**
    CHUAN HOA VIEC NHAP ID THEO DINH DANG

     */
    private String readFormattedId(Scanner scanner) {
        String input;
        while (true) {
            System.out.print("Enter the document ID in the format 'DOCxxx' (e.g., DOC001): ");
            input = scanner.nextLine().trim();
            if (input.matches("^DOC\\d{3}$")) {
                return input;
            } else {
                System.out.println("Invalid format! Please enter something like DOC001, DOC123...");
            }
        }
    }
    
    /**
     * Delete document by ID===========================================================
     */
    public void deleteDocument(Scanner scanner) {
        displayAllDocuments(scanner);
        System.out.print("Enter ID you want to remove: ");
        String idDelete = readFormattedId(scanner);
        int tmp = -1;
        for (int i = 0; i < documents.size(); i++) {
            if (documents.get(i).getId().equals(idDelete)) {
                tmp = i;
                documents.remove(i);
                System.out.println("✅ Document removed successfully!");
                break;
            }
        }
        if(tmp == -1) {
            System.out.println("❌ Document not found!");
        }
        else {
            /**
             * Reset lai ID sach ve dung thu tu
             */
            for (int i = tmp; i < documents.size(); i++) {
                String res2 = documents.get(i).getId();
                documents.get(i).setId(idDelete);
                idDelete = res2;
            }
        }
    }
    
    /**
     * Update document information============================================================
     */
    public void updateDocument(Scanner scanner) {
        displayAllDocuments(scanner);
        System.out.print("Enter the ID you want to update: ");
        String id = readFormattedId(scanner);
        
        for (Document doc : documents) {
            if (doc.getId().equals(id)) {
                System.out.println("Enter new information:");
                System.out.print("-Enter new title: ");
                doc.setTitle(scanner.nextLine());
                System.out.print("-Enter new author: ");
                doc.setAuthor(scanner.nextLine());
                System.out.print("-Enter new publisher: ");
                doc.setPublisher(scanner.nextLine());
                System.out.print("-Enter new category: ");
                doc.setCategory(scanner.nextLine());
                System.out.print("-Enter new year: ");
                doc.setYear(scanner.nextInt());
                System.out.print("-Enter new quantity: ");
                doc.setNumbers(scanner.nextInt());
                scanner.nextLine(); // consume newline
                
                doc.setAvailable(doc.getNumbers() > 0);
                System.out.println("✅ Document updated successfully!");
                return;
            }
        }
        System.out.println("❌ Document not found!");
    }
    
    /**
     * Search documents=======================================================================================
     */
    public void findDocument(Scanner scanner){
        System.out.println("Search document by:\n1. Title\n2. Author\n3. Category");
        System.out.print("Choose option number: ");
        int tmp ;
        do {
            tmp = readScannerInt(scanner,"Choose option number: ");
            if(tmp < 1 || tmp > 3){
                System.out.println("You just enter: 1 - 3");
            }
        }while(tmp < 1 || tmp > 3);
            switch (tmp) {
            case dfDocument.TITLE:
                System.out.print("Enter title to search: ");
                String title = scanner.nextLine();
                boolean found1 = false;
                for (Document doc : documents) {
                    if (doc.getTitle().toLowerCase().contains(title.toLowerCase())) {
                        doc.printFor(doc);
                        found1 = true;
                    }
                }
                if (!found1) System.out.println("❌ No documents found with that title!");
                break;
                
            case dfDocument.AUTHOR:
                System.out.print("Enter author to search: ");
                String author = scanner.nextLine();
                boolean found2 = false;
                for (Document doc : documents) {
                    if (doc.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                        doc.printFor(doc);
                        found2 = true;
                    }
                }
                if (!found2) System.out.println("❌ No documents found with that author!");
                break;
                
            case dfDocument.CATEGORY:
                System.out.print("Enter category to search: ");
                String category = scanner.nextLine();
                boolean found3 = false;
                for (Document doc : documents) {
                    if (doc.getCategory().toLowerCase().contains(category.toLowerCase())) {
                        doc.printFor(doc);
                        found3 = true;
                    }
                }
                if (!found3) System.out.println("❌ No documents found in that category!");
                break;
            default:
                System.out.println("❌ Invalid choice!");
                break;
        }
    }
    
    /**
     * Display all documents=================================================================================
     */
    public void displayAllDocuments(Scanner scanner) {
        if (documents.isEmpty()) {
            System.out.println("❌ No documents available.");
            return;
        }
        System.out.println("You want to watch:\n1. Book\n2. Magazine\n3. All document");
        System.out.print("Choose option number: ");
        int tmp ;
        do {
            tmp = readScannerInt(scanner,"Choose option number: ");
            if(tmp < 1 || tmp > 3){
                System.out.println("You just enter: 1 - 3");
            }
        }while(tmp < 1 || tmp > 3);
        switch(tmp){
            case dfDocument.BOOK:
                for(Document doc : documents){
                    if(doc instanceof Book){
                        doc.printFor(doc);
                    }
                }
                break;
            case dfDocument.MAGAZINE:
                for(Document doc : documents){
                    if(doc instanceof Magazine){
                        doc.printFor(doc);
                    }
                }
                break;
            case dfDocument.ALLDOC:
                for(Document doc : documents){
                   doc.prinAll();
                }
        }
    }

    
    /**
     * Display all users=========================================================================================
     */
    public void displayAllUsers() {
        if (users.isEmpty()) {
            System.out.println("❌ No users available.");
            return;
        }
        System.out.println("===== ALL USERS =====");
        for (User user : users) {
            System.out.println("ID: " + user.getId() + " | Name: " + user.getName() + 
                             " | Email: " + user.getEmail() + " | Phone: " + user.getPhoneNumber() + 
                             " | Borrowed: " + user.getBorrowedDocuments().size() + "/" + user.getBorrowLimit());
        }
    }
}

/**
 * Nhung van de o phan document:
 * Nhap vao nhung tai lieu trung nhau -xong
 * Khi xoa mot tai lieu thi ID cua cac tai lieu con lai phai thay the thu tu cua ID sach ma minh xoa -xong
 * Nhap vao khong trang -Xong
 * Xay dung cac lop ke thua Book va Magazine -Xong
 * Khi in ra tat ca tai lieu thi se gach bo Magazine va Book -xong
 * Dang tim kiem loi
 */


