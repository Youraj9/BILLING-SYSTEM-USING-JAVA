import javax.swing.*;
import java.awt.*;

public class Mainpage {
    private JFrame frame;

    public Mainpage() {
        // Frame setup
        frame = new JFrame("Supermarket Billing System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(54, 33, 89));
        JLabel headerLabel = new JLabel("Supermarket Billing System", JLabel.CENTER);
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel);

        // Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(4, 1, 10, 10));
        centerPanel.setBackground(new Color(240, 240, 240));
        JLabel welcomeLabel = new JLabel("Welcome! Please choose your login type.", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        JButton adminLoginButton = new JButton("Admin Login");
        JButton userLoginButton = new JButton("User Login");
        JButton registrationButton = new JButton("For Registration Click Here ....");

        styleButton(adminLoginButton);
        styleButton(userLoginButton);
        styleButton(registrationButton);

        centerPanel.add(welcomeLabel);
        centerPanel.add(adminLoginButton);
        centerPanel.add(userLoginButton);
        centerPanel.add(registrationButton);

        // Footer panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(64, 43, 100));
        JLabel footerLabel = new JLabel("Developed By :RASHEL SARKAR  ", JLabel.CENTER);
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerPanel.add(footerLabel);

        // Add panels to frame
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(footerPanel, BorderLayout.SOUTH);

        // Button actions
        adminLoginButton.addActionListener(e -> {
            frame.dispose(); // Close current frame
            new LoginPage(); // Open LoginPage
        });

        userLoginButton.addActionListener(e -> {
            frame.dispose(); // Close current frame
            new LoginPage(); // Open LoginPage
        });

        registrationButton.addActionListener(e -> {
            frame.dispose(); // Close current frame
            new RegistrationPage(); // Open RegistrationPage
        });

        // Display frame
        frame.setVisible(true);
    }

    // Helper method to style buttons
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(85, 65, 118));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Mainpage::new);
    }
}
