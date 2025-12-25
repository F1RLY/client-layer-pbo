// File: controller/JadwalController.java
package controller;

import model.JadwalDokter;
import model.Dokter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller untuk manajemen jadwal dokter
 */
public class JadwalController extends BaseController<JadwalDokter> {
    private List<JadwalDokter> jadwalList = new ArrayList<>();
    private Integer lastId = 0;
    
    // Constructor
    public JadwalController() {
        initializeDummyData();
    }
    
    private void initializeDummyData() {
        DokterController dokterController = new DokterController();
        List<Dokter> dokterList = dokterController.getAktifDokter();
        
        if (!dokterList.isEmpty()) {
            // Buat jadwal dummy
            for (int i = 0; i < Math.min(dokterList.size(), 3); i++) {
                Dokter dokter = dokterList.get(i);
                
                // Jadwal Senin - Jumat
                for (DayOfWeek day : new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY}) {
                    JadwalDokter jadwal = new JadwalDokter(
                        dokter,
                        day,
                        LocalTime.of(8, 0),
                        LocalTime.of(12, 0)
                    );
                    jadwal.setId(++lastId);
                    jadwal.setKuota(20);
                    jadwal.setRuangan("Ruang " + (i + 1));
                    jadwalList.add(jadwal);
                }
            }
        }
    }
    
    @Override
    public boolean create(JadwalDokter jadwal) {
        try {
            if (!validate(jadwal)) {
                return false;
            }
            
            // Cek konflik jadwal
            if (hasJadwalConflict(jadwal)) {
                throw new IllegalArgumentException("Konflik jadwal dengan dokter yang sama");
            }
            
            jadwal.setId(++lastId);
            jadwalList.add(jadwal);
            
            System.out.println("Jadwal berhasil ditambahkan untuk dokter: " + 
                             jadwal.getDokter().getNama());
            return true;
            
        } catch (IllegalArgumentException e) {
            System.err.println("Create gagal: " + e.getMessage());
            return false;
        } catch (Exception e) {
            handleException(e, "create jadwal");
            return false;
        }
    }
    
    @Override
    public JadwalDokter read(Integer id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("ID tidak boleh null");
            }
            
            return jadwalList.stream()
                    .filter(j -> j.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            
        } catch (Exception e) {
            handleException(e, "read jadwal");
            return null;
        }
    }
    
    @Override
    public List<JadwalDokter> readAll() {
        return new ArrayList<>(jadwalList);
    }
    
    /**
     * Dapatkan jadwal berdasarkan dokter
     */
    public List<JadwalDokter> getJadwalByDokter(Dokter dokter) {
        try {
            if (dokter == null) {
                return new ArrayList<>();
            }
            
            return jadwalList.stream()
                    .filter(j -> dokter.equals(j.getDokter()))
                    .collect(Collectors.toList());
            
        } catch (Exception e) {
            handleException(e, "get jadwal by dokter");
            return new ArrayList<>();
        }
    }
    
    /**
     * Dapatkan jadwal berdasarkan hari
     */
    public List<JadwalDokter> getJadwalByDay(DayOfWeek day) {
        try {
            if (day == null) {
                return new ArrayList<>();
            }
            
            return jadwalList.stream()
                    .filter(j -> day.equals(j.getHari()))
                    .collect(Collectors.toList());
            
        } catch (Exception e) {
            handleException(e, "get jadwal by day");
            return new ArrayList<>();
        }
    }
    
    /**
     * Dapatkan jadwal untuk hari ini
     */
    public List<JadwalDokter> getJadwalHariIni() {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        return getJadwalByDay(today);
    }
    
    /**
     * Dapatkan dokter yang praktek hari ini
     */
    public List<Dokter> getDokterPraktekHariIni() {
        return getJadwalHariIni().stream()
                .map(JadwalDokter::getDokter)
                .distinct()
                .collect(Collectors.toList());
    }
    
    /**
     * Cek apakah ada jadwal konflik
     */
    private boolean hasJadwalConflict(JadwalDokter newJadwal) {
        return jadwalList.stream()
                .anyMatch(existing -> 
                    existing.getDokter().equals(newJadwal.getDokter()) &&
                    existing.getHari().equals(newJadwal.getHari()) &&
                    isTimeOverlap(
                        existing.getJamMulai(), existing.getJamSelesai(),
                        newJadwal.getJamMulai(), newJadwal.getJamSelesai()
                    )
                );
    }
    
    private boolean isTimeOverlap(LocalTime start1, LocalTime end1, 
                                  LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
    
    @Override
    public boolean update(JadwalDokter jadwal) {
        try {
            if (jadwal == null || jadwal.getId() == null) {
                throw new IllegalArgumentException("Jadwal atau ID tidak valid");
            }
            
            if (!validate(jadwal)) {
                return false;
            }
            
            JadwalDokter existing = read(jadwal.getId());
            if (existing == null) {
                throw new IllegalArgumentException("Jadwal tidak ditemukan");
            }
            
            // Cek konflik jadwal (kecuali dengan dirinya sendiri)
            JadwalDokter tempJadwal = new JadwalDokter(
                jadwal.getDokter(),
                jadwal.getHari(),
                jadwal.getJamMulai(),
                jadwal.getJamSelesai()
            );
            
            if (jadwalList.stream()
                .anyMatch(j -> 
                    !j.getId().equals(jadwal.getId()) &&
                    j.getDokter().equals(tempJadwal.getDokter()) &&
                    j.getHari().equals(tempJadwal.getHari()) &&
                    isTimeOverlap(
                        j.getJamMulai(), j.getJamSelesai(),
                        tempJadwal.getJamMulai(), tempJadwal.getJamSelesai()
                    )
                )) {
                throw new IllegalArgumentException("Konflik jadwal dengan dokter yang sama");
            }
            
            // Update data
            existing.setDokter(jadwal.getDokter());
            existing.setHari(jadwal.getHari());
            existing.setJamMulai(jadwal.getJamMulai());
            existing.setJamSelesai(jadwal.getJamSelesai());
            existing.setKuota(jadwal.getKuota());
            existing.setRuangan(jadwal.getRuangan());
            
            System.out.println("Jadwal berhasil diupdate untuk dokter: " + 
                             jadwal.getDokter().getNama());
            return true;
            
        } catch (IllegalArgumentException e) {
            System.err.println("Update gagal: " + e.getMessage());
            return false;
        } catch (Exception e) {
            handleException(e, "update jadwal");
            return false;
        }
    }
    
    @Override
    public boolean delete(Integer id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("ID tidak boleh null");
            }
            
            JadwalDokter jadwal = read(id);
            if (jadwal == null) {
                throw new IllegalArgumentException("Jadwal tidak ditemukan");
            }
            
            boolean removed = jadwalList.removeIf(j -> j.getId().equals(id));
            
            if (removed) {
                System.out.println("Jadwal berhasil dihapus: ID " + id);
            }
            
            return removed;
            
        } catch (IllegalArgumentException e) {
            System.err.println("Delete gagal: " + e.getMessage());
            return false;
        } catch (Exception e) {
            handleException(e, "delete jadwal");
            return false;
        }
    }
    
    @Override
    protected boolean validate(JadwalDokter jadwal) {
        try {
            // Validasi dokter
            if (jadwal.getDokter() == null) {
                throw new IllegalArgumentException("Dokter harus diisi");
            }
            
            // Validasi hari
            if (jadwal.getHari() == null) {
                throw new IllegalArgumentException("Hari harus diisi");
            }
            
            // Validasi jam
            if (jadwal.getJamMulai() == null) {
                throw new IllegalArgumentException("Jam mulai harus diisi");
            }
            
            if (jadwal.getJamSelesai() == null) {
                throw new IllegalArgumentException("Jam selesai harus diisi");
            }
            
            // Validasi waktu (jam mulai < jam selesai)
            if (!jadwal.getJamMulai().isBefore(jadwal.getJamSelesai())) {
                throw new IllegalArgumentException("Jam mulai harus sebelum jam selesai");
            }
            
            // Validasi durasi (maksimal 8 jam)
            long durasiJam = jadwal.getDurasiJam();
            if (durasiJam > 8) {
                throw new IllegalArgumentException("Durasi praktek maksimal 8 jam");
            }
            
            // Validasi jam praktek (08:00 - 20:00)
            if (jadwal.getJamMulai().isBefore(LocalTime.of(8, 0)) ||
                jadwal.getJamSelesai().isAfter(LocalTime.of(20, 0))) {
                throw new IllegalArgumentException("Jam praktek hanya antara 08:00 - 20:00");
            }
            
            // Validasi kuota
            if (jadwal.getKuota() != null && jadwal.getKuota() <= 0) {
                throw new IllegalArgumentException("Kuota harus lebih dari 0");
            }
            
            return true;
            
        } catch (IllegalArgumentException e) {
            System.err.println("Validasi gagal: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Dapatkan jadwal dalam format tabel
     */
    public String getJadwalTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("JADWAL DOKTER\n");
        sb.append("==========================================\n");
        
        for (DayOfWeek day : DayOfWeek.values()) {
            List<JadwalDokter> jadwalHari = getJadwalByDay(day);
            if (!jadwalHari.isEmpty()) {
                String namaHari = getNamaHariIndonesia(day);
                sb.append("\n").append(namaHari).append(":\n");
                
                for (JadwalDokter jadwal : jadwalHari) {
                    sb.append(String.format("  %s - %s (%s) | Ruang: %s | Kuota: %d\n",
                        jadwal.getJamMulai(),
                        jadwal.getJamSelesai(),
                        jadwal.getDokter().getNama(),
                        jadwal.getRuangan(),
                        jadwal.getKuota()
                    ));
                }
            }
        }
        
        return sb.toString();
    }
    
    private String getNamaHariIndonesia(DayOfWeek day) {
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
}