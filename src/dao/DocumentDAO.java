
// Goi package dao
package dao;

// Import cac thu vien can thiet
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

    /**
     * Tim tai lieu theo tu khoa o tieu de hoac tac gia
     */
    // Ham tim kiem tai lieu theo tu khoa
    public List<Document> findDocument(String keyword) {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM documents WHERE title LIKE ? OR author LIKE ? OR language LIKE ? OR CAST(pages AS CHAR) LIKE ? OR CAST(publication_year AS CHAR) LIKE ? ORDER BY title";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            stmt.setString(5, searchPattern);

            ResultSet rs = stmt.executeQuery();
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
            System.err.println("Error searching documents: " + e.getMessage());
        }

        return documents;
    };

    /**
     * Tim tai lieu theo mot truong va tu khoa cu the
     */
    // Ham tim kiem tai lieu theo truong va tu khoa
    public List<Document> findDocumentsByField(String field, String keyword) {
        List<Document> list = new ArrayList<>(); // Danh sach ket qua
        // Luu y: Noi chuoi truc tiep vao SQL co the gay ra SQL Injection
        // Tuy nhien, truong hop nay field lay tu JComboBox nen it rui ro hon
        String sql = "SELECT * FROM documents WHERE " + field + " LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Gan tu khoa tim kiem vao cau lenh
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Document doc = new Document(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("language"),
                        rs.getInt("pages"),
                        rs.getString("author"),
                        rs.getInt("publication_year"),
                        rs.getBoolean("is_available"));
                list.add(doc);
            }
        } catch (SQLException e) {
            // In ra loi neu co loi khi tim kiem
            e.printStackTrace();
        }
        return list;
    }
}