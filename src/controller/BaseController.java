// File: controller/BaseController.java
package controller;

import java.util.List;

/**
 * Abstract class dasar untuk semua controller
 * @param <T> Tipe model
 */
public abstract class BaseController<T> {
    
    /**
     * Create - Menyimpan data baru
     */
    public abstract boolean create(T data);
    
    /**
     * Read - Mendapatkan data berdasarkan ID
     */
    public abstract T read(Integer id);
    
    /**
     * Read All - Mendapatkan semua data
     */
    public abstract List<T> readAll();
    
    /**
     * Update - Memperbarui data
     */
    public abstract boolean update(T data);
    
    /**
     * Delete - Menghapus data
     */
    public abstract boolean delete(Integer id);
    
    /**
     * Validasi data sebelum disimpan
     */
    protected abstract boolean validate(T data);
    
    /**
     * Exception handler umum
     */
    protected void handleException(Exception e, String operation) {
        System.err.println("Error pada operasi " + operation + ": " + e.getMessage());
        e.printStackTrace();
    }
}