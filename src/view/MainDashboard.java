// File: view/MainDashboard.java
package view;

import javax.swing.*;
import java.awt.*;
import model.User;

public class MainDashboard extends JFrame {
    private User currentUser;
    
    public MainDashboard(User user) {
        this.currentUser = user;
        initComponents();
        setupFrame();
    }
    
    private void initComponents() {
        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        logoutItem.addActionListener(e -> logout());
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Data Menu
        JMenu dataMenu = new JMenu("Data");
        JMenuItem pasienItem = new JMenuItem("Pasien");
        JMenuItem dokterItem = new JMenuItem("Dokter");
        JMenuItem antreanItem = new JMenuItem("Antrean");
        JMenuItem jadwalItem = new JMenuItem("Jadwal");
        
        pasienItem.addActionListener(e -> openPasienFrame());
        dokterItem.addActionListener(e -> openDokterFrame());
        antreanItem.addActionListener(e -> openAntreanFrame());
        jadwalItem.addActionListener(e -> openJadwalFrame());
        
        dataMenu.add(pasienItem);
        dataMenu.add(dokterItem);
        dataMenu.add(antreanItem);
        dataMenu.add(jadwalItem);
        
        // Tools Menu
        JMenu toolsMenu = new JMenu("Tools");
        JMenuItem monitorItem = new JMenuItem("Monitor");
        JMenuItem reportItem = new JMenuItem("Laporan");
        
        monitorItem.addActionListener(e -> openMonitorFrame());
        reportItem.addActionListener(e -> openReportFrame());
        
        toolsMenu.add(monitorItem);
        toolsMenu.add(reportItem);
        
        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(dataMenu);
        menuBar.add(toolsMenu);
        
        setJMenuBar(menuBar);
        
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Welcome Label
        JLabel welcomeLabel = new JLabel(
            "Selamat datang, " + currentUser.getNamaLengkap() + 
            " (" + currentUser.getRole() + ")", 
            SwingConstants.CENTER
        );
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);
        
        // Quick Access Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JButton btnPasien = new JButton("Pasien");
        JButton btnDokter = new JButton("Dokter");
        JButton btnAntrean = new JButton("Antrean");
        JButton btnJadwal = new JButton("Jadwal");
        JButton btnMonitor = new JButton("Monitor");
        JButton btnReport = new JButton("Laporan");
        
        btnPasien.addActionListener(e -> openPasienFrame());
        btnDokter.addActionListener(e -> openDokterFrame());
        btnAntrean.addActionListener(e -> openAntreanFrame());
        btnJadwal.addActionListener(e -> openJadwalFrame());
        btnMonitor.addActionListener(e -> openMonitorFrame());
        btnReport.addActionListener(e -> openReportFrame());
        
        buttonPanel.add(btnPasien);
        buttonPanel.add(btnDokter);
        buttonPanel.add(btnAntrean);
        buttonPanel.add(btnJadwal);
        buttonPanel.add(btnMonitor);
        buttonPanel.add(btnReport);
        
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Status Bar
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        
        JLabel statusLabel = new JLabel("Status: Login sebagai " + currentUser.getRole());
        statusPanel.add(statusLabel);
        
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void setupFrame() {
        setTitle("SMARS Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    // Navigation methods
    private void openPasienFrame() {
        new PasienFrame(currentUser).setVisible(true);
    }
    
    private void openDokterFrame() {
        new DokterFrame(currentUser).setVisible(true);
    }
    
    private void openAntreanFrame() {
        new AntreanFrame(currentUser).setVisible(true);
    }
    
    private void openJadwalFrame() {
        new JadwalFrame(currentUser).setVisible(true);
    }
    
    private void openMonitorFrame() {
        new MonitorFrame().setVisible(true);
    }
    
    private void openReportFrame() {
        new ReportFrame(currentUser).setVisible(true);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Yakin ingin logout?",
            "Konfirmasi Logout",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}