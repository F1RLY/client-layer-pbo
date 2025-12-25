// File: controller/PasienController.java
package controller;

import model.Pasien;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller untuk manajemen pasien
 */
public class PasienController extends BaseController<Pasien> {
    private List<Pasien> pasienList = new ArrayList<>();
    private Integer lastId = 0;
    
    // Constructor
    public PasienController() {
        // Inisialisasi data dummy untuk testing
        initializeDummyData();
    }
    
    private void initializeDummyData() {
        // Data dummy pasien
        create(new Pasien("Budi Santoso", "3201010101010001", 
                LocalDate.of(1990, 5, 15), "Jl. Merdeka No. 10", "081234567890"));
        create(new Pasien("Siti Rahayu", "3201010101010002", 
                LocalDate.of(1985, 8, 22), "Jl. Sudirman No. 45", "081298765432"));
        create(new Pasien("Ahmad Hidayat", "3201010101010003", 
                LocalDate.of(1978, 3, 10), "Jl. Gatot Subroto No. 23", "085712345678"));
    }
    
    @Override
    public boolean create(Pasien pasien) {
        try {
            // Validasi
            if (!validate(pasien)) {
                return false;
            }
            
            // Generate ID
            pasien.setId(++lastId);
            
            // Simpan ke list
            pasienList.add(pasien);
            
            System.out.println("Pasien berhasil ditambahkan: " + pasien.getNama());
            return true;
            
        } catch (Exception e) {
            handleException(e, "create pasien");
            return false;
        }
    }
    
    @Override
    public Pasien read(Integer id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("ID tidak boleh null");
            }
            
            return pasienList.stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            
        } catch (Exception e) {
            handleException(e, "read pasien");
            return null;
        }
    }
    
    @Override
    public List<Pasien> readAll() {
        return new ArrayList<>(pasienList);
    }
    
    /**
     * Cari pasien berdasarkan nama
     */
    public List<Pasien> searchByName(String nama) {
        try {
            if (nama == null || nama.trim().isEmpty()) {
                return readAll();
            }
            
            String searchTerm = nama.toLowerCase().trim();
            return pasienList.stream()
                    .filter(p -> p.getNama().toLowerCase().contains(searchTerm))
                    .collect(Collectors.toList());
            
        } catch (Exception e) {
            handleException(e, "search pasien by name");
            return new ArrayList<>();
        }
    }
    
    /**
     * Cari pasien berdasarkan NIK
     */
    public Pasien searchByNIK(String nik) {
        try {
            if (nik == null || nik.trim().isEmpty()) {
                return null;
            }
            
            return pasienList.stream()
                    .filter(p -> nik.equals(p.getNik()))
                    .findFirst()
                    .orElse(null);
            
        } catch (Exception e) {
            handleException(e, "search pasien by NIK");
            return null;
        }
    }
    
    @Override
    public boolean update(Pasien pasien) {
        try {
            if (pasien == null || pasien.getId() == null) {
                throw new IllegalArgumentException("Pasien atau ID tidak valid");
            }
            
            if (!validate(pasien)) {
                return false;
            }
            
            // Cari pasien yang akan diupdate
            Pasien existing = read(pasien.getId());
            if (existing == null) {
                throw new IllegalArgumentException("Pasien tidak ditemukan");
            }
            
            // Update data
            existing.setNama(pasien.getNama());
            existing.setNik(pasien.getNik());
            existing.setTanggalLahir(pasien.getTanggalLahir());
            existing.setAlamat(pasien.getAlamat());
            existing.setNoTelepon(pasien.getNoTelepon());
            existing.setJenisKelamin(pasien.getJenisKelamin());
            existing.setGolonganDarah(pasien.getGolonganDarah());
            existing.setAlergi(pasien.getAlergi());
            
            System.out.println("Pasien berhasil diupdate: " + pasien.getNama());
            return true;
            
        } catch (IllegalArgumentException e) {
            System.err.println("Update gagal: " + e.getMessage());
            return false;
        } catch (Exception e) {
            handleException(e, "update pasien");
            return false;
        }
    }
    
    @Override
    public boolean delete(Integer id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("ID tidak boleh null");
            }
            
            Pasien pasien = read(id);
            if (pasien == null) {
                throw new IllegalArgumentException("Pasien tidak ditemukan");
            }
            
            boolean removed = pasienList.removeIf(p -> p.getId().equals(id));
            
            if (removed) {
                System.out.println("Pasien berhasil dihapus: ID " + id);
            }
            
            return removed;
            
        } catch (IllegalArgumentException e) {
            System.err.println("Delete gagal: " + e.getMessage());
            return false;
        } catch (Exception e) {
            handleException(e, "delete pasien");
            return false;
        }
    }
    
    @Override
    protected boolean validate(Pasien pasien) {
        try {
            // Validasi nama
            if (pasien.getNama() == null || pasien.getNama().trim().isEmpty()) {
                throw new IllegalArgumentException("Nama pasien tidak boleh kosong");
            }
            
            if (pasien.getNama().trim().length() < 3) {
                throw new IllegalArgumentException("Nama minimal 3 karakter");
            }
            
            // Validasi NIK
            if (pasien.getNik() == null || pasien.getNik().trim().isEmpty()) {
                throw new IllegalArgumentException("NIK tidak boleh kosong");
            }
            
            if (!pasien.getNik().matches("\\d{16}")) {
                throw new IllegalArgumentException("NIK harus 16 digit angka");
            }
            
            // Validasi tanggal lahir
            if (pasien.getTanggalLahir() == null) {
                throw new IllegalArgumentException("Tanggal lahir tidak boleh kosong");
            }
            
            if (pasien.getTanggalLahir().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Tanggal lahir tidak boleh di masa depan");
            }
            
            // Validasi usia
            int usia = Period.between(pasien.getTanggalLahir(), LocalDate.now()).getYears();
            if (usia > 120) {
                throw new IllegalArgumentException("Usia tidak valid (maksimal 120 tahun)");
            }
            
            // Validasi nomor telepon
            if (pasien.getNoTelepon() != null && !pasien.getNoTelepon().trim().isEmpty()) {
                String telp = pasien.getNoTelepon().trim();
                if (!telp.matches("08\\d{9,12}")) {
                    throw new IllegalArgumentException("Format nomor telepon tidak valid");
                }
            }
            
            return true;
            
        } catch (IllegalArgumentException e) {
            System.err.println("Validasi gagal: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Mendapatkan statistik pasien
     */
    public String getStatistics() {
        int total = pasienList.size();
        long usiaDewasa = pasienList.stream()
                .filter(p -> {
                    if (p.getTanggalLahir() == null) return false;
                    int usia = Period.between(p.getTanggalLahir(), LocalDate.now()).getYears();
                    return usia >= 17;
                })
                .count();
        
        return String.format("Total Pasien: %d\nDewasa (≥17 tahun): %d\nAnak-anak: %d", 
                total, usiaDewasa, total - usiaDewasa);
    }
}