CREATE TYPE inventory_transaction_type AS ENUM (
    'INCOMING', 'WRITE_OFF'
);

CREATE TABLE inventories (
    id BIGSERIAL PRIMARY KEY,
    ingredient_id BIGINT NOT NULL,
    type inventory_transaction_type NOT NULL DEFAULT 'WRITE_OFF',
    quantity NUMERIC(19,2) NOT NULL DEFAULT 0,
    actual_stock NUMERIC(19,2) NOT NULL DEFAULT 0,
    total_cost NUMERIC(19,2) NOT NULL DEFAULT 0,
    version INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_inventories_quantity CHECK(quantity > 0),
    CONSTRAINT chk_inventories_actual_stock CHECK(actual_stock >= 0),

    CONSTRAINT fk_inventories_on_ingredient FOREIGN KEY (ingredient_id)
    REFERENCES ingredients(id) ON DELETE CASCADE
);

CREATE INDEX idx_inventories_ingredient_id ON inventories(ingredient_id);

INSERT INTO inventories (ingredient_id, type, quantity, actual_stock, total_cost) VALUES
-- Овощи (цены за кг)
(1, 'INCOMING', 50000.00, 50000.00, 1250000.00), -- Рис Лазер (~25 000 сум/кг)
(2, 'INCOMING', 30000.00, 30000.00, 240000.00),  -- Морковь (~8 000 сум/кг)
(3, 'INCOMING', 20000.00, 20000.00, 100000.00),  -- Лук (~5 000 сум/кг)
(4, 'INCOMING', 40000.00, 40000.00, 240000.00),  -- Картофель (~6 000 сум/кг)

-- Мясо (цены за кг)
(5, 'INCOMING', 25000.00, 25000.00, 2250000.00), -- Говядина (~90 000 сум/кг)
(6, 'INCOMING', 15000.00, 15000.00, 1650000.00), -- Баранина (~110 000 сум/кг)

-- Тесто и заготовки (цены за кг/шт)
(7, 'INCOMING', 5000.00, 5000.00, 150000.00),    -- Тесто сомса (~30 000 сум/кг)
(8, 'INCOMING', 100.00, 100.00, 150000.00),      -- Тесто лаваш (~1 500 сум/шт)
(9, 'INCOMING', 50.00, 50.00, 125000.00),        -- Булочки (~2 500 сум/шт)

-- Прочее
(10, 'INCOMING', 10000.00, 10000.00, 180000.00), -- Масло (~18 000 сум/кг)
(11, 'INCOMING', 100.00, 100.00, 350000.00),     -- Сыр Чеддер (~3 500 сум/слайс)
(12, 'INCOMING', 5000.00, 5000.00, 200000.00),   -- Соус (~40 000 сум/кг)
(13, 'INCOMING', 2000.00, 2000.00, 440000.00),   -- Кофе (~220 000 сум/кг)
(14, 'INCOMING', 20.00, 20.00, 60000.00),        -- Лимон (~3 000 сум/шт)
(15, 'INCOMING', 20.00, 20.00, 100000.00);       -- Компот (набор сухофруктов на 20л)