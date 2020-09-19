DROP TABLE IF EXISTS Ordered;
DROP TABLE IF EXISTS Manufacturing;
DROP TABLE IF EXISTS Storage;



create table Storage(
product_type varchar(255) primary key,
available_value int not null default 0,
min_value int not null default 1,
constraint availableValue_constr check (available_value >= 0 and min_value >= 0)
);

create table Ordered(
order_number serial primary key,
product_type varchar(255) references Storage(product_type) ON DELETE CASCADE,
fio varchar(255) not null,
year int not null,
released boolean not null default false,
constraint fioYear_constr UNIQUE (fio, year),
constraint year_constr check (year > 2000)
);

create table Manufacturing(
request_number serial primary key,
product_type varchar(255) references Storage(product_type) ON DELETE CASCADE,
count int not null,
constraint count_constr check (count > 0)
);


insert into Storage(product_type, available_value) values ('bicycle',3);
insert into Storage(product_type) values ('roller skates');

insert into Ordered(product_type, fio, year) values ('bicycle', 'Ivan',2020);
insert into Ordered(product_type, fio, year) values ('roller skates', 'Masha',2020);
