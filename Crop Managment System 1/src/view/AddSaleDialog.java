package view;

import controller.CropController;
import controller.SaleController;
import model.Crop;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class AddSaleDialog extends JDialog {
    private User currentUser;
    private SaleController saleController;
    private CropController cropController;
    private FarmerDashboard parent;
    
    private JComboBox<Crop> cropCombo;
    private JLabel availableQuantityLabel;
    private JTextField saleDateField;
    private JTextField quantityField;
    private JTextField priceField;
    private JTextField buyerNameField;
    private JTextField buyerContactField;
    private JLabel totalAmountLabel;
    private JButton saveButton;
    
    private DecimalFormat rwfFormat = new DecimalFormat("#,###");

    private final Color PRIMARY_COLOR = new Color(34, 139, 34);
    private final Color SECONDARY_COLOR = new Color(0, 100, 0);
    private final Color GRAY_COLOR = new Color(108, 117, 125);
    private final Color BLUE_COLOR = new Color(0, 123, 255);
    
    public AddSaleDialog(FarmerDashboard parent, User user, SaleController saleController, CropController cropController) {
        super(parent, "Record Sale", true);
        this.parent = parent;
        this.currentUser = user;
        this.saleController = saleController;
        this.cropController = cropController;
        initializeUI();
        loadHarvestedCrops();
    }
    
    private void initializeUI() {
        setSize(500, 650);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        mainPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Record New Sale");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(SECONDARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Enter the sale details below");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        cropCombo = new JComboBox<>();
        cropCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        
        cropCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Crop) {
                    Crop crop = (Crop) value;
                    if (crop.getCropId() == 0) {
                        setText(crop.getCropName());
                    } else {
                        setText(crop.getCropName() + " (Available: " + crop.getQuantityHarvested() + " kg)");
                    }
                }
                return this;
            }
        });

        JPanel availablePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        availablePanel.setBackground(Color.WHITE);
        availablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        availablePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        availableQuantityLabel = new JLabel("Available: Select a crop");
        availableQuantityLabel.setFont(new Font("Arial", Font.BOLD, 11));
        availableQuantityLabel.setForeground(BLUE_COLOR);
        availablePanel.add(availableQuantityLabel);
        
        saleDateField = new JTextField();
        saleDateField.setToolTipText("Format: YYYY-MM-DD");
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        saleDateField.setText(sdf.format(new java.util.Date()));
        
        quantityField = new JTextField();
        priceField = new JTextField();
        buyerNameField = new JTextField();
        buyerContactField = new JTextField();
        
        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.Y_AXIS));
        totalPanel.setBackground(new Color(232, 245, 233));
        totalPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        totalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        totalPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        JLabel totalTitleLabel = new JLabel("Total Amount");
        totalTitleLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        totalTitleLabel.setForeground(Color.GRAY);
        
        totalAmountLabel = new JLabel("0 RWF");
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 22));
        totalAmountLabel.setForeground(PRIMARY_COLOR);
        
        totalPanel.add(totalTitleLabel);
        totalPanel.add(Box.createVerticalStrut(5));
        totalPanel.add(totalAmountLabel);
        
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(createComboField("Select Crop *", cropCombo));
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(availablePanel);
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(createFormField("Sale Date * (YYYY-MM-DD)", saleDateField));
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(createFormField("Quantity * (kg)", quantityField));
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(createFormField("Price Per kg * (RWF)", priceField));
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(createFormField("Buyer Name *", buyerNameField));
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(createFormField("Buyer Contact (Optional)", buyerContactField));
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(totalPanel);
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));
        
        JButton cancelButton = createStyledButton("Cancel", GRAY_COLOR);
        saveButton = createStyledButton("Record Sale", PRIMARY_COLOR);
        
        cancelButton.addActionListener(e -> dispose());
        saveButton.addActionListener(e -> saveSale());
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setupListeners();
    }
    
    private void setupListeners() {
        cropCombo.addActionListener(e -> updateAvailableQuantity());
        
        KeyAdapter calculateListener = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateTotal();
            }
        };
        
        quantityField.addKeyListener(calculateListener);
        priceField.addKeyListener(calculateListener);
        
        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    saveSale();
                }
            }
        };
        
        saleDateField.addKeyListener(enterListener);
        quantityField.addKeyListener(enterListener);
        priceField.addKeyListener(enterListener);
        buyerNameField.addKeyListener(enterListener);
        buyerContactField.addKeyListener(enterListener);
    }
    
    private void loadHarvestedCrops() {
        try {
            List<Crop> crops = cropController.getHarvestedCropsByUserId(currentUser.getUserId());
            cropCombo.removeAllItems();
            
            Crop placeholder = new Crop();
            placeholder.setCropId(0);
            placeholder.setCropName("-- Select a Crop --");
            placeholder.setQuantityHarvested(0);
            cropCombo.addItem(placeholder);
            
            for (Crop crop : crops) {
                if (crop.getQuantityHarvested() > 0) {
                    cropCombo.addItem(crop);
                }
            }
            
            if (cropCombo.getItemCount() <= 1) {
                JOptionPane.showMessageDialog(this, 
                    "No harvested crops with available quantity found.\n\n" +
                    "Please harvest a crop first before recording sales.", 
                    "No Crops Available", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading crops: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateAvailableQuantity() {
        Crop selectedCrop = (Crop) cropCombo.getSelectedItem();
        if (selectedCrop != null && selectedCrop.getCropId() > 0) {
            try {
                double available = cropController.getAvailableQuantity(selectedCrop.getCropId());
                availableQuantityLabel.setText(String.format("Available: %.2f kg", available));
                availableQuantityLabel.setForeground(available > 0 ? BLUE_COLOR : Color.RED);
            } catch (SQLException e) {
                availableQuantityLabel.setText("Available: Error loading");
                availableQuantityLabel.setForeground(Color.RED);
            }
        } else {
            availableQuantityLabel.setText("Available: Select a crop");
            availableQuantityLabel.setForeground(BLUE_COLOR);
        }
    }
    
    private void calculateTotal() {
        try {
            String qtyStr = quantityField.getText().trim();
            String priceStr = priceField.getText().trim();
            
            if (!qtyStr.isEmpty() && !priceStr.isEmpty()) {
                double quantity = Double.parseDouble(qtyStr);
                double price = Double.parseDouble(priceStr);
                double total = quantity * price;
                totalAmountLabel.setText(rwfFormat.format(total) + " RWF");
                
                // Real-time price guidance
                Crop selectedCrop = (Crop) cropCombo.getSelectedItem();
                if (selectedCrop != null && selectedCrop.getCropId() > 0) {
                    validatePriceInRealTime(selectedCrop, price);
                }
            } else {
                totalAmountLabel.setText("0 RWF");
            }
        } catch (NumberFormatException e) {
            totalAmountLabel.setText("0 RWF");
        }
    }
    
    // ==================== BUSINESS GUIDANCE METHODS ====================
    
    /**
     * Check all farming considerations before saving
     */
    private boolean validateBusinessRules(Crop selectedCrop, double quantity, double price, Date saleDate) {
        try {
            // 1. Available Quantity Check
            if (!validateAvailableQuantity(selectedCrop.getCropId(), quantity)) {
                return false;
            }
            
            // 2. Minimum Sale Quantity
            if (!validateMinimumSaleQuantity(quantity)) {
                return false;
            }
            
            // 3. Realistic Price Range
            if (!validatePriceRange(selectedCrop.getCropName(), price)) {
                return false;
            }
            
            // 4. Sale Date Logic
            if (!validateSaleDate(saleDate)) {
                return false;
            }
            
            // 5. Large Sale Warning
            if (!validateLargeSale(quantity, price)) {
                int proceed = JOptionPane.showConfirmDialog(this,
                    "Important Sale Notice\n\n" +
                    "You are about to record a significant sale of " + rwfFormat.format(quantity * price) + " RWF.\n" +
                    "Please double-check the details before proceeding.\n\n" +
                    "Continue with this sale?",
                    "Sale Amount Notice",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
                return proceed == JOptionPane.YES_OPTION;
            }
            
            return true;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "We encountered an issue while checking your sale details: " + e.getMessage(),
                "System Check",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Check if sale quantity exceeds available quantity
     */
    private boolean validateAvailableQuantity(int cropId, double saleQuantity) throws SQLException {
        double availableQuantity = cropController.getAvailableQuantity(cropId);
        
        if (saleQuantity > availableQuantity) {
            JOptionPane.showMessageDialog(this,
                "Quantity Adjustment Needed\n\n" +
                "You're trying to sell " + saleQuantity + " kg, but only " + availableQuantity + " kg is available.\n" +
                "Please adjust the quantity to match your available stock.",
                "Available Quantity Check",
                JOptionPane.WARNING_MESSAGE);
            quantityField.requestFocus();
            return false;
        }
        
        // Advisory if selling more than 80% of available
        if (saleQuantity > availableQuantity * 0.8) {
            int proceed = JOptionPane.showConfirmDialog(this,
                "Inventory Advisory\n\n" +
                "You're planning to sell " + String.format("%.0f%%", (saleQuantity/availableQuantity)*100) + 
                " of your available " + availableQuantity + " kg.\n" +
                "This will leave limited remaining inventory.\n\n" +
                "Proceed with this sale quantity?",
                "Inventory Level Notice",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            return proceed == JOptionPane.YES_OPTION;
        }
        
        return true;
    }
    
    /**
     * Check minimum sale quantity for practical farming
     */
    private boolean validateMinimumSaleQuantity(double quantity) {
        double minimumSale = 1.0; // Minimum 1 kg for practical farming
        
        if (quantity < minimumSale) {
            JOptionPane.showMessageDialog(this,
                "Sale Quantity Consideration\n\n" +
                "For practical farming operations, we recommend a minimum sale of " + minimumSale + " kg.\n" +
                "Smaller quantities may not be practical for handling and transportation.",
                "Sale Quantity Advisory",
                JOptionPane.INFORMATION_MESSAGE);
            quantityField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Check if price is within realistic market ranges
     */
    private boolean validatePriceRange(String cropName, double price) {
        PriceRange range = getMarketPriceRange(cropName);
        
        if (price < range.minPrice) {
            JOptionPane.showMessageDialog(this,
                "Price Check for " + cropName + "\n\n" +
                "The entered price of " + rwfFormat.format(price) + " RWF/kg seems lower than typical market rates.\n" +
                "Current market range: " + rwfFormat.format(range.minPrice) + " - " + rwfFormat.format(range.maxPrice) + " RWF/kg\n\n" +
                "Please confirm this is the intended sale price.",
                "Price Consideration",
                JOptionPane.WARNING_MESSAGE);
            priceField.requestFocus();
            return false;
        }
        
        if (price > range.maxPrice) {
            int proceed = JOptionPane.showConfirmDialog(this,
                "Premium Price Notice\n\n" +
                "The price of " + rwfFormat.format(price) + " RWF/kg for " + cropName + " is higher than typical market rates.\n" +
                "Market range: " + rwfFormat.format(range.minPrice) + " - " + rwfFormat.format(range.maxPrice) + " RWF/kg\n\n" +
                "Is this the correct price for your premium product?",
                "Premium Price Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            return proceed == JOptionPane.YES_OPTION;
        }
        
        return true;
    }
    
    /**
     * Check if sale date makes sense for farming operations
     */
    private boolean validateSaleDate(Date saleDate) {
        java.util.Date today = new java.util.Date();
        java.util.Date saleUtilDate = new java.util.Date(saleDate.getTime());
        
        // Check for future sale dates
        if (saleUtilDate.after(today)) {
            int proceed = JOptionPane.showConfirmDialog(this,
                "Future Sale Date\n\n" +
                "The sale date is set for a future date.\n" +
                "This might be for a forward contract or planned sale.\n\n" +
                "Confirm this future sale date?",
                "Future Sale Date",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            return proceed == JOptionPane.YES_OPTION;
        }
        
        // Check for very old sale dates
        long daysDifference = (today.getTime() - saleUtilDate.getTime()) / (1000 * 60 * 60 * 24);
        if (daysDifference > 365) {
            JOptionPane.showMessageDialog(this,
                "Date Check\n\n" +
                "The sale date appears to be from more than a year ago.\n" +
                "Please verify the date to ensure accurate records.",
                "Historical Date Notice",
                JOptionPane.INFORMATION_MESSAGE);
            saleDateField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Check for large sales that need extra attention
     */
    private boolean validateLargeSale(double quantity, double price) {
        double totalAmount = quantity * price;
        double largeSaleThreshold = 1000000; // 1 million RWF
        
        if (totalAmount > largeSaleThreshold) {
            return false; // This will trigger the large sale advisory
        }
        
        return true;
    }
    
    /**
     * Real-time price guidance as user types
     */
    private void validatePriceInRealTime(Crop selectedCrop, double price) {
        PriceRange range = getMarketPriceRange(selectedCrop.getCropName());
        
        if (price < range.minPrice * 0.8) {
            priceField.setBackground(new Color(255, 245, 230)); // Light orange
            priceField.setToolTipText("Price guidance: Typical " + selectedCrop.getCropName() + " prices are " + 
                                    rwfFormat.format(range.minPrice) + "-" + rwfFormat.format(range.maxPrice) + " RWF/kg");
        } else if (price > range.maxPrice * 1.2) {
            priceField.setBackground(new Color(230, 245, 255)); // Light blue
            priceField.setToolTipText("Premium price noted for " + selectedCrop.getCropName() + " - typical range is " + 
                                    rwfFormat.format(range.minPrice) + "-" + rwfFormat.format(range.maxPrice) + " RWF/kg");
        } else {
            priceField.setBackground(Color.WHITE);
            priceField.setToolTipText("Price within typical range for " + selectedCrop.getCropName());
        }
    }
    
    /**
     * Get market price ranges for different crops
     */
    private PriceRange getMarketPriceRange(String cropName) {
        // These would typically come from a market data service
        switch (cropName.toUpperCase()) {
            case "MAIZE":
                return new PriceRange(300, 600);
            case "BEANS":
                return new PriceRange(800, 1500);
            case "POTATOES":
                return new PriceRange(400, 800);
            case "RICE":
                return new PriceRange(1000, 2000);
            case "WHEAT":
                return new PriceRange(700, 1200);
            case "CASSAVA":
                return new PriceRange(200, 400);
            case "BANANAS":
                return new PriceRange(300, 600);
            case "COFFEE":
                return new PriceRange(1500, 3000);
            default:
                return new PriceRange(200, 1000); // Default range
        }
    }
    
    // Helper class for price ranges
    private class PriceRange {
        double minPrice;
        double maxPrice;
        
        PriceRange(double min, double max) {
            this.minPrice = min;
            this.maxPrice = max;
        }
    }
    
    // ==================== EXISTING UI METHODS ====================
    
    private JPanel createFormField(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setForeground(new Color(70, 70, 70));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        
        return panel;
    }
    
    private JPanel createComboField(String labelText, JComboBox<?> combo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setForeground(new Color(70, 70, 70));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        combo.setBackground(Color.WHITE);
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(combo);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        button.setPreferredSize(new Dimension(140, 40));
        
        Color hoverColor = bgColor.darker();
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void saveSale() {
        Crop selectedCrop = (Crop) cropCombo.getSelectedItem();
        String saleDateStr = saleDateField.getText().trim();
        String quantityStr = quantityField.getText().trim();
        String priceStr = priceField.getText().trim();
        String buyerName = buyerNameField.getText().trim();
        String buyerContact = buyerContactField.getText().trim();
        
        // Technical Validation
        if (selectedCrop == null || selectedCrop.getCropId() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Please select a crop.", 
                "Required Information", 
                JOptionPane.WARNING_MESSAGE);
            cropCombo.requestFocus();
            return;
        }
        
        Date saleDate = null;
        try {
            if (!saleDateStr.isEmpty()) {
                saleDate = Date.valueOf(saleDateStr);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Sale date is required.", 
                    "Required Information", 
                    JOptionPane.WARNING_MESSAGE);
                saleDateField.requestFocus();
                return;
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid sale date format. Please use YYYY-MM-DD format.", 
                "Date Format", 
                JOptionPane.WARNING_MESSAGE);
            saleDateField.requestFocus();
            return;
        }
        
        if (quantityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Quantity is required.", 
                "Required Information", 
                JOptionPane.WARNING_MESSAGE);
            quantityField.requestFocus();
            return;
        }
        
        if (priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Price per kg is required.", 
                "Required Information", 
                JOptionPane.WARNING_MESSAGE);
            priceField.requestFocus();
            return;
        }
        
        if (buyerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Buyer name is required.", 
                "Required Information", 
                JOptionPane.WARNING_MESSAGE);
            buyerNameField.requestFocus();
            return;
        }
        
        double quantity = 0;
        double price = 0;
        try {
            quantity = Double.parseDouble(quantityStr);
            price = Double.parseDouble(priceStr);
            
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Quantity must be greater than 0.", 
                    "Quantity Check", 
                    JOptionPane.WARNING_MESSAGE);
                quantityField.requestFocus();
                return;
            }
            
            if (price <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Price must be greater than 0.", 
                    "Price Check", 
                    JOptionPane.WARNING_MESSAGE);
                priceField.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid quantity or price. Please enter valid numbers.", 
                "Number Format", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Business Guidance
        if (!validateBusinessRules(selectedCrop, quantity, price, saleDate)) {
            return;
        }
        
        double total = quantity * price;
        
        // Final confirmation
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Confirm Sale Details:\n\n" +
            "Crop: " + selectedCrop.getCropName() + "\n" +
            "Quantity: " + quantity + " kg\n" +
            "Price/kg: " + rwfFormat.format(price) + " RWF\n" +
            "Total: " + rwfFormat.format(total) + " RWF\n" +
            "Buyer: " + buyerName + "\n\n" +
            "Proceed with this sale?",
            "Confirm Sale", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        saveButton.setEnabled(false);
        saveButton.setText("Recording...");
        
        try {
            String result = saleController.addSale(
                selectedCrop.getCropId(),
                currentUser.getUserId(),
                saleDate,
                quantityStr,
                priceStr,
                buyerName,
                buyerContact
            );
            
            if ("SUCCESS".equals(result)) {
                JOptionPane.showMessageDialog(this, 
                    "Sale recorded successfully.\n\n" +
                    "Total Amount: " + rwfFormat.format(total) + " RWF\n" +
                    "Thank you for your business.", 
                    "Sale Completed", 
                    JOptionPane.INFORMATION_MESSAGE);
                parent.refreshData();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    result, 
                    "Recording Issue", 
                    JOptionPane.ERROR_MESSAGE);
                saveButton.setEnabled(true);
                saveButton.setText("Record Sale");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "We encountered an issue while saving your sale: " + e.getMessage(), 
                "System Issue", 
                JOptionPane.ERROR_MESSAGE);
            saveButton.setEnabled(true);
            saveButton.setText("Record Sale");
        }
    }
}