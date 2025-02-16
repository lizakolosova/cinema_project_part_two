INSERT INTO movies (title, release_date, rating, genre, image) VALUES
                                                                  ('Monsters, Inc.', '2001-11-02', 8.1, 'ANIMATION', 'movie1.jpg'),
                                                                  ('Toy Story 4', '2019-06-21', 7.8, 'ANIMATION', 'movie2.jpg'),
                                                                  ('The Lion King', '2019-07-19', 6.8, 'ADVENTURE', 'movie3.jpg'),
                                                                  ('Wall-E', '2008-06-27', 8.0, 'ANIMATION', 'movie4.jpg');

INSERT INTO cinemas (name, address, capacity, image) VALUES
                                                        ('Cinema 1', '127 Kattenstraat', 200, 'cinema1.jpg'),
                                                        ('Cinema 2', '365 Pothoekstraat', 150, 'cinema2.jpg'),
                                                        ('Cinema 3', '398 Predikerinnenstraat', 300, 'cinema3.jpg'),
                                                        ('Cinema 4', '741 Bredabaan', 220, 'cinema4.jpg');

INSERT INTO cinema_screens (cinema_id, screen_number, screen_type, size) VALUES
                                                          (1, 1,'IMAX', 100),
                                                          (2, 1,'Regular', 50),
                                                          (1, 2,'Regular', 70),
                                                          (2, 4, 'Small', 40),
                                                          (3, 6,'IMAX', 100),
                                                          (3, 1,'Regular', 55),
                                                          (4, 7,'Regular', 60),
                                                          (4, 6, 'Small', 25);

INSERT INTO tickets (price, show_time, movie_id, format, cinema_id)
VALUES
    (15.00, '2025-02-22 17:00:00', 1,  '2D',1),
    (13.50, '2025-02-22 19:30:00', 2, '3D',2),
    (11.00, '2025-02-23 16:00:00', 3, '2D',3),
    (9.50, '2025-02-23 21:00:00', 4,  'IMAX',4),
    (10.50, '2025-02-24 13:00:00', 1,  '3D',1),
    (14.00, '2025-02-24 18:00:00', 2,  '2D',2),
    (8.00, '2025-02-25 14:30:00', 3,  '2D',3),
    (12.50, '2025-02-25 20:00:00', 4,  'IMAX',4),
    (7.50, '2025-02-26 12:00:00', 1,  '2D',1),
    (10.00, '2025-02-26 15:30:00', 2,  '3D',2),
    (11.50, '2025-02-27 19:00:00', 3,  '2D',3),
    (8.75, '2025-02-28 16:30:00', 1,  '3D',4),
    (10.25, '2025-03-01 14:00:00', 3,  '2D',1),
    (13.00, '2025-03-01 17:30:00', 4,  'IMAX',2);
