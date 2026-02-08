SET search_path TO postgres;

TRUNCATE TABLE carts, cart_items RESTART IDENTITY CASCADE;

INSERT INTO carts (user_id, created_at, updated_at) VALUES
(101, NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days'), -- Корзина пользователя 101 (старая)
(102, NOW() - INTERVAL '1 hour', NOW() - INTERVAL '1 hour'), -- Корзина пользователя 102 (недавняя)
(103, NOW(), NOW());                                         -- Корзина пользователя 103 (новая)

INSERT INTO cart_items (cart_id, food_id, count) VALUES
-- Товары в корзине №1 (пользователь 101)
(1, 1, 2), -- 2 Кофе Американо
(1, 3, 1), -- 1 Круассан

-- Товары в корзине №2 (пользователь 102)
(2, 5, 1), -- 1 Сыр Пармезан
(2, 7, 5), -- 5 Яблок Голден
(2, 8, 2), -- 2 Стейка Рибай

-- Товары в корзине №3 (пользователь 103)
(3, 2, 3), -- 3 Апельсиновых сока
(3, 4, 1), -- 1 Чиабатта
(3, 6, 4); -- 4 Греческих йогурта