/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.7.2-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: el_zorro
-- ------------------------------------------------------
-- Server version	11.7.2-MariaDB

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
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES
(1,'Limpiezaa'),
(2,'SALCHICHONERIA'),
(3,'ABARROTES'),
(5,'JABONES');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES
(2,'1234567890','aaa','aaa','aaa','a@a.com'),
(3,'1234567890','Carlos','Perez','santana','demo@demo.com');
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `detalle_venta`
--

LOCK TABLES `detalle_venta` WRITE;
/*!40000 ALTER TABLE `detalle_venta` DISABLE KEYS */;
/*!40000 ALTER TABLE `detalle_venta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `empleado`
--

LOCK TABLES `empleado` WRITE;
/*!40000 ALTER TABLE `empleado` DISABLE KEYS */;
INSERT INTO `empleado` VALUES
(1,'caja1','caja1','caja1',2,'caja1','$2a$10$bivO0xLMN2N4alduZCfH1uHaibKCgWvjLErBXLKN8jiZEKGEZ9p4K'),
(2,'admin1','admin1','admin1',1,'admin11','$2a$10$O7dq.7wzkyYGzYwWvcN/NulnQsLb0Gnrmzd72nUPNfeXd6XYzZQgO');
/*!40000 ALTER TABLE `empleado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `metodo_pago`
--

LOCK TABLES `metodo_pago` WRITE;
/*!40000 ALTER TABLE `metodo_pago` DISABLE KEYS */;
INSERT INTO `metodo_pago` VALUES
(1,'Efectivo'),
(2,'Tarjeta de credito'),
(3,'Vales de despensa'),
(4,'Tarjeta de credito'),
(5,'Vales de despensa');
/*!40000 ALTER TABLE `metodo_pago` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `movimiento_producto`
--

LOCK TABLES `movimiento_producto` WRITE;
/*!40000 ALTER TABLE `movimiento_producto` DISABLE KEYS */;
/*!40000 ALTER TABLE `movimiento_producto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `orden_proveedor`
--

LOCK TABLES `orden_proveedor` WRITE;
/*!40000 ALTER TABLE `orden_proveedor` DISABLE KEYS */;
/*!40000 ALTER TABLE `orden_proveedor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `producto`
--

LOCK TABLES `producto` WRITE;
/*!40000 ALTER TABLE `producto` DISABLE KEYS */;
INSERT INTO `producto` VALUES
(1,'Palo de escoba','3c07e66e-891a-421a-90fe-a4d94a3076d0.jpg',30.00,4,1,6);
/*!40000 ALTER TABLE `producto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `proveedor`
--

LOCK TABLES `proveedor` WRITE;
/*!40000 ALTER TABLE `proveedor` DISABLE KEYS */;
INSERT INTO `proveedor` VALUES
(2,'Zenon De Ramon Ramos','rarz630709ck2','aaa','56150','_612','derz64@hotmail.com','5581399680','zenon de ramon ramos','ACTIVO'),
(6,'Carlos chavez hernandez','rarz630709c11','iztapalapa','56230','_612','carslos@a.com','5524919293','luis','ACTIVO'),
(7,'b','bbbbbbbbb','b','11111','_621','deamo@demoa.com','1234567892','luisa','ACTIVO'),
(8,'v','vvvvvvvvvvvv','v','11112','_625','avaa@a.com','5520573378','aaaaab','ACTIVO'),
(9,'','','','',NULL,'','','','ACTIVO'),
(11,'proveedor1','AJKNAVPIANPAI','aaaaa','96566','_601','a@aa.com','125616511','avad','ACTIVO'),
(12,'proveedor2','cabvz230709a1','aaaa','56100','_601','aa@a.com','5555555555','avad','ACTIVO');
/*!40000 ALTER TABLE `proveedor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `rol`
--

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` VALUES
(1,'ADMIN'),
(2,'CAJA');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `venta`
--

LOCK TABLES `venta` WRITE;
/*!40000 ALTER TABLE `venta` DISABLE KEYS */;
/*!40000 ALTER TABLE `venta` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2025-05-26 20:50:11
