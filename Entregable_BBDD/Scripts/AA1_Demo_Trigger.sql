-- 1. LIMPIEZA: Borramos los votos anteriores del usuario 5 (FanMarvel) para evitar duplicados
DELETE FROM valora WHERE id_usuario = 5;

-- 2. RESET: Nos aseguramos de que su nivel vuelve a ser 1 (Bronce) para ver si sube
UPDATE usuario SET nivel_prestigio = 1 WHERE id = 5;

-- 3.Insertamos 5 votos nuevos de golpe
INSERT INTO valora (id_pelicula, id_usuario, puntuacion, comentario) VALUES 
(1, 5, 8, 'Test Trigger 1'),
(2, 5, 8, 'Test Trigger 2'),
(3, 5, 8, 'Test Trigger 3'),
(4, 5, 8, 'Test Trigger 4'),
(5, 5, 8, 'Test Trigger 5');

-- 4. VERIFICACIÓN: ¿Ha subido al nivel 2?
SELECT nombre, nivel_prestigio FROM usuario WHERE id = 5;