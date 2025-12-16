-- Incluye: Funciones de Fecha, Outer Joins y Uniones.

-- EXTRA 3: FUNCIONES DE FECHA (5 CONSULTAS)
-- Requisito: Usar funciones variadas de tratamiento de fechas.

-- 1. DAYNAME: ¿Qué día de la semana se estrenaron las películas?
SELECT titulo, fecha_lanzamiento, DAYNAME(fecha_lanzamiento) AS "Día Semana"
FROM pelicula;

-- 2. MONTHNAME: Películas estrenadas en verano (Junio, Julio, Agosto)
SELECT titulo, MONTHNAME(fecha_lanzamiento) AS "Mes Estreno"
FROM pelicula
WHERE MONTH(fecha_lanzamiento) IN (6, 7, 8);

-- 3. DATE_FORMAT: Mostrar la fecha en formato español (DD/MM/YYYY)
-- Muy útil para la interfaz de usuario final.
SELECT titulo, DATE_FORMAT(fecha_lanzamiento, '%d/%m/%Y') AS "Fecha Formateada"
FROM pelicula;

-- 4. DATEDIFF: ¿Cuántos días han pasado desde el estreno hasta hoy?
SELECT titulo, DATEDIFF(NOW(), fecha_lanzamiento) AS "Días desde estreno"
FROM pelicula;

-- 5. ADDDATE: Calcular cuándo caducaría una supuesta licencia (5 años después del estreno)
SELECT titulo, ADDDATE(fecha_lanzamiento, INTERVAL 5 YEAR) AS "Fecha Caducidad Licencia"
FROM pelicula;


-- -----------------------------------------------------------------------------
-- EXTRA 4: OUTER JOINS (LEFT / RIGHT) (5 CONSULTAS)
-- Requisito: Mostrar datos aunque no tengan relación (NULLs).
-- -----------------------------------------------------------------------------

-- 6. LEFT JOIN: Mostrar TODOS los directores, incluso los que no han dirigido ninguna peli
-- (Saldrán NULL en el título si no tienen peli)
SELECT d.nombre AS "Director", p.titulo AS "Película"
FROM director d
LEFT JOIN pelicula p ON d.id = p.id_director;

-- 7. LEFT JOIN: Mostrar TODOS los usuarios, incluso los que no han votado nunca
-- Útil para detectar usuarios inactivos.
SELECT u.nombre, v.puntuacion
FROM usuario u
LEFT JOIN valora v ON u.id = v.id_usuario
WHERE v.id_pelicula IS NULL;

-- 8. RIGHT JOIN: Listar todos los Géneros y sus películas (si las tienen)
-- Similar al LEFT pero desde el otro lado.
SELECT p.titulo, g.nombre AS "Género"
FROM pelicula p
RIGHT JOIN genero g ON p.id_genero = g.id;

-- 9. LEFT JOIN: Actores que no han actuado en ninguna película registrada
SELECT a.nombre
FROM actor a
LEFT JOIN actua ac ON a.id = ac.id_actor
WHERE ac.id_pelicula IS NULL;

-- 10. LEFT JOIN: Películas que no son secuela de nadie (Originales)
-- Usamos la reflexiva para ver las que tienen NULL en 'secuela_de'
SELECT p.titulo AS "Película Original"
FROM pelicula p
WHERE p.id_secuela_de IS NULL;


-- -----------------------------------------------------------------------------
-- EXTRA 5: UNIONES (UNION) (5 CONSULTAS)
-- Requisito: Combinar resultados de varias tablas en una sola lista.
-- -----------------------------------------------------------------------------

-- 11. Lista VIP: Nombres de Directores y Actores en una sola lista
SELECT nombre, 'Director' AS "Rol" FROM director
UNION
SELECT nombre, 'Actor' AS "Rol" FROM actor;

-- 12. Correos masivos: Emails de Usuarios y Webs de Directores
-- Lista para enviar spam o boletines.
SELECT email AS "Contacto" FROM usuario
UNION
SELECT web_oficial FROM director WHERE web_oficial IS NOT NULL;

-- 13. Catálogo Global: Títulos de Películas y Nombres de Géneros
SELECT titulo AS "Elemento" FROM pelicula
UNION
SELECT nombre FROM genero;

-- 14. Ranking Mixto: Películas con presupuesto > 100M y Actores con > 5 premios
-- Unimos dos criterios de "Éxito".
SELECT titulo AS "Nombre", 'Superproducción' AS "Tipo" FROM pelicula WHERE presupuesto > 100000000
UNION
SELECT nombre, 'Actor Laureado' FROM actor WHERE num_premios > 5;

-- 15. Fechas Importantes: Fecha de nacimiento de actores y fechas de estreno de pelis
SELECT nombre AS "Evento", fecha_nacimiento AS "Fecha" FROM actor
UNION
SELECT titulo, fecha_lanzamiento FROM pelicula
ORDER BY "Fecha" DESC;