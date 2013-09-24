
SET FOREIGN_KEY_CHECKS = 0;
DROP DATABASE get_dressed_test;
CREATE DATABASE  IF NOT EXISTS `get_dressed_test` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `get_dressed_test`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: get_dressed_test
-- ------------------------------------------------------
-- Server version	5.5.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `abstract_garments`
--

DROP TABLE IF EXISTS `abstract_garments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `abstract_garments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `age` int(11) DEFAULT '365',
  `views` int(11) DEFAULT '0',
  `saves` int(11) DEFAULT '0',
  `category_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `Abstractgarment_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `abstract_outfits`
--

DROP TABLE IF EXISTS `abstract_outfits`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `abstract_outfits` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `likes` int(11) DEFAULT '0',
  `views` int(11) DEFAULT '0',
  `age` int(11) DEFAULT '365',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `abstract_outfits_components`
--

DROP TABLE IF EXISTS `abstract_outfits_components`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `abstract_outfits_components` (
  `abstract_outfit_id` int(11) NOT NULL DEFAULT '0',
  `abstract_garment_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`abstract_outfit_id`,`abstract_garment_id`),
  KEY `Abstract_outfits_components_ibfk_2` (`abstract_garment_id`),
  CONSTRAINT `Abstract_outfits_components_ibfk_1` FOREIGN KEY (`abstract_outfit_id`) REFERENCES `abstract_outfits` (`id`),
  CONSTRAINT `Abstract_outfits_components_ibfk_2` FOREIGN KEY (`abstract_garment_id`) REFERENCES `abstract_garments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categories` (
  `id` int(11) NOT NULL,
  `name` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `following`
--

DROP TABLE IF EXISTS `following`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `following` (
  `id_u1` int(11) NOT NULL DEFAULT '0',
  `id_u2` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_u1`,`id_u2`),
  KEY `id_u2` (`id_u2`),
  CONSTRAINT `Following_ibfk_1` FOREIGN KEY (`id_u1`) REFERENCES `members` (`id`),
  CONSTRAINT `Following_ibfk_2` FOREIGN KEY (`id_u2`) REFERENCES `members` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `garments`
--

DROP TABLE IF EXISTS `garments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `garments` (
  `id` int(11) NOT NULL,
  `age` int(11) DEFAULT '365',
  `views` int(11) DEFAULT '0',
  `saves` int(11) DEFAULT '0',
  `category_id` int(11) NOT NULL,
  `abstract_garment_id` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `description` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `category_id` (`category_id`),
  KEY `abstract_garment_id` (`abstract_garment_id`),
  CONSTRAINT `garment_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  CONSTRAINT `garment_ibfk_2` FOREIGN KEY (`abstract_garment_id`) REFERENCES `abstract_garments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `members`
--

DROP TABLE IF EXISTS `members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `members` (
  `id` int(11) NOT NULL,
  `name` varchar(40) DEFAULT NULL,
  `complete` BIT NOT NULL DEFAULT 0,
  `num_best_answer` int(11) NOT NULL DEFAULT '0',
  `num_outfit_views` int(11) NOT NULL DEFAULT '0',
  `num_outfit_likes` int(11) NOT NULL DEFAULT '0',
  `num_trophies` int(11) NOT NULL DEFAULT '0',
  `num_followers` int(11) NOT NULL DEFAULT '0',
  `date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `members_likes`
--

DROP TABLE IF EXISTS `members_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `members_likes` (
  `member_id` int(11) NOT NULL DEFAULT '0',
  `outfit_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`outfit_id`,`member_id`),
  KEY `member_id` (`member_id`),
  CONSTRAINT `MemberLikes_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`),
  CONSTRAINT `MemberLikes_ibfk_2` FOREIGN KEY (`outfit_id`) REFERENCES `outfits` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `no_garments`
--

DROP TABLE IF EXISTS `no_garments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `no_garments` (
  `garment_id` int(11) NOT NULL,
  PRIMARY KEY (`garment_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `outfits`
--

DROP TABLE IF EXISTS `outfits`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `outfits` (
  `id` int(11) NOT NULL,
  `member_id` int(11) DEFAULT NULL,
  `likes` int(11) DEFAULT '0',
  `views` int(11) DEFAULT '0',
  `age` int(11) DEFAULT '365',
  `abstract_outfit_id` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `abstract_outfit_id` (`abstract_outfit_id`),
  KEY `member_id` (`member_id`),
  CONSTRAINT `Outfit_ibfk_1` FOREIGN KEY (`abstract_outfit_id`) REFERENCES `abstract_outfits` (`id`),
  CONSTRAINT `Outfit_ibfk_2` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `outfits_components`
--

DROP TABLE IF EXISTS `outfits_components`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `outfits_components` (
  `outfit_id` int(11) NOT NULL DEFAULT '0',
  `garment_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`outfit_id`,`garment_id`),
  KEY `garment_id` (`garment_id`),
  CONSTRAINT `outfits_components_ibfk_1` FOREIGN KEY (`outfit_id`) REFERENCES `outfits` (`id`),
  CONSTRAINT `outfits_components_ibfk_2` FOREIGN KEY (`garment_id`) REFERENCES `garments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-08-22 16:11:16

SET FOREIGN_KEY_CHECKS = 1;
