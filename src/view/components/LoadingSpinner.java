package view.components;

import javax.swing.*;
import java.awt.*;

public class LoadingSpinner extends JDialog {
    public LoadingSpinner(JFrame parent, String message) {
        super(parent, true);
        setUndecorated(true);
        setSize(300, 150);
        setLocationRelativeTo(parent);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(200, 10));
        
        panel.add(label, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);
        
        add(panel);
    }
}