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
        
        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.setBackground(new Color(41, 128, 185));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 14));
        btnRefresh.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        headerPanel.add(lblWelcome, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);
        
        // Stats Cards - PAKAI TEXT ICON
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        
        statsPanel.add(createCard("DOKTER", "12", new Color(41, 128, 185), "D"));
        statsPanel.add(createCard("PASIEN", "156", new Color(39, 174, 96), "P"));
        statsPanel.add(createCard("KONSULTASI", "8", new Color(231, 76, 60), "K"));
        statsPanel.add(createCard("PENDAPATAN", "Rp 12,5jt", new Color(243, 156, 18), "Rp"));
        
        // Activity Panel
        JPanel activityPanel = createActivityPanel();
        
        // Add all
        add(headerPanel, BorderLayout.NORTH);
        add(statsPanel, BorderLayout.CENTER);
        add(activityPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createCard(String title, String value, Color color, String iconText) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Icon Panel (lingkaran dengan teks)
        JPanel iconPanel = new JPanel();
        iconPanel.setPreferredSize(new Dimension(50, 50));
        iconPanel.setBackground(color);
        iconPanel.setLayout(new GridBagLayout());
        iconPanel.setBorder(BorderFactory.createLineBorder(color.darker(), 2));
        
        JLabel iconLabel = new JLabel(iconText);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 16));
        iconLabel.setForeground(Color.WHITE);
        iconPanel.add(iconLabel);
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(100, 100, 100));
        
        // Value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(new Color(60, 60, 60));
        
        // Layout
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(iconPanel, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
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
        
        // Center align untuk kolom tertentu
        table.getColumnModel().getColumn(0).setCellRenderer(new CenterCellRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Custom cell renderer untuk center align
    class CenterCellRenderer extends DefaultTableCellRenderer {
        public CenterCellRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    }
    
    // Custom cell renderer untuk status
    class StatusCellRenderer extends DefaultTableCellRenderer {
        public StatusCellRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            String status = value.toString();
            if (status.equals("Selesai") || status.equals("Lunas")) {
                setBackground(new Color(220, 255, 220)); // Hijau muda
                setForeground(new Color(0, 100, 0));
            } else if (status.equals("Berlangsung")) {
                setBackground(new Color(255, 255, 200)); // Kuning muda
                setForeground(new Color(153, 102, 0));
            } else {
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
            }
            
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }
            
            return this;
        }
    }

    

    
}