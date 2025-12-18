package view;

import controller.CropController;
import controller.CropTypeController;
import model.CropType;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class AddCropDialog extends JDialog {
    private User currentUser;
    private CropController cropController;
    private CropTypeController cropTypeController;
    private FarmerDashboard parent;
    
    private JTextField cropNameField;
    private JComboBox<CropType> cropTypeCombo;
    private JTextField plantingDateField;
    private JTextField expectedHarvestField;
    private JTextField quantityField;
    private JTextArea notesArea;
    private JButton saveButton;
    
    
    private final Color PRIMARY_COLOR = new Color(34, 139, 34);
    private final Color SECONDARY_COLOR = new Color(0, 100, 0);
    private final Color GRAY_COLOR = new Color(108, 117, 125);
    
    public AddCropDialog(FarmerDashboard parent, User user, CropController cropController, CropTypeController cropTypeController) {
        super(parent, "Add New Crop", true);
        this.parent = parent;
        this.currentUser = user;
        this.cropController = cropController;
        this.cropTypeController = cropTypeController;
        initializeUI();
        loadCropTypes();
    }
    
    private void initializeUI() {
        setSize(480, 580);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        mainPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("🌱 Add New Crop");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(SECONDARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Fill in the details below to add a new crop");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        cropNameField = new JTextField();
        cropTypeCombo = new JComboBox<>();
        plantingDateField = new JTextField();
        plantingDateField.setToolTipText("Format: YYYY-MM-DD (e.g., 2025-01-15)");
        expectedHarvestField = new JTextField();
        expectedHarvestField.setToolTipText("Format: YYYY-MM-DD (e.g., 2025-06-15)");
        quantityField = new JTextField();
        
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

        JLabel dateHintLabel = new JLabel("📅 Date format: YYYY-MM-DD");
        dateHintLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        dateHintLabel.setForeground(Color.GRAY);
        dateHintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        plantingDateField.setText(sdf.format(new java.util.Date()));
        
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
        mainPanel.add(createFormField("Quantity Planted * (kg/units)", quantityField));
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(notesPanel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(dateHintLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        JButton cancelButton = createStyledButton("Cancel", GRAY_COLOR);
        saveButton = createStyledButton("Save Crop", PRIMARY_COLOR);
        
        cancelButton.addActionListener(e -> dispose());
        saveButton.addActionListener(e -> saveCrop());
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        mainPanel.add(buttonPanel);
        
        add(mainPanel);

        cropNameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    saveCrop();
                }
            }
        });
    }
    
    private void loadCropTypes() {
        try {
            List<CropType> types = cropTypeController.getAllCropTypes();
            cropTypeCombo.removeAllItems();

            CropType placeholder = new CropType();
            placeholder.setCropTypeId(0);
            placeholder.setCropName("-- Select Crop Type --");
            cropTypeCombo.addItem(placeholder);
            
            for (CropType type : types) {
                cropTypeCombo.addItem(type);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading crop types: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
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
    
    private void saveCrop() {
        String cropName = cropNameField.getText().trim();
        CropType selectedType = (CropType) cropTypeCombo.getSelectedItem();
        String plantingDateStr = plantingDateField.getText().trim();
        String expectedHarvestStr = expectedHarvestField.getText().trim();
        String quantityStr = quantityField.getText().trim();
        String notes = notesArea.getText().trim();

        if (selectedType == null || selectedType.getCropTypeId() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Please select a crop type!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            cropTypeCombo.requestFocus();
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

        saveButton.setEnabled(false);
        saveButton.setText("Saving...");
        
        String result = cropController.addCrop(
            currentUser.getUserId(),
            selectedType.getCropTypeId(),
            cropName,
            plantingDate,
            expectedHarvestDate,
            quantityStr,
            notes
        );
        
        if ("SUCCESS".equals(result)) {
            JOptionPane.showMessageDialog(this, 
                "Crop added successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            parent.refreshData();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                result, 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            saveButton.setEnabled(true);
            saveButton.setText("Save Crop");
        }
    }

    boolean isSuccess() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}