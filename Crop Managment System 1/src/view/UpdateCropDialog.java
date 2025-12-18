package view;

import controller.CropController;
import controller.CropTypeController;
import model.Crop;
import model.CropType;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class UpdateCropDialog extends JDialog {
    private User currentUser;
    private CropController cropController;
    private CropTypeController cropTypeController;
    private FarmerDashboard parent;
    private int cropId;
    private Crop currentCrop;
    
    private JTextField cropNameField;
    private JComboBox<CropType> cropTypeCombo;
    private JTextField plantingDateField;
    private JTextField expectedHarvestField;
    private JTextField quantityPlantedField;
    private JComboBox<String> statusCombo;
    private JTextArea notesArea;
    private JButton updateButton;
    
    // Colors
    private final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private final Color SECONDARY_COLOR = new Color(0, 100, 0);
    private final Color GRAY_COLOR = new Color(108, 117, 125);
    
    public UpdateCropDialog(FarmerDashboard parent, User user, CropController cropController, 
                           CropTypeController cropTypeController, int cropId) {
        super(parent, "Update Crop", true);
        this.parent = parent;
        this.currentUser = user;
        this.cropController = cropController;
        this.cropTypeController = cropTypeController;
        this.cropId = cropId;
        initializeUI();
        loadCropData();
    }
    
    private void initializeUI() {
        setSize(480, 620);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        mainPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("✏️ Update Crop");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(SECONDARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Modify the crop details below");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        cropNameField = new JTextField();
        cropTypeCombo = new JComboBox<>();
        plantingDateField = new JTextField();
        plantingDateField.setToolTipText("Format: YYYY-MM-DD");
        expectedHarvestField = new JTextField();
        expectedHarvestField.setToolTipText("Format: YYYY-MM-DD");
        quantityPlantedField = new JTextField();
        
        statusCombo = new JComboBox<>(new String[]{
            "PLANTED", "GERMINATED", "GROWING", "READY_TO_HARVEST", "HARVESTED"
        });

        notesArea = new JTextArea(3, 20);
        notesArea.setFont(new Font("Arial", Font.PLAIN, 13));
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        JPanel notesPanel = new JPanel();
        notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.Y_AXIS));
        notesPanel.setBackground(Color.WHITE);
        notesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        notesPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        JLabel notesLabel = new JLabel("Notes (Optional)");
        notesLabel.setFont(new Font("Arial", Font.BOLD, 11));
        notesLabel.setForeground(new Color(70, 70, 70));
        notesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JScrollPane notesScroll = new JScrollPane(notesArea);
        notesScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        notesScroll.setBorder(null);
        
        notesPanel.add(notesLabel);
        notesPanel.add(Box.createVerticalStrut(5));
        notesPanel.add(notesScroll);

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(createFormField("Crop Name *", cropNameField));
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(createComboField("Crop Type *", cropTypeCombo));
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(createFormField("Planting Date * (YYYY-MM-DD)", plantingDateField));
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(createFormField("Expected Harvest Date (YYYY-MM-DD)", expectedHarvestField));
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(createFormField("Quantity Planted (kg/units)", quantityPlantedField));
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(createComboField("Growth Status", statusCombo));
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(notesPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        JButton cancelButton = createStyledButton("Cancel", GRAY_COLOR);
        updateButton = createStyledButton("Update Crop", PRIMARY_COLOR);
        
        cancelButton.addActionListener(e -> dispose());
        updateButton.addActionListener(e -> updateCrop());
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(updateButton);
        
        mainPanel.add(buttonPanel);
        
        add(mainPanel);
    }
    
    private void loadCropData() {
        try {
           
            List<CropType> types = cropTypeController.getAllCropTypes();
            cropTypeCombo.removeAllItems();
            for (CropType type : types) {
                cropTypeCombo.addItem(type);
            }
            
            currentCrop = cropController.getCropById(cropId);
            if (currentCrop != null) {
                cropNameField.setText(currentCrop.getCropName());
                plantingDateField.setText(currentCrop.getPlantingDate() != null ? 
                    currentCrop.getPlantingDate().toString() : "");
                expectedHarvestField.setText(currentCrop.getExpectedHarvestDate() != null ? 
                    currentCrop.getExpectedHarvestDate().toString() : "");
                quantityPlantedField.setText(String.valueOf(currentCrop.getQuantityPlanted()));
                notesArea.setText(currentCrop.getNotes() != null ? currentCrop.getNotes() : "");

                for (int i = 0; i < cropTypeCombo.getItemCount(); i++) {
                    CropType type = cropTypeCombo.getItemAt(i);
                    if (type.getCropTypeId() == currentCrop.getCropTypeId()) {
                        cropTypeCombo.setSelectedIndex(i);
                        break;
                    }
                }

                statusCombo.setSelectedItem(currentCrop.getGrowthStatus());
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
        
        combo.setFont(new Font("Arial", Font.PLAIN, 13));
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
    
    private void updateCrop() {
        String cropName = cropNameField.getText().trim();
        CropType selectedType = (CropType) cropTypeCombo.getSelectedItem();
        String plantingDateStr = plantingDateField.getText().trim();
        String expectedHarvestStr = expectedHarvestField.getText().trim();
        String quantityStr = quantityPlantedField.getText().trim();
        String status = (String) statusCombo.getSelectedItem();
        String notes = notesArea.getText().trim();
        
        if (cropName.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Crop name cannot be empty!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            cropNameField.requestFocus();
            return;
        }
        
        if (selectedType == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select a crop type!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Date plantingDate = null;
        Date expectedHarvestDate = null;
        
        try {
            if (!plantingDateStr.isEmpty()) {
                plantingDate = Date.valueOf(plantingDateStr);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid planting date format!\nPlease use YYYY-MM-DD format.", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            plantingDateField.requestFocus();
            return;
        }
        
        try {
            if (!expectedHarvestStr.isEmpty()) {
                expectedHarvestDate = Date.valueOf(expectedHarvestStr);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid expected harvest date format!\nPlease use YYYY-MM-DD format.", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            expectedHarvestField.requestFocus();
            return;
        }

        double quantity = 0;
        try {
            if (!quantityStr.isEmpty()) {
                quantity = Double.parseDouble(quantityStr);
                if (quantity < 0) {
                    throw new NumberFormatException();
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Quantity must be a valid positive number!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            quantityPlantedField.requestFocus();
            return;
        }

        currentCrop.setCropName(cropName);
        currentCrop.setCropTypeId(selectedType.getCropTypeId());
        currentCrop.setPlantingDate(plantingDate);
        currentCrop.setExpectedHarvestDate(expectedHarvestDate);
        currentCrop.setQuantityPlanted(quantity);
        currentCrop.setGrowthStatus(status);
        currentCrop.setNotes(notes);

        updateButton.setEnabled(false);
        updateButton.setText("Updating...");

        String result = cropController.updateCrop(currentCrop);
        
        if ("SUCCESS".equals(result)) {
            JOptionPane.showMessageDialog(this, 
                "Crop updated successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            parent.refreshData();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                result, 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            updateButton.setEnabled(true);
            updateButton.setText("Update Crop");
        }
    }

    boolean isSuccess() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}