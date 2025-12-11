-- create restaurants table
CREATE TABLE restaurants (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    address VARCHAR(500) NOT NULL,
    phone_number VARCHAR(20),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    rating DOUBLE PRECISION DEFAULT 0.0,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

-- create categories table
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    image_url VARCHAR(500),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

-- create menu_items table
CREATE TABLE menu_items (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price BIGINT NOT NULL,
    image_url VARCHAR(500),
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    restaurant_id BIGINT NOT NULL REFERENCES restaurants(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

-- create menu_item_categories junction table
CREATE TABLE menu_item_categories (
    menu_item_id BIGINT NOT NULL REFERENCES menu_items(id) ON DELETE CASCADE,
    category_id BIGINT NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
    PRIMARY KEY (menu_item_id, category_id)
);

-- create indexes
CREATE INDEX idx_restaurants_is_active ON restaurants(is_active);
CREATE INDEX idx_restaurants_rating ON restaurants(rating);
CREATE INDEX idx_menu_items_restaurant_id ON menu_items(restaurant_id);
CREATE INDEX idx_menu_items_is_available ON menu_items(is_available);
