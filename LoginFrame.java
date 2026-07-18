import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passField;
    private ModernButton loginButton;
    private JButton signupButton;

    public LoginFrame() {
        setTitle("University Portal - Login");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel container = new JPanel(new GridLayout(1, 2));
        
        // Left Branding Panel
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(AppTheme.PRIMARY);
        JLabel welcomeLabel = new JLabel("<html><div style='text-align: center;'><b style='font-size:30px;'>WELCOME</b><br><br>University Admission<br>Management System</div></html>", SwingConstants.CENTER);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(AppTheme.LABEL_FONT);
        leftPanel.add(welcomeLabel);

        // Right Login Panel
        JPanel rightPanel = new JPanel(null);
        rightPanel.setBackground(AppTheme.BACKGROUND);
        
        JLabel loginTitle = new JLabel("Member Login");
        loginTitle.setFont(AppTheme.TITLE_FONT);
        loginTitle.setBounds(50, 60, 300, 40);
        loginTitle.setForeground(AppTheme.TEXT_DARK);

        JLabel emailLabel = new JLabel("EMAIL");
        emailLabel.setBounds(50, 130, 200, 20);
        emailLabel.setFont(AppTheme.LABEL_FONT);
        
        emailField = new JTextField();
        emailField.setBounds(50, 155, 300, 40);
        emailField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, AppTheme.PRIMARY));

        JLabel passLabel = new JLabel("PASSWORD");
        passLabel.setBounds(50, 215, 200, 20);
        passLabel.setFont(AppTheme.LABEL_FONT);
        
        passField = new JPasswordField();
        passField.setBounds(50, 240, 300, 40);
        passField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, AppTheme.PRIMARY));

        loginButton = new ModernButton("LOGIN NOW", AppTheme.PRIMARY, AppTheme.PRIMARY_HOVER);
        loginButton.setBounds(50, 310, 300, 50);

        signupButton = new JButton("Create New Account ?");
        signupButton.setBounds(50, 370, 300, 30);
        signupButton.setBorderPainted(false);
        signupButton.setContentAreaFilled(false);
        signupButton.setForeground(AppTheme.TEXT_LIGHT);
        signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        rightPanel.add(loginTitle);
        rightPanel.add(emailLabel); rightPanel.add(emailField);
        rightPanel.add(passLabel); rightPanel.add(passField);
        rightPanel.add(loginButton); rightPanel.add(signupButton);

        container.add(leftPanel); container.add(rightPanel);
        add(container);

        loginButton.addActionListener(e -> login());
        signupButton.addActionListener(e -> { new SignupFrame().setVisible(true); dispose(); });
    }

    private void login() {
        String email = emailField.getText();
        String pass = new String(passField.getPassword());

        // Basic validation
        if (email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        // Hardcoded admin check (Consider moving to DB for production)
        if (email.equalsIgnoreCase("admin@university.com") && pass.equals("admin123")) {
            new AdminDashboardFrame().setVisible(true);
            dispose();
            return;
        }

        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT id FROM users WHERE email = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email); pstmt.setString(2, pass);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("id");
                new AdmissionFrame(userId).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Connection Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}     
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
