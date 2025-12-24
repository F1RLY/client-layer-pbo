package controller;

import model.Dokter;
import service.DokterService;
import service.impl.DokterServiceImpl;
import view.panels.DokterPanel;
import javax.swing.*;
import java.util.List;

public class DokterController {
    private DokterPanel view;
    private DokterService dokterService;
    
    public DokterController(DokterPanel view) {
        this.view = view;
        this.dokterService = new DokterServiceImpl();
        initController();
    }
    
    private void initController() {
        // Load data ke table
        loadDataToTable();
        
        // Attach button listeners
        attachListeners();
    }
    
    private void loadDataToTable() {
        List<Dokter> dokterList = dokterService.getAllDokter();
        
        // Clear table
        view.getTableModel().setRowCount(0);
        
        // Add data to table
        for (Dokter dokter : dokterList) {
            Object[] row = {
                dokter.getId(),
                dokter.getNama(),
                dokter.getSpesialis(),
                dokter.getNoTelp(),
                dokter.getAlamat()
            };
            view.getTableModel().addRow(row);
        }
    }
    
    private void attachListeners() {
        // Tambah button
        view.getBtnTambah().addActionListener(e -> tambahDokter());
        
        // Update button
        view.getBtnUpdate().addActionListener(e -> updateDokter());
        
        // Hapus button
        view.getBtnHapus().addActionListener(e -> hapusDokter());
        
        // Clear button
        view.getBtnClear().addActionListener(e -> clearForm());
        
        // Table selection listener sudah ada di view
    }
    
    private void tambahDokter() {
        try {
            // Get data from form
            String nama = view.getTxtNama().getText().trim();
            String spesialis = view.getTxtSpesialis().getText().trim();
            String noTelp = view.getTxtNoTelp().getText().trim();
            String alamat = view.getTxtAlamat().getText().trim();
            
            // Validation
            if (nama.isEmpty() || spesialis.isEmpty()) {
                JOptionPane.showMessageDialog(view, 
                    "Nama dan Spesialis tidak boleh kosong!", 
                    "Validasi Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create new Dokter
            Dokter dokter = new Dokter(nama, spesialis, noTelp, alamat);
            
            // Add to service
            dokterService.tambahDokter(dokter);
            
            // Refresh table
            loadDataToTable();
            
            // Clear form
            clearForm();
            
            JOptionPane.showMessageDialog(view, 
                "Dokter berhasil ditambahkan!", 
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, 
                "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateDokter() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(view, 
                    "Pilih dokter yang akan diupdate!", 
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Get ID from table
            int id = (int) view.getTableModel().getValueAt(selectedRow, 0);
            
            // Get data from form
            String nama = view.getTxtNama().getText().trim();
            String spesialis = view.getTxtSpesialis().getText().trim();
            String noTelp = view.getTxtNoTelp().getText().trim();
            String alamat = view.getTxtAlamat().getText().trim();
            
            // Validation
            if (nama.isEmpty() || spesialis.isEmpty()) {
                JOptionPane.showMessageDialog(view, 
                    "Nama dan Spesialis tidak boleh kosong!", 
                    "Validasi Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Get existing dokter and update
            Dokter dokter = dokterService.getDokterById(id);
            if (dokter == null) {
                JOptionPane.showMessageDialog(view, 
                    "Dokter tidak ditemukan!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            dokter.setNama(nama);
            dokter.setSpesialis(spesialis);
            dokter.setNoTelp(noTelp);
            dokter.setAlamat(alamat);
            
            // Update in service
            dokterService.updateDokter(dokter);
            
            // Refresh table
            loadDataToTable();
            
            JOptionPane.showMessageDialog(view, 
                "Data dokter berhasil diupdate!", 
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, 
                "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void hapusDokter() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(view, 
                    "Pilih dokter yang akan dihapus!", 
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(view, 
                "Yakin ingin menghapus dokter ini?", 
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            
            // Get ID from table
            int id = (int) view.getTableModel().getValueAt(selectedRow, 0);
            
            // Delete from service
            dokterService.hapusDokter(id);
            
            // Refresh table
            loadDataToTable();
            
            // Clear form
            clearForm();
            
            JOptionPane.showMessageDialog(view, 
                "Dokter berhasil dihapus!", 
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, 
                "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        view.getTxtNama().setText("");
        view.getTxtSpesialis().setText("");
        view.getTxtNoTelp().setText("");
        view.getTxtAlamat().setText("");
        view.getTable().clearSelection();
    }
    
    // Method untuk load selected row ke form (dipanggil dari view)
    public void loadSelectedDokterToForm(int row) {
        if (row >= 0) {
            int id = (int) view.getTableModel().getValueAt(row, 0);
            Dokter dokter = dokterService.getDokterById(id);
            
            if (dokter != null) {
                view.getTxtNama().setText(dokter.getNama());
                view.getTxtSpesialis().setText(dokter.getSpesialis());
                view.getTxtNoTelp().setText(dokter.getNoTelp());
                view.getTxtAlamat().setText(dokter.getAlamat());
            }
        }
    }
}