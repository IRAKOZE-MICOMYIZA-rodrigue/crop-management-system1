-- Database initialization script for Crop Management System

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    farm_location VARCHAR(200),
    phone VARCHAR(20),
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'FARMER' CHECK (role IN ('FARMER', 'ADMIN')),
    login_attempts INTEGER DEFAULT 0,
    is_locked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create crop_types table
CREATE TABLE IF NOT EXISTS crop_types (
    crop_type_id SERIAL PRIMARY KEY,
    type_name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    typical_growth_period INTEGER, -- in days
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create crops table
CREATE TABLE IF NOT EXISTS crops (
    crop_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    crop_type_id INTEGER REFERENCES crop_types(crop_type_id),
    crop_name VARCHAR(100) NOT NULL,
    planting_date DATE NOT NULL,
    expected_harvest_date DATE,
    actual_harvest_date DATE,
    growth_status VARCHAR(20) DEFAULT 'PLANTED' CHECK (growth_status IN ('PLANTED', 'GROWING', 'HARVESTED', 'FAILED')),
    quantity_planted DECIMAL(10,2) NOT NULL,
    quantity_harvested DECIMAL(10,2) DEFAULT 0,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create sales table
CREATE TABLE IF NOT EXISTS sales (
    sale_id SERIAL PRIMARY KEY,
    crop_id INTEGER REFERENCES crops(crop_id) ON DELETE CASCADE,
    buyer_name VARCHAR(100) NOT NULL,
    quantity_sold DECIMAL(10,2) NOT NULL,
    price_per_unit DECIMAL(10,2) NOT NULL,
    total_amount DECIMAL(12,2) NOT NULL,
    sale_date DATE NOT NULL,
    payment_status VARCHAR(20) DEFAULT 'PENDING' CHECK (payment_status IN ('PENDING', 'PAID', 'CANCELLED')),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default crop types
INSERT INTO crop_types (type_name, description, typical_growth_period) VALUES
('Wheat', 'Cereal grain crop', 120),
('Rice', 'Staple food grain', 150),
('Corn', 'Maize crop for food and feed', 100),
('Tomato', 'Vegetable crop', 80),
('Potato', 'Root vegetable', 90),
('Soybean', 'Legume crop', 110)
ON CONFLICT (type_name) DO NOTHING;

-- Insert default admin user
INSERT INTO users (name, email, password, role, farm_location, phone) VALUES
('System Administrator', 'admin@cropmanagement.com', 'admin123', 'ADMIN', 'Main Office', '123-456-7890')
ON CONFLICT (email) DO NOTHING;

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_crops_user_id ON crops(user_id);
CREATE INDEX IF NOT EXISTS idx_crops_status ON crops(growth_status);
CREATE INDEX IF NOT EXISTS idx_sales_crop_id ON sales(crop_id);
CREATE INDEX IF NOT EXISTS idx_sales_date ON sales(sale_date);