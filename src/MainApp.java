import view.MainFrame;
import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        // 1. Set Look and Feel agar tampilan modern
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. Log awal (Opsional untuk debugging)
        System.out.println("Starting SMARS v1.0...");

        // 3. Menjalankan MainFrame
        SwingUtilities.invokeLater(() -> {
            MainFrame app = new MainFrame();
            app.setVisible(true);
        });
    }
}