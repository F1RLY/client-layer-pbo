package view;

import controller.PatientController;
import controller.DoctorController;
import controller.QueueController;
import view.components.Header;
import view.components.SideBar;
import view.panels.*;
import api.WebSocketHandler;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

public class MainFrame extends JFrame {
    // Inisialisasi semua Controller
    private final PatientController patientController = new PatientController();
    private final DoctorController doctorController = new DoctorController();
    private final QueueController queueController = new QueueController();

    private JPanel cardPanel;
    private CardLayout cardLayout;
    private MonitorPanel monitorPanel; // Referensi untuk refresh real-time

    public MainFrame() {
        setTitle("SMARS - Smart Hospital Management System");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        initComponents();
        initWebSocket(); 
    }

    private void initComponents() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        Header header = new Header();
        SideBar sideBar = new SideBar(target -> cardLayout.show(cardPanel, target));

        // Daftarkan semua Panel ke CardLayout
        cardPanel.add(new DashboardPanel(target -> cardLayout.show(cardPanel, target)), "DASHBOARD");
        cardPanel.add(new MasterDataPanel("PASIEN", patientController), "PASIEN");
        cardPanel.add(new MasterDataPanel("DOKTER", doctorController), "DOKTER");
        cardPanel.add(new TransaksiAntreanPanel(patientController, doctorController, queueController), "TRANSAKSI");
        
        // Simpan ke variabel class agar bisa diakses initWebSocket
        this.monitorPanel = new MonitorPanel(queueController);
        cardPanel.add(monitorPanel, "MONITOR");

        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(sideBar, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);
    }

    private void initWebSocket() {
        try {
            // Ganti port 8080 jika server WebSocket PHP Anda menggunakan port berbeda
            URI serverUri = new URI("ws://localhost:8080"); 
            
            WebSocketHandler wsClient = new WebSocketHandler(serverUri, message -> {
                SwingUtilities.invokeLater(() -> {
                    if (monitorPanel != null) {
                        monitorPanel.refreshMonitor();
                        System.out.println("Monitor Updated: " + message);
                    }
                });
            });
            wsClient.connect();
        } catch (Exception e) {
            System.err.println("WebSocket Connection Fail: " + e.getMessage());
        }
    }
}