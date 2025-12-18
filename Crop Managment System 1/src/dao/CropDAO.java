package dao;

import model.Crop;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CropDAO {
    
    public boolean addCrop(Crop crop) throws SQLException {
        String sql = "INSERT INTO crops (user_id, crop_type_id, crop_name, planting_date, expected_harvest_date, quantity_planted, growth_status, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, crop.getUserId());
            stmt.setInt(2, crop.getCropTypeId());
            stmt.setString(3, crop.getCropName());
            stmt.setDate(4, crop.getPlantingDate());
            stmt.setDate(5, crop.getExpectedHarvestDate());
            stmt.setDouble(6, crop.getQuantityPlanted());
            stmt.setString(7, crop.getGrowthStatus() != null ? crop.getGrowthStatus() : "PLANTED");
            stmt.setString(8, crop.getNotes());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        crop.setCropId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    public Crop getCropById(int cropId) throws SQLException {
        String sql = "SELECT c.*, u.name as farmer_name, ct.crop_name as crop_type_name " +
                     "FROM crops c " +
                     "JOIN users u ON c.user_id = u.user_id " +
                     "JOIN crop_types ct ON c.crop_type_id = ct.crop_type_id " +
                     "WHERE c.crop_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cropId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractCropFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    public List<Crop> getAllCrops() throws SQLException {
        List<Crop> crops = new ArrayList<>();
        String sql = "SELECT c.*, u.name as farmer_name, ct.crop_name as crop_type_name " +
                     "FROM crops c " +
                     "JOIN users u ON c.user_id = u.user_id " +
                     "JOIN crop_types ct ON c.crop_type_id = ct.crop_type_id " +
                     "ORDER BY c.created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                crops.add(extractCropFromResultSet(rs));
            }
        }
        return crops;
    }
    
    public List<Crop> getCropsByUserId(int userId) throws SQLException {
        List<Crop> crops = new ArrayList<>();
        String sql = "SELECT c.*, u.name as farmer_name, ct.crop_name as crop_type_name " +
                     "FROM crops c " +
                     "JOIN users u ON c.user_id = u.user_id " +
                     "JOIN crop_types ct ON c.crop_type_id = ct.crop_type_id " +
                     "WHERE c.user_id = ? ORDER BY c.created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    crops.add(extractCropFromResultSet(rs));
                }
            }
        }
        return crops;
    }
    
    public List<Crop> getHarvestedCropsByUserId(int userId) throws SQLException {
        List<Crop> crops = new ArrayList<>();
        String sql = "SELECT c.*, u.name as farmer_name, ct.crop_name as crop_type_name " +
                     "FROM crops c " +
                     "JOIN users u ON c.user_id = u.user_id " +
                     "JOIN crop_types ct ON c.crop_type_id = ct.crop_type_id " +
                     "WHERE c.user_id = ? AND c.growth_status = 'HARVESTED' AND c.quantity_harvested > 0 " +
                     "ORDER BY c.actual_harvest_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    crops.add(extractCropFromResultSet(rs));
                }
            }
        }
        return crops;
    }
    
    public boolean updateCrop(Crop crop) throws SQLException {
        String sql = "UPDATE crops SET crop_type_id = ?, crop_name = ?, planting_date = ?, " +
                     "expected_harvest_date = ?, actual_harvest_date = ?, growth_status = ?, " +
                     "quantity_planted = ?, quantity_harvested = ?, notes = ? WHERE crop_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, crop.getCropTypeId());
            stmt.setString(2, crop.getCropName());
            stmt.setDate(3, crop.getPlantingDate());
            stmt.setDate(4, crop.getExpectedHarvestDate());
            stmt.setDate(5, crop.getActualHarvestDate());
            stmt.setString(6, crop.getGrowthStatus());
            stmt.setDouble(7, crop.getQuantityPlanted());
            stmt.setDouble(8, crop.getQuantityHarvested());
            stmt.setString(9, crop.getNotes());
            stmt.setInt(10, crop.getCropId());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean logHarvest(int cropId, Date harvestDate, double quantityHarvested) throws SQLException {
        String sql = "UPDATE crops SET actual_harvest_date = ?, quantity_harvested = ?, growth_status = 'HARVESTED' WHERE crop_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, harvestDate);
            stmt.setDouble(2, quantityHarvested);
            stmt.setInt(3, cropId);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean updateQuantityAfterSale(int cropId, double quantitySold) throws SQLException {
        String sql = "UPDATE crops SET quantity_harvested = quantity_harvested - ? WHERE crop_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, quantitySold);
            stmt.setInt(2, cropId);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean deleteCrop(int cropId) throws SQLException {
        String sql = "DELETE FROM crops WHERE crop_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cropId);
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean cropNameExistsForFarmer(int userId, String cropName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM crops WHERE user_id = ? AND LOWER(crop_name) = LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, cropName);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    public boolean cropNameExistsForFarmerExcluding(int userId, String cropName, int excludeCropId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM crops WHERE user_id = ? AND LOWER(crop_name) = LOWER(?) AND crop_id != ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, cropName);
            stmt.setInt(3, excludeCropId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    public double getAvailableQuantity(int cropId) throws SQLException {
        String sql = "SELECT quantity_harvested FROM crops WHERE crop_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cropId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("quantity_harvested");
                }
            }
        }
        return 0;
    }
    
    private Crop extractCropFromResultSet(ResultSet rs) throws SQLException {
        Crop crop = new Crop();
        crop.setCropId(rs.getInt("crop_id"));
        crop.setUserId(rs.getInt("user_id"));
        crop.setCropTypeId(rs.getInt("crop_type_id"));
        crop.setCropName(rs.getString("crop_name"));
        crop.setPlantingDate(rs.getDate("planting_date"));
        crop.setExpectedHarvestDate(rs.getDate("expected_harvest_date"));
        crop.setActualHarvestDate(rs.getDate("actual_harvest_date"));
        crop.setGrowthStatus(rs.getString("growth_status"));
        crop.setQuantityPlanted(rs.getDouble("quantity_planted"));
        crop.setQuantityHarvested(rs.getDouble("quantity_harvested"));
        crop.setNotes(rs.getString("notes"));
        crop.setCreatedAt(rs.getTimestamp("created_at"));
        crop.setFarmerName(rs.getString("farmer_name"));
        crop.setCropTypeName(rs.getString("crop_type_name"));
        return crop;
    }
}