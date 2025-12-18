package controller;

import dao.CropTypeDAO;
import model.CropType;
import util.ValidationUtil;

import java.sql.SQLException;
import java.util.List;

public class CropTypeController {
    private CropTypeDAO cropTypeDAO;
    
    public CropTypeController() {
        this.cropTypeDAO = new CropTypeDAO();
    }
    
    public String addCropType(String cropName, String description) {
        if (ValidationUtil.isEmpty(cropName)) {
            return "Crop type name cannot be empty!";
        }
        
        try {
            if (cropTypeDAO.cropTypeNameExists(cropName)) {
                return "Crop type already exists!";
            }
            
            CropType cropType = new CropType(cropName, description);
            if (cropTypeDAO.addCropType(cropType)) {
                return "SUCCESS";
            } else {
                return "Failed to add crop type!";
            }
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }
    
    public List<CropType> getAllCropTypes() throws SQLException {
        return cropTypeDAO.getAllCropTypes();
    }
    
    public CropType getCropTypeById(int cropTypeId) throws SQLException {
        return cropTypeDAO.getCropTypeById(cropTypeId);
    }
    
    public String updateCropType(CropType cropType) {
        if (ValidationUtil.isEmpty(cropType.getCropName())) {
            return "Crop type name cannot be empty!";
        }
        
        try {
            if (cropTypeDAO.updateCropType(cropType)) {
                return "SUCCESS";
            } else {
                return "Failed to update crop type!";
            }
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }
    
    public String deleteCropType(int cropTypeId) {
        try {
            if (cropTypeDAO.deleteCropType(cropTypeId)) {
                return "SUCCESS";
            } else {
                return "Failed to delete crop type!";
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getMessage().contains("constraint")) {
                return "Cannot delete! Crop type is in use.";
            }
            return "Database error: " + e.getMessage();
        }
    }
}