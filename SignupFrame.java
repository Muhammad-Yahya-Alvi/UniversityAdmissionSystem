import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SignupFrame extends JFrame {
    private JTextField nameField, emailField;
    private JPasswordField passField;
    private ModernButton signupButton;
    private JButton backButton;

    public SignupFrame() {
        setTitle("University Portal - Register");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel container = new JPanel(new GridLayout(1, 2));

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(new Color(39, 174, 96));
        JLabel info = new JLabel("<html><div style='text-align: center;'><b style='font-size:24px;'>REGISTRATION</b><br><br>Join our community today!</div></html>", SwingConstants.CENTER);
        info.setForeground(Color.WHITE);
        leftPanel.add(info);

        JPanel rightPanel = new JPanel(null);
        rightPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Sign Up");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setBounds(50, 40, 200, 40);

        JLabel nL = new JLabel("FULL NAME"); nL.setBounds(50, 100, 200, 20);
        nameField = new JTextField(); nameField.setBounds(50, 125, 300, 35);
        nameField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(39, 174, 96)));

        JLabel eL = new JLabel("EMAIL"); eL.setBounds(50, 180, 200, 20);
        emailField = new JTextField(); emailField.setBounds(50, 205, 300, 35);
        emailField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(39, 174, 96)));

        JLabel pL = new JLabel("PASSWORD"); pL.setBounds(50, 260, 200, 20);
        passField = new JPasswordField(); passField.setBounds(50, 285, 300, 35);
        passField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(39, 174, 96)));

        signupButton = new ModernButton("CREATE ACCOUNT", new Color(39, 174, 96), new Color(46, 204, 113));
        signupButton.setBounds(50, 345, 300, 50);

        backButton = new JButton("? Back to Login");
        backButton.setBounds(50, 400, 300, 30);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setForeground(new Color(127, 140, 141));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        rightPanel.add(title);
        rightPanel.add(nL); rightPanel.add(nameField);
        rightPanel.add(eL); rightPanel.add(emailField);
        rightPanel.add(pL); rightPanel.add(passField);
        rightPanel.add(signupButton); rightPanel.add(backButton);

        container.add(leftPanel); container.add(rightPanel);
        add(container);

        signupButton.addActionListener(e -> register());
        backButton.addActionListener(e -> { new LoginFrame().setVisible(true); dispose(); });
    }

    private void register() {
        String name = nameField.getText();
        String email = emailField.getText();
        String pass = new String(passField.getPassword());
        if(name.isEmpty() || email.isEmpty() || pass.isEmpty()) return;
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "INSERT INTO users (full_name, email, password) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name); pstmt.setString(2, email); pstmt.setString(3, pass);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Welcome to the University!");
            new LoginFrame().setVisible(true); dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
