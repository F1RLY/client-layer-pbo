// File: worker/WorkerStatus.java
package worker;

/**
 * Enum untuk status worker
 */
public enum WorkerStatus {
    STOPPED("Berhenti"),
    RUNNING("Berjalan"),
    PAUSED("Jeda"),
    ERROR("Error"),
    COMPLETED("Selesai");
    
    private final String description;
    
    WorkerStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}