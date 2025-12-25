// File: worker/WorkerManager.java
package worker;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Manager untuk mengatur semua worker
 */
public class WorkerManager {
    private static final Logger logger = Logger.getLogger(WorkerManager.class.getName());
    private static WorkerManager instance;
    
    private Map<String, BaseWorker> workers;
    private boolean initialized = false;
    
    private WorkerManager() {
        workers = new HashMap<>();
    }
    
    public static synchronized WorkerManager getInstance() {
        if (instance == null) {
            instance = new WorkerManager();
        }
        return instance;
    }
    
    // Initialize all workers
    public void initializeWorkers() {
        if (initialized) {
            logger.warning("Workers already initialized");
            return;
        }
        
        logger.info("Initializing workers...");
        
        // Create workers
        AntreanWorker antreanWorker = new AntreanWorker();
        NotificationWorker notificationWorker = new NotificationWorker();
        DatabaseWorker databaseWorker = new DatabaseWorker();
        
        // Register workers
        registerWorker("antrean", antreanWorker);
        registerWorker("notification", notificationWorker);
        registerWorker("database", databaseWorker);
        
        initialized = true;
        logger.info("Workers initialized successfully");
    }
    
    // Start all workers
    public void startAllWorkers() {
        logger.info("Starting all workers...");
        
        for (Map.Entry<String, BaseWorker> entry : workers.entrySet()) {
            try {
                entry.getValue().start();
                logger.info("Started worker: " + entry.getKey());
            } catch (Exception e) {
                logger.severe("Failed to start worker " + entry.getKey() + ": " + e.getMessage());
            }
        }
        
        logger.info("All workers started");
    }
    
    // Stop all workers
    public void stopAllWorkers() {
        logger.info("Stopping all workers...");
        
        for (Map.Entry<String, BaseWorker> entry : workers.entrySet()) {
            try {
                entry.getValue().stop();
                logger.info("Stopped worker: " + entry.getKey());
            } catch (Exception e) {
                logger.severe("Failed to stop worker " + entry.getKey() + ": " + e.getMessage());
            }
        }
        
        logger.info("All workers stopped");
    }
    
    // Register a worker
    public void registerWorker(String name, BaseWorker worker) {
        if (workers.containsKey(name)) {
            logger.warning("Worker '" + name + "' already registered, replacing...");
        }
        workers.put(name, worker);
        logger.info("Registered worker: " + name);
    }
    
    // Unregister a worker
    public void unregisterWorker(String name) {
        if (workers.containsKey(name)) {
            BaseWorker worker = workers.remove(name);
            worker.stop();
            logger.info("Unregistered worker: " + name);
        }
    }
    
    // Get worker by name
    public BaseWorker getWorker(String name) {
        return workers.get(name);
    }
    
    // Get all workers status
    public Map<String, WorkerStatus> getAllWorkersStatus() {
        Map<String, WorkerStatus> statusMap = new HashMap<>();
        
        for (Map.Entry<String, BaseWorker> entry : workers.entrySet()) {
            statusMap.put(entry.getKey(), entry.getValue().getStatus());
        }
        
        return statusMap;
    }
    
    // Print workers status
    public void printWorkersStatus() {
        System.out.println("\n=== WORKERS STATUS ===");
        
        Map<String, WorkerStatus> statusMap = getAllWorkersStatus();
        for (Map.Entry<String, WorkerStatus> entry : statusMap.entrySet()) {
            System.out.printf("%-20s : %s%n", 
                entry.getKey(), 
                entry.getValue().getDescription());
        }
        
        System.out.println("=====================\n");
    }
    
    // Getters
    public boolean isInitialized() {
        return initialized;
    }
    
    public int getWorkerCount() {
        return workers.size();
    }
}