# 🎬 Gestión de Películas - Proyecto Integrado AA1

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-FF0000?style=for-the-badge&logo=oracle&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

> **Alumno:** Sandro López Díaz
> **Ciclo:** Desarrollo de Aplicaciones Multiplataforma (DAM)
> **Curso:** 2025-2026

## 📄 Descripción
Este proyecto es una aplicación de escritorio robusta para la gestión integral de una base de datos de cine. Ha sido desarrollada implementando una arquitectura profesional por capas (**MVC** + **DAO**), priorizando la integridad referencial y simulando un entorno de desarrollo real.

El sistema permite administrar el ciclo de vida completo de películas, así como la gestión independiente de actores y directores, ofreciendo una experiencia de usuario moderna y fluida.

---

## 🚀 Características Técnicas

### 🛠️ Stack Tecnológico
* **Lenguaje:** Java (JDK 21)
* **Interfaz Gráfica:** JavaFX (Diseño modular con FXML y CSS)
* **Base de Datos:** MariaDB (Conector JDBC)
* **Gestión de Dependencias:** Apache Maven
* **Control de Versiones:** Git & GitHub

### 🏗️ Arquitectura
* **Patrón MVC:** Separación estricta entre Lógica (Modelo), Interfaz (Vista) y Controladores.
* **Patrón DAO:** Capa de abstracción para persistencia de datos y desacoplamiento de SQL.
* **Lógica Relacional:** Implementación correcta de relaciones 1:N y N:M mediante tablas intermedias.

---

## ✨ Funcionalidades Principales

### 📦 Gestión de Datos (CRUD)
* **Gestión de Películas:** Creación y edición con asignación de Director (FK) y validaciones.
* **Gestión de Directores:** Módulo independiente para alta y mantenimiento de directores.
* **Gestión de Actores:** Base de datos de actores con foto y datos biográficos.
* **Gestión de Reparto (N:M):** Asignación de actores a películas especificando personaje y rol.
* **Buscadores:** Filtrado dinámico en tiempo real en todas las tablas.

### ⭐ Extras Implementados
Para superar los requisitos básicos, se han añadido las siguientes funcionalidades avanzadas:

1.  **🌍 Internacionalización (i18n):**
    * Soporte completo **Español / Inglés** con cambio en caliente.
2.  **🖼️ Gestión Multimedia:**
    * Sistema de carga y persistencia local de imágenes (Pósters y Fotos de perfil).
3.  **📊 Dashboard de Estadísticas:**
    * Panel visual con gráficas dinámicas para analizar la distribución de géneros y volumetría de datos.
4.  **💾 Exportación de Datos:**
    * Funcionalidad para exportar listados a formato `.csv`.
5.  **🚀 Splash Screen:**
    * Pantalla de carga inicial para mejorar la experiencia de usuario.

---

## 🗄️ Base de Datos (SQL)

El diseño de la base de datos se ha refactorizado para garantizar la máxima integridad de los datos.

* **Estructura:** Modelo relacional normalizado (Tablas intermedias con ID propio).
* **Programación en BBDD:**
    * **Triggers:** Control de integridad de datos (ej: validación de fechas de nacimiento y duración positiva).
    * **Vistas:** Consultas predefinidas para rankings y catálogos complejos.
    * **Foreign Keys:** Restricciones de integridad referencial estrictas.

> 📂 **Nota:** Todos los scripts SQL y diagramas se encuentran en la carpeta: [`/Entregable_BBDD`](./Entregable_BBDD)

---

## 🔧 Instalación y Despliegue

1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/8sandro8/Gestion-Peliculas-AA1.git](https://github.com/8sandro8/Gestion-Peliculas-AA1.git)
    ```
2.  **Base de Datos:**
    * Ejecutar el script SQL proporcionado en `Entregable_BBDD` en tu servidor MariaDB para crear la estructura y cargar los datos iniciales.
    * Verificar la configuración en `ConexionBBDD.java`.
3.  **Ejecutar:**
    * Abrir el proyecto en IntelliJ IDEA.
    * Sincronizar dependencias Maven.
    * Ejecutar la clase `App.java`.

---

Copyright © 2026 - Sandro López Díaz