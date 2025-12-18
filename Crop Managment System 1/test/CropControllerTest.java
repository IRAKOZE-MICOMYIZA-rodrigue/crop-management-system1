import controller.CropController;
import java.sql.Date;

public class CropControllerTest {
    private CropController cropController;
    
    public void setUp() {
        cropController = new CropController();
    }
    
    public void testAddCropWithEmptyName() {
        setUp();
        Date plantingDate = Date.valueOf("2024-01-15");
        Date harvestDate = Date.valueOf("2024-06-15");
        
        String result = cropController.addCrop(1, 1, "", 
                                             plantingDate, harvestDate, "100.0", "Test notes");
        
        assert "Crop name cannot be empty!".equals(result);
        System.out.println("✓ Empty name validation test passed");
    }
    
    public void testAddCropWithInvalidQuantity() {
        setUp();
        Date plantingDate = Date.valueOf("2024-01-15");
        Date harvestDate = Date.valueOf("2024-06-15");
        
        String result = cropController.addCrop(1, 1, "Test Crop", 
                                             plantingDate, harvestDate, "-50", "Test notes");
        
        assert "Quantity must be a positive number!".equals(result);
        System.out.println("✓ Invalid quantity validation test passed");
    }
    
    public static void main(String[] args) {
        CropControllerTest test = new CropControllerTest();
        System.out.println("Running tests...");
        
        test.testAddCropWithEmptyName();
        test.testAddCropWithInvalidQuantity();
        
        System.out.println("All tests completed!");
    }
}