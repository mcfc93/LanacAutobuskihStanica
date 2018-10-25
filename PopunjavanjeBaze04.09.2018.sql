use bus;

# MJESTO
INSERT INTO `bus`.`mjesto` (`BrojPoste`,`Naziv`) VALUES (78000,'Banja Luka');
INSERT INTO `bus`.`mjesto` (`BrojPoste`,`Naziv`) VALUES (55000,'Gradiska');
INSERT INTO `bus`.`mjesto` (`BrojPoste`,`Naziv`) VALUES (48000,'Prijedor');

# AUTOBUSKA STANICA
INSERT INTO `bus`.`autobuska_stanica` (`Naziv`,`Adresa`,`BrojPoste`,`BrojTelefona`,`BrojPerona`) VALUES ('Stanica Banja Luka','Neznanih junaka 14',78000,'051/123-321',20);
INSERT INTO `bus`.`autobuska_stanica` (`Naziv`,`Adresa`,`BrojPoste`,`BrojTelefona`,`BrojPerona`) VALUES ('Stanica Gradiska','Milosa Obilica 55',55000,'051/458-788',18);
INSERT INTO `bus`.`autobuska_stanica` (`Naziv`,`Adresa`,`BrojPoste`,`BrojTelefona`,`BrojPerona`) VALUES ('Stanica Prijedor','Cara Lazara 39',48000,'051/987-987',15);

#LINIJA
INSERT INTO `bus`.`linija` (`IdLinije`,`NazivLinije`,`IdStanice`) VALUES ('11','Banja Luka - Prijedor',7);
INSERT INTO `bus`.`linija` (`IdLinije`,`NazivLinije`,`IdStanice`) VALUES ('12','Banja Luka - Gradiska',7);
INSERT INTO `bus`.`linija` (`IdLinije`,`NazivLinije`,`IdStanice`) VALUES ('13','Gradiska - Prijedor',8);

#PREVOZNIK
INSERT INTO `bus`.`prevoznik` (`IdPrevoznika`,`NazivPrevoznika`,`Telefon`,`Email`,`WEBAdresa`,`TekuciRacun`,`BrojPoste`,`IdLinije`) VALUES ('1111','Pavlovic turs','051/545-323','pavlovic_turs@mail.com','www.pavlovicturs.com','444-0000055533-222','78000','12');
INSERT INTO `bus`.`prevoznik` (`IdPrevoznika`,`NazivPrevoznika`,`Telefon`,`Email`,`WEBAdresa`,`TekuciRacun`,`BrojPoste`,`IdLinije`) VALUES ('2222','Bocac tours','051/222-333','bocactours@mail.com','www.bocactourse.com','555-0000055533-333','78000','11');
INSERT INTO `bus`.`prevoznik` (`IdPrevoznika`,`NazivPrevoznika`,`Telefon`,`Email`,`WEBAdresa`,`TekuciRacun`,`BrojPoste`,`IdLinije`) VALUES ('333','Smiljic','051/654-354','smiljicprevoz@mail.com','www.smiljic.com','222-0000044499-111','55000','13');

#ZAPOSLENI
INSERT INTO `bus`.`zaposleni` (`JMBG`, `Ime`, `Prezime`, `Adresa`, `BrojTelefona`, `StrucnaSprema`, `IdStanice`, `BrojPoste`, `Email`) VALUES ('1506988196387', 'Marko', 'Knezic', 'Dragocaj bb', '065/767-748', 'tehnicar', '7', '78000', 'mknezic88@gmail.com');
INSERT INTO `bus`.`zaposleni` (`JMBG`, `Ime`, `Prezime`, `Adresa`, `BrojTelefona`, `StrucnaSprema`, `IdStanice`, `BrojPoste`, `Email`) VALUES ('2509994362654', 'Milan', 'Paspalj', 'Dusanovov bb', '065/123-456', 'SSS', '8', '55000', 'milanpaspalj@gmail.com');

#NALOG
INSERT INTO `bus`.`nalog` (`KorisnickoIme`, `Lozinka`, `Tip`, `JMBG`) VALUES ('mknezic', '5E884898DA28047151D0E56F8DC6292773603D0D6AABBDD62A11EF721D1542D8', 'Administrator', '1506988196387');
INSERT INTO `bus`.`nalog` (`KorisnickoIme`, `Lozinka`, `Tip`, `JMBG`) VALUES ('mpaspalj', '5E884898DA28047151D0E56F8DC6292773603D0D6AABBDD62A11EF721D1542D8', 'Administrator', '2509994362654');

#REZERVACIJA
INSERT INTO `bus`.`rezervacija` (`IdRezervacije`, `DatumRezervacije`, `BrojKarata`, `ImePrezimePutnika`, `KontaktOsoba`, `IdStanice`) VALUES ('0001', '2018-09-15', '10', 'Janko Jankovic', 'Petar', '7');
INSERT INTO `bus`.`rezervacija` (`IdRezervacije`, `DatumRezervacije`, `BrojKarata`, `ImePrezimePutnika`, `KontaktOsoba`, `IdStanice`) VALUES ('0002', '2018-09-20', '4', 'Bojan Bojanic', 'Petar', '7');
INSERT INTO `bus`.`rezervacija` (`IdRezervacije`, `DatumRezervacije`, `BrojKarata`, `ImePrezimePutnika`, `KontaktOsoba`, `IdStanice`) VALUES ('0003', '2018-10-01', '20', 'Goran Zabic', 'Milena', '8');

#RELACIJA
INSERT INTO `bus`.`relacija` (`IdRelacije`, `IdLinije`, `Ishodiste`, `Odrediste`) VALUES ('401', '11', 'Banja Luka', 'Prijedor');
INSERT INTO `bus`.`relacija` (`IdRelacije`, `IdLinije`, `Ishodiste`, `Odrediste`) VALUES ('402', '11', 'Banja Luka', 'Gradiska');
INSERT INTO `bus`.`relacija` (`IdRelacije`, `IdLinije`, `Ishodiste`, `Odrediste`) VALUES ('403', '12', 'Gradiska', 'Prijedor');
INSERT INTO `bus`.`relacija` (`IdRelacije`, `IdLinije`, `Ishodiste`, `Odrediste`) VALUES ('404', '12', 'Gradiska', 'Doboj');

#KARTA
INSERT INTO `bus`.`karta` (`IdKarte`, `IdStanice`, `JMBG`, `IdRelacije`, `IdRezervacije`) VALUES ('1', '7', '1506988196387', '401', '0001');
INSERT INTO `bus`.`karta` (`IdKarte`, `IdStanice`, `JMBG`, `IdRelacije`, `IdRezervacije`) VALUES ('2', '8', '2509994362654', '403', '0002');
INSERT INTO `bus`.`karta` (`IdKarte`, `IdStanice`, `JMBG`, `IdRelacije`) VALUES ('3', '7', '2509994362654', '402');

#JEDNOKRATNA KARTA
INSERT INTO `bus`.`jednokratna_karta` (`IdKarte`, `Cijena`, `IdRelacije`, `VrijemePolaska`, `DatumPolaska`, `DatumIzdavanja`, `Peron`, `BrojSjedista`, `Izdao`, `JMBG`) VALUES ('1', '10', '401', '15:00:00', '2018-09-05', '2018-09-04', '12', '21', 'Milan', '2509994362654');
INSERT INTO `bus`.`jednokratna_karta` (`IdKarte`, `Cijena`, `IdRelacije`, `VrijemePolaska`, `DatumPolaska`, `DatumIzdavanja`, `Peron`, `BrojSjedista`, `Izdao`, `JMBG`) VALUES ('2', '15', '402', '10:00:00', '2018-09-04', '2018-09-04', '9', '6', 'Marko', '1506988196387');

#MJESECNA KARTAINSERT 
INSERT INTO `bus`.`mjesecna_karta` (`IdKarte`, `Cijena`, `MjesecVazenja`, `Ime`, `Prezime`, `Tip`) VALUES ('3', '45', 'Septembar', 'Dragana ', 'Milosevic', 'Djacka');



