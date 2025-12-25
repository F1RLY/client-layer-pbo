// File: controller/DokterController.java
package controller;

import model.Dokter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller untuk manajemen dokter
 */
public class DokterController extends BaseController<Dokter> {
    private List<Dokter> dokterList = new ArrayList<>();
    private Integer lastId = 0;
    
    // Constructor
    public DokterController() {
        initializeDummyData();
    }
    
    private void initializeDummyData() {
        // Data dummy dokter
        create(new Dokter("D001", "Dr. Budi Santoso", "Umum"));
        create(new Dokter("D002", "Dr. Siti Rahayu", "Anak"));
        create(new Dokter("D003", "Dr. Ahmad Hidayat", "Bedah"));
        create(new Dokter("D004", "Dr. Maya Sari", "Kandungan"));
    }
    
    @Override
    public boolean create(Dokter dokter) {
        try {
            if (!validate(dokter)) {
                return false;
            }
            
            // Cek kode dokter unik
            if (dokterList.stream().anyMatch(d -> d.getKodeDokter().equals(dokter.getKodeDokter()))) {
                throw new IllegalArgumentException("Kode dokter sudah digunakan");
            }
            
            dokter.setId(++lastId);
            dokterList.add(dokter);
            
            System.out.println("Dokter berhasil ditambahkan: " + dokter.getNama());
            return true;
            
        } catch (IllegalArgumentException e) {
            System.err.println("Create gagal: " + e.getMessage());
            return false;
        } catch (Exception e) {
            handleException(e, "create dokter");
            return false;
        }
    }
    
    @Override
    public Dokter read(Integer id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("ID tidak boleh null");
            }
            
            return dokterList.stream()
                    .filter(d -> d.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            
        } catch (Exception e) {
            handleException(e, "read dokter");
            return null;
        }
    }
    
    /**
     * Cari dokter berdasarkan kode dokter
     */
    public Dokter readByKode(String kodeDokter) {
        try {
            if (kodeDokter == null || kodeDokter.trim().isEmpty()) {
                return null;
            }
            
            return dokterList.stream()
                    .filter(d -> kodeDokter.equals(d.getKodeDokter()))
                    .findFirst()
                    .orElse(null);
            
        } catch (Exception e) {
            handleException(e, "read dokter by kode");
            return null;
        }
    }
    
    @Override
    public List<Dokter> readAll() {
        return new ArrayList<>(dokterList);
    }
    
    /**
     * Cari dokter berdasarkan spesialisasi
     */
    public List<Dokter> searchBySpesialisasi(String spesialisasi) {
        try {
            if (spesialisasi == null || spesialisasi.trim().isEmpty()) {
                return readAll();
            }
            
            String searchTerm = spesialisasi.toLowerCase().trim();
            return dokterList.stream()
                    .filter(d -> d.getSpesialisasi().toLowerCase().contains(searchTerm))
                    .collect(Collectors.toList());
            
        } catch (Exception e) {
            handleException(e, "search dokter by spesialisasi");
            return new ArrayList<>();
        }
    }
    
    /**
     * Cari dokter aktif
     */
    public List<Dokter> getAktifDokter() {
        return dokterList.stream()
                .filter(d -> "AKTIF".equals(d.getStatus()))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean update(Dokter dokter) {
        try {
            if (dokter == null || dokter.getId() == null) {
                throw new IllegalArgumentException("Dokter atau ID tidak valid");
            }
            
            if (!validate(dokter)) {
                return false;
            }
            
            Dokter existing = read(dokter.getId());
            if (existing == null) {
                throw new IllegalArgumentException("Dokter tidak ditemukan");
            }
            
            // Cek kode dokter unik (kecuali untuk dirinya sendiri)
            if (dokterList.stream()
                .anyMatch(d -> d.getKodeDokter().equals(dokter.getKodeDokter()) 
                            && !d.getId().equals(dokter.getId()))) {
                throw new IllegalArgumentException("Kode dokter sudah digunakan oleh dokter lain");
            }
            
            // Update data
            existing.setKodeDokter(dokter.getKodeDokter());
            existing.setNama(dokter.getNama());
            existing.setSpesialisasi(dokter.getSpesialisasi());
            existing.setNoTelepon(dokter.getNoTelepon());
            existing.setEmail(dokter.getEmail());
            existing.setStatus(dokter.getStatus());
            
            System.out.println("Dokter berhasil diupdate: " + dokter.getNama());
            return true;
            
        } catch (IllegalArgumentException e) {
            System.err.println("Update gagal: " + e.getMessage());
            return false;
        } catch (Exception e) {
            handleException(e, "update dokter");
            return false;
        }
    }
    
    @Override
    public boolean delete(Integer id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("ID tidak boleh null");
            }
            
            Dokter dokter = read(id);
            if (dokter == null) {
                throw new IllegalArgumentException("Dokter tidak ditemukan");
            }
            
            // Soft delete: ubah status menjadi NONAKTIF
            dokter.setStatus("NONAKTIF");
            
            System.out.println("Status dokter diubah menjadi NONAKTIF: " + dokter.getNama());
            return true;
            
        } catch (IllegalArgumentException e) {
            System.err.println("Delete gagal: " + e.getMessage());
            return false;
        } catch (Exception e) {
            handleException(e, "delete dokter");
            return false;
        }
    }
    
    @Override
    protected boolean validate(Dokter dokter) {
        try {
            // Validasi kode dokter
            if (dokter.getKodeDokter() == null || dokter.getKodeDokter().trim().isEmpty()) {
                throw new IllegalArgumentException("Kode dokter tidak boleh kosong");
            }
            
            if (!dokter.getKodeDokter().matches("[A-Z]\\d{3}")) {
                throw new IllegalArgumentException("Kode dokter harus huruf diikuti 3 angka (contoh: D001)");
            }
            
            // Validasi nama
            if (dokter.getNama() == null || dokter.getNama().trim().isEmpty()) {
                throw new IllegalArgumentException("Nama dokter tidak boleh kosong");
            }
            
            if (!dokter.getNama().trim().startsWith("Dr. ")) {
                throw new IllegalArgumentException("Nama dokter harus diawali dengan 'Dr. '");
            }
            
            // Validasi spesialisasi
            if (dokter.getSpesialisasi() == null || dokter.getSpesialisasi().trim().isEmpty()) {
                throw new IllegalArgumentException("Spesialisasi tidak boleh kosong");
            }
            
            // Validasi status
            if (dokter.getStatus() == null) {
                dokter.setStatus("AKTIF");
            } else if (!dokter.getStatus().equals("AKTIF") && !dokter.getStatus().equals("NONAKTIF")) {
                throw new IllegalArgumentException("Status harus AKTIF atau NONAKTIF");
            }
            
            return true;
            
        } catch (IllegalArgumentException e) {
            System.err.println("Validasi gagal: " + e.getMessage());
            return false;
        }
    }
}