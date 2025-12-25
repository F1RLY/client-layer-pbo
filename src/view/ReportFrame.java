// File: view/ReportFrame.java
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import controller.ReportController;
import model.User;

public class ReportFrame extends JFrame {
    private ReportController controller;
    private JTextArea txtReport;
    private JComboBox<String> cmbReportType;
    private JTextField txtStartDate, txtEndDate;
    private JButton btnGenerate, btnExport, btnPrint;
    
    public ReportFrame(User user) {
        controller = new ReportController();
        initComponents();
        setupFrame();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Control Panel
        JPanel controlPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Report Type
        gbc.gridx = 0; gbc.gridy = 0;
        controlPanel.add(new JLabel("Jenis Laporan:"), gbc);
        gbc.gridx = 1;
        cmbReportType = new JComboBox<>(new String[]{
            "Laporan Harian",
            "Laporan Bulanan",
            "Laporan Pasien Baru"
        });
        controlPanel.add(cmbReportType, gbc);
        
        // Start Date
        gbc.gridx = 0; gbc.gridy = 1;
        controlPanel.add(new JLabel("Tanggal Mulai:"), gbc);
        gbc.gridx = 1;
        txtStartDate = new JTextField(LocalDate.now().minusDays(7).toString());
        controlPanel.add(txtStartDate, gbc);
        
        // End Date
        gbc.gridx = 0; gbc.gridy = 2;
        controlPanel.add(new JLabel("Tanggal Akhir:"), gbc);
        gbc.gridx = 1;
        txtEndDate = new JTextField(LocalDate.now().toString());
        controlPanel.add(txtEndDate, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        btnGenerate = new JButton("Generate");
        btnExport = new JButton("Export ke File");
        btnPrint = new JButton("Print Preview");
        
        btnGenerate.addActionListener(e -> generateReport());
        btnExport.addActionListener(e -> exportReport());
        btnPrint.addActionListener(e -> printReport());
        
        buttonPanel.add(btnGenerate);
        buttonPanel.add(btnExport);
        buttonPanel.add(btnPrint);
        
        controlPanel.add(buttonPanel, gbc);
        
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        
        // Report Display Area
        JPanel reportPanel = new JPanel(new BorderLayout());
        reportPanel.setBorder(BorderFactory.createTitledBorder("Hasil Laporan"));
        
        txtReport = new JTextArea(20, 60);
        txtReport.setEditable(false);
        txtReport.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(txtReport);
        
        reportPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(reportPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void setupFrame() {
        setTitle("Generate Laporan");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private void generateReport() {
        try {
            String reportType = (String) cmbReportType.getSelectedItem();
            String reportContent = "";
            
            switch (reportType) {
                case "Laporan Harian":
                    LocalDate date = LocalDate.parse(txtStartDate.getText());
                    reportContent = controller.generateLaporanHarian(date);
                    break;
                    
                case "Laporan Bulanan":
                    LocalDate startDate = LocalDate.parse(txtStartDate.getText());
                    int year = startDate.getYear();
                    int month = startDate.getMonthValue();
                    reportContent = controller.generateLaporanBulanan(year, month);
                    break;
                    
                case "Laporan Pasien Baru":
                    LocalDate start = LocalDate.parse(txtStartDate.getText());
                    LocalDate end = LocalDate.parse(txtEndDate.getText());
                    reportContent = controller.generateLaporanPasienBaru(start, end);
                    break;
            }
            
            txtReport.setText(reportContent);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error generating report: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exportReport() {
        if (txtReport.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Generate laporan terlebih dahulu!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Laporan");
        
        // Set default filename based on report type
        String reportType = (String) cmbReportType.getSelectedItem();
        String defaultName = "laporan_" + reportType.toLowerCase().replace(" ", "_") 
                           + "_" + LocalDate.now() + ".txt";
        fileChooser.setSelectedFile(new File(defaultName));
        
        // Add file filters
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
            }
            
            @Override
            public String getDescription() {
                return "Text Files (*.txt)";
            }
        });
        
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".html");
            }
            
            @Override
            public String getDescription() {
                return "HTML Files (*.html)";
            }
        });
        
        int result = fileChooser.showSaveDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filePath = file.getAbsolutePath();
            
            // Add extension if not present
            javax.swing.filechooser.FileFilter filter = fileChooser.getFileFilter();
            if (filter.getDescription().contains("HTML") && !filePath.endsWith(".html")) {
                filePath += ".html";
            } else if (filter.getDescription().contains("Text") && !filePath.endsWith(".txt")) {
                filePath += ".txt";
            }
            
            try {
                boolean success;
                if (filePath.endsWith(".html")) {
                    success = controller.exportToHTML(txtReport.getText(), filePath);
                } else {
                    success = controller.exportToTextFile(txtReport.getText(), filePath);
                }
                
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Laporan berhasil diexport ke:\n" + filePath,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                }
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error exporting: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void printReport() {
        if (txtReport.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Tidak ada laporan untuk diprint!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            txtReport.print();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error printing: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}