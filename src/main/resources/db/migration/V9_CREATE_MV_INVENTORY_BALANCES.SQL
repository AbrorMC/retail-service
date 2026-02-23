CREATE MATERIALIZED VIEW IF NOT EXISTS mv_inventory_balances AS
SELECT DISTINCT ON (ingredient_id)
	ingredient_id,
	actual_stock
FROM postgres.inventories
WHERE is_active = TRUE
ORDER BY ingredient_id, updated_at DESC, id DESC;

CREATE UNIQUE INDEX IF NOT EXISTS idx_mv_inventory_id ON mv_inventory_balances (ingredient_id);