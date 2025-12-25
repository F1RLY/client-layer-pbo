import view.MainFrame;
import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        // Set tema agar tidak kaku (Windows/Mac Style)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Starting SMARS App...");

        // Jalankan UI
        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error Launching App: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}