use bus;
################################# Procedura za autentikaciju ################################
delimiter $$
create procedure checkAuthentication(in pUserName varchar(35), in pPassword varchar(35))
begin 
	select Tip from nalog where KorisnickoIme=pUserName and Lozinka=pPassword ;
	
end $$
delimiter ;
#############################################################################################

######################### Procedura za prikaz svih autobuskih stanica #######################
delimiter $$
create procedure showBusStation()
begin 
	select Naziv,Adresa,BrojPoste,BrojTelefona,BrojPerona from autobuska_stanica;	
end $$
delimiter ;
#############################################################################################