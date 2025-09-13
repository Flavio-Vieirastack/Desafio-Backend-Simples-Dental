CREATE TABLE tb_products_v2 (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    price       DECIMAL(19, 2) NOT NULL CHECK (price > 0),
    status      BOOLEAN NOT NULL DEFAULT TRUE,
    code        INT UNIQUE,
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_products_category_v2 FOREIGN KEY (category_id) REFERENCES categories (id)
);
