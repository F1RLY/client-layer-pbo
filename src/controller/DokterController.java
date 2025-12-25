// File: controller/DokterController.java
// REPLACE ENTIRE FILE dengan ini:

package controller;

import model.Dokter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DokterController extends BaseController<Dokter> {
    private static List<Dokter> dokterList = new ArrayList<>(); // BUAT STATIC
    private static Integer lastId = 4; // SET AWAL 4 (karena ada 4 data dummy)
    private static boolean dataInitialized = false;
    
    // Constructor
    public DokterController() {
        // NO initialization in constructor
    }
    
    // Inisialisasi data dummy
    private synchronized void initializeDummyData() {
        if (!dataInitialized) {
            try {
                System.out.println("[INIT] Initializing dummy dokter data...");
                
                // Clear list first
                dokterList.clear();
                lastId = 0;
                
                // Add dummy data dengan cara langsung (tanpa panggil create)
                addDokterDirectly("D001", "Dr. Budi Santoso", "Umum");
                addDokterDirectly("D002", "Dr. Siti Rahayu", "Anak");
                addDokterDirectly("D003", "Dr. Ahmad Hidayat", "Bedah");
                addDokterDirectly("D004", "Dr. Maya Sari", "Kandungan");
                
                dataInitialized = true;
                System.out.println("[INIT] Dummy data initialized. Total: " + dokterList.size());
                
            } catch (Exception e) {
                System.err.println("[ERROR] Failed to init dummy data: " + e.getMessage());
            }
        }
    }
    
    private void addDokterDirectly(String kode, String nama, String spesialisasi) {
        Dokter dokter = new Dokter();
        dokter.setId(++lastId);
        dokter.setKodeDokter(kode);
        dokter.setNama(nama);
        dokter.setSpesialisasi(spesialisasi);
        dokter.setStatus("AKTIF");
        dokterList.add(dokter);
        System.out.println("[INIT] Added: " + kode + " - " + nama + " (ID: " + dokter.getId() + ")");
    }
    
    @Override
    public boolean create(Dokter dokter) {
        System.out.println("\n[CREATE] Attempting to create dokter: " + 
                         dokter.getKodeDokter() + " - " + dokter.getNama());
        
        try {
            // Step 1: Validasi
            if (!validate(dokter)) {
                System.out.println("[CREATE] Validation failed");
                return false;
            }
            
            // Step 2: Cek kode unik (case insensitive)
            String kodeInput = dokter.getKodeDokter().toUpperCase().trim();
            for (Dokter d : dokterList) {
                if (d.getKodeDokter().equalsIgnoreCase(kodeInput)) {
                    System.out.println("[CREATE] Kode already exists: " + kodeInput);
                    throw new IllegalArgumentException("Kode dokter sudah digunakan: " + kodeInput);
                }
            }
            
            // Step 3: Set ID dan simpan
            lastId++;
            dokter.setId(lastId);
            dokter.setKodeDokter(kodeInput); // Normalize to uppercase
            
            // Ensure status is set
            if (dokter.getStatus() == null) {
                dokter.setStatus("AKTIF");
            }
            
            // Add to list
            dokterList.add(dokter);
            
            // DEBUG: Print current list
            System.out.println("[CREATE] Success! ID: " + lastId);
            System.out.println("[CREATE] Current list size: " + dokterList.size());
            System.out.println("[CREATE] All dokters:");
            for (Dokter d : dokterList) {
                System.out.println("  - " + d.getId() + " | " + d.getKodeDokter() + " | " + d.getNama());
            }
            
            return true;
            
        } catch (IllegalArgumentException e) {
            System.err.println("[CREATE] Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("[CREATE] Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Dokter read(Integer id) {
        if (id == null) return null;
        
        for (Dokter dokter : dokterList) {
            if (id.equals(dokter.getId())) {
                return dokter;
            }
        }
        return null;
    }
    
    public Dokter readByKode(String kodeDokter) {
        if (kodeDokter == null) return null;
        
        String kode = kodeDokter.toUpperCase().trim();
        for (Dokter dokter : dokterList) {
            if (dokter.getKodeDokter().equalsIgnoreCase(kode)) {
                return dokter;
            }
        }
        return null;
    }
    
    @Override
    public List<Dokter> readAll() {
        // LAZY INIT: Initialize dummy data if empty
        if (dokterList.isEmpty()) {
            initializeDummyData();
        }
        
        System.out.println("[READ ALL] Returning " + dokterList.size() + " dokters");
        return new ArrayList<>(dokterList); // Return copy
    }
    
    public List<Dokter> searchBySpesialisasi(String spesialisasi) {
        if (spesialisasi == null || spesialisasi.trim().isEmpty()) {
            return readAll();
        }
        
        String searchTerm = spesialisasi.toLowerCase().trim();
        return dokterList.stream()
                .filter(d -> d.getSpesialisasi().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }
    
    public List<Dokter> getAktifDokter() {
        return dokterList.stream()
                .filter(d -> "AKTIF".equals(d.getStatus()))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean update(Dokter dokter) {
        System.out.println("\n[UPDATE] Updating dokter ID: " + dokter.getId());
        
        try {
            if (dokter == null || dokter.getId() == null) {
                throw new IllegalArgumentException("Dokter atau ID tidak valid");
            }
            
            if (!validate(dokter)) {
                return false;
            }
            
            // Find existing dokter
            Dokter existing = null;
            for (Dokter d : dokterList) {
                if (d.getId().equals(dokter.getId())) {
                    existing = d;
                    break;
                }
            }
            
            if (existing == null) {
                throw new IllegalArgumentException("Dokter tidak ditemukan");
            }
            
            // Cek kode unik (selain diri sendiri)
            String kodeInput = dokter.getKodeDokter().toUpperCase().trim();
            for (Dokter d : dokterList) {
                if (d.getKodeDokter().equalsIgnoreCase(kodeInput) && 
                    !d.getId().equals(dokter.getId())) {
                    throw new IllegalArgumentException("Kode sudah digunakan: " + kodeInput);
                }
            }
            
            // Update data
            existing.setKodeDokter(kodeInput);
            existing.setNama(dokter.getNama());
            existing.setSpesialisasi(dokter.getSpesialisasi());
            existing.setNoTelepon(dokter.getNoTelepon());
            existing.setEmail(dokter.getEmail());
            existing.setStatus(dokter.getStatus());
            
            System.out.println("[UPDATE] Success!");
            return true;
            
        } catch (IllegalArgumentException e) {
            System.err.println("[UPDATE] Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("[UPDATE] Unexpected error: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean delete(Integer id) {
        System.out.println("\n[DELETE] Deleting dokter ID: " + id);
        
        try {
            if (id == null) {
                throw new IllegalArgumentException("ID tidak boleh null");
            }
            
            // Find and remove
            boolean removed = false;
            for (int i = 0; i < dokterList.size(); i++) {
                if (dokterList.get(i).getId().equals(id)) {
                    Dokter removedDokter = dokterList.remove(i);
                    System.out.println("[DELETE] Removed: " + removedDokter.getKodeDokter() + " - " + removedDokter.getNama());
                    removed = true;
                    break;
                }
            }
            
            if (!removed) {
                throw new IllegalArgumentException("Dokter tidak ditemukan");
            }
            
            System.out.println("[DELETE] Success! Current size: " + dokterList.size());
            return true;
            
        } catch (IllegalArgumentException e) {
            System.err.println("[DELETE] Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("[DELETE] Unexpected error: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    protected boolean validate(Dokter dokter) {
        try {
            // Kode dokter
            if (dokter.getKodeDokter() == null || dokter.getKodeDokter().trim().isEmpty()) {
                throw new IllegalArgumentException("Kode dokter tidak boleh kosong");
            }
            
            String kode = dokter.getKodeDokter().trim();
            if (!kode.matches("[A-Za-z]\\d{3}")) {
                throw new IllegalArgumentException("Format: Huruf + 3 angka (contoh: D001)");
            }
            
            // Nama
            if (dokter.getNama() == null || dokter.getNama().trim().isEmpty()) {
                throw new IllegalArgumentException("Nama tidak boleh kosong");
            }
            
            String nama = dokter.getNama().trim();
            if (!nama.startsWith("Dr. ") && !nama.startsWith("dr. ")) {
                dokter.setNama("Dr. " + nama);
            }
            
            // Spesialisasi
            if (dokter.getSpesialisasi() == null || dokter.getSpesialisasi().trim().isEmpty()) {
                throw new IllegalArgumentException("Spesialisasi tidak boleh kosong");
            }
            
            // Status (default AKTIF)
            if (dokter.getStatus() == null) {
                dokter.setStatus("AKTIF");
            } else if (!dokter.getStatus().equals("AKTIF") && !dokter.getStatus().equals("NONAKTIF")) {
                throw new IllegalArgumentException("Status harus AKTIF atau NONAKTIF");
            }
            
            return true;
            
        } catch (IllegalArgumentException e) {
            System.err.println("[VALIDATE] Error: " + e.getMessage());
            return false;
        }
    }
    
    // Method untuk testing/reset
    public void resetData() {
        dokterList.clear();
        lastId = 0;
        dataInitialized = false;
        System.out.println("[RESET] Data dokter telah direset");
    }
    
    public int getCurrentSize() {
        return dokterList.size();
    }
    
    public int getLastId() {
        return lastId;
    }
}