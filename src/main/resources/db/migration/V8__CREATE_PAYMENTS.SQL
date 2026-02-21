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