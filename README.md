# 🎬 Gestión de Películas - Proyecto Integrado AA1

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-FF0000?style=for-the-badge&logo=oracle&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

> **Alumno:** Sandro López Díaz
> **Ciclo:** Desarrollo de Aplicaciones Multiplataforma (DAM)
> **Curso:** 2025-2026

## 📄 Descripción
Este proyecto es una aplicación de escritorio robusta para la gestión integral de una base de datos de cine. Ha sido desarrollada implementando una arquitectura profesional por capas (**MVC** + **DAO**) y simulando un entorno de desarrollo real con control de versiones.

El sistema permite administrar películas, actores, directores y sus relaciones, ofreciendo una experiencia de usuario moderna y persistencia de datos relacional.

---

## 🚀 Características Técnicas

### 🛠️ Stack Tecnológico
* **Lenguaje:** Java (JDK 21)
* **Interfaz Gráfica:** JavaFX (Diseño modular con FXML)
* **Base de Datos:** MariaDB (Conector JDBC)
* **Gestión de Dependencias:** Apache Maven
* **Control de Versiones:** Git & GitHub (Rama `master` como principal)

### 🏗️ Arquitectura
* **Patrón MVC:** Separación estricta entre Lógica (Modelos), Interfaz (Vistas FXML) y Controladores.
* **Patrón DAO:** Capa de acceso a datos para desacoplar la lógica de negocio de las consultas SQL.
* **Multihilo:** Uso de hilos secundarios para tareas pesadas (Splash Screen, cargas masivas).

---

## ✨ Funcionalidades Principales

### 📦 Gestión de Datos (CRUD)
* Alta, Baja y Modificación de **Películas** y **Actores**.
* Gestión de relaciones N:M (Reparto de actores en películas).
* Buscadores con filtrado en tiempo real.
* Validación de formularios con feedback visual.

### ⭐ Extras Implementados (High Level)
Para superar los requisitos básicos, se han añadido las siguientes funcionalidades avanzadas:

1.  **🌍 Internacionalización (i18n):**
    * Soporte completo **Español / Inglés**.
    * Cambio de idioma en caliente sin reiniciar la app.
2.  **🖼️ Persistencia de Imágenes:**
    * Sistema de gestión de archivos locales (`/imagenes`).
    * Carga y visualización de pósters y fotos de actores.
3.  **📊 Dashboard de Estadísticas:**
    * Panel visual con gráficas (PieChart y BarChart) para analizar géneros y datos de la BBDD.
4.  **💾 Exportación CSV:**
    * Generación de copias de seguridad de los listados en formato `.csv`.
5.  **🚀 Splash Screen:**
    * Pantalla de carga inicial animada.

---

## 🗄️ Base de Datos (SQL)

La lógica de datos se apoya en un diseño relacional complejo y optimizado.

* **Estructura:** 7 Entidades (`Pelicula`, `Actor`, `Director`, `Genero`, `Usuario`...).
* **Programación en BBDD:**
    * **Triggers:** Auditoría de cambios y cálculos automáticos.
    * **Vistas:** Simplificación de consultas complejas.
    * **Procedimientos Almacenados:** Lógica de negocio encapsulada.
    * **Script de Parche:** Incluido para actualizaciones de estructura.

> 📂 **Nota:** Todos los scripts SQL, diagramas E-R y documentación de BBDD se encuentran en la carpeta: [`/Entregable_BBDD`](./Entregable_BBDD)

---

## 🔧 Instalación y Despliegue

1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/8sandro8/Gestion-Peliculas-AA1.git](https://github.com/8sandro8/Gestion-Peliculas-AA1.git)
    ```
2.  **Base de Datos:**
    * Importar el script `AA1_Backup_Completo.sql` (ubicado en `Entregable_BBDD`) en tu servidor MariaDB.
    * Configurar la conexión en el archivo `ConexionBBDD.java`.
3.  **Ejecutar:**
    * Abrir el proyecto en IntelliJ IDEA.
    * Sincronizar dependencias Maven.
    * Ejecutar la clase `App.java` o `Launcher.java`.

---

Copyright © 2025 - Sandro López Díaz