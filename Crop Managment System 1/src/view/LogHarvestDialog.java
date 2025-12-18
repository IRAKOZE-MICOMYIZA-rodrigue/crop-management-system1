package view;

import controller.CropController;
import model.Crop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class LogHarvestDialog extends JDialog {
    private CropController cropController;
    private FarmerDashboard parent;
    private int cropId;
    private Crop currentCrop;
    
    private JLabel cropNameLabel;
    private JLabel plantingDateLabel;
    private JLabel quantityPlantedLabel;
    private JTextField harvestDateField;
    private JTextField quantityHarvestedField;
    private JButton logButton;
    
   
    private final Color PRIMARY_COLOR = new Color(255, 140, 0);
    private final Color SECONDARY_COLOR = new Color(0, 100, 0);
    private final Color GRAY_COLOR = new Color(108, 117, 125);
    
    public LogHarvestDialog(FarmerDashboard parent, CropController cropController, int cropId) {
        super(parent, "Log Harvest", true);
        this.parent = parent;
        this.cropController = cropController;
        this.cropId = cropId;
        initializeUI();
        loadCropData();
    }
    
    private void initializeUI() {
        setSize(450, 450);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        mainPanel.setBackground(Color.WHITE);
        
        
        JLabel titleLabel = new JLabel("🌾 Log Harvest");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(SECONDARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Record the harvest details for your crop");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
      
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(248, 249, 250));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        JLabel infoTitleLabel = new JLabel("📋 Crop Information");
        infoTitleLabel.setFont(new Font("Arial", Font.BOLD, 13));
        infoTitleLabel.setForeground(new Color(70, 70, 70));
        
        cropNameLabel = new JLabel("Crop: Loading...");
        cropNameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        plantingDateLabel = new JLabel("Planted: Loading...");
        plantingDateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        quantityPlantedLabel = new JLabel("Quantity Planted: Loading...");
        quantityPlantedLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        infoPanel.add(infoTitleLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(cropNameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(plantingDateLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(quantityPlantedLabel);
        
       
        harvestDateField = new JTextField();
        harvestDateField.setToolTipText("Format: YYYY-MM-DD");
        
       
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        harvestDateField.setText(sdf.format(new java.util.Date()));
        
        quantityHarvestedField = new JTextField();
        quantityHarvestedField.setToolTipText("Enter the quantity harvested");
        
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(infoPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(createFormField("Harvest Date * (YYYY-MM-DD)", harvestDateField));
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(createFormField("Quantity Harvested * (kg/units)", quantityHarvestedField));
        mainPanel.add(Box.createVerticalStrut(25));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        JButton cancelButton = createStyledButton("Cancel", GRAY_COLOR);
        logButton = createStyledButton("Log Harvest", PRIMARY_COLOR);
        
        cancelButton.addActionListener(e -> dispose());
        logButton.addActionListener(e -> logHarvest());
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(logButton);
        
        mainPanel.add(buttonPanel);
        
        add(mainPanel);
        
    
        quantityHarvestedField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    logHarvest();
                }
            }
        });
    }
    
    private void loadCropData() {
        try {
            currentCrop = cropController.getCropById(cropId);
            if (currentCrop != null) {
                cropNameLabel.setText("🌱 Crop: " + currentCrop.getCropName() + " (" + currentCrop.getCropTypeName() + ")");
                plantingDateLabel.setText("📅 Planted: " + currentCrop.getPlantingDate());
                quantityPlantedLabel.setText("📊 Quantity Planted: " + currentCrop.getQuantityPlanted() + " units");
                
                // Pre-fill quantity with planted amount
                quantityHarvestedField.setText(String.valueOf(currentCrop.getQuantityPlanted()));
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Crop not found!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                dispose();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading crop data: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }
    
    private JPanel createFormField(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setForeground(new Color(70, 70, 70));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        
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
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        
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
    
    private void logHarvest() {
        String harvestDateStr = harvestDateField.getText().trim();
        String quantityStr = quantityHarvestedField.getText().trim();
    
        Date harvestDate = null;
        try {
            if (!harvestDateStr.isEmpty()) {
                harvestDate = Date.valueOf(harvestDateStr);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Harvest date is required!", 
                    "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
                harvestDateField.requestFocus();
                return;
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid harvest date format!\nPlease use YYYY-MM-DD format.", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            harvestDateField.requestFocus();
            return;
        }
        
       
        if (quantityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Quantity harvested is required!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            quantityHarvestedField.requestFocus();
            return;
        }
        
        double quantity;
        try {
            quantity = Double.parseDouble(quantityStr);
            if (quantity <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Quantity must be a positive number!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            quantityHarvestedField.requestFocus();
            return;
        }
        
       
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Log harvest for '" + currentCrop.getCropName() + "'?\n\n" +
            "Harvest Date: " + harvestDateStr + "\n" +
            "Quantity: " + quantity + " units\n\n" +
            "This will mark the crop as HARVESTED.", 
            "Confirm Harvest", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
       
        logButton.setEnabled(false);
        logButton.setText("Logging...");
        
        
        String result = cropController.logHarvest(cropId, harvestDate, quantityStr);
        
        if ("SUCCESS".equals(result)) {
            JOptionPane.showMessageDialog(this, 
                "Harvest logged successfully!\n\n" +
                "The crop is now available for sale.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            parent.refreshData();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                result, 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            logButton.setEnabled(true);
            logButton.setText("Log Harvest");
        }
    }

    boolean isSuccess() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}