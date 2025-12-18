package util;

import java.sql.Date;
import java.util.regex.Pattern;

public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,15}$");
    
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) return true;
        String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)]", "");
        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }
    
    public static boolean isValidNumber(String str) {
        if (isEmpty(str)) return false;
        try {
            Double.parseDouble(str.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean isPositiveNumber(String str) {
        if (!isValidNumber(str)) return false;
        return Double.parseDouble(str.trim()) > 0;
    }
    
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    public static boolean isHarvestDateValid(Date plantingDate, Date harvestDate) {
        if (plantingDate == null || harvestDate == null) return true;
        return !harvestDate.before(plantingDate);
    }
    
    public static boolean isSaleDateValid(Date harvestDate, Date saleDate) {
        if (harvestDate == null || saleDate == null) return false;
        return !saleDate.before(harvestDate);
    }
    
    public static boolean isQuantityValid(double available, double requested) {
        return requested > 0 && requested <= available;
    }
    
    public static double parseDouble(String str) {
        if (!isValidNumber(str)) return 0;
        return Double.parseDouble(str.trim());
    }
}