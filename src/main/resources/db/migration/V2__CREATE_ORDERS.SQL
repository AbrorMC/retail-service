CREATE TYPE order_status_type AS ENUM (
    'CREATED', 'PAID', 'COMPLETED', 'CANCELLED'
);

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    external_id UUID NOT NULL DEFAULT gen_random_uuid(),
    user_id BIGINT NOT NULL,
    item_count INTEGER,
    total_price NUMERIC(19,2),
    status order_status_type,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_orders_external_id UNIQUE (external_id)
);
