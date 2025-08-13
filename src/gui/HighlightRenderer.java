package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Renderer dùng để highlight từ khóa trong JTable.
 * Hỗ trợ tìm kiếm bỏ dấu (ví dụ: "e" khớp "é, è, ê...").
 * Nếu context = "All Fields" thì highlight ở tất cả các cột.
 * Nếu context = tên cột (Id, Title, Author...), chỉ highlight ở cột đó.
 */
public class HighlightRenderer extends DefaultTableCellRenderer {
    private String keyword;
    private final String context;

    public HighlightRenderer(String keyword, String context) {
        setKeyword(keyword);
        this.context = context;
    }

    public void setKeyword(String keyword) {
        this.keyword = (keyword != null) ? keyword.trim().toLowerCase() : "";
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
                

        String columnName = table.getColumnName(column);

        // Nếu context không phải ALL_FIELDS và không trùng tên cột → không highlight
        if (!"All Fields".equalsIgnoreCase(context) &&
            !columnName.equalsIgnoreCase(context)) {
            label.setText(value != null ? value.toString() : "");
            return label;
        }

        // Nếu keyword rỗng hoặc value null → hiển thị bình thường
        if (value != null && keyword != null && !keyword.isEmpty()) {
            String cellText = value.toString();
            String cellTextNoAccent = removeDiacritics(cellText).toLowerCase();
            String keywordNoAccent = removeDiacritics(keyword).toLowerCase();

            int index = cellTextNoAccent.indexOf(keywordNoAccent);

            if (index >= 0) {
                // Tìm đúng vị trí gốc để highlight
                String before = cellText.substring(0, index);
                String match = cellText.substring(index, index + keyword.length());
                String after = cellText.substring(index + keyword.length());

                label.setText("<html>" + escapeHtml(before)
                        + "<span style='background-color:yellow; font-weight:bold;'>" + escapeHtml(match) + "</span>"
                        + escapeHtml(after) + "</html>");
            } else {
                label.setText(cellText);
            }
        } else if (value != null) {
            label.setText(value.toString());
        }

        return label;
    }

    // Hàm bỏ dấu tiếng Việt
    private String removeDiacritics(String s) {
        String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

    // Escape ký tự HTML đặc biệt
    private String escapeHtml(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
