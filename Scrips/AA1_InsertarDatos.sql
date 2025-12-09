-- SCRIPT DE DATOS DE PRUEBA (MOCK DATA)
-- Diseñado para cubrir todos los casos de las consultas obligatorias

-- 1. INSERTAR GÉNEROS (5 registros)
INSERT INTO genero (nombre, descripcion, es_adultos, popularidad, fecha_creacion, publico_objetivo, color_etiqueta) VALUES 
('Ciencia Ficción', 'Futuro, espacio y tecnología', FALSE, 95, '1900-01-01', 'Adolescentes y Adultos', '#0000FF'),
('Drama', 'Conflictos humanos y emociones', FALSE, 80, '1900-01-01', 'Adultos', '#555555'),
('Terror', 'Miedo y sustos', TRUE, 70, '1980-05-20', 'Adultos', '#FF0000'),
('Comedia', 'Humor y risas', FALSE, 90, '1900-01-01', 'Todos los públicos', '#FFFF00'),
('Acción', 'Explosiones y peleas', FALSE, 85, '1970-03-15', 'Adolescentes', '#FF4500');

-- 2. INSERTAR DIRECTORES (5 registros)
INSERT INTO director (nombre, nacionalidad, num_premios, fecha_nacimiento, actividad, web_oficial, biografia) VALUES 
('Denis Villeneuve', 'Canadiense', 15, '1967-10-03', 'Activo', 'denisvilleneuve.com', 'Director conocido por su estilo visual único en sci-fi.'),
('Francis Ford Coppola', 'Estadounidense', 5, '1939-04-07', 'Retirado', NULL, 'Leyenda del cine, director de El Padrino.'),
('Christopher Nolan', 'Británico', 12, '1970-07-30', 'Activo', 'nolanfans.com', 'Conocido por Inception y Batman.'),
('Greta Gerwig', 'Estadounidense', 3, '1983-08-04', 'Activo', NULL, 'Directora y actriz, conocida por Barbie.'),
('Quentin Tarantino', 'Estadounidense', 8, '1963-03-27', 'Activo', 'tarantino.info', 'Famoso por sus diálogos y violencia estilizada.');

-- 3. INSERTAR ACTORES (10 registros)
INSERT INTO actor (nombre, fecha_nacimiento, nacionalidad, num_premios, actividad, foto_url, debut_anio) VALUES 
('Timothée Chalamet', '1995-12-27', 'Estadounidense', 2, 'Activo', 'url_timothee.jpg', 2008),
('Zendaya', '1996-09-01', 'Estadounidense', 4, 'Activo', 'url_zendaya.jpg', 2010),
('Al Pacino', '1940-04-25', 'Estadounidense', 9, 'Activo', 'url_alpacino.jpg', 1969),
('Marlon Brando', '1924-04-03', 'Estadounidense', 10, 'Retirado', 'url_brando.jpg', 1950),
('Leonardo DiCaprio', '1974-11-11', 'Estadounidense', 6, 'Activo', 'url_leo.jpg', 1991),
('Margot Robbie', '1990-07-02', 'Australiana', 3, 'Activo', 'url_margot.jpg', 2008),
('Robert De Niro', '1943-08-17', 'Estadounidense', 8, 'Activo', 'url_deniro.jpg', 1965),
('Florence Pugh', '1996-01-03', 'Británica', 1, 'Activo', 'url_florence.jpg', 2014),
('Cillian Murphy', '1976-05-25', 'Irlandés', 5, 'Activo', 'url_cillian.jpg', 1996),
('Brad Pitt', '1963-12-18', 'Estadounidense', 7, 'Activo', 'url_brad.jpg', 1987);

-- 4. INSERTAR USUARIOS (5 registros)
INSERT INTO usuario (nombre, email, contrasena, fecha_registro, nivel_prestigio, avatar_url, fecha_nacimiento) VALUES 
('SantiProfe', 'santi@cine.com', '123456', '2023-01-01', 'Oro', 'avatar1.png', '1980-01-01'),
('Cinefilo99', 'cinefilo@mail.com', 'pass123', '2024-02-15', 'Plata', 'avatar2.png', '1995-05-20'),
('NovatoPelis', 'newbie@mail.com', 'abcde', '2025-01-10', 'Bronce', 'avatar3.png', '2005-12-12'),
('CriticoFeroz', 'hater@mail.com', 'hate123', '2023-11-30', 'Plata', 'avatar4.png', '1990-07-07'),
('FanMarvel', 'marvel@mail.com', 'avengers', '2024-06-20', 'Bronce', 'avatar5.png', '2000-02-28');

-- 5. INSERTAR PELÍCULAS (12 registros - OJO AL ORDEN para las secuelas)
-- Primero insertamos las "originales" (Padres)
INSERT INTO pelicula (titulo, fecha_lanzamiento, duracion, presupuesto, cartel_url, sinopsis, es_mas_18, id_director, id_genero, id_secuela_de) VALUES 
('Dune: Parte Uno', '2021-10-22', 155, 165000000.00, 'cartel_dune1.jpg', 'Paul Atreides viaja a Arrakis...', FALSE, 1, 1, NULL),
('El Padrino', '1972-03-24', 175, 6000000.00, 'cartel_padrino.jpg', 'El patriarca de una dinastía criminal...', TRUE, 2, 2, NULL),
('Oppenheimer', '2023-07-21', 180, 100000000.00, 'cartel_oppen.jpg', 'Historia del creador de la bomba atómica', FALSE, 3, 2, NULL),
('Barbie', '2023-07-21', 114, 145000000.00, 'cartel_barbie.jpg', 'Barbie sufre una crisis existencial...', FALSE, 4, 4, NULL),
('Pulp Fiction', '1994-10-14', 154, 8000000.00, 'cartel_pulp.jpg', 'Historias cruzadas de criminales...', TRUE, 5, 5, NULL),
('Inception', '2010-07-16', 148, 160000000.00, 'cartel_inception.jpg', 'Un ladrón roba secretos de los sueños...', FALSE, 3, 1, NULL),
('Interstellar', '2014-11-07', 169, 165000000.00, 'cartel_interstellar.jpg', 'Viaje a través de un agujero de gusano...', FALSE, 3, 1, NULL);

-- Ahora insertamos las SECUELAS (Hijas) apuntando a las anteriores
-- ID 8 apunta a ID 1 (Dune 2 -> Dune 1)
INSERT INTO pelicula (titulo, fecha_lanzamiento, duracion, presupuesto, cartel_url, sinopsis, es_mas_18, id_director, id_genero, id_secuela_de) VALUES 
('Dune: Parte Dos', '2024-03-01', 166, 190000000.00, 'cartel_dune2.jpg', 'Paul Atreides se une a los Fremen...', FALSE, 1, 1, 1),
-- ID 9 apunta a ID 2 (Padrino 2 -> Padrino 1)
('El Padrino II', '1974-12-20', 202, 13000000.00, 'cartel_padrino2.jpg', 'Continúa la saga de los Corleone...', TRUE, 2, 2, 2);

-- Otras pelis más
INSERT INTO pelicula (titulo, fecha_lanzamiento, duracion, presupuesto, cartel_url, sinopsis, es_mas_18, id_director, id_genero, id_secuela_de) VALUES 
('Kill Bill: Vol 1', '2003-10-10', 111, 30000000.00, 'cartel_killbill.jpg', 'La novia busca venganza...', TRUE, 5, 5, NULL),
('Kill Bill: Vol 2', '2004-04-16', 137, 30000000.00, 'cartel_killbill2.jpg', 'La novia continúa su venganza...', TRUE, 5, 5, 10); -- Apunta a Kill Bill 1 (ID 10)

-- 6. INSERTAR ACTUA (Reparto) (Varios registros)
INSERT INTO actua (id_pelicula, id_actor, personaje, tipo_papel) VALUES 
(1, 1, 'Paul Atreides', 'Protagonista'), -- Dune 1 - Timothee
(1, 2, 'Chani', 'Secundario'),    -- Dune 1 - Zendaya
(8, 1, 'Paul Atreides', 'Protagonista'), -- Dune 2 - Timothee
(8, 2, 'Chani', 'Co-Protagonista'), -- Dune 2 - Zendaya
(8, 8, 'Princesa Irulan', 'Secundario'), -- Dune 2 - Florence
(2, 3, 'Michael Corleone', 'Secundario'), -- Padrino - Al Pacino
(2, 4, 'Vito Corleone', 'Protagonista'),  -- Padrino - Marlon Brando
(9, 3, 'Michael Corleone', 'Protagonista'), -- Padrino 2 - Al Pacino
(9, 7, 'Vito Corleone Joven', 'Protagonista'), -- Padrino 2 - De Niro
(3, 9, 'J. Robert Oppenheimer', 'Protagonista'), -- Oppenheimer - Cillian
(3, 8, 'Jean Tatlock', 'Secundario'), -- Oppenheimer - Florence
(4, 6, 'Barbie', 'Protagonista'), -- Barbie - Margot Robbie
(6, 5, 'Cobb', 'Protagonista'),   -- Inception - DiCaprio
(6, 9, 'Robert Fischer', 'Secundario'), -- Inception - Cillian
(5, 10, 'Cliff Booth', 'Protagonista'); -- Pulp Fiction (Invento para test) - Brad Pitt

-- 7. INSERTAR VALORA (Opiniones) (Varios registros)
INSERT INTO valora (id_pelicula, id_usuario, puntuacion, comentario) VALUES 
(1, 1, 9, 'Una obra maestra visual.'), -- Santi vota Dune
(1, 2, 10, 'Increíble adaptación.'),
(2, 3, 10, 'Un clásico absoluto.'),
(4, 4, 2, 'Demasiado rosa para mí.'), -- Hater vota Barbie
(4, 5, 8, 'Muy divertida y crítica.'),
(3, 1, 9, 'Nolan lo ha vuelto a hacer.'),
(8, 2, 10, 'Mejor que la primera parte.'),
(8, 4, 5, 'Se me hizo larga.');