package model;

public class Magazine extends Document {
    private int issueNumber;

    public Magazine( String title, String author, int publication_year, int issueNumber) {
        super( title, author, publication_year, DocumentType.MAGAZINE);
        this.issueNumber = issueNumber;
    }

    public Magazine( int id, String title, String author, int publication_year, int issueNumber) {
        super( id, title, author, publication_year, DocumentType.MAGAZINE);
        this.issueNumber = issueNumber;
    }

    @Override
    public void displayInfo() {
        System.out.println("===== MAGAZINE INFORMATION =====");
        System.out.println("ID: " + id);
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Publication_year: " + publication_year);
        System.out.println("Issue No.: " + issueNumber);
        System.out.println("Type: " + documentType);
        System.out.println("Available: " + (isAvailable ? "Yes" : "No"));
        System.out.println("=================================");
    }
    public int getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(int issueNumber) {
        this.issueNumber = issueNumber;
    }
}
