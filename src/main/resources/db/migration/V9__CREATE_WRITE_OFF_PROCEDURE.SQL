CREATE TYPE transaction_type AS ENUM (
    'PAYMENT', 'REFUND'
);

CREATE TYPE payment_status_type AS ENUM (
    'CREATED', 'COMPLETED', 'REFUND', 'FAILED'
);

CREATE TABLE IF NOT EXISTS payments (
    id              BIGSERIAL PRIMARY KEY,
    reference_id    BIGINT NOT NULL,
    type            transaction_type NOT NULL,
    status          payment_status_type,
    amount          NUMERIC(19, 2) NOT NULL CHECK (amount > 0),
    currency        VARCHAR(3) NOT NULL, -- Код валюты (UZS, USD)
    sender_name     VARCHAR(255) NOT NULL,
    sender_token    VARCHAR(255) NOT NULL,
    receiver_name   VARCHAR(255) NOT NULL,
    receiver_token  VARCHAR(255) NOT NULL,

    is_active       BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_payments_reference_id ON payments(reference_id);

CREATE OR REPLACE PROCEDURE sp_deduct_inventory_by_order(p_order_id BIGINT)
LANGUAGE plpgsql
AS $$
DECLARE
    r_item RECORD;
    v_current_stock NUMERIC;
BEGIN
    -- Проходим по всем блюдам в заказе (учитывая их количество)
    FOR r_item IN
        SELECT ri.ingredient_id, (ri.quantity * oi.quantity) as total_required
        FROM order_items oi
        JOIN receipt_items ri ON oi.food_id = ri.food_id
        WHERE oi.order_id = p_order_id
          AND ri.is_active = TRUE
    LOOP
        -- Блокируем строку ингредиента для предотвращения Race Condition
        SELECT actual_stock INTO v_current_stock
        FROM inventories
        WHERE ingredient_id = r_item.ingredient_id
        ORDER BY created_at DESC, id DESC
        LIMIT 1
        FOR UPDATE;

        -- Проверка на наличие
        IF v_current_stock IS NULL OR v_current_stock < r_item.total_required THEN
            RAISE EXCEPTION 'Недостаточно ингредиента ID % (нужно %, в наличии %)',
                r_item.ingredient_id, r_item.total_required, COALESCE(v_current_stock, 0);
        END IF;

        -- Запись транзакции
        INSERT INTO inventories (
            ingredient_id, type, quantity, actual_stock
        ) VALUES (
            r_item.ingredient_id,
            'WRITE_OFF',
            r_item.total_required,
            v_current_stock - r_item.total_required
        );
    END LOOP;
END;
$$;