package view.panels;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class DashboardPanel extends JPanel {
    private Consumer<String> navigation;

    public DashboardPanel(Consumer<String> navigation) {
        this.navigation = navigation;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        initComponents();
    }

    private void initComponents() {
        JLabel lblWelcome = new JLabel("SMARS DASHBOARD", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblWelcome.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        add(lblWelcome, BorderLayout.NORTH);

        // Ubah grid jadi 3x2 supaya cukup buat tombol panggil
        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 50, 50));

        gridPanel.add(createMenuCard("Kelola Pasien", "👤", "PASIEN"));
        gridPanel.add(createMenuCard("Kelola Dokter", "👨‍⚕️", "DOKTER"));
        gridPanel.add(createMenuCard("Ambil Antrean", "📝", "TRANSAKSI"));
        gridPanel.add(createMenuCard("Layar Monitor", "📺", "MONITOR"));
        
        // INI TOMBOL YANG KAMU CARI
        gridPanel.add(createMenuCard("Panggil Berikutnya", "🔊", "AKSI_PANGGIL"));

        add(gridPanel, BorderLayout.CENTER);
    }

    private JButton createMenuCard(String title, String icon, String target) {
        JButton btn = new JButton("<html><center><font size='10'>" + icon + "</font><br><br>" + title + "</center></html>");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(236, 240, 241));
        btn.setFocusPainted(false);
        btn.addActionListener(e -> navigation.accept(target));
        return btn;
    }
}