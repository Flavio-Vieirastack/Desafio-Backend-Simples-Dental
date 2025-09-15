CREATE TABLE tb_products_v2 (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    price       DECIMAL(19, 2) NOT NULL CHECK (price > 0),
    status      BOOLEAN NOT NULL DEFAULT TRUE,
    code        INT UNIQUE,
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_products_category_v2 FOREIGN KEY (category_id) REFERENCES tb_categories (id)
);

INSERT INTO tb_products_v2 (name, description, price, status, code, category_id) VALUES
('Smartphone XYZ', 'Smartphone com 8GB RAM e 128GB de armazenamento', 1299.99, TRUE, 1, (SELECT id FROM tb_categories WHERE name='Smartphones')),
('Notebook ABC', 'Notebook com processador i7, 16GB RAM e SSD 512GB', 3999.99, TRUE, 2, (SELECT id FROM tb_categories WHERE name='Informática')),
('Smart TV 50"', 'Smart TV LED 4K com 50 polegadas', 2499.99, TRUE, 3, (SELECT id FROM tb_categories WHERE name='Eletrônicos')),
('Geladeira Frost Free', 'Geladeira Duplex Frost Free 400L', 3299.99, TRUE, 4, (SELECT id FROM tb_categories WHERE name='Eletrodomésticos')),
('Mesa de Escritório', 'Mesa para escritório com gavetas', 499.99, TRUE, 5, (SELECT id FROM tb_categories WHERE name='Móveis')),
('Cadeira Ergonômica', 'Cadeira ergonômica para escritório', 899.99, TRUE, 6, (SELECT id FROM tb_categories WHERE name='Móveis')),
('Tablet Premium', 'Tablet com tela de 10 polegadas e 64GB', 1899.99, TRUE, 7, (SELECT id FROM tb_categories WHERE name='Eletrônicos')),
('Monitor 27"', 'Monitor LED Full HD 27 polegadas', 1199.99, TRUE, 8, (SELECT id FROM tb_categories WHERE name='Informática')),
('Fone de Ouvido Bluetooth', 'Fone de ouvido sem fio com cancelamento de ruído', 399.99, TRUE, 9, (SELECT id FROM tb_categories WHERE name='Eletrônicos')),
('Microondas 30L', 'Forno microondas com 30 litros e múltiplas funções', 599.99, TRUE, 10, (SELECT id FROM tb_categories WHERE name='Eletrodomésticos'));
