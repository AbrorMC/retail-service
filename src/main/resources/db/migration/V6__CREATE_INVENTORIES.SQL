CREATE TYPE inventory_transaction_type AS ENUM (
    'INCOMING', 'WRITE_OFF'
);

CREATE TABLE inventories (
    id BIGSERIAL PRIMARY KEY,
    ingredient_id BIGINT NOT NULL,
    type inventory_transaction_type NOT NULL DEFAULT 'WRITE_OFF',
    quantity NUMERIC(19,2) NOT NULL DEFAULT 0,
    actual_stock NUMERIC(19,2) NOT NULL DEFAULT 0,
    version INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_inventories_quantity CHECK(quantity > 0),
    CONSTRAINT chk_inventories_actual_stock CHECK(actual_stock >= 0),

    CONSTRAINT fk_inventories_on_ingredient FOREIGN KEY (ingredient_id)
    REFERENCES ingredients(id) ON DELETE CASCADE
);

CREATE INDEX idx_inventories_ingredient_id ON inventories(ingredient_id);

INSERT INTO inventories (ingredient_id, type, quantity, actual_stock) VALUES
(1, 'INCOMING', 50000.00, 50000.00),   -- Рис Лазер Премиум (50 кг)
(2, 'INCOMING', 30000.00, 30000.00),   -- Морковь желтая (30 кг)
(3, 'INCOMING', 20000.00, 20000.00),   -- Лук репчатый (20 кг)
(4, 'INCOMING', 40000.00, 40000.00),   -- Картофель свежий (40 кг)
(5, 'INCOMING', 25000.00, 25000.00),   -- Говядина (мякоть) (25 кг)
(6, 'INCOMING', 15000.00, 15000.00),   -- Баранина (корейка) (15 кг)
(7, 'INCOMING', 10000.00, 10000.00),   -- Куриное филе (10 кг)
(8, 'INCOMING', 5000.00, 5000.00),     -- Тесто для сомсы (5 кг)
(9, 'INCOMING', 100.00, 100.00),       -- Тесто для лаваша (100 шт)
(10, 'INCOMING', 50.00, 50.00),        -- Булочка для бургера (50 шт)
(11, 'INCOMING', 10000.00, 10000.00),  -- Масло хлопковое (10 л)
(12, 'INCOMING', 100.00, 100.00),      -- Сыр Чеддер (100 слайсов)
(13, 'INCOMING', 5000.00, 5000.00),    -- Соус чесночный (5 кг)
(14, 'INCOMING', 2000.00, 2000.00),    -- Кофе зерновой (2 кг)
(15, 'INCOMING', 20.00, 20.00);        -- Лимон свежий (20 шт)