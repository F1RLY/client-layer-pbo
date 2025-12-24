package view;

import controller.DokterController;
import controller.PasienController;
import view.components.HeaderPanel;
import view.components.SidebarPanel;
import view.panels.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private HeaderPanel headerPanel;
    private SidebarPanel sidebarPanel;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    
    // Main panels
    private DashboardPanel dashboardPanel;
    private DokterPanel dokterPanel;
    private PasienPanel pasienPanel;
    
    // Controllers
    private DokterController dokterController;
    private PasienController pasienController;
    
    // Warna
    private final Color HEADER_COLOR = new Color(41, 128, 185);
    private final Color SIDEBAR_COLOR = new Color(52, 73, 94);
    private final Color MAIN_BG = new Color(245, 245, 245);
    
    public MainFrame() {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        initUI();
        setupNavigation();
        showLogin();
    }
    
    private void initUI() {
        setTitle("Aplikasi Manajemen Klinik");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        
        // Initialize panels
        headerPanel = new HeaderPanel(HEADER_COLOR);
        sidebarPanel = new SidebarPanel(SIDEBAR_COLOR);
        dashboardPanel = new DashboardPanel(MAIN_BG);
        dokterPanel = new DokterPanel(MAIN_BG);
        pasienPanel = new PasienPanel(MAIN_BG);
        
        // Initialize controllers
        dokterController = new DokterController(dokterPanel);
        pasienController = new PasienController(pasienPanel);
        
        // Connect panels with controllers
        dokterPanel.setController(dokterController);
        pasienPanel.setController(pasienController);
        
        // Main content area
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(MAIN_BG);
        
        // Tambahkan semua panel
        mainContentPanel.add(createLoginPanel(), "LOGIN");
        mainContentPanel.add(dashboardPanel, "DASHBOARD");
        mainContentPanel.add(dokterPanel, "DOKTER");
        mainContentPanel.add(pasienPanel, "PASIEN");
        
        // SELALU tambahkan header dan sidebar ke frame (akan disembunyikan saat login)
        container.add(headerPanel, BorderLayout.NORTH);
        container.add(sidebarPanel, BorderLayout.WEST);
        container.add(mainContentPanel, BorderLayout.CENTER);
    }
    
    private void setupNavigation() {
        // Dashboard
        sidebarPanel.getBtnDashboard().addActionListener(e -> {
            sidebarPanel.setActiveButton(sidebarPanel.getBtnDashboard());
            showDashboard();
        });
        
        // Dokter
        sidebarPanel.getBtnDokter().addActionListener(e -> {
            sidebarPanel.setActiveButton(sidebarPanel.getBtnDokter());
            showDokterPanel();
        });
        
        // Pasien
        sidebarPanel.getBtnPasien().addActionListener(e -> {
            sidebarPanel.setActiveButton(sidebarPanel.getBtnPasien());
            showPasienPanel();
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
        headerPanel.setVisible(false);    // Sembunyikan header
        sidebarPanel.setVisible(false);   // Sembunyikan sidebar
        setTitle("Login - Aplikasi Klinik");
        
        // Force revalidate untuk update UI
        revalidate();
        repaint();
    }
    
    private void showDashboard() {
        cardLayout.show(mainContentPanel, "DASHBOARD");
        headerPanel.setVisible(true);     // Tampilkan header
        sidebarPanel.setVisible(true);    // Tampilkan sidebar
        sidebarPanel.setActiveButton(sidebarPanel.getBtnDashboard());
        setTitle("Dashboard - Aplikasi Klinik");
        
        // Force revalidate untuk update UI
        revalidate();
        repaint();
    }
    
    private void showDokterPanel() {
        cardLayout.show(mainContentPanel, "DOKTER");
        headerPanel.setVisible(true);
        sidebarPanel.setVisible(true);
        sidebarPanel.setActiveButton(sidebarPanel.getBtnDokter());
        setTitle("Data Dokter - Aplikasi Klinik");
        
        revalidate();
        repaint();
    }
    
    private void showPasienPanel() {
        cardLayout.show(mainContentPanel, "PASIEN");
        headerPanel.setVisible(true);
        sidebarPanel.setVisible(true);
        sidebarPanel.setActiveButton(sidebarPanel.getBtnPasien());
        setTitle("Data Pasien - Aplikasi Klinik");
        
        revalidate();
        repaint();
    }
    
    private JPanel createLoginPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // === HEADER LOGIN ===
        JPanel loginHeaderPanel = new JPanel(new BorderLayout(20, 0));
        loginHeaderPanel.setBackground(new Color(41, 128, 185));
        loginHeaderPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Icon besar [H] dalam kotak
        JPanel bigIconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Kotak biru dengan sudut melengkung
                g2.setColor(new Color(52, 152, 219));
                g2.fillRoundRect(10, 10, 80, 80, 20, 20);
                
                // Border putih
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(10, 10, 80, 80, 20, 20);
                
                // Teks H putih
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 36));
                FontMetrics fm = g2.getFontMetrics();
                int x = (100 - fm.stringWidth("H")) / 2;
                int y = (100 - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString("H", x, y);
            }
        };
        bigIconPanel.setPreferredSize(new Dimension(100, 100));
        bigIconPanel.setOpaque(false);
        
        // Title text
        JLabel titleLabel = new JLabel("<html><center><font size='6'>APLIKASI MANAJEMEN KLINIK</font><br>" +
                                      "<font size='4'>Sistem Pelayanan Kesehatan Terpadu</font></center></html>");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        loginHeaderPanel.add(bigIconPanel, BorderLayout.WEST);
        loginHeaderPanel.add(titleLabel, BorderLayout.CENTER);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(loginHeaderPanel, gbc);
        
        // === LOGIN FORM ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));
        
        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.insets = new Insets(15, 15, 15, 15);
        fgbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Form Title
        JLabel formTitle = new JLabel("LOGIN SISTEM");
        formTitle.setFont(new Font("Arial", Font.BOLD, 22));
        formTitle.setForeground(new Color(41, 128, 185));
        formTitle.setHorizontalAlignment(SwingConstants.CENTER);
        fgbc.gridx = 0;
        fgbc.gridy = 0;
        fgbc.gridwidth = 2;
        formPanel.add(formTitle, fgbc);
        
        // Separator
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 200, 200));
        fgbc.gridy = 1;
        fgbc.insets = new Insets(10, 50, 20, 50);
        formPanel.add(separator, fgbc);
        
        // Username Field
        fgbc.gridy = 2;
        fgbc.insets = new Insets(10, 15, 10, 15);
        fgbc.gridwidth = 1;
        
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userLabel.setForeground(new Color(41, 128, 185));
        fgbc.gridx = 0;
        formPanel.add(userLabel, fgbc);
        
        JTextField txtUser = new JTextField(20);
        txtUser.setPreferredSize(new Dimension(250, 40));
        txtUser.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUser.setText("admin");
        txtUser.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        fgbc.gridx = 1;
        formPanel.add(txtUser, fgbc);
        
        // Password Field
        fgbc.gridy = 3;
        fgbc.gridx = 0;
        
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passLabel.setForeground(new Color(231, 76, 60));
        formPanel.add(passLabel, fgbc);
        
        JPasswordField txtPass = new JPasswordField(20);
        txtPass.setPreferredSize(new Dimension(250, 40));
        txtPass.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPass.setText("admin");
        txtPass.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        fgbc.gridx = 1;
        formPanel.add(txtPass, fgbc);
        
        // Login Button
        fgbc.gridy = 4;
        fgbc.gridx = 0;
        fgbc.gridwidth = 2;
        fgbc.insets = new Insets(30, 50, 0, 50);
        
        JButton btnLogin = new JButton("LOGIN SEKARANG");
        btnLogin.setBackground(new Color(41, 128, 185));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(30, 100, 160), 2),
            BorderFactory.createEmptyBorder(15, 40, 15, 40)
        ));
        btnLogin.setFocusPainted(false);
        
        // Action listener - PENTING: panggil showDashboard()
        btnLogin.addActionListener(e -> {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();
            
            if (user.equals("admin") && pass.equals("admin")) {
                showDashboard();  // INI YANG MEMANGGIL showDashboard()
            } else {
                JOptionPane.showMessageDialog(this,
                    "LOGIN GAGAL!\n\n" +
                    "Username atau password salah.\n" +
                    "Gunakan:\n" +
                    "• Username: admin\n" +
                    "• Password: admin",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Tambahkan Enter key support
        txtPass.addActionListener(e -> btnLogin.doClick());
        
        formPanel.add(btnLogin, fgbc);
        
        // Info
        fgbc.gridy = 5;
        fgbc.insets = new Insets(20, 50, 0, 50);
        JLabel infoLabel = new JLabel("Gunakan: admin / admin (default)");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(infoLabel, fgbc);
        
        // Add form to main panel
        gbc.gridy = 1;
        gbc.insets = new Insets(40, 50, 0, 50);
        mainPanel.add(formPanel, gbc);
        
        // === FOOTER ===
        gbc.gridy = 2;
        gbc.insets = new Insets(30, 0, 20, 0);
        JLabel footer = new JLabel("© 2024 Klinik Sehat Mandiri | Versi 1.0.0");
        footer.setFont(new Font("Arial", Font.ITALIC, 11));
        footer.setForeground(new Color(150, 150, 150));
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(footer, gbc);
        
        return mainPanel;
    }
    
    // Getter untuk controller jika diperlukan
    public DokterController getDokterController() {
        return dokterController;
    }
    
    public PasienController getPasienController() {
        return pasienController;
    }
    
    public DashboardPanel getDashboardPanel() {
        return dashboardPanel;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}