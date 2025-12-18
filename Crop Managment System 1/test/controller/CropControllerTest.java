package controller;

import controller.CropController;
import java.sql.Date;

public class CropControllerTest {
    
    public static void main(String[] args) {
        CropController controller = new CropController();
        
        // Test empty name
        String result1 = controller.addCrop(1, 1, "", Date.valueOf("2024-01-15"), 
                                           Date.valueOf("2024-06-15"), "100.0", "");
        assert "Crop name cannot be empty!".equals(result1);
        System.out.println("✓ Empty name test passed");
        
        // Test invalid quantity
        String result2 = controller.addCrop(1, 1, "Test", Date.valueOf("2024-01-15"), 
                                           Date.valueOf("2024-06-15"), "-50", "");
        assert "Quantity must be a positive number!".equals(result2);
        System.out.println("✓ Invalid quantity test passed");
        
        System.out.println("All controller tests completed!");
    }
}