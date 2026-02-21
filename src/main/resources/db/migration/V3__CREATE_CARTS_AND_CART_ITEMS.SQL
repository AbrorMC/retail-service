CREATE TABLE carts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    total_amount NUMERIC(19,2) NOT NULL DEFAULT 0,
    item_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    cart_id BIGINT,
    order_id BIGINT,
    food_id BIGINT NOT NULL,
    count INTEGER NOT NULL,
    price NUMERIC(19,2) NOT NULL,

    CONSTRAINT fk_cart_items_on_order FOREIGN KEY (order_id)
    REFERENCES orders (id) ON DELETE CASCADE,

    CONSTRAINT chk_cart_item_count CHECK(count > 0),

    CONSTRAINT fk_cart_items_on_cart FOREIGN KEY (cart_id)
    REFERENCES carts (id) ON DELETE CASCADE,

    CONSTRAINT fk_cart_items_on_food FOREIGN KEY (food_id)
    REFERENCES foods (id)
);

CREATE INDEX idx_cart_items_cart_id ON cart_items(cart_id);

INSERT INTO carts (user_id) VALUES (1), (2), (3), (4), (5);