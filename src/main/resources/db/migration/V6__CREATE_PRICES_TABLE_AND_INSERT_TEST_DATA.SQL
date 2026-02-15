
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

--SET search_path TO postgres;
--
--TRUNCATE TABLE prices RESTART IDENTITY CASCADE;

INSERT INTO prices (food_id, price, is_active, created_at, updated_at) VALUES
(1, 15000.00, true, NOW(), NOW()), -- Кофе Американо
(2, 12000.00, true, NOW(), NOW()), -- Апельсиновый сок
(3, 18000.00, true, NOW(), NOW()), -- Круассан с шоколадом
(4, 8000.00,  true, NOW(), NOW()), -- Чиабатта
(5, 45000.00, true, NOW(), NOW()), -- Сыр Пармезан
(6, 9500.00,  true, NOW(), NOW()), -- Йогурт Греческий
(7, 22000.00, true, NOW(), NOW()), -- Яблоки Голден
(8, 120000.00, true, NOW(), NOW()); -- Стейк Рибай