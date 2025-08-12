
// Goi package dao
package dao;

// Import cac thu vien can thiet
import model.DocumentFilter;
import model.Document;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Lop xu ly cac thao tac voi tai lieu
public class DocumentDAO {

    /**
     * Them mot tai lieu moi vao co so du lieu
     * Cot is_available se tu dong duoc gan gia tri TRUE theo mac dinh cua DB
     */
    // Ham them tai lieu moi
    public boolean insertDocument(Document document) {
        // Khong can chen is_available vi DB tu gan TRUE
        String sql = "INSERT INTO documents (title, language, pages, author, publication_year) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Gan gia tri cho cac truong
            stmt.setString(1, document.getTitle());
            stmt.setString(2, document.getLanguage());
            stmt.setInt(3, document.getPages());
            stmt.setString(4, document.getAuthor());
            stmt.setInt(5, document.getPublicationYear());

            // Tra ve true neu chen thanh cong
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            // In ra loi neu co loi khi chen
            System.err.println("Error inserting document: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lay tat ca tai lieu tu co so du lieu
     */
    // Ham lay tat ca tai lieu
    public List<Document> getAllDocuments() {
        List<Document> documents = new ArrayList<>(); // Danh sach luu tai lieu
        String sql = "SELECT * FROM documents ORDER BY id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // Duyet tung dong va tao doi tuong Document
            while (rs.next()) {
                Document doc = new Document(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("language"),
                        rs.getInt("pages"),
                        rs.getString("author"),
                        rs.getInt("publication_year"),
                        rs.getBoolean("is_available"));
                documents.add(doc);
            }

        } catch (SQLException e) {
            // In ra loi neu co loi khi lay du lieu
            System.err.println("Error retrieving documents: " + e.getMessage());
        }

        return documents;
    }

    /**
     * Cap nhat thong tin mot tai lieu, bao gom ca trang thai co san
     */
    // Ham cap nhat tai lieu
    public boolean updateDocument(Document document) {
        String sql = "UPDATE documents SET title = ?, language = ?, pages = ?, author = ?, publication_year = ?, is_available = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Gan gia tri cho cac truong
            stmt.setString(1, document.getTitle());
            stmt.setString(2, document.getLanguage());
            stmt.setInt(3, document.getPages());
            stmt.setString(4, document.getAuthor());
            stmt.setInt(5, document.getPublicationYear());
            stmt.setBoolean(6, document.isAvailable());
            stmt.setInt(7, document.getId());

            // Tra ve true neu cap nhat thanh cong
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // In ra loi neu co loi khi cap nhat
            System.err.println("Error updating document: " + e.getMessage());
            return false;
        }
    }

    /**
     * Xoa mot tai lieu khoi co so du lieu
     */
    // Ham xoa tai lieu
    public boolean deleteDocument(int id) {
        String sql = "DELETE FROM documents WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Gan id va xoa
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // In ra loi neu co loi khi xoa
            System.err.println("Error deleting document: " + e.getMessage());
            return false;
        }
    }

    public Document getDocumentById(int id) {
        String sql = "SELECT * FROM documents WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id); // Gán giá trị id

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Document(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("language"),
                            rs.getInt("pages"),
                            rs.getString("author"),
                            rs.getInt("publication_year"),
                            rs.getBoolean("is_available"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error getting document by ID: " + e.getMessage());
        }

        return null;
    }

    public List<Document> searchDocuments(String keyword, DocumentFilter filter) {
        List<Document> documents = new ArrayList<>();
        String sql;

        switch (filter) {
            case ID:
                sql = "SELECT * FROM documents WHERE CAST(id AS CHAR) LIKE ?";
                break;
            case TITLE:
                sql = "SELECT * FROM documents WHERE LOWER(title) LIKE ?";
                break;
            case AUTHOR:
                sql = "SELECT * FROM documents WHERE LOWER(author) LIKE ?";
                break;
            case LANGUAGE:
                sql = "SELECT * FROM documents WHERE LOWER(language) LIKE ?";
                break;
            case PAGES:
                sql = "SELECT * FROM documents WHERE CAST(pages AS CHAR) LIKE ?";
                break;
            case PUBLICATION_YEAR:
                sql = "SELECT * FROM documents WHERE CAST(publication_year AS CHAR) LIKE ?";
                break;
            case IS_AVAILABLE:
                sql = "SELECT * FROM documents WHERE is_available = ?";
                break;
            case ALL_FIELDS:
            default:
                sql = """
                        SELECT * FROM documents
                        WHERE CAST(id AS CHAR) LIKE ?
                           OR LOWER(title) LIKE ?
                           OR LOWER(author) LIKE ?
                           OR LOWER(language) LIKE ?
                           OR CAST(pages AS CHAR) LIKE ?
                           OR CAST(publication_year AS CHAR) LIKE ?
                           OR is_available = ?
                        """;
                break;
        }

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String like = "%" + keyword.toLowerCase() + "%";

            if (filter == DocumentFilter.ALL_FIELDS) {
                boolean availableMatch = startsWithIgnoreCase("true", keyword)
                        || startsWithIgnoreCase("available", keyword);
                pstmt.setString(1, like); // id
                pstmt.setString(2, like); // title
                pstmt.setString(3, like); // author
                pstmt.setString(4, like); // language
                pstmt.setString(5, like); // pages
                pstmt.setString(6, like); // publication_year
                pstmt.setBoolean(7, availableMatch);
            } else if (filter == DocumentFilter.IS_AVAILABLE) {
                boolean availableMatch = startsWithIgnoreCase("true", keyword)
                        || startsWithIgnoreCase("available", keyword);
                pstmt.setBoolean(1, availableMatch);
            } else {
                pstmt.setString(1, like);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    documents.add(new Document(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("language"),
                            rs.getInt("pages"),
                            rs.getString("author"),
                            rs.getInt("publication_year"),
                            rs.getBoolean("is_available")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return documents;
    }

    // Hàm helper để so sánh bắt đầu bằng (không phân biệt hoa thường)
    private boolean startsWithIgnoreCase(String target, String input) {
        return target.toLowerCase().startsWith(input.toLowerCase());
    }

}