package view;

import controller.PatientController;
import controller.DoctorController;
import controller.QueueController;
import view.components.Header;
import view.components.SideBar;
import view.panels.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    // 1. Inisialisasi semua Controller (Satu instance untuk seluruh aplikasi)
    private final PatientController patientController = new PatientController();
    private final DoctorController doctorController = new DoctorController();
    private final QueueController queueController = new QueueController();

    private JPanel cardPanel;
    private CardLayout cardLayout;

    public MainFrame() {
        // Pengaturan dasar jendela
        setTitle("SMARS - Smart Hospital Management System");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        initComponents();
    }

    private void initComponents() {
        // 2. Setup Layout Konten (CardLayout untuk ganti-ganti halaman)
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // 3. Setup Komponen Navigasi & Header
        Header header = new Header();
        
        // SideBar menerima perintah (target) untuk memutar "kartu" di cardPanel
        SideBar sideBar = new SideBar(target -> cardLayout.show(cardPanel, target));

        // 4. Masukkan semua Panel ke dalam CardLayout
        // Kita oper controller ke masing-masing panel yang membutuhkan
        
        // Dashboard: Butuh akses navigasi untuk tombol shortcut
        cardPanel.add(new DashboardPanel(target -> cardLayout.show(cardPanel, target)), "DASHBOARD");
        
        // Master Data: Kita kirim controller spesifik
        cardPanel.add(new MasterDataPanel("PASIEN", patientController), "PASIEN");
        cardPanel.add(new MasterDataPanel("DOKTER", doctorController), "DOKTER");
        
        // Transaksi: Butuh ketiganya (Pilih pasien, Pilih dokter, Buat antrean)
        cardPanel.add(new TransaksiAntreanPanel(patientController, doctorController, queueController), "TRANSAKSI");
        
        // Monitor: Cukup QueueController untuk panggil antrean
        cardPanel.add(new MonitorPanel(queueController), "MONITOR");

        // 5. Susun Layout Utama Frame
        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);  // Header di atas
        add(sideBar, BorderLayout.WEST);  // Navigasi di kiri
        add(cardPanel, BorderLayout.CENTER); // Konten di tengah
    }
}