package view.panels;

import controller.PatientController;
import controller.DoctorController;
import controller.QueueController;
import javax.swing.*;
import java.awt.*;

public class TransaksiAntreanPanel extends JPanel {
    private PatientController pc;
    private DoctorController dc;
    private QueueController qc;

    // Pastikan urutan parameter sama dengan di MainFrame
    public TransaksiAntreanPanel(PatientController pc, DoctorController dc, QueueController qc) {
        this.pc = pc;
        this.dc = dc;
        this.qc = qc;
        
        setLayout(new BorderLayout());
        add(new JLabel("Halaman Pendaftaran Antrean"), BorderLayout.CENTER);
        // ... sisa kode komponen Anda ...
    }
}