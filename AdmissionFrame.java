import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AdmissionFrame extends JFrame {
    private JTextField nameField, fatherField, cnicField, phoneField;
    private JComboBox<String> courseBox;
    private ModernButton saveButton, logoutButton, claimButton;
    private int loggedInUserId;
    private boolean isUpdating = false;

    public AdmissionFrame(int userId) {
        this.loggedInUserId = userId;
        setTitle("University Portal - Admission Form");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Color.WHITE);

        JPanel header = new JPanel();
        header.setBackground(new Color(44, 62, 80));
        header.setBounds(0, 0, 500, 80);
        JLabel title = new JLabel("Admission Application");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.add(title);

        int startY = 110;
        int spacing = 70;
        int xPos = 50, width = 400, height = 35;

        addLabel(mainPanel, "FULL NAME", xPos, startY);
        nameField = addField(mainPanel, xPos, startY + 25, width, height);
        addLabel(mainPanel, "FATHER NAME", xPos, startY += spacing);
        fatherField = addField(mainPanel, xPos, startY + 25, width, height);
        
        addLabel(mainPanel, "CNIC NUMBER (Type to find old data)", xPos, startY += spacing);
        cnicField = addField(mainPanel, xPos, startY + 25, width, height);
        
        claimButton = new ModernButton("FIND MY OLD DATA", new Color(52, 152, 219), new Color(41, 128, 185));
        claimButton.setBounds(300, startY, 150, 20);
        claimButton.setFont(new Font("Segoe UI", Font.BOLD, 10));
        mainPanel.add(claimButton);

        addLabel(mainPanel, "COURSE", xPos, startY += spacing);
        String[] courses = {"CS", "SE", "IT", "AI", "DS"};
        courseBox = new JComboBox<>(courses);
        courseBox.setBounds(xPos, startY + 25, width, height);
        mainPanel.add(courseBox);

        addLabel(mainPanel, "PHONE", xPos, startY += spacing);
        phoneField = addField(mainPanel, xPos, startY + 25, width, height);

        saveButton = new ModernButton("SUBMIT APPLICATION", new Color(39, 174, 96), new Color(46, 204, 113));
        saveButton.setBounds(50, 500, 400, 50);

        logoutButton = new ModernButton("LOGOUT", new Color(231, 76, 60), new Color(192, 57, 43));
        logoutButton.setBounds(50, 560, 400, 40);

        mainPanel.add(header); mainPanel.add(saveButton); mainPanel.add(logoutButton);
        add(mainPanel);

        checkExistingAdmission();

        saveButton.addActionListener(e -> saveOrUpdate());
        logoutButton.addActionListener(e -> { new LoginFrame().setVisible(true); dispose(); });
        claimButton.addActionListener(e -> claimOldData());
    }

    private void addLabel(JPanel p, String text, int x, int y) {
        JLabel l = new JLabel(text); l.setBounds(x, y, 300, 20);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(new Color(127, 140, 141));
        p.add(l);
    }

    private JTextField addField(JPanel p, int x, int y, int w, int h) {
        JTextField f = new JTextField(); f.setBounds(x, y, w, h);
        f.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        p.add(f); return f;
    }

    private void checkExistingAdmission() {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT * FROM admissions WHERE user_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, loggedInUserId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                fillData(rs);
                isUpdating = true;
                claimButton.setVisible(false);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void claimOldData() {
        String cnic = cnicField.getText();
        if(cnic.isEmpty()) { JOptionPane.showMessageDialog(this, "Please enter CNIC to find old data!"); return; }
        
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT * FROM admissions WHERE cnic = ? AND user_id IS NULL";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cnic);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int confirm = JOptionPane.showConfirmDialog(this, "Found old record for " + rs.getString("student_name") + ". Link to your account?", "Confirm", JOptionPane.YES_NO_OPTION);
                if(confirm == JOptionPane.YES_OPTION) {
                    fillData(rs);
                    isUpdating = true;
                    // Link it now
                    String updateSql = "UPDATE admissions SET user_id = ? WHERE cnic = ? AND user_id IS NULL";
                    PreparedStatement upstmt = conn.prepareStatement(updateSql);
                    upstmt.setInt(1, loggedInUserId);
                    upstmt.setString(2, cnic);
                    upstmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Old data successfully linked to your account!");
                    claimButton.setVisible(false);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No unlinked record found with this CNIC.");
            }
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }

    private void fillData(ResultSet rs) throws SQLException {
        nameField.setText(rs.getString("student_name"));
        fatherField.setText(rs.getString("father_name"));
        cnicField.setText(rs.getString("cnic"));
        courseBox.setSelectedItem(rs.getString("course"));
        phoneField.setText(rs.getString("phone"));
        saveButton.setText("UPDATE APPLICATION");
    }

    private void saveOrUpdate() {
        if(nameField.getText().isEmpty()) return;
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql;
            if (isUpdating) {
                sql = "UPDATE admissions SET student_name=?, father_name=?, cnic=?, course=?, phone=? WHERE user_id=?";
            } else {
                sql = "INSERT INTO admissions (student_name, father_name, cnic, course, phone, user_id) VALUES (?, ?, ?, ?, ?, ?)";
            }
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, fatherField.getText());
            pstmt.setString(3, cnicField.getText());
            pstmt.setString(4, (String)courseBox.getSelectedItem());
            pstmt.setString(5, phoneField.getText());
            pstmt.setInt(6, loggedInUserId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, isUpdating ? "Form Updated!" : "Form Submitted!");
            new LoginFrame().setVisible(true); dispose();
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }
}
