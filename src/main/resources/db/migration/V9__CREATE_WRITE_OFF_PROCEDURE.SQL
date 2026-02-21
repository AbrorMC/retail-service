CREATE OR REPLACE PROCEDURE write_off_inventory_by_order(p_order_id BIGINT)
LANGUAGE plpgsql
AS $$
DECLARE
    r_item RECORD;
    v_current_stock NUMERIC;
BEGIN
    FOR r_item IN
        SELECT
            ri.ingredient_id,
            SUM(ri.quantity * oi.quantity) as total_required
        FROM order_items oi
        JOIN receipt_items ri ON oi.food_id = ri.food_id
        WHERE oi.order_id = p_order_id
          AND ri.is_active = TRUE
        GROUP BY ri.ingredient_id
    LOOP
        SELECT actual_stock INTO v_current_stock
        FROM inventories
        WHERE ingredient_id = r_item.ingredient_id
        ORDER BY created_at DESC, id DESC
        LIMIT 1
        FOR UPDATE;

        IF v_current_stock IS NULL OR v_current_stock < r_item.total_required THEN
            RAISE EXCEPTION 'Недостаточно ингредиента ID % (нужно всего %, в наличии %)',
                r_item.ingredient_id, r_item.total_required, COALESCE(v_current_stock, 0);
        END IF;

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