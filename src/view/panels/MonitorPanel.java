package view.panels;

import controller.QueueController;
import javax.swing.*;
import java.awt.*;

public class MonitorPanel extends JPanel {
    private QueueController qc;

    public MonitorPanel(QueueController qc) {
        this.qc = qc;
        
        setLayout(new BorderLayout());
        add(new JLabel("Halaman Monitor TV"), BorderLayout.CENTER);
        // ... sisa kode komponen Anda ...
    }
}