import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginPage {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        frame = new JFrame("Supermarket Billing System - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(4, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(new JLabel()); // Empty placeholder
        frame.add(loginButton);

        loginButton.addActionListener(new LoginAction());

        frame.setVisible(true);
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try (Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/supermarket", "root", "giganigga");
                    PreparedStatement stmt = connection.prepareStatement(
                            "SELECT role FROM users WHERE username = ? AND password = ?")) {

                stmt.setString(1, username);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String role = rs.getString("role");
                    frame.dispose(); // Close the login page
                    if (role.equalsIgnoreCase("admin")) {
                        new AdminSelectionPage(); // Show admin selection page
                    } else if (role.equalsIgnoreCase("Salesperson") || role.equalsIgnoreCase("USER(SALESPERSON)")
                            || role.equalsIgnoreCase("user")) {
                        new UserDashboards(username); // Show user dashboard
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid Username or Password!");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Database Error: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}
