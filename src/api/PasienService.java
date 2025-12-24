package api;

import model.Pasien;
import java.util.List;

public interface PasienService {
    void tambahPasien(Pasien pasien);
    void updatePasien(Pasien pasien);
    void hapusPasien(int id);
    List<Pasien> getAllPasien();
    Pasien getPasienById(int id);
    Pasien getPasienByNoKtp(String noKtp);
    List<Pasien> cariPasienByNama(String nama);
}