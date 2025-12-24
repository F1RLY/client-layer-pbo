package service.impl;

import model.Dokter;
import service.DokterService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DokterServiceImpl implements DokterService {
    private List<Dokter> dokterList;
    private int nextId = 1;
    
    public DokterServiceImpl() {
        dokterList = new ArrayList<>();
        initDummyData();
    }
    
    private void initDummyData() {
        tambahDokter(new Dokter("Dr. Andi Wijaya", "Umum", "08123456789", "Jl. Merdeka No.1"));
        tambahDokter(new Dokter("Dr. Siti Rahayu", "Anak", "08234567890", "Jl. Sudirman No.45"));
        tambahDokter(new Dokter("Dr. Budi Santoso", "Bedah", "08345678901", "Jl. Gatot Subroto No.12"));
    }
    
    @Override
    public void tambahDokter(Dokter dokter) {
        dokter.setId(nextId++);
        dokterList.add(dokter);
    }
    
    @Override
    public void updateDokter(Dokter dokter) {
        for (int i = 0; i < dokterList.size(); i++) {
            if (dokterList.get(i).getId() == dokter.getId()) {
                dokterList.set(i, dokter);
                return;
            }
        }
        throw new IllegalArgumentException("Dokter dengan ID " + dokter.getId() + " tidak ditemukan");
    }
    
    @Override
    public void hapusDokter(int id) {
        dokterList.removeIf(d -> d.getId() == id);
    }
    
    @Override
    public Dokter getDokterById(int id) {
        return dokterList.stream()
                .filter(d -> d.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public List<Dokter> getAllDokter() {
        return new ArrayList<>(dokterList);
    }
    
    @Override
    public List<Dokter> cariDokterByNama(String nama) {
        return dokterList.stream()
                .filter(d -> d.getNama().toLowerCase().contains(nama.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Dokter> cariDokterBySpesialis(String spesialis) {
        return dokterList.stream()
                .filter(d -> d.getSpesialis().equalsIgnoreCase(spesialis))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean isDokterExist(int id) {
        return dokterList.stream().anyMatch(d -> d.getId() == id);
    }
    
    @Override
    public boolean isTeleponExist(String telepon) {
        return dokterList.stream().anyMatch(d -> d.getNoTelp().equals(telepon));
    }
}