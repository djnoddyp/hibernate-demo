CREATE TYPE Style AS ENUM ('MOUNTAIN', 'ROAD', 'HYBRID');

CREATE TABLE Bikeshop (
  id SERIAL PRIMARY KEY,
  address VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE Bike (
  id SERIAL PRIMARY KEY,
  bikeshop_id INTEGER NOT NULL REFERENCES Bikeshop (id),
  make VARCHAR(255) NOT NULL,
  model VARCHAR(255) NOT NULL,
  gears INTEGER NOT NULL CONSTRAINT positive_gears CHECK (gears > 0),
  style Style,
  price NUMERIC NOT NULL CONSTRAINT positive_price CHECK (price > 0),
  priceInDollars NUMERIC
);

CREATE TABLE Employee (
  id SERIAL PRIMARY KEY,
  bikeshop_id INTEGER NOT NULL REFERENCES Bikeshop (id),
  name VARCHAR(255) NOT NULL,
  employee_number INTEGER NOT NULL UNIQUE
);