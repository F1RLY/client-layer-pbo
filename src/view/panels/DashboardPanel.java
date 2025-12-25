package view.panels;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class DashboardPanel extends JPanel {
    private Consumer<String> navigation;

    // Kita tambahkan parameter Consumer agar dashboard bisa menyuruh MainApp pindah layar
    public DashboardPanel(Consumer<String> navigation) {
        this.navigation = navigation;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        initComponents();
    }

    private void initComponents() {
        // Judul Welcome
        JLabel lblWelcome = new JLabel("SMARS DASHBOARD", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblWelcome.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        add(lblWelcome, BorderLayout.NORTH);

        // Grid Menu Utama
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 50, 50));

        // Buat Button dengan helper agar rapi
        gridPanel.add(createMenuCard("Kelola Pasien", "👥", "PASIEN"));
        gridPanel.add(createMenuCard("Kelola Dokter", "👨‍⚕️", "DOKTER"));
        gridPanel.add(createMenuCard("Ambil Antrean", "🎫", "TRANSAKSI"));
        gridPanel.add(createMenuCard("Layar Monitor", "📺", "MONITOR"));

        add(gridPanel, BorderLayout.CENTER);
    }

    private JButton createMenuCard(String title, String icon, String target) {
        // Gunakan HTML untuk membuat dua baris (Icon Besar + Teks)
        JButton btn = new JButton("<html><center><font size='10'>" + icon + "</font><br><br>" + title + "</center></html>");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(236, 240, 241));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Aksi pindah layar
        btn.addActionListener(e -> navigation.accept(target));

        // Efek Hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(52, 152, 219));
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(236, 240, 241));
                btn.setForeground(Color.BLACK);
            }
        });

        return btn;
    }
}