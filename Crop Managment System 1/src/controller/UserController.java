package controller;

import dao.UserDAO;
import model.User;
import util.ValidationUtil;

import java.sql.SQLException;
import java.util.List;

public class UserController {
    private UserDAO userDAO;
    
    public UserController() {
        this.userDAO = new UserDAO();
    }
    
    public String registerUser(String name, String farmLocation, String phone, 
                               String email, String password, String confirmPassword) {
        if (ValidationUtil.isEmpty(name)) {
            return "Name cannot be empty!";
        }
        if (ValidationUtil.isEmpty(email)) {
            return "Email cannot be empty!";
        }
        if (!ValidationUtil.isValidEmail(email)) {
            return "Invalid email format!";
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            return "Invalid phone number format!";
        }
        if (!ValidationUtil.isValidPassword(password)) {
            return "Password must be at least 6 characters!";
        }
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match!";
        }
        
        try {
            if (userDAO.emailExists(email)) {
                return "Email already registered!";
            }
            
            User user = new User(name, farmLocation, phone, email, password, "FARMER");
            if (userDAO.addUser(user)) {
                return "SUCCESS";
            } else {
                return "Failed to register user!";
            }
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }
    
    public Object[] login(String email, String password) {
        if (ValidationUtil.isEmpty(email) || ValidationUtil.isEmpty(password)) {
            return new Object[]{null, "Email and password are required!"};
        }
        
        try {
            if (userDAO.isAccountLocked(email)) {
                return new Object[]{null, "Account is locked! Contact admin."};
            }
            
            User user = userDAO.authenticate(email, password);
            if (user != null) {
                if (user.isLocked()) {
                    return new Object[]{null, "Account is locked! Contact admin."};
                }
                return new Object[]{user, "SUCCESS"};
            } else {
                userDAO.incrementLoginAttempts(email);
                return new Object[]{null, "Invalid email or password!"};
            }
        } catch (SQLException e) {
            return new Object[]{null, "Database error: " + e.getMessage()};
        }
    }
    
    public List<User> getAllFarmers() throws SQLException {
        return userDAO.getAllFarmers();
    }
    
    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }
    
    public String updateUser(User user) {
        if (ValidationUtil.isEmpty(user.getName())) {
            return "Name cannot be empty!";
        }
        if (!ValidationUtil.isValidEmail(user.getEmail())) {
            return "Invalid email format!";
        }
        
        try {
            if (userDAO.updateUser(user)) {
                return "SUCCESS";
            } else {
                return "Failed to update user!";
            }
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }
    
    public String deleteUser(int userId) {
        try {
            if (userDAO.deleteUser(userId)) {
                return "SUCCESS";
            } else {
                return "Failed to delete user!";
            }
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }
    
    public User getUserById(int userId) throws SQLException {
        return userDAO.getUserById(userId);
    }
    
    public String unlockAccount(int userId) {
        try {
            if (userDAO.unlockAccount(userId)) {
                return "SUCCESS";
            } else {
                return "Failed to unlock account!";
            }
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }
}