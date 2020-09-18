DROP TABLE IF EXISTS Ordered;
DROP TABLE IF EXISTS Storage;
DROP TABLE IF EXISTS Books;


create table Storage(
productType varchar(255) primary key,
availableValue int not null default 0,
minValue int not null default 1,
constraint availableValue_constr check (availableValue >= 0 and minValue >= 0)
);

create table Ordered(
orderNumber serial primary key,
productType varchar(255) references Storage(productType) ON DELETE CASCADE,
fio varchar(255) not null,
year int not null,
constraint fioYear_constr UNIQUE (fio, year),
constraint year_constr check (year > 2000)
);

create table Books(
id int primary key,
name varchar(255)
);



insert into Storage(productType, availableValue, minValue) values ('bicycle',3,2);
insert into Storage(productType, availableValue, minValue) values ('roller skates', 3,2);

insert into Ordered(productType, fio, year) values ('bicycle', 'Ivan',2020);
insert into Ordered(productType, fio, year) values ('roller skates', 'Masha',2020);


insert into books(id, name) values (1, 'Java');
insert into books(id, name) values (2, 'c++');
insert into books(id, name) values (3, 'c#');
insert into books(id, name) values (4, 'javaScript');