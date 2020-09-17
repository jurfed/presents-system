DROP TABLE IF EXISTS Storage;
DROP TABLE IF EXISTS Ordered;

create table Storage(
productType varchar(255) primary key,
availableValue int not null default 0,
minValue int not null default 1
);

create table Ordered(
orderNumber serial primary key,
productType varchar(255) references Storage(productType) ON DELETE CASCADE,
fio varchar(255) not null,
year int not null
);