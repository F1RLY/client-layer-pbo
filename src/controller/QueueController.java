package controller;

import model.Antrean;
import api.AntreanApiClient;

public class QueueController {
    private final AntreanApiClient apiClient = new AntreanApiClient();

    public Antrean getCurrentCalling() {
        try {
            return apiClient.fetchCurrentCalling();
        } catch (Exception e) {
            // Log error ke konsol untuk mempermudah debug di VS Code
            System.err.println("Queue Error: " + e.getMessage());
            return null;
        }
    }
}