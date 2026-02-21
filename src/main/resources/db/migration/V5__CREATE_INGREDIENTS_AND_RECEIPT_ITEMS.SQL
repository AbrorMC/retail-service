CREATE TYPE measurement_unit_type AS ENUM (
   'PIECE', 'GRAM', 'LITRE'
);

CREATE TABLE ingredients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    unit measurement_unit_type
);

CREATE TABLE receipt_items (
    id BIGSERIAL PRIMARY KEY,
    food_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    quantity NUMERIC(19,2) NOT NULL,

    CONSTRAINT chk_receipt_item_quantity CHECK(quantity > 0),

    CONSTRAINT fk_receipt_items_on_food FOREIGN KEY (food_id)
    REFERENCES foods(id) ON DELETE CASCADE,

    CONSTRAINT fk_receipt_items_on_ingredient FOREIGN KEY (ingredient_id)
    REFERENCES ingredients(id) ON DELETE CASCADE
);

CREATE INDEX idx_receipt_items_food_id ON receipt_items(food_id);

INSERT INTO ingredients (name, unit) VALUES
('Кофейные зерна', 'GRAM'),
('Вода очищенная', 'LITRE'),
('Апельсины', 'GRAM'),
('Мука пшеничная', 'GRAM'),
('Шоколадная начинка', 'GRAM'),
('Молоко коровье', 'LITRE'),
('Говядина (отруб)', 'GRAM');

-- Кофе Американо (food_id: 1)
INSERT INTO receipt_items (food_id, ingredient_id, quantity) VALUES
(1, 1, 18.00), -- 18 грамм кофе
(1, 2, 0.20);  -- 0.2 литра воды

-- Апельсиновый сок (food_id: 2)
INSERT INTO receipt_items (food_id, ingredient_id, quantity) VALUES
(2, 3, 500.00); -- 500 грамм апельсинов на порцию

-- Круассан с шоколадом (food_id: 3)
INSERT INTO receipt_items (food_id, ingredient_id, quantity) VALUES
(3, 4, 60.00),  -- 60 грамм муки
(3, 5, 20.00);  -- 20 грамм шоколада

-- Сыр Пармезан (food_id: 5)
INSERT INTO receipt_items (food_id, ingredient_id, quantity) VALUES
(5, 6, 10.00);  -- Условно 10 литров молока на 1 кг сыра

-- Стейк Рибай (food_id: 8)
INSERT INTO receipt_items (food_id, ingredient_id, quantity) VALUES
(8, 7, 350.00); -- 350 грамм мяса