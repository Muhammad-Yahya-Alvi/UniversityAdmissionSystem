import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class AdminDashboardFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private ModernButton deleteButton, logoutButton, refreshButton;

    public AdminDashboardFrame() {
        setTitle("University Portal - Admin Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(44, 62, 80));
        JLabel title = new JLabel("Admin Control Panel - Admission Records");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.add(title);

        // Table Setup
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ID", "Student Name", "Father Name", "CNIC", "Course", "Phone", "Date"});
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        
        loadData();

        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        controlPanel.setBackground(new Color(248, 249, 250));

        refreshButton = new ModernButton("REFRESH", new Color(52, 152, 219), new Color(41, 128, 185));
        refreshButton.setPreferredSize(new Dimension(120, 40));

        deleteButton = new ModernButton("DELETE RECORD", new Color(231, 76, 60), new Color(192, 57, 43));
        deleteButton.setPreferredSize(new Dimension(180, 40));

        logoutButton = new ModernButton("LOGOUT", new Color(44, 62, 80), Color.BLACK);
        logoutButton.setPreferredSize(new Dimension(120, 40));

        controlPanel.add(refreshButton);
        controlPanel.add(deleteButton);
        controlPanel.add(logoutButton);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Actions
        refreshButton.addActionListener(e -> loadData());
        logoutButton.addActionListener(e -> { new LoginFrame().setVisible(true); dispose(); });
        deleteButton.addActionListener(e -> deleteRecord());
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT * FROM admissions ORDER BY id DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("id"));
                row.add(rs.getString("student_name"));
                row.add(rs.getString("father_name"));
                row.add(rs.getString("cnic"));
                row.add(rs.getString("course"));
                row.add(rs.getString("phone"));
                row.add(rs.getString("admission_date"));
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
        }
    }

    private void deleteRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete!");
            return;
        }

        String id = tableModel.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete ID: " + id + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConfig.getConnection()) {
                String sql = "DELETE FROM admissions WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(id));
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Record Deleted Successfully!");
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting record: " + ex.getMessage());
            }
        }
    }
}
