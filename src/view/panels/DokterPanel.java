package view.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DokterPanel extends JPanel {
    private Color backgroundColor;
    
    public DokterPanel(Color bgColor) {
        this.backgroundColor = bgColor;
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(backgroundColor);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Title
        JLabel titleLabel = new JLabel("👨‍⚕️ MANAJEMEN DATA DOKTER");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Main content split
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(350);
        splitPane.setDividerSize(3);
        
        // Left: Form
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        formPanel.add(createForm(), BorderLayout.CENTER);
        
        // Right: Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        tablePanel.add(createTable(), BorderLayout.CENTER);
        
        splitPane.setLeftComponent(formPanel);
        splitPane.setRightComponent(tablePanel);
        
        add(titleLabel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Labels
        String[] labels = {"Nama Dokter:", "Spesialis:", "No. Telepon:", "Alamat:"};
        JTextField[] fields = new JTextField[4];
        
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.anchor = GridBagConstraints.WEST;
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Arial", Font.BOLD, 12));
            panel.add(label, gbc);
            
            gbc.gridx = 1;
            fields[i] = new JTextField(15);
            fields[i].setPreferredSize(new Dimension(200, 30));
            panel.add(fields[i], gbc);
        }
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        String[] buttonNames = {"➕ Tambah", "✏️ Update", "🗑️ Hapus", "🔄 Reset"};
        Color[] buttonColors = {
            new Color(39, 174, 96),
            new Color(41, 128, 185),
            new Color(231, 76, 60),
            new Color(149, 165, 166)
        };
        
        for (int i = 0; i < buttonNames.length; i++) {
            JButton btn = new JButton(buttonNames[i]);
            btn.setBackground(buttonColors[i]);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 12));
            btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            buttonPanel.add(btn);
        }
        
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JScrollPane createTable() {
        String[] columns = {"ID", "NAMA", "SPESIALIS", "TELEPON", "ALAMAT"};
        Object[][] data = {
            {1, "Dr. Andi Wijaya", "Umum", "08123456789", "Jl. Merdeka No.1"},
            {2, "Dr. Siti Rahayu", "Anak", "08234567890", "Jl. Sudirman No.45"},
            {3, "Dr. Budi Santoso", "Bedah", "08345678901", "Jl. Gatot Subroto No.12"}
        };
        
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(245, 245, 245));
        
        return new JScrollPane(table);
    }
}