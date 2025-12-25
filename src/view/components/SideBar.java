package view.components;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class SideBar extends JPanel {
    private Consumer<String> onMenuSelected;

    public SideBar(Consumer<String> onMenuSelected) {
        this.onMenuSelected = onMenuSelected;
        
        // Pengaturan Layout & Warna (Dark Blue Grey)
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(44, 62, 80)); 
        setPreferredSize(new Dimension(200, 0));
        setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        initComponents();
    }

    private void initComponents() {
        // Label Header Menu
        JLabel lblMenu = new JLabel("  NAVIGASI UTAMA");
        lblMenu.setForeground(new Color(149, 165, 166));
        lblMenu.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(lblMenu);
        
        add(Box.createRigidArea(new Dimension(0, 20)));

        // Tombol Menu Tanpa Icon (Hanya Teks)
        add(createMenuButton("Dashboard", "DASHBOARD"));
        add(createMenuButton("Data Pasien", "PASIEN"));
        add(createMenuButton("Data Dokter", "DOKTER"));
        add(createMenuButton("Antrean Baru", "TRANSAKSI"));
        add(createMenuButton("Monitor TV", "MONITOR"));

        // Mendorong konten ke atas
        add(Box.createVerticalGlue());
        
        // Info Versi
        JLabel lblVersion = new JLabel("v1.0.0 ");
        lblVersion.setForeground(new Color(127, 140, 141));
        lblVersion.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblVersion.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(lblVersion);
        add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private JButton createMenuButton(String text, String targetCard) {
    JButton btn = new JButton(text);
    
    // 1. KUNCI UTAMA: Paksa tombol agar memenuhi lebar Sidebar (200px)
    btn.setMaximumSize(new Dimension(200, 45)); 
    btn.setPreferredSize(new Dimension(200, 45));
    btn.setMinimumSize(new Dimension(200, 45));

    // 2. Pastikan alignment tombol konsisten dengan BoxLayout Sidebar
    btn.setAlignmentX(Component.CENTER_ALIGNMENT); 

    btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
    btn.setForeground(new Color(236, 240, 241));
    btn.setBackground(new Color(44, 62, 80));
    btn.setFocusPainted(false);
    btn.setBorderPainted(false);
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
    // Atur teks agar tetap di kiri meskipun tombolnya lebar (Full Width)
    btn.setHorizontalAlignment(SwingConstants.LEFT);
    btn.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0)); // Padding teks

    btn.addActionListener(e -> onMenuSelected.accept(targetCard));

    // Efek Hover
    btn.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            btn.setBackground(new Color(52, 152, 219));
            btn.setOpaque(true); // Pastikan ini true agar warna muncul penuh
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
            btn.setBackground(new Color(44, 62, 80));
        }
    });

    return btn;
}
}