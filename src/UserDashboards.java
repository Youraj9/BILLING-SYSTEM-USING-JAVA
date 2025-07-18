import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.sql.*;

public class UserDashboards {
    private JFrame frame;
    private JTable itemsTable;
    private DefaultTableModel cartModel;

    public UserDashboards(String username) {
        frame = new JFrame("User Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        itemsTable = new JTable();
        cartModel = new DefaultTableModel(new Object[] { "Name", "Quantity", "Price" }, 0);
        JTable cartTable = new JTable(cartModel);

        JScrollPane itemsScrollPane = new JScrollPane(itemsTable);
        JScrollPane cartScrollPane = new JScrollPane(cartTable);

        JButton addToCartButton = new JButton("Add to Cart");
        JButton deleteFromCartButton = new JButton("Delete from Cart");
        JButton purchaseButton = new JButton("Purchase");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addToCartButton);
        buttonPanel.add(deleteFromCartButton);
        buttonPanel.add(purchaseButton);

        mainPanel.add(itemsScrollPane);
        mainPanel.add(cartScrollPane);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        fetchItems();
        addToCartButton.addActionListener(e -> addToCart());
        deleteFromCartButton.addActionListener(e -> deleteFromCart());
        purchaseButton.addActionListener(e -> purchaseItems(username));

        frame.setVisible(true);
    }

    private void fetchItems() {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/supermarket", "root", "giganigga");
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT name, quantity, price FROM items")) {

            DefaultTableModel model = new DefaultTableModel(
                    new Object[] { "Name", "Quantity", "Price" }, 0);
            while (rs.next()) {
                model.addRow(new Object[] { rs.getString("name"), rs.getInt("quantity"),
                        rs.getDouble("price") });
            }
            itemsTable.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Database Error: " + ex.getMessage());
        }
    }

    private void addToCart() {
        int selectedRow = itemsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Select an item to add to the cart.");
            return;
        }

        String name = (String) itemsTable.getValueAt(selectedRow, 0);
        int availableQuantity = (int) itemsTable.getValueAt(selectedRow, 1);
        if (availableQuantity == 0) {
            JOptionPane.showMessageDialog(frame, "This item is out of stock!");
            return;
        }

        String quantityInput = JOptionPane.showInputDialog("Enter quantity:");
        if (quantityInput == null || quantityInput.isEmpty()) {
            return;
        }
        int quantity = Integer.parseInt(quantityInput);

        if (quantity < 1) {
            JOptionPane.showMessageDialog(frame, "Select at least 1 quantity.");
            return;
        }

        if (quantity > availableQuantity) {
            JOptionPane.showMessageDialog(frame, "Insufficient stock! Available: " + availableQuantity);
            return;
        }

        double price = (double) itemsTable.getValueAt(selectedRow, 2);
        cartModel.addRow(new Object[] { name, quantity, price });

        // Update the table display
        itemsTable.setValueAt(availableQuantity - quantity, selectedRow, 1);
    }

    private void deleteFromCart() {
        int selectedRow = cartModel.getRowCount() - 1;
        if (selectedRow != -1) {
            cartModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(frame, "Select an item to delete from the cart.");
        }
    }

    private void purchaseItems(String username) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/supermarket", "root", "giganigga")) {

            // Step 1: Ask for customer details
            String customerName = JOptionPane.showInputDialog(frame, "Enter Customer Name:");
            if (customerName == null || customerName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Customer name is required!");
                return;
            }

            String numberInput = JOptionPane.showInputDialog(frame, "Enter Customer Number:");
            if (numberInput == null || numberInput.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Customer number is required!");
                return;
            }

            int customerNumber;
            try {
                customerNumber = Integer.parseInt(numberInput);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid number!");
                return;
            }

            // Step 2: Save customer info to DB
            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO customer (username, number) VALUES (?, ?)")) {
                stmt.setString(1, customerName);
                stmt.setInt(2, customerNumber);
                stmt.executeUpdate();
            }

            // Step 3: Generate receipt
            double total = 0, cgstTotal = 0, sgstTotal = 0;
            StringBuilder receipt = new StringBuilder("Receipt:\n");
            receipt.append("--------------------------------------------------\n");
            receipt.append("Customer: ").append(customerName).append("\n");
            receipt.append("Customer number:").append(customerNumber).append("\n");
            receipt.append("Salesperson: ").append(username).append("\n");
            // receipt.append("Customer: ").append(customerName).append(" (");
            receipt.append("Date: ").append(new java.util.Date()).append("\n");
            receipt.append("--------------------------------------------------\n");
            receipt.append("Item                Qty       Price      Total\n");

            for (int i = 0; i < cartModel.getRowCount(); i++) {
                String name = (String) cartModel.getValueAt(i, 0);
                int quantity = (int) cartModel.getValueAt(i, 1);
                double price = (double) cartModel.getValueAt(i, 2);

                double itemTotal = quantity * price;
                double cgst = itemTotal * 0.18;
                double sgst = itemTotal * 0.05;

                total += itemTotal;
                cgstTotal += cgst;
                sgstTotal += sgst;

                receipt.append(String.format("%-20s %-8d %-10.2f %.2f\n", name, quantity, price, itemTotal));

                try (PreparedStatement stmt = connection
                        .prepareStatement("UPDATE items SET quantity = quantity - ? WHERE name = ?")) {
                    stmt.setInt(1, quantity);
                    stmt.setString(2, name);
                    stmt.executeUpdate();
                }
            }

            double grandTotal = total + cgstTotal + sgstTotal;
            receipt.append("--------------------------------------------------\n");
            receipt.append(String.format("CGST (18%%):                          ₹%.2f\n", cgstTotal));
            receipt.append(String.format("SGST (5%%):                           ₹%.2f\n", sgstTotal));
            receipt.append(String.format("Grand Total:                         ₹%.2f\n", grandTotal));
            receipt.append("--------------------------------------------------\n");
            receipt.append("Thank you for shopping with us!\n");

            try (PreparedStatement stmt = connection
                    .prepareStatement("INSERT into orders (user, total_amount, cgst, sgst) VALUES (?, ?, ?, ?)")) {
                stmt.setString(1, username);
                stmt.setDouble(2, total);
                stmt.setDouble(3, cgstTotal);
                stmt.setDouble(4, sgstTotal);
                stmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(frame, receipt.toString(), "Receipt", JOptionPane.INFORMATION_MESSAGE);

            saveReceiptToFile(receipt.toString(), username);

            cartModel.setRowCount(0);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error completing purchase: " + ex.getMessage());
        }
    }

    private void saveReceiptToFile(String receiptContent, String username) {
        try {
            String fileName = "Receipt_" + username + "_" + System.currentTimeMillis() + ".txt";
            FileWriter writer = new FileWriter(fileName);
            writer.write(receiptContent);
            writer.close();
            JOptionPane.showMessageDialog(frame, "Receipt saved to file: " + fileName, "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error saving receipt to file: " + e.getMessage());
        }
    }
}
