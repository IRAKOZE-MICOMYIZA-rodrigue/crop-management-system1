package model;

import model.Crop;
import java.sql.Date;

public class CropTest {
    
    public static void main(String[] args) {
        // Test crop creation
        Date plantingDate = Date.valueOf("2024-01-15");
        Date harvestDate = Date.valueOf("2024-06-15");
        
        Crop crop = new Crop(1, 1, "Test Wheat", plantingDate, harvestDate, 150.0);
        
        assert crop.getUserId() == 1;
        assert "Test Wheat".equals(crop.getCropName());
        assert crop.getQuantityPlanted() == 150.0;
        assert "PLANTED".equals(crop.getGrowthStatus());
        
        System.out.println("✓ Crop creation test passed");
        
        // Test setters
        crop.setQuantityHarvested(120.5);
        assert crop.getQuantityHarvested() == 120.5;
        assert crop.getAvailableQuantity() == 120.5;
        
        System.out.println("✓ Crop setters test passed");
        System.out.println("All model tests completed!");
    }
}