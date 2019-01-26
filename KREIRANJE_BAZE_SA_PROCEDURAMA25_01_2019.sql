
-- -----------------------------------------------------
-- Schema bus
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema bus
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `bus` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `bus` ;

-- -----------------------------------------------------
-- Table `bus`.`mjesto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`mjesto` (
  `PostanskiBroj` INT(11) NOT NULL,
  `Naziv` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`PostanskiBroj`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`autobusko_stajaliste`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`autobusko_stajaliste` (
  `IdStajalista` INT(11) NOT NULL AUTO_INCREMENT,
  `Naziv` VARCHAR(45) NULL DEFAULT NULL,
  `PostanskiBroj` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`IdStajalista`),
  INDEX `FK_MJESTA_idx` (`PostanskiBroj` ASC) VISIBLE,
  CONSTRAINT `FK_MJESTA`
    FOREIGN KEY (`PostanskiBroj`)
    REFERENCES `bus`.`mjesto` (`PostanskiBroj`)
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 152
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`autobuska_stanica`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`autobuska_stanica` (
  `JIBStanice` CHAR(13) NOT NULL,
  `Naziv` VARCHAR(50) NULL DEFAULT NULL,
  `Adresa` VARCHAR(50) NULL DEFAULT NULL,
  `PostanskiBroj` INT(11) NULL DEFAULT NULL,
  `BrojTelefona` VARCHAR(25) NULL DEFAULT NULL,
  `BrojPerona` INT(11) NULL DEFAULT NULL,
  `WebStranica` VARCHAR(60) NULL DEFAULT NULL,
  `Email` VARCHAR(60) NULL DEFAULT NULL,
  `IdStajalista` INT(11) NULL DEFAULT NULL,
  `Stanje` SET('Aktivno', 'Blokirano', 'Izbrisano') NULL DEFAULT 'Aktivno',
  PRIMARY KEY (`JIBStanice`),
  INDEX `IDMjesta_fk_idx` (`PostanskiBroj` ASC) VISIBLE,
  INDEX `IDStajalista_fk_idx` (`IdStajalista` ASC) VISIBLE,
  CONSTRAINT `IDMjesta_fk`
    FOREIGN KEY (`PostanskiBroj`)
    REFERENCES `bus`.`mjesto` (`PostanskiBroj`)
    ON UPDATE CASCADE,
  CONSTRAINT `IDStajalista_fk`
    FOREIGN KEY (`IdStajalista`)
    REFERENCES `bus`.`autobusko_stajaliste` (`IdStajalista`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`dani`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`dani` (
  `Id` INT(11) NOT NULL,
  `Opis` VARCHAR(120) NULL DEFAULT NULL,
  PRIMARY KEY (`Id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`prevoznik`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`prevoznik` (
  `JIBPrevoznika` CHAR(13) NOT NULL,
  `NazivPrevoznika` VARCHAR(40) NULL DEFAULT NULL,
  `Telefon` VARCHAR(35) NULL DEFAULT NULL,
  `Email` VARCHAR(60) NULL DEFAULT NULL,
  `WebAdresa` VARCHAR(60) NULL DEFAULT NULL,
  `TekuciRacun` VARCHAR(18) NULL DEFAULT NULL,
  `Adresa` VARCHAR(45) NULL DEFAULT NULL,
  `PostanskiBroj` INT(11) NULL DEFAULT NULL,
  `Stanje` SET('Aktivno', 'Blokirano', 'Izbrisano') NULL DEFAULT 'Aktivno',
  PRIMARY KEY (`JIBPrevoznika`),
  INDEX `PrevoznikMjesto_fk_idx` (`PostanskiBroj` ASC) VISIBLE,
  CONSTRAINT `PrevoznikMjesto_fk`
    FOREIGN KEY (`PostanskiBroj`)
    REFERENCES `bus`.`mjesto` (`PostanskiBroj`)
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`linija`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`linija` (
  `IdLinije` INT(11) NOT NULL AUTO_INCREMENT,
  `NazivLinije` VARCHAR(255) NULL DEFAULT NULL,
  `Peron` INT(11) NOT NULL,
  `JIBPrevoznika` CHAR(13) NOT NULL,
  `Stanje` SET('Aktivno', 'Blokirano', 'Izbrisano') NULL DEFAULT 'Aktivno',
  `VoznjaPraznikom` INT(11) NULL DEFAULT NULL,
  `PStajaliste` INT(11) NULL DEFAULT NULL,
  `OStajaliste` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`IdLinije`),
  INDEX `LinijaFKPrevoznik_idx` (`JIBPrevoznika` ASC) VISIBLE,
  INDEX `VoznjaPraznikomFK_idx` (`VoznjaPraznikom` ASC) VISIBLE,
  INDEX `PstajalistaLinija_idx` (`PStajaliste` ASC) VISIBLE,
  INDEX `OstajaisteLinija_idx` (`OStajaliste` ASC) VISIBLE,
  CONSTRAINT `LinijaFKPrevoznik`
    FOREIGN KEY (`JIBPrevoznika`)
    REFERENCES `bus`.`prevoznik` (`JIBPrevoznika`),
  CONSTRAINT `OstajaisteLinija`
    FOREIGN KEY (`OStajaliste`)
    REFERENCES `bus`.`autobusko_stajaliste` (`IdStajalista`),
  CONSTRAINT `PstajalistaLinija`
    FOREIGN KEY (`PStajaliste`)
    REFERENCES `bus`.`autobusko_stajaliste` (`IdStajalista`),
  CONSTRAINT `VoznjaPraznikomFK`
    FOREIGN KEY (`VoznjaPraznikom`)
    REFERENCES `bus`.`dani` (`Id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`relacija`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`relacija` (
  `IdRelacije` INT(11) NOT NULL AUTO_INCREMENT,
  `IdLinije` INT(11) NOT NULL,
  `Polaziste` INT(11) NOT NULL,
  `Odrediste` INT(11) NULL DEFAULT NULL,
  `CijenaJednokratna` DOUBLE NULL DEFAULT NULL,
  `TrajanjePuta` TIME NULL DEFAULT NULL,
  `CijenaMjesecna` DOUBLE NULL DEFAULT NULL,
  PRIMARY KEY (`IdRelacije`),
  INDEX `rel_linijafk_24_idx` (`IdLinije` ASC) VISIBLE,
  INDEX `Stajaliste_FK_idx` (`Polaziste` ASC) VISIBLE,
  INDEX `Stajaliste_FK@_idx` (`Odrediste` ASC) VISIBLE,
  CONSTRAINT `Stajaliste_FK`
    FOREIGN KEY (`Polaziste`)
    REFERENCES `bus`.`autobusko_stajaliste` (`IdStajalista`),
  CONSTRAINT `Stajaliste_FK@`
    FOREIGN KEY (`Odrediste`)
    REFERENCES `bus`.`autobusko_stajaliste` (`IdStajalista`),
  CONSTRAINT `rel_linijafk_24`
    FOREIGN KEY (`IdLinije`)
    REFERENCES `bus`.`linija` (`IdLinije`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`karta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`karta` (
  `SerijskiBroj` INT(11) NOT NULL AUTO_INCREMENT,
  `IdRelacije` INT(11) NULL DEFAULT NULL,
  `DatumPolaska` DATE NULL DEFAULT NULL,
  `DatumIzdavanja` DATE NULL DEFAULT NULL,
  `BrojSjedista` INT(11) NULL DEFAULT NULL,
  `JIBStanice` CHAR(13) NULL DEFAULT NULL,
  `Cijena` DOUBLE NULL DEFAULT NULL,
  `Stanje` SET('Aktivno', 'Stornirano') NULL DEFAULT 'Aktivno',
  PRIMARY KEY (`SerijskiBroj`),
  INDEX `karta_relacija_Foreign16_idx` (`IdRelacije` ASC) VISIBLE,
  INDEX `karta_astanica_fk_idx` (`JIBStanice` ASC) VISIBLE,
  CONSTRAINT `karta_astanica_fk`
    FOREIGN KEY (`JIBStanice`)
    REFERENCES `bus`.`autobuska_stanica` (`JIBStanice`),
  CONSTRAINT `karta_relacija_Foreign16`
    FOREIGN KEY (`IdRelacije`)
    REFERENCES `bus`.`relacija` (`IdRelacije`))
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
  `Slika` VARCHAR(500) NULL DEFAULT NULL,
  `Tip` SET('ĐAČKA', 'OBIČNA', 'PENZIONERSKA') NULL DEFAULT NULL,
  `SerijskiBroj` INT(11) NULL DEFAULT NULL,
  `Stanje` SET('Aktivno', 'Stornirano') NULL DEFAULT 'Aktivno',
  PRIMARY KEY (`IdMjesecneKarte`),
  INDEX `Mjesecna_Karta_FK_karta_idx` (`SerijskiBroj` ASC) VISIBLE,
  CONSTRAINT `Mjesecna_Karta_FK_karta`
    FOREIGN KEY (`SerijskiBroj`)
    REFERENCES `bus`.`karta` (`SerijskiBroj`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`zaposleni`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`zaposleni` (
  `JMBG` CHAR(13) NOT NULL,
  `Ime` VARCHAR(20) NULL DEFAULT NULL,
  `Prezime` VARCHAR(20) NULL DEFAULT NULL,
  `Adresa` VARCHAR(50) NULL DEFAULT NULL,
  `BrojTelefona` VARCHAR(25) NULL DEFAULT NULL,
  `StrucnaSprema` VARCHAR(35) NULL DEFAULT NULL,
  `Email` VARCHAR(60) NULL DEFAULT NULL,
  `PostanskiBroj` INT(11) NULL DEFAULT NULL,
  `Stanje` SET('Aktivno', 'Blokirano', 'Izbrisano') NULL DEFAULT 'Aktivno',
  `Pol` SET('Muški', 'Ženski') NULL DEFAULT NULL,
  PRIMARY KEY (`JMBG`),
  INDEX `IdMjesta_fk3_idx` (`PostanskiBroj` ASC) INVISIBLE,
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
  `KorisnickoIme` VARCHAR(35) NOT NULL,
  `JMBG` CHAR(13) NOT NULL,
  `Lozinka` VARCHAR(120) NULL DEFAULT NULL,
  `Tip` SET('Šalterski radnik', 'Administrativni radnik', 'Administrator') NULL DEFAULT NULL,
  `JIBStanice` CHAR(13) NULL DEFAULT NULL,
  `Stanje` SET('Aktivno', 'Blokirano', 'Izbrisano') NULL DEFAULT 'Aktivno',
  PRIMARY KEY (`KorisnickoIme`),
  INDEX `JMBG` (`JMBG` ASC) VISIBLE,
  INDEX `nalog_astanica_fk_idx` (`JIBStanice` ASC) VISIBLE,
  CONSTRAINT `nalog_astanica_fk`
    FOREIGN KEY (`JIBStanice`)
    REFERENCES `bus`.`autobuska_stanica` (`JIBStanice`),
  CONSTRAINT `nalog_ibfk_1`
    FOREIGN KEY (`JMBG`)
    REFERENCES `bus`.`zaposleni` (`JMBG`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`popust_prevoznika`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`popust_prevoznika` (
  `IdPopust` INT(11) NOT NULL,
  `DjackiPopust` DOUBLE NULL DEFAULT '1',
  `PenzionerskiPopust` DOUBLE NULL DEFAULT '1',
  `RadnickiPopust` DOUBLE NULL DEFAULT '1',
  `JIBPrevoznika` CHAR(13) NOT NULL,
  PRIMARY KEY (`IdPopust`),
  INDEX `JIBPrevoznika_fk_idx` (`JIBPrevoznika` ASC) VISIBLE,
  CONSTRAINT `JIBPrevoznika_fk`
    FOREIGN KEY (`JIBPrevoznika`)
    REFERENCES `bus`.`prevoznik` (`JIBPrevoznika`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`praznik`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`praznik` (
  `IdPraznika` INT(11) NOT NULL AUTO_INCREMENT,
  `Datum` CHAR(5) NULL DEFAULT NULL,
  `Opis` VARCHAR(120) NULL DEFAULT NULL,
  PRIMARY KEY (`IdPraznika`))
ENGINE = InnoDB
AUTO_INCREMENT = 13
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`red_voznje`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`red_voznje` (
  `IdRedVoznje` INT(11) NOT NULL AUTO_INCREMENT,
  `VrijemePolaska` TIME NULL DEFAULT NULL,
  `VrijemeDolaska` TIME NULL DEFAULT NULL,
  `VrijemePolaskaPovratna` TIME NULL DEFAULT NULL,
  `VrijemeDolaskaPovratna` TIME NULL DEFAULT NULL,
  `Dani` VARCHAR(15) NULL DEFAULT NULL,
  `IdRelacije` INT(11) NULL DEFAULT NULL,
  `IdPolaska` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`IdRedVoznje`),
  INDEX `IdRelacijeRedVoznje_idx` (`IdRelacije` ASC) VISIBLE,
  CONSTRAINT `IdRelacijeRedVoznje`
    FOREIGN KEY (`IdRelacije`)
    REFERENCES `bus`.`relacija` (`IdRelacije`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bus`.`rezervacija`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bus`.`rezervacija` (
  `IdRezervacije` INT(11) NOT NULL AUTO_INCREMENT,
  `DatumRezervacije` DATE NULL DEFAULT NULL,
  `KontaktOsoba` VARCHAR(50) NULL DEFAULT NULL,
  `BrojTelefona` VARCHAR(25) NULL DEFAULT NULL,
  `SerijskiBroj` INT(11) NOT NULL,
  `Stanje` SET('Aktivno', 'Stornirano') NULL DEFAULT 'Aktivno',
  PRIMARY KEY (`IdRezervacije`),
  INDEX `RezervacijaFK_Karta_idx` (`SerijskiBroj` ASC) VISIBLE,
  CONSTRAINT `RezervacijaFK_Karta`
    FOREIGN KEY (`SerijskiBroj`)
    REFERENCES `bus`.`karta` (`SerijskiBroj`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

ALTER TABLE `bus`.`red_voznje` 
ADD COLUMN `Stanje` SET('Aktivno', 'Izbrisano') NULL DEFAULT 'Aktivno' AFTER `IdPolaska`;

ALTER TABLE autobusko_stajaliste AUTO_INCREMENT = 1;
ALTER TABLE linija AUTO_INCREMENT = 1;
ALTER TABLE relacija AUTO_INCREMENT = 1;
ALTER TABLE karta AUTO_INCREMENT = 1;
ALTER TABLE mjesecna_karta AUTO_INCREMENT = 1;
ALTER TABLE praznik AUTO_INCREMENT = 1;
ALTER TABLE rezervacija AUTO_INCREMENT = 1;
ALTER TABLE red_voznje AUTO_INCREMENT = 1;

-- -----------------------------------------------------
-- procedure addBusStation
-- -----------------------------------------------------

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `addBusStation`(in pJIBStanice char(13), in pNaziv varchar(35),in pAdresa varchar(35) ,in pPostanskiBroj int(11), in pBrojTelefona varchar(15),in pBrojPerona int(11),in pWebStranica varchar(45),in pEmail varchar(50))
begin
	INSERT INTO autobusko_stajaliste(Naziv,PostanskiBroj) VALUES (pNaziv,pPostanskiBroj);
	INSERT INTO autobuska_stanica(JIBStanice,Naziv,Adresa,PostanskiBroj,BrojTelefona,BrojPerona,WebStranica,Email,IdStajalista,Stanje) VALUES (pJIBStanice,pNaziv,pAdresa,pPostanskiBroj,pBrojTelefona,pBrojPerona,pWebStranica,pEmail,LAST_INSERT_ID(),'Aktivno');
end$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure addNewEmployee
-- -----------------------------------------------------

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `addNewEmployee`(in pKorisnickoIme varchar(35),in pLozinka varchar(120),in pJIBStanice char(13),in pTip varchar(25),in pIme varchar(35),in pPrezime varchar(35), in pJMBG char(13), in pPol varchar(10), in pAdresa varchar(35), in pPostanskiBroj int(11), in pStrucnaSprema varchar(35),in pBrojTelefona varchar(25),in pEmail varchar(35))
begin 
	INSERT INTO zaposleni (JMBG,Ime,Prezime,Adresa,PostanskiBroj,StrucnaSprema,Pol,BrojTelefona,Email,Stanje) VALUES (pJMBG,pIme,pPrezime,pAdresa,pPostanskiBroj,pStrucnaSprema,pPol,pBrojTelefona,pEmail,'Aktivno');
    INSERT INTO nalog (JMBG,KorisnickoIme,Lozinka,Tip,JIBStanice,Stanje) VALUES (pJMBG,pKorisnickoIme,pLozinka,pTip,pJIBStanice,'Aktivno');
end$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure blockBusStation
-- -----------------------------------------------------

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `blockBusStation`(in pJIBStanice char(13), in pStanje VARCHAR(10))
begin 
	update autobuska_stanica set Stanje=pStanje where JIBStanice=pJIBStanice;
	update nalog, zaposleni set nalog.Stanje=pStanje, zaposleni.Stanje=pStanje where nalog.JIBStanice=pJIBStanice and nalog.JMBG=zaposleni.JMBG and nalog.Stanje!='Izbrisano';
end$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure checkAuthentication
-- -----------------------------------------------------

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `checkAuthentication`(in pUserName varchar(35), in pPassword varchar(120))
begin 
	select JIBStanice,Tip from nalog where KorisnickoIme=pUserName and Lozinka=pPassword and Stanje='Aktivno' ;
end$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure getInfoList
-- -----------------------------------------------------

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getInfoList`(in pPolasciIliDolasci int(1), in pIdStajalista int(11))
begin 
if (pPolasciIliDolasci=1) then
select Dani,VoznjaPraznikom,linija.Peron,prevoznik.NazivPrevoznika,NazivLinije,VrijemePolaska from relacija join (linija,prevoznik,red_voznje) 
on (relacija.IdRelacije=red_voznje.IdRelacije and relacija.IdLinije=linija.IdLinije and linija.JIBPrevoznika=prevoznik.JIBPrevoznika and linija.Stanje='Aktivno') 
where Polaziste=pIdStajalista
union
select Dani,VoznjaPraznikom,linija.Peron,prevoznik.NazivPrevoznika,NazivLinije,VrijemePolaskaPovratna from relacija join (linija,prevoznik,red_voznje) 
on (relacija.IdRelacije=red_voznje.IdRelacije and relacija.IdLinije=linija.IdLinije and linija.JIBPrevoznika=prevoznik.JIBPrevoznika and linija.Stanje='Aktivno')
where Odrediste=pIdStajalista
order by VrijemePolaska;
else
select Dani,VoznjaPraznikom,linija.Peron,prevoznik.NazivPrevoznika,NazivLinije,VrijemeDolaska from relacija join (linija,prevoznik,red_voznje) 
on (relacija.IdRelacije=red_voznje.IdRelacije and relacija.IdLinije=linija.IdLinije and linija.JIBPrevoznika=prevoznik.JIBPrevoznika and linija.Stanje='Aktivno')
where Odrediste=pIdStajalista
union
select Dani,VoznjaPraznikom,linija.Peron,prevoznik.NazivPrevoznika,NazivLinije,VrijemeDolaskaPovratna from relacija join (linija,prevoznik,red_voznje) 
on (relacija.IdRelacije=red_voznje.IdRelacije and relacija.IdLinije=linija.IdLinije and linija.JIBPrevoznika=prevoznik.JIBPrevoznika and linija.Stanje='Aktivno') 
where Polaziste=pIdStajalista
order by VrijemeDolaska;
end if;
end$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure removeBusStation
-- -----------------------------------------------------

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `removeBusStation`(in pJIBStanice char(13))
begin 
	update autobuska_stanica set Stanje="Izbrisano" where JIBStanice=pJIBStanice;
	update nalog, zaposleni set nalog.Stanje="Izbrisano", zaposleni.Stanje="Izbrisano" where nalog.JIBStanice=pJIBStanice and nalog.JMBG=zaposleni.JMBG;
end$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure removeEmployee
-- -----------------------------------------------------

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `removeEmployee`(in pJMBG char(13))
begin 
	update zaposleni set Stanje='Izbrisano' where JMBG=pJMBG;
    update nalog set Stanje='Izbrisano' where JMBG=pJMBG;
end$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure removePrevoznik
-- -----------------------------------------------------

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `removePrevoznik`(in pJIBPrevoznika char(13))
begin 
	update prevoznik set Stanje="Izbrisano" where JIBPrevoznika=pJIBPrevoznika;
	update linija set Stanje="Izbrisano" where JIBPrevoznika=pJIBPrevoznika;
end$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure selectZaposleni
-- -----------------------------------------------------

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `selectZaposleni`(in pUserName varchar(35))
begin 
	select KorisnickoIme, Ime, Prezime, zaposleni.JMBG, StrucnaSprema, Adresa, zaposleni.PostanskiBroj, BrojTelefona, Email, Pol, mjesto.Naziv
	from nalog join (zaposleni,mjesto) on (nalog.JMBG=zaposleni.JMBG and zaposleni.PostanskiBroj=mjesto.PostanskiBroj) where KorisnickoIme=pUserName;
end$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure showBusStations
-- -----------------------------------------------------

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `showBusStations`()
begin 
	select JIBStanice,autobuska_stanica.Naziv,Adresa,autobuska_stanica.PostanskiBroj,mjesto.Naziv,BrojTelefona,BrojPerona,WebStranica,Email,IdStajalista,Stanje from autobuska_stanica join mjesto on (autobuska_stanica.PostanskiBroj=mjesto.PostanskiBroj) where Stanje!='Izbrisano';	
end$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure showEmployees
-- -----------------------------------------------------

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `showEmployees`()
begin 
	select KorisnickoIme, Lozinka, nalog.JIBStanice, Tip, Nalog.JMBG, Ime, Prezime
    from zaposleni join nalog on (zaposleni.JMBG=nalog.JMBG) where nalog.Stanje!='Izbrisano' and zaposleni.Stanje!='Izbrisano';
end$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure updatePassword
-- -----------------------------------------------------

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `updatePassword`(in pUserName varchar(35), in pNewPassword varchar(120), out rezultat INT(11))
BEGIN
	select if( count(*)>0,1,0) into rezultat from nalog where KorisnickoIme=pUsername;
UPDATE `bus`.`nalog` SET `Lozinka` = pNewPassword WHERE (`KorisnickoIme` = pUserName);
end$$

DELIMITER ;
