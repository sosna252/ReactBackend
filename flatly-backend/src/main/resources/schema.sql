DROP TABLE IF EXISTS flatly_backend_schema.Booking;
DROP TABLE IF EXISTS flatly_backend_schema.Item;
DROP TABLE IF EXISTS flatly_backend_schema.User;
DROP SCHEMA IF EXISTS flatly_backend_schema;

CREATE SCHEMA flatly_backend_schema AUTHORIZATION postgres;
CREATE TABLE flatly_backend_schema.User
(
    UserId int NOT NULL UNIQUE PRIMARY KEY,
    Login varchar(20) NOT NULL,
    Password varchar(20) NOT NULL,
    FirstName varchar(30) NOT NULL,
    LastName varchar(50) NOT NULL,
    SecurityToken int NOT NULL UNIQUE
);
CREATE TABLE flatly_backend_schema.Item
(
    Id int NOT NULL UNIQUE PRIMARY KEY,
    "user" int NOT NULL UNIQUE REFERENCES flatly_backend_schema.User(UserId),
    StartDateTime date NOT NULL,
    EndDateTime date NOT NULL,
    Title varchar(30) NOT NULL,
    Description varchar(500) NOT NULL,
    RoomNumber int NOT NULL,
    Price decimal NOT NULL,
    Rating decimal NOT NULL,
    City varchar(30) NOT NULL,
    Address varchar(50) NOT NULL,
    Country varchar(20) NOT NULL
);
CREATE TABLE flatly_backend_schema.Booking
(
    Id int NOT NULL UNIQUE PRIMARY KEY,
    item int NOT NULL UNIQUE REFERENCES flatly_backend_schema.Item(Id),
    StartDate date NOT NULL,
    EndDate date NOT NULL,
    Name varchar(30) NOT NULL,
    LastName varchar(50) NOT NULL,
    Email varchar(100) NOT NULL,
    People int NOT NULL
);