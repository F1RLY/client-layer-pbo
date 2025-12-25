// File: TestDokterDirect.java
package test;

import controller.DokterController;
import model.Dokter;

public class TestDokterDirect {
    public static void main(String[] args) {
        System.out.println("=== TEST DOKTER CONTROLLER DIRECT ===\n");
        
        DokterController controller = new DokterController();
        
        // 1. Get initial data
        System.out.println("1. Initial data:");
        System.out.println("   Size: " + controller.readAll().size());
        
        // 2. Create new dokter
        System.out.println("\n2. Creating new dokter...");
        Dokter baru = new Dokter("D005", "Dr. Joko", "THT");
        boolean success = controller.create(baru);
        System.out.println("   Create success? " + success);
        
        // 3. Get data after create
        System.out.println("\n3. Data after create:");
        System.out.println("   Size: " + controller.readAll().size());
        
        for (Dokter d : controller.readAll()) {
            System.out.println("   - " + d.getId() + " | " + d.getKodeDokter() + " | " + d.getNama());
        }
        
        System.out.println("\n=== TEST COMPLETE ===");
    }
}