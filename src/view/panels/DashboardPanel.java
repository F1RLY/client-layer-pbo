package view.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardPanel extends JPanel {
    private JLabel lblWelcome;
    private JButton btnRefresh;
    private JPanel statsPanel;
    
    // Statistik counters
    private JLabel lblJumlahDokter;
    private JLabel lblJumlahPasien;
    private JLabel lblJumlahKonsultasiHariIni;
    
    public DashboardPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        initHeader();
        initStatsPanel();
        initRecentActivity();
        
        // Untuk testing, update statistik dengan dummy data
        updateStats(5, 120, 8);
    }
    
    private void initHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        lblWelcome = new JLabel("Selamat Datang, Admin!");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 24));
        
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(DashboardPanel.this, 
                    "Statistik direfresh!", "Info", JOptionPane.INFORMATION_MESSAGE);
                // Nanti di sini panggil controller untuk update data
            }
        });
        
        headerPanel.add(lblWelcome, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
    }
    
    private void initStatsPanel() {
        statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistik"));
        
        // Card 1: Jumlah Dokter
        JPanel dokterCard = createStatCard("Dokter", "5", new Color(70, 130, 180));
        lblJumlahDokter = (JLabel) ((JPanel) dokterCard.getComponent(1)).getComponent(0);
        
        // Card 2: Jumlah Pasien
        JPanel pasienCard = createStatCard("Pasien", "120", new Color(60, 179, 113));
        lblJumlahPasien = (JLabel) ((JPanel) pasienCard.getComponent(1)).getComponent(0);
        
        // Card 3: Konsultasi Hari Ini
        JPanel konsultasiCard = createStatCard("Konsultasi Hari Ini", "8", new Color(220, 20, 60));
        lblJumlahKonsultasiHariIni = (JLabel) ((JPanel) konsultasiCard.getComponent(1)).getComponent(0);
        
        statsPanel.add(dokterCard);
        statsPanel.add(pasienCard);
        statsPanel.add(konsultasiCard);
        
        add(statsPanel, BorderLayout.CENTER);
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(Color.WHITE);
        
        // Title
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(color);
        
        // Value
        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 32));
        valueLabel.setForeground(Color.DARK_GRAY);
        valuePanel.add(valueLabel);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valuePanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void initRecentActivity() {
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBorder(BorderFactory.createTitledBorder("Aktivitas Terbaru"));
        
        String[] columns = {"Waktu", "Aktivitas", "User"};
        Object[][] data = {
            {"10:30", "Pasien baru mendaftar", "Budi Santoso"},
            {"09:45", "Konsultasi dengan Dr. Andi", "Siti Rahayu"},
            {"08:15", "Update data dokter", "Admin"},
            {"Kemarin", "Pembayaran lunas", "Dewi Anggraini"}
        };
        
        JTable table = new JTable(data, columns);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        
        activityPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Letakkan di SOUTH dengan height terbatas
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(activityPanel, BorderLayout.CENTER);
        wrapper.setPreferredSize(new Dimension(0, 200)); // Fixed height
        
        add(wrapper, BorderLayout.SOUTH);
    }
    
    // Method untuk update statistik (nanti dipanggil dari controller)
    public void updateStats(int dokter, int pasien, int konsultasi) {
        lblJumlahDokter.setText(String.valueOf(dokter));
        lblJumlahPasien.setText(String.valueOf(pasien));
        lblJumlahKonsultasiHariIni.setText(String.valueOf(konsultasi));
    }
    
    public void setWelcomeMessage(String username, String role) {
        lblWelcome.setText("Selamat Datang, " + username + " (" + role + ")!");
    }
    
    public JButton getBtnRefresh() {
        return btnRefresh;
    }
}