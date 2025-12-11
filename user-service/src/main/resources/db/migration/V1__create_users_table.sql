-- create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(500),
    role VARCHAR(20) NOT NULL CHECK (role IN ('USER', 'ADMIN')),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- create indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_active ON users(active);

-- insert sample users
INSERT INTO users (email, first_name, last_name, phone, address, role, active, created_at, updated_at)
VALUES 
    ('admin@quickbite.com', 'Admin', 'User', '+77001234567', 'Almaty, Kazakhstan', 'ADMIN', TRUE, NOW(), NOW()),
    ('john.doe@example.com', 'John', 'Doe', '+77001234568', 'Astana, Kazakhstan', 'USER', TRUE, NOW(), NOW()),
    ('jane.smith@example.com', 'Jane', 'Smith', '+77001234569', 'Shymkent, Kazakhstan', 'USER', TRUE, NOW(), NOW());
