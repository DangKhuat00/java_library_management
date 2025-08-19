package dao;

// Import cac thu vien can thiet
import model.DocumentFilter;
import model.Document;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lop xu ly cac thao tac voi tai lieu
 * Xu ly them, sua, xoa, tim kiem tai lieu trong co so du lieu
 */
public class DocumentDAO {

    /**
     * Them mot tai lieu moi vao co so du lieu
     * Cot is_available se tu dong duoc gan gia tri TRUE theo mac dinh cua DB
     * 
     * @param document doi tuong Document can them
     * @return true neu them thanh cong
     */
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
     * 
     * @return danh sach Document
     */
    public List<Document> getAllDocuments() {
        List<Document> documents = new ArrayList<>(); // Danh sach luu tai lieu
        String sql = "SELECT * FROM documents ORDER BY id DESC"; // Lấy tất cả, sắp xếp theo ID giảm dần

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
     * 
     * @param document doi tuong Document can cap nhat
     * @return true neu cap nhat thanh cong
     */
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
     * 
     * @param id ma tai lieu can xoa
     * @return true neu xoa thanh cong
     */
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

    /**
     * Lay tai lieu theo ID
     * 
     * @param id ma tai lieu can tim
     * @return doi tuong Document neu tim thay, null neu khong tim thay
     */
    public Document getDocumentById(int id) {
        String sql = "SELECT * FROM documents WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Gan gia tri id vao cau lenh
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Tao doi tuong Document tu ket qua truy van
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
            // In ra loi neu co loi khi lay theo ID
            System.err.println("Error getting document by ID: " + e.getMessage());
        }

        // Tra ve null neu khong tim thay
        return null;
    }

    /**
     * Tim kiem tai lieu theo tu khoa va bo loc
     * 
     * @param keyword tu khoa tim kiem
     * @param filter  bo loc tim kiem (theo truong nao)
     * @return danh sach Document phu hop
     */
    public List<Document> searchDocuments(String keyword, DocumentFilter filter) {
        List<Document> documents = new ArrayList<>(); // Danh sach ket qua
        String sql;

        // Xay dung cau lenh SQL theo bo loc
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
            case ALL_FIELDS:
            default:
                // Tim kiem tren tat ca cac truong
                sql = "SELECT * FROM documents " +
                        "WHERE CAST(id AS CHAR) LIKE ? " +
                        "   OR LOWER(title) LIKE ? " +
                        "   OR LOWER(author) LIKE ? " +
                        "   OR LOWER(language) LIKE ? " +
                        "   OR CAST(pages AS CHAR) LIKE ? " +
                        "   OR CAST(publication_year AS CHAR) LIKE ?";
                break;
        }

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Tao pattern tim kiem
            String like = "%" + keyword.toLowerCase() + "%";

            if (filter == DocumentFilter.ALL_FIELDS) {
                // Gan pattern cho tat ca cac truong
                pstmt.setString(1, like); // id
                pstmt.setString(2, like); // title
                pstmt.setString(3, like); // author
                pstmt.setString(4, like); // language
                pstmt.setString(5, like); // pages
                pstmt.setString(6, like); // publication_year
            } else {
                // Gan pattern cho mot truong cu the
                pstmt.setString(1, like);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                // Duyet ket qua va tao danh sach Document
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
            // In ra loi neu co loi khi tim kiem
            e.printStackTrace();
        }

        return documents;
    }
}