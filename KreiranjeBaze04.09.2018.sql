
USE `bus` ;

-- -----------------------------------------------------
-- Table `bus`.`mjesto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`mjesto` (
  `BrojPoste` INT(11) NOT NULL,
  `Naziv` VARCHAR(35) NULL DEFAULT NULL,
  PRIMARY KEY (`BrojPoste`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`autobuska_stanica`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`autobuska_stanica` (
  `IdStanice` INT(11) NOT NULL AUTO_INCREMENT,
  `Naziv` VARCHAR(35) NULL DEFAULT NULL,
  `Adresa` VARCHAR(35) NULL DEFAULT NULL,
  `BrojPoste` INT(11) NOT NULL,
  `BrojTelefona` VARCHAR(15) NULL DEFAULT NULL,
  `BrojPerona` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`IdStanice`),
  INDEX `R_4` (`BrojPoste` ASC) VISIBLE,
  CONSTRAINT `R_4`
    FOREIGN KEY (`BrojPoste`)
    REFERENCES `bus`.`mjesto` (`BrojPoste`))
ENGINE = InnoDB
AUTO_INCREMENT = 10
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`linija`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`linija` (
  `IdLinije` INT(11) NOT NULL,
  `NazivLinije` VARCHAR(35) NULL DEFAULT NULL,
  `IdStanice` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`IdLinije`),
  INDEX `linija_ibfk_1` (`IdStanice` ASC) VISIBLE,
  CONSTRAINT `linija_ibfk_1`
    FOREIGN KEY (`IdStanice`)
    REFERENCES `bus`.`autobuska_stanica` (`IdStanice`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`relacija`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`relacija` (
  `IdRelacije` INT(11) NOT NULL,
  `IdLinije` INT(11) NOT NULL,
  `Ishodiste` VARCHAR(35) NULL DEFAULT NULL,
  `Odrediste` VARCHAR(35) NULL DEFAULT NULL,
  PRIMARY KEY (`IdRelacije`),
  INDEX `R_53` (`IdLinije` ASC) VISIBLE,
  CONSTRAINT `R_53`
    FOREIGN KEY (`IdLinije`)
    REFERENCES `bus`.`linija` (`IdLinije`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`zaposleni`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`zaposleni` (
  `JMBG` CHAR(13) NOT NULL,
  `Ime` VARCHAR(35) NULL DEFAULT NULL,
  `Prezime` VARCHAR(35) NULL DEFAULT NULL,
  `Adresa` VARCHAR(35) NULL DEFAULT NULL,
  `BrojTelefona` VARCHAR(25) NULL DEFAULT NULL,
  `StrucnaSprema` VARCHAR(35) NULL DEFAULT NULL,
  `IdStanice` INT(11) NOT NULL,
  `BrojPoste` INT(11) NOT NULL,
  `Email` VARCHAR(35) NULL DEFAULT NULL,
  PRIMARY KEY (`JMBG`),
  INDEX `R_58` (`BrojPoste` ASC) VISIBLE,
  INDEX `IdStanice` (`IdStanice` ASC) VISIBLE,
  CONSTRAINT `R_58`
    FOREIGN KEY (`BrojPoste`)
    REFERENCES `bus`.`mjesto` (`BrojPoste`),
  CONSTRAINT `zaposleni_ibfk_1`
    FOREIGN KEY (`IdStanice`)
    REFERENCES `bus`.`autobuska_stanica` (`IdStanice`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`rezervacija`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`rezervacija` (
  `IdRezervacije` INT(11) NOT NULL,
  `DatumRezervacije` DATE NULL DEFAULT NULL,
  `BrojKarata` INT(11) NULL DEFAULT NULL,
  `ImePrezimePutnika` VARCHAR(35) NULL DEFAULT NULL,
  `KontaktOsoba` VARCHAR(35) NULL DEFAULT NULL,
  `IdStanice` INT(11) NOT NULL,
  PRIMARY KEY (`IdRezervacije`),
  INDEX `IdStanice` (`IdStanice` ASC) VISIBLE,
  CONSTRAINT `rezervacija_ibfk_1`
    FOREIGN KEY (`IdStanice`)
    REFERENCES `bus`.`autobuska_stanica` (`IdStanice`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`karta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`karta` (
  `IdKarte` INT(11) NOT NULL,
  `IdStanice` INT(11) NOT NULL,
  `JMBG` CHAR(13) NOT NULL,
  `IdRelacije` INT(11) NULL DEFAULT NULL,
  `IdRezervacije` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`IdKarte`),
  INDEX `R_31` (`JMBG` ASC) VISIBLE,
  INDEX `R_55` (`IdRelacije` ASC) VISIBLE,
  INDEX `R_63` (`IdRezervacije` ASC) VISIBLE,
  INDEX `IdStanice` (`IdStanice` ASC) VISIBLE,
  CONSTRAINT `R_31`
    FOREIGN KEY (`JMBG`)
    REFERENCES `bus`.`zaposleni` (`JMBG`),
  CONSTRAINT `R_55`
    FOREIGN KEY (`IdRelacije`)
    REFERENCES `bus`.`relacija` (`IdRelacije`),
  CONSTRAINT `R_63`
    FOREIGN KEY (`IdRezervacije`)
    REFERENCES `bus`.`rezervacija` (`IdRezervacije`),
  CONSTRAINT `karta_ibfk_1`
    FOREIGN KEY (`IdStanice`)
    REFERENCES `bus`.`autobuska_stanica` (`IdStanice`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`jednokratna_karta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`jednokratna_karta` (
  `IdKarte` INT(11) NOT NULL,
  `Cijena` INT(11) NULL DEFAULT NULL,
  `Povratna` VARCHAR(35) NULL DEFAULT NULL,
  `IdRelacije` INT(11) NOT NULL,
  `VrijemePolaska` TIME NULL DEFAULT NULL,
  `DatumPolaska` DATE NULL DEFAULT NULL,
  `DatumIzdavanja` DATE NULL DEFAULT NULL,
  `Peron` INT(11) NULL DEFAULT NULL,
  `BrojSjedista` INT(11) NULL DEFAULT NULL,
  `Izdao` VARCHAR(35) NULL DEFAULT NULL,
  `JMBG` CHAR(13) NOT NULL,
  PRIMARY KEY (`IdKarte`),
  INDEX `R_22` (`IdRelacije` ASC) VISIBLE,
  INDEX `R_62` (`JMBG` ASC) VISIBLE,
  CONSTRAINT `R_22`
    FOREIGN KEY (`IdRelacije`)
    REFERENCES `bus`.`relacija` (`IdRelacije`),
  CONSTRAINT `R_62`
    FOREIGN KEY (`JMBG`)
    REFERENCES `bus`.`zaposleni` (`JMBG`),
  CONSTRAINT `jednokratna_karta_ibfk_1`
    FOREIGN KEY (`IdKarte`)
    REFERENCES `bus`.`karta` (`IdKarte`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`mjesecna_karta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`mjesecna_karta` (
  `IdKarte` INT(11) NOT NULL,
  `Cijena` INT(11) NULL DEFAULT NULL,
  `MjesecVazenja` VARCHAR(10) NULL DEFAULT NULL,
  `Ime` VARCHAR(20) NULL DEFAULT NULL,
  `Prezime` VARCHAR(20) NULL DEFAULT NULL,
  `Slika` MEDIUMBLOB NULL DEFAULT NULL,
  `Tip` VARCHAR(35) NULL DEFAULT NULL,
  PRIMARY KEY (`IdKarte`),
  CONSTRAINT `mjesecna_karta_ibfk_1`
    FOREIGN KEY (`IdKarte`)
    REFERENCES `bus`.`karta` (`IdKarte`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`nalog`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`nalog` (
  `KorisnickoIme` VARCHAR(35) NULL DEFAULT NULL,
  `Lozinka` VARCHAR(35) NULL DEFAULT NULL,
  `Tip` VARCHAR(25) NULL DEFAULT NULL,
  `JMBG` CHAR(13) NOT NULL,
  PRIMARY KEY (`JMBG`),
  CONSTRAINT `R_45`
    FOREIGN KEY (`JMBG`)
    REFERENCES `bus`.`zaposleni` (`JMBG`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`prevoznik`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`prevoznik` (
  `IdPrevoznika` INT(11) NOT NULL,
  `NazivPrevoznika` VARCHAR(35) NULL DEFAULT NULL,
  `Telefon` VARCHAR(35) NULL DEFAULT NULL,
  `Email` VARCHAR(35) NULL DEFAULT NULL,
  `WEBAdresa` VARCHAR(35) NULL DEFAULT NULL,
  `TekuciRacun` VARCHAR(18) NULL DEFAULT NULL,
  `BrojPoste` INT(11) NOT NULL,
  `IdLinije` INT(11) NOT NULL,
  PRIMARY KEY (`IdPrevoznika`),
  INDEX `R_39` (`BrojPoste` ASC) VISIBLE,
  INDEX `R_40` (`IdLinije` ASC) VISIBLE,
  CONSTRAINT `R_39`
    FOREIGN KEY (`BrojPoste`)
    REFERENCES `bus`.`mjesto` (`BrojPoste`),
  CONSTRAINT `R_40`
    FOREIGN KEY (`IdLinije`)
    REFERENCES `bus`.`linija` (`IdLinije`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`red_voznje`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`red_voznje` (
  `DanVrijemePolaska` CHAR(18) NULL DEFAULT NULL,
  `DanVrijemeDolaska` CHAR(18) NULL DEFAULT NULL,
  `CijenaUJednomSmjeru` CHAR(18) NULL DEFAULT NULL,
  `CijenaPovratneKarte` CHAR(18) NULL DEFAULT NULL,
  `CijenaMjesecneKarte` CHAR(18) NULL DEFAULT NULL,
  `IdPolaskaDolaska` CHAR(18) NOT NULL,
  PRIMARY KEY (`IdPolaskaDolaska`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;



