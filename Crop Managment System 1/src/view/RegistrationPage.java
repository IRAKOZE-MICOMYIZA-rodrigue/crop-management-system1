package view;

import controller.UserController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegistrationPage extends JFrame {
    private JTextField nameField;
    private JTextField farmLocationField;
    private JTextField phoneField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private UserController userController;
    
    public RegistrationPage() {
        userController = new UserController();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Crop Management System - Registration");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(34, 139, 34), 0, getHeight(), new Color(0, 100, 0));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        
        JPanel regPanel = new JPanel();
        regPanel.setLayout(new BoxLayout(regPanel, BoxLayout.Y_AXIS));
        regPanel.setBackground(Color.WHITE);
        regPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        regPanel.setPreferredSize(new Dimension(420, 570));
        
        JLabel logoLabel = new JLabel("🌾");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(34, 139, 34));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Join our farming community");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        nameField = new JTextField();
        farmLocationField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        
        JPanel namePanel = createFormField("Full Name *", nameField);
        JPanel farmPanel = createFormField("Farm Location", farmLocationField);
        JPanel phonePanel = createFormField("Phone Number", phoneField);
        JPanel emailPanel = createFormField("Email Address *", emailField);
        JPanel passPanel = createFormField("Password * (min 6 chars)", passwordField);
        JPanel confirmPanel = createFormField("Confirm Password *", confirmPasswordField);
        
        registerButton = new JButton("Create Account");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(34, 139, 34));
        registerButton.setForeground(Color.WHITE);
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setOpaque(true);
        
        registerButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(new Color(0, 120, 0));
            }
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(new Color(34, 139, 34));
            }
        });
        
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel hasAccountLabel = new JLabel("Already have an account?");
        hasAccountLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        hasAccountLabel.setForeground(Color.GRAY);
        
        JButton loginButton = new JButton("Sign In");
        loginButton.setFont(new Font("Arial", Font.BOLD, 11));
        loginButton.setForeground(new Color(34, 139, 34));
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setFocusPainted(false);
        
        loginPanel.add(hasAccountLabel);
        loginPanel.add(loginButton);
        
        regPanel.add(logoLabel);
        regPanel.add(titleLabel);
        regPanel.add(Box.createVerticalStrut(3));
        regPanel.add(subtitleLabel);
        regPanel.add(Box.createVerticalStrut(20));
        regPanel.add(namePanel);
        regPanel.add(Box.createVerticalStrut(10));
        regPanel.add(farmPanel);
        regPanel.add(Box.createVerticalStrut(10));
        regPanel.add(phonePanel);
        regPanel.add(Box.createVerticalStrut(10));
        regPanel.add(emailPanel);
        regPanel.add(Box.createVerticalStrut(10));
        regPanel.add(passPanel);
        regPanel.add(Box.createVerticalStrut(10));
        regPanel.add(confirmPanel);
        regPanel.add(Box.createVerticalStrut(20));
        regPanel.add(registerButton);
        regPanel.add(Box.createVerticalStrut(12));
        regPanel.add(loginPanel);
        
        mainPanel.add(regPanel);
        add(mainPanel);
        
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleRegistration();
            }
        });
        
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LoginPage().setVisible(true);
                dispose();
            }
        });
        
        confirmPasswordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleRegistration();
                }
            }
        });
    }
    
    private JPanel createFormField(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setForeground(new Color(70, 70, 70));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        if (field instanceof JTextField) {
            ((JTextField) field).setFont(new Font("Arial", Font.PLAIN, 13));
        }
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(4));
        panel.add(field);
        
        return panel;
    }
    
    private void handleRegistration() {
        String name = nameField.getText().trim();
        String farmLocation = farmLocationField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        registerButton.setEnabled(false);
        registerButton.setText("Creating account...");
        
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            protected String doInBackground() {
                return userController.registerUser(name, farmLocation, phone, email, password, confirmPassword);
            }
            
            protected void done() {
                try {
                    String result = get();
                    
                    if ("SUCCESS".equals(result)) {
                        JOptionPane.showMessageDialog(RegistrationPage.this, 
                            "Account created successfully!\nYou can now login.", 
                            "Registration Successful", 
                            JOptionPane.INFORMATION_MESSAGE);
                        new LoginPage().setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(RegistrationPage.this, 
                            result, 
                            "Registration Failed", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(RegistrationPage.this, 
                        "Error: " + e.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                } finally {
                    registerButton.setEnabled(true);
                    registerButton.setText("Create Account");
                }
            }
        };
        worker.execute();
    }
}