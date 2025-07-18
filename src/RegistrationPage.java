import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegistrationPage {
    private JFrame frame;
    private JTextField usernameField;
    private JComboBox<String> roleBox;
    private JPasswordField passwordField;

    public RegistrationPage() {
        // Frame setup
        frame = new JFrame("User Registration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(5, 2, 10, 10));

        // Form fields
        JLabel usernameLabel = new JLabel("Username:");
        JLabel roleLabel = new JLabel("Role:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField();
        String[] roles = { "USER(SALESPERSON)", "ADMIN" };
        roleBox = new JComboBox<>(roles);
        passwordField = new JPasswordField();

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(85, 65, 118));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));

        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(roleLabel);
        frame.add(roleBox);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(new JLabel()); // Empty placeholder
        frame.add(registerButton);

        // Button action
        registerButton.addActionListener(e -> registerUser());

        frame.setVisible(true);
    }

    private void registerUser() {
        String username = usernameField.getText();
        String role = (String) roleBox.getSelectedItem();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields are required!");
            return;
        }

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/supermarket", "root", "giganigga");
                PreparedStatement checkStmt = connection.prepareStatement(
                        "SELECT COUNT(*) FROM users WHERE username = ?");
                PreparedStatement insertStmt = connection.prepareStatement(
                        "INSERT INTO users (username, role, password) VALUES (?, ?, ?)")) {

            // Check if username already exists
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(frame, "USERNAME TAKEN");
                return;
            }

            // Insert new user
            insertStmt.setString(1, username);
            insertStmt.setString(2, role);
            insertStmt.setString(3, password);
            insertStmt.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Successfully Registered!");
            frame.dispose();
            new Mainpage(); // Redirect to Mainpage
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Database Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegistrationPage::new);
    }
}
