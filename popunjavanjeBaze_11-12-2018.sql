use bus;

INSERT INTO `bus`.`mjesto` (`PostanskiBroj`, `Naziv`) VALUES ('21000', 'Novi Sad');
INSERT INTO `bus`.`mjesto` (`PostanskiBroj`, `Naziv`) VALUES ('22251', 'Batrovci');
INSERT INTO `bus`.`mjesto` (`PostanskiBroj`, `Naziv`) VALUES ('22402', 'Ruma');
INSERT INTO `bus`.`mjesto` (`PostanskiBroj`, `Naziv`) VALUES ('70260', 'Mrkonjić Grad');
INSERT INTO `bus`.`mjesto` (`PostanskiBroj`, `Naziv`) VALUES ('74400', 'Derventa');
INSERT INTO `bus`.`mjesto` (`PostanskiBroj`, `Naziv`) VALUES ('74480', 'Modriča');
INSERT INTO `bus`.`mjesto` (`PostanskiBroj`, `Naziv`) VALUES ('76100', 'Brčko');
INSERT INTO `bus`.`mjesto` (`PostanskiBroj`, `Naziv`) VALUES ('76300', 'Bijeljina');
INSERT INTO `bus`.`mjesto` (`PostanskiBroj`, `Naziv`) VALUES ('77000', 'Bihać');
INSERT INTO `bus`.`mjesto` (`PostanskiBroj`, `Naziv`) VALUES ('78000', 'Banja Luka');
INSERT INTO `bus`.`mjesto` (`PostanskiBroj`, `Naziv`) VALUES ('78400', 'Gradiška');
INSERT INTO `bus`.`mjesto` (`PostanskiBroj`, `Naziv`) VALUES ('78430', 'Prnjavor');
INSERT INTO `bus`.`mjesto` (`PostanskiBroj`, `Naziv`) VALUES ('79102', 'Prijedor');
INSERT INTO `bus`.`mjesto` (`PostanskiBroj`, `Naziv`) VALUES ('101801', 'Beograd');



INSERT INTO `bus`.`autobuska_stanica` (`JIBStanice`, `Naziv`, `Adresa`, `PostanskiBroj`, `BrojTelefona`, `BrojPerona`, `WebStranica`, `StanicaAktivna`) VALUES ('1111111111', 'Stanica Banja Luka', 'Neznanih Junaka 14', '78000', '051/123-321', '25', 'https://www.busbl.com', '1');
INSERT INTO `bus`.`autobuska_stanica` (`JIBStanice`, `Naziv`, `Adresa`, `PostanskiBroj`, `BrojTelefona`, `BrojPerona`, `WebStranica`, `StanicaAktivna`) VALUES ('2222222222', 'Stanica Gradiška', 'Miloša Obilića 55', '78400', '055/441-512', '15', 'https://www.gradiskastanica.com', '1');
INSERT INTO `bus`.`autobuska_stanica` (`JIBStanice`, `Naziv`, `Adresa`, `PostanskiBroj`, `BrojTelefona`, `BrojPerona`, `WebStranica`, `StanicaAktivna`) VALUES ('3333333333', 'Stanica Prijedor', 'Cara Lazara 13', '79102', '051/512-767', '13', 'https://www.prijedorskastanica.com', '1');



INSERT INTO `bus`.`zaposleni` (`JMBG`, `Ime`, `Prezime`, `Adresa`, `BrojTelefona`, `StrucnaSprema`, `Email`, `PostanskiBroj`, `Pol`, `JIBStanice`) VALUES ('1210993101010', 'Milan', 'Paspalj', 'Čatrnja bb', '065/123-123', 'SSS', 'milanp93@hotmail.com', '78400', 'M', '1111111111');
INSERT INTO `bus`.`zaposleni` (`JMBG`, `Ime`, `Prezime`, `Adresa`, `BrojTelefona`, `StrucnaSprema`, `Email`, `PostanskiBroj`, `Pol`, `JIBStanice`) VALUES ('1012993103251', 'Vanja', 'Ćulum', 'Cara Lazara 100', '065/456-456', 'SSS', '93vanja@gmail.com', '78000', 'M', '1111111111');
INSERT INTO `bus`.`zaposleni` (`JMBG`, `Ime`, `Prezime`, `Adresa`, `BrojTelefona`, `StrucnaSprema`, `Email`, `PostanskiBroj`, `Pol`, `JIBStanice`) VALUES ('1506988196387', 'Marko', 'Knežić', 'Dragocaj bb', '065/789-789', 'SSS', 'mknezic88@gmail.com', '78000', 'M', '1111111111');


INSERT INTO `bus`.`nalog` (`JMBG`, `KorisnickoIme`, `Lozinka`, `Tip`, `JIBStanice`) VALUES ('1210993101010', 'mcfc93', '264C8C381BF16C982A4E59B0DD4C6F7808C51A05F64C35DB42CC78A2A72875BB', 'Administrator', '1111111111');
INSERT INTO `bus`.`nalog` (`JMBG`, `KorisnickoIme`, `Lozinka`, `Tip`, `JIBStanice`) VALUES ('1012993103251', 'vculum', '5E884898DA28047151D0E56F8DC6292773603D0D6AABBDD62A11EF721D1542D8', 'SalterRadnik', '2222222222');
INSERT INTO `bus`.`nalog` (`JMBG`, `KorisnickoIme`, `Lozinka`, `Tip`, `JIBStanice`) VALUES ('1506988196387', 'mknezic', '5E884898DA28047151D0E56F8DC6292773603D0D6AABBDD62A11EF721D1542D8', 'AdministrativniRadnik', '2222222222');



INSERT INTO `bus`.`prevoznik` VALUES ('4444444444', 'Smiljic', '064/111-222', 'smiljic@gmail.com', 'www.smiljic.com', '15212252241', 'Cara Lazar 120', 70260);
INSERT INTO `bus`.`prevoznik` VALUES ('5555555555', 'BocacTours', '53252', 'vas@gmail.com', 'fwwfa.fff', '1512555', 'Marka Kraljevica 15', 70260);



INSERT INTO `bus`.`linija` VALUES (11, 'Banja Luka - Novi Sad', 1, '4444444444', 'MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY');
INSERT INTO `bus`.`linija` VALUES (12, 'Banja Luka - Novi Sad (HRV)', 4, '4444444444', 'MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY');
INSERT INTO `bus`.`linija` VALUES (13, 'Gradiska - Prijedor', 2, '4444444444', 'MONDAY');
INSERT INTO `bus`.`linija` VALUES (14, 'Novi Sad - Banja Luka', 3, '4444444444', 'MONDAY,TUESDAY,WEDNESDAY');



INSERT INTO `bus`.`relacija` VALUES (401,11,'Banja Luka', 'Prnjavor', '12:00:00', '13:00:00', 4, 30);
INSERT INTO `bus`.`relacija` VALUES (402, 11, 'Banja Luka', 'Derventa', '12:00:00', '14:00:00', 7.5, 45);
INSERT INTO `bus`.`relacija` VALUES (403, 11, 'Banja Luka', 'Modrica', '12:00:00', '15:00:00', 10, 70);
INSERT INTO `bus`.`relacija` VALUES (404, 11, 'Banja Luka', 'Brcko', '12:00:00', '16:00:00', 25, 300);
INSERT INTO `bus`.`relacija` VALUES (405, 11, 'Banja Luka', 'Bijeljina', '12:00:00', '17:00:00', 29, 400);
INSERT INTO `bus`.`relacija` VALUES (406, 11, 'Banja Luka', 'Ruma', '12:00:00', '18:00:00', 35, 450);
INSERT INTO `bus`.`relacija` VALUES (407, 11, 'Banja Luka', 'Novi Sad', '12:00:00', '18:30:00', 45, 500);
INSERT INTO `bus`.`relacija` VALUES (408, 12, 'Banja Luka', 'Gradiska', '20:00:00', '21:00:00', 5, 30);
INSERT INTO `bus`.`relacija` VALUES (409, 12, 'Banja Luka', 'Batrovci', '20:00:00', '00:00:00', 30, 250);
INSERT INTO `bus`.`relacija` VALUES (410, 12, 'Banja Luka', 'Ruma', '20:00:00', '00:30:00', 35, 400);
INSERT INTO `bus`.`relacija` VALUES (411, 12, 'Banja Luka', 'Novi Sad', '20:00:00', '01:00:00', 48, 250);
INSERT INTO `bus`.`relacija` VALUES (444, 14, 'Novi Sad', 'Banja Luka', '15:00:00', '20:00:00', 50, 200);
INSERT INTO `bus`.`relacija` VALUES (450, 11, 'Brcko', 'Bijeljina', '16:00:00', '17:00:00', 6, 40);
INSERT INTO `bus`.`relacija` VALUES (456, 11, 'Batrovci', 'Novi Sad', '20:00:00', '23:00:00', 15, 250);
INSERT INTO `bus`.`relacija` VALUES (470, 14, 'Novi Sad', 'Batrovci', '15:00:00', '17:00:00', 7, 500);