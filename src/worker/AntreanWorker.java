// File: worker/AntreanWorker.java
package worker;

import controller.AntreanController;
import model.Antrean;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Level;

/**
 * Worker untuk background tasks terkait antrean
 */
public class AntreanWorker extends BaseWorker {
    private AntreanController antreanController;
    
    public AntreanWorker() {
        super("AntreanWorker", 30000); // Run setiap 30 detik
        this.antreanController = new AntreanController();
    }
    
    @Override
    protected void performTask() throws Exception {
        logger.fine("AntreanWorker performing tasks...");
        
        // 1. Auto-batalkan antrean yang terlalu lama menunggu (>2 jam)
        autoBatalkanAntreanLama();
        
        // 2. Update statistik
        updateStatistik();
        
        // 3. Check antrean yang menunggu terlalu lama (warning)
        checkAntreanMenungguLama();
        
        logger.fine("AntreanWorker tasks completed");
    }
    
    private void autoBatalkanAntreanLama() {
        try {
            List<Antrean> semuaAntrean = antreanController.getAntreanHariIni();
            int dibatalkan = 0;
            
            for (Antrean antrean : semuaAntrean) {
                if (antrean.isMenunggu()) {
                    LocalDateTime waktuMasuk = antrean.getWaktuMasuk();
                    long menitMenunggu = ChronoUnit.MINUTES.between(waktuMasuk, LocalDateTime.now());
                    
                    // Auto batalkan jika menunggu > 120 menit (2 jam)
                    if (menitMenunggu > 120) {
                        antrean.batalkan("Auto-batalkan: waktu tunggu terlalu lama (" + menitMenunggu + " menit)");
                        dibatalkan++;
                        logger.info("Auto-batalkan antrean: " + antrean.getNomorAntrean());
                    }
                }
            }
            
            if (dibatalkan > 0) {
                logger.info("Auto-batalkan " + dibatalkan + " antrean yang menunggu terlalu lama");
            }
            
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error auto-batalkan antrean: " + e.getMessage(), e);
        }
    }
    
    private void updateStatistik() {
        try {
            List<Antrean> antreanHariIni = antreanController.getAntreanHariIni();
            long menunggu = antreanHariIni.stream().filter(Antrean::isMenunggu).count();
            long dipanggil = antreanHariIni.stream().filter(Antrean::isDipanggil).count();
            long selesai = antreanHariIni.stream().filter(Antrean::isSelesai).count();
            
            logger.info(String.format(
                "Statistik Antrean: Total=%d, Menunggu=%d, Dipanggil=%d, Selesai=%d",
                antreanHariIni.size(), menunggu, dipanggil, selesai
            ));
            
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error update statistik: " + e.getMessage(), e);
        }
    }
    
    private void checkAntreanMenungguLama() {
        try {
            List<Antrean> antreanMenunggu = antreanController.getAntreanMenunggu();
            
            for (Antrean antrean : antreanMenunggu) {
                long menitMenunggu = ChronoUnit.MINUTES.between(
                    antrean.getWaktuMasuk(), 
                    LocalDateTime.now()
                );
                
                // Warning jika menunggu > 60 menit
                if (menitMenunggu > 60 && menitMenunggu <= 120) {
                    logger.warning(String.format(
                        "Antrean %s menunggu %d menit: %s (Dr. %s)",
                        antrean.getNomorAntrean(),
                        menitMenunggu,
                        antrean.getPasien().getNama(),
                        antrean.getDokter().getNama()
                    ));
                }
            }
            
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error check antrean menunggu lama: " + e.getMessage(), e);
        }
    }
    
    // Additional methods
    public void setCheckInterval(int minutes) {
        setIntervalMs(minutes * 60 * 1000); // Convert minutes to milliseconds
        logger.info("AntreanWorker interval set to " + minutes + " minutes");
    }
    
    public void forceCheckNow() {
        logger.info("Manual force check triggered");
        try {
            performTask();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in force check: " + e.getMessage(), e);
        }
    }
}