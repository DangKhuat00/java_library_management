package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class HighlightRenderer extends DefaultTableCellRenderer {
    private String keyword;
    private final String context; // dùng để phân biệt panel hay cột nếu cần

    // Constructor
    public HighlightRenderer(String keyword, String context) {
        setKeyword(keyword);
        this.context = context;
    }

    // Cho phép đổi keyword sau khi tạo (hữu ích khi reset)
    public void setKeyword(String keyword) {
        this.keyword = (keyword != null) ? keyword.trim().toLowerCase() : "";
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        // Nếu keyword rỗng hoặc null → không highlight
        if (value != null && keyword != null && !keyword.isEmpty()) {
            String cellText = value.toString();
            String lowerText = cellText.toLowerCase();
            int index = lowerText.indexOf(keyword);

            if (index >= 0) {
                // Highlight phần trùng → nền vàng + in đậm
                String before = cellText.substring(0, index);
                String match = cellText.substring(index, index + keyword.length());
                String after = cellText.substring(index + keyword.length());

                label.setText("<html>" + escapeHtml(before)
                        + "<span style='background-color:yellow; font-weight:bold;'>" + escapeHtml(match) + "</span>"
                        + escapeHtml(after) + "</html>");
            } else {
                label.setText(cellText); // Không match → hiển thị bình thường
            }
        } else if (value != null) {
            label.setText(value.toString()); // Không highlight nếu keyword trống
        }

        return label;
    }

    // Escape ký tự HTML đặc biệt
    private String escapeHtml(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
