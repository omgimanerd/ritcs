create table if not exists customers (
  id serial primary key,
  name varchar(255),
  phone bigint,
  gender varchar(16),
  income int,
  address_street varchar(255),
  address_state varchar(255),
  address_zipcode int
);

create table if not exists brands (
  name varchar(255) primary key,
  country varchar(255),
  reliability varchar(255)
);

create table if not exists dealers (
  id serial primary key,
  name varchar(255),
  phone bigint
);

create table if not exists brand_dealer (
  dealer int references dealers(id),
  brand varchar(255) references brands(name)
);

create table if not exists sales (
  id serial primary key,
  close_date date,
  customer int references customers(id),
  dealer int references dealers(id)
);

create table if not exists vehicles (
  vin int primary key,
  color varchar(255),
  brand varchar(255) references brands(name),
  model varchar(255),
  engine varchar(255),
  transmission varchar(255),
  mileage int,
  price int,
  sale int references sales(id),
  dealer int references dealers(id),
  owner int references customers(id)
);
