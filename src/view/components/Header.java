package view.components;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Header extends JPanel {

    public Header() {
        // Layout dan Warna Dasar
        setLayout(new BorderLayout());
        setBackground(new Color(52, 152, 219)); // Biru Cerah
        setPreferredSize(new Dimension(0, 70)); // Tinggi header 70px
        
        // Garis bawah tipis agar terlihat elegan
        setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(41, 128, 185)));

        initComponents();
    }

    private void initComponents() {
        // Sisi Kiri: Judul & Subtitle
        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));

        JLabel lblTitle = new JLabel("SMARS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSubtitle = new JLabel("Smart Hospital Queue System");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(new Color(236, 240, 241));

        leftPanel.add(lblTitle);
        leftPanel.add(lblSubtitle);

        // Sisi Kanan: Info Waktu
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy"));
        JLabel lblTime = new JLabel(date + "  ");
        lblTime.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblTime.setForeground(Color.WHITE);
        lblTime.setHorizontalAlignment(SwingConstants.RIGHT);

        // Gabungkan ke Panel Utama
        add(leftPanel, BorderLayout.WEST);
        add(lblTime, BorderLayout.EAST);
    }
}