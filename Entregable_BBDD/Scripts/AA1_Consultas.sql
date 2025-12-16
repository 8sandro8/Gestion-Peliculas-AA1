-- Script de consultas para la AA1
-- Proyecto: Gestión de Películas

-- Para las dos consultas extras
-- "Dame todas las pelis de Ciencia Ficción" (sin hacer joins manuales)
SELECT titulo, director, duracion
FROM vista_ficha_pelicula
WHERE genero = 'Ciencia Ficción';

-- "Dime los 3 usuarios más activos"
SELECT usuario, total_valoraciones
FROM vista_ranking_usuarios
LIMIT 3;

-- 1. Listado simple de películas y sus directores ordenados
SELECT p.titulo AS "Película", d.nombre AS "Director"
FROM pelicula p
JOIN director d ON p.id_director = d.id
ORDER BY d.nombre ASC;

-- 2. Buscamos películas de un género concreto (Ciencia Ficción)
SELECT p.titulo, p.fecha_lanzamiento
FROM pelicula p
JOIN genero g ON p.id_genero = g.id
WHERE g.nombre = 'Ciencia Ficción';

-- 3. Ficha completa: Peli, Director y Género (3 tablas)
SELECT p.titulo, d.nombre AS "Director", g.nombre AS "Género"
FROM pelicula p
JOIN director d ON p.id_director = d.id
JOIN genero g ON p.id_genero = g.id;

-- 4. Ver las valoraciones de los usuarios
SELECT u.nombre AS "Usuario", p.titulo AS "Película", v.puntuacion
FROM valora v
JOIN usuario u ON v.id_usuario = u.id
JOIN pelicula p ON v.id_pelicula = p.id
ORDER BY v.puntuacion DESC;

-- 5. Consulta completa: Actor, Personaje, Película y Género (4 tablas)
SELECT a.nombre AS "Actor", ac.personaje, p.titulo, g.nombre AS "Género"
FROM actor a
JOIN actua ac ON a.id = ac.id_actor
JOIN pelicula p ON ac.id_pelicula = p.id
JOIN genero g ON p.id_genero = g.id;

-- 6. Usuarios que han votado pelis de Christopher Nolan
SELECT u.nombre, p.titulo, v.puntuacion
FROM usuario u
JOIN valora v ON u.id = v.id_usuario
JOIN pelicula p ON v.id_pelicula = p.id
JOIN director d ON p.id_director = d.id
WHERE d.nombre = 'Christopher Nolan';

-- 7. Contar cuántas pelis tiene cada director
SELECT d.nombre, COUNT(p.id) AS "Total Películas"
FROM director d
LEFT JOIN pelicula p ON d.id = p.id_director
GROUP BY d.id, d.nombre
ORDER BY COUNT(p.id) DESC;

-- 8. Nota media de las películas
SELECT p.titulo, ROUND(AVG(v.puntuacion), 2) AS "Nota Media"
FROM pelicula p
JOIN valora v ON p.id = v.id_pelicula
GROUP BY p.id, p.titulo;

-- 9. Suma de presupuesto por género
SELECT g.nombre, SUM(p.presupuesto) AS "Inversión Total"
FROM genero g
JOIN pelicula p ON g.id = p.id_genero
GROUP BY g.nombre;

-- 10. Película con el presupuesto más alto (MAX)
SELECT titulo, presupuesto
FROM pelicula
WHERE presupuesto = (SELECT MAX(presupuesto) FROM pelicula);

-- 11. Cuántos actores salen en cada peli
SELECT p.titulo, COUNT(ac.id_actor) AS "Num Actores"
FROM pelicula p
JOIN actua ac ON p.id = ac.id_pelicula
GROUP BY p.id, p.titulo;

-- 12. Pelis que duran más que la media (Subconsulta)
SELECT titulo, duracion
FROM pelicula
WHERE duracion > (SELECT AVG(duracion) FROM pelicula);

-- 13. Actores que han hecho Drama (CORREGIDA)
-- Usamos IN en lugar de = por si hay géneros duplicados
SELECT nombre
FROM actor
WHERE id IN (
    SELECT id_actor 
    FROM actua 
    WHERE id_pelicula IN (
        SELECT id FROM pelicula WHERE id_genero IN (SELECT id FROM genero WHERE nombre = 'Drama')
    )
);

-- 14. Directores con superproducciones (+100 millones)
SELECT nombre 
FROM director 
WHERE id IN (SELECT id_director FROM pelicula WHERE presupuesto > 100000000);

-- 15. Pelis sin votos todavía (NOT IN)
SELECT titulo 
FROM pelicula 
WHERE id NOT IN (SELECT id_pelicula FROM valora);

-- 16. Usuarios con más prestigio que 'NovatoPelis'
SELECT nombre, nivel_prestigio 
FROM usuario 
WHERE nivel_prestigio > (SELECT nivel_prestigio FROM usuario WHERE nombre = 'NovatoPelis');

-- 17. Antigüedad de las películas (Funciones de fecha)
SELECT titulo, (YEAR(NOW()) - YEAR(fecha_lanzamiento)) AS "Años de antigüedad"
FROM pelicula
ORDER BY fecha_lanzamiento;

-- 18. Crear frase descriptiva (Funciones de cadena)
SELECT CONCAT('La película ', UPPER(titulo), ' es de ', (SELECT nombre FROM director WHERE id = pelicula.id_director)) AS "Info"
FROM pelicula;

-- 19. Actores veteranos (más de 20 años desde su debut)
SELECT nombre, debut_anio AS "Año Debut"
FROM actor
WHERE (YEAR(NOW()) - debut_anio) > 20;

-- 20. RELACIÓN REFLEXIVA: Ver secuelas y originales
SELECT 
    hija.titulo AS "Secuela",
    'es continuación de' AS "Relación",
    padre.titulo AS "Original"
FROM pelicula hija
JOIN pelicula padre ON hija.id_secuela_de = padre.id;