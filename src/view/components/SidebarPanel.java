package view.components;

import javax.swing.*;
import java.awt.*;

public class SidebarPanel extends JPanel {
    private JButton[] buttons;
    
    // Menu items tanpa emoji
    private final String[] MENU_ITEMS = {
        "DASHBOARD",
        "DATA DOKTER", 
        "DATA PASIEN",
        "JANJI TEMU",
        "LAPORAN",
        "",
        "PENGATURAN",
        "KELUAR"
    };
    
    public SidebarPanel(Color color) {
        setLayout(new GridLayout(8, 1, 0, 5));
        setBackground(color);
        setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        buttons = new JButton[MENU_ITEMS.length];
        
        for (int i = 0; i < MENU_ITEMS.length; i++) {
            if (MENU_ITEMS[i].isEmpty()) {
                add(Box.createVerticalStrut(20));
                continue;
            }
            
            buttons[i] = new JButton(MENU_ITEMS[i]);
            buttons[i].setHorizontalAlignment(SwingConstants.LEFT);
            
            // BIARKAN DEFAULT STYLE SYSTEM
            // Jangan set background/foreground manual
            
            add(buttons[i]);
        }
        
        // Set dashboard as active
        setActiveButton(0);
    }
    
    public void setActiveButton(int index) {
        // Reset semua button ke default
        for (JButton btn : buttons) {
            if (btn != null) {
                // Reset ke look default
                btn.setBackground(null);
                btn.setForeground(null);
                btn.setFont(new Font("Arial", Font.PLAIN, 13));
            }
        }
        
        // Set active button dengan styling minimal
        if (buttons[index] != null) {
            buttons[index].setFont(new Font("Arial", Font.BOLD, 13));
        }
    }
    
    public void setActiveButton(JButton button) {
        // Cari index button yang diklik
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == button) {
                setActiveButton(i);
                break;
            }
        }
    }
    
    // Getter methods
    public JButton getBtnDashboard() { 
        return (buttons != null && buttons.length > 0) ? buttons[0] : null; 
    }
    public JButton getBtnDokter() { 
        return (buttons != null && buttons.length > 1) ? buttons[1] : null; 
    }
    public JButton getBtnPasien() { 
        return (buttons != null && buttons.length > 2) ? buttons[2] : null; 
    }
    public JButton getBtnLaporan() { 
        return (buttons != null && buttons.length > 4) ? buttons[4] : null; 
    }
    public JButton getBtnLogout() { 
        return (buttons != null && buttons.length > 7) ? buttons[7] : null; 
    }
}