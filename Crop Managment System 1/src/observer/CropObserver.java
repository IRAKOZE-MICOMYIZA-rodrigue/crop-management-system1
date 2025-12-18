package observer;

import model.Crop;

/**
 * Observer interface for crop status changes
 * Implements Observer Design Pattern
 */
public interface CropObserver {
    void onCropStatusChanged(Crop crop, String oldStatus, String newStatus);
    void onCropHarvested(Crop crop, double quantity);
    void onCropAdded(Crop crop);
}