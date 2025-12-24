package view.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private Color backgroundColor;
    
    public DashboardPanel(Color bgColor) {
        this.backgroundColor = bgColor;
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(backgroundColor);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel lblWelcome = new JLabel("DASHBOARD UTAMA");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 24));
        lblWelcome.setForeground(new Color(60, 60, 60));
        
        // TOMBOL REFRESH - BIARKAN DEFAULT, JANGAN SET BACKGROUND/FOREGROUND
        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.setFont(new Font("Arial", Font.PLAIN, 14));
        
        headerPanel.add(lblWelcome, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);
        
        // Stats Cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        
        statsPanel.add(createCard("DOKTER", "12", new Color(41, 128, 185)));
        statsPanel.add(createCard("PASIEN", "156", new Color(39, 174, 96)));
        statsPanel.add(createCard("KONSULTASI", "8", new Color(231, 76, 60)));
        statsPanel.add(createCard("PENDAPATAN", "Rp 12,5jt", new Color(243, 156, 18)));
        
        // Activity Panel
        JPanel activityPanel = createActivityPanel();
        
        // Add all
        add(headerPanel, BorderLayout.NORTH);
        add(statsPanel, BorderLayout.CENTER);
        add(activityPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Title dengan icon teks dalam []
        String iconText = "";
        switch(title) {
            case "DOKTER": iconText = "[D]"; break;
            case "PASIEN": iconText = "[P]"; break;
            case "KONSULTASI": iconText = "[K]"; break;
            case "PENDAPATAN": iconText = "[$]"; break;
        }
        
        JLabel titleLabel = new JLabel(iconText + " " + title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(color);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(new Color(60, 60, 60));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Footer
        JLabel footerLabel = new JLabel("↑ 12% dari bulan lalu");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(Color.GRAY);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(footerLabel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createActivityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("AKTIVITAS TERBARU"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Table dengan data fix
        String[] columns = {"WAKTU", "AKTIVITAS", "PENANGGUNG JAWAB", "STATUS"};
        Object[][] data = {
            {"10:30", "Pendaftaran pasien baru", "Budi Santoso", "Selesai"},
            {"09:45", "Konsultasi dengan Dr. Andi", "Siti Rahayu", "Berlangsung"},
            {"08:15", "Update data dokter", "Administrator", "Selesai"},
            {"Kemarin", "Pembayaran invoice", "Dewi Anggraini", "Lunas"}
        };
        
        JTable table = new JTable(data, columns);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(245, 245, 245));
        
        // Simple center alignment tanpa custom renderer
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // WAKTU
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // STATUS
        
        // Simple status coloring
        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Arial", Font.BOLD, 12));
                
                String status = value.toString();
                if (status.equals("Selesai") || status.equals("Lunas")) {
                    c.setBackground(new Color(220, 255, 220));
                    c.setForeground(new Color(0, 100, 0));
                } else if (status.equals("Berlangsung")) {
                    c.setBackground(new Color(255, 255, 200));
                    c.setForeground(new Color(153, 102, 0));
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                }
                
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };
        
        table.getColumnModel().getColumn(3).setCellRenderer(statusRenderer);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
}