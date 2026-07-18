import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ModernButton extends JButton {
    private Color baseColor;
    private Color hoverColor;
    private Color currentColor;
    private boolean isHovered = false;
    private int verticalOffset = 0;

    public ModernButton(String text, Color baseColor, Color hoverColor) {
        super(text);
        this.baseColor = baseColor;
        this.hoverColor = hoverColor;
        this.currentColor = baseColor;
        
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                animate(true);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                animate(false);
            }
        });
    }

    private void animate(boolean entering) {
        Timer timer = new Timer(10, new ActionListener() {
            int steps = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (entering) {
                    verticalOffset = -2; // Lift up
                    currentColor = hoverColor;
                } else {
                    verticalOffset = 0; // Return
                    currentColor = baseColor;
                }
                repaint();
                ((Timer)e.getSource()).stop();
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Shadow effect
        if (isHovered) {
            g2.setColor(new Color(0, 0, 0, 50));
            g2.fillRoundRect(3, 5 + verticalOffset, getWidth() - 6, getHeight() - 10, 15, 15);
        }

        // Main Button
        g2.setColor(currentColor);
        g2.fillRoundRect(0, verticalOffset, getWidth(), getHeight() - 5, 15, 15);
        
        g2.dispose();
        super.paintComponent(g);
    }
}
