// File: worker/BaseWorker.java
package worker;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * Abstract class dasar untuk semua worker
 */
public abstract class BaseWorker implements Runnable {
    protected static final Logger logger = Logger.getLogger(BaseWorker.class.getName());
    
    protected final String workerName;
    protected WorkerStatus status;
    protected final AtomicBoolean running;
    protected Thread workerThread;
    protected int intervalMs;
    
    public BaseWorker(String workerName, int intervalMs) {
        this.workerName = workerName;
        this.intervalMs = intervalMs;
        this.running = new AtomicBoolean(false);
        this.status = WorkerStatus.STOPPED;
    }
    
    @Override
    public void run() {
        logger.info("Worker '" + workerName + "' started");
        status = WorkerStatus.RUNNING;
        
        try {
            while (running.get()) {
                try {
                    performTask();
                } catch (Exception e) {
                    logger.severe("Error in worker '" + workerName + "': " + e.getMessage());
                    status = WorkerStatus.ERROR;
                    break;
                }
                
                if (intervalMs > 0) {
                    Thread.sleep(intervalMs);
                }
            }
        } catch (InterruptedException e) {
            logger.info("Worker '" + workerName + "' interrupted");
            Thread.currentThread().interrupt();
        } finally {
            status = WorkerStatus.STOPPED;
            logger.info("Worker '" + workerName + "' stopped");
        }
    }
    
    // Abstract method untuk diimplementasikan subclass
    protected abstract void performTask() throws Exception;
    
    // Start worker
    public synchronized void start() {
        if (!running.get()) {
            running.set(true);
            workerThread = new Thread(this, workerName + "-Thread");
            workerThread.setDaemon(true); // Thread daemon (berhenti jika app exit)
            workerThread.start();
            logger.info("Worker '" + workerName + "' started");
        }
    }
    
    // Stop worker
    public synchronized void stop() {
        if (running.get()) {
            running.set(false);
            if (workerThread != null) {
                workerThread.interrupt();
            }
            logger.info("Worker '" + workerName + "' stopping...");
        }
    }
    
    // Pause worker
    public synchronized void pause() {
        if (running.get() && status == WorkerStatus.RUNNING) {
            status = WorkerStatus.PAUSED;
            logger.info("Worker '" + workerName + "' paused");
        }
    }
    
    // Resume worker
    public synchronized void resume() {
        if (running.get() && status == WorkerStatus.PAUSED) {
            status = WorkerStatus.RUNNING;
            logger.info("Worker '" + workerName + "' resumed");
        }
    }
    
    // Getters
    public String getWorkerName() {
        return workerName;
    }
    
    public WorkerStatus getStatus() {
        return status;
    }
    
    public boolean isRunning() {
        return running.get();
    }
    
    public int getIntervalMs() {
        return intervalMs;
    }
    
    public void setIntervalMs(int intervalMs) {
        this.intervalMs = intervalMs;
    }
}