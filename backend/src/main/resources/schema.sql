-- ===================================================================
-- InteriorAI Database Schema (MySQL Compatible)
-- ===================================================================

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    mobile VARCHAR(15),
    role VARCHAR(20) DEFAULT 'ROLE_USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

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

CREATE TABLE IF NOT EXISTS budget_items (
    item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    budget_id BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL,
    item_name VARCHAR(150),
    estimated_amount DECIMAL(12,2) NOT NULL,
    notes TEXT,
    CONSTRAINT fk_budget_items_budget FOREIGN KEY (budget_id) REFERENCES budgets (budget_id) ON DELETE CASCADE
);

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

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_rooms_user ON rooms(user_id);
CREATE INDEX IF NOT EXISTS idx_designs_room ON designs(room_id);
CREATE INDEX IF NOT EXISTS idx_budgets_room ON budgets(room_id);
CREATE INDEX IF NOT EXISTS idx_projects_user ON projects(user_id);
CREATE INDEX IF NOT EXISTS idx_bookings_user ON bookings(user_id);
CREATE INDEX IF NOT EXISTS idx_professionals_service ON professionals(service_type);
CREATE INDEX IF NOT EXISTS idx_professionals_location ON professionals(location);
