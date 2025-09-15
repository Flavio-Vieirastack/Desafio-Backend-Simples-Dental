CREATE TABLE tb_categories (
    id          BIGSERIAL    PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255)
);

-- Inserir categorias
INSERT INTO tb_categories (name, description) VALUES
('Eletrônicos', 'Produtos eletrônicos e gadgets'),
('Informática', 'Produtos para computadores e acessórios'),
('Smartphones', 'Telefones celulares e acessórios'),
('Eletrodomésticos', 'Aparelhos para casa'),
('Móveis', 'Móveis para escritório e casa');

CREATE TABLE tb_products (
    id          BIGSERIAL    PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    price DECIMAL(19, 2) NOT NULL CHECK (price > 0),
    status BOOLEAN NOT NULL DEFAULT true,
    code VARCHAR(50) UNIQUE,
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES tb_categories (id)
);

INSERT INTO tb_products (name, description, price, status, code, category_id) VALUES
('Smartphone XYZ', 'Smartphone com 8GB RAM e 128GB de armazenamento', 1299.99, true, 'PROD-001', (SELECT id FROM tb_categories WHERE name='Smartphones')),
('Notebook ABC', 'Notebook com processador i7, 16GB RAM e SSD 512GB', 3999.99, true, 'PROD-002', (SELECT id FROM tb_categories WHERE name='Informática')),
('Smart TV 50"', 'Smart TV LED 4K com 50 polegadas', 2499.99, true, 'PROD-003', (SELECT id FROM tb_categories WHERE name='Eletrônicos')),
('Geladeira Frost Free', 'Geladeira Duplex Frost Free 400L', 3299.99, true, 'PROD-004', (SELECT id FROM tb_categories WHERE name='Eletrodomésticos')),
('Mesa de Escritório', 'Mesa para escritório com gavetas', 499.99, true, 'PROD-005', (SELECT id FROM tb_categories WHERE name='Móveis')),
('Cadeira Ergonômica', 'Cadeira ergonômica para escritório', 899.99, true, 'PROD-006', (SELECT id FROM tb_categories WHERE name='Móveis')),
('Tablet Premium', 'Tablet com tela de 10 polegadas e 64GB', 1899.99, true, 'PROD-007', (SELECT id FROM tb_categories WHERE name='Eletrônicos')),
('Monitor 27"', 'Monitor LED Full HD 27 polegadas', 1199.99, true, 'PROD-008', (SELECT id FROM tb_categories WHERE name='Informática')),
('Fone de Ouvido Bluetooth', 'Fone de ouvido sem fio com cancelamento de ruído', 399.99, true, 'PROD-009', (SELECT id FROM tb_categories WHERE name='Eletrônicos')),
('Microondas 30L', 'Forno microondas com 30 litros e múltiplas funções', 599.99, true, 'PROD-010', (SELECT id FROM tb_categories WHERE name='Eletrodomésticos'));
