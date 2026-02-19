ALTER TABLE finances ADD COLUMN is_active BOOLEAN DEFAULT TRUE;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_enum WHERE enumtypid = 'order_status_type'::regtype AND enumlabel = 'COMPLETED') THEN
        ALTER TYPE order_status_type ADD VALUE 'COMPLETED';
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_enum WHERE enumtypid = 'order_status_type'::regtype AND enumlabel = 'CANCELLED') THEN
        ALTER TYPE order_status_type ADD VALUE 'CANCELLED';
    END IF;
END
$$;