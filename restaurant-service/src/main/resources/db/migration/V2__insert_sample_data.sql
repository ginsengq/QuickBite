-- insert sample categories
INSERT INTO categories (name, description, image_url, created_at, updated_at) VALUES
('Pizza', 'Delicious pizzas with various toppings', 'https://example.com/pizza.jpg', NOW(), NOW()),
('Burgers', 'Juicy burgers and sandwiches', 'https://example.com/burger.jpg', NOW(), NOW()),
('Sushi', 'Fresh Japanese sushi and rolls', 'https://example.com/sushi.jpg', NOW(), NOW()),
('Salads', 'Healthy and fresh salads', 'https://example.com/salad.jpg', NOW(), NOW()),
('Desserts', 'Sweet treats and desserts', 'https://example.com/dessert.jpg', NOW(), NOW()),
('Drinks', 'Beverages and refreshments', 'https://example.com/drinks.jpg', NOW(), NOW()),
('Pasta', 'Italian pasta dishes', 'https://example.com/pasta.jpg', NOW(), NOW()),
('Asian', 'Asian cuisine specialties', 'https://example.com/asian.jpg', NOW(), NOW());

-- insert sample restaurants
INSERT INTO restaurants (name, description, address, phone_number, is_active, rating, created_at, updated_at) VALUES
('Pizza Paradise', 'Best pizzas in town with authentic Italian recipes', '123 Main Street, City Center', '+1234567890', TRUE, 4.5, NOW(), NOW()),
('Burger King Palace', 'Premium burgers made with fresh ingredients', '456 Oak Avenue, Downtown', '+1234567891', TRUE, 4.2, NOW(), NOW()),
('Sushi Master', 'Traditional Japanese sushi experience', '789 Pine Road, East District', '+1234567892', TRUE, 4.8, NOW(), NOW()),
('Healthy Bites', 'Fresh salads and healthy meals', '321 Elm Street, North Side', '+1234567893', TRUE, 4.0, NOW(), NOW());

-- insert sample menu items for Pizza Paradise (restaurant_id = 1)
INSERT INTO menu_items (name, description, price, image_url, is_available, restaurant_id, created_at, updated_at) VALUES
('Margherita Pizza', 'Classic pizza with tomato sauce, mozzarella, and basil', 1200, 'https://example.com/margherita.jpg', TRUE, 1, NOW(), NOW()),
('Pepperoni Pizza', 'Spicy pepperoni with extra cheese', 1500, 'https://example.com/pepperoni.jpg', TRUE, 1, NOW(), NOW()),
('Vegetarian Pizza', 'Fresh vegetables with cheese', 1400, 'https://example.com/vegetarian.jpg', TRUE, 1, NOW(), NOW());

-- insert sample menu items for Burger King Palace (restaurant_id = 2)
INSERT INTO menu_items (name, description, price, image_url, is_available, restaurant_id, created_at, updated_at) VALUES
('Classic Burger', 'Beef patty with lettuce, tomato, and special sauce', 800, 'https://example.com/classic-burger.jpg', TRUE, 2, NOW(), NOW()),
('Cheese Burger', 'Double cheese with beef patty', 1000, 'https://example.com/cheese-burger.jpg', TRUE, 2, NOW(), NOW()),
('Chicken Burger', 'Grilled chicken breast burger', 900, 'https://example.com/chicken-burger.jpg', TRUE, 2, NOW(), NOW());

-- insert sample menu items for Sushi Master (restaurant_id = 3)
INSERT INTO menu_items (name, description, price, image_url, is_available, restaurant_id, created_at, updated_at) VALUES
('California Roll', 'Crab, avocado, and cucumber roll', 1100, 'https://example.com/california-roll.jpg', TRUE, 3, NOW(), NOW()),
('Salmon Nigiri', 'Fresh salmon over rice', 1300, 'https://example.com/salmon-nigiri.jpg', TRUE, 3, NOW(), NOW()),
('Tuna Sashimi', 'Sliced raw tuna', 1600, 'https://example.com/tuna-sashimi.jpg', TRUE, 3, NOW(), NOW());

-- link menu items to categories
-- Pizza Paradise items -> Pizza category (id = 1)
INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES
(1, 1), (2, 1), (3, 1);

-- Burger King Palace items -> Burgers category (id = 2)
INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES
(4, 2), (5, 2), (6, 2);

-- Sushi Master items -> Sushi category (id = 3)
INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES
(7, 3), (8, 3), (9, 3);
