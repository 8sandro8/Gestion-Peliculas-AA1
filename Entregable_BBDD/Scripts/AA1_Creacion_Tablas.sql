-- SCRIPT SQL PARA CREAR LA BASE DE DATOS 'PROYECTO_PELICULAS'
-- Basado en el diseño aprobado y el estilo de la wiki bbdd.codeandcoke.com

-- 1. CREACIÓN DE TABLAS INDEPENDIENTES
-- Primero creamos las que no dependen de nadie para evitar errores.

CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    fecha_registro DATE,
    nivel_prestigio VARCHAR(50),
    avatar_url VARCHAR(255),
    fecha_nacimiento DATE
);

CREATE TABLE actor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE,
    nacionalidad VARCHAR(50),
    num_premios INT DEFAULT 0,
    actividad VARCHAR(255), -- Ejemplo: 'Activo', 'Retirado'
    foto_url VARCHAR(255),
    debut_anio INT
);

CREATE TABLE genero (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion TEXT,
    es_adultos BOOLEAN DEFAULT FALSE,
    popularidad INT DEFAULT 0,
    fecha_creacion DATE,
    publico_objetivo VARCHAR(100),
    color_etiqueta VARCHAR(20) -- Ejemplo: '#FF0000'
);

CREATE TABLE director (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    nacionalidad VARCHAR(50),
    num_premios INT DEFAULT 0,
    fecha_nacimiento DATE,
    actividad VARCHAR(255),
    web_oficial VARCHAR(255),
    biografia TEXT
);

-- 2. CREACIÓN DE LA TABLA PRINCIPAL (PELICULA)
-- Aquí añadimos las claves foráneas (FK) hacia Director y Género.

CREATE TABLE pelicula (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    fecha_lanzamiento DATE,
    duracion INT, -- en minutos
    presupuesto DECIMAL(15, 2), -- permite decimales para dinero
    cartel_url VARCHAR(255),
    sinopsis TEXT,
    es_mas_18 BOOLEAN DEFAULT FALSE,
    id_director INT,
    id_genero INT,
    id_secuela_de INT, -- Relación Reflexiva
    
    -- Definición de las Relaciones (Foreign Keys)
    CONSTRAINT fk_pelicula_director FOREIGN KEY (id_director) REFERENCES director(id),
    CONSTRAINT fk_pelicula_genero FOREIGN KEY (id_genero) REFERENCES genero(id),
    CONSTRAINT fk_pelicula_secuela FOREIGN KEY (id_secuela_de) REFERENCES pelicula(id)
);

-- 3. CREACIÓN DE TABLAS INTERMEDIAS (RELACIONES N:M)

-- Tabla para la relación Actor - Película
CREATE TABLE actua (
    id_pelicula INT,
    id_actor INT,
    personaje VARCHAR(100),
    tipo_papel VARCHAR(50), -- 'Protagonista', 'Secundario'...
    
    PRIMARY KEY (id_pelicula, id_actor), -- Clave compuesta
    CONSTRAINT fk_actua_pelicula FOREIGN KEY (id_pelicula) REFERENCES pelicula(id),
    CONSTRAINT fk_actua_actor FOREIGN KEY (id_actor) REFERENCES actor(id)
);

-- Tabla para la relación Usuario - Película (Valoraciones)
CREATE TABLE valora (
    id_pelicula INT,
    id_usuario INT,
    puntuacion INT CHECK (puntuacion BETWEEN 0 AND 10), -- Restricción simple (0-10)
    comentario TEXT,
    
    PRIMARY KEY (id_pelicula, id_usuario), -- Clave compuesta
    CONSTRAINT fk_valora_pelicula FOREIGN KEY (id_pelicula) REFERENCES pelicula(id),
    CONSTRAINT fk_valora_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);