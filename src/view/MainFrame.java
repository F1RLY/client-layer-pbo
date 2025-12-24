package view;

import view.components.HeaderPanel;
import view.components.SidebarPanel;
import com.formdev.flatlaf.FlatLightLaf;
import view.panels.*;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;

public class MainFrame extends JFrame {
    private HeaderPanel headerPanel;
    private SidebarPanel sidebarPanel;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    
    // Main panels
    private DashboardPanel dashboardPanel;
    private DokterPanel dokterPanel;
    private PasienPanel pasienPanel;
    
    // Warna terpisah jelas
    private final Color HEADER_COLOR = new Color(41, 128, 185);     // Biru tua
    private final Color SIDEBAR_COLOR = new Color(52, 73, 94);      // Abu-abu biru gelap
    private final Color LOGIN_BG = new Color(236, 240, 241);        // Abu-abu muda
    private final Color MAIN_BG = new Color(245, 245, 245);         // Abu-abu sangat muda
    
    public MainFrame() {
        // Set UI Look
        setupFlatLaf();
        
        initUI();
        setupNavigation();
        showLogin();
        
    }
    // Tambahkan di MainFrame constructor:
private void forceFont() {
    // Force pakai font yang konsisten
    Font font = new Font("Arial", Font.PLAIN, 12);
    
    Enumeration<Object> keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements()) {
        Object key = keys.nextElement();
        Object value = UIManager.get(key);
        if (value instanceof Font) {
            UIManager.put(key, font);
        }
    }
    
    // Set khusus untuk component
    UIManager.put("Button.font", new Font("Arial", Font.PLAIN, 13));
    UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 12));
    UIManager.put("TextField.font", new Font("Arial", Font.PLAIN, 12));
    UIManager.put("Table.font", new Font("Arial", Font.PLAIN, 12));
}
    
 private void setupFlatLaf() {
        try {
            // Gunakan FlatLaf theme
            UIManager.setLookAndFeel(new FlatLightLaf());
            
            // Custom warna FlatLaf
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 8);
            UIManager.put("ProgressBar.arc", 8);
            UIManager.put("TextComponent.arc", 5);
            
            // Warna custom
            UIManager.put("Panel.background", new Color(245, 245, 245));
            UIManager.put("Table.background", Color.WHITE);
            UIManager.put("Table.selectionBackground", new Color(220, 235, 252));
            UIManager.put("Table.selectionForeground", Color.BLACK);
            
        } catch (Exception e) {
            e.printStackTrace();
            try {
                // Fallback ke system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void initUI() {
        setTitle("Aplikasi Manajemen Klinik");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        
        // Main container
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        
        // Initialize panels dengan warna berbeda
        headerPanel = new HeaderPanel(HEADER_COLOR);
        sidebarPanel = new SidebarPanel(SIDEBAR_COLOR);
        dashboardPanel = new DashboardPanel(MAIN_BG);
        dokterPanel = new DokterPanel(MAIN_BG);
        pasienPanel = new PasienPanel(MAIN_BG);
        
        // Main content area
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(MAIN_BG);
        
        // Tambahkan semua panel
        mainContentPanel.add(createLoginPanel(), "LOGIN");
        mainContentPanel.add(dashboardPanel, "DASHBOARD");
        mainContentPanel.add(dokterPanel, "DOKTER");
        mainContentPanel.add(pasienPanel, "PASIEN");
        
        // Tambahkan ke frame
        container.add(headerPanel, BorderLayout.NORTH);
        container.add(sidebarPanel, BorderLayout.WEST);
        container.add(mainContentPanel, BorderLayout.CENTER);
    }
    
    private void setupNavigation() {
        // Dashboard
        sidebarPanel.getBtnDashboard().addActionListener(e -> {
            sidebarPanel.setActiveButton(sidebarPanel.getBtnDashboard());
            cardLayout.show(mainContentPanel, "DASHBOARD");
            setTitle("Dashboard - Aplikasi Klinik");
        });
        
        // Dokter
        sidebarPanel.getBtnDokter().addActionListener(e -> {
            sidebarPanel.setActiveButton(sidebarPanel.getBtnDokter());
            cardLayout.show(mainContentPanel, "DOKTER");
            setTitle("Data Dokter - Aplikasi Klinik");
        });
        
        // Pasien
        sidebarPanel.getBtnPasien().addActionListener(e -> {
            sidebarPanel.setActiveButton(sidebarPanel.getBtnPasien());
            cardLayout.show(mainContentPanel, "PASIEN");
            setTitle("Data Pasien - Aplikasi Klinik");
        });
        
        // Logout
        sidebarPanel.getBtnLogout().addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                showLogin();
            }
        });
    }
    
    private void showLogin() {
        cardLayout.show(mainContentPanel, "LOGIN");
        headerPanel.setVisible(false);
        sidebarPanel.setVisible(false);
        setTitle("Login - Aplikasi Klinik");
    }
    
    private JPanel createLoginPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(LOGIN_BG);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(HEADER_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        JLabel titleLabel = new JLabel("🏥 APLIKASI MANAJEMEN KLINIK");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titlePanel, gbc);
        
        // Login Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        
        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.insets = new Insets(15, 15, 15, 15);
        fgbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username
        fgbc.gridx = 0; fgbc.gridy = 0;
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(lblUser, fgbc);
        
        JTextField txtUser = new JTextField(20);
        txtUser.setPreferredSize(new Dimension(250, 35));
        txtUser.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        fgbc.gridx = 1;
        formPanel.add(txtUser, fgbc);
        
        // Password
        fgbc.gridx = 0; fgbc.gridy = 1;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(lblPass, fgbc);
        
        JPasswordField txtPass = new JPasswordField(20);
        txtPass.setPreferredSize(new Dimension(250, 35));
        txtPass.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        fgbc.gridx = 1;
        formPanel.add(txtPass, fgbc);
        
        // Login Button
        fgbc.gridx = 0; fgbc.gridy = 2;
        fgbc.gridwidth = 2;
        fgbc.insets = new Insets(30, 0, 0, 0);
        JButton btnLogin = new JButton("🔐 MASUK KE APLIKASI");
        btnLogin.setBackground(HEADER_COLOR);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        btnLogin.setFocusPainted(false);
        
        btnLogin.addActionListener(e -> {
            String user = txtUser.getText();
            String pass = new String(txtPass.getPassword());
            
            if (user.equals("admin") && pass.equals("admin")) {
                cardLayout.show(mainContentPanel, "DASHBOARD");
                headerPanel.setVisible(true);
                sidebarPanel.setVisible(true);
                sidebarPanel.setActiveButton(sidebarPanel.getBtnDashboard());
                setTitle("Dashboard - Aplikasi Klinik");
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Username atau password salah!\n\nGunakan:\nUsername: admin\nPassword: admin", 
                    "Login Gagal", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        formPanel.add(btnLogin, fgbc);
        
        // Add form to main panel
        gbc.gridy = 1;
        gbc.insets = new Insets(40, 0, 0, 0);
        mainPanel.add(formPanel, gbc);
        
        // Footer info
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        JLabel footer = new JLabel("© 2024 Klinik Sehat Mandiri - Versi 1.0");
        footer.setFont(new Font("Arial", Font.ITALIC, 12));
        footer.setForeground(Color.GRAY);
        mainPanel.add(footer, gbc);
        
        return mainPanel;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}