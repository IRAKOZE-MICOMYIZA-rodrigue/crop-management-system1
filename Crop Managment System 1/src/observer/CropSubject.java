package observer;

import model.Crop;
import java.util.ArrayList;
import java.util.List;

/**
 * Subject class for Observer Pattern
 * Manages crop observers and notifications
 */
public class CropSubject {
    private List<CropObserver> observers = new ArrayList<>();
    
    public void addObserver(CropObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(CropObserver observer) {
        observers.remove(observer);
    }
    
    public void notifyStatusChanged(Crop crop, String oldStatus, String newStatus) {
        for (CropObserver observer : observers) {
            observer.onCropStatusChanged(crop, oldStatus, newStatus);
        }
    }
    
    public void notifyHarvested(Crop crop, double quantity) {
        for (CropObserver observer : observers) {
            observer.onCropHarvested(crop, quantity);
        }
    }
    
    public void notifyAdded(Crop crop) {
        for (CropObserver observer : observers) {
            observer.onCropAdded(crop);
        }
    }
}