package service.impl;

import model.Pasien;
import service.PasienService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PasienServiceImpl implements PasienService {
    private List<Pasien> pasienList;
    private int nextId = 1;
    
    public PasienServiceImpl() {
        pasienList = new ArrayList<>();
        initDummyData();
    }
    
    private void initDummyData() {
        tambahPasien(new Pasien("Budi Santoso", "3201010101010001", 
            "Jl. Merdeka No.10", "0811111111", new Date(), "Laki-laki"));
        
        tambahPasien(new Pasien("Siti Rahayu", "3201010101010002", 
            "Jl. Sudirman No.20", "0822222222", new Date(), "Perempuan"));
        
        tambahPasien(new Pasien("Ahmad Wijaya", "3201010101010003", 
            "Jl. Gatot Subroto No.30", "0833333333", new Date(), "Laki-laki"));
    }
    
    @Override
    public void tambahPasien(Pasien pasien) {
        // Validasi No KTP unik
        if (isNoKtpExist(pasien.getNoKtp())) {
            throw new IllegalArgumentException("No KTP sudah terdaftar");
        }
        
        pasien.setId(nextId++);
        pasienList.add(pasien);
    }
    
    @Override
    public void updatePasien(Pasien pasien) {
        for (int i = 0; i < pasienList.size(); i++) {
            if (pasienList.get(i).getId() == pasien.getId()) {
                // Cek jika No KTP berubah dan sudah dipakai orang lain
                Pasien existing = pasienList.get(i);
                if (!existing.getNoKtp().equals(pasien.getNoKtp()) && 
                    isNoKtpExist(pasien.getNoKtp())) {
                    throw new IllegalArgumentException("No KTP sudah terdaftar");
                }
                
                pasienList.set(i, pasien);
                return;
            }
        }
        throw new IllegalArgumentException("Pasien dengan ID " + pasien.getId() + " tidak ditemukan");
    }
    
    @Override
    public void hapusPasien(int id) {
        pasienList.removeIf(p -> p.getId() == id);
    }
    
    @Override
    public Pasien getPasienById(int id) {
        return pasienList.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public Pasien getPasienByNoKtp(String noKtp) {
        return pasienList.stream()
                .filter(p -> p.getNoKtp().equals(noKtp))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public List<Pasien> getAllPasien() {
        return new ArrayList<>(pasienList);
    }
    
    @Override
    public List<Pasien> cariPasienByNama(String nama) {
        return pasienList.stream()
                .filter(p -> p.getNama().toLowerCase().contains(nama.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Pasien> cariPasienByAlamat(String alamat) {
        return pasienList.stream()
                .filter(p -> p.getAlamat().toLowerCase().contains(alamat.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean isPasienExist(int id) {
        return pasienList.stream().anyMatch(p -> p.getId() == id);
    }
    
    @Override
    public boolean isNoKtpExist(String noKtp) {
        return pasienList.stream().anyMatch(p -> p.getNoKtp().equals(noKtp));
    }
    
    @Override
    public boolean isTeleponExist(String telepon) {
        return pasienList.stream().anyMatch(p -> p.getNoTelp().equals(telepon));
    }
    
    @Override
    public int getJumlahPasien() {
        return pasienList.size();
    }
    
    @Override
    public List<Pasien> getPasienBaruBulanIni() {
        // Logika sederhana: return 2 pasien terakhir
        int size = pasienList.size();
        return pasienList.subList(Math.max(0, size - 2), size);
    }
}