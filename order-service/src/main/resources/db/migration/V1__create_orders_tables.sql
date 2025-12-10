CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        restaurant_id BIGINT NOT NULL,
                        status VARCHAR(32) NOT NULL,
                        total_price BIGINT NOT NULL,
                        created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                        updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE order_items (
                             id BIGSERIAL PRIMARY KEY,
                             order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
                             menu_item_id BIGINT NOT NULL,
                             quantity INT NOT NULL,
                             price BIGINT NOT NULL
);