use bus;

delimiter $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `checkAuthentication`(in pUserName varchar(35), in pPassword varchar(120))
begin 
select JIBStanice,Tip from nalog where KorisnickoIme=pUserName and Lozinka=pPassword ;
end $$
delimiter ;use bus;

delimiter $$
create PROCEDURE updatePassword(in pUserName varchar(35), in pNewPassword varchar(120), out rezultat INT(11))
BEGIN
select if( count(*)>0,1,0) into rezultat
from nalog
where KorisnickoIme=pUsername;
UPDATE `bus`.`nalog` SET `Lozinka` = pNewPassword WHERE (`KorisnickoIme` = pUserName);
end $$
delimiter ;use bus;

delimiter $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `selectZaposleni`(in pUserName varchar(35))
begin 
select KorisnickoIme, nalog.JMBG, Ime, Prezime, zaposleni.JMBG, StrucnaSprema, Adresa, PostanskiBroj, BrojTelefona, Email
from nalog join zaposleni on (nalog.JMBG=zaposleni.JMBG) where KorisnickoIme=pUserName;
end $$
delimiter ;

delimiter $$
create procedure showBusStations()
begin 
select Naziv,Adresa,PostanskiBroj,BrojTelefona,BrojPerona from autobuska_stanica;	
end $$
delimiter ;

delimiter $$
CREATE PROCEDURE addBusStation(in pJIBStanice char(10), in pNaziv varchar(35),in pAdresa varchar(35) ,in pPostanskiBroj int(11), in pBrojTelefona varchar(15),in pBrojPerona int(11),in pWebStranica varchar(45),in pEmail varchar(50))
begin 
	INSERT INTO autobuska_stanica(JIBStanice,Naziv,Adresa,PostanskiBroj,BrojTelefona,BrojPerona,WebStranica,Email,StanicaAktivna) VALUES (pJIBStanice,pNaziv,pAdresa,pPostanskiBroj,pBrojTelefona,pBrojPerona,pWebStranica,pEmail,1);
end $$
delimiter ;

delimiter $$
CREATE PROCEDURE addNewEmployee(in pPostanskiBroj int(11), in pAdresa varchar(35), in pStrucnaSprema varchar(35),in pPol varchar(10),in pBrojTelefona varchar(25),in pEmail varchar(35),in pKorisnickoIme varchar(35),in pLozinka varchar(120),in pJIBStanice char(10),in pTip varchar(25),in pIme varchar(35),in pPrezime varchar(35), in pJMBG char(13))
begin 
	INSERT INTO zaposleni (JMBG,Ime,Prezime,Adresa,PostanskiBroj,StrucnaSprema,Pol,BrojTelefona,Email,JIBStanice,ZaposleniAktivan) VALUES (pJMBG,pIme,pPrezime,pAdresa,pPostanskiBroj,pStrucnaSprema,pPol,pBrojTelefona,pEmail,pJIBStanice,1);
    INSERT INTO nalog (JMBG,KorisnickoIme,Lozinka,Tip,JIBStanice,NalogAktivan) VALUES (pJMBG,pKorisnickoIme,pLozinka,pTip,pJIBStanice,1);
end $$
delimiter ;