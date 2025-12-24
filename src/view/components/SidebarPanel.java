package view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SidebarPanel extends JPanel {
    private JButton btnPendaftaran;
    private JButton btnAntrean;
    private JButton btnDataPasien;
    private JButton btnMonitor;
    private JButton btnLaporan;
    private JButton btnLogout;
    
    public SidebarPanel() {
        initUI();
    }
    
    private void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(44, 62, 80));
        setPreferredSize(new Dimension(200, 600));
        setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        // Title
        JLabel lblTitle = new JLabel("MENU");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        add(lblTitle);
        add(Box.createVerticalStrut(30));
        
        // Buttons
        btnPendaftaran = createMenuButton("📝 Pendaftaran");
        btnAntrean = createMenuButton("🔔 Antrean");
        btnDataPasien = createMenuButton("👥 Data Pasien");
        btnMonitor = createMenuButton("📺 Monitor");
        btnLaporan = createMenuButton("📊 Laporan");
        
        add(btnPendaftaran);
        add(Box.createVerticalStrut(10));
        add(btnAntrean);
        add(Box.createVerticalStrut(10));
        add(btnDataPasien);
        add(Box.createVerticalStrut(10));
        add(btnMonitor);
        add(Box.createVerticalStrut(10));
        add(btnLaporan);
        
        // Spacer
        add(Box.createVerticalGlue());
        
        // Logout button
        btnLogout = new JButton("🚪 Logout");
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setMaximumSize(new Dimension(180, 40));
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 12));
        
        add(btnLogout);
    }
    
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 45));
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 152, 219));
            }
        });
        
        return button;
    }
    
    // Set action listeners
    public void setPendaftaranListener(ActionListener listener) {
        btnPendaftaran.addActionListener(listener);
    }
    
    public void setAntreanListener(ActionListener listener) {
        btnAntrean.addActionListener(listener);
    }
    
    public void setDataPasienListener(ActionListener listener) {
        btnDataPasien.addActionListener(listener);
    }
    
    public void setMonitorListener(ActionListener listener) {
        btnMonitor.addActionListener(listener);
    }
    
    public void setLaporanListener(ActionListener listener) {
        btnLaporan.addActionListener(listener);
    }
    
    public void setLogoutListener(ActionListener listener) {
        btnLogout.addActionListener(listener);
    }
    
    // Enable/disable buttons based on role
    public void setRolePermissions(String role) {
        boolean isAdmin = "ADMIN".equals(role);
        boolean isResepsionis = "RESEPSIONIS".equals(role);
        boolean isDokter = "DOKTER".equals(role);
        
        btnPendaftaran.setEnabled(isResepsionis);
        btnAntrean.setEnabled(isResepsionis);
        btnDataPasien.setEnabled(isResepsionis || isAdmin);
        btnMonitor.setEnabled(true); // All roles can view monitor
        btnLaporan.setEnabled(isAdmin);
    }
}