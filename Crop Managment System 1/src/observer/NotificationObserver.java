package observer;

import model.Crop;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Concrete Observer implementation for notifications
 */
public class NotificationObserver implements CropObserver {
    private String observerName;
    
    public NotificationObserver(String name) {
        this.observerName = name;
    }
    
    @Override
    public void onCropStatusChanged(Crop crop, String oldStatus, String newStatus) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("[" + timestamp + "] " + observerName + 
                          ": Crop '" + crop.getCropName() + "' status changed from " + 
                          oldStatus + " to " + newStatus);
    }
    
    @Override
    public void onCropHarvested(Crop crop, double quantity) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("[" + timestamp + "] " + observerName + 
                          ": Crop '" + crop.getCropName() + "' harvested: " + 
                          quantity + " units");
    }
    
    @Override
    public void onCropAdded(Crop crop) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("[" + timestamp + "] " + observerName + 
                          ": New crop added: '" + crop.getCropName() + "'");
    }
}