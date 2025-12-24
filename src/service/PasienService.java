package service;

import model.Pasien;
import java.util.List;

public interface PasienService {
    // CRUD operations
    void tambahPasien(Pasien pasien);
    void updatePasien(Pasien pasien);
    void hapusPasien(int id);
    Pasien getPasienById(int id);
    Pasien getPasienByNoKtp(String noKtp);
    List<Pasien> getAllPasien();
    
    // Search operations
    List<Pasien> cariPasienByNama(String nama);
    List<Pasien> cariPasienByAlamat(String alamat);
    
    // Validation
    boolean isPasienExist(int id);
    boolean isNoKtpExist(String noKtp);
    boolean isTeleponExist(String telepon);
    
    // Statistics
    int getJumlahPasien();
    List<Pasien> getPasienBaruBulanIni();
}