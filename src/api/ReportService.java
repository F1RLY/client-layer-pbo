package api;

import java.util.Date;

public interface ReportService {
    String generateLaporanPasien(Date start, Date end);
    String generateLaporanDokter();
    String generateLaporanKunjungan(Date tanggal);
}