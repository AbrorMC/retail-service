ALTER TABLE cart_items RENAME TO order_items;
ALTER TABLE order_items
    ALTER COLUMN cart_id DROP NOT NULL,
    ADD COLUMN order_id BIGINT,

    ADD CONSTRAINT fk_cart_items_on_order FOREIGN KEY (order_id)
    REFERENCES orders (id) ON DELETE CASCADE;