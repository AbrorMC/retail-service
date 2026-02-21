CREATE SCHEMA IF NOT EXISTS postgres;

CREATE TABLE categories (

    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT NOT NULL,

    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE foods (

    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category_id BIGINT NOT NULL,

    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_food_category
        FOREIGN KEY (category_id)
        REFERENCES categories (id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_foods_category_id ON foods(category_id);

INSERT INTO categories (name, description) VALUES
('Национальная кухня', 'Традиционные узбекские блюда: плов, сомса, лагман'),
('Фастфуд', 'Бургеры, хот-доги и популярные закуски'),
('Напитки', 'Горячие и холодные напитки, соки и чай');

INSERT INTO foods (name, category_id) VALUES
('Плов Ташкентский (Чайханский)', 1),
('Сомса с говядиной (запеченная в тандыре)', 1),
('Лагман жареный (Гуйру)', 1),
('Шурпа из баранины', 1),
('Double Cheese Burger', 2),
('Классический Лаваш с говядиной', 2),
('Картофель фри с соусом', 2),
('Чай черный с лимоном и медом', 3),
('Компот домашний из сухофруктов', 3),
('Кофе Американо 200мл', 3);