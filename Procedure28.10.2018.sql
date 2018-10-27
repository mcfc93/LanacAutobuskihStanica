use bus;

delimiter $$
create procedure checkAuthentication(in pUserName varchar(35), in pPassword varchar(35))
begin 
	select Tip from nalog where KorisnickoIme=pUserName and Lozinka=pPassword ;
	
end $$
delimiter ;