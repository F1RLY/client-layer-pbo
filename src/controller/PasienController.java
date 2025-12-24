package controller;

import model.Pasien;
import service.PasienService;
import service.impl.PasienServiceImpl;
import view.panels.PasienPanel;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PasienController {
    private PasienPanel view;
    private PasienService pasienService;
    private SimpleDateFormat dateFormat;

    public PasienController(PasienPanel view) {
        this.view = view;
        this.pasienService = new PasienServiceImpl();
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        initController();
    }

    private void initController() {
        // 1. Load data pertama kali
        loadDataToTable();

        // 2. Pasang Listener ke tombol-tombol yang ada di View kamu
        attachListeners();

        // 3. Listener Klik Tabel (Penting: agar data muncul di form saat baris diklik)
        view.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = view.getTable().getSelectedRow();
                loadSelectedPasienToForm(selectedRow);
            }
        });
    }

    private void attachListeners() {
        view.getBtnTambah().addActionListener(e -> tambahPasien());
        view.getBtnUpdate().addActionListener(e -> updatePasien());
        view.getBtnHapus().addActionListener(e -> hapusPasien());
        view.getBtnClear().addActionListener(e -> clearForm());
        view.getBtnCari().addActionListener(e -> cariPasien());
        view.getBtnResetSearch().addActionListener(e -> resetSearch());
        view.getBtnRefresh().addActionListener(e -> loadDataToTable());
        view.getBtnPrint().addActionListener(e -> printData());
        view.getBtnExport().addActionListener(e -> exportData());
    }

    // --- LOGIKA LOAD DATA ---
    public void loadDataToTable() {
        try {
            List<Pasien> pasienList = pasienService.getAllPasien();
            view.getTableModel().setRowCount(0);

            for (Pasien p : pasienList) {
                Object[] row = {
                    p.getId(),
                    p.getNama(),
                    p.getNoKtp(),
                    p.getAlamat(),
                    p.getNoTelp(),
                    dateFormat.format(p.getTanggalLahir()),
                    p.getJenisKelamin()
                };
                view.getTableModel().addRow(row);
            }
            view.updateJumlahPasien(pasienService.getJumlahPasien());
        } catch (Exception e) {
            System.err.println("Error load data: " + e.getMessage());
        }
    }

    // --- LOGIKA TAMBAH ---
    private void tambahPasien() {
        try {
            Pasien p = getPasienFromForm();
            if (p.getNoKtp().length() != 16) {
                throw new Exception("No KTP harus 16 digit!");
            }
            pasienService.tambahPasien(p);
            JOptionPane.showMessageDialog(view, "Data Pasien Berhasil Disimpan!");
            loadDataToTable();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Gagal Tambah: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- LOGIKA UPDATE ---
    private void updatePasien() {
        int selectedRow = view.getTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Pilih data yang akan diubah pada tabel!");
            return;
        }

        try {
            int id = (int) view.getTableModel().getValueAt(selectedRow, 0);
            Pasien p = getPasienFromForm();
            p.setId(id); // Set ID dari tabel agar service tahu mana yang diupdate

            pasienService.updatePasien(p);
            JOptionPane.showMessageDialog(view, "Data Pasien Berhasil Diperbarui!");
            loadDataToTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Gagal Update: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- LOGIKA HAPUS ---
    private void hapusPasien() {
        int selectedRow = view.getTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Pilih data yang akan dihapus!");
            return;
        }

        int id = (int) view.getTableModel().getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(view, "Yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            pasienService.hapusPasien(id);
            loadDataToTable();
            clearForm();
            JOptionPane.showMessageDialog(view, "Data Terhapus!");
        }
    }

    // --- LOGIKA CARI ---
    private void cariPasien() {
        String keyword = view.getTxtCari().getText().trim();
        if (keyword.isEmpty()) return;

        List<Pasien> hasil = pasienService.cariPasienByNama(keyword);
        view.getTableModel().setRowCount(0);
        for (Pasien p : hasil) {
            view.getTableModel().addRow(new Object[]{
                p.getId(), p.getNama(), p.getNoKtp(), p.getAlamat(), 
                p.getNoTelp(), dateFormat.format(p.getTanggalLahir()), p.getJenisKelamin()
            });
        }
    }

    // --- LOGIKA EXPORT (Bonus Poin G Laporan) ---
    private void exportData() {
        try {
            // Sederhana: simpan ke file teks .csv
            java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.File("Laporan_Pasien.csv"));
            pw.println("ID,Nama,NoKTP,Alamat,NoTelp");
            List<Pasien> list = pasienService.getAllPasien();
            for (Pasien p : list) {
                pw.println(p.getId() + "," + p.getNama() + "," + p.getNoKtp() + "," + p.getAlamat() + "," + p.getNoTelp());
            }
            pw.close();
            JOptionPane.showMessageDialog(view, "Data berhasil diexport ke Laporan_Pasien.csv");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Export Gagal: " + e.getMessage());
        }
    }

    // --- HELPER METHODS ---
    private Pasien getPasienFromForm() throws Exception {
        String nama = view.getTxtNama().getText().trim();
        String noKtp = view.getTxtNoKtp().getText().trim();
        String alamat = view.getTxtAlamat().getText().trim();
        String noTelp = view.getTxtNoTelp().getText().trim();
        String tglStr = view.getTxtTanggalLahir().getText().trim();
        String jk = view.getCmbJenisKelamin().getSelectedItem().toString();

        if (nama.isEmpty() || noKtp.isEmpty()) throw new Exception("Nama dan No KTP wajib diisi!");

        Date tglLahir = dateFormat.parse(tglStr);
        return new Pasien(nama, noKtp, alamat, noTelp, tglLahir, jk);
    }

    public void loadSelectedPasienToForm(int row) {
        if (row >= 0) {
            view.getTxtNama().setText(view.getTableModel().getValueAt(row, 1).toString());
            view.getTxtNoKtp().setText(view.getTableModel().getValueAt(row, 2).toString());
            view.getTxtAlamat().setText(view.getTableModel().getValueAt(row, 3).toString());
            view.getTxtNoTelp().setText(view.getTableModel().getValueAt(row, 4).toString());
            view.getTxtTanggalLahir().setText(view.getTableModel().getValueAt(row, 5).toString());
            view.getCmbJenisKelamin().setSelectedItem(view.getTableModel().getValueAt(row, 6).toString());
        }
    }

    private void clearForm() {
        view.getTxtNama().setText("");
        view.getTxtNoKtp().setText("");
        view.getTxtAlamat().setText("");
        view.getTxtNoTelp().setText("");
        view.getTxtTanggalLahir().setText(dateFormat.format(new Date()));
        view.getTable().clearSelection();
    }

    private void resetSearch() {
        view.getTxtCari().setText("");
        loadDataToTable();
    }

    private void printData() {
        JOptionPane.showMessageDialog(view, "Fungsi cetak dokumen sedang disiapkan.");
    }
}