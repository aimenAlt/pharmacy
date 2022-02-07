-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema project1
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema project1
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `project1` DEFAULT CHARACTER SET utf8 ;
USE `project1` ;

-- -----------------------------------------------------
-- Table `project1`.`doctor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `project1`.`doctor` (
  `doctor_id` INT NOT NULL AUTO_INCREMENT,
  `ssn` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `specialty` VARCHAR(45) NOT NULL,
  `practice_since_year` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`doctor_id`),
  UNIQUE INDEX `doctor_id_UNIQUE` (`doctor_id` ASC) VISIBLE,
  UNIQUE INDEX `doctor_ssn_UNIQUE` (`ssn` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project1`.`patient`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `project1`.`patient` (
  `patient_id` INT NOT NULL AUTO_INCREMENT,
  `ssn` VARCHAR(45) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `birthdate` VARCHAR(45) NOT NULL,
  `street` VARCHAR(100) NOT NULL,
  `city` VARCHAR(100) NOT NULL,
  `state` VARCHAR(45) NOT NULL,
  `zipcode` VARCHAR(45) NOT NULL,
  `primary_id` INT NOT NULL,
  PRIMARY KEY (`patient_id`),
  INDEX `fk_patient_doctor1_idx` (`primary_id` ASC) VISIBLE,
  UNIQUE INDEX `patient_id_UNIQUE` (`patient_id` ASC) VISIBLE,
  UNIQUE INDEX `patient_ssn_UNIQUE` (`ssn` ASC) VISIBLE,
  CONSTRAINT `fk_patient_doctor1`
    FOREIGN KEY (`primary_id`)
    REFERENCES `project1`.`doctor` (`doctor_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project1`.`pharmacy`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `project1`.`pharmacy` (
  `pharmacy_id` INT NOT NULL AUTO_INCREMENT,
  `pharmacy_name` VARCHAR(45) NOT NULL,
  `street` VARCHAR(45) NOT NULL,
  `city` VARCHAR(45) NOT NULL,
  `state` VARCHAR(45) NOT NULL,
  `zip` INT NOT NULL,
  `phone_num` INT NOT NULL,
  PRIMARY KEY (`pharmacy_id`),
  UNIQUE INDEX `pharmacy_id_UNIQUE` (`pharmacy_id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project1`.`pharma_comp`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `project1`.`pharma_comp` (
  `pharma_comp_id` INT NOT NULL AUTO_INCREMENT,
  `phone_num` INT NOT NULL,
  `pharma_comp_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`pharma_comp_id`),
  UNIQUE INDEX `pharma_comp_id_UNIQUE` (`pharma_comp_id` ASC) VISIBLE,
  UNIQUE INDEX `pharma_comp_name_UNIQUE` (`pharma_comp_name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project1`.`drug`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `project1`.`drug` (
  `drug_id` INT(11) NOT NULL,
  `formula` VARCHAR(200) NOT NULL,
  `pharma_comp_id` INT NULL DEFAULT NULL,
  `trade_name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`drug_id`),
  UNIQUE INDEX `drug_id_UNIQUE` (`drug_id` ASC) VISIBLE,
  INDEX `fk_drug_pharma_comp1_idx` (`pharma_comp_id` ASC) VISIBLE,
  CONSTRAINT `fk_drug_pharma_comp1`
    FOREIGN KEY (`pharma_comp_id`)
    REFERENCES `project1`.`pharma_comp` (`pharma_comp_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project1`.`drug_listing`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `project1`.`drug_listing` (
  `drug_listing_id` INT NOT NULL AUTO_INCREMENT,
  `drug_id` INT NOT NULL,
  `pharmacy_id` INT NOT NULL,
  `price` DECIMAL(7,2) NOT NULL,
  PRIMARY KEY (`drug_listing_id`),
  UNIQUE INDEX `drug_listing_id_UNIQUE` (`drug_listing_id` ASC) VISIBLE,
  INDEX `fk_drug_listing_pharmacy1_idx` (`pharmacy_id` ASC) VISIBLE,
  INDEX `fk_drug_listing_drug1_idx` (`drug_id` ASC) VISIBLE,
  CONSTRAINT `fk_drug_listing_pharmacy1`
    FOREIGN KEY (`pharmacy_id`)
    REFERENCES `project1`.`pharmacy` (`pharmacy_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_drug_listing_drug1`
    FOREIGN KEY (`drug_id`)
    REFERENCES `project1`.`drug` (`drug_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project1`.`transaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `project1`.`transaction` (
  `transaction_id` INT NOT NULL AUTO_INCREMENT,
  `drug_listing_id` INT NOT NULL,
  `pharmacy_id` INT NOT NULL,
  `filled_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `prescription_id` INT NOT NULL,
  PRIMARY KEY (`transaction_id`),
  UNIQUE INDEX `transaction_id_UNIQUE` (`transaction_id` ASC) VISIBLE,
  INDEX `fk_transaction_drug_listing1_idx` (`drug_listing_id` ASC) VISIBLE,
  INDEX `fk_transaction_pharmacy1_idx` (`pharmacy_id` ASC) VISIBLE,
  CONSTRAINT `fk_transaction_drug_listing1`
    FOREIGN KEY (`drug_listing_id`)
    REFERENCES `project1`.`drug_listing` (`drug_listing_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_transaction_pharmacy1`
    FOREIGN KEY (`pharmacy_id`)
    REFERENCES `project1`.`pharmacy` (`pharmacy_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project1`.`prescription`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `project1`.`prescription` (
  `rxid` INT NOT NULL AUTO_INCREMENT,
  `quantity` VARCHAR(45) NOT NULL,
  `patient_ssn` VARCHAR(45) NOT NULL,
  `doctor_ssn` VARCHAR(45) NOT NULL,
  `transaction_id` INT NULL DEFAULT NULL,
  `drug_name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`rxid`),
  INDEX `fk_prescription_doctor1_idx` (`doctor_ssn` ASC) VISIBLE,
  UNIQUE INDEX `prescription_id_UNIQUE` (`rxid` ASC) VISIBLE,
  INDEX `fk_prescription_patient2_idx` (`patient_ssn` ASC) VISIBLE,
  INDEX `fk_prescription_transaction1_idx` (`transaction_id` ASC) VISIBLE,
  CONSTRAINT `fk_prescription_doctor1`
    FOREIGN KEY (`doctor_ssn`)
    REFERENCES `project1`.`doctor` (`ssn`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_prescription_patient2`
    FOREIGN KEY (`patient_ssn`)
    REFERENCES `project1`.`patient` (`ssn`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_prescription_transaction1`
    FOREIGN KEY (`transaction_id`)
    REFERENCES `project1`.`transaction` (`transaction_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project1`.`supervisor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `project1`.`supervisor` (
  `supervisor_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `phone_num` INT NOT NULL,
  `pharmacy_id` INT NOT NULL,
  PRIMARY KEY (`supervisor_id`),
  UNIQUE INDEX `supervisor_id_UNIQUE` (`supervisor_id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project1`.`contract`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `project1`.`contract` (
  `contract_id` INT NOT NULL AUTO_INCREMENT,
  `pharmacy_id` INT NOT NULL,
  `pharma_comp_id` INT NOT NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NULL,
  `contract_text` VARCHAR(45) NOT NULL,
  `supervisor_id` INT NOT NULL,
  PRIMARY KEY (`contract_id`),
  UNIQUE INDEX `contract_id_UNIQUE` (`contract_id` ASC) VISIBLE,
  INDEX `fk_contract_pharmacy1_idx` (`pharmacy_id` ASC) VISIBLE,
  INDEX `fk_contract_pharma_comp1_idx` (`pharma_comp_id` ASC) VISIBLE,
  INDEX `fk_contract_supervisor1_idx` (`supervisor_id` ASC) VISIBLE,
  CONSTRAINT `fk_contract_pharmacy1`
    FOREIGN KEY (`pharmacy_id`)
    REFERENCES `project1`.`pharmacy` (`pharmacy_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_contract_pharma_comp1`
    FOREIGN KEY (`pharma_comp_id`)
    REFERENCES `project1`.`pharma_comp` (`pharma_comp_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_contract_supervisor1`
    FOREIGN KEY (`supervisor_id`)
    REFERENCES `project1`.`supervisor` (`supervisor_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
