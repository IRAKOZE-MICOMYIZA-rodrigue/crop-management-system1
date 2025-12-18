package controller;

import dao.CropDAO;
import dao.CropTypeDAO;
import model.Crop;
import model.CropType;
import observer.CropSubject;
import observer.NotificationObserver;
import util.ValidationUtil;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller class for crop management operations
 * Implements Observer pattern for notifications
 * Follows Google Java Style Guide
 */
public class CropController {
    private final CropDAO cropDAO;
    private final CropTypeDAO cropTypeDAO;
    private final CropSubject cropSubject;
    
    public CropController() {
        this.cropDAO = new CropDAO();
        this.cropTypeDAO = new CropTypeDAO();
        this.cropSubject = new CropSubject();
        
        // Add default notification observer
        this.cropSubject.addObserver(new NotificationObserver("System"));
    }
    
    public String addCrop(int userId, int cropTypeId, String cropName, 
                          Date plantingDate, Date expectedHarvestDate, String quantityStr, String notes) {
        if (ValidationUtil.isEmpty(cropName)) {
            return "Crop name cannot be empty!";
        }
        if (cropTypeId <= 0) {
            return "Please select a crop type!";
        }
        if (plantingDate == null) {
            return "Planting date is required!";
        }
        if (expectedHarvestDate != null && !ValidationUtil.isHarvestDateValid(plantingDate, expectedHarvestDate)) {
            return "Expected harvest date cannot be before planting date!";
        }
        if (!ValidationUtil.isPositiveNumber(quantityStr)) {
            return "Quantity must be a positive number!";
        }
        
        try {
            CropType cropType = cropTypeDAO.getCropTypeById(cropTypeId);
            if (cropType == null) {
                return "Invalid crop type!";
            }
            
            if (cropDAO.cropNameExistsForFarmer(userId, cropName)) {
                return "You already have a crop with this name!";
            }
            
            double quantity = ValidationUtil.parseDouble(quantityStr);
            Crop crop = new Crop(userId, cropTypeId, cropName, plantingDate, expectedHarvestDate, quantity);
            crop.setNotes(notes);
            
            if (cropDAO.addCrop(crop)) {
                // Notify observers about new crop
                cropSubject.notifyAdded(crop);
                return "SUCCESS";
            } else {
                return "Failed to add crop!";
            }
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }
    
    public String updateCrop(Crop crop) {
        if (ValidationUtil.isEmpty(crop.getCropName())) {
            return "Crop name cannot be empty!";
        }
        if (crop.getExpectedHarvestDate() != null && 
            !ValidationUtil.isHarvestDateValid(crop.getPlantingDate(), crop.getExpectedHarvestDate())) {
            return "Expected harvest date cannot be before planting date!";
        }
        
        try {
            if (cropDAO.cropNameExistsForFarmerExcluding(crop.getUserId(), crop.getCropName(), crop.getCropId())) {
                return "You already have another crop with this name!";
            }
            
            if (cropDAO.updateCrop(crop)) {
                return "SUCCESS";
            } else {
                return "Failed to update crop!";
            }
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }
    
    public String logHarvest(int cropId, Date harvestDate, String quantityStr) {
        if (harvestDate == null) {
            return "Harvest date is required!";
        }
        if (!ValidationUtil.isPositiveNumber(quantityStr)) {
            return "Quantity must be a positive number!";
        }
        
        try {
            Crop crop = cropDAO.getCropById(cropId);
            if (crop == null) {
                return "Crop not found!";
            }
            
            if (!ValidationUtil.isHarvestDateValid(crop.getPlantingDate(), harvestDate)) {
                return "Harvest date cannot be before planting date!";
            }
            
            double quantity = ValidationUtil.parseDouble(quantityStr);
            
            if (cropDAO.logHarvest(cropId, harvestDate, quantity)) {
                // Update crop status and notify observers
                crop.setActualHarvestDate(harvestDate);
                crop.setQuantityHarvested(quantity);
                String oldStatus = crop.getGrowthStatus();
                crop.setGrowthStatus("HARVESTED");
                
                cropSubject.notifyStatusChanged(crop, oldStatus, "HARVESTED");
                cropSubject.notifyHarvested(crop, quantity);
                return "SUCCESS";
            } else {
                return "Failed to log harvest!";
            }
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }
    
    public List<Crop> getCropsByUserId(int userId) throws SQLException {
        return cropDAO.getCropsByUserId(userId);
    }
    
    public List<Crop> getAllCrops() throws SQLException {
        return cropDAO.getAllCrops();
    }
    
    public List<Crop> getHarvestedCropsByUserId(int userId) throws SQLException {
        return cropDAO.getHarvestedCropsByUserId(userId);
    }
    
    public Crop getCropById(int cropId) throws SQLException {
        return cropDAO.getCropById(cropId);
    }
    
    public String deleteCrop(int cropId) {
        try {
            if (cropDAO.deleteCrop(cropId)) {
                return "SUCCESS";
            } else {
                return "Failed to delete crop!";
            }
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }
    
    public double getAvailableQuantity(int cropId) throws SQLException {
        return cropDAO.getAvailableQuantity(cropId);
    }
}