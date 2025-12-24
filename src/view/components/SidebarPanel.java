package view.components;

import javax.swing.*;
import java.awt.*;

public class SidebarPanel extends JPanel {
    private JButton[] buttons;
    private Color sidebarColor = new Color(52, 73, 94);
    private Color buttonColor = new Color(250, 250, 250);
    private Color activeColor = new Color(41, 128, 185);
    
    // Ganti emoji dengan simbol teks sederhana
    private final String[] BUTTON_TEXTS = {
        "📊 DASHBOARD",          // Tetap coba emoji, fallback ke text
        "👨 DOKTER",            // Simplified emoji
        "👤 PASIEN",            // Simplified emoji  
        "📅 JANJI TEMU",
        "📋 LAPORAN",
        "",                     // Separator
        "⚙ PENGATURAN",
        "🚪 KELUAR"
    };
    
    // Fallback jika emoji rusak
    private final String[] FALLBACK_TEXTS = {
        "[D] DASHBOARD",
        "[D] DATA DOKTER", 
        "[P] DATA PASIEN",
        "[J] JANJI TEMU",
        "[R] LAPORAN",
        "",
        "[S] PENGATURAN",
        "[X] KELUAR"
    };
    
    public SidebarPanel(Color color) {
        this.sidebarColor = color;
        initUI();
    }
    
    private void initUI() {
        setLayout(new GridLayout(8, 1, 0, 5));
        setBackground(sidebarColor);
        setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        buttons = new JButton[BUTTON_TEXTS.length];
        
        for (int i = 0; i < BUTTON_TEXTS.length; i++) {
            if (BUTTON_TEXTS[i].isEmpty()) {
                add(Box.createVerticalStrut(20));
                continue;
            }
            
            // Coba deteksi jika emoji rusak, pakai fallback
            String buttonText = BUTTON_TEXTS[i];
            if (isEmojiBroken()) {
                buttonText = FALLBACK_TEXTS[i];
            }
            
            buttons[i] = createMenuButton(buttonText);
            add(buttons[i]);
        }
        
        // Set dashboard as active
        setActiveButton(buttons[0]);
    }
    
    private boolean isEmojiBroken() {
        // Deteksi sederhana: jika font tidak support emoji
        Font font = new Font("Arial", Font.PLAIN, 12);
        String testText = "📊";
        return font.canDisplayUpTo(testText) != -1;
    }
    
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBackground(buttonColor);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        btn.setFocusPainted(false);
        
        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!btn.getBackground().equals(activeColor)) {
                    btn.setBackground(new Color(230, 240, 255));
                }
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!btn.getBackground().equals(activeColor)) {
                    btn.setBackground(buttonColor);
                }
            }
        });
        
        return btn;
    }
    
    public void setActiveButton(JButton activeBtn) {
        // Reset all buttons
        for (JButton btn : buttons) {
            if (btn != null) {
                btn.setBackground(buttonColor);
                btn.setForeground(Color.BLACK);
                btn.setFont(new Font("Arial", Font.PLAIN, 13));
            }
        }
        
        // Set active button
        if (activeBtn != null) {
            activeBtn.setBackground(activeColor);
            activeBtn.setForeground(Color.WHITE);
            activeBtn.setFont(new Font("Arial", Font.BOLD, 13));
        }
    }
    
    // Getter methods
    public JButton getBtnDashboard() { return buttons[0]; }
    public JButton getBtnDokter() { return buttons[1]; }
    public JButton getBtnPasien() { return buttons[2]; }
    public JButton getBtnLaporan() { return buttons[4]; }
    public JButton getBtnLogout() { return buttons[7]; }
}