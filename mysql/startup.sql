CREATE DATABASE arturospizzeria;

USE arturospizzeria;

CREATE TABLE item_beverages (
  id INT NOT NULL AUTO_INCREMENT,
  price DECIMAL(5,2) NOT NULL,
  name VARCHAR(50) NOT NULL,
  enabled BOOLEAN NOT NULL,
  PRIMARY KEY (id)
);