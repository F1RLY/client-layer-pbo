package view.panels;

import controller.QueueController;
import model.Antrean;
import javax.swing.*;
import java.awt.*;

public class MonitorPanel extends JPanel {
    private QueueController qc;
    private JLabel lblNomorAntrean;
    private JLabel lblNamaPasien;
    private JLabel lblNamaDokter;
    private Timer refreshTimer;

    public MonitorPanel(QueueController qc) {
        this.qc = qc;
        initComponents();
        setupAutoRefresh(); // Menjalankan pengecekan otomatis ke server
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(44, 62, 80)); // Warna Biru Gelap (Elegant)

        // --- HEADER ---
        JLabel lblHeader = new JLabel("MONITOR ANTREAN PASIEN", SwingConstants.CENTER);
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        add(lblHeader, BorderLayout.NORTH);

        // --- BODY (NOMOR & NAMA) ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        lblNomorAntrean = new JLabel("---", SwingConstants.CENTER);
        lblNomorAntrean.setFont(new Font("Serif", Font.BOLD, 200)); // Ukuran nomor sangat besar
        lblNomorAntrean.setForeground(new Color(241, 196, 15)); // Warna Kuning Emas
        lblNomorAntrean.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblNamaPasien = new JLabel("BELUM ADA PANGGILAN", SwingConstants.CENTER);
        lblNamaPasien.setFont(new Font("SansSerif", Font.BOLD, 45));
        lblNamaPasien.setForeground(Color.WHITE);
        lblNamaPasien.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblNamaDokter = new JLabel("Silakan Menunggu", SwingConstants.CENTER);
        lblNamaDokter.setFont(new Font("SansSerif", Font.ITALIC, 28));
        lblNamaDokter.setForeground(new Color(189, 195, 199));
        lblNamaDokter.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(lblNomorAntrean);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(lblNamaPasien);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(lblNamaDokter);
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);

        // --- FOOTER ---
        JLabel lblFooter = new JLabel("Harap Menuju ke Ruang Periksa Saat Nama Anda Dipanggil", SwingConstants.CENTER);
        lblFooter.setForeground(new Color(149, 165, 166));
        lblFooter.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblFooter, BorderLayout.SOUTH);
    }

    private void setupAutoRefresh() {
        // Cek data ke server setiap 3000ms (3 detik)
        refreshTimer = new Timer(3000, e -> refreshMonitor());
        refreshTimer.start();
    }

    public void refreshMonitor() {
        Antrean currentQueue = qc.getCurrentCalling(); 
        if (currentQueue != null && currentQueue.getNomor() != null) {
            lblNomorAntrean.setText(currentQueue.getNomor());
            // Tampilkan nama dari field yang sudah di-mapping tadi
            lblNamaPasien.setText(currentQueue.getNamaPasien().toUpperCase());
            lblNamaDokter.setText("Menuju: " + currentQueue.getNamaDokter());
        } else {
            lblNomorAntrean.setText("---");
            lblNamaPasien.setText("Belum Ada Panggilan");
            lblNamaDokter.setText("Silakan Menunggu");
        }
    }
    
    // Matikan timer jika panel ditutup untuk menghemat memori
    public void stopTimer() {
        if (refreshTimer != null) refreshTimer.stop();
    }
}