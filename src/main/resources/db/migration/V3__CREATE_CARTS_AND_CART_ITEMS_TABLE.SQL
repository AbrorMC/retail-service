
CREATE TABLE carts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE cart_items (
    id BIGSERIAL PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    food_id BIGINT NOT NULL,
    count INTEGER NOT NULL,

    CONSTRAINT chk_cart_item_count CHECK(count > 0),

    CONSTRAINT fk_cart_items_on_cart FOREIGN KEY (cart_id)
    REFERENCES carts (id) ON DELETE CASCADE,

    CONSTRAINT fk_cart_items_on_food FOREIGN KEY (food_id)
    REFERENCES foods (id)
);

CREATE INDEX idx_cart_items_cart_id ON cart_items(cart_id);
