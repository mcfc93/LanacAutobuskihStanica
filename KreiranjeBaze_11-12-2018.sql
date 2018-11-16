CREATE SCHEMA IF NOT EXISTS `bus` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `bus` ;

-- -----------------------------------------------------
-- Table `bus`.`mjesto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`mjesto` (
  `PostanskiBroj` INT(11) NOT NULL,
  `Naziv` VARCHAR(35) NULL DEFAULT NULL,
  PRIMARY KEY (`PostanskiBroj`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`autobuska_stanica`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`autobuska_stanica` (
  `JIBStanice` CHAR(10) NOT NULL,
  `Naziv` VARCHAR(35) NULL DEFAULT NULL,
  `Adresa` VARCHAR(35) NULL DEFAULT NULL,
  `PostanskiBroj` INT(11) NULL DEFAULT NULL,
  `BrojTelefona` VARCHAR(15) NULL DEFAULT NULL,
  `BrojPerona` INT(11) NULL DEFAULT NULL,
  `WebStranica` VARCHAR(45) NULL DEFAULT NULL,
  `StanicaAktivna` TINYINT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`JIBStanice`),
  INDEX `IDMjesta_fk_idx` (`PostanskiBroj` ASC) VISIBLE,
  CONSTRAINT `IDMjesta_fk`
    FOREIGN KEY (`PostanskiBroj`)
    REFERENCES `bus`.`mjesto` (`PostanskiBroj`)
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`prevoznik`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`prevoznik` (
  `JIBPrevoznika` CHAR(10) NOT NULL,
  `NazivPrevoznika` VARCHAR(35) NULL DEFAULT NULL,
  `Telefon` VARCHAR(35) NULL DEFAULT NULL,
  `Email` VARCHAR(35) NULL DEFAULT NULL,
  `WebAdresa` VARCHAR(35) NULL DEFAULT NULL,
  `TekuciRacun` VARCHAR(18) NULL DEFAULT NULL,
  `Adresa` VARCHAR(45) NULL DEFAULT NULL,
  `PostanskiBroj` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`JIBPrevoznika`),
  INDEX `PrevoznikMjesto_fk_idx` (`PostanskiBroj` ASC) VISIBLE,
  CONSTRAINT `PrevoznikMjesto_fk`
    FOREIGN KEY (`PostanskiBroj`)
    REFERENCES `bus`.`mjesto` (`PostanskiBroj`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`linija`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`linija` (
  `IdLinije` INT(11) NOT NULL,
  `NazivLinije` VARCHAR(35) NULL DEFAULT NULL,
  `Peron` INT(11) NOT NULL,
  `JIBPrevoznika` CHAR(10) NOT NULL,
  `DaniUSedmici` SET('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY') NULL DEFAULT NULL,
  PRIMARY KEY (`IdLinije`),
  INDEX `LinijaPrevoznikFK_idx` (`JIBPrevoznika` ASC) VISIBLE,
  CONSTRAINT `LinijaPrevoznikFK`
    FOREIGN KEY (`JIBPrevoznika`)
    REFERENCES `bus`.`prevoznik` (`JIBPrevoznika`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`relacija`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`relacija` (
  `IdRelacije` INT(11) NOT NULL,
  `IdLinije` INT(11) NOT NULL,
  `Polaziste` VARCHAR(35) NULL DEFAULT NULL,
  `Odrediste` VARCHAR(35) NULL DEFAULT NULL,
  `VrijemePolaska` TIME NULL DEFAULT NULL,
  `VrijemeDolaska` TIME NULL DEFAULT NULL,
  `CijenaJednokratna` DOUBLE NULL DEFAULT NULL,
  `CijenaMjesecna` DOUBLE NULL DEFAULT NULL,
  PRIMARY KEY (`IdRelacije`),
  INDEX `R_53` (`IdLinije` ASC) VISIBLE,
  CONSTRAINT `R_53`
    FOREIGN KEY (`IdLinije`)
    REFERENCES `bus`.`linija` (`IdLinije`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`karta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`karta` (
  `SerijskiBroj` INT(11) NOT NULL AUTO_INCREMENT,
  `IdRelacije` INT(11) NULL DEFAULT NULL,
  `Datum` DATE NULL DEFAULT NULL,
  `BrojSjedista` INT(11) NULL DEFAULT NULL,
  `JIBStanice` CHAR(10) NULL DEFAULT NULL,
  `Cijena` DOUBLE NULL DEFAULT NULL,
  PRIMARY KEY (`SerijskiBroj`),
  INDEX `R_55` (`IdRelacije` ASC) VISIBLE,
  INDEX `Karta_Stanica_FK15_idx` (`JIBStanice` ASC) VISIBLE,
  CONSTRAINT `Karta_Stanica_FK15`
    FOREIGN KEY (`JIBStanice`)
    REFERENCES `bus`.`autobuska_stanica` (`JIBStanice`),
  CONSTRAINT `R_55`
    FOREIGN KEY (`IdRelacije`)
    REFERENCES `bus`.`relacija` (`IdRelacije`)
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`mjesecna_karta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`mjesecna_karta` (
  `IdMjesecneKarte` INT(11) NOT NULL AUTO_INCREMENT,
  `Cijena` DOUBLE NULL DEFAULT NULL,
  `Ime` VARCHAR(20) NULL DEFAULT NULL,
  `Prezime` VARCHAR(20) NULL DEFAULT NULL,
  `Slika` VARCHAR(45) NULL DEFAULT NULL,
  `Tip` SET('DJACKA', 'RADNICKA', 'PENZIONERSKA') NULL DEFAULT NULL,
  `SerijskiBroj` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`IdMjesecneKarte`),
  INDEX `Mjesecna_Karta_FK_karta_idx` (`SerijskiBroj` ASC) VISIBLE,
  CONSTRAINT `Mjesecna_Karta_FK_karta`
    FOREIGN KEY (`SerijskiBroj`)
    REFERENCES `bus`.`karta` (`SerijskiBroj`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 32
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
  `Email` VARCHAR(35) NULL DEFAULT NULL,
  `PostanskiBroj` INT(11) NULL DEFAULT NULL,
  `Pol` VARCHAR(10) NULL DEFAULT NULL,
  `JIBStanice` CHAR(10) NOT NULL,
  PRIMARY KEY (`JMBG`),
  INDEX `IdMjesta_fk3_idx` (`PostanskiBroj` ASC) INVISIBLE,
  INDEX `Zaposleni_Stanica_fk16_idx` (`JIBStanice` ASC) VISIBLE,
  CONSTRAINT `Zaposleni_Stanica_fk16`
    FOREIGN KEY (`JIBStanice`)
    REFERENCES `bus`.`autobuska_stanica` (`JIBStanice`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `postanski_fk_broj_zaposleni`
    FOREIGN KEY (`PostanskiBroj`)
    REFERENCES `bus`.`mjesto` (`PostanskiBroj`)
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`nalog`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`nalog` (
  `JMBG` CHAR(13) NOT NULL,
  `KorisnickoIme` VARCHAR(35) NULL DEFAULT NULL,
  `Lozinka` VARCHAR(120) NULL DEFAULT NULL,
  `Tip` VARCHAR(25) NULL DEFAULT NULL,
  `JIBStanice` CHAR(10) NULL DEFAULT NULL,
  PRIMARY KEY (`JMBG`),
  INDEX `Nalog_Stanica_FK17_idx` (`JIBStanice` ASC) VISIBLE,
  CONSTRAINT `Nalog_Stanica_FK17`
    FOREIGN KEY (`JIBStanice`)
    REFERENCES `bus`.`autobuska_stanica` (`JIBStanice`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `R_45`
    FOREIGN KEY (`JMBG`)
    REFERENCES `bus`.`zaposleni` (`JMBG`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`rezervacija`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`rezervacija` (
  `IdRezervacije` INT(11) NOT NULL AUTO_INCREMENT,
  `DatumRezervacije` DATE NULL DEFAULT NULL,
  `KontaktOsoba` VARCHAR(35) NULL DEFAULT NULL,
  `BrojTelefona` VARCHAR(15) NULL DEFAULT NULL,
  `SerijskiBroj` INT(11) NOT NULL,
  PRIMARY KEY (`IdRezervacije`),
  INDEX `RezervacijaFK_Karta_idx` (`SerijskiBroj` ASC) VISIBLE,
  CONSTRAINT `RezervacijaFK_Karta`
    FOREIGN KEY (`SerijskiBroj`)
    REFERENCES `bus`.`karta` (`SerijskiBroj`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 29
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

alter table autobuska_stanica
add Email varchar(50);

alter table nalog
add NalogAktivan tinyint(1);