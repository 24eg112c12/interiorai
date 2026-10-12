-- ===================================================================
-- InteriorAI Complete MySQL Database Setup Script
-- Run this in MySQL Workbench or via command line:
-- mysql -u root -p < interiorai_db.sql
-- ===================================================================

CREATE DATABASE IF NOT EXISTS interiorai_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE interiorai_db;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    mobile VARCHAR(15),
    role VARCHAR(20) DEFAULT 'ROLE_USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Rooms Table
CREATE TABLE IF NOT EXISTS rooms (
    room_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    room_type VARCHAR(50) NOT NULL,
    length_m DECIMAL(8,2) NOT NULL,
    width_m DECIMAL(8,2) NOT NULL,
    ceiling_height_m DECIMAL(8,2),
    existing_furniture TEXT,
    number_of_doors INT DEFAULT 1,
    number_of_windows INT DEFAULT 1,
    number_of_occupants INT DEFAULT 1,
    additional_requirements TEXT,
    photo_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rooms_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

-- 3. Designs Table
CREATE TABLE IF NOT EXISTS designs (
    design_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id BIGINT NOT NULL,
    design_name VARCHAR(150) NOT NULL,
    interior_style VARCHAR(50) NOT NULL,
    color_palette VARCHAR(100),
    description TEXT,
    image_url VARCHAR(500),
    prompt_used TEXT,
    generation_status VARCHAR(30) DEFAULT 'COMPLETED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_designs_room FOREIGN KEY (room_id) REFERENCES rooms (room_id) ON DELETE CASCADE
);

-- 4. Budgets Table
CREATE TABLE IF NOT EXISTS budgets (
    budget_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id BIGINT NOT NULL,
    design_id BIGINT NULL,
    total_budget DECIMAL(12,2) NOT NULL,
    estimated_cost DECIMAL(12,2) NOT NULL,
    actual_cost DECIMAL(12,2) DEFAULT 0.00,
    currency CHAR(3) DEFAULT 'INR',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_budgets_room FOREIGN KEY (room_id) REFERENCES rooms (room_id) ON DELETE CASCADE,
    CONSTRAINT fk_budgets_design FOREIGN KEY (design_id) REFERENCES designs (design_id) ON DELETE SET NULL
);

-- Supporting table: budget_items
CREATE TABLE IF NOT EXISTS budget_items (
    item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    budget_id BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL,
    item_name VARCHAR(150),
    estimated_amount DECIMAL(12,2) NOT NULL,
    notes TEXT,
    CONSTRAINT fk_budget_items_budget FOREIGN KEY (budget_id) REFERENCES budgets (budget_id) ON DELETE CASCADE
);

-- 5. Professionals Table
CREATE TABLE IF NOT EXISTS professionals (
    professional_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    mobile VARCHAR(15),
    email VARCHAR(150),
    experience_years INT DEFAULT 1,
    location VARCHAR(255) NOT NULL,
    service_type VARCHAR(80) NOT NULL,
    rating DECIMAL(3,2) DEFAULT 4.8,
    reviews_count INT DEFAULT 12,
    hourly_or_base_rate VARCHAR(50),
    portfolio_image_url VARCHAR(500),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. Projects Table
CREATE TABLE IF NOT EXISTS projects (
    project_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    design_id BIGINT NULL,
    project_name VARCHAR(150) NOT NULL,
    status VARCHAR(30) DEFAULT 'PLANNING',
    start_date DATE,
    target_end_date DATE,
    progress_percentage INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_projects_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_projects_room FOREIGN KEY (room_id) REFERENCES rooms (room_id) ON DELETE CASCADE,
    CONSTRAINT fk_projects_design FOREIGN KEY (design_id) REFERENCES designs (design_id) ON DELETE SET NULL
);

-- 7. Bookings Table (References users, professionals, and projects)
CREATE TABLE IF NOT EXISTS bookings (
    booking_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    professional_id BIGINT NOT NULL,
    project_id BIGINT NULL,
    booking_date DATE NOT NULL,
    message TEXT,
    status VARCHAR(30) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_bookings_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_bookings_prof FOREIGN KEY (professional_id) REFERENCES professionals (professional_id) ON DELETE CASCADE,
    CONSTRAINT fk_bookings_project FOREIGN KEY (project_id) REFERENCES projects (project_id) ON DELETE SET NULL
);

-- Sample Demo User (demo@interiorai.com / password123)
INSERT INTO users (user_id, full_name, email, password, mobile, role)
VALUES (1, 'Aarav Sharma', 'demo@interiorai.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '+91 98765 43210', 'ROLE_USER')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);

-- Sample Professionals
INSERT INTO professionals (professional_id, name, mobile, email, experience_years, location, service_type, rating, reviews_count, hourly_or_base_rate, portfolio_image_url, description)
VALUES 
(1, 'Priya Sundaram', '+91 98451 22345', 'priya.studio@interiorai.demo', 9, 'Bengaluru, Karnataka (Indiranagar / Koramangala)', 'Interior Designer', 4.9, 48, '₹1,500/hr consultation', 'https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=800&q=80', 'Award-winning interior architect specializing in contemporary Indian apartments, smart space optimization, and Scandinavian-Indian fusion.'),
(2, 'Vikram Malhotra & Associates', '+91 98203 99881', 'vikram.designs@interiorai.demo', 12, 'Mumbai, Maharashtra (Bandra / Andheri West)', 'Interior Designer', 4.9, 62, '₹2,000/hr consultation', 'https://images.unsplash.com/photo-1618221195710-dd6b41faaea6?auto=format&fit=crop&w=800&q=80', 'Luxury residential and high-end penthouse interior consultant with over 150 completed turnkey projects across Mumbai and Pune.'),
(3, 'Rajesh Kumar Painting Works', '+91 98112 33445', 'rajesh.paints@interiorai.demo', 8, 'Delhi-NCR (South Delhi / Gurugram)', 'Painter', 4.8, 35, '₹18/sq.ft wall emulsion', 'https://images.unsplash.com/photo-1589939705384-5185137a7f0f?auto=format&fit=crop&w=800&q=80', 'Specialist in luxury royal emulsions, texture paints, waterproofing, and airless spray painting with premium dust-free machines.'),
(4, 'Mohan Suthar Wood Craft', '+91 98230 44556', 'mohan.wood@interiorai.demo', 14, 'Pune, Maharashtra (Kothrud / Baner)', 'Carpenter', 4.9, 51, '₹850/sq.ft modular woodwork', 'https://images.unsplash.com/photo-1538688525198-9b88f6f53126?auto=format&fit=crop&w=800&q=80', 'Master craftsman specializing in modular kitchens, customized wardrobes, Teakwood temple mandirs, and concealed acoustic wall panelling.'),
(5, 'Suresh Naidu Electricals', '+91 99401 55667', 'suresh.power@interiorai.demo', 7, 'Hyderabad, Telangana (Gachibowli / Hitec City)', 'Electrician', 4.7, 29, '₹600 base service charge', 'https://images.unsplash.com/photo-1621905251189-08b45d6a269e?auto=format&fit=crop&w=800&q=80', 'Certified smart home automation installer, concealed circuit re-wiring, inverter setups, and electrical panel distribution expert.'),
(6, 'Lumina Studio & Architectural Lighting', '+91 98402 66778', 'lumina.studio@interiorai.demo', 6, 'Chennai, Tamil Nadu (T. Nagar / Anna Nagar)', 'Lighting Specialist', 4.8, 31, '₹1,200 lighting plan design', 'https://images.unsplash.com/photo-1513506003901-1e6a229e2d15?auto=format&fit=crop&w=800&q=80', 'Cove lighting, magnetic track lights, smart RGB ambient profiles, and energy-efficient accent lighting installations for luxury homes.'),
(7, 'Gyproc Artisans & POP Ceiling Works', '+91 98301 77889', 'gyproc.ceilings@interiorai.demo', 10, 'Kolkata, West Bengal (Salt Lake / New Town)', 'False-Ceiling Worker', 4.8, 42, '₹110/sq.ft Saint-Gobain POP', 'https://images.unsplash.com/photo-1600565193348-f74bd3c7ccdf?auto=format&fit=crop&w=800&q=80', 'Precision gypsum board ceilings, peripheral cove mouldings, acoustic drywall, and designer rafters with seamless surface finish.'),
(8, 'Royal Comfort Sofa & Upholstery', '+91 98250 88990', 'royalcomfort@interiorai.demo', 11, 'Ahmedabad, Gujarat (Satellite / Bodakdev)', 'Sofa Maker', 4.9, 38, '₹24,000 for 3-seater custom sofa', 'https://images.unsplash.com/photo-1555041469-a586c61ea9bc?auto=format&fit=crop&w=800&q=80', 'Bespoke L-shape sectionals, recliner refurbishing, imported velvet & genuine leather upholstery with high-density Sleepwell foam.'),
(9, 'Apex Sanitary & Plumbing Services', '+91 98455 11223', 'apex.plumbing@interiorai.demo', 9, 'Bengaluru, Karnataka (Whitefield / HSR Layout)', 'Plumber', 4.7, 27, '₹550 base service charge', 'https://images.unsplash.com/photo-1584622650111-993a426fbf0a?auto=format&fit=crop&w=800&q=80', 'Concealed diverters, Grohe/Kohler sanitaryware fittings, bathroom remodeling drainage, and booster pump installations.'),
(10, 'Gurukripa Modular Wardrobes & Woodwork', '+91 98101 22334', 'gurukripa@interiorai.demo', 15, 'Delhi-NCR (Noida / Greater Noida)', 'Carpenter', 4.8, 44, '₹920/sq.ft boiling waterproof ply', 'https://images.unsplash.com/photo-1616486338812-3dadae4b4ace?auto=format&fit=crop&w=800&q=80', 'Floor-to-ceiling glass-door wardrobes, hydraulic beds, fluted wooden wall panels, and factory-pressed acrylic finish cabinets.'),
(11, 'Colors of India Decor & Texture Paints', '+91 98200 77665', 'colorsofindia@interiorai.demo', 10, 'Mumbai, Maharashtra (Thane / Powai)', 'Painter', 4.8, 33, '₹22/sq.ft metallic stencil finish', 'https://images.unsplash.com/photo-1562663474-6cbb3eaa4d14?auto=format&fit=crop&w=800&q=80', 'Italian stucco marble effects, textured accent walls, damp-proof exterior coatings, and anti-fungal kitchen/bathroom paint.'),
(12, 'Studio Minimal - Budget Renovation Experts', '+91 98450 99001', 'studiominimal@interiorai.demo', 5, 'Bengaluru, Karnataka (Electronic City / Sarjapur)', 'Interior Designer', 4.8, 39, '₹800/hr budget consulting', 'https://images.unsplash.com/photo-1598928506311-c55ded91a20c?auto=format&fit=crop&w=800&q=80', 'Budget-friendly 1BHK/2BHK renovations focusing on Ikea/Pepperfry furniture styling, warm earthy palettes, and zero-waste makeovers.')
ON DUPLICATE KEY UPDATE name=VALUES(name);
