import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class InventoryPage {
    private JFrame frame;
    private JTable stockTable;

    public InventoryPage() {
        // Frame setup
        frame = new JFrame("STOCK IN SUPERMARKET");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Header with Back Arrow
        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("←");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            frame.dispose();
            new AdminSelectionPage(); // Redirect to Admin Selection Page
        });

        JLabel titleLabel = new JLabel("STOCK IN SUPERMARKET", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(54, 33, 89));

        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        frame.add(headerPanel, BorderLayout.NORTH);

        // Table setup
        stockTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(stockTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        fetchStockData();

        frame.setVisible(true);
    }

    private void fetchStockData() {
        DefaultTableModel model = new DefaultTableModel(new Object[] { "Name", "Quantity", "Price", "Stock Status" },
                0);
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/supermarket", "root", "giganigga");
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT name, quantity, price FROM items")) {

            while (rs.next()) {
                String name = rs.getString("name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                String stockStatus;

                if (quantity < 5) {
                    stockStatus = "NEED STOCK URGENTLY";
                } else if (quantity > 20) {
                    stockStatus = "SUFFICIENT";
                } else {
                    stockStatus = "MODERATE";
                }

                model.addRow(new Object[] { name, quantity, price, stockStatus });
            }

            stockTable.setModel(model);
            applyRowColoring();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Database Error: " + ex.getMessage());
        }
    }

    private void applyRowColoring() {
        // Custom cell renderer to apply color based on stock status
        stockTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String stockStatus = (String) table.getValueAt(row, 3);

                if ("NEED STOCK URGENTLY".equals(stockStatus)) {
                    cell.setBackground(new Color(255, 182, 193)); // Light Red
                } else {
                    cell.setBackground(Color.WHITE); // Default color
                }

                setHorizontalAlignment(CENTER); // Center-align cell content
                return cell;
            }
        });

        stockTable.setRowHeight(30); // Increase row height for better readability
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InventoryPage::new);
    }
}
