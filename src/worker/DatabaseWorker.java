// File: worker/DatabaseWorker.java
package worker;

import java.util.logging.Level;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Worker untuk background database operations
 */
public class DatabaseWorker extends BaseWorker {
    private String backupDir;
    
    public DatabaseWorker() {
        super("DatabaseWorker", 3600000); // Run setiap 1 jam (3600000 ms)
        this.backupDir = "backup/";
        ensureBackupDir();
    }
    
    @Override
    protected void performTask() throws Exception {
        logger.fine("DatabaseWorker performing tasks...");
        
        // 1. Backup operations (simulasi)
        performBackup();
        
        // 2. Cleanup old backups
        cleanupOldBackups();
        
        // 3. Log database status
        logDatabaseStatus();
        
        logger.fine("DatabaseWorker tasks completed");
    }
    
    private void ensureBackupDir() {
        File dir = new File(backupDir);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                logger.info("Created backup directory: " + backupDir);
            }
        }
    }
    
    private void performBackup() {
        try {
            // Simulasi backup (dalam implementasi nyata, ini akan backup database)
            String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            
            String backupFile = backupDir + "backup_" + timestamp + ".txt";
            String content = "Database Backup - SMARS System\n" +
                           "Timestamp: " + java.time.LocalDateTime.now() + "\n" +
                           "Backup created by DatabaseWorker\n";
            
            Files.write(Paths.get(backupFile), content.getBytes());
            
            logger.info("Backup created: " + backupFile);
            
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error creating backup: " + e.getMessage(), e);
        }
    }
    
    private void cleanupOldBackups() {
        try {
            File dir = new File(backupDir);
            File[] files = dir.listFiles((d, name) -> name.startsWith("backup_") && name.endsWith(".txt"));
            
            if (files != null) {
                long cutoff = System.currentTimeMillis() - (7L * 24 * 60 * 60 * 1000); // 7 hari
                int deleted = 0;
                
                for (File file : files) {
                    if (file.lastModified() < cutoff) {
                        if (file.delete()) {
                            deleted++;
                            logger.fine("Deleted old backup: " + file.getName());
                        }
                    }
                }
                
                if (deleted > 0) {
                    logger.info("Cleaned up " + deleted + " old backup files");
                }
            }
            
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error cleaning up old backups: " + e.getMessage(), e);
        }
    }
    
    private void logDatabaseStatus() {
        // Simulasi status database
        logger.info("Database status: OK - Last backup: " + 
            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
    
    // Method untuk manual backup
    public void triggerManualBackup() {
        logger.info("Manual backup triggered");
        try {
            performBackup();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in manual backup: " + e.getMessage(), e);
        }
    }
    
    // Method untuk set backup directory
    public void setBackupDir(String backupDir) {
        this.backupDir = backupDir;
        ensureBackupDir();
        logger.info("Backup directory set to: " + backupDir);
    }
}