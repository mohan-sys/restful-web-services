insert into user_details(id,birth_date,name)
values(10001,current_date(),'Mohan');

insert into user_details(id,birth_date,name)
values(10002,current_date(),'Jisha');

insert into user_details(id,birth_date,name)
values(10003,current_date(),'Karthik');

insert into post(id,description, user_id)
values (20001,'I want to learn AWS',10001);

insert into post(id,description, user_id)
values (20002,'I want to learn Azure',10001);

insert into post(id,description, user_id)
values (20003,'I want to learn GCP',10002);

insert into post(id,description, user_id)
values (20004,'I want to learn DevOPs',10002);