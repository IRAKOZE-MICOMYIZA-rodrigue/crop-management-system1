-- ULTIMATE Database schema - Fixes ALL DAO issues

-- Create users table (UserDAO compatible)
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

-- Create crop_types table (CropTypeDAO compatible)
CREATE TABLE IF NOT EXISTS crop_types (
    crop_type_id SERIAL PRIMARY KEY,
    crop_name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create crops table (CropDAO compatible)
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

-- Create sales table (SaleDAO compatible - ALL COLUMNS INCLUDED)
CREATE TABLE IF NOT EXISTS sales (
    sale_id SERIAL PRIMARY KEY,
    crop_id INTEGER REFERENCES crops(crop_id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    sale_date DATE NOT NULL,
    quantity_sold DECIMAL(10,2) NOT NULL,
    price_per_unit DECIMAL(10,2) NOT NULL,
    total_amount DECIMAL(12,2) NOT NULL,
    buyer_name VARCHAR(100) NOT NULL,
    buyer_contact VARCHAR(100),
    payment_status VARCHAR(20) DEFAULT 'COMPLETED' CHECK (payment_status IN ('PENDING', 'PAID', 'CANCELLED', 'COMPLETED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert crop types
INSERT INTO crop_types (crop_name, description) VALUES
('Wheat', 'Cereal grain crop'),
('Rice', 'Staple food grain'),
('Corn', 'Maize crop for food and feed'),
('Tomato', 'Vegetable crop'),
('Potato', 'Root vegetable'),
('Soybean', 'Legume crop')
ON CONFLICT (crop_name) DO NOTHING;

-- Insert users
INSERT INTO users (name, email, password, role, farm_location, phone) VALUES
('System Administrator', 'admin@cropmanagement.com', 'admin123', 'ADMIN', 'Main Office', '0791728668'),
('John Farmer', 'john@farm.com', 'farmer123', 'FARMER', 'Farm Location', '123-456-7890')
ON CONFLICT (email) DO NOTHING;

-- Performance indexes
CREATE INDEX IF NOT EXISTS idx_crops_user_id ON crops(user_id);
CREATE INDEX IF NOT EXISTS idx_crops_status ON crops(growth_status);
CREATE INDEX IF NOT EXISTS idx_sales_crop_id ON sales(crop_id);
CREATE INDEX IF NOT EXISTS idx_sales_user_id ON sales(user_id);
CREATE INDEX IF NOT EXISTS idx_sales_date ON sales(sale_date);