CREATE TABLE inventories (
    id BIGSERIAL PRIMARY KEY,
    ingredient_id BIGINT NOT NULL,
    quantity NUMERIC(19,2) NOT NULL,

    CONSTRAINT chk_inventories_quantity CHECK(quantity > 0),

    CONSTRAINT fk_inventories_on_ingredient FOREIGN KEY (ingredient_id)
    REFERENCES ingredients(id) ON DELETE CASCADE
);

CREATE INDEX idx_inventories_ingredient_id ON inventories(ingredient_id);

INSERT INTO inventories (ingredient_id, quantity) VALUES
(1, 200.00),
(2, 10.00),
(3, 3000.00),
(4, 500.00),
(5, 200.00),
(6, 40.00),
(7, 1500.00);
