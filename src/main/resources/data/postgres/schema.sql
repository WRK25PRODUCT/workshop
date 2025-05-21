DROP TABLE IF EXISTS history;
DROP TABLE IF EXISTS inventory;
DROP TABLE IF EXISTS configuration;
DROP TABLE IF EXISTS products;

CREATE TABLE products (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price NUMERIC(10, 2) NOT NULL,
    weight DOUBLE PRECISION NOT NULL,
    category VARCHAR(20) NOT NULL,
    in_catalog BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id)
);

CREATE TABLE inventory (
    product_id BIGINT PRIMARY KEY,
    stock INT DEFAULT 0 CHECK (stock >= 0) NOT NULL,
    sales INT DEFAULT 0 NOT NULL CHECK (sales >= 0),
    last_sold_date TIMESTAMP,
    CONSTRAINT fk_inventory_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);


CREATE TABLE configuration (
    id SERIAL PRIMARY KEY,
    config_key VARCHAR(100) UNIQUE NOT NULL,
    config_value VARCHAR(255) NOT NULL,
    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE history (
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    PRIMARY KEY (user_id, product_id, timestamp),
    CONSTRAINT fk_history_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
        ON DELETE CASCADE
);
