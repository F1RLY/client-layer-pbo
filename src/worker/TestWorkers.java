// File: worker/TestWorkers.java
package worker;

public class TestWorkers {
    public static void main(String[] args) {
        System.out.println("=== TESTING WORKER LAYER ===\n");
        
        try {
            // 1. Get WorkerManager instance
            WorkerManager manager = WorkerManager.getInstance();
            System.out.println("✅ WorkerManager instance created");
            
            // 2. Initialize workers
            manager.initializeWorkers();
            System.out.println("✅ Workers initialized");
            System.out.println("   Total workers: " + manager.getWorkerCount());
            
            // 3. Start all workers
            manager.startAllWorkers();
            System.out.println("✅ Workers started");
            
            // 4. Show status
            System.out.println("\n📊 Workers Status:");
            manager.printWorkersStatus();
            
            // 5. Let workers run for 10 seconds
            System.out.println("⏳ Letting workers run for 10 seconds...");
            Thread.sleep(10000);
            
            // 6. Stop workers
            manager.stopAllWorkers();
            System.out.println("✅ Workers stopped");
            
            // 7. Final status
            System.out.println("\n📊 Final Workers Status:");
            manager.printWorkersStatus();
            
            System.out.println("🎉 WORKER LAYER TEST COMPLETE!");
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}