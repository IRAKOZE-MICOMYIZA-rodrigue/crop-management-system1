package controller;

import dao.CropDAO;
import dao.SaleDAO;
import model.Crop;
import model.Sale;
import util.ValidationUtil;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class SaleController {
    private SaleDAO saleDAO;
    private CropDAO cropDAO;
    
    public SaleController() {
        this.saleDAO = new SaleDAO();
        this.cropDAO = new CropDAO();
    }
    
    public String addSale(int cropId, int userId, Date saleDate, String quantityStr, 
                          String priceStr, String buyerName, String buyerContact) {
        if (cropId <= 0) {
            return "Please select a crop!";
        }
        if (saleDate == null) {
            return "Sale date is required!";
        }
        if (!ValidationUtil.isPositiveNumber(quantityStr)) {
            return "Quantity must be a positive number!";
        }
        if (!ValidationUtil.isPositiveNumber(priceStr)) {
            return "Price must be a positive number!";
        }
        if (ValidationUtil.isEmpty(buyerName)) {
            return "Buyer name cannot be empty!";
        }
        
        try {
            Crop crop = cropDAO.getCropById(cropId);
            if (crop == null) {
                return "Crop not found!";
            }
            
            if (!"HARVESTED".equals(crop.getGrowthStatus())) {
                return "Cannot sell crop that hasn't been harvested!";
            }
            
            if (crop.getActualHarvestDate() != null && saleDate.before(crop.getActualHarvestDate())) {
                return "Sale date cannot be before harvest date!";
            }
            
            double quantity = ValidationUtil.parseDouble(quantityStr);
            double price = ValidationUtil.parseDouble(priceStr);
            
            double available = cropDAO.getAvailableQuantity(cropId);
            if (!ValidationUtil.isQuantityValid(available, quantity)) {
                return "Quantity exceeds available stock! Available: " + available;
            }
            
            Sale sale = new Sale(cropId, userId, saleDate, quantity, price, buyerName, buyerContact);
            
            if (saleDAO.addSale(sale)) {
                cropDAO.updateQuantityAfterSale(cropId, quantity);
                return "SUCCESS";
            } else {
                return "Failed to record sale!";
            }
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }
    
    public List<Sale> getSalesByUserId(int userId) throws SQLException {
        return saleDAO.getSalesByUserId(userId);
    }
    
    public List<Sale> getAllSales() throws SQLException {
        return saleDAO.getAllSales();
    }
    
    public double getTotalEarningsByUserId(int userId) throws SQLException {
        return saleDAO.getTotalEarningsByUserId(userId);
    }
    
    public double getTotalSalesAmount() throws SQLException {
        return saleDAO.getTotalSalesAmount();
    }
    
    public String deleteSale(int saleId) {
        try {
            if (saleDAO.deleteSale(saleId)) {
                return "SUCCESS";
            } else {
                return "Failed to delete sale!";
            }
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }
}