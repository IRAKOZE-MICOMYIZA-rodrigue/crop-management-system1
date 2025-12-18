package model;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String name;
    private String farmLocation;
    private String phone;
    private String email;
    private String password;
    private String role;
    private Timestamp createdAt;
    private int loginAttempts;
    private boolean isLocked;

    public User() {}

    public User(String name, String farmLocation, String phone, String email, String password, String role) {
        this.name = name;
        this.farmLocation = farmLocation;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getFarmLocation() { return farmLocation; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public Timestamp getCreatedAt() { return createdAt; }
    public int getLoginAttempts() { return loginAttempts; }
    public boolean isLocked() { return isLocked; }

    
    public void setUserId(int userId) { this.userId = userId; }
    public void setName(String name) { this.name = name; }
    public void setFarmLocation(String farmLocation) { this.farmLocation = farmLocation; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public void setLoginAttempts(int loginAttempts) { this.loginAttempts = loginAttempts; }
    public void setLocked(boolean locked) { isLocked = locked; }

    @Override
    public String toString() {
        return name;
    }
}