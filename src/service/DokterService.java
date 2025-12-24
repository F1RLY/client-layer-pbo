package service;

import model.Dokter;
import java.util.List;

public interface DokterService {
    // CRUD operations
    void tambahDokter(Dokter dokter);
    void updateDokter(Dokter dokter);
    void hapusDokter(int id);
    Dokter getDokterById(int id);
    List<Dokter> getAllDokter();
    
    // Search operations
    List<Dokter> cariDokterByNama(String nama);
    List<Dokter> cariDokterBySpesialis(String spesialis);
    
    // Validation
    boolean isDokterExist(int id);
    boolean isTeleponExist(String telepon);
}