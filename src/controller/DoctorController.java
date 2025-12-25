package controller;

import model.Dokter;
import java.util.ArrayList;
import java.util.List;

public class DoctorController {
    private List<Dokter> dokterList = new ArrayList<>();

    public void addDokter(String nama, String spesialis) {
        int id = dokterList.size() + 1;
        dokterList.add(new Dokter(id, nama, spesialis));
    }

    public List<Dokter> getAllDokter() {
        return dokterList;
    }
}