// File: controller/AntreanController.java
package controller;

import model.Antrean;
import model.Pasien;
import model.Dokter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller untuk manajemen antrean (Core Business Logic)
 */
public class AntreanController extends BaseController<Antrean> {
    private List<Antrean> antreanList = new ArrayList<>();
    private Integer lastId = 0;
    private Integer counterHarian = 0;
    private LocalDate lastCounterDate;
    
    // Constructor
    public AntreanController() {
        this.lastCounterDate = LocalDate.now();
        initializeDummyData();
    }
    
    private void initializeDummyData() {
        // Inisialisasi controller lain untuk data dummy
        PasienController pasienController = new PasienController();
        DokterController dokterController = new DokterController();
        
        List<Pasien> pasienList = pasienController.readAll();
        List<Dokter> dokterList = dokterController.getAktifDokter();
        
        if (!pasienList.isEmpty() && !dokterList.isEmpty()) {
            // Buat beberapa antrean dummy
            for (int i = 0; i < 5; i++) {
                Pasien pasien = pasienList.get(i % pasienList.size());
                Dokter dokter = dokterList.get(i % dokterList.size());
                
                Antrean antrean = new Antrean(generateNomorAntrean(), pasien, dokter);
                antrean.setId(++lastId);
                antrean.setStatus(Antrean.STATUS_MENUNGGU);
                antreanList.add(antrean);
            }
        }
    }
    
    @Override
    public boolean create(Antrean antrean) {
        try {
            if (!validate(antrean)) {
                return false;
            }
            
            // Generate nomor antrean jika belum ada
            if (antrean.getNomorAntrean() == null) {
                antrean.setNomorAntrean(generateNomorAntrean());
            }
            
            antrean.setId(++lastId);
            antreanList.add(antrean);
            
            System.out.println("Antrean berhasil dibuat: " + antrean.getNomorAntrean());
            return true;
            
        } catch (Exception e) {
            handleException(e, "create antrean");
            return false;
        }
    }
    
    /**
     * Buat antrean baru untuk pasien
     */
    public Antrean buatAntreanBaru(Pasien pasien, Dokter dokter) {
        try {
            if (pasien == null || dokter == null) {
                throw new IllegalArgumentException("Pasien dan dokter harus diisi");
            }
            
            // Generate nomor antrean
            String nomorAntrean = generateNomorAntrean();
            
            // Buat antrean baru
            Antrean antrean = new Antrean(nomorAntrean, pasien, dokter);
            antrean.setId(++lastId);
            
            // Simpan
            antreanList.add(antrean);
            
            System.out.println("Antrean baru dibuat - No: " + nomorAntrean + 
                             ", Pasien: " + pasien.getNama() + 
                             ", Dokter: " + dokter.getNama());
            
            return antrean;
            
        } catch (Exception e) {
            handleException(e, "buat antrean baru");
            return null;
        }
    }
    
    @Override
    public Antrean read(Integer id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("ID tidak boleh null");
            }
            
            return antreanList.stream()
                    .filter(a -> a.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            
        } catch (Exception e) {
            handleException(e, "read antrean");
            return null;
        }
    }
    
    /**
     * Cari antrean berdasarkan nomor antrean
     */
    public Antrean readByNomor(String nomorAntrean) {
        try {
            if (nomorAntrean == null || nomorAntrean.trim().isEmpty()) {
                return null;
            }
            
            return antreanList.stream()
                    .filter(a -> nomorAntrean.equals(a.getNomorAntrean()))
                    .findFirst()
                    .orElse(null);
            
        } catch (Exception e) {
            handleException(e, "read antrean by nomor");
            return null;
        }
    }
    
    @Override
    public List<Antrean> readAll() {
        return new ArrayList<>(antreanList);
    }
    
    /**
     * Dapatkan antrean hari ini
     */
    public List<Antrean> getAntreanHariIni() {
        LocalDate today = LocalDate.now();
        
        return antreanList.stream()
                .filter(a -> a.getWaktuMasuk().toLocalDate().equals(today))
                .sorted(Comparator.comparing(Antrean::getWaktuMasuk))
                .collect(Collectors.toList());
    }
    
    /**
     * Dapatkan antrean berdasarkan status
     */
    public List<Antrean> getAntreanByStatus(String status) {
        try {
            if (status == null) {
                return new ArrayList<>();
            }
            
            return antreanList.stream()
                    .filter(a -> status.equals(a.getStatus()))
                    .sorted(Comparator.comparing(Antrean::getWaktuMasuk))
                    .collect(Collectors.toList());
            
        } catch (Exception e) {
            handleException(e, "get antrean by status");
            return new ArrayList<>();
        }
    }
    
    /**
     * Dapatkan antrean menunggu
     */
    public List<Antrean> getAntreanMenunggu() {
        return getAntreanByStatus(Antrean.STATUS_MENUNGGU);
    }
    
    /**
     * Dapatkan antrean yang sedang dipanggil
     */
    public List<Antrean> getAntreanDipanggil() {
        return getAntreanByStatus(Antrean.STATUS_DIPANGGIL);
    }
    
    /**
     * Dapatkan antrean berdasarkan dokter
     */
    public List<Antrean> getAntreanByDokter(Dokter dokter) {
        try {
            if (dokter == null) {
                return new ArrayList<>();
            }
            
            return antreanList.stream()
                    .filter(a -> dokter.equals(a.getDokter()))
                    .sorted(Comparator.comparing(Antrean::getWaktuMasuk))
                    .collect(Collectors.toList());
            
        } catch (Exception e) {
            handleException(e, "get antrean by dokter");
            return new ArrayList<>();
        }
    }
    
    /**
     * Panggil antrean berikutnya untuk dokter tertentu
     */
    public Antrean panggilAntreanBerikutnya(Dokter dokter, String ruangan) {
        try {
            if (dokter == null) {
                throw new IllegalArgumentException("Dokter harus diisi");
            }
            
            // Cari antrean menunggu tertua untuk dokter ini
            Optional<Antrean> nextAntrean = antreanList.stream()
                    .filter(a -> dokter.equals(a.getDokter()))
                    .filter(Antrean::isMenunggu)
                    .min(Comparator.comparing(Antrean::getWaktuMasuk));
            
            if (nextAntrean.isPresent()) {
                Antrean antrean = nextAntrean.get();
                antrean.panggil(ruangan);
                
                System.out.println("Antrean dipanggil - No: " + antrean.getNomorAntrean() + 
                                 ", Ruangan: " + ruangan + 
                                 ", Dokter: " + dokter.getNama());
                
                return antrean;
            }
            
            System.out.println("Tidak ada antrean menunggu untuk dokter " + dokter.getNama());
            return null;
            
        } catch (Exception e) {
            handleException(e, "panggil antrean berikutnya");
            return null;
        }
    }
    
    /**
     * Panggil antrean tertentu
     */
    public boolean panggilAntrean(String nomorAntrean, String ruangan) {
        try {
            Antrean antrean = readByNomor(nomorAntrean);
            if (antrean == null) {
                throw new IllegalArgumentException("Antrean tidak ditemukan: " + nomorAntrean);
            }
            
            if (!antrean.isMenunggu()) {
                throw new IllegalStateException("Antrean tidak dalam status MENUNGGU");
            }
            
            antrean.panggil(ruangan);
            
            System.out.println("Antrean " + nomorAntrean + " dipanggil ke ruangan " + ruangan);
            return true;
            
        } catch (Exception e) {
            handleException(e, "panggil antrean");
            return false;
        }
    }
    
    /**
     * Selesaikan antrean
     */
    public boolean selesaikanAntrean(String nomorAntrean) {
        try {
            Antrean antrean = readByNomor(nomorAntrean);
            if (antrean == null) {
                throw new IllegalArgumentException("Antrean tidak ditemukan: " + nomorAntrean);
            }
            
            if (!antrean.isDipanggil()) {
                throw new IllegalStateException("Antrean harus dalam status DIPANGGIL");
            }
            
            antrean.selesaikan();
            
            System.out.println("Antrean " + nomorAntrean + " telah selesai");
            return true;
            
        } catch (Exception e) {
            handleException(e, "selesaikan antrean");
            return false;
        }
    }
    
    /**
     * Batalkan antrean
     */
    public boolean batalkanAntrean(String nomorAntrean, String alasan) {
        try {
            Antrean antrean = readByNomor(nomorAntrean);
            if (antrean == null) {
                throw new IllegalArgumentException("Antrean tidak ditemukan: " + nomorAntrean);
            }
            
            if (antrean.isSelesai() || antrean.isBatal()) {
                throw new IllegalStateException("Antrean sudah selesai atau dibatalkan");
            }
            
            antrean.batalkan(alasan);
            
            System.out.println("Antrean " + nomorAntrean + " dibatalkan. Alasan: " + alasan);
            return true;
            
        } catch (Exception e) {
            handleException(e, "batalkan antrean");
            return false;
        }
    }
    
    @Override
    public boolean update(Antrean antrean) {
        try {
            if (antrean == null || antrean.getId() == null) {
                throw new IllegalArgumentException("Antrean atau ID tidak valid");
            }
            
            if (!validate(antrean)) {
                return false;
            }
            
            Antrean existing = read(antrean.getId());
            if (existing == null) {
                throw new IllegalArgumentException("Antrean tidak ditemukan");
            }
            
            // Update data
            existing.setNomorAntrean(antrean.getNomorAntrean());
            existing.setPasien(antrean.getPasien());
            existing.setDokter(antrean.getDokter());
            existing.setStatus(antrean.getStatus());
            existing.setRuangan(antrean.getRuangan());
            existing.setCatatan(antrean.getCatatan());
            
            // Update waktu berdasarkan status
            if (antrean.getWaktuPanggil() != null) {
                existing.setWaktuPanggil(antrean.getWaktuPanggil());
            }
            
            if (antrean.getWaktuSelesai() != null) {
                existing.setWaktuSelesai(antrean.getWaktuSelesai());
            }
            
            System.out.println("Antrean berhasil diupdate: " + antrean.getNomorAntrean());
            return true;
            
        } catch (IllegalArgumentException e) {
            System.err.println("Update gagal: " + e.getMessage());
            return false;
        } catch (Exception e) {
            handleException(e, "update antrean");
            return false;
        }
    }
    
    @Override
    public boolean delete(Integer id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("ID tidak boleh null");
            }
            
            Antrean antrean = read(id);
            if (antrean == null) {
                throw new IllegalArgumentException("Antrean tidak ditemukan");
            }
            
            // Hanya bisa hapus jika belum diproses
            if (antrean.isDipanggil() || antrean.isSelesai()) {
                throw new IllegalStateException("Tidak bisa menghapus antrean yang sudah diproses");
            }
            
            boolean removed = antreanList.removeIf(a -> a.getId().equals(id));
            
            if (removed) {
                System.out.println("Antrean berhasil dihapus: ID " + id);
            }
            
            return removed;
            
        } catch (Exception e) {
            handleException(e, "delete antrean");
            return false;
        }
    }
    
    @Override
    protected boolean validate(Antrean antrean) {
        try {
            // Validasi nomor antrean
            if (antrean.getNomorAntrean() == null || antrean.getNomorAntrean().trim().isEmpty()) {
                throw new IllegalArgumentException("Nomor antrean tidak boleh kosong");
            }
            
            // Validasi pasien
            if (antrean.getPasien() == null) {
                throw new IllegalArgumentException("Pasien harus diisi");
            }
            
            // Validasi dokter
            if (antrean.getDokter() == null) {
                throw new IllegalArgumentException("Dokter harus diisi");
            }
            
            // Validasi status
            if (antrean.getStatus() == null) {
                throw new IllegalArgumentException("Status antrean tidak boleh kosong");
            }
            
            List<String> validStatus = List.of(
                Antrean.STATUS_MENUNGGU,
                Antrean.STATUS_DIPANGGIL,
                Antrean.STATUS_SELESAI,
                Antrean.STATUS_BATAL
            );
            
            if (!validStatus.contains(antrean.getStatus())) {
                throw new IllegalArgumentException("Status antrean tidak valid");
            }
            
            return true;
            
        } catch (IllegalArgumentException e) {
            System.err.println("Validasi gagal: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Generate nomor antrean unik (format: A-YYYYMMDD-XXX)
     */
    private String generateNomorAntrean() {
        LocalDate today = LocalDate.now();
        
        // Reset counter jika beda hari
        if (!today.equals(lastCounterDate)) {
            counterHarian = 0;
            lastCounterDate = today;
        }
        
        counterHarian++;
        String dateStr = today.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String counterStr = String.format("%03d", counterHarian);
        
        return "A-" + dateStr + "-" + counterStr;
    }
    
    /**
     * Dapatkan statistik antrean hari ini
     */
    public String getStatistikHariIni() {
        List<Antrean> antreanHariIni = getAntreanHariIni();
        
        long menunggu = antreanHariIni.stream().filter(Antrean::isMenunggu).count();
        long dipanggil = antreanHariIni.stream().filter(Antrean::isDipanggil).count();
        long selesai = antreanHariIni.stream().filter(Antrean::isSelesai).count();
        long batal = antreanHariIni.stream().filter(Antrean::isBatal).count();
        
        return String.format(
            "Statistik Hari Ini:\n" +
            "Total: %d\n" +
            "Menunggu: %d\n" +
            "Dipanggil: %d\n" +
            "Selesai: %d\n" +
            "Batal: %d",
            antreanHariIni.size(), menunggu, dipanggil, selesai, batal
        );
    }
}