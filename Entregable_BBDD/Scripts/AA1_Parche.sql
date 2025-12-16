-- Script de actualización (Parche) para la AA1
-- Simulación de cambios pedidos por el cliente tras el lanzamiento

-- 1. Añadimos teléfono y ciudad al usuario (Obligatorios)
-- Ponemos valores por defecto para que no falle con los usuarios que ya existen
ALTER TABLE usuario ADD COLUMN telefono VARCHAR(15) NOT NULL DEFAULT '000000000';
ALTER TABLE usuario ADD COLUMN ciudad VARCHAR(50) NOT NULL DEFAULT 'Desconocida';

-- 2. Quitamos el color de la etiqueta de los géneros porque no se usa
ALTER TABLE genero DROP COLUMN color_etiqueta;

-- 3. Cambiamos el nivel de prestigio de Texto a Número
-- Primero convertimos los textos actuales (Oro, Plata...) a números
UPDATE usuario SET nivel_prestigio = '3' WHERE nivel_prestigio = 'Oro';
UPDATE usuario SET nivel_prestigio = '2' WHERE nivel_prestigio = 'Plata';
UPDATE usuario SET nivel_prestigio = '1' WHERE nivel_prestigio = 'Bronce';
-- Por si acaso queda alguno raro, lo ponemos a 0
UPDATE usuario SET nivel_prestigio = '0' WHERE nivel_prestigio NOT IN ('1', '2', '3');

-- Ahora ya podemos cambiar el tipo de la columna a Entero
ALTER TABLE usuario MODIFY nivel_prestigio INT DEFAULT 0;

-- 4. Hacemos obligatoria la nacionalidad del director
-- Antes de poner la restricción, nos aseguramos de que no haya nulos
UPDATE director SET nacionalidad = 'Desconocida' WHERE nacionalidad IS NULL;

-- Aplicamos el NOT NULL
ALTER TABLE director MODIFY nacionalidad VARCHAR(50) NOT NULL;