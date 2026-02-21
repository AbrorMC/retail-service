
CREATE TABLE prices (
    id BIGSERIAL PRIMARY KEY,
    food_id BIGINT NOT NULL,
    price NUMERIC(19,2) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_price CHECK(price > 0),

    CONSTRAINT fk_prices_on_food FOREIGN KEY (food_id)
    REFERENCES foods (id)
);

CREATE INDEX idx_prices_food_id ON prices(food_id);

INSERT INTO prices (food_id, price) VALUES
(1, 45000.00),  -- Плов Ташкентский (Чайханский)
(2, 12000.00),  -- Сомса с говядиной
(3, 35000.00),  -- Лагман жареный (Гуйру)
(4, 32000.00),  -- Шурпа из баранины
(5, 55000.00),  -- Double Cheese Burger
(6, 28000.00),  -- Классический Лаваш с говядиной
(7, 15000.00),  -- Картофель фри с соусом
(8, 10000.00),  -- Чай черный с лимоном и медом
(9, 12000.00),  -- Компот домашний из сухофруктов
(10, 22000.00); -- Кофе Американо 200мл