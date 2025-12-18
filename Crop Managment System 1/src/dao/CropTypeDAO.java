package dao;

import model.CropType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CropTypeDAO {
    
    public boolean addCropType(CropType cropType) throws SQLException {
        String sql = "INSERT INTO crop_types (crop_name, description) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, cropType.getCropName());
            stmt.setString(2, cropType.getDescription());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cropType.setCropTypeId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    public CropType getCropTypeById(int cropTypeId) throws SQLException {
        String sql = "SELECT * FROM crop_types WHERE crop_type_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cropTypeId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractCropTypeFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    public List<CropType> getAllCropTypes() throws SQLException {
        List<CropType> cropTypes = new ArrayList<>();
        String sql = "SELECT * FROM crop_types ORDER BY crop_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                cropTypes.add(extractCropTypeFromResultSet(rs));
            }
        }
        return cropTypes;
    }
    
    public boolean updateCropType(CropType cropType) throws SQLException {
        String sql = "UPDATE crop_types SET crop_name = ?, description = ? WHERE crop_type_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cropType.getCropName());
            stmt.setString(2, cropType.getDescription());
            stmt.setInt(3, cropType.getCropTypeId());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean deleteCropType(int cropTypeId) throws SQLException {
        String sql = "DELETE FROM crop_types WHERE crop_type_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cropTypeId);
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean cropTypeNameExists(String cropName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM crop_types WHERE LOWER(crop_name) = LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cropName);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    private CropType extractCropTypeFromResultSet(ResultSet rs) throws SQLException {
        CropType cropType = new CropType();
        cropType.setCropTypeId(rs.getInt("crop_type_id"));
        cropType.setCropName(rs.getString("crop_name"));
        cropType.setDescription(rs.getString("description"));
        cropType.setCreatedAt(rs.getTimestamp("created_at"));
        return cropType;
    }
}