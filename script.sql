-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: sistemabancario
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `dni_usuario` varchar(12) NOT NULL,
  `idCliente` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`dni_usuario`),
  UNIQUE KEY `idCliente` (`idCliente`),
  CONSTRAINT `cliente_ibfk_1` FOREIGN KEY (`dni_usuario`) REFERENCES `usuario` (`dni`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES ('12345678A','CLI-2026-001'),('87654321B','CLI-2026-002');
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cuenta`
--

DROP TABLE IF EXISTS `cuenta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cuenta` (
  `numeroCuenta` varchar(20) NOT NULL,
  `dni_titular` varchar(12) NOT NULL,
  `saldo` decimal(15,2) DEFAULT 0.00,
  `fechaRegistro` date DEFAULT NULL,
  `estadoCuenta` tinyint(1) DEFAULT 1,
  `tipoCuenta` enum('AHORRO','CORRIENTE') NOT NULL,
  PRIMARY KEY (`numeroCuenta`),
  KEY `dni_titular` (`dni_titular`),
  CONSTRAINT `cuenta_ibfk_1` FOREIGN KEY (`dni_titular`) REFERENCES `usuario` (`dni`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cuenta`
--

LOCK TABLES `cuenta` WRITE;
/*!40000 ALTER TABLE `cuenta` DISABLE KEYS */;
INSERT INTO `cuenta` VALUES ('ES210001','12345678A',5400.00,'2026-01-10',1,'AHORRO'),('ES210002','12345678A',150.25,'2026-02-15',1,'CORRIENTE'),('ES210003','87654321B',12500.00,'2026-03-01',1,'AHORRO'),('ES210004','87654321B',-50.00,'2026-03-20',1,'CORRIENTE');
/*!40000 ALTER TABLE `cuenta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cuentaahorro`
--

DROP TABLE IF EXISTS `cuentaahorro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cuentaahorro` (
  `numeroCuenta` varchar(20) NOT NULL,
  `tasaInteresAhorro` decimal(5,2) DEFAULT 0.00,
  PRIMARY KEY (`numeroCuenta`),
  CONSTRAINT `cuentaahorro_ibfk_1` FOREIGN KEY (`numeroCuenta`) REFERENCES `cuenta` (`numeroCuenta`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cuentaahorro`
--

LOCK TABLES `cuentaahorro` WRITE;
/*!40000 ALTER TABLE `cuentaahorro` DISABLE KEYS */;
INSERT INTO `cuentaahorro` VALUES ('ES210001',2.50),('ES210003',3.75);
/*!40000 ALTER TABLE `cuentaahorro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cuentacorriente`
--

DROP TABLE IF EXISTS `cuentacorriente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cuentacorriente` (
  `numeroCuenta` varchar(20) NOT NULL,
  `limiteDescubierto` decimal(15,2) DEFAULT 0.00,
  `comisionMantenimiento` decimal(10,2) DEFAULT 0.00,
  PRIMARY KEY (`numeroCuenta`),
  CONSTRAINT `cuentacorriente_ibfk_1` FOREIGN KEY (`numeroCuenta`) REFERENCES `cuenta` (`numeroCuenta`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cuentacorriente`
--

LOCK TABLES `cuentacorriente` WRITE;
/*!40000 ALTER TABLE `cuentacorriente` DISABLE KEYS */;
INSERT INTO `cuentacorriente` VALUES ('ES210002',500.00,12.00),('ES210004',1000.00,10.00);
/*!40000 ALTER TABLE `cuentacorriente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empleado`
--

DROP TABLE IF EXISTS `empleado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empleado` (
  `dni_usuario` varchar(12) NOT NULL,
  `idEmpleado` varchar(50) DEFAULT NULL,
  `salario` decimal(15,2) DEFAULT NULL,
  `tipoPuesto` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`dni_usuario`),
  UNIQUE KEY `idEmpleado` (`idEmpleado`),
  CONSTRAINT `empleado_ibfk_1` FOREIGN KEY (`dni_usuario`) REFERENCES `usuario` (`dni`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empleado`
--

LOCK TABLES `empleado` WRITE;
/*!40000 ALTER TABLE `empleado` DISABLE KEYS */;
INSERT INTO `empleado` VALUES ('44556677C','EMP-001',1850.50,'Cajero'),('99887766D','EMP-002',2900.00,'Gestor de Cuentas');
/*!40000 ALTER TABLE `empleado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `incidencia`
--

DROP TABLE IF EXISTS `incidencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `incidencia` (
  `idIncidencia` int(11) NOT NULL AUTO_INCREMENT,
  `titulo` varchar(100) DEFAULT NULL,
  `descripcion` text DEFAULT NULL,
  `estado` varchar(20) DEFAULT 'ABIERTA',
  `dni_cliente` varchar(12) DEFAULT NULL,
  `dni_empleado` varchar(12) DEFAULT NULL,
  PRIMARY KEY (`idIncidencia`),
  KEY `dni_cliente` (`dni_cliente`),
  KEY `dni_empleado` (`dni_empleado`),
  CONSTRAINT `incidencia_ibfk_1` FOREIGN KEY (`dni_cliente`) REFERENCES `cliente` (`dni_usuario`),
  CONSTRAINT `incidencia_ibfk_2` FOREIGN KEY (`dni_empleado`) REFERENCES `empleado` (`dni_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `incidencia`
--

LOCK TABLES `incidencia` WRITE;
/*!40000 ALTER TABLE `incidencia` DISABLE KEYS */;
INSERT INTO `incidencia` VALUES (1,'Tarjeta bloqueada','El cliente no recuerda su PIN tras 3 intentos','ABIERTA','12345678A','44556677C'),(2,'Error en Transferencia','No se refleja el abono en cuenta destino','EN PROCESO','87654321B','99887766D');
/*!40000 ALTER TABLE `incidencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logauditoria`
--

DROP TABLE IF EXISTS `logauditoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `logauditoria` (
  `idLog` int(11) NOT NULL AUTO_INCREMENT,
  `accion` varchar(255) DEFAULT NULL,
  `fechaAccion` timestamp NOT NULL DEFAULT current_timestamp(),
  `resultado` tinyint(1) DEFAULT NULL,
  `idUsuario` varchar(12) DEFAULT NULL,
  PRIMARY KEY (`idLog`),
  KEY `idUsuario` (`idUsuario`),
  CONSTRAINT `logauditoria_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`dni`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logauditoria`
--

LOCK TABLES `logauditoria` WRITE;
/*!40000 ALTER TABLE `logauditoria` DISABLE KEYS */;
INSERT INTO `logauditoria` VALUES (1,'Login Exitoso','2026-04-20 15:31:32',1,'12345678A'),(2,'Cambio de Password','2026-04-20 15:31:32',0,'87654321B'),(3,'Acceso a Panel Admin','2026-04-20 15:31:32',1,'99887766D');
/*!40000 ALTER TABLE `logauditoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operacion`
--

DROP TABLE IF EXISTS `operacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operacion` (
  `idOperacion` int(11) NOT NULL AUTO_INCREMENT,
  `importe` decimal(15,2) NOT NULL,
  `tipoOperacion` enum('TRANSFERENCIA','BIZUM','EFECTIVO') NOT NULL,
  `idCuentaOrigen` varchar(20) DEFAULT NULL,
  `idCuentaDestino` varchar(20) DEFAULT NULL,
  `fechaOperacion` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`idOperacion`),
  KEY `idCuentaOrigen` (`idCuentaOrigen`),
  KEY `idCuentaDestino` (`idCuentaDestino`),
  CONSTRAINT `operacion_ibfk_1` FOREIGN KEY (`idCuentaOrigen`) REFERENCES `cuenta` (`numeroCuenta`),
  CONSTRAINT `operacion_ibfk_2` FOREIGN KEY (`idCuentaDestino`) REFERENCES `cuenta` (`numeroCuenta`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operacion`
--

LOCK TABLES `operacion` WRITE;
/*!40000 ALTER TABLE `operacion` DISABLE KEYS */;
INSERT INTO `operacion` VALUES (1,200.00,'TRANSFERENCIA','ES210001','ES210003','2026-04-20 15:31:32'),(2,50.00,'BIZUM','ES210002','ES210004','2026-04-20 15:31:32'),(3,100.00,'EFECTIVO','ES210001',NULL,'2026-04-20 15:31:32');
/*!40000 ALTER TABLE `operacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tarjeta`
--

DROP TABLE IF EXISTS `tarjeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tarjeta` (
  `numeroTarjeta` varchar(16) NOT NULL,
  `cvv` int(11) NOT NULL,
  `fechaCaducidad` date NOT NULL,
  `pin` varchar(4) NOT NULL,
  `tipoTarjeta` enum('DEBITO','CREDITO') NOT NULL,
  `limite` decimal(15,2) DEFAULT NULL,
  `numeroCuenta` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`numeroTarjeta`),
  KEY `numeroCuenta` (`numeroCuenta`),
  CONSTRAINT `tarjeta_ibfk_1` FOREIGN KEY (`numeroCuenta`) REFERENCES `cuenta` (`numeroCuenta`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tarjeta`
--

LOCK TABLES `tarjeta` WRITE;
/*!40000 ALTER TABLE `tarjeta` DISABLE KEYS */;
INSERT INTO `tarjeta` VALUES ('4532880011223344',123,'2029-12-31','1402','DEBITO',1200.00,'ES210002'),('5412990088776655',456,'2028-06-30','5592','CREDITO',3000.00,'ES210001');
/*!40000 ALTER TABLE `tarjeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `dni` varchar(12) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `pais` varchar(50) DEFAULT NULL,
  `id_interno` int(11) DEFAULT NULL,
  `rol` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `direccion` text DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `passwordHash` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`dni`),
  UNIQUE KEY `id_interno` (`id_interno`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES ('12345678A','Carlos Javier Ruiz','+34 600111222','España',101,'CLIENTE','carlos@gmail.com','Calle Mayor 1, Madrid','1234','hash_simulado_1'),('44556677C','Roberto Gómez Piriz','+34 655999888','España',103,'EMPLEADO','roberto.staff@banco.com','Plaza España 5, Valencia','staff123','hash_simulado_3'),('87654321B','Elena Moreno Sanz','+34 600333444','España',102,'CLIENTE','elena@outlook.es','Av. Libertad 20, Sevilla','abcd','hash_simulado_2'),('99887766D','Lucía Fernández','+34 611222333','España',104,'EMPLEADO','lucia.admin@banco.com','Calle Luna 12, Barcelona','admin2026','hash_simulado_4');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-27 16:01:30
