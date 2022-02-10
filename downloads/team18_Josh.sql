CREATE DATABASE  IF NOT EXISTS `project1` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `project1`;
-- MySQL dump 10.13  Distrib 8.0.26, for macos11 (x86_64)
--
-- Host: 127.0.0.1    Database: project1
-- ------------------------------------------------------
-- Server version	8.0.26

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
-- Table structure for table `contract`
--

DROP TABLE IF EXISTS `contract`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contract` (
  `contract_id` int NOT NULL AUTO_INCREMENT,
  `pharmacy_id` int NOT NULL,
  `pharma_comp_id` int NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date DEFAULT NULL,
  `contract_text` varchar(45) NOT NULL,
  `supervisor_id` int NOT NULL,
  PRIMARY KEY (`contract_id`),
  UNIQUE KEY `contract_id_UNIQUE` (`contract_id`),
  KEY `fk_contract_pharmacy1_idx` (`pharmacy_id`),
  KEY `fk_contract_pharma_comp1_idx` (`pharma_comp_id`),
  KEY `fk_contract_supervisor1_idx` (`supervisor_id`),
  CONSTRAINT `fk_contract_pharma_comp1` FOREIGN KEY (`pharma_comp_id`) REFERENCES `pharma_comp` (`pharma_comp_id`),
  CONSTRAINT `fk_contract_pharmacy1` FOREIGN KEY (`pharmacy_id`) REFERENCES `pharmacy` (`pharmacy_id`),
  CONSTRAINT `fk_contract_supervisor1` FOREIGN KEY (`supervisor_id`) REFERENCES `supervisor` (`supervisor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contract`
--

LOCK TABLES `contract` WRITE;
/*!40000 ALTER TABLE `contract` DISABLE KEYS */;
/*!40000 ALTER TABLE `contract` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor`
--

DROP TABLE IF EXISTS `doctor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor` (
  `doctor_id` int NOT NULL AUTO_INCREMENT,
  `ssn` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `specialty` varchar(45) NOT NULL,
  `practice_since_year` varchar(45) NOT NULL,
  PRIMARY KEY (`doctor_id`),
  UNIQUE KEY `doctor_id_UNIQUE` (`doctor_id`),
  UNIQUE KEY `doctor_ssn_UNIQUE` (`ssn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor`
--

LOCK TABLES `doctor` WRITE;
/*!40000 ALTER TABLE `doctor` DISABLE KEYS */;
/*!40000 ALTER TABLE `doctor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drug`
--

DROP TABLE IF EXISTS `drug`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `drug` (
  `drug_id` int NOT NULL,
  `formula` varchar(200) NOT NULL,
  `pharma_comp_id` int DEFAULT NULL,
  `trade_name` varchar(100) NOT NULL,
  `price` decimal(7,2) NOT NULL,
  PRIMARY KEY (`drug_id`),
  UNIQUE KEY `drug_id_UNIQUE` (`drug_id`),
  KEY `fk_drug_pharma_comp1_idx` (`pharma_comp_id`),
  CONSTRAINT `fk_drug_pharma_comp1` FOREIGN KEY (`pharma_comp_id`) REFERENCES `pharma_comp` (`pharma_comp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drug`
--

LOCK TABLES `drug` WRITE;
/*!40000 ALTER TABLE `drug` DISABLE KEYS */;
INSERT INTO `drug` VALUES (1,'acetaminophen and codeine',NULL,'Tylenol with Codeine',0.00),(2,'albuterol aerosol',NULL,'Proair Proventil',0.00),(3,'albuterol HFA',NULL,'Accuneb',0.00),(4,'alendronate',NULL,'Fosamax',0.00),(5,'allopurinol',NULL,'Zyloprim',0.00),(6,'alprazolam',NULL,'Xanax',0.00),(7,'amitriptyline',NULL,'Elavil',0.00),(8,'amoxicillin and clavulanate K+',NULL,'Augmentin',0.00),(9,'amoxicillin',NULL,'Amoxil',0.00),(10,'amphetamine and dextroamphetamine XR',NULL,'Adderall XR',0.00),(11,'atenolol',NULL,'Tenormin',0.00),(12,'atorvastatin',NULL,'Lipitor',0.00),(13,'azithromycin',NULL,'Zithromax',0.00),(14,'benazepril and amlodipine',NULL,'Lotrel',0.00),(15,'carisoprodol',NULL,'Soma',0.00),(16,'carvedilol',NULL,'Coreg',0.00),(17,'cefdinir',NULL,'Omnicef',0.00),(18,'celecoxib',NULL,'Celebrex',0.00),(19,'cephalexin',NULL,'Keflex',0.00),(20,'ciprofloxacin',NULL,'Cipro',0.00),(21,'citalopram',NULL,'Celexa',0.00),(22,'clonazepam',NULL,'Klonopin',0.00),(23,'clonidine HCl',NULL,'Catapres',0.00),(24,'clopidogrel',NULL,'Plavix',0.00),(25,'conjugated estrogens',NULL,'Premarin',0.00),(26,'cyclobenzaprine',NULL,'Flexeril',0.00),(27,'diazepam',NULL,'Valium',0.00),(28,'diclofenac sodium',NULL,'Voltaren',0.00),(29,'drospirenone and ethinyl estradiol',NULL,'Yaz',0.00),(30,'Duloxetine',NULL,'Cymbalta',0.00),(31,'doxycycline hyclate',NULL,'Vibramycin',0.00),(32,'enalapril',NULL,'Vasotec',0.00),(33,'escitalopram',NULL,'Lexapro',0.00),(34,'esomeprazole',NULL,'Nexium',0.00),(35,'ezetimibe',NULL,'Zetia',0.00),(36,'fenofibrate',NULL,'Tricor',0.00),(37,'fexofenadine',NULL,'Allegra',0.00),(38,'fluconozole',NULL,'Diflucan',0.00),(39,'fluoxetine HCl',NULL,'Prozac',0.00),(40,'fluticasone and salmeterol inhaler',NULL,'Advair',0.00),(41,'fluticasone nasal spray',NULL,'Flonase',0.00),(42,'folic acid',NULL,'Folic Acid',0.00),(43,'furosemide',NULL,'Lasix',0.00),(44,'gabapentin',NULL,'Neurontin',0.00),(45,'glimepiride',NULL,'Amaryl',0.00),(46,'glyburide',NULL,'Diabeta',0.00),(47,'glipizide',NULL,'Glucotrol',0.00),(48,'hydrochlorothiazide',NULL,'Microzide',0.00),(49,'hydrocodone and acetaminophen',NULL,'Lortab',0.00),(50,'ibuprophen',NULL,'Motrin',0.00),(51,'insulin glargine',NULL,'Lantus',0.00),(52,'isosorbide mononitrate',NULL,'Imdur',0.00),(53,'lansoprazole',NULL,'Prevacid',0.00),(54,'levofloxacin',NULL,'Levaquin',0.00),(55,'levothyroxine sodium',NULL,'Levoxl',0.00),(56,'lisinopril and hydrochlorothiazide',NULL,'Zestoretic',0.00),(57,'lisinopril',NULL,'Prinivil',0.00),(58,'lorazepam',NULL,'Ativan',0.00),(59,'losartan',NULL,'Cozaar',0.00),(60,'lovastatin',NULL,'Mevacor',0.00),(61,'meloxicam',NULL,'Mobic',0.00),(62,'metformin HCl',NULL,'Glucophage',0.00),(63,'methylprednisone',NULL,'Medrol',0.00),(64,'metoprolol succinate XL',NULL,'Toprol',0.00),(65,'metoprolol tartrate',NULL,'Lopressor',0.00),(66,'mometasone',NULL,'Nasonex',0.00),(67,'montelukast',NULL,'Singulair',0.00),(68,'naproxen',NULL,'Naprosyn',0.00),(69,'omeprazole',NULL,'Prilosec',0.00),(70,'oxycodone and acetaminophen',NULL,'Percocet',0.00),(71,'pantoprazole',NULL,'Protonix',0.00),(72,'paroxetine',NULL,'Paxil',0.00),(73,'pioglitazone',NULL,'Actos',0.00),(74,'potassium Chloride',NULL,'Klor-Con',0.00),(75,'pravastatin',NULL,'Pravachol',0.00),(76,'prednisone',NULL,'Deltasone',0.00),(77,'pregabalin',NULL,'Lyrica',0.00),(78,'promethazine',NULL,'Phenergan',0.00),(79,'quetiapine',NULL,'Seroquel',0.00),(80,'ranitidine',NULL,'Zantac',0.00),(81,'rosuvastatin',NULL,'Crestor',0.00),(82,'sertraline HCl',NULL,'Zoloft',0.00),(83,'sildenafil HCl',NULL,'Viagra',0.00),(84,'simvastatin and ezetimibe',NULL,'Vytorin',0.00),(85,'simvastatin',NULL,'Zocor',0.00),(86,'spironolactone',NULL,'Aldactone',0.00),(87,'sulfamethoxazole and trimethoprim DS',NULL,'Bactrim DS',0.00),(88,'tamsulosin',NULL,'Flomax',0.00),(89,'temezepam',NULL,'Restoril',0.00),(90,'topiramate',NULL,'Topamax',0.00),(91,'tramadol',NULL,'Ultram',0.00),(92,'triamcinolone Ace topical',NULL,'Aristocort',0.00),(93,'trazodone HCl',NULL,'Desyrel',0.00),(94,'triamterene and hydrochlorothiazide',NULL,'Dyazide',0.00),(95,'valaciclovir',NULL,'Valtrex',0.00),(96,'valsartan',NULL,'Diovan',0.00),(97,'venlafaxine XR',NULL,'Effexor XR',0.00),(98,'verapamil SR',NULL,'Calan SR',0.00),(99,'zolpidem',NULL,'Ambien',0.00);
/*!40000 ALTER TABLE `drug` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drug_listing`
--

DROP TABLE IF EXISTS `drug_listing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `drug_listing` (
  `drug_listing_id` int NOT NULL AUTO_INCREMENT,
  `drug_id` int NOT NULL,
  `pharmacy_id` int NOT NULL,
  `price` decimal(7,2) NOT NULL,
  PRIMARY KEY (`drug_listing_id`),
  UNIQUE KEY `drug_listing_id_UNIQUE` (`drug_listing_id`),
  KEY `fk_drug_listing_pharmacy1_idx` (`pharmacy_id`),
  KEY `fk_drug_listing_drug1_idx` (`drug_id`),
  CONSTRAINT `fk_drug_listing_drug1` FOREIGN KEY (`drug_id`) REFERENCES `drug` (`drug_id`),
  CONSTRAINT `fk_drug_listing_pharmacy1` FOREIGN KEY (`pharmacy_id`) REFERENCES `pharmacy` (`pharmacy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drug_listing`
--

LOCK TABLES `drug_listing` WRITE;
/*!40000 ALTER TABLE `drug_listing` DISABLE KEYS */;
/*!40000 ALTER TABLE `drug_listing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient` (
  `patient_id` int NOT NULL AUTO_INCREMENT,
  `ssn` varchar(45) NOT NULL,
  `name` varchar(100) NOT NULL,
  `birthdate` varchar(45) NOT NULL,
  `street` varchar(100) NOT NULL,
  `city` varchar(100) NOT NULL,
  `state` varchar(45) NOT NULL,
  `zipcode` varchar(45) NOT NULL,
  `primary_id` int NOT NULL,
  PRIMARY KEY (`patient_id`),
  UNIQUE KEY `patient_id_UNIQUE` (`patient_id`),
  UNIQUE KEY `patient_ssn_UNIQUE` (`ssn`),
  KEY `fk_patient_doctor1_idx` (`primary_id`),
  CONSTRAINT `fk_patient_doctor1` FOREIGN KEY (`primary_id`) REFERENCES `doctor` (`doctor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pharma_comp`
--

DROP TABLE IF EXISTS `pharma_comp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pharma_comp` (
  `pharma_comp_id` int NOT NULL AUTO_INCREMENT,
  `phone_num` int NOT NULL,
  `pharma_comp_name` varchar(45) NOT NULL,
  PRIMARY KEY (`pharma_comp_id`),
  UNIQUE KEY `pharma_comp_id_UNIQUE` (`pharma_comp_id`),
  UNIQUE KEY `pharma_comp_name_UNIQUE` (`pharma_comp_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pharma_comp`
--

LOCK TABLES `pharma_comp` WRITE;
/*!40000 ALTER TABLE `pharma_comp` DISABLE KEYS */;
/*!40000 ALTER TABLE `pharma_comp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pharmacy`
--

DROP TABLE IF EXISTS `pharmacy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pharmacy` (
  `pharmacy_id` int NOT NULL AUTO_INCREMENT,
  `pharmacy_name` varchar(45) NOT NULL,
  `street` varchar(45) NOT NULL,
  `city` varchar(45) NOT NULL,
  `state` varchar(45) NOT NULL,
  `zip` int NOT NULL,
  `phone_num` int NOT NULL,
  PRIMARY KEY (`pharmacy_id`),
  UNIQUE KEY `pharmacy_id_UNIQUE` (`pharmacy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pharmacy`
--

LOCK TABLES `pharmacy` WRITE;
/*!40000 ALTER TABLE `pharmacy` DISABLE KEYS */;
/*!40000 ALTER TABLE `pharmacy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescription`
--

DROP TABLE IF EXISTS `prescription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescription` (
  `rxid` int NOT NULL AUTO_INCREMENT,
  `quantity` varchar(45) NOT NULL,
  `patient_ssn` varchar(45) NOT NULL,
  `doctor_ssn` varchar(45) NOT NULL,
  `pharmacy_id` int DEFAULT NULL,
  `drug_name` varchar(100) NOT NULL,
  `fill_date` varchar(45) DEFAULT NULL,
  `request_date` varchar(45) NOT NULL,
  PRIMARY KEY (`rxid`),
  UNIQUE KEY `prescription_id_UNIQUE` (`rxid`),
  KEY `fk_prescription_doctor1_idx` (`doctor_ssn`),
  KEY `fk_prescription_patient2_idx` (`patient_ssn`),
  KEY `fk_prescription_pharmacy1_idx` (`pharmacy_id`),
  CONSTRAINT `fk_prescription_doctor1` FOREIGN KEY (`doctor_ssn`) REFERENCES `doctor` (`ssn`),
  CONSTRAINT `fk_prescription_patient2` FOREIGN KEY (`patient_ssn`) REFERENCES `patient` (`ssn`),
  CONSTRAINT `fk_prescription_pharmacy1` FOREIGN KEY (`pharmacy_id`) REFERENCES `pharmacy` (`pharmacy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescription`
--

LOCK TABLES `prescription` WRITE;
/*!40000 ALTER TABLE `prescription` DISABLE KEYS */;
/*!40000 ALTER TABLE `prescription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supervisor`
--

DROP TABLE IF EXISTS `supervisor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supervisor` (
  `supervisor_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `phone_num` int NOT NULL,
  `pharmacy_id` int NOT NULL,
  PRIMARY KEY (`supervisor_id`),
  UNIQUE KEY `supervisor_id_UNIQUE` (`supervisor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supervisor`
--

LOCK TABLES `supervisor` WRITE;
/*!40000 ALTER TABLE `supervisor` DISABLE KEYS */;
/*!40000 ALTER TABLE `supervisor` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-02-09 20:21:16
