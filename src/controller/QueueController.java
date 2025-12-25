package controller;

import model.Antrean;
import model.Pasien;
import model.Dokter;
import java.util.LinkedList;
import java.util.Queue;

public class QueueController {
    private Queue<Antrean> antreanQueue = new LinkedList<>();
    private int counter = 1;

    public String generateNumber() {
        return String.format("A-%03d", counter++);
    }

    public void registerAntrean(Pasien p, Dokter d) {
        String nomor = generateNumber();
        Antrean antrean = new Antrean(antreanQueue.size() + 1, nomor, p, d);
        antreanQueue.add(antrean);
    }

    public Antrean panggilAntreanNext() {
        return antreanQueue.poll(); // Ambil dan hapus dari antrean (FIFO)
    }

    public int getTotalAntrean() {
        return antreanQueue.size();
    }
}