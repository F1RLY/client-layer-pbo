package view.components;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {
    public HeaderPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(70, 130, 180));
        
        JLabel titleLabel = new JLabel("Aplikasi Manajemen Klinik", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        add(titleLabel, BorderLayout.CENTER);
        
        // Optional: tambahkan logo atau user info di kanan
        JLabel userLabel = new JLabel("Admin");
        userLabel.setForeground(Color.WHITE);
        userLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        add(userLabel, BorderLayout.EAST);
    }
}