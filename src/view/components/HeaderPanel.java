package view.components;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {
    private JLabel lblTitle;
    private JLabel lblUserInfo;
    private JLabel lblDateTime;
    
    public HeaderPanel() {
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(52, 73, 94));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Left side - Title
        lblTitle = new JLabel("SISTEM MANAJEMEN ANTREAN RUMAH SAKIT");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        
        // Right side - User info and time
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        
        lblUserInfo = new JLabel("User: - | Role: -");
        lblUserInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblUserInfo.setForeground(Color.WHITE);
        
        lblDateTime = new JLabel("Date: -");
        lblDateTime.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDateTime.setForeground(Color.WHITE);
        
        rightPanel.add(lblUserInfo);
        rightPanel.add(Box.createHorizontalStrut(20));
        rightPanel.add(lblDateTime);
        
        add(lblTitle, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }
    
    public void setUserInfo(String username, String role) {
        lblUserInfo.setText("User: " + username + " | Role: " + role);
    }
    
    public void setDateTime(String dateTime) {
        lblDateTime.setText(dateTime);
    }
    
    public void setTitle(String title) {
        lblTitle.setText(title);
    }
}