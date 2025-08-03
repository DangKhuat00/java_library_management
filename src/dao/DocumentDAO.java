package dao;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentDAO {

    public boolean insertDocument(Document document) {
        String sql = "INSERT INTO documents (id, title, author, publication_year, document_type, number_of_pages, issue_number) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, document.getId());
            stmt.setString(2, document.getTitle());
            stmt.setString(3, document.getAuthor());
            stmt.setInt(4, document.getYear());
            stmt.setString(5, document.getDocumentType().name());

            if (document instanceof Book book) {
                stmt.setInt(6, book.getNumberOfPages());
                stmt.setNull(7, Types.INTEGER);
            } else if (document instanceof Magazine magazine) {
                stmt.setNull(6, Types.INTEGER);
                stmt.setInt(7, magazine.getIssueNumber());
            } else {
                stmt.setNull(6, Types.INTEGER);
                stmt.setNull(7, Types.INTEGER);
            }

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting document: " + e.getMessage());
            return false;
        }
    }

    public List<Document> getAllDocuments() {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM documents ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Document doc = createDocumentFromResultSet(rs);
                if (doc != null) {
                    documents.add(doc);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving documents: " + e.getMessage());
        }

        return documents;
    }

    public Document findDocumentById(String id) {
        String sql = "SELECT * FROM documents WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return createDocumentFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding document: " + e.getMessage());
        }

        return null;
    }

    public boolean updateDocument(Document document) {
        String sql = "UPDATE documents SET title = ?, author = ?, publication_year = ?, number_of_pages = ?, issue_number = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, document.getTitle());
            stmt.setString(2, document.getAuthor());
            stmt.setInt(3, document.getYear());

            if (document instanceof Book book) {
                stmt.setInt(4, book.getNumberOfPages());
                stmt.setNull(5, Types.INTEGER);
            } else if (document instanceof Magazine magazine) {
                stmt.setNull(4, Types.INTEGER);
                stmt.setInt(5, magazine.getIssueNumber());
            } else {
                stmt.setNull(4, Types.INTEGER);
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.setString(6, document.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating document: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteDocument(String id) {
        String sql = "DELETE FROM documents WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting document: " + e.getMessage());
            return false;
        }
    }

    public List<Document> searchDocuments(String keyword) {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM documents WHERE title LIKE ? OR author LIKE ? ORDER BY title";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Document doc = createDocumentFromResultSet(rs);
                if (doc != null) {
                    documents.add(doc);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error searching documents: " + e.getMessage());
        }

        return documents;
    }

    /*
     * Chuyen 1 dong trong bang thanh 1 doi tuong
     */
    private Document createDocumentFromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String title = rs.getString("title");
        String author = rs.getString("author");
        int year = rs.getInt("publication_year");
        String typeStr = rs.getString("document_type");
        DocumentType type = DocumentType.valueOf(typeStr);

        return switch (type) {
            case BOOK -> new Book(id, title, author, year, rs.getInt("number_of_pages"));
            case MAGAZINE -> new Magazine(id, title, author, year, rs.getInt("issue_number"));
            default -> throw new IllegalArgumentException("Unknown document type: " + type);
        };
    }
}
