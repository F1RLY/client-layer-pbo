package view.components;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {
    private Color headerColor;
    
    public HeaderPanel(Color color) {
        this.headerColor = color;
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 70));
        setBackground(headerColor);
        
        // Left: Logo & Title dengan icon
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);
        
        // Icon lingkaran dengan huruf "H" (Hospital)
        JPanel iconCircle = createIconCircle("H", Color.WHITE, new Color(52, 152, 219));
        
        JLabel titleLabel = new JLabel("KLINIK SEHAT MANDIRI");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        
        leftPanel.add(iconCircle);
        leftPanel.add(titleLabel);
        
        // Right: Time & User
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        
        // User icon
        JPanel userIcon = createIconCircle("A", Color.WHITE, new Color(46, 204, 113));
        
        JLabel userLabel = new JLabel("Administrator");
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userLabel.setForeground(Color.WHITE);
        
        rightPanel.add(userIcon);
        rightPanel.add(userLabel);
        
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }
    
    private JPanel createIconCircle(String text, Color textColor, Color bgColor) {
        JPanel circlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillOval(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        
        circlePanel.setLayout(new GridBagLayout());
        circlePanel.setPreferredSize(new Dimension(40, 40));
        circlePanel.setOpaque(false);
        
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(textColor);
        
        circlePanel.add(label);
        return circlePanel;
    }
}