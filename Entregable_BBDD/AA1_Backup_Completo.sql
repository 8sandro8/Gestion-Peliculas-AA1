/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.7.2-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: aa1
-- ------------------------------------------------------
-- Server version	12.0.2-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `actor`
--

DROP TABLE IF EXISTS `actor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `actor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `nacionalidad` varchar(50) DEFAULT NULL,
  `num_premios` int(11) DEFAULT 0,
  `actividad` varchar(255) DEFAULT NULL,
  `foto_url` varchar(255) DEFAULT NULL,
  `debut_anio` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actor`
--

LOCK TABLES `actor` WRITE;
/*!40000 ALTER TABLE `actor` DISABLE KEYS */;
INSERT INTO `actor` VALUES
(1,'Timothée Chalamet','1995-12-27','Estadounidense',2,'Activo','url_timothee.jpg',2008),
(2,'Zendaya','1996-09-01','Estadounidense',4,'Activo','url_zendaya.jpg',2010),
(3,'Al Pacino','1940-04-25','Estadounidense',9,'Activo','url_alpacino.jpg',1969),
(4,'Marlon Brando','1924-04-03','Estadounidense',10,'Retirado','url_brando.jpg',1950),
(5,'Leonardo DiCaprio','1974-11-11','Estadounidense',6,'Activo','url_leo.jpg',1991),
(6,'Margot Robbie','1990-07-02','Australiana',3,'Activo','url_margot.jpg',2008),
(7,'Robert De Niro','1943-08-17','Estadounidense',8,'Activo','url_deniro.jpg',1965),
(8,'Florence Pugh','1996-01-03','Británica',1,'Activo','url_florence.jpg',2014),
(9,'Cillian Murphy','1976-05-25','Irlandés',5,'Activo','url_cillian.jpg',1996),
(10,'Brad Pitt','1963-12-18','Estadounidense',7,'Activo','url_brad.jpg',1987),
(11,'Timothée Chalamet','1995-12-27','Estadounidense',2,'Activo','url_timothee.jpg',2008),
(12,'Zendaya','1996-09-01','Estadounidense',4,'Activo','url_zendaya.jpg',2010),
(13,'Al Pacino','1940-04-25','Estadounidense',9,'Activo','url_alpacino.jpg',1969),
(14,'Marlon Brando','1924-04-03','Estadounidense',10,'Retirado','url_brando.jpg',1950),
(15,'Leonardo DiCaprio','1974-11-11','Estadounidense',6,'Activo','url_leo.jpg',1991),
(16,'Margot Robbie','1990-07-02','Australiana',3,'Activo','url_margot.jpg',2008),
(17,'Robert De Niro','1943-08-17','Estadounidense',8,'Activo','url_deniro.jpg',1965),
(18,'Florence Pugh','1996-01-03','Británica',1,'Activo','url_florence.jpg',2014),
(19,'Cillian Murphy','1976-05-25','Irlandés',5,'Activo','url_cillian.jpg',1996),
(20,'Brad Pitt','1963-12-18','Estadounidense',7,'Activo','url_brad.jpg',1987);
/*!40000 ALTER TABLE `actor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `actua`
--

DROP TABLE IF EXISTS `actua`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `actua` (
  `id_pelicula` int(11) NOT NULL,
  `id_actor` int(11) NOT NULL,
  `personaje` varchar(100) DEFAULT NULL,
  `tipo_papel` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_pelicula`,`id_actor`),
  KEY `fk_actua_actor` (`id_actor`),
  CONSTRAINT `fk_actua_actor` FOREIGN KEY (`id_actor`) REFERENCES `actor` (`id`),
  CONSTRAINT `fk_actua_pelicula` FOREIGN KEY (`id_pelicula`) REFERENCES `pelicula` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actua`
--

LOCK TABLES `actua` WRITE;
/*!40000 ALTER TABLE `actua` DISABLE KEYS */;
INSERT INTO `actua` VALUES
(1,1,'Paul Atreides','Protagonista'),
(1,2,'Chani','Secundario'),
(2,3,'Michael Corleone','Secundario'),
(2,4,'Vito Corleone','Protagonista'),
(3,8,'Jean Tatlock','Secundario'),
(3,9,'J. Robert Oppenheimer','Protagonista'),
(4,6,'Barbie','Protagonista'),
(5,10,'Cliff Booth','Protagonista'),
(6,5,'Cobb','Protagonista'),
(6,9,'Robert Fischer','Secundario'),
(8,1,'Paul Atreides','Protagonista'),
(8,2,'Chani','Co-Protagonista'),
(8,8,'Princesa Irulan','Secundario'),
(9,3,'Michael Corleone','Protagonista'),
(9,7,'Vito Corleone Joven','Protagonista');
/*!40000 ALTER TABLE `actua` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `director`
--

DROP TABLE IF EXISTS `director`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `director` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `nacionalidad` varchar(50) DEFAULT NULL,
  `num_premios` int(11) DEFAULT 0,
  `fecha_nacimiento` date DEFAULT NULL,
  `actividad` varchar(255) DEFAULT NULL,
  `web_oficial` varchar(255) DEFAULT NULL,
  `biografia` text DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `director`
--

LOCK TABLES `director` WRITE;
/*!40000 ALTER TABLE `director` DISABLE KEYS */;
INSERT INTO `director` VALUES
(1,'Denis Villeneuve','Canadiense',15,'1967-10-03','Activo','denisvilleneuve.com','Director conocido por su estilo visual único en sci-fi.'),
(2,'Francis Ford Coppola','Estadounidense',5,'1939-04-07','Retirado',NULL,'Leyenda del cine, director de El Padrino.'),
(3,'Christopher Nolan','Británico',12,'1970-07-30','Activo','nolanfans.com','Conocido por Inception y Batman.'),
(4,'Greta Gerwig','Estadounidense',3,'1983-08-04','Activo',NULL,'Directora y actriz, conocida por Barbie.'),
(5,'Quentin Tarantino','Estadounidense',8,'1963-03-27','Activo','tarantino.info','Famoso por sus diálogos y violencia estilizada.'),
(6,'Denis Villeneuve','Canadiense',15,'1967-10-03','Activo','denisvilleneuve.com','Director conocido por su estilo visual único en sci-fi.'),
(7,'Francis Ford Coppola','Estadounidense',5,'1939-04-07','Retirado',NULL,'Leyenda del cine, director de El Padrino.'),
(8,'Christopher Nolan','Británico',12,'1970-07-30','Activo','nolanfans.com','Conocido por Inception y Batman.'),
(9,'Greta Gerwig','Estadounidense',3,'1983-08-04','Activo',NULL,'Directora y actriz, conocida por Barbie.'),
(10,'Quentin Tarantino','Estadounidense',8,'1963-03-27','Activo','tarantino.info','Famoso por sus diálogos y violencia estilizada.');
/*!40000 ALTER TABLE `director` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `genero`
--

DROP TABLE IF EXISTS `genero`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `genero` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `es_adultos` tinyint(1) DEFAULT 0,
  `popularidad` int(11) DEFAULT 0,
  `fecha_creacion` date DEFAULT NULL,
  `publico_objetivo` varchar(100) DEFAULT NULL,
  `color_etiqueta` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genero`
--

LOCK TABLES `genero` WRITE;
/*!40000 ALTER TABLE `genero` DISABLE KEYS */;
INSERT INTO `genero` VALUES
(1,'Ciencia Ficción','Futuro, espacio y tecnología',0,95,'1900-01-01','Adolescentes y Adultos','#0000FF'),
(2,'Drama','Conflictos humanos y emociones',0,80,'1900-01-01','Adultos','#555555'),
(3,'Terror','Miedo y sustos',1,70,'1980-05-20','Adultos','#FF0000'),
(4,'Comedia','Humor y risas',0,90,'1900-01-01','Todos los públicos','#FFFF00'),
(5,'Acción','Explosiones y peleas',0,85,'1970-03-15','Adolescentes','#FF4500'),
(6,'Ciencia Ficción','Futuro, espacio y tecnología',0,95,'1900-01-01','Adolescentes y Adultos','#0000FF'),
(7,'Drama','Conflictos humanos y emociones',0,80,'1900-01-01','Adultos','#555555'),
(8,'Terror','Miedo y sustos',1,70,'1980-05-20','Adultos','#FF0000'),
(9,'Comedia','Humor y risas',0,90,'1900-01-01','Todos los públicos','#FFFF00'),
(10,'Acción','Explosiones y peleas',0,85,'1970-03-15','Adolescentes','#FF4500');
/*!40000 ALTER TABLE `genero` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pelicula`
--

DROP TABLE IF EXISTS `pelicula`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `pelicula` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `titulo` varchar(150) NOT NULL,
  `fecha_lanzamiento` date DEFAULT NULL,
  `duracion` int(11) DEFAULT NULL,
  `presupuesto` decimal(15,2) DEFAULT NULL,
  `cartel_url` varchar(255) DEFAULT NULL,
  `sinopsis` text DEFAULT NULL,
  `es_mas_18` tinyint(1) DEFAULT 0,
  `id_director` int(11) DEFAULT NULL,
  `id_genero` int(11) DEFAULT NULL,
  `id_secuela_de` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_pelicula_director` (`id_director`),
  KEY `fk_pelicula_genero` (`id_genero`),
  KEY `fk_pelicula_secuela` (`id_secuela_de`),
  CONSTRAINT `fk_pelicula_director` FOREIGN KEY (`id_director`) REFERENCES `director` (`id`),
  CONSTRAINT `fk_pelicula_genero` FOREIGN KEY (`id_genero`) REFERENCES `genero` (`id`),
  CONSTRAINT `fk_pelicula_secuela` FOREIGN KEY (`id_secuela_de`) REFERENCES `pelicula` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pelicula`
--

LOCK TABLES `pelicula` WRITE;
/*!40000 ALTER TABLE `pelicula` DISABLE KEYS */;
INSERT INTO `pelicula` VALUES
(1,'Dune: Parte Uno','2021-10-22',155,165000000.00,'cartel_dune1.jpg','Paul Atreides viaja a Arrakis...',0,1,1,NULL),
(2,'El Padrino','1972-03-24',175,6000000.00,'cartel_padrino.jpg','El patriarca de una dinastía criminal...',1,2,2,NULL),
(3,'Oppenheimer','2023-07-21',180,100000000.00,'cartel_oppen.jpg','Historia del creador de la bomba atómica',0,3,2,NULL),
(4,'Barbie','2023-07-21',114,145000000.00,'cartel_barbie.jpg','Barbie sufre una crisis existencial...',0,4,4,NULL),
(5,'Pulp Fiction','1994-10-14',154,8000000.00,'cartel_pulp.jpg','Historias cruzadas de criminales...',1,5,5,NULL),
(6,'Inception','2010-07-16',148,160000000.00,'cartel_inception.jpg','Un ladrón roba secretos de los sueños...',0,3,1,NULL),
(7,'Interstellar','2014-11-07',169,165000000.00,'cartel_interstellar.jpg','Viaje a través de un agujero de gusano...',0,3,1,NULL),
(8,'Dune: Parte Dos','2024-03-01',166,190000000.00,'cartel_dune2.jpg','Paul Atreides se une a los Fremen...',0,1,1,1),
(9,'El Padrino II','1974-12-20',202,13000000.00,'cartel_padrino2.jpg','Continúa la saga de los Corleone...',1,2,2,2),
(10,'Kill Bill: Vol 1','2003-10-10',111,30000000.00,'cartel_killbill.jpg','La novia busca venganza...',1,5,5,NULL),
(11,'Kill Bill: Vol 2','2004-04-16',137,30000000.00,'cartel_killbill2.jpg','La novia continúa su venganza...',1,5,5,10);
/*!40000 ALTER TABLE `pelicula` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `fecha_registro` date DEFAULT NULL,
  `nivel_prestigio` varchar(50) DEFAULT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES
(1,'SantiProfe','santi@cine.com','123456','2023-01-01','Oro','avatar1.png','1980-01-01'),
(2,'Cinefilo99','cinefilo@mail.com','pass123','2024-02-15','Plata','avatar2.png','1995-05-20'),
(3,'NovatoPelis','newbie@mail.com','abcde','2025-01-10','Bronce','avatar3.png','2005-12-12'),
(4,'CriticoFeroz','hater@mail.com','hate123','2023-11-30','Plata','avatar4.png','1990-07-07'),
(5,'FanMarvel','marvel@mail.com','avengers','2024-06-20','2','avatar5.png','2000-02-28');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `valora`
--

DROP TABLE IF EXISTS `valora`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `valora` (
  `id_pelicula` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `puntuacion` int(11) DEFAULT NULL CHECK (`puntuacion` between 0 and 10),
  `comentario` text DEFAULT NULL,
  PRIMARY KEY (`id_pelicula`,`id_usuario`),
  KEY `fk_valora_usuario` (`id_usuario`),
  CONSTRAINT `fk_valora_pelicula` FOREIGN KEY (`id_pelicula`) REFERENCES `pelicula` (`id`),
  CONSTRAINT `fk_valora_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `valora`
--

LOCK TABLES `valora` WRITE;
/*!40000 ALTER TABLE `valora` DISABLE KEYS */;
INSERT INTO `valora` VALUES
(1,1,9,'Una obra maestra visual.'),
(1,2,10,'Increíble adaptación.'),
(2,3,10,'Un clásico absoluto.'),
(3,1,9,'Nolan lo ha vuelto a hacer.'),
(4,4,2,'Demasiado rosa para mí.'),
(8,2,10,'Mejor que la primera parte.'),
(8,4,5,'Se me hizo larga.');
/*!40000 ALTER TABLE `valora` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER auto_subir_nivel
AFTER INSERT ON valora
FOR EACH ROW
BEGIN
    -- 1. Declaramos una variable para guardar el número de votos
    DECLARE total_votos INT;

    -- 2. Contamos cuántas valoraciones tiene el usuario que acaba de votar
    SELECT COUNT(*) INTO total_votos 
    FROM valora 
    WHERE id_usuario = NEW.id_usuario;

    -- 3. Lógica de ascenso (Gamificación)
    -- Si tiene 10 o más, sube a Nivel 3 (Experto)
    IF total_votos >= 10 THEN
        UPDATE usuario SET nivel_prestigio = 3 WHERE id = NEW.id_usuario;
    
    -- Si tiene 5 o más, sube a Nivel 2 (Intermedio)
    ELSEIF total_votos >= 5 THEN
        UPDATE usuario SET nivel_prestigio = 2 WHERE id = NEW.id_usuario;
    END IF;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Temporary table structure for view `vista_ficha_pelicula`
--

DROP TABLE IF EXISTS `vista_ficha_pelicula`;
/*!50001 DROP VIEW IF EXISTS `vista_ficha_pelicula`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8mb4;
/*!50001 CREATE VIEW `vista_ficha_pelicula` AS SELECT
 1 AS `id_pelicula`,
  1 AS `titulo`,
  1 AS `fecha_lanzamiento`,
  1 AS `director`,
  1 AS `genero`,
  1 AS `duracion` */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `vista_ranking_usuarios`
--

DROP TABLE IF EXISTS `vista_ranking_usuarios`;
/*!50001 DROP VIEW IF EXISTS `vista_ranking_usuarios`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8mb4;
/*!50001 CREATE VIEW `vista_ranking_usuarios` AS SELECT
 1 AS `usuario`,
  1 AS `nivel_prestigio`,
  1 AS `total_valoraciones` */;
SET character_set_client = @saved_cs_client;

--
-- Dumping routines for database 'aa1'
--

--
-- Final view structure for view `vista_ficha_pelicula`
--

/*!50001 DROP VIEW IF EXISTS `vista_ficha_pelicula`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_uca1400_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vista_ficha_pelicula` AS select `p`.`id` AS `id_pelicula`,`p`.`titulo` AS `titulo`,`p`.`fecha_lanzamiento` AS `fecha_lanzamiento`,`d`.`nombre` AS `director`,`g`.`nombre` AS `genero`,`p`.`duracion` AS `duracion` from ((`pelicula` `p` join `director` `d` on(`p`.`id_director` = `d`.`id`)) join `genero` `g` on(`p`.`id_genero` = `g`.`id`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vista_ranking_usuarios`
--

/*!50001 DROP VIEW IF EXISTS `vista_ranking_usuarios`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_uca1400_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vista_ranking_usuarios` AS select `u`.`nombre` AS `usuario`,`u`.`nivel_prestigio` AS `nivel_prestigio`,count(`v`.`id_pelicula`) AS `total_valoraciones` from (`usuario` `u` left join `valora` `v` on(`u`.`id` = `v`.`id_usuario`)) group by `u`.`id`,`u`.`nombre`,`u`.`nivel_prestigio` order by count(`v`.`id_pelicula`) desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2025-12-02 23:23:49
