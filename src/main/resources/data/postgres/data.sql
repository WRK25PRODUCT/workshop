INSERT INTO products (name, description, price, weight, category, in_catalog, stock, threshold, total_sales) VALUES
('Pelota de fútbol', 'Pelota profesional tamaño 5', 29.99, 0.45, 'SPORTS', true, 40, 4, 150),
('Muñeca articulada', 'Muñeca con accesorios intercambiables', 24.99, 0.3, 'TOYS', true, 35, 3, 90),
('Libro de aventuras', 'Novela épica para jóvenes', 12.50, 0.5, 'BOOKS', true, 60, 5, 200),
('Caja de cereales', 'Cereales integrales con miel', 4.75, 0.8, 'MEAL', true, 100, 10, 75),
('Camiseta básica', 'Camiseta de algodón', 15.99, 0.2, 'CLOTHES', true, 80, 5, 180);

INSERT INTO promotions (start_date, end_date, discount, promotion_type) VALUES
('2025-06-01', '2025-06-15', 0.15, 'SEASON'),
('2025-07-01', '2025-07-31', 0.10, 'QUANTITY'),
('2025-08-15', '2025-08-31', 0.20, 'SEASON'),
('2025-06-01', '2025-06-30', 0.10, 'QUANTITY');

INSERT INTO promotion_season (id, name) VALUES
(1, 'Summer Sale'),
(3, 'Back to School');

INSERT INTO promotion_season_categories (promotion_season_id, category) VALUES
(1, 'CLOTHES'),
(1, 'SPORTS'),
(3, 'BOOKS');

INSERT INTO promotion_quantities (id, quantity, category) VALUES
(2, 2, 'TOYS'),
(4, 5, 'TOYS');

INSERT INTO history (user_id, product_id, timestamp) VALUES
('user01', 1, CURRENT_TIMESTAMP - INTERVAL '2 days'),
('user02', 2, CURRENT_TIMESTAMP - INTERVAL '1 day'),
('user01', 3, CURRENT_TIMESTAMP),
('user03', 1, CURRENT_TIMESTAMP - INTERVAL '3 days');
