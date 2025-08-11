package dao;

import model.Document;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentDAO {

    /**
     * Thêm một tài liệu mới vào cơ sở dữ liệu.
     * Cột is_available sẽ tự động được gán giá trị TRUE theo mặc định của DB.
     */
    public boolean insertDocument(Document document) {
        // Bỏ is_available vì DB sẽ tự gán giá trị mặc định là TRUE
        String sql = "INSERT INTO documents (title, language, pages, author, publication_year) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, document.getTitle());
            stmt.setString(2, document.getLanguage());
            stmt.setInt(3, document.getPages());
            stmt.setString(4, document.getAuthor());
            stmt.setInt(5, document.getPublicationYear());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting document: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lấy tất cả tài liệu từ cơ sở dữ liệu.
     */
    public List<Document> getAllDocuments() {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM documents ORDER BY id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Document doc = new Document(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("language"),
                    rs.getInt("pages"),
                    rs.getString("author"),
                    rs.getInt("publication_year"),
                    rs.getBoolean("is_available") // Thay đổi ở đây
                );
                documents.add(doc);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving documents: " + e.getMessage());
        }

        return documents;
    }

    /**
     * Cập nhật thông tin một tài liệu, bao gồm cả trạng thái có sẵn.
     */
    public boolean updateDocument(Document document) {
        String sql = "UPDATE documents SET title = ?, language = ?, pages = ?, author = ?, publication_year = ?, is_available = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, document.getTitle());
            stmt.setString(2, document.getLanguage());
            stmt.setInt(3, document.getPages());
            stmt.setString(4, document.getAuthor());
            stmt.setInt(5, document.getPublicationYear());
            stmt.setBoolean(6, document.isAvailable()); // Thay đổi ở đây
            stmt.setInt(7, document.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating document: " + e.getMessage());
            return false;
        }
    }

    /**
     * Xóa một tài liệu khỏi cơ sở dữ liệu.
     */
    public boolean deleteDocument(int id) {
        String sql = "DELETE FROM documents WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting document: " + e.getMessage());
            return false;
        }
    }

    /**
     * Tìm tài liệu theo từ khóa ở tiêu đề hoặc tác giả.
     */
    public List<Document> findDocument(String keyword) {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM documents WHERE title LIKE ? OR author LIKE ? ORDER BY title";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Document doc = new Document(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("language"),
                    rs.getInt("pages"),
                    rs.getString("author"),
                    rs.getInt("publication_year"),
                    rs.getBoolean("is_available") // Thay đổi ở đây
                );
                documents.add(doc);
            }

        } catch (SQLException e) {
            System.err.println("Error searching documents: " + e.getMessage());
        }

        return documents;
    }

    /**
     * Tìm tài liệu theo một trường và từ khóa cụ thể.
     */
    public List<Document> findDocumentsByField(String field, String keyword) {
        List<Document> list = new ArrayList<>();
        // Lưu ý: Nối chuỗi trực tiếp vào SQL có thể gây ra SQL Injection.
        // Tuy nhiên, trong trường hợp này, `field` được lấy từ JComboBox nên ít rủi ro hơn.
        String sql = "SELECT * FROM documents WHERE " + field + " LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Document doc = new Document(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("language"),
                        rs.getInt("pages"),
                        rs.getString("author"),
                        rs.getInt("publication_year"), // Sửa lỗi: "year" -> "publication_year"
                        rs.getBoolean("is_available") // Thay đổi ở đây
                );
                list.add(doc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}