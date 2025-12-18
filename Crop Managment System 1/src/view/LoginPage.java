package view;

import controller.UserController;
import model.User;
import dao.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

public class LoginPage extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private int loginAttempts = 0;
    private UserController userController;

    // Theme colors
    private static final Color PRIMARY_GREEN = new Color(34, 139, 34);
    private static final Color PRIMARY_DARK = new Color(25, 90, 25);
    private static final Color LIGHT_BACKGROUND = new Color(245, 247, 250);
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);

   
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║           CROP MANAGEMENT SYSTEM v1.0                      ║");
        System.out.println("║           Starting Application...                          ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");

        // Test database connection
        System.out.println("\n[INFO] Connecting to database...");
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("[SUCCESS] Database connected!\n");
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Database connection failed: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                "Cannot connect to database!\n\n" +
                "Error: " + e.getMessage() + "\n\n" +
                "Please check:\n" +
                "1. PostgreSQL is running\n" +
                "2. Database 'crop_management_system_db' exists\n" +
                "3. Username and password are correct",
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equalsIgnoreCase(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
               
            }
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginPage().setVisible(true);
                System.out.println("[SUCCESS] Application started!");
                System.out.println("\n╔════════════════════════════════════════╗");
                System.out.println("║         LOGIN CREDENTIALS              ║");
                System.out.println("╠════════════════════════════════════════╣");
                System.out.println("║  Admin:  admin@farm.com / admin123     ║");
                System.out.println("║  Farmer: john@farm.com / farmer123     ║");
                System.out.println("╚════════════════════════════════════════╝\n");
            }
        });
    }

  
    public LoginPage() {
        userController = new UserController();
        
        setTitle("Crop Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initUI();

        pack();
        setLocationRelativeTo(null);
    }

 
    private void initUI() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(LIGHT_BACKGROUND);
        setContentPane(content);

        JPanel leftPanel = new GradientPanel();
        leftPanel.setPreferredSize(new Dimension(360, 540));
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        JLabel appTitle = new JLabel("<html><div style='text-align:left;'>CROP<br>MANAGEMENT<br>SYSTEM</div></html>");
        appTitle.setForeground(Color.WHITE);
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JLabel tagline = new JLabel("<html>"
                + "Monitor crops, manage fields,<br>"
                + "and optimize yields in a<br>"
                + "single integrated platform."
                + "</html>");
        tagline.setForeground(new Color(230, 250, 230));
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel leftTextPanel = new JPanel();
        leftTextPanel.setOpaque(false);
        leftTextPanel.setLayout(new BoxLayout(leftTextPanel, BoxLayout.Y_AXIS));
        leftTextPanel.add(appTitle);
        leftTextPanel.add(Box.createVerticalStrut(15));
        leftTextPanel.add(tagline);

        leftPanel.add(leftTextPanel, BorderLayout.NORTH);

        JPanel featuresPanel = new JPanel();
        featuresPanel.setOpaque(false);
        featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));
        featuresPanel.add(createBulletLabel("Real-time crop monitoring"));
        featuresPanel.add(Box.createVerticalStrut(6));
        featuresPanel.add(createBulletLabel("Field-wise yield analytics"));
        featuresPanel.add(Box.createVerticalStrut(6));
        featuresPanel.add(createBulletLabel("Smart alerts & reminders"));
        featuresPanel.add(Box.createVerticalStrut(6));
        featuresPanel.add(createBulletLabel("Sales tracking & reports"));

        leftPanel.add(featuresPanel, BorderLayout.SOUTH);

        content.add(leftPanel, BorderLayout.WEST);

        JPanel rightWrapper = new JPanel(new GridBagLayout());
        rightWrapper.setBackground(LIGHT_BACKGROUND);

        JPanel loginCard = createLoginCard();
        rightWrapper.add(loginCard);

        content.add(rightWrapper, BorderLayout.CENTER);
    }

    private JLabel createBulletLabel(String text) {
        JLabel label = new JLabel("✓ " + text);
        label.setForeground(new Color(225, 245, 225));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return label;
    }

    private JPanel createLoginCard() {
        RoundedPanel card = new RoundedPanel(18);
        card.setBackground(CARD_BACKGROUND);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
        card.setPreferredSize(new Dimension(400, 420));

        JLabel title = new JLabel("Sign in");
        title.setFont(TITLE_FONT);
        title.setForeground(Color.DARK_GRAY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Welcome back, please login to continue.");
        subtitle.setFont(SUBTITLE_FONT);
        subtitle.setForeground(new Color(120, 120, 120));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(25));

        JLabel emailLabel = new JLabel("Email / Username");
        emailLabel.setFont(LABEL_FONT);
        emailLabel.setForeground(Color.DARK_GRAY);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(emailLabel);
        card.add(Box.createVerticalStrut(6));

        emailField = new JTextField();
        styleTextField(emailField);
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(emailField);
        card.add(Box.createVerticalStrut(18));
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(LABEL_FONT);
        passLabel.setForeground(Color.DARK_GRAY);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(passLabel);
        card.add(Box.createVerticalStrut(6));

        passwordField = new JPasswordField();
        styleTextField(passwordField);
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(passwordField);
        card.add(Box.createVerticalStrut(10));

        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.setOpaque(false);
        optionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        optionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        JCheckBox showPassword = new JCheckBox("Show password");
        showPassword.setOpaque(false);
        showPassword.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        showPassword.setForeground(new Color(90, 90, 90));
        showPassword.setFocusPainted(false);
        showPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (showPassword.isSelected()) {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('•');
                }
            }
        });
        optionsPanel.add(showPassword, BorderLayout.WEST);

        JLabel forgotLabel = new JLabel("Forgot password?");
        forgotLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        forgotLabel.setForeground(new Color(60, 120, 200));
        forgotLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(LoginPage.this,
                    "Password recovery feature coming soon!\n\nPlease contact administrator.",
                    "Forgot Password",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            public void mouseEntered(MouseEvent e) {
                forgotLabel.setForeground(new Color(30, 90, 170));
            }
            public void mouseExited(MouseEvent e) {
                forgotLabel.setForeground(new Color(60, 120, 200));
            }
        });
        optionsPanel.add(forgotLabel, BorderLayout.EAST);

        card.add(optionsPanel);
        card.add(Box.createVerticalStrut(22));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        loginButton = new JButton("Login");
        stylePrimaryButton(loginButton);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        cancelButton = new JButton("Cancel");
        styleSecondaryButton(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    LoginPage.this,
                    "Are you sure you want to exit?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        card.add(buttonPanel);
        card.add(Box.createVerticalStrut(22));

      
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(230, 230, 230));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        card.add(separator);
        card.add(Box.createVerticalStrut(18));

        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        registerPanel.setOpaque(false);
        registerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel info = new JLabel("Don't have an account? ");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        info.setForeground(new Color(110, 110, 110));

        JLabel registerLink = new JLabel("Register");
        registerLink.setFont(new Font("Segoe UI", Font.BOLD, 12));
        registerLink.setForeground(new Color(30, 120, 200));
        registerLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                handleRegistration();
            }
            public void mouseEntered(MouseEvent e) {
                registerLink.setForeground(new Color(10, 90, 170));
            }
            public void mouseExited(MouseEvent e) {
                registerLink.setForeground(new Color(30, 120, 200));
            }
        });

        registerPanel.add(info);
        registerPanel.add(registerLink);

        card.add(registerPanel);
        card.add(Box.createVerticalStrut(15));

        JPanel demoPanel = new JPanel();
        demoPanel.setLayout(new BoxLayout(demoPanel, BoxLayout.Y_AXIS));
        demoPanel.setBackground(new Color(240, 248, 240));
        demoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 230, 200), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        demoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        demoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JLabel demoTitle = new JLabel("💡 Demo Credentials");
        demoTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        demoTitle.setForeground(PRIMARY_DARK);

        JLabel demoInfo = new JLabel("<html>Admin: admin@farm.com / admin123<br>Farmer: john@farm.com / farmer123</html>");
        demoInfo.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        demoInfo.setForeground(new Color(80, 80, 80));

        demoPanel.add(demoTitle);
        demoPanel.add(Box.createVerticalStrut(4));
        demoPanel.add(demoInfo);

        card.add(demoPanel);


        getRootPane().setDefaultButton(loginButton);

        emailField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });

        return card;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setPreferredSize(new Dimension(320, 42));
        
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_GREEN, 2),
                    BorderFactory.createEmptyBorder(9, 11, 9, 11)
                ));
            }
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
                ));
            }
        });
    }

    private void stylePrimaryButton(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_GREEN);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 28, 10, 28));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PRIMARY_DARK);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_GREEN);
            }
        });
    }

    private void styleSecondaryButton(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setForeground(new Color(80, 80, 80));
        button.setBackground(new Color(230, 230, 230));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(210, 210, 210));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(230, 230, 230));
            }
        });
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both email and password.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            if (email.isEmpty()) {
                emailField.requestFocus();
            } else {
                passwordField.requestFocus();
            }
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("Signing in...");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        SwingWorker<Object[], Void> worker = new SwingWorker<Object[], Void>() {
            @Override
            protected Object[] doInBackground() throws Exception {
                return userController.login(email, password);
            }

            @Override
            protected void done() {
                try {
                    Object[] result = get();
                    User user = (User) result[0];
                    String message = (String) result[1];

                    if ("SUCCESS".equals(message) && user != null) {
                        JOptionPane.showMessageDialog(LoginPage.this,
                            "Welcome, " + user.getName() + "!\n\n" +
                            "Role: " + user.getRole() + "\n" +
                            "Email: " + user.getEmail(),
                            "Login Successful",
                            JOptionPane.INFORMATION_MESSAGE);

                        dispose();

                        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                            new AdminDashboard(user).setVisible(true);
                        } else {
                            new FarmerDashboard(user).setVisible(true);
                        }
                    } else {
                        loginAttempts++;
                        if (loginAttempts >= 5) {
                            JOptionPane.showMessageDialog(LoginPage.this,
                                "Too many failed attempts (" + loginAttempts + "/5).\n" +
                                "Please try again later or contact administrator.",
                                "Login Blocked",
                                JOptionPane.ERROR_MESSAGE);
                            if (loginAttempts >= 10) {
                                System.exit(0);
                            }
                        } else {
                            JOptionPane.showMessageDialog(LoginPage.this,
                                message + "\n\nAttempts: " + loginAttempts + "/5",
                                "Login Failed",
                                JOptionPane.ERROR_MESSAGE);
                        }
                        passwordField.setText("");
                        passwordField.requestFocus();
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(LoginPage.this,
                        "An error occurred during login.\n\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                } finally {
                    loginButton.setEnabled(true);
                    loginButton.setText("Login");
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        };
        worker.execute();
    }

    private void handleRegistration() {
        dispose();
        new RegistrationPage().setVisible(true);
    }

    private static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            int w = getWidth();
            int h = getHeight();

            Color start = new Color(25, 115, 50);
            Color end = new Color(60, 160, 90);

            GradientPaint gp = new GradientPaint(0, 0, start, w, h, end);
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);

        
            g2.setColor(new Color(255, 255, 255, 20));
            g2.fillOval(-30, -30, 150, 150);
            g2.fillOval(w - 80, h - 120, 160, 160);
            g2.fillOval(20, h - 80, 100, 100);

            g2.setColor(new Color(255, 255, 255, 15));
            g2.setStroke(new BasicStroke(1.5f));
            for (int i = 0; i < 5; i++) {
                g2.drawLine(0, 80 + i * 60, w, 120 + i * 60);
            }

            g2.dispose();
        }
    }

    private static class RoundedPanel extends JPanel {
        private final int cornerRadius;

        public RoundedPanel(int radius) {
            super();
            this.cornerRadius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

            g2.setColor(new Color(0, 0, 0, 10));
            g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, cornerRadius, cornerRadius);
            g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);

          
            g2.setColor(new Color(220, 220, 220));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}