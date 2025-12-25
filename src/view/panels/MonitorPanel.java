package view.panels;

import controller.QueueController;
import javax.swing.*;
import java.awt.*;

public class MonitorPanel extends JPanel {
    private QueueController qc;
    private JLabel lblNomorAntrean;
    private JLabel lblNamaPasien;

    public MonitorPanel(QueueController qc) {
        this.qc = qc;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(44, 62, 80)); // Warna gelap biar elegan seperti TV rumah sakit

        // Panel Tengah untuk Nomor Antrean
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.setOpaque(false);

        lblNomorAntrean = new JLabel("A-000", SwingConstants.CENTER);
        lblNomorAntrean.setFont(new Font("Serif", Font.BOLD, 120));
        lblNomorAntrean.setForeground(Color.WHITE);

        lblNamaPasien = new JLabel("Menunggu Antrean...", SwingConstants.CENTER);
        lblNamaPasien.setFont(new Font("SansSerif", Font.PLAIN, 30));
        lblNamaPasien.setForeground(new Color(189, 195, 199));

        centerPanel.add(lblNomorAntrean);
        centerPanel.add(lblNamaPasien);

        add(new JLabel("MONITOR ANTREAN", SwingConstants.CENTER), BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    // Fungsi ini akan dipanggil oleh WORKER saat ada pesan dari WebSocket
    public void refreshMonitor() {
        // Ambil data terbaru dari Controller (yang sudah konek ke API)
        // Misal kita ambil antrean yang statusnya 'CALLING'
        var currentQueue = qc.getCurrentCalling(); 
        
        if (currentQueue != null) {
            lblNomorAntrean.setText(currentQueue.getNomor());
            lblNamaPasien.setText(currentQueue.getPasien().getNama());
            
            // Opsional: Tambahkan suara "Nomor antrean..." di sini
        }
    }
}