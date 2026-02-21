-- 1. Создаем ENUM типы (если они еще не созданы)
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'transaction_type') THEN
        CREATE TYPE transaction_type AS ENUM ('PAYMENT', 'REFUND');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'payment_status_type') THEN
        CREATE TYPE payment_status_type AS ENUM ('CREATED', 'COMPLETED', 'REFUND', 'FAILED');
    END IF;
END $$;

-- 2. Создаем таблицу payments
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

    -- Поля из BaseEntity (обычно это они)
    is_active       BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 3. Индекс для быстрого поиска по reference_id (часто используется в логике)
CREATE INDEX IF NOT EXISTS idx_payments_reference_id ON payments(reference_id);