-- FUNCIONALIDAD EXTRA 1: VISTAS (VIEWS)
-- Objetivo: Simplificar consultas complejas guardándolas como 'tablas virtuales'.

-- 1. VISTA DE FICHA TÉCNICA
-- Para no tener que hacer los 3 JOINs cada vez que queremos info básica de una peli.
CREATE OR REPLACE VIEW vista_ficha_pelicula AS
SELECT 
    p.id AS id_pelicula,
    p.titulo,
    p.fecha_lanzamiento,
    d.nombre AS director,
    g.nombre AS genero,
    p.duracion
FROM pelicula p
JOIN director d ON p.id_director = d.id
JOIN genero g ON p.id_genero = g.id;

-- Prueba de uso:
-- SELECT * FROM vista_ficha_pelicula WHERE director = 'Denis Villeneuve';


-- 2. VISTA DE RANKING DE USUARIOS
-- Una vista rápida para ver quiénes son los usuarios más activos y su nivel.
CREATE OR REPLACE VIEW vista_ranking_usuarios AS
SELECT 
    u.nombre AS usuario, 
    u.nivel_prestigio,
    COUNT(v.id_pelicula) AS total_valoraciones
FROM usuario u
LEFT JOIN valora v ON u.id = v.id_usuario
GROUP BY u.id, u.nombre, u.nivel_prestigio
ORDER BY total_valoraciones DESC;

-- Prueba de uso:
-- SELECT * FROM vista_ranking_usuarios;