package model;

import java.sql.Date;
import java.sql.Timestamp;

public class Crop {
    private int cropId;
    private int userId;
    private int cropTypeId;
    private String cropName;
    private Date plantingDate;
    private Date expectedHarvestDate;
    private Date actualHarvestDate;
    private String growthStatus;
    private double quantityPlanted;
    private double quantityHarvested;
    private String notes;
    private Timestamp createdAt;
    private String farmerName;
    private String cropTypeName;

    public Crop() {}

    public Crop(int userId, int cropTypeId, String cropName, Date plantingDate, 
                Date expectedHarvestDate, double quantityPlanted) {
        this.userId = userId;
        this.cropTypeId = cropTypeId;
        this.cropName = cropName;
        this.plantingDate = plantingDate;
        this.expectedHarvestDate = expectedHarvestDate;
        this.quantityPlanted = quantityPlanted;
        this.growthStatus = "PLANTED";
    }

    
    public int getCropId() { return cropId; }
    public int getUserId() { return userId; }
    public int getCropTypeId() { return cropTypeId; }
    public String getCropName() { return cropName; }
    public Date getPlantingDate() { return plantingDate; }
    public Date getExpectedHarvestDate() { return expectedHarvestDate; }
    public Date getActualHarvestDate() { return actualHarvestDate; }
    public String getGrowthStatus() { return growthStatus; }
    public double getQuantityPlanted() { return quantityPlanted; }
    public double getQuantityHarvested() { return quantityHarvested; }
    public String getNotes() { return notes; }
    public Timestamp getCreatedAt() { return createdAt; }
    public String getFarmerName() { return farmerName; }
    public String getCropTypeName() { return cropTypeName; }
    public double getAvailableQuantity() { return quantityHarvested; }

    
    public void setCropId(int cropId) { this.cropId = cropId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setCropTypeId(int cropTypeId) { this.cropTypeId = cropTypeId; }
    public void setCropName(String cropName) { this.cropName = cropName; }
    public void setPlantingDate(Date plantingDate) { this.plantingDate = plantingDate; }
    public void setExpectedHarvestDate(Date expectedHarvestDate) { this.expectedHarvestDate = expectedHarvestDate; }
    public void setActualHarvestDate(Date actualHarvestDate) { this.actualHarvestDate = actualHarvestDate; }
    public void setGrowthStatus(String growthStatus) { this.growthStatus = growthStatus; }
    public void setQuantityPlanted(double quantityPlanted) { this.quantityPlanted = quantityPlanted; }
    public void setQuantityHarvested(double quantityHarvested) { this.quantityHarvested = quantityHarvested; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public void setFarmerName(String farmerName) { this.farmerName = farmerName; }
    public void setCropTypeName(String cropTypeName) { this.cropTypeName = cropTypeName; }

    @Override
    public String toString() {
        return cropName + " (Available: " + quantityHarvested + ")";
    }
}