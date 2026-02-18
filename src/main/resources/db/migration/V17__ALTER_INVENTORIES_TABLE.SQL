ALTER TABLE inventories
ADD COLUMN actual_stock NUMERIC(19,2) NOT NULL DEFAULT 0,

ADD CONSTRAINT chk_inventories_actual_stock CHECK(actual_stock >= 0);

UPDATE inventories SET actual_stock = quantity;