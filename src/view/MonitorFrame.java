// File: view/MonitorFrame.java
package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import controller.AntreanController;
import model.Antrean;

public class MonitorFrame extends JFrame {
    private AntreanController controller;
    private JLabel lblCurrent, lblNext1, lblNext2, lblNext3;
    private JTextArea txtLog;
    private Timer refreshTimer;
    
    public MonitorFrame() {
        controller = new AntreanController();
        initComponents();
        setupFrame();
        startAutoRefresh();
    }
    
    private void initComponents() {
        // Main panel with GridLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Header - Title
        JPanel headerPanel = new JPanel();
        JLabel lblTitle = new JLabel("MONITOR ANTREAN RUMAH SAKIT");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 100, 0)); // Dark green
        headerPanel.add(lblTitle);
        
        // Current Antrean Panel (BIG Display)
        JPanel currentPanel = new JPanel(new BorderLayout());
        currentPanel.setBorder(BorderFactory.createTitledBorder("SEDANG DIPANGGIL"));
        currentPanel.setBackground(new Color(255, 255, 200)); // Light yellow
        
        lblCurrent = new JLabel("-", SwingConstants.CENTER);
        lblCurrent.setFont(new Font("Arial", Font.BOLD, 72));
        lblCurrent.setForeground(Color.RED);
        currentPanel.add(lblCurrent, BorderLayout.CENTER);
        
        // Next Antrean Panel
        JPanel nextPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        nextPanel.setBorder(BorderFactory.createTitledBorder("ANTREAN BERIKUTNYA"));
        
        lblNext1 = createNextLabel();
        lblNext2 = createNextLabel();
        lblNext3 = createNextLabel();
        
        nextPanel.add(lblNext1);
        nextPanel.add(lblNext2);
        nextPanel.add(lblNext3);
        
        // Log Panel
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder("LOG PEMANGGILAN"));
        
        txtLog = new JTextArea(10, 40);
        txtLog.setEditable(false);
        txtLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(txtLog);
        logPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton btnRefresh = new JButton("Refresh Manual");
        JButton btnClear = new JButton("Clear Log");
        
        btnRefresh.addActionListener(e -> refreshDisplay());
        btnClear.addActionListener(e -> txtLog.setText(""));
        
        controlPanel.add(btnRefresh);
        controlPanel.add(btnClear);
        logPanel.add(controlPanel, BorderLayout.SOUTH);
        
        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(currentPanel, BorderLayout.CENTER);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(nextPanel, BorderLayout.NORTH);
        centerPanel.add(logPanel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JLabel createNextLabel() {
        JLabel label = new JLabel("-", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 36));
        label.setForeground(new Color(0, 0, 150)); // Dark blue
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        label.setOpaque(true);
        label.setBackground(new Color(230, 230, 255)); // Light blue
        return label;
    }
    
    private void setupFrame() {
        setTitle("SMARS - Monitor Ruang Tunggu");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Initial display
        refreshDisplay();
    }
    
    private void startAutoRefresh() {
        // Refresh every 5 seconds
        refreshTimer = new Timer(5000, e -> refreshDisplay());
        refreshTimer.start();
    }
    
    private void refreshDisplay() {
        try {
            // Get current called antrean
            List<Antrean> dipanggil = controller.getAntreanDipanggil();
            
            if (!dipanggil.isEmpty()) {
                // Show most recently called
                Antrean current = dipanggil.get(dipanggil.size() - 1);
                lblCurrent.setText(current.getNomorAntrean());
                
                // Log the call
                String logEntry = String.format("[%s] %s - %s (Ruang: %s)\n",
                    java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")),
                    current.getNomorAntrean(),
                    current.getPasien().getNama(),
                    current.getRuangan());
                
                txtLog.append(logEntry);
                txtLog.setCaretPosition(txtLog.getDocument().getLength());
            } else {
                lblCurrent.setText("-");
            }
            
            // Get next 3 waiting antrean
            List<Antrean> menunggu = controller.getAntreanMenunggu();
            
            if (menunggu.size() > 0) {
                lblNext1.setText(menunggu.get(0).getNomorAntrean());
            } else {
                lblNext1.setText("-");
            }
            
            if (menunggu.size() > 1) {
                lblNext2.setText(menunggu.get(1).getNomorAntrean());
            } else {
                lblNext2.setText("-");
            }
            
            if (menunggu.size() > 2) {
                lblNext3.setText(menunggu.get(2).getNomorAntrean());
            } else {
                lblNext3.setText("-");
            }
            
            // Update status in title
            setTitle(String.format("SMARS Monitor - Menunggu: %d, Dipanggil: %d - %s",
                menunggu.size(),
                dipanggil.size(),
                java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"))));
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error refreshing data: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void dispose() {
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
        super.dispose();
    }
}