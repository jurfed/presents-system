DROP TABLE IF EXISTS Ordered;
DROP TABLE IF EXISTS Storage;
DROP TABLE IF EXISTS Books;


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
constraint fioYear_constr UNIQUE (fio, year),
constraint year_constr check (year > 2000)
);

create table Books(
boo_id int primary key,
book_name varchar(255)
);



insert into Storage(product_type, available_value) values ('bicycle',3);
insert into Storage(product_type) values ('roller skates');

insert into Ordered(product_type, fio, year) values ('bicycle', 'Ivan',2020);
insert into Ordered(product_type, fio, year) values ('roller skates', 'Masha',2020);
