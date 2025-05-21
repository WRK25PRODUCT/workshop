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

INSERT INTO promotions (start_date, end_date, discount, promotion_type)
 VALUES
   ('2025-05-01', '2025-05-15', 0.20, 'QUANTITY'),
   ('2025-05-10', '2025-05-20', 0.15, 'QUANTITY'),
   ('2025-06-01', '2025-06-30', 0.05, 'QUANTITY'),
   ('2025-11-01', '2025-11-30', 0.20, 'SEASON'),
   ('2025-12-01', '2025-12-25', 0.15, 'SEASON'),
   ('2025-02-01', '2025-02-14', 0.05, 'SEASON'),
   ('2025-06-01', '2025-06-30', 0.05, 'SEASON');

INSERT INTO promotion_quantities (id, quantity, category, promotion_id)
VALUES
  (1, 6, 'TOYS', 1),
  (2, 2, 'BOOKS', 2),
  (3, 3, 'SPORTS', 3),
  (4, 4, 'MEAL', 4),
  (5, 5, 'CLOTHES', 5),
  (6, 0, 'OTHERS', 6);

INSERT INTO promotion_season (id, name)
VALUES
  (1, 'Black Friday'),
  (2, 'Christmas'),
  (3, 'Valentine’s Day'),
  (4, 'Summer');

INSERT INTO promotion_season_categories (promotion_season_id, category)
VALUES
  (1, 'TOYS'),
  (1, 'CLOTHES'),
  (2, 'BOOKS'),
  (2, 'TOYS'),
  (3, 'MEAL'),
  (3, 'SPORTS'),
  (4, 'SPORTS');
