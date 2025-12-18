package view;

import controller.CropController;
import controller.SaleController;
import controller.CropTypeController;
import model.Crop;
import model.Sale;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class FarmerDashboard extends JFrame {
    private User currentUser;
    private JTable cropTable;
    private JTable salesTable;
    private DefaultTableModel cropTableModel;
    private DefaultTableModel salesTableModel;
    private JLabel totalEarningsLabel;
    private JLabel totalCropsLabel;
    private JLabel harvestedCropsLabel;
    private JLabel pendingHarvestLabel;
    
    private CropController cropController;
    private SaleController saleController;
    private CropTypeController cropTypeController;
    
    private final Color PRIMARY_GREEN = new Color(34, 139, 34);
    private final Color DARK_GREEN = new Color(0, 100, 0);
    private final Color LIGHT_GREEN = new Color(144, 238, 144);
    private final Color BG_COLOR = new Color(245, 247, 250);
    private final Color SIDEBAR_COLOR = Color.WHITE;
    
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private JButton cropsNavBtn;
    private JButton salesNavBtn;
    private JButton welcomeNavBtn;
    
    public FarmerDashboard(User user) {
        this.currentUser = user;
        this.cropController = new CropController();
        this.saleController = new SaleController();
        this.cropTypeController = new CropTypeController();
        initializeUI();
        loadData();
    }
    
    private void initializeUI() {
        setTitle("Crop Management System - " + currentUser.getName());
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 700));
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);
        
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createMainContentPanel(), BorderLayout.CENTER);
        mainPanel.add(createStatsPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_GREEN);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        headerPanel.setPreferredSize(new Dimension(0, 70));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Crop Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        
        leftPanel.add(titleLabel);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel("User: " + currentUser.getName());
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userLabel.setForeground(Color.WHITE);
        
        String location = currentUser.getFarmLocation() != null ? currentUser.getFarmLocation() : "";
        JLabel locationLabel = new JLabel(location.isEmpty() ? "" : "Location: " + location);
        locationLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        locationLabel.setForeground(new Color(200, 255, 200));
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setForeground(PRIMARY_GREEN);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> logout());
        
        rightPanel.add(userLabel);
        if (!location.isEmpty()) {
            rightPanel.add(locationLabel);
        }
        rightPanel.add(Box.createHorizontalStrut(15));
        rightPanel.add(logoutButton);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createMainContentPanel() {
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(BG_COLOR);
        
        JPanel sidebar = createSidebar();
        mainContent.add(sidebar, BorderLayout.WEST);
        
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(BG_COLOR);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        mainContentPanel.add(createWelcomePanel(), "WELCOME");
        mainContentPanel.add(createCropsPanel(), "CROPS");
        mainContentPanel.add(createSalesPanel(), "SALES");
        
        mainContent.add(mainContentPanel, BorderLayout.CENTER);
        
        cardLayout.show(mainContentPanel, "WELCOME");
        setActiveNavButton(welcomeNavBtn);
        
        return mainContent;
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        JPanel topSection = createUserIconSection();
        sidebar.add(topSection, BorderLayout.NORTH);
        
        JPanel navSection = createNavigationSection();
        sidebar.add(navSection, BorderLayout.CENTER);
        
        return sidebar;
    }
    
    private JPanel createUserIconSection() {
        JPanel userSection = new JPanel();
        userSection.setBackground(new Color(250, 250, 250));
        userSection.setLayout(new BoxLayout(userSection, BoxLayout.Y_AXIS));
        userSection.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(25, 20, 25, 20)
        ));

        JPanel iconContainer = new JPanel(new GridBagLayout());
        iconContainer.setBackground(new Color(250, 250, 250));
        iconContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel userIcon = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                int size = Math.min(width, height) - 10;
                int x = (width - size) / 2;
                int y = (height - size) / 2;

                GradientPaint gradient = new GradientPaint(
                    x, y, PRIMARY_GREEN.brighter(),
                    x + size, y + size, PRIMARY_GREEN.darker()
                );
                g2.setPaint(gradient);
                
                g2.fillOval(x, y, size, size);

                GradientPaint highlight = new GradientPaint(
                    x, y, new Color(255, 255, 255, 100),
                    x + size/2, y + size/2, new Color(255, 255, 255, 0)
                );
                g2.setPaint(highlight);
                g2.fillOval(x + 5, y + 5, size - 30, size - 30);
                
                g2.setColor(PRIMARY_GREEN.darker().darker());
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(x, y, size, size);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 28));
                FontMetrics fm = g2.getFontMetrics();
                String initial = currentUser.getName().substring(0, 1).toUpperCase();
                int textX = (width - fm.stringWidth(initial)) / 2;
                int textY = (height - fm.getHeight()) / 2 + fm.getAscent();
                
                g2.setColor(new Color(0, 0, 0, 50));
                g2.drawString(initial, textX + 1, textY + 1);
                
                g2.setColor(Color.WHITE);
                g2.drawString(initial, textX, textY);
                
                g2.dispose();
            }
        };
        userIcon.setPreferredSize(new Dimension(90, 90));
        userIcon.setMinimumSize(new Dimension(90, 90));
        userIcon.setMaximumSize(new Dimension(90, 90));
        
        iconContainer.add(userIcon);
        userSection.add(iconContainer);
        
        userSection.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel userNameLabel = new JLabel(currentUser.getName());
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userNameLabel.setForeground(DARK_GREEN);
        userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userRoleLabel = new JLabel("FARMER");
        userRoleLabel.setFont(new Font("Arial", Font.BOLD, 11));
        userRoleLabel.setForeground(Color.GRAY);
        userRoleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userRoleLabel.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        userRoleLabel.setBackground(new Color(240, 240, 240));
        userRoleLabel.setOpaque(true);
        
        userSection.add(userNameLabel);
        userSection.add(Box.createRigidArea(new Dimension(0, 8)));
        userSection.add(userRoleLabel);
        
        return userSection;
    }
    
    private JPanel createNavigationSection() {
        JPanel navSection = new JPanel();
        navSection.setBackground(SIDEBAR_COLOR);
        navSection.setLayout(new BoxLayout(navSection, BoxLayout.Y_AXIS));
        navSection.setBorder(BorderFactory.createEmptyBorder(25, 15, 25, 15));

        welcomeNavBtn = createNavButton("Welcome");
        welcomeNavBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        welcomeNavBtn.addActionListener(e -> {
            cardLayout.show(mainContentPanel, "WELCOME");
            setActiveNavButton(welcomeNavBtn);
        });
        navSection.add(welcomeNavBtn);
        
        navSection.add(Box.createRigidArea(new Dimension(0, 10)));

        cropsNavBtn = createNavButton("My Crops");
        cropsNavBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        cropsNavBtn.addActionListener(e -> {
            cardLayout.show(mainContentPanel, "CROPS");
            setActiveNavButton(cropsNavBtn);
            loadCrops();
        });
        navSection.add(cropsNavBtn);
        
        navSection.add(Box.createRigidArea(new Dimension(0, 10)));

        salesNavBtn = createNavButton("My Sales");
        salesNavBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        salesNavBtn.addActionListener(e -> {
            cardLayout.show(mainContentPanel, "SALES");
            setActiveNavButton(salesNavBtn);
            loadSales();
        });
        navSection.add(salesNavBtn);
        
        navSection.add(Box.createVerticalGlue());
        
        return navSection;
    }
    
    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(BG_COLOR);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel welcomeLabel = new JLabel("Welcome to Crop Management System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        welcomeLabel.setForeground(DARK_GREEN);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 40, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(welcomeLabel, gbc);

        JLabel greetingLabel = new JLabel("Hello, " + currentUser.getName() + "!");
        greetingLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        greetingLabel.setForeground(new Color(100, 100, 100));
        greetingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 50, 0);
        contentPanel.add(greetingLabel, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JPanel quickActionsCard = createFeatureCard(
            "Quick Actions",
            "Get started with your farming activities",
            new String[]{"Add new crops", "Record sales", "Track harvest"},
            PRIMARY_GREEN
        );
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(quickActionsCard, gbc);
        
        JPanel statsCard = createFeatureCard(
            "Dashboard Overview",
            "Monitor your farming progress",
            new String[]{"Crop performance", "Sales analytics", "Growth tracking"},
            new Color(70, 130, 180)
        );
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPanel.add(statsCard, gbc);
        
        JPanel managementCard = createFeatureCard(
            "Farm Management",
            "Organize your farming operations",
            new String[]{"Crop inventory", "Sales history", "Buyer contacts"},
            new Color(255, 140, 0)
        );
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(managementCard, gbc);
        
        JPanel tipsCard = createFeatureCard(
            "Farming Tips",
            "Best practices for better yield",
            new String[]{"Seasonal planting", "Pest control", "Market trends"},
            new Color(40, 167, 69)
        );
        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPanel.add(tipsCard, gbc);
        
        welcomePanel.add(contentPanel, BorderLayout.CENTER);
        
        return welcomePanel;
    }
    
    private JPanel createFeatureCard(String title, String description, String[] features, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setMaximumSize(new Dimension(300, 200));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(color);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLabel);
        
        card.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(new Color(120, 120, 120));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(descLabel);
        
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        for (String feature : features) {
            JLabel featureLabel = new JLabel("• " + feature);
            featureLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            featureLabel.setForeground(new Color(80, 80, 80));
            featureLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(featureLabel);
            card.add(Box.createRigidArea(new Dimension(0, 4)));
        }
        
        return card;
    }
    
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(SIDEBAR_COLOR);
        button.setForeground(Color.DARK_GRAY);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(240, 240, 240), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(220, 50));
        button.setOpaque(true);
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!button.getBackground().equals(PRIMARY_GREEN)) {
                    button.setBackground(new Color(248, 248, 248));
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                        BorderFactory.createEmptyBorder(15, 20, 15, 20)
                    ));
                }
            }
            public void mouseExited(MouseEvent e) {
                if (!button.getBackground().equals(PRIMARY_GREEN)) {
                    button.setBackground(SIDEBAR_COLOR);
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 240, 240), 1),
                        BorderFactory.createEmptyBorder(15, 20, 15, 20)
                    ));
                }
            }
        });
        
        return button;
    }
    
    private void setActiveNavButton(JButton activeButton) {
        welcomeNavBtn.setBackground(SIDEBAR_COLOR);
        welcomeNavBtn.setForeground(Color.DARK_GRAY);
        welcomeNavBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(240, 240, 240), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        cropsNavBtn.setBackground(SIDEBAR_COLOR);
        cropsNavBtn.setForeground(Color.DARK_GRAY);
        cropsNavBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(240, 240, 240), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        salesNavBtn.setBackground(SIDEBAR_COLOR);
        salesNavBtn.setForeground(Color.DARK_GRAY);
        salesNavBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(240, 240, 240), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        activeButton.setBackground(PRIMARY_GREEN);
        activeButton.setForeground(Color.WHITE);
        activeButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_GREEN.darker(), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
    }
    
    private JPanel createCropsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("My Crops Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(DARK_GREEN);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton addCropBtn = createActionButton("Add Crop", PRIMARY_GREEN);
        JButton updateCropBtn = createActionButton("Update", new Color(70, 130, 180));
        JButton logHarvestBtn = createActionButton("Log Harvest", new Color(255, 140, 0));
        JButton deleteCropBtn = createActionButton("Delete", new Color(220, 53, 69));
        JButton refreshBtn = createActionButton("Refresh", new Color(108, 117, 125));
        
        buttonPanel.add(addCropBtn);
        buttonPanel.add(updateCropBtn);
        buttonPanel.add(logHarvestBtn);
        buttonPanel.add(deleteCropBtn);
        buttonPanel.add(refreshBtn);
        
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        String[] cropColumns = {"ID", "Crop Name", "Type", "Planting Date", "Expected Harvest", 
                                "Actual Harvest", "Status", "Qty Planted", "Qty Available"};
        cropTableModel = new DefaultTableModel(cropColumns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        cropTable = new JTable(cropTableModel);
        styleTable(cropTable);
        
        JScrollPane scrollPane = new JScrollPane(cropTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        addCropBtn.addActionListener(e -> openAddCropDialog());
        updateCropBtn.addActionListener(e -> openUpdateCropDialog());
        logHarvestBtn.addActionListener(e -> openLogHarvestDialog());
        deleteCropBtn.addActionListener(e -> deleteCrop());
        refreshBtn.addActionListener(e -> loadCrops());
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSalesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("My Sales");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(DARK_GREEN);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton addSaleBtn = createActionButton("Record Sale", PRIMARY_GREEN);
        JButton deleteSaleBtn = createActionButton("Delete", new Color(220, 53, 69));
        JButton refreshBtn = createActionButton("Refresh", new Color(108, 117, 125));
        
        buttonPanel.add(addSaleBtn);
        buttonPanel.add(deleteSaleBtn);
        buttonPanel.add(refreshBtn);
        
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        String[] salesColumns = {"ID", "Crop Name", "Sale Date", "Quantity", "Price/Unit", 
                                 "Total Amount", "Buyer", "Contact", "Status"};
        salesTableModel = new DefaultTableModel(salesColumns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        salesTable = new JTable(salesTableModel);
        styleTable(salesTable);
        
        JScrollPane scrollPane = new JScrollPane(salesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        addSaleBtn.addActionListener(e -> openAddSaleDialog());
        deleteSaleBtn.addActionListener(e -> deleteSale());
        refreshBtn.addActionListener(e -> loadSales());
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        Color hoverColor = bgColor.darker();
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(hoverColor); }
            public void mouseExited(MouseEvent e) { button.setBackground(bgColor); }
        });
        
        return button;
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 20, 0));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        
        totalCropsLabel = new JLabel("0", SwingConstants.CENTER);
        totalCropsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        totalCropsLabel.setForeground(PRIMARY_GREEN);
        
        harvestedCropsLabel = new JLabel("0", SwingConstants.CENTER);
        harvestedCropsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        harvestedCropsLabel.setForeground(new Color(40, 167, 69));
        
        pendingHarvestLabel = new JLabel("0", SwingConstants.CENTER);
        pendingHarvestLabel.setFont(new Font("Arial", Font.BOLD, 24));
        pendingHarvestLabel.setForeground(new Color(255, 140, 0));
        
        totalEarningsLabel = new JLabel("FRW 0.00", SwingConstants.CENTER);
        totalEarningsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        totalEarningsLabel.setForeground(new Color(0, 123, 255));
        
        panel.add(createStatCard("Total Crops", totalCropsLabel));
        panel.add(createStatCard("Harvested", harvestedCropsLabel));
        panel.add(createStatCard("Pending", pendingHarvestLabel));
        panel.add(createStatCard("Earnings", totalEarningsLabel));
        
        return panel;
    }
    
    private JPanel createStatCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLbl.setForeground(Color.GRAY);
        
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(titleLbl, BorderLayout.SOUTH);
        
        return card;
    }
    
    private void styleTable(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(LIGHT_GREEN);
        table.setSelectionForeground(Color.BLACK);
        table.setBackground(Color.WHITE);
        table.setFillsViewportHeight(true);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setBackground(PRIMARY_GREEN);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setReorderingAllowed(false);
        
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(PRIMARY_GREEN);
                setForeground(Color.WHITE);
                setFont(new Font("Arial", Font.BOLD, 13));
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(255, 255, 255, 100)),
                    BorderFactory.createEmptyBorder(0, 8, 0, 8)
                ));
                return this;
            }
        });
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(Color.WHITE);
        centerRenderer.setFont(new Font("Arial", Font.PLAIN, 12));
        
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        leftRenderer.setBackground(Color.WHITE);
        leftRenderer.setFont(new Font("Arial", Font.PLAIN, 12));
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        rightRenderer.setBackground(Color.WHITE);
        rightRenderer.setFont(new Font("Arial", Font.PLAIN, 12));
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            String columnName = table.getColumnName(i);
            
            if (columnName.equals("Crop Name") || columnName.equals("Type") || 
                columnName.equals("Buyer") || columnName.equals("Contact") || 
                columnName.equals("Status")) {
                table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
            } else if (columnName.equals("Quantity") || columnName.equals("Price/Unit") || 
                       columnName.equals("Total Amount") || columnName.equals("Qty Planted") || 
                       columnName.equals("Qty Available")) {
                table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
            } else {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
    }
    
    private void loadData() {
        updateStats();
    }
    
    private void loadCrops() {
        try {
            cropTableModel.setRowCount(0);
            List<Crop> crops = cropController.getCropsByUserId(currentUser.getUserId());
            for (Crop crop : crops) {
                cropTableModel.addRow(new Object[]{
                    crop.getCropId(),
                    crop.getCropName(),
                    crop.getCropTypeName(),
                    crop.getPlantingDate(),
                    crop.getExpectedHarvestDate() != null ? crop.getExpectedHarvestDate() : "-",
                    crop.getActualHarvestDate() != null ? crop.getActualHarvestDate() : "-",
                    crop.getGrowthStatus(),
                    String.format("%.2f", crop.getQuantityPlanted()),
                    String.format("%.2f", crop.getQuantityHarvested())
                });
            }
            updateStats();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading crops: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadSales() {
        try {
            salesTableModel.setRowCount(0);
            List<Sale> sales = saleController.getSalesByUserId(currentUser.getUserId());
            for (Sale sale : sales) {
                salesTableModel.addRow(new Object[]{
                    sale.getSaleId(),
                    sale.getCropName(),
                    sale.getSaleDate(),
                    String.format("%.2f", sale.getQuantitySold()),
                    String.format("FRW %.2f", sale.getPricePerUnit()),
                    String.format("FRW %.2f", sale.getTotalAmount()),
                    sale.getBuyerName(),
                    sale.getBuyerContact() != null ? sale.getBuyerContact() : "-",
                    sale.getPaymentStatus()
                });
            }
            updateStats();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading sales: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateStats() {
        try {
            List<Crop> crops = cropController.getCropsByUserId(currentUser.getUserId());
            int totalCrops = crops.size();
            int harvested = (int) crops.stream().filter(c -> "HARVESTED".equals(c.getGrowthStatus())).count();
            int pending = totalCrops - harvested;
            double earnings = saleController.getTotalEarningsByUserId(currentUser.getUserId());
            
            totalCropsLabel.setText(String.valueOf(totalCrops));
            harvestedCropsLabel.setText(String.valueOf(harvested));
            pendingHarvestLabel.setText(String.valueOf(pending));
            totalEarningsLabel.setText(String.format("FRW %.2f", earnings));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void openAddCropDialog() {
        AddCropDialog dialog = new AddCropDialog(this, currentUser, cropController, cropTypeController);
        dialog.setVisible(true);
        loadCrops();
    }
    
    private void openUpdateCropDialog() {
        int selectedRow = cropTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a crop to update!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int cropId = (int) cropTableModel.getValueAt(selectedRow, 0);
        UpdateCropDialog dialog = new UpdateCropDialog(this, currentUser, cropController, cropTypeController, cropId);
        dialog.setVisible(true);
        loadCrops();
    }
    
    private void openLogHarvestDialog() {
        int selectedRow = cropTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a crop to log harvest!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int cropId = (int) cropTableModel.getValueAt(selectedRow, 0);
        String status = (String) cropTableModel.getValueAt(selectedRow, 6);
        
        if ("HARVESTED".equals(status)) {
            JOptionPane.showMessageDialog(this, "This crop has already been harvested!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        LogHarvestDialog dialog = new LogHarvestDialog(this, cropController, cropId);
        dialog.setVisible(true);
        loadCrops();
    }
    
    private void deleteCrop() {
        int selectedRow = cropTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a crop to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String cropName = (String) cropTableModel.getValueAt(selectedRow, 1);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Delete crop '" + cropName + "'?\nThis will also delete related sales.", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int cropId = (int) cropTableModel.getValueAt(selectedRow, 0);
            String result = cropController.deleteCrop(cropId);
            
            if ("SUCCESS".equals(result)) {
                JOptionPane.showMessageDialog(this, "Crop deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCrops();
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void openAddSaleDialog() {
        try {
            List<Crop> harvestedCrops = cropController.getHarvestedCropsByUserId(currentUser.getUserId());
            if (harvestedCrops.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No harvested crops available!\nPlease harvest a crop first.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            AddSaleDialog dialog = new AddSaleDialog(this, currentUser, saleController, cropController);
            dialog.setVisible(true);
            loadSales();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteSale() {
        int selectedRow = salesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a sale to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this sale record?", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int saleId = (int) salesTableModel.getValueAt(selectedRow, 0);
            String result = saleController.deleteSale(saleId);
            
            if ("SUCCESS".equals(result)) {
                JOptionPane.showMessageDialog(this, "Sale deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadSales();
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new LoginPage().setVisible(true);
            dispose();
        }
    }
    
    public void refreshData() {
        updateStats();
    }
}