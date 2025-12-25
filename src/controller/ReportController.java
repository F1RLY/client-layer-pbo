// File: controller/ReportController.java
package controller;

import model.Antrean;
import model.Pasien;
import model.Dokter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Controller untuk generate laporan (Bonus Feature)
 */
public class ReportController {
    private AntreanController antreanController;
    private PasienController pasienController;
    private DokterController dokterController;
    
    // Constructor
    public ReportController() {
        this.antreanController = new AntreanController();
        this.pasienController = new PasienController();
        this.dokterController = new DokterController();
    }
    
    /**
     * Generate laporan harian
     */
    public String generateLaporanHarian(LocalDate tanggal) {
        try {
            if (tanggal == null) {
                tanggal = LocalDate.now();
            }
            
            final LocalDate reportDate = tanggal;
            
            List<Antrean> antreanHariIni = antreanController.getAntreanHariIni().stream()
                    .filter(a -> a.getWaktuMasuk().toLocalDate().equals(reportDate))
                    .collect(Collectors.toList());
            
            long total = antreanHariIni.size();
            long menunggu = antreanHariIni.stream().filter(Antrean::isMenunggu).count();
            long dipanggil = antreanHariIni.stream().filter(Antrean::isDipanggil).count();
            long selesai = antreanHariIni.stream().filter(Antrean::isSelesai).count();
            long batal = antreanHariIni.stream().filter(Antrean::isBatal).count();
            
            // Group by dokter
            Map<Dokter, Long> antreanByDokter = antreanHariIni.stream()
                    .collect(Collectors.groupingBy(Antrean::getDokter, Collectors.counting()));
            
            StringBuilder report = new StringBuilder();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            report.append("=".repeat(60)).append("\n");
            report.append("LAPORAN HARIAN - ").append(tanggal.format(formatter)).append("\n");
            report.append("=".repeat(60)).append("\n\n");
            
            report.append("STATISTIK UMUM:\n");
            report.append("-".repeat(40)).append("\n");
            report.append(String.format("Total Antrean: %d\n", total));
            report.append(String.format("Status Menunggu: %d\n", menunggu));
            report.append(String.format("Status Dipanggil: %d\n", dipanggil));
            report.append(String.format("Status Selesai: %d\n", selesai));
            report.append(String.format("Status Batal: %d\n\n", batal));
            
            report.append("DISTRIBUSI PER DOKTER:\n");
            report.append("-".repeat(40)).append("\n");
            antreanByDokter.forEach((dokter, count) -> {
                report.append(String.format("%s: %d antrean\n", 
                    dokter.getNama(), count));
            });
            
            report.append("\n").append("=".repeat(60)).append("\n");
            report.append("Laporan dibuat pada: ").append(java.time.LocalDateTime.now()).append("\n");
            
            return report.toString();
            
        } catch (Exception e) {
            handleException(e, "generate laporan harian");
            return "Error generating report: " + e.getMessage();
        }
    }
    
    /**
     * Generate laporan bulanan
     */
    public String generateLaporanBulanan(int tahun, int bulan) {
        try {
            LocalDate startDate = LocalDate.of(tahun, bulan, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            
            final LocalDate finalStartDate = startDate;
            final LocalDate finalEndDate = endDate;
            
            List<Antrean> semuaAntrean = antreanController.readAll();
            List<Antrean> antreanBulanan = semuaAntrean.stream()
                    .filter(a -> {
                        LocalDate tanggalAntrean = a.getWaktuMasuk().toLocalDate();
                        return !tanggalAntrean.isBefore(finalStartDate) && 
                               !tanggalAntrean.isAfter(finalEndDate);
                    })
                    .collect(Collectors.toList());
            
            // Hitung statistik
            long total = antreanBulanan.size();
            long selesai = antreanBulanan.stream().filter(Antrean::isSelesai).count();
            double persentaseSelesai = total > 0 ? (selesai * 100.0 / total) : 0;
            
            // Rata-rata per hari
            long hariKerja = 0;
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                if (date.getDayOfWeek().getValue() <= 5) { // Senin-Jumat
                    hariKerja++;
                }
            }
            
            double rataPerHari = hariKerja > 0 ? (double) total / hariKerja : 0;
            
            // Group by tanggal
            Map<LocalDate, Long> antreanPerHari = antreanBulanan.stream()
                    .collect(Collectors.groupingBy(
                        a -> a.getWaktuMasuk().toLocalDate(),
                        Collectors.counting()
                    ));
            
            StringBuilder report = new StringBuilder();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
            
            report.append("=".repeat(60)).append("\n");
            report.append("LAPORAN BULANAN - ").append(startDate.format(formatter)).append("\n");
            report.append("=".repeat(60)).append("\n\n");
            
            report.append("RINGKASAN:\n");
            report.append("-".repeat(40)).append("\n");
            report.append(String.format("Periode: %s - %s\n", 
                startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            report.append(String.format("Total Antrean: %d\n", total));
            report.append(String.format("Antrean Selesai: %d (%.1f%%)\n", selesai, persentaseSelesai));
            report.append(String.format("Hari Kerja: %d\n", hariKerja));
            report.append(String.format("Rata-rata per Hari: %.1f\n\n", rataPerHari));
            
            report.append("DATA HARIAN:\n");
            report.append("-".repeat(40)).append("\n");
            antreanPerHari.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        report.append(String.format("%s: %d antrean\n",
                            entry.getKey().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            entry.getValue()));
                    });
            
            // Top 5 dokter dengan antrean terbanyak
            Map<Dokter, Long> topDokter = antreanBulanan.stream()
                    .collect(Collectors.groupingBy(Antrean::getDokter, Collectors.counting()))
                    .entrySet().stream()
                    .sorted(Map.Entry.<Dokter, Long>comparingByValue().reversed())
                    .limit(5)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            
            report.append("\nTOP 5 DOKTER:\n");
            report.append("-".repeat(40)).append("\n");
            topDokter.forEach((dokter, count) -> {
                report.append(String.format("%s: %d antrean\n", dokter.getNama(), count));
            });
            
            report.append("\n").append("=".repeat(60)).append("\n");
            report.append("Laporan dibuat pada: ").append(java.time.LocalDateTime.now()).append("\n");
            
            return report.toString();
            
        } catch (Exception e) {
            handleException(e, "generate laporan bulanan");
            return "Error generating monthly report: " + e.getMessage();
        }
    }
    
    /**
     * Generate laporan pasien baru
     */
    public String generateLaporanPasienBaru(LocalDate dari, LocalDate sampai) {
        try {
            if (dari == null) {
                dari = LocalDate.now().minusDays(30);
            }
            if (sampai == null) {
                sampai = LocalDate.now();
            }
            
            final LocalDate finalDari = dari;
            final LocalDate finalSampai = sampai;
            
            List<Pasien> semuaPasien = pasienController.readAll();
            List<Pasien> pasienBaru = semuaPasien.stream()
                    .filter(p -> {
                        LocalDate tanggalDaftar = p.getCreatedAt().toLocalDate();
                        return !tanggalDaftar.isBefore(finalDari) && 
                               !tanggalDaftar.isAfter(finalSampai);
                    })
                    .collect(Collectors.toList());
            
            // Group by usia
            Map<String, Long> groupByUsia = pasienBaru.stream()
                    .collect(Collectors.groupingBy(p -> {
                        int usia = p.getUsia();
                        if (usia < 12) return "Anak (<12)";
                        else if (usia < 18) return "Remaja (12-17)";
                        else if (usia < 40) return "Dewasa Muda (18-39)";
                        else if (usia < 60) return "Dewasa (40-59)";
                        else return "Lansia (≥60)";
                    }, Collectors.counting()));
            
            // Group by jenis kelamin
            Map<String, Long> groupByGender = pasienBaru.stream()
                    .collect(Collectors.groupingBy(
                        p -> p.getJenisKelamin() != null ? p.getJenisKelamin() : "Tidak Diketahui",
                        Collectors.counting()
                    ));
            
            StringBuilder report = new StringBuilder();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            report.append("=".repeat(60)).append("\n");
            report.append("LAPORAN PASIEN BARU\n");
            report.append("Periode: ").append(dari.format(formatter))
                  .append(" - ").append(sampai.format(formatter)).append("\n");
            report.append("=".repeat(60)).append("\n\n");
            
            report.append("STATISTIK UMUM:\n");
            report.append("-".repeat(40)).append("\n");
            report.append(String.format("Total Pasien Baru: %d\n", pasienBaru.size()));
            report.append(String.format("Rata-rata per Hari: %.1f\n\n", 
                (double) pasienBaru.size() / dari.until(sampai).getDays()));
            
            report.append("DISTRIBUSI USIA:\n");
            report.append("-".repeat(40)).append("\n");
            groupByUsia.forEach((kategori, count) -> {
                double persentase = pasienBaru.size() > 0 ? (count * 100.0 / pasienBaru.size()) : 0;
                report.append(String.format("%s: %d (%.1f%%)\n", kategori, count, persentase));
            });
            
            report.append("\nDISTRIBUSI JENIS KELAMIN:\n");
            report.append("-".repeat(40)).append("\n");
            groupByGender.forEach((gender, count) -> {
                double persentase = pasienBaru.size() > 0 ? (count * 100.0 / pasienBaru.size()) : 0;
                report.append(String.format("%s: %d (%.1f%%)\n", gender, count, persentase));
            });
            
            report.append("\nDETAIL PASIEN:\n");
            report.append("-".repeat(40)).append("\n");
            pasienBaru.forEach(pasien -> {
                report.append(String.format("- %s (NIK: %s, Usia: %d)\n",
                    pasien.getNama(), pasien.getNik(), pasien.getUsia()));
            });
            
            report.append("\n").append("=".repeat(60)).append("\n");
            report.append("Laporan dibuat pada: ").append(java.time.LocalDateTime.now()).append("\n");
            
            return report.toString();
            
        } catch (Exception e) {
            handleException(e, "generate laporan pasien baru");
            return "Error generating patient report: " + e.getMessage();
        }
    }
    
    /**
     * Export laporan ke file teks
     */
    public boolean exportToTextFile(String content, String filename) {
        try {
            if (filename == null || filename.trim().isEmpty()) {
                filename = "laporan_" + System.currentTimeMillis() + ".txt";
            }
            
            if (!filename.endsWith(".txt")) {
                filename += ".txt";
            }
            
            Files.write(Paths.get(filename), content.getBytes());
            
            System.out.println("Laporan berhasil diexport ke: " + filename);
            return true;
            
        } catch (Exception e) {
            handleException(e, "export to text file");
            return false;
        }
    }
    
    /**
     * Export laporan ke file HTML
     */
    public boolean exportToHTML(String content, String filename) {
        try {
            if (filename == null || filename.trim().isEmpty()) {
                filename = "laporan_" + System.currentTimeMillis() + ".html";
            }
            
            if (!filename.endsWith(".html")) {
                filename += ".html";
            }
            
            // Build HTML content
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n");
            html.append("<html>\n");
            html.append("<head>\n");
            html.append("    <meta charset=\"UTF-8\">\n");
            html.append("    <title>Laporan SMARS</title>\n");
            html.append("    <style>\n");
            html.append("        body { font-family: Arial, sans-serif; margin: 40px; }\n");
            html.append("        h1 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }\n");
            html.append("        .section { margin-bottom: 30px; }\n");
            html.append("        .stat { background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin-bottom: 15px; }\n");
            html.append("        table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }\n");
            html.append("        th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }\n");
            html.append("        th { background-color: #3498db; color: white; }\n");
            html.append("        .footer { margin-top: 30px; color: #7f8c8d; font-size: 0.9em; }\n");
            html.append("    </style>\n");
            html.append("</head>\n");
            html.append("<body>\n");
            html.append("    <h1>Laporan Sistem Manajemen Antrean Rumah Sakit (SMARS)</h1>\n");
            html.append("    <div class=\"content\">\n");
            
            // Process content
            String processedContent = content
                .replace("\n", "<br>\n")
                .replace("=".repeat(60), "<hr>")
                .replace("-".repeat(40), "<hr style='border-top: 1px dashed #ccc;'>");
            
            html.append(processedContent);
            
            html.append("    </div>\n");
            html.append("    <div class=\"footer\">\n");
            html.append("        <p>Dibuat secara otomatis oleh Sistem SMARS</p>\n");
            html.append("        <p>© Politeknik Negeri Indramayu - D3 Teknik Informatika</p>\n");
            html.append("    </div>\n");
            html.append("</body>\n");
            html.append("</html>");
            
            Files.write(Paths.get(filename), html.toString().getBytes());
            
            System.out.println("Laporan berhasil diexport ke HTML: " + filename);
            return true;
            
        } catch (Exception e) {
            handleException(e, "export to HTML");
            return false;
        }
    }
    
    /**
     * Export laporan ke file PDF (membutuhkan library iText)
     */
    public boolean exportToPDF(String content, String filename) {
        try {
            // Catatan: Membutuhkan library iTextPDF
            System.out.println("Fitur export PDF membutuhkan library iTextPDF");
            System.out.println("Silakan tambahkan dependency iText ke project Anda");
            
            // Fallback: export ke HTML
            return exportToHTML(content, filename.replace(".pdf", ".html"));
            
        } catch (Exception e) {
            handleException(e, "export to PDF");
            return false;
        }
    }
    
    private void handleException(Exception e, String operation) {
        System.err.println("Error pada operasi " + operation + ": " + e.getMessage());
        e.printStackTrace();
    }
}