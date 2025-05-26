-- MySQL dump 10.13  Distrib 8.0.41, for Linux (x86_64)
--
-- Host: localhost    Database: clinica_odontologica
-- ------------------------------------------------------
-- Server version	8.0.41-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Administrativo`
--

DROP TABLE IF EXISTS `Administrativo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Administrativo` (
  `usuario_id` int NOT NULL,
  `departamento` varchar(50) NOT NULL,
  PRIMARY KEY (`usuario_id`),
  CONSTRAINT `Administrativo_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `Usuario` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Administrativo`
--

LOCK TABLES `Administrativo` WRITE;
/*!40000 ALTER TABLE `Administrativo` DISABLE KEYS */;
INSERT INTO `Administrativo` VALUES (1,'Administración'),(4,'Administración');
/*!40000 ALTER TABLE `Administrativo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DiaSemana`
--

DROP TABLE IF EXISTS `DiaSemana`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DiaSemana` (
  `nombre` varchar(10) NOT NULL,
  PRIMARY KEY (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DiaSemana`
--

LOCK TABLES `DiaSemana` WRITE;
/*!40000 ALTER TABLE `DiaSemana` DISABLE KEYS */;
INSERT INTO `DiaSemana` VALUES ('JUEVES'),('LUNES'),('MARTES'),('MIERCOLES'),('VIERNES');
/*!40000 ALTER TABLE `DiaSemana` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Especialidad`
--

DROP TABLE IF EXISTS `Especialidad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Especialidad` (
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Especialidad`
--

LOCK TABLES `Especialidad` WRITE;
/*!40000 ALTER TABLE `Especialidad` DISABLE KEYS */;
INSERT INTO `Especialidad` VALUES ('CIRUGIA'),('ENDODONCIA'),('ODONTOPEDIATRIA'),('ORTODONCIA'),('PERIODONCIA');
/*!40000 ALTER TABLE `Especialidad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EstadoVisita`
--

DROP TABLE IF EXISTS `EstadoVisita`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EstadoVisita` (
  `nombre` varchar(20) NOT NULL,
  PRIMARY KEY (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EstadoVisita`
--

LOCK TABLES `EstadoVisita` WRITE;
/*!40000 ALTER TABLE `EstadoVisita` DISABLE KEYS */;
INSERT INTO `EstadoVisita` VALUES ('COMPLETADA'),('PROGRAMADA');
/*!40000 ALTER TABLE `EstadoVisita` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Horario`
--

DROP TABLE IF EXISTS `Horario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Horario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `odontologo_id` int NOT NULL,
  `dia` varchar(10) NOT NULL,
  `diaSemana` enum('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO') NOT NULL,
  `disponible` bit(1) NOT NULL,
  `horaFin` time(6) NOT NULL,
  `horaInicio` time(6) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `dia` (`dia`),
  KEY `FKx3ocg57cx62wq9h6cwbv1tjp` (`odontologo_id`),
  CONSTRAINT `FKx3ocg57cx62wq9h6cwbv1tjp` FOREIGN KEY (`odontologo_id`) REFERENCES `Usuario` (`id`),
  CONSTRAINT `Horario_ibfk_1` FOREIGN KEY (`odontologo_id`) REFERENCES `Odontologo` (`usuario_id`) ON DELETE CASCADE,
  CONSTRAINT `Horario_ibfk_2` FOREIGN KEY (`dia`) REFERENCES `DiaSemana` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Horario`
--

LOCK TABLES `Horario` WRITE;
/*!40000 ALTER TABLE `Horario` DISABLE KEYS */;
INSERT INTO `Horario` VALUES (2,5,'MIERCOLES','MIERCOLES',_binary '\0','09:00:00.000000','08:00:00.000000'),(3,5,'LUNES','LUNES',_binary '','00:00:00.000000','00:00:00.000000'),(4,5,'MIERCOLES','MIERCOLES',_binary '','09:00:00.000000','08:00:00.000000'),(5,5,'JUEVES','JUEVES',_binary '','09:00:00.000000','08:00:00.000000'),(6,5,'LUNES','LUNES',_binary '','00:00:00.000000','00:00:00.000000');
/*!40000 ALTER TABLE `Horario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Odontologo`
--

DROP TABLE IF EXISTS `Odontologo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Odontologo` (
  `usuario_id` int NOT NULL,
  `matricula` varchar(50) NOT NULL,
  `especialidad` varchar(50) NOT NULL,
  `activo` bit(1) NOT NULL,
  PRIMARY KEY (`usuario_id`),
  UNIQUE KEY `matricula` (`matricula`),
  KEY `especialidad` (`especialidad`),
  CONSTRAINT `Odontologo_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `Usuario` (`id`) ON DELETE CASCADE,
  CONSTRAINT `Odontologo_ibfk_2` FOREIGN KEY (`especialidad`) REFERENCES `Especialidad` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Odontologo`
--

LOCK TABLES `Odontologo` WRITE;
/*!40000 ALTER TABLE `Odontologo` DISABLE KEYS */;
INSERT INTO `Odontologo` VALUES (5,'12G5','Periodoncia',_binary '\0');
/*!40000 ALTER TABLE `Odontologo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Paciente`
--

DROP TABLE IF EXISTS `Paciente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Paciente` (
  `dni` varchar(20) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `fecha_nacimiento` date NOT NULL,
  `telefono` varchar(20) NOT NULL,
  `obra_social` varchar(50) DEFAULT NULL,
  `mutua` varchar(50) DEFAULT NULL,
  `tipo_pago` varchar(20) NOT NULL,
  PRIMARY KEY (`dni`),
  KEY `tipo_pago` (`tipo_pago`),
  CONSTRAINT `Paciente_ibfk_1` FOREIGN KEY (`tipo_pago`) REFERENCES `TipoPago` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Paciente`
--

LOCK TABLES `Paciente` WRITE;
/*!40000 ALTER TABLE `Paciente` DISABLE KEYS */;
INSERT INTO `Paciente` VALUES ('53833277Y','Dani','Jiménez Parreño','2003-12-30','123456789',NULL,'','PARTICULAR'),('53833278F','Dabit','Jimenez Parreño','2003-12-30','123456789',NULL,'wq','MUTUA'),('53833278Y','Dani','sdfghjk','2003-12-30','123456789','sderfgthynj','','PARTICULAR');
/*!40000 ALTER TABLE `Paciente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Parentesco`
--

DROP TABLE IF EXISTS `Parentesco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Parentesco` (
  `nombre` varchar(10) NOT NULL,
  PRIMARY KEY (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Parentesco`
--

LOCK TABLES `Parentesco` WRITE;
/*!40000 ALTER TABLE `Parentesco` DISABLE KEYS */;
INSERT INTO `Parentesco` VALUES ('MADRE'),('PADRE'),('TUTOR');
/*!40000 ALTER TABLE `Parentesco` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Responsable`
--

DROP TABLE IF EXISTS `Responsable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Responsable` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `dni` varchar(20) NOT NULL,
  `telefono` varchar(20) NOT NULL,
  `parentesco` varchar(10) NOT NULL,
  `paciente_dni` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `dni` (`dni`),
  KEY `parentesco` (`parentesco`),
  KEY `paciente_dni` (`paciente_dni`),
  CONSTRAINT `Responsable_ibfk_1` FOREIGN KEY (`parentesco`) REFERENCES `Parentesco` (`nombre`),
  CONSTRAINT `Responsable_ibfk_2` FOREIGN KEY (`paciente_dni`) REFERENCES `Paciente` (`dni`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Responsable`
--

LOCK TABLES `Responsable` WRITE;
/*!40000 ALTER TABLE `Responsable` DISABLE KEYS */;
/*!40000 ALTER TABLE `Responsable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TipoPago`
--

DROP TABLE IF EXISTS `TipoPago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TipoPago` (
  `nombre` varchar(20) NOT NULL,
  PRIMARY KEY (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TipoPago`
--

LOCK TABLES `TipoPago` WRITE;
/*!40000 ALTER TABLE `TipoPago` DISABLE KEYS */;
INSERT INTO `TipoPago` VALUES ('MUTUA'),('PARTICULAR');
/*!40000 ALTER TABLE `TipoPago` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Tratamiento`
--

DROP TABLE IF EXISTS `Tratamiento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Tratamiento` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text,
  `precio` double NOT NULL,
  `duracionMinutos` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Tratamiento`
--

LOCK TABLES `Tratamiento` WRITE;
/*!40000 ALTER TABLE `Tratamiento` DISABLE KEYS */;
/*!40000 ALTER TABLE `Tratamiento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Usuario`
--

DROP TABLE IF EXISTS `Usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Usuario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `DTYPE` varchar(31) NOT NULL,
  `tipo` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Usuario`
--

LOCK TABLES `Usuario` WRITE;
/*!40000 ALTER TABLE `Usuario` DISABLE KEYS */;
INSERT INTO `Usuario` VALUES (1,'test','$2a$10$oRYOLIXQ5cCcjUvuBZPLnOZQSRMc5E3qSWmF7UU7VIZvRDW8YC1iO','test@test.com','Administrativo',''),(4,'dani','$2a$10$vwM0RtuaTdlcYvO28U6BZOTrsprf/UyEZx8hFpOp3PshhC.Ev2E0G','dani@gmail.com','Administrativo',''),(5,'neil','$2a$10$4EQZNlbzT.WdHdgff1uTM.mcgellnc1MNxKqu4GbBAI0XWc8l6Qtu','neil@gmail.com','Odontologo','');
/*!40000 ALTER TABLE `Usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Visita`
--

DROP TABLE IF EXISTS `Visita`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Visita` (
  `id` int NOT NULL AUTO_INCREMENT,
  `paciente_dni` varchar(20) NOT NULL,
  `odontologo_id` int NOT NULL,
  `tratamiento_id` int DEFAULT NULL,
  `fecha_hora` datetime NOT NULL,
  `motivo` varchar(255) NOT NULL,
  `observaciones` text,
  `estado` varchar(20) NOT NULL DEFAULT 'PROGRAMADA',
  PRIMARY KEY (`id`),
  KEY `paciente_dni` (`paciente_dni`),
  KEY `tratamiento_id` (`tratamiento_id`),
  KEY `estado` (`estado`),
  KEY `FK11qwq03rcs1m640b9q2pei0la` (`odontologo_id`),
  CONSTRAINT `FK11qwq03rcs1m640b9q2pei0la` FOREIGN KEY (`odontologo_id`) REFERENCES `Usuario` (`id`),
  CONSTRAINT `Visita_ibfk_1` FOREIGN KEY (`paciente_dni`) REFERENCES `Paciente` (`dni`),
  CONSTRAINT `Visita_ibfk_2` FOREIGN KEY (`odontologo_id`) REFERENCES `Odontologo` (`usuario_id`),
  CONSTRAINT `Visita_ibfk_3` FOREIGN KEY (`tratamiento_id`) REFERENCES `Tratamiento` (`id`),
  CONSTRAINT `Visita_ibfk_4` FOREIGN KEY (`estado`) REFERENCES `EstadoVisita` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Visita`
--

LOCK TABLES `Visita` WRITE;
/*!40000 ALTER TABLE `Visita` DISABLE KEYS */;
INSERT INTO `Visita` VALUES (3,'53833277Y',5,NULL,'2025-05-22 07:40:27','Caries','aaaaa','PROGRAMADA'),(4,'53833278F',5,NULL,'2025-05-21 07:07:58','aaJibirii','Jibiriiii','PROGRAMADA');
/*!40000 ALTER TABLE `Visita` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-21 14:03:45
