package view.components;

import javax.swing.*;
import java.awt.*;

public class SidebarPanel extends JPanel {
    private JButton btnDashboard, btnDokter, btnPasien, btnLaporan, btnLogout;
    
    public SidebarPanel() {
        setLayout(new GridLayout(10, 1, 0, 10));
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        btnDashboard = createButton("Dashboard");
        btnDokter = createButton("Data Dokter");
        btnPasien = createButton("Data Pasien");
        btnLaporan = createButton("Laporan");
        btnLogout = createButton("Logout");
        
        add(btnDashboard);
        add(btnDokter);
        add(btnPasien);
        add(new JSeparator());
        add(btnLaporan);
        add(Box.createVerticalGlue());
        add(btnLogout);
    }
    
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }
    
    // Getter untuk button (nanti dihubungkan ke controller)
    public JButton getBtnDokter() { return btnDokter; }
    public JButton getBtnPasien() { return btnPasien; }
    public JButton getBtnDashboard() { return btnDashboard; }
    public JButton getBtnLogout() { return btnLogout; }
}