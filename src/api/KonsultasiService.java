package api;

import model.Konsultasi;
import java.util.List;

public interface KonsultasiService {
    void buatJanji(Konsultasi konsultasi);
    void updateJanji(Konsultasi konsultasi);
    void hapusJanji(int id);
    List<Konsultasi> getAllKonsultasi();
    Konsultasi getKonsultasiById(int id);
    List<Konsultasi> getKonsultasiByPasien(int pasienId);
    List<Konsultasi> getKonsultasiByDokter(int dokterId);
}