CREATE TYPE inventory_transaction_type AS ENUM (
    'INCOMING', 'WRITE_OFF'
);

ALTER TABLE inventories
ADD COLUMN type inventory_transaction_type NOT NULL DEFAULT 'WRITE_OFF';