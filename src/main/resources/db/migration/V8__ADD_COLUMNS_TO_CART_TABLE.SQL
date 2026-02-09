ALTER TABLE carts
    ADD COLUMN "total_amount" NUMERIC(19,2),
    ADD COLUMN "item_count" INTEGER;