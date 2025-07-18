import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AdminDashboard {
    private JFrame frame;
    private JTable ordersTable;
    // private JLabel totalSalesLabel;

    public AdminDashboard() {
        frame = new JFrame("Supermarket Billing System - Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        // Top Panel with Title and Back Button
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("←");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setFocusPainted(false);
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            frame.dispose();
            new AdminSelectionPage();
        });

        JLabel titleLabel = new JLabel("Admin Dashboard - Daily Transactions", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        frame.add(topPanel, BorderLayout.NORTH);

        // Table Panel
        ordersTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(ordersTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Transactions Today"));
        frame.add(tableScrollPane, BorderLayout.CENTER);

        // Bottom Panel with Total Sales
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // totalSalesLabel = new JLabel("Total Sales Today: ₹0.00");
        // totalSalesLabel.setFont(new Font("Arial", Font.BOLD, 18));
        // bottomPanel.add(totalSalesLabel);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Fetch and Display Orders
        fetchOrders();

        frame.setVisible(true);
    }

    private void fetchOrders() {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/supermarket", "root", "giganigga");
                Statement stmt = connection.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT * FROM orders");

            // Create Table Model
            DefaultTableModel model = new DefaultTableModel(
                    new String[] { "Salesperson", "TOTAL AMOUNT", "CGST", "SGST", "Order Date" }, 0);

            // double totalSales = 0;
            while (rs.next()) {
                String user = rs.getString("user");
                double totalAmount = rs.getDouble("total_amount");
                double cgst = rs.getDouble("cgst");
                double sgst = rs.getDouble("sgst");
                Timestamp orderDate = rs.getTimestamp("order_date");

                model.addRow(new Object[] { user, "₹" + totalAmount, "₹" + cgst, "₹" + sgst, orderDate.toString() });
                // totalSales += totalAmount;
            }

            ordersTable.setModel(model);

            // Customize Table Appearance
            ordersTable.setFont(new Font("Arial", Font.PLAIN, 14));
            ordersTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            ordersTable.setRowHeight(30);
            ordersTable.setSelectionBackground(Color.LIGHT_GRAY);

            // Align Columns Center
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            for (int i = 0; i < ordersTable.getColumnCount(); i++) {
                ordersTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            // Update Total Sales Label
            // totalSalesLabel.setText("Total Sales Today: ₹" + totalSales);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Database Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminDashboard::new);
    }
}
