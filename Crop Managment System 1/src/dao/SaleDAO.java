package dao;

import model.Sale;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleDAO {
    
    public boolean addSale(Sale sale) throws SQLException {
        String sql = "INSERT INTO sales (crop_id, user_id, sale_date, quantity_sold, price_per_unit, total_amount, buyer_name, buyer_contact, payment_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, sale.getCropId());
            stmt.setInt(2, sale.getUserId());
            stmt.setDate(3, sale.getSaleDate());
            stmt.setDouble(4, sale.getQuantitySold());
            stmt.setDouble(5, sale.getPricePerUnit());
            stmt.setDouble(6, sale.getTotalAmount());
            stmt.setString(7, sale.getBuyerName());
            stmt.setString(8, sale.getBuyerContact());
            stmt.setString(9, sale.getPaymentStatus() != null ? sale.getPaymentStatus() : "COMPLETED");
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        sale.setSaleId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    public Sale getSaleById(int saleId) throws SQLException {
        String sql = "SELECT s.*, c.crop_name, u.name as farmer_name " +
                     "FROM sales s " +
                     "JOIN crops c ON s.crop_id = c.crop_id " +
                     "JOIN users u ON s.user_id = u.user_id " +
                     "WHERE s.sale_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, saleId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractSaleFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    public List<Sale> getAllSales() throws SQLException {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT s.*, c.crop_name, u.name as farmer_name " +
                     "FROM sales s " +
                     "JOIN crops c ON s.crop_id = c.crop_id " +
                     "JOIN users u ON s.user_id = u.user_id " +
                     "ORDER BY s.sale_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                sales.add(extractSaleFromResultSet(rs));
            }
        }
        return sales;
    }
    
    public List<Sale> getSalesByUserId(int userId) throws SQLException {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT s.*, c.crop_name, u.name as farmer_name " +
                     "FROM sales s " +
                     "JOIN crops c ON s.crop_id = c.crop_id " +
                     "JOIN users u ON s.user_id = u.user_id " +
                     "WHERE s.user_id = ? ORDER BY s.sale_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sales.add(extractSaleFromResultSet(rs));
                }
            }
        }
        return sales;
    }
    
    public boolean updateSale(Sale sale) throws SQLException {
        String sql = "UPDATE sales SET sale_date = ?, quantity_sold = ?, price_per_unit = ?, " +
                     "total_amount = ?, buyer_name = ?, buyer_contact = ?, payment_status = ? WHERE sale_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, sale.getSaleDate());
            stmt.setDouble(2, sale.getQuantitySold());
            stmt.setDouble(3, sale.getPricePerUnit());
            stmt.setDouble(4, sale.getTotalAmount());
            stmt.setString(5, sale.getBuyerName());
            stmt.setString(6, sale.getBuyerContact());
            stmt.setString(7, sale.getPaymentStatus());
            stmt.setInt(8, sale.getSaleId());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean deleteSale(int saleId) throws SQLException {
        String sql = "DELETE FROM sales WHERE sale_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, saleId);
            return stmt.executeUpdate() > 0;
        }
    }
    
    public double getTotalEarningsByUserId(int userId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM sales WHERE user_id = ? AND payment_status = 'COMPLETED'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0;
    }
    
    public double getTotalSalesAmount() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM sales WHERE payment_status = 'COMPLETED'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0;
    }
    
    private Sale extractSaleFromResultSet(ResultSet rs) throws SQLException {
        Sale sale = new Sale();
        sale.setSaleId(rs.getInt("sale_id"));
        sale.setCropId(rs.getInt("crop_id"));
        sale.setUserId(rs.getInt("user_id"));
        sale.setSaleDate(rs.getDate("sale_date"));
        sale.setQuantitySold(rs.getDouble("quantity_sold"));
        sale.setPricePerUnit(rs.getDouble("price_per_unit"));
        sale.setTotalAmount(rs.getDouble("total_amount"));
        sale.setBuyerName(rs.getString("buyer_name"));
        sale.setBuyerContact(rs.getString("buyer_contact"));
        sale.setPaymentStatus(rs.getString("payment_status"));
        sale.setCreatedAt(rs.getTimestamp("created_at"));
        sale.setCropName(rs.getString("crop_name"));
        sale.setFarmerName(rs.getString("farmer_name"));
        return sale;
    }
}