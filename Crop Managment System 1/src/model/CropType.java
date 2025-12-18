package model;

import java.sql.Timestamp;

public class CropType {
    private int cropTypeId;
    private String cropName;
    private String description;
    private Timestamp createdAt;

    public CropType() {}

    public CropType(String cropName, String description) {
        this.cropName = cropName;
        this.description = description;
    }

    
    public int getCropTypeId() { return cropTypeId; }
    public String getCropName() { return cropName; }
    public String getDescription() { return description; }
    public Timestamp getCreatedAt() { return createdAt; }

    
    public void setCropTypeId(int cropTypeId) { this.cropTypeId = cropTypeId; }
    public void setCropName(String cropName) { this.cropName = cropName; }
    public void setDescription(String description) { this.description = description; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return cropName;
    }
}