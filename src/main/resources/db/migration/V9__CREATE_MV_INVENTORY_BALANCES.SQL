CREATE MATERIALIZED VIEW mv_inventory_balances AS
SELECT DISTINCT ON (ingredient_id)
	ingredient_id,
	actual_stock
FROM inventories
WHERE is_active = TRUE
ORDER BY ingredient_id, updated_at DESC, id DESC;

CREATE UNIQUE INDEX idx_mv_inventory_id ON mv_inventory_balances (ingredient_id);