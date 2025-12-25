package view.panels;

import controller.PatientController;
import controller.DoctorController;
import javax.swing.*;
import java.awt.*;

public class MasterDataPanel extends JPanel {
    private String tipe;
    private PatientController pc;
    private DoctorController dc;

    // Constructor harus menerima Object controller
    public MasterDataPanel(String tipe, Object controller) {
        this.tipe = tipe;
        
        // Cek tipe dan simpan controllernya
        if (tipe.equals("PASIEN")) {
            this.pc = (PatientController) controller;
        } else {
            this.dc = (DoctorController) controller;
        }
        
        setLayout(new BorderLayout());
        add(new JLabel("Halaman Master Data " + tipe), BorderLayout.CENTER);
        // ... sisa kode komponen Anda ...
    }
}