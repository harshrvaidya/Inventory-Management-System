-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: inventory
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Table structure for table `product_master`
--

DROP TABLE IF EXISTS `product_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_master` (
  `Product_Id` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) DEFAULT NULL,
  `Quantity` int DEFAULT NULL,
  `Price` int DEFAULT NULL,
  `Shelf` varchar(45) DEFAULT NULL,
  `Compartment` varchar(1) DEFAULT NULL,
  `Brand` varchar(45) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Product_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_master`
--

LOCK TABLES `product_master` WRITE;
/*!40000 ALTER TABLE `product_master` DISABLE KEYS */;
INSERT INTO `product_master` VALUES (1,'Amoxicillin',100,20,'2','A','Cipla','Tablet'),(2,'Pantoprazole',150,50,'2','A','Sunpharma','Capsule'),(3,'SunProtect',200,60,'2','A','Cipla','Tablet'),(4,'CoughSyrup',250,100,'2','A','Benydryl','Syrup'),(5,'Insulin',60,250,'2','E','Novo Nordisk','Injectable'),(6,'Paracetamol',300,15,'3','A','GSK','Tablet'),(7,'Vitamin B Complex',80,60,'5','B','Alkem','Capsule'),(8,'ORS Sachet',110,25,'4','F','FDC','Sachet'),(9,'Rabipur',20,400,'2','G','Sanofi','Injectable'),(10,'Azithromycin',90,35,'1','E','Pfizer','Tablet'),(11,'Amlodipine',75,50,'2','H','Zydus','Tablet'),(12,'Metformin',85,30,'3','C','Torrent Pharma','Tablet'),(13,'Montelukast',120,60,'4','B','Glenmark','Tablet'),(14,'Ceftriaxone',45,500,'2','D','Lupin','Injectable'),(15,'Amphotericin B',10,1200,'1','J','Sun Pharma','Injectable'),(16,'Salbutamol Inhaler',40,300,'1','K','Cipla','Inhaler'),(17,'Budesonide Nasal Spray',31,280,'2','L','Sun Pharma','Nasal Spray'),(18,'Cetirizine',150,12,'2','A','Zydus','Tablet'),(19,'Dicyclomine Syrup',65,45,'3','B','Abbott','Syrup'),(20,'Diclofenac Gel',45,60,'4','M','Novartis','Gel'),(21,'Bacillus Clausii',87,100,'4','N','Sanofi','Sachet'),(22,'Ofloxacin',55,50,'2','H','Sun Pharma','Tablet'),(23,'Fluconazole',65,64,'3','B','Pfizer','Tablet'),(24,'Neomycin Ointment',34,85,'5','O','Intas','Ointment'),(25,'Timolol Eye Drops',27,90,'1','P','Ajanta','Drops'),(26,'Cefixime',140,90,'2','B','Alkem','Tablet'),(27,'Amikacin',19,230,'3','Q','Bharat Serum','Injectable'),(28,'Loratadine',99,35,'4','A','Alembic','Tablet'),(29,'Ranitidine',101,20,'5','C','Ranbaxy','Tablet'),(30,'Omeprazole',112,40,'5','D','AstraZeneca','Capsule'),(31,'Dulcolax Suppository',16,145,'2','R','Boehringer','Suppository'),(32,'Isotretinoin Capsule',31,230,'3','S','Sun Pharma','Capsule'),(33,'Chloramphenicol Ear Drops',29,110,'2','T','GSK','Drops'),(34,'Clotrimazole Cream',40,55,'2','E','Glenmark','Ointment'),(35,'Dexamethasone Inj',22,180,'1','F','Zydus','Injectable'),(36,'Haloperidol Tablet',73,65,'1','U','Cipla','Tablet'),(37,'Albendazole Syrup',64,88,'4','B','Intas','Syrup'),(38,'Doxycycline Capsule',87,80,'5','V','Luminarc','Capsule'),(39,'Levocetirizine',56,15,'4','X','Dr Reddys','Tablet'),(40,'Desloratadine',27,35,'2','W','Sun Pharma','Tablet'),(41,'Folic Acid Tablet',120,10,'1','Y','Alkem','Tablet'),(42,'Ivermectin Tablet',42,55,'3','Z','Unichem','Tablet'),(43,'Ibuprofen Suspension',84,75,'3','D','Abbott','Syrup'),(44,'Gentamicin Eye Drops',37,105,'5','N','Indoco','Drops'),(45,'Riboflavin Tablet',65,30,'5','B','Cadila','Tablet'),(46,'Magaldrate Gel',80,70,'1','U','Cipla','Gel'),(47,'Heparin Injection',25,420,'3','A','Baxter','Injectable'),(48,'Salbutamol Syrup',91,60,'5','Q','Cipla','Syrup'),(49,'Nicotine Patch',38,330,'2','C','Pfizer','Patch'),(50,'Hydrocortisone Cream',28,125,'4','B','GSK','Ointment'),(51,'zulu',200,200,'5','A','cipla','abc');
/*!40000 ALTER TABLE `product_master` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales` (
  `Sales_id` int NOT NULL AUTO_INCREMENT,
  `Product_Id` int NOT NULL,
  `Quantity` int NOT NULL,
  `Total_price` int DEFAULT NULL,
  `SalesDate` date DEFAULT NULL,
  PRIMARY KEY (`Sales_id`),
  KEY `fk_Product_Id_idx` (`Product_Id`),
  CONSTRAINT `fk_Product_Id` FOREIGN KEY (`Product_Id`) REFERENCES `product_master` (`Product_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` VALUES (1,1,50,100,'2025-01-10'),(2,2,70,3500,'2025-03-24'),(3,3,25,1500,'2025-02-26'),(4,4,5,1250,'2025-11-10'),(5,5,60,900,'2025-11-10'),(6,6,12,720,'2025-11-10'),(7,7,20,500,'2025-11-11'),(8,8,3,1200,'2025-11-12'),(9,9,9,315,'2025-11-12'),(10,10,6,300,'2025-11-13'),(11,11,15,450,'2025-11-13'),(12,12,8,480,'2025-11-13'),(13,13,2,1000,'2025-11-14'),(14,14,1,1200,'2025-11-14'),(15,15,1,1200,'2025-12-10'),(16,16,8,2400,'2025-11-13'),(17,17,5,1400,'2025-11-13'),(18,18,35,420,'2025-11-14'),(19,19,10,450,'2025-11-14'),(20,20,12,720,'2025-11-15'),(21,21,9,900,'2025-11-15'),(22,22,22,1100,'2025-11-16'),(23,23,7,448,'2025-11-16'),(24,24,6,510,'2025-11-17'),(25,25,5,450,'2025-11-17'),(26,26,19,1710,'2025-11-17'),(27,27,4,920,'2025-11-18'),(28,28,16,560,'2025-11-18'),(29,29,12,240,'2025-11-19'),(30,30,13,520,'2025-11-19'),(31,31,3,435,'2025-11-19'),(32,32,5,1150,'2025-11-20'),(33,33,7,770,'2025-11-20'),(34,34,2,110,'2025-11-20'),(35,35,5,900,'2025-11-20'),(36,36,20,1300,'2025-11-21'),(37,37,9,792,'2025-11-21'),(38,38,14,1120,'2025-11-22'),(39,39,24,360,'2025-11-22'),(40,40,7,245,'2025-11-22'),(41,41,25,250,'2025-11-23'),(42,42,10,550,'2025-11-23'),(43,43,15,1125,'2025-11-23'),(44,44,9,945,'2025-11-23'),(45,45,17,510,'2025-11-24'),(46,46,8,560,'2025-11-24'),(47,47,5,2100,'2025-11-25'),(48,48,19,1140,'2025-11-25'),(49,49,7,2310,'2025-11-26'),(50,50,6,750,'2025-11-26');
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_item`
--

DROP TABLE IF EXISTS `sales_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_item` (
  `salesitem_ID` int NOT NULL AUTO_INCREMENT,
  `sales_ID` varchar(45) DEFAULT NULL,
  `product_id` varchar(45) DEFAULT NULL,
  `quantity` varchar(45) DEFAULT NULL,
  `price` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`salesitem_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_item`
--

LOCK TABLES `sales_item` WRITE;
/*!40000 ALTER TABLE `sales_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `sales_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `Email` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Harsh','12345','harshrvaidya@gmail.com'),(2,'yuvi','123456','yuvrajyadav617@gmail.com'),(5,'ritik','123456','abc@xyz.com');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-13 22:46:36
