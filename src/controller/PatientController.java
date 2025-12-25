package controller;

import model.Pasien;
import java.util.ArrayList;
import java.util.List;

public class PatientController {
    private List<Pasien> pasienList = new ArrayList<>();

    public void addPasien(String nama, String nik) {
        int id = pasienList.size() + 1;
        pasienList.add(new Pasien(id, nama, nik));
    }

    public List<Pasien> getAllPasien() {
        return pasienList;
    }

    public void deletePasien(int index) {
        if (index >= 0 && index < pasienList.size()) {
            pasienList.remove(index);
        }
    }
}