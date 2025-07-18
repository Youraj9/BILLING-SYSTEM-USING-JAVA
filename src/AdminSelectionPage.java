import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminSelectionPage {
    private JFrame frame;

    public AdminSelectionPage() {
        // Frame setup
        frame = new JFrame("Supermarket System - Admin Operations");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Header
        JLabel titleLabel = new JLabel("Welcome, Admin!", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(54, 33, 89));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JButton dailyTransactionsButton = new JButton("View Daily Transactions");
        JButton inventoryPageButton = new JButton("View Inventory");

        dailyTransactionsButton.setFont(new Font("Arial", Font.BOLD, 18));
        inventoryPageButton.setFont(new Font("Arial", Font.BOLD, 18));

        dailyTransactionsButton.setBackground(new Color(85, 172, 238));
        dailyTransactionsButton.setForeground(Color.WHITE);
        dailyTransactionsButton.setFocusPainted(false);

        inventoryPageButton.setBackground(new Color(85, 172, 238));
        inventoryPageButton.setForeground(Color.WHITE);
        inventoryPageButton.setFocusPainted(false);

        buttonPanel.add(dailyTransactionsButton);
        buttonPanel.add(inventoryPageButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

        // Button Actions
        dailyTransactionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the selection page
                new AdminDashboard(); // Open Admin Dashboard
            }
        });

        inventoryPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the selection page
                new InventoryPage(); // Open Inventory Page
            }
        });

        // Display the frame
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminSelectionPage::new);
    }
}
