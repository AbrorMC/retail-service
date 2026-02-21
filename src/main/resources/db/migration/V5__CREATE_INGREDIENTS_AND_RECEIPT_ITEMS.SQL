CREATE TYPE measurement_unit_type AS ENUM (
   'PIECE', 'GRAM', 'LITRE'
);

CREATE TABLE ingredients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    unit measurement_unit_type,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE receipt_items (
    id BIGSERIAL PRIMARY KEY,
    food_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    quantity NUMERIC(19,2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,

    CONSTRAINT chk_receipt_item_quantity CHECK(quantity > 0),

    CONSTRAINT fk_receipt_items_on_food FOREIGN KEY (food_id)
    REFERENCES foods(id) ON DELETE CASCADE,

    CONSTRAINT fk_receipt_items_on_ingredient FOREIGN KEY (ingredient_id)
    REFERENCES ingredients(id) ON DELETE CASCADE
);

CREATE INDEX idx_receipt_items_food_id ON receipt_items(food_id);

INSERT INTO ingredients (name, unit) VALUES
-- Овощи и база
('Рис Лазер Премиум', 'GRAM'),
('Морковь желтая (Ташкентская)', 'GRAM'),
('Лук репчатый', 'GRAM'),
('Картофель свежий', 'GRAM'),
-- Мясо
('Говядина (мякоть)', 'GRAM'),
('Баранина (корейка)', 'GRAM'),
('Куриное филе', 'GRAM'),
-- Тесто и выпечка
('Тесто для сомсы (слоеное)', 'GRAM'),
('Тесто для лаваша', 'PIECE'),
('Булочка для бургера', 'PIECE'),
-- Прочее
('Масло хлопковое', 'GRAM'),
('Сыр Чеддер (слайсы)', 'PIECE'),
('Соус чесночный', 'GRAM'),
('Кофе зерновой (Blend)', 'GRAM'),
('Лимон свежий', 'PIECE');

-- 1. Плов Ташкентский (Food 1)
INSERT INTO receipt_items (food_id, ingredient_id, quantity) VALUES
(1, 1, 250.00), -- Рис
(1, 5, 150.00), -- Говядина
(1, 2, 200.00); -- Морковь

-- 2. Сомса с говядиной (Food 2)
INSERT INTO receipt_items (food_id, ingredient_id, quantity) VALUES
(2, 7, 80.00),  -- Тесто
(2, 5, 60.00);  -- Говядина

-- 3. Лагман жареный (Food 3)
INSERT INTO receipt_items (food_id, ingredient_id, quantity) VALUES
(3, 5, 120.00), -- Говядина
(3, 3, 40.00);  -- Лук

-- 4. Шурпа из баранины (Food 4)
INSERT INTO receipt_items (food_id, ingredient_id, quantity) VALUES
(4, 6, 200.00), -- Баранина
(4, 4, 100.00); -- Картофель

-- 5. Double Cheese Burger (Food 5)
INSERT INTO receipt_items (food_id, ingredient_id, quantity) VALUES
(5, 9, 1.00),   -- Булочка
(5, 11, 2.00),  -- Сыр
(5, 5, 200.00); -- Говядина

-- 6. Классический Лаваш с говядиной (Food 6)
INSERT INTO receipt_items (food_id, ingredient_id, quantity) VALUES
(6, 8, 1.00),   -- Тесто лаваша
(6, 5, 100.00), -- Говядина
(6, 12, 40.00); -- Соус

-- 7. Картофель фри (Food 7)
INSERT INTO receipt_items (food_id, ingredient_id, quantity) VALUES
(7, 4, 150.00), -- Картофель
(7, 10, 20.00); -- Масло

-- 8. Чай с лимоном (Food 8)
INSERT INTO receipt_items (food_id, ingredient_id, quantity) VALUES
(8, 14, 0.50);  -- Лимон

-- 10. Кофе Американо (Food 10)
INSERT INTO receipt_items (food_id, ingredient_id, quantity) VALUES
(10, 13, 18.00); -- Кофе зерно