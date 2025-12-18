package view;

import controller.*;
import model.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

public class AdminDashboard extends JFrame {
    
    private User user;
    private UserController userCtrl;
    private CropController cropCtrl;
    private SaleController saleCtrl;
    private CropTypeController cropTypeCtrl;
    
    private JTable farmersTable, cropsTable, salesTable, cropTypesTable;
    private DefaultTableModel farmersModel, cropsModel, salesModel, cropTypesModel;
    private JLabel lblFarmers, lblCrops, lblSales, lblRevenue;
    
    private JButton btnFarmers, btnCrops, btnSales, btnCropTypes, btnReports, btnDashboard;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Colors - Green Theme
    private Color darkGreen = new Color(0, 100, 0);
    private Color primaryGreen = new Color(34, 139, 34);
    private Color lightGreen = new Color(144, 238, 144);
    private Color paleGreen = new Color(240, 255, 240);
    private Color bgColor = new Color(245, 250, 245);
    private Color sidebarColor = new Color(255, 255, 255);
    private DecimalFormat fmt = new DecimalFormat("#,###");
    
    public AdminDashboard(User user) {
        this.user = user;
        this.userCtrl = new UserController();
        this.cropCtrl = new CropController();
        this.saleCtrl = new SaleController();
        this.cropTypeCtrl = new CropTypeController();
        
        initializeLabels();
        buildUI();
        loadAllData();
    }
    
    private void initializeLabels() {
        lblFarmers = new JLabel("0");
        lblFarmers.setFont(new Font("Arial", Font.BOLD, 26));
        lblFarmers.setForeground(new Color(0, 123, 255));
        
        lblCrops = new JLabel("0");
        lblCrops.setFont(new Font("Arial", Font.BOLD, 26));
        lblCrops.setForeground(primaryGreen);
        
        lblSales = new JLabel("0");
        lblSales.setFont(new Font("Arial", Font.BOLD, 26));
        lblSales.setForeground(new Color(255, 140, 0));
        
        lblRevenue = new JLabel("0 RWF");
        lblRevenue.setFont(new Font("Arial", Font.BOLD, 26));
        lblRevenue.setForeground(new Color(40, 167, 69));
    }
    
    private void buildUI() {
        setTitle("Crop Management System - Admin Dashboard");
        setSize(1350, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1100, 700));
        
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(bgColor);
        main.add(headerPanel(), BorderLayout.NORTH);
        
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebarPanel(), contentPanel());
        split.setDividerLocation(260);
        split.setDividerSize(1);
        split.setBorder(null);
        split.setEnabled(false);
        main.add(split, BorderLayout.CENTER);
        main.add(statsPanel(), BorderLayout.SOUTH);
        
        add(main);
    }
    
    private JPanel headerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(darkGreen);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        panel.setPreferredSize(new Dimension(0, 70));
        
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);
        
      
        JLabel title = new JLabel("Crop Management System - Admin Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        
        left.add(title);
        
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        right.setOpaque(false);
        
        JLabel userLbl = new JLabel("Welcome, " + user.getName());
        userLbl.setFont(new Font("Arial", Font.BOLD, 14));
        userLbl.setForeground(Color.WHITE);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 12));
        logoutBtn.setBackground(Color.WHITE);
        logoutBtn.setForeground(darkGreen);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> logout());
        
        right.add(userLbl);
        right.add(Box.createHorizontalStrut(10));
        right.add(logoutBtn);
        
        panel.add(left, BorderLayout.WEST);
        panel.add(right, BorderLayout.EAST);
        return panel;
    }
    
    private JPanel sidebarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(sidebarColor);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 230, 220)));
        panel.setPreferredSize(new Dimension(260, 0));
        
        panel.add(userIconPanel(), BorderLayout.NORTH);
        panel.add(navPanel(), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel userIconPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(paleGreen);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 230, 200)),
            BorderFactory.createEmptyBorder(30, 20, 30, 20)
        ));
        
        JPanel iconWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        iconWrapper.setBackground(paleGreen);
        iconWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel userIcon = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                int size = Math.min(w, h) - 8;
                int x = (w - size) / 2;
                int y = (h - size) / 2;
                
                g2.setColor(new Color(0, 80, 0, 40));
                g2.fillOval(x + 4, y + 4, size, size);
                
                GradientPaint gp = new GradientPaint(x, y, new Color(50, 160, 50), x + size, y + size, new Color(0, 100, 0));
                g2.setPaint(gp);
                g2.fillOval(x, y, size, size);
                
                GradientPaint highlight = new GradientPaint(x, y, new Color(255, 255, 255, 90), x + size/2, y + size/2, new Color(255, 255, 255, 0));
                g2.setPaint(highlight);
                g2.fillOval(x + 8, y + 8, size - 35, size - 35);
                
                g2.setColor(new Color(0, 80, 0));
                g2.setStroke(new BasicStroke(3));
                g2.drawOval(x, y, size, size);
                
                String initial = user.getName().substring(0, 1).toUpperCase();
                g2.setFont(new Font("Arial", Font.BOLD, 36));
                FontMetrics fm = g2.getFontMetrics();
                int textX = (w - fm.stringWidth(initial)) / 2;
                int textY = (h - fm.getHeight()) / 2 + fm.getAscent();
                
                g2.setColor(new Color(0, 0, 0, 40));
                g2.drawString(initial, textX + 2, textY + 2);
                
                g2.setColor(Color.WHITE);
                g2.drawString(initial, textX, textY);
                
                g2.dispose();
            }
        };
        userIcon.setPreferredSize(new Dimension(100, 100));
        iconWrapper.add(userIcon);
        
        JLabel nameLbl = new JLabel(user.getName());
        nameLbl.setFont(new Font("Arial", Font.BOLD, 18));
        nameLbl.setForeground(darkGreen);
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel badgeWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        badgeWrapper.setBackground(paleGreen);
        badgeWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel roleLbl = new JLabel("ADMINISTRATOR");
        roleLbl.setFont(new Font("Arial", Font.BOLD, 10));
        roleLbl.setForeground(Color.WHITE);
        roleLbl.setBackground(primaryGreen);
        roleLbl.setOpaque(true);
        roleLbl.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        badgeWrapper.add(roleLbl);
        
        panel.add(iconWrapper);
        panel.add(Box.createVerticalStrut(15));
        panel.add(nameLbl);
        panel.add(Box.createVerticalStrut(10));
        panel.add(badgeWrapper);
        
        return panel;
    }

    private JPanel navPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(sidebarColor);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 15, 20, 15));
        
        // Navigation buttons - Added Dashboard button
        btnDashboard = navBtn("Dashboard");
        btnFarmers = navBtn("Farmers Management");
        btnCrops = navBtn("All Crops");
        btnSales = navBtn("Sales Transactions");
        btnCropTypes = navBtn("Crop Types");
        btnReports = navBtn("Reports & Analytics");
        
        btnDashboard.addActionListener(e -> showPanel("DASHBOARD", btnDashboard));
        btnFarmers.addActionListener(e -> showPanel("FARMERS", btnFarmers));
        btnCrops.addActionListener(e -> showPanel("CROPS", btnCrops));
        btnSales.addActionListener(e -> showPanel("SALES", btnSales));
        btnCropTypes.addActionListener(e -> showPanel("CROP_TYPES", btnCropTypes));
        btnReports.addActionListener(e -> showPanel("REPORTS", btnReports));
        
        panel.add(btnDashboard);
        panel.add(Box.createVerticalStrut(6));
        panel.add(btnFarmers);
        panel.add(Box.createVerticalStrut(6));
        panel.add(btnCrops);
        panel.add(Box.createVerticalStrut(6));
        panel.add(btnSales);
        panel.add(Box.createVerticalStrut(6));
        panel.add(btnCropTypes);
        panel.add(Box.createVerticalStrut(6));
        panel.add(btnReports);
        
        panel.add(Box.createVerticalGlue());
        
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBackground(paleGreen);
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 230, 200)),
            BorderFactory.createEmptyBorder(15, 10, 10, 10)
        ));
        footerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        footerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        footerPanel.add(Box.createVerticalGlue());
        
        panel.add(footerPanel);
        
        return panel;
    }
    
    private JButton navBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setBackground(sidebarColor);
        btn.setForeground(new Color(60, 80, 60));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder(14, 15, 14, 15));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!btn.getBackground().equals(primaryGreen)) {
                    btn.setBackground(paleGreen);
                    btn.setForeground(darkGreen);
                }
            }
            public void mouseExited(MouseEvent e) {
                if (!btn.getBackground().equals(primaryGreen)) {
                    btn.setBackground(sidebarColor);
                    btn.setForeground(new Color(60, 80, 60));
                }
            }
        });
        return btn;
    }
    
    private void setActiveBtn(JButton active) {
        JButton[] btns = {btnDashboard, btnFarmers, btnCrops, btnSales, btnCropTypes, btnReports};
        for (JButton b : btns) {
            b.setBackground(sidebarColor);
            b.setForeground(new Color(60, 80, 60));
            b.setFont(new Font("Arial", Font.PLAIN, 13));
        }
        active.setBackground(primaryGreen);
        active.setForeground(Color.WHITE);
        active.setFont(new Font("Arial", Font.BOLD, 13));
    }
    
    private void showPanel(String name, JButton btn) {
        cardLayout.show(contentPanel, name);
        setActiveBtn(btn);
        switch (name) {
            case "FARMERS": loadFarmers(); break;
            case "CROPS": loadAllCrops(); break;
            case "SALES": loadAllSales(); break;
            case "CROP_TYPES": loadCropTypes(); break;
        }
    }
    
    private JPanel contentPanel() {
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        contentPanel.setBackground(Color.WHITE);
        
        contentPanel.add(dashboardPanel(), "DASHBOARD");
        contentPanel.add(farmersPanel(), "FARMERS");
        contentPanel.add(cropsPanel(), "CROPS");
        contentPanel.add(salesPanel(), "SALES");
        contentPanel.add(cropTypesPanel(), "CROP_TYPES");
        contentPanel.add(reportsPanel(), "REPORTS");
        
        cardLayout.show(contentPanel, "DASHBOARD");
        setActiveBtn(btnDashboard);
        
        return contentPanel;
    }
    
    private JPanel dashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        
        JLabel welcomeLabel = new JLabel("Welcome to Admin Dashboard");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        welcomeLabel.setForeground(darkGreen);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subTitle = new JLabel("Crop Management System - Administrative Control Center");
        subTitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subTitle.setForeground(new Color(100, 120, 100));
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
       
        JSeparator separator = new JSeparator();
        separator.setForeground(lightGreen);
        separator.setMaximumSize(new Dimension(600, 2));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel capabilitiesPanel = new JPanel();
        capabilitiesPanel.setLayout(new BoxLayout(capabilitiesPanel, BoxLayout.Y_AXIS));
        capabilitiesPanel.setBackground(Color.WHITE);
        capabilitiesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        capabilitiesPanel.setMaximumSize(new Dimension(800, Integer.MAX_VALUE));
        
        JLabel capabilitiesTitle = new JLabel("Administrator Capabilities");
        capabilitiesTitle.setFont(new Font("Arial", Font.BOLD, 24));
        capabilitiesTitle.setForeground(primaryGreen);
        capabilitiesTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gridPanel.setMaximumSize(new Dimension(800, 400));

        gridPanel.add(createCapabilityCard(
            "👨‍🌾 Farmers Management", 
            "Manage all farmer accounts in the system",
            new String[]{
                "• Add new farmer accounts",
                "• Update farmer information",
                "• Lock/unlock farmer accounts",
                "• Delete farmer accounts",
                "• View all farmer details"
            }
        ));
        
        gridPanel.add(createCapabilityCard(
            "🌱 Crop Types", 
            "Manage crop categories and types",
            new String[]{
                "• Add new crop types",
                "• Update crop type information",
                "• Delete crop types",
                "• Manage crop descriptions",
                "• Organize crop categories"
            }
        ));
        
        gridPanel.add(createCapabilityCard(
            "📊 All Crops Overview", 
            "Monitor all crops in the system",
            new String[]{
                "• View all planted crops",
                "• Track crop growth status",
                "• Monitor planting/harvest dates",
                "• Delete crop records",
                "• Overview of crop quantities"
            }
        ));
        
        gridPanel.add(createCapabilityCard(
            "💰 Sales Transactions", 
            "Manage and monitor all sales",
            new String[]{
                "• View all sales records",
                "• Monitor revenue streams",
                "• Track payment status",
                "• Delete sales records",
                "• Sales analytics overview"
            }
        ));
        
        gridPanel.add(createCapabilityCard(
            "📈 Reports & Analytics", 
            "Generate comprehensive reports",
            new String[]{
                "• Crop status reports",
                "• Sales revenue reports",
                "• Farmer activity reports",
                "• System summary reports",
                "• Export-ready data"
            }
        ));
        
        gridPanel.add(createCapabilityCard(
            "📊 System Statistics", 
            "Real-time system overview",
            new String[]{
                "• Total farmers count",
                "• Active crops monitoring",
                "• Sales transactions tracking",
                "• Revenue calculations",
                "• System health metrics"
            }
        ));

        JPanel quickActionsPanel = new JPanel();
        quickActionsPanel.setLayout(new BoxLayout(quickActionsPanel, BoxLayout.Y_AXIS));
        quickActionsPanel.setBackground(Color.WHITE);
        quickActionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        quickActionsPanel.setMaximumSize(new Dimension(800, 120));
        
        JLabel quickActionsTitle = new JLabel("Quick Actions");
        quickActionsTitle.setFont(new Font("Arial", Font.BOLD, 20));
        quickActionsTitle.setForeground(darkGreen);
        quickActionsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel actionButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        actionButtons.setBackground(Color.WHITE);
        actionButtons.setMaximumSize(new Dimension(800, 60));
        
        JButton addFarmerBtn = createQuickActionButton("Add New Farmer", primaryGreen);
        JButton viewReportsBtn = createQuickActionButton("Generate Reports", new Color(70, 130, 180));
        JButton manageCropsBtn = createQuickActionButton("Manage Crop Types", new Color(255, 140, 0));
        
        addFarmerBtn.addActionListener(e -> showPanel("FARMERS", btnFarmers));
        viewReportsBtn.addActionListener(e -> showPanel("REPORTS", btnReports));
        manageCropsBtn.addActionListener(e -> showPanel("CROP_TYPES", btnCropTypes));
        
        actionButtons.add(addFarmerBtn);
        actionButtons.add(viewReportsBtn);
        actionButtons.add(manageCropsBtn);
        
        quickActionsPanel.add(quickActionsTitle);
        quickActionsPanel.add(Box.createVerticalStrut(10));
        quickActionsPanel.add(actionButtons);
        
        content.add(Box.createVerticalStrut(20));
        content.add(welcomeLabel);
        content.add(Box.createVerticalStrut(5));
        content.add(subTitle);
        content.add(Box.createVerticalStrut(30));
        content.add(separator);
        content.add(Box.createVerticalStrut(30));
        content.add(capabilitiesTitle);
        content.add(Box.createVerticalStrut(20));
        content.add(gridPanel);
        content.add(Box.createVerticalStrut(30));
        content.add(quickActionsPanel);
        
        panel.add(Box.createVerticalStrut(20), BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCapabilityCard(String title, String description, String[] features) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(paleGreen);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(lightGreen, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(250, 220));
        
        
        JLabel titleLabel = new JLabel("<html><div style='text-align: center;'>" + title + "</div></html>");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(darkGreen);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
      
        JLabel descLabel = new JLabel("<html><div style='text-align: center; font-size: 11px; color: #666;'>" + description + "</div></html>");
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
       
        JPanel featuresPanel = new JPanel();
        featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));
        featuresPanel.setBackground(paleGreen);
        
        for (String feature : features) {
            JLabel featureLabel = new JLabel("<html><div style='font-size: 10px; margin: 2px 0;'>" + feature + "</div></html>");
            featureLabel.setForeground(new Color(80, 100, 80));
            featuresPanel.add(featureLabel);
        }
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(paleGreen);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(descLabel, BorderLayout.CENTER);
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(featuresPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JButton createQuickActionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(color); }
        });
        return btn;
    }

    private JPanel farmersPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Manage Farmers");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(darkGreen);
        
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setBackground(Color.WHITE);
        btns.add(actionBtn("Add Farmer", primaryGreen, e -> addFarmer()));
        btns.add(actionBtn("Update", new Color(70, 130, 180), e -> updateFarmer()));
        btns.add(actionBtn("Delete", new Color(220, 53, 69), e -> deleteFarmer()));
        btns.add(actionBtn("Unlock", new Color(255, 140, 0), e -> unlockFarmer()));
        btns.add(actionBtn("Refresh", new Color(108, 117, 125), e -> loadFarmers()));
        
        top.add(title, BorderLayout.WEST);
        top.add(btns, BorderLayout.EAST);
        
        String[] cols = {"ID", "Name", "Farm Location", "Phone", "Email", "Role", "Status"};
        farmersModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        farmersTable = new JTable(farmersModel);
        styleTable(farmersTable);
        
        JScrollPane scroll = new JScrollPane(farmersTable);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 220)));
        
        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel cropsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("All Crops in System");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(darkGreen);
        
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setBackground(Color.WHITE);
        btns.add(actionBtn("Delete", new Color(220, 53, 69), e -> deleteCrop()));
        btns.add(actionBtn("Refresh", new Color(108, 117, 125), e -> loadAllCrops()));
        
        top.add(title, BorderLayout.WEST);
        top.add(btns, BorderLayout.EAST);
        
        String[] cols = {"ID", "Farmer", "Crop Name", "Type", "Planting Date", "Harvest Date", "Status", "Qty Planted", "Qty Available"};
        cropsModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        cropsTable = new JTable(cropsModel);
        styleTable(cropsTable);
        
        JScrollPane scroll = new JScrollPane(cropsTable);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 220)));
        
        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel salesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("All Sales Transactions");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(darkGreen);
        
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setBackground(Color.WHITE);
        btns.add(actionBtn("Delete", new Color(220, 53, 69), e -> deleteSale()));
        btns.add(actionBtn("Refresh", new Color(108, 117, 125), e -> loadAllSales()));
        
        top.add(title, BorderLayout.WEST);
        top.add(btns, BorderLayout.EAST);
        
        String[] cols = {"ID", "Farmer", "Crop", "Sale Date", "Qty (kg)", "Price/kg (RWF)", "Total (RWF)", "Buyer", "Status"};
        salesModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        salesTable = new JTable(salesModel);
        styleTable(salesTable);
        
        JScrollPane scroll = new JScrollPane(salesTable);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 220)));
        
        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel cropTypesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Manage Crop Types");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(darkGreen);
        
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setBackground(Color.WHITE);
        btns.add(actionBtn("Add Type", primaryGreen, e -> addCropType()));
        btns.add(actionBtn("Update", new Color(70, 130, 180), e -> updateCropType()));
        btns.add(actionBtn("Delete", new Color(220, 53, 69), e -> deleteCropType()));
        btns.add(actionBtn("Refresh", new Color(108, 117, 125), e -> loadCropTypes()));
        
        top.add(title, BorderLayout.WEST);
        top.add(btns, BorderLayout.EAST);
        
        String[] cols = {"ID", "Crop Name", "Description", "Created At"};
        cropTypesModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        cropTypesTable = new JTable(cropTypesModel);
        styleTable(cropTypesTable);
        
        JScrollPane scroll = new JScrollPane(cropTypesTable);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 220)));
        
        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel reportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Generate Reports");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(darkGreen);
        
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btns.setBackground(Color.WHITE);
        
        JTextArea reportArea = new JTextArea();
        reportArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        reportArea.setEditable(false);
        reportArea.setBackground(paleGreen);
        reportArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        reportArea.setText(getWelcomeText());
        
        btns.add(actionBtn("Crop Report", primaryGreen, e -> cropReport(reportArea)));
        btns.add(actionBtn("Sales Report", new Color(70, 130, 180), e -> salesReport(reportArea)));
        btns.add(actionBtn("Farmer Report", new Color(255, 140, 0), e -> farmerReport(reportArea)));
        btns.add(actionBtn("Summary", new Color(156, 39, 176), e -> summaryReport(reportArea)));
        btns.add(actionBtn("Clear", new Color(108, 117, 125), e -> reportArea.setText(getWelcomeText())));
        
        JPanel topWrapper = new JPanel();
        topWrapper.setLayout(new BoxLayout(topWrapper, BoxLayout.Y_AXIS));
        topWrapper.setBackground(Color.WHITE);
        topWrapper.add(title);
        topWrapper.add(Box.createVerticalStrut(15));
        topWrapper.add(btns);
        
        JScrollPane scroll = new JScrollPane(reportArea);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 230, 200)));
        
        panel.add(topWrapper, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
    
    private String getWelcomeText() {
        return "\n" +
               "           CROP MANAGEMENT SYSTEM - REPORT GENERATOR\n" +
               "    =============================================================\n\n" +
               "    Click one of the buttons above to generate a report:\n\n" +
               "       Crop Report     - View all crops with status and details\n" +
               "       Sales Report    - View all sales transactions and revenue\n" +
               "       Farmer Report   - View all registered farmers\n" +
               "       Summary         - View overall system summary\n";
    }
    
    // ==================== STATS PANEL ====================
    private JPanel statsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(paleGreen);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        panel.add(statCard("Total Farmers", lblFarmers, new Color(0, 123, 255)));
        panel.add(statCard("Total Crops", lblCrops, primaryGreen));
        panel.add(statCard("Total Sales", lblSales, new Color(255, 140, 0)));
        panel.add(statCard("Total Revenue", lblRevenue, new Color(40, 167, 69)));
        return panel;
    }
    
    private JPanel statCard(String title, JLabel valueLbl, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 230, 200)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Left accent bar
        JPanel accentBar = new JPanel();
        accentBar.setBackground(accentColor);
        accentBar.setPreferredSize(new Dimension(4, 0));
        
        JPanel content = new JPanel(new BorderLayout(5, 5));
        content.setBackground(Color.WHITE);
        
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLbl.setForeground(new Color(100, 120, 100));
        
        content.add(titleLbl, BorderLayout.NORTH);
        content.add(valueLbl, BorderLayout.CENTER);
        
        card.add(accentBar, BorderLayout.WEST);
        card.add(content, BorderLayout.CENTER);
        return card;
    }
    
    
    private void styleTable(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(38);
        table.setGridColor(new Color(230, 240, 230));
        table.setSelectionBackground(lightGreen);
        table.setSelectionForeground(Color.BLACK);
        table.setFillsViewportHeight(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int row, int col) {
                JLabel lbl = new JLabel(val.toString());
                lbl.setOpaque(true);
                lbl.setBackground(darkGreen);
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("Arial", Font.BOLD, 12));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0, 80, 0)));
                return lbl;
            }
        });
        
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setHorizontalAlignment(CENTER);
                if (!sel) setBackground(row % 2 == 0 ? Color.WHITE : paleGreen);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        });
    }
    
    private JButton actionBtn(String text, Color bg, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        btn.addActionListener(action);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }
    
    
    private void loadAllData() {
        loadFarmers();
        loadAllCrops();
        loadAllSales();
        loadCropTypes();
        updateStats();
    }
    
    private void loadFarmers() {
        try {
            farmersModel.setRowCount(0);
            for (User f : userCtrl.getAllFarmers()) {
                farmersModel.addRow(new Object[]{
                    f.getUserId(), f.getName(),
                    f.getFarmLocation() != null ? f.getFarmLocation() : "-",
                    f.getPhone() != null ? f.getPhone() : "-",
                    f.getEmail(), f.getRole(),
                    f.isLocked() ? "Locked" : "Active"
                });
            }
            updateStats();
        } catch (SQLException e) { showError("Error: " + e.getMessage()); }
    }
    
    private void loadAllCrops() {
        try {
            cropsModel.setRowCount(0);
            for (Crop c : cropCtrl.getAllCrops()) {
                cropsModel.addRow(new Object[]{
                    c.getCropId(), c.getFarmerName(), c.getCropName(), c.getCropTypeName(),
                    c.getPlantingDate(),
                    c.getActualHarvestDate() != null ? c.getActualHarvestDate() : "-",
                    c.getGrowthStatus(),
                    String.format("%.2f", c.getQuantityPlanted()),
                    String.format("%.2f", c.getQuantityHarvested())
                });
            }
            updateStats();
        } catch (SQLException e) { showError("Error: " + e.getMessage()); }
    }
    
    private void loadAllSales() {
        try {
            salesModel.setRowCount(0);
            for (Sale s : saleCtrl.getAllSales()) {
                salesModel.addRow(new Object[]{
                    s.getSaleId(), s.getFarmerName(), s.getCropName(), s.getSaleDate(),
                    String.format("%.2f", s.getQuantitySold()),
                    fmt.format(s.getPricePerUnit()) + " RWF",
                    fmt.format(s.getTotalAmount()) + " RWF",
                    s.getBuyerName(), s.getPaymentStatus()
                });
            }
            updateStats();
        } catch (SQLException e) { showError("Error: " + e.getMessage()); }
    }
    
    private void loadCropTypes() {
        try {
            cropTypesModel.setRowCount(0);
            for (CropType t : cropTypeCtrl.getAllCropTypes()) {
                cropTypesModel.addRow(new Object[]{
                    t.getCropTypeId(), t.getCropName(),
                    t.getDescription() != null ? t.getDescription() : "-",
                    t.getCreatedAt()
                });
            }
        } catch (SQLException e) { showError("Error: " + e.getMessage()); }
    }
    
    private void updateStats() {
        if (lblFarmers == null || lblCrops == null || lblSales == null || lblRevenue == null) return;
        try {
            lblFarmers.setText(String.valueOf(userCtrl.getAllFarmers().size()));
            lblCrops.setText(String.valueOf(cropCtrl.getAllCrops().size()));
            lblSales.setText(String.valueOf(saleCtrl.getAllSales().size()));
            lblRevenue.setText(fmt.format(saleCtrl.getTotalSalesAmount()) + " RWF");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void addFarmer() {
        JDialog dialog = createDialog("Add New Farmer", 420, 500);
        JPanel panel = dialogPanel("Add New Farmer");
        
        JTextField nameField = new JTextField();
        JTextField farmField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passField = new JPasswordField();
        
        panel.add(field("Full Name *", nameField));
        panel.add(Box.createVerticalStrut(12));
        panel.add(field("Farm Location", farmField));
        panel.add(Box.createVerticalStrut(12));
        panel.add(field("Phone Number", phoneField));
        panel.add(Box.createVerticalStrut(12));
        panel.add(field("Email *", emailField));
        panel.add(Box.createVerticalStrut(12));
        panel.add(field("Password * (min 6 chars)", passField));
        panel.add(Box.createVerticalStrut(25));
        panel.add(dialogBtns(dialog, "Save Farmer", () -> {
            String result = userCtrl.registerUser(
                nameField.getText().trim(), farmField.getText().trim(),
                phoneField.getText().trim(), emailField.getText().trim(),
                new String(passField.getPassword()), new String(passField.getPassword())
            );
            if ("SUCCESS".equals(result)) { showSuccess("Farmer added!"); dialog.dispose(); loadFarmers(); }
            else showError(result);
        }));
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void updateFarmer() {
        int row = farmersTable.getSelectedRow();
        if (row == -1) { showWarning("Select a farmer to update"); return; }
        
        try {
            User f = userCtrl.getUserById((int) farmersModel.getValueAt(row, 0));
            if (f == null) { showError("Farmer not found"); return; }
            
            JDialog dialog = createDialog("Update Farmer", 420, 420);
            JPanel panel = dialogPanel("Update Farmer");
            
            JTextField nameField = new JTextField(f.getName());
            JTextField farmField = new JTextField(f.getFarmLocation() != null ? f.getFarmLocation() : "");
            JTextField phoneField = new JTextField(f.getPhone() != null ? f.getPhone() : "");
            JTextField emailField = new JTextField(f.getEmail());
            
            panel.add(field("Full Name *", nameField));
            panel.add(Box.createVerticalStrut(12));
            panel.add(field("Farm Location", farmField));
            panel.add(Box.createVerticalStrut(12));
            panel.add(field("Phone Number", phoneField));
            panel.add(Box.createVerticalStrut(12));
            panel.add(field("Email *", emailField));
            panel.add(Box.createVerticalStrut(25));
            panel.add(dialogBtns(dialog, "Update", () -> {
                f.setName(nameField.getText().trim());
                f.setFarmLocation(farmField.getText().trim());
                f.setPhone(phoneField.getText().trim());
                f.setEmail(emailField.getText().trim());
                String result = userCtrl.updateUser(f);
                if ("SUCCESS".equals(result)) { showSuccess("Farmer updated!"); dialog.dispose(); loadFarmers(); }
                else showError(result);
            }));
        
            dialog.add(panel);
            dialog.setVisible(true);
        } catch (SQLException e) { showError(e.getMessage()); }
    }
    
    private void deleteFarmer() {
        int row = farmersTable.getSelectedRow();
        if (row == -1) { showWarning("Select a farmer to delete"); return; }
        if (confirm("Delete farmer '" + farmersModel.getValueAt(row, 1) + "'?\nThis deletes all their data!")) {
            String result = userCtrl.deleteUser((int) farmersModel.getValueAt(row, 0));
            if ("SUCCESS".equals(result)) { showSuccess("Farmer deleted!"); loadAllData(); }
            else showError(result);
        }
    }
    
    private void unlockFarmer() {
        int row = farmersTable.getSelectedRow();
        if (row == -1) { showWarning("Select a farmer to unlock"); return; }
        if (!"Locked".equals(farmersModel.getValueAt(row, 6))) { showWarning("Account not locked"); return; }
        if (confirm("Unlock account for '" + farmersModel.getValueAt(row, 1) + "'?")) {
            String result = userCtrl.unlockAccount((int) farmersModel.getValueAt(row, 0));
            if ("SUCCESS".equals(result)) { showSuccess("Account unlocked!"); loadFarmers(); }
            else showError(result);
        }
    }

    private void addCropType() {
        JDialog dialog = createDialog("Add Crop Type", 420, 300);
        JPanel panel = dialogPanel("Add New Crop Type");
        
        JTextField nameField = new JTextField();
        JTextArea descArea = new JTextArea(3, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        
        panel.add(field("Crop Type Name *", nameField));
        panel.add(Box.createVerticalStrut(12));
        panel.add(textAreaField("Description", descArea));
        panel.add(Box.createVerticalStrut(20));
        panel.add(dialogBtns(dialog, "Save", () -> {
            String result = cropTypeCtrl.addCropType(nameField.getText().trim(), descArea.getText().trim());
            if ("SUCCESS".equals(result)) { showSuccess("Crop type added!"); dialog.dispose(); loadCropTypes(); }
            else showError(result);
        }));
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void updateCropType() {
        int row = cropTypesTable.getSelectedRow();
        if (row == -1) { showWarning("Select a crop type to update"); return; }
        
        int id = (int) cropTypesModel.getValueAt(row, 0);
        String name = (String) cropTypesModel.getValueAt(row, 1);
        String desc = cropTypesModel.getValueAt(row, 2).toString();
        if ("-".equals(desc)) desc = "";
        
        JDialog dialog = createDialog("Update Crop Type", 420, 300);
        JPanel panel = dialogPanel("Update Crop Type");
        
        JTextField nameField = new JTextField(name);
        JTextArea descArea = new JTextArea(desc, 3, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        
        panel.add(field("Crop Type Name *", nameField));
        panel.add(Box.createVerticalStrut(12));
        panel.add(textAreaField("Description", descArea));
        panel.add(Box.createVerticalStrut(20));
        
        final int typeId = id;
        panel.add(dialogBtns(dialog, "Update", () -> {
            CropType ct = new CropType();
            ct.setCropTypeId(typeId);
            ct.setCropName(nameField.getText().trim());
            ct.setDescription(descArea.getText().trim());
            String result = cropTypeCtrl.updateCropType(ct);
            if ("SUCCESS".equals(result)) { showSuccess("Crop type updated!"); dialog.dispose(); loadCropTypes(); }
            else showError(result);
        }));
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void deleteCropType() {
        int row = cropTypesTable.getSelectedRow();
        if (row == -1) { showWarning("Select a crop type to delete"); return; }
        if (confirm("Delete crop type '" + cropTypesModel.getValueAt(row, 1) + "'?")) {
            String result = cropTypeCtrl.deleteCropType((int) cropTypesModel.getValueAt(row, 0));
            if ("SUCCESS".equals(result)) { showSuccess("Crop type deleted!"); loadCropTypes(); }
            else showError(result);
        }
    }
    
    private void deleteCrop() {
        int row = cropsTable.getSelectedRow();
        if (row == -1) { showWarning("Select a crop to delete"); return; }
        if (confirm("Delete crop '" + cropsModel.getValueAt(row, 2) + "'?\nThis deletes related sales!")) {
            String result = cropCtrl.deleteCrop((int) cropsModel.getValueAt(row, 0));
            if ("SUCCESS".equals(result)) { showSuccess("Crop deleted!"); loadAllData(); }
            else showError(result);
        }
    }
    
    private void deleteSale() {
        int row = salesTable.getSelectedRow();
        if (row == -1) { showWarning("Select a sale to delete"); return; }
        if (confirm("Delete this sale record?")) {
            String result = saleCtrl.deleteSale((int) salesModel.getValueAt(row, 0));
            if ("SUCCESS".equals(result)) { showSuccess("Sale deleted!"); loadAllData(); }
            else showError(result);
        }
    }
    
    private void cropReport(JTextArea area) {
        try {
            List<Crop> crops = cropCtrl.getAllCrops();
            StringBuilder sb = new StringBuilder();
            sb.append("\n           CROP REPORT\n");
            sb.append("           Generated: ").append(java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
            sb.append("    =============================================================\n\n");
            sb.append(String.format("    Total Crops: %d\n\n", crops.size()));
            
            long harvested = crops.stream().filter(c -> "HARVESTED".equals(c.getGrowthStatus())).count();
            sb.append("    STATUS SUMMARY\n");
            sb.append("    -------------------------------------------------------------\n");
            sb.append(String.format("    Harvested: %d | Pending: %d\n\n", harvested, crops.size() - harvested));
            
            sb.append(String.format("    %-15s %-15s %-12s %-15s\n", "Farmer", "Crop", "Type", "Status"));
            sb.append("    -------------------------------------------------------------\n");
            for (Crop c : crops)
                sb.append(String.format("    %-15s %-15s %-12s %-15s\n",
                    trunc(c.getFarmerName(), 15), trunc(c.getCropName(), 15),
                    trunc(c.getCropTypeName(), 12), c.getGrowthStatus()));
            
            area.setText(sb.toString());
            area.setCaretPosition(0);
        } catch (SQLException e) { area.setText("Error: " + e.getMessage()); }
    }
    
    private void salesReport(JTextArea area) {
        try {
            List<Sale> sales = saleCtrl.getAllSales();
            double total = saleCtrl.getTotalSalesAmount();
            StringBuilder sb = new StringBuilder();
            sb.append("\n           SALES REPORT\n");
            sb.append("           Generated: ").append(java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
            sb.append("    =============================================================\n\n");
            sb.append(String.format("    Total Sales: %d | Revenue: %s RWF\n\n", sales.size(), fmt.format(total)));
            
            sb.append(String.format("    %-12s %-12s %-12s %-15s %-10s\n", "Date", "Farmer", "Crop", "Total (RWF)", "Status"));
            sb.append("    -------------------------------------------------------------\n");
            for (Sale s : sales)
                sb.append(String.format("    %-12s %-12s %-12s %-15s %-10s\n",
                    s.getSaleDate(), trunc(s.getFarmerName(), 12), trunc(s.getCropName(), 12),
                    fmt.format(s.getTotalAmount()), s.getPaymentStatus()));
            
            area.setText(sb.toString());
            area.setCaretPosition(0);
        } catch (SQLException e) { area.setText("Error: " + e.getMessage()); }
    }
    
    private void farmerReport(JTextArea area) {
        try {
            List<User> farmers = userCtrl.getAllFarmers();
            StringBuilder sb = new StringBuilder();
            sb.append("\n           FARMER REPORT\n");
            sb.append("           Generated: ").append(java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
            sb.append("    =============================================================\n\n");
            sb.append(String.format("    Total Farmers: %d\n\n", farmers.size()));
            
            sb.append(String.format("    %-20s %-25s %-15s %-10s\n", "Name", "Location", "Phone", "Status"));
            sb.append("    -------------------------------------------------------------\n");
            for (User f : farmers)
                sb.append(String.format("    %-20s %-25s %-15s %-10s\n",
                    trunc(f.getName(), 20),
                    trunc(f.getFarmLocation() != null ? f.getFarmLocation() : "N/A", 25),
                    trunc(f.getPhone() != null ? f.getPhone() : "N/A", 15),
                    f.isLocked() ? "Locked" : "Active"));
            
            area.setText(sb.toString());
            area.setCaretPosition(0);
        } catch (SQLException e) { area.setText("Error: " + e.getMessage()); }
    }
    
    private void summaryReport(JTextArea area) {
        try {
            List<User> farmers = userCtrl.getAllFarmers();
            List<Crop> crops = cropCtrl.getAllCrops();
            List<Sale> sales = saleCtrl.getAllSales();
            double revenue = saleCtrl.getTotalSalesAmount();
            
            StringBuilder sb = new StringBuilder();
            sb.append("\n           SYSTEM SUMMARY\n");
            sb.append("           Generated: ").append(java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
            sb.append("    =============================================================\n\n");
            sb.append("    OVERVIEW\n");
            sb.append("    -------------------------------------------------------------\n");
            sb.append(String.format("    Total Farmers:    %d\n", farmers.size()));
            sb.append(String.format("    Total Crops:      %d\n", crops.size()));
            sb.append(String.format("    Total Sales:      %d\n", sales.size()));
            sb.append(String.format("    Total Revenue:    %s RWF\n", fmt.format(revenue)));
            
            area.setText(sb.toString());
            area.setCaretPosition(0);
        } catch (SQLException e) { area.setText("Error: " + e.getMessage()); }
    }
    
    private String trunc(String s, int len) {
        if (s == null) return "";
        return s.length() > len ? s.substring(0, len - 2) + ".." : s;
    }
    
    private JDialog createDialog(String title, int w, int h) {
        JDialog d = new JDialog(this, title, true);
        d.setSize(w, h);
        d.setLocationRelativeTo(this);
        d.setResizable(false);
        return d;
    }
    
    private JPanel dialogPanel(String title) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        p.setBackground(Color.WHITE);
        
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Arial", Font.BOLD, 18));
        lbl.setForeground(darkGreen);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lbl);
        p.add(Box.createVerticalStrut(20));
        return p;
    }
    
    private JPanel field(String label, JComponent comp) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setForeground(new Color(60, 80, 60));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        comp.setFont(new Font("Arial", Font.PLAIN, 13));
        comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        comp.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 210, 180)),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        comp.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        p.add(lbl);
        p.add(Box.createVerticalStrut(4));
        p.add(comp);
        return p;
    }
    
    private JPanel textAreaField(String label, JTextArea area) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setForeground(new Color(60, 80, 60));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        area.setFont(new Font("Arial", Font.PLAIN, 13));
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 210, 180)),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        JScrollPane sp = new JScrollPane(area);
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        sp.setBorder(null);
        
        p.add(lbl);
        p.add(Box.createVerticalStrut(4));
        p.add(sp);
        return p;
    }
    
    private JPanel dialogBtns(JDialog dialog, String saveText, Runnable saveAction) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        p.setBackground(Color.WHITE);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        JButton cancelBtn = actionBtn("Cancel", new Color(108, 117, 125), e -> dialog.dispose());
        JButton saveBtn = actionBtn(saveText, primaryGreen, e -> saveAction.run());
        
        p.add(cancelBtn);
        p.add(saveBtn);
        return p;
    }
    
   
    private void showSuccess(String msg) { JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE); }
    private void showError(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }
    private void showWarning(String msg) { JOptionPane.showMessageDialog(this, msg, "Warning", JOptionPane.WARNING_MESSAGE); }
    private boolean confirm(String msg) { return JOptionPane.showConfirmDialog(this, msg, "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION; }
    
    private void logout() {
        if (confirm("Logout?")) { new LoginPage().setVisible(true); dispose(); }
    }
}