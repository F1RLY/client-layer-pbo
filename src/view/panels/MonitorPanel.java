package view.panels;

import controller.QueueController;
import model.Antrean;
import javax.swing.*;
import java.awt.*;

public class MonitorPanel extends JPanel {
    private QueueController qc;
    private JLabel lblNomorAntrean;
    private JLabel lblNamaPasien;
    private JLabel lblNamaDokter; // Tambahan: Biar tahu dipanggil ke dokter mana

    public MonitorPanel(QueueController qc) {
        this.qc = qc;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(44, 62, 80)); // Warna Midnight Blue elegan

        // --- Panel Judul (Header) ---
        JLabel lblHeader = new JLabel("MONITOR ANTREAN PASIEN", SwingConstants.CENTER);
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblHeader, BorderLayout.NORTH);

        // --- Panel Tengah (Konten Utama) ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        lblNomorAntrean = new JLabel("---", SwingConstants.CENTER);
        lblNomorAntrean.setFont(new Font("Serif", Font.BOLD, 150)); // Lebih besar agar jelas
        lblNomorAntrean.setForeground(new Color(46, 204, 113)); // Warna hijau terang (Emerald)
        lblNomorAntrean.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblNamaPasien = new JLabel("Belum Ada Panggilan", SwingConstants.CENTER);
        lblNamaPasien.setFont(new Font("SansSerif", Font.BOLD, 40));
        lblNamaPasien.setForeground(Color.WHITE);
        lblNamaPasien.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblNamaDokter = new JLabel("Menunggu Antrean...", SwingConstants.CENTER);
        lblNamaDokter.setFont(new Font("SansSerif", Font.ITALIC, 25));
        lblNamaDokter.setForeground(new Color(189, 195, 199));
        lblNamaDokter.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(lblNomorAntrean);
        centerPanel.add(lblNamaPasien);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(lblNamaDokter);
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);
    }

    // Fungsi refresh yang dipanggil berkala atau via WebSocket
    public void refreshMonitor() {
        // Method getCurrentCalling() di QueueController harus sudah konek ke API GET
        Antrean currentQueue = qc.getCurrentCalling(); 
        
        if (currentQueue != null) {
            // Pastikan method di model Antrean.java adalah getNomor() sesuai kolom 'nomor' di DB
            lblNomorAntrean.setText(currentQueue.getNomor());
            
            // Mengambil nama dari objek Pasien dan Dokter yang di-JOIN dari PHP
            if (currentQueue.getPasien() != null) {
                lblNamaPasien.setText(currentQueue.getPasien().getNama().toUpperCase());
            }
            
            if (currentQueue.getDokter() != null) {
                lblNamaDokter.setText("Menuju: " + currentQueue.getDokter().getNama());
            }

            // Opsional: Trigger suara panggilan jika nomor berubah
            // playVoice(currentQueue.getNomor());
        }
    }
}