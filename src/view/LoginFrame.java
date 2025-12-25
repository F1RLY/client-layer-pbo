package view;

import javax.swing.*;
import java.awt.*;
import controller.AuthController;
import model.User;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit;
    
    public LoginFrame() {
        initComponents();
        setupFrame();
    }
    
    private void initComponents() {
        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTitle = new JLabel("LOGIN SMARS");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(lblTitle, gbc);
        
        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        txtUsername = new JTextField(15);
        mainPanel.add(txtUsername, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        txtPassword = new JPasswordField(15);
        mainPanel.add(txtPassword, gbc);
        
        // Buttons Panel
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        btnLogin = new JButton("Login");
        btnExit = new JButton("Exit");
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnExit);
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
    }
    
    private void setupFrame() {
        setTitle("SMARS Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Actions
        btnLogin.addActionListener(e -> loginAction());
        btnExit.addActionListener(e -> System.exit(0));
        
        // Enter key for login
        getRootPane().setDefaultButton(btnLogin);
    }
    
    private void loginAction() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        
        // Simple validation
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Username dan password harus diisi!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Authentication
        AuthController auth = new AuthController();
        User user = auth.login(username, password);
        
        if (user != null) {
            // Open main dashboard
            new MainDashboard(user).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Login gagal!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}