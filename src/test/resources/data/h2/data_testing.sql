INSERT INTO products (name, description, price, weight, category, in_catalog)
VALUES
  ('Pelota', 'Pelota de goma para niños', 9.99, 0.2, 'TOYS', true),
  ('Libro de aventuras', 'Una historia épica', 14.95, 0.5, 'BOOKS', true),
  ('Camiseta deportiva', 'Para actividades al aire libre', 19.99, 0.3, 'CLOTHES', true),
  ('Bicicleta infantil', 'Bici para niños de 4 a 6 años', 79.90, 5.0, 'SPORTS', false);

INSERT INTO inventory (product_id, stock, sales, last_sold_date)
VALUES
  (1, 50, 120, '2025-05-10'),
  (2, 30, 45, '2025-05-12'),
  (3, 0, 90, '2025-04-30'),
  (4, 5, 15, '2025-01-15');

INSERT INTO configuration (config_key, config_value)
VALUES
  ('stock_threshold', '10');

INSERT INTO history (user_id, product_id, timestamp)
VALUES
  (1, 1, '2025-05-01 10:15:00'),
  (1, 2, '2025-05-01 10:17:00'),
  (2, 3, '2025-04-29 14:00:00'),
  (1, 1, '2025-05-10 09:00:00');

