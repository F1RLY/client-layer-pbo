package api;

import model.Dokter;
import java.util.List;

public interface DokterService {
    void tambahDokter(Dokter dokter);
    void updateDokter(Dokter dokter);
    void hapusDokter(int id);
    List<Dokter> getAllDokter();
    Dokter getDokterById(int id);
    List<Dokter> cariDokterByNama(String nama);
    List<Dokter> cariDokterBySpesialis(String spesialis);
}