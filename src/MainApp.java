// File: MainApp.java
import view.LoginFrame;
import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        // Set Look and Feel to System Default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Run the application
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}