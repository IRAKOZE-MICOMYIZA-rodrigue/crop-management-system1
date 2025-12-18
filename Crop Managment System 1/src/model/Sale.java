package model;

import java.sql.Date;
import java.sql.Timestamp;

public class Sale {
    private int saleId;
    private int cropId;
    private int userId;
    private Date saleDate;
    private double quantitySold;
    private double pricePerUnit;
    private double totalAmount;
    private String buyerName;
    private String buyerContact;
    private String paymentStatus;
    private Timestamp createdAt;
    private String cropName;
    private String farmerName;

    public Sale() {}

    public Sale(int cropId, int userId, Date saleDate, double quantitySold, 
                double pricePerUnit, String buyerName, String buyerContact) {
        this.cropId = cropId;
        this.userId = userId;
        this.saleDate = saleDate;
        this.quantitySold = quantitySold;
        this.pricePerUnit = pricePerUnit;
        this.totalAmount = quantitySold * pricePerUnit;
        this.buyerName = buyerName;
        this.buyerContact = buyerContact;
        this.paymentStatus = "COMPLETED";
    }

    
    public int getSaleId() { return saleId; }
    public int getCropId() { return cropId; }
    public int getUserId() { return userId; }
    public Date getSaleDate() { return saleDate; }
    public double getQuantitySold() { return quantitySold; }
    public double getPricePerUnit() { return pricePerUnit; }
    public double getTotalAmount() { return totalAmount; }
    public String getBuyerName() { return buyerName; }
    public String getBuyerContact() { return buyerContact; }
    public String getPaymentStatus() { return paymentStatus; }
    public Timestamp getCreatedAt() { return createdAt; }
    public String getCropName() { return cropName; }
    public String getFarmerName() { return farmerName; }

    
    public void setSaleId(int saleId) { this.saleId = saleId; }
    public void setCropId(int cropId) { this.cropId = cropId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setSaleDate(Date saleDate) { this.saleDate = saleDate; }
    public void setQuantitySold(double quantitySold) { this.quantitySold = quantitySold; }
    public void setPricePerUnit(double pricePerUnit) { this.pricePerUnit = pricePerUnit; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }
    public void setBuyerContact(String buyerContact) { this.buyerContact = buyerContact; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public void setCropName(String cropName) { this.cropName = cropName; }
    public void setFarmerName(String farmerName) { this.farmerName = farmerName; }

    public void setSaleDate(String saleDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}