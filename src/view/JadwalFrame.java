// File: view/JadwalFrame.java
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import controller.JadwalController;
import controller.DokterController;
import model.JadwalDokter;
import model.Dokter;
import java.time.DayOfWeek;
import model.User;

public class JadwalFrame extends JFrame {
    private JadwalController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public JadwalFrame(User user) {
        controller = new JadwalController();
        initComponents();
        setupFrame();
        loadData();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Hapus");
        JButton btnRefresh = new JButton("Refresh");
        
        btnAdd.addActionListener(e -> addJadwal());
        btnEdit.addActionListener(e -> editJadwal());
        btnDelete.addActionListener(e -> deleteJadwal());
        btnRefresh.addActionListener(e -> loadData());
        
        toolbar.add(btnAdd);
        toolbar.add(btnEdit);
        toolbar.add(btnDelete);
        toolbar.add(btnRefresh);
        
        mainPanel.add(toolbar, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Dokter", "Hari", "Jam Mulai", "Jam Selesai", "Ruangan"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void setupFrame() {
        setTitle("Jadwal Dokter");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        
        for (JadwalDokter jadwal : controller.readAll()) {
            Object[] row = {
                jadwal.getId(),
                jadwal.getDokter().getNama(),
                convertDayOfWeek(jadwal.getHari()),
                jadwal.getJamMulai(),
                jadwal.getJamSelesai(),
                jadwal.getRuangan()
            };
            tableModel.addRow(row);
        }
    }
    
    private String convertDayOfWeek(DayOfWeek day) {
        switch(day) {
            case MONDAY: return "SENIN";
            case TUESDAY: return "SELASA";
            case WEDNESDAY: return "RABU";
            case THURSDAY: return "KAMIS";
            case FRIDAY: return "JUMAT";
            case SATURDAY: return "SABTU";
            case SUNDAY: return "MINGGU";
            default: return day.toString();
        }
    }
    
    private void addJadwal() {
        new JadwalFormDialog(this, null).setVisible(true);
        loadData();
    }
    
    private void editJadwal() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih jadwal");
            return;
        }
        
        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        JadwalDokter jadwal = controller.read(id);
        
        if (jadwal != null) {
            new JadwalFormDialog(this, jadwal).setVisible(true);
            loadData();
        }
    }
    
    private void deleteJadwal() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih jadwal");
            return;
        }
        
        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Hapus jadwal ini?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.delete(id)) {
                JOptionPane.showMessageDialog(this, "Berhasil dihapus");
                loadData();
            }
        }
    }
}

class JadwalFormDialog extends JDialog {
    private JadwalDokter jadwal;
    private JadwalController controller;
    
    private JComboBox<Dokter> cmbDokter;
    private JComboBox<String> cmbHari;
    private JTextField txtJamMulai, txtJamSelesai, txtRuangan;
    
    public JadwalFormDialog(JFrame parent, JadwalDokter jadwal) {
        super(parent, "Form Jadwal", true);
        this.jadwal = jadwal;
        this.controller = new JadwalController();
        
        initComponents();
        setupDialog();
        loadDokterData();
        
        if (jadwal != null) {
            loadData();
        }
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        
        mainPanel.add(new JLabel("Dokter:"));
        cmbDokter = new JComboBox<>();
        mainPanel.add(cmbDokter);
        
        mainPanel.add(new JLabel("Hari:"));
        cmbHari = new JComboBox<>(new String[]{"SENIN", "SELASA", "RABU", "KAMIS", "JUMAT", "SABTU", "MINGGU"});
        mainPanel.add(cmbHari);
        
        mainPanel.add(new JLabel("Jam Mulai (HH:mm):"));
        txtJamMulai = new JTextField("08:00");
        mainPanel.add(txtJamMulai);
        
        mainPanel.add(new JLabel("Jam Selesai (HH:mm):"));
        txtJamSelesai = new JTextField("12:00");
        mainPanel.add(txtJamSelesai);
        
        mainPanel.add(new JLabel("Ruangan:"));
        txtRuangan = new JTextField();
        mainPanel.add(txtRuangan);
        
        JPanel buttonPanel = new JPanel();
        JButton btnSave = new JButton("Simpan");
        JButton btnCancel = new JButton("Batal");
        
        btnSave.addActionListener(e -> saveData());
        btnCancel.addActionListener(e -> dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupDialog() {
        setSize(400, 250);
        setLocationRelativeTo(getParent());
    }
    
    private void loadDokterData() {
        DokterController dokterCtrl = new DokterController();
        cmbDokter.removeAllItems();
        
        for (Dokter dokter : dokterCtrl.getAktifDokter()) {
            cmbDokter.addItem(dokter);
        }
    }
    
    private void loadData() {
        if (jadwal != null) {
            // Set dokter
            for (int i = 0; i < cmbDokter.getItemCount(); i++) {
                if (cmbDokter.getItemAt(i).getId().equals(jadwal.getDokter().getId())) {
                    cmbDokter.setSelectedIndex(i);
                    break;
                }
            }
            
            // Set hari
            cmbHari.setSelectedItem(convertDayOfWeek(jadwal.getHari()));
            
            txtJamMulai.setText(jadwal.getJamMulai().toString());
            txtJamSelesai.setText(jadwal.getJamSelesai().toString());
            txtRuangan.setText(jadwal.getRuangan());
        }
    }
    
    private String convertDayOfWeek(DayOfWeek day) {
        switch(day) {
            case MONDAY: return "SENIN";
            case TUESDAY: return "SELASA";
            case WEDNESDAY: return "RABU";
            case THURSDAY: return "KAMIS";
            case FRIDAY: return "JUMAT";
            case SATURDAY: return "SABTU";
            case SUNDAY: return "MINGGU";
            default: return "SENIN";
        }
    }
    
    private DayOfWeek convertToDayOfWeek(String hari) {
        switch(hari) {
            case "SENIN": return DayOfWeek.MONDAY;
            case "SELASA": return DayOfWeek.TUESDAY;
            case "RABU": return DayOfWeek.WEDNESDAY;
            case "KAMIS": return DayOfWeek.THURSDAY;
            case "JUMAT": return DayOfWeek.FRIDAY;
            case "SABTU": return DayOfWeek.SATURDAY;
            case "MINGGU": return DayOfWeek.SUNDAY;
            default: return DayOfWeek.MONDAY;
        }
    }
    
    private void saveData() {
        // Validasi
        if (cmbDokter.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih dokter");
            return;
        }
        
        try {
            java.time.LocalTime.parse(txtJamMulai.getText());
            java.time.LocalTime.parse(txtJamSelesai.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Format jam tidak valid (gunakan HH:mm)");
            return;
        }
        
        // Simpan
        if (jadwal == null) {
            jadwal = new JadwalDokter();
        }
        
        jadwal.setDokter((Dokter) cmbDokter.getSelectedItem());
        jadwal.setHari(convertToDayOfWeek((String) cmbHari.getSelectedItem()));
        jadwal.setJamMulai(java.time.LocalTime.parse(txtJamMulai.getText()));
        jadwal.setJamSelesai(java.time.LocalTime.parse(txtJamSelesai.getText()));
        jadwal.setRuangan(txtRuangan.getText());
        
        boolean success;
        if (jadwal.getId() == null) {
            success = controller.create(jadwal);
        } else {
            success = controller.update(jadwal);
        }
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Data disimpan");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan");
        }
    }
}