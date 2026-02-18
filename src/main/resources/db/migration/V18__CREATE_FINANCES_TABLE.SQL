CREATE TYPE financial_state_type AS ENUM (
   'INCOME', 'EXPENSE'
);

CREATE TABLE finances (
    id BIGSERIAL PRIMARY KEY,
    state financial_state_type NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_finances_amount CHECK(amount > 0)
);